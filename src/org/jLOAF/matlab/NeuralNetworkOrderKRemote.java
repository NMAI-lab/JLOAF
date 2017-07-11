package org.jLOAF.matlab;

import java.util.LinkedList;
import java.util.List;

import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;

public class NeuralNetworkOrderKRemote {
	
	 	static int nextIndex = 1;
	    
	    int index = 0;
	    int XSIZE = 3, YSIZE = 3, ORDER = 2;
	    public static MatlabProxy proxy = null;
	    List<List<Double>> lastInputs = new LinkedList<List<Double>>();
	    List<Integer> lastOutputs = new LinkedList<Integer>();
	    
	public NeuralNetworkOrderKRemote(String trace, int a_XSIZE, int a_YSIZE, int a_ORDER) {
		 	index = nextIndex++;
	        XSIZE = a_XSIZE;
	        ORDER = a_ORDER;
	        YSIZE = a_YSIZE;
	        String matlabCommand = "nnet" + index + " = learnNNOrderK([";
	        matlabCommand+="'" + trace + "';";
	        matlabCommand+="]," + XSIZE + "," + YSIZE + "," + ORDER + ",0);";

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

	public int run(List<Double> input) {
		 double []ret;
	        
	        String matlabCommand = "nnet" + index + "([";;
	        for(int i = 0;i<ORDER-1;i++) {
	            if (lastInputs.size()>i) {
	                for(int j = 0;j<XSIZE;j++) 
	                    matlabCommand+= lastInputs.get(i).get(j) + ";";
	                for(int j = 0;j<YSIZE;j++) {
	                    if (lastOutputs.get(i) == j) {
	                        matlabCommand+= 1 + ";";
	                    } else {
	                        matlabCommand+= 0 + ";";
	                    }
	                }
	            } else {
	                for(int j = 0;j<XSIZE;j++) 
	                    matlabCommand+= input.get(j) + ";";
	                for(int j = 0;j<YSIZE;j++) 
	                    matlabCommand+= 0 + ";";
	            }

	        }
	        for(int i = 0;i<XSIZE;i++) {
	            matlabCommand+= input.get(i) + ";";
	        }
	        matlabCommand+="])";   
	        
//	        System.out.println(matlabCommand);
	        
	        try {
	            ret = (double [])(proxy.returningEval(matlabCommand,1)[0]);
	            List<Double> output = new LinkedList<Double>();
	            for(double d:ret) {
	                output.add(Math.max(0,d));
	            }
	            
	            int max = 0;
	    		for(int ii = 0;ii<output.size();ii++) {
	    			if (output.get(ii)>output.get(max)) max = ii;
	    		}
	    		
	            int action = max;
	            
	            lastInputs.add(0,input);            
	            lastOutputs.add(0,action);
	            
	            return action;
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            return 0;
	        }        
	}

	public void replaceLastAction(int a) {
		 lastOutputs.remove(0);
	     lastOutputs.add(0,a);
	}

}
