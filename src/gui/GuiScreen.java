package gui;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import main.InputConfiguration;

public class GuiScreen {
	
	// updates per second
	public static final int UPS = 40;
	
	private static GuiScreen NULL_SCREEN;
	
	public static GuiScreen nullScreen(AppGameContainer gc) {
		if (NULL_SCREEN == null)
			NULL_SCREEN = new GuiScreen(gc);
		return NULL_SCREEN;
	}
	
	private static final float UPDATE_INTERVAL = 1f / UPS;
	
	// basically a shadow container to contain + manage all the gui components
	protected GuiComponent container;
	
	protected AppGameContainer gc;
	
	private float updateCounter = 0;
	private int updateCounterMillis = 0;
	
	public GuiScreen(AppGameContainer gc) {
		this.gc = gc;
		container = new GuiComponent(gc, 1, 1);
		resolutionChanged();
	}
	
	public void attached() {
		
	}
	
	public void update(float tpf, int delta) {
		updateCounter += tpf;
		updateCounterMillis += delta;
		if (updateCounter >= UPDATE_INTERVAL) {
			container.update(updateCounter, updateCounterMillis);
			updateCounter = 0;
			updateCounterMillis = 0;
		}
	}
	
	public void render(Graphics g) {
		container.render(g);
	}
	
	public void detached() {
		
	}
	
	public GuiComponent getContainer() {
		return container;
	}
	
	public void resolutionChanged() {
		container.x = container.y = 0;
		container.width = gc.getWidth();
		container.height = gc.getHeight();
		container.calculateCoordinates();
	}
	
	// for docs on the input methods, see GameState#InputMethods
	// return true if input has been consumed, false otherwise

	public boolean mouseMoved(int oldX, int oldY, int newX, int newY) {
		return container.mouseMoved(oldX, oldY, newX, newY);
	}
	
	public boolean mouseDragged(int oldX, int oldY, int newX, int newY) {
		return container.mouseDragged(oldX, oldY, newX, newY);
	}
	
	public boolean mousePressed(int button, int x, int y) {
		return container.mousePressed(button, x, y);
	}
	
	public boolean mouseReleased(int button, int x, int y) {
		return container.mouseReleased(button, x, y);
	}
	
	public boolean mouseClicked(int button, int x, int y, int clickCount) {
		return container.mouseClicked(button, x, y, clickCount);
	}
	
	public boolean keyPressed(int key, char c) {
		if (key == Input.KEY_TAB) {
			if (InputConfiguration.isShiftDown()) {
				container.moveFocusBackward();
			}
			else {
				container.moveFocusForward();
			}
			return true;
		}
		else if (key == Input.KEY_ENTER) {
			if (container.performGenericAction())
				return true;
		}
		
		return container.keyPressed(key, c);
	}
	
	public boolean keyReleased(int key, char c) {
		return container.keyReleased(key, c);
	}
	
	public boolean mouseWheelMoved(int newValue) {
		return container.mouseWheelMoved(newValue);
	}
}
