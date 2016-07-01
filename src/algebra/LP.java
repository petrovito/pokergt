package algebra;

import java.util.ArrayList;

import org.jscience.mathematics.number.Rational;

public class LP {
	
	private Matrix A_;
	private Vector b_, c_;
	private int length_;
	Simplex simplex_;
	public boolean maximize_ = true;
	ArrayList<Constraint> equalities_ = new ArrayList<Constraint>();
	ArrayList<Constraint> less_than_ = new ArrayList<Constraint>();
	ArrayList<Rational> obj_;
	ArrayList<Integer> unbounded_ = new ArrayList<Integer>();
	public Rational value_;
	
	public void add_equalities(ArrayList<Constraint> c) {
		equalities_.addAll(c);
	}

	public void add_less_than(Constraint constraint) {
		less_than_.add(constraint);
	}

	public void add_greater_than(ArrayList<Constraint> gt) {
		for (Constraint c: gt) {
			less_than_.add(c.negation());
		}
	}
	
	public void add_greater_than(Constraint con) {
		less_than_.add(con.negation());
	}
	
	public void set_unbounded(int i) {
		unbounded_.add(i);
	}
	
	
	public void set_objective(ArrayList<Rational> obj) {
		obj_ = obj;
	}
	
	private void set_datas() {
		ArrayList<Rational> obj = new ArrayList<>(obj_);
		for (int i: unbounded_) {
			obj.add(obj.get(i).opposite());
		}
		ArrayList<Constraint> equalities = new ArrayList<Constraint>();
		for (Constraint c: equalities_) {
			Constraint con = new Constraint(c);
			for (int i: unbounded_) {
				con.add(c.get(i).opposite());
			}
			equalities.add(con);
		}
		ArrayList<Constraint> less_than = new ArrayList<Constraint>();
		for (Constraint c: less_than_) {
			Constraint con = new Constraint(c);
			for (int i: unbounded_) {
				con.add(c.get(i).opposite());
			}
			less_than.add(con);
		}
		int eq = equalities.size();
		int lt = less_than.size();
		c_ = new Vector(obj).under(Vector.zero(lt));
		Matrix A_eq = null, A_lt = null;
		Vector b_eq = null, b_lt = null;
		if (eq > 0) {
			length_ = equalities.get(0).size();
			A_eq = new Matrix(eq, length_);
			b_eq = new Vector(eq);
			for (int i = 0; i < eq; i++) {
				Constraint c = equalities.get(i);
				for (int j = 0; j < length_; j++) {
					A_eq.m_[i][j] = c.get(j);
				}
				b_eq.v_[i] = c.constant_;
			}
			if (lt == 0)  {
				A_ = A_eq;
				b_ = b_eq;
				return;
			}
		}
		if (lt > 0) {
			length_ = less_than.get(0).size()+lt;
			A_lt = new Matrix(lt, less_than.get(0).size());
			b_lt = new Vector(lt);
			for (int i = 0; i < lt; i++) {
				Constraint c = less_than.get(i);
				for (int j = 0; j < less_than.get(0).size(); j++) {
					A_lt.m_[i][j] = c.get(j);
				}
				b_lt.v_[i] = c.constant_;
			}
			A_lt = A_lt.next(Matrix.unit(lt));
			if (eq == 0)  {
				A_ = A_lt;
				b_ = b_lt;
				return;
			}
		}
		A_eq = A_eq.next(Matrix.zero(eq,lt));
		A_ = A_eq.under(A_lt);
		b_ = b_eq.under(b_lt);
	}
	
	public void solve() {
		set_datas();
		if (simplex_ != null) {
			simplex_.solve();
		}
		simplex_ = new Simplex(A_, b_, c_);
		if (!simplex_.has_primal_basis())
			simplex_.find_initial_basis();
		simplex_.solve();
		value_ = simplex_.value_;
		System.out.println(variables());
	}
	
	
	public ArrayList<Rational> variables() {
		ArrayList<Rational> coeff = new ArrayList<Rational>();
		for (int i = 0; i < obj_.size(); i++) {
			if (simplex_.actual_basis_.contains(i)) {
				coeff.add(simplex_.b_base_.v_[simplex_.actual_basis_.indexOf(i)]);
			} else coeff.add(Rational.ZERO); 
		}
		for (int i = 0; i < unbounded_.size(); i++) {
			if (simplex_.actual_basis_.contains(obj_.size()+i)) {
				coeff.set(unbounded_.get(i),coeff.get(unbounded_.get(i)).minus(
					simplex_.b_base_.v_[simplex_.actual_basis_.indexOf(obj_.size()+i)]));
			}			
		}
		return coeff;
	}

	

}
