package search;

import java.util.*;

import struct.*;
import struct.inst.*;
import struct.temp.*;
import temp.*;
import util.*;

/**
 * 1st Phase
 * 
 * Takes as input an incomplete model and returns (enumerates) all models where
 * the top-level processes are fully instantiated.
 * 
 * @author darko
 * 
 */
public class ModelRefiner {
	private ExtendedModel model;

	private ListMap<String, IPRefiner> refiners = new ListMap<String, IPRefiner>();

	private int count;
	private int current;

	private int[] variation;
	private int[] maxVariation;

	/**
	 * auxiliary variable used in initialization to distinguish between the first call to nextModel and subsequent ones
	 * The next element is the first element
	 */
	private boolean nextFirst;
	
	ModelRefiner(ExtendedModel extendedModel) {
		this.model = extendedModel;

		Collection<IP> topLevelIPs = extendedModel.getModel().topLevelIPs.values();

		variation = new int[topLevelIPs.size()];
		maxVariation = new int[topLevelIPs.size()];
		
		this.nextFirst = true;

		int i = 0;
		int count = 1;
		for (IP ip : topLevelIPs) {
			IPRefiner refiner = new IPRefiner(this.model, ip, true);
			this.refiners.put(ip.getFullName(), refiner);

			this.maxVariation[i] = refiner.getCount();

			count *= this.maxVariation[i];
			i++;
		}
		this.current = 0;
		this.count = count;
	}
	
	public ExtendedModel nextModel() {
		if (this.nextFirst) {
			for (int i = 0; i < refiners.size(); i++) {
				refiners.getValue(i).firstState();						//NOTE: new implementation 	old: refiners.get(i).firstRefinement();
				this.variation[i] = 1;
			}
			this.current++;
	
			this.nextFirst = false;
		} else {
			int index = -1;
			for (int i = 0; i < this.refiners.size(); i++) {
				if (this.refiners.getValue(i).hasNextState()) { 	//NOTE: new implementation (old was hasNextRefinement()
					index = i;
					break;
				}
			}
			if (index==-1) { // no more refinements
				throw new NoSuchElementException();
			}

			for (int i = 0; i < index; i++) {
				this.variation[i] = 1;
				this.refiners.getValue(i).firstState();				//NOTE: new implementation	 old: this.refiners.get(i).refineToRoot(); this.refiners.get(i).firstRefinement();
			}

			this.variation[index]++;
			this.refiners.getValue(index).nextState();				//NOTE: new implementation old: this.refiners.get(index).nextRefinement();
			this.current++;
		}
		
		return this.getCurrentModel();
	}

	ExtendedModel getFirstModel() {
		for (int i = 0; i < refiners.size(); i++) {
			refiners.getValue(i).firstState();	//NOTE: new implementation 	old: refiners.get(i).firstRefinement();
			this.variation[i] = 1;
		}
		this.current++;

//		printState();
		return this.getCurrentModel();
	}

	public boolean hasNextModel() {
		if (this.nextFirst) {
			return true;
		} else {
			int index = -1;
			for (int i = 0; i < this.refiners.size(); i++) {
				if (this.refiners.getValue(i).hasNextState()) { //NOTE: new implementation (old was hasNextRefinement()
					index = i;
					break;
				}
			}
			return index!=-1;
		}
	}

	ExtendedModel getNextModel() {
		int index = -1;
		for (int i = 0; i < this.refiners.size(); i++) {
			if (this.refiners.getValue(i).hasNextState()) { //NOTE: new implementation (old was hasNextRefinement()
				index = i;
				break;
			}
		}
		if (index==-1) { // no more refinements
			throw new NoSuchElementException();
		}

		for (int i = 0; i < index; i++) {
			this.variation[i] = 1;
			this.refiners.getValue(i).firstState();		//NOTE: new implementation	 old: this.refiners.get(i).refineToRoot(); this.refiners.get(i).firstRefinement();
		}

		this.variation[index]++;
		this.refiners.getValue(index).nextState();		//NOTE: new implementation old: this.refiners.get(index).nextRefinement();
		this.current++;

//		printState();
		return this.getCurrentModel();
	}

