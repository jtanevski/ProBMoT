package search;

import java.util.*;

import org.apache.commons.lang3.*;

import struct.*;
import struct.inst.*;
import struct.temp.*;
import temp.*;
import util.*;

/**
 * 3rd Phase
 * @author darko
 *
 */
public class ComponentProcessRefiner {
	private ExtendedModel model;

	/**
	 *
	 * Refiners for all top-level processes. They are already fixed, but will recursively refine the component processes
	 */
	public ListMap<String, IPRecursiveRefiner> refiners = new ListMap<String, IPRecursiveRefiner>();

	private int count;
	private int current;

	/**
	 * Helper for the nextModel method.
	 * The next element is the first element
	 */
	private boolean nextFirst;

	public ComponentProcessRefiner(ExtendedModel extendedModel) {
		this.model = extendedModel;

		Collection<IP> topLevelIPs = extendedModel.getModel().topLevelIPs.values();

		int count = 1;
		for (IP ip : topLevelIPs) {
			IPRecursiveRefiner refiner = new IPRecursiveRefiner(this.model, ip, true);
			this.refiners.put(ip.getFullName(), refiner);
		}

		this.nextFirst = true;

		this.current = 0;
		this.count = count;
	}


	public ExtendedModel nextModel() {
		if (this.nextFirst) {
			for (int i = 0; i < refiners.size(); i++) {
				refiners.getValue(i).firstState();
			}
			this.current++;

			this.nextFirst = false;
		} else {
			int index = -1;
			for (int i = 0; i < this.refiners.size(); i++) {
				if (this.refiners.getValue(i).hasNextState()) {
					index = i;
					break;
				}
			}
			if (index==-1) {											 // no more refinements
				throw new NoSuchElementException();
			}

			for (int i = 0; i < index; i++) {
				this.refiners.getValue(i).firstState();
//				this.refiners.get(i).refineToRoot();
//				this.refiners.get(i).firstRefinement();
			}

			this.refiners.getValue(index).nextState();

			this.current++;
		}

		return this.getCurrentModel();
	}

	ExtendedModel getFirstModel() {
		for (int i = 0; i < refiners.size(); i++) {
			refiners.getValue(i).firstState();
		}
		this.current++;

		return this.getCurrentModel();
	}

	/**
	 * If any of the Top-Level processes can be refined (trough component processes) than there is a next model
	 * otherwise there is no next model
	 * @return
	 */
	public boolean hasNextModel() {
		if (this.nextFirst) {
			return true;
		} else {
			for (int i = 0; i < this.refiners.size(); i++) {
				if (this.refiners.getValue(i).hasNextState())
					return true;
			}
			return false;
		}
	}

	ExtendedModel getNextModel() {
		int index = -1;
		for (int i = 0; i < this.refiners.size(); i++) {
			if (this.refiners.getValue(i).hasNextState()) {
				index = i;
				break;
			}
		}
		if (index==-1) {											 // no more refinements
			throw new NoSuchElementException();
		}

		for (int i = 0; i < index; i++) {
			this.refiners.getValue(i).firstState();
//			this.refiners.get(i).refineToRoot();
//			this.refiners.get(i).firstRefinement();
		}

		this.refiners.getValue(index).nextState();

		this.current++;

		return this.getCurrentModel();
	}

	ExtendedModel getCurrentModel() {
		ExtendedModel copied = this.model.copy();
//		System.out.println(copied);
		return copied;
	}

	public void printState() {
		for (int i = 0; i < this.refiners.size(); i++) {
			System.out.print(this.refiners.getValue(i).getProcess().id + " : " + this.refiners.getValue(i).getProcess().template.id + "; ");
		}
		System.out.println();
	}

	public int getCount() {
		return this.count;
	}


}

