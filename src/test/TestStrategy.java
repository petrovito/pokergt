package test;

import org.jscience.mathematics.number.Rational;

import bids.Bid;
import bids.Sequence;
import dealers.Card;
import dealers.Deal;
import dealers.Dealing;
import game.GamePlay;
import game.Strategy;

public class TestStrategy {
	

	public static Strategy test_strategy = TestGame.test_game.first_strategy(0);
	public static Strategy test_strategy1 = TestGame.test_game.first_strategy(1);
	
	public static void main(String[] args) {
		System.out.println("Test Strategy");
		Dealing d2 = new Dealing(TestDealer.test_dealer,new Deal(1, new Card(2)));
		Dealing d3 = new Dealing(TestDealer.test_dealer,new Deal(1, new Card(3)));
		Sequence seq = new Sequence(TestBidSystem.test_bids,Bid.RAISE(Rational.valueOf(2, 1), 0));
		assert TestGame.test_game.best_response(test_strategy).get(new GamePlay(TestGame.test_game,d2,seq)).equals(
				Bid.FOLD(1));
		assert TestGame.test_game.best_response(test_strategy).get(new GamePlay(TestGame.test_game,d3,seq)).equals(
				Bid.CALL(1));
		d2 = new Dealing(TestDealer.test_dealer,new Deal(0, new Card(4)));
		d3 = new Dealing(TestDealer.test_dealer,new Deal(0, new Card(5)));
		seq = new Sequence(TestBidSystem.test_bids);
		assert TestGame.test_game.best_response(test_strategy1).get(new GamePlay(TestGame.test_game,d2,seq)).equals(
				Bid.FOLD(0));
		assert TestGame.test_game.best_response(test_strategy1).get(new GamePlay(TestGame.test_game,d3,seq)).equals(
				Bid.RAISE(Rational.valueOf(2, 1),0));
		System.out.println("OK");
		
	}
	
	
}
