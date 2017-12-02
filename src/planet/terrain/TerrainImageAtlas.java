package planet.terrain;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import main.ExceptionHandler;
import main.Utilities;

public class TerrainImageAtlas {
	public static final int IMAGES_PER_AXIS = Constants.MAX_ATLAS_SIZE / Constants.TERRAIN_IMAGE_SIZE;
	public static final int MAX_IMAGES_CONTAINED = IMAGES_PER_AXIS * IMAGES_PER_AXIS;
	
	public final int ID;
	
	private int amountOfSpritesContained;
	private Image atlas;
	private Graphics atlasGraphics;
	
	public TerrainImageAtlas(int ID) {
		this.ID = ID;
		amountOfSpritesContained = 0;
		
		try {
			atlas = new Image(Constants.MAX_ATLAS_SIZE, Constants.MAX_ATLAS_SIZE);
			
			atlas.setBlendMode(Image.BLEND_MODE_ONLY_SOURCE);
			
			atlasGraphics = atlas.getGraphics();
		}
		catch (Throwable t) {
			ExceptionHandler.handleErrorCreatingImage(t);
		}
	}
	
	public boolean hasCapacity() {
		return amountOfSpritesContained < MAX_IMAGES_CONTAINED;
	}
	
	/** Stamp an image onto the atlas. The original image will be destroyed. */
	public TerrainImage stampIntoAtlas(Image image) {
		int x1 = (amountOfSpritesContained % IMAGES_PER_AXIS) * Constants.TERRAIN_IMAGE_SIZE;
		int y1 = (amountOfSpritesContained / IMAGES_PER_AXIS) * Constants.TERRAIN_IMAGE_SIZE;
		int x2 = x1 + Constants.TERRAIN_IMAGE_SIZE;
		int y2 = y1 + Constants.TERRAIN_IMAGE_SIZE;
		
		atlasGraphics.drawImage(image, x1, y1, x2, y2, 0, 0, image.getWidth(), image.getHeight());
		
		atlasGraphics.flush();
		
		amountOfSpritesContained++;
		
		if (amountOfSpritesContained == MAX_IMAGES_CONTAINED) {
			atlasGraphics = null;
		}
		
		Utilities.handleDestruction(image);
		
		return new TerrainImage(atlas.getSubImage(x1, y1, Constants.TERRAIN_IMAGE_SIZE, Constants.TERRAIN_IMAGE_SIZE), this);
	}
	
	public void startUse() {
		atlas.startUse();
	}
	
	public void endUse() {
		atlas.endUse();
	}
}