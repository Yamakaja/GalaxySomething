package example.shader;

import org.lwjgl.Sys;

import main.GalaxySomething;
import main.GameState;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.shader.ShaderProgram;

/**
 * Lesson 1 from the shader tutorial series on the wiki. // note: Mentioned tutorial series does not exist (any more) :-(
 * 
 * @author davedes
 */
public class ShaderLesson1 extends GameState {

	private ShaderProgram program;
	private boolean shaderWorks = false;
	
	@Override
	public void init(AppGameContainer gc, GalaxySomething gs) {
		super.init(gc, gs);
		// this test requires shaders
		if (!ShaderProgram.isSupported()) {
			// Sys is part of LWJGL -- it's a handy way to show an alert
			Sys.alert("Error", "Your graphics card doesn't support OpenGL shaders.");
			gc.exit();
			return;
		}
	
		// load our shader program
		try {
			// load our vertex and fragment shaders
			final String VERT = "assets/testdata/shaders/pass.vert";
			final String FRAG = "assets/testdata/shaders/lesson1.frag";
			program = ShaderProgram.loadProgram(VERT, FRAG);
			shaderWorks = true;
		} catch (SlickException e) {
			// there was a problem compiling our source! show the log
			e.printStackTrace();
		}
	}
	
	@Override
	public void render(Graphics g) {
		//start using our program
		if (shaderWorks)
			program.bind();
		
		//render our shapes with the shader enabled
		g.fillRect(220, 200, 50, 50);
		
		//stop using our program
		if (shaderWorks)
			program.unbind();
		
		String txt = shaderWorks ? "Shader works!" : "Shader did not compile, check log";
		g.drawString(txt, 10, 25);
	}

	@Override
	public void update(float tpf, int delta) {
		
	}
}
