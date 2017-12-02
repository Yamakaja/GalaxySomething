package planet.terrain.biomes;

import planet.terrain.Biome;
import planet.terrain.Terrain;

public class TemperateBiome extends Biome {

	public TemperateBiome(float minTemperatureConstant, float maxTemperatureConstant,
			              float minTemperatureVariationAmplitude, float maxTemperatureVariationAmplitude) {
		super(minTemperatureConstant, maxTemperatureConstant,
			  minTemperatureVariationAmplitude, maxTemperatureVariationAmplitude);
		
		//hasFertility = true;
		usesWarpField = true;
		
		setTerrainLevels(
			level(0.3f, Terrain.WATER),
			level(0.32f, Terrain.SAND_TERRAINS),
			level(0.72f, Terrain.GRASS_TERRAINS),
			level(1.0f, Terrain.CRACKY_ROCK, Terrain.UNEVEN_ROCK_1)
		);
	}
	
}
