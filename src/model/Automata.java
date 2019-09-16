package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
public class Automata {
	
	/**
	 * Attribute to store data of the automata, basically the matrix
	 */
	private HashMap<String,String[]> data;
	/**
	 * Attribute to store the outputs that a row has to have in a Mealy machine
	 */
	private HashMap<String,String> statesMealy;
	/**
	 * Attribute to store the entry alphabet of the machine working on
	 */
	private HashSet<Character> entryAlphabet;
	/**
	 * Attribute to store the output alphabet of the machine working on
	 */
	private HashSet<String> outputAlphabet;
	/**
	 * Attribute to know the number of columns in the matrix withou counting the column for states
	 */
	private Integer numberOfColumns;
	/**
	 * Attribute to know type of table, True for Mealy and False for Moore
	 */
	private Boolean automataType;
	/**
	 * Attribute to know the initial state
	 */
	private String q1;
	
	/**
	 * Builder
	 * @param automataType - to know type of table working on, True for Mealy and False for Moore
	 */
	public Automata(Boolean automataType) {
		this.automataType = automataType;
		data = new HashMap<String,String[]>();
		statesMealy = new HashMap<String,String>();
		entryAlphabet = new HashSet<Character>();
		outputAlphabet = new HashSet<String>();
//		chargeTest(); it doesn't work just uncommenting this line
	}
	
	/**
	 * Method to get a string matrix that represents the values of each cell in the table to show
	 * @return the String matrix with their cell values
	 */
	public String[][] getInitialTable() {
		String[][] rst = new String[data.keySet().size()+1][numberOfColumns+1];
		int i = 0;
		for (Character c : entryAlphabet) {
			rst[0][i+1] = c+"";
			i++;
		}
		i = 0;
		for (String out : data.keySet()) {
			rst[i+1][0] = out;
			i++;
		}
		i = 1;
		for (String key : data.keySet()) {
			if (data.get(key)==null)
				data.put(key, new String[numberOfColumns]);
			for (int j = 0; j < data.get(key).length; j++) {
				rst[i][j+1] = data.get(key)[j];
			}
			i++;
		}
		return rst;
	}
	
	/**
	 * Method to test the program during implementation
	 */
	public void chargeTest() {
		setInitialData(new String[] {"A B C D E F G H","a b","0 1","A"}); //Works with states put, if there is more than table doesn't mine but not otherwise
		//For Mealy
		automataType = true;
		numberOfColumns = automataType ? 2:3;
		addRowData(new String[] {
				"A D0 C0",
				"B C0 A0",
				"C A0 B1",
				"D D0 E1",
				"E D0 D0"
		});
		//For Moore
//		automataType = false;
//		numberOfColumns = automataType ? 2:3;
//		addRowData(new String[] {
//				/*"A A B 0",
//				"B C B 1",
//				"C B D 0",
//				"D E D 0",
//				"E E A 0"*/
//				"A B C 0",
//				"B B D 1",
//				"C B E 1",
//				"D B E 1",
//				"E E E 0"/*
//				"A D C 0",
//				"B F H 0",
//				"C E D 1",
//				"D A E 0",
//				"E C A 1",
//				"F F B 1",
//				"G B H 0",
//				"H C G 1"*/
//		});
	}
	
	/**
	 * Method to calculate the conex and minimum equivalent automata introduce before
	 * @return a string matrix with the answer
	 */
	public String[][] calculate() {
		/* If you want to use the algorithm without step one, just uncomment this part and comment the return enable
		HashSet<String> t = new HashSet<String>();
		for (String s : data.keySet()) {
			t.add(s);
		}
		return stepThree(stepTwo(t));*/
		HashSet<String> a = stepOne();
		ArrayList<ArrayList<String>> b = stepTwo(a);
		String[][] c = stepThree(b);
		return c;
	}
	
