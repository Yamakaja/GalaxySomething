package example;

import java.util.Random;

import main.Vector2f;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class Ball {
	
	public static final Random random = new Random();
	
	// in pixel per second
	public final static float MAX_VELOCITY = 400;
	
	// in pixel per second²
	public final static float MAX_ACCELERATION = 800;
	
	private float radius;
	
	private Color color;
	
	
	private Vector2f position;
	
	private Vector2f velocity;
	
	private Vector2f acceleration;
	
	private BallState ballState;
	
	private boolean isGone = false;
	
	public Ball(BallState ballState, Vector2f position, float radius, Color color) {
		this.ballState = ballState;
		this.position = position.copy();
		this.radius = radius;
		this.color = color;
		velocity = new Vector2f(0, 0);
		acceleration = new Vector2f(0, 0);
	}
	
	public void update(float tpf) {
		position.addLocal(velocity.multiply(tpf));
		
		velocity.addLocal(acceleration.multiply(tpf));
		
		if (velocity.length() > MAX_VELOCITY) {
			velocity.normaliseLocal();
			velocity.multiplyLocal(MAX_VELOCITY);
		}
		
		Input input = ballState.getGameContainer().getInput();
		
		Vector2f mousePosition = new Vector2f(input.getMouseX(), input.getMouseY());
		
		Vector2f vectorToMouse = mousePosition.subtract(position);
		
		if (vectorToMouse.length() <= 20 && radius > 5) {
			vanish();
			spawnChildren(mousePosition);
			
			return;
		}
		
		Vector2f wantedVelocity;
		
		if (vectorToMouse.length() < radius * 1.5f) {
			Vector2f positionInASecond = position.add(velocity);
			
			wantedVelocity = mousePosition.subtract(positionInASecond);
		}
		else {
			wantedVelocity = mousePosition.subtract(position);
		}
		
		
		acceleration = wantedVelocity;
		
		//if (acceleration.length() > MAX_ACCELERATION) {
			acceleration.normaliseLocal();
			acceleration.multiplyLocal(MAX_ACCELERATION);
		//}
		
		
	}
	
	public void render(Graphics g) {
		g.setColor(color);
		g.fillOval(position.getX() - radius, position.getY() - radius, 2 * radius, 2 * radius);
	}
	
	public void vanish() {
		isGone = true;
	}
	
	public boolean isGone() {
		return isGone;
	}
	
	private void spawnChildren(Vector2f mousePosition) {
		for (int i = 0; i < 2; i++) {
			final float childDistance = 20;
			Vector2f childPosition = position.add(
					new Vector2f(random.nextFloat() * 2 - 1,
								 random.nextFloat() * 2 - 1)
					.normaliseLocal().multiplyLocal(childDistance));
			
			Vector2f childVelocity = childPosition.subtract(mousePosition);
			
			childVelocity.normaliseLocal().multiplyLocal(MAX_VELOCITY);
			
			Ball child = new Ball(ballState,
								  childPosition,
								  radius / (float) Math.sqrt(2),
								  color);
			child.velocity = childVelocity;
			
			ballState.addBall(child);
		}
	}
	
}





