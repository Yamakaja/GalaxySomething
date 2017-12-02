package procedural;

import main.Utilities.UtilRandom;

public class InfinitePerlin2D1D extends Perlin2D1D {
    
    public InfinitePerlin2D1D(int startingScale, int octaves) {
        Perlin2D1D(startingScale, octaves, null);
    }

	@Override
	protected Noise2D1D createNoise(UtilRandom random, float weight, float spacing) {
		return new InfiniteNoise2D1D(weight, spacing);
	}
}
