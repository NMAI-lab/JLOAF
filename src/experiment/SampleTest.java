package experiment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.jLOAF.Reasoning;
import org.jLOAF.action.Action;
import org.jLOAF.action.AtomicAction;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.reasoning.WeightedKNN;
import org.jLOAF.sim.AtomicSimilarityMetricStrategy;
import org.jLOAF.sim.ComplexSimilarityMetricStrategy;
import org.jLOAF.sim.StateBasedSimilarity;
import org.jLOAF.sim.StateBased.KOrderedSimilarity;
import org.jLOAF.sim.atomic.EuclideanDistance;
import org.jLOAF.sim.complex.GreedyMunkrezMatching;
import org.jLOAF.sim.complex.Mean;
import org.jLOAF.sim.complex.WeightedMean;
import org.jLOAF.weights.SimilarityWeights;

public class SampleTest {
	
	
	public static CaseBase reader(String filename) throws IOException{
		
		ComplexInput input;
		ComplexInput ginput;
		ComplexInput binput;
		ComplexInput flaginput;
		ComplexInput flaginput2;
		ComplexInput flags;
		Action action;
		
		AtomicSimilarityMetricStrategy Atomic_strat = new EuclideanDistance();
		ComplexSimilarityMetricStrategy ballGoal_strat = new Mean();
		ComplexSimilarityMetricStrategy flag_strat = new GreedyMunkrezMatching();
		StateBasedSimilarity stateBasedSim = new KOrderedSimilarity(1);	
		SimilarityWeights sim_weights = new SimilarityWeights(); 
		ComplexSimilarityMetricStrategy RoboCup_strat = new WeightedMean(sim_weights);
		
		CaseBase cb = new CaseBase();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			String [] row = null;
			
			
			while((line = br.readLine())!=null){
				row = line.split(",");
				input = new ComplexInput("SenseEnvironment", RoboCup_strat);
				
				if(!row[0].equals("")){
					binput = new ComplexInput("ball",ballGoal_strat);
					Feature ballDist = new Feature(Double.parseDouble(row[0])); 
					Feature ballAngle = new Feature(Double.parseDouble(row[1]));
					binput.add(new AtomicInput("ball_dist", ballDist, Atomic_strat));
					binput.add(new AtomicInput("ball_dir", ballAngle, Atomic_strat));
					input.add(binput);
				}
			
				if(!row[2].equals("")){
					ginput = new ComplexInput("goal", ballGoal_strat);
					Feature goalDist = new Feature(Double.parseDouble(row[2])); 
					Feature goalAngle = new Feature(Double.parseDouble(row[3]));
					ginput.add(new AtomicInput("goal_dist", goalDist, Atomic_strat));
					ginput.add(new AtomicInput("goal_dir", goalAngle, Atomic_strat));
					input.add(ginput);
				}
			
				if(!row[4].equals("") || !row[6].equals("")){
					flags = new ComplexInput("flags",flag_strat);
			
					if(!row[4].equals("")){
					
						flaginput = new ComplexInput("flt20", ballGoal_strat);
						
						Feature Dist = new Feature(Double.parseDouble(row[4])); 
						Feature Angle = new Feature(Double.parseDouble(row[5]));
						flaginput.add(new AtomicInput("flt20"+"_dist", Dist,Atomic_strat));
						flaginput.add(new AtomicInput("flt20"+"_dir", Angle,Atomic_strat));
						
						//add to input
						flags.add(flaginput);
					}
					if(!row[6].equals("")){
						flaginput2 = new ComplexInput("frt20", ballGoal_strat);
						
						Feature Dist1 = new Feature(Double.parseDouble(row[6])); 
						Feature Angle1 = new Feature(Double.parseDouble(row[7]));
						flaginput2.add(new AtomicInput("frt20"+"_dist", Dist1,Atomic_strat));
						flaginput2.add(new AtomicInput("frt20"+"_dir", Angle1,Atomic_strat));
						
						//add to input
						flags.add(flaginput2);
					}
					
					input.add(flags);
				}
				
				action = new AtomicAction(row[8]);
				
				cb.createThenAdd(input, action, stateBasedSim);
			}
			
			sim_weights.setFeatureWeight("ball", 1);
			sim_weights.setFeatureWeight("goal", 1);
			sim_weights.setFeatureWeight("flags", 0);
			
			br.close();
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cb;
	}
	
	public static void main(String [] args) throws IOException{
		CaseBase cb = SampleTest.reader("data/training_data.csv");
		System.out.println("Loaded casebase");
		CaseBase tb = SampleTest.reader("data/testing_data.csv");
		System.out.println("Loaded testbase");
		
		Reasoning r = new WeightedKNN(3,cb);
		int count = 0;
		
		System.out.println("Starting test...");
		for(Case c: tb.getCases()){
			String correct = c.getAction().getName();
			String predicted = r.selectAction(c.getInput()).getName();
			System.out.println("Correct: "+ correct + "| Predicted: "+ predicted);
			
			if(correct.equals(predicted)){
				count++;
			}
		}
		System.out.println("Done testing!");
		System.out.println("Accuracy:" + count/(tb.getSize()*1.0));
	}
}
