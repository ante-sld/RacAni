package racani.space_invaders;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

public class Invaders {
	private int alienTexture;
	private int rowCount;
	private int columnCount;
	private int direction;
	private Alien[][] aliens;
	private int shotTexture;
	
	public Invaders(int alienTexture, int shotTexture, int rowCount) {
		this.alienTexture = alienTexture;
		this.rowCount = rowCount;
		this.columnCount = 6;   
		this.aliens = createAliens();
		this.direction = 1;
		this.shotTexture = shotTexture;
	}
	
	private Alien[][] createAliens() {
		Alien[][] res = new Alien[this.columnCount][this.rowCount];
		for(int i = 0; i < this.columnCount; i++) {
			for(int j = 0; j < this.rowCount; j++) {
				res[i][j] = new Alien(i * Helper.HORIZONTAL_SPACE, j * Helper.VERTICAL_SPACE, this.alienTexture);
			}
		}
		return res;
	}
	
	private Alien[] findLowestVisibleAliens() {
		Alien[] res = new Alien[this.columnCount];
		for(int i = 0; i < this.columnCount; i++) {
			for(int j = 0; j < this.rowCount; j++) {
				if(this.aliens[i][j].isVisible()) {
					res[i] = this.aliens[i][j];
				}
			}
		}
		return res;
	}
	
	
	
	private void checkDirection(int width) {
		Alien furthestAlien;
		if(this.direction == 1) {
			furthestAlien = findFurthestAlien();
			if(furthestAlien.getX() + furthestAlien.getWidth() >= width) {
				this.direction = -1;
				goDown();
			}
		} else if(this.direction == -1) {
			furthestAlien = findFurthestAlien();
			if(furthestAlien.getX() <= 0) {
				this.direction = 1;
				goDown();
			}
		}
	}
	
	private Alien findFurthestAlien() {
		for(int i = 0; i < this.rowCount; i++) {
			if(this.direction == 1) {
				for(int j = this.columnCount - 1; j >= 0; j--) {
					if(this.aliens[j][i].isVisible()) {
						return this.aliens[j][i];
					}		
				}
			} else if(this.direction == -1) {
				for(int j = 0; j < this.columnCount; j++) {
					if(this.aliens[j][i].isVisible())
						return this.aliens[j][i];
				}
			}	
		}
		return this.aliens[0][0];
	}
	
	private void goDown() {
		for(int i = 0; i < this.aliens.length; i++) {
			for(int j = 0; j < this.aliens[0].length; j++) {
				aliens[i][j].goDown();
			}
		}	
	}
	
	public boolean checkColision(Shot s) {
		for(int i = 0; i < this.aliens.length; i++) {
			for(int j = 0; j < this.aliens[0].length; j++) {
				if(!this.aliens[i][j].isVisible())
					continue;
				
				boolean hit = this.aliens[i][j].checkColision(s);
				if(hit) {
					this.aliens[i][j].setVisible(false);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean checkColision(Player player) {
		for(int i = 0; i < this.columnCount; i++) {
			for(int j = 0; j < this.rowCount; j++) {
				if(aliens[i][j].checkColision(player, this.direction) && this.aliens[i][j].isVisible()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void generateShots(ArrayList<Shot> shots) {
		Alien[] lowestVisibleAliens = findLowestVisibleAliens();
		for(Alien a : lowestVisibleAliens) {
			if(a != null)
				a.generateShot(shots, this.shotTexture);
		}
	}
	
	public boolean noVisibleAliens() {
		boolean res;
		for(int i = 0; i < this.columnCount; i++) {
			for(int j = 0; j < this.rowCount; j++) { 
				if(aliens[i][j].isVisible()) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void update(int width) {
		checkDirection(width);
		for(int i = 0; i < this.columnCount; i++) {
			for(int j = 0; j < this.rowCount; j++) {
				aliens[i][j].update(this.direction);
			}
		}
	}
	
	public void drawInvaders(GL2 gl, int width, int height) {
		
		for(int i = 0; i < this.columnCount; i++) {
			for(int j = 0; j < this.rowCount; j++) {
				aliens[i][j].drawAlien(gl, width, height);
			}
		}
	}


	
}
