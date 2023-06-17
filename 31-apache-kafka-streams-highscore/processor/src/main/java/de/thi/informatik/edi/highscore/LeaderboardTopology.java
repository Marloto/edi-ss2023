package de.thi.informatik.edi.highscore;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;

import de.thi.informatik.edi.highscore.model.Enriched;
import de.thi.informatik.edi.highscore.model.Leaderboard;
import de.thi.informatik.edi.highscore.model.Player;
import de.thi.informatik.edi.highscore.model.Product;
import de.thi.informatik.edi.highscore.model.ScoreEvent;
import de.thi.informatik.edi.highscore.model.ScoreWithPlayer;

public class LeaderboardTopology {
    public static Topology build() {
        StreamsBuilder builder = new StreamsBuilder();

        // score-events, kein key, verteilt über partitionen
        KStream<String, ScoreEvent> stream = builder.stream(Configurator.SCORE_EVENTS,
                Consumed.with(Serdes.String(), JsonSerdes.scoreEvent())) // <- hat keinen key
                .selectKey((key, value) -> value.getPlayerId().toString()); // <- rekeying

        KTable<String, Player> table = builder.table(Configurator.PLAYERS,
                Consumed.with(Serdes.String(), JsonSerdes.player())); // <- verwendet playerid als key

        GlobalKTable<String, Product> products = builder.globalTable(Configurator.PRODUCTS,
                Consumed.with(Serdes.String(), JsonSerdes.product())); // <- verwendet productid als key

        KStream<String, ScoreWithPlayer> scoreWithPlayers = stream.join(table, ScoreWithPlayer::new,
                Joined.with(Serdes.String(), JsonSerdes.scoreEvent(), JsonSerdes.player())); // <- key ist playerId

        // join.foreach((key, value) -> System.out.println(key + ": " + value)); <- ggf.
        // toString ergänzen

        // (el1, el2) -> new Enriched(el1, el2) vgl. zu Enriched::new
        KStream<String, Enriched> withProducts = scoreWithPlayers.join(
                products,
                (leftKey, scoreWithPlayer) -> String.valueOf(scoreWithPlayer.getScoreEvent().getProductId()),
                Enriched::new);

        // -> ereignisse je scoreevent, die mit player und product kombiniert sind, so
        // dass namen verfügbar sind

        withProducts
                .groupBy((key, value) -> value.getProductId().toString(),
                        Grouped.with(Serdes.String(), JsonSerdes.enriched())) // <- was macht groupBy? gruppiert nach einem Element
                .aggregate(Leaderboard::new, (key, value, state) -> state.update(value),
                        Materialized.with(Serdes.String(), JsonSerdes.leaderboard()))
                .toStream().to(Configurator.LEADER_BOARDS);

        return builder.build();
    }
}
