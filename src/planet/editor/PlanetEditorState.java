package planet.editor;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import gui.GuiScreen;
import main.GalaxySomething;
import main.Cam;
import main.GameState;
import main.MainMenuState;
import main.Utilities.UtilRandom;
import planet.PlanetGenerator;
import planet.PlanetSurface;
import planet.PlanetType;
import planet.terrain.Constants;
import planet.terrain.Terrain;

/**
 * A fairly hacky testing environment.
 *
 */
public class PlanetEditorState extends GameState {
	
	private PlanetSurface planet;
	private Cam cam;
	
	private int selectedIndex = 0;
	private Terrain[] selection = Terrain.TERRAIN_ARRAY;
	private Terrain selectedTerrain = selection[selectedIndex];
	
	@Override
	public void init(AppGameContainer gc, GalaxySomething aow) {
		super.init(gc, aow);
		planet = PlanetGenerator.generatePlanet(new UtilRandom(), PlanetType.DWARF_PLANET);
		//planet = new PlanetSurface(TerrainType.MEDIUM_GRASS, PlanetColorSchemeDefinition.DEFAULT_COLOR_SCHEME.createColorScheme(new UtilRandom()),
		//						   gc.getWidth() / Constants.TERRAIN_FIELD_SIZE, gc.getHeight() / Constants.TERRAIN_FIELD_SIZE);
		cam = new Cam(gc);
		cam.zoom = Math.min(25, Math.max(planet.widthInTiles, planet.heightInTiles));
		cam.setCamBounds(0, 0, planet.widthInTotal, planet.heightInTotal);
		cam.setCamArea(0, 0, planet.widthInTotal, planet.heightInTotal);
		cam.setScreenArea(0, 0, gc.getWidth(), gc.getHeight());
		cam.setMaxRelativeOffset(0);
		cam.update(0, 0);
		
		aow.switchGuiScreen(GuiScreen.nullScreen(gc));
	}

	@Override
	public void update(float tpf, int delta) {
		planet.updatePlanet(tpf, delta);
		cam.update(tpf, delta);
	}

	@Override
	public void render(Graphics g) {
		planet.renderPlanet(g, cam);
	}
	
	@Override
	public void keyReleased(int key, char c) {
		if (key >= Input.KEY_1 && key <= Input.KEY_0) {
			selectTerrain(key - Input.KEY_1);
		}
		else if (key == Input.KEY_ESCAPE) {
			game.switchToState(new MainMenuState());
		}
		else if (key == Input.KEY_ADD) {
			selectTerrain(selectedIndex + 1);
		}
		else if (key == Input.KEY_SUBTRACT) {
			selectTerrain(selectedIndex - 1);
		}
	}
	
	private void selectTerrain(int index) {
		selectedIndex = (index + selection.length) % selection.length;
		selectedTerrain  = selection[selectedIndex];
	}
	
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		if (!cam.isOnRenderArea(x, y))
			return;
		
		int tileX = (int) (cam.toCamCoordinatesX(x) / Constants.TERRAIN_FIELD_SIZE);
		int tileY = (int) (cam.toCamCoordinatesY(y) / Constants.TERRAIN_FIELD_SIZE);
		
		planet.setTerrain(tileX, tileY, selectedTerrain);
	}
	
	@Override
	public void mouseDragged(int oldX, int oldY, int newX, int newY) {
		mouseClicked(0, newX, newY, 0);
	}
	
	@Override
	public void mouseWheelMoved(int newValue) {
		if (newValue > 0) {
			cam.changeZoom(1);
		}
		else {
			cam.changeZoom(-1);
		}
	}

}
