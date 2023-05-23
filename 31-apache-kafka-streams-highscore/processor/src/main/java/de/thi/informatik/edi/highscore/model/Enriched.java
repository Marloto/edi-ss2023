package de.thi.informatik.edi.highscore.model;

import java.util.Objects;

public class Enriched implements Comparable<Enriched> {
	private Long playerId;
	private Long productId;
	private String playerName;
	private String gameName;
	private Double score;
	
	public Enriched() {
	}

	public Enriched(ScoreWithPlayer scoreEventWithPlayer, Product product) {
		this.playerId = scoreEventWithPlayer.getPlayer().getId();
		this.productId = product.getId();
		this.playerName = scoreEventWithPlayer.getPlayer().getName();
		this.gameName = product.getName();
		this.score = scoreEventWithPlayer.getScoreEvent().getScore();
	}

	@Override
	public int compareTo(Enriched o) {
		return Double.compare(o.score, score);
	}

	public Long getPlayerId() {
		return this.playerId;
	}

	public Long getProductId() {
		return this.productId;
	}

	public String getPlayerName() {
		return this.playerName;
	}

	public String getGameName() {
		return this.gameName;
	}

	public Double getScore() {
		return this.score;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(playerId, productId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Enriched other = (Enriched) obj;
		return Objects.equals(playerId, other.playerId) && Objects.equals(productId, other.productId);
	}

	@Override
	public String toString() {
		return "{" + " playerId='" + getPlayerId() + "'" + ", playerName='" + getPlayerName() + "'" + ", gameName='"
				+ getGameName() + "'" + ", score='" + getScore() + "'" + "}";
	}
}
