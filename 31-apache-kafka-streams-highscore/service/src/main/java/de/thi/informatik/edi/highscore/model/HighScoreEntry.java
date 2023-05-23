package de.thi.informatik.edi.highscore.model;

public class HighScoreEntry {
	private Long playerId;
	private String playerName;
	private Double score;

	public HighScoreEntry(Enriched enriched) {
		playerId = enriched.getPlayerId();
		playerName = enriched.getPlayerName();
		score = enriched.getScore();
	}

	public HighScoreEntry() {
	}

	public Long getPlayerId() {
		return playerId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public Double getScore() {
		return score;
	}

}
