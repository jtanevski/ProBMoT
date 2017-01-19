package fit;

import fit.jvode.bridj.cvode.*;

public enum NonlinearSolver {
	FUNCTIONAL(CvodeLibrary.CV_FUNCTIONAL),
	NEWTON(CvodeLibrary.CV_NEWTON);
	
	private int value;
	NonlinearSolver(int value) {
		this.value = value;
	}
	public int value() {
		return this.value;
	}
}
