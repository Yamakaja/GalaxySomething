package main;

import org.newdawn.slick.Image;
import org.newdawn.slick.ImageRenderer.BorderInvariantImageRenderer;

import gui.Button;

public class GuiStyle {
	
	private static final BorderInvariantImageRenderer normalButtonImage = new BorderInvariantImageRenderer();
	private static final BorderInvariantImageRenderer hoveredButtonImage = new BorderInvariantImageRenderer();
	
	static {
		Image buttonImage = ImageManager.loadImage(FilePaths.GUI_IMAGE_DIR + "button.png");
		
		normalButtonImage.setImage(buttonImage);
		hoveredButtonImage.setImage(buttonImage);
		
		normalButtonImage.setRenderEpsilon(Constants.RENDER_EPSILON);
		hoveredButtonImage.setRenderEpsilon(Constants.RENDER_EPSILON);
		
		normalButtonImage.setImageRenderParameters(0, 6, 394, 400, 0, 6, 44, 50);
		hoveredButtonImage.setImageRenderParameters(0, 6, 394, 400, 50, 56, 94, 100);
	}
	
	public static Button setupBigButton(Button button) {
		button.setScaleModes(normalButtonImage, hoveredButtonImage);
		button.setLabelStyle(Fonts.BIG_BUTTON_FONT, Colors.GUI_BUTTON_LABEL_COLOR, 1/64f);
		
		return button;
	}
}
