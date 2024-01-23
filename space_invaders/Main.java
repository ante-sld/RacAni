package racani.space_invaders;

import java.awt.BorderLayout;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class Main {

	static {
		GLProfile.initSingleton();
	}
	public static void main(String[] args) {
		
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				GLProfile glprofile = GLProfile.getDefault();
				GLCapabilities glcapabilities = new GLCapabilities(glprofile);
				final GLCanvas glcanvas = new GLCanvas(glcapabilities);
				FPSAnimator animator = new FPSAnimator(60);
				animator.add(glcanvas);
				
				SpaceInvadersKeyListener keyListener = new SpaceInvadersKeyListener(animator);
				SpaceInvadersEventListener eventListener = new SpaceInvadersEventListener(keyListener, animator); 
				
				glcanvas.addGLEventListener(eventListener);
				
				Player p = eventListener.getPlayer();
				keyListener.setPlayer(p);
				
				glcanvas.addKeyListener(keyListener);
				
				final JFrame jframe = new JFrame("Space invaders");
				jframe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				jframe.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						jframe.dispose();
						System.exit(0);
					}	
				});
				jframe.getContentPane().add(glcanvas, BorderLayout.CENTER);
				jframe.setSize(Helper.WIDTH, Helper.HEIGHT);
				jframe.setVisible(true);
				jframe.requestFocus();
				glcanvas.requestFocusInWindow();
			}
		});

	}

}
