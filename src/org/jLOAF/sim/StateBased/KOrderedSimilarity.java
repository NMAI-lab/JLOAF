package org.jLOAF.sim.StateBased;

import org.jLOAF.inputs.Input;
import org.jLOAF.inputs.StateBasedInput;
import org.jLOAF.sim.StateBasedSimilarity;
/*
 * this class represents a similarity metric that compares the traces until it reaches k-steps to the back
 *@author Ibrahim Ali Fawaz
 */
public class KOrderedSimilarity extends StateBasedSimilarity  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		//k is how many steps you want to go back in the trace
		private int k;
		public KOrderedSimilarity(int k){
			this.k=k;
		}

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
				int penalty =Math.abs(k-sizeSt2);
				if(penalty > 5){
					return 0;
				}
				double similarity = 0;
				for(int i=0;i<k;i=i+2){
					if(i==sizeSt1 ||i==sizeSt2){
						break;
					}
					similarity+=st1.getInput(i).similarity(st2.getInput(i));
					if(i+1 <Math.min(sizeSt1, sizeSt2) && i+1 <k){
						
						similarity+=st1.getAction(i+1).similarity(st2.getAction(i+1));}
				}
				
				return similarity/k;
	}

}
