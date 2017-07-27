package search.heuristic;
import java.util.LinkedList;

import temp.ExtendedModel;
import xml.OutputSpec;
import jmetal.core.Variable;

abstract class HeuristicCodec {
	protected ExtendedModel extendedModel;
	protected LinkedList<Integer> code;
	protected EnumeratingCodec internalEnumeratingCodec;
	public abstract LinkedList<Integer> encode(ExtendedModel extendedModel, OutputSpec outputSpec);
	public abstract ExtendedModel decode(Variable[] genotype);
}
