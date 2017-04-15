package org.jLOAF.casebase;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jLOAF.action.Action;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Input;

public class CaseBase implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private ArrayList<Case> cb;
	
	public CaseBase(){
		this.cb = new ArrayList<Case>();
	}
	
	public ArrayList<Case> getCases(){
		return this.cb;
	}
	
	public void add(Case c){
		this.cb.add(c);
	}
	

	public int getSize(){
		return this.cb.size();
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
	
	public static void saveAsTrace(CaseBase casebase, String filename, List<String> actions) throws IOException{
		if(filename == null || casebase == null){
			throw new IllegalArgumentException("A null value was given for the file name");
		}
		
		List<Double> input;
		String action;
		int action_num;
		FileWriter f1 = null;
		try {
			f1 = new FileWriter(filename);
			
			for(Case cb: casebase.getCases()){
				Input i = cb.getInput();
				Action a = cb.getAction();
				input = convert(i);
				
				for(Double val: input){
					f1.write(String.valueOf(val));
					f1.write(",");
				}
				
				action = a.getName();
				action_num = getActionNum(action, actions);
				f1.write(String.valueOf(action_num));
				f1.write("\r\n");
			}
			
			f1.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static int getActionNum(String action, List<String> actions) {
		return actions.indexOf(action);
	}

	private static List<Double> convert(Input i) {
		// takes an input and reads it. Assumes its made up of complexInputs which is made up of complex or atomic inputs. 
		List<Double> input = new ArrayList<Double>();
		Input result;
		
		if (i instanceof ComplexInput){
			ComplexInput c_input = ((ComplexInput) i);
			for (String key: (c_input.getChildNames())){
				result = c_input.get(key);
				if(result instanceof AtomicInput){
					input.add(((AtomicInput) result).getFeature().getValue());
				}else if(result instanceof ComplexInput){
					//if its the case that there is a nested complexInput then it will go through the function again.  
					input = convert(result);
				}
			}
		}
		//if it's simply one atomic Input then it converts it. 
		if (i instanceof AtomicInput){
			input.add(((AtomicInput) i).getFeature().getValue());
		}
		return input;
	}
}