	/**
	 * Method to setup the values of Q,S,R and q1 of the mahcine
	 * @param initialData with the values to store, each row represents Q,S,R,q1 respectively
	 * @return a boolean if everything goes fine
	 */
	public Boolean setInitialData(String[] initialData) {
		
		if (initialData[0].length()<1 || initialData[1].length()<1 || initialData[2].length()<1 || initialData[3].length()<1)
			return false;
		
		String[] Q = initialData[0].split(" ");
		String[] S = initialData[1].split(" ");
		numberOfColumns = automataType ? S.length:S.length+1; //I already know how much columns it's gonna be base on automataType
		String[] R = initialData[2].split(" ");
		String q1 = initialData[3].toString();
		boolean entroQ1 = false;
		
		for (String aux: S) {
			if (aux.length()>1)
				return false;
			entryAlphabet.add(aux.charAt(0));
		}
		
		for (String aux : R) {
			if (aux.length()>1)
				return false;
			outputAlphabet.add(aux);
		}
		
		for (String aux: Q) {
			if (!data.containsKey(aux)) {
				if (q1.equals(aux)) { //Setting Object of initial state that has to be in data
					this.q1 = aux;
					entroQ1 = true;
				}
				data.put(aux,null);
			}
		}
		
		return entroQ1;
		
	}
	
	/**
	 * Method to add the information of a row or rows, the state with their transition (Where it moves with some entry)
	 * @param add with the cell information
	 * @return a boolean if everything goes fine
	 */
	public Boolean addRowData(String[] add) {
		if (add.length<1)
			return false;
		if (automataType) {			
			for (String s : add) {
				if (s.length()<1)
					return false;
				statesMealy.put(s.substring(0,s.length()-1),s.substring(s.length()-1));
			}
		}
		for (String aux: add) {
			String tmp[] = aux.split(" ");
			if (tmp.length!=(numberOfColumns+1) || !data.containsKey(tmp[0]))
				return false;
			String rowAdd[] = new String[numberOfColumns];
			for (int i = 1; i < tmp.length; i++)
				rowAdd[i-1] = tmp[i];
			data.put(tmp[0],rowAdd);
		}
		return true;
	}
	
	/**
	 * A recursive method that get out the accessible states from a given
	 * @param visited that help to store the states visited
	 * @param add with the state to add
	 * @return a hashSet collection that has all the states accessible from add
	 */
	private HashSet<String> auxStepOne(HashSet<String> visited,String add) {
		
		if (visited.contains(add))
			return visited;
		visited.add(add);
		for (String v : data.get(add)) {
			v = v.split("\r")[0];
			if (!automataType && outputAlphabet.contains(v))
				break;
			auxStepOne(visited, automataType ? v.substring(0, v.length()-1):v);
		}
		return visited;
		
	}
	
	/**
	 * Method public for call stepOne, get the accessible nodes from initial state
	 * @return hashSet wich has all the states accessible from initial state
	 */
	public HashSet<String> stepOne() {
		HashSet<String> t = auxStepOne(new HashSet<String>(),q1);
		return t;
		
	}
	
	//Esto fue lo que pense hacer par el paso No. 1 porque lo pense por "grafo conexo". Debo poder llegar desde un estado q a q1
//	private HashSet<String> auxStepOne(HashSet<String> visited,String add) {
//		if (visited.contains(add) || visited.contains(q1))
//			return visited;
//		visited.add(add);
//		for (String b: data.get(add)) {
//			if (!automataType && outputAlphabet.contains(b))
//				break;
//			String aux = automataType ? b.substring(0,b.length()-1):b;
//			auxStepOne(visited,aux);
//		}
//		return visited;
//	}
//	
//	public HashSet<String> stepOne() {
//		
//		HashSet<String> rst = new HashSet<String>();
//		for (String key: data.keySet()) {
//			if (q1.equals(key)) {
//				rst.add(key);
//				continue;
//			}
//			HashSet<String> aux = auxStepOne(new HashSet<String>(),key);
//			if (aux.contains(q1))
//				rst.add(key);
//		}
//		
//		return rst;
//		
//	}
	
