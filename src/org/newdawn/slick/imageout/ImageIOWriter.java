package org.newdawn.slick.imageout;

import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

/**
 * A utility to write a Slick image out using ImageIO
 * 
 * @author Jon
 */
public class ImageIOWriter implements ImageWriter {
	private static final ImageIOWriter instance = new ImageIOWriter();
	
	public static ImageIOWriter getInstance() {
		return instance;
	}

	@Override
	public void saveImage(final Image image, String format, OutputStream out, final boolean writeAlpha) throws IOException {
		saveImage(new ImageDataProvider() {
			@Override
			public Color getColor(int x, int y) {
				return image.getColor(x, y);
			}
			@Override
			public boolean hasAlpha() {
				return writeAlpha;
			}
			@Override
			public int getImageWidth() {
				return image.getWidth();
			}
			@Override
			public int getImageHeight() {
				return image.getHeight();
			}
		}, format, out);
	}
	
	public static void saveImage(ImageDataProvider image, String format, OutputStream output)
			throws IOException {
		
		// convert the image into a byte buffer by reading each pixel in turn
		int length = (image.hasAlpha() ? 4 : 3) * image.getImageWidth() * image.getImageHeight();
		
		ByteBuffer out = ByteBuffer.allocate(length);
		Color color;

		for (int y = 0; y < image.getImageHeight(); y++) {
			for (int x = 0; x < image.getImageWidth(); x++) {
				color = image.getColor(x, y);

				out.put((byte) (color.r * 255.0f));
				out.put((byte) (color.g * 255.0f));
				out.put((byte) (color.b * 255.0f));
				if (image.hasAlpha()) {
					out.put((byte) (color.a * 255.0f));
				}
			}
		}

		// create a raster of the correct format and fill it with our buffer
		DataBufferByte dataBuffer = new DataBufferByte(out.array(), length);
		
		PixelInterleavedSampleModel sampleModel;

		ColorModel cm;
		
		if (image.hasAlpha()) {
			int[] offsets = { 0, 1, 2, 3 };
			sampleModel = new PixelInterleavedSampleModel(
					DataBuffer.TYPE_BYTE, image.getImageWidth(), image.getImageHeight(), 4,
					4 * image.getImageWidth(), offsets);
			
			cm = new ComponentColorModel(ColorSpace
					.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 8 },
					true, false, ComponentColorModel.TRANSLUCENT,
					DataBuffer.TYPE_BYTE);
		} else {
			int[] offsets = { 0, 1, 2};
			sampleModel = new PixelInterleavedSampleModel(
					DataBuffer.TYPE_BYTE, image.getImageWidth(), image.getImageHeight(), 3,
					3 * image.getImageWidth(), offsets);
			
			cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
	                new int[] {8,8,8,0},
	                false,
	                false,
	                ComponentColorModel.OPAQUE,
	                DataBuffer.TYPE_BYTE);
		}
		WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuffer, new Point(0, 0));

		// finally create the buffered image based on the data from the texture
		// and spit it through to ImageIO
		BufferedImage img = new BufferedImage(cm, raster, false, null);
		
		ImageIO.write(img, format, output);
	}
	
	public static interface ImageDataProvider {
		public Color getColor(int x, int y);
		public boolean hasAlpha();
		public int getImageWidth();
		public int getImageHeight();
	}
}
