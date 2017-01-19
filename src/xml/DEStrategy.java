package xml;

import javax.xml.bind.annotation.*;

@XmlEnum(String.class)
public enum DEStrategy {

	@XmlEnumValue("rand/1/bin")
	RAND_1_BIN("rand/1/bin"),

	@XmlEnumValue("best/1/bin")
	BEST_1_BIN("best/1/bin"),

	@XmlEnumValue("rand/1/exp")
	RAND_1_EXP("rand/1/exp"),

	@XmlEnumValue("best/1/exp")
	BEST_1_EXP("best/1/exp"),

	@XmlEnumValue("current-to-rand/1")
	CURRENT_TO_RAND_1("current-to-rand/1"),

	@XmlEnumValue("current-to-best/1")
	CURRENT_TO_BEST_1("current-to-best/1"),

	@XmlEnumValue("current-to-rand/1/bin")
	CURRENT_TO_RAND_1_BIN("current-to-rand/1/bin"),

	@XmlEnumValue("current-to-best/1/bin")
	CURRENT_TO_BEST_1_BIN("current-to-best/1/bin"),

	@XmlEnumValue("current-to-rand/1/exp")
	CURRENT_TO_RAND_1_EXP("current-to-rand/1/exp"),

	@XmlEnumValue("current-to-best/1/exp")
	CURRENT_TO_BEST_1_EXP("current-to-best/1/exp");

	private String name;
	private DEStrategy(String name) {
		this.name = name;
	}

	public String toString() {
		return this.name;
	}
}
