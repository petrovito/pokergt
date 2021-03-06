package game;

import java.util.ArrayList;
import java.util.Arrays;

import org.jscience.mathematics.number.Rational;

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
				situations.add(new GamePlay(this,dealing,seq));
			}
		}
		return situations;
	}
	
	
	public ArrayList<GamePlay> next_situations(GamePlay game_play, int player) {
		ArrayList<GamePlay> situations = new ArrayList<GamePlay>();
		if (game_play.is_game_end()) return situations;
		if (game_play.is_bidding_end()) {
			for (Dealing dealing: dealer_.next_dealings(game_play.dealing_, player)) {
				for (Sequence sequence: biddings_.get(game_play.size()).
						first_situations(player,game_play.last().equities_)) {
					situations.add(game_play.copy(dealing, sequence));
				}
			}
			return situations;
		}
		if (game_play.next_player() == player) {
			situations.add(game_play);
			return situations;
		}
		BidSystem actual_bid_system = biddings_.get(game_play.size()-1);
		Sequence actual_sequence = game_play.last();
		for (Bid bid: actual_bid_system.possible_moves(actual_sequence)) {
			situations.addAll(next_situations(game_play.copy(bid), player));
		}
		return situations;
	}
	

	
	public ArrayList<GamePlay> all_situations(int player) {
		ArrayList<GamePlay> situations = new ArrayList<GamePlay>();
		for (GamePlay game_play: first_situations(player)) {
			situations.addAll(recursive_situations(game_play,player));
		}
		return situations;
	}

	private ArrayList<GamePlay> recursive_situations(GamePlay game_play, int player) {
		ArrayList<GamePlay> situations = new ArrayList<GamePlay>();
		situations.add(game_play);
		for (Bid bid: actual_bid_system(game_play).possible_moves(game_play.last())) {;
			GamePlay new_game_play = game_play.copy(bid);
			for (GamePlay new_situation: next_situations(new_game_play, player)) {
				situations.addAll(recursive_situations(new_situation, player));
			}
		}
		return situations;
	}

	public BidSystem actual_bid_system(GamePlay game_play) {
		return biddings_.get(game_play.size()-1);
	}

	public Strategy first_strategy(int player) {
		ArrayList<GamePlay> sitauations = all_situations(player);
		Strategy strategy = new Strategy(this,player);
		for (GamePlay game_play: sitauations) {
			ArrayList<Rational> list = new ArrayList<Rational>();
			for (int i = 0; i < game_play.possible_bids().size()-1; i++) {
				list.add(Rational.ZERO);
			}
			list.add(Rational.ONE);
			strategy.put(game_play, list);
		}
		return strategy;
	}
	

}
