package bids;

import java.util.ArrayList;

public class Sequence extends ArrayList<Bid> {

	private static final long serialVersionUID = 8347804022559984012L;
	
	private BidSystem bid_system_;
	public double[] equities_;
	private double to_call_;
	
	public Sequence(BidSystem bid_system) { 
		bid_system_ = bid_system;
		equities_ = new double[bid_system.num_players()];
		for (Bid bid : bid_system.blinds()) {
			equities_[bid.player_]+=bid.amount_;
		}
		to_call_=bid_system.blinds().get(bid_system.blinds().size()-1).amount_;
	}

	public Sequence(Sequence sequence) {
		super(sequence);
		bid_system_ = sequence.bid_system_;
		equities_ = sequence.equities_.clone();
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
	
	public boolean is_bid_end() {
		return bid_system_.is_bid_end(this);
	}
	
	public boolean is_game_end() {
		return bid_system_.is_game_end(this);
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
