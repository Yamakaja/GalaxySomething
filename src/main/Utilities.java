package main;

import java.util.List;
import java.util.Random;

import org.newdawn.slick.Image;

public class Utilities {
	public static final UtilRandom RANDOM = new UtilRandom();
	
	/** Helper method */
	public static void handleDestruction(Image... images) {
		for (int i = 0; i < images.length; i++) {
			try {
				images[i].destroy();
			}
			catch (Throwable t) {
				ExceptionHandler.handleErrorDestroyingImage(t);
			}
		}
	}
	
	/**
     * Generates a random value in (-1, 1) from the three parameters.
     * @param seed
     * @param x
     * @param y
     * @return 
     */
	public static float pseudoRandomFloat(int seed, int x, int y) {
		int n = x + y * seed;
		n = (n<<13) ^ n;
		return ( 1f - ( (n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824f);
	}
    
    /**
	 * @param v0 Wert vor v1
	 * @param v1 und v2: Die Werte, zwischen denen sich x befindet
	 * @param v3 Wert nach v2
	 * @param x Wert zwischen 0 u. 1, der den Fortschritt von X zwischen v1 und v2 angibt
	 */
	public static float interpolateCubic(float v0, float v1, float v2, float v3, float x) {
        //if (true) return (1-x) * v1 + x * v2;
            
        //Logger.print("Interpolating cubic with values " + v0 + ", " + v1 + ", " + v2 + ", " + v3 + ", " + x);
		/*float P = (v3 - v2) - (v0 - v1),
			  Q = (v0 - v1) - P,
			  R = v2 - v0,
			  S = v1;
		return P*x*x*x + Q*x*x + R*x + S;*/
		float P = (v3 - v2) - (v0 - v1);
		return P*x*x*x + ((v0 - v1) - P)*x*x + (v2 - v0)*x + v1;
	}
	
	public static float smootherstep(float edge0, float edge1, float x) {
	    // Scale
	    x = (x - edge0)/(edge1 - edge0);
	    // clamp x to 0..1 range or evaluate polynomial (nach Hornerschema)
	    return x <= 0 ? 0 : x >= 1 ? 1 : x*x*x*(x*(x*6 - 15) + 10);
	}

	public static int[] createShuffledIndexArray(Random random, int planetSize) {
		int[] array = new int[planetSize];
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}
		int j; // element at index j will be swapped with index i
		int element;
		for (int i = array.length - 1; i > 0; i--) {
			j = random.nextInt(i);
			element = array[j];
			array[j] = array[i];
			array[i] = element;
		}
		
		return array;
	}
    
	public static void splitAndIterateAreaMultithreaded(int minX, int minY, int sizeX, int sizeY, int threadsX, int threadsY, final ConcurrentAreaIterator iterator) {
	    Thread[] threads = new Thread[threadsX * threadsY];
	    
	    int avrgSpanPerThreadX = sizeX / threadsX;
	    int avrgSpanPerThreadY = sizeY / threadsY;
	
	    for (int i = 0; i < threadsX; i++) {
	        final int startX = minX + i * avrgSpanPerThreadX;
	        final int numX = i + 1 == threadsX ? sizeX - startX : avrgSpanPerThreadX;
	        for (int j = 0; j < threadsY; j++) {
	            final int startY = minY + j * avrgSpanPerThreadY;
	            final int numY = j + 1 == threadsY ? sizeY - startY : avrgSpanPerThreadY;
	
	            threads[i * threadsY + j] = new Thread(new Runnable() {
	                public void run() {
	                    for (int x = 0; x < numX; x++) {
	                        for (int y = 0; y < numY; y++) {
	                            iterator.iterate(startX + x, startY + y);
	                        }
	                    }
	                }
	            });
	            threads[i * threadsY + j].start();
	        }
	    }
	
	    for (Thread thread : threads) {
	        try {
	            thread.join();
	        } catch (InterruptedException ex) {
	
	        }
	    }
	}
	
	public static interface ConcurrentAreaIterator {
	    public void iterate(int x, int y);
	}
	
	public static class UtilRandom extends Random {
		private static final long serialVersionUID = -4480393838777536714L;
		
		public float nextFloat(float a, float b) {
			return a+nextFloat()*(b-a);
		}
		
		/** @return in einem von x Fällen true */
		public boolean chance(int x) {
			return nextInt(x) == 0;
		}
		
		/**
		 * @return eine Ganzzahl zwischen a und b (all inclusive)
		 */
		public int nextInt(int a, int b) {
			return a + nextInt(b-a+1);
			//für a == 3 und b == 5:    3 + RANDOM.nextInt(5-3+1) == 3 + RANDOM.nextInt(3) == 3 + [0,1,2] == 3-5
		}
		
		/** float von -1 bis .999 */
		public float nextSignedFloat() {
			return nextFloat() * 2 - 1;
		}
	    
		public <T> T selectRandomElement(List<T> list) {
			if(list.isEmpty())
				return null;
			else
				return list.get(nextInt(list.size()));
		}
		
		public <T> T selectRandomElement(T[] array) {
			if (array.length == 0)
				return null;
			else
				return array[nextInt(array.length)];
		}
	}
}
