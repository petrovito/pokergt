package algebra;

import java.util.ArrayList;

import org.jscience.mathematics.number.Rational;

/**
 * Ax=b
 * x>0
 * max cx
 * @author petrovito
 *
 */
public class Simplex {
	
	public ArrayList<Integer> actual_basis_;
	public Matrix A_;
	public Vector b_, c_;
	private Matrix B_inverse_;
	protected Vector b_base_, c_base_;
	public Rational value_;
	
	
	public Simplex(Matrix A, Vector b, Vector c) {
		A_ = A;
		b_ = b;
		c_ = c;
	}
	
	
	@SuppressWarnings("unchecked")
	public void set_initial_basis(ArrayList<Integer> basis) {
		actual_basis_ = (ArrayList<Integer>) basis.clone();
		B_inverse_ = A_.submatrix_columns(actual_basis_).inverse();
	}
	
	
	public void solve_primal() {
		if (actual_basis_ == null) throw new ArithmeticException("no basis");
		B_inverse_ = A_.submatrix_columns(actual_basis_).inverse();
		b_base_ = B_inverse_.times(b_);
		if (! b_base_.is_non_negative())
			throw new ArithmeticException("not a primal basis");
		c_base_ = c_.subvector(actual_basis_).times(B_inverse_).times(A_).minus(c_);
		value_  = c_.subvector(actual_basis_).scalar_product(b_base_);
		while (!c_base_.is_non_negative()) {
			Rational min_c = Rational.ZERO;
			int argmin_c = -1;
			for (int i = 0; i < c_base_.dim_; i++) {
				if (min_c.isGreaterThan(c_base_.v_[i])) {
					argmin_c = i;
					min_c = c_base_.v_[i];
				}
			}
			Vector a_p = B_inverse_.times(A_.column(argmin_c));
			if (a_p.is_non_positive()) 
				throw new ArithmeticException("unbounded obj fnc");
			Rational b_ratio = Rational.valueOf(1000,1);
			int argmin_p = -1;
			for (int i = 0; i < a_p.dim_; i++) {
				if (! a_p.v_[i].isPositive()) continue;
				Rational ratio = b_base_.v_[i].divide(a_p.v_[i]);
				if (b_ratio.isGreaterThan(ratio)) {
					argmin_p = i;
					b_ratio = ratio;
				}
			}			
			if (b_ratio.equals(Rational.valueOf(1000,1)))
				throw new ArithmeticException("small minimum, programming shit");
			pivot(argmin_p, argmin_c, a_p, b_ratio);
		}
	}

	public void solve_dual() {
		if (actual_basis_ == null) throw new ArithmeticException("no basis");
		B_inverse_ = A_.submatrix_columns(actual_basis_).inverse();
		b_base_ = B_inverse_.times(b_);
		c_base_ = c_.subvector(actual_basis_).times(B_inverse_).times(A_).minus(c_);
		value_  = c_.subvector(actual_basis_).scalar_product(b_base_);	
		if (! c_base_.is_non_negative())
			throw new ArithmeticException("not a dual basis");	
		while (!b_base_.is_non_negative()) {
			Rational min_b = Rational.ZERO;
			int argmin_b = -1;
			for (int i = 0; i < b_base_.dim_; i++) {
				if (min_b.isGreaterThan(b_base_.v_[i])) {
					argmin_b = i;
					min_b = b_base_.v_[i];
				}
			}
			Vector a_p = B_inverse_.row(argmin_b).times(A_);
			if (a_p.is_non_positive()) 
				throw new ArithmeticException("model is not feasible");
			Rational c_ratio = Rational.valueOf(1000,1);
			int argmin_p = -1;
			for (int i = 0; i < a_p.dim_; i++) {
				if (! a_p.v_[i].isNegative()) continue;
				Rational ratio = c_base_.v_[i].divide(a_p.v_[i]).opposite();
				if (c_ratio.isGreaterThan(ratio)) {
					argmin_p = i;
					c_ratio = ratio;
				}
			}			
			if (c_ratio.equals(Rational.valueOf(1000,1)))
				throw new ArithmeticException("small minimum, programming shit");
			Vector in_row = B_inverse_.times(A_.column(argmin_p));
			pivot(argmin_b, argmin_p, in_row,
					b_base_.v_[argmin_b].divide(in_row.v_[argmin_b]));
			
		}
	}


	private void pivot(int argmin_p, int argmin_c, Vector a_p,
			Rational b_ratio) {
		Matrix m = new Matrix(B_inverse_.row_num_,B_inverse_.column_num_);
		for (int j = 0; j < B_inverse_.column_num_; j++) {
			m.m_[argmin_p][j] = B_inverse_.m_[argmin_p][j].divide(a_p.v_[argmin_p]);
		}
		b_base_.v_[argmin_p] = b_ratio;
		for (int i = 0; i < B_inverse_.row_num_; i++) {
			if (i == argmin_p) continue;
			for (int j = 0; j < B_inverse_.column_num_; j++) {
					m.m_[i][j] = B_inverse_.m_[i][j].minus(
							m.m_[argmin_p][j].times(a_p.v_[i]));
			}
			b_base_.v_[i] = b_base_.v_[i].minus(b_ratio.times(a_p.v_[i]));
		}
		actual_basis_.set(argmin_p, argmin_c);
		B_inverse_ =m;
		c_base_ = c_.subvector(actual_basis_).times(B_inverse_).times(A_).minus(c_);
		value_  = c_.subvector(actual_basis_).scalar_product(b_base_);
	}


	public void find_initial_primal_basis() {
		for (int i = 0; i < A_.row_num_; i++) {
			if (!b_.v_[i].isPositive()) {
				b_.v_[i] = b_.v_[i].opposite();
				for (int j = 0; j < A_.column_num_; j++)
					A_.m_[i][j] = A_.m_[i][j].opposite();
			}
		}
		Matrix A = A_.next(Matrix.unit(A_.row_num_));
		Simplex simplex = new Simplex(A, b_, Vector.zero(
				A_.column_num_).under(Vector.unit(A_.row_num_).multiply_scalar(
						Rational.valueOf(-1,1))));
		ArrayList<Integer> b = new ArrayList<Integer>();
		for (int i = A_.column_num_; i < A_.column_num_+A_.row_num_; i++) {
			b.add(i);
		}
		simplex.set_initial_basis(b);
		simplex.solve_primal();
		if (simplex.value_.equals(Rational.ZERO)) {
			actual_basis_ = simplex.actual_basis_;
			return;
		}
		throw new ArithmeticException("model infeasible");
	}


	public boolean has_primal_basis() {
		if (actual_basis_ == null) return false;
		return B_inverse_.times(b_).is_non_negative();
	}


	public boolean has_dual_basis() {
		if (actual_basis_ == null) return false;
		return c_.subvector(actual_basis_).times(B_inverse_).
				times(A_).minus(c_).is_non_negative();
	}

	


}
