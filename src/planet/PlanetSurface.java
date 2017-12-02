package planet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.shader.ShaderProgram;

import main.Cam;
import main.ExceptionHandler;
import main.ImageEditor;
import main.Shaders;
import planet.terrain.BiomeData;
import planet.terrain.Constants;
import planet.terrain.TerrainImage;
import planet.terrain.TerrainImageAtlas;
import planet.terrain.TerrainImageManager;
import planet.terrain.TerrainTile;
import planet.terrain.TerrainVariation;
import planet.terrain.Terrain;

public class PlanetSurface {
	
	public final int widthInTiles, heightInTiles;
	public final int widthInTotal, heightInTotal;
	
	private TerrainTile[][] tiles;
	
	// Images connected to terrain[x][y] are terrainImages [x][y], [x+1][y], [x][y+1] and [x+1][y+1]
	// Wrapped in a "holder" class so I can adequately manage update enquiries
	// (if I put tiles in there, lots of images would be unnecessarily updated twice. Not nice. Do not do.)
	private TerrainImageHolder[][] terrainImages;
	
	private PlanetColorScheme colorScheme;
	
	private Image minimap;
	
	// Solely to adapt the minimap when terrain has changed,
	// NOT to update the tile images themselves!
	private HashSet<TerrainTile> changedTilesSinceLastUpdate = new HashSet<>();
	
	private boolean terrainHasChanged = true;
	private int renderX, renderY, renderX2, renderY2; // terrain area that was rendered in the last render call
	private HashMap<TerrainImageAtlas, ArrayList<TerrainImageHolder>> terrainTileRenderMap = new HashMap<>(); // temporarily cached for optimized rendering
	
	private ShaderProgram terrainShader = Shaders.getPlanetTerrainShader();
	
	/**
	 * 
	 * @param terrainType The "default" terrain type that all terrain tiles will be initialized with
	 */
	public PlanetSurface(final Terrain terrain, PlanetColorScheme colorScheme, int width, int height) {
		this (new TerrainTileProvider() {
			TerrainVariation variation = colorScheme.colorOf(terrain);
			@Override
			public TerrainTile createTileAt(int x, int y, PlanetSurface planet) {
				return new TerrainTile(variation, x, y, planet);
			}
		}, colorScheme, width, height);
	}
	
	public PlanetSurface(BiomeData biomeData, int width, int height) {
		this(biomeData, biomeData.colorScheme, width, height);
	}
	
