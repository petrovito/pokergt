package algebra;

import java.util.ArrayList;

import org.jscience.mathematics.number.Rational;

public class Vector {
	
	public Rational[] v_;
	public int dim_;

	public Vector(int n) {
		v_ = new Rational[n];
		dim_ = n;
	}

	public Vector(Rational[] v) {
		v_ = v.clone();
		dim_ = v.length;
	}
	
	public Vector(ArrayList<Rational> vec) {
		v_ = new Rational[vec.size()];
		for (int i = 0; i < vec.size(); i++) {
			v_[i] = vec.get(i);
		}
	}
		
	public Vector times(Matrix matrix) {
		Vector v = new Vector(matrix.column_num_);
		for (int i = 0; i < matrix.column_num_; i++) {
			v.v_[i] = Rational.ZERO;
			for (int k = 0; k < matrix.row_num_; k++)
				v.v_[i] = v.v_[i].plus(matrix.m_[k][i].times(v_[k]));
		}
		return v;
	}
		
	public Rational scalar_product(Vector v) {
		Rational r = Rational.ZERO;
		for (int i = 0; i < v_.length; i++) {
			r = r.plus(v_[i].times(v.v_[i]));
		}
		return r;
	}
		
	public Vector subvector(ArrayList<Integer> entry) {
		Vector v = new Vector(entry.size());
		for (int i = 0; i < entry.size(); i++) {
				v.v_[i] = v_[entry.get(i)];
		}
		return v;
	}

	public Vector minus(Vector vector) {
		Vector v = new Vector(v_.length);
		for (int i = 0; i < v_.length; i++) {
				v.v_[i] = v_[i].minus(vector.v_[i]);
		}
		return v;
	}
	
	public Vector plus(Vector vector) {
		Vector v = new Vector(v_.length);
		for (int i = 0; i < v_.length; i++) {
				v.v_[i] = v_[i].plus(vector.v_[i]);
		}
		return v;
	}

	public boolean is_non_positive() {
		for (Rational r: v_) {
			if (r.isPositive()) return false;
		}
		return true;
	}
	
	public boolean is_non_negative() {
		for (Rational r: v_) {
			if (r.isNegative()) return false;
		}
		return true;
	}
	
	
	@Override
	public String toString() {
		String s = "[ ";
		for (Rational r: v_) s+= r.toString()+" ";
		return s+"]";
	}
	
	
	
	
}
