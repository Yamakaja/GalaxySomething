package main;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;

public abstract class GameState {
	
	protected AppGameContainer gameContainer;
	protected GalaxySomething game;
	
	public void init(AppGameContainer gc, GalaxySomething gs) {
		this.gameContainer = gc;
		this.game = gs;
	}
	
	public abstract void update(float tpf, int delta);
	
	public abstract void render(Graphics g);
	
	public void destroy() {
		
	}
	
	
	// input methods - keep in mind that the mouse wheel counts as a mouse button.
	
	/** Called when the mouse is moved while no mouse button is held down */
	public void mouseMoved(int oldX, int oldY, int newX, int newY) {}
	
	/** Called when the mouse is moved while at least one mouse button is held down */
	public void mouseDragged(int oldX, int oldY, int newX, int newY) {}
	
	/** Called once when a mouse button changes state from not being held down to being held down */
	public void mousePressed(int button, int x, int y) {}
	
	/** Called once when a mouse button changes state from being held down to not being held down */
	public void mouseReleased(int button, int x, int y) {}
	
	/** Called when a mouse button is released.
	 * If the mouse button is released for the second time in a short amount of time ("double click"),
	 * clickCount is 2. Otherwise, clickCount is 1.
	 */
	public void mouseClicked(int button, int x, int y, int clickCount) {}

	/** Called once when a key changes state from not being held down to being held down */
	public void keyPressed(int key, char c) {}

	/** Called once when a key changes state from being held down to not being held down */
	public void keyReleased(int key, char c) {}
	
	/** Called when the mouse wheel is moved.
	 * @param newValue is positive when the mouse wheel was moved upwards, negative otherwise.
	 */
	public void mouseWheelMoved(int newValue) {}
	
}
