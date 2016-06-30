package solver.twoplayer;

import java.util.ArrayList;
import java.util.HashMap;

import org.jscience.mathematics.number.Rational;

import bids.Bid;
import bids.Sequence;
import dealers.Dealing;
import game.GamePlay;
import game.PureStrategy;
import game.twoplayer.TwoPlayerGame;

public class BRSolver {
	
	
	TwoPlayerGame game_;
	
	
	public BRSolver(TwoPlayerGame game) {
		game_ = game;
	}
	
	
	public Constraint constraintOf(PureStrategy strategy) {
		Constraint constraint = new Constraint();
		GamePlay first_game_play = new GamePlay(game_);
		HashMap<Dealing, Boolean> in_game = new HashMap<Dealing, Boolean>();
		in_game.put(first_game_play.dealing_.restrict_player(1-strategy.player_), true);
		recursive_constraint(strategy, constraint,0,-1,first_game_play,in_game);
		constraint.add(Rational.ONE.opposite());
		return constraint;
	}
	
	private int recursive_constraint(PureStrategy strategy, Constraint constraint,
			int actual_index, int parent_index, GamePlay game_play, HashMap<Dealing, Boolean> in_game) {
		if (game_play.is_game_end()) {
			Rational coeff = Rational.ZERO;
			for (Dealing dealing: game_play.dealing_.possible_opponent_dealings()) {
					if (!in_game.get(dealing)) {
						continue;
					}
					coeff = coeff.plus(game_play.winnings(dealing).get(1-strategy.player_).
							times(game_play.possibility(dealing)));
			}
			constraint.add_to(parent_index, coeff);
			return 0;			
		}
		if (game_play.is_bidding_end()) {
			int delta_index = 0;
			HashMap<Dealing, Boolean> new_in_game = new HashMap<Dealing,Boolean>();
			for (Dealing old_dealing: in_game.keySet()) {
				for (Dealing new_dealing: old_dealing.next_dealings(strategy.player_)) {
					new_in_game.put(new_dealing, in_game.get(old_dealing));					
				}
			}
			for (Dealing dealing: game_play.dealing_.next_dealings(1-strategy.player_)) {
				GamePlay new_game_play = game_play.copy(dealing);
				new_game_play.add(new Sequence(game_play.game_.biddings_.get(0)));
				delta_index += recursive_constraint(strategy, constraint,
						actual_index+delta_index,parent_index,new_game_play, new_in_game);
			}
			return delta_index;
		}
		if (game_play.next_player()==strategy.player_) {
			int delta_index = 0;
			ArrayList<Bid> possible_bids = game_play.possible_bids();
			for (int i = 0; i < possible_bids.size(); i++) {
				HashMap<Dealing, Boolean> new_in_game = new HashMap<Dealing, Boolean>();
				for (Dealing dealing: in_game.keySet()) {
					new_in_game.put(dealing, in_game.get(dealing) &&
							possible_bids.get(i).equals(strategy.get(game_play.opponent_dealing(dealing))));
				}
				GamePlay new_game_play = game_play.copy(possible_bids.get(i));
				delta_index += recursive_constraint(strategy, constraint, actual_index, parent_index,
						new_game_play, new_in_game);
			}
			return delta_index;			
		}
		ArrayList<Bid> bids = game_play.possible_bids();
		int new_actual_index = actual_index+bids.size();
		for (int i = 0; i < bids.size(); i++) {
			GamePlay new_game_play = game_play.copy(bids.get(i));
			new_actual_index += recursive_constraint(strategy, constraint, new_actual_index,
					actual_index+i, new_game_play, in_game);
		}
		return new_actual_index-actual_index;
	}
	
	
	public ArrayList<Constraint> strategy_constraints(int player) {
		
	}
	

}
