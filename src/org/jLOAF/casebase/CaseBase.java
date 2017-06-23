package org.jLOAF.casebase;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jLOAF.action.Action;
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
}
