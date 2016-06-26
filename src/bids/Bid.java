package bids;

import java.util.ArrayList;

import org.jscience.mathematics.number.Rational;

//TODO Bid<T extends BidSystem>
public class Bid {
	
	public Action act_;
	public Rational amount_;
	public int player_;
	
	public  Bid(Action a, Rational am, int player_) {act_=a; amount_=am; this.player_=player_;}
	
	public boolean is_fold() { return act_==Action.FOLD; }
	public boolean is_call() { return act_==Action.CALL; }
	public boolean is_check() { return act_==Action.CHECK; }

	public static Bid FOLD(int num_player) {
		return new Bid(Action.FOLD, Rational.ZERO, num_player);
	}	
	public static Bid RAISE(Rational amount, int num_player) {
		return new Bid(Action.RAISE, amount, num_player);
	}
	public static ArrayList<Bid> BLINDS(Rational... amount_s) {
		ArrayList<Bid> bids= new ArrayList<Bid>();
		for (int i = 0; i < amount_s.length; i++) {
			bids.add(new Bid(Action.BLIND,amount_s[i],i));
		}
		return bids;
	}
	public static final Bid CHECK(int num_player) {
		return new Bid(Action.CHECK, Rational.ZERO, num_player);
	}
	public static final Bid CALL(int num_player) {
		return new Bid(Action.CALL, Rational.ZERO, num_player);
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Bid) {
			Bid bid = (Bid) obj;
			return bid.act_ == act_ && bid.player_ == player_ && bid.amount_ .equals(amount_);
		}
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		return "("+player_+":"+act_+","+amount_+")";
	}
	

}

enum Action {
	BLIND, FOLD, CALL, CHECK, BET, RAISE
}