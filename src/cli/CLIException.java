package cli;

import com.martiansoftware.jsap.*;

public class CLIException
		extends RuntimeException {

	private JSAPResult result;

	public CLIException(JSAPResult result) {
		this.result = result;
	}

	public JSAPResult getResult() {
		return this.result;
	}
}
