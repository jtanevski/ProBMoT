package traverse;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import org.apache.commons.lang3.*;
import org.slf4j.*;

import parser.*;
import struct.*;
import struct.inst.*;
import struct.temp.*;
import util.*;

public class Traverse {
	public static StructInfoRegistry structReg;

	public static PrimitiveRegistry primReg = PrimitiveRegistry.REGISTRY;
	public static InheritInfoRegistry inhReg = InheritInfoRegistry.REGISTRY;
	public static ComposeInfoRegistry compReg = ComposeInfoRegistry.REGISTRY;

	public static String LIB_EXT = "pbl";
	public static String MODEL_EXT = "pbm";

	public static final Logger logger = LoggerFactory.getLogger(Traverse.class);

	public static Library addLibrary(String filepath)
			throws IOException, RecognitionException, InstantiationException, IllegalAccessException, NoSuchFieldException,
			InvocationTargetException, NoSuchMethodException {
		CommonTree libraryTree = Traverse.readStructure(filepath, LIB_EXT, "library");
		Library library = loadLibrary(libraryTree);
		Library.LIBRARIES.put(library.getIdS(), library);

		logger.info("Library '" + filepath + "' compiled successfully");

		return library;
	}

	public static Model addModel(String filepath)
			throws IOException, RecognitionException, IllegalAccessException, NoSuchFieldException, InvocationTargetException,
			NoSuchMethodException, InstantiationException {
		CommonTree modelTree = Traverse.readStructure(filepath, MODEL_EXT, "model");
		Model model = loadModel(modelTree, Library.LIBRARIES);
		Model.MODELS.put(model.getIdS(), model);

		logger.info("Model '" + filepath + "' compiled successfully");

		return model;
	}

	public static IncompleteModel addIncompleteModel(String filepath)
			throws IOException, RecognitionException, IllegalAccessException, NoSuchFieldException,
			InvocationTargetException, NoSuchMethodException, InstantiationException {
		CommonTree modelTree = Traverse.readStructure(filepath, MODEL_EXT, "Incomplete model");
		IncompleteModel model = loadIncompleteModel(modelTree, Library.LIBRARIES);
		// PartialModel.MODELS.put(model.getIdS(), model);

		logger.info("Incomplete Model '" + filepath + "' compiled successfully");

		return model;
	}

	public static CommonTree readStructure(InputStream in)
			throws IOException, RecognitionException {
		ANTLRInputStream inputStream;
		PBFLexer lexer;
		CommonTokenStream tokenStream;
		PBFParser parser;
		PBFParser.file_return parserReturn;
		CommonTree parserTree;
		CommonTreeNodeStream nodeStream;
		PBFFilter filter;
		PBFFilter.node_return filterReturn;
		CommonTree filterTree;

		inputStream = new ANTLRInputStream(in);
		lexer = new PBFLexer(inputStream);
		tokenStream = new CommonTokenStream();
		tokenStream.setTokenSource(lexer);

		parser = new PBFParser(tokenStream);
		parserReturn = parser.file();

		parserTree = (CommonTree) parserReturn.getTree();

		nodeStream = new CommonTreeNodeStream(parserTree);
		nodeStream.setTokenStream(tokenStream);

		filter = new PBFFilter(nodeStream, null);

		filterReturn = filter.node();
		filterTree = (CommonTree) filterReturn.getTree();

		CommonTree structureTree = filterTree;

		return structureTree;
	}

	public static CommonTree readStructure(File file)
			throws IOException, RecognitionException {
		return Traverse.readStructure(new FileInputStream(file));
	}

	public static CommonTree readStructure(String filepath, String extension, String structureType)
			throws IOException, RecognitionException {
		String[] parts = filepath.split("[/\\\\]");
		String justname = parts[parts.length - 1];

		int dotPos = justname.lastIndexOf('.');
		if (dotPos == -1)
			throw new ParsingException(String.format(Errors.ERRORS[0], justname, structureType, extension));

		String name = justname.substring(0, dotPos);
		String ext = justname.substring(dotPos + 1);
		if (!ext.toLowerCase().equals(extension))
			throw new ParsingException(String.format(Errors.ERRORS[1], ext, structureType, extension));

		CommonTree structureTree = Traverse.readStructure(new File(filepath));
		String structureName = ((CommonTree) structureTree.getChild(0)).token.getText();

		if (!structureName.equals(name))
			throw new ParsingException(String.format(Errors.ERRORS[2], name, structureType, structureName));

		return structureTree;
	}

	public static Library loadLibrary(CommonTree libraryTree)
			throws InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
		structReg = StructInfoRegistry.LIBRARY_REGISTRY;

		Library library = (Library) Traverse.contTraverse(libraryTree, null);
		Traverse.refTraverse(libraryTree, library, true);
		Traverse.checkCirularRef(library);
		Traverse.inheritLibrary(library);
		Traverse.refTraverse(libraryTree, library, false);
		Traverse.validateLibrary(library);
		// Traverse.validatePPS(library); now obsolete, incorporated into validateLibrary
		Traverse.buildTQs(library);

		library.determineTopLevelTPs();

