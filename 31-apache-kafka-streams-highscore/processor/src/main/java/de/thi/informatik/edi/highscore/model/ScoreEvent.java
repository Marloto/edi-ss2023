package de.thi.informatik.edi.highscore.model;

public class ScoreEvent {
	private Long playerId;
	private Long productId;
	private Double score;
	
	public ScoreEvent() {
	}
	
	public ScoreEvent(Long playerId, Long productId, Double score) {
		this.playerId = playerId;
		this.productId = productId;
		this.score = score;
	}

	public Long getPlayerId() {
		return this.playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	public Long getProductId() {
		return this.productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Double getScore() {
		return this.score;
	}

	public void setScore(Double score) {
		this.score = score;
	}
}