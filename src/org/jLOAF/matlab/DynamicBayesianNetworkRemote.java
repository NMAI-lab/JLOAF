package org.jLOAF.matlab;

import java.util.List;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;

/**
 * @author sachagunaratne
 * 
 * This class creates a DBN in matlab and trains it using the provided trace
 * It also provides functionality to perform inference given an input by querying the model in matlab 
 * 
 * @since June 2017
 * ***/

public class DynamicBayesianNetworkRemote {
	
	public static MatlabProxy proxy = null;    
	static int STATES = 4;
	/**
	 * This function proxyes Matlab and calls learnLfODBNContinuousGMM() which creates and trains a DBN using the provided trace data.
	 * @param trace the name of the tracefile
	 * @param a_XSIZE the number of features
	 * @param Em_iter the number of EM iterations to run before terminating.
	 */
	public DynamicBayesianNetworkRemote(String trace, int a_XSIZE, int Em_iter){
		
		String matlabCommand = "[dbn,bnetengine] = learnLfODBNContinuousGMM([";
		matlabCommand+="'" + trace + "';";
		matlabCommand+="], " + Em_iter + " ," + STATES + "," + a_XSIZE + "," + 1 + ");"; //changed 20 size of array to 10 to reflect the change in perception size

		System.out.println(matlabCommand);

		MatlabProxyFactoryOptions options = null;
		MatlabProxyFactory factory = null;
		if (proxy==null) {
			options = new MatlabProxyFactoryOptions.Builder().setUsePreviouslyControlledSession(true).build();
			factory = new MatlabProxyFactory(options);
		}
		try {
			if (proxy==null) proxy = factory.getProxy();
			proxy.eval(matlabCommand);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/***
	 * get the initial state value based on the given input
	 * 
	 * @param list of input values
	 * 
	 * @return an integer state value
	 * 
	 * @author sachagunaratne
	 * ***/
	public int getInitialState(List<Double> x) {
		try {
			proxy.eval("initialState = getInitialState(" + x + ", dbn);");
			double [] s = (double []) proxy.returningEval("initialState", 1)[0];
			return (int) s[0];
			
		}catch (MatlabInvocationException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	/***
	 * get the new state value based on the given input and the previous state
	 * 
	 * @param list of input values, integer past state
	 * 
	 * @return an integer state value
	 * @author sachagunaratne
	 * ***/
	public int getNewState(List<Double> x, int action, int state) {
		try {
			action +=1;
			proxy.eval("newState = getNewState("+x+","+state+","+action+",dbn);");
			double[] s = (double[]) proxy.returningEval("newState", 1)[0];	
			return (int) s[0];
		}catch (MatlabInvocationException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	/***
	 * get the new state value based on the given input and the previous state
	 * 
	 * @param list of input which contain the input as well as the current state
	 * 
	 * @return an integer action value
	 * @author sachagunaratne
	 * ***/
	public int getAction(List<Double> input) {
		try {
			proxy.eval("action = getAction("+input+",dbn);");
			double[] s = (double[]) proxy.returningEval("action", 1)[0];	
			return (int) s[0];
		}catch (MatlabInvocationException ex) {
			ex.printStackTrace();
			return 0;
		}
	}
}