	ExtendedModel getCurrentModel() {
		return this.model.copyNoEQ();
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

/**
 * Given one IP, it generates all possible type refinements of that IP This
 * class can have only one iterator. It always returns the same one.
 * 
 * @author darko
 * 
 */
class IPRefiner {

	// NOTE: old code BEGIN. Will be removed pretty soon
	RefinementNode originalNode;
	RefinementNode currentNode;
	int count;
	int current;
	
	/**
	 * do a depth-first until you reach the terminal nodes (IPs) that can be
	 * instantiated
	 */
	public void firstPrimitiveRefinement() {

		while (currentNode.currentChild() != null) {
			RefinementNode newNode = currentNode.currentChild();
			refineDown(newNode.tp);
			currentNode = newNode;
		}
	}

	public IP firstRefinement() {
		this.firstPrimitiveRefinement();
		if (onlyTopLevel) {
			while (!currentNode.tp.isTopLevel()) {
				nextPrimitiveRefinement();
			}
		}
		this.current = 1;

		return ip;
	}

	public boolean hasNextRefinement() {
		if (this.current == this.count) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * generate the next terminal refinement, if there is one
	 * 
	 * @return
	 */
	public void nextPrimitiveRefinement() {
		// make sure there is a next refinement
		if (!this.hasNextRefinement()) {
			throw new NoSuchElementException();
		}
		do {
			RefinementNode newNode = currentNode.parent;
			refineUp();
			currentNode = newNode;
		} while (currentNode.nextChild() == null);
		int current = this.current;
		firstPrimitiveRefinement();
	}

	public IP nextRefinement() {
		this.nextPrimitiveRefinement();
		if (onlyTopLevel) {
			while (!currentNode.tp.isTopLevel()) {
				this.nextPrimitiveRefinement();
			}
		}
		this.current++;

		return ip;
	}

	/**
	 * UpRefine until you reach the root where the refinement started
	 * 
	 * @return
	 */
	public IP refineToRoot() {
		while (currentNode.parent != null) {
			refineUp();
			currentNode = currentNode.parent;
		}

		this.currentNode.reset();
		return this.ip;

	}

	/*
	private int count(RefinementNode node) {
		if (node.children.isEmpty()) {
			if (this.onlyTopLevel) {
				return node.tp.isTopLevel() ? 1 : 0;
			} else {
				return 1;
			}
		} else {
			int count = 0;
			for (RefinementNode childNode : node.children) {
				count += count(childNode);
			}
			return count;
		}
	}
*/
//NOTE: old code END
	

	
	private ExtendedModel extendedModel;
	private IP ip;
	private TP originalTP;
	boolean onlyTopLevel;

	private RefState state;


	IPRefiner(ExtendedModel extendedModel, IP ip, boolean onlyTopLevel) {
		this.extendedModel = extendedModel;
		this.ip = ip;
		this.originalTP = ip.getTemplate();
		this.onlyTopLevel = onlyTopLevel;
		
		this.state = new RefState(this.originalTP, this.onlyTopLevel);

		//NOTE: old code
		this.originalNode = new RefinementNode(originalTP);
		this.currentNode = this.originalNode;
//		this.count = count(this.originalNode);

	}

	IPRefiner(ExtendedModel extendedModel, IP ip) {
		this(extendedModel, ip, true);
	}
	
	public IP getProcess() {
		return this.ip;
	}

	public int getCount() {
		return this.state.getCount();
	}

	
	private void executeRefCommands(List<RefCommand> comms) {
		for (RefCommand comm : comms) {
			switch (comm.dir) {
				case UP:
					this.refineUp();
					break;
				case DOWN:
					this.refineDown(comm.tp);
					break;
				default:
					assert false;
			}
		}
	}
	
	public IP firstState() {
		List<RefCommand> comms = this.state.firstState();
		executeRefCommands(comms);
		return this.ip;
	}
	
	public boolean hasNextState() {
		return this.state.hasNextState();
	}
	
	public IP nextState() {
		List<RefCommand> comms = this.state.nextState();
		executeRefCommands(comms);
		return this.ip;
	}
	
	public IP initialState() {
		List<RefCommand> comms = this.state.initialState();
		executeRefCommands(comms);
		return this.ip;
	}
	
	

	/**
	 * makes the IP of a sub-type, adding the necessary structures - change
	 * template - create suitable AEs - create suitable APs - create suitable ICs
	 * - create suitable IQs - not needed, can be created with composeIQs, once
	 * the parameters are fixed
	 */
	private void refineDown(TP newTP) {
		ip.template = newTP;

		createICs();
		createAEs();
		createAPs();
		// createIQs();
	}

	/**
	 * makes the IP of a super-type dropping some information (with the goal of
	 * refining it down to another type
	 * 
	 * - change template - drop some AEs - drop some APs - drop some ICs - drop
	 * some IQs
	 */
	private void refineUp() {

		dropICs();
		dropAEs();
		dropAPs();
		dropIQs();

		ip.template = ip.template.zuper;
	}

	void createICs() {
		Set<TC> newTCs = new LinkedHashSet<TC>(ip.template.consts.values());
		newTCs.removeAll(ip.template.zuper.consts.values()); // get only the new TCs

		for (TC tc : newTCs) { // create and initialize the new IC
			IIC ic = new IIC();
			ic.cont = ip;
			ic.id = tc.id;
			ic.range = tc.range;
			ic.template = tc;
			ic.unit = tc.unit;
			ic.value = null; // has to be fitted

			ip.consts.put(ic.id, ic); // add the IC when it is created
		}
	}

	/**
	 * this is not used in practice, because no process adds new parameters in
	 * the libraries, other than the ones who have the root process 'PROCESS' as
	 * parent here for completeness and further use
	 */
	private void createAEs() {
		Set<PE> newPEs = new LinkedHashSet<PE>(ip.template.parameters.values());
		newPEs.removeAll(ip.template.zuper.parameters.values()); // get only the new PEs

		for (PE pe : newPEs) {
			IAE ae = new IAE();
			ae.id = pe.idI;
			ae.ip = ip;
			ae.card = pe.card;
			ae.template = pe;
			ae.te = pe.te;
			ae.args = new ListMap<String, IE>(); // no arguments
			ae.lowerArgs = new ListMap<String, IE>(); // set lower and upper appropriately to empty and maximum set
			ae.upperArgs = new ListMap<String, IE>();
			List<IE> ies = extendedModel.getModel().TEtoIEs.get(ae.te.getFullName());
			for (IE ie : ies) {
				ae.upperArgs.put(ie.id, ie);
			}

			ip.arguments.add(ae); // add the AE when it is created
		}
	}

	/**
	 * create an AP which is actually empty (only template is given), just a
	 * placeholder for the search for suitable component processes
	 * 
	 */
	private void createAPs() {
		List<PP> newPPs = new ArrayList<PP>(ip.template.processes);
		newPPs.removeAll(ip.template.zuper.processes); // get only the new PPs

		for (PP pp : newPPs) {
			// create the IAP if it is not iterated
			if (pp.isIter)
				continue;
			IAP ap = new IAP();
			ap.id = pp.id;
			ap.ip = ip;
			ap.template = pp;
			ap.tp = pp.tp;
			ap.argTemp = pp.tp;

			ip.processes.add(ap); // add the AP when it is created
		}
	}

	/**
	 * drop the ICs which are not in the parent
	 */
	private void dropICs() {
		Set<TC> oldTCs = new LinkedHashSet<TC>(ip.template.consts.values());
		oldTCs.removeAll(ip.template.zuper.consts.values()); // the TCs to be removed

		for (TC tc : oldTCs) {
			ip.consts.remove(tc.id);
		}
	}

	private void dropAEs() {
		Set<PE> oldPEs = new LinkedHashSet<PE>(ip.template.parameters.values());
		oldPEs.removeAll(ip.template.zuper.parameters.values()); // the PEs to be removed

		for (PE pe : oldPEs) {
			for (int i = 0; i < ip.arguments.size(); i++) {
				if (ip.arguments.get(i).id.equals(pe.idI)) {
					ip.arguments.remove(i);
					break;
				}
			}
		}
	}

	private void dropAPs() {
		List<PP> oldPPs = new ArrayList<PP>(ip.template.processes);
		oldPPs.removeAll(ip.template.zuper.processes); // the PPs to be removed

		for (PP pp : oldPPs) {
			for (int i = 0; i < ip.processes.size(); i++) {
				if (ip.processes.get(i).id.equals(pp.id)) {
					ip.processes.remove(i);
					break;
				}
			}
		}
	}

	/**
	 * still not sure whether I need this
	 */
	private void dropIQs() {
		List<TQ> oldTQs = new ArrayList<TQ>(ip.template.equations);
		oldTQs.removeAll(ip.template.zuper.equations); // the TQs to be removed

		for (TQ tq : oldTQs) {
			IQ iq = null;
			for (IQ iq2 : ip.equations) { // find the IQ that corresponds to the TQ
				if (iq2.template == tq) {
					iq = iq2;
					break;
				}
			}
			ip.equations.remove(iq);
		}
	}
}