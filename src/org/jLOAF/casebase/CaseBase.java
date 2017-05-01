package org.jLOAF.casebase;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.jLOAF.Reasoning;
import org.jLOAF.reasoning.WeightedKNN;

public class CaseBase implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Collection<Case> cb;
	
	public CaseBase(){
		this.cb = new ArrayList<Case>();
	}
	
	public Collection<Case> getCases(){
		return this.cb;
	}
	
	public void add(Case c){
		this.cb.add(c);
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
	 * preprocess the casebase by only adding a case into the casebase if the prediction of the action using the current casebase is wrong
	 * return the new casebase
	 * sacha gunaratne 2017 may
	 * ***/
	public static CaseBase preProcess(CaseBase cb){
		CaseBase cnew = new CaseBase();
		Reasoning r;
		int count = 0;
		int k = 1;
		
		for (Case c: cb.getCases()){
			if(count>0){
				cnew.add(c);
				if(count>0&&count<7)k=count;
				r = new WeightedKNN(k,cnew);
				if(r.selectAction(c.getInput()).equals(c.getAction())) {cnew.remove(c);count++;}
			}
			else{
				cnew.add(c);
				count++;
			}
		}
		return cnew;
	}
}
