package models;

import java.util.ArrayList;
import java.util.Arrays;

public class Sequence extends ArrayList<Bid> {

	private static final long serialVersionUID = 8347804022559984012L;
	
	public int num_players_;
	public double[] equities;
	private double to_call;
	
	public Sequence(int num_players, Bid... bids) { 
		super(Arrays.asList(bids));
		num_players_ = num_players;
		equities = new double[num_players];
		for (Bid bid : bids) {
			equities[bid.player]+=bid.amount;
		}
		to_call=bids[bids.length-1].amount;
	}

	public Sequence(int num_player, double... blinds) {
		this(num_player,Bid.BLINDS(blinds));
	}

	public Sequence(Sequence sequence) {
		super(sequence);
		equities = sequence.equities.clone();
		num_players_ = sequence.num_players_;
		to_call = sequence.to_call;
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
		switch (bid.act) {
		case CALL:
			equities[bid.player]=to_call;
			break;
		case BET:
		case RAISE:
			equities[bid.player]=bid.amount;
			to_call=bid.amount;
		default:
			break;
		}
		return true;
	}

}
