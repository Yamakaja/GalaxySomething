package main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

public class Cam {
	
	public static final float ZOOM_STEP = 1.5f;
	
	public static final float MOVEMENT_SPEED_UP = 5;
	
	public static final int ZOOM_UNIT = 32;
	
	public float camSpeed = 200;
	
	// Bereich, der gerendert wird
	public float camX = 0;
	public float camY = 0;
	
	public float camWidth;
	public float camHeight;
	
	// Area valid for the cam to move around in
	private float camBoundX = 0;
	private float camBoundY = 0;
	
	private float camBoundWidth;
	private float camBoundHeight;
	
	// Bereich, auf den gerendert wird
	public float offsetX;
	public float offsetY;
	
	public float renderWidth;
	public float renderHeight;
	
	// Bereich, auf den gerendert werden kann
	private float screenX = 0;
	private float screenY = 0;
	
	private float screenWidth = 1920;
	private float screenHeight = 1080;
	
	private float maxOffsetPercentage = 0.5f;
	
	// behaves inverse to zoom such that <sizeInWorld> * ratio = <sizeOnScreen>
	public float ratio = 1;
	
	// basically determines how many terrain fields will be displayed on screen
	public float zoom;
	
	// how many terrain fields max can be displayed on screen
	public float maxZoom;
	
	// determines whether zooming will always reset offset (e.g. when the cam is centered around a character)
	public boolean resetOffsetOnZoom;
	
	public boolean movementEnabled = true;
	
	public boolean zoomToMousePosition = true;
	
	private MovementHandler movementHandler = new MovementHandler();
	
	private Input input;
	
	// so drag and move events caused by camera movement can be processed
	private CamRelatedInputListener listener = null;
	
	public Cam(GameContainer gc) {
		input = gc.getInput();
	}
	
	public void update(float tpf, int delta) {
		if (movementEnabled) {
			float movementX = 0, movementY = 0;
			boolean movement = false;
			float movementSpeed = InputConfiguration.isKeyDown(InputConfiguration.MOVEMENT_SPEED_MODIFIER) ? camSpeed * MOVEMENT_SPEED_UP : camSpeed;
			
			if (InputConfiguration.isKeyDown(InputConfiguration.KEY_MOVE_LEFT)) {
				movementX -= tpf * movementSpeed;
				movement = true;
			}
			if (InputConfiguration.isKeyDown(InputConfiguration.KEY_MOVE_RIGHT)) {
				movementX += tpf * movementSpeed;
				movement = true;
			}
			if (InputConfiguration.isKeyDown(InputConfiguration.KEY_MOVE_UP)) {
				movementY -= tpf * movementSpeed;
				movement = true;
			}
			if (InputConfiguration.isKeyDown(InputConfiguration.KEY_MOVE_DOWN)) {
				movementY += tpf * movementSpeed;
				movement = true;
			}
			
			
			moveCamX(movementX, false);
			moveCamY(movementY, false);
			
			if (movement && listener != null) {
				fireInputForListener();
			}
		}
	}
	
	public void moveCamX(float movementX) {
		moveCamX(movementX, true);
	}
	
	public void moveCamX(float movementX, boolean fireInput) {
		
		// copy elements into utility class
		movementHandler.camStart = camX;
		movementHandler.camSpan = camWidth;
		
		movementHandler.camBoundStart = camBoundX;
		movementHandler.camBoundSpan = camBoundWidth;
		
		movementHandler.offset = offsetX;
		movementHandler.renderSpan = renderWidth;
		
		movementHandler.screenStart = screenX;
		movementHandler.screenSpan = screenWidth;
		
		// perform actual action
		movementHandler.handleMovement(movementX);
		
		// copy results back
		camX = movementHandler.camStart;
		camWidth = movementHandler.camSpan;
		
		camBoundX = movementHandler.camBoundStart;
		camBoundWidth = movementHandler.camBoundSpan;
		
		offsetX = movementHandler.offset;
		renderWidth = movementHandler.renderSpan;
		
		screenX = movementHandler.screenStart;
		screenWidth = movementHandler.screenSpan;
		
		if (fireInput && listener != null && movementX != 0) {
			fireInputForListener();
		}
	}
	
	public void moveCamY(float movementY) {
		moveCamY(movementY, true);
	}
	
	public void moveCamY(float movementY, boolean fireInput) {
		
		// copy elements into utility class
		movementHandler.camStart = camY; 
		movementHandler.camSpan = camHeight;
		
		movementHandler.camBoundStart = camBoundY;
		movementHandler.camBoundSpan = camBoundHeight;
		
		movementHandler.offset = offsetY;
		movementHandler.renderSpan = renderHeight;
		
		movementHandler.screenStart = screenY;
		movementHandler.screenSpan = screenHeight;
		
		// perform actual action
		movementHandler.handleMovement(movementY);
		
		// copy results back
		camY = movementHandler.camStart;
		camHeight = movementHandler.camSpan;
		
		camBoundY = movementHandler.camBoundStart;
		camBoundHeight = movementHandler.camBoundSpan;
		
		offsetY = movementHandler.offset;
		renderHeight = movementHandler.renderSpan;
		
		screenY = movementHandler.screenStart;
		screenHeight = movementHandler.screenSpan;
		
		if (fireInput && listener != null && movementY != 0) {
			fireInputForListener();
		}
	}
	
