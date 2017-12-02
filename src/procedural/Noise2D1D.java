package procedural;

import main.Utilities;

public abstract class Noise2D1D {
	
	protected float weight;
	protected float spacing;
	
	public Noise2D1D(float weight, float spacing) {
        this.weight = weight;
        this.spacing = spacing;
	}
    
    public float getWeightedValue(float x, float y) {
        int localX = indexOf(x);
        int localY = indexOf(y);
        
        float dX = (x - spacing*localX) / spacing;
        float dY = (y - spacing*localY) / spacing;
        
        //According to testing, cubic interpolation between values from 0 to 1 can be in range from -0.25 to 1.25
        //And cubic interpolation between values from -0.25 to 1.25 is in range from -0.625 to 1.625
        //Thus, the value has to be scaled.
        return weight * (Utilities.interpolateCubic(
        					Utilities.interpolateCubic(getValueAt(localX-1, localY-1), getValueAt(localX, localY-1), getValueAt(localX+1, localY-1), getValueAt(localX+2, localY-1), dX),
                            Utilities.interpolateCubic(getValueAt(localX-1, localY  ), getValueAt(localX, localY  ), getValueAt(localX+1, localY  ), getValueAt(localX+2, localY  ), dX),
                            Utilities.interpolateCubic(getValueAt(localX-1, localY+1), getValueAt(localX, localY+1), getValueAt(localX+1, localY+1), getValueAt(localX+2, localY+1), dX),
                            Utilities.interpolateCubic(getValueAt(localX-1, localY+2), getValueAt(localX, localY+2), getValueAt(localX+1, localY+2), getValueAt(localX+2, localY+2), dX),
                            dY
                        ) + 0.625f) / 2.25f;
                
        
    }
    
    protected int indexOf(float f) {
    	return (int) (f / spacing) - (f < 0 ? 1 : 0);
    }
	
	protected abstract float getValueAt(int x, int y);
}
