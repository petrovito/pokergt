package algebra;


public class VectorD {
	
	public double[] v_;
	public int dim_;

	public VectorD(int n) {
		v_ = new double[n];
		dim_ = n;
	}

	
	public VectorD(double[] d) {
		dim_ = d.length;
		v_ = new double[dim_];
		for (int i = 0; i < d.length; i++)
			v_[i] = d[i];
	}


	public VectorD times(MatrixD matrix) {
		VectorD v = new VectorD(matrix.column_num_);
		for (int i = 0; i < matrix.column_num_; i++) {
			v.v_[i] = 0;
			for (int k = 0; k < matrix.row_num_; k++)
				v.v_[i] = v.v_[i]+matrix.m_[k][i]*v_[k];
		}
		return v;
	}
		
	public double scalar_product(VectorD v) {
		double r = 0;
		for (int i = 0; i < v_.length; i++) {
			r = r+v_[i]*v.v_[i];
		}
		return r;
	}
	
	public MatrixD matrix_product(VectorD v) {
		MatrixD m = new MatrixD(dim_,v.dim_);
		for (int i = 0; i < m.row_num_; i++) {
			for (int j = 0; j < m.column_num_; j++) {
				m.m_[i][j] = v_[i]*v.v_[j];
			}
		}
		return m;
	}	
	
	public static VectorD zero(int n) {
		VectorD v = new VectorD(n);
		for (int i = 0; i < n; i++)
			v.v_[i]=0;
		return v;
	}
	
	
	public VectorD multiply_scalar(double r) {
		VectorD v = new VectorD(dim_);
		for (int i = 0; i < dim_; i++)
			v.v_[i]=r*(v_[i]);
		return v;
	}
	
	public VectorD minus(VectorD vector) {
		VectorD v = new VectorD(v_.length);
		for (int i = 0; i < v_.length; i++) {
				v.v_[i] = v_[i]-vector.v_[i];
		}
		return v;
	}
	
	@Override
	public String toString() {
		String s = "[ ";
		for (double r: v_) s+= r+" ";
		return s+"]";
	}
	

}
