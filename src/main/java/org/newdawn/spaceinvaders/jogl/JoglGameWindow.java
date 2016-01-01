package org.newdawn.spaceinvaders.jogl;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//import net.java.games.jogl.Animator;
//import net.java.games.jogl.GL;
//import net.java.games.jogl.GLCanvas;
//import net.java.games.jogl.GLCapabilities;
//import net.java.games.jogl.GLDrawable;
//import net.java.games.jogl.GLDrawableFactory;
//import net.java.games.jogl.GLEventListener;





import javax.swing.JFrame;

import org.newdawn.spaceinvaders.GameWindow;
import org.newdawn.spaceinvaders.GameWindowCallback;
import org.newdawn.spaceinvaders.util.Keyboard;

import com.jogamp.nativewindow.WindowClosingProtocol.WindowClosingMode;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.WindowListener;
import com.jogamp.newt.event.WindowUpdateEvent;
import com.jogamp.newt.opengl.GLWindow;
//import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

/**
 * An implementation of GameWindow that will use OPENGL (JOGL) to 
 * render the scene. Its also responsible for monitoring the keyboard
 * using AWT.
 * 
 * @author Kevin Glass
 */
public class JoglGameWindow implements GLEventListener, GameWindow {
	
	/** The frame containing the JOGL display */
	private Frame frame;
	/** The callback which should be notified of window events */
	private GameWindowCallback callback;
	/** The width of the game display area */
	private int width;
	/** The height of the game display area */
	private int height;
	/** The canvas which gives us access to OpenGL */
	private GLCanvas canvas;
	/** The OpenGL content, we use this to access all the OpenGL commands */
	private GL2 gl;
	/** The loader responsible for converting images into OpenGL textures */
	private TextureLoader textureLoader;
	
	public String title = getClass().getSimpleName();
	private FPSAnimator  animator;
//	private Animator animator;
	private int whichwasPressed = 0;
	
	/**
	 * Create a new game window that will use OpenGL to 
	 * render our game.
	 */
	public JoglGameWindow() {
		frame = new Frame();
	}
	
	/**
	 * Retrieve access to the texture loader that converts images
	 * into OpenGL textures. Note, this has been made package level
	 * since only other parts of the JOGL implementations need to access
	 * it.
	 * 
	 * @return The texture loader that can be used to load images into
	 * OpenGL textures.
	 */
	TextureLoader getTextureLoader() {
		return textureLoader;
	}
	
	/**
	 * Get access to the GL context that can be used in JOGL to
	 * call OpenGL commands.
	 * 
	 * @return The GL context which can be used for this window
	 */
	GL getGL() {
		return gl;
	}
	
	/**
	 * Set the title of this window.
	 *
	 * @param title The title to set on this window
	 */
	public void setTitle(String title) {
		frame.setTitle(title);
	}

	/**
	 * Set the resolution of the game display area.
	 *
	 * @param x The width of the game display area
	 * @param y The height of the game display area
	 */
	public void setResolution(int x, int y) {
		width = x;
		height = y;
	}

