package main;

import org.newdawn.slick.Image;
import org.newdawn.slick.ImageData;

public class GrayscaleImage {
	public final float averageValue;
	
	private final float[][] values;
	
	public GrayscaleImage(Image image) {
		this(image, true);
	}
	
	public GrayscaleImage(Image image, boolean destroyImage) {
		ImageData data = new ImageData(image).setAlphaFromGrayscale();
		
		values = new float[data.imageWidth][data.imageHeight];
		
		float sum = 0;
		
		for (int y = 0; y < data.imageHeight; y++) {
			for (int x = 0; x < data.imageWidth; x++) {
				values[x][y] = data.getColor(x, y).a;
				sum += values[x][y];
			}
		}
		
		averageValue = sum / (data.imageWidth * data.imageHeight);
		
		if (destroyImage) {
			Utilities.handleDestruction(image);
		}
	}
	
	public float getValue(int x, int y) {
		return values[x][y];
	}
	
	public int getWidth() {
		return values.length;
	}
	
	public int getHeight() {
		return values[0].length;
	}
}
