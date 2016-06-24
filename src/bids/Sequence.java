package bids;

import java.util.ArrayList;
import java.util.Arrays;

public class Sequence extends ArrayList<Bid> {

	private static final long serialVersionUID = 8347804022559984012L;
	
	public int num_players_;
	public double[] equities_;
	private double to_call_;
	
	public Sequence(int num_players, Bid... bids) { 
		num_players_ = num_players;
		equities_ = new double[num_players];
		for (Bid bid : bids) {
			equities_[bid.player_]+=bid.amount_;
		}
		to_call_=bids[bids.length-1].amount_;
	}

	public Sequence(int num_player, double... blinds) {
		this(num_player,Bid.BLINDS(blinds));
	}

	public Sequence(Sequence sequence) {
		super(sequence);
		equities_ = sequence.equities_.clone();
		num_players_ = sequence.num_players_;
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
			equities_[bid.player_]=to_call_;
			break;
		case BET:
		case RAISE:
			equities_[bid.player_]=bid.amount_;
			to_call_=bid.amount_;
		default:
			break;
		}
		return true;
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
