package fit;

import java.io.IOException;
import java.util.*;

import org.antlr.runtime.*;

import com.google.common.collect.*;

import struct.inst.*;
import temp.*;
import util.*;
import xml.*;

/**
 * Simulates ODEs and computes Output
 *
 * @author Darko
 *
 */
public class ModelSimulator {

	private Model model;
	private List<Dataset> datasets;
	private BiMap<String, String> dimsToCols;
	private BiMap<String, String> exosToCols;
	private OutputSpec outSpec;

	private ODEModel odeModel;
	private IQGraph graph;
	private Output output;

	private CVodeSimulator odeSimulator;
	private Dataset odeSimulation;

	ModelSimulator(Model model, List<Dataset> datasets, BiMap<String, String> dimsToCols, BiMap<String, String> exosToCols, OutputSpec outSpec)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, RecognitionException, FailedSimulationException, IOException {
		this.model = model;
		this.datasets = datasets;
		this.dimsToCols = dimsToCols;
		this.exosToCols = exosToCols;
		this.outSpec = outSpec;

		this.graph = new IQGraph(this.model);
		this.odeModel = new ODEModel(this.graph, this.datasets, this.dimsToCols, this.exosToCols);

		odeSimulator = new CVodeSimulator(ODESolver.BDF, NonlinearSolver.NEWTON);
		odeSimulator.initialize(odeModel);

		odeSimulator.setTolerances(1.0e-3, 1.0e-3);
		odeSimulator.setLinearSolver(LinearSolver.SPGMR);

		odeSimulation = odeSimulator.simulate();

		this.output = new Output(this.outSpec, this.graph);


	}
}