	private void fireInputForListener() {
		int mouseX = input.getMouseX(), mouseY = input.getMouseY();
		if (isOnRenderArea(mouseX, mouseY)) {
			if (InputConfiguration.isAnyMouseButtonDown(input))
				listener.mouseDraggedInCam(toCamCoordinatesX(mouseX), toCamCoordinatesY(mouseY));
			else
				listener.mouseMovedInCam(toCamCoordinatesX(mouseX), toCamCoordinatesY(mouseY));
		}
	}
	
	public Cam setCamPosition(float x, float y) {
		camX = x;
		camY = y;
		return this;
	}
	
	public Cam setRatio(float newRatio) {
		this.ratio = newRatio;
		return this;
	}
	
	public Cam setCamArea(float x, float y, float width, float height) {
		camX = x;
		camY = y;
		camWidth = width;
		camHeight = height;
		return this;
	}
	
	public Cam setCamBounds(float x, float y, float width, float height) {
		camBoundX = x;
		camBoundY = y;
		camBoundWidth = width;
		camBoundHeight = height;
		return this;
	}
	
	public Cam setCamBounds(float width, float height) {
		camBoundWidth = width;
		camBoundHeight = height;
		return this;
	}
	
	public Cam setScreenArea(float x, float y, float width, float height) {
		screenX = x;
		screenY = y;
		screenWidth = width;
		screenHeight = height;
		return this;
	}
	
	public Cam setMaxRelativeOffset(float percentage) {
		this.maxOffsetPercentage = percentage;
		return this;
	}
	
	public void changeZoom(float change) {
		//Begrenzung erfolgt durch 1 und maxZoom  bzw. camBounds
		if (change < 0) {
			zoom += ZOOM_STEP;
			if (maxZoom > 0 && zoom > maxZoom)
				zoom = maxZoom;
			float temp;
			if (camBoundHeight < camBoundWidth)
				temp = camBoundWidth / ZOOM_UNIT;
			else
				temp = camBoundHeight / ZOOM_UNIT;
			if (zoom > temp)
				zoom = temp;
		}
		else if (change > 0) {
			zoom -= ZOOM_STEP;
			if(zoom < 1)
				zoom = 1;
		}
		
		if (resetOffsetOnZoom)
			offsetX = offsetY = 0;
		
		float mouseX = input.getMouseX(), mouseY = input.getMouseY();
		float mouseInCamX = -1, mouseInCamY = -1;
		if (zoomToMousePosition && isOnRenderArea(mouseX, mouseY)) {
			mouseInCamX = toCamCoordinatesX(mouseX);
			mouseInCamY = toCamCoordinatesY(mouseY);
		}
		
		//zoomFactor < 1: Ranzoomen, > 1: Rauszoomen;
		// Der Bereich, auf den gerendert wird, sinkt (wird mit zoomFactor malgenommen)
		// - der Bereich, der gerendert wird, bleibt idR gleich groß
		// realRenderWH MüSSEN proportional zur Framegröße sein. Ebenso aber proportional zu renderWH. Dies ist aber normalerweise nicht der Fall.
		// daraus folgt, dass der größere Sichtweitenwert genommen wird.
		// Aus dem Verhältnis der größeren rrWH zur Framesize lässt sich der komplementäre rrWH errechnen. Ist dieser wiederum zu groß für die Map, kommen offsets und Ergänzungen von rrWH ins Spiel.
		
		//Ist somit der Bereich, der gerendert wird
		camWidth  = ZOOM_UNIT * zoom;
		camHeight = ZOOM_UNIT * zoom;
		
		if(camBoundWidth < camWidth)
			camWidth = camBoundWidth;
		if(camBoundHeight < camHeight)
			camHeight = camBoundHeight;
		
		renderWidth  = screenWidth;
		renderHeight = screenHeight;
		
		//camWidth / camHeight muss proportional zu renderWidth / renderHeight sein.
		float camRatio = camWidth / camHeight, renderRatio = renderWidth / renderHeight;
		
		if (camRatio < renderRatio) {
			//renderWidth ist zu groß
			camWidth = camHeight * renderWidth / renderHeight;
			if (camWidth > camBoundWidth) {
				camWidth = camBoundWidth;
				renderWidth = camWidth * renderHeight / camHeight;
				offsetX = (screenWidth - renderWidth) / 2;
			}
		}
		else {
			//Den RARIGSTEN (da floats) Fall, dass renderRatio == realRenderRatio ist, lasse ich hier mal aus. Die extra if-Abfrage lohnt den sowieso nicht zu erwartenden Effekt bei dessen Eintritt nicht.
			//renderHeight ist zu groß; es muss rH / rW == rrH / rrW --> rh = rrH * rW / rrW
			camHeight = camWidth * renderHeight / renderWidth;
			if(camHeight > camBoundHeight) {
				//renderHeight = map.getHeight = renderWidth * realRenderHeight / realRenderWidth --> realRenderHeight = renderHeight * realRenderWidth / renderWidth
				camHeight = camBoundHeight;
				renderHeight = camHeight * renderWidth / camWidth;
				offsetY = (screenHeight - renderHeight) / 2;
			}
		}
		
		ratio = renderWidth / camWidth;
		if ( (renderWidth == screenWidth || renderHeight == screenHeight)
		    && ratio >= 0.95 && ratio <= 1.05f 
		    && renderWidth <= camBoundWidth && renderHeight <= camBoundHeight) {
			//Logger.print("Difference was minor, so made the render dimension to " + realRenderWidth + "x"+realRenderHeight);
			camWidth = renderWidth;
			camHeight = renderHeight;
			ratio = 1;
		}
		//Logger.print("Ratio is: " + ratio);
		
		
		// should fire "fake cam movements" anyway to adapt cam to bounds and stuff.
		if(mouseInCamX >= 0 && isOnRenderArea(mouseX, mouseY)) {
			mouseInCamX = mouseInCamX - toCamCoordinatesX(mouseX);
			mouseInCamY = mouseInCamY - toCamCoordinatesY(mouseY);
		}
		else
			mouseInCamX = mouseInCamY = 0;
		
		moveCamX(mouseInCamX*ratio, false);
		moveCamY(mouseInCamY*ratio, false);
		
		if (listener != null) {
			fireInputForListener();
		}
	}
	
