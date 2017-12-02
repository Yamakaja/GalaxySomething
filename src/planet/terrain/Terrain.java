package planet.terrain;

import java.util.ArrayList;
import java.util.HashMap;

import main.FilePaths;
import main.GrayscaleImage;
import main.ImageManager;

public class Terrain {

	/** ID -> Terrain. WARNING: This map can only be assumed to be constant at runtime!
	 * ONLY EVER SAVE TERRAIN BY NAME IN FILES, NEVER BY ID!
	 */
	public static final Terrain[] TERRAIN_ARRAY;
	
	// name -> terrainType
	private static final HashMap<String, Terrain> TERRAIN_MAP;
	
	
	public static final Terrain DIRT_1;
	public static final Terrain DIRT_2;
	public static final Terrain MUD;
	
	public static final Terrain LOW_GRASS;
	public static final Terrain MEDIUM_GRASS;
	public static final Terrain HIGH_GRASS;
	
	public static final Terrain GRAVEL;
	public static final Terrain SAND_1;
	public static final Terrain SAND_2;
	public static final Terrain RIBBLED_SAND_1;
	public static final Terrain RIBBLED_SAND_2;

	public static final Terrain CRACKY_ROCK ;
	public static final Terrain MIXED_ROCK;
	public static final Terrain UNEVEN_ROCK_1;
	public static final Terrain UNEVEN_ROCK_2;
	
	public static final Terrain FLAT_STONE_1;
	/** doesn't tile */
	public static final Terrain FLAT_STONE_2;
	public static final Terrain UNEVEN_STONE;

	public static final Terrain SLIMEY_BREW;
	public static final Terrain WATER;
	public static final Terrain CRYSTALLIC_WATER;
	
	
	public static final Terrain[] DIRT_TERRAINS;
	public static final Terrain[] GRASS_TERRAINS;
	public static final Terrain[] GRAVEL_TERRAINS;
	public static final Terrain[] SAND_TERRAINS;
	public static final Terrain[] ROCK_TERRAINS;
	public static final Terrain[] STONE_TERRAINS;
	public static final Terrain[] WATER_TERRAINS;
	public static final Terrain[] MISC_LIQUID_TERRAINS;
	
	
	private static ArrayList<Terrain> terrainList;
	private static HashMap<TerrainType, ArrayList<Terrain>> terrainTypeMap;
	static {
		TERRAIN_MAP = new HashMap<>();
		
		terrainList = new ArrayList<>();
		terrainTypeMap = new HashMap<>();
		for (TerrainType type : TerrainType.values()) {
			terrainTypeMap.put(type, new ArrayList<>());
		}
		
		DIRT_1 = new Terrain(TerrainType.DIRT, "dirt_1");
		DIRT_2 = new Terrain(TerrainType.DIRT, "dirt_2");
		MUD = new Terrain(TerrainType.DIRT, "mud_1");
		
		LOW_GRASS = new Terrain(TerrainType.GRASS, "grass_low_1");
		MEDIUM_GRASS = new Terrain(TerrainType.GRASS, "grass_medium_1");
		HIGH_GRASS = new Terrain(TerrainType.GRASS, "grass_high_1");
		
		GRAVEL = new Terrain(TerrainType.GRAVEL, "gravel_1");
		SAND_1 = new Terrain(TerrainType.SAND, "sand_1");
		SAND_2 = new Terrain(TerrainType.SAND, "sand_2");
		RIBBLED_SAND_1 = new Terrain(TerrainType.SAND, "sand_ribbled_1");
		RIBBLED_SAND_2 = new Terrain(TerrainType.SAND, "sand_ribbled_2");

		CRACKY_ROCK = new Terrain(TerrainType.ROCK, "rock_cracky_1");
		MIXED_ROCK = new Terrain(TerrainType.ROCK, "rock_mixed_1");
		UNEVEN_ROCK_1 = new Terrain(TerrainType.ROCK, "rock_uneven_1");
		UNEVEN_ROCK_2 = new Terrain(TerrainType.ROCK, "rock_uneven_2");
		
		FLAT_STONE_1 = new Terrain(TerrainType.STONE, "stone_flat_1");
		
		FLAT_STONE_2 = new Terrain(TerrainType.STONE, "stone_flat_2");
		UNEVEN_STONE = new Terrain(TerrainType.STONE, "stone_uneven_1");
		
		WATER = new Terrain(TerrainType.WATER, "water_1");
		CRYSTALLIC_WATER = new Terrain(TerrainType.WATER, "water_2");
		
		
		SLIMEY_BREW = new Terrain(TerrainType.MISC_LIQUID, "slimey_brew_1");
		
		
		// do not construct any terrain below this line, it's the end
		
		TERRAIN_ARRAY = toArray(terrainList);
		DIRT_TERRAINS = toArray(terrainTypeMap.get(TerrainType.DIRT));
		GRASS_TERRAINS = toArray(terrainTypeMap.get(TerrainType.GRASS));
		GRAVEL_TERRAINS = toArray(terrainTypeMap.get(TerrainType.GRAVEL));
		SAND_TERRAINS = toArray(terrainTypeMap.get(TerrainType.SAND));
		ROCK_TERRAINS = toArray(terrainTypeMap.get(TerrainType.ROCK));
		STONE_TERRAINS = toArray(terrainTypeMap.get(TerrainType.STONE));
		WATER_TERRAINS = toArray(terrainTypeMap.get(TerrainType.WATER));
		MISC_LIQUID_TERRAINS = toArray(terrainTypeMap.get(TerrainType.MISC_LIQUID));
		
		terrainList.clear();
		terrainList = null;
		terrainTypeMap.clear();
		terrainTypeMap = null;
	}
	
	private static Terrain[] toArray(ArrayList<Terrain> list) {
		return list.toArray(new Terrain[list.size()]);
	}
	
	public static Terrain getTerrainType(String name) {
		return TERRAIN_MAP.get(name);
	}
	
	public static Terrain getTerrain(int ID) {
		return TERRAIN_ARRAY[ID];
	}
	
	public static int getNumberOfTerrains() {
		return TERRAIN_ARRAY.length;
	}
	
	public final TerrainType type;
	
	public final String name;
	
	public final int ID;
	
	public final GrayscaleImage image;
	
	
	private Terrain(TerrainType type, String name) {
		this(type, name, name);
	}
	
	private Terrain(TerrainType type, String name, String fileName) {
		this.type = type;
		this.name = name;
		this.ID = terrainList.size();
		image = new GrayscaleImage(ImageManager.loadImage(FilePaths.TERRAIN_IMAGE_DIR + fileName + ".png", false));
		
		TERRAIN_MAP.put(name, this);
		terrainList.add(this);
		terrainTypeMap.get(type).add(this);
	}
	
	public float getGrayscaleValue(int x, int y) {
		return image.getValue(x, y);
	}
}
