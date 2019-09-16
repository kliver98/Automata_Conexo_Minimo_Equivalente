package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class PanelResult extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Attributes
	private JTextField[][] matrix;
	private String[][] data;
	
	//Relations
	
	//Methods
	public PanelResult() {
		setup();
	}
	
	public void init() {
		clearPanel();
		matrix = new JTextField[data.length][data[0].length];
		JPanel aux = new JPanel(new GridLayout(matrix.length,matrix[0].length));
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				matrix[i][j] = new JTextField();
				JTextField tmp = matrix[i][j];
				tmp.setHorizontalAlignment(JTextField.CENTER);
				if (i==0 && j>0) {
					tmp.setBackground(Color.WHITE);
					tmp.setForeground(Color.BLACK);
				}else if (i>0 && j==0) {
					tmp.setBackground(Color.BLACK);
					tmp.setForeground(Color.WHITE);
				}
				tmp.setEditable(false);
				tmp.setText(data[i][j]);
				aux.add(tmp);
			}
		}
		JScrollPane tmp = new JScrollPane(aux);
		tmp.setBorder(BorderFactory.createMatteBorder(8,4,8,8,MainWindow.BACKGROUND));
		add(tmp);
//		add(new JLabel(new Random().nextInt(100)+""));
	}
	
	public void clearPanel() {
		this.removeAll();
		this.revalidate();
		this.repaint();
	}
	
	private void setup() {
		setLayout(new GridLayout(1,1));
		setPreferredSize(new Dimension(MainWindow.WINDOW_WIDTH,MainWindow.WINDOW_HEIGHT));
		setBackground(Color.DARK_GRAY);
	}
	
	public void setData(String[][] data) {
		this.data = data;
	}
	
}
