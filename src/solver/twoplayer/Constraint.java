package solver.twoplayer;

import java.util.ArrayList;

import org.jscience.mathematics.number.Rational;

public class Constraint extends ArrayList<Rational> {

	private static final long serialVersionUID = -8722837168406326210L;
	
	
	public Rational constant_ = Rational.ZERO;
	
	public void to_integer() {
		for (int i = 0; i < size(); i++) {
			int divisor = get(i).getDivisor().intValue();
			if (divisor != 1) {
				for (int j = 0; j < size(); j++) {
					set(j, get(j).times(divisor));
				}
				constant_ = constant_.times(divisor);
			}
		}
		int divisor = constant_.getDivisor().intValue();
		if (divisor != 1) {
			for (int j = 0; j < size(); j++) {
				set(j, get(j).times(divisor));
			}
			constant_ = constant_.times(divisor);
		}
	}

	public void add_to(int index, Rational r) {
		if (index == -1) {
			constant_ = constant_.plus(r);
			return;
		}
		for (int i = 0; i < index+1-size(); i++) {
			add(Rational.ZERO);
		}
		set(index,get(index).plus(r));
	}
	
	@Override
	public String toString() {
		return super.toString()+"::"+constant_;
	}
	
}
