package linearna;

public class MatrixVectorView extends AbstractMatrix {
	private boolean asRowMatrix;
	private IVector v;
	
	public MatrixVectorView(IVector v, boolean flag) {
		this.asRowMatrix = flag;
		this.v = v;
	}
	
	@Override
	public int getRowsCount() {
		if(this.asRowMatrix) {
			return 1;
		} else {
			return this.v.getDimension();
		}
	}

	@Override
	public int getColsCount() {
		if(this.asRowMatrix) {
			return this.v.getDimension();
		} else {
			return 1;
		}
	}

	@Override
	public double get(int i, int j) {
		if(this.asRowMatrix) {
			return this.v.get(j);
		} else { 
			return this.v.get(i);
		}
	}

	@Override
	public IMatrix set(int i, int j, double value) {
		if(this.asRowMatrix) {
			this.v.set(j, value);
		} else {
			this.v.set(i, value);
		}
		
		return this;
	}

	@Override
	public IMatrix copy() {
		return new MatrixVectorView(this.v.copy(), this.asRowMatrix);
	}

	@Override
	public IMatrix newInstance(int i, int j) {
		if(this.asRowMatrix) {
			return new MatrixVectorView(this.v.newInstance(j), this.asRowMatrix);
		} else {
			return new MatrixVectorView(this.v.newInstance(i), this.asRowMatrix);
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	

}
