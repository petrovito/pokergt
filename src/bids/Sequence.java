package bids;

import java.util.ArrayList;
import java.util.Arrays;

import org.jscience.mathematics.number.Rational;

public class Sequence extends ArrayList<Bid> {

	private static final long serialVersionUID = 8347804022559984012L;
	
	private BidSystem bid_system_;
	public ArrayList<Rational> equities_;
	private Rational to_call_;

	public Sequence(BidSystem bid_system) { 
		bid_system_ = bid_system;
		equities_ = new ArrayList<Rational>();
		for (Bid bid : bid_system.blinds()) {
			equities_.add(bid.amount_);
		}
		to_call_=bid_system.blinds().get(bid_system.blinds().size()-1).amount_;
	}

	public Sequence(BidSystem bid_system, Bid... bids) { 
		super(Arrays.asList(bids));
		bid_system_ = bid_system;
		equities_ = new ArrayList<Rational>();
		for (Bid bid : bid_system.blinds()) {
			equities_.add(bid.amount_);
		}
		to_call_=bid_system.blinds().get(bid_system.blinds().size()-1).amount_;
	}
	
	public Sequence(BidSystem bid_system, ArrayList<Rational> equities) { 
		bid_system_ = bid_system;
		equities_ = new ArrayList<Rational>(equities);
		to_call_=equities.get(equities.size()-1);
	}

	public Sequence(Sequence sequence) {
		super(sequence);
		bid_system_ = sequence.bid_system_;
		equities_ = new ArrayList<Rational>(sequence.equities_);
		to_call_ = sequence.to_call_;
	}

	public Sequence copy() {
		return new Sequence(this);
	}

	public Sequence copy(Bid bid) {
		Sequence new_seq = new Sequence(this);
		new_seq.add(bid);
		return new_seq;
	}
	
	public boolean add(Bid bid) {
		super.add(bid);
		switch (bid.act_) {
		case CALL:
			equities_.set(bid.player_,to_call_);
			break;
		case BET:
		case RAISE:
			to_call_=bid.amount_;
			equities_.set(bid.player_,to_call_);
		default:
			break;
		}
		return true;
	}
	
	public boolean is_bid_end() {
		return bid_system_.is_bid_end(this);
	}
	
	public boolean is_game_end() {
		return bid_system_.is_game_end(this);
	}

	public int next_player() {
		return bid_system_.next_player(this);
	}
	
	
	public ArrayList<Rational> winnings() {
		Rational sum = Rational.ZERO;
		Rational max = Rational.ZERO;
		int winner = -1;
		for (int i = 0; i < equities_.size(); i++) {
			Rational r = equities_.get(i);
			sum = sum.plus(r);
			if (r.isGreaterThan(max)) {
				winner = i;
				max = r;
			} 
		}
		ArrayList<Rational> winnings = new ArrayList<Rational>();
		for (int i = 0; i < equities_.size(); i++) {
			if (i==winner)
				winnings.add(sum.minus(equities_.get(i)));
			else 
				winnings.add(equities_.get(i).opposite());
		}
		return winnings;		
	}

	public ArrayList<Bid> possible_bids() {
		return bid_system_.possible_moves(this);
	}
	
	/*
	@Override
	public boolean equals(Object o) {
		if (o instanceof Sequence) {
			Sequence seq = (Sequence) o;
			return super.equals(seq) && Arrays.equals(equities_,seq.equities_)  
					&& seq.num_players_ == num_players_;
		}
		return super.equals(o);
	}
	*/

}
