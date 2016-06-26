package test;


import java.util.ArrayList;
import java.util.Arrays;

import org.jscience.mathematics.number.Rational;

import dealers.Deal;
import dealers.Dealer;
import dealers.Dealing;

public class TestDealer {
	

	
	public static Dealer test_dealer = new Dealer() {
		int num_cards_ = 13;
		public ArrayList<Integer> winners(Dealing dealing) {
			int c0 = dealing.get(0).cards_.get(0).num_;
			int c1 = dealing.get(1).cards_.get(0).num_;
			if (c0==c1) return new ArrayList<Integer>(Arrays.asList(0,1));
			if (c0>c1) return new ArrayList<Integer>(Arrays.asList(0));
			return new ArrayList<Integer>(Arrays.asList(1));
		}
		public Rational possibility(Dealing dealing) {
			if (dealing.size() == 1) return Rational.valueOf(1,num_cards_);
			return Rational.valueOf(1,num_cards_*num_cards_);
		}
		public boolean is_end(Dealing dealing) {
			return dealing.size()>0;
		}
		@Override
		public ArrayList<Deal> possible_deals(Dealing dealing, int player) {
			ArrayList<Deal> deals = new ArrayList<Deal>();
			if (dealing.size()!=0) return deals;
			for (int i =0; i<num_cards_;i++) {
				deals.add(new Deal(player, i));
			}
			return deals;
		}
		@Override
		public ArrayList<Dealing> possible_opponent_dealings(Dealing dealing) {
			ArrayList<Dealing> dealings = new ArrayList<Dealing>();
			if (dealing.size()!=1) return dealings;
			for (int i =0; i<num_cards_;i++) {
				dealings.add(dealing.copy(new Deal(1-dealing.get(0).player_, i)));
			}
			return dealings;
		}
	};

	
	public static void main(String[] args) {
		System.out.println("Test Dealer");
		assert test_dealer.possible_dealings(0).toString().equals(
				"[[0: [0]], [0: [1]], [0: [2]], [0: [3]], [0: [4]], [0: [5]], [0: [6]], [0: [7]], [0: [8]], [0: [9]], [0: [10]], [0: [11]], [0: [12]]]");
		assert test_dealer.possible_dealings(1).toString().equals(
				"[[1: [0]], [1: [1]], [1: [2]], [1: [3]], [1: [4]], [1: [5]], [1: [6]], [1: [7]], [1: [8]], [1: [9]], [1: [10]], [1: [11]], [1: [12]]]");
		assert test_dealer.possible_opponent_dealings(new Dealing(test_dealer,0)).toString().equals(
				"[[0: [0], 1: [0]], [0: [0], 1: [1]], [0: [0], 1: [2]], [0: [0], 1: [3]], [0: [0], 1: [4]], [0: [0], 1: [5]], [0: [0], 1: [6]], [0: [0], 1: [7]], [0: [0], 1: [8]], [0: [0], 1: [9]], [0: [0], 1: [10]], [0: [0], 1: [11]], [0: [0], 1: [12]]]");
		assert test_dealer.possibility(new Dealing(test_dealer,0,1)).getDivisor().intValue()==169;
		assert test_dealer.possible_dealings(0).get(0).equals(new Dealing(test_dealer,0));
		System.out.println("OK");		
	}

}
