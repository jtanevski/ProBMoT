package search;

import java.util.*;

import struct.inst.*;
import temp.*;
import util.*;

/**
 * 2nd Phase
 * 
 * Takes as input a model where all top-level processes are instantiated (from
 * ModelRefiner for example) and returns (enumerates) all models with complete
 * matched parameters.
 * 
 * @author darko
 * 
 */
public class ParameterMatcher {
	private ExtendedModel model;

	private ListMap<String, IAE> allAEs = new ListMap<String, IAE>(); // not sure I need this. We'll see :)

	/**
	 * Maps AE -> The List of all valid values for that AE. Each valid value is a
	 * MapList (essentially a set) of IEs
	 */
	private ListMap<IAE, List<ListMap<String, IE>>> aeValues = new ListMap<IAE, List<ListMap<String, IE>>>();

	// keeping track of available/current variation
	private int varCount;
	private int varCurrentIndex;

	private int[] varCurrent;
	private int[] varMax;
	
	/**
	 * Indicates to the nextModel method whether this is the first model to be generated or not
	 * The next element is the first element
	 */
	private boolean nextFirst;

	ParameterMatcher(ExtendedModel extendedModel) {
		this.model = extendedModel;

		Collection<IP> topLevelIPs = extendedModel.getModel().topLevelIPs.values();

		int totalAEs = 0; // number of AEs in all processes
		for (IP ip : topLevelIPs) {
			totalAEs += ip.arguments.size();
			for (int i = 0; i < ip.arguments.size(); i++) {
				IAE ae = (IAE) ip.arguments.get(i);

				allAEs.put(ae.getFullName(), ae);
				aeValues.put(ae, new LinkedList<ListMap<String, IE>>());

				if (!ae.args.isEmpty()) {					// IF the argument is fixed
					aeValues.get(ae).add(ae.args); 		//   set the fixed value as only possibility for this AE
				} else if (ae.upperArgs.isEmpty()) {	// IF the argument is fixed and has 0 IEs (argument with cardinality 0)
					aeValues.get(ae).add(ae.args);		//   set the fixed value (empty maplist) as only posibility
				} else {											// IF argument has lower/upper bounds
					aeValues.get(ae).addAll(generateCombs(ae));
				}
			}
		}
		this.varCurrent = new int[totalAEs];
		this.varMax = new int[totalAEs];
		
		int count = 1;
		for (int i = 0; i < totalAEs; i++) {
			this.varCurrent[i] = 0;
			this.varMax[i] = aeValues.getValue(i).size();
			count*=this.varMax[i];
		}
		this.varCurrentIndex = 0; 							// the first is 1; this indicates that has not yet started
		this.varCount = count;
		this.nextFirst = true;
	}

	private static List<ListMap<String, IE>> generateCombs(IAE ae) {
		List<ListMap<String, IE>> combs = new LinkedList<ListMap<String, IE>>();

		int lowerCard = (int) ae.card.getLower();
		int upperCard = (int) ae.card.getUpper(); // NOTE: +infinity will be truncated to biggest INT

		ListMap<String, IE> lowerArgs = ae.lowerArgs;
		ListMap<String, IE> upperArgs = ae.upperArgs;

		if (lowerCard > upperArgs.size()) {
			throw new RuntimeException("too few available arguments"); // has to choose at least lowerCard arguments
		}
		if (lowerArgs.size() > upperCard) {
			throw new RuntimeException("too many mandatory arguments"); // has to use all in lowerArgs
		}

		// mandatory = lower
		ListMap<String, IE> mandatory = new ListMap<String, IE>();
		mandatory.putAll(0, lowerArgs);

		List<IE> opt = new LinkedList<IE>(upperArgs.values());
		opt.removeAll(mandatory.values());

		// optional = upper \ lower
		ListMap<String, IE> optional = new ListMap<String, IE>();
		for (IE ie : opt) {
			optional.put(ie.id, ie);
		}

		int minSize = Math.max(lowerCard - lowerArgs.size(), 0); // the minimum set size for the comb
		int maxSize = Math.min(upperCard - lowerArgs.size(), optional.size()); // the maximum set size for the comb

		for (int k = minSize; k <= maxSize; k++) {
			if (k == 0) {
				ListMap<String, IE> comb = new ListMap<String, IE>();
				comb.putAll(0, mandatory);
				combs.add(comb); // add the empty optional set (only mandatory)
				continue; // CombinationGenerator doesn't support combinations with k=0
			}

			CombinationGenerator<IE> generator = new CombinationGenerator<IE>(new LinkedList(optional.values()), k); // choose k from optional
			for (List<IE> combList : generator) {
				ListMap<String, IE> comb = new ListMap<String, IE>();
				comb.putAll(0, mandatory);
				for (IE ie : combList) {
					comb.put(ie.id, ie);
				}
				combs.add(comb);
			}
		}
		return combs;
	}
	
