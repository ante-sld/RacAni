package lab5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import linearna.*;

public class Face3D {
	private int indexes[];
	private double a, b, c, d;
	private boolean visible;
	
	public Face3D(int[] indexes) {
		this.indexes = indexes;
		this.visible = true;
	}

	
	
	public void calculateCoef(List<Vertex3D> vertexes) throws IncompatibleOperandException {
		IVector[] vi  = new Vector[3];
		for(int i = 0; i < 3; i++) {
			vi[i] = new Vector(vertexes.get(indexes[i] - 1).toDoubleArray());
//			System.out.println(vertexes.get(indexes[i] - 1));
		}
		
		IVector v1 = vi[1].nSub(vi[0]);
//		System.out.println("Prvi izracun: " + vi[1] + "  -  " + vi[0] + "  =  " + v1);
		IVector v2 = vi[2].nSub(vi[0]);
//		System.out.println("Drugi izracun: " + vi[2] + "  -  " + vi[0] + "  =  " + v2);
		IVector n = v1.nVectorProduct(v2);
//		System.out.println("Treci izracun: " + v1 + "  x  " + v2 + "  =  " + n);
		this.a = n.get(0);
		this.b = n.get(1);
		this.c = n.get(2);
		IVector abc = Vector.parseSimple(a + " " + b + " " + c);
		this.d = abc.scalarProduct(vi[0]) * -1;
//		System.out.println("Final: " + a + " " + b + " " + c  + " " + d);
//		System.exit(0);
	}

	public String pointOrientation(Vertex3D vertex) {
		IVector v = new Vector(vertex.toDoubleArray());
		IVector abc = Vector.parseSimple(this.a + " " + this.b + " " + this.c);
//		System.out.println(v);
//		System.out.println(abc + " " +  this.d);
		int over = 0, under = 0, on = 0;
		double r = v.scalarProduct(abc) + this.d;
//		System.out.println("R = " + r );
		if(r > 0)
			return "over";
		else if( r < 0) 
			return "under";
		else if(r == 0)
			return "on";
		return "";
	}

	public double[] getCoef() {
		return new double[] {a, b, c, d};
	}
	
	public int[] getIndexes() {
		return indexes;
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("f ");
		for(int i : indexes) {
			sb.append(i + " ");
		}
		sb.delete(sb.length()-1, sb.length());
		return sb.toString();
	}
	
	
}
