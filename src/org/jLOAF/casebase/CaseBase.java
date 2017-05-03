package org.jLOAF.casebase;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.jLOAF.Reasoning;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Input;
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
	
	public static CaseBase normalize(CaseBase cb){
		CaseBase cnew = new CaseBase();
		Input in;
		Input in2;
		double val;
		double max = 0;
		double min =0;
		double count =0;
		
		//gets min and max values 
		for(Case c: cb.getCases()){
			in = c.getInput();
			max = getMax(max,count,in);
			min = getMin(max,count,in);
			count =1;
		}
		
		
		return cnew;
	}

	
	private static double getMax(double max, double count, Input in){
		Input in2;
		double val;
		double max_new = max;
		if (in instanceof ComplexInput){
			ComplexInput cmplx = (ComplexInput) in;
			for(String s: cmplx.getChildNames()){
				in2 = cmplx.get(s);
				if (in2 instanceof ComplexInput){
					getMax(max_new,count,in2);
				}
				if(in2 instanceof AtomicInput){
					AtomicInput atmc = (AtomicInput) in2;
					val = atmc.getFeature().getValue();
					
					if(count==0){
						max_new=val;
					}else{
						if(val>max_new) max_new = val;
					}
					count=1;
				}
			}
		}else if(in instanceof AtomicInput){
			AtomicInput atmc = (AtomicInput) in;
			val = atmc.getFeature().getValue();
			
			if(count==0){
				max_new=val;
			}else{
				if(val>max_new) max_new = val;
			}
			count=1;
		}
		return max_new;
	}
	
	private static double getMin(double min, double count, Input in){
		Input in2;
		double val;
		double min_new = min;
		if (in instanceof ComplexInput){
			ComplexInput cmplx = (ComplexInput) in;
			for(String s: cmplx.getChildNames()){
				in2 = cmplx.get(s);
				if (in2 instanceof ComplexInput){
					getMin(min_new,count,in2);
				}
				if(in2 instanceof AtomicInput){
					AtomicInput atmc = (AtomicInput) in2;
					val = atmc.getFeature().getValue();
					
					if(count==0){
						min_new=val;
					}else{
						if(val>min_new) min_new = val;
					}
					count=1;
				}
			}
		}else if(in instanceof AtomicInput){
			AtomicInput atmc = (AtomicInput) in;
			val = atmc.getFeature().getValue();
			
			if(count==0){
				min_new=val;
			}else{
				if(val>min_new) min_new = val;
			}
			count=1;
		}
		return min_new;
	}
}
