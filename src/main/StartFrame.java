package main;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import org.newdawn.slick.AppGameContainer;


@SuppressWarnings("serial")
public class StartFrame extends JFrame implements ActionListener, ItemListener {
	
    public static void main(String[] args) {
    	new StartFrame("Launch configuration");
    }
	
	private static Dimension defaultResolution = GalaxySomething.IS_IN_TUT ? new Dimension(800, 600) : new Dimension(1920, 1080);
	private static boolean fullscreen = false;
	private static float volume = 0.5f, music = 0.5f;
	private static boolean sound = true;
	
	//GUI stuff
	private JButton startButton;
	private JLabel resolutionLabel, displayModeLabel, volumeLabel, soundLabel, musicLabel;
	private JSlider volumeSlider, musicSlider;
	private JCheckBox soundCheckbox;
	@SuppressWarnings("rawtypes")
	private JComboBox resolutionCombobox, displayModeCombobox;
	
	private final int width = 556, height = 300;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public StartFrame(String title) {
		super(title);
		GraphicsConfiguration gc = getGraphicsConfiguration();
		Rectangle screenRect = gc.getBounds();
		this.setSize(width, height);
		setResizable(false);
		setLocation((screenRect.width - width) / 2, (screenRect.height - height) / 2);
		
		Container c = getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		
		JPanel dropDownPanel = new JPanel();
		dropDownPanel.setLayout(new BoxLayout(dropDownPanel, BoxLayout.X_AXIS));
		
		JPanel outerResolutionPanel = new JPanel();
		outerResolutionPanel.setLayout(new GridBagLayout());
		JPanel innerResolutionPanel = new JPanel();
		innerResolutionPanel.setLayout(new BoxLayout(innerResolutionPanel, BoxLayout.Y_AXIS));
		resolutionLabel = new JLabel("Resolution:");
		String defaultResolutionString = (int) defaultResolution.getWidth() + " x " + (int) defaultResolution.getHeight() ;
		List<String> resolutions = new ArrayList<String>();
		GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		DisplayMode[] displayModes = graphicsDevice.getDisplayModes();
		for (int j = 0; j < displayModes.length; j++) {
			DisplayMode m = displayModes[j];
			String resolution = m.getWidth() +" x "+ m.getHeight();
			if (!resolutions.contains(resolution)) {
				resolutions.add(resolution);
				if (resolution.equals(defaultResolutionString))
					defaultResolutionString = resolution;
			}
		}
		resolutionCombobox = new JComboBox(resolutions.toArray());
		resolutionCombobox.setSelectedItem(defaultResolutionString);
		innerResolutionPanel.add(resolutionLabel);
		innerResolutionPanel.add(resolutionCombobox);
		outerResolutionPanel.add(innerResolutionPanel, gridBagConstraints);
		
		JPanel outerDisplayModePanel = new JPanel();
		outerDisplayModePanel.setLayout(new GridBagLayout());
		JPanel innerDisplayModePanel = new JPanel();
		innerDisplayModePanel.setLayout(new BoxLayout(innerDisplayModePanel, BoxLayout.Y_AXIS));
		displayModeLabel = new JLabel("Display mode:");
		displayModeCombobox = new JComboBox(new String[]{"Fullscreen", "Windowed"});
		displayModeCombobox.setSelectedItem(fullscreen ? "Fullscreen" : "Windowed");
		displayModeCombobox.addItemListener(this);
		innerDisplayModePanel.add(displayModeLabel);
		innerDisplayModePanel.add(displayModeCombobox);
		outerDisplayModePanel.add(innerDisplayModePanel, gridBagConstraints);
		
		dropDownPanel.add(outerResolutionPanel);
		dropDownPanel.add(outerDisplayModePanel);
		
		
		JPanel soundPanel = new JPanel();
		soundPanel.setLayout(new BoxLayout(soundPanel, BoxLayout.X_AXIS));
		
		JPanel outerVolumePanel = new JPanel();
		outerVolumePanel.setLayout(new GridBagLayout());
		JPanel innerVolumePanel = new JPanel();
		innerVolumePanel.setLayout(new BoxLayout(innerVolumePanel, BoxLayout.Y_AXIS));
		volumeLabel = new JLabel("Volume:");
		volumeSlider = new JSlider();
		volumeSlider.setValue( (int) (volume * 100 + 0.5f));
		innerVolumePanel.add(volumeLabel);
		innerVolumePanel.add(volumeSlider);
		outerVolumePanel.add(innerVolumePanel, gridBagConstraints);
		
		JPanel outerSoundPanel = new JPanel();
		outerSoundPanel.setLayout(new GridBagLayout());
		JPanel innerSoundPanel = new JPanel();
		innerSoundPanel.setLayout(new BoxLayout(innerSoundPanel, BoxLayout.X_AXIS));
		soundLabel = new JLabel("Sound:");
		soundCheckbox = new JCheckBox();
		soundCheckbox.setSelected(sound);
		innerSoundPanel.add(soundLabel);
		innerSoundPanel.add(soundCheckbox);
		outerSoundPanel.add(innerSoundPanel, gridBagConstraints);
		
		JPanel outerMusicPanel = new JPanel();
		outerMusicPanel.setLayout(new GridBagLayout());
		JPanel innerMusicPanel = new JPanel();
		innerMusicPanel.setLayout(new BoxLayout(innerMusicPanel, BoxLayout.Y_AXIS));
		musicLabel = new JLabel("Music:");
		musicSlider = new JSlider();
		musicSlider.setValue( (int) (music * 100 + 0.5f));
		innerMusicPanel.add(musicLabel);
		innerMusicPanel.add(musicSlider);
		outerMusicPanel.add(innerMusicPanel, gridBagConstraints);
		
		soundPanel.add(outerVolumePanel);
		soundPanel.add(outerSoundPanel);
		soundPanel.add(outerMusicPanel);
		
		
		JPanel start = new JPanel();
		start.setLayout(new GridBagLayout());
		startButton = new JButton("Done");
		startButton.addActionListener(this);
		start.add(startButton, gridBagConstraints);
		
		c.add(dropDownPanel);
		c.add(soundPanel);
		c.add(start);
		
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		pack();
        
        setResizable(true);
        this.setSize(width, height);
		
		setVisible(true);
	}
	
	//-------------------------------Listener-Methoden-------------------------------------
	
	@Override
	public void itemStateChanged(ItemEvent ie) {
		if (ie.getStateChange() == ItemEvent.DESELECTED)
			return;
		if (ie.getSource() == displayModeCombobox) {
			fullscreen = ( (String) ie.getItem()).equals("Fullscreen");
			return;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		JButton b = (JButton) ae.getSource();
		if (b.equals(startButton)) {
			volume = volumeSlider.getValue() / 100f;
			music = musicSlider.getValue() / 100f;
			sound = soundCheckbox.isSelected();
			
			int width, height;
			StringTokenizer tokenizer = new StringTokenizer((String) resolutionCombobox.getSelectedItem(), " x", false);
			width = Integer.parseInt(tokenizer.nextToken());
			height = Integer.parseInt(tokenizer.nextToken());
			defaultResolution = new Dimension(width, height);
			
			super.dispose();
			
			try {
				GalaxySomething game = new GalaxySomething();
				AppGameContainer app = new AppGameContainer(game);
				app.setDisplayMode(width, height, fullscreen);
				app.setTargetFrameRate(240);
				//app.setResizable(true);
				app.start();
			} catch(Throwable t) {
				
			}
		}
	}
}
