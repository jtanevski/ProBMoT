package traverse;

import java.util.*;

import struct.*;
import struct.inst.*;
import struct.temp.*;
import util.*;

public interface Validator<T extends Structure> {
	public void validate(T struct);
}

class AEValidator
		implements Validator<AE> {
	public void validate(AE struct) {
		AE instance = (AE) struct;
		PE template = instance.template;

		if (instance.args.size() < template.card.getLower() || instance.args.size() > template.card.getUpper())
			throw new ParsingException(String.format(Errors.ERRORS[39], instance.ip.getIdS(), instance.getIdI(),
					instance.args.size(), template.getIdS(), template.card.toString()));
		for (IE ie : instance.args.values()) {
			if (!ie.template.isEqualOrSuccessorOf(template.te))
				throw new ParsingException(String.format(Errors.ERRORS[40], instance.ip.getIdS(), instance.getIdI(), ie
						.getIdS(), ie.template.getIdS(), template.getIdS(), template.te.getIdS()));
		}
	}
}


class IAEValidator
implements Validator<IAE> {
	public void validate(IAE struct) {
		IAE instance = (IAE) struct;
		PE template = instance.template;

		if (instance.args.isEmpty() && (instance.isUpperAll || !instance.upperArgs.isEmpty())) {			// it has lower/upper
			if (instance.upperArgs.size() < template.card.getLower() || instance.lowerArgs.size() > template.card.getUpper())
				throw new RuntimeException("Too many obligatory or too few optional arguments");
		} else {																														// it is a normal AE
			if (instance.args.size() < template.card.getLower() || instance.args.size() > template.card.getUpper())
				throw new ParsingException(String.format(Errors.ERRORS[39], instance.ip.getIdS(), instance.getIdI(),
						instance.args.size(), template.getIdS(), template.card.toString()));
			for (IE ie : instance.args.values()) {
				if (!ie.template.isEqualOrSuccessorOf(template.te))
					throw new ParsingException(String.format(Errors.ERRORS[40], instance.ip.getIdS(), instance.getIdI(), ie
							.getIdS(), ie.template.getIdS(), template.getIdS(), template.te.getIdS()));
			}
		}
	}
}


class APValidator
		implements Validator<AP> {
	public void validate(AP struct) {
		AP instance = (AP) struct;
		PP template = (PP) instance.template;
		if (instance instanceof IAP) {
			if (instance.arg == null && ((IAP)instance).argTemp != null) {
				//NOTE: here I should check whether the argument template is compatible but for now I skip this
				// should implement as soon as possible
				return;
			}
		}
		// Validate against template
		if (!instance.arg.template.isEqualOrSuccessorOf(template.tp))
			throw new ParsingException(String.format(Errors.ERRORS[41], instance.ip.getIdS(), instance.getIdI(),
					instance.arg.getIdS(), instance.arg.template.getIdS(), template.tp.getIdS()));

		// NOTE: This is probably (think so) obsolete, because the IP is checked against the template in the normal
		// validation of properties
		/*
		 * for (int i = 0; i < template.parameters.size(); i++) { PE pe = template.parameters.get(i); AE ae =
		 * instance.arg.arguments.get(i);
		 * 
		 * if (ae.args.size() < pe.card.getLower() || ae.args.size() > pe.card.getUpper()) throw new
		 * ParsingException(String.format(Errors.ERRORS[42], instance.ip.getIdS(), instance.getIdI(),
		 * instance.arg.getIdS(), i, ae.args.size(), pe.card.toString())); for (IE ie : ae.args.list) { if
		 * (!ie.template.isEqualOrSuccessorOf(pe.te)) throw new RuntimeException(); } }
		 */

		// Validate against containing IP
		IP ip = instance.getContainer();
		TP tp = ip.getTemplate();

		for (int i = 0; i < template.parameters.size(); i++) {
			String argName = template.parameters.getKey(i);
			if (template.isIter && argName.equals(template.iterator.id)) { // FIXME: check this code!! (No idea why I wrote
																								// this, but seems to work fine :) // if it is
																								// the iterated argument
				int argPos = tp.parameters.indexOf(template.iterated.id);
				AE ipArg = ip.arguments.get(argPos);
				AE instanceArg = instance.arg.arguments.get(i);

				IE arg = ipArg.args.getValue(instance.index);
				if (instanceArg.args.size() > 1)
					throw new ParsingException(String.format(Errors.ERRORS[60], ip.getIdS(), instance.getIdI(), instance.arg
							.getIdS(), argPos, instanceArg.args.size()));
				if (arg != instanceArg.args.getValue(0))
					throw new ParsingException(String.format(Errors.ERRORS[43], ip.getIdS(), instance.getIdI(), instance.arg
							.getIdS(), i, instanceArg.args.getValue(0).getIdS(), arg.getIdS()));
			} else { // if it is a normal argument
				int argPos = tp.parameters.indexOf(argName);
				AE ipArg = ip.arguments.get(argPos);
				AE instanceArg = instance.arg.arguments.get(i);
				if (!ipArg.args.valueSet().equals(instanceArg.args.valueSet()))
					throw new ParsingException(String.format(Errors.ERRORS[43], ip.getIdS(), instance.getIdI(), instance.arg
							.getIdS(), i, names(instanceArg.args.values()).toString(), names(ipArg.args.values()).toString()));
			}
		}

		// if (! ifValue.arg.template.isSuccesorOf(tfValue.tp))
		// throw new RuntimeException();
		// for (int argIndex = 0; argIndex < tfValue.parameters.size(); argIndex++) {
		// String argName = tfValue.parameters.find(argIndex);
		// }
	}

	List<String> names(Collection<IE> ies) {
		List<String> nameList = new ArrayList<String>();
		for (IE ie : ies) {
			nameList.add(ie.getIdS());
		}
		return nameList;
	}
}

class TEValidator
		implements Validator<TE> {

	@Override
	public void validate(TE struct) {
		// check duplicate identifiers (vars and consts cannot have same IDs)
		for (String varName : struct.vars.keySet()) {
			if (struct.consts.containsKey(varName)) {
				throw new ParsingException(String.format(Errors.ERRORS[59], struct.getIdS(), varName));
			}
		}
	}
}


class PPValidator implements Validator<PP> {

	public  void validate(PP struct) {
		TP procRef = struct.tp;
		TP tp = struct.cont;
		if (struct.parameters.size() != procRef.parameters.size()) // TP definition and reference don't match in
			// parameter count
			throw new ParsingException(String.format(Errors.ERRORS[21], tp.getIdS(), struct.getIdI(), procRef.getIdS(),
					struct.parameters.size(), procRef.parameters.size()));
		for (int i = 0; i < struct.parameters.size(); i++) { // Traverse all parameters (both in Decl and Ref)
			PE declPE = procRef.parameters.getValue(i);
			PE refPE = struct.parameters.getValue(i);

			if (!refPE.te.isEqualOrSuccessorOf(declPE.te)) // Decl and Ref have to have the same Type
				throw new ParsingException(String.format(Errors.ERRORS[22], tp.getIdS(), struct.getIdI(), procRef
						.getIdS(), i, refPE.getIdS(), refPE.te.getIdS(), declPE.getIdS(), declPE.te.getIdS()));
			if (!refPE.card.contains(declPE.card))
				throw new ParsingException(String.format(Errors.ERRORS[23], tp.getIdS(), struct.getIdI(), procRef
						.getIdS(), i, refPE.getIdS(), refPE.card.toString(), declPE.getIdS(), declPE.card.toString()));
		}
	}
}


