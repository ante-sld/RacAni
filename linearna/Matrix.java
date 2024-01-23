package linearna;

public class Matrix extends AbstractMatrix {
	double[][] elements;
	int rows;
	int cols;
	
	public Matrix(int i, int j) {
		this.rows = i;
		this.cols = j;
		this.elements = new double[i][j];
	}
	
	public Matrix(int i, int j, double[][] array) {
		this.rows = i;
		this.cols = j;
		this.elements = array;
	}
	
	@Override
	public IMatrix newInstance(int i, int j) {
//		System.out.println(" i: " + i + " j: " + j);
		return new Matrix(i, j);
	}

	@Override
	public int getRowsCount() {
		return this.rows;
	}

	@Override
	public int getColsCount() {
		return this.cols;
	}

	@Override
	public double get(int i, int j) {
		return this.elements[i][j];
	}

	@Override
	public IMatrix set(int i, int j, double value) {
		this.elements[i][j] = value;
		return this;
	}

	@Override
	public IMatrix copy() {
		double[][] tmp = new double[this.rows][this.cols];
		for(int i = 0; i < this.rows; i++) {
			for(int j = 0; j < this.cols; j++) {
				tmp[i][j] = this.elements[i][j];
			}
		}
		return new Matrix(this.rows, this.cols, tmp);
	}

	public static IMatrix parseSimple(String s) {
		String[] rows = s.split("\\|");
		double[][] matrix = new double[rows.length][rows[0].split(" ").length];
		for(int i = 0; i < rows.length; i++) {
			for(int j = 0; j < rows[i].strip().split(" ").length; j++) {			
				matrix[i][j] = Double.parseDouble(rows[i].strip().split(" ")[j]);
			}
		}
		return new Matrix(rows.length, rows[0].split(" ").length, matrix);
		
		
	}
}
