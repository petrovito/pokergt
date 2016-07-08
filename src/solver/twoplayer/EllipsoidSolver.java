package solver.twoplayer;

import java.util.ArrayList;

import org.jscience.mathematics.number.Rational;

import algebra.Constraint;
import algebra.EllipsoidOptimizer;
import algebra.Matrix;
import algebra.MatrixD;
import algebra.SeparationOracle;
import algebra.Tools;
import algebra.Vector;
import algebra.VectorD;
import game.PureStrategy;
import game.Strategy;
import game.twoplayer.TwoPlayerGame;

public class EllipsoidSolver extends BRSolver {
	
	
	Matrix kernel_;
	MatrixD kerneld_;
	Vector initial_;
	VectorD initiald_;
	int dim_;
	SeparationOracle sep_ = new SEP();
	EllipsoidOptimizer ell_;
	Strategy strategy_;
	PureStrategy best_response_;
	Rational epsilon_ = Rational.valueOf(1,1024);
	Rational value_;
	VectorD obj_;
	
	
	class SEP implements SeparationOracle {
		Vector center;
		Vector actual;
		boolean violate;
		Vector violation;
		VectorD violationd;

		public void set_center(VectorD v) {
			center = Tools.to_rational(v);
			actual = initial_.plus(kernel_.times(center));
			if (! actual.is_non_negative()) {
				violate = true;
				int arg = 0;
				while (!actual.v_[arg].isNegative()) arg++;
				if (arg < dim_-1) {
					violationd = Tools.to_double(kernel_.row(arg).opposite());
					return;
				}
			}
			strategy_ = EllipsoidSolver.super.vector_to_strategy(actual, 0);
			best_response_ = game_.best_response(strategy_);
			value_ = actual.v_[actual.dim_-1];
			if (value_.isLessThan(best_response_.value.opposite().minus(epsilon_))) {
				violate = true;
				violation = new Vector(EllipsoidSolver.super.constraintOf(best_response_));
				violationd = Tools.to_double(violation.times(kernel_));
			} else violate=false;
		}

		public boolean is_in() {
			return violate;
		}

		public VectorD violation() {
			return violationd;
		}
		
	}
	

	public EllipsoidSolver(TwoPlayerGame game) {
		super(game);
		ArrayList<Constraint> strat_cons = super.strategy_constraints(0);
		Matrix strats = new Matrix(strat_cons.size(),strat_cons.get(0).size());
		for (int i = 0; i < strats.row_num_; i++) {
			for (int j = 0; j < strats.column_num_; j++) {
				strats.m_[i][j] = strat_cons.get(i).get(j);
			}			
		}
		kernel_ = strats.kernel();
		kerneld_ = Tools.to_double(kernel_);
		initial_ = super.strategy_to_vector(game.first_strategy(0),0);
		dim_ = kernel_.column_num_;
		ell_ = new EllipsoidOptimizer(dim_, sep_);
		ArrayList<Rational> obj = new ArrayList<Rational>();
		for (int i = 0; i < initial_.dim_-1; i++)
			obj.add(Rational.ZERO);
		obj.add(Rational.ONE);
		obj_ = Tools.to_double(new Vector(obj).times(kernel_));
		ell_.set_objective(obj_);
	}
	
	
	public Strategy equilibrium() {
		ell_.find_optimum();
		return strategy_;
	}


	
	
	
	
	

}
