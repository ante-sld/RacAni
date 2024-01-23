package linearna;

import lab5.Vertex3D;

public class IRG {
	
	static public IMatrix translate3D(float dx, float dy, float dz) {
		IMatrix result = Matrix.parseSimple("1 0 0 0 | 0 1 0 0 | 0 0 1 0 | " + dx + " " + dy + " " + dz + " 1");
		return result; 
	}
	
	static public IMatrix scale3D(float sx, float sy, float sz) {
		IMatrix result = Matrix.parseSimple(sx + " 0 0 0 | 0 " + sy + " 0 0 | 0 0 " + sz + " 0 | 0 0 0 1");
		return result; 
	}
	
	static public IMatrix lookAtMatrix(IVector eye, IVector center, IVector viewUp) throws IncompatibleOperandException {
		IVector F = center.nSub(eye);		
		F.normalize();
		IVector s = F.nVectorProduct(viewUp);
		s.normalize();
		IVector u = s.nVectorProduct(F).normalize();
//		IMatrix result = Matrix.parseSimple(s.get(0) + " " + s.get(1) + " " + s.get(2) + " 0 | "  //transponirana
//				+ u.get(0) + " " + u.get(1) + " " + u.get(2) + " 0 | "
//				+ (-F.get(0)) + " " + (-F.get(1)) + " " + (-F.get(2)) + " 0 | "
//				+ "0 0 0 1");
		IMatrix result = Matrix.parseSimple(s.get(0) + " " + u.get(0) + " " + (-F.get(0)) + " 0 | "
											+ s.get(1) + " " + u.get(1) + " " + (-F.get(1)) + " 0 | "
											+ s.get(2) + " " + u.get(2) + " " + (-F.get(2)) + " 0 | "
											+ "0 0 0 1");
		return result;
	}
	
	static public IMatrix buildFrustumMatrix(double l, double r, double b, double t, int n, int f) {
//		IMatrix result = Matrix.parseSimple(((2*n)/(r-l)) + " 0 " + (r+l)/(r-l) + " 0 |"
//										+ "	0 " + 2*n/(t-b) + " " + (t+b)/(t-b) + " 0 |"
//										+ " 0 0 " + (-(f+n)/(f-n)) + " " + -2*f*n/(f-n) + " |"
//										+ " 0 0 -1 0");
		IMatrix result = Matrix.parseSimple((2*n)/(r-l) + " 0 0 0 |" +
											" 0 " + (2*n)/(t-b) + " 0 0 |" +
											(r+l)/(r-l) + " " + (t+b)/(t-b) + " " + (-(f+n)/(f-n)) + " -1 |" +
											" 0 0 " + (-2*(f*n)/(f-n)) + " 0");	
		return result;
	}
	
	static public boolean isAntiClockwise(Vertex3D[] v) throws IncompatibleOperandException {
		if(v.length != 3) {
			return false;
		}
//		double area = v[0].getX() * v[1].getY() - v[1].getX() * v[0].getY() + 
//						v[1].getX() * v[2].getY() - v[2].getX() * v[1].getY() + 
//						v[2].getX() * v[0].getY() - v[0].getX() * v[2].getY();
		
//		System.out.println(v[0]);
//		System.out.println(v[1]);
//		System.out.println(v[2]);
		
		IMatrix m = Matrix.parseSimple(v[0].getX() + " " + v[0].getY() + " 1 | " + 
										v[1].getX() + " " + v[1].getY() + " 1 | " + 
										v[2].getX() + " " + v[2].getY() + " 1 ");
		boolean result = m.determinant() >= 0;
		return result;
	}
}
