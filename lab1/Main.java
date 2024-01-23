package racani.lab1;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.ObjLongConsumer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import lab5.Face3D;
import lab5.ObjectModel;
import lab5.Vertex3D;
import linearna.IVector;
import linearna.IncompatibleOperandException;
import linearna.Vector;

public class Main {
	
	static {
		GLProfile.initSingleton();
	}

	public static void main(String[] args) throws Exception {
		ArrayList<Vertex3D> vertexes = new ArrayList<Vertex3D>();
		ArrayList<Face3D> faces = new ArrayList<Face3D>();
		Scanner sc = new Scanner(Paths.get(args[0]));
		while(sc.hasNext()) {
			String[] row = sc.nextLine().strip().split(" ");
			if(row[0].equals("v")) {
				vertexes.add(new Vertex3D(Double.parseDouble(row[1]), Double.parseDouble(row[2]), Double.parseDouble(row[3])));
			} else if(row[0].equals("f")) {
				faces.add(new Face3D(new int[] {(int)Math.round(Double.parseDouble(row[1])), (int)Math.round(Double.parseDouble(row[2])), (int)Math.round(Double.parseDouble(row[3]))}));
			}
			
		}
		
		
		Vertex3D[] vertexesArray = new Vertex3D[vertexes.size()];
		for(int i = 0; i < vertexes.size(); i++) {
			vertexesArray[i] = vertexes.get(i);
		}
		
		Vertex3D[] normVertexesArray = Vertex3D.norm(vertexesArray);
		
		for(Face3D f : faces) {
			f.calculateCoef(Arrays.asList(normVertexesArray));
//			double[] coef = f.getCoef();
		}	
		
		Face3D[] facesArray = new Face3D[faces.size()];
		for(int i = 0; i < faces.size(); i++) {
			facesArray[i] = faces.get(i);
		}
		
		ObjectModel om = new ObjectModel(normVertexesArray, facesArray);
		
		ArrayList<Vertex3D> controlPoints = new ArrayList<Vertex3D>();
		sc = new Scanner(Paths.get(args[1]));
		while(sc.hasNext()) {
			String[] row = sc.nextLine().split(" ");
			controlPoints.add(new Vertex3D(Integer.parseInt(row[1]), Integer.parseInt(row[2]), Integer.parseInt(row[3])));
		}
//		Vertex3D[] vertexesArrayTmp = new Vertex3D[vertexes.size()];
		for(int i = 0; i < vertexes.size(); i++) {
			vertexesArray[i] = vertexes.get(i);
		}
		
		Vertex3D[] controlPointsNorm = Vertex3D.norm(vertexesArray);
		
		//tangents and b-spline computation
		ArrayList<Vertex3D> bSpline = new ArrayList<Vertex3D>();
		ArrayList<IVector> tangents = new ArrayList<IVector>();
		for(int i = 0; i < controlPoints.size() - 3; i++) { // for each segment
			Vertex3D v1 = controlPoints.get(i);
			Vertex3D v2 = controlPoints.get(i+1);
			Vertex3D v3 = controlPoints.get(i+2);
			Vertex3D v4 = controlPoints.get(i+3);
			
			for(double j = 0; j < 100; j++) {
				double t = j/100;
				double k1 = (-1 * Math.pow(t, 3) + 3 * Math.pow(t, 2) - 3 * t + 1) / 6;
				double k2 = (3 * Math.pow(t, 3) - 6 * Math.pow(t, 2) + 4) / 6;
				double k3 = (-3 * Math.pow(t, 3) + 3 * Math.pow(t, 2) + 3 * t + 1) / 6;
				double k4 = Math.pow(t, 3) / 6;
				
				double Vx = k1 * v1.getX() + k2 * v2.getX() + k3 * v3.getX() + k4 * v4.getX();
				double Vy = k1 * v1.getY() + k2 * v2.getY() + k3 * v3.getY() + k4 * v4.getY();
				double Vz = k1 * v1.getZ() + k2 * v2.getZ() + k3 * v3.getZ() + k4 * v4.getZ();
				
				Vertex3D v = new Vertex3D(Vx, Vy, Vz);
				bSpline.add(v);
				
				k1 = (-1 * Math.pow(t, 2) + 2 * t - 1) / 2;
				k2 = (3 * Math.pow(t, 2) - 4 * t) / 2;
				k3 = (-3 * Math.pow(t, 2) + 2 * t + 1) / 2;
				k4 = (Math.pow(t, 2)) / 2;
				
				double tx = k1 * v1.getX() + k2 * v2.getX() + k3 * v3.getX() + k4 * v4.getX();
				double ty = k1 * v1.getY() + k2 * v2.getY() + k3 * v3.getY() + k4 * v4.getY();
				double tz = k1 * v1.getZ() + k2 * v2.getZ() + k3 * v3.getZ() + k4 * v4.getZ();
				IVector tang = Vector.parseSimple(tx + " " + ty + " " + tz);
				tangents.add(tang);	
			}
		}
		System.out.println("BSline:" + bSpline.size());
		System.out.println("Tang: " + tangents.size());
		
//		for(int i = 0; i < bSpline.size(); i++) {
//			System.out.println("V" + i + ": " + bSpline.get(i));
//		}
		
		//object center computation
		IVector objectCenterV = Vector.parseSimple("0 0 0"); 
		for(Vertex3D v : om.getVertexes()) {
			IVector tmp = Vector.parseSimple(v.getX() + " " + v.getY() + " " + v.getZ());
			objectCenterV.add(tmp);
//			System.out.println(objectCenterV);
		}
		objectCenterV.scalarMultiply(1.0/(double)(om.getVertexes().length));
		System.out.println("Object center : " + objectCenterV);
		Vertex3D objectCenter = new Vertex3D(objectCenterV.get(0), objectCenterV.get(1), objectCenterV.get(2));
		
		IVector s = Vector.parseSimple("0 0 1");
		
		 
		
		GLProfile glprofile = GLProfile.getDefault();
		GLCapabilities glcapabilities = new GLCapabilities(glprofile);
		final GLCanvas glcanvas = new GLCanvas(glcapabilities);
		FPSAnimator animator = new FPSAnimator(60);
		animator.add(glcanvas);
		
		glcanvas.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyChar() == 's') 
					animator.start();
				else if(e.getKeyChar() == 'p') 
					animator.pause();
				else if(e.getKeyChar() == 'r') 
					animator.resume();
				
