package org.newdawn.spaceinvaders.jogl;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.newdawn.spaceinvaders.GameWindow;
import org.newdawn.spaceinvaders.GameWindowCallback;
import org.newdawn.spaceinvaders.util.Keyboard;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

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

	private String title = GameWindow.TITLE + getClass().getSimpleName();

	private Animator animator;

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
	 *         OpenGL textures.
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
		frame.setTitle(title);
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
		canvas = new GLCanvas();
		canvas.addGLEventListener(this);
		canvas.setFocusable(true);
		Keyboard.init(canvas);

		frame.setLayout(new BorderLayout());
		frame.add(canvas);
		frame.setSize(width, height);
		frame.setResizable(false);
		frame.setVisible(true);
		canvas.requestFocus();

		// add a listener to respond to the user closing the window. If they
		// do we'd like to exit the game
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (callback != null) {
					System.out.println("Finished with " + getClass().getSimpleName());
					callback.windowClosed();
				} else {
					System.exit(0);
				}
			}
		});

		// start a animating thread (provided by JOGL) to actively update the canvas
		animator = new Animator(canvas);
		animator.start();
	}

	@Override
	public void stopRendering() {
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
	 * Check if a particular key is current pressed.
	 *
	 * @param keyCode
	 *            The code associated with the key to check
	 * @return True if the specified key is pressed
	 */
	public boolean isKeyPressed(int keyCode) {
		return Keyboard.isPressed(keyCode);
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
		if (animator.isAnimating()) {
			animator.stop();
			animator.remove(drawable);
		}
	}

}
