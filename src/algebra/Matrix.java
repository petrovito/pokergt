package algebra;

import java.util.ArrayList;
import java.util.Arrays;

import org.jscience.mathematics.number.Rational;

public class Matrix {
	
	public Rational[][] m_;
	public int row_num_;
	public int column_num_;
	public ArrayList<Integer> lin_independents = new ArrayList<Integer>();
	
	public Matrix(int m, int n) {
		this.m_ = new Rational[m][n];
		row_num_ = m;
		column_num_ = n;
	}	
	
	public Matrix(ArrayList<ArrayList<Rational>> rows) {
		m_ = new Rational[rows.size()][rows.get(0).size()];
		row_num_ = rows.size();
		column_num_ = rows.get(0).size();
		for (int i = 0; i < rows.size(); i++) {
			ArrayList<Rational> row = rows.get(i);
			for (int j = 0; j < row.size(); j++) {
				m_[i][j] = row.get(j);
			}
		}
	}
	
	public Matrix(Matrix matrix) {
		row_num_ = matrix.row_num_;
		column_num_ = matrix.column_num_;
		m_ = matrix.m_.clone();
	}
	
	public Matrix next(Matrix matrix) {
		Matrix m = new Matrix(row_num_,column_num_+matrix.column_num_);
		for (int i = 0; i < row_num_; i++) {
			int j = 0;
			for (; j < column_num_; j++) {
				m.m_[i][j] = m_[i][j];
			}
			for (; j < column_num_+matrix.column_num_; j++) {
				m.m_[i][j] = matrix.m_[i][j-column_num_];
			}
		}
		return m;
	}

