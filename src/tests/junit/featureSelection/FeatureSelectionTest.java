package tests.junit.featureSelection;

import org.jLOAF.action.AtomicAction;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.sim.StateBasedSimilarity;
import org.jLOAF.sim.StateBased.KOrderedSimilarity;
import org.jLOAF.sim.atomic.Equality;
import org.jLOAF.sim.complex.Mean;
import org.jLOAF.sim.complex.WeightedMean;
import org.jLOAF.weights.SimilarityWeights;
import org.junit.Before;
/*
 * this class is just for the initializing of the casebase and cases for its subclasses, these subclasses are featureSelection and weightModification classes.
 */
public class FeatureSelectionTest {
	
	protected CaseBase cb;
	protected SimilarityWeights sim2 ;
	
	/*
	 * in the initialization, we create 12 cases, which have four actions equally distributed.
	 * any FeatureSelection Class or weightModification class should be able to distinguish the feature(input) that is most relevent to the output.
	 * in our case in feature "a".
	 * as it's shown below, the user only has to create the inputs and the actions and pass them the createThenAdd method in the casesBase
	 * this method will create a trace, based on the order of the inputs and actions passed to the createThenAdd method.
	 * if the KOrederedSimilarity is used for the stateBasedInput with K equals to one, then the agent will be considered a reactive agent.
	 */
	public void initialize(){
		cb= new CaseBase();
		//let's create 10  cases, and each case has a complex input of two atomic input, where only one feature matter.
		Feature f1 =new Feature(1);
		Feature f2 = new Feature(2);
		Feature f3 = new Feature(3);
		Feature f4= new Feature(4);
		sim2 = new SimilarityWeights();// if you want to use weighted mean strategy, you should always have a similarityWeight class .
		//feature selection only works for weightedMean similarity, since it needs to have weights so it can select or evaluate features.
		SimilarityMetricStrategy sim = new WeightedMean(sim2);
		SimilarityMetricStrategy sim1 = new Mean();
		SimilarityMetricStrategy sim3 = new Equality();
		
		AtomicInput i1 = new AtomicInput("a",f1,sim3);
		AtomicInput i2 = new AtomicInput("a1",f1, sim3);
		//let's assume, when the first atomic Input is f1 the action is play, when it is f2 the action is stay, when it is f3 the action is eat,
		//and the second input is irrelevant to the action.
		AtomicAction a1 =new AtomicAction("play");
		AtomicAction a2 =new AtomicAction("stay");
		AtomicAction a3 =new AtomicAction("eat");
		AtomicInput i3 = new AtomicInput("a",f2,sim3);
		AtomicInput i4 = new AtomicInput("a1",f4, sim3);
		AtomicInput i5 = new AtomicInput("a",f3,sim3);
		AtomicInput i6 = new AtomicInput("a1",f4, sim3);
		AtomicInput i7 = new AtomicInput("a",f1,sim3);
		AtomicInput i8 = new AtomicInput("a1",f4, sim3);
		ComplexInput ci1 = new ComplexInput("a",sim1);
		ci1.add(i1);//since we need to have a complex Input, we are going to wrap the atomic input in the complexInputs,
		//but in this case each Complex Input represents the only atomic Input it contains.
		ComplexInput ci2 = new ComplexInput("a1",sim1);
		ci2.add(i2);
		// and now we need a bigger Complex Input to contain both of those ComplexInputs.
		//and this is the input that takes weightedMean as a similarity.
		//keep in mind that the names have to always be same, because inputs get compared based on their names. i.e (a and a1 should never change)
		ComplexInput b1 = new ComplexInput("b",sim);
		//since in this case we only care about the latest input in the trace,we are going to use the KOrderedSimilarity for state based inputs ,
		//with k =1 .
		b1.add(ci1);
		b1.add(ci2);
		StateBasedSimilarity stm=  new KOrderedSimilarity(1);
		cb.createThenAdd(b1, a1,stm );//here since i1 has f1 as value we say the action is play.
		//now we repeat the above steps.
		
		ComplexInput ci3 = new ComplexInput("a",sim1);
		ComplexInput ci4 = new ComplexInput("a1",sim1);
		ComplexInput ci5 = new ComplexInput("a",sim1);
		ComplexInput ci6 = new ComplexInput("a1",sim1);
		ComplexInput ci7 = new ComplexInput("a",sim1);
		ComplexInput ci8 = new ComplexInput("a1",sim1);
		ComplexInput ci9 = new ComplexInput("a",sim1);
		ComplexInput ci10 = new ComplexInput("a1",sim1);
		ComplexInput ci11 = new ComplexInput("a",sim1);
		ComplexInput ci12 = new ComplexInput("a1",sim1);
		ComplexInput ci13 = new ComplexInput("a",sim1);
		ComplexInput ci14 = new ComplexInput("a1",sim1);
		ComplexInput ci15 = new ComplexInput("a1",sim1);
		ComplexInput ci16 = new ComplexInput("a",sim1);
		ComplexInput ci17 = new ComplexInput("a1",sim1);
		ComplexInput ci18 = new ComplexInput("a",sim1);
		ComplexInput ci19 = new ComplexInput("a1",sim1);
		ComplexInput ci20 = new ComplexInput("a1",sim1);
		ComplexInput ci21 = new ComplexInput("a",sim1);
		ComplexInput ci22 = new ComplexInput("a1",sim1);
		ComplexInput ci23 = new ComplexInput("a",sim1);
		ComplexInput ci24 = new ComplexInput("a1",sim1);
		ComplexInput ci25 = new ComplexInput("a",sim1);
		ComplexInput ci26 = new ComplexInput("a1",sim1);
		ci3.add(i3);
		ci4.add(i4);
		ci5.add(i5);
		ci6.add(i6);
		ci7.add(i7);
		ci8.add(i8);
		ci9.add(i3);
		ci10.add(i4);
		ci11.add(i3);
		ci12.add(i4);
		ci13.add(i3);
		ci14.add(i4);
		ci15.add(i5);
		ci16.add(i4);
		ci17.add(i5);
		ci18.add(i4);
		ci19.add(i5);
		ci20.add(i4);
		ci21.add(i7);
		ci22.add(i4);
		ci23.add(i7);
		ci24.add(i4);
		ci25.add(i7);
		ci26.add(i4);
		ComplexInput b2 = new ComplexInput("b",sim);
		b2.add(ci3);
		b2.add(ci4);
		ComplexInput b3 = new ComplexInput("b",sim);
		b3.add(ci5);
		b3.add(ci6);
		ComplexInput b4 = new ComplexInput("b",sim);
		b4.add(ci7);
		b4.add(ci8);
		ComplexInput b5 = new ComplexInput("b",sim);
		b5.add(ci9);
		b5.add(ci10);
		ComplexInput b6 = new ComplexInput("b",sim);
		b6.add(ci11);
		b6.add(ci12);
		ComplexInput b7 = new ComplexInput("b",sim);
		b7.add(ci13);
		b7.add(ci14);
		ComplexInput b8 = new ComplexInput("b",sim);
		b8.add(ci15);
		b8.add(ci16);
		ComplexInput b9 = new ComplexInput("b",sim);
		b9.add(ci17);
		b9.add(ci18);
		ComplexInput b10 = new ComplexInput("b",sim);
		b10.add(ci19);
		b10.add(ci20);
		ComplexInput b11 = new ComplexInput("b",sim);
		b11.add(ci21);
		b11.add(ci22);
		ComplexInput b12 = new ComplexInput("b",sim);
		b12.add(ci23);
		b12.add(ci24);
		ComplexInput b13 = new ComplexInput("b",sim);
		b13.add(ci25);
		b13.add(ci26);
		
		cb.createThenAdd(b2, a2, stm);//the action here is stay since the relevant input has a feature f2.
		cb.createThenAdd(b3, a3, stm);//the action here is eat since the relevant input has a feature f3.
		cb.createThenAdd(b4, a1, stm);//here since i7 has f1 as value we say the action is play.
		cb.createThenAdd(b5, a2, stm);//the action here is stay since the relevant input has a feature f2.
		cb.createThenAdd(b6, a2, stm);//the action here is stay since the relevant input has a feature f2.
		cb.createThenAdd(b7, a2, stm);//the action here is stay since the relevant input has a feature f2.
		cb.createThenAdd(b8, a3, stm);//the action here is eat since the relevant input has a feature f3
		cb.createThenAdd(b9, a3, stm);//the action here is eat since the relevant input has a feature f3
		cb.createThenAdd(b10, a3, stm);//the action here is eat since the relevant input has a feature f3
		cb.createThenAdd(b11, a1, stm);
		cb.createThenAdd(b12, a1, stm);
		cb.createThenAdd(b13, a1, stm); 
		
	}

}
