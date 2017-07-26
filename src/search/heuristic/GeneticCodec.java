package search.heuristic;

import java.util.Collection;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import jmetal.core.Variable;
import jmetal.util.JMException;
import search.ComponentProcessRefiner;
import search.IPRecursiveRefiner;
import search.ModelEnumerator;
import search.ParameterMatcher;
import struct.inst.IP;
import temp.ExtendedModel;
import traverse.Traverse;
import xml.OutputSpec;

/**
 * Class used to encode and decode from a genotype represented as an array of integers to a phenotype represented as a structurally complete model to be optimized.
 * To be used in the context of the problem of search heuristics based on genetic algorithms.
 * 
 * @author jovant
 */


//TODO: Maybe the parameters of the incomplete model should also be part of the code
//what about the output parameters? see VariableSuperSet
//Just use VariableSuperSet and actually go trough all models to find out all unknown parameters
//The signature for the encode function should include OutputSpec. The output should be generalized to list of Number that includes Integer and Double?
public class GeneticCodec extends HeuristicCodec {

	private LinkedList<Integer> longCode;
	private LinkedList<Integer> longCodeRep;
	private BiMap<Integer,Integer> codeMap;
	 //the values stored in code represent the upper boundaries for optimization

	public static final Logger logger = LoggerFactory.getLogger(GeneticCodec.class);
	
	public GeneticCodec() {
		extendedModel = null;
		code=null; 
		internalEnumeratingCodec = null;
	}
	
	@Override
	public LinkedList<Integer> encode(ExtendedModel extendedModel, OutputSpec outputSpec) {

		//Extract information about unknown parameters, initials and outputs by enumeration 
		internalEnumeratingCodec = new EnumeratingCodec();
		internalEnumeratingCodec.encode(extendedModel.copy(), outputSpec);
		
		// Create long template for genotype encoding, and list of lengths of code representation on each level
		logger.info("Encoding");
		this.extendedModel = extendedModel.copy();
		Collection<IP> topLevelIPs;

		topLevelIPs = extendedModel.getModel().topLevelIPs.values();	
		longCode = new LinkedList<Integer>();
		longCodeRep = new LinkedList<Integer>();

		for (IP ip : topLevelIPs) {
			IPRecursiveRefiner ipr = new IPRecursiveRefiner(extendedModel, ip, false);
			IPDescription ipRepresentation = geneSeek(extendedModel, ipr);
			longCode.addAll(ipRepresentation.longCode);
			longCodeRep.addAll(ipRepresentation.longCodeRep);
		}
				
		//Compress. Create compact representation.
		int mapIndex=0;
		code = new LinkedList<Integer>();
		codeMap = HashBiMap.create();
		
		int total = 1;
		
		for(int i=0; i<longCode.size(); i++){
			if (longCode.get(i)!=1){
				code.add(longCode.get(i));
				total *= longCode.get(i);
				codeMap.put(mapIndex++, i);
			}
		}
		
		if(total != internalEnumeratingCodec.code.get(0)) System.out.println("WARNING: Encoding contains spurious models due to the presence of unbalanced number of subprocesses in the incomplete model. There is bias towards selecting alternative processes with maximal depth.");
		
		//System.out.println(code);
		//System.out.println(longCode);
		//System.out.println(longCodeRep);
		return code;

	}

	private IPDescription geneSeek(ExtendedModel em, IPRecursiveRefiner ipr) {
		IPDescription result = new IPDescription();
		
		int alts = ipr.state.getCount();
		result.longCode.add(alts); //this process has alts alternatives. has to be at least one

		LinkedList<Integer> nextLevelMax = new LinkedList<Integer>();
		LinkedList<Integer> nextLevelRepMax = new LinkedList<Integer>();
		
		//size of additional integers needed for representation on each level is equal to the maximal number of refiners present in the refinements
		if (!ipr.state.hasNextState()) { // if there is only one alternative
			result.longCodeRep.add(ipr.refiners.size()); // it must have exactly this many subprocesses
			for (IPRecursiveRefiner ipr2 : ipr.refiners.valueList()) { //and these are the subprocess representations
				IPDescription ll1 = geneSeek(em, ipr2);
				result.longCode.addAll(ll1.longCode);
				result.longCodeRep.addAll(ll1.longCodeRep);
			}
		} else { // there is more than one alternative
			int maxsubp = 0; //each of the alternatives can have different number of subprocesses
			//ipr.firstState();
			while(ipr.state.hasNextState()){ 
			ipr.nextStateSelf(); //refine self
			if (ipr.refiners.size() > maxsubp) maxsubp = ipr.refiners.size();
							
			for(IPRecursiveRefiner ipr2 : ipr.refiners.valueList()){ 
				IPDescription ll1 = geneSeek(em, ipr2);
				//Aggregation: compare to this level max and maximize, subprocesses may contain different number of subprocesses of their own and different number of alternatives for each subprocess
				if(nextLevelMax.size() == 0){
					nextLevelMax.addAll(ll1.longCode);
				} else {
					for(int i=0; i< Math.min(nextLevelMax.size(), ll1.longCode.size()); i++){
						nextLevelMax.set(i, Math.max(nextLevelMax.get(i), ll1.longCode.get(i)));
					}
					if(nextLevelMax.size() < ll1.longCode.size()){
						nextLevelMax.addAll(ll1.longCode.subList(nextLevelMax.size(), ll1.longCode.size()));
					}
				}
				
				if(nextLevelRepMax.size() == 0){
					nextLevelRepMax.addAll(ll1.longCodeRep);
				} else {
					for(int i=0; i< Math.min(nextLevelRepMax.size(), ll1.longCodeRep.size()); i++){
						nextLevelRepMax.set(i, Math.max(nextLevelRepMax.get(i), ll1.longCodeRep.get(i)));
					}
					if(nextLevelRepMax.size() < ll1.longCodeRep.size()){
						nextLevelRepMax.addAll(ll1.longCodeRep.subList(nextLevelRepMax.size(), ll1.longCodeRep.size()));
					}
				}
				
			}
		}
		result.longCode.addAll(nextLevelMax);
		result.longCodeRep.add(maxsubp);
		result.longCodeRep.addAll(nextLevelRepMax);
	}
		
		return result;
	}
	
