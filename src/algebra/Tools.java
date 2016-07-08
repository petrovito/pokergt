package algebra;

import org.jscience.mathematics.number.Rational;

public class Tools {
	
	
	public static Rational to_rational(double d) {
		return to_rational(d, 7);
	}
	
	public static Rational to_rational(double d, int prec) {
		int enumerator = 1 << prec;
		return Rational.valueOf((int)(d*enumerator),enumerator);
	}
	
	public static Vector to_rational(VectorD v) {
		Vector vector = new Vector(v.dim_);
		for (int i = 0; i < v.dim_; i++)
			vector.v_[i] = to_rational(v.v_[i]);
		return vector;
	}
	
	public static Matrix to_rational(MatrixD m) {
		Matrix matrix = new Matrix(m.row_num_,m.column_num_);
		for (int i = 0; i < m.row_num_; i++)
			for (int j = 0; j < m.column_num_; j++)
				matrix.m_[i][j] = to_rational(m.m_[i][j]);
		return matrix;
	}
	
	public static MatrixD to_double(Matrix m) {
		MatrixD matrix = new MatrixD(m.row_num_,m.column_num_);
		for (int i = 0; i < m.row_num_; i++)
			for (int j = 0; j < m.column_num_; j++)
				matrix.m_[i][j] = m.m_[i][j].doubleValue();
		return matrix;
	}

	public static VectorD to_double(Vector vector) {
		VectorD v = new VectorD(vector.dim_);
		for (int i = 0; i < v.dim_; i++)
			v.v_[i] = vector.v_[i].doubleValue();
		return v;
	}
	
	

}
