package main;

import org.newdawn.slick.Color;
import org.newdawn.slick.ImageBuffer;

public class ImageEditor {
	
	public static final int ALPHA = 0;
	public static final int RED   = 1;
	public static final int GREEN = 2;
	public static final int BLUE  = 3;
	
	public static void setPixel(ImageBuffer image, int x, int y, int a, int r, int g, int b) {
		image.setRGBA(x, y, r, g, b, a);
	}
	
	public static void setPixel(ImageBuffer image, int x, int y, int[] argb) {
		setPixel(image, x, y, argb[ALPHA], argb[RED], argb[GREEN], argb[BLUE]);
	}
	
	public static void setPixelInterpolated(ImageBuffer image, int x, int y, int[] argb1, int[] argb2, float f) {
		setPixel(image, x, y, (int) ((1 - f) * argb1[ALPHA] + f * argb2[ALPHA]),
							  (int) ((1 - f) * argb1[RED  ] + f * argb2[RED  ]),
							  (int) ((1 - f) * argb1[GREEN] + f * argb2[GREEN]),
							  (int) ((1 - f) * argb1[BLUE ] + f * argb2[BLUE ]));
	}
	
	public static int[] interpolateColors(int[] argb1, int[] argb2, float f, int[] resultARGB) {
		resultARGB[ALPHA] = (byte) ((1 - f) * argb1[ALPHA] + f * argb2[ALPHA]);
		resultARGB[RED  ] = (byte) ((1 - f) * argb1[RED  ] + f * argb2[RED  ]);
		resultARGB[GREEN] = (byte) ((1 - f) * argb1[GREEN] + f * argb2[GREEN]);
		resultARGB[BLUE ] = (byte) ((1 - f) * argb1[BLUE ] + f * argb2[BLUE ]);
		
		return resultARGB;
	}
	
	/**
	 * @param image
	 * @param x and y: Coordinates on the imageBuffer to set
	 * @param dx distance between left and right (0 -> 1)
	 * @param dy distance between up and down (0 -> 1)
	 * @param upperLeft
	 * @param upperRight
	 * @param lowerLeft
	 * @param lowerRight
	 */
	public static void setPixelColorInterpolated(ImageBuffer image, int x, int y, float dx, float dy, Color upperLeft, Color upperRight, Color lowerLeft, Color lowerRight) {
		float upperLeftWeight = (1 - dx) * (1 - dy);
		float upperRightWeight = dx * (1 - dy);
		float lowerLeftWeight = (1 - dx) * dy;
		float lowerRightWeight = dx * dy;
		
		image.setRGBA(x, y,
			toInt(upperLeftWeight * upperLeft.r + upperRightWeight * upperRight.r + lowerLeftWeight * lowerLeft.r + lowerRightWeight * lowerRight.r),
			toInt(upperLeftWeight * upperLeft.g + upperRightWeight * upperRight.g + lowerLeftWeight * lowerLeft.g + lowerRightWeight * lowerRight.g),
			toInt(upperLeftWeight * upperLeft.b + upperRightWeight * upperRight.b + lowerLeftWeight * lowerLeft.b + lowerRightWeight * lowerRight.b),
			255);
	}
	
