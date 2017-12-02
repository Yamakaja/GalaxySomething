package planet.terrain;

import main.Utilities.UtilRandom;
import planet.PlanetColorScheme;
import planet.PlanetSurface;
import procedural.FinitePerlin2D1D;

public class BiomeData extends planet.PlanetSurface.TerrainTileProvider {
	public final Biome biome;
	public final PlanetColorScheme colorScheme;
	public final int planetSize;
	public final UtilRandom random;
	public final float temperatureConstant;
	public final float temperatureVariationAmplitude;
	
	FinitePerlin2D1D terrainField;
	FinitePerlin2D1D temperatureField;
	FinitePerlin2D1D fertilityField;
	FinitePerlin2D1D xWarpField, yWarpField;
	int[] coordinateWarp;
	
	public BiomeData(UtilRandom random, Biome biome, PlanetColorScheme colorScheme,
					 int planetSize, float temperatureConstant, float temperatureVariationAmplitude) {
		this.random = random;
		this.biome = biome;
		this.colorScheme = colorScheme;
		this.planetSize = planetSize;
		this.temperatureConstant = temperatureConstant;
		this.temperatureVariationAmplitude = temperatureVariationAmplitude;
	}

	@Override
	public TerrainTile createTileAt(int x, int y, PlanetSurface planet) {
		return biome.generateTerrainTile(this, x, y, planet);
	}
}
