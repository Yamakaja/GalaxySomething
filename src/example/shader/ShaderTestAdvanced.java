package example.shader;

import org.newdawn.slick.Color;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.shader.ShaderProgram;
import org.newdawn.slick.util.Log;

import main.GalaxySomething;
import main.GameState;
import main.ImageManager;

/**
 * A simple shader test that inverts the color of an image.
 * @author davedes
 */
public class ShaderTestAdvanced extends GameState {

	private Image logo;
	private ShaderProgram blurHoriz, blurVert; // those are very crude blur filters. Don't take them as a serious blurring technique.
	private String log;
	private boolean shaderWorks, useVert=true, useHoriz=true;
	private boolean supported = false;
	
	private float rot, radius=1f;
	
	private Image postImage;
	private Graphics postGraphics;
	private float alpha = 0.5f; // note that this alpha actually has no effect
	
	@Override
	public void init(AppGameContainer container, GalaxySomething gs) {
		super.init(container, gs);
		logo = ImageManager.loadImage("assets/testdata/logo.png");
		container.setClearEachFrame(false);
		
		supported = ShaderProgram.isSupported();
		
		if (supported) {
			try {
				//first we will try to create our offscreen image
				//this may fail in very very rare cases if the user has no FBO/PBuffer support
				postImage = Image.createOffscreenImage(container.getWidth(), container.getHeight());
				postGraphics = postImage.getGraphics();
				
				String h = "assets/testdata/shaders/hblur.frag";
				String v = "assets/testdata/shaders/vblur.frag";
				String vert = "assets/testdata/shaders/blur.vert";
				
				blurHoriz = ShaderProgram.loadProgram(vert, h);
				blurVert = ShaderProgram.loadProgram(vert, v);
				shaderWorks = true;
				
				//good idea to print/display the log anyways incase there are warnings..
				log = blurHoriz.getLog()+"\n"+blurVert.getLog();
				
				//note that strict mode is ENABLED so these uniforms must be active in shader
				
				//set up our uniforms for horizontal blur...
				blurHoriz.bind();
				blurHoriz.setUniform1i("tex0", 0); //texture 0
				blurHoriz.setUniform1f("resolution", container.getWidth()); //width of img
				blurHoriz.setUniform1f("radius", radius);
				blurHoriz.setUniform1f("alpha", alpha);
				
				//set up our uniforms for vertical blur... 
				blurVert.bind();
				blurVert.setUniform1i("tex0", 0); //texture 0
				blurVert.setUniform1f("resolution", container.getHeight()); //height of img
				blurVert.setUniform1f("radius", radius);
				alpha = 0.75f;
				blurVert.setUniform1f("alpha", alpha);
				
				ShaderProgram.unbindAll();
			} catch (SlickException e) {
				log = e.getMessage();
				Log.error(log);
				shaderWorks = false;
			}		
		}
	}
	
	public void renderScene(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(400, 350, 100, 100);
		logo.setRotation(0f);
		g.drawImage(logo, 100, 300);
		logo.setRotation(rot);
		g.drawImage(logo, 400, 200);
	}
	
	@Override
	public void render(Graphics screenGraphics) {
		//clear the screen
		screenGraphics.clear();
		
		Graphics target = screenGraphics;
		if (shaderWorks && (useHoriz||useVert)) {
			//make the target / post-proc graphics our current
			target = postGraphics;
			Graphics.setCurrent(target);
			target.clear();
		}
		
		//render the scene as usual with our target graphics
		renderScene(target);
		
		//flush the target graphics before proceeding... if the target is screen then flush does nothing
		target.flush();
		
		
		
		if (shaderWorks && (useHoriz||useVert)) {
			screenGraphics.drawImage(postImage, 0, 0);
			
			//if we're doing post-processing...
			if (shaderWorks && useHoriz) {
				blurHoriz.bind();
				blurHoriz.setUniform1f("radius", radius);
				blurHoriz.setUniform1f("alpha", alpha);
				screenGraphics.drawImage(postImage, 0, 0);
			}
			if (shaderWorks && useVert) {
				blurVert.bind();
				blurVert.setUniform1f("radius", radius);
				blurVert.setUniform1f("alpha", alpha);
				screenGraphics.drawImage(postImage, 0, 0);
			}
			ShaderProgram.unbindAll();
		}
		
		
		screenGraphics.setColor(Color.white);
		
		//now we can render on top of post processing (on screen)
		if (shaderWorks)
			screenGraphics.drawString("H to toggle horiz" + (useHoriz?" (enabled)":"") +
					"\nV to toggle vert" + (useVert?" (enabled)":"")+
					"\nUP/DOWN to change radius: "+radius+
					"\nQ/A to change alpha blending: "+alpha, 10, 25);
		else if (!supported)
			screenGraphics.drawString("Your drivers do not support OpenGL Shaders, sorry!", 10, 25);
		else
			screenGraphics.drawString("Oops, shader didn't load!", 10, 25);
		if (log!=null && log.trim().length()!=0)
			screenGraphics.drawString("Shader Log:\n"+log, 10, 75);
	}

	@Override
	public void update(float tpf, int delta) {
		if (gameContainer.getInput().isKeyPressed(Input.KEY_V)) 
			useVert = !useVert;
		if (gameContainer.getInput().isKeyPressed(Input.KEY_H))
			useHoriz = !useHoriz;
		if (gameContainer.getInput().isKeyDown(Input.KEY_DOWN)) {
			radius = Math.max(0.5f, radius-0.0003f*delta);
		} else if (gameContainer.getInput().isKeyDown(Input.KEY_UP)) {
			radius = Math.min(5f, radius+0.0003f*delta);
		}
		if (gameContainer.getInput().isKeyDown(Input.KEY_A)) {
			alpha = Math.max(0.0f, alpha-0.0003f*delta);
		} else if (gameContainer.getInput().isKeyDown(Input.KEY_Q)) {
			alpha = Math.min(1f, alpha+0.0003f*delta);
		}
		rot += 0.03f*delta;
	}
}
