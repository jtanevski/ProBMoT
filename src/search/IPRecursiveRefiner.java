package search;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import struct.inst.AE;
import struct.inst.AP;
import struct.inst.IAE;
import struct.inst.IAP;
import struct.inst.IE;
import struct.inst.IIC;
import struct.inst.IP;
import struct.inst.IQ;
import struct.temp.PE;
import struct.temp.PP;
import struct.temp.TC;
import struct.temp.TP;
import struct.temp.TQ;
import temp.ExtendedModel;
import util.ListMap;

/**
*
*
* Given one IP, it generates all possible type refinements of that IP
*
* @author darko
*
*/
public class IPRecursiveRefiner {

	//OLD CODE BEGIN
	int count;
	int current;
	RefinementNode originalNode;
	RefinementNode currentNode;


	/**
	 * do a depth-first until you reach the terminal nodes (IPs) that can be
	 * instantiated
	 */
	void firstPrimitiveRefinement() {

		while (currentNode.currentChild() != null) {
			RefinementNode newNode = currentNode.currentChild();
			refineDown(this.ip, newNode.tp);
			currentNode = newNode;
		}
	}

	/**
	 * no support for top-level here.
	 * @return
	 */
	IP firstRefinement() {
		this.firstPrimitiveRefinement();
		for (IPRecursiveRefiner refiner : this.refiners.values()) {
			refiner.firstRefinement();
		}


//		if (onlyTopLevel) {
//			while (!currentNode.tp.isTopLevel()) {
//				nextPrimitiveRefinement();
//			}
//		}
		this.current = 1;

		return ip;
	}

	boolean hasNextPrimitiveRefinement() {
		if (this.current == this.count) {
			return false;
		} else {
			return true;
		}
	}
	boolean hasNextRefinement() {

		// if the componenet processes can be refined return true
		for (IPRecursiveRefiner refiner : this.refiners.values()) {
			if (refiner.hasNextRefinement())
				return true;
		}

		// if the component processes can not be refined check whether this process can be refined
		return this.hasNextPrimitiveRefinement();
	}

	/**
	 * generate the next terminal refinement, if there is one
	 *
	 * @return
	 */
	void nextPrimitiveRefinement() {
		// make sure there is a next refinement
		if (!this.hasNextRefinement()) {
			throw new NoSuchElementException();
		}
		do {
			RefinementNode newNode = currentNode.parent;
			refineUp(this.ip);
			currentNode = newNode;
		} while (currentNode.nextChild() == null);
		int current = this.current;
		firstPrimitiveRefinement();
	}

	IP nextRefinement() {
		// if a component process can be refined, refine it
		int index = -1;
		for (int i = 0; i < this.refiners.size(); i++) {
			if (this.refiners.getValue(i).hasNextRefinement()) {
				index = i;
				break;
			}
		}
		if (index!=-1) { // refine a component process
			for (int i = 0; i < index; i++) {
				this.refiners.getValue(i).refineToRoot();
				this.refiners.getValue(i).firstRefinement();
			}

			this.refiners.getValue(index).nextRefinement();

		} else { // if not, refine this process
			this.nextPrimitiveRefinement();
			// rest all component processes to their first refinement
			for (int i = 0; i < this.refiners.size(); i++) {
				this.refiners.getValue(i).refineToRoot();
				this.refiners.getValue(i).firstRefinement();
			}




//			if (onlyTopLevel) {
//				while (!currentNode.tp.isTopLevel()) {
//					this.nextPrimitiveRefinement();
//				}
//			}
			this.current++;
		}

		return ip;
	}

