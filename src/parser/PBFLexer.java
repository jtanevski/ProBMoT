// $ANTLR 3.2 Sep 23, 2009 12:02:23 D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g 2010-12-14 19:35:00

package parser;

import util.*;				//NOTE: comment in debug


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class PBFLexer extends Lexer {
    public static final int LT=26;
    public static final int STAR=31;
    public static final int LBRACE=23;
    public static final int DOUBLE_LITERAL=17;
    public static final int Exponent=8;
    public static final int LIBRARY=36;
    public static final int ID=46;
    public static final int EOF=-1;
    public static final int LPAREN=19;
    public static final int ENTITY=40;
    public static final int LBRACKET=21;
    public static final int RPAREN=20;
    public static final int STRING_LITERAL=18;
    public static final int SLASH=32;
    public static final int COMMA=28;
    public static final int BLOCK_COMMENT=15;
    public static final int BANGEQ=35;
    public static final int PLUS=9;
    public static final int MODEL=37;
    public static final int ExponentIndicator=6;
    public static final int Sign=7;
    public static final int RBRACKET=22;
    public static final int EQ=33;
    public static final int DOT=29;
    public static final int IdentifierPart=12;
    public static final int EQEQ=34;
    public static final int INT_LITERAL=16;
    public static final int RBRACE=24;
    public static final int LINE_COMMENT=14;
    public static final int NULL=45;
    public static final int IdentifierStart=11;
    public static final int INCOMPLETE=44;
    public static final int MINUS=10;
    public static final int Digit=5;
    public static final int UNSPECIFIED=43;
    public static final int SEMI=27;
    public static final int INF=42;
    public static final int COLON=30;
    public static final int WS=13;
    public static final int TEMPLATE=41;
    public static final int NonZeroDigit=4;
    public static final int COMPARTMENT=38;
    public static final int GT=25;
    public static final int PROCESS=39;


    public Token nextToken() {
    	while (true) {
    		state.token = null;
    		state.channel = Token.DEFAULT_CHANNEL;
    		state.tokenStartCharIndex = input.index();
    		state.tokenStartCharPositionInLine = input.getCharPositionInLine();
    		state.tokenStartLine = input.getLine();
    		state.text = null;
    		if ( input.LA(1)==CharStream.EOF ) {
    			return Token.EOF_TOKEN;
    		}
    		try {
    			mTokens();
    			if ( state.token==null ) {
    				emit();
    			}
    			else if ( state.token==Token.SKIP_TOKEN ) {
    				continue;
    			}
    			return state.token;
    		}
    		catch (RecognitionException re) {
    			reportError(re);
    		}
    	}
    }

    	public void reportError(RecognitionException e) {
    		// if we've already reported an error and have not matched a token
    		// yet successfully, don't report any errors.
    		if ( state.errorRecovery ) {
    			//System.err.print("[SPURIOUS] ");
    			return;
    		}
    		state.syntaxErrors++; // don't count spurious
    		state.errorRecovery = true;

    		int linenum = e.line;
    		int posnum = e.charPositionInLine;
    		
    		String message = getErrorMessage(e, this.getTokenNames());
    		throw new ParsingException(linenum, posnum, message, e);			//NOTE: comment in debug
    		//displayRecognitionError(this.getTokenNames(), e);
    	}


    protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow)
    		throws RecognitionException {	
    	throw new MismatchedTokenException(ttype, input);
    }



    // delegates
    // delegators

    public PBFLexer() {;} 
    public PBFLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public PBFLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g"; }

    // $ANTLR start "NonZeroDigit"
    public final void mNonZeroDigit() throws RecognitionException {
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:79:2: ( '1' .. '9' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:79:4: '1' .. '9'
            {
            matchRange('1','9'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "NonZeroDigit"

    // $ANTLR start "Digit"
    public final void mDigit() throws RecognitionException {
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:84:2: ( '0' | NonZeroDigit )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "Digit"

    // $ANTLR start "Exponent"
    public final void mExponent() throws RecognitionException {
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:90:2: ( ExponentIndicator ( Sign )? ( Digit )+ )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:90:4: ExponentIndicator ( Sign )? ( Digit )+
            {
            mExponentIndicator(); 
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:90:22: ( Sign )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='+'||LA1_0=='-') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:90:22: Sign
                    {
                    mSign(); 

                    }
                    break;

            }

            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:90:28: ( Digit )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='0' && LA2_0<='9')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:90:28: Digit
            	    {
            	    mDigit(); 

            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            }

        }
        finally {
        }
    }
    // $ANTLR end "Exponent"

    // $ANTLR start "ExponentIndicator"
    public final void mExponentIndicator() throws RecognitionException {
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:95:2: ( 'E' | 'e' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "ExponentIndicator"

    // $ANTLR start "Sign"
    public final void mSign() throws RecognitionException {
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:101:2: ( PLUS | MINUS )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:
            {
            if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "Sign"

    // $ANTLR start "IdentifierStart"
    public final void mIdentifierStart() throws RecognitionException {
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:107:2: ( 'A' .. 'Z' | 'a' .. 'z' | '_' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "IdentifierStart"

    // $ANTLR start "IdentifierPart"
    public final void mIdentifierPart() throws RecognitionException {
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:114:2: ( IdentifierStart | Digit )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "IdentifierPart"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:128:2: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:129:2: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:129:2: ( ' ' | '\\t' | '\\r' | '\\n' )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='\t' && LA3_0<='\n')||LA3_0=='\r'||LA3_0==' ') ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);

            _channel = HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "LINE_COMMENT"
    public final void mLINE_COMMENT() throws RecognitionException {
        try {
            int _type = LINE_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:136:2: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r\\n' | '\\r' | '\\n' ) | '//' (~ ( '\\n' | '\\r' ) )* )
            int alt7=2;
            alt7 = dfa7.predict(input);
            switch (alt7) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:136:4: '//' (~ ( '\\n' | '\\r' ) )* ( '\\r\\n' | '\\r' | '\\n' )
                    {
                    match("//"); 

                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:136:9: (~ ( '\\n' | '\\r' ) )*
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);

                        if ( ((LA4_0>='\u0000' && LA4_0<='\t')||(LA4_0>='\u000B' && LA4_0<='\f')||(LA4_0>='\u000E' && LA4_0<='\uFFFF')) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:136:9: ~ ( '\\n' | '\\r' )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;

                    	default :
                    	    break loop4;
                        }
                    } while (true);

                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:136:24: ( '\\r\\n' | '\\r' | '\\n' )
                    int alt5=3;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0=='\r') ) {
                        int LA5_1 = input.LA(2);

                        if ( (LA5_1=='\n') ) {
                            alt5=1;
                        }
                        else {
                            alt5=2;}
                    }
                    else if ( (LA5_0=='\n') ) {
                        alt5=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 5, 0, input);

                        throw nvae;
                    }
                    switch (alt5) {
                        case 1 :
                            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:136:25: '\\r\\n'
                            {
                            match("\r\n"); 


                            }
                            break;
                        case 2 :
                            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:136:34: '\\r'
                            {
                            match('\r'); 

                            }
                            break;
                        case 3 :
                            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:136:41: '\\n'
                            {
                            match('\n'); 

                            }
                            break;

                    }

                    _channel = HIDDEN;

                    }
                    break;
                case 2 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:137:4: '//' (~ ( '\\n' | '\\r' ) )*
                    {
                    match("//"); 

                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:137:9: (~ ( '\\n' | '\\r' ) )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( ((LA6_0>='\u0000' && LA6_0<='\t')||(LA6_0>='\u000B' && LA6_0<='\f')||(LA6_0>='\u000E' && LA6_0<='\uFFFF')) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:137:9: ~ ( '\\n' | '\\r' )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);

                    _channel = HIDDEN;

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LINE_COMMENT"

    // $ANTLR start "BLOCK_COMMENT"
    public final void mBLOCK_COMMENT() throws RecognitionException {
        try {
            int _type = BLOCK_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:141:2: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:141:4: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:141:9: ( options {greedy=false; } : . )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0=='*') ) {
                    int LA8_1 = input.LA(2);

                    if ( (LA8_1=='/') ) {
                        alt8=2;
                    }
                    else if ( ((LA8_1>='\u0000' && LA8_1<='.')||(LA8_1>='0' && LA8_1<='\uFFFF')) ) {
                        alt8=1;
                    }


                }
                else if ( ((LA8_0>='\u0000' && LA8_0<=')')||(LA8_0>='+' && LA8_0<='\uFFFF')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:141:36: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            match("*/"); 

            _channel = HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BLOCK_COMMENT"

    // $ANTLR start "INT_LITERAL"
    public final void mINT_LITERAL() throws RecognitionException {
        try {
            int _type = INT_LITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:148:2: ( '0' | NonZeroDigit ( Digit )* )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0=='0') ) {
                alt10=1;
            }
            else if ( ((LA10_0>='1' && LA10_0<='9')) ) {
                alt10=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }
            switch (alt10) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:148:4: '0'
                    {
                    match('0'); 

                    }
                    break;
                case 2 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:149:4: NonZeroDigit ( Digit )*
                    {
                    mNonZeroDigit(); 
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:149:17: ( Digit )*
                    loop9:
                    do {
                        int alt9=2;
                        int LA9_0 = input.LA(1);

                        if ( ((LA9_0>='0' && LA9_0<='9')) ) {
                            alt9=1;
                        }


                        switch (alt9) {
                    	case 1 :
                    	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:149:17: Digit
                    	    {
                    	    mDigit(); 

                    	    }
                    	    break;

                    	default :
                    	    break loop9;
                        }
                    } while (true);


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_LITERAL"

    // $ANTLR start "DOUBLE_LITERAL"
    public final void mDOUBLE_LITERAL() throws RecognitionException {
        try {
            int _type = DOUBLE_LITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:153:2: ( ( Digit )+ '.' ( Digit )* ( Exponent )? | '.' ( Digit )+ ( Exponent )? | ( Digit )+ Exponent )
            int alt17=3;
            alt17 = dfa17.predict(input);
            switch (alt17) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:153:4: ( Digit )+ '.' ( Digit )* ( Exponent )?
                    {
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:153:4: ( Digit )+
                    int cnt11=0;
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( ((LA11_0>='0' && LA11_0<='9')) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:153:4: Digit
                    	    {
                    	    mDigit(); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt11 >= 1 ) break loop11;
                                EarlyExitException eee =
                                    new EarlyExitException(11, input);
                                throw eee;
                        }
                        cnt11++;
                    } while (true);

                    match('.'); 
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:153:15: ( Digit )*
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( ((LA12_0>='0' && LA12_0<='9')) ) {
                            alt12=1;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:153:15: Digit
                    	    {
                    	    mDigit(); 

                    	    }
                    	    break;

                    	default :
                    	    break loop12;
                        }
                    } while (true);

                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:153:22: ( Exponent )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0=='E'||LA13_0=='e') ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:153:22: Exponent
                            {
                            mExponent(); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:154:4: '.' ( Digit )+ ( Exponent )?
                    {
                    match('.'); 
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:154:8: ( Digit )+
                    int cnt14=0;
                    loop14:
                    do {
                        int alt14=2;
                        int LA14_0 = input.LA(1);

                        if ( ((LA14_0>='0' && LA14_0<='9')) ) {
                            alt14=1;
                        }


                        switch (alt14) {
                    	case 1 :
                    	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:154:8: Digit
                    	    {
                    	    mDigit(); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt14 >= 1 ) break loop14;
                                EarlyExitException eee =
                                    new EarlyExitException(14, input);
                                throw eee;
                        }
                        cnt14++;
                    } while (true);

                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:154:15: ( Exponent )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0=='E'||LA15_0=='e') ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:154:15: Exponent
                            {
                            mExponent(); 

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:155:4: ( Digit )+ Exponent
                    {
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:155:4: ( Digit )+
                    int cnt16=0;
                    loop16:
                    do {
                        int alt16=2;
                        int LA16_0 = input.LA(1);

                        if ( ((LA16_0>='0' && LA16_0<='9')) ) {
                            alt16=1;
                        }


                        switch (alt16) {
                    	case 1 :
                    	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:155:4: Digit
                    	    {
                    	    mDigit(); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt16 >= 1 ) break loop16;
                                EarlyExitException eee =
                                    new EarlyExitException(16, input);
                                throw eee;
                        }
                        cnt16++;
                    } while (true);

                    mExponent(); 

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOUBLE_LITERAL"

    // $ANTLR start "STRING_LITERAL"
    public final void mSTRING_LITERAL() throws RecognitionException {
        try {
            int _type = STRING_LITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:159:5: ( '\"' (~ ( '\"' | '\\r' | '\\n' ) )* '\"' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:159:9: '\"' (~ ( '\"' | '\\r' | '\\n' ) )* '\"'
            {
            match('\"'); 
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:159:13: (~ ( '\"' | '\\r' | '\\n' ) )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( ((LA18_0>='\u0000' && LA18_0<='\t')||(LA18_0>='\u000B' && LA18_0<='\f')||(LA18_0>='\u000E' && LA18_0<='!')||(LA18_0>='#' && LA18_0<='\uFFFF')) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:159:14: ~ ( '\"' | '\\r' | '\\n' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop18;
                }
            } while (true);

            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING_LITERAL"

    // $ANTLR start "LPAREN"
    public final void mLPAREN() throws RecognitionException {
        try {
            int _type = LPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:167:8: ( '(' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:167:10: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LPAREN"

    // $ANTLR start "RPAREN"
    public final void mRPAREN() throws RecognitionException {
        try {
            int _type = RPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:169:8: ( ')' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:169:10: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RPAREN"

    // $ANTLR start "LBRACKET"
    public final void mLBRACKET() throws RecognitionException {
        try {
            int _type = LBRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:171:10: ( '[' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:171:12: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LBRACKET"

    // $ANTLR start "RBRACKET"
    public final void mRBRACKET() throws RecognitionException {
        try {
            int _type = RBRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:173:10: ( ']' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:173:12: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RBRACKET"

    // $ANTLR start "LBRACE"
    public final void mLBRACE() throws RecognitionException {
        try {
            int _type = LBRACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:175:8: ( '{' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:175:10: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LBRACE"

    // $ANTLR start "RBRACE"
    public final void mRBRACE() throws RecognitionException {
        try {
            int _type = RBRACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:177:8: ( '}' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:177:10: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RBRACE"

    // $ANTLR start "GT"
    public final void mGT() throws RecognitionException {
        try {
            int _type = GT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:179:6: ( '>' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:179:8: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GT"

    // $ANTLR start "LT"
    public final void mLT() throws RecognitionException {
        try {
            int _type = LT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:181:6: ( '<' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:181:8: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LT"

    // $ANTLR start "SEMI"
    public final void mSEMI() throws RecognitionException {
        try {
            int _type = SEMI;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:186:7: ( ';' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:186:9: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SEMI"

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:188:8: ( ',' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:188:10: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMA"

    // $ANTLR start "DOT"
    public final void mDOT() throws RecognitionException {
        try {
            int _type = DOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:190:6: ( '.' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:190:8: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOT"

    // $ANTLR start "COLON"
    public final void mCOLON() throws RecognitionException {
        try {
            int _type = COLON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:192:8: ( ':' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:192:10: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COLON"

    // $ANTLR start "PLUS"
    public final void mPLUS() throws RecognitionException {
        try {
            int _type = PLUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:197:7: ( '+' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:197:9: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PLUS"

    // $ANTLR start "MINUS"
    public final void mMINUS() throws RecognitionException {
        try {
            int _type = MINUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:199:8: ( '-' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:199:10: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MINUS"

    // $ANTLR start "STAR"
    public final void mSTAR() throws RecognitionException {
        try {
            int _type = STAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:201:7: ( '*' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:201:9: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STAR"

    // $ANTLR start "SLASH"
    public final void mSLASH() throws RecognitionException {
        try {
            int _type = SLASH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:203:8: ( '/' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:203:10: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SLASH"

    // $ANTLR start "EQ"
    public final void mEQ() throws RecognitionException {
        try {
            int _type = EQ;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:205:6: ( '=' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:205:8: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EQ"

    // $ANTLR start "EQEQ"
    public final void mEQEQ() throws RecognitionException {
        try {
            int _type = EQEQ;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:207:7: ( '==' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:207:9: '=='
            {
            match("=="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EQEQ"

    // $ANTLR start "BANGEQ"
    public final void mBANGEQ() throws RecognitionException {
        try {
            int _type = BANGEQ;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:209:8: ( '!=' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:209:10: '!='
            {
            match("!="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BANGEQ"

    // $ANTLR start "LIBRARY"
    public final void mLIBRARY() throws RecognitionException {
        try {
            int _type = LIBRARY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:216:10: ( 'library' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:216:12: 'library'
            {
            match("library"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LIBRARY"

    // $ANTLR start "MODEL"
    public final void mMODEL() throws RecognitionException {
        try {
            int _type = MODEL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:218:9: ( 'model' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:218:11: 'model'
            {
            match("model"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MODEL"

    // $ANTLR start "COMPARTMENT"
    public final void mCOMPARTMENT() throws RecognitionException {
        try {
            int _type = COMPARTMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:220:13: ( 'compartment' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:220:15: 'compartment'
            {
            match("compartment"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMPARTMENT"

    // $ANTLR start "PROCESS"
    public final void mPROCESS() throws RecognitionException {
        try {
            int _type = PROCESS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:222:10: ( 'process' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:222:12: 'process'
            {
            match("process"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PROCESS"

    // $ANTLR start "ENTITY"
    public final void mENTITY() throws RecognitionException {
        try {
            int _type = ENTITY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:224:9: ( 'entity' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:224:11: 'entity'
            {
            match("entity"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ENTITY"

    // $ANTLR start "TEMPLATE"
    public final void mTEMPLATE() throws RecognitionException {
        try {
            int _type = TEMPLATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:226:11: ( 'template' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:226:13: 'template'
            {
            match("template"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TEMPLATE"

    // $ANTLR start "INF"
    public final void mINF() throws RecognitionException {
        try {
            int _type = INF;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:228:7: ( 'inf' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:228:9: 'inf'
            {
            match("inf"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INF"

    // $ANTLR start "UNSPECIFIED"
    public final void mUNSPECIFIED() throws RecognitionException {
        try {
            int _type = UNSPECIFIED;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:230:13: ( 'unspecified' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:230:15: 'unspecified'
            {
            match("unspecified"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "UNSPECIFIED"

    // $ANTLR start "INCOMPLETE"
    public final void mINCOMPLETE() throws RecognitionException {
        try {
            int _type = INCOMPLETE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:232:12: ( 'incomplete' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:232:14: 'incomplete'
            {
            match("incomplete"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INCOMPLETE"

    // $ANTLR start "NULL"
    public final void mNULL() throws RecognitionException {
        try {
            int _type = NULL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:237:8: ( 'null' )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:237:10: 'null'
            {
            match("null"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NULL"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:243:2: ( IdentifierStart ( IdentifierPart )* )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:243:4: IdentifierStart ( IdentifierPart )*
            {
            mIdentifierStart(); 
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:243:20: ( IdentifierPart )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( ((LA19_0>='0' && LA19_0<='9')||(LA19_0>='A' && LA19_0<='Z')||LA19_0=='_'||(LA19_0>='a' && LA19_0<='z')) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:243:20: IdentifierPart
            	    {
            	    mIdentifierPart(); 

            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ID"

    public void mTokens() throws RecognitionException {
        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:8: ( WS | LINE_COMMENT | BLOCK_COMMENT | INT_LITERAL | DOUBLE_LITERAL | STRING_LITERAL | LPAREN | RPAREN | LBRACKET | RBRACKET | LBRACE | RBRACE | GT | LT | SEMI | COMMA | DOT | COLON | PLUS | MINUS | STAR | SLASH | EQ | EQEQ | BANGEQ | LIBRARY | MODEL | COMPARTMENT | PROCESS | ENTITY | TEMPLATE | INF | UNSPECIFIED | INCOMPLETE | NULL | ID )
        int alt20=36;
        alt20 = dfa20.predict(input);
        switch (alt20) {
            case 1 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:10: WS
                {
                mWS(); 

                }
                break;
            case 2 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:13: LINE_COMMENT
                {
                mLINE_COMMENT(); 

                }
                break;
            case 3 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:26: BLOCK_COMMENT
                {
                mBLOCK_COMMENT(); 

                }
                break;
            case 4 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:40: INT_LITERAL
                {
                mINT_LITERAL(); 

                }
                break;
            case 5 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:52: DOUBLE_LITERAL
                {
                mDOUBLE_LITERAL(); 

                }
                break;
            case 6 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:67: STRING_LITERAL
                {
                mSTRING_LITERAL(); 

                }
                break;
            case 7 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:82: LPAREN
                {
                mLPAREN(); 

                }
                break;
            case 8 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:89: RPAREN
                {
                mRPAREN(); 

                }
                break;
            case 9 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:96: LBRACKET
                {
                mLBRACKET(); 

                }
                break;
            case 10 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:105: RBRACKET
                {
                mRBRACKET(); 

                }
                break;
            case 11 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:114: LBRACE
                {
                mLBRACE(); 

                }
                break;
            case 12 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:121: RBRACE
                {
                mRBRACE(); 

                }
                break;
            case 13 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:128: GT
                {
                mGT(); 

                }
                break;
            case 14 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:131: LT
                {
                mLT(); 

                }
                break;
            case 15 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:134: SEMI
                {
                mSEMI(); 

                }
                break;
            case 16 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:139: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 17 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:145: DOT
                {
                mDOT(); 

                }
                break;
            case 18 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:149: COLON
                {
                mCOLON(); 

                }
                break;
            case 19 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:155: PLUS
                {
                mPLUS(); 

                }
                break;
            case 20 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:160: MINUS
                {
                mMINUS(); 

                }
                break;
            case 21 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:166: STAR
                {
                mSTAR(); 

                }
                break;
            case 22 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:171: SLASH
                {
                mSLASH(); 

                }
                break;
            case 23 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:177: EQ
                {
                mEQ(); 

                }
                break;
            case 24 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:180: EQEQ
                {
                mEQEQ(); 

                }
                break;
            case 25 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:185: BANGEQ
                {
                mBANGEQ(); 

                }
                break;
            case 26 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:192: LIBRARY
                {
                mLIBRARY(); 

                }
                break;
            case 27 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:200: MODEL
                {
                mMODEL(); 

                }
                break;
            case 28 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:206: COMPARTMENT
                {
                mCOMPARTMENT(); 

                }
                break;
            case 29 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:218: PROCESS
                {
                mPROCESS(); 

                }
                break;
            case 30 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:226: ENTITY
                {
                mENTITY(); 

                }
                break;
            case 31 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:233: TEMPLATE
                {
                mTEMPLATE(); 

                }
                break;
            case 32 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:242: INF
                {
                mINF(); 

                }
                break;
            case 33 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:246: UNSPECIFIED
                {
                mUNSPECIFIED(); 

                }
                break;
            case 34 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:258: INCOMPLETE
                {
                mINCOMPLETE(); 

                }
                break;
            case 35 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:269: NULL
                {
                mNULL(); 

                }
                break;
            case 36 :
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFLexer.g:1:274: ID
                {
                mID(); 

                }
                break;

        }

    }


    protected DFA7 dfa7 = new DFA7(this);
    protected DFA17 dfa17 = new DFA17(this);
    protected DFA20 dfa20 = new DFA20(this);
    static final String DFA7_eotS =
        "\2\uffff\2\4\2\uffff";
    static final String DFA7_eofS =
        "\6\uffff";
    static final String DFA7_minS =
        "\2\57\2\0\2\uffff";
    static final String DFA7_maxS =
        "\2\57\2\uffff\2\uffff";
    static final String DFA7_acceptS =
        "\4\uffff\1\2\1\1";
    static final String DFA7_specialS =
        "\2\uffff\1\0\1\1\2\uffff}>";
    static final String[] DFA7_transitionS = {
            "\1\1",
            "\1\2",
            "\12\3\1\5\2\3\1\5\ufff2\3",
            "\12\3\1\5\2\3\1\5\ufff2\3",
            "",
            ""
    };

    static final short[] DFA7_eot = DFA.unpackEncodedString(DFA7_eotS);
    static final short[] DFA7_eof = DFA.unpackEncodedString(DFA7_eofS);
    static final char[] DFA7_min = DFA.unpackEncodedStringToUnsignedChars(DFA7_minS);
    static final char[] DFA7_max = DFA.unpackEncodedStringToUnsignedChars(DFA7_maxS);
    static final short[] DFA7_accept = DFA.unpackEncodedString(DFA7_acceptS);
    static final short[] DFA7_special = DFA.unpackEncodedString(DFA7_specialS);
    static final short[][] DFA7_transition;

    static {
        int numStates = DFA7_transitionS.length;
        DFA7_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA7_transition[i] = DFA.unpackEncodedString(DFA7_transitionS[i]);
        }
    }

    class DFA7 extends DFA {

        public DFA7(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 7;
            this.eot = DFA7_eot;
            this.eof = DFA7_eof;
            this.min = DFA7_min;
            this.max = DFA7_max;
            this.accept = DFA7_accept;
            this.special = DFA7_special;
            this.transition = DFA7_transition;
        }
        public String getDescription() {
            return "135:1: LINE_COMMENT : ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r\\n' | '\\r' | '\\n' ) | '//' (~ ( '\\n' | '\\r' ) )* );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA7_2 = input.LA(1);

                        s = -1;
                        if ( ((LA7_2>='\u0000' && LA7_2<='\t')||(LA7_2>='\u000B' && LA7_2<='\f')||(LA7_2>='\u000E' && LA7_2<='\uFFFF')) ) {s = 3;}

                        else if ( (LA7_2=='\n'||LA7_2=='\r') ) {s = 5;}

                        else s = 4;

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA7_3 = input.LA(1);

                        s = -1;
                        if ( ((LA7_3>='\u0000' && LA7_3<='\t')||(LA7_3>='\u000B' && LA7_3<='\f')||(LA7_3>='\u000E' && LA7_3<='\uFFFF')) ) {s = 3;}

                        else if ( (LA7_3=='\n'||LA7_3=='\r') ) {s = 5;}

                        else s = 4;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 7, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA17_eotS =
        "\5\uffff";
    static final String DFA17_eofS =
        "\5\uffff";
    static final String DFA17_minS =
        "\2\56\3\uffff";
    static final String DFA17_maxS =
        "\1\71\1\145\3\uffff";
    static final String DFA17_acceptS =
        "\2\uffff\1\2\1\1\1\3";
    static final String DFA17_specialS =
        "\5\uffff}>";
    static final String[] DFA17_transitionS = {
            "\1\2\1\uffff\12\1",
            "\1\3\1\uffff\12\1\13\uffff\1\4\37\uffff\1\4",
            "",
            "",
            ""
    };

    static final short[] DFA17_eot = DFA.unpackEncodedString(DFA17_eotS);
    static final short[] DFA17_eof = DFA.unpackEncodedString(DFA17_eofS);
    static final char[] DFA17_min = DFA.unpackEncodedStringToUnsignedChars(DFA17_minS);
    static final char[] DFA17_max = DFA.unpackEncodedStringToUnsignedChars(DFA17_maxS);
    static final short[] DFA17_accept = DFA.unpackEncodedString(DFA17_acceptS);
    static final short[] DFA17_special = DFA.unpackEncodedString(DFA17_specialS);
    static final short[][] DFA17_transition;

    static {
        int numStates = DFA17_transitionS.length;
        DFA17_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA17_transition[i] = DFA.unpackEncodedString(DFA17_transitionS[i]);
        }
    }

    class DFA17 extends DFA {

        public DFA17(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 17;
            this.eot = DFA17_eot;
            this.eof = DFA17_eof;
            this.min = DFA17_min;
            this.max = DFA17_max;
            this.accept = DFA17_accept;
            this.special = DFA17_special;
            this.transition = DFA17_transition;
        }
        public String getDescription() {
            return "152:1: DOUBLE_LITERAL : ( ( Digit )+ '.' ( Digit )* ( Exponent )? | '.' ( Digit )+ ( Exponent )? | ( Digit )+ Exponent );";
        }
    }
    static final String DFA20_eotS =
        "\2\uffff\1\43\2\44\1\47\17\uffff\1\51\1\uffff\11\40\6\uffff\1\44"+
        "\3\uffff\17\40\1\103\11\40\1\uffff\2\40\1\117\1\40\1\121\6\40\1"+
        "\uffff\1\40\1\uffff\2\40\1\133\3\40\1\137\1\40\1\141\1\uffff\3\40"+
        "\1\uffff\1\40\1\uffff\1\146\3\40\1\uffff\3\40\1\155\1\40\1\157\1"+
        "\uffff\1\160\2\uffff";
    static final String DFA20_eofS =
        "\161\uffff";
    static final String DFA20_minS =
        "\1\11\1\uffff\1\52\2\56\1\60\17\uffff\1\75\1\uffff\1\151\2\157"+
        "\1\162\1\156\1\145\2\156\1\165\6\uffff\1\56\3\uffff\1\142\1\144"+
        "\1\155\1\157\1\164\1\155\1\143\1\163\1\154\1\162\1\145\1\160\1\143"+
        "\1\151\1\160\1\60\1\157\1\160\1\154\1\141\1\154\1\141\1\145\1\164"+
        "\1\154\1\uffff\1\155\1\145\1\60\1\162\1\60\1\162\1\163\1\171\1\141"+
        "\1\160\1\143\1\uffff\1\171\1\uffff\1\164\1\163\1\60\1\164\1\154"+
        "\1\151\1\60\1\155\1\60\1\uffff\2\145\1\146\1\uffff\1\145\1\uffff"+
        "\1\60\1\164\1\151\1\156\1\uffff\2\145\1\164\1\60\1\144\1\60\1\uffff"+
        "\1\60\2\uffff";
    static final String DFA20_maxS =
        "\1\175\1\uffff\1\57\2\145\1\71\17\uffff\1\75\1\uffff\1\151\2\157"+
        "\1\162\1\156\1\145\2\156\1\165\6\uffff\1\145\3\uffff\1\142\1\144"+
        "\1\155\1\157\1\164\1\155\1\146\1\163\1\154\1\162\1\145\1\160\1\143"+
        "\1\151\1\160\1\172\1\157\1\160\1\154\1\141\1\154\1\141\1\145\1\164"+
        "\1\154\1\uffff\1\155\1\145\1\172\1\162\1\172\1\162\1\163\1\171\1"+
        "\141\1\160\1\143\1\uffff\1\171\1\uffff\1\164\1\163\1\172\1\164\1"+
        "\154\1\151\1\172\1\155\1\172\1\uffff\2\145\1\146\1\uffff\1\145\1"+
        "\uffff\1\172\1\164\1\151\1\156\1\uffff\2\145\1\164\1\172\1\144\1"+
        "\172\1\uffff\1\172\2\uffff";
    static final String DFA20_acceptS =
        "\1\uffff\1\1\4\uffff\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1\15\1\16"+
        "\1\17\1\20\1\22\1\23\1\24\1\25\1\uffff\1\31\11\uffff\1\44\1\2\1"+
        "\3\1\26\1\4\1\5\1\uffff\1\21\1\30\1\27\31\uffff\1\40\13\uffff\1"+
        "\43\1\uffff\1\33\11\uffff\1\36\3\uffff\1\32\1\uffff\1\35\4\uffff"+
        "\1\37\6\uffff\1\42\1\uffff\1\34\1\41";
    static final String DFA20_specialS =
        "\161\uffff}>";
    static final String[] DFA20_transitionS = {
            "\2\1\2\uffff\1\1\22\uffff\1\1\1\26\1\6\5\uffff\1\7\1\10\1\24"+
            "\1\22\1\20\1\23\1\5\1\2\1\3\11\4\1\21\1\17\1\16\1\25\1\15\2"+
            "\uffff\32\40\1\11\1\uffff\1\12\1\uffff\1\40\1\uffff\2\40\1\31"+
            "\1\40\1\33\3\40\1\35\2\40\1\27\1\30\1\37\1\40\1\32\3\40\1\34"+
            "\1\36\5\40\1\13\1\uffff\1\14",
            "",
            "\1\42\4\uffff\1\41",
            "\1\45\1\uffff\12\45\13\uffff\1\45\37\uffff\1\45",
            "\1\45\1\uffff\12\46\13\uffff\1\45\37\uffff\1\45",
            "\12\45",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\50",
            "",
            "\1\52",
            "\1\53",
            "\1\54",
            "\1\55",
            "\1\56",
            "\1\57",
            "\1\60",
            "\1\61",
            "\1\62",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\45\1\uffff\12\46\13\uffff\1\45\37\uffff\1\45",
            "",
            "",
            "",
            "\1\63",
            "\1\64",
            "\1\65",
            "\1\66",
            "\1\67",
            "\1\70",
            "\1\72\2\uffff\1\71",
            "\1\73",
            "\1\74",
            "\1\75",
            "\1\76",
            "\1\77",
            "\1\100",
            "\1\101",
            "\1\102",
            "\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\1\104",
            "\1\105",
            "\1\106",
            "\1\107",
            "\1\110",
            "\1\111",
            "\1\112",
            "\1\113",
            "\1\114",
            "",
            "\1\115",
            "\1\116",
            "\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\1\120",
            "\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\1\122",
            "\1\123",
            "\1\124",
            "\1\125",
            "\1\126",
            "\1\127",
            "",
            "\1\130",
            "",
            "\1\131",
            "\1\132",
            "\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\1\134",
            "\1\135",
            "\1\136",
            "\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\1\140",
            "\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "",
            "\1\142",
            "\1\143",
            "\1\144",
            "",
            "\1\145",
            "",
            "\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\1\147",
            "\1\150",
            "\1\151",
            "",
            "\1\152",
            "\1\153",
            "\1\154",
            "\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\1\156",
            "\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "",
            "\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "",
            ""
    };

    static final short[] DFA20_eot = DFA.unpackEncodedString(DFA20_eotS);
    static final short[] DFA20_eof = DFA.unpackEncodedString(DFA20_eofS);
    static final char[] DFA20_min = DFA.unpackEncodedStringToUnsignedChars(DFA20_minS);
    static final char[] DFA20_max = DFA.unpackEncodedStringToUnsignedChars(DFA20_maxS);
    static final short[] DFA20_accept = DFA.unpackEncodedString(DFA20_acceptS);
    static final short[] DFA20_special = DFA.unpackEncodedString(DFA20_specialS);
    static final short[][] DFA20_transition;

    static {
        int numStates = DFA20_transitionS.length;
        DFA20_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA20_transition[i] = DFA.unpackEncodedString(DFA20_transitionS[i]);
        }
    }

    class DFA20 extends DFA {

        public DFA20(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 20;
            this.eot = DFA20_eot;
            this.eof = DFA20_eof;
            this.min = DFA20_min;
            this.max = DFA20_max;
            this.accept = DFA20_accept;
            this.special = DFA20_special;
            this.transition = DFA20_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( WS | LINE_COMMENT | BLOCK_COMMENT | INT_LITERAL | DOUBLE_LITERAL | STRING_LITERAL | LPAREN | RPAREN | LBRACKET | RBRACKET | LBRACE | RBRACE | GT | LT | SEMI | COMMA | DOT | COLON | PLUS | MINUS | STAR | SLASH | EQ | EQEQ | BANGEQ | LIBRARY | MODEL | COMPARTMENT | PROCESS | ENTITY | TEMPLATE | INF | UNSPECIFIED | INCOMPLETE | NULL | ID );";
        }
    }
 

}