package main;

import java.util.Collection;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

public class Fonts {
	
	/*
	 * Font situation in Slick2D:
	 * - SpriteSheetFont: Monospace only. No fancy stuff (pretty much only ASCII).
	 * - UnicodeFont: Broken all around. Do not use.
	 * - TrueTypeFont: Works. Can be derived from system fonts. Not sure: Supports \n?
	 * - AngelCodeFont: Works. Hard to get. Not sure: Supports \n?     // <-- one of these two does, but I forgot which.
	 */
	
	public static final AngelCodeFont TEMPORARY_FONT = createTemporaryAngelCodeFont();
	
	public static final TrueTypeFont SANS_SERIF = new TrueTypeFont(new java.awt.Font("Sans Serif", java.awt.Font.BOLD, 14), false);
	public static final TrueTypeFont CONSOLE_FONT = new TrueTypeFont(new java.awt.Font("Fixedsys", java.awt.Font.BOLD, 14), false);
	
	public static final Font BIG_BUTTON_FONT = new TrueTypeFont(new java.awt.Font("Sans Serif", java.awt.Font.BOLD, 40), true);
	
	private static final AngelCodeFont createTemporaryAngelCodeFont() {
		try {
			return new AngelCodeFont("temporarily",
					ClassLoader.getSystemResourceAsStream(FilePaths.FONT_DIR + "aurulent-sans-16.fnt"),
					ClassLoader.getSystemResourceAsStream(FilePaths.FONT_DIR + "aurulent-sans-16_0.png"));
		}
		catch(Throwable t) {
			Logger.print("Error creating temporary AngelCodeFont!");
			t.printStackTrace();
		}
		return null;
	}
	
	/** Little helper method. Hacky, but it has been working ... so far. */
	public static float maxFontHeight(Font font) {
		return font.getHeight("HjgusiftqÜßy");
	}
	
	public static float maxStringWidth(Font font, Collection<String> lines) {
		float max = 0;
		for (String line : lines) {
			float width = font.getWidth(line);
			if (width > max)
				max = width;
		}
		return max;
	}
	
	public static void drawScaledStringCentered(Graphics g, String text, float centerX, float centerY, float scale) {
		//boolean wasAlias = g.isAntiAlias();
		//g.setAntiAlias(true); // <-- DO NOT ENABLE ALIAS ON g - it will cause diagonal line artifacts on every element that is rendered ...
		
		g.scale(scale, scale);
		g.drawString(text, centerX / scale - g.getFont().getWidth(text) / 2, centerY / scale - g.getFont().getHeight(text) / 2);
		g.scale( 1 / scale,  1 / scale );
		
		//g.setAntiAlias(wasAlias);
	}
}