	/**
	 * Method for step Two, get out the final partition 
	 * @param cEq with all the states it must to work
	 * @return a list of list that has the blocks of final partition
	 */
	public ArrayList<ArrayList<String>> stepTwo(HashSet<String> cEq) {
		
		ArrayList<ArrayList<String>> rst = stepTwoA(cEq);
		rst = stepTwoB(rst);
		
		return rst;
		
	}
	
	/**
	 * Method for step To One, gets the start partition
	 * @param cEq with all states it has to work
	 * @return a list of list that has the blocks of first partition
	 */
	private ArrayList<ArrayList<String>> stepTwoA(HashSet<String> cEq) {
		
		ArrayList<ArrayList<String>> rst = new ArrayList<ArrayList<String>>();
		HashMap<String,HashSet<String>> aux = new HashMap<String,HashSet<String>>();
		
		for (String actual : cEq) {
			String[] b = data.get(actual);
			String key = !automataType ? data.get(actual)[data.get(actual).length-1]:"";
			key = key.split("\r")[0];
			if (automataType) {
				for (String a : b) {
					a = a.split("\r")[0];
					key += a.charAt(a.length()-1);
				}
			}
			HashSet<String> tmp = aux.getOrDefault(key, new HashSet<String>());
			if (!tmp.contains(actual))
				tmp.add(actual);
			aux.put(key, tmp);
		}
		
		for (String s : aux.keySet()) {
			ArrayList<String> tmp = new ArrayList<String>();
			tmp.addAll(aux.get(s));
			rst.add(tmp);
		}
		
		return rst;
		
	}
	
	/**
	 * Method to find the states achievable from a state
	 * @param tmp the state to know their accessible states
	 * @return a list with their accessible nodes 
	 */
	private ArrayList<String> findStatesAchievable(String tmp) {
		ArrayList<String> rst = new ArrayList<String>();
		for (String a : data.get(tmp)) {
			a =  a.split("\r")[0];
			if (!automataType && outputAlphabet.contains(a))
				break;
			rst.add(automataType ? a.substring(0, a.length()-1):a);
		}
		return rst;
	}

	/**
	 * Method to find the first achievable states from a given state
	 * @param arr with the partition to search
	 * @param tmp the state to find
	 * @return a block where the state tmp is
	 */
	private ArrayList<String> findStatesFirstAchievable(ArrayList<ArrayList<String>> arr, String tmp) {
		for (ArrayList<String> a : arr) {
			for (String s : a) {
				if (tmp.equals(s))
					return a;
			}
		}
		return null;
	}
	
	/**
	 * Method to know if to states belongs to same block of a partition
	 * @param arr with the partition to search
	 * @param tmp with the name of state one
	 * @param tmp2 with the name of state two
	 * @return
	 */
	private Boolean isPartOfSameBlock(ArrayList<ArrayList<String>> arr, String tmp, String tmp2) {
		ArrayList<String> auxTmp = findStatesAchievable(tmp);
		ArrayList<String> auxTmp2 = findStatesAchievable(tmp2);
		if (auxTmp.size()!=auxTmp2.size())
			return false;
		for (int i = 0; i < auxTmp.size(); i++) {
			String a = auxTmp.get(i);
			String b = auxTmp2.get(i);
			if (findStatesFirstAchievable(arr,a)!=findStatesFirstAchievable(arr,b))
				return false;
		}
		return true;
	}
	
//	private void print(ArrayList<ArrayList<String>> a) {
//		System.out.println("-------");
//		for (int i = 0; i < a.size(); i++) {
//			for (int j = 0; j < a.get(i).size(); j++) {
//				System.out.print(a.get(i).get(j)+" ");
//			}
//			System.out.println();
//		}
//	}
	
