package main;

import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class ImageManager {
	
	private static HashMap<String, SpriteSheet> loadedSheets = new HashMap<>();
	
	private static HashMap<String, Image> loadedImages = new HashMap<>();
	
	private static final SpriteSheet ERROR_IMAGE;
	
	static {
		ERROR_IMAGE = null; // maybe throw something together here, sometime
	}
	
	private ImageManager() {}
	
	public static Image loadImage(String ref) {
		return loadImage(ref, true);
	}
	
	public static Image loadImage(String ref, boolean cache) {
		if (ref.length() <= 4 || ref.charAt(ref.length() - 4) != '.') {
			ref = ref + ".png";
		}
		Image img = loadedImages.get(ref);
		if (img == null) {
			String resourceReference;
			try {
				img = new Image(ref);
				resourceReference = img.getResourceReference();
			}
			catch (Throwable t) {
				ExceptionHandler.handleErrorLoadingImage(t);
				return ERROR_IMAGE;
			}
			loadedImages.put(ref, img);
			loadedImages.put(resourceReference, img);
		}
		return img;
	}
	
	public static Image createEmptyImage(int width, int height) {
		try {
			return new Image(width, height);
		}
		catch (Throwable t) {
			ExceptionHandler.handleErrorCreatingImage(t);
			return ERROR_IMAGE;
		}
	}
	
	public static SpriteSheet loadSpriteSheet(String ref) {
		//SpriteSheets are buffered TWICE in the loadedSheets HashMap - with the loading path given to the RenderManager, AND with the path Slick references them with.
		SpriteSheet sheet = loadedSheets.get(ref);
		if (sheet == null) {
			String resourceReference;
			try {
				sheet = new SpriteSheet(ref, 16, 16); // last two params effectively don't matter here
				resourceReference = sheet.getResourceReference();
			} catch (Throwable t) {
				ExceptionHandler.handleErrorLoadingImage(t);
				return ERROR_IMAGE;
			}
			loadedSheets.put(ref, sheet);
			loadedImages.put(ref, sheet);
			loadedSheets.put(resourceReference, sheet);
			loadedImages.put(resourceReference, sheet);
		}
		return sheet;
	}
}