	/**
	 * UpRefine until you reach the root where the refinement started
	 *
	 * @return
	 */
	IP refineToRoot() {
		while (currentNode.parent != null) {
			refineUp(this.ip);
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

	//NOTE OLD CODE END



	private ExtendedModel extendedModel;
	public IP ip;
	private TP originalTP;
	private boolean onlyTopLevel;

	public ListMap<String, IPRecursiveRefiner> refiners = new ListMap<String, IPRecursiveRefiner>();

	public RefState state;
	
	private static final Random rand = new Random(42); //always use the same random seed for reproducibility and for process matching within the heuristic search

	/**
	 *
	 * @param extendedModel
	 * @param ip
	 * @param onlyTopLevel
	 */
	public IPRecursiveRefiner(ExtendedModel extendedModel, IP ip, boolean onlyTopLevel) {
		this.extendedModel = extendedModel;
		this.ip = ip;
		this.originalTP = ip.getTemplate();

		this.onlyTopLevel = onlyTopLevel;

		this.state = new RefState(this.originalTP, this.onlyTopLevel);

//		this.originalNode = new RefinementNode(originalTP);
//		this.currentNode = this.originalNode;
//		this.count = count(this.originalNode);

		for (int i = 0; i < ip.processes.size(); i++) {
			IAP ap = (IAP) ip.processes.get(i);
			findOrCreateIP(ap);
			refiners.put(ap.arg.getFullName(), new IPRecursiveRefiner(extendedModel, ap.arg, false));
		}

		if (ip.template.processes.size() > ip.processes.size()) {										// there is an iterated PP  at the end
			PP  pp = ip.template.processes.get(ip.template.processes.size() - 1); 					// the last PP is the iterated
			int argIndex = pp.cont.parameters.indexOfValue(pp.iterated);						// the index of the iterated PE in the process
			int argSize = ip.arguments.get(argIndex).args.size();						// the cardinality of the iterated AE
			for (int count = 0; count < argSize; count++) {
				IAP newAP = new IAP();
				newAP.index = count;
				newAP.id = pp.id + count;														// the index of the AP within the whole list of all APs
				newAP.ip = ip;
				newAP.template = pp;
				newAP.tp = pp.tp;
				newAP.parameters = pp.parameters;
				newAP.argTemp = pp.tp;

				findOrCreateIP(newAP); 							// use the index field in newAP to find the suitable argument
				ip.processes.add(newAP); // add the AP when it is created
				refiners.put(newAP.arg.getFullName(), new IPRecursiveRefiner(extendedModel, newAP.arg, this.onlyTopLevel));
			}
		}

	}

	IPRecursiveRefiner(ExtendedModel extendedModel, IP ip) {
		this(extendedModel, ip, false);
	}

	public IP getProcess() {
		return this.ip;
	}

	/**
	 * This is a rough count estimate based on the current Refiners used. Changes dynamically.
	 * Estimates as Nx(AxBxCx...xZ) where N is the number of Process Forms for this IP
	 * and A, B, C ... Z, are the number of Process Forms for its current component IPs
	 * @return
	 */
	public int getCount() {
		int count = 1;
		for (IPRecursiveRefiner subRefiner : this.refiners.values()) {
			count *= subRefiner.getCount();
		}
		return count*this.state.getCount();

	}

	public void executeRefCommands(List<RefCommand> comms) {
		for (RefCommand comm : comms) {
			switch (comm.dir) {
				case UP:
					this.refineUp(this.ip);
					break;
				case DOWN:
					this.refineDown(this.ip, comm.tp);
					break;
				default:
					assert false;
			}
		}
	}

	/**
	 * Move the current IP to firstState.
	 * That can introduce a number of component IPs, which will register their refiners in this.refiners.
	 * Then recursively call first state on all component refiners.
	 * @return
	 */
	public IP firstState() {
		List<RefCommand> comms = this.state.firstState();
		executeRefCommands(comms);
		for (IPRecursiveRefiner subRefiner : this.refiners.values()) {
			subRefiner.firstState();
		}

		return this.ip;
	}

	/**
	 * If any of the component processes can be refined than that will be the next refinement
	 * If not, than check whether this process can be refined
	 * @return
	 */
	public boolean hasNextState() {
		for (IPRecursiveRefiner subRefiner : this.refiners.values()) {
			if (subRefiner.hasNextState())
				return true;
		}
		return this.state.hasNextState();
	}


	/**
	 * If a component process can be refined, refine it
	 * If not, refine this IP
	 * @return
	 */
	public IP nextState() {
		int index = -1;
		for (int i = 0; i < this.refiners.size(); i++) {
			if (this.refiners.getValue(i).hasNextState()) {
				index = i;
				break;
			}
		}
		if (index!=-1) { 													// refine a component process
			for (int i = 0; i < index; i++) {
				this.refiners.getValue(i).firstState();
			}
			this.refiners.getValue(index).nextState();
		} else {																// if not, refine this process
			List<RefCommand> comms = this.state.nextState();
			executeRefCommands(comms);
			for (int i = 0; i < this.refiners.size(); i++) {
				this.refiners.getValue(i).firstState();
			}
		}
		return this.ip;
	}
	
	public IP nextStateSelf(){
		
		//refiners = new ListMap<String, IPRecursiveRefiner>();
		
		List<RefCommand> comms = this.state.nextState();
		executeRefCommands(comms);
		
		for (int i = 0; i < this.refiners.size(); i++) {
			this.refiners.getValue(i).firstState();
		}
		
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
	void refineDown(IP ip, TP newTP) {
		ip.template = newTP;

		createICs(ip);
		createAEs(ip);
		createAPs(ip);
		// createIQs();

		ip.getModel().upkeep();
	}

	/**
	 * makes the IP of a super-type dropping some information (with the goal of
	 * refining it down to another type
	 *
	 * - change template - drop some AEs - drop some APs - drop some ICs - drop
	 * some IQs
	 */
	void refineUp(IP ip) {

		dropICs(ip);
		dropAEs(ip);
		dropAPs(ip);
		dropIQs(ip);

		ip.template = ip.template.zuper;
		ip.getModel().upkeep();
	}

	void createICs(IP ip) {
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
	void createAEs(IP ip) {

		Set<PE> newPEs = new LinkedHashSet<PE>(ip.template.parameters.values());
		newPEs.removeAll(ip.template.zuper.parameters.values()); // get only the new PEs

		for (PE pe : newPEs) {
//			ExhaustiveSearch.logger.warn("Creating AE - check this behavior");

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
	 * create APs
	 * for each PP create a suitable AP (or APs if the PP is iterated)
	 * for each AP first check if a suitable IP exists
	 *   if it does, connect to it
	 *   if it doesn't, create an IP
	 * for each AP, create a recursive refiner that will refine the IP (if needed)
	 *
	 */
	void createAPs(IP ip) {
		List<PP> newPPs = new ArrayList<PP>(ip.template.processes);
		newPPs.removeAll(ip.template.zuper.processes); // get only the new PPs

		for (PP pp : newPPs) {
			if (pp.isIter) {
				// find the iterated argument
				// for each value of the iterated argument repeat what is esentialy in the 'else' clause
				int argIndex = pp.cont.parameters.indexOfValue(pp.iterated);						// the index of the iterated PE in the process
				int argSize = ip.arguments.get(argIndex).args.size();									// the cardinality of the iterated AE
				for (int count = 0; count < argSize; count++) {
					IAP newAP = new IAP();
					newAP.index = count;
					newAP.id = pp.id + count;														// the index of the AP within the whole list of all APs
					newAP.ip = ip;
					newAP.template = pp;
					newAP.tp = pp.tp;
					newAP.parameters = pp.parameters;
					newAP.argTemp = pp.tp;

					findOrCreateIP(newAP); 							// use the index field in newAP to find the suitable argument
					ip.processes.add(newAP); // add the AP when it is created
					refiners.put(newAP.arg.getFullName(), new IPRecursiveRefiner(extendedModel, newAP.arg));

				}
			} else {
				IAP newAP = new IAP();
				newAP.id = pp.id;
				newAP.ip = ip;
				newAP.template = pp;
				newAP.tp = pp.tp;
				newAP.parameters = pp.parameters;
				newAP.argTemp = pp.tp;

				findOrCreateIP(newAP);
				ip.processes.add(newAP); // add the AP when it is created
				refiners.put(newAP.arg.getFullName(), new IPRecursiveRefiner(extendedModel, newAP.arg));
			}
		}
	}
	/**If it is connected to an IP than do nothing
	 * Check if a suitable IP exists
	 *   if it does, connect to it
	 *   if it doesn't, create an IP
	 * @param ap
	 */
	public void findOrCreateIP(IAP ap) {
		if (ap.arg!=null)
			return;

		IP ip = findIP(ap);
		if (ip == null) {
			ip = createIP(ap);
		}
		ap.arg = ip;
	}

	/**
	 * if a suitable exists, return it (with matched parameters)
	 * if not return null
	 * @param ap
	 * @return
	 */
	IP findIP(IAP ap) {			//TODO   restructure this function. Only handling of the actual iterated PE is different, everything else is the same for iterated and non-iterated PP
		TP tp = ap.getContainer().getTemplate();		// the template of the process where this AP is
		PP pp = ap.getTemplate();							// the template of this AP

		List<IP> candidates = this.extendedModel.getModel().TPtoIPs.get(ap.tp.getFullName());
		if (candidates == null)																// if no plausable IPs
			return null;

		if (pp.isIter) {				// handling of iterated AP
			int argSetIndex = pp.cont.parameters.indexOfValue(pp.iterated);			// the index of the iterated PE in the process argument list
			int argSize = ip.arguments.get(argSetIndex).args.size();					// the cardinality of the iterated AE
			int argIndex = ap.index;															// the index of the needed arg within the corresponding argument set
			IE arg = ip.arguments.get(argSetIndex).args.getValue(argIndex);				// the IE that should be used for this AP

			int argIterPos = ap.parameters.indexOfValue(pp.iterator);
			for (IP candidate : candidates) {												// check each IP candidate
				boolean matches = true;

				for (int i = 0; i < ap.parameters.size(); i++) {						// check all parameters
					if (i == argIterPos) {														// this is the iterated
						IAE argCandidate = (IAE) candidate.arguments.get(argIterPos);// the argument value in the candidate IP
						if (argCandidate.args.size()!=1 || argCandidate.args.getValue(0) != arg) { // the argCandidate has to have exactly one IE and it has to be the right one
							// check whether the lower/upper match
							boolean lowerMatch = (argCandidate.lowerArgs.size() == 0) || (argCandidate.lowerArgs.size() == 1 && argCandidate.lowerArgs.getValue(0) == arg) ; // lower bound is is empty or the right IE
							boolean upperMatch = argCandidate.upperArgs.values().contains(arg); // upper bound contains the required IE
							if (!argCandidate.args.isEmpty() || !lowerMatch || !upperMatch) {									// if lower/upper don't match
								matches = false;														// this is not a plausible IP
								break;
							}
						}
					} else {																			// this is a regular parameter
						PE param = ap.parameters.getValue(i);
						String paramName= param.id; 												// Parameter name in the template process
						int argPos = tp.parameters.indexOf(paramName);
						AE argMandatory = ap.getContainer().arguments.get(argPos);		// the argument value that has to be present
						//NOTE: double check whether here should be argPos or i		argPos is the position within the containing IP
						//																				i is the position within the candidate IP
						IAE argCandidate = (IAE) candidate.arguments.get(i);				// the argument value in the candidate IP
						if (!argMandatory.args.valueSet().equals(argCandidate.args.valueSet())) {			// the args don't match
							// check whether the lower/upper match
							boolean lowerMatch = argMandatory.args.values().containsAll(argCandidate.lowerArgs.values()); // lower bound is contained
							boolean upperMatch = argCandidate.upperArgs.values().containsAll(argMandatory.args.values()); // upper bound contains the set
							if (!argCandidate.args.isEmpty() || !lowerMatch || !upperMatch) {									// if lower/upper don't match
								matches = false;														// this is not a plausable IP
								break;
							}
						}
					}
				}
				if (matches) { // the candidate matches. Use it
					// before returning it, set the arguments which are not matched
					matchArguments(candidate, ap);
					return candidate;
				}
			}
			return null;	// no match is found

		} else {							// handling of regular AP
			for (IP candidate : candidates) {												// check each IP candidate
				boolean matches = true;

				for (int i = 0; i < ap.parameters.size(); i++) {						// check all parameters
					PE param = ap.parameters.getValue(i);
					String paramName= param.id; 												// Parameter name in the template process
					int argPos = tp.parameters.indexOf(paramName);
					AE argMandatory = ap.getContainer().arguments.get(argPos);		// the argument value that has to be present
					IAE argCandidate = (IAE) candidate.arguments.get(i);		// the argument value in the candidate IP
					if (!argMandatory.args.valueSet().equals(argCandidate.args.valueSet())) {			// the args don't match
						// check whether the lower/upper match
						boolean lowerMatch = argMandatory.args.values().containsAll(argCandidate.lowerArgs.values()); // lower bound is contained
						boolean upperMatch = argCandidate.upperArgs.values().containsAll(argMandatory.args.values()); // upper bound contains the set
						if (!argCandidate.args.isEmpty() || !lowerMatch || !upperMatch) {									// if lower/upper don't match
							matches = false;														// this is not a plausable IP
							break;
						}
					}
				}
				if (matches) { // the candidate matches. Use it
					// before returning it, set the arguments which are not matched
					matchArguments(candidate, ap);
					return candidate;
				}
			}
			return null;	// no match is found
		}
	}

	/**
	 * for these APs, the IP was newly created. When the AP is removed, the IP should be deleted
	 */
	Set<AP> createdIPs = new LinkedHashSet<AP>();

	/**
	 * create a suitable unbound IP
	 */
	IP createIP(IAP ap) {												// creating IPs is the same regardless of whether it is iterated
		IP newIP = new IP();
		newIP.id = StringUtils.uncapitalize(ap.argTemp.id) + rand.nextInt(10000); //WARNING: setting a bound might be limiting for some applications 

		newIP.setContainer(this.ip.getContainer());
		newIP.template = ip.getModel().getTemplate().PROCESS;  // set as the root process and then refine downto the wanted process

		ip.getModel().ips.put(newIP.id, newIP);
		ip.getModel().ipsRec.put(newIP.getFullName(), newIP);
		ip.getModel().upkeep();
		matchArguments(newIP, ap);


		List<TP> templates = new LinkedList<TP>();				// a list of TPs from the root TP to the needed TP
		TP temp = ap.argTemp;
		while (temp != ip.getModel().getTemplate().PROCESS) {
			templates.add(0, temp);
			temp = temp.zuper;
		}
		for (TP tp : templates) {										// instantize the proper type
			refineDown(newIP, tp);
			matchArguments(newIP, ap);
		}
		this.createdIPs.add(ap);

		return newIP;
	}

	/**
	 * Copy all arguments from the AE to the IP. The IP is either newly created or found to be appropriate
	 * @param ip
	 * @param ae
	 */
	void matchArguments(IP ip, IAP ap) {
		TP tp = ap.getContainer().getTemplate();										// the template of the container
		PP pp = ap.getTemplate();															// the template of this AP
		if (pp.isIter) {																		// handling of iterated AP
			int argSetIndex = pp.cont.parameters.indexOfValue(pp.iterated);		// the index of the iterated PE in the process argument list
			int argSize = ap.ip.arguments.get(argSetIndex).args.size();			// the cardinality of the iterated AE
			int argIndex = ap.index;														// the index of the needed arg within the corresponding argument set
			IE arg = ap.ip.arguments.get(argSetIndex).args.getValue(argIndex);		// the IE that should be used for this AP

			int argIterPos = ap.parameters.indexOfValue(pp.iterator);

			for (int i = 0; i < ip.arguments.size(); i++) {							// set all arguments
				if (i == argIterPos) {														// this is the iterated
					IAE argCandidate = (IAE) ip.arguments.get(argIterPos);		// the argument value in the candidate IP
					if (argCandidate.args.size()!=1 || argCandidate.args.getValue(0) != arg) { // the argCandidate has to have exactly one IE and it has to be the right one
						argCandidate.args = new ListMap<String, IE>();
						argCandidate.args.put(arg.id, arg);
					}
				} else {																			// this is a regular parameter
					PE param = ap.parameters.getValue(i);
					String paramName= param.id; 												// Parameter name in the template process
					int argPos = tp.parameters.indexOf(paramName);
					AE argMandatory = ap.getContainer().arguments.get(argPos);		// the argument value that has to be present
					IAE argCandidate = (IAE) ip.arguments.get(i);					// the argument value in the candidate IP
					if (!argMandatory.args.valueSet().equals(argCandidate.args.valueSet())) {			// the args don't match (but they are compatible (established before calling this function)
						argCandidate.args = new ListMap<String, IE>();
						argCandidate.args.putAll(0, argMandatory.args);
					}
				}
			}
		} else {																					// handling of regular AP
			for (int i = 0; i < ip.arguments.size(); i++) {							// set all arguments NOTE: arguments can be less than parameters, because this may not be a fully constructed IP
				PE param = ap.parameters.getValue(i);
				String paramName= param.id; 												// Parameter name in the template process
				int argPos = tp.parameters.indexOf(paramName);
				AE argMandatory = ap.getContainer().arguments.get(argPos);		// the argument value that has to be present
				IAE argCandidate = (IAE) ip.arguments.get(i);					// the argument value in the candidate IP
				if (!argMandatory.args.valueSet().equals(argCandidate.args.valueSet())) {			// the args don't match (but they are compatible (established before calling this function)
					argCandidate.args = new ListMap<String, IE>();
					argCandidate.args.putAll(0, argMandatory.args);
				}
			}
		}

	}


	/**
	 * drop the ICs which are not in the parent
	 */
	void dropICs(IP ip) {
		Set<TC> oldTCs = new LinkedHashSet<TC>(ip.template.consts.values());
		oldTCs.removeAll(ip.template.zuper.consts.values()); // the TCs to be removed

		for (TC tc : oldTCs) {
			ip.consts.remove(tc.id);
		}
	}

	void dropAEs(IP ip) {
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

	void dropAPs(IP ip) {
		List<PP> oldPPs = new ArrayList<PP>(ip.template.processes);
		oldPPs.removeAll(ip.template.zuper.processes); // the PPs to be removed

		for (PP pp : oldPPs) {												// FIXME iterated PPs have more than one AP. Should remove all
			if (pp.isIter) {
				List<IAP> toRemove = new LinkedList<IAP>();

				for (int i = 0; i < ip.processes.size(); i++) {
					IAP ap = (IAP)ip.processes.get(i);
					if (ap.template == pp) {
						toRemove.add(ap);
//						ip.processes.remove(i);
//						if (createdIPs.contains(ap)) {
//							removeIP(ap);
//						} else {
//							disconnectIP(ap);
//						}
//						break;
					}
				}
				for (IAP ap : toRemove) {
					ip.processes.remove(ap);
					this.refiners.remove(ap.arg.getFullName());
					if (createdIPs.contains(ap)) {
						removeIP(ap);
					} else {
						disconnectIP(ap);
					}
				}

			} else {
				for (int i = 0; i < ip.processes.size(); i++) {
					IAP ap = (IAP)ip.processes.get(i);
					if (ap.template == pp) {
						ip.processes.remove(i);
						this.refiners.remove(ap.arg.getFullName());
						if (createdIPs.contains(ap)) {
							removeIP(ap);
						} else {
							disconnectIP(ap);
						}
						break;
					}
				}
			}
		}
	}

	/**
	 * remove the IP from this AP because it was created when the AP was created
	 * @param ap
	 */
	void removeIP(IAP ap) {
		//FIXME when removing IP, should remove all component IPs, or disconnect them
		ip.getModel().ips.remove(ap.arg.id);
		ip.getModel().ipsRec.remove(ap.arg.getFullName());
		ip.getModel().upkeep();
	}

	/**
	 * Disconnect all arguments of the IP from this AP.
	 * When the IP was matched to this AP, some arguments were matched. This effect should be undone now.
	 * @param ap
	 */
	void disconnectIP(IAP ap) {
		for (AE ae : ap.arg.arguments) {
			if (((IAE)ae).upperArgs.size() > 0) {			// if the arg has an upper bound, it was matched
				ae.args = new ListMap<String, IE>();
			}
		}
	}

	/**
	 * still not sure whether I need this
	 */
	void dropIQs(IP ip) {
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
