package linearna;

public interface IVector {
	
	public double get(int i);
	public IVector set(int i, double value);
	public int getDimension();
	public IVector copy();
	public IVector copyPart(int i);
	public IVector newInstance(int i);
	public IVector add(IVector v) throws IncompatibleOperandException;
	public IVector nAdd(IVector v) throws IncompatibleOperandException;
	public IVector sub(IVector v) throws IncompatibleOperandException;
	public IVector nSub(IVector v) throws IncompatibleOperandException;
	public IVector scalarMultiply(double d);
	public IVector nScalarMultiPly(double d);
	public double norm();
	public IVector normalize();
	public double cosine(IVector v) throws IncompatibleOperandException;
	public double scalarProduct(IVector v);
	public IVector nVectorProduct(IVector v) throws IncompatibleOperandException;
	public IVector nFormHomogeneus();
	public IMatrix toRowMatrix();
	public IMatrix toColumnMatrix();
	public double[] toArray();
}
