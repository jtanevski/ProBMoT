package search.heuristic;
import java.util.LinkedList;

import temp.ExtendedModel;
import xml.OutputSpec;
import jmetal.core.Variable;

interface HeuristicCodec {
	public LinkedList<Integer> encode(ExtendedModel extendedModel, OutputSpec outputSpec);
	public ExtendedModel decode(Variable[] genotype);
}
