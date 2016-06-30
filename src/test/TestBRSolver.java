package test;

import solver.twoplayer.BRSolver;

public class TestBRSolver {
	
	public static BRSolver test_brsolver = new BRSolver(TestGame.test_game);
	
	
	public static void main(String[] args) {
		System.out.println("TestBrSolver");
		assert test_brsolver.constraintOf(TestGame.test_game.best_response(TestStrategy.test_strategy1)).toString()
			.equals("[-8/169, -16/169, -8/169, -16/169, -8/169, -16/169, -8/169, -16/169, -8/169, -16/169, -8/169, -14/169, -8/169, -10/169, -8/169, -6/169, -8/169, -2/169, -8/169, 2/169, -8/169, 6/169, -8/169, 10/169, -8/169, 14/169, -1/1]::5/26");

		assert test_brsolver.strategy_constraints(0).get(0).toString().equals(
				"[1/1, 1/1, 0/1, 0/1, 0/1, 0/1, 0/1, 0/1, 0/1, 0/1, 0/1, 0/1, 0/1, 0/1, 0/1, 0/1, 0/1, 0/1, 0/1, 0/1, 0/1, 0/1, 0/1, 0/1, 0/1, 0/1, 0/1]::1/1");

		//System.out.println(test_brsolver.vector_to_strategy(test_brsolver.constraintOf(TestGame.test_game.best_response(TestStrategy.test_strategy1)), 0));
		System.out.println("OK");
	}

}
