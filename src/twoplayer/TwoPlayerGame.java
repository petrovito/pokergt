package twoplayer;

import java.util.ArrayList;
import java.util.Arrays;

import bids.BidSystem;
import dealers.Dealer;
import game.Game;
import game.PureStrategy;
import game.Strategy;

public class TwoPlayerGame extends Game {

	public TwoPlayerGame(Dealer dealer, ArrayList<BidSystem> biddings) {
		super(dealer, biddings);
	}
	
	public TwoPlayerGame(Dealer dealer, BidSystem... biddings) {
		this(dealer, new ArrayList<BidSystem>(Arrays.asList(biddings)));
	}
	
	
	public PureStrategy best_response(Strategy strategy) {
		return null;
	}
	
	

}
