package main;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.shader.ShaderProgram;

public class Shaders {
	
	static {
		ShaderProgram.setStrictMode(false);
	}
	
	
	private static ShaderProgram planetTerrainShader;
	
	
	public static ShaderProgram getPlanetTerrainShader() {
		if (planetTerrainShader == null) {
			try {
				planetTerrainShader = ShaderProgram.loadProgram(
						FilePaths.SHADER_DIR + "terrainshader.vert", FilePaths.SHADER_DIR + "terrainshader.frag");
				planetTerrainShader.bind();
				planetTerrainShader.setUniform1i("tex0", 0); // 0 means the texture that "is currently drawn" (in my current understanding)
				planetTerrainShader.unbind();
			}
			catch (SlickException e) {
				ExceptionHandler.handleErrorLoadingShader(e);
			}
		}
		
		return planetTerrainShader;
	}
}
