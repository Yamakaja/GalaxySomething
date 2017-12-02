package planet.terrain;

import main.Utilities;
import main.Utilities.UtilRandom;
import planet.PlanetColorSchemeDefinition;
import planet.PlanetSurface;
import planet.terrain.biomes.TemperateBiome;
import procedural.FinitePerlin2D1D;

public abstract class Biome {
	public static final TemperateBiome TEMPERATE = new TemperateBiome(0, 0, 0, 0);
	
	public static final Biome[] ALL_BIOMES = {
		TEMPERATE
	};
	
	public final float minTemperatureConstant, maxTemperatureConstant;
	public final float minTemperatureVariationAmplitude, maxTemperatureVariationAmplitude;
	
	protected boolean hasFertility = false;
	
	protected boolean usesWarpField = false;
	
	// TODO tendency to have certain ressources
	
	private static final float DEFAULT_VARIATION_SPACING = 10;
	
	private PlanetColorSchemeDefinition[] colorSchemes = { PlanetColorSchemeDefinition.DEFAULT_COLOR_SCHEME };
	
	protected TerrainLevel[] terrainLevels;
	
	public Biome(float minTemperatureConstant, float maxTemperatureConstant,
				 float minTemperatureVariationAmplitude, float maxTemperatureVariationAmplitude) {
		this.minTemperatureConstant = minTemperatureConstant;
		this.maxTemperatureConstant = maxTemperatureConstant;
		this.minTemperatureVariationAmplitude = minTemperatureVariationAmplitude;
		this.maxTemperatureVariationAmplitude = maxTemperatureVariationAmplitude;
	}
	
	public BiomeData generateBiomeData(UtilRandom random, int planetSize) {
		BiomeData biomeData = new BiomeData(random, this, random.selectRandomElement(colorSchemes).createColorScheme(random), planetSize,
				random.nextFloat(minTemperatureConstant, maxTemperatureConstant),
				random.nextFloat(minTemperatureVariationAmplitude, maxTemperatureVariationAmplitude));
		generateTerrainField(biomeData);
		generateTemperatureField(biomeData);
		generateResourceField(biomeData);
		if (hasFertility)
			generateFertilityField(biomeData);
		if (usesWarpField)
			generateWarpField(biomeData);
		
		return biomeData;
	}

	public TerrainTile generateTerrainTile(BiomeData data, int x, int y, PlanetSurface planetSurface) {
		float terrainNoiseValue = data.terrainField.getValue(x, y);
		TerrainLevel level = getTerrainLevel(terrainNoiseValue);
		TerrainTile tile = new TerrainTile(level.getType(data, x, y), x, y, planetSurface);
		if (hasFertility) {
			tile.setFertility(data.fertilityField.getValue(x, y));
		}
		
		populateTerrainTile(data, tile);
		
		return tile;
	}
	
	protected void populateTerrainTile(BiomeData data, TerrainTile tile) {
		// left for children to override
	}
	
	protected void generateTerrainField(BiomeData data) {
		data.terrainField = new FinitePerlin2D1D(data.random, 80, 9, data.planetSize, data.planetSize).prefilter();
	}
	
	protected void generateTemperatureField(BiomeData data) {
		data.temperatureField = new FinitePerlin2D1D(data.random, 400, 3, data.planetSize, data.planetSize).prefilter();
	}
	
	protected void generateWarpField(BiomeData data) {
		data.xWarpField = new FinitePerlin2D1D(data.random, 200, 9, data.planetSize, data.planetSize).prefilter();
		data.yWarpField = new FinitePerlin2D1D(data.random, 200, 9, data.planetSize, data.planetSize).prefilter();
		data.coordinateWarp = Utilities.createShuffledIndexArray(data.random, data.planetSize);
	}
	
	protected void generateFertilityField(BiomeData data) {
		// left for children to override
	}
	
	protected void generateResourceField(BiomeData data) {
		// left for children to override
	}
	
	protected TerrainLevel getTerrainLevel(float terrainNoiseValue) {
		for (int i = 0; i < terrainLevels.length; i++) {
			if (terrainNoiseValue <= terrainLevels[i].level) {
				float previousLevel = i > 0 ? terrainLevels[i-1].level : 0;
				terrainLevels[i].f = (terrainNoiseValue - previousLevel) / (terrainLevels[i].level - previousLevel);
				return terrainLevels[i];
			}
		}
		
		return null;
	}
	
	protected void setTerrainLevels(TerrainLevel... terrainLevels) {
		this.terrainLevels = terrainLevels;
	}
	
	protected static TerrainLevel level(float level, Terrain... terrains) {
		return new TerrainLevel(level, terrains);
	}
	
	protected void setColorSchemes(PlanetColorSchemeDefinition... colorSchemes) {
		this.colorSchemes = colorSchemes;
	}
	
	protected static class TerrainLevel {
		public final float level;
		/** signifies how "progressed" this level is at the coordinate it has last been evaluated at. */
		public float f;
		private final Terrain[] egligibleTerrain;
		private float terrainSpacing = DEFAULT_VARIATION_SPACING;
		private float warpRange = 256;
		
		public TerrainLevel(float level, Terrain[] egligibleTerrain) {
			this.level = level;
			this.egligibleTerrain = egligibleTerrain;
		}
		
		public TerrainLevel spacing(float spacing) {
			terrainSpacing = spacing;
			return this;
		}
		
		public TerrainLevel warpRange(float range) {
			warpRange = range;
			return this;
		}
		
		public Terrain getType(BiomeData data, int x, int y) {
			if (egligibleTerrain.length == 1) {
				return egligibleTerrain[0];
			}
			else {
				float warpedX = x, warpedY = y;
				if (data.xWarpField != null) {
					warpedX += warpRange * data.xWarpField.getValue(x, y);
				}
				if (data.yWarpField != null) {
					warpedY += warpRange * data.yWarpField.getValue(x, y);
				}
				int fx = (int) (warpedX / terrainSpacing);
				int fy = (int) (warpedY / terrainSpacing);
				return terrainAtIndex(data, fx, fy); // egligibleTerrain[(fx + fy) % egligibleTerrain.length]
			}
		}
		
		private Terrain terrainAtIndex(BiomeData data, int x, int y) {
			final int[] P = data.coordinateWarp;
			y = y % P.length;
			x = (x + P[y]) % P.length;
			return egligibleTerrain[ P[x] % egligibleTerrain.length ];
		}
	}
}
