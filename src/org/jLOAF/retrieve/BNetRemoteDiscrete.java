package org.jLOAF.retrieve;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import matlabcontrol.*;

/**
 *
 * @author santi
 */
public class BNetRemoteDiscrete {
    static int nextIndex = 1;
    
    int index = 0;
    int XSIZE = 8, YSIZE = 1;
    public static MatlabProxy proxy = null;
    
    public BNetRemoteDiscrete(List<String> traces, int a_XSIZE, int a_YSIZE) {
        index = nextIndex++;
        XSIZE = a_XSIZE;
        YSIZE = a_YSIZE;
        String matlabCommand = "[bnet" + index + ",bnetengine" + index + "] = learnBNet([";;
        for(String trace:traces) {
             matlabCommand+="'" + trace + "';";
        }
        matlabCommand+="]," + XSIZE + "," + YSIZE + ");";

//        System.out.println(matlabCommand);
        
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
    
    
    public List<Double> run(List<Double> input) {                
        double []ret;
//        System.out.println("BNetRemove.run( " + input + " )");
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

    
    public double[] run(double[] input) {
        List<Double> l = new LinkedList<Double>();
        for(double d:input) l.add(d);
        
        List<Double> lr = run(l);
        double []output = new double[lr.size()];
        for(int i = 0;i<lr.size();i++) output[i] = lr.get(i);
        return output;
    }

    
    public static void main(String args[]) {
        List<String> traces = new LinkedList<String>();
        traces.add("traces-forcefourraydistance/trace-m0-ForceStraightLineAgent.txt");
        traces.add("traces-forcefourraydistance/trace-m1-ForceStraightLineAgent.txt");
        BNetRemoteDiscrete net = new BNetRemoteDiscrete(traces,10,2);
        
        double []input={1,1,1,1,1,1,1,1,1,1};
        double []output;
        
        output = net.run(input);
        
        for(double o:output) System.out.print(o + " ");
    }
}
