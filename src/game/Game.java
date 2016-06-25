package game;

import java.util.ArrayList;
import java.util.Arrays;

import bids.BidSystem;
import dealers.Dealer;

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
	
	
	
	
	
	

}
