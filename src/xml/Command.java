package xml;

import javax.xml.bind.annotation.*;

@XmlEnum(String.class)
public enum Command {
	@XmlEnumValue("validate")
	VALIDATE,

	@XmlEnumValue("simulate")
	SIMULATE,

	@XmlEnumValue("fit")
	FIT,

	@XmlEnumValue("write_eq")
	WRITE_EQ,

	@XmlEnumValue("msc")
	MSC,

	@XmlEnumValue("xml")
	XML,

	@XmlEnumValue("java")
	JAVA,

	@XmlEnumValue("duplicate")
	DUPLICATE,

	@XmlEnumValue("fit_each")
	FIT_EACH,

	@XmlEnumValue("exhaustive_search")
	EXHAUSTIVE_SEARCH,

	@XmlEnumValue("enumerate")
	ENUMERATE,

	@XmlEnumValue("enumerate_c")
	ENUMERATE_C,

	@XmlEnumValue("process_list")
	PROCESS_LIST,

	@XmlEnumValue("dot_names")
	DOT_NAMES,

	@XmlEnumValue("dot_all")
	DOT_ALL,

	@XmlEnumValue("dot_proper")
	DOT_PROPER,

	@XmlEnumValue("mat_tree")
	MAT_TREE,

	@XmlEnumValue("count")
	COUNT,

	@XmlEnumValue("compile")
	COMPILE,

	@XmlEnumValue("simulate_model")
	SIMULATE_MODEL,

	@XmlEnumValue("fit_model")
	FIT_MODEL,

	@XmlEnumValue("create_output")
	CREATE_OUTPUT,

	@XmlEnumValue("test_objective")
	TEST_OBJECTIVE,

	@XmlEnumValue("test_de")
	TEST_DE,
	
	@XmlEnumValue("grid_mode")
	CLUSTERIFY,
	
	@XmlEnumValue("evaluate")
	EVALUATE,
	
	@XmlEnumValue("test_evaluate")
	TEST_EVALUATE,
}
