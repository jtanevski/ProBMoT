package run;

import java.io.*;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

import parser.*;

public class PBFFilterDebug {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		ANTLRInputStream inputStream;
		PBFLexer lexer;
		CommonTokenStream tokenStream;
		PBFParser parser;
		PBFParser.file_return parserReturn;
		CommonTree parserTree;
		CommonTreeNodeStream nodeStream;
		PBFFilter filter;
		PBFFilter.prog_return filterReturn;
		CommonTree filterTree;

		System.out.println("Library");
		inputStream = new ANTLRInputStream(new FileInputStream("res\\AquaticEcosystem.pbl"));
		lexer = new PBFLexer(inputStream);
		tokenStream = new CommonTokenStream();
		tokenStream.setTokenSource(lexer);

		parser = new PBFParser(tokenStream);
		parserReturn = parser.file();

		parserTree = (CommonTree) parserReturn.getTree();
		System.out.println(parserTree.toStringTree());

		nodeStream = new CommonTreeNodeStream(parserTree);
		nodeStream.setTokenStream(tokenStream);
		System.out.println(nodeStream.toString());

		filter = new PBFFilter(nodeStream, /* 50000, */ null); // uncomment for debugging, leave it commented otherwise

		filterReturn = filter.prog();
		filterTree = (CommonTree) filterReturn.getTree();
		System.out.println(filterTree.toStringTree());

	}

}
