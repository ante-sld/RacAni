package racani.space_invaders;

import java.util.ArrayList;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

public class Player {
	private int x;
	private int y;
	private int texture;
	private int width = 30;
	private int height = 20;
	private int moving = 0;
	private long lastShotTime;
	private int shotTexture;
	private int windowHeight;
	private int health;
	
	public Player(int texture, int shotTexture) {
		this.x = Math.round(Helper.WIDTH / 2 - width / 2);
		this.y = Math.round(Helper.HEIGHT / 10);
		this.texture = texture;
		this.shotTexture = shotTexture;
		this.lastShotTime = System.currentTimeMillis();
		this.health = Helper.MAX_HEALTH;
	}
	
	public int getHealth() {
		return this.health;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}

	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void setMoving(int direction) {
		this.moving = direction;
	}
	
	public void update(int surfaceWidth, int surfaceHeight) {
		this.windowHeight = surfaceHeight;
		if(this.moving != 0) {
			if(this.x + 2*this.moving > 0 && this.x + this.width + 2*this.moving < surfaceWidth) {
				this.x += 4*this.moving;
			}	
		}
	}
	
	public void shoot(ArrayList<Shot> shots) {
		long currentTime = System.currentTimeMillis();
		if(currentTime - this.lastShotTime > 500) {
			this.lastShotTime = currentTime;
			Shot s = new Shot(Math.round(this.x + this.width / 2), Math.round(this.y + this.height), this.shotTexture,  1);
			shots.add(s);
		}
	}
	
	public boolean checkColision(Shot s) {
		if(s.getX() >= this.x && s.getX() <= this.x + this.width) {
			if(s.getY() >= this.y && s.getY() + s.getHeight() <= this.y + this.height) {
				this.health--;
				return true;
			}
		}
		return false;
	}
	
	public void drawPlayer(GL2 gl) {
		// player
		gl.glBindTexture(GL2.GL_TEXTURE_2D, this.texture);
		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord2i(0, 0); gl.glVertex2i(this.x, this.y);
		gl.glTexCoord2i(1, 0); gl.glVertex2i(this.x + this.width, this.y);
		gl.glTexCoord2i(1, 1); gl.glVertex2i(this.x + this.width, this.y + this.height);
		gl.glTexCoord2i(0, 1); gl.glVertex2i(this.x, this.y + this.height);
		gl.glEnd();
		
		// health bar 
		gl.glDisable(GL2.GL_TEXTURE_2D);
		gl.glBegin(GL2.GL_LINE_STRIP);
		gl.glVertex2i(0, 0); 
		gl.glVertex2i(Helper.MAX_HEALTH * 15, 0); 
		gl.glVertex2i(Helper.MAX_HEALTH * 15, 15); 
		gl.glVertex2i(0, 15); gl.glVertex2i(0, 0); 
		gl.glEnd();
		
		for(int i = 0; i < this.health; i++) {
			gl.glBegin(GL2.GL_QUADS);
			gl.glVertex2i(i * 15, 0);
			gl.glVertex2i((i + 1) * 15, 0);
			gl.glVertex2i((i + 1) * 15, 15);
			gl.glVertex2i(i * 15, 15);
			gl.glEnd();
		}
	}
	
}
