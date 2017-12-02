package gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.ImageRenderer.ImageRenderer;

import main.Constants;
import main.Fonts;

public class Button extends GuiComponent {
	
	private Image normalImage, hoveredImage;
	private ImageRenderer normalScaleMode, hoveredScaleMode;
	
	private boolean isHovered;
	
	private String label = null;
	
	private Font font;
	private Color textColor;
	private float scaleFactor = 1 / 64f;

	public Button(GameContainer gc, float relativeWidth, float relativeHeight) {
		super(gc, relativeWidth, relativeHeight);
	}

	public Button(GameContainer gc, float relativeX, float relativeY, float relativeWidth, float relativeHeight) {
		super(gc, relativeX, relativeY, relativeWidth, relativeHeight);
	}

	public Button(GameContainer gc, float relativeWidth, float relativeHeight, int i, int j, int n, int m) {
		super(gc, relativeWidth, relativeHeight, i, j, n, m);
	}
	
	private float textScale = 1;
	
	@Override
	public void update(float tpf, int delta) {
		super.update(tpf, delta);
		isHovered = isHovered();
	}
	
	@Override
	public void render(Graphics g) {
		super.render(g);
		
		Image image = isHovered ? hoveredImage : normalImage;
		ImageRenderer scaleMode = isHovered ? hoveredScaleMode : normalScaleMode;
		
		if (scaleMode != null) {
			scaleMode.setRenderBounds(x, y, width, height);
			if (image != null) {
				scaleMode.render(g, image);
			}
			else {
				scaleMode.render(g);
			}
		}
		else {
			image.draw(x + Constants.RENDER_EPSILON, y + Constants.RENDER_EPSILON, width - 2 * Constants.RENDER_EPSILON, height - 2 * Constants.RENDER_EPSILON);
		}
		
		if (label != null) {
			g.setFont(font);
			g.setColor(textColor);
			
			Fonts.drawScaledStringCentered(g, label, x + width / 2, y + height / 2, textScale);
		}
	}
	
	@Override
	public boolean performGenericAction() {
		onClick();
		return true;
	}
	
	@Override
	public boolean mouseClicked(int button, int x, int y, int clickCount) {
		if (!aabb.contains(x, y))
			return false;
		
		if (button == Input.MOUSE_LEFT_BUTTON) {
			onClick();
		}
		else if (button == Input.MOUSE_MIDDLE_BUTTON) {
			onWheelClick();
		}
		else if (button == Input.MOUSE_RIGHT_BUTTON) {
			onRightClick();
		}
		return true;
	}
	
	public void onClick() {
		// for anonymous override
	}
	
	public void onWheelClick() {
		// for anonymous override
	}
	
	public void onRightClick() {
		// for anonymous override
	}
	
	public Button setImages(Image normalImage, Image hoveredImage) {
		this.normalImage = normalImage;
		this.hoveredImage = hoveredImage;
		return this;
	}
	
	public Button setScaleModes(ImageRenderer normalScaleMode, ImageRenderer hoveredScaleMode) {
		this.normalScaleMode = normalScaleMode;
		this.hoveredScaleMode = hoveredScaleMode;
		return this;
	}
	
	public Button setLabel(String label) {
		this.label = label;
		return this;
	}
	
	public Button setLabelStyle(Font font, Color color, float scaleFactor) {
		this.font = font;
		this.textColor = color;
		this.scaleFactor = scaleFactor;
		return this;
	}
	
	@Override
	public void calculateCoordinates() {
		super.calculateCoordinates();
		isHovered = isHovered();
		textScale = scaleFactor * height;
	}
}
