package de.thi.informatik.edi.highscore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.thi.informatik.edi.highscore.model.HighScoreEntry;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("api/v1/leaderboard")
public class HighScoreController {
	private HighScoreService highScore;

	public HighScoreController(@Autowired HighScoreService highScore) {
		this.highScore = highScore;
	}

	@GetMapping("/{id}")
	public Flux<HighScoreEntry> get(@PathVariable Long id) {
		return this.highScore.get(id).map(HighScoreEntry::new);
	}
}