		return library;
	}

	public static Model loadModel(CommonTree modelTree, Map<String, Library> libraries)
			throws IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException {
		structReg = StructInfoRegistry.MODEL_REGISTRY;

		Model model = (Model) Traverse.contTraverse(modelTree, null);
		model.libraryLookup = libraries;
		Traverse.buildRecursiveContent(model);
		Traverse.refTraverse(modelTree, model, true);
		Traverse.composeModel(model);
		Traverse.validateModel(model);
		Traverse.composeIQs(model);
		model.upkeep(); // building up-keep structures

		return model;
	}

	public static IncompleteModel loadIncompleteModel(CommonTree modelTree, Map<String, Library> libraries)
			throws IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException {
		structReg = StructInfoRegistry.INCOMPLETE_MODEL_REGISTRY;

		IncompleteModel incomplete = (IncompleteModel) Traverse.contTraverse(modelTree, null);
		incomplete.libraryLookup = libraries;
		Traverse.buildRecursiveContent(incomplete);
		Traverse.refTraverse(modelTree, incomplete, true);
		Traverse.composeModel(incomplete);
		Traverse.validateIncompleteModel(incomplete); // ??
		Traverse.composeIQs(incomplete); // ??
		incomplete.upkeep(); // building up-keep structures

		return incomplete;
	}

	public static Structure contTraverse(CommonTree tree, Structure container)
			throws IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException {
		/*
		 * cur - current - the one from this tree child - the one from the child tree grand - the one from the grand-child tree
		 */

		/* Create element */
		Token curToken = tree.getToken();
		if (curToken.getType() != PBFFilter.STRUCT) // tree has to be a STRUCT
			throw new ParsingException(curToken, String.format(Errors.ERRORS[6], curToken.getText()));

		String structName = curToken.getText();
		StructInfo info = null;
		info = structReg.infos.get(structName);
		if (info == null) // must be a valid structure
			throw new ParsingException(curToken, String.format(Errors.ERRORS[7], curToken.getText()));

		Class clazz = info.clazz;
		Structure current = (Structure) clazz.newInstance(); // create a new instance

		current.setContainer(container); // set the parent

		int subTreeStart = 0;
		switch (info.idtype) { // set the id - integer of string
		case Integer:
			int intID = tree.childIndex;
			current.setIdI(intID);
			break;
		case String:
			Token tok = ((CommonTree) tree.getChild(0)).getToken();
			if (tok.getType() != PBFFilter.ID)
				throw new ParsingException(tok, String.format(Errors.ERRORS[8], tok.getText()));
			String strID = tok.getText();
			current.setIdS(strID);
			subTreeStart++;
			break;
		case Both:
			int intID1 = tree.childIndex;
			Token tok1 = ((CommonTree) tree.getChild(0)).getToken();
			if (tok1.getType() != PBFFilter.ID)
				throw new ParsingException(tok1, String.format(Errors.ERRORS[8], tok1.getText()));
			String strID1 = tok1.getText();
			current.setIdI(intID1);
			current.setIdS(strID1);
			subTreeStart++;
			break;
		default:
			throw new RuntimeException("Invalid state");
		}

		/* Traverse children - each child is one property */
		for (int i = subTreeStart; i < tree.getChildCount(); i++) {

			CommonTree childTree = (CommonTree) tree.getChild(i);
			Token childToken = childTree.token;
			if (childToken.getType() != PBFFilter.ID) // child has to be an ID
				throw new ParsingException(childToken, String.format(Errors.ERRORS[8], childToken.getText()));
			String propName = childToken.getText();
			ContentInfo ci = info.contProps.get(propName);
			if (ci != null) { // this pass works with content properties only
				String childType = ci.propType;
				boolean isChildStruct;
				if (structReg.infos.containsKey(childType)) {
					isChildStruct = true;
				} else if (primReg.primitives.containsKey(childType)) {
					isChildStruct = false;
				} else {
					throw new ParsingException(childToken, String.format(Errors.ERRORS[9], childToken, childType));
				}
				String varName = ci.varName;
				double minCard = ci.minCard;
				double maxCard = ci.maxCard;
				int grandCount = childTree.getChildCount();
				if (grandCount < minCard || grandCount > maxCard) // check cardinality
					throw new ParsingException(childToken, String.format(Errors.ERRORS[10], propName, minCard, maxCard, grandCount));

				if (maxCard > 1) { // if cardinality is bigger than 1 create a list/map
					IDType childIdType;
					if (isChildStruct) {
						childIdType = structReg.infos.get(childType).idtype;
					} else {
						childIdType = IDType.Integer;
					}

					/* Traverse grand children - each grand child is one value */
					for (int j = 0; j < grandCount; j++) {

						Object value; // traverse grand child
						CommonTree grandTree = (CommonTree) childTree.getChild(j);
						if (isChildStruct) {
							Token grandToken = grandTree.token;
							String grandName = grandToken.getText();
							if (!grandName.equals(childType)) // grand child must be of appropriate type
								throw new ParsingException(grandToken, String.format(Errors.ERRORS[11], propName,
										(grandToken.getType() == PBFFilter.STRUCT) ? grandName : PBFFilter.tokenNames[grandToken.getType()],
										childType));
							value = contTraverse(grandTree, current);
						} else {
							Primitive prim = primReg.primitives.get(childType);
							value = prim.create(grandTree, current);
						}

						Object curField = clazz.getField(varName).get(current);
						switch (childIdType) {
						case Integer:
							List list = (List) curField;
							list.add(value);
							break;
						case String:
							Map map = (Map) curField;
							Object old = map.put(((Structure) value).getIdS(), value);
							if (old != null)
								throw new ParsingException(grandTree.token, String.format(Errors.ERRORS[44], ((Structure) value).getIdS()));
							break;
						case Both:
							ListMap maplist = (ListMap) curField;
							if (maplist.containsKey(((Structure) value).getIdS()))
								throw new ParsingException(grandTree.token, String.format(Errors.ERRORS[44], ((Structure) value).getIdS()));
							maplist.put(((Structure) value).getIdS(), value);
							break;
						default:
							throw new RuntimeException("Invalid state");
						}
					}
				} else if (grandCount == 1) { // if there is one child set it
					Object value;
					CommonTree grandTree = (CommonTree) childTree.getChild(0);

					if (isChildStruct) {
						Token grandToken = grandTree.token;
						String grandName = grandToken.getText();
						if (!grandName.equals(childType)) // grand child must be of appropriate type
							throw new ParsingException(grandToken, String.format(Errors.ERRORS[11], propName,
									(grandToken.getType() == PBFFilter.STRUCT) ? grandName : PBFFilter.tokenNames[grandToken.getType()], childType));
						value = contTraverse(grandTree, current);
					} else {
						Primitive prim = primReg.primitives.get(childType);
						value = prim.create(grandTree, current);
					}
					clazz.getField(varName).set(current, value);
				} else {
					// if there are no values don't do anything (field already has a null value)
				}
			}
		}

		/*
		 * Traverse all content properties, and for each property check - whether it is mandatory but missing, in which case raise an
		 * exception - whether it has a default value and missing, in which case assign the default value
		 */
		for (ContentInfo ci : info.contProps.values()) { // Traverse all properties
			String varName = ci.varName; // property name
			Object curField = clazz.getField(varName).get(current); // property field

			/* establish whether the field is empty or not. If it has cardinality bigger than 1, invoke the 'size' method */
			int curFieldSize;
			if (ci.maxCard > 1) { // if cardinality is bigger than 1 it is a list/map/map-list
				curFieldSize = (Integer) curField.getClass().getMethod("size", null).invoke(curField, null);
			} else {
				curFieldSize = (curField != null) ? 1 : 0;
			}

			if (curFieldSize == 0) { // if the property is missing
				if (ci.minCard > 0) { // if it is mandatory
					throw new ParsingException(curToken, String.format(Errors.ERRORS[12],
							(info.idtype == IDType.String || info.idtype == IDType.Both) ? tree.getChild(0).getText() : curToken.getText(),
							ci.propName));
				} else if (ci.defavlt != null) { // if it has a default value
					clazz.getField(varName).set(current, ci.defavlt);
				}
			}
		}

		/* Check that every mandatory field has a value */
		/*
		 * for (ContentInfo ci : info.contProps.values()) { if (ci.minCard > 0) { boolean contains = false; for (int i = subTreeStart; i <
		 * tree.getChildCount(); i++) { CommonTree subTree = (CommonTree) tree.getChild(i); if (subTree.token.getText().equals(ci.propName)) {
		 * contains = true; break; } } if (!contains) { throw new ParsingException(curToken, String.format(Errors.ERRORS[12], (info.idtype ==
		 * IDType.String || info.idtype == IDType.Both) ? tree.getChild(0).getText() : curToken.getText(), ci.propName)); } } }
		 */
		return current;
	}

	/**
	 * Traverse the tree recursing by content and resolving references
	 * 
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public static void refTraverse(CommonTree tree, Structure current, boolean preInherit)
			throws NoSuchFieldException, IllegalAccessException {
		/*
		 * cur - current - the one from this tree child - the one from the child tree grand - the one from the grand-child tree
		 */

		/* Check the current node */
		Token curToken = tree.getToken();
		if (curToken.getType() != PBFFilter.STRUCT) // tree has to be a STRUCT
			throw new ParsingException(curToken, String.format(Errors.ERRORS[6], curToken.getText()));
		String structName = curToken.getText();

		StructInfo info = null;
		info = structReg.infos.get(structName);
		if (info == null) // must be a valid structure
			throw new ParsingException(curToken, String.format(Errors.ERRORS[7], curToken.getText()));

		int subTreeStart;
		switch (info.idtype) {
		case Integer:
			subTreeStart = 0;
			break;
		case String:
		case Both:
			subTreeStart = 1;
			break;
		default:
			throw new RuntimeException("Invalid state");
		}

		/* Traverse all child nodes */
		for (int i = subTreeStart; i < tree.getChildCount(); i++) { // traverse the children

			CommonTree childTree = (CommonTree) tree.getChild(i);
			Token childToken = childTree.token;
			if (childToken.getType() != PBFFilter.ID) // child has to be an ID
				throw new ParsingException(childToken, String.format(Errors.ERRORS[8], childToken.getText()));

			String propName = childToken.getText();
			ContentInfo ci = info.contProps.get(propName);
			ReferenceInfo ri = (preInherit) ? info.refPropsPre.get(propName) : info.refPropsPost.get(propName);
			ReferenceInfo ri2 = (preInherit) ? info.refPropsPost.get(propName) : info.refPropsPre.get(propName);

			/* The field is either content or reference */
			if (ci != null) { // the field is content - recurse

				String childType = ci.propType;
				boolean isChildStruct;
				if (structReg.infos.containsKey(childType)) {
					isChildStruct = true;
				} else if (primReg.primitives.containsKey(childType)) {
					isChildStruct = false;
				} else {
					throw new ParsingException(childToken, String.format(Errors.ERRORS[9], childToken, childType));
				}

				if (isChildStruct) { // recurse by children which are structures

					String varName = ci.varName;
					double maxCard = ci.maxCard;

					int grandCount = childTree.getChildCount();

					for (int j = 0; j < grandCount; j++) { // traverse all grand children

						Object value;
						CommonTree grandTree = (CommonTree) childTree.getChild(j);

						Class curClass = current.getClass();
						Field cf = curClass.getField(varName);
						Object childField = cf.get(current);
						Structure child;

						if (maxCard > 1) {
							IDType subIdType;
							if (isChildStruct) {
								subIdType = structReg.infos.get(childType).idtype;
							} else {
								subIdType = IDType.Integer;
							}

							switch (subIdType) {
							case Integer:
								List list = (List) childField;
								child = (Structure) list.get(j);
								break;
							case String:
								Map map = (Map) childField;
								String childName = ((CommonTree) grandTree.getChild(0)).token.getText();
								child = (Structure) map.get(childName);
								break;
							case Both:
								ListMap maplist = (ListMap) childField;
								child = (Structure) maplist.getValue(j);
								break;
							default:
								throw new RuntimeException("Invalid state");
							}
						} else {
							child = (Structure) childField;
						}

						refTraverse(grandTree, child, preInherit); // RECURSE
					}
				}
			} else if (ri != null) { // the field is a reference - resolve

				String varName = ri.varName;
				double minCard = ri.minCard;
				double maxCard = ri.maxCard;

				int grandCount = childTree.getChildCount();
				if (grandCount < minCard || grandCount > maxCard) // check cardinality
					throw new ParsingException(childToken, String.format(Errors.ERRORS[10], propName, minCard, maxCard, grandCount));
				if (maxCard > 1) { // if cardinality is bigger than 1 create a list/map
					for (int j = 0; j < grandCount; j++) { // traverse all grand children
						CommonTree grandTree = (CommonTree) childTree.getChild(j);
						Token grandToken = grandTree.token;

						/* References can be IDs or ITERators */
						if (grandToken.getType() == PBFFilter.ID) { // If it is an ID

							String referenceName = grandToken.getText();
							String[] fieldNames = ri.lookup();
							Class curClazz = current.getClass();
							Object object = current;
							Class clazz;

							for (String fieldName : fieldNames) {
								clazz = object.getClass();
								Field field = clazz.getField(fieldName);
								object = field.get(object);
							}

							Structure reference;
							Object[] allowed;
							try {
								Map mapObject = (Map) object;
								reference = (Structure) mapObject.get(referenceName);
								allowed = mapObject.keySet().toArray();
							} catch (ClassCastException ex) {
								ListMap maplistObject = (ListMap) object;
								reference = (Structure) maplistObject.get(referenceName);
								allowed = maplistObject.keyList().toArray();
							}

							if (reference == null) {// if the ID doesn't exist it is illegal
								// NOTE: hard-coded handling of the value 'all' for AE lists
								if (structName.equals("AE") && propName.equals("upperArg") && referenceName.equals("all")) {
									((IAE) current).isUpperAll = true;

								} else {

									throw new ParsingException(grandToken, String.format(Errors.ERRORS[13], referenceName,
											ArrayUtils.toString(allowed)));
								}
							}

							try {
								Map mapField = (Map) curClazz.getField(varName).get(current);
								mapField.put(referenceName, reference);
							} catch (ClassCastException ex) {
								ListMap mapField = (ListMap) curClazz.getField(varName).get(current);
								mapField.put(referenceName, reference);
							}

						} else if (grandToken.getType() == PBFFilter.ITER) { // If it is an ITER
							/*
							 * Iterators are currently only supported in PPs, and the iterator can only reference PE That's why the iterator
							 * handling is hard-coded.
							 */

							if (!(current instanceof PP)) // iterators allowed only in PPs
								throw new ParsingException(grandToken, String.format(
										Errors.ERRORS[14],
										(info.idtype == IDType.String || info.idtype == IDType.Both) ? tree.getChild(0).getText() : curToken
												.getText()));

							PP currentPP = (PP) current;
							if (currentPP.isIter) // only one iterator is allowed
								throw new ParsingException(grandToken, String.format(
										Errors.ERRORS[15],
										(info.idtype == IDType.String || info.idtype == IDType.Both) ? tree.getChild(0).getText() : curToken
												.getText()));

							Token iteratorToken = ((CommonTree) grandTree.getChild(0)).token;
							if (iteratorToken.getType() != PBFFilter.ID)
								throw new ParsingException(iteratorToken, String.format(Errors.ERRORS[16], iteratorToken.getText()));
							String iteratorName = iteratorToken.getText();

							Token iteratedToken = ((CommonTree) grandTree.getChild(1)).token;
							if (iteratedToken.getType() != PBFFilter.ID)
								throw new ParsingException(iteratedToken, String.format(Errors.ERRORS[17], iteratedToken.getText()));
							String iteratedName = iteratedToken.getText();

							PE iteratedPE = currentPP.cont.parameters.get(iteratedName);
							if (iteratedPE == null)
								throw new ParsingException(iteratedToken, String.format(Errors.ERRORS[18], iteratedToken.getText()));

							currentPP.iterated = iteratedPE;
							currentPP.iterator = new PE();
							currentPP.iterator.id = iteratorName;
							currentPP.iterator.card = new Interval(1);
							currentPP.iterator.te = iteratedPE.te;
							currentPP.iterator.tp = iteratedPE.tp;
							currentPP.iterator.idI = iteratedPE.idI;

							currentPP.isIter = true;

							currentPP.parameters.put(iteratorName, currentPP.iterator);

						} else if (grandToken.getType() == PBFFilter.LIST) { // If it is a LIST
							// lists are only allowed in incomplete models, when specifying lower and upper bound of process arguments

						} else { // If it is neither ID nor ITER
							throw new ParsingException(grandToken, String.format(Errors.ERRORS[19], grandToken.getText(),
									PBFFilter.tokenNames[grandToken.getType()]));
						}
					}
				} else if (grandCount == 1) { // if there is one child set it

					CommonTree grandTree = (CommonTree) childTree.getChild(0);
					Token grandToken = grandTree.token;

					/* References can be IDs or ITERators */
					if (grandToken.getType() == PBFFilter.ID) { // If it is an ID

						String referenceName = grandToken.getText();
						String[] fieldNames = ri.lookup();
						Class curClazz = current.getClass();
						Object object = current;
						Class clazz;

						for (String fieldName : fieldNames) {
							clazz = object.getClass();
							Field field = clazz.getField(fieldName);
							object = field.get(object);
						}

						Structure reference;
						Object[] allowed;
						try {
							Map mapObject = (Map) object;
							reference = (Structure) mapObject.get(referenceName);
							allowed = mapObject.keySet().toArray();
						} catch (ClassCastException ex) {
							ListMap maplistObject = (ListMap) object;
							reference = (Structure) maplistObject.get(referenceName);
							allowed = maplistObject.keyList().toArray();
						}

						if (reference == null) {// if the ID doesn't exist it is illegal
							// if this is an incomplete model and if a AP is missing it may be that this is a reference to
							// process template
							// NOTE: need a way to establish that this is a Incomplete model, otherwise raise an exception
							if (current instanceof AP) {
								AP ap = (AP) current;
								Map<String, TP> tps = ((Library) ap.ip.cont.getTemplate()).tps;
								reference = tps.get(referenceName);
								if (reference == null) { // it is not a TP reference
									throw new ParsingException(grandToken, String.format(Errors.ERRORS[13], referenceName,
											ArrayUtils.toString(allowed)));
								} else {
									varName = "argTemp";
								}

							} else {
								throw new ParsingException(grandToken,
										String.format(Errors.ERRORS[13], referenceName, ArrayUtils.toString(allowed)));
							}
						}
						curClazz.getField(varName).set(current, reference);

					} else if (grandToken.getType() == PBFFilter.ITER) { // If it is an ITER
						/* Currently not possible */
						assert false : "unreachable";

						/*
						 * Iterators are currently only supported in PPs, and the iterator can only reference PE That's why the iterator handling
						 * is hardcoded.
						 */

						if (!(current instanceof PP)) // iterators allowed only in PPs
							throw new ParsingException(grandToken, String.format(Errors.ERRORS[14],
									(info.idtype == IDType.String || info.idtype == IDType.Both) ? tree.getChild(0).getText() : curToken.getText()));

						PP currentPP = (PP) current;
						if (currentPP.isIter) // only one iterator is allowed
							throw new ParsingException(grandToken, String.format(Errors.ERRORS[15],
									(info.idtype == IDType.String || info.idtype == IDType.Both) ? tree.getChild(0).getText() : curToken.getText()));

						Token iteratorToken = ((CommonTree) grandTree.getChild(0)).token;
						if (iteratorToken.getType() != PBFFilter.ID)
							throw new ParsingException(iteratorToken, String.format(Errors.ERRORS[16], iteratorToken.getText()));
						String iteratorName = iteratorToken.getText();

						Token iteratedToken = ((CommonTree) grandTree.getChild(1)).token;
						if (iteratedToken.getType() != PBFFilter.ID)
							throw new ParsingException(iteratedToken, String.format(Errors.ERRORS[17], iteratedToken.getText()));
						String iteratedName = iteratedToken.getText();

						PE iteratedPE = currentPP.cont.parameters.get(iteratedName);
						if (iteratedPE == null)
							throw new ParsingException(iteratedToken, String.format(Errors.ERRORS[18], iteratedToken.getText()));

						currentPP.iterated = iteratedPE;
						currentPP.iterator = new PE();
						currentPP.iterator.id = iteratorName;
						currentPP.iterator.card = new Interval(1);
						currentPP.iterator.te = iteratedPE.te;
						currentPP.iterator.tp = iteratedPE.tp;
						currentPP.iterator.idI = iteratedPE.idI;

						currentPP.isIter = true;

						currentPP.parameters.put(iteratorName, currentPP.iterator);

					} else { // It is neither ID nor ITER
						throw new ParsingException(grandToken, String.format(Errors.ERRORS[19], grandToken.getText(),
								PBFFilter.tokenNames[grandToken.getType()]));
					}
				}
			} else if (ri2 != null) { // just check whether it exists
			} else { // if the property doesn't exist
				if (preInherit) {
					logger.warn("{}:{}:{} - the property '{}' is an illegal property", new Object[] { MDC.get("file"), childToken.getLine(),
							childToken.getCharPositionInLine(), propName });
				}
			}
		}

		// check that every mandatory field has a value
		for (ReferenceInfo ri : ((preInherit) ? info.refPropsPre : info.refPropsPost).values()) {
			if (ri.minCard > 0) {
				boolean contains = false;
				for (int i = subTreeStart; i < tree.getChildCount(); i++) {
					CommonTree subTree = (CommonTree) tree.getChild(i);
					if (subTree.token.getText().equals(ri.propName)) {
						contains = true;
						break;
					}
				}
				if (!contains) {
					throw new ParsingException(curToken, String.format(Errors.ERRORS[12],
							(info.idtype == IDType.String || info.idtype == IDType.Both) ? tree.getChild(0).getText() : curToken.getText(),
							ri.propName));
				}
			}
		}

		// Register in backreferences (if in preInherit)
		if (preInherit) {
			String[][] backrefs = info.backrefs();

			for (String[] backref : backrefs) {
				Class curClazz = current.getClass();
				Object object = current;
				Class clazz;

				for (String fieldName : backref) {
					clazz = object.getClass();
					Field field = clazz.getField(fieldName);
					object = field.get(object);
				}

				try {
					Map map = (Map) object;
					map.put(current.getIdS(), current);
				} catch (ClassCastException ex) {
					ListMap maplist = (ListMap) object;
					maplist.put(current.getIdS(), current);
				}
			}
		}
	}

	public static void inheritLibrary(Library lib)
			throws NoSuchFieldException, IllegalAccessException {
		inheritElement(lib.ENTITY, inhReg.infos.get("TE"));
		inheritElement(lib.PROCESS, inhReg.infos.get("TP"));
	}

	public static void inheritElement(Structure obj, InheritInfo info)
			throws NoSuchFieldException, IllegalAccessException {
		Class clazz = obj.getClass();

		String childrenName = info.children;
		Field childrenField = clazz.getField(childrenName);
		Map children = (Map) childrenField.get(obj);

		for (Object ch : children.values()) {
			Structure child = (Structure) ch;
			// copy fields
			for (FieldInfo fi : info.fields) {
				// copy each field
				String inhName = fi.id;
				Field inhField = clazz.getField(inhName);
				switch (fi.type) {
				case Integer:
					List parentList = (List) inhField.get(obj);
					List childList = (List) inhField.get(child);
					childList.addAll(0, parentList);
					for (int i = 0; i < childList.size(); i++) {
						try {
						((Structure)childList.get(i)).setIdI(i);
						} catch (UnsupportedOperationException ex) {
							
						}
					}
					break;
				case String:
					Map parentMap = (Map) inhField.get(obj);
					Map childMap = (Map) inhField.get(child);

					for (Object childKey : childMap.keySet()) {
						if (parentMap.containsKey(childKey)) // There is a duplicate in the child - not allowed
							throw new ParsingException(String.format(Errors.ERRORS[20], child.getIdS(), inhName, childKey));
					}

					// int parentSize = parentMap.size();
					// int childSize = childMap.size();
					childMap.putAll(parentMap);
					// if (childMap.size() != parentSize + childSize)
					// throw new RuntimeException();
					break;
				case Both:
					ListMap parentMaplist = (ListMap) inhField.get(obj);
					ListMap childMaplist = (ListMap) inhField.get(child);
					for (Object chKey : childMaplist.keyList()) {
						if (parentMaplist.containsKey(chKey))
							throw new ParsingException(String.format(Errors.ERRORS[20], child.getIdS(), inhName, chKey));
					}
					// int pSize = parentMaplist.size();
					// int cSize = childMaplist.size();
					childMaplist.putAll(0, parentMaplist);
					for (int i = 0; i < childMaplist.size(); i++) {
						try {
						((Structure)childMaplist.get(i)).setIdI(i);
						} catch (UnsupportedOperationException ex) {
							
						}
					}
					// if (childMaplist.size() != pSize + cSize)
					// throw new RuntimeException();
					break;
				default:
					throw new RuntimeException("invalid state");
				}
			}
			inheritElement(child, info); // recurse
		}
	}

	/*
	 * public static void validatePPS(Library lib) { for (TP tp : lib.tps.values()) { for (PP pp : tp.processes) { TP procRef = pp.tp; if
	 * (pp.parameters.size() != procRef.parameters.size()) // TP definition and reference don't match in // parameter count throw new
	 * ParsingException(String.format(Errors.ERRORS[21], tp.getIdS(), pp.getIdI(), procRef.getIdS(), pp.parameters.size(),
	 * procRef.parameters.size())); for (int i = 0; i < pp.parameters.size(); i++) { // Traverse all parameters (both in Decl and Ref) PE
	 * declPE = procRef.parameters.get(i); PE refPE = pp.parameters.get(i);
	 * 
	 * if (!refPE.te.isEqualOrSuccessorOf(declPE.te)) // Decl and Ref have to have the same Type throw new
	 * ParsingException(String.format(Errors.ERRORS[22], tp.getIdS(), pp.getIdI(), procRef .getIdS(), i, refPE.getIdS(), refPE.te.getIdS(),
	 * declPE.getIdS(), declPE.te.getIdS())); if (!refPE.card.isSubOf(declPE.card)) throw new
	 * ParsingException(String.format(Errors.ERRORS[23], tp.getIdS(), pp.getIdI(), procRef .getIdS(), i, refPE.getIdS(),
	 * refPE.card.toString(), declPE.getIdS(), declPE.card.toString())); } } } }
	 */

	public static void buildTQs(Library lib) {
		for (TP tp : lib.tps.values()) {
			for (TQ tq : tp.equations) {
				tq.build();
			}
		}
	}

	public static void checkCirularRef(Library library) {
		for (TE te : library.tes.values()) {
			if (te.isSuccessorOf(te)) // A circular reference in TEs
				throw new ParsingException(String.format(Errors.ERRORS[24], te.getIdS()));
		}
		for (TP tp : library.tps.values()) {
			if (tp.isSuccessorOf(tp)) // A circular reference in TPs
				throw new ParsingException(String.format(Errors.ERRORS[25], tp.getIdS()));
		}
	}

	public static void composeModel(Model model)
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Traverse.compose(model, "MODEL");
	}

	public static void compose(Instance instance, String name)
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		ComposeInfo info = compReg.infos.get(name);
		Template template = instance.getTemplate();
		if (template == null)
			throw new ParsingException(String.format(Errors.ERRORS[26], instance.getIdS()));

		Class instanceClass = instance.getClass();
		Class templateClass = template.getClass();

		// Copy fields
		for (CopyInfo ci : info.copy) {
			String ifName = ci.instanceField;
			String tfName = ci.templateField;

			Field ifField = instanceClass.getField(ifName);
			Field tfField = templateClass.getField(tfName);

			Object tfValue = tfField.get(template);
			ifField.set(instance, tfValue);
		}

		// Recurse structures
		for (RecurseInfo ri : info.recurse) {
			String ifName = ri.instanceField;
			String tfName = ri.templateField;

			Field ifField = instanceClass.getField(ifName);
			Field tfField = templateClass.getField(tfName);

			switch (ri.type) {
			case Explicit:
				Map ifValues = (Map) ifField.get(instance);
				for (Instance ifValue : (Collection<Instance>) ifValues.values()) {
					Traverse.compose(ifValue, ri.name);
				}
				break;
			case Name:
				Map ifValuesMap;
				Map tfValuesMap;
				ifValuesMap = (Map) ifField.get(instance);
				try {
					tfValuesMap = (Map) tfField.get(template);
				} catch (ClassCastException ex) {
					// MapList ifValuesMaplist = (MapList) ifField.get(instance);
					// ifValuesMap = ifValuesMaplist.map;
					ListMap tfValuesMaplist = (ListMap) tfField.get(template);
					tfValuesMap = tfValuesMaplist;
				}

				if (ifValuesMap.size() > tfValuesMap.size()) {
					for (String ifTD : (Collection<String>) ifValuesMap.keySet()) {
						if (!tfValuesMap.containsKey(ifTD))
							throw new ParsingException(String.format(Errors.ERRORS[27], instance.getIdS(), ifName, ifTD, template.getIdS()));
					}
				} else if (ifValuesMap.size() <= tfValuesMap.size()) {
					for (String tfTD : (Collection<String>) tfValuesMap.keySet()) {
						if (!ifValuesMap.containsKey(tfTD))
							throw new ParsingException(String.format(Errors.ERRORS[28], instance.getIdS(), ifName, tfTD, template.getIdS()));
					}
				}

				for (String tfID : (Collection<String>) tfValuesMap.keySet()) {
					Template tfValue = (Template) tfValuesMap.get(tfID);
					Instance ifValue = (Instance) ifValuesMap.get(tfID);
					if (ifValue == null) // all discrepancies in template and instance should have already been resolved
						throw new RuntimeException("invalid state");
					ifValue.setTemplate(tfValue);

					// set templateID field
					try {
						ifValue.templateID = tfValue.getIdS();
					} catch (UnsupportedOperationException ex) {
						ifValue.templateID = tfValue.getIdI();
					}
					Traverse.compose(ifValue, ri.name);
				}
				break;
			case Position:
				List ifValuesList;
				List tfValuesList;
				ifValuesList = (List) ifField.get(instance);
				try {
					tfValuesList = (List) tfField.get(template);
				} catch (ClassCastException ex) {
					// MapList ifValuesMaplist = (MapList) ifField.get(instance);
					// ifValuesList = ifValuesMaplist.list;
					ListMap tfValuesMaplist = (ListMap) tfField.get(template);
					tfValuesList = tfValuesMaplist.valueList();
				}

				Object test = (tfValuesList.size() > 0) ? tfValuesList.get(0) : null;
				if (test instanceof PP) { // Handling of iterators is hard-coded for PPs
					int ifSize = ifValuesList.size();
					int tfSize = tfValuesList.size();

					// NOTE: Such a simple check is not valid, because some PP can have 0 AP (if there are 0 AE for the corresponding iterated PE)
					// if (tfSize > ifSize)
					// throw new ParsingException(String.format(Errors.ERRORS[30], instance.getIdS(), ifName, ifSize));
					int ifIndex = 0;
					for (int tfIndex = 0; tfIndex < tfSize; tfIndex++, ifIndex++) {
						PP tfValue = (PP) tfValuesList.get(tfIndex);
						if (tfValue.isIter) {
							int argIndex = tfValue.cont.parameters.indexOfValue(tfValue.iterated);
							int argSize = ((IP) instance).arguments.get(argIndex).args.size();
							if (ifIndex + argSize > ifSize) {// Not enough APs, the PP is iterated and wants more (decided by the size of the
																		// iterated argument)
								// NOTE: skip this PP
								// ifIndex--;
								continue;
								// throw new ParsingException(String.format(Errors.ERRORS[31], instance.getIdS(), ifName, tfValue.tp.getIdS(),
								// tfValue.iterated.getIdS(), argSize));
							}
							for (int count = 0; count < argSize; count++, ifIndex++) {
								AP ifValue = (AP) ifValuesList.get(ifIndex);
								ifValue.setTemplate(tfValue);
								ifValue.index = count;
								Traverse.compose(ifValue, ri.name);
							}
							ifIndex--; // FIXME: check this code as soon as possible!! (No idea why I wrote this, but seems
							// to work fine :)
						} else {
							AP ifValue = (AP) ifValuesList.get(ifIndex);
							ifValue.setTemplate(tfValue);
							Traverse.compose(ifValue, ri.name);
						}

					}
					// if (ifIndex != ifSize) // too many APs
					// throw new ParsingException(String.format(Errors.ERRORS[32], instance.getIdS(), ifName, ifIndex,
					// ifSize));
					// FIXME: disabled for now, because it raises an exception with constructing processes while searching
					// should be changed to account for IAP and IAE

				} else { // Normal code (non PP)
					if (ifValuesList.size() != tfValuesList.size())
						throw new ParsingException(String.format(Errors.ERRORS[29], instance.getIdS(), ifName, ifValuesList.size(),
								tfValuesList.size()));
					for (int i = 0; i < ifValuesList.size(); i++) {
						Template tfValue = (Template) tfValuesList.get(i);
						Instance ifValue = (Instance) ifValuesList.get(i);
						ifValue.setTemplate(tfValue);
						Traverse.compose(ifValue, ri.name);
					}
				}

				break;
			default:
				throw new RuntimeException("invalid state");
			}
		}

		for (ProprietaryInfo pi : info.prop) {
			pi.compose();
		}
	}

	public static void buildRecursiveContent(Model model) {
		recurseContent(model);
	}

	public static void recurseContent(AM am) {
		for (IK ik : am.iks.values()) {
			recurseContent(ik);
		}

		// IEs
		for (Map.Entry<String, IE> entry : am.ies.entrySet()) {
			String ieName = entry.getKey();
			IE ie = entry.getValue();

			am.iesRec.put(ieName, ie);
		}

		for (Map.Entry<String, IK> entry : am.iks.entrySet()) {
			String ikName = entry.getKey();
			IK ik = entry.getValue();

			for (Map.Entry<String, IE> entry2 : ik.iesRec.entrySet()) {
				String ieName = entry2.getKey();
				IE ie = entry2.getValue();

				am.iesRec.put(ikName + "." + ieName, ie);
			}
		}

		// IPs
		for (IP ip : am.ips.values()) {
			am.ipsRec.put(ip.getFullName(), ip);
		}
		for (IK ik : am.iks.values()) {
			for (IP ip : ik.ipsRec.values()) {
				am.ipsRec.put(ip.getFullName(), ip);
			}
		}

		// IKs
		for (IK ik : am.iks.values()) {
			am.iksRec.put(ik.getFullName(), ik);
		}
		for (IK ik : am.iks.values()) {
			for (IK ik2 : ik.iksRec.values()) {
				am.iksRec.put(ik2.getFullName(), ik2);
			}
		}
	}

	public static void composeIQs(Model model) {
		for (IP ip : model.ipsRec.values()) {
			for (TQ tq : ip.template.equations) {
				ip.equations.addAll(IQ.createIQs(tq, ip));
			}
		}
	}

	public static void validateIncompleteModel(Model model)
			throws NoSuchFieldException, IllegalAccessException {
		Traverse.validate(model, "INCOMPLETE_MODEL");
	}

	public static void validateModel(Model model)
			throws NoSuchFieldException, IllegalAccessException {
		Traverse.validate(model, "MODEL");
	}

	public static void validateLibrary(Library library)
			throws NoSuchFieldException, IllegalAccessException {
		Traverse.validate(library, "LIBRARY");
	}

	/*
	 * public static void validate(Structure instance, String name) throws Exception { ComposeInfo info = compReg.infos.get(name);
	 * 
	 * if (info.validator != null) { info.validator.validate(instance); } Class instanceClass = instance.getClass();
	 * 
	 * // Recurse structures for (RecurseInfo ri : info.recurse) { String ifName = ri.instanceField;
	 * 
	 * Field ifField = instanceClass.getField(ifName);
	 * 
	 * switch (ri.type) { case Explicit: case Name: Map ifValues = (Map) ifField.get(instance); for (Instance ifValue :
	 * (Collection<Instance>) ifValues.values()) { Traverse.validate(ifValue, ri.name); } break; case Position: List ifValuesList = (List)
	 * ifField.get(instance); for (int i = 0; i < ifValuesList.size(); i++) { Instance ifValue = (Instance) ifValuesList.get(i);
	 * Traverse.validate(ifValue, ri.name); } break; default: throw new RuntimeException("invalid state"); } }
	 * 
	 * for (ProprietaryInfo pi : info.prop) { pi.validate(); } }
	 */

	public static void validate(Structure instance, String name)
			throws NoSuchFieldException, IllegalAccessException {
		StructInfo info = structReg.infos.get(name);

		if (info.validator != null) {
			info.validator.validate(instance);
		}
		Class instanceClass = instance.getClass();

		// Recurse structures
		for (ContentInfo ci : info.contProps.values()) {
			StructInfo field_si = structReg.infos.get(ci.propType);

			if (field_si == null) {
				continue;
			}
			String ifName = ci.varName;

			Field ifField = instanceClass.getField(ifName);

			switch (field_si.idtype) {
			case Both:
				ListMap ifValuesMapList = (ListMap) ifField.get(instance);
				for (int i = 0; i < ifValuesMapList.size(); i++) {
					Structure ifValue = (Structure) ifValuesMapList.getValue(i);
					Traverse.validate(ifValue, ci.propType);
				}
			case String:
				Map ifValuesMap;
				try {
					ifValuesMap = (Map) ifField.get(instance);
				} catch (ClassCastException ex) {
					ifValuesMap = ((ListMap) ifField.get(instance));
				}
				for (Structure ifValue : (Collection<Structure>) ifValuesMap.values()) {
					Traverse.validate(ifValue, ci.propType);
				}
				break;
			case Integer:
				List ifValuesList = (List) ifField.get(instance);
				for (int i = 0; i < ifValuesList.size(); i++) {
					Structure ifValue = (Structure) ifValuesList.get(i);
					Traverse.validate(ifValue, ci.propType);
				}
				break;
			default:
				throw new IllegalStateException("invalid state");
			}
		}

		// for (ProprietaryInfo pi : info.prop) {
		// pi.validate();
		// }
	}

	/*
	 * 
	 * 
	 * public static Library addLibrary(String filename) throws Exception { String[] parts = filename.split("\\\\"); String justname =
	 * parts[parts.length - 1];
	 * 
	 * int dotPos = justname.lastIndexOf('.'); if (dotPos == -1) throw new ParsingException(String.format(Errors.ERRORS[0], justname,
	 * LIB_EXT));
	 * 
	 * String name = justname.substring(0, dotPos); String ext = justname.substring(dotPos + 1); if (!ext.toLowerCase().equals(LIB_EXT))
	 * throw new ParsingException(String.format(Errors.ERRORS[1], ext, LIB_EXT));
	 * 
	 * Library library = Traverse.readLibrary(filename); if (!library.getIdS().equals(name)) throw new
	 * ParsingException(String.format(Errors.ERRORS[2], name, library.getIdS()));
	 * 
	 * Library.LIBRARIES.put(name, library); return library; }
	 * 
	 * 
	 * public static Model addModel(String filename) throws Exception { int charPos = filename.lastIndexOf(File.separatorChar); String
	 * justname = filename.substring(charPos + 1);
	 * 
	 * int dotPos = justname.lastIndexOf('.'); if (dotPos == -1) throw new ParsingException(String.format(Errors.ERRORS[3], justname,
	 * MODEL_EXT));
	 * 
	 * String name = justname.substring(0, dotPos); String ext = justname.substring(dotPos + 1); if (!ext.toLowerCase().equals(MODEL_EXT))
	 * throw new ParsingException(String.format(Errors.ERRORS[4], ext, MODEL_EXT));
	 * 
	 * Model model = Traverse.readModel(filename); if (!model.getIdS().equals(name)) throw new
	 * ParsingException(String.format(Errors.ERRORS[5], name, model.getIdS()));
	 * 
	 * Model.MODELS.put(name, model); return model; }
	 * 
	 * public static Library readLibrary(InputStream in) throws Exception { ANTLRInputStream inputStream; PBFLexer lexer; CommonTokenStream
	 * tokenStream; PBFParser parser; PBFParser.file_return parserReturn; CommonTree parserTree; CommonTreeNodeStream nodeStream; PBFFilter
	 * filter; PBFFilter.node_return filterReturn; CommonTree filterTree;
	 * 
	 * inputStream = new ANTLRInputStream(in); lexer = new PBFLexer(inputStream); tokenStream = new CommonTokenStream();
	 * tokenStream.setTokenSource(lexer);
	 * 
	 * parser = new PBFParser(tokenStream); parserReturn = parser.file();
	 * 
	 * parserTree = (CommonTree) parserReturn.getTree();
	 * 
	 * nodeStream = new CommonTreeNodeStream(parserTree); nodeStream.setTokenStream(tokenStream);
	 * 
	 * filter = new PBFFilter(nodeStream, null);
	 * 
	 * filterReturn = filter.node(); filterTree = (CommonTree) filterReturn.getTree();
	 * 
	 * CommonTree libraryTree = filterTree; Library library = Traverse.loadLibrary(libraryTree);
	 * 
	 * return library;
	 * 
	 * }
	 * 
	 * 
	 * public static Structure readLibrary(File file) throws Exception { return Traverse.readLibrary(new FileInputStream(file)); }
	 * 
	 * public static Structure readLibrary(String filename) throws Exception { return Traverse.readLibrary(new File(filename)); }
	 * 
	 * 
	 * public static Model readModel(InputStream in) throws Exception { ANTLRInputStream inputStream; PBFLexer lexer; CommonTokenStream
	 * tokenStream; PBFParser parser; PBFParser.file_return parserReturn; CommonTree parserTree; CommonTreeNodeStream nodeStream; PBFFilter
	 * filter; PBFFilter.node_return filterReturn; CommonTree filterTree;
	 * 
	 * inputStream = new ANTLRInputStream(in); lexer = new PBFLexer(inputStream); tokenStream = new CommonTokenStream();
	 * tokenStream.setTokenSource(lexer);
	 * 
	 * parser = new PBFParser(tokenStream); parserReturn = parser.file();
	 * 
	 * parserTree = (CommonTree) parserReturn.getTree();
	 * 
	 * nodeStream = new CommonTreeNodeStream(parserTree); nodeStream.setTokenStream(tokenStream);
	 * 
	 * filter = new PBFFilter(nodeStream, null);
	 * 
	 * filterReturn = filter.node(); filterTree = (CommonTree) filterReturn.getTree();
	 * 
	 * CommonTree modelTree = filterTree; Model model; model = Traverse.loadModel(modelTree, Library.LIBRARIES);
	 * 
	 * return model;
	 * 
	 * }
	 * 
	 * public static Model readModel(File file) throws Exception { return Traverse.readModel(new FileInputStream(file)); }
	 * 
	 * public static Model readModel(String filename) throws Exception { return Traverse.readModel(new File(filename)); }
	 */
}
