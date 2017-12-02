package main;

public class ExceptionHandler {
	
	public static void handleErrorLoadingImage(Throwable t) {
		t.printStackTrace();
	}
	
	public static void handleErrorDestroyingImage(Throwable t) {
		t.printStackTrace();
	}
	
	public static void handleErrorCreatingImage(Throwable t) {
		t.printStackTrace();
	}
	
	public static void handleErrorLoadingShader(Throwable t) {
		t.printStackTrace();
	}
	
	public static void handleUndefinedError(Throwable t) {
		t.printStackTrace();
	}
}
