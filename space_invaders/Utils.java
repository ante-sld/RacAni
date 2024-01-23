package racani.space_invaders;

import java.io.File;
import java.io.FileInputStream;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;


public class Utils {
	
	public static int[] loadTexture(GLAutoDrawable glautodrawable) {
		GL2 gl = glautodrawable.getGL().getGL2();
		int[] res = new int[3];
		
		try {
			File f = new File(System.getProperty("user.dir") + "\\resources\\alien.png");
			Texture texture = TextureIO.newTexture(new FileInputStream(f), true, TextureIO.PNG);
			res[0] = texture.getTextureObject(gl);
			
			f = new File(System.getProperty("user.dir") + "\\resources\\player.png");
			texture = TextureIO.newTexture(new FileInputStream(f), true, TextureIO.PNG);
			res[1] = texture.getTextureObject(gl);
			
			f = new File(System.getProperty("user.dir") + "\\resources\\shot.png");
			texture = TextureIO.newTexture(new FileInputStream(f), true, TextureIO.PNG);
			res[2] = texture.getTextureObject(gl);
			
		} catch (Exception e) {
			System.err.println(e);
		}
		
		return res;
		
	}
}
