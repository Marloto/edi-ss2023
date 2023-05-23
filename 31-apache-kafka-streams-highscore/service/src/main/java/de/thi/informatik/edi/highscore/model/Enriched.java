package de.thi.informatik.edi.highscore.model;

public class Enriched {
	private Long playerId;
	private Long productId;
	private String playerName;
	private String gameName;
	private Double score;
	
	public Enriched() {
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
	public String toString() {
		return "{" + " playerId='" + getPlayerId() + "'" + ", playerName='" + getPlayerName() + "'" + ", gameName='"
				+ getGameName() + "'" + ", score='" + getScore() + "'" + "}";
	}
}
