package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
public class Automata {
	
	private HashMap<String,String[]> data;
	private HashSet<Character> entryAlphabet;
	private HashSet<String> outputAlphabet;
	private Integer numberOfColumns;
	/**
	 * Attribute to know type of table, True for Mealy and False for Moore
	 */
	private Boolean automataType;
	/**
	 * Attribute to know the initial state
	 */
	private String q1;
	
	public Automata(Boolean automataType) {
		this.automataType = automataType;
		data = new HashMap<String,String[]>();
		entryAlphabet = new HashSet<Character>();
		outputAlphabet = new HashSet<String>();
	}
	
	public void chargeTest() {
		setInitialData(new String[] {"A B C D E","a b","0 1 2","A"});
		automataType = true;
		numberOfColumns = automataType ? 2:3;
		boolean t = addRowData(new String[] {
				"A D0 C0",
				"B C0 A0",
				"C A0 B1",
				"D D0 E1",
				"E D0 D0"
		});
//		automataType = false;
//		numberOfColumns = automataType ? 2:3;
//		boolean t = addRowData(new String[] {
//				"A B C 0",
//				"B C B 1",
//				"C D C 0",
//				"D E D 0",
//				"E E A 0"
//		});
		HashSet<String> a = stepOne();
//		for (String b:a) {
//			System.out.print(b+" ");
//			for (String s : data.get(b)) {
//				System.out.print(s+" ");
//			}
//			System.out.println("");
//		}
		stepTwo(a);
	}
	
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
			entryAlphabet.add(aux.toCharArray()[0]);
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
	
	public Boolean addRowData(String[] add) {
		for (String aux: add) {
			String tmp[] = aux.split(" ");
			if (tmp.length!=(numberOfColumns+1) || !data.containsKey(tmp[0]) || data.getOrDefault(tmp[0], null)!=null)
				return false;
			String rowAdd[] = new String[numberOfColumns];
			for (int i = 1; i < tmp.length; i++)
				rowAdd[i-1] = tmp[i];
			data.put(tmp[0],rowAdd);
		}
		return true;
	}
	
	private HashSet<String> auxStepOne(HashSet<String> visited,String add) {
		if (visited.contains(add) || visited.contains(q1))
			return visited;
		visited.add(add);
		for (String b: data.get(add)) {
			if (!automataType && outputAlphabet.contains(b))
				break;
			String aux = automataType ? b.substring(0,b.length()-1):b;
			auxStepOne(visited,aux);
		}
		return visited;
	}
	
	public HashSet<String> stepOne() {
		
		HashSet<String> rst = new HashSet<String>();
		for (String key: data.keySet()) {
			if (q1.equals(key)) {
				rst.add(key);
				continue;
			}
			HashSet<String> aux = auxStepOne(new HashSet<String>(),key);
			if (aux.contains(q1))
				rst.add(key);
		}
		
		return rst;
		
	}
	
	public ArrayList<ArrayList<String>> stepTwo(HashSet<String> cEq) {
		
		ArrayList<ArrayList<String>> rst = stepTwoA(cEq);
		rst = stepTwoB(rst);
		rst = stepTwoC(rst);
		
		return null;
		
	}
	
	private ArrayList<ArrayList<String>> stepTwoA(HashSet<String> cEq) {
		
		ArrayList<ArrayList<String>> rst = new ArrayList<ArrayList<String>>();
		HashMap<String,HashSet<String>> aux = new HashMap<String,HashSet<String>>();
		
		for (String actual : cEq) {
			String[] b = data.get(actual);
			String key = !automataType ? data.get(actual)[data.get(actual).length-1]:"";
			if (automataType) {
				for (String a : b)
					key += a.charAt(a.length()-1);
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

	private ArrayList<ArrayList<String>> stepTwoB(ArrayList<ArrayList<String>> parts) {
		
		ArrayList<ArrayList<String>> rst = new ArrayList<ArrayList<String>>();
		
		
		
		return rst;
		
	}
	
	private ArrayList<ArrayList<String>> stepTwoC(ArrayList<ArrayList<String>> parts) {
		
		ArrayList<ArrayList<String>> rst = new ArrayList<ArrayList<String>>();
		
		
		
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
