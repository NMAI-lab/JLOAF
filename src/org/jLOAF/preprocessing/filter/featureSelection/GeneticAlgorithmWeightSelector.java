package org.jLOAF.preprocessing.filter.featureSelection;

import java.util.ArrayList;
import java.util.Collection;
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
				Object[] temp = fitness.toArray();
				float [] fitness_array = new float [temp.length];
				for(int lenght=0;lenght<temp.length;lenght++){
					fitness_array[lenght] = (float)temp[lenght];
				}
				Map<Float, Integer> map = new TreeMap<Float, Integer>();
			    for (int i2 = 0; i2 < fitness_array.length; ++i2) {
			        map.put(fitness_array[i2], i2);
			    }
			    Collection<Integer> indices = map.values();
				
			    Object [] indexes1 = indices.toArray(); 
			    int [] indexes = new int [temp.length];
			    for(int lenght=0;lenght<temp.length;lenght++){
			    	indexes[lenght] = (int)indexes1[lenght];
				}
			    
			    
			    ArrayList<ArrayList<Double>> mating_pool =  new ArrayList<ArrayList<Double>>();
			    //add two of the best weight lists to the mating pool
			    for(int counter1=0;counter1<2;counter1++){
			    	mating_pool.add(listOfWeights.get(indexes[counter1]));
			    }
			    
			    //cross produce - step 3 GA - mating
			    //----------------------------------------------------------------------------
			    
				
				//mutate - step 3 GA - mutation
			    //----------------------------------------------------------------------------
			}
		}
		return initial;
	}

}
