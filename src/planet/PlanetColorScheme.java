package planet;

import planet.terrain.Terrain;
import planet.terrain.TerrainVariation;

public class PlanetColorScheme {
	
	// note that each planet should have his own copy of the static instances
	// otherwise changing a color of a planet would affect all planets with the same color scheme
	
	
	private TerrainVariation[] colorMap = new TerrainVariation[Terrain.getNumberOfTerrains()];
	
	public PlanetColorScheme(TerrainVariation... colorMapping) {
		for (int i = 0; i < colorMapping.length; i++) {
			colorMap[colorMapping[i].terrainLuminance.terrain.ID] = colorMapping[i];
		}
	}
	
	public void setColor(TerrainVariation color) {
		colorMap[color.terrainLuminance.terrain.ID] = color;
	}
	
	public TerrainVariation colorOf(Terrain terrain) {
		return colorMap[terrain.ID];
	}
}
