package planet;

import org.newdawn.slick.Color;

import main.Colors;
import main.Utilities.UtilRandom;
import planet.terrain.Terrain;
import planet.terrain.TerrainVariation;

public class PlanetColorSchemeDefinition {
	
	private static final float BRIGHT = 1.2f;
	private static final float NORMAL = 1.0f;
	private static final float GLOOMY = 0.8f;
	
	private final ColorDefinition[] colorDefinitions;
	
	public static final PlanetColorSchemeDefinition DEFAULT_COLOR_SCHEME = new PlanetColorSchemeDefinition(
		Terrain.DIRT_1, Colors.DIRT_BROWN,
		Terrain.DIRT_2, Colors.DIRT_BROWN,
		Terrain.MUD,    Colors.DIRT_BROWN,
		
		Terrain.LOW_GRASS,    Colors.GRASS_GREEN,
		Terrain.MEDIUM_GRASS, Colors.GRASS_GREEN,
		Terrain.HIGH_GRASS,   Colors.GRASS_GREEN,
		
		Terrain.GRAVEL,    	  	Colors.GRAVEL_TINTISH_GREY,
		Terrain.SAND_1,	 	  	Colors.SAND_YELLOW,
		Terrain.SAND_2, 		Colors.SAND_YELLOW,
		Terrain.RIBBLED_SAND_1, Colors.SAND_YELLOW,
		Terrain.RIBBLED_SAND_2, color(Colors.SAND_YELLOW, BRIGHT),

		Terrain.CRACKY_ROCK,   Colors.STONE_GREY,
		Terrain.MIXED_ROCK,    Colors.STONE_GREY,
		Terrain.UNEVEN_ROCK_1, Colors.STONE_GREY,
		Terrain.UNEVEN_ROCK_2, Colors.STONE_GREY,
		
		Terrain.FLAT_STONE_1,  Colors.STONE_GREY,
		Terrain.FLAT_STONE_2,  Colors.STONE_GREY,
		Terrain.UNEVEN_STONE,  Colors.STONE_GREY,
		
		Terrain.SLIMEY_BREW,      Colors.GRASS_GREEN,
		Terrain.WATER,  		  Colors.WATER_DARK_BLUE,
		Terrain.CRYSTALLIC_WATER, Colors.WATER_CYAN
	);
	
	private PlanetColorSchemeDefinition(Object... definitions) {
		colorDefinitions = new ColorDefinition[definitions.length / 2];
		Terrain terrain;
		ColorGetter color;
		for (int i = 0; i < colorDefinitions.length; i++) {
			terrain = (Terrain) definitions[2 * i];
			color = definitions[2*i+1].getClass() == Color.class ? color((Color)definitions[2*i+1]) : (ColorGetter) definitions[2*i+1];
			colorDefinitions[i] = new ColorDefinition(terrain, color);
		}
	}
	
	public PlanetColorScheme createColorScheme(UtilRandom random) {
		PlanetColorScheme colorScheme;
		if (this == DEFAULT_COLOR_SCHEME)
			colorScheme = new PlanetColorScheme();
		else
			colorScheme = DEFAULT_COLOR_SCHEME.createColorScheme(random);
		
		for (int i = 0; i < colorDefinitions.length; i++) {
			colorScheme.setColor(new TerrainVariation(colorDefinitions[i].terrain,
													  colorDefinitions[i].color.getColor(random),
													  colorDefinitions[i].color.getLuminance()));
		}
		return colorScheme;
	}
	
	private static class ColorDefinition {
		public final Terrain terrain;
		public final ColorGetter color;
		public ColorDefinition(Terrain terrain, ColorGetter color) {
			this.terrain = terrain;
			this.color = color;
		}
	}
	
	private static abstract class ColorGetter {
		protected float luminance;
		public ColorGetter(float luminance) {
			this.luminance = luminance;
		}
		public abstract Color getColor(UtilRandom random);
		public float getLuminance() {
			return luminance;
		}
	}
	
	private static ColorGetter color(Color color) {
		return color(color, NORMAL);
	}
	
	private static ColorGetter color(final Color color, float luminance) {
		return new ColorGetter(luminance) {
			public Color getColor(UtilRandom random) {
				return color;
			}
		};
	}
	
	private static ColorGetter interval(Color A, Color B) {
		return interval(A, B, NORMAL);
	}
	
	private static ColorGetter interval(final Color a, final Color b, float luminance) {
		return new ColorGetter(luminance) {
			public Color getColor(UtilRandom random) {
				float f = random.nextFloat();
				return new Color(
					f * a.r + (1 - f) * b.r,
					f * a.g + (1 - f) * b.g,
					f * a.b + (1 - f) * b.b
				);
			}
		};
	}
	
	private static ColorGetter choice(Object... options) {
		return choice(NORMAL, options);
	}
	
	private static ColorGetter choice(float luminance, Object... options) {
		final ColorGetter[] colors = new ColorGetter[options.length];
		for (int i = 0; i < options.length; i++) {
			colors[i] = options[i].getClass() == Color.class ? color((Color)options[i]) : (ColorGetter)options[i];
		}
		return new ColorGetter(luminance) {
			private int index;
			public Color getColor(UtilRandom random) {
				index = random.nextInt(colors.length);
				return colors[index].getColor(random);
			}
			public float getLuminance() {
				return colors[index].getLuminance();
			}
		};
	}
}
