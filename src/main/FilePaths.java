package main;

public final class FilePaths {
	
	// relative paths
	
	public static final String TERRAIN_IMAGE_DIR = "assets/terrain/";
	
	public static final String GUI_IMAGE_DIR = "assets/gui/";
	
	public static final String FONT_DIR = "assets/font/";
	
	public static final String SHADER_DIR = "assets/shaders/";
	
	
	// absolute paths
	
	public static final String GRAYSCALE_CONVERSION_INPUT_DIR = System.getProperty("user.dir") + "/grayscaleconversion/input";
	
	public static final String GRAYSCALE_CONVERSION_OUTPUT_DIR = System.getProperty("user.dir") + "/grayscaleconversion/output";
	
	
	private FilePaths() {}
}
