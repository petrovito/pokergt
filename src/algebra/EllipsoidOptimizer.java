package algebra;

import java.util.ArrayList;

import org.jscience.mathematics.number.Rational;

public class EllipsoidOptimizer {
	
	double actual_value_ = 0d;
	double epsilon_ = 0.01d;
	double initial_delta_ = 0d, actual_delta_;
	int deepness_ = 0; //for binary search
	int dimension_;
	boolean increasing_ = true;
	VectorD obj_;
	SeparationOracle sep_;
	Ellipsoid ell_;
	MatrixD rads_ = null;
	VectorD center_ = null;
	
	public void set_objective(ArrayList<Rational> obj) {
		obj_ = new VectorD(obj.size());
		for (int i = 0; i < obj.size(); i++) {
			obj_.v_[i] = -obj.get(i).doubleValue();
		}
	}	
	
	public void set_objective(VectorD obj) {
		obj_ = new VectorD(obj.dim_);
		for (int i = 0; i < obj.dim_; i++) {
			obj_.v_[i] = -obj.v_[i];
		}
	}
	
	
	public EllipsoidOptimizer(int dimension, SeparationOracle sep) {
		dimension_ = dimension;
		sep_ = sep;
		ell_ = new Ellipsoid(opt_sep_, dimension);
	}
	
	
	SeparationOracle opt_sep_ = new SeparationOracle() {	
		VectorD center;
		boolean violate = false;
		public VectorD violation() {
			if (violate) return obj_;
			return sep_.violation();
		}
		public void set_center(VectorD v) {
			center = v;
			violate = (center.scalar_product(obj_)>-actual_value_);
			if (!violate) sep_.set_center(v);
		}
		public boolean is_in() {
			if (violate) return false;
			return sep_.is_in();
		}
	};
	
	
	public double find_optimum() {
		if (!ell_.is_feasible()) throw new ArithmeticException("model not feasible at given start value");
		initial_delta_ = epsilon_;
		actual_delta_ = epsilon_;
		int maxcounter = 25;
		while (ell_.is_feasible()) {
			if (deepness_ == maxcounter) throw new ArithmeticException("obj unbounded");
			rads_ = new MatrixD(ell_.rads_);
			center_ = new VectorD(ell_.center_);
			deepness_++;
			actual_delta_ *= 2;
			actual_value_ += actual_delta_;
		}
		ell_.center_ = new VectorD(center_);
		ell_.rads_ = new MatrixD(rads_);
		actual_delta_ /= 2;
		actual_value_ -= actual_delta_;
		while (deepness_ > 0) {
			actual_delta_ /= 2;
			deepness_--;
			if (ell_.is_feasible()) {
				actual_value_ += actual_delta_;
				rads_ = new MatrixD(ell_.rads_);
				center_ = new VectorD(ell_.center_);
			} else {
				actual_value_ -= actual_delta_;		
				ell_.center_ = new VectorD(center_);
				ell_.rads_ = new MatrixD(rads_);		
			}
		}		
		return actual_value_;
	}
	
	

}
