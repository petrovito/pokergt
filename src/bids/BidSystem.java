package bids;

import java.util.ArrayList;

public abstract class BidSystem {
	
	public boolean is_bid_end(Sequence s) { return is_game_end(s); }
	public abstract boolean is_game_end(Sequence s);
	public int next_player(Sequence s) { return s.size()%2; }
	public abstract Bid[] possible_moves(Sequence s);
	public Bid[] blinds() {
		return Bid.BLINDS(0.5,1);
	}
	public int num_players() { return 2; }
	
	public ArrayList<Sequence> sequence_to_respond(int player) {
		ArrayList<Sequence> sequences = new ArrayList<Sequence>();
		recursive_sequences(sequences, new Sequence(num_players(),blinds()),player);
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
	
}
