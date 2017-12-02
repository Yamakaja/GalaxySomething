package gui;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import tools.AABB;


public class GuiComponent {
	protected GuiComponent parent = null;
	
	protected ArrayList<GuiComponent> children = new ArrayList<>();
	
	// absolute AABB on screen
	protected AABB aabb = new AABB();
	
	// relative coordinates to parent
	protected float relativeX, relativeY, relativeWidth, relativeHeight;
	
	// actual coordinates on screen
	protected float x, y, width, height;
	
	protected int focussedIndex = -1;
	
	protected GameContainer gc;
	
	// the amount of direct (!) children that can achieve focus.
	private int amountOfFocusableDirectChildren = 0;
	
	// this component's index in its parent's children list
	private int indexInParent = -1;

	
	public GuiComponent(GameContainer gc, float relativeX, float relativeY, float relativeWidth, float relativeHeight) {
		this.gc = gc;
		this.relativeX = relativeX;
		this.relativeY = relativeY;
		this.relativeWidth = relativeWidth;
		this.relativeHeight = relativeHeight;
	}
	
	public GuiComponent(GameContainer gc, float relativeWidth, float relativeHeight) {
		this( gc, (1 - relativeWidth) / 2, (1 - relativeHeight) / 2, relativeWidth, relativeHeight );
	}
	
	/**
	 * Places this component so that n|m components with given relative size are equally spaced along the x|y axis,
	 * whereas this component covers grid slot nr. i|j (with i|j ranging in 0|0...n-1|m-1)
	 * @param relativeWidth
	 * @param relativeHeight
	 * @param i
	 * @param j
	 * @param n
	 * @param m
	 */
	public GuiComponent(GameContainer gc, float relativeWidth, float relativeHeight, int i, int j, int n, int m) {
		if (n * relativeWidth > 1 || m * relativeHeight > 1) {
			throw new NullPointerException("Error: invalid grid assignment!");
		}
		this.gc = gc;
		float relativeSpacingX = (1 - n * relativeWidth ) / n;
		float relativeSpacingY = (1 - m * relativeHeight) / m;
		this.relativeX = (i + 0.5f) * relativeSpacingX + i * relativeWidth;
		this.relativeY = (j + 0.5f) * relativeSpacingY + j * relativeHeight;
		this.relativeWidth = relativeWidth;
		this.relativeHeight = relativeHeight;
	}
	
	public void update(float tpf, int delta) {
		for (int i = 0; i < children.size(); i++) {
			children.get(i).update(tpf, delta);
		}
	}
	
	public void render(Graphics g) {
		for (int i = 0; i < children.size(); i++) {
			children.get(i).render(g);
		}
	}
	
	/** not really sure if this is even needed */
	public void destroy() {
		for (int i = 0; i < children.size(); i++) {
			children.get(i).destroy();
		}
	}
	
	public void attachChild(GuiComponent child) {
		child.indexInParent = children.size();
		children.add(child);
		child.parent = this;
		child.calculateCoordinates();
		if (child.canAchieveFocus()) {
			childBecameFocussable();
		}
	}
	
	public void removeChild(GuiComponent child) {
		children.remove(child.indexInParent);
		if (child.indexInParent == focussedIndex) {
			focussedIndex--;
			moveFocusForward();
		}
		else if (child.indexInParent > focussedIndex) {
			focussedIndex -= 2;
			moveFocusForward();
		}
		child.indexInParent = -1;
		child.parent = null;
		if (child.canAchieveFocus()) {
			childBecameUnfocussable();
		}
	}
	
	/**
	 * Assumes that parent size has already been calculated to new, correct values
	 * (or that if there is no parent then these component's size has been set manually externally)
	 */
	public void calculateCoordinates() {
		if (parent != null) {
			x = parent.x + relativeX * parent.width;
			y = parent.y + relativeY * parent.height;
			width = relativeWidth * parent.width;
			height = relativeHeight * parent.height;
			
		}
		
		aabb.set(x, y, x+width, y+height);
		
		GuiComponent child;
		for (int i = 0; i < children.size(); i++) {
			child = children.get(i);
			child.calculateCoordinates();
			// aabb.include(child.aabb); // should not be necessary
		}
	}
	
