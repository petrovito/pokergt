package test;

import org.jscience.mathematics.number.Rational;

import algebra.Matrix;

public class TestMatrix {
	
	public static Matrix test_matrix = new Matrix(2,2);
	
	public static void main(String[] args) {
		System.out.println("TestMatrix");
		test_matrix.m_[0][0] = Rational.valueOf(0, 1);
		test_matrix.m_[0][1] = Rational.valueOf(1, 1);
		test_matrix.m_[1][0] = Rational.valueOf(2, 1);
		test_matrix.m_[1][1] = Rational.valueOf(3, 1);
		Matrix inverse = test_matrix.inverse();
		assert inverse.m_[0][0].equals(Rational.valueOf(-3,2));
		assert inverse.m_[0][1].equals(Rational.valueOf(1,2));
		assert inverse.m_[1][0].equals(Rational.valueOf(1,1));
		assert inverse.m_[1][1].equals(Rational.valueOf(0,1));
		Matrix new_m = test_matrix.elem_basis_transformation(0, 1);
		assert new_m.m_[0][0].equals(Rational.valueOf(0,1));	
		assert new_m.m_[1][0].equals(Rational.valueOf(2,1));
		assert test_matrix.rank() == 2;
		
		Matrix A = new Matrix(2,3);
		A.m_[0][0] = Rational.valueOf(1, 1);
		A.m_[0][1] = Rational.valueOf(2, 1);
		A.m_[0][2] = Rational.valueOf(3, 1);
		A.m_[1][0] = Rational.valueOf(4, 1);
		A.m_[1][1] = Rational.valueOf(5, 1);
		A.m_[1][2] = Rational.valueOf(6, 1);		
		Matrix ker = A.kernel();
		assert ker.m_[0][0].equals(Rational.valueOf(-1,1));
		assert ker.m_[1][0].equals(Rational.valueOf(2,1));
		
		System.out.println("OK");
	}

}
