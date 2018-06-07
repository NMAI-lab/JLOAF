package org.jLOAF.action;

import java.util.ArrayList;
import java.util.List;

import org.jLOAF.inputs.Feature;
/**
 * an Atomic Action is the Atomic level of an Action, it can only take one feature, and has a name, usually the name represents the action itself.
 * usually atomic actions are within complexActions.
 */
public class AtomicAction extends Action {

	private static final long serialVersionUID = 1L;

	protected Feature feat;
	
	
	/**
	 * Constructor that constructs the atomic Action ny given it a name.
	 * @param name the name of the action, or the action itself.
	 */
	public AtomicAction(String name) {
		super(name);
		
	}
	/**
	 * sets the feature of the action
	 * @param f the feature of the action.
	 */
	public void setFeature(Feature f){
		feat= f;
	}
	/**
	 * returns the feature of the action
	 * @return the feature of the action
	 */
	public Feature getFeature(){
		return feat;
	}
	
	
	
	
	
	/**
	 * returns the similarity between two actions, by first using the similarity method of the parent class,then if the names were the same, it checks
	 * their features.
	 * @param the action to be compared to this action.
	 * @return the simialiry between two actions
	 * @see org.jLOAF.action.Action#similarity(org.jLOAF.action.Action)
	 */
	public double similarity(Action action) {
		
		AtomicAction action1 = (AtomicAction)action;
		
		double v=super.similarity(action);
		
		if(v==0){
			return v;
		}
		
			if(feat!=null && action1.getFeature()!=null){
			double x=feat.getValue();
			double x1=action1.getFeature().getValue();
		
			
			if((x==0.0 && x1==0.0) || x==x1*-1){
				return 0;
			}
			v-=Math.abs(x-x1)/((x+x1)/2);
			
			
			}
		
		
		return v;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
	        return false;
	    }
		AtomicAction a = (AtomicAction) obj;
		return (this.name.equals(a.name));
	}
	
	
	
}

