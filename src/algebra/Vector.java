package algebra;

import java.util.ArrayList;

import org.jscience.mathematics.number.Rational;

public class Vector {
	
	Rational[] v_;

	public Vector(int n) {
		v_ = new Rational[n];
	}

	public Vector(Rational[] v) {
		v_ = v.clone();
	}
	
	public Vector(ArrayList<Rational> vec) {
		v_ = new Rational[vec.size()];
		for (int i = 0; i < vec.size(); i++) {
			v_[i] = vec.get(i);
		}
	}

}
