package linearna;

import java.util.Arrays;

public class Vector extends AbstractVector {
	private double[] elements;
	private int dimension;
	private boolean readOnly;
	
	public Vector(double[] array) {
		this.dimension = array.length;
		this.readOnly = false;
		this.elements = array;
	}
	
	@Override
	public double get(int i) {
		return this.elements[i];
	}

	@Override
	public IVector set(int i, double value) {
		this.elements[i] = value;
		return this;
	}

	@Override
	public int getDimension() {
		return this.dimension;
	}

	@Override
	public IVector copy() {
		double[] array = new double[this.dimension];
		for(int i = 0; i < array.length; i++) {
			array[i] = this.get(i);
		}
		return new Vector(array);
	}

	@Override
	public IVector newInstance(int i) {
//		System.out.println(i);
		return new Vector(new double[i]);
	}

	public static Vector parseSimple(String s) {
		String[] arStr = s.split(" ");
		double[] tmp = new double[arStr.length];
		for(int i = 0; i < arStr.length; i++) {
			tmp[i] = Double.parseDouble(arStr[i]);
		}
		return new Vector(tmp);
	}

	
	
	
	
}
