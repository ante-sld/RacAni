package linearna;

import java.math.BigDecimal;

public abstract class AbstractVector implements IVector {

	public AbstractVector() {}
	abstract public double get(int i);
	abstract public IVector set(int i, double value);
	abstract public int getDimension();
	abstract public IVector copy();
	abstract public IVector newInstance(int i);
	
	@Override
	public IVector copyPart(int i) {
//		System.out.println("i " + i);
//		System.out.println(this.getClass());
		IVector v = this.newInstance(i);
//		System.out.println(v.getDimension());
//		System.out.println("dim " + this.getDimension());
		if(i > this.getDimension()) {
			int j = 0;
			while(j <= this.getDimension()) {
				v.set(j, this.get(j));
				j++;
			}
			while(j < i - 1) {
				v.set(j, 0);
				j++;
			}
		} else {
			for(int j = 0; j < i; j++) {
//				System.out.println("settam " + j + " " + this.get(j));
				
				v.set(j, this.get(j));
			}
		}
		return v;
	}
	
	@Override
	public IVector add(IVector v) throws IncompatibleOperandException {
		if(this.getDimension() != v.getDimension()) {
			throw new IncompatibleOperandException();
		}
		for (int i = this.getDimension() - 1; i >= 0; i--) {
			this.set(i, this.get(i) + v.get(i));
		}
		
		return this;
	}
	
	@Override
	public IVector nAdd(IVector v) throws IncompatibleOperandException {
		return this.copy().add(v);
	}
	
	@Override
	public IVector sub(IVector v) throws IncompatibleOperandException {
		if(this.getDimension() != v.getDimension()) {
			throw new IncompatibleOperandException();
		}
		for (int i = this.getDimension() - 1; i >= 0; i--) {
			this.set(i, this.get(i) - v.get(i));
		}
		
		return this;
	}
	
	@Override
	public IVector nSub(IVector v) throws IncompatibleOperandException {
		return this.copy().sub(v);
	}
	
	@Override
	public IVector scalarMultiply(double d) {
//		System.out.println("d : " + d);
		for(int i = this.getDimension() - 1; i >= 0; i--) {
			this.set(i, this.get(i) * d);
		}
		return this;
	}
	
	@Override
	public IVector nScalarMultiPly(double d) {
		return this.copy().scalarMultiply(d);
	}
	
	@Override
	public double norm() {
		double sum = 0;
		for(int i = this.getDimension() - 1; i >= 0; i--) {
			sum += Math.pow(this.get(i), 2);
		}	
		if(sum == 0) 
			return 1;
		return Math.sqrt(sum);
	}
	
	@Override
	public IVector normalize() {
		double norm = this.norm();
//		System.out.println("norm :" + norm);
		for(int i = this.getDimension() - 1; i >= 0; i--) {
//			System.out.println(i + " " + this.get(i) + " " + this.get(i) / norm);
			this.set(i, this.get(i) / norm);
		}
		return this;
	}
	
	@Override
	public double cosine(IVector v) throws IncompatibleOperandException {
		if(this.getDimension() != v.getDimension()) {
			throw new IncompatibleOperandException();
		}
		return this.scalarProduct(v) / (this.norm() * v.norm());
	}
	
	@Override
	public double scalarProduct(IVector v) {
		double sum = 0;
		for(int i = 0; i < this.getDimension(); i++) {
			sum += (this.get(i) * v.get(i));
		}
		return sum;
	}
	@Override
	public IVector nVectorProduct(IVector v) throws IncompatibleOperandException {
		if(this.getDimension() != v.getDimension()) {
			throw new IncompatibleOperandException();
		}
		if(this.getDimension() != 3) {
			throw new IllegalArgumentException("Wrong size");
		}
		
		BigDecimal bd11 = BigDecimal.valueOf(this.get(1) * v.get(2));
		BigDecimal bd12 = BigDecimal.valueOf(this.get(2) * v.get(1));
		
		BigDecimal bd21 = BigDecimal.valueOf(this.get(2) * v.get(0));
		BigDecimal bd22 = BigDecimal.valueOf(this.get(0) * v.get(2));
		
		BigDecimal bd31 = BigDecimal.valueOf(this.get(0) * v.get(1));
		BigDecimal bd32 = BigDecimal.valueOf(this.get(1) * v.get(0));
		
//		System.out.println("Big decimal 1: " + (bd11.subtract(bd12)));
//		System.out.println("Big decimal 2: " + (bd21.subtract(bd22)));
//		System.out.println("Big decimal 3: " + (bd31.subtract(bd32)));
		
		IVector ret = this.newInstance(3);
//		System.out.println(this.get(1) + " " + v.get(2) + " " + this.get(2) + " " +  v.get(1));
		ret.set(0, bd11.doubleValue() - bd12.doubleValue());
		ret.set(1, bd21.doubleValue() - bd22.doubleValue());
		ret.set(2, bd31.doubleValue() - bd32.doubleValue());
		return ret;
	}
	@Override
	public IVector nFormHomogeneus() {
//		System.out.println(this + " " + this.getClass());
		double homVal = this.get(this.getDimension()-1);
//		System.out.println("homogen value: " + homVal);
//		System.out.println("dimension: " + (this.getDimension()-1));
		IVector workVect = this.copyPart(this.getDimension()-1);
//		System.out.println("work vect: " + workVect);
//		System.out.println("1 / homVal: " + (1/homVal));
		IVector result = workVect.scalarMultiply(1/homVal);
//		System.out.println("work vect after scalar multiply:  " + result);
		return result;
	}
	
	@Override
	public IMatrix toRowMatrix() {
		return new MatrixVectorView(this, true);
	}
	@Override
	public IMatrix toColumnMatrix() {

		return new MatrixVectorView(this, false);
	}
	
	@Override
	public double[] toArray() {
		double[] res = new double[this.getDimension()];
		for(int i = 0; i < this.getDimension(); i++) {
			res[i] = this.get(i);
		}
		return res;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(int i = 0; i < this.getDimension(); i++) {
			sb.append(" " + String.valueOf(this.get(i)));
		}
		sb.append("]");
		return sb.toString();
	}
	
	 
}
