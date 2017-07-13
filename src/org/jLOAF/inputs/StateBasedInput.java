package org.jLOAF.inputs;



import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.sim.StateBasedSimilarity;

public class StateBasedInput extends Input {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Input input;
	private Case c;
	int size;
	public StateBasedInput(String name, StateBasedSimilarity sim) {
		super(name);
		simStrategy=sim;
		size=0;
		c=null;
		
	}
	
	public Case getCase(){
		return c;
	}
	
	public Input getInput(){
		return input;
	}
	public void setInput(Input i){
		input=i;
		size++;
	}
	public void setCase(Case c1){
		c=c1;
		if(c1!=null){
		size++;
		}
	}
	public int getSize(){
		if(c!=null){
			return size+((StateBasedInput)c.getInput()).getSize();
		}
		return size;
		
	}
	private Case getCase(int indx){
		Case cc=c;
			int i=3;
			while(i<=indx){
				if(i%2!=0){
					cc=((StateBasedInput)cc.getInput()).getCase();
				}
				if(cc==null){
					return null;
				}
				i=i+2;
			}
			
		return cc;
		
	}
	
	public Action getAction(int indx){
		Case cc=getCase(indx);
		if(cc==null){
			return null;
		}
		
		return cc.getAction();
	}
	public Input getInput(int indx){
		if(indx==0){
			return input;
		}
		Case cc=getCase(indx);
		if(cc==null){
			return null;
		}
		return ((StateBasedInput)cc.getInput()).getInput();
	}
	/*
	 * this function copies an input's trace for the earliest element to the position specified
	 * the elements of the copied trace are of the same instances of the ones of the original one.
	 * this method is usually only used for testing purposes
	 * @param i the index you want to stop at in the trace, i can only be even. 
	 */
	public StateBasedInput copyTraceupTo(int i){
		if(i%2 !=0 || i==getSize()){
			throw new IllegalArgumentException();
		}
		
		return copy(i,0);
			
			
			
	}
	/*
	 * a private method the is used by the copyUpTo method, it recursively copies the trace of an input
	 */
	private StateBasedInput copy(int i, int j) {
		
		StateBasedInput newOne = new StateBasedInput(this.getInput(j).getName(),(StateBasedSimilarity)this.getSimilarityMetricStrategy());
		newOne.setInput(this.getInput(j));
		if(j==i){
			return newOne;
		}
		
		newOne.setCase(new Case(copy(i,j+2),getAction(j+1)));
		return newOne;
			
	}
	
	
	
	

}
