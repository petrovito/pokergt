package game;

import java.util.ArrayList;
import java.util.HashMap;

import org.jscience.mathematics.number.Rational;

import bids.Bid;

public class Strategy extends HashMap<GamePlay, ArrayList<Rational>> {

	private static final long serialVersionUID = 203595977536803285L;

	public Game game_;
	public int player_;
	public Rational value;
	
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
			Bid response = strategy.get(game_play);
			ArrayList<Rational> answer = new ArrayList<Rational>();
			for (Bid bid: game_play.possible_bids()) {
				if (bid.equals(response)) answer.add(Rational.ONE);
				else answer.add(Rational.ZERO);
			}
			put(game_play,answer);
		}
	}
	
	
}
