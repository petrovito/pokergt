package game;

import java.util.ArrayList;
import java.util.Arrays;

import bids.Bid;
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
	
	//implicitly: everyone has bids after first deal
	//should call perhaps next_situations with blank  gameplay
	public ArrayList<GamePlay> first_situations(int player) {
		ArrayList<GamePlay> situations = new ArrayList<GamePlay>();
		for (Dealing dealing: dealer_.first_dealings(player)) {
			for (Sequence seq: biddings_.get(0).first_situations(player)) {
				situations.add(new GamePlay(this,new DealingPlay(dealing,seq)));
			}
		}
		return situations;
	}
	
	
	public ArrayList<GamePlay> next_situations(GamePlay game_play, int player) {
		ArrayList<GamePlay> situations = new ArrayList<GamePlay>();
		if (game_play.is_game_end()) return situations;
		if (game_play.is_bidding_end()) {
			for (Dealing dealing: dealer_.next_dealings(game_play.last().dealing_, player)) {
				for (Sequence sequence: biddings_.get(game_play.size()).first_situations(player)) {
					DealingPlay dealing_play = new DealingPlay(dealing, sequence);
					situations.add(game_play.copy(dealing_play));
				}
			}
			return situations;
		}
		if (game_play.next_player() == player) {
			situations.add(game_play);
			return situations;
		}
		BidSystem actual_bid_system = biddings_.get(game_play.size()-1);
		Sequence actual_sequence = game_play.last().sequence_;
		for (Bid bid: actual_bid_system.possible_moves(actual_sequence)) {
			situations.addAll(next_situations(game_play.copy(bid), player));
		}
		return situations;
	}
	
	
	
	
	
	

}
