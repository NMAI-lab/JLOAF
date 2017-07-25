package tests.junit.preprocessing.filter.casebasefilter;

import static org.junit.Assert.assertEquals;

import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.preprocessing.filter.casebasefilter.Clustering;
import org.jLOAF.preprocessing.filter.casebasefilter.FullClustering;
import org.jLOAF.sim.ComplexSimilarityMetricStrategy;
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.sim.StateBasedSimilarity;
import org.jLOAF.sim.StateBased.KOrderedSimilarity;
import org.jLOAF.sim.atomic.EuclideanDistance;
import org.junit.Before;
import org.junit.Test;
/*
 * class to test the clustering preprocessing 
 */
public class ClusteringTest {
	private AtomicInput i1,i2,i3,i4,i5;
	private Case c1,c2,c3,c4,c5;
	private Action a;
	private Clustering clustering;
	private SimilarityMetricStrategy  sim;
	private CaseBase cb;
	
	@Before
	public void initializing(){
		sim = new EuclideanDistance();
		i1 = new AtomicInput("A",new Feature(10.0),sim);
		i2=new AtomicInput("A",new Feature(10.0),sim);
		i3=new AtomicInput("A",new Feature(10.0),sim);
		i4=new AtomicInput("A",new Feature(10.0),sim);
		i5=new AtomicInput("A",new Feature(10.0),sim);
		a= new Action("hello");
		cb=new CaseBase();
		StateBasedSimilarity sim1 = new  KOrderedSimilarity(1);
		cb.createThenAdd(i1, a,sim1 );
		cb.createThenAdd(i2, a,sim1 );
		cb.createThenAdd(i3, a,sim1 );
		cb.createThenAdd(i4, a,sim1 );
		cb.createThenAdd(i5, a,sim1 );
		clustering = new FullClustering(null);
		}
	/*
	 * in this test we are going to pass five cases that have the exact same inputs to a clustering filter
	 * the resulting casebase should be of size one, because
	 * it must remove the redundant cases.
	 * --------------------------------------------------------------------------
	 * the way this works:
	 * Step1:you pass a casebase to the filter, and it get the list of cases it has.
	 * Step2: loop through the cases, if its the first case of the casebase, create a new Cluster and add the case to it.
	 * keep looping through the cases, comparing each case with the first case of the casebase
	 * if the simlarity between those cases is high, add the comparedCase,which is still in the list to the cluster of the first case
	 * if the similarity is low, create a new cluster, and add the case in the list to it.
	 * then compare the remaining cases to the cases of the cluster, and do the same thing.
	 * Step 3: clear the original casebase, and add one case of each cluster to the casebase.
	 * Step 4: return the new casebase
	 * ----------------------------------------------------------------------------
	 * 
	 * @author Ibrahim Ali Fawaz
	 */
	@Test
	public void TestAllCasesSimilar(){
		cb= clustering.filter(cb);
		assertEquals(cb.getSize(),1);
		
	}
	
	/*
	 * in this test we are going to have the initial five case that have the exact same inputs with an additional different case to a clustering filter
	 * the resulting casebase should be of size two, because
	 * it must remove the redundant cases.
	 * @author Ibrahim Ali Fawaz
	 */
	@Test
	public void TestDifferentCases(){
		cb.createThenAdd(new AtomicInput("A",new Feature(30.0),sim),a,new KOrderedSimilarity(1));
		cb= clustering.filter(cb);
		assertEquals(cb.getSize(),2);
		
	}
	
	
	
	
	

	
}
