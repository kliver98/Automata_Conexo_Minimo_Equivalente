package view;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class PanelTable extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Attributes
	private Integer rows;
	private Integer columns;
	private JTextField[][] matrix;
	private JScrollPane container;
	
	//Relations
	private MainWindow main;

	//Methods
	public PanelTable(MainWindow main) {
		this.main = main;
		setup();
		init();
	}
	
	private void init() {
		rows = 1;
		columns = main.getAutomataType() ? 1:2;
		matrix = new JTextField[rows][columns];
		initializeMatrix();
		showTable();
	}
	
	private void showTable() {
		JPanel aux = new JPanel(new GridLayout(rows,columns));
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				aux.add(matrix[i][j]);
			}
		}
		container = new JScrollPane(aux);
		add(container);
	}
	
	public void restartTable() {
		container.removeAll();
		this.removeAll();
		init();
		container.revalidate();
		container.repaint();
		this.revalidate();
		this.repaint();
	}
	
	private void initializeMatrix() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				JTextField tmp = matrix[i][j];
				if (tmp==null)
					tmp = new JTextField();
				tmp.setHorizontalAlignment(JTextField.CENTER);
				matrix[i][j] = tmp;
				if (i==0 && j>0) {
					matrix[i][j].setEditable(false);
					tmp.setBackground(Color.WHITE);
					tmp.setForeground(Color.BLACK);
				}else if (i>0 && j==0) {
					matrix[i][j].setEditable(false);
					tmp.setBackground(Color.BLACK);
					tmp.setForeground(Color.WHITE);
				} else {
					tmp.setBackground(new Color(218, 222, 230));
				}
			}
		}
		matrix[0][0].setEditable(false);
	}
	
	private void setup() {
		setLayout(new GridLayout(1,1));
		setBackground(MainWindow.BACKGROUND);
		this.setBorder(BorderFactory.createMatteBorder(8,4,8,8,main.getBackground()));
	}
	
}
