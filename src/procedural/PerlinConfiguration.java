package procedural;

public class PerlinConfiguration {
	private static boolean useReproducibleRandom = false;
	
	public static boolean useReproducibleRandom() {
		return useReproducibleRandom;
	}
	
	public static void setReproducibleRandom(boolean b) {
		useReproducibleRandom = b;
	}
}
