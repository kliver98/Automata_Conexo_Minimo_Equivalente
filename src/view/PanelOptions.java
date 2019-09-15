package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class PanelOptions extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Attributes
	private JPanel container;
	private JTextPane jtPane;
	public static final String CHANGE_TYPE = "Cambiar autómata";
	public static final String CLOSE_APP = "Cerrar aplicación";
	public static final String DELETE_TABLE = "Borrar datos";
	public static final String ADD_DATA = "Agregar datos";
	public static final String SOLVE = "Resolver/Calcular";
	
	//Relations
	private MainWindow main;
	
	//Methods
	public PanelOptions(MainWindow main) {
		this.main = main;
		setup();
	}
	
	private void init() {
		JPanel pData = new JPanel(new GridLayout(1,2));
		JPanel pBtns = new JPanel(new GridLayout(1,4));
		Font font = new Font("Arial",Font.BOLD,(int)(MainWindow.WINDOW_WIDTH/88));
		
		jtPane = new JTextPane();
		StyledDocument doc = jtPane.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		jtPane.setFont(font);
		
		JLabel lblData = new JLabel("Agregar datos (por filas): ");
		lblData.setHorizontalAlignment(SwingConstants.CENTER);
		lblData.setFont(new Font("Arial",Font.BOLD,(int)(MainWindow.WINDOW_WIDTH/30)));
		
		JButton btnAddData = new JButton(ADD_DATA);
		btnAddData.addActionListener(this);
		btnAddData.setFont(font);
		btnAddData.setFocusPainted(false);
		btnAddData.setContentAreaFilled(false);
		btnAddData.setForeground(Color.MAGENTA);
		
		JButton btnChangeType = new JButton(CHANGE_TYPE);
		btnChangeType.addActionListener(this);
		btnChangeType.setFont(font);
		btnChangeType.setFocusPainted(false);
		JButton btnCloseApp = new JButton(CLOSE_APP);
		btnCloseApp.addActionListener(this);
		btnCloseApp.setFont(font);
		btnCloseApp.setFocusPainted(false);
		JButton btnDeleteTable = new JButton(DELETE_TABLE);
		btnDeleteTable.addActionListener(this);
		btnDeleteTable.setFont(font);
		btnDeleteTable.setFocusPainted(false);
		JButton btnSolve = new JButton(SOLVE);
		btnSolve.addActionListener(this);
		btnSolve.setFont(font);
		btnSolve.setFocusPainted(false);
		
		pData.add(new JScrollPane(jtPane));
		pData.add(btnAddData);
		pBtns.add(btnCloseApp);
		pBtns.add(btnChangeType);
		pBtns.add(btnDeleteTable);
		pBtns.add(btnSolve);
		pBtns.setBorder(BorderFactory.createMatteBorder((int)((MainWindow.WINDOW_HEIGHT/3)*(0.25)),0,0,0,main.getBackground()));
		
		container.add(lblData);
		container.add(pData);
		container.add(pBtns);
	}
	
	private void setup() {
		container = new JPanel(new GridLayout(3,1));
		setLayout(new GridLayout(1,1));
		setBackground(MainWindow.BACKGROUND);
		this.setBorder(BorderFactory.createMatteBorder(8,8,8,4,main.getBackground()));
		init();
		add(container);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String str = e.getActionCommand();
		switch (str) {
			case CLOSE_APP:
				main.closeApp();
				break;
			case DELETE_TABLE:
				jtPane.setText("");
				break;
			case CHANGE_TYPE:
				main.init();
				break;
			case SOLVE:
				break;
		}
	}
	
}
