package fit;

public interface OutputFunction {
	/**
	 *Computes the final output of the model including all algebraic and output equations
	 *
	 * @param dimensions
	 * @param parameters
	 * @param exogenous
	 * @param state
	 * @param prime - output from the function
	 */
	public void output(double[] dimensions, double[] modelParameters, double[] exogenous, double[] state, double[] outputParameters, double[] output);
}
