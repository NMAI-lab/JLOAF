package org.jLOAF.matlab;

import java.util.LinkedList;
import java.util.List;

import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;
/**
 * 
 * @author sachagunaratne
 *
 */
public class BayesianNetworkRemote {

	static int nextIndex = 1;
	double placeholder = 6.6;

	int index = 0;
	int XSIZE = 8, YSIZE = 1;
	public static MatlabProxy proxy = null;

	/**
	 * Proxys Matlab and creates a Bnet in Matlab and trains it on the provided run information. 
	 * It calls the Matlab function learnBNetGMM()
	 * @param trace the name of the tracefile
	 * @param a_XSIZE the number of features
	 * @param a_YSIZE the number of action nodes - in this case is 1 always
	 * @author Santi Ontanon & sacha gunaratne
	 * 
	 */
	public BayesianNetworkRemote(String trace, int a_XSIZE, int a_YSIZE) {
		index = nextIndex++;
		XSIZE = a_XSIZE;
		YSIZE = a_YSIZE;
		String matlabCommand = "[bnet" + index + ",bnetengine" + index + "] = learnBNetGMMPartialObserve([";
		matlabCommand+="'" + trace + "';";

		matlabCommand+="]," + XSIZE + "," + YSIZE + ");";

		//System.out.println(matlabCommand);

		MatlabProxyFactoryOptions options = null;
		MatlabProxyFactory factory = null;
		if (proxy==null) {
			options = new MatlabProxyFactoryOptions.Builder().setUsePreviouslyControlledSession(true).build();
			factory = new MatlabProxyFactory(options);
		}
		try {
			if (proxy==null) proxy = factory.getProxy();
			//proxy.eval(matlabCommand);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method calculates the probabilities of each class given some input and a trained model
	 * @param input a list of features as doubles (the current environmental perception)
	 * @return the probability of each class as a list
	 * @author Santi Ontanon and sachagunaratne
	 */
	public List<Double> run(List<Double> input) {                
		double []ret;
		//	        System.out.println("BNetRemove.run( " + input + " )");
		try {
			proxy.eval("evidence = cell(1," + (XSIZE + YSIZE) + ");");

			for(int i = 0;i<input.size();i++) {
				if(input.get(i)!=placeholder){
					proxy.eval("evidence{" + (i+1) + "} = " + input.get(i) + ";");
				}
			}
			proxy.eval("[tmpeng, tmpll] = enter_evidence(bnetengine" + index + ", evidence);");
			proxy.eval("tmp = [];\n");
			for(int i = 0;i<YSIZE;i++) {
				proxy.eval("marg1 = marginal_nodes(tmpeng, " + (XSIZE+i+1) + ");");
				proxy.eval("tmp = [tmp;marg1.T];");
			}
			ret = (double [])(proxy.returningEval("tmp",1)[0]);
			List<Double> output = new LinkedList<Double>();
			for(double d:ret) {
				output.add(d);
			}
			return output;
		} catch (MatlabInvocationException ex) {
			ex.printStackTrace();
			return null;
		}        
	}


}
