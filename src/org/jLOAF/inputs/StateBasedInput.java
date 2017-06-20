package org.jLOAF.inputs;



import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.sim.SimilarityMetricStrategy;

public class StateBasedInput extends Input {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Input input;
	private Case c;
	public StateBasedInput(String name, SimilarityMetricStrategy sim) {
		super(name);
		simStrategy=sim;
	
		
	}
	
	public Case getCase(){
		return c;
	}
	
	public Input getInput(){
		return input;
	}
	public void setInput(Input i){
		input=i;
	}
	public void setCase(Case c1){
		c=c1;
	}
	private Case getCase(int indx){
		Case cc=c;
			int i=3;
			while(i<=indx){
				if(i%2!=0){
					cc=((StateBasedInput)c.getInput()).getCase();
				}
				if(cc==null){
					return null;
				}
				
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
	
	
	
	

}
