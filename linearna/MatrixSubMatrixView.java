package linearna;

public class MatrixSubMatrixView extends AbstractMatrix {
	private int[] rowIndexes;
	private int[] colIndexes;
	private IMatrix m;
	
	public MatrixSubMatrixView(IMatrix m, int i, int j) {
		this.rowIndexes = new int[m.getRowsCount()-1];
		for(int c_i = 0; c_i < m.getRowsCount()-1; c_i++) {
			if(c_i >= i) {
				this.rowIndexes[c_i] = c_i + 1;
			} else {
				this.rowIndexes[c_i] = c_i;
			}
			
		}
		
		this.colIndexes = new int[m.getColsCount()-1];
		for(int c_j = 0; c_j < m.getColsCount()-1; c_j++) {
			if(c_j >= j) {
				this.colIndexes[c_j] = c_j + 1;
			} else {
				this.colIndexes[c_j] = c_j;
			}
			
		}
		this.m = m;
		
	}
	@Override
	public int getRowsCount() {
		return this.rowIndexes.length;
	}

	@Override
	public int getColsCount() {
		return this.colIndexes.length;
	}

	@Override
	public double get(int i, int j) {
		return this.m.get(this.rowIndexes[i], this.colIndexes[j]);
	}

	@Override
	public IMatrix set(int i, int j, double value) {
		this.m.set(this.rowIndexes[i], this.colIndexes[j], value);
		return this;
	}

	@Override
	public IMatrix copy() {
		// TODO Auto-generated method stub
		return new MatrixSubMatrixView(this.m.copy(), this.getRowsCount(), this.getColsCount());
	}

	@Override
	public IMatrix newInstance(int i, int j) {
		return new MatrixSubMatrixView(this.m.newInstance(this.getRowsCount(), this.getColsCount()), this.getRowsCount(), this.getColsCount());
	}

}
