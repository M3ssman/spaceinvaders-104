package org.newdawn.spaceinvaders.jogl;

import org.newdawn.spaceinvaders.GameWindow;
import org.newdawn.spaceinvaders.GameWindowCallback;

import com.jogamp.nativewindow.WindowClosingProtocol.WindowClosingMode;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

/**
 * An implementation of GameWindow that will use OPENGL (JOGL) to
 * render the scene. Its also responsible for monitoring the keyboard
 * using AWT.
 * 
 * @author Kevin Glass
 */
public class JoglGLWindow implements GLEventListener, GameWindow {

	/** The callback which should be notified of window events */
	private GameWindowCallback callback;
	
	/** The width of the game display area */
	private int width;
	
	/** The height of the game display area */
	private int height;

	/** The OpenGL content, we use this to access all the OpenGL commands */
	private GL2 gl;
	
	/** The loader responsible for converting images into OpenGL textures */
	private TextureLoader textureLoader;

	private String title = GameWindow.TITLE + getClass().getSimpleName();
	
	private FPSAnimator animator;
	
	private int whichwasPressed = KeyEvent.VK_F13;

	private GLWindow glWindow;

	/**
	 * Retrieve access to the texture loader that converts images
	 * into OpenGL textures. Note, this has been made package level
	 * since only other parts of the JOGL implementations need to access
	 * it.
	 * 
	 * @return The texture loader that can be used to load images into
	 *         OpenGL textures.
	 */
	TextureLoader getTextureLoader() {
		return textureLoader;
	}

	GLWindow getGLWindow() {
		if(glWindow == null) {
			glWindow = createGLWindow();
		}
		return glWindow;
	}
	
	/**
	 * Get access to the GL context that can be used in JOGL to
	 * call OpenGL commands.
	 * 
	 * @return The GL context which can be used for this window
	 */
	public GL2 getGL() {
		return gl;
	}

	/**
	 * Set the title of this window.
	 *
	 * @param title
	 *            The title to set on this window
	 */
	public void setTitle(String title) {
		getGLWindow().setTitle(title);
	}
	
	public String getTitle() {
		return title;
	}

	/**
	 * Set the resolution of the game display area.
	 *
	 * @param x
	 *            The width of the game display area
	 * @param y
	 *            The height of the game display area
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
		GLWindow glw = getGLWindow();
		glw.setSize(width, height);
		glw.setUndecorated(false);
		glw.setPointerVisible(true);
		glw.setVisible(true);
		glw.setDefaultCloseOperation(WindowClosingMode.DISPOSE_ON_CLOSE);
		glw.addGLEventListener(this);
		glw.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				whichwasPressed = KeyEvent.VK_F13;
			}

			/**
			 * The Mapping from JOGL KeyEvent to Java AWT KeyEvent
			 */
			@Override
			public void keyPressed(KeyEvent e) {
				if ( e.getKeyCode() == KeyEvent.VK_LEFT) {
					whichwasPressed = java.awt.event.KeyEvent.VK_LEFT;
				} else if ( e.getKeyCode() == KeyEvent.VK_RIGHT) {
					whichwasPressed = java.awt.event.KeyEvent.VK_RIGHT;
				} else {
					whichwasPressed = e.getKeyCode();
				}
			}
		});

		animator = new FPSAnimator(glw, 60, true);
		animator.start();
	}

	@Override
	public void stopRendering() {
		if(animator.isAnimating()) {
			animator.stop(); // stop the animator loop
		}
	}

	GLWindow createGLWindow() {
	    GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL2));
		caps.setBackgroundOpaque(true);
		caps.setDoubleBuffered(true);
		return GLWindow.create(caps);
    }

	/**
	 * Register a callback that will be notified of game window
	 * events.
	 *
	 * @param callback
	 *            The callback that should be notified of game
	 *            window events.
	 */
	public void setGameWindowCallback(GameWindowCallback callback) {
		this.callback = callback;
	}

	/**
	 * Compare the stored Key
	 *
	 * @param keyCode
	 *            The code associated with the key to check
	 *            
	 * @return True if the specified key is pressed
	 */
	public boolean isKeyPressed(int keyCode) {
		return keyCode == whichwasPressed;
	}

	/**
	 * Called by the JOGL rendering process at initialisation. This method
	 * is responsible for setting up the GL context.
	 *
	 * @param drawable
	 *            The GL context which is being initialised
	 */
	public void init(GLAutoDrawable drawable) {
		// get hold of the GL content
		gl = drawable.getGL().getGL2();

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
	 * @param drawable
	 *            The GL context component being drawn
	 */
	public void display(GLAutoDrawable drawable) {
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
	 * @param drawable
	 *            The GL content component being resized
	 * @param x
	 *            The new x location of the component
	 * @param y
	 *            The new y location of the component
	 * @param width
	 *            The width of the component
	 * @param height
	 *            The height of the component
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
		if(animator.isAnimating()) {
			animator.stop();
		}
		animator.remove(drawable);
	}

}
