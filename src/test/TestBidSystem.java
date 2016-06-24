package test;

import java.util.ArrayList;

import bids.Bid;
import bids.BidSystem;
import bids.Sequence;

public class TestBidSystem {
	
	public static void main(String[] args) {
		System.out.println("TestBidSystem");
		
		BidSystem test_bids = new BidSystem() {
			
			public Bid[] blinds() {
				return Bid.BLINDS(0.5,1);
			}
			
			@Override
			public Bid[] possible_moves(Sequence s) {
				if (s.size() == 0) {
					return new Bid[]{Bid.FOLD(0), Bid.RAISE(2, 0)} ;
				}
				return new Bid[] {Bid.CALL(1),Bid.FOLD(1)};
			}
			
			@Override
			public boolean is_game_end(Sequence s) {
				if (s.size() == 0) return false;
				return s.size()==2 || s.get(0).equals(Bid.FOLD(0));
			}
		};
		ArrayList<Sequence> seq0 = test_bids.sequence_to_respond(0);
		assert seq0.size() == 1;
		assert seq0.contains(new Sequence(2,0.5,1));
		ArrayList<Sequence> seq1 = test_bids.sequence_to_respond(1);
		assert seq1.size() == 1;
		assert seq1.get(0).equals(new Sequence(2,0.5,1).copy(Bid.RAISE(2,0)));
		
		System.out.println("OK");
	}

}
