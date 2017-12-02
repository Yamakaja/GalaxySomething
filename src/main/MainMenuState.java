package main;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import gui.Button;
import gui.GuiComponent;
import gui.GuiScreen;
import planet.editor.PlanetEditorState;

public class MainMenuState extends GameState {
	
	private GuiScreen GUI;
	
	@Override
	public void init(AppGameContainer gc, GalaxySomething aow) {
		super.init(gc, aow);
		
		GUI = new GuiScreen(gc);
		
		GuiComponent centralPanel = new GuiComponent(gc, 0.5f, 0.4f);
		
		GUI.getContainer().attachChild(centralPanel);
		
		centralPanel.attachChild(GuiStyle.setupBigButton(new Button(gc, 0.8f, 0.15f, 0, 0, 1, 4) {{
				setLabel("NEW GAME");
			}
			public void onClick() {
				// TODO new game
				Logger.print("New game!");
			}
		}));
		centralPanel.attachChild(GuiStyle.setupBigButton(new Button(gc, 0.8f, 0.15f, 0, 1, 1, 4) {{
				setLabel("TEST EDITOR");
			}
			public void onClick() {
				// TODO settings
				game.switchToState(new PlanetEditorState());
			}
		}));
		centralPanel.attachChild(GuiStyle.setupBigButton(new Button(gc, 0.8f, 0.15f, 0, 2, 1, 4) {{
			setLabel("PLAY WITH BALLS");
		}
		public void onClick() {
			// TODO settings
			game.switchToState(new example.BallState());
		}
	}));
		centralPanel.attachChild(GuiStyle.setupBigButton(new Button(gc, 0.8f, 0.15f, 0, 3, 1, 4) {{
				setLabel("QUIT");
			}
			public void onClick() {
				gc.exit();
			}
		}));
		
		aow.switchGuiScreen(GUI);
	}
	
	@Override
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_ESCAPE)
			gameContainer.exit();
	}

	@Override
	public void update(float tpf, int delta) {
		
	}

	@Override
	public void render(Graphics g) {
		
	}
}
