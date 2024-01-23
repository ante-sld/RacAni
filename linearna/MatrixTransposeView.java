package linearna;

public class MatrixTransposeView extends AbstractMatrix {
	private IMatrix m;
	public MatrixTransposeView(IMatrix m) {
		this.m = m;
	}
	@Override
	public int getRowsCount() {
		return this.m.getColsCount();
	}

	@Override
	public int getColsCount() {
		return this.m.getRowsCount();
	}

	@Override
	public double get(int i, int j) {
		return this.m.get(j, i);
	}

	@Override
	public IMatrix set(int i, int j, double value) {
		this.m.set(j, i, value);
		return this;
	}

	@Override
	public IMatrix copy() {
		IMatrix res = this.newInstance(this.getColsCount(), this.getRowsCount());
		for(int i = 0; i < this.getColsCount(); i++) {
			for(int j = 0; j < this.getRowsCount(); j++) {
				res.set(i, j, this.get(i, j));
			}
		}
		
		return res;
		
	}

	@Override
	public IMatrix newInstance(int i, int j) {
		return new MatrixTransposeView(new Matrix(i, j));
	}

}
