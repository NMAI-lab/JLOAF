package org.jLOAF.casebase;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.jLOAF.action.Action;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Input;
import org.jLOAF.inputs.StateBasedInput;
import org.jLOAF.sim.StateBased.OrderedSimilarity;


public class CaseBase implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Collection<Case> cb;
	private Case latest;
	public CaseBase(){
		this.cb = new ArrayList<Case>();
		latest=null;
	}
	
	public Collection<Case> getCases(){
		return this.cb;
	}
	
	public void add(Case c){
		this.cb.add(c);
	}
	
	/***
	 * Adds a new casebase to the current casebase
	 * Sacha Gunaratne 2017 May
	 * **/
	
	private void addCaseBase(CaseBase cbnew){
		for(Case c: cbnew.getCases()){
			cb.add(c);
		}
	}
	
	public void createThenAdd(Input i,Action a){
		StateBasedInput i1 = new  StateBasedInput(i.getName(),new OrderedSimilarity());
		i1.setInput(i);
		i1.setCase(latest);
		latest=new Case(i1,a);
		cb.add(latest);
	}
	
	/***
	 * Adds a list of new casebases to the current casebase
	 * Sacha Gunaratne 2017 May
	 * **/
	public void addListOfCaseBases(List<CaseBase> cblist){
		for(CaseBase cnew: cblist){
			this.addCaseBase(cnew);
		}
	}

	public int getSize(){
		return this.cb.size();
	}
	
	public void remove(Case c){
		cb.remove(c);
	}
	
	public static CaseBase load(String filename) {
		//test the parameters
		if(filename == null){
			throw new IllegalArgumentException("A null value was given for the file name");
		}
				
		try{
			//open the file streams
			FileInputStream fileIn = new FileInputStream(filename);
			ObjectInputStream ois = new ObjectInputStream(fileIn);
					
			Object o = ois.readObject();
					
			//make sure we read a CaseBase
			if( !(o instanceof CaseBase)){
					return null;
			}
			return (CaseBase)o;
		}catch(Exception e){
			//if there was a file problem we return null
			System.out.println("Error loading CaseBase:" + e.getMessage());
			return null;
		}	
	}
	
	public static void save(CaseBase casebase, String filename) {
		//test the parameters
		if(filename == null || casebase == null){
			throw new IllegalArgumentException("A null value was given for the file name");
		}
		
		try{
			//create the output streams
			FileOutputStream fileOut = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fileOut);
			    
			//write the case base
			oos.writeObject(casebase);
				
			//close the output streams
			oos.close();
			fileOut.close();
		}catch(IOException e){
			System.out.println("Error saving CaseBase:" + e.toString());
		}
	
	}
	
	/***
	 * Converts a casebase into a csv file with all the features
	 * In the case of missing features it places a template value of 1000.0
	 * The columns names are places on top of each column
	 * The actions are converted into numerical values
	 * 
	 * @author sachagunaratne
	 * @since 2017 June
	 * ***/
	public static void saveAsTrace(CaseBase casebase, String filename, boolean outputColumnNames) throws IOException{
		if(filename == null ){
			throw new IllegalArgumentException("A null value was given for the file name");
		}
		if(casebase == null){
			throw new IllegalArgumentException("A null value was given for the casebase ");
		}
		
		List<Action> actions = CaseBase.getActionNames(casebase);
		
		HashMap<String, Double> input = new HashMap<String, Double>();
		String action;
		double action_num;
		FileWriter f1 = null;
		HashMap<String,List<Double>> inputs= new HashMap<String,List<Double>>();
		List<Double> actions_container= new ArrayList<Double>();
		try {
			f1 = new FileWriter(filename);
			int count = 0;
			
			for(Case cb: casebase.getCases()){
				Input i = cb.getInput();
				Action a = cb.getAction();
				input = convert(i);
				
				for(String key: input.keySet()){
					if (!inputs.containsKey(key)){
						List<Double> temp_list = new ArrayList<Double>();
						for(int ii=0;ii<count;ii++){
							temp_list.add(300.5);
						}
						temp_list.add(input.get(key));
						inputs.put(key,temp_list);
					}else{
						List<Double> actual_list = inputs.get(key);
						for(int ii=actual_list.size();ii<count;ii++){
							actual_list.add(300.5);
						}
						actual_list.add(input.get(key));
						inputs.replace(key, actual_list);
					}
				}
					
				action = a.getName();
				action_num = getActionNum(action, actions);
				actions_container.add(action_num+1);
				
				count++;
			}
			//System.out.println("CaseBase size: "+ casebase.getSize());
			boolean leave = false;
			if(outputColumnNames){
				for(String keys2: inputs.keySet()){
					f1.write(keys2);
					f1.write(",");
				}
				f1.write("\n");
			}
			
			for(int jj=0;jj<casebase.getSize();jj++){
				for(String key3: inputs.keySet()){
					List<Double> results = inputs.get(key3);
					//System.out.println("Results size: "+ results.size());
					if(jj==3651){
						leave = true;
						break;
					}
					double val = results.get(jj);
					if(val!=1000.0){
						val += 1.0;
						f1.write(String.valueOf(val));
					}else{
						f1.write(".");
					}
					f1.write(",");
				}
				
				if(leave){break;}
				f1.write(String.valueOf(actions_container.get(jj)));
				
				f1.write("\n");
				
			}
			
			f1.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Completed trace creation!");
	}
	
	
	/***
	 * Takes a casebase and gets the actions and returns a list of them
	 * @author sachagunaratne
	 * @param CaseBase
	 * @since 2017 June
	 * ***/
	public static List<Action> getActionNames(CaseBase casebase) {
		List<Action> actions = new ArrayList<Action>();
		Action act;
		
		for(Case c: casebase.getCases()){
			boolean exists = false;
			act = c.getAction();
			if(actions.isEmpty()){actions.add(act);}
			for(Action a:actions){
				if (a.getName().equals(act.getName())){
					exists = true;
				}
			}
			if(!exists){actions.add(act);}
		}
		return actions;
	}


	private static int getActionNum(String action, List<Action> actions) {
		int index =0;
		for(Action a: actions){
			if(a.getName().equals(action)){return index;}
			index++;
		}
		return -1;
	}
	
	/***
	 * Converts a input into a list of double values
	 * 
	 * @param Input i
	 * 
	 * @author sachagunaratne
	 * ***/
	public static HashMap<String, Double> convert(Input i) {
		// takes an input and reads it. Assumes its made up of complexInputs which is made up of complex or atomic inputs. 
		HashMap<String, Double> input = new HashMap<String, Double>();
		Input result;
		
		if (i instanceof ComplexInput){
			ComplexInput c_input = ((ComplexInput) i);
			HashMap<String, Double> input_temp = null;
			for (String key: (c_input.getChildNames())){
				result = c_input.get(key);
				if(result instanceof AtomicInput){
					input.put(((AtomicInput) result).getName(),((AtomicInput) result).getFeature().getValue());
				}else if(result instanceof ComplexInput){
					//if its the case that there is a nested complexInput then it will go through the function again.  
					input_temp = convert(result);
					for (String key2: input_temp.keySet()){
						input.put(key2,(input_temp.get(key2)));
					}
				}
			}
		}
		//if it's simply one atomic Input then it converts it. 
		if (i instanceof AtomicInput){
			input.put(((AtomicInput) i).getName(),((AtomicInput) i).getFeature().getValue());
		}
		return input;
	}
}
