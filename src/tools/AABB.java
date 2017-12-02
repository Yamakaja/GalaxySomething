package tools;

public class AABB {
	private float leftX = Float.POSITIVE_INFINITY;
	private float rightX = Float.NEGATIVE_INFINITY;
	private float topY = Float.POSITIVE_INFINITY;
	private float bottomY = Float.NEGATIVE_INFINITY;
	
	public AABB() {
		
	}
	
	public AABB(float x1, float y1, float x2, float y2) {
		set(x1, y1, x2, y2);
	}
	
	public AABB include(float x, float y) {
		leftX = Math.min(x, leftX);
		rightX = Math.max(x, rightX);
		topY = Math.min(y, topY);
		bottomY = Math.max(y, bottomY);
		return this;
	}
	
	public void include(AABB other) {
		include(other.leftX, other.topY);
		include(other.rightX, other.bottomY);
	}
	
	/**
	 * Resets the AABB to occupy exactly the given space
	 */
	public void set(float x1, float y1, float x2, float y2) {
		leftX = x1;
		rightX = x2;
		topY = y1;
		bottomY = y2;
	}
	
	public boolean contains(float x, float y) {
		return x >= leftX && x <= rightX && y >= topY && y <= bottomY;
	}
	
	public String toString() {
		return "AABB[(" + leftX + ", " + topY + "), (" + rightX + ", " + bottomY + ")]";
	}
}
