package search.heuristic;

import jmetal.core.Problem;
import jmetal.core.SolutionType;
import jmetal.core.Variable;
import jmetal.encodings.variable.Int;
import jmetal.encodings.variable.Real;

/**
 * Class used to represent the mixed integer solution type of the heuristic model search problem.
 * 
 * @author jovant
 */

public class ModelSolution extends SolutionType {

	
	private HeuristicCodec codec;
	
	public ModelSolution(Problem problem) {
		super(problem);
		codec = ((ModelSearchProblem)problem).codec;

	}

	@Override
	public Variable[] createVariables() throws ClassNotFoundException {
		Variable[] variables = new Variable[problem_.getNumberOfVariables()];
		for(int i=0; i<codec.code.size(); i++) variables[i] = new Int();
		for(int i=codec.code.size(); i<variables.length; i++) variables[i] = new Real();
		return variables;
		
	}

}
