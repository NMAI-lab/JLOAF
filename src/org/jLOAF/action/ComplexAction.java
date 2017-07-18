package org.jLOAF.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/*
 * a Complex Action is a higher level of the action class, it has a list of Action, which they could be either atomic or complexAction.
 */
public class ComplexAction extends Action {

	private static final long serialVersionUID = 1L;
	
	private Map<String,Action> collect;
	/*
	 * Constructor
	 * @param name the name of the action.
	 */
	public ComplexAction(String name) {
		super(name);
		collect = new HashMap<String,Action>();
	}
	/*
	 * adds an action, that is either complex or atomic, to the collection of action that this action has
	 * @param a an action to be added to the collection of actions that this action has
	 */
	public void add(Action a){
		collect.put(a.name, a);
	}
	/*
	 * returns the name of the action
	 * @return the name of the action
	 */
	public Action get(String name){
		return collect.get(name);
	}
	/*
	 * returns a set of the names of the actions that this action has.
	 * @return a set of the names of the actions that this action has
	 */
	public Set<String> getChildNames(){
		return collect.keySet();
	}
	/*
	 * goes through the list of actions that it has, and it compares them using their own similarities.
	 * @param a the action to be compared to this action
	 * @return the similarity between two actions.
	 * @see org.jLOAF.action.Action#similarity(org.jLOAF.action.Action)
	 */
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
