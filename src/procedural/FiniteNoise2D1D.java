package procedural;

import main.Utilities.UtilRandom;

public class FiniteNoise2D1D extends Noise2D1D {
	
	private float[][] values;
	private int offsetX, offsetY;
	
	/**
	 * @param min min coords that will be requested (inclusive)
	 * @param max max coords that will be requested (exclusive)
	 */
	public FiniteNoise2D1D(UtilRandom random, float weight, float spacing, float minX, float minY, float maxX, float maxY) {
		super(weight, spacing);
		
		int minIndexX = indexOf(minX - 1) - 1;
		int minIndexY = indexOf(minY - 1) - 1; // -1 bzw. +2 'cause cubic interpolation increases range by some bit. Add another one because prefilter security.
		int maxIndexX = indexOf(maxX + 1) + 2;
		int maxIndexY = indexOf(maxY + 1) + 2;
		
		offsetX = -minIndexX;
		offsetY = -minIndexY;
		
		values = new float[maxIndexX - minIndexX + 1][maxIndexY - minIndexY + 1];
		
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				values[i][j] = random.nextFloat();
			}
		}
	}
	
	/**
	 * @param max max coords that will be requested (inclusive). Min coords are assumed to be 0|0 (inclusive)
	 */
	public FiniteNoise2D1D(UtilRandom random, float weight, float spacing, float maxX, float maxY) {
		this(random, weight, spacing, 0, 0, maxX, maxY);
	}
	
	@Override
	protected float getValueAt(int x, int y) {
		//main.Logger.print("Getting value at index " + x + "|" + y);
		return values[offsetX + x][offsetY + y];
	}
	
}
