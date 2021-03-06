package search.heuristic;

import java.io.IOException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import jmetal.core.Variable;
import jmetal.util.JMException;

import org.antlr.runtime.RecognitionException;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import search.ModelEnumerator;
import struct.Interval;
import task.Task;
import temp.ExtendedModel;
import temp.IQGraph;
import temp.IVNode;
import temp.Output;
import temp.OutputCons;
import util.ListMap;
import xml.OutputSpec;
import xml.Parameter;
import temp.ICNode;

/**
 * Class used to encode and decode a simple integer representation of a complete model given an incomplete one.
 * To be used in the context of the problem of search heuristics.
 * 
 * @author jovant
 */


class EnumeratingCodec extends HeuristicCodec {

	private ModelEnumerator search;
	protected ListMap<String,ICNode> params;
	protected ListMap<String,IVNode> initials;
	protected ListMap<String,OutputCons> outputs;
	protected double pCompLow, pCompHigh, fCompLow, fCompHigh;
	
	public static final Logger logger = LoggerFactory.getLogger(EnumeratingCodec.class);

	public EnumeratingCodec() {
		extendedModel = null;
		code = null; 
		internalEnumeratingCodec = this;
		enumerate = true;
	}

	@Override
	public LinkedList<Integer> encode(ExtendedModel extendedModel, OutputSpec outputSpec) {
		logger.info("Enumerating. This may take a while.");
		this.extendedModel = extendedModel.copy();
		search = new ModelEnumerator(extendedModel);
		int counter = 0;
		code = new LinkedList<Integer>();
		params = new ListMap<String,ICNode>(); 
		initials = new ListMap<String,IVNode>();
		outputs = new ListMap<String,OutputCons>();
		
		try {
			
			//Extract initial values
			//WARNING: Risk! Variables might not be reachable in general but we will still be fitting
			for(String iName : extendedModel.getModel().allVars.keySet()) {
				IVNode ivn = new IVNode(extendedModel.getModel().allVars.get(iName));
				if (ivn.var.initial == null) {
					initials.put(iName, ivn);
				}
			}
			
			//Extract output parameters
			for (Parameter par : outputSpec.constants) {
				OutputCons cons = new OutputCons();
				cons.name = par.name;
				if (par.fit) {
					cons.value = null;
					cons.fit_start = par.initial;
					cons.fit_range = new Interval(par.lower, par.upper);
					outputs.put(cons.name, cons);
				}
			}
		
			pCompLow = fCompLow = Integer.MAX_VALUE;
			pCompHigh = fCompHigh = 0;
			
			//Have to go trough all models in order to find all referenced parameters
			while(search.hasNextModel()) {
				ExtendedModel currentModel = search.nextModel();
				
				//Make sure that we are optimizing the parameters that are actually used in the models
				IQGraph graph = new IQGraph(currentModel.getModel());
				Output currentOutput = new Output(outputSpec, graph);
				
				//Protect against no parameters because Java
				//Extract parameters
				if (currentOutput.graph.unknownParameters.size() != 0) params.putAll(currentOutput.graph.unknownParameters);
				
				int pComp = currentOutput.graph.reachParameters.size();
				
				if(pComp<pCompLow) pCompLow = pComp;
				if(pComp>pCompHigh) pCompHigh = pComp;
				
				//TODO: is this the best way to do this?
				int fComp = 0;
				for(IVNode var : currentOutput.graph.reachVariables.valueList()) fComp += var.inputIQs.size();
				
				if(fComp<fCompLow) fCompLow = pComp;
				if(fComp>fCompHigh) fCompHigh = pComp;
						
			
				//Take care of the number of models
				counter++;
				
				if(counter%10000 == 0) System.out.print(counter + " ");
			}
			

			
		} catch (ConfigurationException | IOException | InterruptedException
				| RecognitionException e) {
			System.out.println("Coding error.");
		}
		logger.info("\nEnumerated " + counter + " candidate models.");
		logger.info("Encoding");
		code.add(counter);
		return code;

	}

	@Override
	public ExtendedModel decode(Variable[] genotype) {
		
		if(this.extendedModel == null){
			throw new RuntimeException("Enumerating codec not initialized. Run encode first.");
		}
		
		search = new ModelEnumerator(extendedModel.copy()); //must copy because of the ref states

		int modelNum = 0;
		try {
			modelNum = (int)genotype[0].getValue();
		} catch (JMException e) {
			System.out.println("Cannot get code for gene.");
		}
		try {
			while (search.getCounter() < modelNum - 1) {
				search.nextModel();
			}
			return search.nextModel().copy();
		} catch (ConfigurationException | IOException | InterruptedException
				| RecognitionException | NoSuchElementException e ) {
			System.out.println("Model generation failed");
			return null;
		}
	}

}
