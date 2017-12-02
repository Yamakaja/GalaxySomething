package planet.terrain;

import org.newdawn.slick.Image;

public class TerrainImage {
	public final Image image;
	public final TerrainImageAtlas parent;
	
	public TerrainImage(Image image, TerrainImageAtlas parent) {
		this.image = image;
		this.parent = parent;
	}
}
