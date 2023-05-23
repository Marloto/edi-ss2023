package de.thi.informatik.edi.highscore;

import java.time.Instant;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

import de.thi.informatik.edi.highscore.model.Vital;

public class VitalTimestampExtractor implements TimestampExtractor {
	public long extract(ConsumerRecord<Object, Object> record, long partitionTime) {
		Vital measurement = (Vital) record.value();
		if (measurement != null && measurement.getTimestamp() != null) {
			String timestamp = measurement.getTimestamp();
			return Instant.parse(timestamp).toEpochMilli();
		}
		return partitionTime;
	}
}