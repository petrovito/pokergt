package algebra;


public class Ellipsoid {
	
	SeparationOracle sep_;
	VectorD center_;
	MatrixD rads_;
	int actual_num_;
	int max_num_;
	int dimension_;
	
	
	public Ellipsoid(SeparationOracle sep, int dimension) {
		sep_ = sep;
		dimension_ = dimension;
		rads_ = MatrixD.diagonal(1, dimension);
		center_ = VectorD.zero(dimension);
		actual_num_ = 0;
		max_num_ = 50*dimension*dimension;		
	}
	
	
	
	public boolean is_feasible() {
		while (actual_num_ < max_num_) {
			System.out.println(center_);
			sep_.set_center(center_);
			if (sep_.is_in())
				return true;
			VectorD c = sep_.violation();
			double coeff_sqr = c.times(rads_).scalar_product(c);
			double coeff = 1/Math.sqrt(coeff_sqr);
			VectorD b = rads_.times(c).multiply_scalar(coeff);
			center_ = center_.minus(b.multiply_scalar(1./(dimension_+1)));
			double coeff2 = 1.*dimension_*dimension_/(dimension_*dimension_-1);
			rads_ = rads_.minus(b.matrix_product(b.multiply_scalar(2./(dimension_+1)))).multiply_scalar(coeff2);
			actual_num_++;
		}
		return false;
	}


/*
	private Rational sqrt(Rational r, int precision) {
		double d = r.doubleValue()*denominator_sqr;
		int p = (int) Math.sqrt(d);
		System.out.println(p);
		System.out.println(Math.sqrt(r.doubleValue())+" "+Rational.valueOf(p,denominator).doubleValue());
		return Rational.valueOf(p,denominator);
	}*/
	
	

}
