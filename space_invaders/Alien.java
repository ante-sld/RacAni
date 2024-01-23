package racani.space_invaders;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.jogamp.opengl.GL2;

public class Alien {
	private int x;
	private int y;
	private int texture;
	private int width = 24;
	private int height = 14;
	private int windowHeight;
	private boolean visible;
	private long lastShotTime;
	private int shotInterval;
	
	
	public Alien(int x, int y, int texture) {
		this.x = x;
		this.y = y;
		this.texture = texture;
		this.visible = true;
		this.lastShotTime = System.currentTimeMillis();
		this.shotInterval = ThreadLocalRandom.current().nextInt(3, 7) * 1000;
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
	
	public void setVisible(boolean b) {
		this.visible = b;
	}
	
	public boolean isVisible() {
		return this.visible;
	}
	
	public void update(int direction) {
			this.x += direction;				
	}

	public void drawAlien(GL2 gl, int windowWidth, int windowHeight) {
		if(!this.visible)
			return;
		
		this.windowHeight = windowHeight;
		gl.glBindTexture(GL2.GL_TEXTURE_2D, this.texture);
		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord2i(0, 0); gl.glVertex2i(this.x, windowHeight - (this.y + this.height));					
		gl.glTexCoord2i(1, 0); gl.glVertex2i(this.x + this.width, windowHeight - (this.y + this.height));														
		gl.glTexCoord2i(1, 1); gl.glVertex2i(this.x + this.width, windowHeight - this.y);
		gl.glTexCoord2i(0, 1); gl.glVertex2i(this.x, windowHeight - this.y);
		gl.glEnd();
	}

	public void goDown() {
		this.y += Helper.VERTICAL_SPACE * 15;
		
	}
	
	public boolean checkColision(Shot s) {
		if(s.getX() >= this.x && s.getX() <= this.x + this.width) {
			if(s.getY() + s.getHeight() >= this.windowHeight - (this.y + this.height) && s.getY() + s.getHeight() <= this.windowHeight - this.y) {
				return true;
			}
		}
		return false;
	}
	
	public boolean checkColision(Player p, int direction) {
		if(direction == 1) {
			if(p.getX() >= this.x && p.getX() <= this.x + this.width) {
				if(p.getY() + p.getHeight() >= this.windowHeight - (this.y + this.height) && p.getY() + p.getHeight() <= this.windowHeight - this.y) {
					return true;
				}
			}
		} else if(direction == -1) {
			if(p.getX() + p.getWidth() >= this.x && p.getX() + p.getWidth() <= this.x + this.width) {
				if(p.getY() + p.getHeight() >= this.windowHeight - (this.y + this.height) && p.getY() + p.getHeight() <= this.windowHeight - this.y) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void generateShot(ArrayList<Shot> shots, int texture) {
		long currentTime = System.currentTimeMillis();
		if(currentTime - this.lastShotTime > this.shotInterval) {
			this.lastShotTime = currentTime;
			this.shotInterval = ThreadLocalRandom.current().nextInt(3, 7) * 1000;
			Shot s = new Shot(Math.round(this.x + this.width / 2), Math.round(this.windowHeight - (this.y + this.height)), texture, -1);
			shots.add(s);
		}
			
	}
}
