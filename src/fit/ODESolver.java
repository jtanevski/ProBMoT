package fit;

import fit.jvode.bridj.cvode.*;

public enum ODESolver {
	ADAMS(CvodeLibrary.CV_ADAMS),
	BDF(CvodeLibrary.CV_BDF);
	
	private int value;
	ODESolver(int value) {
		this.value = value;
	}
	public int value() {
		return this.value;
	}
}
