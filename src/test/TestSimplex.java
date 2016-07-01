package test;

import java.util.ArrayList;

import org.jscience.mathematics.number.Rational;

import algebra.Matrix;
import algebra.Simplex;
import algebra.Vector;

public class TestSimplex {
	
	public static void main(String[] args) {
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
		simplex.set_initial_basis(basis);
		simplex.solve();
		assert simplex.value_.equals(Rational.valueOf(2,3));
	}
	
	

}
