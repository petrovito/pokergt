package game;

import java.util.ArrayList;
import java.util.Arrays;
import bids.BidSystem;
import bids.Sequence;
import dealers.Dealer;
import dealers.Dealing;

public class Game {
	
	public Dealer dealer_;
	public ArrayList<BidSystem> biddings_;
	
	public Game(Dealer dealer, ArrayList<BidSystem> biddings) {
		dealer_ = dealer;
		biddings_ = biddings;
	}
	
	public Game(Dealer dealer, BidSystem... biddings) {
		dealer_ = dealer;
		biddings_ = new ArrayList<BidSystem>(Arrays.asList(biddings));
	}
	
	
	public ArrayList<GamePlay> first_situations(int player) {
		ArrayList<GamePlay> situations = new ArrayList<GamePlay>();
		for (Dealing deal: dealer_.first_dealings(player)) {
			for (Sequence seq: biddings_.get(0).first_situations(player)) {
				situations.add(new GamePlay(this,new DealingPlay(deal,seq)));
			}
		}
		return situations;
	}
	
	
	
	
	
	
	

}
