package org.newdawn.slick.fixed;

import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 * A simple Circle geometry
 * WITH A FIXED contains(x, y) METHOD !!! (Seriously, how could you fuck this up?!?)
 * 
 * @author Kevin Glass
 */
public strictfp class Circle extends Ellipse {
	
	private static final long serialVersionUID = -4035704097579925392L;
	
	/** The radius of the circle */
	public float radius;
	
	/** The squared radius of the circle */
	public float radiusSQ;
	
	/**
	 * Create a new circle based on its radius
	 * 
	 * @param centerPointX The x location of the center of the circle
	 * @param centerPointY The y location of the center of the circle
	 * @param radius The radius of the circle
	 */
	public Circle(float centerPointX, float centerPointY, float radius) {
        this(centerPointX, centerPointY, radius, DEFAULT_SEGMENT_COUNT);
	}

	/**
	 * Create a new circle based on its radius
	 * 
	 * @param centerPointX The x location of the center of the circle
	 * @param centerPointY The y location of the center of the circle
	 * @param radius The radius of the circle
	 * @param segmentCount The number of segments to build the circle out of
	 */
	public Circle(float centerPointX, float centerPointY, float radius, int segmentCount) {
        super(centerPointX, centerPointY, radius, radius, segmentCount);
        this.x = centerPointX - radius;
        this.y = centerPointY - radius;
        this.radius = radius;
        this.radiusSQ = radius * radius;
        boundingCircleRadius = radius;
	}
	
	/** 
	 * Get the x coordinate of the centre of the circle
	 * 
	 * @return The x coordinate of the centre of the circle
	 */
	public float getCenterX() {
		return getX() + radius;
	}
	
	/** 
	 * Get the y coordinate of the centre of the circle
	 * 
	 * @return The y coordinate of the centre of the circle
	 */
	public float getCenterY() {
		return getY() + radius;
	}
	
	/**
	 * Set the radius of this circle
	 * 
	 * @param radius The radius of this circle
	 */
	public void setRadius(float radius) {
		if (radius != this.radius) {
	        pointsDirty = true;
			this.radius = radius;
	        this.radiusSQ = radius * radius;
	        setRadii(radius, radius);
		}
	}
	
	/**
	 * Get the radius of the circle
	 * 
	 * @return The radius of the circle
	 */
	public float getRadius() {
		return radius;
	}
	
	/**
	 * Check if this circle touches another shape
	 * 
	 * @param shape The other shape
	 * @return True if they touch
	 */
	public boolean intersects(Shape shape) {
        if(shape instanceof Circle) {
            return intersectsWithCircle((Circle)shape);
        }
        else if(shape instanceof Rectangle) {
            return intersects((Rectangle)shape);
        }
        else {
            return super.intersects(shape);
        }
	}
	
	/**
	 * Check if this circle touches another
	 * 
	 * @param other The other circle
	 * @return True if they touch
	 */
	public boolean intersectsWithCircle(Circle other) {
		float totalRad2 = getRadius() + other.getRadius();
		
		float dx = Math.abs(other.getCenterX() - getCenterX());
		if (dx > totalRad2) {
			return false;
		}
		
		float dy = Math.abs(other.getCenterY() - getCenterY());
		if (dy > totalRad2) {
			return false;
		}
		
		totalRad2 *= totalRad2;
		
		return totalRad2 >= ((dx*dx) + (dy*dy));
	}
	
	/**
	 * Check if a point is contained by this circle
	 * 
	 * @param x The x coordinate of the point to check
	 * @param y The y coorindate of the point to check
	 * @return True if the point is contained by this circle
	 */
    public boolean contains(float x, float y)  { 
        return distanceSquaredTo(x, y) <= radiusSQ; 
    }
    
    /** 
     * @return the squared distance of the point (x, y) to this circle's center
     */
    public float distanceSquaredTo(float x, float y) {
    	return (x - getCenterX()) * (x - getCenterX()) + (y - getCenterY()) * (y - getCenterY());
    }
    
    /**
     * Check if circle contains the line 
     * @param line Line to check against 
     * @return True if line inside circle 
     */ 
    private boolean contains(Line line) { 
         return contains(line.getX1(), line.getY1()) && contains(line.getX2(), line.getY2()); 
    }
    
	/**
	 * @see org.newdawn.slick.geom.Ellipse#findCenter()
	 */
    protected void findCenter() {
        center = new float[2];
        center[0] = x + radius;
        center[1] = y + radius;
    }

    /**
     * @see org.newdawn.slick.geom.Ellipse#calculateRadius()
     */
    protected void calculateRadius() {
        boundingCircleRadius = radius;
    }

    /**
	 * Check if this circle touches a rectangle
	 * 
	 * @param other The rectangle to check against
	 * @return True if they touch
	 */
	private boolean intersects(Rectangle other) {
		Rectangle box = other;
		Circle circle = this;
		
		if (box.contains(x+radius,y+radius)) {
			return true;
		}
		
		float x1 = box.getX();
		float y1 = box.getY();
		float x2 = box.getX() + box.getWidth();
		float y2 = box.getY() + box.getHeight();
		
		Line[] lines = new Line[4];
		lines[0] = new Line(x1,y1,x2,y1);
		lines[1] = new Line(x2,y1,x2,y2);
		lines[2] = new Line(x2,y2,x1,y2);
		lines[3] = new Line(x1,y2,x1,y1);
		
		float r2 = circle.radiusSQ;
		
		Vector2f pos = new Vector2f(circle.getCenterX(), circle.getCenterY());
		
		for (int i=0;i<4;i++) {
			float dis = lines[i].distanceSquared(pos);
			if (dis < r2) {
				return true;
			}
		}
		
		return false;
	}
	
	/** 
     * Check if circle touches a line. 
     * @param other The line to check against 
     * @return True if they touch 
     */ 
    private boolean intersects(Line other) { 
        // put it nicely into vectors 
        Vector2f lineSegmentStart = new Vector2f(other.getX1(), other.getY1()); 
        Vector2f lineSegmentEnd = new Vector2f(other.getX2(), other.getY2()); 
        Vector2f circleCenter = new Vector2f(getCenterX(), getCenterY()); 

        // calculate point on line closest to the circle center and then 
        // compare radius to distance to the point for intersection result 
        Vector2f closest; 
        Vector2f segv = lineSegmentEnd.copy().sub(lineSegmentStart); 
        Vector2f ptv = circleCenter.copy().sub(lineSegmentStart); 
        float segvLength = segv.length(); 
        float projvl = ptv.dot(segv) / segvLength; 
        if (projvl < 0) 
        { 
            closest = lineSegmentStart; 
        } 
        else if (projvl > segvLength) 
        { 
            closest = lineSegmentEnd; 
        } 
        else 
        { 
            Vector2f projv = segv.copy().scale(projvl / segvLength); 
            closest = lineSegmentStart.copy().add(projv); 
        } 
        boolean intersects = circleCenter.copy().sub(closest).lengthSquared() <= radiusSQ; 
        
        return intersects; 
    } 
}