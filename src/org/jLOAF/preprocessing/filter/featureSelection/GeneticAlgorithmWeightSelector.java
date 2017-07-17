package org.jLOAF.preprocessing.filter.featureSelection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.weights.SimilarityWeights;
import org.jLOAF.weights.Weights;
/**
 * This method will take a casebase that has some similarityWeights for every feature and determine the best weights for each feature.
 * The filter will return a casebase with updated weights
 * @author sachagunaratne
 * @since 2017 June
 * 
 * ***/
public class GeneticAlgorithmWeightSelector extends FeatureSelection {
	Random r;
	private int numberOfPopulation=4;
	public GeneticAlgorithmWeightSelector(CaseBaseFilter fs) {
		super(fs);
		r = new Random();
	}
	
	private void mutate(FeatureNode node) {
		Random r = new Random();
		double p = r.nextDouble();
		double c=0.5;
		int i=0;
		for(String w:node.getWeights().getWeightedItems()){
			if(p>0.8){
				if(i%2==0){
					((SimilarityWeights)node.getWeights()).setFeatureWeight(w,node.getWeights().getWeight(w)+c*10);
				}else{
					double check = node.getWeights().getWeight(w)-c*10;
					if(check>0){
						((SimilarityWeights)node.getWeights()).setFeatureWeight(w,check);
					}
					
				}
			}
			
			p=r.nextDouble();
			i++;
		}
		
		
	}

	

	

	@Override
	protected FeatureNode filterFeatures(Weights allIn) {
		FeatureNode initialNode = new FeatureNode();
		
		initialNode.setWeights(allIn);
		//create a list of nodes which will hold the weights
		ArrayList<FeatureNode> nodes= new ArrayList<FeatureNode>();
		populate(nodes,numberOfPopulation,allIn);
		
		double old_accuracy = 0;
		double new_accuracy = 1;
		evaluate(initialNode);
		
		new_accuracy =initialNode.getEvaluateNumber();
		
		while((new_accuracy-old_accuracy)>0.001){
			old_accuracy = new_accuracy;
			
			//calculate fitness - step 2 GA - selection
			//---------------------------------------------------------
				for(int j=0;j<nodes.size();j++){
					evaluate(nodes.get(j));
				}
				
				//create reproduction pool - step 2 GA - selection
				//--------------------------------------------------------
				Collections.sort(nodes);//now the first two nodes are the best ones.
				FeatureNode best = nodes.get(0);
				FeatureNode best_2 =nodes.get(1);
				new_accuracy = best.getEvaluateNumber();
				initialNode=best;
		    //cross produce - step 3 GA - mating
		    //----------------------------------------------------------------------------
				nodes.clear();
				nodes.add(mergeMean(best,best_2));
				nodes.add(crossProduce(best,best_2));
				nodes.add(crossProduce(best_2,best));
			//mutate - step 3 GA - mutation
		    //---------------------------------------------------------------------------
				for(int j=0;j<nodes.size();j++){
					mutate(nodes.get(j));
				}
		}
		return initialNode;
		
	}

	
	
	
	
	private FeatureNode crossProduce(FeatureNode best, FeatureNode best_2) {
			int size = best.getWeights().getWeightedItems().size();
		SimilarityWeights sim = new SimilarityWeights();
			int i=0;
		for(String w:best.getWeights().getWeightedItems()){
			if(i<size/2){
				sim.setFeatureWeight(w, best.getWeights().getWeight(w));
			}else {
				sim.setFeatureWeight(w, best_2.getWeights().getWeight(w));
			}
			i++;
		}
		FeatureNode newNode = new FeatureNode();
		newNode.setWeights(sim);
		return newNode;
	}

	private FeatureNode mergeMean(FeatureNode best, FeatureNode best_2) {
		SimilarityWeights sim = new SimilarityWeights();
		for(String w : best.getWeights().getWeightedItems()){
			sim.setFeatureWeight(w, (best.getWeights().getWeight(w)+best_2.getWeights().getWeight(w))/2);
		}
		FeatureNode newNode = new FeatureNode();
		newNode.setWeights(sim);
		return newNode;
	}
	

	private void populate(ArrayList<FeatureNode> nodes, int numberOfPopulation2,Weights allIn) {
		for(int i=0;i<numberOfPopulation2;i++){
			FeatureNode newNode = new FeatureNode();
			newNode.randomizeWeights(allIn);
			nodes.add(newNode);
		}
		
	}
	
	

}
