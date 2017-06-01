package org.jLOAF.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ComplexAction extends Action {

	private static final long serialVersionUID = 1L;
	
	private Map<String,Action> collect;
	
	public ComplexAction(String name) {
		super(name);
		collect = new HashMap<String,Action>();
	}

	public void add(Action a){
		collect.put(a.name, a);
	}
	
	public Action get(String name){
		return collect.get(name);
	}
	
	public Set<String> getChildNames(){
		return collect.keySet();
	}
	public double similarity(Action action){
		
		ComplexAction action1 =((ComplexAction)action);
		double v=super.similarity(action);
		if(v==0){
			return v;
		}
		for(String a:collect.keySet()){
			if(action1.get(a)!=null){
			v+=collect.get(a).similarity(action1.get(a));
			}else{
				v-=0.5;
			}
		}
		return v;
	}
	
}
