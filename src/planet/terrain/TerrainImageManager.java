package planet.terrain;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;

import main.ImageEditor;
import main.Utilities;
import procedural.FinitePerlin2D1D;

public class TerrainImageManager {
	
	public static final int MAX_NUM_VARIATIONS_PER_COMBO = 4;
	
	public static final int UPPER_LEFT = 0;
	public static final int UPPER_RIGHT = 1;
	public static final int LOWER_LEFT = 2;
	public static final int LOWER_RIGHT = 3;
	
	private static final ArrayList<TerrainImageAtlas> atlasList;
	
	private static final HashMap<TerrainTuple, TerrainImage> blendedTerrainCache;
	
	private static final float[] WEIGHTING = new float[Constants.TERRAIN_IMAGE_SIZE];
	
	private static final float MAX_WARP = Constants.TERRAIN_IMAGE_SIZE * 12f / 64f;
	
	static {
		atlasList = new ArrayList<>();
		atlasList.add(new TerrainImageAtlas(0));
		
		blendedTerrainCache = new HashMap<>();
		
		for (int i = 0; i < Constants.TERRAIN_IMAGE_SIZE; i++) {
			WEIGHTING[i] = (
							//0) + main.Utilities.smootherstep(0, 1,
							(i + 0.5f) / Constants.TERRAIN_IMAGE_SIZE);
		}
	}
	
	public static TerrainImage getCombination(TerrainLuminance... terrainTypes) {
		boolean allSame = true; // don't need no procedural weighting when it has no effect on the result anyway. Would be a waste of memory then.
		
		for (int i = 0; i < 4; i++) {
			allSame = allSame && terrainTypes[i] == terrainTypes[0];
		}
		
		int variation = allSame ? 0 : Utilities.RANDOM.nextInt(MAX_NUM_VARIATIONS_PER_COMBO);
		
		TerrainTuple tuple = new TerrainTuple(variation, terrainTypes);
		TerrainImage cached = blendedTerrainCache.get(tuple);
		
		if (cached == null) {
			// Must NOT check for rotations since textures themselves are NOT invariant to rotation!!
			
			cached = stampIntoAtlas(blend(terrainTypes).setBlendMode(Image.BLEND_MODE_ONLY_SOURCE));
			// yes, even for an image that only exists for a short blink in time the blend mode needs to be set.
			// otherwise, OpenGL will happily blend its values away into the void when it is drawn on the atlas.
			
			cached.image.setBlendMode(Image.BLEND_MODE_ONLY_SOURCE);
			
			blendedTerrainCache.put(tuple, cached);
		}
		
		return cached;
	}
	
	private static Image blend(TerrainLuminance[] types) {
		ImageBuffer buffer = new ImageBuffer(Constants.TERRAIN_IMAGE_SIZE, Constants.TERRAIN_IMAGE_SIZE);
		
		float[][][] weightWarping = createWarpField();
		
		float dx, dy;
		int xCoord, yCoord;
		for (int y = 0; y < Constants.TERRAIN_IMAGE_SIZE; y++) {
			// dy = weighting[y];
			yCoord = y < Constants.HALF_TERRAIN_IMAGE_SIZE ? y + Constants.HALF_TERRAIN_IMAGE_SIZE : y - Constants.HALF_TERRAIN_IMAGE_SIZE;
			for (int x = 0; x < Constants.TERRAIN_IMAGE_SIZE; x++) {
				// dx = weighting[x];
				
				dx = WEIGHTING[(int) (x + weightWarping[x][y][0])];
				dy = WEIGHTING[(int) (y + weightWarping[x][y][1])];
				
				xCoord = x < Constants.HALF_TERRAIN_IMAGE_SIZE ? x + Constants.HALF_TERRAIN_IMAGE_SIZE : x - Constants.HALF_TERRAIN_IMAGE_SIZE;
				
				// TODO the luminance should affect only the relative weighting and not ultimately be directly multiplied with the output color weight (as it is currently ...)
				// on second thought, I am actually not really sure on this... both might be feasible, both might be not ...
				ImageEditor.setPixelWeightsInterpolatedWithGreyscaleBlending(buffer, x, y, dx, dy,
						types[UPPER_LEFT] .getGrayscaleValue(xCoord, yCoord),
						types[UPPER_RIGHT].getGrayscaleValue(xCoord, yCoord),
						types[LOWER_LEFT] .getGrayscaleValue(xCoord, yCoord),
						types[LOWER_RIGHT].getGrayscaleValue(xCoord, yCoord));
			}
		}
		
		
		return buffer.getImage();
	}
	
