package planet.terrain;

import org.newdawn.slick.Color;

import planet.PlanetSurface;

public class TerrainTile {
	/** The indices of this object within the world terrain array */
	public final int x, y;
	
	public final PlanetSurface planet;
	
	/** The planet (surface) temperature at this position in °C */
	private float temperature;
	
	/** The fertility of the planet surface at this position */
	private float fertility;
	
	
	
	private TerrainVariation terrain;
	
	public TerrainTile(Terrain terrain, int x, int y, PlanetSurface planet) {
		this (planet.getColorOf(terrain), x, y, planet);
	}
	
	public TerrainTile(Terrain type, int x, int y, PlanetSurface planet, float temperature, float fertility) {
		this(planet.getColorOf(type), x, y, planet, temperature, fertility);
	}
	
	public TerrainTile(TerrainVariation type, int x, int y, PlanetSurface planet) {
		this (type, x, y, planet, 0, 0);
	}
	
	public TerrainTile(TerrainVariation terrain, int x, int y, PlanetSurface planet, float temperature, float fertility) {
		this.terrain = terrain;
		this.x = x;
		this.y = y;
		this.planet = planet;
		this.temperature = temperature;
		this.fertility = fertility;
	}
	
	public boolean setTerrain(TerrainVariation newTerrain) {
		if (newTerrain != terrain) {
			terrain = newTerrain;
			return true;
		}
		else {
			return false;
		}
	}
	
	public Terrain getTerrain() {
		return terrain.terrainLuminance.terrain;
	}
	
	public TerrainLuminance getLuminance() {
		return terrain.terrainLuminance;
	}
	
	public Color getColor() {
		return terrain.getColor();
	}
	
	public Color getAverageColor() {
		return terrain.getAverageColor();
	}
	
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
	
	public float getTemperature() {
		return temperature;
	}
	
	public void setFertility(float fertility) {
		this.fertility = fertility;
	}
	
	public float getFertility() {
		return fertility;
	}
}
