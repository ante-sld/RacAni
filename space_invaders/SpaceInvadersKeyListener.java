package racani.space_invaders;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import com.jogamp.opengl.util.FPSAnimator;

public class SpaceInvadersKeyListener implements KeyListener {
	private FPSAnimator animator;
	private Player player;
	private int surfaceWidth;
	private ArrayList<Shot> shots;
	
	public SpaceInvadersKeyListener(FPSAnimator animator) {
		this.animator = animator;
		this.surfaceWidth = 0;
	}
	
	public void setPlayer(Player p) {
		this.player = p;
	}
	
	public void setShots(ArrayList<Shot> shots) {
		this.shots = shots;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyChar() == 's') {
			animator.start();
		} else if(e.getKeyChar() == 'p') {
			animator.pause();
		} else if(e.getKeyChar() == 'r') {
			animator.resume();
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			this.player.setMoving(-1);
		} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			this.player.setMoving(1);
		} else if(e.getKeyCode() == KeyEvent.VK_UP) {
			this.player.shoot(this.shots);
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
			this.player.setMoving(0);
		} 

	}

	public void setWidth(int surfaceWidth) {
		this.surfaceWidth = surfaceWidth;	
	}

}
