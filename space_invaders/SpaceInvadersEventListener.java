package racani.space_invaders;

import java.util.ArrayList;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

public class SpaceInvadersEventListener implements GLEventListener{
	private int[] textures;
	private Invaders invaders;
	private Player player;
	private SpaceInvadersKeyListener keyListener;
	private ArrayList<Shot> shots;
	private FPSAnimator animator;
	
	public SpaceInvadersEventListener(SpaceInvadersKeyListener keyListener, FPSAnimator animator) {
		this.keyListener = keyListener;
		this.animator = animator;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	
	@Override
	public void display(GLAutoDrawable glautodrawable) {
		GL2 gl = glautodrawable.getGL().getGL2();
		int width = glautodrawable.getSurfaceWidth();
		int height = glautodrawable.getSurfaceHeight();
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);						
		gl.glLoadIdentity();
		gl.glEnable(GL2.GL_TEXTURE_2D);
		
		
		this.invaders.update(width);
		this.invaders.generateShots(this.shots);
//		System.out.println(this.shots.size());
		this.player.update(width, height);
		for(int i = 0; i < this.shots.size(); i++) {
			boolean forDestruction = this.shots.get(i).update(height, invaders, player);
			if(forDestruction) {
				this.shots.remove(i);
				i--;
			}
		}
		
		this.invaders.drawInvaders(gl, width, height);
		for(Shot s : this.shots) {
			s.drawShot(gl);
		}
		this.player.drawPlayer(gl);
		if(this.player.getHealth() == 0 || invaders.checkColision(this.player)) {
			System.out.println("Unfortunately, you lost :( ");
			animator.stop();
		}
		if(this.invaders.noVisibleAliens()) {
			System.out.println("YOU WON!!!");
			animator.stop();
		}
		
		
		
		
	}
	
	@Override
	public void dispose(GLAutoDrawable glautodrawable) {
		
	}

	@Override
	public void init(GLAutoDrawable glautodrawable) {
		this.textures = Utils.loadTexture(glautodrawable);
		this.invaders = new Invaders(this.textures[0], this.textures[2], 5);
		this.player = new Player(this.textures[1], this.textures[2]);
		this.shots = new ArrayList<Shot>();
		keyListener.setPlayer(this.player);
		keyListener.setShots(this.shots);
		keyListener.setWidth(glautodrawable.getSurfaceWidth());
	}

	@Override
	public void reshape(GLAutoDrawable glautodrawable, int x, int y, int width, int height) {
		GL2 gl2 = glautodrawable.getGL().getGL2(); 
		gl2.glMatrixMode(GL2.GL_PROJECTION);
		gl2.glLoadIdentity();
		
		GLU glu = new GLU();
		glu.gluOrtho2D(0.0f, glautodrawable.getSurfaceWidth(), 0.0f, glautodrawable.getSurfaceHeight());
		
		gl2.glMatrixMode(GL2.GL_MODELVIEW);
		gl2.glLoadIdentity();
		
		gl2.glViewport(0, 0, glautodrawable.getSurfaceWidth(), glautodrawable.getSurfaceHeight());
		
	}

}
