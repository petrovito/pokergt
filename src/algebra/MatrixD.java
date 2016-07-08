package algebra;


public class MatrixD {

	public double[][] m_;
	public int row_num_;
	public int column_num_;
	
	public MatrixD(int m, int n) {
		this.m_ = new double[m][n];
		row_num_ = m;
		column_num_ = n;
	}	

	public MatrixD(MatrixD matrix) {
		row_num_ = matrix.row_num_;
		column_num_ = matrix.column_num_;
		m_ = matrix.m_.clone();
	}
	


	public VectorD column(int index) {
		VectorD vector = new VectorD(row_num_);
		for (int i = 0; i < row_num_; i++) {
			vector.v_[i] = m_[i][index]; 
		}
		return vector;
	}
	
	public VectorD row(int i) {
		return new VectorD(m_[i]);		
	}
	

	public static MatrixD diagonal(double d, int n) {
		MatrixD m = new MatrixD(n,n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				m.m_[i][j] = (i==j) ? d : 0;
			}			
		}
		return m;
	}

	public MatrixD minus(MatrixD matrix) {
		MatrixD m = new MatrixD(row_num_,column_num_);
		for (int i = 0; i < row_num_; i++) {
			for (int j = 0; j < column_num_; j++) {
				m.m_[i][j] = m_[i][j]-matrix.m_[i][j];
			}
		}
		return m;
	}

	
	public MatrixD multiply_scalar(double d) {
		MatrixD m = new MatrixD(row_num_,column_num_);
		for (int i = 0; i < row_num_; i++) {
			for (int j = 0; j < column_num_; j++) {
				m.m_[i][j] = m_[i][j]*d;
			}
		}
		return m;
	}
	
	public MatrixD times(MatrixD matrix) {
		MatrixD m = new MatrixD(row_num_,matrix.column_num_);
		for (int i = 0; i < m.row_num_; i++) {
			for (int j = 0; j < m.column_num_; j++) {
				m.m_[i][j] = 0;
				for (int k = 0; k < column_num_; k++)
					m.m_[i][j] = m.m_[i][j]+(m_[i][k]*matrix.m_[k][j]);
			}
		}
		return m;
	}
	
	public VectorD times(VectorD vector) {
		VectorD v = new VectorD(row_num_);
		for (int i = 0; i < row_num_; i++) {
			v.v_[i] = 0;
			for (int k = 0; k < column_num_; k++)
				v.v_[i] = v.v_[i]+m_[i][k]*vector.v_[k];
		}
		return v;
	}
	
	

	
	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < row_num_; i++) {
			s += row(i).toString()+"\n";
		}
		return s;
	}
	
	
	
	
	
}
