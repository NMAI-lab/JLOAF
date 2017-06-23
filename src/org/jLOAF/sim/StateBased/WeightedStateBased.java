package org.jLOAF.sim.StateBased;

import java.util.HashMap;

import org.jLOAF.inputs.Input;
import org.jLOAF.inputs.StateBasedInput;
import org.jLOAF.sim.StateBasedSimilarity;
/*
 * this class represents a similarity Metric that compares stateBasedInputs, given that the most important features are most recent ones in their
 * traces
 * @authore Ibrahim Ali Fawaz
 */
public class WeightedStateBased extends StateBasedSimilarity 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final int weight=10;
	

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
				
				double similarity = 0;
				int count=0;
				int multiplyer;
				for(int i=0;i<Math.min(sizeSt1, sizeSt2);i=i+2){
					multiplyer=weight/i+1;
					if(multiplyer==0){
						multiplyer=1;
					}
					count+=multiplyer;
					similarity+=st1.getInput(i).similarity(st2.getInput(i))*multiplyer;
					if(i+1 !=Math.min(sizeSt1, sizeSt2)){
						
						similarity+=st1.getAction(i+1).similarity(st2.getAction(i+1))*multiplyer;}
				}
				
				return similarity/count;
	}
}
