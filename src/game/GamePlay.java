package game;

import java.util.ArrayList;
import java.util.Arrays;

import org.jscience.mathematics.number.Rational;

import bids.Bid;
import bids.Sequence;
import dealers.Dealing;

public class GamePlay extends ArrayList<Sequence> {

	private static final long serialVersionUID = -5216638401515725089L;
	
	private Game game_;
	public Dealing dealing_;
	
	public GamePlay(Game game) {
		game_ = game;
		dealing_ = new Dealing(game_.dealer_);
	}
	
	public GamePlay(Game game, Dealing dealing, Sequence... sequences) {
		super(Arrays.asList(sequences));
		dealing_ = dealing;
		game_ = game;
	}
	
	public GamePlay(GamePlay game_play) {
		for (Sequence sequence: game_play) {
			add(sequence.copy());
		}
		dealing_ = game_play.dealing_.copy();
		game_ = game_play.game_;
	}
	
	
	public boolean equals(Object o) {
		if (o instanceof GamePlay) {
			GamePlay game_play = (GamePlay) o;
			if (size() != game_play.size())
				return false;
			for (int i = 0; i < size(); i++) {
				if (!get(i).equals(game_play.get(i)))
					return false;
			}
			return dealing_.equals(game_play.dealing_);
		}
		return super.equals(o);
	}
	
	public Sequence last() {
		return get(size()-1);
	}
	
	public boolean is_game_end() {
		if (size() == 0) return false;
		if (last().is_game_end()) return true;
		return last().is_bid_end() && dealing_.is_end();
	}
	
	public boolean is_bidding_end() {
		if (dealing_.isEmpty()) return true;
		return last().is_bid_end();
	}

	public int next_player() {
		if (!is_bidding_end()) return last().next_player();
		return game_.biddings_.get(size()).first_player();
	}
	
	public ArrayList<Rational> winnings() {
		if (last().is_game_end())
			return last().winnings();
		Rational sum = Rational.ZERO;
		Rational max = Rational.ZERO;
		ArrayList<Integer> active_players = new ArrayList<Integer>();
		for (int i = 0; i < last().equities_.size(); i++) {
			Rational r = last().equities_.get(i);
			sum = sum.plus(r);
			if (r.isGreaterThan(max)) {
				active_players = new ArrayList<Integer>();
				active_players.add(i);
				max = r;
			} else if (r.equals(max)) {
				active_players.add(i);
			}
		}
		Dealing dealing = dealing_.restrict_players(active_players);		
		ArrayList<Integer> winners = game_.dealer_.winners(dealing);
		int num_winners = winners.size();
		sum = sum.times(Rational.valueOf(1, num_winners));
		ArrayList<Rational> winnings = new ArrayList<Rational>();
		for (int i = 0; i < last().equities_.size(); i++) {
			Rational r = last().equities_.get(i);
			if (winners.contains(i))
				winnings.add(sum.minus(r));
			else 
				winnings.add(r.opposite());
		}
		return winnings;
	}

	public GamePlay copy(Dealing dealing, Sequence sequence) {
		GamePlay game_play = new GamePlay(this);
		game_play.add(sequence);
		dealing_ = dealing.copy();
		return game_play;
	}
	
	public GamePlay copy(Dealing dealing) {
		GamePlay game_play = new GamePlay(this);
		game_play.dealing_ = dealing.copy();
		return game_play;
	}

	public GamePlay copy(Bid bid) {
		GamePlay game_play = new GamePlay(this);
		game_play.last().add(bid);
		game_play.dealing_ = dealing_.copy();
		return game_play;
	}
	
	
	public ArrayList<Bid> possible_bids() {
		if (size()==0) {
			add(new Sequence(game_.biddings_.get(0)));
		}
		return last().possible_bids();
	}
	
	
	@Override
	public String toString() {
		String str = "("+dealing_.toString()+"::";
		for (Sequence seq: this) {
			str += seq.toString();
		}
		return str+")";
	}
	
	public GamePlay merge(Dealing dealing) {
		GamePlay game_play = new GamePlay(this);
		game_play.dealing_ = game_play.dealing_.merge(dealing);
		return game_play;
	}

	public ArrayList<Rational> winnings(Dealing dealing) {
		return merge(dealing).winnings();
	}
	
	@Override
	public int hashCode() {
		if (dealing_.size()==0) return 0;
		return dealing_.get(0).hashCode();
	}
	

}