				glcanvas.display();
				
			}
			
		});
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				glcanvas.addGLEventListener(new GLEventListener() {
					private int t = 0;
					@Override
					public void reshape(GLAutoDrawable glautodrawable, int x, int y, int width, int length) {
						GL2 gl2 = glautodrawable.getGL().getGL2();
						gl2.glMatrixMode(GL2.GL_PROJECTION);
						gl2.glLoadIdentity();				
						
						GLU glu = new GLU();
						glu.gluPerspective(60, 1, 0.1, 100);
						
						gl2.glMatrixMode(GL2.GL_MODELVIEW);
						gl2.glViewport(0, 0, glautodrawable.getSurfaceWidth(), glautodrawable.getSurfaceHeight());
					}
					
					@Override
					public void init(GLAutoDrawable glautodrawable) {
//						animator.start();
						
					}
					
					@Override
					public void dispose(GLAutoDrawable glautodrawable) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void display(GLAutoDrawable glautodrawable) {
						double start = System.currentTimeMillis();
						GL2 gl2 = glautodrawable.getGL().getGL2();
						int width = glautodrawable.getSurfaceWidth();
						int height = glautodrawable.getSurfaceHeight();
						ArrayList<IVector> eList = new ArrayList<IVector>();
						
						gl2.glClearColor(1, 1, 1, 1);
						gl2.glClear(GL.GL_COLOR_BUFFER_BIT);						
						gl2.glLoadIdentity();
						
						GLU glu = new GLU();	 
						glu.gluLookAt(-1, -3, -5, 0, 0, 0, 0, 1, 0);

						
						// control points
						gl2.glColor3f(1.0f, 0.0f, 0.0f);
						gl2.glPointSize(3);
						gl2.glBegin(GL2.GL_POINTS);
						for(int i = 0; i < controlPoints.size(); i++) {
							gl2.glVertex3d(controlPoints.get(i).getX(), controlPoints.get(i).getY(), controlPoints.get(i).getZ());
						}
						gl2.glEnd();
						
						//b-spline curve
						gl2.glPointSize(1);
						gl2.glBegin(GL2.GL_LINE_STRIP);
						gl2.glColor3f(0.0f, 0.0f, 0.0f);
						for(int i = 0; i < bSpline.size(); i = i + 2) {
							Vertex3D v = bSpline.get(i);
							gl2.glVertex3d(v.getX(), v.getY(), v.getZ());
						}
						gl2.glEnd();
						
						//tangents
						gl2.glBegin(GL2.GL_LINES);
						for(int i = 0; i < tangents.size(); i++) {
							Vertex3D v1 = bSpline.get(i);
							IVector tmp = Vector.parseSimple(v1.getX() + " " + v1.getY() + " " + v1.getZ());
							try {
								IVector res = tmp.nAdd(tangents.get(i).nScalarMultiPly(0.5));
								eList.add(res);
								Vertex3D v2 = new Vertex3D(res.get(0), res.get(1), res.get(2));
								if(i % 25 == 0) {
									gl2.glVertex3d(v1.getX(), v1.getY(), v1.getZ());
									gl2.glVertex3d(v2.getX(), v2.getY(), v2.getZ());
								}
								
							} catch (IncompatibleOperandException e) {
							}
						}
						gl2.glEnd();
						
						//transform object coordinates
						gl2.glTranslated(bSpline.get(t).getX() - objectCenter.getX(), bSpline.get(t).getY() - objectCenter.getY(), bSpline.get(t).getZ() - objectCenter.getZ());
						IVector axis = Vector.parseSimple("0 0 1");
						IVector e = Vector.parseSimple("0 0 0");
						try {
							e = eList.get(t).nSub(bSpline.get(t).toVector());
							axis = s.nVectorProduct(e);
						} catch (IncompatibleOperandException err) {
						}
						double angle = Math.acos(s.scalarProduct(e) / (s.norm() * e.norm())) / (2 * Math.PI) * 360;
						gl2.glRotated(angle, axis.get(0), axis.get(1), axis.get(2));
						
						//object
						for (Face3D f : om.getFaces()) {
							gl2.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
							gl2.glPointSize(2);
							gl2.glColor3f(0.0f, 0.0f, 0.0f);
							gl2.glBegin(GL2.GL_POLYGON);
							int[] ind = f.getIndexes();
							Vertex3D v0 = om.getVertexes()[ind[0] - 1];
							Vertex3D v1 = om.getVertexes()[ind[1] - 1];
							Vertex3D v2 = om.getVertexes()[ind[2] - 1];
//							System.out.println("v0 : " + v0);
//							System.out.println("v1 : " + v1);
//							System.out.println("v2 : " + v2);
							gl2.glVertex3d(v0.getX(), v0.getY(), v0.getZ());
							gl2.glVertex3d(v1.getX(), v1.getY(), v1.getZ());
							gl2.glVertex3d(v2.getX(), v2.getY(), v2.getZ());
							gl2.glEnd();
						}
						
						//object axis'
						gl2.glBegin(GL2.GL_LINES);
						gl2.glColor3d(1.0, 0.0, 0.0);
						gl2.glVertex3d(objectCenter.getX(), objectCenter.getY(), objectCenter.getZ());
						gl2.glVertex3d(objectCenter.getX() + 1, objectCenter.getY(), objectCenter.getZ());
						
						gl2.glColor3d(0.0, 1.0, 0.0);
						gl2.glVertex3d(objectCenter.getX(), objectCenter.getY(), objectCenter.getZ());
						gl2.glVertex3d(objectCenter.getX(), objectCenter.getY() + 1, objectCenter.getZ());
						
						gl2.glColor3d(0.0, 0.0, 1.0);
						gl2.glVertex3d(objectCenter.getX(), objectCenter.getY(), objectCenter.getZ());
						gl2.glVertex3d(objectCenter.getX(), objectCenter.getY(), objectCenter.getZ() + 1);
						gl2.glEnd();
						

						if(++t >= 900) t = 0;

						
					}
				});
				
				final JFrame jframe = new JFrame("Animacija objekta");
				jframe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				jframe.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						jframe.dispose();
						System.exit(0);
					}	
				});
				jframe.getContentPane().add(glcanvas, BorderLayout.CENTER);
				jframe.setSize(1500, 1000);
				jframe.setVisible(true);
				jframe.requestFocus(); 
				glcanvas.requestFocusInWindow();
				glcanvas.display();
				
			}
		});

	}

}
