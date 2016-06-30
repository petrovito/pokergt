package algebra;

import java.util.ArrayList;

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



	public Vector column(int index) {
		Vector vector = new Vector(m_[0].length);
		for (int i = 0; i < m_[0].length; i++) {
			vector.v_[i] = m_[i][index]; 
		}
		return vector;
	}
	
	public Vector row(int i) {
		return new Vector(m_[i]);		
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
				if (!matrix.m_[i][j].equals(Rational.ZERO)) {
					matrix = matrix.inverse_pivot(i, j);
					lin_independents.add(j);
					break;
				}
				if (j+1==column_num_) 
					throw new ArrayIndexOutOfBoundsException("not invertible");
			}
		}
		return row_permutation(lin_independents).times(matrix).
				times(column_permutation(lin_independents));		
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
	
	
	

}
