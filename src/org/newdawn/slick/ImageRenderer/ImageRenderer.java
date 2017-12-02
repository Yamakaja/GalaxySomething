package org.newdawn.slick.ImageRenderer;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

public class ImageRenderer {
	protected float renderX, renderY, renderWidth, renderHeight;
	protected Image defaultImage = null;
	protected float epsilon = 0.08f;
	
	
	public ImageRenderer setImage(Image image) {
		this.defaultImage = image;
		return this;
	}
	
	public void update(float tpf, int delta) {
		// nothing to do here
	}
	
	public void render(Graphics g) {
		render(g, defaultImage);
	}
	
	public void render(Graphics g, Image image) {
		g.drawImage(image, renderX - epsilon, renderY - epsilon, renderX+renderWidth + epsilon, renderY+renderHeight + epsilon,
			       		   epsilon, epsilon, image.getWidth() - epsilon, image.getHeight() - epsilon);
	}
	
	
	
	public ImageRenderer setRenderEpsilon(float renderEpsilon) {
		epsilon = renderEpsilon;
		return this;
	}

	public ImageRenderer setRenderBounds(Rectangle bounds) {
		renderX = bounds.getX();
		renderY = bounds.getY();
		renderWidth = bounds.getWidth();
		renderHeight = bounds.getHeight();
		return this;
	}
	
	public ImageRenderer setRenderBounds(float x, float y, float width, float height) {
		this.renderX = x;
		this.renderY = y;
		this.renderWidth = width;
		this.renderHeight = height;
		return this;
	}
	
	public ImageRenderer setRenderLocation(float x, float y) {
		this.renderX = x;
		this.renderY = y;
		return this;
	}
	
	public ImageRenderer setRenderSize(float width, float height) {
		this.renderWidth = width;
		this.renderHeight = height;
		return this;
	}
	
}
