package fit;

import java.io.*;

import org.apache.commons.lang.*;
import org.bridj.*;
import temp.*;
import util.*;
import static fit.jvode.bridj.cvode.CvodeLibrary.*;
import fit.jvode.bridj.cvode.*;
import fit.jvode.bridj.nvector.*;
import fit.jvode.bridj.sundials.*;

public class CVodeSimulator {
	public static final String JVODE_LIB_PATH = setLibraryPath();

	private static String setLibraryPath() {
		String libpath = "lib" + File.separator + "jvode" + File.separator;
		if (Platform.isWindows() && Platform.is64Bits()) {
			libpath += "win64";
		} else if (Platform.isWindows() && !Platform.is64Bits()) {
			libpath += "win32";
		} else if (Platform.isLinux() && Platform.is64Bits()) {
			libpath += "linux_x64";
		} else if (Platform.isLinux() && !Platform.is64Bits()) {
			libpath += "linux_x86";
		} else if (Platform.isMacOSX()) {
			libpath += "mac_osx";
		}

		else {
			throw new UnsupportedOperationException(
					"No CVODE version for this platform");
		}

		return libpath;
	}

// TODO Remove unused code found by UCDetector
// 	public static final Logger logger = LoggerFactory.getLogger(CLITaskMain.class);

	private ODESolver odeSolver;
	private NonlinearSolver nonlinearSolver;
	private LinearSolver linearSolver;

	private Pointer<?> cvode;
	private ODEModel odeModel;
	private Pointer<Double> tret;

	public CVodeSimulator(ODESolver odeSolver, NonlinearSolver nonlinearSolver) {
		this.odeSolver = odeSolver;
		this.nonlinearSolver = nonlinearSolver;

		cvode = CvodeLibrary.CVodeCreate(odeSolver.value(), nonlinearSolver.value());
		tret = Pointer.allocateDouble();

	}
	
	public void setMaxNumSteps(int steps) {
		CvodeLibrary.CVodeSetMaxNumSteps(cvode, steps);
	}

	public void initialize(ODEModel odeModel) {
		this.odeModel = odeModel;

		CvodeLibrary.CVodeInit(cvode, odeModel.toPointer(), odeModel.getT0(), odeModel.getY());
	}
	
	public void setODEModel(ODEModel odeModel){
		this.odeModel = odeModel;
	}

	public void reinitialize(ODEModel odeModel) {
		this.odeModel=odeModel;
		CvodeLibrary.CVodeReInit(cvode, odeModel.getT0(), odeModel.getY());
	}

	public void setTolerances(double relTol, double absTol) {
		CvodeLibrary.CVodeSStolerances(cvode, relTol, absTol);
	}
	public void setMinStep (double hmin)
	{
		CvodeLibrary.CVodeSetMinStep(cvode, hmin);
	}

	
	public void setTolerances(double relTol, double[] absTols) {
		Pointer<_generic_N_Vector> nVector = NvectorLibrary.N_VNew_Serial(absTols.length);
		_generic_N_Vector.arrayToNVector(absTols, nVector);

		CvodeLibrary.CVodeSVtolerances(cvode, relTol, nVector);
	}

	public void setLinearSolver(LinearSolver linearSolver) {
		this.linearSolver = linearSolver;

		switch (this.linearSolver) {
		case DENSE:
			CvodeLibrary.CVDense(cvode, odeModel.size());
			break;
		case DIAG:
			CvodeLibrary.CVDiag(cvode);
			break;
		case SPGMR:
			CvodeLibrary.CVSpgmr(cvode, SundialsLibrary.PREC_NONE, 0);
			break;
		default:
			throw new UnsupportedOperationException("Unsupported linear solver type: " + this.linearSolver);
		}
	}
	
// TODO Remove unused code found by UCDetector
// 	public void free(){
// 		//TODO: free memory
// 		
// 		
// 		
// 	}

	public Dataset simulate() throws FailedSimulationException {
		double[] time = odeModel.getTimeCol();

		// simulated data stored by column
		Double[][] simulated = new Double[odeModel.size() + 1][time.length];

		//Pointer<Double> tret = Pointer.allocateDouble();
		Pointer<_generic_N_Vector> yNVector = odeModel.getY();
		double[] yArray = new double[odeModel.size()];


		_generic_N_Vector.pointerToArray(odeModel.yVectorData, yArray);

		simulated[0][0] = time[0];
		for (int j = 0; j < yArray.length; j++) {
			simulated[j+1][0] = yArray[j];
		}

//		CVodeSimulator.logger.info("Simulation started");

		for (int i = 1; i < time.length; i++) {
			simulated[0][i] = time[i];
			int errorCode = CvodeLibrary.CVode(cvode, time[i], yNVector, tret, CvodeLibrary.CV_NORMAL);

			switch (errorCode) {
				case CV_SUCCESS:
				case CV_TSTOP_RETURN:
				case CV_ROOT_RETURN:
					break;
				case CV_TOO_MUCH_WORK:
				case CV_TOO_MUCH_ACC:
				case CV_ERR_FAILURE:
				case CV_CONV_FAILURE:
				case CV_LINIT_FAIL:
				case CV_LSETUP_FAIL:
				case CV_LSOLVE_FAIL:
				case CV_RHSFUNC_FAIL:
				case CV_FIRST_RHSFUNC_ERR:
				case CV_REPTD_RHSFUNC_ERR:
				case CV_UNREC_RHSFUNC_ERR:
				case CV_RTFUNC_FAIL:
				case CV_MEM_NULL:
				case CV_ILL_INPUT:
				case CV_NO_MALLOC:
				case CV_TOO_CLOSE:
					System.err.println(errorCode + " " + tret.getDouble() + " " + time[i]);

					throw new FailedSimulationException();
			}

			_generic_N_Vector.pointerToArray(odeModel.yVectorData, yArray);
			for (int j = 0; j < yArray.length; j++) {
				simulated[j + 1][i] = yArray[j];
			}
		}
//		CVodeSimulator.logger.info("Simulation ended");

		String[] headers = (String[]) ArrayUtils.add(odeModel.getStateNames(), 0, odeModel.getTimeName());
		Dataset dataset = new Dataset(simulated, headers);
		return dataset;

	}
}