	/**
	 * Method for step Two B that help to get next partition of a given
	 * @param parts that represents the partition that has to be partitioned again
	 * @return the final partition equivalent
	 */
	private ArrayList<ArrayList<String>> stepTwoB(ArrayList<ArrayList<String>> parts) {
		
		ArrayList<ArrayList<String>> rst = new ArrayList<ArrayList<String>>();
		
		for (ArrayList<String> arr : parts) {
			String tmp = arr.get(0);
			ArrayList<String> aux = new ArrayList<>();
			ArrayList<String> aux2 = new ArrayList<>();
			rst.add(aux);
			aux.add(tmp);
			for (int i = 1; i < arr.size(); i++) {
				String tmp2 = arr.get(i);
				if (isPartOfSameBlock(parts,tmp,tmp2))
					aux.add(tmp2);
				else
					aux2.add(tmp2);
			}
			if (aux2.size()>0 && !rst.contains(aux2))
				rst.add(aux2);
		}
		
//		print(parts); Prints each partition
		while (!stepTwoC(parts,rst)) {
			parts = rst;
			rst = stepTwoB(rst);
		}
		
		return rst;
		
	}
	
	/**
	 * Method to check if to partitions are equal
	 * @param pm with one partition
	 * @param pm1 with another partition
	 * @return true if there equal, false otherwise
	 */
	private Boolean stepTwoC(ArrayList<ArrayList<String>> pm,ArrayList<ArrayList<String>> pm1) {
		
		if (pm.size()!=pm1.size())
			return false;
		
		for (int i = 0; i < pm.size(); i++) {
			if (pm.get(i).size()!=pm1.get(i).size())
				return false;
			for (int j = 0; j < pm.get(i).size(); j++) {
				if (!pm.get(i).get(j).equals(pm.get(i).get(j)))
					return false;
			}
		}
		
		return true;
		
	}
	
	/**
	 * Method to find the index of a state in a partition
	 * @param stepTwo it's the partition where to search
	 * @param find it's the state to find
	 * @return the index where was found the state in the partition
	 */
	private int findQn(ArrayList<ArrayList<String>> stepTwo, String find) {
		int i = -1;
		for (ArrayList<String> arr : stepTwo) {
			for (String arr2 : arr) {
				if (arr2.equals(find))
					return i+1;
			}
			i++;
		}
		return i+1;
	}
	
	/**
	 * Method to do final step, step three to get the minimum equivalent
	 * @param stepTwo with the final partition produced by step Two
	 * @return a string matrix with their cells meaning the information of calculus computed
	 */
	private String[][] stepThree(ArrayList<ArrayList<String>> stepTwo) {
		HashMap<String,HashSet<String>> veryHelpful = new HashMap<String,HashSet<String>>();
		String[][] rst = new String[stepTwo.size()+1][numberOfColumns+1];
		rst[0][0] = "   ";
		int a = 1;
		for (Character c : entryAlphabet) {
			rst[0][a] = c+"";
			a++;
		}
		if (!automataType)
			rst[0][numberOfColumns] = "";
		a = 1;
		for (int i = 0; i < stepTwo.size(); i++) { //Each qn
			rst[i+1][0] = "q"+a;
			HashSet<String> helpful = new HashSet<String>();
			veryHelpful.put(rst[i+1][0],helpful);
			String test = stepTwo.get(i).get(0);
			for (int j = 0; j < entryAlphabet.size(); j++) {
				String cell = data.get(test)[j];
				cell = cell.split("\r")[0];
				String qn = "q"+(findQn(stepTwo, automataType ? cell.substring(0,cell.length()-1):cell)+1);
				qn = automataType ? qn+"/"+cell.substring(cell.length()-1):qn;
				rst[i+1][j+1] = qn;
				helpful.add(data.get(test)[numberOfColumns-1].split("\r")[0]);
			}
			a++;
		}
		if (!automataType) {
			for (int i = 0; i < veryHelpful.keySet().size(); i++) { //Cada fila
				String key = (String) veryHelpful.keySet().toArray()[i];
				rst[i+1][numberOfColumns] = veryHelpful.get(key).toArray()[0].toString();
			}
		}
		return rst;
	}
	
	/**
	 * Method to know type of automata working on
	 * @return automataType - True for Mealy and False for Moore
	 */
	public Boolean getAutomataType() {
		return automataType;
	}
	
}
