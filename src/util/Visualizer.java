package util;

import java.io.*;
import java.util.*;

import org.apache.commons.lang3.*;

import struct.temp.*;

public class Visualizer {
	public static String toDotString(Library library, DotFlag flag, boolean showRoot) {
		StringBuilder libDot = new StringBuilder();

		libDot.append("digraph EntityHierarchy {\n\n");

		libDot.append("\tnode[\n");
		libDot.append("\t\tshape=plaintext\n");
		//		libDot.append("\t\tfontname = \"Bitstream Vera Sans\"\n");
		libDot.append("\t\tfontsize = 10\n");
		libDot.append("\t]\n\n");

		for (TE te : library.tes.values()) {
			if (showRoot == true || te != library.ENTITY)
				libDot.append(toDotString(te, flag) + "\n");
		}

		for (TE te : library.tes.values()) {
			if (showRoot == true || te != library.ENTITY) {
				for (TE subTE : te.subs.values()) {
					libDot.append("\t" + te.id + " -> " + subTE.id + ";\n");
				}
			}
		}

		libDot.append("\n\n");
		
		for (TP tp : library.tps.values()) {
			if (showRoot == true || tp != library.PROCESS)
				libDot.append(toDotString(tp, flag) + "\n");
		}

		for (TP tp : library.tps.values()) {
			if (showRoot == true || tp != library.PROCESS) {
				for (TP subTP : tp.subs.values()) {
					libDot.append("\t" + tp.id + " -> " + subTP.id + ";\n");
				}
			}
		}
		libDot.append("}\n");

		return libDot.toString();
	}

	public static void toDotFile(Library library, DotFlag flag, boolean showRoot, String filename)
			throws FileNotFoundException {
		String dotString = toDotString(library, flag, showRoot);
		PrintStream out = new PrintStream(filename);
		out.print(dotString);
		out.close();
	}

	public static String toDotString(TE te, DotFlag flag) {
		StringBuilder teDot = new StringBuilder();

		// Header
		teDot.append("\t" + te.id + " [\n");
		teDot.append("\t\tlabel=<\n");
		teDot.append("\t\t\t<TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"1\" >\n");
		teDot.append("\t\t\t\t<TR><TD>" + te.id + "</TD></TR>\n");

		if (flag == DotFlag.ALL || flag == DotFlag.PROPER) {

			// Vars
			teDot.append("\t\t\t\t<TR><TD>\n");
			teDot.append("\t\t\t\t\t<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\" >\n");
			Set<TV> vars = new LinkedHashSet<TV>(te.vars.values());
			if (flag == DotFlag.PROPER && te.zuper != null)
				vars.removeAll(te.zuper.vars.values());
			if (vars.isEmpty()) {
				teDot.append("\t\t\t\t\t\t<TR><TD ALIGN=\"LEFT\"></TD></TR>\n");
			} else {
				for (TV tv : vars) {
					teDot.append("\t\t\t\t\t\t" + toDotString(tv) + "\n");
				}
			}
			teDot.append("\t\t\t\t\t</TABLE>\n");
			teDot.append("\t\t\t\t</TD></TR>\n");

			// Consts
			teDot.append("\t\t\t\t<TR><TD>\n");
			teDot.append("\t\t\t\t\t<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\" >\n");
			Set<TC> consts = new LinkedHashSet<TC>(te.consts.values());
			if (flag == DotFlag.PROPER && te.zuper != null)
				consts.removeAll(te.zuper.consts.values());
			if (consts.isEmpty()) {
				teDot.append("\t\t\t\t\t\t<TR><TD ALIGN=\"LEFT\"></TD></TR>\n");
			} else {
				for (TC tc : consts) {
					teDot.append("\t\t\t\t\t\t" + toDotString(tc) + "\n");
				}
			}
			teDot.append("\t\t\t\t\t</TABLE>\n");
			teDot.append("\t\t\t\t</TD></TR>\n");

		}

		// Closing
		teDot.append("\t\t\t</TABLE>\n");
		teDot.append("\t\t>\n");
		teDot.append("\t];\n");

		return teDot.toString();
	}


