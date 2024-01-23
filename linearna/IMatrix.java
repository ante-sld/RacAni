package linearna;

public interface IMatrix {

	public int getRowsCount();
	public int getColsCount();
	public double get(int i, int j);
	public IMatrix set(int i, int j, double value);
	public IMatrix copy();
	public IMatrix newInstance(int i, int j);
	public IMatrix nTranspose(boolean flag);
	public IMatrix add(IMatrix m) throws IncompatibleOperandException;
	public IMatrix nAdd(IMatrix m) throws IncompatibleOperandException;
	public IMatrix sub(IMatrix m) throws IncompatibleOperandException;
	public IMatrix nSub(IMatrix m) throws IncompatibleOperandException;
	public IMatrix nMultiply(IMatrix m) throws IncompatibleOperandException;
	public double determinant() throws IncompatibleOperandException;
	public IMatrix subMatrix(int i, int j, boolean flag);
	public IMatrix nInvert() throws IncompatibleOperandException;
	public double[][] toArray();
	public IVector toVector(boolean flag );
}
