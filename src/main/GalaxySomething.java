package main;

import java.io.File;

import org.newdawn.slick.*;

import planet.terrain.GrayscaleConverter;
import gui.GuiScreen;

public class GalaxySomething extends BasicGame {
	// a flag so the resolution is preset to 800 x 600 during the tutorial^^
	public static final boolean IS_IN_TUT = false;
	
	private GameState currentState;
	
	private AppGameContainer gameContainer;
	
	private GuiScreen guiScreen;
	
	
	public GalaxySomething() {
		super("Galaxy-Something");
	}
	
	private GameState getFirstGameState() {
		//return new example.shader.ShaderTestAdvanced();
		//return new example.shader.ShaderLesson1();
		//return new example.shader.ShaderTest();
		return new MainMenuState();
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		try {
			
			GrayscaleConverter.convertAll(new File(FilePaths.GRAYSCALE_CONVERSION_INPUT_DIR), new File(FilePaths.GRAYSCALE_CONVERSION_OUTPUT_DIR), false);
			
			this.gameContainer = (AppGameContainer) container;
			guiScreen = GuiScreen.nullScreen(gameContainer);
			
			currentState = getFirstGameState();
			currentState.init(gameContainer, this);
			
			
		}
		catch(Throwable t) {
			t.printStackTrace();
			System.exit(0);
		}
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		try {
			float tpf = delta / 1000f;
			currentState.update(tpf, delta);
			guiScreen.update(tpf, delta);
			
			if (gameContainer.wasResized()) {
				System.out.println("Game container size is: " + gameContainer.getWidth() + " | " + gameContainer.getHeight());
				
			}
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		try {
			currentState.render(g);
			guiScreen.render(g);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void switchToState(GameState nextState) {
		try {
			currentState.destroy();
			currentState = nextState;
			currentState.init(gameContainer, this);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void switchGuiScreen(GuiScreen nextScreen) {
		try {
			guiScreen.detached();
			guiScreen = nextScreen;
			guiScreen.attached();
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	public GameContainer getGameContainer() {
		return gameContainer;
	}
	
	@Override
	public void mouseMoved(int oldX, int oldY, int newX, int newY) {
		try {
			if (guiScreen.mouseMoved(oldX, oldY, newX, newY))
				return;
			
			currentState.mouseMoved(oldX, oldY, newX, newY);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	@Override
	public void mouseDragged(int oldX, int oldY, int newX, int newY) {
		try {
			if (guiScreen.mouseDragged(oldX, oldY, newX, newY))
				return;
			
			currentState.mouseDragged(oldX, oldY, newX, newY);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	@Override
	public void mousePressed(int button, int x, int y) {
		try {
			if (guiScreen.mousePressed(button, x, y))
				return;
			
			currentState.mousePressed(button, x, y);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	@Override
	public void mouseReleased(int button, int x, int y) {
		try {
			if (guiScreen.mouseReleased(button, x, y))
				return;
			
			currentState.mouseReleased(button, x, y);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		try {
			if (guiScreen.mouseClicked(button, x, y, clickCount))
				return;
			
			currentState.mouseClicked(button, x, y, clickCount);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	@Override
	public void keyPressed(int key, char c) {
		try {
			if (guiScreen.keyPressed(key, c))
				return;
			
			currentState.keyPressed(key, c);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	@Override
	public void keyReleased(int key, char c) {
		try {
			if (guiScreen.keyReleased(key, c))
				return;
			
			currentState.keyReleased(key, c);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	@Override
	public void mouseWheelMoved(int newValue) {
		try {
			if (guiScreen.mouseWheelMoved(newValue))
				return;
			
			currentState.mouseWheelMoved(newValue);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
}
