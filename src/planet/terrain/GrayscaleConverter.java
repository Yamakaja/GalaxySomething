package planet.terrain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageData;
import org.newdawn.slick.imageout.ImageIOWriter;
import org.newdawn.slick.imageout.ImageIOWriter.ImageDataProvider;

import main.ExceptionHandler;

public class GrayscaleConverter {
	
	public static final String OUTPUT_FORMAT = "png";
	
	public static void convertAll(File inputFolder, File outputFolder) {
		convertAll(inputFolder, outputFolder, true);
	}
	
	public static void convertAll(File inputFolder, File outputFolder, boolean overwrite) {
		if (!inputFolder.exists()) {
			return;
		}
		
		try {
			File[] inputFiles = inputFolder.listFiles();
			
			for (File inputFile : inputFiles) {
				File outputFile = new File(outputFolder.getAbsolutePath() + "/" + inputFile.getName());
				if (outputFile.exists()) {
					if (!overwrite)
						continue;
					if (outputFile.isDirectory()) {
						outputFile.delete();
					}
				}
				if (!outputFile.exists()) {
					outputFile.mkdirs();
					outputFile.delete();
					outputFile.createNewFile();
				}
				
				convert(new FileInputStream(inputFile), new FileOutputStream(outputFile));
			}
		}
		catch (Exception e) {
			ExceptionHandler.handleUndefinedError(e);
		}
	}
	
	public static void convert(FileInputStream input, FileOutputStream output) {
		try {
			final ImageData sourceData = new ImageData(new Image(input)).setAlphaFromGrayscale();
			float min = 1, max = 0;
			Color color;
			
			for (int y = 0; y < sourceData.imageHeight; y++) {
				for (int x = 0; x < sourceData.imageWidth; x++) {
					color = sourceData.getColor(x, y);
					min = Math.min(min, color.a);
					max = Math.max(max, color.a);
				}
			}
			
			final float MIN = min, MAX = max;
			
			ImageIOWriter.saveImage(new ImageDataProvider() {
				@Override
				public Color getColor(int x, int y) {
					Color color = sourceData.getColor(x, y);
					float value = MIN + (1 - MIN) * (color.a - MIN) / (MAX - MIN);
					
					return new Color(value, value, value, 1);
				}
				@Override
				public boolean hasAlpha() {
					return sourceData.hasAlpha;
				}
				@Override
				public int getImageWidth() {
					return sourceData.imageWidth;
				}
				@Override
				public int getImageHeight() {
					return sourceData.imageHeight;
				}
			}, OUTPUT_FORMAT, output);
		}
		catch (Exception e) {
			ExceptionHandler.handleUndefinedError(e);
		}
	}
}
