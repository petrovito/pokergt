package test;

import java.util.ArrayList;
import java.util.Arrays;

import org.jscience.mathematics.number.Rational;

import bids.Bid;
import bids.BidSystem;
import bids.Sequence;

public class TestBidSystem {
	
	public static BidSystem test_bids = new BidSystem() {
				
		@Override
		public ArrayList<Bid> possible_moves(Sequence s) {
			if (s.size() == 0) {
				return new ArrayList<Bid>(Arrays.asList(
						new Bid[]{Bid.FOLD(0), Bid.RAISE(Rational.valueOf(2, 1), 0)})) ;
			}
			return new ArrayList<Bid>(Arrays.asList(
					new Bid[] {Bid.CALL(1),Bid.FOLD(1)}));
		}
		
		@Override
		public boolean is_game_end(Sequence s) {
			if (s.size() == 0) return false;
			return s.get(0).is_fold() || (s.size()>1 && s.get(1).is_fold());
		}

		@Override
		public boolean is_bid_end(Sequence s) {
			return is_game_end(s) || s.size()==2;
		}
	};
	
	public static void main(String[] args) {
		System.out.println("TestBidSystem");
		ArrayList<Sequence> seq0 = test_bids.sequences_to_respond(0);
		assert seq0.size() == 1;
		assert seq0.contains(new Sequence(test_bids));
		ArrayList<Sequence> seq1 = test_bids.sequences_to_respond(1);
		assert seq1.size() == 1;
		assert seq1.get(0).equals(new Sequence(test_bids).copy(Bid.RAISE(Rational.valueOf(2, 1),0)));
		
		assert test_bids.first_situations(1).get(0).size()==1;
		assert test_bids.first_situations(1).get(0).get(0).equals(Bid.RAISE(Rational.valueOf(2, 1), 0));
		
		assert test_bids.first_situations(1).get(0).copy(Bid.FOLD(1)).winnings().toString().equals(
				"[1/1, -1/1]");
		assert new Sequence(test_bids).copy(Bid.FOLD(0)).winnings().toString().equals(
				"[-1/2, 1/2]");
		
		System.out.println("OK");
	}

}
