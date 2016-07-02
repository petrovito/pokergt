package solver.twoplayer;

import java.util.ArrayList;
import java.util.HashMap;

import org.jscience.mathematics.number.Rational;

import algebra.Constraint;
import algebra.LP;
import bids.Bid;
import bids.Sequence;
import dealers.Dealing;
import game.GamePlay;
import game.PureStrategy;
import game.Strategy;
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
		ArrayList<Constraint> constraints = new ArrayList<Constraint>();
		int length = recursive_strategy_constrains(constraints, player, 0,-1, new GamePlay(game_));
		for (Constraint constraint: constraints) {
			constraint.set_size(length+1);
		}
		return constraints;
	}


	private int recursive_strategy_constrains(ArrayList<Constraint> constraints, int player, 
			int actual_index, int parent_index, GamePlay game_play) {
		if (game_play.is_game_end()) return 0;
		int delta_index = 0;
		if (game_play.is_bidding_end()) {
			for (Dealing dealing: game_play.dealing_.next_dealings(player)) {
				GamePlay new_game_play = game_play.copy(dealing);
				new_game_play.add(new Sequence(game_play.game_.biddings_.get(0)));
				delta_index += recursive_strategy_constrains(constraints, player,
						actual_index+delta_index,parent_index,new_game_play);
			}
			return delta_index;			
		}
		if (game_play.next_player() != player) {
			ArrayList<Bid> possible_bids = game_play.possible_bids();
			for (int i = 0; i < possible_bids.size(); i++) {
				GamePlay new_game_play = game_play.copy(possible_bids.get(i));
				delta_index += recursive_strategy_constrains(constraints, player, actual_index, parent_index,
						new_game_play);
			}
			return delta_index;
		}
		ArrayList<Bid> bids = game_play.possible_bids();
		Constraint constraint = new Constraint();
		if (parent_index==-1) constraint.add_to(-1, Rational.ONE);
		else constraint.add_to(parent_index, Rational.ONE.opposite());
		int new_actual_index = actual_index+bids.size();
		for (int i = 0; i < bids.size(); i++) {
			constraint.add_to(actual_index+i, Rational.ONE);
			GamePlay new_game_play = game_play.copy(bids.get(i));
			new_actual_index += recursive_strategy_constrains(constraints, player, new_actual_index,
					actual_index+i, new_game_play);
		}
		constraints.add(constraint);
		return new_actual_index-actual_index;		
	}
	
	
	
	public Strategy vector_to_strategy(ArrayList<Rational> vector, int player) {
		Strategy strategy = new Strategy(game_,player);
		recursive_vector_to_strategy(vector,strategy,0,0, new GamePlay(game_));
		return strategy;
	}
	
	
	private int recursive_vector_to_strategy(ArrayList<Rational> vector, Strategy strategy,
			int actual_index, int parent_index, GamePlay game_play) {
		if (game_play.is_game_end()) return 0;
		int delta_index = 0;
		if (game_play.is_bidding_end()) {
			for (Dealing dealing: game_play.dealing_.next_dealings(strategy.player_)) {
				GamePlay new_game_play = game_play.copy(dealing);
				new_game_play.add(new Sequence(game_play.game_.biddings_.get(0)));
				delta_index += recursive_vector_to_strategy(vector, strategy,
						actual_index+delta_index,parent_index,new_game_play);
			}
			return delta_index;			
		}
		if (game_play.next_player() != strategy.player_) {
			ArrayList<Bid> possible_bids = game_play.possible_bids();
			for (int i = 0; i < possible_bids.size(); i++) {
				GamePlay new_game_play = game_play.copy(possible_bids.get(i));
				delta_index += recursive_vector_to_strategy(vector, strategy, actual_index, parent_index,
						new_game_play);
			}
			return delta_index;
		}
		ArrayList<Bid> bids = game_play.possible_bids();
		int new_actual_index = actual_index+bids.size();
		Rational sum = Rational.ZERO;
		for (int i = 0; i < bids.size(); i++) {
			sum = sum.plus(vector.get(actual_index+i));
			GamePlay new_game_play = game_play.copy(bids.get(i));
			new_actual_index += recursive_vector_to_strategy(vector, strategy, new_actual_index,
					actual_index+i, new_game_play);
		}
		ArrayList<Rational> distribution = new ArrayList<Rational>();
		for (int i = 0; i < bids.size(); i++) {
			distribution.add(vector.get(actual_index+i).divide(sum));
		}
		strategy.put(game_play, distribution);
		return new_actual_index-actual_index;		
	}


	public Strategy equilibrium(int player) {		
		ArrayList<Constraint> strategy_constraints = strategy_constraints(player);
		LP lp = new LP();
		lp.add_equalities(strategy_constraints);
		Constraint con = constraintOf(game_.best_response(game_.first_strategy(player)));
		lp.add_greater_than(con);
		lp.set_unbounded(strategy_constraints.get(0).size()-1);
		ArrayList<Rational> obj = new ArrayList<Rational>();
		for (int i = 0; i < con.size()-1; i++)
			obj.add(Rational.ZERO);
		obj.add(Rational.ONE);
		lp.set_objective(obj);
		lp.set_unbounded(obj.size()-1);
		Strategy strategy;
		PureStrategy br;
		do {
			lp.solve();
			ArrayList<Rational> lp_var = lp.variables();
			strategy = vector_to_strategy(lp_var, player);
			strategy.value = lp_var.get(lp_var.size()-1);
			br = game_.best_response(strategy);
			lp.add_greater_than(constraintOf(br));
			//System.out.println(strategy);
			//System.out.println(br);
			//System.out.println(value+", "+br.value.opposite());			
		} while (!strategy.value.equals(br.value.opposite()));		
		return strategy;
	}
	
	
	

}
