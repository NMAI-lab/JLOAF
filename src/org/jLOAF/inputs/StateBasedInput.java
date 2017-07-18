package org.jLOAF.inputs;



import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.sim.StateBasedSimilarity;
/**
 * a stateBasedInput is a trace of inputs and action, this input is used in for satebased agents that use the jloaf frame work.
 * it is also that input of the cases of the expert agents, where the first input is the most recent one.
 * the stateBased input has a case and an input, where the input cannot be sateBased input, and the case has a statebased Input and an action.
 * this recursive architecture of the statebased input represent the trace of this input.
 * a stateBasedInput trace would be like this: input-Action-input-Action-input. always of odd size.
 * @author Ibrahim Ali Fawaz
 *
 */
public class StateBasedInput extends Input {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Input input;
	private Case c;
	int size;
	/**
	 * Constructor 
	 * @param name the name of the statebasedInput
	 * @param sim the similarityMetric strategy used by this input for comparison purposes. 
	 */
	public StateBasedInput(String name, StateBasedSimilarity sim) {
		super(name);
		simStrategy=sim;
		size=0;
		c=null;
		
	}
	/**
	 * 
	 * @return the most recent case of this input.
	 */
	public Case getCase(){
		return c;
	}
	/**
	 * this method usually used for reactive agents.
	 * @return the most recent Input of this input.
	 */
	public Input getInput(){
		return input;
	}
	/**
	 * sets the latest input for this stateBasedInput (the trace)
	 * @param i the latest input to be set for this stateBasedInput (trace)
	 */
	public void setInput(Input i){
		input=i;
		size++;
	}
	/**
	 * sets the case for this stateBasedInput(case)
	 * @param c1 the case to be set for this stateBasedInput
	 */
	public void setCase(Case c1){
		c=c1;
		if(c1!=null){
		size++;
		}
	}
	/**
	 * returns the size of the trace of the stateBasedInput which is always odd
	 * @return the size of the trace of the stateBasedInput which is always odd
	 */
	
	public int getSize(){
		if(c!=null){
			return size+((StateBasedInput)c.getInput()).getSize();
		}
		return size;
		
	}
	/**
	 * returns the ith case based on the index passed to the function
	 * @param indx the index of the case to be returned
	 * @return a case with the given index
	 */
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
	/**
	 * returns the action of the given index
	 * @param indx the index of the action to be returned, the index of the action is always an odd number
	 * @return the action of the given index
	 */
	public Action getAction(int indx){
		Case cc=getCase(indx);
		if(cc==null){
			return null;
		}
		
		return cc.getAction();
	}
	/**
	 * returns the input of the given index. where zero is the most recent input, and the index of the input is alwasy even.
	 * @param indx the index of the input to be returned
	 * @return the input of the given index
	 */
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
	/**
	 * this function copies an input's trace for the earliest element to the position specified
	 * the elements of the copied trace are of the same instances of the ones of the original one.
	 * this method is usually only used for testing purposes
	 * @param i the index you want to stop at in the trace, i can only be even. 
	 * @return stateBasedInput with the copied trace.
	 */
	public StateBasedInput copyTraceupTo(int i){
		if(i%2 !=0 || i==getSize()){
			throw new IllegalArgumentException();
		}
		
		return copy(i,0);
			
			
			
	}
	/**
	 * a private method the is used by the copyUpTo method, it recursively copies the trace of an input
	 * @param i the maximum number of copied elements.
	 * @pram j the current index of the case or input to be copied.
	 * @return a stateBasedInput that is a copy of a given stateBased input up to i elements
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
