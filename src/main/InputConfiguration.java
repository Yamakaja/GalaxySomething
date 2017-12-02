package main;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Input;

/**
 * Temporary solution for now. I'm not entirely pleased with this as it is.
 *
 */
public final class InputConfiguration {
	
	// HAVE to separate between mouse and key input, since both take different methods to check for
	
	// Keyboard input
	
	// First level: OR, Second level: AND
	
	public static final int[][] KEY_MOVE_UP = {{Input.KEY_W}, {Input.KEY_UP}};
	public static final int[][] KEY_MOVE_LEFT = {{Input.KEY_A}, {Input.KEY_LEFT}};
	public static final int[][] KEY_MOVE_DOWN = {{Input.KEY_S}, {Input.KEY_DOWN}};
	public static final int[][] KEY_MOVE_RIGHT = {{Input.KEY_D}, {Input.KEY_RIGHT}};
	
	public static final int[][] MOVEMENT_SPEED_MODIFIER = {{Input.KEY_LSHIFT}, {Input.KEY_RSHIFT}};
	
	// Utility
	
	public static boolean isCtrlDown() {
		return Keyboard.isKeyDown(Input.KEY_LCONTROL) || Keyboard.isKeyDown(Input.KEY_RCONTROL);
	}
	
	public static boolean isShiftDown() {
		return Keyboard.isKeyDown(Input.KEY_LSHIFT) || Keyboard.isKeyDown(Input.KEY_RSHIFT);
	}
	
	public static boolean isKeyDown(int[][] key) {
		boolean allTrue;
		for (int i = 0; i < key.length; i++) {
			allTrue = true;
			for (int j = 0; j < key[i].length; j++) {
				if (!Keyboard.isKeyDown(key[i][j])) {
					allTrue = false;
					break;
				}
			}
			if (allTrue)
				return true;
		}
		return false;
	}
	
	public static boolean isAnyMouseButtonDown(Input input) {
		return input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) || input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON) || input.isMouseButtonDown(Input.MOUSE_MIDDLE_BUTTON);
	}
	
	private InputConfiguration () {}
}
