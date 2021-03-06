package test;


import algebra.Ellipsoid;
import algebra.EllipsoidOptimizer;
import algebra.SeparationOracle;
import algebra.VectorD;
import solver.twoplayer.EllipsoidSolver;

public class TestEllipsoid {
	
	
	public static SeparationOracle test_sep = new SEP();
	
	
	public static void main(String[] args) {
		Ellipsoid ell = new Ellipsoid(test_sep, 2);
		assert ell.is_feasible();
		VectorD obj = new VectorD(new double[] {1,0});
		EllipsoidOptimizer eo = new EllipsoidOptimizer(2, test_sep);
		eo.set_objective(obj);
		System.out.println(eo.find_optimum());
		
		EllipsoidSolver es = new EllipsoidSolver(TestGame.test_game);
		es.equilibrium();
	}

}


class SEP implements SeparationOracle {
	VectorD c1 = new VectorD(new double[] {1,1});
	VectorD c2 = new VectorD(new double[] {-1,-2});
	VectorD e1 = new VectorD(new double[] {-1,0});
	VectorD e2 = new VectorD(new double[] {0,-1});
	VectorD center;
	
	
	public VectorD violation() {
		if (center.scalar_product(c1)>1)
			return c1;
		//if (center.scalar_product(c2)>-0.9999)
		//	return c2;
		if (center.scalar_product(e1)>0)
			return e1;
		if (center.scalar_product(e2)>0)
			return e2;
		return null;
	}
	public void set_center(VectorD v) {
		center = v;
	}
	public boolean is_in() {
		if (center.scalar_product(c1)>1)
			return false;
		//if (center.scalar_product(c2)>-0.9999)
		//	return false;
		if (center.scalar_product(e1)>0)
			return false;
		if (center.scalar_product(e2)>0)
			return false;
		return true;
	}
	
}
