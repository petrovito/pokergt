package algebra;

import java.util.ArrayList;

import org.jscience.mathematics.number.Rational;

public class EllipsoidOptimizer {
	
	double actual_value_ = 0d;
	double epsilon_ = 0.01d;
	double initial_delta_ = 0d, actual_delta_;
	int deepness_ = 1; //for binary search
	int dimension_;
	boolean increasing_ = true;
	VectorD obj_;
	SeparationOracle sep_;
	Ellipsoid ell_;
	
	public void set_objective(ArrayList<Rational> obj) {
		obj_ = new VectorD(obj.size());
		for (int i = 0; i < obj.size(); i++) {
			obj_.v_[i] = -obj.get(i).doubleValue();
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
		}
		public boolean is_in() {
			if (violate) return true;
			return sep_.is_in();
		}
	};
	
	
	public void find_optimum() {
		
	}
	
	

}
