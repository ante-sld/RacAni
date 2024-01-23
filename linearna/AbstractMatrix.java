package linearna;

public abstract class AbstractMatrix implements IMatrix {

	public AbstractMatrix() {}
	public abstract int getRowsCount();
	public abstract int getColsCount();
	public abstract double get(int i, int j);
	public abstract IMatrix set(int i, int j, double value);
	public abstract IMatrix copy();
	public abstract IMatrix newInstance(int i, int j);
	
	@Override
	public IMatrix nTranspose(boolean flag) {
		if(flag) {
			return new MatrixTransposeView(this);
		}
		IMatrix res = new Matrix(this.getColsCount(), this.getRowsCount());
		for(int i = 0; i < this.getColsCount(); i++) {
			for(int j = 0; j < this.getRowsCount(); j++) {
				res.set(i, j, this.get(j, i));
			}
		}
		return res;
	}
	@Override
	public IMatrix add(IMatrix m) throws IncompatibleOperandException {
		if(this.getRowsCount() != m.getRowsCount() || this.getColsCount() != m.getColsCount()) {
			throw new IncompatibleOperandException();
		}
		for(int i = 0; i < this.getRowsCount(); i++) {
			for(int j = 0; j < this.getColsCount(); j++) {
				this.set(i, j, this.get(i, j) + m.get(i, j));
			}
		}
		return this;
	}
	@Override
	public IMatrix nAdd(IMatrix m) throws IncompatibleOperandException {
		return this.copy().add(m);
	}
	@Override
	public IMatrix sub(IMatrix m) throws IncompatibleOperandException {
		if(this.getRowsCount() != m.getRowsCount() || this.getColsCount() != m.getColsCount()) {
			throw new IncompatibleOperandException();
		}
		for(int i = 0; i < this.getRowsCount(); i++) {
			for(int j = 0; j < this.getColsCount(); j++) {
				this.set(i, j, this.get(i, j) - m.get(i, j));
			}
		}
		return this;
	}
	@Override
	public IMatrix nSub(IMatrix m) throws IncompatibleOperandException {
		return this.copy().sub(m);
	}
	@Override
	public IMatrix nMultiply(IMatrix m) throws IncompatibleOperandException {
		if(this.getColsCount() != m.getRowsCount()) {
			throw new IncompatibleOperandException();
		}
		double[][] tmp = new double[this.getRowsCount()][m.getColsCount()];
		for(int i = 0; i < this.getRowsCount(); i++) {
			for(int j = 0; j < m.getColsCount(); j++) {
				tmp[i][j] = 0;
				for(int k = 0; k < this.getColsCount(); k++) {
					tmp[i][j] += this.get(i, k) * m.get(k, j);
				}
			}
		}
		
		return new Matrix(this.getRowsCount(), m.getColsCount(), tmp);
	}
	@Override
	public double determinant() throws IncompatibleOperandException {
		if(this.getRowsCount() == 1 && this.getColsCount() == 1) {
			return this.get(0, 0);
		} else if(this.getRowsCount() == 2 && this.getColsCount() == 2) {
			return this.get(0, 0) * this.get(1, 1) - this.get(0, 1) * this.get(1, 0);
		} else if(this.getRowsCount() == this.getColsCount()) {
			double det = 0;
	        for (int i = 0; i < this.getColsCount(); i++)
	            det += Math.pow(-1, i) * this.get(0, i)
	                    * this.subMatrix(0, i, false).determinant();
	        return det;
		} else {
			throw new IncompatibleOperandException();
		}
	}
	@Override
	public IMatrix subMatrix(int i, int j, boolean flag) {
		if(flag) {
			return new MatrixSubMatrixView(this, i, j);
		}
		double[][] res = new double[this.getRowsCount()-1][this.getColsCount()-1];
		for(int k = 0; k < this.getRowsCount()-1; k++) {
			for(int m = 0; m < this.getColsCount()-1; m++) {
				if(k >= i && m >= j) {
					res[k][m] = this.get(k + 1, m + 1);
				} else if(k >= i) {
					res[k][m] = this.get(k + 1, m);
				} else if(m >= j) {
					res[k][m] = this.get(k,  m + 1);
				} else {
					res[k][m] = this.get(k, m);
				}
			}
		}
		
		
		return new Matrix(this.getRowsCount()-1, this.getColsCount()-1, res);
	}
	@Override
	public IMatrix nInvert() throws IncompatibleOperandException {
		double[][] inverse = new double[this.getRowsCount()][this.getColsCount()];

        for (int i = 0; i < this.getRowsCount(); i++)
            for (int j = 0; j < this.getColsCount(); j++)
                inverse[i][j] = Math.pow(-1, i + j)
                        * this.subMatrix(i, j, false).determinant();   

        double det = 1.0 / this.determinant();
        for (int i = 0; i < inverse.length; i++) {
            for (int j = 0; j <= i; j++) {
                double temp = inverse[i][j];
                inverse[i][j] = inverse[j][i] * det;
                inverse[j][i] = temp * det;
            }
        }

        return new Matrix(this.getRowsCount(), this.getColsCount(), inverse);
	}
	@Override
	public double[][] toArray() {
		double[][] res = new double[this.getRowsCount()][this.getColsCount()];
		for(int i = 0; i < this.getRowsCount(); i++) {
			for(int j = 0; j < this.getColsCount(); j ++) {
				res[i][j] = this.get(i, j);
			}
		}
		return res;
	}
	@Override
	public IVector toVector(boolean flag) {
		return new VectorMatrixView(this, flag);
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < this.getRowsCount(); i++) {
			sb.append("[");
			for(int j = 0; j < this.getColsCount(); j++) {
				sb.append( " " + this.get(i, j)   + " ");
			}
			sb.append("]\n");
		}
		return sb.toString();
	}
	
	
	
}
