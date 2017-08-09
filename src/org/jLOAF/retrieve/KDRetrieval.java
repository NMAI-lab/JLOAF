package org.jLOAF.retrieve;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.jLOAF.casebase.Case;
import org.jLOAF.inputs.Input;
import org.jLOAF.preprocessing.filter.casebasefilter.BucketNode;
import org.jLOAF.preprocessing.filter.casebasefilter.KDTree;

public class KDRetrieval implements Retrieval{

	private KDTree tree ;
	private BucketNode bestNode;
	private double maxSim=-1;
	private HashSet<BucketNode> visitedList;
	
	public KDRetrieval(KDTree tree){
		this.tree=tree;
		visitedList = new HashSet<BucketNode>();
	}
	
	
	@Override
	public List<Case> retrieve(Input i) {
		BucketNode root = tree.getRoot();
		
		BucketNode leafNode=goToLeaf(root,i);
		bestNode =leafNode;
		visitedList.add(bestNode);
		Case c = returnCase(leafNode.getSibling(),i);
		
		ArrayList<Case> cases= new ArrayList<Case>();
		
		cases.add(c);
	
		return cases;
	
	}

	private Case returnCase(BucketNode leafNode, Input i) {
			visitedList.add(leafNode);
			bestNode = getBigger(bestNode,leafNode,i);
			if(getNext(leafNode)==null){
				return bestNode.getPoint();
				
			}
			
			if(!bestNode.isLeaf()){
				returnCase(expand(bestNode,leafNode),i);
			}
			
			return	returnCase(getNext(leafNode),i);
			
		
		
	}


	private BucketNode expand(BucketNode bestNode2, BucketNode leafNode) {
		if(visitedList.contains(bestNode2.getRight()) && !visitedList.contains(bestNode2.getLeft())){
			return bestNode2.getLeft();
		}else if(!visitedList.contains(bestNode2.getRight()) && visitedList.contains(bestNode2.getLeft())){
			return bestNode2.getRight();
		}else if (visitedList.contains(bestNode2.getRight()) && visitedList.contains(bestNode2.getLeft()) && !visitedList.contains(bestNode2.getParent())){
			return bestNode2.getParent();
		}else {
			return getNext(leafNode);
		}
	
	
	}


	private BucketNode getNext(BucketNode leafNode) {
		if(visitedList.contains(leafNode.getSibling())){
			return leafNode.getParent();
		}
		return leafNode.getSibling();
	}


	private BucketNode getBigger(BucketNode bestNode2, BucketNode leafNode,Input i) {
			String key=getSplitFeature(bestNode2,leafNode);
			double fI=Input.getFeature(i, key);
			double bf =Input.getFeature(bestNode2.getPoint().getInput(), key);
			double bf2=Input.getFeature(leafNode.getPoint().getInput(), key);
			if(Math.abs(fI-bf)>Math.abs(fI-bf2)){
				return leafNode;
			}else{
				return bestNode2;
			}
	
	}


	


	private String getSplitFeature(BucketNode bestNode2, BucketNode leafNode) {
		if(bestNode2.getParent().getDepth()<leafNode.getParent().getDepth()){
			return bestNode2.getParent().getFeature();
		}
		return leafNode.getParent().getFeature();
	}


	private BucketNode goToLeaf(BucketNode root, Input i) {
		if(root.isLeaf()){
			return root;
		}
		if(Input.getFeature(i, root.getFeature())>root.getMedian()){
			root =root.getRight();
		}else {
			root=root.getLeft();
		}
		return goToLeaf(root,i);
		
		
		
	}


	@Override
	public Distance[] getDist() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	
	

}
