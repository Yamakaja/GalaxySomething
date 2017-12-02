package example;

import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import main.GalaxySomething;
import main.GameState;
import main.Vector2f;

public class BallState extends GameState {
	
	final int initialBalls = 1;
	
	private Ball[] balls = new Ball[3 * initialBalls];
	
	private int numBalls = 0;
	
	@Override
	public void init(AppGameContainer gc, GalaxySomething gs) {
		super.init(gc, gs);
		
		Random random = new Random();
		
		for (int i = 0; i < initialBalls; i++) {
			addBall(new Ball(this,
						new Vector2f(random.nextInt(gc.getWidth()),
								 	 random.nextInt(gc.getHeight())),
								 	 25,
								 	 Color.red));
		}
	}
	
	@Override
	public void update(float tpf, int delta) {
		int nextBalls = 0;
		Ball[] ballsBuffer = new Ball[3 * numBalls];
		for (int i = 0; i < numBalls; i++) {
			balls[i].update(tpf);
			if (!balls[i].isGone()) {
				ballsBuffer[nextBalls++] = balls[i];
			}
		}
		
		numBalls = nextBalls;
		balls = ballsBuffer;
	}

	@Override
	public void render(Graphics g) {
		for (int i = 0; i < numBalls; i++) {
			balls[i].render(g);
		}
	}
	
	public void addBall(Ball ball) {
		balls[numBalls] = ball;
		numBalls++;
	}
	
	public AppGameContainer getGameContainer() {
		return gameContainer;
	}
}
