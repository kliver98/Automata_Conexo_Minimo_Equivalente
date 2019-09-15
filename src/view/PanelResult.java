package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class PanelResult extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Attributes
	
	
	//Relations
	private MainWindow main;
	
	//Methods
	public PanelResult(MainWindow main) {
		this.main = main;
		setup();
	}
	
	private void setup() {
		setLayout(new GridLayout(1,1));
		setPreferredSize(new Dimension(MainWindow.WINDOW_WIDTH,MainWindow.WINDOW_HEIGHT));
		setBackground(Color.BLACK);
	}
	
}
