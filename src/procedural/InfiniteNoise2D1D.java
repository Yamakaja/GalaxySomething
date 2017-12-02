package procedural;

import main.Utilities;
import main.Utilities.UtilRandom;

public class InfiniteNoise2D1D extends Noise2D1D {
    private int seed;
    
    public InfiniteNoise2D1D(float weight, float spacing) {
    	this (Utilities.RANDOM, weight, spacing);
    }
    
    public InfiniteNoise2D1D(UtilRandom random, float weight, float spacing) {
        super(weight, spacing);
        if (PerlinConfiguration.useReproducibleRandom())
            seed = (int) (64 * spacing);
        else
            seed = random.nextInt(128);
    }
    
    @Override
    protected float getValueAt(int x, int y) {
        return (Utilities.pseudoRandomFloat(seed, x, y) + 1) * 0.5f;
    }
    
}
