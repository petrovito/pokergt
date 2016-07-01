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
	}
	
	
	public void solve() {
		if (actual_basis_ == null) throw new ArithmeticException("no basis");
		B_inverse_ = A_.submatrix_columns(actual_basis_).inverse();
		b_base_ = B_inverse_.times(b_);
		if (! b_base_.is_non_negative())
			throw new ArithmeticException("not a primal basis");
		c_base_ = c_.subvector(actual_basis_).times(B_inverse_).times(A_).minus(c_);
		value_  = c_.subvector(actual_basis_).scalar_product(b_base_);
		while (!c_base_.is_non_negative()) {
			System.out.println("ASD");
			System.out.println(value_);
			System.out.println(actual_basis_);
			System.out.println(A_.column_num_);
			System.out.println(c_);
			System.out.println(c_base_);
			System.out.println(b_base_);//*/
			Rational min_c = Rational.ZERO;
			int argmin_c = -1;
			for (int i = 0; i < c_base_.dim_; i++) {
				if (min_c.isGreaterThan(c_base_.v_[i])) {
					argmin_c = i;
					min_c = c_base_.v_[i];
				}
			}
			System.out.println(argmin_c + ":" +min_c);
			Vector a_p = (B_inverse_).times(A_.column(argmin_c));//quick?
			System.out.println(argmin_c + ":" + a_p);
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
		System.out.println("ASDASDASD");
		System.out.println(value_);
		System.out.println(actual_basis_);
		System.out.println(c_base_);
		System.out.println(b_base_);//*/
	}


	private void pivot(int argmin_p, int argmin_c, Vector a_p,
			Rational b_ratio) {
		Matrix m = new Matrix(B_inverse_.row_num_,B_inverse_.column_num_);
		for (int j = 0; j < B_inverse_.column_num_; j++) {
			if (j == argmin_p) {
				m.m_[argmin_p][argmin_p] = a_p.v_[argmin_p].inverse();
				continue;
			}
			m.m_[argmin_p][j] = B_inverse_.m_[argmin_p][j].divide(a_p.v_[argmin_p]);
		}
		b_base_.v_[argmin_p] = b_ratio;
		for (int i = 0; i < B_inverse_.row_num_; i++) {
			if (i == argmin_p) continue;
			for (int j = 0; j < B_inverse_.column_num_; j++) {
				if (j == argmin_p)
					m.m_[i][j] = a_p.v_[i].divide(a_p.v_[argmin_p]).opposite();
				else
					m.m_[i][j] = B_inverse_.m_[i][j].minus(
							m.m_[argmin_p][j].times(a_p.v_[i]));
			}
			b_base_.v_[i] = b_base_.v_[i].minus(b_ratio.times(a_p.v_[i]));
		}
		System.out.println(A_.submatrix_columns(actual_basis_).rank());
		actual_basis_.set(argmin_p, argmin_c);
		B_inverse_ = A_.submatrix_columns(actual_basis_).inverse();
		System.out.println(b_ratio);
		Matrix matrix = A_.submatrix_columns(actual_basis_).times(m);
		for (int i = 0; i < matrix.column_num_; i++) {
			for (int j = 0; j < matrix.column_num_; j++) {
				System.out.print(" "+matrix.m_[i][j]);
			}			
			System.out.println("");
		}
		System.out.println(m.rank());
		//assert m.equals(A_.submatrix_columns(actual_basis_).inverse());
		System.out.println(actual_basis_);
		System.out.println(c_.subvector(actual_basis_));
		c_base_ = c_.subvector(actual_basis_).times(B_inverse_).times(A_).minus(c_);
		System.out.println(c_base_);
		assert c_base_.subvector(actual_basis_).equals(Vector.zero(B_inverse_.column_num_));
		value_  = c_.subvector(actual_basis_).scalar_product(b_base_);
	}


	public void find_initial_basis() {
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
		simplex.solve();
		if (simplex.value_.equals(Rational.ZERO)) {
			actual_basis_ = simplex.actual_basis_;
			return;
		}
		throw new ArithmeticException("model infeasible");
	}


	public boolean has_primal_basis() {
		if (actual_basis_ == null) return false;
		return A_.submatrix_rows(actual_basis_).inverse().times(b_).is_non_negative();
	}
	


}
