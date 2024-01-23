package racani.space_invaders;

import com.jogamp.opengl.GL2;

public class Shot {
	private int x;
	private int y;
	private int width = 1;
	private int height = 5;
	private int direction;
	private int texture;
	
	public Shot(int x, int y, int texture, int direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.texture = texture;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getDirection() {
		return direction;
	}

	public boolean update(int surfaceHeight, Invaders invaders, Player player) {
		if(this.direction == 1) {
			if(this.y + this.height < surfaceHeight) {
				boolean hit = invaders.checkColision(this);
				if(hit) {
					return true;
				}
				this.y += direction * 8;
			} else {
				return true;
			}
		} else if(this.direction == -1)  {
//			System.out.printsln("Shot going to player");
			if(this.y > 0) {
				boolean hit = player.checkColision(this);
				if(hit) {
					return true;
				}
				this.y += direction * 8;
				
			} else {
				return true;
			}
		}
		
		
		return false;
	}
	
	public void drawShot(GL2 gl) {
		gl.glBindTexture(GL2.GL_TEXTURE_2D, this.texture);
		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord2i(0, 0); gl.glVertex2i(this.x, this.y);
		gl.glTexCoord2i(1, 0); gl.glVertex2i(this.x + this.width, this.y);
		gl.glTexCoord2i(1, 1); gl.glVertex2i(this.x + this.width, this.y + this.height);
		gl.glTexCoord2i(0, 1); gl.glVertex2i(this.x, this.y + this.height);
		gl.glEnd();
	}
}
