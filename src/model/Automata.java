package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
public class Automata {
	
	private HashMap<String,String[]> data;
	private HashMap<String,String> statesMealy;
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
		statesMealy = new HashMap<String,String>();
		entryAlphabet = new HashSet<Character>();
		outputAlphabet = new HashSet<String>();
	}
	
	public void chargeTest() {
		setInitialData(new String[] {"A B C D E","a b","0 1","A"});
		automataType = true;
		numberOfColumns = automataType ? 2:3;
		addRowData(new String[] {
				"A D0 C0",
				"B C0 A0",
				"C A0 B1",
				"D D0 E1",
				"E D0 D0"
		});
//		automataType = false;
//		numberOfColumns = automataType ? 2:3;
//		boolean t = addRowData(new String[] {
//				"A A B 0",
//				"B C B 1",
//				"C B D 0",
//				"D E D 0",
//				"E E A 0"/*
//				"A B C 0",
//				"B B D 1",
//				"C B E 1",
//				"D B E 1",
//				"E E E 0"*/
//		});
		HashSet<String> a = stepOne();
		ArrayList<ArrayList<String>> b = stepTwo(a);
		printTwo(b);
		stepThree(b);
		
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
		if (automataType) {			
			for (String s : add) {
				statesMealy.put(s.substring(0,s.length()-1),s.substring(s.length()-1));
			}
		}
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
		
		if (visited.contains(add))
			return visited;
		visited.add(add);
		for (String v : data.get(add)) {
			if (!automataType && outputAlphabet.contains(v))
				break;
			auxStepOne(visited, automataType ? v.substring(0, v.length()-1):v);
		}
		return visited;
		
	}
	
	public HashSet<String> stepOne() {
	
		return auxStepOne(new HashSet<String>(),q1);
		
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
	
	public void printTwo(ArrayList<ArrayList<String>> rst) {
		int i = 1;
		for (ArrayList<String> a : rst) {
			System.out.print("B"+i+" = { ");
			for (String s : a) {
				System.out.print(s+" ");
			}
			System.out.print("}\n");
			i++;
		}
		System.out.println("--------------");
	}
	
	public ArrayList<ArrayList<String>> stepTwo(HashSet<String> cEq) {
		
		ArrayList<ArrayList<String>> rst = stepTwoA(cEq);
		printTwo(rst);
		rst = stepTwoB(rst);
		
		return rst;
		
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
	
	private ArrayList<String> findStatesAchievable(String tmp) {
		ArrayList<String> rst = new ArrayList<String>();
		for (String a : data.get(tmp)) {
			if (!automataType && outputAlphabet.contains(a))
				break;
			rst.add(automataType ? a.substring(0, a.length()-1):a);
		}
		return rst;
	}

	private ArrayList<String> findStatesFirstAchievable(ArrayList<ArrayList<String>> arr, String tmp) {
		for (ArrayList<String> a : arr) {
			for (String s : a) {
				if (tmp.equals(s))
					return a;
			}
		}
		return null;
	}
	
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
		
		while (!stepTwoC(parts,rst)) {
			printTwo(rst);
			parts = rst;
			rst = stepTwoB(rst);
		}
		
		return rst;
		
	}
	
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
	
	private String[][] stepThree(ArrayList<ArrayList<String>> stepTwo) {
		System.out.println("Last Step");
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
				String qn = "q"+(findQn(stepTwo, automataType ? cell.substring(0,cell.length()-1):cell)+1);
				qn = automataType ? qn+"/"+cell.substring(cell.length()-1):qn;
				rst[i+1][j+1] = qn;
				helpful.add(data.get(test)[j]);
			}
			a++;
		}
		if (!automataType) {
			for (int i = 0; i < veryHelpful.keySet().size(); i++) {
				String n = "";
				String key = (String) veryHelpful.keySet().toArray()[i];
				HashSet<String> vhValues = veryHelpful.get(key);
				for (String k : data.keySet()) {
					HashSet<String> vhValues2 = new HashSet<String>();
					for (int j = 1; j < data.get(k).length-1; j++) {
						vhValues2.add(data.get(k)[j]);
					}
					for (String s : vhValues) {
						if (vhValues2.contains(s)) {
							n = data.get(k)[numberOfColumns-1];
							break;
						}
					}
				}
				rst[i+1][numberOfColumns] = n;
			}
		}
		for (int i = 0; i < rst.length; i++) {
			for (int j = 0; j < rst[i].length; j++) {
				System.out.print(rst[i][j]+" ");
			}
			System.out.println();
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
