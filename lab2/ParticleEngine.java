package racani.lab2;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

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
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import linearna.IVector;
import linearna.IncompatibleOperandException;
import linearna.Vector;

class Source {
	public double x, y, z;
	public float r, g, b;
	public float size;
	public int p;
}

class Particle {
	public double x, y, z;
	public float r, g, b;
	public float v;
	public int t;
	public double sx, sy, sz;
	public double angle;
	public IVector axis;
	public float size;
	public float alpha;
}

public class ParticleEngine {

	static {
		GLProfile.initSingleton();
	}

	
	
	public static void main(String[] args) {
		GLProfile glprofile = GLProfile.getDefault();
		GLCapabilities glcapabilities = new GLCapabilities(glprofile);
		final GLCanvas glcanvas = new GLCanvas(glcapabilities);
		FPSAnimator animator = new FPSAnimator(60);
		animator.add(glcanvas);
		IVector pov = Vector.parseSimple("0 0 60");
		
		glcanvas.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyChar() == 'g') 
					animator.start();
				else if(e.getKeyChar() == 'p') 
					animator.pause();
				else if(e.getKeyChar() == 'r') 
					animator.resume();
				else if(e.getKeyChar() == 'w') 
					pov.set(2, pov.get(2)-1);
				else if(e.getKeyChar() == 'a') 
					pov.set(0, pov.get(0)+1);
				else if(e.getKeyChar() == 's') 
					pov.set(2, pov.get(2)+1);
				else if(e.getKeyChar() == 'd') 
					pov.set(0, pov.get(0)-1);
				else if(e.getKeyChar() == 'x') 
					pov.set(1, pov.get(1)+1);
				else if(e.getKeyChar() == 'y') 
					pov.set(1, pov.get(1)-1);
				
				glcanvas.display();
				
			}
			
		});
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {

				glcanvas.addGLEventListener(new GLEventListener() {
					
					private int texture;
					private double previousTime = 0;
					private double currentTime = 0;
					private Source source;
					private ArrayList<Particle> particles;
					private float dc = 0.01f;
					
					IVector s = Vector.parseSimple("0 0 1");
					
					@Override
					public void reshape(GLAutoDrawable glautodrawable, int arg1, int arg2, int arg3, int arg4) {
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
						GL2 gl = glautodrawable.getGL().getGL2();
						gl.glEnable(GL2.GL_TEXTURE_2D);
						try {
							File im = new File("D:\\Java\\WS\\IRGLab\\objects\\bee.jpg");
							Texture t = TextureIO.newTexture(new FileInputStream(im), true, TextureIO.JPG);
							texture = t.getTextureObject(gl);
						} catch (Exception e) {
						}
						source = new Source();
						source.x = 0.0; source.y = 0.0; source.z = 1.0;
						source.r = 1f; source.g = 0f; source.b = 0f;
						source.size = 0.6f;
						source.p = 10;
						particles = new ArrayList<Particle>();
						
					}
					
					@Override
					public void dispose(GLAutoDrawable glautodrawable) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void display(GLAutoDrawable glautodrawable) {
						GL2 gl = glautodrawable.getGL().getGL2();
						int width = glautodrawable.getSurfaceWidth();
						int height = glautodrawable.getSurfaceHeight();
						gl.glClearColor(0, 0, 0, 0);
						gl.glClear(GL.GL_COLOR_BUFFER_BIT);						
						gl.glLoadIdentity();
						gl.glEnable(GL2.GL_BLEND);
						gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
						gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);
						gl.glTranslated(pov.get(0), pov.get(1), -pov.get(2));
						
						double currentTime = (double)System.currentTimeMillis();
						double timeInterval = currentTime - previousTime;
						
						// create new particles
						if(timeInterval > 100) {
							int n = (int)(Math.random() * source.p) + 1;
							for(int i = 0; i < n; i++) {
								double x, y, z;
								float r, g, b;
								Particle p = new Particle();
								p.x = source.x; p.y = source.y; p.z = source.z;
								p.r = source.r; p.g = source.g; p.b = source.b;
								p.sx = Math.random() - 0.5; p.sy = Math.random() - 0.5; p.sz = Math.random() - 0.5;
								p.t = (int)(Math.random() * 50) + 50;
								p.size = source.size;
								p.v = 0.5f;
								p.alpha = 1f;
								particles.add(p);
							}
							previousTime = currentTime;
						}
							
						// modify all particles
						
						for(int i = 0; i < particles.size(); i++) {
							Particle p = particles.get(i);
							IVector axis = Vector.parseSimple("0 0 1");
							IVector e = Vector.parseSimple("0 0 0");
							
							try {
								e = pov.nSub(Vector.parseSimple(p.x + " " + p.sy + " " + p.z));
								axis = s.nVectorProduct(e);
							} catch (IncompatibleOperandException e1) {						
							}
							
							double angle = Math.acos(s.scalarProduct(e) / (s.norm() * e.norm())) / (2 * Math.PI) * 360;
							p.angle = angle;
							p.axis = axis;
							
							p.x += p.sx * p.v; p.y += p.sy * p.v; p.z += p.sz * p.v;
//								p.r *= dc; p.g *= dc; p.b *= dc;
							p.alpha -= dc;
							p.t--;
							if(p.t <= 0) {
								particles.remove(i);
								i--;
							}
						}
						
						// drawing particles
						
						for(int i = 0; i < particles.size(); i++) {
							Particle p = particles.get(i);
							gl.glColor4f(p.r, p.g, p.b, p.alpha);
							gl.glTranslated(p.x, p.y, p.z);
							gl.glRotated(p.angle, p.axis.get(0), p.axis.get(1), p.axis.get(2));
							gl.glBegin(GL2.GL_QUADS);
							
							gl.glTexCoord2d(0.0,0.0); gl.glVertex3f(-p.size, -p.size, 0.0f);
							gl.glTexCoord2d(1.0,0.0); gl.glVertex3f(-p.size, +p.size, 0.0f);
							gl.glTexCoord2d(1.0,1.0); gl.glVertex3f(+p.size, +p.size, 0.0f);
							gl.glTexCoord2d(0.0,1.0); gl.glVertex3f(+p.size, -p.size, 0.0f);
							gl.glEnd();
							gl.glRotated(-p.angle,p.axis.get(0), p.axis.get(1),  p.axis.get(2));
							gl.glTranslated(-p.x, -p.y, -p.z);
						}
							
					}
				});
				
				final JFrame jframe = new JFrame("Èestice");
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
