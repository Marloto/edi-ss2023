package de.thi.informatik.edi.highscore.model;

public class ScoreWithPlayer {
	private Player player;
	private ScoreEvent scoreEvent;

	public ScoreWithPlayer(ScoreEvent scoreEvent, Player player) {
		super();
		this.player = player;
		this.scoreEvent = scoreEvent;
	}

	public Player getPlayer() {
		return player;
	}

	public ScoreEvent getScoreEvent() {
		return scoreEvent;
	}
}
