package bids;

import java.util.ArrayList;

import org.jscience.mathematics.number.Rational;

public abstract class BidSystem {
	
	public abstract boolean is_bid_end(Sequence s);
	public abstract boolean is_game_end(Sequence s);
	public int next_player(Sequence s) { return s.size()%2; }
	public abstract ArrayList<Bid> possible_moves(Sequence s);
	public ArrayList<Bid> blinds() {
		return Bid.BLINDS(Rational.valueOf(1, 2),Rational.ONE);
	}
	public int num_players() { return 2; }
	
	public ArrayList<Sequence> sequences_to_respond(int player) {
		ArrayList<Sequence> sequences = new ArrayList<Sequence>();
		recursive_sequences(sequences, new Sequence(this),player);
		return sequences;
	}
	
	private void recursive_sequences(ArrayList<Sequence> sequences, 
			Sequence actual_sequence, int player) {
		if (next_player(actual_sequence) == player) 
			sequences.add(actual_sequence);
		for (Bid bid: possible_moves(actual_sequence)) {
			Sequence new_seq = actual_sequence.copy(bid);
			if (!is_bid_end(new_seq))
				recursive_sequences(sequences, new_seq, player);
		}
	}
	
	public int first_player() {
		return next_player(new Sequence(this));
	}
	
	public ArrayList<Sequence> first_situations(int player) {
		return next_situations(new Sequence(this),player);
	}
	
	public ArrayList<Sequence> first_situations(int player, ArrayList<Rational> equities) {
		return next_situations(new Sequence(this,equities),player);
	}
	
	
	
	public ArrayList<Sequence> next_situations(Sequence seq, int player) {
		ArrayList<Sequence> moves = new ArrayList<Sequence>();
		if (is_bid_end(seq)) return moves;
		if (next_player(seq)==player) 		
			moves.add(seq);
		else {
			for (Bid bid: possible_moves(seq)) {
				Sequence new_sequence = seq.copy(bid);
					moves.addAll(next_situations(new_sequence, player));
			}
		}
		return moves;
	}
}
