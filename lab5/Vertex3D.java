package lab5;

import linearna.IVector;
import linearna.Vector;

public class Vertex3D {
	private double x;
	private double y;
	private double z;
	private IVector normal;
	
	
	public Vertex3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z; 
	}

	public double[] toDoubleArray() {
		return new double[] {x, y, z};
	}
	
	
	
	public IVector getNormal() {
		return normal;
	}

	public void setNormal(IVector normal) {
		this.normal = normal;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Vertex3D)) {
			return false;
		}
		Vertex3D v = (Vertex3D) obj;
		if(v.getX() == this.getX() && v.getY() == this.getY() && v.getZ() == this.getZ()) 
			return true;
		else
			return false;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	@Override
	public String toString() {
		return "v " + this.x + " " + this.y + " " + this.z;
	}
	
	public static Vertex3D[] norm(Vertex3D[] vertexes) {
		Vertex3D[] res = new Vertex3D[vertexes.length];
		double xmin = 1000, xmax = -1000;
		double ymin = 1000, ymax = -1000;
		double zmin = 1000, zmax = -1000;
		for(Vertex3D v : vertexes) {
			if(v.getX() < xmin) {
				xmin = v.getX();
			} else if(v.getX() > xmax) {
				xmax = v.getX();
			}
			
			if(v.getY() < ymin) {
				ymin = v.getY();
			} else if(v.getY() > ymax) {
				ymax = v.getY();
			}
			
			if(v.getZ() < zmin) {
				zmin = v.getZ();
			} else if(v.getZ() > zmax) {
				zmax = v.getZ();
			}
		}
//		System.out.println(xmax + " " + ymax + " " + zmax);
//		System.out.println(xmin + " " + ymin + " " + zmin);
		
		double xdelta = xmax - xmin;
		double ydelta = ymax - ymin;
		double zdelta = zmax - zmin;
		
		double max = 1;
		if(xdelta >= zdelta && xdelta >= ydelta) {
			max = xdelta;
		} else if(ydelta >= xdelta && ydelta >= zdelta) {
			max = ydelta;
		} else {
			max = zdelta;
		}
//		System.out.println("Max : " + max);
		
		double xmean = (xmax + xmin) / 2; 
//		System.out.println(xmean);
		double ymean = (ymax + ymin) / 2;
//		System.out.println(ymean);
		double zmean = (zmax + zmin) / 2;
//		System.out.println(zmean);
		for(int i = 0; i < vertexes.length; i++) {
			Vertex3D v = vertexes[i];
			res[i] = new Vertex3D((v.getX() - xmean) * 2 / max, (v.getY() - ymean) * 2 / max, (v.getZ() - zmean) * 2 / max);
		}
		
		return res;
	}
	
	public IVector toVector() {
		return Vector.parseSimple(this.getX() + " " + this.getY() + " " + this.getZ());
	}
	
}
