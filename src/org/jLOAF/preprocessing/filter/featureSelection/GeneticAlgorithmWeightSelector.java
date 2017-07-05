package org.jLOAF.preprocessing.filter.featureSelection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.IntStream;

import org.jLOAF.Agent;
import org.jLOAF.agents.GenericAgent;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.performance.Statistics;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.sim.complex.WeightedMean;
import org.jLOAF.weights.SimilarityWeights;
/**
 * This method will take a casebase that has some similarityWeights for every feature and determine the best weights for each feature.
 * The filter will return a casebase with updated weights
 * @author sachagunaratne
 * @since 2017 June
 * 
 * ***/
public class GeneticAlgorithmWeightSelector extends CaseBaseFilter {
	Random r;
	public GeneticAlgorithmWeightSelector(CaseBaseFilter f) {
		super(f);
		r = new Random();
	}

	@Override
	public CaseBase filter(CaseBase initial) {
		Case first = (Case)initial.getCases().toArray()[0];
		
		ComplexInput i = null; 
		try{
			i = (ComplexInput)first.getInput();
		}catch(ClassCastException e){
			System.out.println(e);
			return initial;
		}
		
		if(i.getSimilarityMetricStrategy() instanceof WeightedMean){
			SimilarityWeights sim =((WeightedMean)i.getSimilarityMetricStrategy()).getSimilarityWeights();
			
			//set similarityfeatures randomly - step 1 GA - initialization
			//-------------------------------------------------------------------------
			int len = sim.getWeightedItems().size();
			//create a list of lists which will hold the initial weights
			ArrayList<ArrayList<Double>> listOfWeights = new ArrayList<ArrayList<Double>>();
			ArrayList<Double> weights1 = new ArrayList<Double>();
			ArrayList<Double> weights2 = new ArrayList<Double>();
			ArrayList<Double> weights3 = new ArrayList<Double>();
			ArrayList<Double> weights4 = new ArrayList<Double>();
			
			//contains the fitness for each weight
			ArrayList<Float> fitness = new ArrayList<Float>();
			
			//initialize the weights for each list
			for(int ii=0;ii<len;ii++){
				weights1.add(r.nextDouble());
				weights2.add(r.nextDouble());
				weights3.add(r.nextDouble());
				weights4.add(r.nextDouble());
			}
			//add the weights to the list of lists
			listOfWeights.add(weights1);
			listOfWeights.add(weights2);
			listOfWeights.add(weights3);
			listOfWeights.add(weights4);

			double old_accuracy = 0;
			double new_accuracy = 1;
			while((new_accuracy-old_accuracy)<1e-4){
				old_accuracy = new_accuracy;
				
				
				
				Agent a = new GenericAgent();
				Statistics st = new Statistics(a);
				
				//calculate fitness - step 2 GA - selection
				//---------------------------------------------------------
				for(ArrayList<Double> weight: listOfWeights){
					//set the simFeatures for each set of weights
					int index = 0;
					for(String w:i.getChildNames()){
						sim.setFeatureWeight(w, weight.get(index));
					}
					//train on new sim feature weights
					a.train(initial);
					
					//test the prediction capability
					for(Case test: initial.getCases()){
						st.predictedCorrectActionName(test);
					}
					//add accuracy to fitness function
					fitness.add(st.getClassificationAccuracy());
					
				}
				
				//create reproduction pool - step 2 GA - selection
				//--------------------------------------------------------
				
				//choose the two best weights using the fitness function
				
				//choose the best two fitness functions
				Float[] temp = new Float[fitness.size()];
				temp = fitness.toArray(temp);
				Arrays.sort(temp);  
			    int [] indexes = new int [temp.length];
			    indexes[0]=fitness.indexOf(temp[0]);
			    indexes[1]=fitness.indexOf(temp[1]);//this should be generic, but for now let's make it like this
			    
			    new_accuracy =fitness.get(indexes[0]);
			    ArrayList<ArrayList<Double>> mating_pool =  new ArrayList<ArrayList<Double>>();
			    //add two of the best weight lists to the mating pool
			    for(int counter1=0;counter1<2;counter1++){
			    	mating_pool.add(listOfWeights.get(indexes[counter1]));
			    }
			    
			    //cross produce - step 3 GA - mating
			    //----------------------------------------------------------------------------
			    listOfWeights.clear();
			    weights1.clear();
			    for(int j=0;j<mating_pool.get(0).size();j++){
			    	weights1.add((mating_pool.get(0).get(j)+mating_pool.get(1).get(j))/2);
			    }
			    listOfWeights.add(crossProduce(mating_pool,0,1));
			    listOfWeights.add(crossProduce(mating_pool,1,0));
				
				//mutate - step 3 GA - mutation
			    //---------------------------------------------------------------------------
			    for(int j=0;j<listOfWeights.size();j++){
			    	mutate(listOfWeights.get(j));
			    }
			}
		}
		return initial;
	}
	
	private void mutate(ArrayList<Double> arrayList) {
		Random r = new Random();
		double p = r.nextDouble();
		double c=0.5;
		for(int i=0;i<arrayList.size();i++){
			if(p>0.8){
				if(i%2==0){
				arrayList.set(i,arrayList.get(i)+c);
				
				}else {
					if(arrayList.get(i)-c>0){
					arrayList.set(i,arrayList.get(i)-c);
					}
				}
				
			
			}
		}
		
		
		
	}

	private ArrayList<Double> crossProduce(ArrayList<ArrayList<Double>> arrayList, int i,int j) {
		int size=arrayList.get(0).size();
		Double[] temp = new Double[size];
		for(int k=0;k<arrayList.get(0).size()/2;k++){
			temp[k] =arrayList.get(i).get(k);
			temp[size-k-1]=arrayList.get(j).get(size-k-1);
		}
		return (ArrayList<Double>) Arrays.asList(temp);
	}

	public static void main(String[] args){
		HashMap<Integer,Integer> test = new HashMap<Integer,Integer>();
		test.put(2, 3);test.put(4, 1);test.put(5, 2);test.put(1,4);
		for(Integer w:test.keySet()){
		System.out.println(0%4);
		break;
		}
		for(Integer w:test.keySet()){
			System.out.println(w);
			break;
			}
		
		Double c;
		ArrayList<Double> a = new ArrayList<Double>();
		a.add(23.4);
		c=a.get(0);
		
		System.out.println(a.get(0));
	}

}