	public static String toDotString(TP tp, DotFlag flag) {
		StringBuilder tpDot = new StringBuilder();

		// Header
		tpDot.append("\t" + tp.id + " [\n");
		tpDot.append("\t\tlabel=<\n");
		tpDot.append("\t\t\t<TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"1\" >\n");
		tpDot.append("\t\t\t\t<TR><TD>" + tp.id + "</TD></TR>\n");

		if (flag == DotFlag.ALL || flag == DotFlag.PROPER) {

			// Arguments
			tpDot.append("\t\t\t\t<TR><TD>\n");
			tpDot.append("\t\t\t\t\t<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\" >\n");
			Set<PE> args = new LinkedHashSet<PE>(tp.parameters.values());
			if (flag == DotFlag.PROPER && tp.zuper != null)
				args.removeAll(tp.zuper.parameters.values());
			if (args.isEmpty()) {
				tpDot.append("\t\t\t\t\t\t<TR><TD ALIGN=\"LEFT\"></TD></TR>\n");
			} else {
				for (PE pe : args) {
					tpDot.append("\t\t\t\t\t\t" + toDotString(pe) + "\n");
				}
			}
			tpDot.append("\t\t\t\t\t</TABLE>\n");
			tpDot.append("\t\t\t\t</TD></TR>\n");
			
			
			// Consts
			tpDot.append("\t\t\t\t<TR><TD>\n");
			tpDot.append("\t\t\t\t\t<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\" >\n");
			Set<TC> consts = new LinkedHashSet<TC>(tp.consts.values());
			if (flag == DotFlag.PROPER && tp.zuper != null)
				consts.removeAll(tp.zuper.consts.values());
			if (consts.isEmpty()) {
				tpDot.append("\t\t\t\t\t\t<TR><TD ALIGN=\"LEFT\"></TD></TR>\n");
			} else {
				for (TC tc : consts) {
					tpDot.append("\t\t\t\t\t\t" + toDotString(tc) + "\n");
				}
			}
			tpDot.append("\t\t\t\t\t</TABLE>\n");
			tpDot.append("\t\t\t\t</TD></TR>\n");
			
			// Processes
			tpDot.append("\t\t\t\t<TR><TD>\n");
			tpDot.append("\t\t\t\t\t<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\" >\n");
			List<PP> processes = new LinkedList<PP>(tp.processes);
			if (flag == DotFlag.PROPER && tp.zuper != null)
				processes.removeAll(tp.zuper.processes);
			if (processes.isEmpty()) {
				tpDot.append("\t\t\t\t\t\t<TR><TD ALIGN=\"LEFT\"></TD></TR>\n");
			} else {
				for (PP pp : processes) {
					tpDot.append("\t\t\t\t\t\t" + toDotString(pp) + "\n");
				}
			}
			tpDot.append("\t\t\t\t\t</TABLE>\n");
			tpDot.append("\t\t\t\t</TD></TR>\n");

			// Equations
			tpDot.append("\t\t\t\t<TR><TD>\n");
			tpDot.append("\t\t\t\t\t<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\" >\n");
			List<TQ> equations = new LinkedList<TQ>(tp.equations);
			if (flag == DotFlag.PROPER && tp.zuper != null)
				equations.removeAll(tp.zuper.equations);
			if (equations.isEmpty()) {
				tpDot.append("\t\t\t\t\t\t<TR><TD ALIGN=\"LEFT\"></TD></TR>\n");
			} else {
				for (TQ tq : equations) {
					tpDot.append("\t\t\t\t\t\t" + toDotString(tq) + "\n");
				}
			}
			tpDot.append("\t\t\t\t\t</TABLE>\n");
			tpDot.append("\t\t\t\t</TD></TR>\n");

		}

		// Closing
		tpDot.append("\t\t\t</TABLE>\n");
		tpDot.append("\t\t>\n");
		tpDot.append("\t];\n");

		return tpDot.toString();
	}

	
	public static String toDotString(TV tv) {
		String tvDot = String.format("<TR><TD ALIGN=\"LEFT\">%s %s &lt;%s,%s&gt;</TD></TR>", tv.agg.toChar(), tv.id, tv.range.getLower(),
				tv.range.getUpper());
		return tvDot;
	}

	public static String toDotString(TC tc) {
		String tcDot = String.format("<TR><TD ALIGN=\"LEFT\">  %s &lt;%s,%s&gt;</TD></TR>", tc.id, tc.range.getLower(), tc.range.getUpper());
		return tcDot;
	}
	
	public static String toDotString(PE pe) {
		String peDot = String.format("<TR><TD ALIGN=\"LEFT\">%s</TD></TR>", StringEscapeUtils.escapeHtml4(pe.toString()));
		return peDot;
	}
	
	public static String toDotString(PP pp) {
		String ppDot = String.format("<TR><TD ALIGN=\"LEFT\">%s</TD></TR>", StringEscapeUtils.escapeHtml4(pp.toString()));
		return ppDot;
	}

	public static String toDotString(TQ tq) {
		String tqDot = String.format("<TR><TD ALIGN=\"LEFT\">%s</TD></TR>", StringEscapeUtils.escapeHtml4(tq.toString()));
		return tqDot;
	}
	
}