	public PlanetSurface(TerrainTileProvider tileProvider, PlanetColorScheme colorScheme, int width, int height) {
		this.colorScheme = colorScheme;
		widthInTiles = width;
		heightInTiles = height;
		widthInTotal = width * Constants.TERRAIN_FIELD_SIZE;
		heightInTotal = height * Constants.TERRAIN_FIELD_SIZE;
		
		tiles = new TerrainTile[width][height];
		terrainImages = new TerrainImageHolder[width+1][height+1];
		ImageBuffer minimapBuffer = new ImageBuffer(width, height);
		
		Color color;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				tiles[x][y] = tileProvider.createTileAt(x, y, this);
				color = tiles[x][y].getAverageColor();
				minimapBuffer.setRGBA(x, y, ImageEditor.toInt(color.r),
											ImageEditor.toInt(color.g),
											ImageEditor.toInt(color.b),
											255);
				initImageHolder(x, y);
			}
			initImageHolder(x, height);
		}
		for (int y = 0; y <= height; y++) {
			initImageHolder(width, y);
		}
		
		minimap = minimapBuffer.getImage();
	}
	
	private void initImageHolder(int x, int y) {
		terrainImages[x][y] = new TerrainImageHolder(x, y);
		int left, up, right, down;
		left  = clampX(x - 1);
		right = clampX(x); //yes, gotta clamp "normal" coords too
		up    = clampY(y - 1);
		down  = clampY(y);
		terrainImages[x][y].setTiles(tiles[left][up],   tiles[right][up],
	   			 					 tiles[left][down], tiles[right][down]);
	}
	
	public boolean setTerrain(int x, int y, Terrain terrain) {
		if (tiles[x][y].setTerrain(colorScheme.colorOf(terrain))) {
			markImagesAtTileForUpdate(x, y);
			changedTilesSinceLastUpdate.add(tiles[x][y]);
			terrainHasChanged = true;
			return true;
		}
		return false;
	}
	
	private void markImagesAtTileForUpdate(int x, int y) {
		// need to update the neighborhood
		terrainImages[x][y].markForUpdate();
		terrainImages[x][y+1].markForUpdate();
		terrainImages[x+1][y].markForUpdate();
		terrainImages[x+1][y+1].markForUpdate();
	}
	
	public void updatePlanet(float tpf, int delta) {
		if (changedTilesSinceLastUpdate.size() > 0) {
			try {
				Graphics g = minimap.getGraphics();
				for (TerrainTile tile : changedTilesSinceLastUpdate) {
					g.setColor(tile.getAverageColor());
					g.fillRect(tile.x, tile.y, 1, 1);
				}
				changedTilesSinceLastUpdate.clear();
				g.flush();
			}
			catch (SlickException se) {
				ExceptionHandler.handleUndefinedError(se);
			}
		}
	}
	
	public void renderPlanet(Graphics g, Cam cam) {
		renderTerrain(cam);
		
		// determine render area in world field coordinates
		int startX = (int) (cam.camX / Constants.TERRAIN_FIELD_SIZE);
		if (startX < 0)
			startX = 0;
		
		int startY = (int) (cam.camY / Constants.TERRAIN_FIELD_SIZE);
		if (startY < 0)
			startY = 0;
		
		int endX = (int) ((cam.camX+cam.camWidth) / Constants.TERRAIN_FIELD_SIZE);
		if (endX >= widthInTiles)
			endX = widthInTiles-1;
		
		int endY = (int) ((cam.camY+cam.camHeight) / Constants.TERRAIN_FIELD_SIZE);
		if (endY >= heightInTiles)
			endY = heightInTiles-1;
		
		//TODO render more planet stuff here later on
	}
	
	private void renderTerrain(Cam cam) {
		// determine render area in planet field coordinates
		int startX = (int) ((cam.camX + Constants.HALF_TERRAIN_IMAGE_SIZE) / Constants.TERRAIN_FIELD_SIZE);
		if (startX < 0)
			startX = 0;
		
		int startY = (int) ((cam.camY +  Constants.HALF_TERRAIN_IMAGE_SIZE) / Constants.TERRAIN_FIELD_SIZE);
		if (startY < 0)
			startY = 0;
		
		int endX = (int) ((cam.camX+cam.camWidth + Constants.HALF_TERRAIN_IMAGE_SIZE) / Constants.TERRAIN_FIELD_SIZE);
		if (endX > widthInTiles)
			endX = widthInTiles;
		
		int endY = (int) ((cam.camY+cam.camHeight + Constants.HALF_TERRAIN_IMAGE_SIZE) / Constants.TERRAIN_FIELD_SIZE);
		if (endY > heightInTiles)
			endY = heightInTiles;
		
		ArrayList<TerrainImageHolder> respectiveList;
		
		if (terrainHasChanged || startX != renderX || startY != renderY || endX != renderX2 || endY != renderY2) {
			// Zu rendernde TerrainTiles neu sortieren
			terrainTileRenderMap.clear();
			
			for (int x = startX; x <= endX; x++) {
				for (int y = startY; y <= endY; y++) {
					terrainImages[x][y].prepareForRender();
					respectiveList = terrainTileRenderMap.get(terrainImages[x][y].image.parent);
					if (respectiveList == null)
						terrainTileRenderMap.put(terrainImages[x][y].image.parent, respectiveList = new ArrayList<>());
					respectiveList.add(terrainImages[x][y]);
				}
			}
			
			terrainHasChanged = false;
			renderX = startX;
			renderY = startY;
			renderX2 = endX;
			renderY2 = endY;
		}
		
		@SuppressWarnings("unused")
		TerrainImageAtlas atlas;
		TerrainImageHolder imageHolder;
		float x, y;
		final float epsilon = main.Constants.RENDER_EPSILON * cam.ratio;
		
		
		terrainShader.bind();
		
		// TODO draw tiles on BUFFER IMAGE using start() and end() and NO SHADER!
		// Then draw BUFFER IMAGE on SCREEN using SHADER
		// BOOM only 2x binding stuff per render!! :D :D
		
		for (Entry<TerrainImageAtlas, ArrayList<TerrainImageHolder>> entry : terrainTileRenderMap.entrySet()) {
			// time to actually render the terrain
			atlas = entry.getKey();
			respectiveList = entry.getValue();
			
			atlas.startUse(); // beginning embedded use on a texture automatically unbinds the shader... need to dig into OpenGL / Slick to find out why...
			
			for (int i = 0; i < respectiveList.size(); i++) {
				imageHolder = respectiveList.get(i);
				
				x = cam.offsetX + (imageHolder.x * Constants.TERRAIN_FIELD_SIZE - cam.camX - Constants.HALF_TERRAIN_IMAGE_SIZE) * cam.ratio;
				y = cam.offsetY + (imageHolder.y * Constants.TERRAIN_FIELD_SIZE - cam.camY - Constants.HALF_TERRAIN_IMAGE_SIZE) * cam.ratio;
				
				terrainShader.setUniform3f("rColor", imageHolder.upperLeft.getColor().r,  imageHolder.upperLeft.getColor().g, imageHolder.upperLeft.getColor().b);
				terrainShader.setUniform3f("gColor", imageHolder.upperRight.getColor().r, imageHolder.upperRight.getColor().g, imageHolder.upperRight.getColor().b);
				terrainShader.setUniform3f("bColor", imageHolder.lowerLeft.getColor().r,  imageHolder.lowerLeft.getColor().g, imageHolder.lowerLeft.getColor().b);
				terrainShader.setUniform3f("aColor", imageHolder.lowerRight.getColor().r, imageHolder.lowerRight.getColor().g, imageHolder.lowerRight.getColor().b);

				// TODO this should be abstracted out into the camera
				imageHolder.image.image.draw(
						x - epsilon,
						y - epsilon,
						x + Constants.TERRAIN_FIELD_SIZE * cam.ratio + epsilon,
						y + Constants.TERRAIN_FIELD_SIZE * cam.ratio + epsilon,
						main.Constants.RENDER_EPSILON,
						main.Constants.RENDER_EPSILON,
						Constants.TERRAIN_IMAGE_SIZE - main.Constants.RENDER_EPSILON,
						Constants.TERRAIN_IMAGE_SIZE - main.Constants.RENDER_EPSILON);
			}
			
			//atlas.endUse();
		}
		
		terrainShader.unbind();
	}
	
	// Gnampf if by changing a color the luminance is changed, all affected terraintiles will still need to update their image...
	
	public TerrainVariation getColorOf(Terrain terrain) {
		return colorScheme.colorOf(terrain);
	}
	
	private int clampX(int x) {
		return x < 0 ? 0 : x >= widthInTiles ? widthInTiles - 1 : x;
	}
	
	private int clampY(int y) {
		return y < 0 ? 0 : y >= heightInTiles ? heightInTiles - 1 : y;
	}
	
	public static abstract class TerrainTileProvider {
		public abstract TerrainTile createTileAt(int x, int y, PlanetSurface planet);
	}
	
	private static class TerrainImageHolder {
		// indices in array
		public final int x, y;
		public TerrainImage image;
		
		// keep in mind that the TerrainTile references below could all be to the same instance (e.g. on the border)
		public TerrainTile upperLeft;
		public TerrainTile upperRight;
		public TerrainTile lowerLeft;
		public TerrainTile lowerRight;
		
		public TerrainImageHolder(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public void setTiles(TerrainTile upperLeftTile, TerrainTile upperRightTile,
							 TerrainTile lowerLeftTile, TerrainTile lowerRightTile) {
			upperLeft = upperLeftTile;
			upperRight = upperRightTile;
			lowerLeft = lowerLeftTile;
			lowerRight = lowerRightTile;
		}
		
		public void updateImage() {
			image = TerrainImageManager.getCombination(upperLeft.getLuminance(), upperRight.getLuminance(),
													   lowerLeft.getLuminance(), lowerRight.getLuminance());
		}
		
		public void prepareForRender() {
			if (image == null)
				updateImage();
		}
		
		public void markForUpdate() {
			image = null;
		}
	}
}
