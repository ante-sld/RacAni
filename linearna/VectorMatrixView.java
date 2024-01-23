package linearna;

public class VectorMatrixView extends AbstractVector {
	private boolean rowMatrix;
	private int dimension;
	private IMatrix m;
	
	public VectorMatrixView(IMatrix m, boolean flag) {
		if(flag) {
			this.dimension = m.getRowsCount();
		} else {
			this.dimension = m.getColsCount();
		}
		this.rowMatrix = flag;
//		System.out.println(rowMatrix);
		this.m = m;
	}

	@Override
	public double get(int i) {
		if(!this.rowMatrix) 
			return this.m.get(0, i);
		else 
			return this.m.get(i, 0);
	}

	@Override
	public IVector set(int i, double value) {
//		System.out.println(this.dimension + " " + this.rowMatrix + " " + this.m.getColsCount() + " " + this.m.getRowsCount());
		if(!this.rowMatrix) {
			this.m.set(0, i, value);
		} else {
			this.m.set(i, 0, value);
		}
		
		return this;
			
	}

	@Override
	public int getDimension() {
		return this.dimension;
	}

	@Override
	public IVector copy() {
		return new VectorMatrixView(this.m.copy(), this.rowMatrix);
	}

	@Override
	public IVector newInstance(int i) {
//		System.out.println(this.rowMatrix);
		if(!this.rowMatrix) {
			return new VectorMatrixView(this.m.newInstance(1, i), this.rowMatrix);
		} else {
			return new VectorMatrixView(this.m.newInstance(i, 1), this.rowMatrix);
		}
		
	}
	
	
	
}