	public Matrix under(Matrix matrix) {
		Matrix m = new Matrix(row_num_+matrix.row_num_,column_num_);
		int i = 0;
		for (; i < row_num_; i++) {			
			for (int j = 0; j < column_num_; j++) {
				m.m_[i][j] = m_[i][j];
			}
		}
		for (; i < row_num_+matrix.row_num_; i++) {			
			for (int j = 0; j < column_num_; j++) {
				m.m_[i][j] = matrix.m_[i-row_num_][j];
			}
		}
		return m;
	}
	
	
	public static Matrix unit(int n) {
		Matrix m = new Matrix(n,n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				m.m_[i][j] = (i==j) ? Rational.ONE : Rational.ZERO;
			}			
		}
		return m;
	}
	
	
	public static Matrix zero(int m, int n) {
		Matrix matrix = new Matrix(m,n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				matrix.m_[i][j] = Rational.ZERO;
			}			
		}
		return matrix;
	}


	public Vector column(int index) {
		Vector vector = new Vector(row_num_);
		for (int i = 0; i < row_num_; i++) {
			vector.v_[i] = m_[i][index]; 
		}
		return vector;
	}
	
	public Vector row(int i) {
		return new Vector(m_[i]);		
	}

	
	public Matrix elem_basis_transformation(int row, int col) {
		Matrix m = new Matrix(row_num_,column_num_);
		for (int j = 0; j < column_num_; j++) {
			if (j == col) {
				m.m_[row][j] = Rational.ONE;
				continue;
			}
			m.m_[row][j] = m_[row][j].divide(m_[row][col]); 
		}
		for (int i = 0; i < row_num_; i++) {
			if (i == row) continue;
			for (int j = 0; j < column_num_; j++) {
				if (j == col) {
					m.m_[i][j] = Rational.ZERO;
					continue;
				}
				m.m_[i][j] = m_[i][j].minus(m.m_[row][j].times(m_[i][col]));
			}
		}
		return m;
	}
	
	
	public int rank() {
		lin_independents = new ArrayList<Integer>();
		Matrix matrix = new Matrix(this);
		for (int i = 0; i < row_num_; i++) {
			for (int j = 0; j < column_num_; j++) {
				if (!matrix.m_[i][j].equals(Rational.ZERO)) {
					matrix = matrix.elem_basis_transformation(i, j);
					lin_independents.add(j);
					break;
				}
			}
		}
		return lin_independents.size();
	}
	
	
	
	private Matrix inverse_pivot(int row, int col) {
		Matrix m = new Matrix(row_num_,column_num_);
		for (int j = 0; j < column_num_; j++) {
			if (j == col) {
				m.m_[row][j] = m_[row][col].inverse();
				continue;
			}
			m.m_[row][j] = m_[row][j].divide(m_[row][col]); 
		}
		for (int i = 0; i < row_num_; i++) {
			if (i == row) continue;
			for (int j = 0; j < column_num_; j++) {
				if (j == col) {
					m.m_[i][j] = m.m_[row][col].times(m_[i][col]).opposite();
					continue;
				}
				m.m_[i][j] = m_[i][j].minus(m.m_[row][j].times(m_[i][col]));
			}
		}
		return m;		
	}
	
	
	
	public Matrix inverse() {
		lin_independents = new ArrayList<Integer>();
		Matrix matrix = new Matrix(this);
		for (int i = 0; i < row_num_; i++) {
			for (int j = 0; j < column_num_; j++) {
				if (! lin_independents.contains(j) &&!matrix.m_[i][j].equals(Rational.ZERO)) {
					matrix = matrix.inverse_pivot(i, j);
					lin_independents.add(j);
					break;
				}
				if (j+1==column_num_) 
					throw new ArithmeticException("not invertible");
			}
		}
		return matrix.permute(lin_independents);		
	}



	public Matrix permute(ArrayList<Integer> order) {
		Matrix m = new Matrix(row_num_, column_num_);
		for (int i = 0; i < row_num_; i++) {
			for (int j = 0; j < column_num_; j++) {
				m.m_[order.get(i)][order.get(j)] = m_[i][j];
			}
		}
		return m;
	}



	public Matrix times(Matrix matrix) {
		Matrix m = new Matrix(row_num_,matrix.column_num_);
		for (int i = 0; i < m.row_num_; i++) {
			for (int j = 0; j < m.column_num_; j++) {
				m.m_[i][j] = Rational.ZERO;
				for (int k = 0; k < column_num_; k++)
					m.m_[i][j] = m.m_[i][j].plus(m_[i][k].times(matrix.m_[k][j]));
			}
		}
		return m;
	}
	
	public Vector times(Vector vector) {
		Vector v = new Vector(row_num_);
		for (int i = 0; i < row_num_; i++) {
			v.v_[i] = Rational.ZERO;
			for (int k = 0; k < column_num_; k++)
				v.v_[i] = v.v_[i].plus(m_[i][k].times(vector.v_[k]));
		}
		return v;
	}



	public static Matrix row_permutation(ArrayList<Integer> columns) {
		int n = columns.size();
		Matrix m = new Matrix(n,n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				m.m_[i][j] = (columns.get(i)==j) ? Rational.ONE : Rational.ZERO;
			}			
		}
		return m;
	}


	public static Matrix column_permutation(ArrayList<Integer> columns) {
		int n = columns.size();
		Matrix m = new Matrix(n,n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				m.m_[i][j] = (columns.get(j)==i) ? Rational.ONE : Rational.ZERO;
			}			
		}
		return m;
	}
	

	
	public Matrix submatrix_rows(ArrayList<Integer> rows) {
		Matrix m = new Matrix(rows.size(),row_num_);
		for (int i = 0; i < column_num_; i++) {
			for (int j = 0; j < rows.size(); j++) {
				m.m_[i][j] = m_[rows.get(j)][i];
			}
		}
		return m;
	}

	public Matrix submatrix_columns(ArrayList<Integer> columns) {
		Matrix m = new Matrix(row_num_,columns.size());
		for (int i = 0; i < row_num_; i++) {
			for (int j = 0; j < columns.size(); j++) {
				m.m_[i][j] = m_[i][columns.get(j)];
			}
		}
		return m;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Matrix) {
			Matrix m = (Matrix) obj;
			if (! (row_num_ == m.row_num_ && column_num_ == m.column_num_)) return false;
			for (int i = 0; i < row_num_; i++) {
				//if (i>2) continue;
				if (!Arrays.equals(m.m_[i], m_[i])) return false;
				//if (!Arrays.asList(m_).contains(m.m_[i])) return false;
			}
			return true;
		}
		return super.equals(obj);
	}
	
	
}
