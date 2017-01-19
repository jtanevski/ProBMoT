package util;

public class FailedSimulationException
		extends RuntimeException {
	public FailedSimulationException() {
		super();
	}

	public FailedSimulationException(String message) {
		super(message);
	}
}