	/**
	 * Start the rendering process. This method will cause the 
	 * display to redraw as fast as possible.
	 */
	public void startRendering() {
		 final GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL2));
         caps.setBackgroundOpaque(true);
         caps.setDoubleBuffered(true);
         GLWindow glWindow = GLWindow.create(caps); 
         glWindow.setSize(width, height);
         glWindow.setUndecorated(false);
         glWindow.setPointerVisible(true);
         glWindow.setVisible(true);
         glWindow.setDefaultCloseOperation(WindowClosingMode.DISPOSE_ON_CLOSE); 
		
         glWindow.addGLEventListener(this);
         glWindow.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				whichwasPressed = e.getKeyCode();
				
			}
		});
         
         glWindow.addWindowListener(new WindowListener() {
			
			@Override
			public void windowResized(com.jogamp.newt.event.WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowRepaint(WindowUpdateEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowMoved(com.jogamp.newt.event.WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowLostFocus(com.jogamp.newt.event.WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowGainedFocus(com.jogamp.newt.event.WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDestroyed(com.jogamp.newt.event.WindowEvent e) {
                // Use a dedicate thread to run the stop() to ensure that the
                // animator stops before program exits.
                new Thread() {
                   @Override
                   public void run() {
                      animator.stop(); // stop the animator loop
                      System.exit(0);
                   }
                }.start();
			}
			
			@Override
			public void windowDestroyNotify(com.jogamp.newt.event.WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
          });
//		canvas = new GLCanvas();
//		canvas.addGLEventListener(this);		
//		canvas.setFocusable(true);
//		Keyboard.init(canvas);
//		
//		frame.setLayout(new BorderLayout());
//		frame.add(canvas);
//        frame.setSize(width, height);
//        frame.setResizable(false);
//        frame.setVisible(true);
//        canvas.requestFocus();
		
		// add a listener to respond to the user closing the window. If they
		// do we'd like to exit the game
//		frame.addWindowListener(new WindowAdapter() {
//			public void windowClosing(WindowEvent e) {
//				if (callback != null) {
//					System.out.println("Finished with "+getClass().getSimpleName());
//					callback.windowClosed();
//				} else {
//					System.exit(0);
//				}
//			}
//		});
		
		// start a animating thread (provided by JOGL) to actively update the canvas
//         animator = new Animator(canvas);
//		animator.start();
         animator = new FPSAnimator(glWindow, 60, true);
 		animator.start();
	}

	/**
	 * Register a callback that will be notified of game window
	 * events.
	 *
	 * @param callback The callback that should be notified of game
	 * window events. 
	 */
	public void setGameWindowCallback(GameWindowCallback callback) {
		this.callback = callback;
	}

	/**
	 * Check if a particular key is current pressed.
	 *
	 * @param keyCode The code associated with the key to check 
	 * @return True if the specified key is pressed
	 */
	public boolean isKeyPressed(int keyCode) {
//		return Keyboard.isPressed(keyCode);
		return keyCode == whichwasPressed;
	}

	/**
	 * Called by the JOGL rendering process at initialisation. This method
	 * is responsible for setting up the GL context.
	 *
	 * @param drawable The GL context which is being initialised
	 */
	public void init(GLAutoDrawable drawable) {
		System.out.println("init at "+getClass().getSimpleName());
		// get hold of the GL content
		gl = drawable.getGL().getGL2();
		
		// init animation
//        animator = new Animator(drawable);
//		animator.start();
		
		// enable textures since we're going to use these for our sprites
		gl.glEnable(GL.GL_TEXTURE_2D);
		
		// set the background colour of the display to black
		gl.glClearColor(0, 0, 0, 0);
		// set the area being rendered
		gl.glViewport(0, 0, width, height);
		// disable the OpenGL depth test since we're rendering 2D graphics
		gl.glDisable(GL.GL_DEPTH_TEST);
		
		textureLoader = new TextureLoader(drawable.getGL());
		
		if (callback != null) {
			callback.initialise();
		}
	}

	/**
	 * Called by the JOGL rendering process to display a frame. In this
	 * case its responsible for blanking the display and then notifing
	 * any registered callback that the screen requires rendering.
	 * 
	 * @param drawable The GL context component being drawn
	 */
	public void display(GLAutoDrawable drawable) {
		System.out.println("display at "+getClass().getSimpleName());
		// get hold of the GL content
		gl = drawable.getGL().getGL2();
		
		// clear the screen and setup for rendering
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		// if a callback has been registered notify it that the
		// screen is being rendered
		if (callback != null) {
			callback.frameRendering();
		}
		
		// flush the graphics commands to the card
		gl.glFlush();
	}

	/**
	 * Called by the JOGL rendering process if and when the display is 
	 * resized.
	 *
	 * @param drawable The GL content component being resized
	 * @param x The new x location of the component
	 * @param y The new y location of the component
	 * @param width The width of the component
	 * @param height The height of the component 
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		gl = drawable.getGL().getGL2();
		
		// at reshape we're going to tell OPENGL that we'd like to 
		// treat the screen on a pixel by pixel basis by telling
		// it to use Orthographic projection.
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		gl.glOrtho(0, width, height, 0, -1, 1);
	}

	@Override
    public void dispose(GLAutoDrawable drawable) {
	    // TODO Auto-generated method stub
		System.out.println("dispose at "+getClass().getSimpleName());
		
//	    drawable.destroy();
    }

}