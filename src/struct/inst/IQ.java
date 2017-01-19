package struct.inst;

import java.util.*;

import org.apache.commons.lang3.*;

import struct.*;
import struct.temp.*;
import util.*;
import equation.*;

/** Instance Equation **/
public class IQ
		extends Instance {

	/* ID */
	public String id;

	/* Container */
	public IP cont;

	/* Content */

	public EQType type;

	IV lhs;
	Node rhs;

	public Map<String, IV> ieVars = new LinkedHashMap<String, IV>();
	public Map<String, IC> ieConsts = new LinkedHashMap<String, IC>();
	public Map<String, IC> ipConsts = new LinkedHashMap<String, IC>();

	/* Reference */

	/* Back-reference */

	/* Template */
	public transient TQ template;
	public boolean isIter;					// whether it is built from an iterated TQ (must be equal to template.isIster)
	public int index;							// if it is iterated, then the index of this IQ in the list of all IQs; meaningless otherwise

	/* Other */

	public static List<IQ> createIQs(TQ tq, IP ip) {
		List<IQ> iqs = new LinkedList<IQ> ();
		if (!tq.isIter) { // Normal
			IQ iq = new IQ();

			iq.cont = ip;
			iq.template = tq;
			iq.type = tq.type;

			// LHS
			PEVar peVarLHS = tq.lhs;
			AE ae = ip.arguments.get(peVarLHS.pe.idI);
			if (ae.args.size() != 1) {
				throw new ParsingException(String.format(Errors.ERRORS[57], peVarLHS.pe.id, ae.args.size()));
			}
			IV iv = ae.args.getValue(0).vars.get(peVarLHS.var.id);
			iq.setLHS(iv);

			// RHS
			Node o = (Node) SerializationUtils.clone(tq.rhs);

			iq.rhs = iq.substituteReferenceNodes(o);
			
			iqs.add(iq);

		} else { // Iterated
			int iterIndex = tq.iterated.idI;
			AE iterAE = ip.arguments.get(iterIndex);

			for (int i = 0; i < iterAE.args.size(); i++) {
				IE ie = iterAE.args.getValue(i);
				
				
				
				IQ iq = new IQ();

				iq.cont = ip;
				iq.template = tq;
				iq.type = tq.type;
				
				iq.isIter = true;
				iq.index = i;

				// LHS
				PEVar peVarLHS = tq.lhs;
				AE ae = ip.arguments.get(peVarLHS.pe.idI);

				if (peVarLHS.pe == tq.iterator) {
					IV iv = iterAE.args.getValue(i).vars.get(peVarLHS.var.id);
					iq.setLHS(iv);
				} else if (ae.args.size() == 1) {
					IV iv = ae.args.getValue(0).vars.get(peVarLHS.var.id);
					iq.setLHS(iv);
				} else {
					throw new ParsingException(String.format(Errors.ERRORS[57], peVarLHS.pe.id, ae.args.size()));	
				}

				// RHS
				Node o = (Node) SerializationUtils.clone(tq.rhs);

				iq.rhs = iq.substituteReferenceNodes(o);
				
				iqs.add(iq);
			}
		}
		return iqs;

		// System.out.println(); // for debugging
	}

	protected IQ() {
	}


	private Node substituteReferenceNodes(Node node) {
		if (node instanceof PEAttrRef) {
			if (node.getClass() == PEVarRef.class) {
				PEVar peVar = template.peVars.get(((PEVarRef) node).id);
				AE ae = cont.arguments.get(peVar.pe.idI);

				if (peVar.pe == this.template.iterator) {
					ae = cont.arguments.get(template.iterated.idI);
					IV iv = ae.args.getValue(this.index).vars.get(peVar.var.id);
					ieVars.put(iv.getFullName(), iv);
//					AEVar aeVar = ieVars.get(aeVarName);
//					if (aeVar == null) {
//						aeVar = new AEVar(peVar, ae, iv);
//						ieVars.put(aeVarName, aeVar);
//					}
					AEVarRef newRef = new AEVarRef(iv);

					//PEVarRef peVarRef = (PEVarRef) node;
					// peVarRef.parent.setChild(peVarRef.index, newRef);
					return newRef;
			
				} else if (ae.args.size() == 1) {
					IV iv = ae.args.getValue(0).vars.get(peVar.var.id);
//					String aeVarName = iv.ie.id + "." + iv.id;
					ieVars.put(iv.getFullName(), iv);
//					AEVar aeVar = ieVars.get(aeVarName);
//					if (aeVar == null) {
//						aeVar = new AEVar(peVar, ae, iv);
//						ieVars.put(aeVarName, aeVar);
//					}
					AEVarRef newRef = new AEVarRef(iv);

					PEVarRef peVarRef = (PEVarRef) node;
					// peVarRef.parent.setChild(peVarRef.index, newRef);
					return newRef;

				} else {
					throw new ParsingException(String.format(Errors.ERRORS[57], peVar.pe.id, ae.args.size()));
				}


			} else if (node.getClass() == PEConstRef.class) {
				PEConst peConst = template.peConsts.get(((PEConstRef) node).id);
				AE ae = cont.arguments.get(peConst.pe.idI);
				
				if (peConst.pe == this.template.iterator) {
					ae = cont.arguments.get(template.iterated.idI);
					IC ic = ae.args.getValue(this.index).consts.get(peConst.cons.id);
//					String aeConstName = ic.cont.getIdS() + "." + ic.id;
					ieConsts.put(ic.getFullName(), ic);
//					AEConst aeConst = ieConsts.get(aeConstName);
//					if (aeConst == null) {
//						aeConst = new AEConst(peConst, ae, ic);
//						ieConsts.put(aeConstName, aeConst);
//					}
					AEConstRef newRef = new AEConstRef(ic);

					PEConstRef peConstRef = (PEConstRef) node;
					// peConstRef.parent.setChild(peConstRef.index, newRef);
					return newRef;
				
				} else if (ae.args.size() == 1) {
					IC ic = ae.args.getValue(0).consts.get(peConst.cons.id);
//					String aeConstName = ic.cont.getIdS() + "." + ic.id;
					ieConsts.put(ic.getFullName(), ic);
//					AEConst aeConst = ieConsts.get(aeConstName);
//					if (aeConst == null) {
//						aeConst = new AEConst(peConst, ae, ic);
//						ieConsts.put(aeConstName, aeConst);
//					}
					AEConstRef newRef = new AEConstRef(ic);

					PEConstRef peConstRef = (PEConstRef) node;
					// peConstRef.parent.setChild(peConstRef.index, newRef);
					return newRef;
				} else {
					throw new ParsingException(String.format(Errors.ERRORS[57], peConst.pe.id, ae.args.size()));
					
				}

			} else if (node.getClass() == TCRef.class) {
				TC tc = template.tcs.get(((TCRef) node).id);

				IC ic = cont.consts.get(tc.id);
				ipConsts.put(ic.getFullName(), ic);
				ICRef newRef = new ICRef(ic);

				TCRef tcRef = (TCRef) node;
				// tcRef.parent.setChild(tcRef.index, newRef);
				return newRef;

			} else {
				throw new ParsingException(String.format(Errors.ERRORS[58], node.getClass(), Arrays.toString(new Class[] {
						PEVarRef.class, PEConstRef.class, TCRef.class })));
			}

		} else {
			for (int i = 0; i < node.getChildCount(); i++) {
				node.setChild(i, substituteReferenceNodes(node.getChild(i)));
			}
			return node;
		}
	}

	public IV getLHS() {
		return this.lhs;
	}
	
	public void setLHS(IV iv) {
		this.lhs = iv;
		this.id = iv.getFullName();
	}
	
	public Node getRHS() {
		return this.rhs;
	}
	
	@Override
	public String getIdS() {
		return this.id;
	}

	@Override
	public void setIdS(String id) {
		this.id = (String) id;
	}

	@Override
	public IP getContainer() {
		return this.cont;
	}

	@Override
	public void setContainer(Structure container) {
		this.cont = (IP) container;
	}

	@Override
	public TQ getTemplate() {
		return this.template;
	}

	@Override
	public void setTemplate(Template template) {
		this.template = (TQ) template;
	}
	
	public String toString() {
		return lhs.getFullName() + ((type == EQType.Differential) ? "'" : "") + "=" + rhs.toString();
	}

	@Override
	public void restoreTemplate() {
		// TODO Later
	}
}
