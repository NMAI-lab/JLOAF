package org.jLOAF.preprocessing.filter.casebasefilter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.Input;
import org.jLOAF.inputs.StateBasedInput;
import org.jLOAF.preprocessing.standardization.Standardization;

public class KDTree {

	private BucketNode root;
	public BucketNode getRoot() {
		return root;
	}

	public void setRoot(BucketNode root) {
		this.root = root;
	}

	private Standardization st ;
	public KDTree(){
		root = null;
		
		st = new Standardization(null);
		
	}
	
	public KDTree createTree(CaseBase initial){
		
		HashMap<String,List<Double>> inputs = st.getFeatures(initial);	
		
		
	
		ArrayList<String> features = new ArrayList(inputs.keySet());
		
		root = new BucketNode();
		root.setBucket(initial);
		root.setDepth(0);
		populateNode(root,features,0);
		
		
		
		
		
		
		return null;
	}

	private void populateNode(BucketNode root2, ArrayList<String> features,int index) {
			if(index == features.size()){
				index=0;
			}
			
			String key = features.get(index);
			root2.setFeature(key);
			root2.getBucket().sort(new Comparator<Case>(){


				@Override
				public int compare(Case o1, Case o2) {
					double fo1=Input.getFeature(o1.getInput(), key);
					double fo2=Input.getFeature(o2.getInput(),key);
					if(fo1>fo2){
						return 1;
					}else if(fo1<fo2){
						return -1;
					}else{
						return 0;
					}
					
				}
				
			});
			if(root2.getBucket().size()==0 ){
				return ;
				
			}
			root2.setPoint(root2.getBucket().remove(root2.getBucket().size()/2));
			
		
		
		
		BucketNode rightNode =new BucketNode();
		BucketNode leftNode =new BucketNode();
		for(Case c:root2.getBucket()){
			if(Input.getFeature(c.getInput(),root2.getFeature())>=root2.getMedian()){
				
				rightNode.addToBucket(c);
				rightNode.setDepth(root2.getDepth()+1);
				root2.setRight(rightNode);
			}else{
				leftNode.addToBucket(c);
				leftNode.setDepth(root2.getDepth()+1);
				root2.setLeft(leftNode);
			}
			
		}
			
			populateNode(rightNode,features,index+1);
			populateNode(leftNode,features,index+1);
	}

	
	

}
