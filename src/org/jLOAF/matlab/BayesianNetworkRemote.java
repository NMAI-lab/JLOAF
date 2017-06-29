package org.jLOAF.matlab;

import java.util.LinkedList;
import java.util.List;

import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;

public class BayesianNetworkRemote {

	 static int nextIndex = 1;
	    
	    int index = 0;
	    int XSIZE = 8, YSIZE = 1;
	    public static MatlabProxy proxy = null;
	    
	    public BayesianNetworkRemote(String trace, int a_XSIZE, int a_YSIZE) {
	        index = nextIndex++;
	        XSIZE = a_XSIZE;
	        YSIZE = a_YSIZE;
	        String matlabCommand = "[bnet" + index + ",bnetengine" + index + "] = learnBNetGMM([";
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
	            proxy.eval(matlabCommand);
	        }catch(Exception e) {
	            e.printStackTrace();
	        }
	    }
	    /***
	     * This method calculates the probabilities of each class given some input and a trained model
	     * 
	     * @param a list of features as doubles (the current environmental perception)
	     * 
	     * @return the proabbility of each class
	     * 
	     * @author Santi Ontanon
	     * ***/
	    public List<Double> run(List<Double> input) {                
	        double []ret;
//	        System.out.println("BNetRemove.run( " + input + " )");
	        try {
	            proxy.eval("evidence = cell(1," + (XSIZE + YSIZE) + ");");
	            
	            for(int i = 0;i<XSIZE;i++) {
	                proxy.eval("evidence{" + (i+1) + "} = " + input.get(i) + ";");
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