	ExtendedModel getFirstModel() {
		for (int i = 0; i < this.aeValues.size(); i++) {
			this.aeValues.getKey(i).args = aeValues.getValue(i).get(0);
			this.varCurrent[i] = 1;
		}
		this.varCurrentIndex++;

		//	printState();
		return this.getCurrentModel();
	}

	public boolean hasNextModel() {
		if (this.nextFirst) {
			return true;
		} else {
			return (this.varCurrentIndex < this.varCount);
		}
	}

	
	/**
	 *  generate next variation
	 * generating in order 111 211 311 121 221 321 112 212 312 122 222 322
	 * from (1:3, 1:2, 1:2)
	 * @return
	 */
	public ExtendedModel nextModel() {
		if (this.nextFirst) {
			for (int i = 0; i < this.aeValues.size(); i++) {
				this.aeValues.getKey(i).args = aeValues.getValue(i).get(0);
				this.varCurrent[i] = 1;
			}
			this.varCurrentIndex++;

			this.nextFirst = false;
		} else {
			if (this.varCurrentIndex == this.varCount) {
				throw new NoSuchElementException();
			}
			int index = -1;
			for (int i = 0; i < this.varCurrent.length; i++) {
				if (this.varCurrent[i] < this.varMax[i]) {
					index = i;
					break;
				}
			}

			for (int i = 0; i < index; i++) {
				this.aeValues.getKey(i).args = aeValues.getValue(i).get(0);
				this.varCurrent[i] = 1;
			}

			this.varCurrent[index]++;
			this.aeValues.getKey(index).args = this.aeValues.getValue(index).get(varCurrent[index]-1);

			this.varCurrentIndex++;
		}
		
		return this.getCurrentModel();
	}
	
	/**
	 *  generate next variation
	 * generating in order 111 211 311 121 221 321 112 212 312 122 222 322
	 * from (1:3, 1:2, 1:2)
	 * @return
	 */
	ExtendedModel getNextModel() {
		if (this.varCurrentIndex == this.varCount) {
			throw new NoSuchElementException();
		}
		int index = -1;
		for (int i = 0; i < this.varCurrent.length; i++) {
			if (this.varCurrent[i] < this.varMax[i]) {
				index = i;
				break;
			}
		}

		for (int i = 0; i < index; i++) {
			this.aeValues.getKey(i).args = aeValues.getValue(i).get(0);
			this.varCurrent[i] = 1;
		}

		this.varCurrent[index]++;
		this.aeValues.getKey(index).args = this.aeValues.getValue(index).get(varCurrent[index]-1);

		this.varCurrentIndex++;

		return this.getCurrentModel();
	}

	ExtendedModel getCurrentModel() {
		//return this.model;
		return this.model.copyNoEQ(); //NOTE: copying doesn't work properly on incomplete processes because compose() is called within
	}

	public int getCount() {
		return this.varCount;
	}

	private void printState() {
		for (int i = 0; i < this.aeValues.size(); i++) {
			System.out.println(this.aeValues.getKey(i).getFullName() + " : " + this.aeValues.getKey(i).args + "; ");
		}
		System.out.println();
	}
}