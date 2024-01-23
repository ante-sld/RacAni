package lab5;

import java.util.ArrayList;

import linearna.IVector;
import linearna.IncompatibleOperandException;
import linearna.Vector;

public class ObjectModel {
	private Vertex3D[] vertexes;
	private Face3D[] faces;
	
	public ObjectModel(Vertex3D[] v, Face3D[] f) {
		this.vertexes = v;
		this.faces = f;
	}
	
	public ObjectModel copy() {
		return new ObjectModel(this.vertexes, this.faces);
	}
	
	public IVector calculateVertexNormals(Vertex3D v) {
		ArrayList<Face3D> sharedFaces = new ArrayList<Face3D>();
		int ind = -1;
		for(int i = 0; i < this.vertexes.length; i++) {
			if(this.vertexes[i].equals(v)) {
				ind = i + 1;
				break;
			}
		}
//		System.out.println("Vertex index : " + ind);
		
		for(Face3D f : this.faces) {
			int[] indexes = f.getIndexes();
			if(indexes[0] == ind || indexes[1] == ind || indexes[2] == ind)
				sharedFaces.add(f);
		}
//		System.out.println("Shared faces: ");
//		for(Face3D f : sharedFaces) {
//			
//			System.out.println(f);
//		}
		
		IVector n = Vector.parseSimple("0 0 0");
		for(Face3D f : sharedFaces) {
			IVector faceN = new Vector(f.getCoef()).normalize();
//			System.out.println("Face coef: " + faceN);
			try {
				n.add(Vector.parseSimple(faceN.get(0) + " " + faceN.get(1) + " " + faceN.get(2)));
			} catch (IncompatibleOperandException e) {
				System.out.println("Ne ide add");
			}
		}
//		System.out.println(this.vertexes[ind-1]);
//		System.out.println("Normal : "  + n.normalize());
//		System.out.println(sharedFaces.size());
		double s = 1.0 / sharedFaces.size();
//		System.out.println(s);
		n.scalarMultiply(s);
		return n.normalize();
//		this.vertexes[ind - 1].setNormal(n.normalize());
//		System.out.println("new normal: " + this.vertexes[ind-1].getNormal());
	}
	
	
	public String checkPoint(Vertex3D vertex) {
		String[] s = new String[faces.length];
		int over = 0, under = 0, on = 0;
		for(int i = 0; i < faces.length; i++) {
			String tmpRes = faces[i].pointOrientation(vertex);
			if(tmpRes.equals("over")) {
				over++;
			} else if(tmpRes.equals("under")) {
				under++;
			} else if(tmpRes.equals("on")) {
				on++;
			}
		}
//		System.out.println("on: " + on + " over: " + over  + " under: "+ under);
		if(over > 0) {			
			return "out";
		} else if (on != 0) {
			return "on";
		} else {
			return "in";
		}
	}
	public String dumpToOBJ() {
		StringBuilder sb = new StringBuilder();
		for(Vertex3D v : this.vertexes) {
			sb.append(v + "\n");
		}
		sb.append("\n");
		for(Face3D f : this.faces) {
			sb.append(f + "\n");
		}
		return sb.toString();
	}

	public Vertex3D[] getVertexes() {
		return vertexes;
	}

	public Face3D[] getFaces() {
		return faces;
	}
	
	public void determineFaceVisibilities1(IVector eye) {
		//algoritam 1
		
		for(Face3D f : this.faces) {
			double[] coef = f.getCoef();
			double result = coef[0] * eye.get(0) + coef[1] * eye.get(1) + coef[2] * eye.get(2) + coef[3];
			if(result > 0) {
				f.setVisible(true);
			} else {
				f.setVisible(false);
			}
		}
		
		return;
	}
	
	public void determineFaceVisibilities2(IVector eye) throws IncompatibleOperandException {
		//algoritam 2
		for(Face3D f : this.faces) {
			int[] indexes = f.getIndexes();
			double[] coef = f.getCoef();
			
			IVector v0 = Vector.parseSimple(this.vertexes[indexes[0] - 1].getX() + " " +  this.vertexes[indexes[0] - 1].getY() + " " + this.vertexes[indexes[0] - 1].getZ());
			IVector v1 = Vector.parseSimple(this.vertexes[indexes[1] - 1].getX() + " " +  this.vertexes[indexes[1] - 1].getY() + " " + this.vertexes[indexes[1] - 1].getZ());
			IVector v2 = Vector.parseSimple(this.vertexes[indexes[2] - 1].getX() + " " +  this.vertexes[indexes[2] - 1].getY() + " " + this.vertexes[indexes[2] - 1].getZ());
			
			IVector c = v0.nAdd(v1).nAdd(v2).nScalarMultiPly(1/3);
			IVector e = eye.nSub(c);
			IVector n = Vector.parseSimple(coef[0] + " " + coef[1] + " " + coef[2]);
			double result = n.scalarProduct(e);
			if(result > 0) {
				f.setVisible(true);
			} else {
				f.setVisible(false);
			}
		}
		
		return;
	}
	
	
}
