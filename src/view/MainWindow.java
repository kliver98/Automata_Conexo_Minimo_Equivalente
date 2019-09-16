package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Automata;
/**
 * 
 * @author Kliver Daniel Giron
 *
 */
public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Attributes
	/**
	 * Attribute to know the name of application
	 */
	public static final String APP_NAME = "Autómata Conexo y Mínimo Equivalente de Autómata finito dado";
	/**
	 * Attribute to know the width of the screen running the application
	 */
	public static final Integer WIDTH = (int)(Toolkit.getDefaultToolkit().getScreenSize().width);
	/**
	 * Attribute to know the height of the screen running the application
	 */
	public static final Integer HEIGHT = (int)(Toolkit.getDefaultToolkit().getScreenSize().height);
	/**
	 * Attribute to know the percentage for screen borders
	 */
	private static final Double PERCENTAGE = (double)20/100;
	/**
	 * Attribute to represents the Width of the application
	 */
	public static final Integer WINDOW_WIDTH = (int)(WIDTH-(WIDTH*PERCENTAGE));
	/**
	 * Attribute to represents the height of the application
	 */
	public static final Integer WINDOW_HEIGHT = (int)(HEIGHT-(HEIGHT*PERCENTAGE));
	/**
	 * Attribute to set up the background color of the application
	 */
	public static final Color BACKGROUND = new Color(220,220,220);
	
	//Relations
	/**
	 * Relation with the main class of the model
	 */
	private Automata model;
	/**
	 * Relation with panel that allow user to add rows and columns for a Mealy or Moore machine 
	 */
	private PanelOptions pOptions;
	/**
	 * Relation with panel that allow user to see the information calculated for the automaton received by program
	 */
	private PanelResult pResult;
	/**
	 * Relation with panel that allow user to see and add information of table for input of Mealy or Moore machine
	 */
	private PanelTable pTable;
	/**
	 * Relation with panel that contain all visual information inside the app 
	 */
	private JPanel container;
	
	//Methods
	/**
	 * Builder
	 */
	public MainWindow() {
//		model = new Automata(true);	//Test
//		model.chargeTest();			//Test
		setup();
		init();
	}
	
	/**
	 * Method to add the panels that has the application
	 * @param data with the cell's information of the panel result
	 */
	private void addPanels(String[][] data) {
		pOptions = new PanelOptions(this);
		pResult = new PanelResult();
		pTable = new PanelTable(this,data);
		container = new JPanel(new GridLayout(1,2));
		JPanel aux = new JPanel(new GridLayout(2,1));
		aux.add(pTable);
		aux.add(pOptions);
		container.add(aux);
		container.add(pResult);
		add(container);
	}
	
	/**
	 * Method to check the constrains of the window size screen
	 */
	public void checkScreenDimension() {
		if (HEIGHT<720 || WIDTH<720) {
			JOptionPane.showMessageDialog(null, "No cumples con la restricción mínima de dimensiones de pantalla requerida: 720x720 pixeles",
					"Error de dimensiones de pantalla", JOptionPane.ERROR_MESSAGE);
			closeApp();
		}
	}
	
	/**
	 * Method to close the application
	 */
	public void closeApp() {
		System.exit(0);
	}
	
	/**
	 * Method to add data of a row or rows into the automata and then show it at pTable panel
	 * @param add with the data to add in row or rows
	 * @return true if it was possible or false otherwise
	 */
	public Boolean addRowData(String[] add) {
		boolean result = model.addRowData(add);
		if (result) {
			clearTable(true);
			pTable.setData(model.getInitialTable());
			pTable.init();
			this.revalidate();
			this.repaint();
		}
		return result;
	}
	
	/**
	 * Methos that ask for initial data that help to work wih th machine
	 */
	public void init() {
		setVisible(false);
		//If you want run Test, comment this part
		int typeAutomata = askTypeAutomata(); // 0 to Mealy - 1 to Moore
		if (typeAutomata==JOptionPane.CLOSED_OPTION || (typeAutomata==2 && model==null))
			closeApp();
		else if (typeAutomata==2) {
			setVisible(true);
			return;
		}
		if (pTable!=null)
			clearTable(true);
		model = new Automata(typeAutomata==0);
		boolean aceptado = false;
		while (!aceptado) {
			String[] initialData = null;
			try {			
				initialData = askInitialEntries();
			} catch (NullPointerException ex) {
				closeApp();
			}
			aceptado = model.setInitialData(initialData);
			if (!aceptado)
				JOptionPane.showMessageDialog(null, "Se detecto un error con las entradas","Vuelva a intentarlo", JOptionPane.ERROR_MESSAGE);
		}
		//'Till here
		addPanels(model.getInitialTable());
		setVisible(true);
	}
	
	/**
	 * Method to calculate the result of the conex and minimum equivalent machine
	 */
	public void calculate() {
		pResult.setData(model.calculate());
		pResult.init();
	}
	
	/**
	 * Mehtod to ask initial entries, Q,S,R and q1
	 * @return data of Q,S,R,q1 each one in one position of the array
	 * @throws NullPointerException if user don't put a valid input
	 */
	public String[] askInitialEntries() throws NullPointerException {
		String Q = JOptionPane.showInputDialog(this, "Ingrese los estados Q separados por espacios: ", "Ejemplo: A B C D", JOptionPane.QUESTION_MESSAGE).toString().trim();
		String S = JOptionPane.showInputDialog(this, "Ingrese el alfabeto finito de entrada S: ", "Ejemplo: a b c", JOptionPane.QUESTION_MESSAGE).toString().trim();
		String R = JOptionPane.showInputDialog(this, "Ingrese el alfabeto finito de salida R: ", "Ejemplo: 0 1 2", JOptionPane.QUESTION_MESSAGE).toString().trim();
		String q1 = JOptionPane.showInputDialog(this, "Ingrese el estado inicial q1 (tiene que estar definido en Q): ", "Ejemplo: A", JOptionPane.QUESTION_MESSAGE).toString().trim();
		return new String[] {Q,S,R,q1};
	}
	
	/**
	 * Method that ask the type of automata to work on
	 * @return value choosen by user
	 */
	public int askTypeAutomata() {
		String[] opt = {"Mealy","Moore","Cancelar"};
		int x = JOptionPane.showOptionDialog(null, "Seleccione el tipo de autómata a insertar","Elija", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opt, opt[2]);
		return x;
	}

	/**
	 * Method to know type of automata working on
	 * @return automataType - True for Mealy and False for Moore
	 */
	public Boolean getAutomataType() {
		return model.getAutomataType();
	}
	
	/**
	 * Method to erase the data put in the table, up and left
	 * @param option
	 */
	public void clearTable(boolean option) {
		pTable.restartTable();
		if (option)
			pTable.clearPanel();
	}

	/**
	 * Method to configure the application dimension and other configurations
	 */
	private void setup() {
		checkScreenDimension();
		setLayout(new BorderLayout(1,1));
		setTitle(APP_NAME);
		setPreferredSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT));
		setLocation((int)(WIDTH*(PERCENTAGE/2.0)),(int)(HEIGHT*(PERCENTAGE/2.0)));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		pack();
	}

	/**
	 * main
	 * @param args
	 */
	public static void main(String[] args) {
		new MainWindow();
	}

}
