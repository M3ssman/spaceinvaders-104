package org.newdawn.spaceinvaders;

/**
 * The window in which the game will be displayed. This interface exposes just
 * enough to allow the game logic to interact with, while still maintaining an
 * abstraction away from any physical implementation of windowing (i.e. AWT, LWJGL)
 *
 * @author Kevin Glass
 */
public interface GameWindow {
	
	public static final String TITLE = "Space Invaders 104 - ";
	
	/**
	 * Set the title of the game window
	 * 
	 * @param title The new title for the game window
	 */
	public void setTitle(String title);
	
	/**
	 * 
	 * Get the initial Title Prefix for each Game Type
	 * 
	 * @return title
	 */
	public String getTitle();
	
	/**
	 * Set the game display resolution
	 * 
	 * @param x The new x resolution of the display
	 * @param y The new y resolution of the display
	 */
	public void setResolution(int x,int y);
	
	/**
	 * Start the game window rendering the display
	 */
	public void startRendering();
	
	/**
	 * Signal that we're going to stop the game and exit
	 */
	public void stopRendering();
	
	/**
	 * Set the callback that should be notified of the window
	 * events.
	 * 
	 * @param callback The callback that should be notified of game
	 * window events.
	 */
	public void setGameWindowCallback(GameWindowCallback callback);
	
	/**
	 * Check if a particular key is pressed
	 * 
	 * @param keyCode The code associate with the key to check
	 * @return True if the particular key is pressed
	 */
	public boolean isKeyPressed(int keyCode);
}