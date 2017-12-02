package procedural;

import main.Utilities.UtilRandom;

public class FinitePerlin2D1D extends Perlin2D1D {
	
	private float minX, minY, maxX, maxY;
	
	/**
	 * @param min min coords that will be requested (inclusive)
	 * @param max max coords that will be requested (exclusive)
	 */
	public FinitePerlin2D1D(UtilRandom random, int startingScale, int octaves, float minX, float minY, float maxX, float maxY) {
		
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
		
		// now call parent "constructor"
		Perlin2D1D(startingScale, octaves, random);
	}
	
	/**
	 * @param max max coords that will be requested (exclusive). Min coords are assumed to be 0|0 (inclusive)
	 */
	public FinitePerlin2D1D(UtilRandom random, int startingScale, int octaves, float maxX, float maxY) {
		this(random, startingScale, octaves, 0, 0, maxX, maxY);
	}

	@Override
	protected Noise2D1D createNoise(UtilRandom random, float weight, float spacing) {
		return new FiniteNoise2D1D(random, weight, spacing, minX, minY, maxX, maxY);
	}
    
    /** 
     * @see Perlin2D1D#prefilter(int, int)
     */
    public FinitePerlin2D1D prefilter() {
        float min = 1, max = 0;
        
        for (int x = (int) (minX - 1); x < (int) (maxX + 1); x++) {
            for (int y = (int) (minY - 1); y < (int) (maxY + 1); y++) {
                float f = unscaledValue(x, y);
                if (f > max)
                    max = f;
                else if (f < min)
                    min = f;
            }
        }
        minValue = min;
        maxValue = max;
        
        //main.Logger.print("maxH: " + maxH + ", minH: " + minH);
        
        return this;
    }
}
