package xml;

import javax.xml.bind.annotation.*;

public class Settings {
	@XmlElement
	public InitialValuesSpec initialvalues;
	
	@XmlElement
	public SimulatorSpec simulator;

	@XmlElement
	public FitterSpec fitter;
	
	@XmlElement
	public EvalSpec evaluation;
	
	@XmlElement
	public EnsembleSpec ensemble;
}