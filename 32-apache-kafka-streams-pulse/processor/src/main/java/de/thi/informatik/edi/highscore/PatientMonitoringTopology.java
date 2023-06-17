package de.thi.informatik.edi.highscore;

import java.time.Duration;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.StreamJoined;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.kstream.Suppressed.BufferConfig;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.thi.informatik.edi.highscore.model.BodyTemp;
import de.thi.informatik.edi.highscore.model.CombinedVitals;
import de.thi.informatik.edi.highscore.model.Pulse;

public class PatientMonitoringTopology {
	private static final Logger logger = LoggerFactory.getLogger(PatientMonitoringTopology.class);
	public static Topology build() {
		StreamsBuilder builder = new StreamsBuilder();
		
		KStream<String, Pulse> pulseEvents =
		        builder.stream(Configurator.PULSE_EVENTS, Consumed.with(Serdes.String(), JsonSerdes.pulse())
			            .withTimestampExtractor(new VitalTimestampExtractor()));
		
		KStream<String, BodyTemp> tempEvents =
		        builder.stream(Configurator.BODY_TEMP_EVENTS, Consumed.with(Serdes.String(), JsonSerdes.bodyTemp())
			            .withTimestampExtractor(new VitalTimestampExtractor()));
		
		// Kleine Fenster -> bei zu hoher Latenz könnten ungünstig viele Ereignisse raus fallen
		// Große Fenster  -> es muss lange gewartet werden, bis das Ergebnis errechnet werden kann
		TimeWindows window = TimeWindows.ofSizeAndGrace(Duration.ofSeconds(60), Duration.ofSeconds(5));
		KTable<Windowed<String>, Long> pulseCounts =
		        pulseEvents
		            .groupByKey()
		            .windowedBy(window)
		            .count(Materialized.as(Configurator.PULSE_COUNTS))
		            .suppress(Suppressed.untilWindowCloses(BufferConfig.unbounded().shutDownWhenFull()));
		
		KStream<String, Long> highPulse =
		        pulseCounts
		            .toStream()
		            .peek(
		                (key, value) -> {
							String id = new String(key.key());
							Long start = key.window().start();
							Long end = key.window().end();
							logger.info("Patient {} had a heart rate of {} between {} and {}", id, value, start, end);
		                })
		            .filter((key, value) -> value >= 100)
		            .map((windowedKey, value) -> KeyValue.pair(windowedKey.key(), value));
		
		KStream<String, BodyTemp> highTemp =
		        tempEvents.filter(
		            (key, value) -> value != null && value.getTemperature() != null && value.getTemperature() > 38);
		
		JoinWindows joinWindows = JoinWindows.ofTimeDifferenceAndGrace(Duration.ofSeconds(60), Duration.ofSeconds(10));        
        KStream<String, CombinedVitals> vitalsJoined =
                highPulse.join(
                		highTemp, 
                		(pulseRate, bodyTemp) -> new CombinedVitals(pulseRate.intValue(), bodyTemp), 
                		joinWindows, 
                		StreamJoined.with(Serdes.String(), Serdes.Long(), JsonSerdes.bodyTemp()));
        
        vitalsJoined.to(Configurator.ALERTS, Produced.with(Serdes.String(), JsonSerdes.combinedVitals()));
        
        pulseCounts
	        .toStream()
	        .print(Printed.<Windowed<String>, Long>toSysOut().withLabel("pulse-counts"));
	    highPulse.print(Printed.<String, Long>toSysOut().withLabel("high-pulse"));
	    highTemp.print(Printed.<String, BodyTemp>toSysOut().withLabel("high-temp"));
	    vitalsJoined.print(Printed.<String, CombinedVitals>toSysOut().withLabel("vitals-joined"));
		
		return builder.build();
	}
}
