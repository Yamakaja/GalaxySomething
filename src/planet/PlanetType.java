package planet;

import planet.terrain.Biome;

public class PlanetType {
	
	public static final PlanetType DWARF_PLANET = new PlanetType(500, 800);
	public static final PlanetType NORMAL_PLANET = new PlanetType(1000, 1500);
	
	// planets are always squares. Take that, flat earth society!
	public final int minSize;
	public final int maxSize;
	
	public final Biome[] validBiomes;
	
	private PlanetType(int minSize, int maxSize) {
		this(minSize, maxSize, Biome.ALL_BIOMES);
	}
	
	private PlanetType(int minSize, int maxSize, Biome[] validBiomes) {
		this.minSize = minSize;
		this.maxSize = maxSize;
		this.validBiomes = validBiomes;
	}
}