	/**
	 * @param image
	 * @param x and y: Coordinates on the imageBuffer to set
	 * @param dx distance between left and right (0 -> 1)
	 * @param dy distance between up and down (0 -> 1)
	 * @param upperLeft
	 * @param upperRight
	 * @param lowerLeft
	 * @param lowerRight
	 */
	public static void setPixelColorInterpolatedWithGreyscaleBlending(
			ImageBuffer image, int x, int y, final float dx, final float dy,
			Color upperLeft, Color upperRight, Color lowerLeft, Color lowerRight) {
		//float avrg = (upperLeft.a + upperRight.a + lowerLeft.a + lowerRight.a) / 4;
		//float logavrg = (float) Math.exp((Math.log(upperLeft.a) + Math.log(upperRight.a) + Math.log(lowerLeft.a) + Math.log(lowerRight.a)) / 4);
		
		float cap = 0.35f;
		
		float a1 = Math.min(cap, upperLeft.a)  + (1 - dx) * (1 - dy);
		float a2 = Math.min(cap, upperRight.a) +      dx  * (1 - dy);
		float a3 = Math.min(cap, lowerLeft.a)  + (1 - dx) * dy;
		float a4 = Math.min(cap, lowerRight.a) +      dx  * dy;
		
		final float depth = 0.3f;
		final float threshold = Math.max(a1, Math.max(a2, Math.max(a3, a4))) - depth;
		
		final float b1 = Math.max(a1 - threshold, 0);
		final float b2 = Math.max(a2 - threshold, 0);
		final float b3 = Math.max(a3 - threshold, 0);
		final float b4 = Math.max(a4 - threshold, 0);
		final float sum = b1 + b2 + b3 + b4;
		
		float upperLeftWeight = b1 / sum;
		float upperRightWeight = b2 / sum;
		float lowerLeftWeight = b3 / sum;
		float lowerRightWeight = b4 / sum;
		
		image.setRGBA(x, y,
			toInt(upperLeftWeight * upperLeft.r + upperRightWeight * upperRight.r + lowerLeftWeight * lowerLeft.r + lowerRightWeight * lowerRight.r),
			toInt(upperLeftWeight * upperLeft.g + upperRightWeight * upperRight.g + lowerLeftWeight * lowerLeft.g + lowerRightWeight * lowerRight.g),
			toInt(upperLeftWeight * upperLeft.b + upperRightWeight * upperRight.b + lowerLeftWeight * lowerLeft.b + lowerRightWeight * lowerRight.b),
			255);
	}
	
	/**
	 * @param image
	 * @param x and y: Coordinates on the imageBuffer to set
	 * @param dx distance between left and right (0 -> 1)
	 * @param dy distance between up and down (0 -> 1)
	 * @param upperLeft
	 * @param upperRight
	 * @param lowerLeft
	 * @param lowerRight
	 */
	public static void setPixelWeightsInterpolatedWithGreyscaleBlending(
			ImageBuffer image, int x, int y, final float dx, final float dy,
			float upperLeft, float upperRight, float lowerLeft, float lowerRight) {
		//float avrg = (upperLeft.a + upperRight.a + lowerLeft.a + lowerRight.a) / 4;
		//float logavrg = (float) Math.exp((Math.log(upperLeft.a) + Math.log(upperRight.a) + Math.log(lowerLeft.a) + Math.log(lowerRight.a)) / 4);
		
		float cap = 0.35f;
		
		float a1 = Math.min(cap, upperLeft)  + (1 - dx) * (1 - dy);
		float a2 = Math.min(cap, upperRight) +      dx  * (1 - dy);
		float a3 = Math.min(cap, lowerLeft)  + (1 - dx) * dy;
		float a4 = Math.min(cap, lowerRight) +      dx  * dy;
		
		final float depth = 0.3f;
		final float threshold = Math.max(a1, Math.max(a2, Math.max(a3, a4))) - depth;
		
		final float b1 = Math.max(a1 - threshold, 0);
		final float b2 = Math.max(a2 - threshold, 0);
		final float b3 = Math.max(a3 - threshold, 0);
		final float b4 = Math.max(a4 - threshold, 0);
		final float sum = b1 + b2 + b3 + b4;
		
		float upperLeftWeight = b1 / sum;
		float upperRightWeight = b2 / sum;
		float lowerLeftWeight = b3 / sum;
		float lowerRightWeight = b4 / sum;
		
		image.setRGBA(x, y, toInt(upperLeftWeight * upperLeft), toInt(upperRightWeight * upperRight), toInt(lowerLeftWeight * lowerLeft), toInt(lowerRightWeight * lowerRight));
		
	}
	
	/** Simple helper to cast a float in range [0, 1] to a bytesize-valued int in range [0, 255] */
	public static int toInt(float f) {
		f = f < 0 ? 0 : f > 1 ? 1 : f;
		return (int) (f * 255.5);
	}
	
	/** Simple helper to cast a float in range [0, 1] to a byte in range [0, 255] */
	public static byte toByte(float f) {
		f = f < 0 ? 0 : f > 1 ? 1 : f;
		return (byte) (f * 255.5);
	}
	
	//public static byte[] toByteArray(int a, int r, int g, int b) {
	//	return new byte[]{(byte) a, (byte) r, (byte) g, (byte) b};
	//}
}
