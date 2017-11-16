package org.jLOAF.sim.StateBased;


import org.jLOAF.inputs.Input;
import org.jLOAF.inputs.StateBasedInput;
import org.jLOAF.sim.StateBasedSimilarity;
/**
 * this class represents a similarity metric that calculates the similarity between two stateBasedInputs based on the whole trace,in order.
 * and if the difference between the two traces is more than 5, in size, the similarity is zero, other wise there is a penalty for the smaller differences.
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
				int sizeSt2 = st2.getSize();
				
				double similarity = 0;
				
				for(int i=0;i<Math.min(sizeSt1, sizeSt2);i++){
					similarity+=st1.getElement(i).similarity(st2.getElement(i));
				}
				return similarity/(Math.max(sizeSt1, sizeSt2));
	}

}
