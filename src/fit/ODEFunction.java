package fit;

import util.*;

public interface ODEFunction {
	/**
	 *
	 * @param dimensions
	 * @param parameters
	 * @param exogenous
	 * @param state
	 * @param prime - output from the function
	 */
	public void yprime(double[] dimensions, double[] parameters, double[] exogenous, double[] state, double[] prime);
}
