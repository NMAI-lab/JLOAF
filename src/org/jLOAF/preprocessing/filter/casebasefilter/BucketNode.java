package org.jLOAF.preprocessing.filter.casebasefilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.Input;
import org.jLOAF.inputs.StateBasedInput;

public class BucketNode {
		private ArrayList<Case> bucket ;
		private Case point;
		private  BucketNode right;
		private BucketNode left;
		private String feature;
		private double median;
		private BucketNode parent;
		private int depth;
		
		public BucketNode(){
			right=null;
			left=null;
			parent=null;
			bucket = new ArrayList<Case>();
		}
	public void setBucket(CaseBase initial) {

		bucket = new ArrayList<Case>(initial.getCases());
		
	}
	
	public void setPoint(Case c){
		point=c;
		setMedian(Input.getFeature(c.getInput(), getFeature()));
	}
	public Case getPoint(){
		return point;
	}

	public ArrayList<Case> getBucket() {
		
		return bucket;
	}

	public void addToBucket(Case c) {
		bucket.add(c);
		
	}

	public void setRight(BucketNode rightNode) {
		right =rightNode;
		right.setParent(this);
	}
	
	private void setParent(BucketNode n){
		if(parent!=null){
		parent=n;
		}
	}
	public BucketNode getParent(){
		return parent;
	}
	
	public void setLeft(BucketNode leftNode) {
		left=leftNode;
		left.setParent(this);
	}

	public String getFeature() {
		
		return feature;
	}

	public double getMedian() {
		
		return median;
	}

	public void setFeature(String remove) {
		feature=remove;
		
	}
	public void setMedian(double m){
		median =m;
	} 
	public BucketNode getRight(){
		return right;
	}
	public BucketNode getLeft(){
		return left;
	}
	public boolean isLeaf(){
		return right==null && left ==null;
	}
	public BucketNode getSibling(){
		if(parent.getRight().equals(this)){
			return parent.getLeft();
		}
		return parent.getRight();
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
}
