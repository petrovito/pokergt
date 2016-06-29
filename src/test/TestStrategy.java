package test;

import game.Strategy;

public class TestStrategy {
	
	
	public static Strategy test_strategy = TestGame.test_game.first_strategy(0);
	
	public static void main(String[] args) {
		System.out.println("Test Strategy");
		System.out.println(test_strategy);
		System.out.println(TestGame.test_game.best_response(test_strategy));
		System.out.println("OK");
	}
	
	
}
