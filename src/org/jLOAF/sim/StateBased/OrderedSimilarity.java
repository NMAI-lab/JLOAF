package org.jLOAF.sim.StateBased;


import org.jLOAF.inputs.Input;
import org.jLOAF.inputs.StateBasedInput;
import org.jLOAF.sim.StateBasedSimilarity;
/*
 * this class represents a similarity metric that calculates the similarity between two stateBasedInputs based on the whole trace,in order
 * @author Ibrahim Ali Fawaz
 */
public class OrderedSimilarity extends StateBasedSimilarity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public double similarity(Input i1, Input i2) {
		//null check
				if(i1==null || i2 ==null) return 0.0;
				
				if(!(i1 instanceof StateBasedInput) || !(i2 instanceof StateBasedInput)){
					throw new IllegalArgumentException("this metric is only used for StateBased inputs");
				}
				
				StateBasedInput st1 = (StateBasedInput)i1;
				StateBasedInput st2 = (StateBasedInput)i2;
				int sizeSt1 = st1.getSize();
				int sizeSt2=st2.getSize();
				int penalty =Math.abs(sizeSt1-sizeSt2);
				if(penalty > 5){
					return 0;
				}
				double similarity = 0;
				for(int i=0;i<Math.min(sizeSt1, sizeSt2);i=i+2){
					similarity+=st1.getInput(i).similarity(st2.getInput(i));
					if(i+1 !=Math.min(sizeSt1, sizeSt2)){
						
						similarity+=st1.getAction(i+1).similarity(st2.getAction(i+1));}
				}
				
				return similarity-(similarity*penalty/10)/Math.min(sizeSt1, sizeSt2);
	}

}