	private LinkedList<Integer> getTopGene(LinkedList<Integer> code){
		LinkedList<Integer> result = new LinkedList<Integer>();
		int geneSize = code.getFirst();
		int toGo=geneSize;
		result.add(geneSize);
		int offset=1;
		while(toGo > 0){
			if(code.get(offset) < 1){
				result.add(code.get(offset++));
				
			} else {
				LinkedList<Integer> temp = getTopGene(new LinkedList<Integer>(code.subList(offset, code.size())));
				result.addAll(temp);
				offset+=temp.size();
			}
			toGo--;
		}
		return result;
	}
	
	
	@Override
	public ExtendedModel decode(Variable[] genotype){
		if(this.extendedModel == null){
			throw new RuntimeException("Genetic codec not initialized. Run encode first.");
		}
		
		ExtendedModel extendedModel = this.extendedModel.copy(); //must do copy because of the ref states
		
		//Decompress. Convert genotype to long representation.
		LinkedList<Integer> longGenotype = new LinkedList<Integer>();
		longGenotype.addAll(longCode);
		
		for (int gene = 0; gene < genotype.length; gene++) {
			try {
				longGenotype.set(codeMap.get(gene), (int) genotype[gene].getValue());
			} catch (JMException e) {
				System.out.println("Cannot get code for gene.");
			}
		}
		
		Collection<IP> topLevelIPs;

		topLevelIPs = extendedModel.getModel().topLevelIPs.values();
		
		
		//generate refiners for the model and set states according to the longGenotype
		LinkedList<Integer> lcR = new LinkedList<Integer>();
		lcR.addAll(longCodeRep);
		LinkedList<Integer> geneCodeRep = getTopGene(lcR);
//		int offset=0;	
		//passing trough in the same way as in the encoding
		for (IP ip : topLevelIPs) {
			IPRecursiveRefiner ipr = new IPRecursiveRefiner(extendedModel, ip, false);
			LinkedList<Integer> geneCode = new LinkedList<Integer>(longGenotype.subList(0, geneCodeRep.size()));
			setIPRefiners(ipr,geneCode,geneCodeRep);
//			offset+=geneCodeRep.size();
			longGenotype = new LinkedList<Integer>(longGenotype.subList(geneCode.size(), longGenotype.size()));
			lcR = new LinkedList<Integer>(lcR.subList(geneCodeRep.size(), lcR.size()));
			if(lcR.size() > 0) geneCodeRep = getTopGene(lcR);
		}
		
		//copy the extendedModel using the state of the refiners
			
		//ParameterMatcher matcher = new ParameterMatcher(extendedModel.copy());		
		//ComponentProcessRefiner refiner = new ComponentProcessRefiner(matcher.nextModel());
		ExtendedModel toReturn = null;
		try {
			 toReturn = (new ModelEnumerator(extendedModel.copy())).nextModel();
		} catch (Exception e) {
			
		}
		
		return toReturn; 
	}

	private int setIPRefiners(IPRecursiveRefiner ipr, LinkedList<Integer> geneCode, LinkedList<Integer> geneCodeRep) {
		int selfAlternative = geneCode.getFirst();
		ipr.firstState();

		while(selfAlternative-- > 1 && ipr.state.hasNextState()){ // if larger than number of alternatives then collapse to last model. We introduce bias to the last alternative here
			ipr.nextStateSelf();
		}
		
		int subs = Math.min(geneCodeRep.getFirst(), ipr.refiners.size());
		int offset = 1;

		//match next level rep with number of subprocesses 
		for(int i=0; i<subs; i++){
			offset += setIPRefiners(ipr.refiners.get(i),new LinkedList<Integer>(geneCode.subList(offset, geneCode.size())), new LinkedList<Integer>(geneCodeRep.subList(offset, geneCodeRep.size())));
		}
		// cutoff the ones not needed
		for(int i=0; i<geneCodeRep.getFirst()-ipr.refiners.size(); i++){
			offset += getIPLength(new LinkedList<Integer>(geneCodeRep.subList(offset, geneCodeRep.size())));
		}
		//we are done with this
		
		return offset;
	}

	private int getIPLength(LinkedList<Integer> geneCodeRep) {
		int size = geneCodeRep.getFirst();
		int offset=1;
		if(size != 0){
			for(int i=0;i<size; i++){
				offset += getIPLength(new LinkedList<Integer>(geneCodeRep.subList(offset, geneCodeRep.size())));
			}
		}
		return offset;
	}

}