	private static TerrainImage stampIntoAtlas(Image image) {
		TerrainImageAtlas atlas = atlasList.get(atlasList.size() - 1);
		
		if (!atlas.hasCapacity()) {
			atlas = new TerrainImageAtlas(atlasList.size());
			atlasList.add(atlas);
		}
		
		return atlas.stampIntoAtlas(image);
	}
	
	private static float[][][] createWarpField() {
		float[][][] warpField = new float[Constants.TERRAIN_IMAGE_SIZE][Constants.TERRAIN_IMAGE_SIZE][2];
		
		FinitePerlin2D1D xWarp, yWarp;
		
		xWarp = new FinitePerlin2D1D(Utilities.RANDOM, 32, 4, Constants.TERRAIN_IMAGE_SIZE, Constants.TERRAIN_IMAGE_SIZE).prefilter();
		yWarp = new FinitePerlin2D1D(Utilities.RANDOM, 32, 4, Constants.TERRAIN_IMAGE_SIZE, Constants.TERRAIN_IMAGE_SIZE).prefilter();
		
		float xFactor, yFactor, factor;
		for (int x = 0; x < Constants.TERRAIN_IMAGE_SIZE; x++) {
			if (x < MAX_WARP) {
				xFactor = Utilities.smootherstep(0, MAX_WARP, x);
			}
			else if (Constants.TERRAIN_IMAGE_SIZE - x < MAX_WARP) {
				 xFactor = Utilities.smootherstep(Constants.TERRAIN_IMAGE_SIZE, Constants.TERRAIN_IMAGE_SIZE - MAX_WARP, x);
			}
			else {
				xFactor = 1;
			}
			for (int y = 0; y < Constants.TERRAIN_IMAGE_SIZE; y++) {
				if (y < MAX_WARP) {
					yFactor = Utilities.smootherstep(0, MAX_WARP, y);
				}
				else if (Constants.TERRAIN_IMAGE_SIZE - y < MAX_WARP) {
					 yFactor = Utilities.smootherstep(Constants.TERRAIN_IMAGE_SIZE, Constants.TERRAIN_IMAGE_SIZE - MAX_WARP, y);
				}
				else {
					yFactor = 1;
				}
				factor = Math.min(xFactor, yFactor);
				
				warpField[x][y][0] = (xWarp.getValue(x, y) * 2 - 1) * factor * MAX_WARP;
				if (warpField[x][y][0] < -x) {
					warpField[x][y][0] = -x;
				}
				else if (x + warpField[x][y][0] >= Constants.TERRAIN_IMAGE_SIZE) {
					warpField[x][y][0] = Constants.TERRAIN_IMAGE_SIZE - x - 1;
				}
				warpField[x][y][1] = (yWarp.getValue(x, y) * 2 - 1) * factor * MAX_WARP;
				if (warpField[x][y][1] < -y) {
					warpField[x][y][1] = -y;
				}
				else if (y + warpField[x][y][1] >= Constants.TERRAIN_IMAGE_SIZE) {
					warpField[x][y][1] = Constants.TERRAIN_IMAGE_SIZE - y - 1;
				}
			}
		}
		
		return warpField;
	}
	
	
	private static class TerrainTuple {
		
		private static final int[] multipliers = {
				1, 19, 61, 131, 257, 521, 1031, 2053, 4099
		};
		
		//Gnampf: 9 ints to 1:
		// i0 + 19 * i1 + 61 * i2 + 131 * i3 + 257 * i4 + 521 * i5 + 1031 * i6 + 2053 * i7 + 4099 * i8
		
		private final int hashCode;
		
		private TerrainLuminance[] types;
		
		private int variation;
		
		public TerrainTuple(int variation, TerrainLuminance... terrainTypes) {
			this.variation = variation;
			types = new TerrainLuminance[terrainTypes.length];
			int sum = 0;
			for (int i = 0; i < terrainTypes.length; i++) {
				types[i] = terrainTypes[i];
				sum += multipliers[i << 1] * terrainTypes[i].hashCode();
				// there's only 4 terrainTypes, thus the indices 0, 2, 4 and 6 are used for this
			}
			sum += multipliers[8] * variation;
			
			hashCode = sum;
		}
		
		public boolean equals(Object o) {
			// since this is a private class, it may be assumed that this method is never called with "null" or "this" as parameter.
			if (o.getClass() != TerrainTuple.class || this.hashCode != o.hashCode())
				return false;
			
			if (o == this)
				return true;
			
			TerrainTuple other = (TerrainTuple) o;
			
			for (int i = 0; i < types.length; i++) {
				if (types[i] != other.types[i])
					return false;
			}
			
			return this.variation == other.variation;
		}
		
		public int hashCode() {
			return hashCode;
		}
		
	}
}
