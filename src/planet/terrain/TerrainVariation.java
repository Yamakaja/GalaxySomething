package planet.terrain;

import org.newdawn.slick.Color;

public class TerrainVariation {
	public final TerrainLuminance terrainLuminance;
	
	private Color color;
	
	private Color averageColor;
	
	public TerrainVariation(Terrain terrain, Color color) {
		this(terrain, color, 1);
	}
	
	public TerrainVariation(Terrain terrain, Color color, float luminance) {
		this(TerrainLuminance.get(terrain, luminance), color);
	}
	
	public TerrainVariation(TerrainLuminance terrainLuminance, Color color) {
		this.terrainLuminance = terrainLuminance;
		setColor(color);
	}

	public Color getColor() {
		return color;
	}
	
	public Color getAverageColor() {
		return averageColor;
	}

	public void setColor(Color color) {
		this.color = color;
		float avrg = terrainLuminance.getAverageGrayscaleValue();
		this.averageColor = new Color(avrg * color.r, avrg * color.g, avrg * color.b, 1);
	}
	
	@Override
	public boolean equals(Object o) {
		// used for an equality check when changing a planet color scheme
		if (o == this)
			return true;
		
		if (o == null || o.getClass() != TerrainVariation.class)
			return false;
		
		TerrainVariation other = (TerrainVariation) o;
		
		return this.terrainLuminance.equals(other.terrainLuminance) && this.color.equals(other.color);
	}
}
