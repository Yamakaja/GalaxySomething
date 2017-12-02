package planet.terrain;

import org.newdawn.slick.BigImage;

public final class Constants {
	
	public static final int TERRAIN_FIELD_SIZE = 128;
	
	public static final int TERRAIN_IMAGE_SIZE = 128;
	
	public static final int HALF_TERRAIN_IMAGE_SIZE = TERRAIN_IMAGE_SIZE / 2;
	
	// on my modern GPU, the max texture size is 32768. Which is a bit too much.
	// (in fact, trying to create an image of that size causes a crash ... )
	// 8192 seems like a reasonable limit to me, since it equals to 64 x 64 = 4096 images (at image size 128x128).
	public static final int MAX_ATLAS_SIZE = Math.min(8192, BigImage.getMaxSingleImageSize());
	
	private Constants() {} //class is not supposed to be instantiated
}
