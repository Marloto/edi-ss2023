package de.thi.informatik.edi.highscore;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;

public class LeaderboardTopology {
	public static Topology build() {
		StreamsBuilder builder = new StreamsBuilder();
		
		return builder.build();
	}
}
