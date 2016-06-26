package game;

import java.util.ArrayList;
import java.util.HashMap;

import org.jscience.mathematics.number.Rational;

import bids.Bid;

public class Strategy extends HashMap<GamePlay, ArrayList<Rational>> {

	private static final long serialVersionUID = 203595977536803285L;

	private Game game_;
	public int player_;
	
	public Strategy(Game game, int player) { game_ = game; player_ = player; }
	
	public Strategy(Strategy strategy) {
		super(strategy);
		game_ = strategy.game_;
		player_ = strategy.player_;
	}
	
	public Strategy(PureStrategy strategy) {
		game_ = strategy.game_;
		player_ = strategy.player_;
		for (GamePlay game_play: strategy.keySet()) {
			ArrayList<Bid> responses = game_play.next_moves(player_);
			for (int i = 0; i < responses.size(); i++) {
				//...
			}
		}
	}
	
	
}
