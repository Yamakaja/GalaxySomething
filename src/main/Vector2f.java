package main;

import java.io.Serializable;

import org.newdawn.slick.util.FastTrig;

/**
* A two dimensional vector
*
* @author Kevin Glass
*/
public strictfp class Vector2f implements Serializable {
	/** The version ID for this class */
    private static final long serialVersionUID = 1339934L;
        
    private float x, y;
    private float length = -1;
        
    /**
     * Create an empty vector
     */
    public Vector2f() {
    	x = 0;
    	y = 0;
    }
    
    /**
     * Create a new vector based on an angle
     *
     * @param theta The angle of the vector in degrees
     */
    public Vector2f(double theta) {
    	x = 1;
    	y = 1;
        setTheta(theta);
    }

    /**
     * Calculate the components of the vectors based on a angle
     *
     * @param theta The angle to calculate the components from (in degrees)
     */
    public void setTheta(double theta) {
        // Next lines are to prevent numbers like -1.8369701E-16
        // when working with negative numbers
        if ((theta < -360) || (theta > 360)) {
                theta = theta % 360;
        }
        if (theta < 0) {
                theta = 360 + theta;
        }

        float len = length();
        x = len * (float) FastTrig.cos(StrictMath.toRadians(theta));
        y = len * (float) FastTrig.sin(StrictMath.toRadians(theta));
        
//                x = x / (float) FastTrig.cos(StrictMath.toRadians(oldTheta))
//                                * (float) FastTrig.cos(StrictMath.toRadians(theta));
//                y = x / (float) FastTrig.sin(StrictMath.toRadians(oldTheta))
//                                * (float) FastTrig.sin(StrictMath.toRadians(theta));
    }
    
    /**
     * Adjust this vector by a given angle
     *
     * @param theta
     * The angle to adjust the angle by (in degrees)
     * @return This vector - useful for chaining operations
     *
     */
    public Vector2f add(double theta) {
        setTheta(getTheta() + theta);
        
        return this;
    }

    /**
     * Adjust this vector by a given angle
     *
     * @param theta The angle to adjust the angle by (in degrees)
     * @return This vector - useful for chaining operations
     */
    public Vector2f sub(double theta) {
        setTheta(getTheta() - theta);
        
        return this;
    }
    
    /**
     * Get the angle this vector is at
     *
     * @return The angle this vector is at (in degrees)
     */
    public double getTheta() {
        double theta = StrictMath.toDegrees(StrictMath.atan2(x, y));
        if ((theta < -360) || (theta > 360)) {
                theta = theta % 360;
        }
        if (theta < 0) {
                theta = 360 + theta;
        }

        return theta;
    }
    
    /**
     * Get the x component
     *
     * @return The x component
     */
    public float getX() {
        return x;
    }

    /**
     * Get the y component
     *
     * @return The y component
     */
    public float getY() {
        return y;
    }
    
    /**
     * Create a new vector based on another
     *
     * @param other The other vector to copy into this one
     */
    public Vector2f(Vector2f other) {
        this(other.getX(),other.getY());
        this.length = other.length();
    }
    
    /**
     * Create a new vector
     *
     * @param x The x component to assign
     * @param y The y component to assign
     */
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    private Vector2f(float x, float y, float length) {
    	this(x, y);
    	this.length = length;
    }

    /**
     * Set the value of this vector
     *
     * @param other The values to set into the vector
     */
    public Vector2f set(Vector2f other) {
    	set(other.getX(),other.getY());
    	length = other.length;
    	return this;
    }
    
    /**
     * Dot this vector against another
     *
     * @param other The other vector dot agianst
     * @return The dot product of the two vectors
     */
    public float dot(Vector2f other) {
        return (x * other.x) + (y * other.y);
    }
    
    /**
     * Set the values in this vector
     *
     * @param x The x component to set
     * @param y The y component to set
     * @return This vector - useful for chaining operations
     */
    public Vector2f set(float x, float y) {
    	this.x = x;
    	this.y = y;
        length = -1;
        return this;
    }
    
    /**
     * A vector perpendicular to this vector.
     *
     * @return a vector perpendicular to this vector
     */
    public Vector2f getPerpendicular() {
    	return new Vector2f(-y, x, length);
    }
    
    /**
     * Negate this vector
     *
     * @return A copy of this vector negated
     */
    public Vector2f negate() {
    	return new Vector2f(-x, -y, length);
    }

    /**
     * Negate this vector without creating a new copy
     *
     * @return This vector - useful for chaning operations
     */
    public Vector2f negateLocal() {
        x = -x;
        y = -y;
        return this;
    }
    
    /**
     * Add a vector to this vector
     *
     * @param v The vector to add
     * @return This vector - useful for chaning operations
     */
    public Vector2f addLocal(Vector2f v) {
        x += v.x;
        y += v.y;
        length = -1;
        return this;
    }
    
    public Vector2f add(Vector2f v) {
    	return new Vector2f(x+v.x, y+v.y);
    }
    
    /**
     * Subtract a vector from this vector
     *
     * @param v The vector subtract
     * @return This vector - useful for chaining operations
     */
    public Vector2f subtractLocal(Vector2f v) {
	    x -= v.x;
	    y -= v.y;
	    length = -1;
	    return this;
    }
    
    public Vector2f subtract(Vector2f v) {
    	return new Vector2f(x-v.x, y-v.y);
    }

    /**
     * Scale this vector by a value
     *
     * @param a The value to scale this vector by
     * @return This vector - useful for chaining operations
     */
    public Vector2f multiplyLocal(float a) {
        x *= a;
        y *= a;
        length *= Math.abs(a);
        return this;
    }

    /**
     * Normalise the vector
     *
     * @return This vector - useful for chaning operations
     */
    public Vector2f normaliseLocal() {
        float l = length();
        
        if (l == 0) {
        	return this;
        }
        
        x /= l;
        y /= l;
        length = 1;
        return this;
    }
    
    /**
	 * The normal of the vector
	 *
	 * @return A unit vector with the same direction as the vector
	 */
	public Vector2f getNormal() {
	     return copy().normaliseLocal();
	}
	
	/** Returns a normalised copy of this vector */
	public Vector2f normalise() {
		return getNormal();
	}

    /**
     * The length of the vector squared
     *
     * @return The length of the vector squared
     */
    public float lengthSquared() {
    	return x*x + y*y;
    }
    
    /**
     * Get the length of this vector
     *
     * @return The length of this vector
     */
    public float length() {
    	if(length <= 0)
    		length = (float)Math.sqrt(x*x+y*y);
    	return length;
    }
    
    /**
     * Project this vector onto another
     *
     * @param b The vector to project onto
     * @param result The projected vector
     */
    public void projectOntoUnit(Vector2f b, Vector2f result) {
        float dp = b.dot(this);
        
        result.x = dp * b.x;
        result.y = dp * b.y;
    }
    
    /**
     * Return a copy of this vector
     *
     * @return The new instance that copies this vector
     */
    public Vector2f copy() {
    	return new Vector2f(x, y, length);
    }
    
    /**
     * Get the distance from this point to another
     *
     * @param other The other point we're measuring to
     * @return The distance to the other point
     */
    public float distance(Vector2f other) {
        return (float) Math.sqrt(distanceSquared(other));
    }
    
    /**
     * Get the distance from this point to another, squared. This
     * can sometimes be used in place of distance and avoids the
     * additional sqrt.
     *
     * @param other The other point we're measuring to
     * @return The distance to the other point squared
     */
    public float distanceSquared(Vector2f other) {
        float dx = other.x - x;
        float dy = other.y - y;
        
        return (float) (dx*dx)+(dy*dy);
    }
    
    public float distanceSquared(float otherx, float othery) {
        float dx = otherx - x;
        float dy = othery - y;
        
        return (float) (dx*dx)+(dy*dy);
    }
    
    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
    	//large primes!
    	return 997 * ((int)x) ^ 991 * ((int)y);
    }
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {
        if (other instanceof Vector2f) {
            Vector2f o = ((Vector2f) other);
            return (o.x == x) && (o.y == y);
        }
        
        return false;
    }
    
    public Vector2f multiply(Vector2f bVector) {
		return new Vector2f(x*bVector.x, y*y);
	}
    
    public Vector2f divide(Vector2f bVector) {
		return new Vector2f(x/bVector.x, y/bVector.y);
	}

	public Vector2f multiply(float value) {
		return new Vector2f(x*value, y*value, length*Math.abs(value));
	}

	public Vector2f divide(float value) {
		return new Vector2f(x/value, y/value, length/Math.abs(value));
	}

	public Vector2f divideLocal(float value) {
		x /= value;
		y /= value;
		length /= Math.abs(value);
		return this;
	}
	
	public String toString() {
		return "Vector2f("+((int)(x*100))/100f+","+((int)(y*100))/100f+")";
	}

	public void setX(float a) {
		x = a;
		length = -1;
	}
	
	public void setY(float b) {
		y = b;
		length = -1;
	}
	
	public void modX(float a) {
		x += a;
		length = -1;
	}
	
	public void modY(float b) {
		y += b;
		length = -1;
	}
}