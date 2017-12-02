package org.newdawn.slick;

import org.newdawn.slick.Image;
import org.newdawn.slick.util.FastTrig;


public class ExtendedImageDrawer extends Image {
	
	/** rotationCenter: on screen! */
	public static void drawEmbedded(Image image, float x, float y, float x_2, float y_2, float srcx, float srcy, float srcx2, float srcy2, float rotation, float rotationCenterX, float rotationCenterY) {
		if(rotation == 0) {
			image.drawEmbedded(x, y, x_2, y_2, srcx, srcy, srcx2, srcy2);
			return;
		}
		image.init();
		//float mywidth = x_2 - x;
		//float myheight = y_2 - y;
		float texwidth = srcx2 - srcx;
		float texheight = srcy2 - srcy;

		float newTextureOffsetX = (((srcx) / (image.width)) * image.textureWidth)
				+ image.textureOffsetX;
		float newTextureOffsetY = (((srcy) / (image.height)) * image.textureHeight)
				+ image.textureOffsetY;
		float newTextureWidth = ((texwidth) / (image.width))
				* image.textureWidth;
		float newTextureHeight = ((texheight) / (image.height))
				* image.textureHeight;
		
		//float scaleX = mywidth/image.width;
		//float scaleY = myheight/image.height;

		float cx = rotationCenterX; // * scaleX
		float cy = rotationCenterY; // * scaleY
		
		//TOP LEFT
		float p1x = x-cx;
		float p1y = y-cy;
		//TOP RIGHT
		float p2x = x_2 - cx;
		float p2y = y-cy;
		//BOTTOM RIGHT
		float p3x = x_2 - cx;
		float p3y = y_2 - cy;
		//BOTTOM LEFT
		float p4x = x-cx;
		float p4y = y_2 - cy;

		double rad = Math.toRadians(rotation);
		final float cos = (float) FastTrig.cos(rad);
		final float sin = (float) FastTrig.sin(rad);

		float tx = newTextureOffsetX;
		float ty = newTextureOffsetY;
		float tw = newTextureWidth;
		float th = newTextureHeight;
		
		// TOP LEFT
		float x1 = (cos * p1x - sin * p1y) + cx; 
		float y1 = (sin * p1x + cos * p1y) + cy;
		// BOTTOM LEFT
		float x2 = (cos * p4x - sin * p4y) + cx;
		float y2 = (sin * p4x + cos * p4y) + cy;
		// BOTTOM RIGHT
		float x3 = (cos * p3x - sin * p3y) + cx; 
		float y3 = (sin * p3x + cos * p3y) + cy;
		// TOP RIGHT
		float x4 = (cos * p2x - sin * p2y) + cx; 
		float y4 = (sin * p2x + cos * p2y) + cy;
		if (image.corners == null) {
			GL.glTexCoord2f(tx, ty);
			GL.glVertex3f(x1, y1, 0);
			GL.glTexCoord2f(tx, ty + th);
			GL.glVertex3f(x2, y2, 0);
			GL.glTexCoord2f(tx + tw, ty + th);
			GL.glVertex3f(x3, y3, 0);
			GL.glTexCoord2f(tx + tw, ty);
			GL.glVertex3f(x4, y4, 0);
		} else {
			image.corners[TOP_LEFT].bind();
			GL.glTexCoord2f(tx, ty);
			GL.glVertex3f(x1, y1, 0);
			image.corners[BOTTOM_LEFT].bind();
			GL.glTexCoord2f(tx, ty + th);
			GL.glVertex3f(x2, y2, 0);
			image.corners[BOTTOM_RIGHT].bind();
			GL.glTexCoord2f(tx + tw, ty + th);
			GL.glVertex3f(x3, y3, 0);
			image.corners[TOP_RIGHT].bind();
			GL.glTexCoord2f(tx + tw, ty);
			GL.glVertex3f(x4, y4, 0);
		}
	}
	/*
	
	
	public void drawEmbedded(float x, float y, float width, float height, float rotation) {
		if (rotation==0) {
			drawEmbedded(x, y, width, height);
			return;
		}
		init();
		float scaleX = width/this.width;
		float scaleY = height/this.height;

		float cx = getCenterOfRotationX()*scaleX;
		float cy = getCenterOfRotationY()*scaleY;

		float p1x = -cx;
		float p1y = -cy;
		float p2x = width - cx;
		float p2y = -cy;
		float p3x = width - cx;
		float p3y = height - cy;
		float p4x = -cx;
		float p4y = height - cy;

		double rad = Math.toRadians(rotation);
		final float cos = (float) FastTrig.cos(rad);
		final float sin = (float) FastTrig.sin(rad);

		float tx = getTextureOffsetX();
		float ty = getTextureOffsetY();
		float tw = getTextureWidth();
		float th = getTextureHeight();

		float x1 = (cos * p1x - sin * p1y) + cx; // TOP LEFT
		float y1 = (sin * p1x + cos * p1y) + cy;
		float x2 = (cos * p4x - sin * p4y) + cx; // BOTTOM LEFT
		float y2 = (sin * p4x + cos * p4y) + cy;
		float x3 = (cos * p3x - sin * p3y) + cx; // BOTTOM RIGHT
		float y3 = (sin * p3x + cos * p3y) + cy;
		float x4 = (cos * p2x - sin * p2y) + cx; // TOP RIGHT
		float y4 = (sin * p2x + cos * p2y) + cy;
		if (corners == null) {
			GL.glTexCoord2f(tx, ty);
			GL.glVertex3f(x+x1, y+y1, 0);
			GL.glTexCoord2f(tx, ty + th);
			GL.glVertex3f(x+x2, y+y2, 0);
			GL.glTexCoord2f(tx + tw, ty + th);
			GL.glVertex3f(x+x3, y+y3, 0);
			GL.glTexCoord2f(tx + tw, ty);
			GL.glVertex3f(x+x4, y+y4, 0);
		} else {
			corners[TOP_LEFT].bind();
			GL.glTexCoord2f(tx, ty);
			GL.glVertex3f(x+x1, y+y1, 0);
			corners[BOTTOM_LEFT].bind();
			GL.glTexCoord2f(tx, ty + th);
			GL.glVertex3f(x+x2, y+y2, 0);
			corners[BOTTOM_RIGHT].bind();
			GL.glTexCoord2f(tx + tw, ty + th);
			GL.glVertex3f(x+x3, y+y3, 0);
			corners[TOP_RIGHT].bind();
			GL.glTexCoord2f(tx + tw, ty);
			GL.glVertex3f(x+x4, y+y4, 0);
		}
	}
	
	public void drawEmbedded(float x, float y, float x2, float y2, float srcx, float srcy, float srcx2, float srcy2, Color filter) {
		init();
		if (filter != null) {
			filter.bind();
		}

		float mywidth = x2 - x;
		float myheight = y2 - y;
		float texwidth = srcx2 - srcx;
		float texheight = srcy2 - srcy;

		float newTextureOffsetX = (((srcx) / (width)) * textureWidth)
				+ textureOffsetX;
		float newTextureOffsetY = (((srcy) / (height)) * textureHeight)
				+ textureOffsetY;
		float newTextureWidth = ((texwidth) / (width))
				* textureWidth;
		float newTextureHeight = ((texheight) / (height))
				* textureHeight;

		GL.glTexCoord2f(newTextureOffsetX, newTextureOffsetY);
		GL.glVertex3f(x,y, 0.0f);
		GL.glTexCoord2f(newTextureOffsetX, newTextureOffsetY
				+ newTextureHeight);
		GL.glVertex3f(x,(y + myheight), 0.0f);
		GL.glTexCoord2f(newTextureOffsetX + newTextureWidth,
				newTextureOffsetY + newTextureHeight);
		GL.glVertex3f((x + mywidth),(y + myheight), 0.0f);
		GL.glTexCoord2f(newTextureOffsetX + newTextureWidth,
				newTextureOffsetY);
		GL.glVertex3f((x + mywidth),y, 0.0f);
	}
	
	*/
}
