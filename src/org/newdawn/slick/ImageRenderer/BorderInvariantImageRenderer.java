package org.newdawn.slick.ImageRenderer;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class BorderInvariantImageRenderer extends ImageRenderer {
	private float x1, x2, x3, x4, y1, y2, y3, y4;
	
	
	public BorderInvariantImageRenderer setImageRenderParameters(float x1, float x2, float x3, float x4, float y1, float y2, float y3, float y4) {
		this.x1 = x1;
		this.x2 = x2;
		this.x3 = x3;
		this.x4 = x4;
		this.y1 = y1;
		this.y2 = y2;
		this.y3 = y3;
		this.y4 = y4;
		return this;
	}
	
	@Override
	public void render(Graphics g, Image image) {
		if(x4 - x1 > renderWidth) {
			if(y4 - y1 > renderHeight) {
				// image is downscaled => scale entire source down equally to prevent artifacts
				g.drawImage(image, renderX - epsilon, renderY - epsilon, renderX+renderWidth + epsilon, renderY+renderHeight + epsilon,
							       x1 + epsilon, y1 + epsilon, x4 - epsilon, y4 - epsilon);
				return;
			}
			else {
				// x wird scaliert, y aber nicht
				image.startUse();
				// Von oben nach unten kann ich die Streifen als ganze Einheiten rendern, da x-Skalierung nicht individuell gilt
				image.drawEmbedded(renderX - epsilon, renderY - epsilon, renderX+renderWidth + epsilon, renderY+(y2-y1) + epsilon,
							       x1 + epsilon, y1 + epsilon, x4 - epsilon, y2 - epsilon);
				image.drawEmbedded(renderX - epsilon, renderY+(y2-y1) - epsilon, renderX+renderWidth + epsilon, renderY+renderHeight-(y4-y3) + epsilon,
								   x1 + epsilon, y2 + epsilon, x4 - epsilon, y3 - epsilon);
				image.drawEmbedded(renderX - epsilon, renderY+renderHeight-(y4-y3) - epsilon, renderX+renderWidth + epsilon, renderY+renderHeight + epsilon,
								   x1 + epsilon, y3 + epsilon, x4 - epsilon, y4 - epsilon);
				image.endUse();
				return;
			}
		}
		else {
			if(y4 - y1 > renderHeight) {
				// y wird scaliert, x aber nicht
				image.startUse();
				// Von links nach rechts kann ich die Streifen als ganze Einheiten rendern, da y-Skalierung nicht individuell gilt
				image.drawEmbedded(renderX - epsilon, renderY - epsilon, renderX+(x2-x1) + epsilon, renderY+renderHeight + epsilon,
								   x1 + epsilon, y1 + epsilon, x2 - epsilon, y4 - epsilon);
				image.drawEmbedded(renderX+(x2-x1) - epsilon, renderY - epsilon, renderX+renderWidth-(x4-x3) + epsilon, renderY+renderHeight + epsilon,
								   x2 + epsilon, y1 + epsilon, x3 - epsilon, y4 - epsilon);
				image.drawEmbedded(renderX+renderWidth-(x4-x3) - epsilon, renderY - epsilon, renderX+renderWidth + epsilon, renderY+renderHeight + epsilon,
								   x3 + epsilon, y1 + epsilon, x4 - epsilon, y4 - epsilon);
				image.endUse();
				return;
			}
			else {
				image.startUse();
				
				// oben links
				image.drawEmbedded(renderX - epsilon, renderY - epsilon, renderX+(x2-x1) + epsilon, renderY+(y2-y1) + epsilon,
								   x1 + epsilon, y1 + epsilon, x2 - epsilon, y2 - epsilon);
				// oben mitte
				image.drawEmbedded(renderX+(x2-x1) - epsilon, renderY - epsilon, renderX+renderWidth-(x4-x3) + epsilon, renderY+(y2-y1) + epsilon,
								   x2 + epsilon, y1 + epsilon, x3 - epsilon, y2 - epsilon);
				// oben rechts
				image.drawEmbedded(renderX+renderWidth-(x4-x3) - epsilon, renderY - epsilon, renderX+renderWidth + epsilon, renderY+(y2-y1) + epsilon,
								   x3 + epsilon, y1 + epsilon, x4 - epsilon, y2 - epsilon);
				// mitte links
				image.drawEmbedded(renderX - epsilon, renderY+(y2-y1) - epsilon, renderX+(x2-x1) + epsilon, renderY+renderHeight-(y4-y3) + epsilon,
								   x1 + epsilon, y2 + epsilon, x2 - epsilon, y3 - epsilon);
				// mitte mitte
				image.drawEmbedded(renderX+(x2-x1) - epsilon, renderY+(y2-y1) - epsilon, renderX+renderWidth-(x4-x3) + epsilon, renderY+renderHeight-(y4-y3) + epsilon,
								   x2 + epsilon, y2 + epsilon, x3 - epsilon, y3 - epsilon);
				// mitte rechts
				image.drawEmbedded(renderX+renderWidth-(x4-x3) - epsilon, renderY+(y2-y1) - epsilon, renderX+renderWidth + epsilon, renderY+renderHeight-(y4-y3) + epsilon,
								   x3 + epsilon, y2 + epsilon, x4 - epsilon, y3 - epsilon);
				// unten links
				image.drawEmbedded(renderX - epsilon, renderY+renderHeight-(y4-y3) - epsilon, renderX+(x2-x1) + epsilon, renderY+renderHeight + epsilon,
								   x1 + epsilon, y3 + epsilon, x2 - epsilon, y4 - epsilon);
				// unten mitte
				image.drawEmbedded(renderX+(x2-x1) - epsilon, renderY+renderHeight-(y4-y3) - epsilon, renderX+renderWidth-(x4-x3) + epsilon, renderY+renderHeight + epsilon,
							       x2 + epsilon, y3 + epsilon, x3 - epsilon, y4 - epsilon);
				// unten rechts
				image.drawEmbedded(renderX+renderWidth-(x4-x3) - epsilon, renderY+renderHeight-(y4-y3) - epsilon, renderX+renderWidth + epsilon, renderY+renderHeight + epsilon,
								   x3 + epsilon, y3 + epsilon, x4 - epsilon, y4 - epsilon);
				
				image.endUse();
				return;
			}
		}
	}
	
	public BorderInvariantImageRenderer calculateImageRenderParametersFromImage() {
		return calculateImageRenderParametersFromImage(defaultImage);
	}
	
	public BorderInvariantImageRenderer calculateImageRenderParametersFromImage(Image image) {
		x1 = 0;
		x2 = image.getWidth()*1/3f;
		x3 = image.getWidth()*2/3f;
		x4 = image.getWidth();
		
		y1 = 0;
		y2 = image.getHeight()*1/3f;
		y3 = image.getHeight()*2/3f;
		y4 = image.getHeight();
		
		return this;
	}
}
