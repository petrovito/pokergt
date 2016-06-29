package test;

import org.jscience.mathematics.number.Rational;

import bids.Bid;
import bids.Sequence;
import dealers.Dealing;
import game.GamePlay;
import game.twoplayer.TwoPlayerGame;

public class TestGame {
	
	public static TwoPlayerGame test_game = new TwoPlayerGame(TestDealer.test_dealer,TestBidSystem.test_bids);
	
	public static void main(String[] args) {
		System.out.println("TestGame");
		assert test_game.first_situations(0).toString().equals(
			"[([0: [0]]::[]), ([0: [1]]::[]), ([0: [2]]::[]), ([0: [3]]::[]), ([0: [4]]::[]), ([0: [5]]::[]), ([0: [6]]::[]), ([0: [7]]::[]), ([0: [8]]::[]), ([0: [9]]::[]), ([0: [10]]::[]), ([0: [11]]::[]), ([0: [12]]::[])]");
		assert test_game.all_situations(1).toString().equals(
			"[([1: [0]]::[(0:RAISE,2/1)]), ([1: [1]]::[(0:RAISE,2/1)]), ([1: [2]]::[(0:RAISE,2/1)]), ([1: [3]]::[(0:RAISE,2/1)]), ([1: [4]]::[(0:RAISE,2/1)]), ([1: [5]]::[(0:RAISE,2/1)]), ([1: [6]]::[(0:RAISE,2/1)]), ([1: [7]]::[(0:RAISE,2/1)]), ([1: [8]]::[(0:RAISE,2/1)]), ([1: [9]]::[(0:RAISE,2/1)]), ([1: [10]]::[(0:RAISE,2/1)]), ([1: [11]]::[(0:RAISE,2/1)]), ([1: [12]]::[(0:RAISE,2/1)])]");
		GamePlay test_game_play = new GamePlay(test_game);
		Sequence seq = new Sequence(TestBidSystem.test_bids);
		seq.add(Bid.RAISE(Rational.valueOf(2,1), 0));
		seq.add(Bid.CALL(1));
		test_game_play.dealing_ = new Dealing(TestDealer.test_dealer,5,8);
		test_game_play.add(seq);
		assert test_game_play.winnings().toString().equals("[-2/1, 2/1]");
		
		test_game_play = new GamePlay(test_game);
		seq = new Sequence(TestBidSystem.test_bids);
		seq.add(Bid.RAISE(Rational.valueOf(2,1), 0));
		seq.add(Bid.FOLD(1));
		test_game_play.dealing_ = new Dealing(TestDealer.test_dealer,5,8);
		test_game_play.add(seq);
		assert test_game_play.winnings().toString().equals("[1/1, -1/1]");
		
		System.out.println("OK");
	}

}
