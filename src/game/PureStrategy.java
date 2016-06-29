package game;

import java.util.HashMap;

import org.jscience.mathematics.number.Rational;

import bids.Bid;

public class PureStrategy extends HashMap<GamePlay, Bid> {
	
	private static final long serialVersionUID = -6159855155790750297L;
	
	protected Game game_;
	public int player_;
	public Rational value;
	
	public PureStrategy(Game game, int player) { game_ = game; player_ = player; }
	
	public PureStrategy(PureStrategy strategy) {
		super(strategy);
		game_ = strategy.game_;
		player_ = strategy.player_;
	}
	

}
