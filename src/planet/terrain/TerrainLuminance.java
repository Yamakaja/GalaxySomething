package planet.terrain;

import java.util.HashMap;

public class TerrainLuminance {
	
	public final Terrain terrain;
	public final float luminance;
	
	private static final HashMap<TerrainLuminance, TerrainLuminance> instances = new HashMap<>();
	
	private final int hashCode;
	
	public static TerrainLuminance get(Terrain terrain, float luminance) {
		TerrainLuminance instance = new TerrainLuminance(terrain, luminance);
		
		TerrainLuminance existing = instances.get(instance);
		if (existing == null) {
			instances.put(instance, instance);
			existing = instance;
		}
		
		return existing;
	}
	
	private TerrainLuminance(Terrain terrain, float luminance) {
		this.terrain = terrain;
		this.luminance = luminance;

		int result = 1;
		result = 31 + Float.floatToIntBits(luminance);
		result = 31 * result + terrain.hashCode();
		hashCode = result;
	}
	
	public float getGrayscaleValue(int x, int y) {
		return luminance * terrain.getGrayscaleValue(x, y);
	}
	
	public float getAverageGrayscaleValue() {
		return luminance * terrain.image.averageValue;
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null || this.getClass() != obj.getClass())
			return false;
		
		TerrainLuminance other = (TerrainLuminance) obj;
		
		return luminance == other.luminance && terrain == other.terrain;
	}
}
