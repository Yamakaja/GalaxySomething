package procedural;

import main.Utilities;
import main.Utilities.UtilRandom;

public abstract class Perlin2D1D {
	
	protected Noise2D1D[] noises;
    
    protected float minValue = 0, maxValue = 1;
	
	// default constructor -- initialization needs overridden methods, which need initialized class members. Thus a method instead.
	protected void Perlin2D1D(int startingScale, int octaves, UtilRandom random) {
		noises = new Noise2D1D[octaves];
		
        float weight = .5f;
        float spacing = startingScale;
        
        for (int i = 0; i < octaves; i++) {
        	noises[i] = createNoise(random, weight, spacing * pseudoRandomFactor(random, startingScale, octaves, i));
            weight *= .5f;
            spacing *= .5f;
        }
	}
	
	protected float pseudoRandomFactor(UtilRandom random, int startingScale, int octaves, int i) {
		return random != null ?
				1 + random.nextFloat(-0.15f, 0.15f)
			  : 1 + Utilities.pseudoRandomFloat(startingScale * octaves / 3, 0, i)*0.15f;
	}
	
	protected abstract Noise2D1D createNoise(UtilRandom random, float weight, float spacing);
    
    /** because "standard" perlin noise is (disappointingly) average,
     * this method serves to scale the noise relatively to its min and max value in the given "area"
     * to achieve a more exciting value range.
     */
    public Perlin2D1D prefilter(int width, int height) {
        float min = 1, max = 0;
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                float f = unscaledValue(i, j);
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

    public float getValue(float x, float y) {
        float value = unscaledValue(x, y);
        return (value - minValue) / (maxValue - minValue);
    }
    
    protected float unscaledValue(float x, float y) {
        float sum = 0;
        for (int i = 0; i < noises.length; i++) {
            sum += noises[i].getWeightedValue(x, y);
        }
        return sum;
    }
}
