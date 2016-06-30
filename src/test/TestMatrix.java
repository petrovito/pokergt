package test;

import org.jscience.mathematics.number.Rational;

import algebra.Matrix;

public class TestMatrix {
	
	public static Matrix test_matrix = new Matrix(2,2);
	
	public static void main(String[] args) {
		System.out.println("TestMatrix");
		test_matrix.m_[0][0] = Rational.ONE;
		test_matrix.m_[0][1] = Rational.valueOf(3, 1);
		test_matrix.m_[1][0] = Rational.valueOf(2, 1);
		test_matrix.m_[1][1] = Rational.valueOf(4, 1);
		Matrix new_m = test_matrix.elem_basis_transformation(0, 0);
		assert new_m.m_[0][1].equals(Rational.valueOf(3,1));	
		assert new_m.m_[1][1].equals(Rational.valueOf(-2,1));
		assert test_matrix.rank() == 2;
		System.out.println("OK");
	}

}
