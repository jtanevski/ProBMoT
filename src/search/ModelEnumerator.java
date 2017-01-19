package search;

import java.io.*;
import java.util.*;

import org.antlr.runtime.*;
import org.apache.commons.configuration.*;
import org.slf4j.*;

import fit.*;

import struct.temp.*;
import temp.*;
import util.*;
import xml.*;

public class ModelEnumerator {
	public static final Logger logger = LoggerFactory.getLogger(ModelEnumerator.class);

	ExtendedModel extendedModel;

	List<Dataset> datasets;
	String timeColumn;
	Map<String, Integer> varIndex;
	Map<String, Integer> uncIndex;


	ModelRefiner refiner;
	ExtendedModel refinedModel;

	ParameterMatcher matcher;
	ExtendedModel matchedModel;

	ComponentProcessRefiner processRefiner;
	ExtendedModel completeModel;

	private boolean fit;
	private FitterOld fitter;
	private String logDirpath;

	private OutputSpec outputSpec;

	private int counter;

	public ModelEnumerator(ExtendedModel extendedModel, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex, Map<String, Integer> uncIndex, boolean fit, FitterOld fitter, String logDirpath, OutputSpec outputSpec) {
		this.extendedModel = extendedModel;

		this.datasets = datasets;
		this.timeColumn = timeColumn;
		this.varIndex = varIndex;
		this.uncIndex = uncIndex;
		this.fit = fit;
		this.fitter = fitter;

		this.logDirpath = logDirpath;

		this.outputSpec = outputSpec;

		refiner = new ModelRefiner(extendedModel);
		refinedModel = refiner.nextModel();

		matcher = new ParameterMatcher(refinedModel);
		matchedModel = matcher.nextModel();

		processRefiner = new ComponentProcessRefiner(matchedModel);
		completeModel = null;

		counter = 0;
	}

	public boolean hasNextModel() {
		if (processRefiner.hasNextModel())
			return true;
		else if (matcher.hasNextModel())
			return true;
		else if (refiner.hasNextModel())
			return true;
		else
			return false;
	}

	public ExtendedModel nextModel() throws IOException, InterruptedException, ConfigurationException, RecognitionException {
		if (processRefiner.hasNextModel()) {
			completeModel = processRefiner.nextModel();
		} else if (matcher.hasNextModel()) {
			matchedModel = matcher.nextModel();

			processRefiner = new ComponentProcessRefiner(matchedModel);
			completeModel = processRefiner.nextModel();
		} else if (refiner.hasNextModel()) {
			refinedModel = refiner.nextModel();

			matcher = new ParameterMatcher(refinedModel);
			matchedModel = matcher.nextModel();

			processRefiner = new ComponentProcessRefiner(matchedModel);
			completeModel = processRefiner.nextModel();
		} else {
			throw new NoSuchElementException();
		}

		counter++;
		ExtendedModel toReturn;
		if (this.fit) {
			try {
				Output output = new Output(outputSpec, new IQGraph(completeModel.getModel()));
				toReturn = fitter.fit(output, this.datasets, this.timeColumn, this.varIndex, this.uncIndex, logDirpath + "/model-" + counter);
			} catch (FailedSimulationException ex) {
				toReturn = this.completeModel;
				toReturn.setSuccessful(false);
			}
		} else {
			toReturn = this.completeModel;
		}

		return toReturn;
	}

	public int getCounter() {
		return this.counter;
	}


	/**
	 * This method generates and discards all models and returns the number of generated models. ModelEnumerator in is useless afterwards.
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws ConfigurationException
	 * @throws RecognitionException
	 */
	public int count() throws ConfigurationException, IOException, InterruptedException, RecognitionException {
		while (this.hasNextModel()) {
			this.nextModel();
		}
		return this.getCounter();
	}
	/*
	public void enumerate(boolean fit)
			throws InterruptedException, IOException {

		ModelRefiner refiner;
		ExtendedModel refinedModel = null;

		ParameterMatcher matcher = null;
		ExtendedModel matchedModel = null;

		ComponentProcessRefiner processRefiner;
		ExtendedModel completeModel;

		long counter = 0;

		refiner = new ModelRefiner(extendedModel);
		do {
			refinedModel = refiner.nextModel();
			// System.out.println(refinedModel);

			matcher = new ParameterMatcher(refinedModel);
			do {
				matchedModel = matcher.nextModel();
				// System.out.println(matchedModel);

				processRefiner = new ComponentProcessRefiner(matchedModel);
				do {
					completeModel = processRefiner.nextModel();
					try {
						if (fit) {
							ExtendedModel fittedModel = MSC.MSC.fit(completeModel.getModel(), this.datasets, this.timeColumn, varIndex, null);
							System.out.println("Model #" + counter);
							System.out.println(fittedModel);
							// System.err.println("Model #" + counter + " written");
						} else {
							System.out.println("Model #" + counter);
							System.out.println(completeModel);
						}
						counter++;
					} catch (NumberFormatException ex) {
						logger.warn("Model #" + counter + " skipped due to failed fitting");
						System.out.println(completeModel);
						counter++;
					}
				} while (processRefiner.hasNextModel());
			} while (matcher.hasNextModel());
		} while (refiner.hasNextModel());
	}
	*/
}



/**
 * A tree structure that annotates the TP tree. It keeps the state in which the current traversal of the TP hierarchy
 *
 * @author darko
 *
 */
@Deprecated
class RefinementNode {
	TP tp;

	RefinementNode parent;
	List<RefinementNode> children = new ArrayList<RefinementNode>();

	Integer currentIndex;

	RefinementNode(TP tp, RefinementNode parent) {
		this.parent = parent;

		this.tp = tp;
		for (TP subTP : tp.subs.values()) {
			this.children.add(new RefinementNode(subTP, this));
		}

		if (this.children.isEmpty()) {
			currentIndex = null;
		} else {
			currentIndex = 0;
		}
	}

	RefinementNode(TP tp) {
		this(tp, null);
	}

	RefinementNode currentChild() {
		if (currentIndex == null) {
			return null;
		} else {
			return children.get(currentIndex);
		}
	}

	RefinementNode nextChild() {
		if (currentIndex == null) {
			return null;
		} else if (currentIndex == children.size() - 1) {
			this.currentIndex = null;
			return null;
		} else {
			this.currentIndex++;
			return currentChild();
		}
	}

	boolean hasNextChild() {
		if (currentIndex == null) {
			return false;
		} else if (currentIndex == children.size() - 1) {
			return false;
		} else {
			return true;
		}
	}

	void reset() {
		if (this.children.isEmpty()) {
			currentIndex = null;
		} else {
			currentIndex = 0;
			for (RefinementNode child : this.children) {
				child.reset();
			}
		}
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("[ ");
		buf.append(this.tp.id + " ");
		for (RefinementNode child : this.children) {
			buf.append(child.toString() + " ");
		}
		buf.append("]");
		return buf.toString();
	}
}