	/**
	 * Tell this GUI component that it is focussed.
	 * That will either focus the gui component, add focus to a child component of this gui component
	 * or move the focus within this GUI component to the next child component.
	 * @return Whether the focus is still on this GUI component or one of its children
	 */
	public boolean moveFocusForward() {
		if (amountOfFocusableDirectChildren == 0)
			return false;
		
		// if focus can be achieved, there are children
		
		if (focussedIndex == -1) {
			focussedIndex = 0;
		}
		
		for ( ; focussedIndex < children.size(); focussedIndex++) {
			if (children.get(focussedIndex).moveFocusForward()) {
				return true;
			}
		}
		
		focussedIndex = -1;
		
		return false;
	}
	
	/**
	 * @see GuiComponent#moveFocusForward()
	 */
	public boolean moveFocusBackward() {
		if (amountOfFocusableDirectChildren == 0 || focussedIndex == -1)
			return false;
		
		for ( ; focussedIndex >= 0; focussedIndex--) {
			if (children.get(focussedIndex).moveFocusBackward()) {
				return true;
			}
		}
		
		//focussedIndex is now -1 due to for loop condition.
		
		return false;
	}
	
	protected void childBecameFocussable() {
		amountOfFocusableDirectChildren++;
		if (amountOfFocusableDirectChildren == 1 && parent != null) {
			// this component just became focusable, tell parent
			parent.childBecameFocussable();
		}
	}
	
	protected void childBecameUnfocussable() {
		amountOfFocusableDirectChildren--;
		if (amountOfFocusableDirectChildren == 0 && parent != null) {
			// this component just became unfocusable, tell parent
			parent.childBecameUnfocussable();
		}
	}
	
	public boolean canAchieveFocus() {
		return amountOfFocusableDirectChildren != 0;
	}
	
	public boolean isHovered() {
		return aabb.contains(gc.getInput().getMouseX(), gc.getInput().getMouseY());
	}
	
	// perform generic action if component is activated via keyboard controls
	public boolean performGenericAction() {
		return focussedIndex != -1 ? children.get(focussedIndex).performGenericAction() : false;
	}
	
	// for docs on the input methods, see GameState#InputMethods
	// return true if input has been consumed, false otherwise

	public boolean mouseMoved(int oldX, int oldY, int newX, int newY) {
		if (aabb.contains(oldX, oldY) || aabb.contains(newX, newY)) {
			for (int i = 0; i < children.size(); i++) {
				if (children.get(i).mouseMoved(oldX, oldY, newX, newY))
					return true;
			}
		}
		
		return false;
	}
	
	public boolean mouseDragged(int oldX, int oldY, int newX, int newY) {
		if (aabb.contains(oldX, oldY) || aabb.contains(newX, newY)) {
			for (int i = 0; i < children.size(); i++) {
				if (children.get(i).mouseDragged(oldX, oldY, newX, newY))
					return true;
			}
		}
		
		return false;
	}
	
	public boolean mousePressed(int button, int x, int y) {
		if (aabb.contains(x, y)) {
			for (int i = 0; i < children.size(); i++) {
				if (children.get(i).mousePressed(button, x, y))
					return true;
			}
		}
		
		return false;
	}
	
	public boolean mouseReleased(int button, int x, int y) {
		if (aabb.contains(x, y)) {
			for (int i = 0; i < children.size(); i++) {
				if (children.get(i).mouseReleased(button, x, y))
					return true;
			}
		}
		
		return false;
	}
	
	public boolean mouseClicked(int button, int x, int y, int clickCount) {
		if (aabb.contains(x, y)) {
			for (int i = 0; i < children.size(); i++) {
				if (children.get(i).mouseClicked(button, x, y, clickCount))
					return true;
			}
		}
		
		return false;
	}
	
	public boolean keyPressed(int key, char c) {
		return focussedIndex != -1 ? children.get(focussedIndex).keyPressed(key, c) : false;
	}
	
	public boolean keyReleased(int key, char c) {
		return focussedIndex != -1 ? children.get(focussedIndex).keyReleased(key, c) : false;
	}
	
	public boolean mouseWheelMoved(int newValue) {
		return focussedIndex != -1 ? children.get(focussedIndex).mouseWheelMoved(newValue) : false;
	}
}
