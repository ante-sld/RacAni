package linearna;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IncompatibleOperandException {
		if(args[0].equals("1")) {
			IVector vTemp1 = Vector.parseSimple("2 3 -4");
			IVector vTemp2 = Vector.parseSimple("-1 4 -3");
			IVector v1 = vTemp1.nAdd(vTemp2);
			System.out.println("V1: ");
			System.out.println(v1);
			
			double s = v1.scalarProduct(vTemp2);
			System.out.println("s: " + s);
			
			IVector v2 = v1.nVectorProduct(Vector.parseSimple("2 2 4"));
			System.out.println("V2:");
			System.out.println(v2);
			
			IVector v3 = v2.normalize();
			System.out.println("V3");
			System.out.println(v3);
			
			IVector v4 = v2.scalarMultiply(-1.0);
			System.out.println("v4");
			System.out.println(v4);
			
			IMatrix mTemp1 = Matrix.parseSimple("1 2 3 | 2 1 3 | 4 5 1");
			IMatrix mTemp2 = Matrix.parseSimple("-1 2 -3 | 5 -2 7 | -4 -1 3");
			IMatrix m1 = mTemp1.nAdd(mTemp2);
			System.out.println("M1:");
			System.out.println(m1);
			
			IMatrix m2 = mTemp1.nMultiply(mTemp2.nTranspose(false));
			System.out.println("M2");
			System.out.println(m2);
			
			IMatrix m3 = Matrix.parseSimple("-24 18 5 | 20 -15 -4 | -5 4 1").nInvert().nMultiply(mTemp1.nInvert());
			
			System.out.println("M3");
			System.out.println(m3);
			
		} else if(args[0].equals("2")) {
			Scanner sc = new Scanner(System.in);
			String input = sc.nextLine();
			String[] equations = input.split(",");
			double[][] resMatrix = new double[equations.length / 4][3];
			double[] resVector = new double[equations.length / 4];
			for(int i = 0; i < equations.length; i = i + 4) {
				resMatrix[Math.floorDiv(i, 4)][0] = Double.parseDouble(equations[i]);
				resMatrix[Math.floorDiv(i, 4)][1] = Double.parseDouble(equations[i+1]);
				resMatrix[Math.floorDiv(i, 4)][2] = Double.parseDouble(equations[i+2]);
				resVector[Math.floorDiv(i, 4)] = Double.parseDouble(equations[i+3]);
				
			}
			IMatrix m1 = new Matrix(equations.length / 4, 3, resMatrix);
			IVector v1 = new Vector(resVector);
			System.out.println("M1:");
			System.out.println(m1);
			System.out.println("V1:");
			System.out.println(v1);
			IMatrix res = m1.nInvert().nMultiply(v1.toRowMatrix().nTranspose(false));
			System.out.println("Result:");
			System.out.println(res);
			
		} else if(args[0].equals("3")) {
			Scanner sc = new Scanner(System.in);
			String input;
			System.out.print("Vertex A: ");
			input = sc.nextLine();
			IVector a = Vector.parseSimple(input);
			
			System.out.println("Vertex B: ");
			input = sc.nextLine();
			IVector b = Vector.parseSimple(input);
			
			System.out.println("Vertex C: ");
			input = sc.nextLine();
			IVector c = Vector.parseSimple(input);
			
			System.out.println("Point T: ");
			input = sc.nextLine();
			IVector t = Vector.parseSimple(input);
			double pov = b.nSub(a).nVectorProduct(c.nSub(a)).norm() / 2.0;
			double povA = b.nSub(t).nVectorProduct(c.nSub(t)).norm() / 2.0;
			double povB = a.nSub(t).nVectorProduct(c.nSub(t)).norm() / 2.0;
			double povC = a.nSub(t).nVectorProduct(b.nSub(t)).norm() / 2.0;
			double t1 = povA / pov;
			double t2 = povB / pov;
			double t3 = povC / pov;
			
			System.out.println("Baricentricne koordinate su: ("+t1+" "+t2+" "+t3+")");
		}
		/*
		IVector a = Vector.parseSimple("1 0 0");
		IVector b = Vector.parseSimple("5 0 0");
		IVector c = Vector.parseSimple("3 8 0");
		
		IVector t = Vector.parseSimple("3 4 0");
		double pov = b.nSub(a).nVectorProduct(c.nSub(a)).norm() / 2.0;
		double povA = b.nSub(t).nVectorProduct(c.nSub(t)).norm() / 2.0;
		double povB = a.nSub(t).nVectorProduct(c.nSub(t)).norm() / 2.0;
		double povC = a.nSub(t).nVectorProduct(b.nSub(t)).norm() / 2.0;
		double t1 = povA / pov;
		double t2 = povB / pov;
		double t3 = povC / pov;
		
		System.out.println("Baricentricne koordinate su: ("+t1+" "+t2+" "+t3+")");
		*/
		
		/*
		 * Test transponiranja
		 * 
		IMatrix m1 = Matrix.parseSimple("1 2 3 | 4 5 6");
		IMatrix m2 = m1.nTranspose(true);
		System.out.println("m1:");
		System.out.println(m1.toString());
		System.out.println("m2:");
		System.out.println(m2.toString());
		System.out.println();
		
		m2.set(2, 1, 9);
		System.out.println("m1:");
		System.out.println(m1.toString());
		System.out.println("m2:");
		System.out.println(m2.toString());
		*/
		
		/*
		 * Test submatrica
		 *  
		IMatrix m1 = Matrix.parseSimple("1 2 3 | 4 5 6");
		IMatrix m2 = m1.subMatrix(0, 1, false);
		System.out.println("m1:");
		System.out.println(m1.toString());
		System.out.println("m2:");
		System.out.println(m2.toString());
		System.out.println();
		
		m2.set(0, 1, 9);
		System.out.println("m1:");
		System.out.println(m1.toString());
		System.out.println("m2:");
		System.out.println(m2.toString());
		
		IMatrix m1 = Matrix.parseSimple("1 10 3 | 4 5 6 | 7 8 9");
		System.out.println(m1.determinant());
		
		
		
		IMatrix a = Matrix.parseSimple("1 5 3 | 0 0 8 | 1 1 1");
		IMatrix r = Matrix.parseSimple("3 | 4 | 1");
		IMatrix v = a.nInvert().nMultiply(r);
		System.out.println(a.nInvert());
		System.out.println("Rjesenje sustava je:");
		System.out.println(v);
		*/ 
		
		
		
		
		
		
	}

}