	public boolean isOnRenderArea(float screenX, float screenY) {
		screenX -= this.screenX + offsetX;
		screenY -= this.screenY + offsetY;
		return screenX >= 0 && screenY >= 0 && screenX <= renderWidth && screenY <= renderHeight;
	}
	
	public float toCamCoordinatesX(float screenX) {
		return (screenX - this.screenX - offsetX) / ratio + camX;
	}
	
	public float toCamCoordinatesY(float screenY) {
		return (screenY - this.screenY - offsetY) / ratio + camY;
	}
	
	public void setCamRelatedInputListener(CamRelatedInputListener listener) {
		this.listener = listener;
	}
	
	public void removeCamRelatedInputListener() {
		listener = null;
	}
	
	public static interface CamRelatedInputListener {
		
		public void mouseDraggedInCam(float mouseX, float mouseY);
		
		public void mouseMovedInCam(float mouseX, float mouseY);
	}
	
	private class MovementHandler {
		float camStart; 		// camX/Y
		float camSpan;  		// camWidth/Height
		
		float camBoundStart;	// camBoundX/Y
		float camBoundSpan;		// camBoundWidth/Height
		
		float offset;			// offsetX/Y
		float renderSpan;		// renderWidth/Height
		
		float screenStart;   	// screenX/Y
		float screenSpan; 		// screenWidth/Height
		
		public void handleMovement(float movement) {
			if (movement < 0) {
				if (camStart <= camBoundStart && offset <= screenSpan * maxOffsetPercentage) {
					// camStart <= camBoundstart means camStart = camBoundStart to ensure that cam cannot move further left
					// thus offset is moved
					offset -= movement; // - und - = +
					if (offset > screenSpan * maxOffsetPercentage)
						offset = screenSpan * maxOffsetPercentage;
					camStart = camBoundStart;
				}
				else {
					// move cam left
					camStart += movement / ratio;
					if (camStart < camBoundStart) {
						camStart = camBoundStart;
					}
				}
			}
			else {
				if (offset > 0) {
					// reduce offset
					offset -= movement;
					if(offset < 0) {
						offset = 0;
					}
					camStart = camBoundStart;
				}
				else
					camStart += movement / ratio;
			}

			// will now ensure that camSpan * ratio = renderSpan
			
			if (offset > 0 && (offset + renderSpan) >= screenSpan) {
				renderSpan = screenSpan - offset;
				camSpan = renderSpan / ratio;
			}
			else if (camStart + camSpan > camBoundSpan) {
				camSpan = camBoundSpan - camStart;
				renderSpan = camSpan * ratio;
				if (renderSpan < screenSpan - offset) {
					renderSpan = screenSpan - offset;
					camSpan = renderSpan / ratio;
					camStart = camBoundSpan - camSpan;
				}
			}
			else if (renderSpan != screenSpan) {
				camSpan = screenSpan / ratio;
				if (camStart + camSpan > camBoundSpan) {
					camSpan = camBoundSpan - camStart;
				}
				renderSpan = camSpan * ratio;
			}
		}
		
	}
}
