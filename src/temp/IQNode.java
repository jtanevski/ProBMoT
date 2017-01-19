package temp;

import java.util.*;

import struct.*;
import struct.inst.*;

public class IQNode {

	static class INT_SEQ {
		private static int INT = 0;

		public static int nextInt() {
			return INT++;
		}
	}

	public int id;

	public EQType type;
	public IQ iq;

	IVNode outputVar;

	// Variables and Parameters that appear in the equation
	Map<String, IVNode> inputVariables = new LinkedHashMap<String, IVNode>();
	Map<String, ICNode> inputParameters = new LinkedHashMap<String, ICNode>();

	// fields used for the topological sorting. Should not be used by other routines!
	// boolean passed = false;
	Map<Integer, IQNode> passedInputIQs = new LinkedHashMap<Integer, IQNode>();



	// Equations that are connected to this equations

	// All equation links
	Map<Integer, IQNode> allInputIQs = new LinkedHashMap<Integer, IQNode> ();
	Map<Integer, IQNode> allOutputIQs = new LinkedHashMap<Integer, IQNode> ();

	// Used equation links (excluding only exogenous nodes)
	Map<Integer, IQNode> usedInputIQs = new LinkedHashMap<Integer, IQNode> ();
	Map<Integer, IQNode> usedOutputIQs = new LinkedHashMap<Integer, IQNode> ();

	// Reachable equation links (reachable from the start set)
	Map<Integer, IQNode> reachInputIQs = new LinkedHashMap<Integer, IQNode> ();
	Map<Integer, IQNode> reachOutputIQs = new LinkedHashMap<Integer, IQNode> ();

	// Differential equation links (used in the differential simulation)
	Map<Integer, IQNode> diffInputIQs = new LinkedHashMap<Integer, IQNode> ();
	Map<Integer, IQNode> diffOutputIQs = new LinkedHashMap<Integer, IQNode> ();

	// Algebraic equation links (used in the algebraic simulation)
	Map<Integer, IQNode> algInputIQs = new LinkedHashMap<Integer, IQNode> ();
	Map<Integer, IQNode> algOutputIQs = new LinkedHashMap<Integer, IQNode> ();



	// Equations that are connected to this equation excluding Differential-Differential dependencies
	// All equation links
	Map<Integer, IQNode> allInputIQsA = new LinkedHashMap<Integer, IQNode> ();
	Map<Integer, IQNode> allOutputIQsA = new LinkedHashMap<Integer, IQNode> ();

	// Used equation links (excluding only exogenous nodes)
	Map<Integer, IQNode> usedInputIQsA = new LinkedHashMap<Integer, IQNode> ();
	Map<Integer, IQNode> usedOutputIQsA = new LinkedHashMap<Integer, IQNode> ();

	// Reachable equation links (reachable from the start set)
	Map<Integer, IQNode> reachInputIQsA = new LinkedHashMap<Integer, IQNode> ();
	Map<Integer, IQNode> reachOutputIQsA = new LinkedHashMap<Integer, IQNode> ();

	// Differential equation links (used in the differential simulation)
	Map<Integer, IQNode> diffInputIQsA = new LinkedHashMap<Integer, IQNode> ();
	Map<Integer, IQNode> diffOutputIQsA = new LinkedHashMap<Integer, IQNode> ();

	// Algebraic equation links (used in the algebraic simulation)
	Map<Integer, IQNode> algInputIQsA = new LinkedHashMap<Integer, IQNode> ();
	Map<Integer, IQNode> algOutputIQsA = new LinkedHashMap<Integer, IQNode> ();



	public IQNode() {
		this.id = INT_SEQ.nextInt();
	}

	public IQNode(IQ iq) {
		this.iq = iq;
		this.type = iq.type;

		this.id = INT_SEQ.nextInt();
	}

	public void setIQ(IQ iq) {
		this.iq = iq;
		this.type = iq.type;
	}

	public String toString() {
		return this.iq.toString();
	}

}