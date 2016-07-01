package test;

import java.util.ArrayList;

import org.jscience.mathematics.number.Rational;

import algebra.Constraint;
import algebra.LP;
import algebra.Matrix;
import algebra.Simplex;
import algebra.Vector;

public class TestSimplex {
	
	public static void main(String[] args) {
		/*Matrix b0 = new Matrix(3,3);
		b0.m_[0] = new Rational [] { Rational.valueOf(0, 1), Rational.valueOf(1, 1),
				Rational.valueOf(0, 1)};
		b0.m_[1] = new Rational [] { Rational.valueOf(0, 1), Rational.valueOf(0, 1),
				Rational.valueOf(1, 1) };
		b0.m_[2] = new Rational [] { Rational.valueOf(1, 1), Rational.valueOf(0, 1),
				Rational.valueOf(0, 1) };
		System.out.println(b0.inverse().column(0));
		System.out.println(b0.inverse().column(1));
		System.out.println(b0.inverse().column(2));*/
		Matrix A = new Matrix(2,4);
		A.m_[0] = new Rational [] { Rational.valueOf(2, 1), Rational.valueOf(1, 1),
				Rational.valueOf(1, 1) ,Rational.valueOf(0, 1) };
		A.m_[1] = new Rational [] { Rational.valueOf(1, 1), Rational.valueOf(2, 1),
				Rational.valueOf(0, 1), Rational.valueOf(1, 1) };
		Vector b = new Vector(2);
		b.v_[0]=Rational.ONE;
		b.v_[1]=Rational.ONE;
		Vector c = new Vector(4);
		c.v_[0]=Rational.ONE;
		c.v_[1]=Rational.ONE;
		c.v_[2]=Rational.ZERO;
		c.v_[3]=Rational.ZERO;
		Simplex simplex = new Simplex(A, b, c);
		ArrayList<Integer> basis = new ArrayList<Integer>();
		basis.add(2);
		basis.add(3);
		simplex.find_initial_basis();
		simplex.solve();
		assert simplex.value_.equals(Rational.valueOf(2,3));
		
		LP lp = new LP();
		lp.add_equalities(TestBRSolver.test_brsolver.strategy_constraints(0));
		Constraint con = TestBRSolver.test_brsolver.constraintOf(
				TestGame.test_game.best_response(TestStrategy.test_strategy));
		//con.constant_=Rational.valueOf(66,338);
		System.out.println(con);
		lp.add_greater_than(con);
		ArrayList<Rational> obj = new ArrayList<Rational>();
		for (int i = 0; i < con.size()-1; i++)
			obj.add(Rational.ZERO);
		obj.add(Rational.ONE);
		lp.set_objective(obj);
		//lp.set_unbounded(obj.size()-1);
		lp.solve();
		assert lp.value_.equals(Rational.valueOf(-129,338));
	}
	
	

}
