package org.jLOAF.action;

import java.util.ArrayList;
import java.util.List;

import org.jLOAF.inputs.Feature;

public class AtomicAction extends Action {

	private static final long serialVersionUID = 1L;

	protected Feature feat;
	
	

	public AtomicAction(String name) {
		super(name);
		
	}

	public void setFeature(Feature f){
		feat= f;
	}
	public Feature getFeature(){
		return feat;
	}
	
	
	
	
	
	
	public double similarity(Action action) {
		
		AtomicAction action1 = (AtomicAction)action;
		
		double v=super.similarity(action);
		
		if(v==0){
			return v;
		}
		
			if(feat!=null && action1.getFeature()!=null){
			double x=feat.getValue();
			double x1=action1.getFeature().getValue();
		
			
			v-=Math.abs(x-x1)/((x+x1)/2);
			}
		
		
		return v;
	}
	}

