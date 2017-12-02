package planet;

import main.Utilities.UtilRandom;
import planet.terrain.Biome;
import planet.terrain.BiomeData;

public class PlanetGenerator {
	
	
	public static PlanetSurface generatePlanet(UtilRandom random, PlanetType planetType) {
		
		int planetSize = random.nextInt(planetType.minSize, planetType.maxSize);
		
		// every planet has a temperature tendency, meaning a constant upon which the temperature noise is added.
		// thus arctic planets are always cold, but still have more and less cold regions.
		// the amplitude of the noise therein also depends on the planet type.
		
		// the same principle applies to the height and fertility (if the latter is an option, depending on the biome)
		
		Biome biome = random.selectRandomElement(planetType.validBiomes);
		
		BiomeData biomeData = biome.generateBiomeData(random, planetSize);
		
		return new PlanetSurface(biomeData, planetSize, planetSize);
	}
}
