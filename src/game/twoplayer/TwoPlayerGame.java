package game.twoplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.jscience.mathematics.number.Rational;

import bids.Bid;
import bids.BidSystem;
import dealers.Dealer;
import dealers.Dealing;
import game.Game;
import game.GamePlay;
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
		PureStrategy response = new PureStrategy(strategy.game_, 1-strategy.player_);
		HashMap<Dealing,Rational> possibilities = new HashMap<Dealing,Rational>();
		possibilities.put(new Dealing(strategy.game_.dealer_), Rational.ONE);
		Rational EV = recursive_best_response(response, strategy, new GamePlay(strategy.game_), possibilities);
		response.value = EV;
		return response;
	}

	private Rational recursive_best_response(PureStrategy response, 
			Strategy strategy, GamePlay game_play, HashMap<Dealing, Rational> possibilities) {
		if (game_play.is_game_end()) {
			Rational EV = Rational.ZERO;
			for (Dealing dealing: game_play.dealing_.possible_opponent_dealings()) {
				Rational p = possibilities.get(dealing);
				if (!p.equals(Rational.ZERO)) {
					EV = EV.plus(game_play.winnings(dealing).get(response.player_).times(dealing.possibility()).times(p));
				}
			}
			return EV;
		}
		if (game_play.is_bidding_end()) {
			HashMap<Dealing, Rational> new_p = new HashMap<Dealing, Rational>();
			for (Dealing old_dealing: possibilities.keySet()) {
				for (Dealing new_dealing: old_dealing.next_dealings(1-response.player_)) {
					new_p.put(new_dealing, possibilities.get(old_dealing));					
				}
			}
			Rational EV = Rational.ZERO;
			for (Dealing dealing: game_play.dealing_.next_dealings(response.player_)) {
				GamePlay new_game_play = game_play.copy(dealing);
				for (Bid bid: new_game_play.possible_bids()) 
					EV = EV.plus(recursive_best_response(response, strategy, new_game_play.copy(bid), new_p));
			}
			return EV;
		}
		if (game_play.next_player() != response.player_) {
			Rational EV = Rational.ZERO;
			ArrayList<Bid> possible_bids = game_play.possible_bids();
			ArrayList<Rational> distribution = strategy.get(game_play);
			for (int i = 0; i < possible_bids.size(); i++) {
				GamePlay new_game_play = game_play.copy(possible_bids.get(i));
				HashMap<Dealing, Rational> new_p = new HashMap<Dealing, Rational>();
				for (Dealing dealing: possibilities.keySet()) {
					new_p.put(dealing, possibilities.get(dealing).times(distribution.get(i)));
				}
				recursive_best_response(response, strategy, new_game_play, new_p);
			}
			return EV;
		}
		//next_player is strategy.player!
		Rational max_EV = Rational.valueOf(-1000, 1);
		int argmax = -1;
		ArrayList<Bid> possible_bids = game_play.possible_bids();
		for (int i = 0; i < possible_bids.size(); i++) {
			GamePlay new_game_play = game_play.copy(possible_bids.get(i));
			Rational EV = recursive_best_response(response, strategy, new_game_play, possibilities);
			if (EV.isGreaterThan(max_EV)) {
				max_EV = EV;
				argmax = i;
			}
		}
		response.put(game_play, possible_bids.get(argmax));
		return max_EV;
	}
	
	

}
