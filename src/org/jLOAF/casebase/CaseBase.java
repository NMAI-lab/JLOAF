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
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.sim.StateBasedSimilarity;
import org.jLOAF.sim.StateBased.OrderedSimilarity;
import org.jLOAF.weights.SimilarityWeights;
import org.jLOAF.sim.complex.*;
/**
 * a caseBase is simply a list of cases, which is a full experiment done by an expert.
 */
public class CaseBase implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Collection<Case> cb;
	private Case latest;
	/**
	 * Constructor that constructs the casebase and initialize its variables.
	 */
	public CaseBase(){
		this.cb = new ArrayList<Case>();
		latest=null;
	}
	
	/**
	 * returns the collections of cases this casebase has.
	 * @return the collection of cases this casebase has.
	 */
	public Collection<Case> getCases(){
		return this.cb;
	}
	/**
	 * adds a case to this casebase, and while adding it, it gives the features of the input of the given case a weight of one, and that only
	 * happens if the similarityMetric used by the case's input is the weightedMean similarity.
	 * @param c a case to be added to the casebase.
	 */
	public void add(Case c){
		Input i = ((StateBasedInput)c.getInput()).getInput();

		ComplexInput i1 =null;
		try{
			i1= (ComplexInput)i;	
		}catch(ClassCastException e){
			cb.add(c);
			return ;
		}
		if(i1.getSimilarityMetricStrategy() instanceof WeightedMean){
			if(((WeightedMean)i1.getSimilarityMetricStrategy()).getSimilarityWeights() instanceof SimilarityWeights ){
				SimilarityWeights sim =(SimilarityWeights)((WeightedMean)i1.getSimilarityMetricStrategy()).getSimilarityWeights();
				for(String w:i1.getChildNames()){
					if(sim.getWeight(w)==0){
						sim.setFeatureWeight(w,1);
					}

				}
			}
		}
		this.cb.add(c);
	}
	
	/**
	 * loops through a casebase and adds its cases to the list of cases this casebase has.
	 * @param cbnew the casesbase to be added to this casebase.
	 */
	
	public void addCaseBase(CaseBase cbnew){
		for(Case c: cbnew.getCases()){
			cb.add(c);
		}
	}
	/**
	 * creates a case based on the input and action passed to it, where the input of the case created is a created stateBasedInput(a trace),
	 * that takes the input passed to the function to be its most recent input.
	 * @param i, the most recent Input of the stateBasedInput of the case created and added.
	 * @param a the action of the case created and added.
	 * @param sim the similarity of the stateBasedInput created. 
	 */
	public void createThenAdd(Input i,Action a,StateBasedSimilarity sim){
		StateBasedInput i1 = new  StateBasedInput(i.getName(),sim);
		i1.setInput(i);
		i1.setCase(latest);
		latest=new Case(i1,a);
		add(latest);
	}
	
	/***
	 * Adds a list of new casebases to the current casebase
	 * @param cblist the casebase list to be added to this casebase.
	 * **/
	public void addListOfCaseBases(List<CaseBase> cblist){
		for(CaseBase cnew: cblist){
			this.addCaseBase(cnew);
		}
	}
	/**
	 * returns the size of the casebase.
	 * @return the size of the casebase
	 */
	public int getSize(){
		return this.cb.size();
	}
	/**
	 * removes a case from the casebase.
	 * @param c the case to be removed from the casebase.
	 */
	public void remove(Case c){
		cb.remove(c);
	}
	
	public void removeAll(Case c){
		cb.removeAll((Collection<?>) c);
	}
	/**
	 * a static method that loads the content of a file, that has a casebase object written in it, using the serializable interface.
	 * @param filename the name of the file that contains a saved casebase object.
	 * @return a casebase based on the object written in the given file
	 */
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
	/**
	 * exports a casebase to a file using the serializable interface
	 * @param casebase the casebase to be written in the file.
	 * @param filename  the name of destination file.
	 */
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
	
	/**
	 * Converts a casebase into a csv file with all the features
	 * In the case of missing features it places a template value of 1000.0
	 * The columns names are places on top of each column
	 * The actions are converted into numerical values
	 * 
	 * @author sachagunaratne
	 * @since 2017 June
	 * 
	 */
	public static List<String> saveAsTrace(CaseBase casebase, String filename, boolean outputColumnNames) throws IOException{
		if(filename == null ){
			throw new IllegalArgumentException("A null value was given for the file name");
		}
		if(casebase == null){
			throw new IllegalArgumentException("A null value was given for the casebase ");
		}
		
		List<Action> actions = CaseBase.getActionNames(casebase);
		List<String> feature_names = new ArrayList<String>();
		
		HashMap<String, Double> input = new HashMap<String, Double>();
		String action;
		double action_num;
		double placeholder_val = 6.6;
		FileWriter f1 = null;
		HashMap<String,List<Double>> inputs= new HashMap<String,List<Double>>();
		List<Double> actions_container= new ArrayList<Double>();
		try {
			f1 = new FileWriter(filename);
			int count = 0;
			
			for(Case cb: casebase.getCases()){
				Input i = ((StateBasedInput)cb.getInput()).getInput();
				Action a = cb.getAction();
				input = convert(i);
				
				for(String key: input.keySet()){
					if (!inputs.containsKey(key)){
						List<Double> temp_list = new ArrayList<Double>();
						for(int ii=0;ii<count;ii++){
							temp_list.add(placeholder_val);
						}
						temp_list.add(input.get(key));
						inputs.put(key,temp_list);
					}else{
						List<Double> actual_list = inputs.get(key);
						for(int ii=actual_list.size();ii<count;ii++){
							actual_list.add(placeholder_val);
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
			
			//make sure that all lists in inputs are the length of casebase by adding placeholder variables at the end
			for(String key:inputs.keySet()){
				List<Double> actual_list = inputs.get(key);
				for(int ii=actual_list.size();ii<casebase.getSize();ii++){
					actual_list.add(placeholder_val);
				}
			}
			
			//System.out.println("CaseBase size: "+ casebase.getSize());
			if(outputColumnNames){
				for(String keys2: inputs.keySet()){
					f1.write(keys2);
					f1.write(",");
				}
				f1.write("\n");
			}
			
			for(String keys2: inputs.keySet()){
				feature_names.add(keys2);
			}
			
			//writing
			for(int jj=0;jj<casebase.getSize();jj++){
				for(String key3: inputs.keySet()){
					List<Double> results = inputs.get(key3);
					double val = results.get(jj);
					if(Double.isNaN(val)){
						throw new ArithmeticException("There shouldn't be any NaN values - could be something wrong with the logFile2CaseBase.java ");
					}
					//val += 1.0;
					f1.write(String.valueOf(val));
					f1.write(",");
				}
				
				f1.write(String.valueOf(actions_container.get(jj)));
				
				f1.write("\n");
				
			}
			
			f1.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Completed trace creation!");
		return feature_names;
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
	/**
	 * returns the index of an action in a given list of actions
	 * @param action the actions we are looking for 
	 * @param actions the list of actions that contains the action we are looking for
	 * @return the index of the action in the given list of actions
	 */
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
	 * @param  i the input to be converted
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
