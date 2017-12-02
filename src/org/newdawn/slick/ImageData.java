package org.newdawn.slick;

import org.newdawn.slick.opengl.Texture;

public class ImageData {
	
	private final byte[] pixelData;
	
	private final Color[] colorCache;
	
	public final int imageWidth;
	public final int imageHeight;
	
	private final int textureWidth;
	//private final int textureHeight;
	
	public final boolean hasAlpha;
	
	private boolean alphaFromGrayscale = false;
	
	private Color outOfBoundsColor = Color.black;
	
	public ImageData(Image source) {
		this(source, true);
	}
	
	public ImageData(Image source, boolean cacheColors) {
		Texture texture = source.getTexture();
		
		pixelData = texture.getTextureData();
		
		imageWidth = source.getWidth();
		imageHeight = source.getHeight();
		
		textureWidth = texture.getTextureWidth();
		//textureHeight = texture.getTextureHeight();
		
		hasAlpha = texture.hasAlpha();
		
		colorCache = cacheColors ? new Color[imageWidth * imageHeight] : null;
	}
	
	public ImageData setOutOfBoundsColor(Color outOfBounds) {
		outOfBoundsColor = outOfBounds;
		
		return this;
	}
	
	public ImageData setAlphaFromGrayscale() {
		alphaFromGrayscale = true;
		return this;
	}
	
	public Color getColor(int x, int y) {
		if (x < 0 || y < 0 || x >= imageWidth || y >=imageHeight)
			return outOfBoundsColor;
		else {
			Color color = colorCache[y * imageWidth + x];
			if (color == null) {
				int offset = x + (y * textureWidth);
				offset *= hasAlpha ? 4 : 3;
				
				if (hasAlpha) {
					color = new Color(Byte.toUnsignedInt(pixelData[offset  ]),Byte.toUnsignedInt(pixelData[offset+1]),
							          Byte.toUnsignedInt(pixelData[offset+2]),Byte.toUnsignedInt(pixelData[offset+3]));
				}
				else {
					// alpha will just default to 1
					color = new Color(Byte.toUnsignedInt(pixelData[offset]),Byte.toUnsignedInt(pixelData[offset+1]),
							          Byte.toUnsignedInt(pixelData[offset+2]));
				}
				
				if (alphaFromGrayscale) {
					color.a = 0.2989f * color.r + 0.5870f * color.g + 0.1140f * color.b;
				}
				
				colorCache[y * imageWidth + x] = color;
			}
			
			return color;
		}
	}
}
