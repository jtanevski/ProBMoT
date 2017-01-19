package util;

import org.antlr.runtime.*;
import org.slf4j.*;

public class ParsingException
		extends RuntimeException {

	private String filename;
	private int linenum;
	private int posnum;
	
	public ParsingException(String filename, int linenum, int posnum, String message, Throwable cause) {
		super(message, cause);
		
		this.filename = filename;
		this.linenum = linenum;
		this.posnum = posnum;
	}
	
	
	public ParsingException(int linenum, int posnum, String message, Throwable cause) {
		this(MDC.get("file"), linenum, posnum, message, cause);
	}
	
	public ParsingException(String message, Throwable cause) {
		this(-1, -1, message, cause);
	}
	

	public ParsingException(String filename, int linenum, int posnum, String message) {
		this(filename, linenum, posnum, message, null);
	}

	public ParsingException(int linenum, int posnum, String message) {
		this(MDC.get("file"), linenum, posnum, message);
	}
	public ParsingException(Token token, String message) {
		this(token.getLine(), token.getCharPositionInLine(), message);
	}
	
	public ParsingException(String message) {
		this(-1, -1, message);
	}

	public ParsingException() {
		this(null);
	}

	public ParsingException(String filename, Token token, String message) {
		this(filename, token.getLine(), token.getCharPositionInLine(), message);
	}
	public ParsingException(String filename, String message) {
		this(filename, -1, -1, message);
	}

	public String getFilename() {
		return this.filename;
	}
	
	public int getLinenum() {
		return this.linenum;
	}
	
	public int getPosnum() {
		return this.posnum;
	}
}
