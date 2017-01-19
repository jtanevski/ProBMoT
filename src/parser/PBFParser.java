// $ANTLR 3.2 Sep 23, 2009 12:02:23 D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g 2010-12-14 19:35:01

package parser;

import util.*;      //NOTE: comment in debug


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;

public class PBFParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "NonZeroDigit", "Digit", "ExponentIndicator", "Sign", "Exponent", "PLUS", "MINUS", "IdentifierStart", "IdentifierPart", "WS", "LINE_COMMENT", "BLOCK_COMMENT", "INT_LITERAL", "DOUBLE_LITERAL", "STRING_LITERAL", "LPAREN", "RPAREN", "LBRACKET", "RBRACKET", "LBRACE", "RBRACE", "GT", "LT", "SEMI", "COMMA", "DOT", "COLON", "STAR", "SLASH", "EQ", "EQEQ", "BANGEQ", "LIBRARY", "MODEL", "COMPARTMENT", "PROCESS", "ENTITY", "TEMPLATE", "INF", "UNSPECIFIED", "INCOMPLETE", "NULL", "ID", "FILE", "DECL", "DEFS", "SUPER", "TYPE", "PARAM", "ARG", "CALL", "ITER", "STRUCT", "LIST", "INTER", "TES", "TPS", "IES", "IPS", "FES", "FPS", "MES", "MPS", "IKS", "TE", "TP", "IE", "IP", "FE", "FP", "ME", "MP"
    };
    public static final int LT=26;
    public static final int STAR=31;
    public static final int FES=63;
    public static final int LBRACE=23;
    public static final int DOUBLE_LITERAL=17;
    public static final int ME=74;
    public static final int Exponent=8;
    public static final int PARAM=52;
    public static final int LIBRARY=36;
    public static final int ID=46;
    public static final int EOF=-1;
    public static final int TPS=60;
    public static final int LPAREN=19;
    public static final int TYPE=51;
    public static final int IE=70;
    public static final int ENTITY=40;
    public static final int LBRACKET=21;
    public static final int RPAREN=20;
    public static final int SLASH=32;
    public static final int STRING_LITERAL=18;
    public static final int FE=72;
    public static final int ARG=53;
    public static final int IP=71;
    public static final int COMMA=28;
    public static final int MP=75;
    public static final int BLOCK_COMMENT=15;
    public static final int BANGEQ=35;
    public static final int PLUS=9;
    public static final int MODEL=37;
    public static final int ExponentIndicator=6;
    public static final int SUPER=50;
    public static final int Sign=7;
    public static final int RBRACKET=22;
    public static final int EQ=33;
    public static final int DOT=29;
    public static final int IES=61;
    public static final int IdentifierPart=12;
    public static final int EQEQ=34;
    public static final int TE=68;
    public static final int INT_LITERAL=16;
    public static final int RBRACE=24;
    public static final int LINE_COMMENT=14;
    public static final int ITER=55;
    public static final int NULL=45;
    public static final int TP=69;
    public static final int IdentifierStart=11;
    public static final int INTER=58;
    public static final int STRUCT=56;
    public static final int INCOMPLETE=44;
    public static final int DEFS=49;
    public static final int FP=73;
    public static final int MINUS=10;
    public static final int Digit=5;
    public static final int LIST=57;
    public static final int FILE=47;
    public static final int UNSPECIFIED=43;
    public static final int SEMI=27;
    public static final int INF=42;
    public static final int TES=59;
    public static final int IKS=67;
    public static final int MES=65;
    public static final int COLON=30;
    public static final int MPS=66;
    public static final int IPS=62;
    public static final int WS=13;
    public static final int TEMPLATE=41;
    public static final int DECL=48;
    public static final int NonZeroDigit=4;
    public static final int COMPARTMENT=38;
    public static final int GT=25;
    public static final int FPS=64;
    public static final int CALL=54;
    public static final int PROCESS=39;

    // delegates
    // delegators


        public PBFParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public PBFParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return PBFParser.tokenNames; }
    public String getGrammarFileName() { return "D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g"; }



    enum Type {
      LIBRARY,
      MODEL,
      INCOMPLETE_MODEL,
      FULL_MODEL,
      MIXED_MODEL,
    }


    Type fileType;


    private String concat(List tokens) {
      StringBuilder buf = new StringBuilder();
      for (Object token : tokens) {
        buf.append(((CommonToken)token).getText());
      }
      return buf.toString();
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
      throw new ParsingException(linenum, posnum, message, e);      //NOTE: comment in debug
      //displayRecognitionError(this.getTokenNames(), e);
    }


    protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow)
        throws RecognitionException { 
      throw new MismatchedTokenException(ttype, input);
    }
      


    public static class literal_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "literal"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:109:1: literal : ( INT_LITERAL | DOUBLE_LITERAL | STRING_LITERAL );
    public final PBFParser.literal_return literal() throws RecognitionException {
        PBFParser.literal_return retval = new PBFParser.literal_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set1=null;

        CommonTree set1_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:112:3: ( INT_LITERAL | DOUBLE_LITERAL | STRING_LITERAL )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set1=(Token)input.LT(1);
            if ( (input.LA(1)>=INT_LITERAL && input.LA(1)<=STRING_LITERAL) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set1));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "literal"

    public static class keyword_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "keyword"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:118:1: keyword : ( LIBRARY | MODEL | PROCESS | ENTITY | TEMPLATE | INF | UNSPECIFIED | COMPARTMENT );
    public final PBFParser.keyword_return keyword() throws RecognitionException {
        PBFParser.keyword_return retval = new PBFParser.keyword_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set2=null;

        CommonTree set2_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:119:3: ( LIBRARY | MODEL | PROCESS | ENTITY | TEMPLATE | INF | UNSPECIFIED | COMPARTMENT )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set2=(Token)input.LT(1);
            if ( (input.LA(1)>=LIBRARY && input.LA(1)<=UNSPECIFIED) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set2));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "keyword"

    public static class structure_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "structure"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:131:1: structure : ( keyword )* ID ( block ( EQ value )? | eq= EQ value ) -> ^( STRUCT[$ID, \"STRUCT\"] ID ( keyword )* ( block )? ( ^( ID[$eq, \"value\"] value ) )? ) ;
    public final PBFParser.structure_return structure() throws RecognitionException {
        PBFParser.structure_return retval = new PBFParser.structure_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token eq=null;
        Token ID4=null;
        Token EQ6=null;
        PBFParser.keyword_return keyword3 = null;

        PBFParser.block_return block5 = null;

        PBFParser.value_return value7 = null;

        PBFParser.value_return value8 = null;


        CommonTree eq_tree=null;
        CommonTree ID4_tree=null;
        CommonTree EQ6_tree=null;
        RewriteRuleTokenStream stream_EQ=new RewriteRuleTokenStream(adaptor,"token EQ");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_keyword=new RewriteRuleSubtreeStream(adaptor,"rule keyword");
        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        RewriteRuleSubtreeStream stream_block=new RewriteRuleSubtreeStream(adaptor,"rule block");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:132:3: ( ( keyword )* ID ( block ( EQ value )? | eq= EQ value ) -> ^( STRUCT[$ID, \"STRUCT\"] ID ( keyword )* ( block )? ( ^( ID[$eq, \"value\"] value ) )? ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:132:5: ( keyword )* ID ( block ( EQ value )? | eq= EQ value )
            {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:132:5: ( keyword )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=LIBRARY && LA1_0<=UNSPECIFIED)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:132:5: keyword
            	    {
            	    pushFollow(FOLLOW_keyword_in_structure391);
            	    keyword3=keyword();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_keyword.add(keyword3.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            ID4=(Token)match(input,ID,FOLLOW_ID_in_structure394); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(ID4);

            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:133:5: ( block ( EQ value )? | eq= EQ value )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==LBRACE) ) {
                alt3=1;
            }
            else if ( (LA3_0==EQ) ) {
                alt3=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:133:7: block ( EQ value )?
                    {
                    pushFollow(FOLLOW_block_in_structure404);
                    block5=block();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_block.add(block5.getTree());
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:133:13: ( EQ value )?
                    int alt2=2;
                    int LA2_0 = input.LA(1);

                    if ( (LA2_0==EQ) ) {
                        alt2=1;
                    }
                    switch (alt2) {
                        case 1 :
                            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:133:14: EQ value
                            {
                            EQ6=(Token)match(input,EQ,FOLLOW_EQ_in_structure407); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_EQ.add(EQ6);

                            pushFollow(FOLLOW_value_in_structure409);
                            value7=value();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_value.add(value7.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:134:7: eq= EQ value
                    {
                    eq=(Token)match(input,EQ,FOLLOW_EQ_in_structure421); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_EQ.add(eq);

                    pushFollow(FOLLOW_value_in_structure423);
                    value8=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value.add(value8.getTree());

                    }
                    break;

            }



            // AST REWRITE
            // elements: block, ID, value, ID, keyword
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 135:53: -> ^( STRUCT[$ID, \"STRUCT\"] ID ( keyword )* ( block )? ( ^( ID[$eq, \"value\"] value ) )? )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:135:56: ^( STRUCT[$ID, \"STRUCT\"] ID ( keyword )* ( block )? ( ^( ID[$eq, \"value\"] value ) )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRUCT, ID4, "STRUCT"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:135:83: ( keyword )*
                while ( stream_keyword.hasNext() ) {
                    adaptor.addChild(root_1, stream_keyword.nextTree());

                }
                stream_keyword.reset();
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:135:92: ( block )?
                if ( stream_block.hasNext() ) {
                    adaptor.addChild(root_1, stream_block.nextTree());

                }
                stream_block.reset();
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:135:99: ( ^( ID[$eq, \"value\"] value ) )?
                if ( stream_ID.hasNext()||stream_value.hasNext() ) {
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:135:99: ^( ID[$eq, \"value\"] value )
                    {
                    CommonTree root_2 = (CommonTree)adaptor.nil();
                    root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, eq, "value"), root_2);

                    adaptor.addChild(root_2, stream_value.nextTree());

                    adaptor.addChild(root_1, root_2);
                    }

                }
                stream_ID.reset();
                stream_value.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "structure"

    public static class structureNull_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "structureNull"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:138:1: structureNull : ( keyword )* ID ( blockNull ( EQ valueNull )? | eq= EQ valueNull ) -> ^( STRUCT[$ID, \"STRUCT\"] ID ( keyword )* ( blockNull )? ( ^( ID[$eq, \"value\"] valueNull ) )? ) ;
    public final PBFParser.structureNull_return structureNull() throws RecognitionException {
        PBFParser.structureNull_return retval = new PBFParser.structureNull_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token eq=null;
        Token ID10=null;
        Token EQ12=null;
        PBFParser.keyword_return keyword9 = null;

        PBFParser.blockNull_return blockNull11 = null;

        PBFParser.valueNull_return valueNull13 = null;

        PBFParser.valueNull_return valueNull14 = null;


        CommonTree eq_tree=null;
        CommonTree ID10_tree=null;
        CommonTree EQ12_tree=null;
        RewriteRuleTokenStream stream_EQ=new RewriteRuleTokenStream(adaptor,"token EQ");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_valueNull=new RewriteRuleSubtreeStream(adaptor,"rule valueNull");
        RewriteRuleSubtreeStream stream_blockNull=new RewriteRuleSubtreeStream(adaptor,"rule blockNull");
        RewriteRuleSubtreeStream stream_keyword=new RewriteRuleSubtreeStream(adaptor,"rule keyword");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:139:3: ( ( keyword )* ID ( blockNull ( EQ valueNull )? | eq= EQ valueNull ) -> ^( STRUCT[$ID, \"STRUCT\"] ID ( keyword )* ( blockNull )? ( ^( ID[$eq, \"value\"] valueNull ) )? ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:139:5: ( keyword )* ID ( blockNull ( EQ valueNull )? | eq= EQ valueNull )
            {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:139:5: ( keyword )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>=LIBRARY && LA4_0<=UNSPECIFIED)) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:139:5: keyword
            	    {
            	    pushFollow(FOLLOW_keyword_in_structureNull512);
            	    keyword9=keyword();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_keyword.add(keyword9.getTree());

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            ID10=(Token)match(input,ID,FOLLOW_ID_in_structureNull515); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(ID10);

            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:140:5: ( blockNull ( EQ valueNull )? | eq= EQ valueNull )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==LBRACE) ) {
                alt6=1;
            }
            else if ( (LA6_0==EQ) ) {
                alt6=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:140:7: blockNull ( EQ valueNull )?
                    {
                    pushFollow(FOLLOW_blockNull_in_structureNull525);
                    blockNull11=blockNull();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_blockNull.add(blockNull11.getTree());
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:140:17: ( EQ valueNull )?
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0==EQ) ) {
                        alt5=1;
                    }
                    switch (alt5) {
                        case 1 :
                            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:140:18: EQ valueNull
                            {
                            EQ12=(Token)match(input,EQ,FOLLOW_EQ_in_structureNull528); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_EQ.add(EQ12);

                            pushFollow(FOLLOW_valueNull_in_structureNull530);
                            valueNull13=valueNull();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_valueNull.add(valueNull13.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:141:7: eq= EQ valueNull
                    {
                    eq=(Token)match(input,EQ,FOLLOW_EQ_in_structureNull542); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_EQ.add(eq);

                    pushFollow(FOLLOW_valueNull_in_structureNull544);
                    valueNull14=valueNull();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_valueNull.add(valueNull14.getTree());

                    }
                    break;

            }



            // AST REWRITE
            // elements: ID, valueNull, ID, blockNull, keyword
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 142:53: -> ^( STRUCT[$ID, \"STRUCT\"] ID ( keyword )* ( blockNull )? ( ^( ID[$eq, \"value\"] valueNull ) )? )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:142:56: ^( STRUCT[$ID, \"STRUCT\"] ID ( keyword )* ( blockNull )? ( ^( ID[$eq, \"value\"] valueNull ) )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRUCT, ID10, "STRUCT"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:142:83: ( keyword )*
                while ( stream_keyword.hasNext() ) {
                    adaptor.addChild(root_1, stream_keyword.nextTree());

                }
                stream_keyword.reset();
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:142:92: ( blockNull )?
                if ( stream_blockNull.hasNext() ) {
                    adaptor.addChild(root_1, stream_blockNull.nextTree());

                }
                stream_blockNull.reset();
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:142:103: ( ^( ID[$eq, \"value\"] valueNull ) )?
                if ( stream_valueNull.hasNext()||stream_ID.hasNext() ) {
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:142:103: ^( ID[$eq, \"value\"] valueNull )
                    {
                    CommonTree root_2 = (CommonTree)adaptor.nil();
                    root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, eq, "value"), root_2);

                    adaptor.addChild(root_2, stream_valueNull.nextTree());

                    adaptor.addChild(root_1, root_2);
                    }

                }
                stream_valueNull.reset();
                stream_ID.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "structureNull"

    public static class block_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "block"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:146:1: block : LBRACE propDefs RBRACE ;
    public final PBFParser.block_return block() throws RecognitionException {
        PBFParser.block_return retval = new PBFParser.block_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LBRACE15=null;
        Token RBRACE17=null;
        PBFParser.propDefs_return propDefs16 = null;


        CommonTree LBRACE15_tree=null;
        CommonTree RBRACE17_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:147:3: ( LBRACE propDefs RBRACE )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:147:5: LBRACE propDefs RBRACE
            {
            root_0 = (CommonTree)adaptor.nil();

            LBRACE15=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_block634); if (state.failed) return retval;
            pushFollow(FOLLOW_propDefs_in_block643);
            propDefs16=propDefs();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, propDefs16.getTree());
            RBRACE17=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_block651); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "block"

    public static class blockNull_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "blockNull"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:152:1: blockNull : LBRACE propDefsNull RBRACE ;
    public final PBFParser.blockNull_return blockNull() throws RecognitionException {
        PBFParser.blockNull_return retval = new PBFParser.blockNull_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LBRACE18=null;
        Token RBRACE20=null;
        PBFParser.propDefsNull_return propDefsNull19 = null;


        CommonTree LBRACE18_tree=null;
        CommonTree RBRACE20_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:153:3: ( LBRACE propDefsNull RBRACE )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:153:5: LBRACE propDefsNull RBRACE
            {
            root_0 = (CommonTree)adaptor.nil();

            LBRACE18=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_blockNull667); if (state.failed) return retval;
            pushFollow(FOLLOW_propDefsNull_in_blockNull676);
            propDefsNull19=propDefsNull();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, propDefsNull19.getTree());
            RBRACE20=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_blockNull682); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "blockNull"

    public static class propDefs_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "propDefs"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:158:1: propDefs : (pd+= propDef ( SEMI pd+= propDef )* ( SEMI )? )? -> ( $pd)* ;
    public final PBFParser.propDefs_return propDefs() throws RecognitionException {
        PBFParser.propDefs_return retval = new PBFParser.propDefs_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token SEMI21=null;
        Token SEMI22=null;
        List list_pd=null;
        RuleReturnScope pd = null;
        CommonTree SEMI21_tree=null;
        CommonTree SEMI22_tree=null;
        RewriteRuleTokenStream stream_SEMI=new RewriteRuleTokenStream(adaptor,"token SEMI");
        RewriteRuleSubtreeStream stream_propDef=new RewriteRuleSubtreeStream(adaptor,"rule propDef");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:159:3: ( (pd+= propDef ( SEMI pd+= propDef )* ( SEMI )? )? -> ( $pd)* )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:159:5: (pd+= propDef ( SEMI pd+= propDef )* ( SEMI )? )?
            {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:159:5: (pd+= propDef ( SEMI pd+= propDef )* ( SEMI )? )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==ID) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:159:6: pd+= propDef ( SEMI pd+= propDef )* ( SEMI )?
                    {
                    pushFollow(FOLLOW_propDef_in_propDefs699);
                    pd=propDef();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_propDef.add(pd.getTree());
                    if (list_pd==null) list_pd=new ArrayList();
                    list_pd.add(pd.getTree());

                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:159:18: ( SEMI pd+= propDef )*
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( (LA7_0==SEMI) ) {
                            int LA7_1 = input.LA(2);

                            if ( (LA7_1==ID) ) {
                                alt7=1;
                            }


                        }


                        switch (alt7) {
                    	case 1 :
                    	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:159:19: SEMI pd+= propDef
                    	    {
                    	    SEMI21=(Token)match(input,SEMI,FOLLOW_SEMI_in_propDefs702); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_SEMI.add(SEMI21);

                    	    pushFollow(FOLLOW_propDef_in_propDefs706);
                    	    pd=propDef();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_propDef.add(pd.getTree());
                    	    if (list_pd==null) list_pd=new ArrayList();
                    	    list_pd.add(pd.getTree());


                    	    }
                    	    break;

                    	default :
                    	    break loop7;
                        }
                    } while (true);

                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:159:38: ( SEMI )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==SEMI) ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:159:38: SEMI
                            {
                            SEMI22=(Token)match(input,SEMI,FOLLOW_SEMI_in_propDefs710); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_SEMI.add(SEMI22);


                            }
                            break;

                    }


                    }
                    break;

            }



            // AST REWRITE
            // elements: pd
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: pd
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_pd=new RewriteRuleSubtreeStream(adaptor,"token pd",list_pd);
            root_0 = (CommonTree)adaptor.nil();
            // 159:65: -> ( $pd)*
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:159:68: ( $pd)*
                while ( stream_pd.hasNext() ) {
                    adaptor.addChild(root_0, stream_pd.nextTree());

                }
                stream_pd.reset();

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "propDefs"

    public static class propDefsNull_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "propDefsNull"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:162:1: propDefsNull : (pd+= propDefNull ( SEMI pd+= propDefNull )* ( SEMI )? )? -> ( $pd)* ;
    public final PBFParser.propDefsNull_return propDefsNull() throws RecognitionException {
        PBFParser.propDefsNull_return retval = new PBFParser.propDefsNull_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token SEMI23=null;
        Token SEMI24=null;
        List list_pd=null;
        RuleReturnScope pd = null;
        CommonTree SEMI23_tree=null;
        CommonTree SEMI24_tree=null;
        RewriteRuleTokenStream stream_SEMI=new RewriteRuleTokenStream(adaptor,"token SEMI");
        RewriteRuleSubtreeStream stream_propDefNull=new RewriteRuleSubtreeStream(adaptor,"rule propDefNull");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:163:3: ( (pd+= propDefNull ( SEMI pd+= propDefNull )* ( SEMI )? )? -> ( $pd)* )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:163:5: (pd+= propDefNull ( SEMI pd+= propDefNull )* ( SEMI )? )?
            {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:163:5: (pd+= propDefNull ( SEMI pd+= propDefNull )* ( SEMI )? )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==ID) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:163:6: pd+= propDefNull ( SEMI pd+= propDefNull )* ( SEMI )?
                    {
                    pushFollow(FOLLOW_propDefNull_in_propDefsNull754);
                    pd=propDefNull();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_propDefNull.add(pd.getTree());
                    if (list_pd==null) list_pd=new ArrayList();
                    list_pd.add(pd.getTree());

                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:163:22: ( SEMI pd+= propDefNull )*
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( (LA10_0==SEMI) ) {
                            int LA10_1 = input.LA(2);

                            if ( (LA10_1==ID) ) {
                                alt10=1;
                            }


                        }


                        switch (alt10) {
                    	case 1 :
                    	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:163:23: SEMI pd+= propDefNull
                    	    {
                    	    SEMI23=(Token)match(input,SEMI,FOLLOW_SEMI_in_propDefsNull757); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_SEMI.add(SEMI23);

                    	    pushFollow(FOLLOW_propDefNull_in_propDefsNull761);
                    	    pd=propDefNull();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_propDefNull.add(pd.getTree());
                    	    if (list_pd==null) list_pd=new ArrayList();
                    	    list_pd.add(pd.getTree());


                    	    }
                    	    break;

                    	default :
                    	    break loop10;
                        }
                    } while (true);

                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:163:46: ( SEMI )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==SEMI) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:163:46: SEMI
                            {
                            SEMI24=(Token)match(input,SEMI,FOLLOW_SEMI_in_propDefsNull765); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_SEMI.add(SEMI24);


                            }
                            break;

                    }


                    }
                    break;

            }



            // AST REWRITE
            // elements: pd
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: pd
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_pd=new RewriteRuleSubtreeStream(adaptor,"token pd",list_pd);
            root_0 = (CommonTree)adaptor.nil();
            // 163:67: -> ( $pd)*
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:163:70: ( $pd)*
                while ( stream_pd.hasNext() ) {
                    adaptor.addChild(root_0, stream_pd.nextTree());

                }
                stream_pd.reset();

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "propDefsNull"

    public static class propDef_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "propDef"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:166:1: propDef : ID COLON valueList ;
    public final PBFParser.propDef_return propDef() throws RecognitionException {
        PBFParser.propDef_return retval = new PBFParser.propDef_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token ID25=null;
        Token COLON26=null;
        PBFParser.valueList_return valueList27 = null;


        CommonTree ID25_tree=null;
        CommonTree COLON26_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:167:3: ( ID COLON valueList )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:167:5: ID COLON valueList
            {
            root_0 = (CommonTree)adaptor.nil();

            ID25=(Token)match(input,ID,FOLLOW_ID_in_propDef800); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ID25_tree = (CommonTree)adaptor.create(ID25);
            root_0 = (CommonTree)adaptor.becomeRoot(ID25_tree, root_0);
            }
            COLON26=(Token)match(input,COLON,FOLLOW_COLON_in_propDef803); if (state.failed) return retval;
            pushFollow(FOLLOW_valueList_in_propDef806);
            valueList27=valueList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, valueList27.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "propDef"

    public static class propDefNull_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "propDefNull"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:170:1: propDefNull : ID COLON valueListNull ;
    public final PBFParser.propDefNull_return propDefNull() throws RecognitionException {
        PBFParser.propDefNull_return retval = new PBFParser.propDefNull_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token ID28=null;
        Token COLON29=null;
        PBFParser.valueListNull_return valueListNull30 = null;


        CommonTree ID28_tree=null;
        CommonTree COLON29_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:171:3: ( ID COLON valueListNull )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:171:5: ID COLON valueListNull
            {
            root_0 = (CommonTree)adaptor.nil();

            ID28=(Token)match(input,ID,FOLLOW_ID_in_propDefNull819); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ID28_tree = (CommonTree)adaptor.create(ID28);
            root_0 = (CommonTree)adaptor.becomeRoot(ID28_tree, root_0);
            }
            COLON29=(Token)match(input,COLON,FOLLOW_COLON_in_propDefNull822); if (state.failed) return retval;
            pushFollow(FOLLOW_valueListNull_in_propDefNull825);
            valueListNull30=valueListNull();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, valueListNull30.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "propDefNull"

    public static class valueList_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "valueList"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:175:1: valueList : value ( COMMA value )* ;
    public final PBFParser.valueList_return valueList() throws RecognitionException {
        PBFParser.valueList_return retval = new PBFParser.valueList_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token COMMA32=null;
        PBFParser.value_return value31 = null;

        PBFParser.value_return value33 = null;


        CommonTree COMMA32_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:176:3: ( value ( COMMA value )* )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:176:5: value ( COMMA value )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_value_in_valueList839);
            value31=value();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, value31.getTree());
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:176:11: ( COMMA value )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==COMMA) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:176:12: COMMA value
            	    {
            	    COMMA32=(Token)match(input,COMMA,FOLLOW_COMMA_in_valueList842); if (state.failed) return retval;
            	    pushFollow(FOLLOW_value_in_valueList845);
            	    value33=value();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, value33.getTree());

            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "valueList"

    public static class valueListNull_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "valueListNull"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:179:1: valueListNull : valueNull ( COMMA valueNull )* ;
    public final PBFParser.valueListNull_return valueListNull() throws RecognitionException {
        PBFParser.valueListNull_return retval = new PBFParser.valueListNull_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token COMMA35=null;
        PBFParser.valueNull_return valueNull34 = null;

        PBFParser.valueNull_return valueNull36 = null;


        CommonTree COMMA35_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:180:3: ( valueNull ( COMMA valueNull )* )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:180:5: valueNull ( COMMA valueNull )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_valueNull_in_valueListNull860);
            valueNull34=valueNull();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, valueNull34.getTree());
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:180:15: ( COMMA valueNull )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==COMMA) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:180:16: COMMA valueNull
            	    {
            	    COMMA35=(Token)match(input,COMMA,FOLLOW_COMMA_in_valueListNull863); if (state.failed) return retval;
            	    pushFollow(FOLLOW_valueNull_in_valueListNull866);
            	    valueNull36=valueNull();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, valueNull36.getTree());

            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "valueListNull"

    public static class value_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "value"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:183:1: value : ( ( INT_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> INT_LITERAL | ( DOUBLE_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> DOUBLE_LITERAL | ( STRING_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> STRING_LITERAL | interval | ( ID ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> ID | expression | structure | list );
    public final PBFParser.value_return value() throws RecognitionException {
        PBFParser.value_return retval = new PBFParser.value_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token INT_LITERAL37=null;
        Token DOUBLE_LITERAL38=null;
        Token STRING_LITERAL39=null;
        Token ID41=null;
        PBFParser.interval_return interval40 = null;

        PBFParser.expression_return expression42 = null;

        PBFParser.structure_return structure43 = null;

        PBFParser.list_return list44 = null;


        CommonTree INT_LITERAL37_tree=null;
        CommonTree DOUBLE_LITERAL38_tree=null;
        CommonTree STRING_LITERAL39_tree=null;
        CommonTree ID41_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:184:3: ( ( INT_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> INT_LITERAL | ( DOUBLE_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> DOUBLE_LITERAL | ( STRING_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> STRING_LITERAL | interval | ( ID ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> ID | expression | structure | list )
            int alt15=8;
            alt15 = dfa15.predict(input);
            switch (alt15) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:184:5: ( INT_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> INT_LITERAL
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    INT_LITERAL37=(Token)match(input,INT_LITERAL,FOLLOW_INT_LITERAL_in_value913); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    INT_LITERAL37_tree = (CommonTree)adaptor.create(INT_LITERAL37);
                    adaptor.addChild(root_0, INT_LITERAL37_tree);
                    }

                    }
                    break;
                case 2 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:185:5: ( DOUBLE_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> DOUBLE_LITERAL
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    DOUBLE_LITERAL38=(Token)match(input,DOUBLE_LITERAL,FOLLOW_DOUBLE_LITERAL_in_value948); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    DOUBLE_LITERAL38_tree = (CommonTree)adaptor.create(DOUBLE_LITERAL38);
                    adaptor.addChild(root_0, DOUBLE_LITERAL38_tree);
                    }

                    }
                    break;
                case 3 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:186:5: ( STRING_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> STRING_LITERAL
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    STRING_LITERAL39=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_value983); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRING_LITERAL39_tree = (CommonTree)adaptor.create(STRING_LITERAL39);
                    adaptor.addChild(root_0, STRING_LITERAL39_tree);
                    }

                    }
                    break;
                case 4 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:187:5: interval
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_interval_in_value989);
                    interval40=interval();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, interval40.getTree());

                    }
                    break;
                case 5 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:188:5: ( ID ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> ID
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    ID41=(Token)match(input,ID,FOLLOW_ID_in_value1032); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    ID41_tree = (CommonTree)adaptor.create(ID41);
                    adaptor.addChild(root_0, ID41_tree);
                    }

                    }
                    break;
                case 6 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:189:5: expression
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_expression_in_value1038);
                    expression42=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression42.getTree());

                    }
                    break;
                case 7 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:190:5: structure
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_structure_in_value1044);
                    structure43=structure();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, structure43.getTree());

                    }
                    break;
                case 8 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:191:5: list
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_list_in_value1050);
                    list44=list();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, list44.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "value"

    public static class valueNull_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "valueNull"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:194:1: valueNull : ( ( NULL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> NULL | ( INT_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> INT_LITERAL | ( DOUBLE_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> DOUBLE_LITERAL | ( STRING_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> STRING_LITERAL | interval | ( ID ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> ID | expression | structureNull | list );
    public final PBFParser.valueNull_return valueNull() throws RecognitionException {
        PBFParser.valueNull_return retval = new PBFParser.valueNull_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token NULL45=null;
        Token INT_LITERAL46=null;
        Token DOUBLE_LITERAL47=null;
        Token STRING_LITERAL48=null;
        Token ID50=null;
        PBFParser.interval_return interval49 = null;

        PBFParser.expression_return expression51 = null;

        PBFParser.structureNull_return structureNull52 = null;

        PBFParser.list_return list53 = null;


        CommonTree NULL45_tree=null;
        CommonTree INT_LITERAL46_tree=null;
        CommonTree DOUBLE_LITERAL47_tree=null;
        CommonTree STRING_LITERAL48_tree=null;
        CommonTree ID50_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:195:3: ( ( NULL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> NULL | ( INT_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> INT_LITERAL | ( DOUBLE_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> DOUBLE_LITERAL | ( STRING_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> STRING_LITERAL | interval | ( ID ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> ID | expression | structureNull | list )
            int alt16=9;
            alt16 = dfa16.predict(input);
            switch (alt16) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:195:5: ( NULL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> NULL
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    NULL45=(Token)match(input,NULL,FOLLOW_NULL_in_valueNull1102); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NULL45_tree = (CommonTree)adaptor.create(NULL45);
                    adaptor.addChild(root_0, NULL45_tree);
                    }

                    }
                    break;
                case 2 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:196:5: ( INT_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> INT_LITERAL
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    INT_LITERAL46=(Token)match(input,INT_LITERAL,FOLLOW_INT_LITERAL_in_valueNull1140); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    INT_LITERAL46_tree = (CommonTree)adaptor.create(INT_LITERAL46);
                    adaptor.addChild(root_0, INT_LITERAL46_tree);
                    }

                    }
                    break;
                case 3 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:197:5: ( DOUBLE_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> DOUBLE_LITERAL
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    DOUBLE_LITERAL47=(Token)match(input,DOUBLE_LITERAL,FOLLOW_DOUBLE_LITERAL_in_valueNull1175); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    DOUBLE_LITERAL47_tree = (CommonTree)adaptor.create(DOUBLE_LITERAL47);
                    adaptor.addChild(root_0, DOUBLE_LITERAL47_tree);
                    }

                    }
                    break;
                case 4 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:198:5: ( STRING_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> STRING_LITERAL
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    STRING_LITERAL48=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_valueNull1210); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRING_LITERAL48_tree = (CommonTree)adaptor.create(STRING_LITERAL48);
                    adaptor.addChild(root_0, STRING_LITERAL48_tree);
                    }

                    }
                    break;
                case 5 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:199:5: interval
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_interval_in_valueNull1216);
                    interval49=interval();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, interval49.getTree());

                    }
                    break;
                case 6 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:200:5: ( ID ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> ID
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    ID50=(Token)match(input,ID,FOLLOW_ID_in_valueNull1259); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    ID50_tree = (CommonTree)adaptor.create(ID50);
                    adaptor.addChild(root_0, ID50_tree);
                    }

                    }
                    break;
                case 7 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:201:5: expression
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_expression_in_valueNull1265);
                    expression51=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression51.getTree());

                    }
                    break;
                case 8 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:202:5: structureNull
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_structureNull_in_valueNull1271);
                    structureNull52=structureNull();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, structureNull52.getTree());

                    }
                    break;
                case 9 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:203:5: list
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_list_in_valueNull1277);
                    list53=list();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, list53.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "valueNull"

    public static class list_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "list"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:207:1: list : LBRACKET value ( COMMA value )* RBRACKET -> ( value )* ;
    public final PBFParser.list_return list() throws RecognitionException {
        PBFParser.list_return retval = new PBFParser.list_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LBRACKET54=null;
        Token COMMA56=null;
        Token RBRACKET58=null;
        PBFParser.value_return value55 = null;

        PBFParser.value_return value57 = null;


        CommonTree LBRACKET54_tree=null;
        CommonTree COMMA56_tree=null;
        CommonTree RBRACKET58_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:208:3: ( LBRACKET value ( COMMA value )* RBRACKET -> ( value )* )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:208:5: LBRACKET value ( COMMA value )* RBRACKET
            {
            LBRACKET54=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_list1293); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET54);

            pushFollow(FOLLOW_value_in_list1301);
            value55=value();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_value.add(value55.getTree());
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:209:13: ( COMMA value )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==COMMA) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:209:14: COMMA value
            	    {
            	    COMMA56=(Token)match(input,COMMA,FOLLOW_COMMA_in_list1304); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA56);

            	    pushFollow(FOLLOW_value_in_list1306);
            	    value57=value();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_value.add(value57.getTree());

            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);

            RBRACKET58=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_list1314); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET58);



            // AST REWRITE
            // elements: value
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 210:55: -> ( value )*
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:210:58: ( value )*
                while ( stream_value.hasNext() ) {
                    adaptor.addChild(root_0, stream_value.nextTree());

                }
                stream_value.reset();

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "list"

    public static class interval_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "interval"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:214:1: interval : lt= LT lb= signedNumberOrInfinity COMMA ub= signedNumberOrInfinity GT -> ^( INTER[$lt, \"INTER\"] $lb $ub) ;
    public final PBFParser.interval_return interval() throws RecognitionException {
        PBFParser.interval_return retval = new PBFParser.interval_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token lt=null;
        Token COMMA59=null;
        Token GT60=null;
        PBFParser.signedNumberOrInfinity_return lb = null;

        PBFParser.signedNumberOrInfinity_return ub = null;


        CommonTree lt_tree=null;
        CommonTree COMMA59_tree=null;
        CommonTree GT60_tree=null;
        RewriteRuleTokenStream stream_GT=new RewriteRuleTokenStream(adaptor,"token GT");
        RewriteRuleTokenStream stream_LT=new RewriteRuleTokenStream(adaptor,"token LT");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_signedNumberOrInfinity=new RewriteRuleSubtreeStream(adaptor,"rule signedNumberOrInfinity");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:215:3: (lt= LT lb= signedNumberOrInfinity COMMA ub= signedNumberOrInfinity GT -> ^( INTER[$lt, \"INTER\"] $lb $ub) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:215:5: lt= LT lb= signedNumberOrInfinity COMMA ub= signedNumberOrInfinity GT
            {
            lt=(Token)match(input,LT,FOLLOW_LT_in_interval1376); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LT.add(lt);

            pushFollow(FOLLOW_signedNumberOrInfinity_in_interval1380);
            lb=signedNumberOrInfinity();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_signedNumberOrInfinity.add(lb.getTree());
            COMMA59=(Token)match(input,COMMA,FOLLOW_COMMA_in_interval1382); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COMMA.add(COMMA59);

            pushFollow(FOLLOW_signedNumberOrInfinity_in_interval1386);
            ub=signedNumberOrInfinity();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_signedNumberOrInfinity.add(ub.getTree());
            GT60=(Token)match(input,GT,FOLLOW_GT_in_interval1388); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_GT.add(GT60);



            // AST REWRITE
            // elements: ub, lb
            // token labels: 
            // rule labels: retval, ub, lb
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_ub=new RewriteRuleSubtreeStream(adaptor,"rule ub",ub!=null?ub.tree:null);
            RewriteRuleSubtreeStream stream_lb=new RewriteRuleSubtreeStream(adaptor,"rule lb",lb!=null?lb.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 215:75: -> ^( INTER[$lt, \"INTER\"] $lb $ub)
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:215:78: ^( INTER[$lt, \"INTER\"] $lb $ub)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(INTER, lt, "INTER"), root_1);

                adaptor.addChild(root_1, stream_lb.nextTree());
                adaptor.addChild(root_1, stream_ub.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "interval"

    public static class signedNumberOrInfinity_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "signedNumberOrInfinity"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:221:1: signedNumberOrInfinity : ( signedNumber | signedInfinity );
    public final PBFParser.signedNumberOrInfinity_return signedNumberOrInfinity() throws RecognitionException {
        PBFParser.signedNumberOrInfinity_return retval = new PBFParser.signedNumberOrInfinity_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        PBFParser.signedNumber_return signedNumber61 = null;

        PBFParser.signedInfinity_return signedInfinity62 = null;



        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:222:3: ( signedNumber | signedInfinity )
            int alt18=2;
            switch ( input.LA(1) ) {
            case PLUS:
            case MINUS:
                {
                int LA18_1 = input.LA(2);

                if ( (LA18_1==INF) ) {
                    alt18=2;
                }
                else if ( ((LA18_1>=INT_LITERAL && LA18_1<=DOUBLE_LITERAL)) ) {
                    alt18=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 18, 1, input);

                    throw nvae;
                }
                }
                break;
            case INT_LITERAL:
            case DOUBLE_LITERAL:
                {
                alt18=1;
                }
                break;
            case INF:
                {
                alt18=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;
            }

            switch (alt18) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:222:5: signedNumber
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_signedNumber_in_signedNumberOrInfinity1421);
                    signedNumber61=signedNumber();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, signedNumber61.getTree());

                    }
                    break;
                case 2 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:223:5: signedInfinity
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_signedInfinity_in_signedNumberOrInfinity1427);
                    signedInfinity62=signedInfinity();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, signedInfinity62.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "signedNumberOrInfinity"

    public static class signedNumber_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "signedNumber"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:226:1: signedNumber : ( signedInteger | signedDouble );
    public final PBFParser.signedNumber_return signedNumber() throws RecognitionException {
        PBFParser.signedNumber_return retval = new PBFParser.signedNumber_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        PBFParser.signedInteger_return signedInteger63 = null;

        PBFParser.signedDouble_return signedDouble64 = null;



        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:227:3: ( signedInteger | signedDouble )
            int alt19=2;
            switch ( input.LA(1) ) {
            case PLUS:
            case MINUS:
                {
                int LA19_1 = input.LA(2);

                if ( (LA19_1==DOUBLE_LITERAL) ) {
                    alt19=2;
                }
                else if ( (LA19_1==INT_LITERAL) ) {
                    alt19=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 19, 1, input);

                    throw nvae;
                }
                }
                break;
            case INT_LITERAL:
                {
                alt19=1;
                }
                break;
            case DOUBLE_LITERAL:
                {
                alt19=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;
            }

            switch (alt19) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:227:5: signedInteger
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_signedInteger_in_signedNumber1440);
                    signedInteger63=signedInteger();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, signedInteger63.getTree());

                    }
                    break;
                case 2 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:228:5: signedDouble
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_signedDouble_in_signedNumber1446);
                    signedDouble64=signedDouble();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, signedDouble64.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "signedNumber"

    public static class signedInteger_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "signedInteger"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:231:1: signedInteger : ( sign )? INT_LITERAL ;
    public final PBFParser.signedInteger_return signedInteger() throws RecognitionException {
        PBFParser.signedInteger_return retval = new PBFParser.signedInteger_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token INT_LITERAL66=null;
        PBFParser.sign_return sign65 = null;


        CommonTree INT_LITERAL66_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:232:3: ( ( sign )? INT_LITERAL )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:232:5: ( sign )? INT_LITERAL
            {
            root_0 = (CommonTree)adaptor.nil();

            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:232:9: ( sign )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( ((LA20_0>=PLUS && LA20_0<=MINUS)) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:232:9: sign
                    {
                    pushFollow(FOLLOW_sign_in_signedInteger1459);
                    sign65=sign();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(sign65.getTree(), root_0);

                    }
                    break;

            }

            INT_LITERAL66=(Token)match(input,INT_LITERAL,FOLLOW_INT_LITERAL_in_signedInteger1463); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            INT_LITERAL66_tree = (CommonTree)adaptor.create(INT_LITERAL66);
            adaptor.addChild(root_0, INT_LITERAL66_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "signedInteger"

    public static class signedDouble_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "signedDouble"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:235:1: signedDouble : ( sign )? DOUBLE_LITERAL ;
    public final PBFParser.signedDouble_return signedDouble() throws RecognitionException {
        PBFParser.signedDouble_return retval = new PBFParser.signedDouble_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token DOUBLE_LITERAL68=null;
        PBFParser.sign_return sign67 = null;


        CommonTree DOUBLE_LITERAL68_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:236:3: ( ( sign )? DOUBLE_LITERAL )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:236:5: ( sign )? DOUBLE_LITERAL
            {
            root_0 = (CommonTree)adaptor.nil();

            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:236:9: ( sign )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( ((LA21_0>=PLUS && LA21_0<=MINUS)) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:236:9: sign
                    {
                    pushFollow(FOLLOW_sign_in_signedDouble1476);
                    sign67=sign();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(sign67.getTree(), root_0);

                    }
                    break;

            }

            DOUBLE_LITERAL68=(Token)match(input,DOUBLE_LITERAL,FOLLOW_DOUBLE_LITERAL_in_signedDouble1480); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            DOUBLE_LITERAL68_tree = (CommonTree)adaptor.create(DOUBLE_LITERAL68);
            adaptor.addChild(root_0, DOUBLE_LITERAL68_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "signedDouble"

    public static class signedInfinity_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "signedInfinity"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:239:1: signedInfinity : ( sign )? INF ;
    public final PBFParser.signedInfinity_return signedInfinity() throws RecognitionException {
        PBFParser.signedInfinity_return retval = new PBFParser.signedInfinity_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token INF70=null;
        PBFParser.sign_return sign69 = null;


        CommonTree INF70_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:240:3: ( ( sign )? INF )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:240:5: ( sign )? INF
            {
            root_0 = (CommonTree)adaptor.nil();

            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:240:9: ( sign )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( ((LA22_0>=PLUS && LA22_0<=MINUS)) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:240:9: sign
                    {
                    pushFollow(FOLLOW_sign_in_signedInfinity1493);
                    sign69=sign();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(sign69.getTree(), root_0);

                    }
                    break;

            }

            INF70=(Token)match(input,INF,FOLLOW_INF_in_signedInfinity1497); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            INF70_tree = (CommonTree)adaptor.create(INF70);
            adaptor.addChild(root_0, INF70_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "signedInfinity"

    public static class integerOrInfinity_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "integerOrInfinity"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:244:1: integerOrInfinity : ( INT_LITERAL | INF );
    public final PBFParser.integerOrInfinity_return integerOrInfinity() throws RecognitionException {
        PBFParser.integerOrInfinity_return retval = new PBFParser.integerOrInfinity_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set71=null;

        CommonTree set71_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:245:3: ( INT_LITERAL | INF )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set71=(Token)input.LT(1);
            if ( input.LA(1)==INT_LITERAL||input.LA(1)==INF ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set71));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "integerOrInfinity"

    public static class parameters_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "parameters"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:252:1: parameters : LPAREN ( parameterList )? RPAREN ;
    public final PBFParser.parameters_return parameters() throws RecognitionException {
        PBFParser.parameters_return retval = new PBFParser.parameters_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LPAREN72=null;
        Token RPAREN74=null;
        PBFParser.parameterList_return parameterList73 = null;


        CommonTree LPAREN72_tree=null;
        CommonTree RPAREN74_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:253:3: ( LPAREN ( parameterList )? RPAREN )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:253:5: LPAREN ( parameterList )? RPAREN
            {
            root_0 = (CommonTree)adaptor.nil();

            LPAREN72=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_parameters1534); if (state.failed) return retval;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:253:13: ( parameterList )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==ID) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:253:13: parameterList
                    {
                    pushFollow(FOLLOW_parameterList_in_parameters1537);
                    parameterList73=parameterList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, parameterList73.getTree());

                    }
                    break;

            }

            RPAREN74=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_parameters1540); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "parameters"

    public static class parameterList_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "parameterList"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:256:1: parameterList : parameter ( COMMA parameter )* ;
    public final PBFParser.parameterList_return parameterList() throws RecognitionException {
        PBFParser.parameterList_return retval = new PBFParser.parameterList_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token COMMA76=null;
        PBFParser.parameter_return parameter75 = null;

        PBFParser.parameter_return parameter77 = null;


        CommonTree COMMA76_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:257:3: ( parameter ( COMMA parameter )* )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:257:5: parameter ( COMMA parameter )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_parameter_in_parameterList1554);
            parameter75=parameter();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, parameter75.getTree());
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:257:15: ( COMMA parameter )*
            loop24:
            do {
                int alt24=2;
                int LA24_0 = input.LA(1);

                if ( (LA24_0==COMMA) ) {
                    alt24=1;
                }


                switch (alt24) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:257:16: COMMA parameter
            	    {
            	    COMMA76=(Token)match(input,COMMA,FOLLOW_COMMA_in_parameterList1557); if (state.failed) return retval;
            	    pushFollow(FOLLOW_parameter_in_parameterList1560);
            	    parameter77=parameter();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, parameter77.getTree());

            	    }
            	    break;

            	default :
            	    break loop24;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "parameterList"

    public static class parameter_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "parameter"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:259:1: parameter : instID= ID c= COLON tempID= ID (cc= card )? -> ^( PARAM[$instID, \"PARAM\"] $instID ^( ID[$c, \"template\"] $tempID) ^( ID[$tempID, \"card\"] ( card )? ) ) ;
    public final PBFParser.parameter_return parameter() throws RecognitionException {
        PBFParser.parameter_return retval = new PBFParser.parameter_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token instID=null;
        Token c=null;
        Token tempID=null;
        PBFParser.card_return cc = null;


        CommonTree instID_tree=null;
        CommonTree c_tree=null;
        CommonTree tempID_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_card=new RewriteRuleSubtreeStream(adaptor,"rule card");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:260:3: (instID= ID c= COLON tempID= ID (cc= card )? -> ^( PARAM[$instID, \"PARAM\"] $instID ^( ID[$c, \"template\"] $tempID) ^( ID[$tempID, \"card\"] ( card )? ) ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:260:5: instID= ID c= COLON tempID= ID (cc= card )?
            {
            instID=(Token)match(input,ID,FOLLOW_ID_in_parameter1576); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(instID);

            c=(Token)match(input,COLON,FOLLOW_COLON_in_parameter1580); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COLON.add(c);

            tempID=(Token)match(input,ID,FOLLOW_ID_in_parameter1584); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(tempID);

            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:260:35: (cc= card )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==LT) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:260:35: cc= card
                    {
                    pushFollow(FOLLOW_card_in_parameter1588);
                    cc=card();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_card.add(cc.getTree());

                    }
                    break;

            }



            // AST REWRITE
            // elements: card, tempID, ID, instID, ID
            // token labels: instID, tempID
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_instID=new RewriteRuleTokenStream(adaptor,"token instID",instID);
            RewriteRuleTokenStream stream_tempID=new RewriteRuleTokenStream(adaptor,"token tempID",tempID);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 260:67: -> ^( PARAM[$instID, \"PARAM\"] $instID ^( ID[$c, \"template\"] $tempID) ^( ID[$tempID, \"card\"] ( card )? ) )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:260:70: ^( PARAM[$instID, \"PARAM\"] $instID ^( ID[$c, \"template\"] $tempID) ^( ID[$tempID, \"card\"] ( card )? ) )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(PARAM, instID, "PARAM"), root_1);

                adaptor.addChild(root_1, stream_instID.nextNode());
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:260:104: ^( ID[$c, \"template\"] $tempID)
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, c, "template"), root_2);

                adaptor.addChild(root_2, stream_tempID.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:260:134: ^( ID[$tempID, \"card\"] ( card )? )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, tempID, "card"), root_2);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:260:156: ( card )?
                if ( stream_card.hasNext() ) {
                    adaptor.addChild(root_2, stream_card.nextTree());

                }
                stream_card.reset();

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "parameter"

    public static class card_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "card"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:263:1: card : lt= LT lb= integerOrInfinity ( COMMA ub= integerOrInfinity )? GT -> ^( INTER[$lt, \"INTER\"] $lb ( $ub)? ) ;
    public final PBFParser.card_return card() throws RecognitionException {
        PBFParser.card_return retval = new PBFParser.card_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token lt=null;
        Token COMMA78=null;
        Token GT79=null;
        PBFParser.integerOrInfinity_return lb = null;

        PBFParser.integerOrInfinity_return ub = null;


        CommonTree lt_tree=null;
        CommonTree COMMA78_tree=null;
        CommonTree GT79_tree=null;
        RewriteRuleTokenStream stream_GT=new RewriteRuleTokenStream(adaptor,"token GT");
        RewriteRuleTokenStream stream_LT=new RewriteRuleTokenStream(adaptor,"token LT");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_integerOrInfinity=new RewriteRuleSubtreeStream(adaptor,"rule integerOrInfinity");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:264:3: (lt= LT lb= integerOrInfinity ( COMMA ub= integerOrInfinity )? GT -> ^( INTER[$lt, \"INTER\"] $lb ( $ub)? ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:264:5: lt= LT lb= integerOrInfinity ( COMMA ub= integerOrInfinity )? GT
            {
            lt=(Token)match(input,LT,FOLLOW_LT_in_card1655); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LT.add(lt);

            pushFollow(FOLLOW_integerOrInfinity_in_card1659);
            lb=integerOrInfinity();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_integerOrInfinity.add(lb.getTree());
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:264:32: ( COMMA ub= integerOrInfinity )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==COMMA) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:264:33: COMMA ub= integerOrInfinity
                    {
                    COMMA78=(Token)match(input,COMMA,FOLLOW_COMMA_in_card1662); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_COMMA.add(COMMA78);

                    pushFollow(FOLLOW_integerOrInfinity_in_card1666);
                    ub=integerOrInfinity();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_integerOrInfinity.add(ub.getTree());

                    }
                    break;

            }

            GT79=(Token)match(input,GT,FOLLOW_GT_in_card1670); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_GT.add(GT79);



            // AST REWRITE
            // elements: ub, lb
            // token labels: 
            // rule labels: retval, ub, lb
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_ub=new RewriteRuleSubtreeStream(adaptor,"rule ub",ub!=null?ub.tree:null);
            RewriteRuleSubtreeStream stream_lb=new RewriteRuleSubtreeStream(adaptor,"rule lb",lb!=null?lb.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 264:73: -> ^( INTER[$lt, \"INTER\"] $lb ( $ub)? )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:264:76: ^( INTER[$lt, \"INTER\"] $lb ( $ub)? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(INTER, lt, "INTER"), root_1);

                adaptor.addChild(root_1, stream_lb.nextTree());
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:264:102: ( $ub)?
                if ( stream_ub.hasNext() ) {
                    adaptor.addChild(root_1, stream_ub.nextTree());

                }
                stream_ub.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "card"

    public static class superEntity_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "superEntity"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:270:1: superEntity : COLON ID ;
    public final PBFParser.superEntity_return superEntity() throws RecognitionException {
        PBFParser.superEntity_return retval = new PBFParser.superEntity_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token COLON80=null;
        Token ID81=null;

        CommonTree COLON80_tree=null;
        CommonTree ID81_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:271:3: ( COLON ID )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:271:5: COLON ID
            {
            root_0 = (CommonTree)adaptor.nil();

            COLON80=(Token)match(input,COLON,FOLLOW_COLON_in_superEntity1709); if (state.failed) return retval;
            ID81=(Token)match(input,ID,FOLLOW_ID_in_superEntity1712); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ID81_tree = (CommonTree)adaptor.create(ID81);
            adaptor.addChild(root_0, ID81_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "superEntity"

    public static class entityType_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "entityType"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:274:1: entityType : COLON ID ;
    public final PBFParser.entityType_return entityType() throws RecognitionException {
        PBFParser.entityType_return retval = new PBFParser.entityType_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token COLON82=null;
        Token ID83=null;

        CommonTree COLON82_tree=null;
        CommonTree ID83_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:275:3: ( COLON ID )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:275:5: COLON ID
            {
            root_0 = (CommonTree)adaptor.nil();

            COLON82=(Token)match(input,COLON,FOLLOW_COLON_in_entityType1725); if (state.failed) return retval;
            ID83=(Token)match(input,ID,FOLLOW_ID_in_entityType1728); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ID83_tree = (CommonTree)adaptor.create(ID83);
            adaptor.addChild(root_0, ID83_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "entityType"

    public static class superProcess_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "superProcess"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:279:1: superProcess : COLON ID ;
    public final PBFParser.superProcess_return superProcess() throws RecognitionException {
        PBFParser.superProcess_return retval = new PBFParser.superProcess_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token COLON84=null;
        Token ID85=null;

        CommonTree COLON84_tree=null;
        CommonTree ID85_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:280:3: ( COLON ID )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:280:5: COLON ID
            {
            root_0 = (CommonTree)adaptor.nil();

            COLON84=(Token)match(input,COLON,FOLLOW_COLON_in_superProcess1742); if (state.failed) return retval;
            ID85=(Token)match(input,ID,FOLLOW_ID_in_superProcess1745); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ID85_tree = (CommonTree)adaptor.create(ID85);
            adaptor.addChild(root_0, ID85_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "superProcess"

    public static class processType_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "processType"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:283:1: processType : COLON ID ;
    public final PBFParser.processType_return processType() throws RecognitionException {
        PBFParser.processType_return retval = new PBFParser.processType_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token COLON86=null;
        Token ID87=null;

        CommonTree COLON86_tree=null;
        CommonTree ID87_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:284:3: ( COLON ID )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:284:5: COLON ID
            {
            root_0 = (CommonTree)adaptor.nil();

            COLON86=(Token)match(input,COLON,FOLLOW_COLON_in_processType1758); if (state.failed) return retval;
            ID87=(Token)match(input,ID,FOLLOW_ID_in_processType1761); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ID87_tree = (CommonTree)adaptor.create(ID87);
            adaptor.addChild(root_0, ID87_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "processType"

    public static class compType_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "compType"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:287:1: compType : COLON ID ;
    public final PBFParser.compType_return compType() throws RecognitionException {
        PBFParser.compType_return retval = new PBFParser.compType_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token COLON88=null;
        Token ID89=null;

        CommonTree COLON88_tree=null;
        CommonTree ID89_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:288:3: ( COLON ID )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:288:5: COLON ID
            {
            root_0 = (CommonTree)adaptor.nil();

            COLON88=(Token)match(input,COLON,FOLLOW_COLON_in_compType1774); if (state.failed) return retval;
            ID89=(Token)match(input,ID,FOLLOW_ID_in_compType1777); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ID89_tree = (CommonTree)adaptor.create(ID89);
            adaptor.addChild(root_0, ID89_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "compType"

    public static class templateEntity_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "templateEntity"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:294:1: templateEntity : (t= TEMPLATE ENTITY id= ID se= superEntity block -> ^( STRUCT[$t, \"TE\"] ID ^( ID[$se.tree.getToken(), \"super\"] $se) ( block )? ) | t= TEMPLATE ENTITY id= ID block -> ^( STRUCT[$t, \"TE\"] ID ^( ID[$id, \"super\"] ID[$id, \"Entity\"] ) ( block )? ) );
    public final PBFParser.templateEntity_return templateEntity() throws RecognitionException {
        PBFParser.templateEntity_return retval = new PBFParser.templateEntity_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token t=null;
        Token id=null;
        Token ENTITY90=null;
        Token ENTITY92=null;
        PBFParser.superEntity_return se = null;

        PBFParser.block_return block91 = null;

        PBFParser.block_return block93 = null;


        CommonTree t_tree=null;
        CommonTree id_tree=null;
        CommonTree ENTITY90_tree=null;
        CommonTree ENTITY92_tree=null;
        RewriteRuleTokenStream stream_TEMPLATE=new RewriteRuleTokenStream(adaptor,"token TEMPLATE");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_ENTITY=new RewriteRuleTokenStream(adaptor,"token ENTITY");
        RewriteRuleSubtreeStream stream_superEntity=new RewriteRuleSubtreeStream(adaptor,"rule superEntity");
        RewriteRuleSubtreeStream stream_block=new RewriteRuleSubtreeStream(adaptor,"rule block");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:295:3: (t= TEMPLATE ENTITY id= ID se= superEntity block -> ^( STRUCT[$t, \"TE\"] ID ^( ID[$se.tree.getToken(), \"super\"] $se) ( block )? ) | t= TEMPLATE ENTITY id= ID block -> ^( STRUCT[$t, \"TE\"] ID ^( ID[$id, \"super\"] ID[$id, \"Entity\"] ) ( block )? ) )
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==TEMPLATE) ) {
                int LA27_1 = input.LA(2);

                if ( (LA27_1==ENTITY) ) {
                    int LA27_2 = input.LA(3);

                    if ( (LA27_2==ID) ) {
                        int LA27_3 = input.LA(4);

                        if ( (LA27_3==COLON) ) {
                            alt27=1;
                        }
                        else if ( (LA27_3==LBRACE) ) {
                            alt27=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 27, 3, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 27, 2, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 27, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;
            }
            switch (alt27) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:295:5: t= TEMPLATE ENTITY id= ID se= superEntity block
                    {
                    t=(Token)match(input,TEMPLATE,FOLLOW_TEMPLATE_in_templateEntity1796); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_TEMPLATE.add(t);

                    ENTITY90=(Token)match(input,ENTITY,FOLLOW_ENTITY_in_templateEntity1798); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ENTITY.add(ENTITY90);

                    id=(Token)match(input,ID,FOLLOW_ID_in_templateEntity1802); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(id);

                    pushFollow(FOLLOW_superEntity_in_templateEntity1806);
                    se=superEntity();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_superEntity.add(se.getTree());
                    pushFollow(FOLLOW_block_in_templateEntity1823);
                    block91=block();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_block.add(block91.getTree());


                    // AST REWRITE
                    // elements: ID, se, ID, block
                    // token labels: 
                    // rule labels: retval, se
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_se=new RewriteRuleSubtreeStream(adaptor,"rule se",se!=null?se.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 295:67: -> ^( STRUCT[$t, \"TE\"] ID ^( ID[$se.tree.getToken(), \"super\"] $se) ( block )? )
                    {
                        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:295:70: ^( STRUCT[$t, \"TE\"] ID ^( ID[$se.tree.getToken(), \"super\"] $se) ( block )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRUCT, t, "TE"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());
                        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:295:92: ^( ID[$se.tree.getToken(), \"super\"] $se)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, (se!=null?((CommonTree)se.tree):null).getToken(), "super"), root_2);

                        adaptor.addChild(root_2, stream_se.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }
                        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:295:132: ( block )?
                        if ( stream_block.hasNext() ) {
                            adaptor.addChild(root_1, stream_block.nextTree());

                        }
                        stream_block.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:296:5: t= TEMPLATE ENTITY id= ID block
                    {
                    t=(Token)match(input,TEMPLATE,FOLLOW_TEMPLATE_in_templateEntity1853); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_TEMPLATE.add(t);

                    ENTITY92=(Token)match(input,ENTITY,FOLLOW_ENTITY_in_templateEntity1855); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ENTITY.add(ENTITY92);

                    id=(Token)match(input,ID,FOLLOW_ID_in_templateEntity1859); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(id);

                    pushFollow(FOLLOW_block_in_templateEntity1887);
                    block93=block();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_block.add(block93.getTree());


                    // AST REWRITE
                    // elements: ID, ID, ID, block
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 296:63: -> ^( STRUCT[$t, \"TE\"] ID ^( ID[$id, \"super\"] ID[$id, \"Entity\"] ) ( block )? )
                    {
                        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:296:66: ^( STRUCT[$t, \"TE\"] ID ^( ID[$id, \"super\"] ID[$id, \"Entity\"] ) ( block )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRUCT, t, "TE"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());
                        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:296:88: ^( ID[$id, \"super\"] ID[$id, \"Entity\"] )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, id, "super"), root_2);

                        adaptor.addChild(root_2, (CommonTree)adaptor.create(ID, id, "Entity"));

                        adaptor.addChild(root_1, root_2);
                        }
                        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:296:126: ( block )?
                        if ( stream_block.hasNext() ) {
                            adaptor.addChild(root_1, stream_block.nextTree());

                        }
                        stream_block.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "templateEntity"

    public static class templateProcess_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "templateProcess"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:299:1: templateProcess : (t= TEMPLATE PROCESS id= ID (par= parameters )? sp= superProcess block -> ^( STRUCT[$t, \"TP\"] ID ^( ID[$id, \"parameters\"] ( $par)? ) ^( ID[$sp.tree.getToken(), \"super\"] $sp) ( block )? ) | t= TEMPLATE PROCESS id= ID (par= parameters )? block -> ^( STRUCT[$t, \"TP\"] ID ^( ID[$id, \"parameters\"] ( $par)? ) ^( ID[$id, \"super\"] ID[$id, \"Process\"] ) ( block )? ) );
    public final PBFParser.templateProcess_return templateProcess() throws RecognitionException {
        PBFParser.templateProcess_return retval = new PBFParser.templateProcess_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token t=null;
        Token id=null;
        Token PROCESS94=null;
        Token PROCESS96=null;
        PBFParser.parameters_return par = null;

        PBFParser.superProcess_return sp = null;

        PBFParser.block_return block95 = null;

        PBFParser.block_return block97 = null;


        CommonTree t_tree=null;
        CommonTree id_tree=null;
        CommonTree PROCESS94_tree=null;
        CommonTree PROCESS96_tree=null;
        RewriteRuleTokenStream stream_TEMPLATE=new RewriteRuleTokenStream(adaptor,"token TEMPLATE");
        RewriteRuleTokenStream stream_PROCESS=new RewriteRuleTokenStream(adaptor,"token PROCESS");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_block=new RewriteRuleSubtreeStream(adaptor,"rule block");
        RewriteRuleSubtreeStream stream_superProcess=new RewriteRuleSubtreeStream(adaptor,"rule superProcess");
        RewriteRuleSubtreeStream stream_parameters=new RewriteRuleSubtreeStream(adaptor,"rule parameters");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:300:3: (t= TEMPLATE PROCESS id= ID (par= parameters )? sp= superProcess block -> ^( STRUCT[$t, \"TP\"] ID ^( ID[$id, \"parameters\"] ( $par)? ) ^( ID[$sp.tree.getToken(), \"super\"] $sp) ( block )? ) | t= TEMPLATE PROCESS id= ID (par= parameters )? block -> ^( STRUCT[$t, \"TP\"] ID ^( ID[$id, \"parameters\"] ( $par)? ) ^( ID[$id, \"super\"] ID[$id, \"Process\"] ) ( block )? ) )
            int alt30=2;
            alt30 = dfa30.predict(input);
            switch (alt30) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:300:5: t= TEMPLATE PROCESS id= ID (par= parameters )? sp= superProcess block
                    {
                    t=(Token)match(input,TEMPLATE,FOLLOW_TEMPLATE_in_templateProcess1924); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_TEMPLATE.add(t);

                    PROCESS94=(Token)match(input,PROCESS,FOLLOW_PROCESS_in_templateProcess1926); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_PROCESS.add(PROCESS94);

                    id=(Token)match(input,ID,FOLLOW_ID_in_templateProcess1930); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(id);

                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:300:33: (par= parameters )?
                    int alt28=2;
                    int LA28_0 = input.LA(1);

                    if ( (LA28_0==LPAREN) ) {
                        alt28=1;
                    }
                    switch (alt28) {
                        case 1 :
                            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:300:33: par= parameters
                            {
                            pushFollow(FOLLOW_parameters_in_templateProcess1934);
                            par=parameters();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_parameters.add(par.getTree());

                            }
                            break;

                    }

                    pushFollow(FOLLOW_superProcess_in_templateProcess1939);
                    sp=superProcess();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_superProcess.add(sp.getTree());
                    pushFollow(FOLLOW_block_in_templateProcess1944);
                    block95=block();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_block.add(block95.getTree());


                    // AST REWRITE
                    // elements: ID, par, ID, ID, sp, block
                    // token labels: 
                    // rule labels: par, retval, sp
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_par=new RewriteRuleSubtreeStream(adaptor,"rule par",par!=null?par.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_sp=new RewriteRuleSubtreeStream(adaptor,"rule sp",sp!=null?sp.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 300:73: -> ^( STRUCT[$t, \"TP\"] ID ^( ID[$id, \"parameters\"] ( $par)? ) ^( ID[$sp.tree.getToken(), \"super\"] $sp) ( block )? )
                    {
                        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:300:76: ^( STRUCT[$t, \"TP\"] ID ^( ID[$id, \"parameters\"] ( $par)? ) ^( ID[$sp.tree.getToken(), \"super\"] $sp) ( block )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRUCT, t, "TP"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());
                        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:300:98: ^( ID[$id, \"parameters\"] ( $par)? )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, id, "parameters"), root_2);

                        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:300:122: ( $par)?
                        if ( stream_par.hasNext() ) {
                            adaptor.addChild(root_2, stream_par.nextTree());

                        }
                        stream_par.reset();

                        adaptor.addChild(root_1, root_2);
                        }
                        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:300:129: ^( ID[$sp.tree.getToken(), \"super\"] $sp)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, (sp!=null?((CommonTree)sp.tree):null).getToken(), "super"), root_2);

                        adaptor.addChild(root_2, stream_sp.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }
                        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:300:169: ( block )?
                        if ( stream_block.hasNext() ) {
                            adaptor.addChild(root_1, stream_block.nextTree());

                        }
                        stream_block.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:301:5: t= TEMPLATE PROCESS id= ID (par= parameters )? block
                    {
                    t=(Token)match(input,TEMPLATE,FOLLOW_TEMPLATE_in_templateProcess1983); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_TEMPLATE.add(t);

                    PROCESS96=(Token)match(input,PROCESS,FOLLOW_PROCESS_in_templateProcess1985); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_PROCESS.add(PROCESS96);

                    id=(Token)match(input,ID,FOLLOW_ID_in_templateProcess1989); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(id);

                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:301:33: (par= parameters )?
                    int alt29=2;
                    int LA29_0 = input.LA(1);

                    if ( (LA29_0==LPAREN) ) {
                        alt29=1;
                    }
                    switch (alt29) {
                        case 1 :
                            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:301:33: par= parameters
                            {
                            pushFollow(FOLLOW_parameters_in_templateProcess1993);
                            par=parameters();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_parameters.add(par.getTree());

                            }
                            break;

                    }

                    pushFollow(FOLLOW_block_in_templateProcess2011);
                    block97=block();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_block.add(block97.getTree());


                    // AST REWRITE
                    // elements: ID, ID, ID, block, ID, par
                    // token labels: 
                    // rule labels: par, retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_par=new RewriteRuleSubtreeStream(adaptor,"rule par",par!=null?par.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 301:69: -> ^( STRUCT[$t, \"TP\"] ID ^( ID[$id, \"parameters\"] ( $par)? ) ^( ID[$id, \"super\"] ID[$id, \"Process\"] ) ( block )? )
                    {
                        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:301:72: ^( STRUCT[$t, \"TP\"] ID ^( ID[$id, \"parameters\"] ( $par)? ) ^( ID[$id, \"super\"] ID[$id, \"Process\"] ) ( block )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRUCT, t, "TP"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());
                        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:301:94: ^( ID[$id, \"parameters\"] ( $par)? )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, id, "parameters"), root_2);

                        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:301:118: ( $par)?
                        if ( stream_par.hasNext() ) {
                            adaptor.addChild(root_2, stream_par.nextTree());

                        }
                        stream_par.reset();

                        adaptor.addChild(root_1, root_2);
                        }
                        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:301:125: ^( ID[$id, \"super\"] ID[$id, \"Process\"] )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, id, "super"), root_2);

                        adaptor.addChild(root_2, (CommonTree)adaptor.create(ID, id, "Process"));

                        adaptor.addChild(root_1, root_2);
                        }
                        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:301:164: ( block )?
                        if ( stream_block.hasNext() ) {
                            adaptor.addChild(root_1, stream_block.nextTree());

                        }
                        stream_block.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "templateProcess"

    public static class instanceEntity_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "instanceEntity"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:304:1: instanceEntity : i= ENTITY id= ID et= entityType block -> ^( STRUCT[$i, \"IE\"] ID ^( ID[$et.tree.getToken(), \"template\"] $et) ( block )? ) ;
    public final PBFParser.instanceEntity_return instanceEntity() throws RecognitionException {
        PBFParser.instanceEntity_return retval = new PBFParser.instanceEntity_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token i=null;
        Token id=null;
        PBFParser.entityType_return et = null;

        PBFParser.block_return block98 = null;


        CommonTree i_tree=null;
        CommonTree id_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_ENTITY=new RewriteRuleTokenStream(adaptor,"token ENTITY");
        RewriteRuleSubtreeStream stream_block=new RewriteRuleSubtreeStream(adaptor,"rule block");
        RewriteRuleSubtreeStream stream_entityType=new RewriteRuleSubtreeStream(adaptor,"rule entityType");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:305:3: (i= ENTITY id= ID et= entityType block -> ^( STRUCT[$i, \"IE\"] ID ^( ID[$et.tree.getToken(), \"template\"] $et) ( block )? ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:305:5: i= ENTITY id= ID et= entityType block
            {
            i=(Token)match(input,ENTITY,FOLLOW_ENTITY_in_instanceEntity2057); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ENTITY.add(i);

            id=(Token)match(input,ID,FOLLOW_ID_in_instanceEntity2061); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(id);

            pushFollow(FOLLOW_entityType_in_instanceEntity2065);
            et=entityType();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_entityType.add(et.getTree());
            pushFollow(FOLLOW_block_in_instanceEntity2090);
            block98=block();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_block.add(block98.getTree());


            // AST REWRITE
            // elements: ID, et, block, ID
            // token labels: 
            // rule labels: retval, et
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_et=new RewriteRuleSubtreeStream(adaptor,"rule et",et!=null?et.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 305:65: -> ^( STRUCT[$i, \"IE\"] ID ^( ID[$et.tree.getToken(), \"template\"] $et) ( block )? )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:305:68: ^( STRUCT[$i, \"IE\"] ID ^( ID[$et.tree.getToken(), \"template\"] $et) ( block )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRUCT, i, "IE"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:305:90: ^( ID[$et.tree.getToken(), \"template\"] $et)
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, (et!=null?((CommonTree)et.tree):null).getToken(), "template"), root_2);

                adaptor.addChild(root_2, stream_et.nextTree());

                adaptor.addChild(root_1, root_2);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:305:133: ( block )?
                if ( stream_block.hasNext() ) {
                    adaptor.addChild(root_1, stream_block.nextTree());

                }
                stream_block.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "instanceEntity"

    public static class instanceProcess_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "instanceProcess"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:308:1: instanceProcess : i= PROCESS id= ID (arg= arguments1 )? pt= processType block -> ^( STRUCT[$i, \"IP\"] ID ^( ID[$id, \"arguments\"] ( $arg)? ) ^( ID[$pt.tree.getToken(), \"template\"] $pt) ( block )? ) ;
    public final PBFParser.instanceProcess_return instanceProcess() throws RecognitionException {
        PBFParser.instanceProcess_return retval = new PBFParser.instanceProcess_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token i=null;
        Token id=null;
        PBFParser.arguments1_return arg = null;

        PBFParser.processType_return pt = null;

        PBFParser.block_return block99 = null;


        CommonTree i_tree=null;
        CommonTree id_tree=null;
        RewriteRuleTokenStream stream_PROCESS=new RewriteRuleTokenStream(adaptor,"token PROCESS");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_processType=new RewriteRuleSubtreeStream(adaptor,"rule processType");
        RewriteRuleSubtreeStream stream_block=new RewriteRuleSubtreeStream(adaptor,"rule block");
        RewriteRuleSubtreeStream stream_arguments1=new RewriteRuleSubtreeStream(adaptor,"rule arguments1");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:309:3: (i= PROCESS id= ID (arg= arguments1 )? pt= processType block -> ^( STRUCT[$i, \"IP\"] ID ^( ID[$id, \"arguments\"] ( $arg)? ) ^( ID[$pt.tree.getToken(), \"template\"] $pt) ( block )? ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:309:5: i= PROCESS id= ID (arg= arguments1 )? pt= processType block
            {
            i=(Token)match(input,PROCESS,FOLLOW_PROCESS_in_instanceProcess2127); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_PROCESS.add(i);

            id=(Token)match(input,ID,FOLLOW_ID_in_instanceProcess2131); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(id);

            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:309:24: (arg= arguments1 )?
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( (LA31_0==LPAREN) ) {
                alt31=1;
            }
            switch (alt31) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:309:24: arg= arguments1
                    {
                    pushFollow(FOLLOW_arguments1_in_instanceProcess2135);
                    arg=arguments1();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_arguments1.add(arg.getTree());

                    }
                    break;

            }

            pushFollow(FOLLOW_processType_in_instanceProcess2140);
            pt=processType();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_processType.add(pt.getTree());
            pushFollow(FOLLOW_block_in_instanceProcess2153);
            block99=block();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_block.add(block99.getTree());


            // AST REWRITE
            // elements: ID, ID, arg, block, pt, ID
            // token labels: 
            // rule labels: arg, retval, pt
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_arg=new RewriteRuleSubtreeStream(adaptor,"rule arg",arg!=null?arg.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_pt=new RewriteRuleSubtreeStream(adaptor,"rule pt",pt!=null?pt.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 309:71: -> ^( STRUCT[$i, \"IP\"] ID ^( ID[$id, \"arguments\"] ( $arg)? ) ^( ID[$pt.tree.getToken(), \"template\"] $pt) ( block )? )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:309:74: ^( STRUCT[$i, \"IP\"] ID ^( ID[$id, \"arguments\"] ( $arg)? ) ^( ID[$pt.tree.getToken(), \"template\"] $pt) ( block )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRUCT, i, "IP"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:309:96: ^( ID[$id, \"arguments\"] ( $arg)? )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, id, "arguments"), root_2);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:309:119: ( $arg)?
                if ( stream_arg.hasNext() ) {
                    adaptor.addChild(root_2, stream_arg.nextTree());

                }
                stream_arg.reset();

                adaptor.addChild(root_1, root_2);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:309:126: ^( ID[$pt.tree.getToken(), \"template\"] $pt)
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, (pt!=null?((CommonTree)pt.tree):null).getToken(), "template"), root_2);

                adaptor.addChild(root_2, stream_pt.nextTree());

                adaptor.addChild(root_1, root_2);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:309:169: ( block )?
                if ( stream_block.hasNext() ) {
                    adaptor.addChild(root_1, stream_block.nextTree());

                }
                stream_block.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "instanceProcess"

    public static class incompleteEntity_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "incompleteEntity"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:312:1: incompleteEntity : i= ENTITY id= ID et= entityType blockNull -> ^( STRUCT[$i, \"IE\"] ID ^( ID[$et.tree.getToken(), \"template\"] $et) ( blockNull )? ) ;
    public final PBFParser.incompleteEntity_return incompleteEntity() throws RecognitionException {
        PBFParser.incompleteEntity_return retval = new PBFParser.incompleteEntity_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token i=null;
        Token id=null;
        PBFParser.entityType_return et = null;

        PBFParser.blockNull_return blockNull100 = null;


        CommonTree i_tree=null;
        CommonTree id_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_ENTITY=new RewriteRuleTokenStream(adaptor,"token ENTITY");
        RewriteRuleSubtreeStream stream_blockNull=new RewriteRuleSubtreeStream(adaptor,"rule blockNull");
        RewriteRuleSubtreeStream stream_entityType=new RewriteRuleSubtreeStream(adaptor,"rule entityType");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:313:3: (i= ENTITY id= ID et= entityType blockNull -> ^( STRUCT[$i, \"IE\"] ID ^( ID[$et.tree.getToken(), \"template\"] $et) ( blockNull )? ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:313:5: i= ENTITY id= ID et= entityType blockNull
            {
            i=(Token)match(input,ENTITY,FOLLOW_ENTITY_in_incompleteEntity2199); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ENTITY.add(i);

            id=(Token)match(input,ID,FOLLOW_ID_in_incompleteEntity2203); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(id);

            pushFollow(FOLLOW_entityType_in_incompleteEntity2207);
            et=entityType();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_entityType.add(et.getTree());
            pushFollow(FOLLOW_blockNull_in_incompleteEntity2232);
            blockNull100=blockNull();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_blockNull.add(blockNull100.getTree());


            // AST REWRITE
            // elements: et, ID, ID, blockNull
            // token labels: 
            // rule labels: retval, et
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_et=new RewriteRuleSubtreeStream(adaptor,"rule et",et!=null?et.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 313:66: -> ^( STRUCT[$i, \"IE\"] ID ^( ID[$et.tree.getToken(), \"template\"] $et) ( blockNull )? )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:313:69: ^( STRUCT[$i, \"IE\"] ID ^( ID[$et.tree.getToken(), \"template\"] $et) ( blockNull )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRUCT, i, "IE"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:313:91: ^( ID[$et.tree.getToken(), \"template\"] $et)
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, (et!=null?((CommonTree)et.tree):null).getToken(), "template"), root_2);

                adaptor.addChild(root_2, stream_et.nextTree());

                adaptor.addChild(root_1, root_2);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:313:134: ( blockNull )?
                if ( stream_blockNull.hasNext() ) {
                    adaptor.addChild(root_1, stream_blockNull.nextTree());

                }
                stream_blockNull.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "incompleteEntity"

    public static class incompleteProcess_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "incompleteProcess"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:316:1: incompleteProcess : i= PROCESS id= ID (arg= arguments1 )? pt= processType blockNull -> ^( STRUCT[$i, \"IP\"] ID ^( ID[$id, \"arguments\"] ( $arg)? ) ^( ID[$pt.tree.getToken(), \"template\"] $pt) ( blockNull )? ) ;
    public final PBFParser.incompleteProcess_return incompleteProcess() throws RecognitionException {
        PBFParser.incompleteProcess_return retval = new PBFParser.incompleteProcess_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token i=null;
        Token id=null;
        PBFParser.arguments1_return arg = null;

        PBFParser.processType_return pt = null;

        PBFParser.blockNull_return blockNull101 = null;


        CommonTree i_tree=null;
        CommonTree id_tree=null;
        RewriteRuleTokenStream stream_PROCESS=new RewriteRuleTokenStream(adaptor,"token PROCESS");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_processType=new RewriteRuleSubtreeStream(adaptor,"rule processType");
        RewriteRuleSubtreeStream stream_blockNull=new RewriteRuleSubtreeStream(adaptor,"rule blockNull");
        RewriteRuleSubtreeStream stream_arguments1=new RewriteRuleSubtreeStream(adaptor,"rule arguments1");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:317:3: (i= PROCESS id= ID (arg= arguments1 )? pt= processType blockNull -> ^( STRUCT[$i, \"IP\"] ID ^( ID[$id, \"arguments\"] ( $arg)? ) ^( ID[$pt.tree.getToken(), \"template\"] $pt) ( blockNull )? ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:317:5: i= PROCESS id= ID (arg= arguments1 )? pt= processType blockNull
            {
            i=(Token)match(input,PROCESS,FOLLOW_PROCESS_in_incompleteProcess2266); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_PROCESS.add(i);

            id=(Token)match(input,ID,FOLLOW_ID_in_incompleteProcess2270); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(id);

            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:317:24: (arg= arguments1 )?
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==LPAREN) ) {
                alt32=1;
            }
            switch (alt32) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:317:24: arg= arguments1
                    {
                    pushFollow(FOLLOW_arguments1_in_incompleteProcess2274);
                    arg=arguments1();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_arguments1.add(arg.getTree());

                    }
                    break;

            }

            pushFollow(FOLLOW_processType_in_incompleteProcess2279);
            pt=processType();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_processType.add(pt.getTree());
            pushFollow(FOLLOW_blockNull_in_incompleteProcess2292);
            blockNull101=blockNull();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_blockNull.add(blockNull101.getTree());


            // AST REWRITE
            // elements: ID, ID, pt, ID, arg, blockNull
            // token labels: 
            // rule labels: arg, retval, pt
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_arg=new RewriteRuleSubtreeStream(adaptor,"rule arg",arg!=null?arg.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_pt=new RewriteRuleSubtreeStream(adaptor,"rule pt",pt!=null?pt.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 317:72: -> ^( STRUCT[$i, \"IP\"] ID ^( ID[$id, \"arguments\"] ( $arg)? ) ^( ID[$pt.tree.getToken(), \"template\"] $pt) ( blockNull )? )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:317:75: ^( STRUCT[$i, \"IP\"] ID ^( ID[$id, \"arguments\"] ( $arg)? ) ^( ID[$pt.tree.getToken(), \"template\"] $pt) ( blockNull )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRUCT, i, "IP"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:317:97: ^( ID[$id, \"arguments\"] ( $arg)? )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, id, "arguments"), root_2);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:317:120: ( $arg)?
                if ( stream_arg.hasNext() ) {
                    adaptor.addChild(root_2, stream_arg.nextTree());

                }
                stream_arg.reset();

                adaptor.addChild(root_1, root_2);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:317:127: ^( ID[$pt.tree.getToken(), \"template\"] $pt)
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, (pt!=null?((CommonTree)pt.tree):null).getToken(), "template"), root_2);

                adaptor.addChild(root_2, stream_pt.nextTree());

                adaptor.addChild(root_1, root_2);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:317:170: ( blockNull )?
                if ( stream_blockNull.hasNext() ) {
                    adaptor.addChild(root_1, stream_blockNull.nextTree());

                }
                stream_blockNull.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "incompleteProcess"

    public static class fullEntity_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "fullEntity"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:323:1: fullEntity : f= ENTITY id= ID block -> ^( STRUCT[$f, \"FE\"] ID ( block )? ) ;
    public final PBFParser.fullEntity_return fullEntity() throws RecognitionException {
        PBFParser.fullEntity_return retval = new PBFParser.fullEntity_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token f=null;
        Token id=null;
        PBFParser.block_return block102 = null;


        CommonTree f_tree=null;
        CommonTree id_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_ENTITY=new RewriteRuleTokenStream(adaptor,"token ENTITY");
        RewriteRuleSubtreeStream stream_block=new RewriteRuleSubtreeStream(adaptor,"rule block");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:324:3: (f= ENTITY id= ID block -> ^( STRUCT[$f, \"FE\"] ID ( block )? ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:324:5: f= ENTITY id= ID block
            {
            f=(Token)match(input,ENTITY,FOLLOW_ENTITY_in_fullEntity2338); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ENTITY.add(f);

            id=(Token)match(input,ID,FOLLOW_ID_in_fullEntity2342); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(id);

            pushFollow(FOLLOW_block_in_fullEntity2375);
            block102=block();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_block.add(block102.getTree());


            // AST REWRITE
            // elements: block, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 324:59: -> ^( STRUCT[$f, \"FE\"] ID ( block )? )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:324:62: ^( STRUCT[$f, \"FE\"] ID ( block )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRUCT, f, "FE"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:324:84: ( block )?
                if ( stream_block.hasNext() ) {
                    adaptor.addChild(root_1, stream_block.nextTree());

                }
                stream_block.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "fullEntity"

    public static class fullProcess_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "fullProcess"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:327:1: fullProcess : f= PROCESS id= ID (arg= arguments1 )? block -> ^( STRUCT[$f, \"FP\"] ID ^( ID[$id, \"arguments\"] ( $arg)? ) ( block )? ) ;
    public final PBFParser.fullProcess_return fullProcess() throws RecognitionException {
        PBFParser.fullProcess_return retval = new PBFParser.fullProcess_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token f=null;
        Token id=null;
        PBFParser.arguments1_return arg = null;

        PBFParser.block_return block103 = null;


        CommonTree f_tree=null;
        CommonTree id_tree=null;
        RewriteRuleTokenStream stream_PROCESS=new RewriteRuleTokenStream(adaptor,"token PROCESS");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_block=new RewriteRuleSubtreeStream(adaptor,"rule block");
        RewriteRuleSubtreeStream stream_arguments1=new RewriteRuleSubtreeStream(adaptor,"rule arguments1");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:328:3: (f= PROCESS id= ID (arg= arguments1 )? block -> ^( STRUCT[$f, \"FP\"] ID ^( ID[$id, \"arguments\"] ( $arg)? ) ( block )? ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:328:5: f= PROCESS id= ID (arg= arguments1 )? block
            {
            f=(Token)match(input,PROCESS,FOLLOW_PROCESS_in_fullProcess2406); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_PROCESS.add(f);

            id=(Token)match(input,ID,FOLLOW_ID_in_fullProcess2410); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(id);

            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:328:24: (arg= arguments1 )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==LPAREN) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:328:24: arg= arguments1
                    {
                    pushFollow(FOLLOW_arguments1_in_fullProcess2414);
                    arg=arguments1();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_arguments1.add(arg.getTree());

                    }
                    break;

            }

            pushFollow(FOLLOW_block_in_fullProcess2439);
            block103=block();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_block.add(block103.getTree());


            // AST REWRITE
            // elements: block, arg, ID, ID
            // token labels: 
            // rule labels: arg, retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_arg=new RewriteRuleSubtreeStream(adaptor,"rule arg",arg!=null?arg.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 328:67: -> ^( STRUCT[$f, \"FP\"] ID ^( ID[$id, \"arguments\"] ( $arg)? ) ( block )? )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:328:70: ^( STRUCT[$f, \"FP\"] ID ^( ID[$id, \"arguments\"] ( $arg)? ) ( block )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRUCT, f, "FP"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:328:92: ^( ID[$id, \"arguments\"] ( $arg)? )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, id, "arguments"), root_2);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:328:115: ( $arg)?
                if ( stream_arg.hasNext() ) {
                    adaptor.addChild(root_2, stream_arg.nextTree());

                }
                stream_arg.reset();

                adaptor.addChild(root_1, root_2);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:328:122: ( block )?
                if ( stream_block.hasNext() ) {
                    adaptor.addChild(root_1, stream_block.nextTree());

                }
                stream_block.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "fullProcess"

    public static class mixedEntity_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "mixedEntity"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:331:1: mixedEntity : m= ENTITY id= ID (et= entityType )? block -> ^( STRUCT[$m, \"ME\"] ID ^( ID[$et.tree.getToken(), \"template\"] ( $et)? ) ( block )? ) ;
    public final PBFParser.mixedEntity_return mixedEntity() throws RecognitionException {
        PBFParser.mixedEntity_return retval = new PBFParser.mixedEntity_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token m=null;
        Token id=null;
        PBFParser.entityType_return et = null;

        PBFParser.block_return block104 = null;


        CommonTree m_tree=null;
        CommonTree id_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_ENTITY=new RewriteRuleTokenStream(adaptor,"token ENTITY");
        RewriteRuleSubtreeStream stream_block=new RewriteRuleSubtreeStream(adaptor,"rule block");
        RewriteRuleSubtreeStream stream_entityType=new RewriteRuleSubtreeStream(adaptor,"rule entityType");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:332:3: (m= ENTITY id= ID (et= entityType )? block -> ^( STRUCT[$m, \"ME\"] ID ^( ID[$et.tree.getToken(), \"template\"] ( $et)? ) ( block )? ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:332:5: m= ENTITY id= ID (et= entityType )? block
            {
            m=(Token)match(input,ENTITY,FOLLOW_ENTITY_in_mixedEntity2479); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ENTITY.add(m);

            id=(Token)match(input,ID,FOLLOW_ID_in_mixedEntity2483); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(id);

            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:332:22: (et= entityType )?
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( (LA34_0==COLON) ) {
                alt34=1;
            }
            switch (alt34) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:332:22: et= entityType
                    {
                    pushFollow(FOLLOW_entityType_in_mixedEntity2487);
                    et=entityType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_entityType.add(et.getTree());

                    }
                    break;

            }

            pushFollow(FOLLOW_block_in_mixedEntity2512);
            block104=block();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_block.add(block104.getTree());


            // AST REWRITE
            // elements: ID, et, block, ID
            // token labels: 
            // rule labels: retval, et
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_et=new RewriteRuleSubtreeStream(adaptor,"rule et",et!=null?et.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 332:65: -> ^( STRUCT[$m, \"ME\"] ID ^( ID[$et.tree.getToken(), \"template\"] ( $et)? ) ( block )? )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:332:68: ^( STRUCT[$m, \"ME\"] ID ^( ID[$et.tree.getToken(), \"template\"] ( $et)? ) ( block )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRUCT, m, "ME"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:332:90: ^( ID[$et.tree.getToken(), \"template\"] ( $et)? )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, (et!=null?((CommonTree)et.tree):null).getToken(), "template"), root_2);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:332:128: ( $et)?
                if ( stream_et.hasNext() ) {
                    adaptor.addChild(root_2, stream_et.nextTree());

                }
                stream_et.reset();

                adaptor.addChild(root_1, root_2);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:332:134: ( block )?
                if ( stream_block.hasNext() ) {
                    adaptor.addChild(root_1, stream_block.nextTree());

                }
                stream_block.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "mixedEntity"

    public static class mixedProcess_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "mixedProcess"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:335:1: mixedProcess : m= PROCESS id= ID (arg= arguments1 )? (pt= processType (par= parameters )? )? block -> ^( STRUCT[$m, \"MP\"] ID ^( ID[$id, \"arguments\"] ( $arg)? ) ^( ID[$id, \"template\"] ( $pt)? ) ( ^( ID[$id, \"parameters\"] $par) )? ( block )? ) ;
    public final PBFParser.mixedProcess_return mixedProcess() throws RecognitionException {
        PBFParser.mixedProcess_return retval = new PBFParser.mixedProcess_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token m=null;
        Token id=null;
        PBFParser.arguments1_return arg = null;

        PBFParser.processType_return pt = null;

        PBFParser.parameters_return par = null;

        PBFParser.block_return block105 = null;


        CommonTree m_tree=null;
        CommonTree id_tree=null;
        RewriteRuleTokenStream stream_PROCESS=new RewriteRuleTokenStream(adaptor,"token PROCESS");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_processType=new RewriteRuleSubtreeStream(adaptor,"rule processType");
        RewriteRuleSubtreeStream stream_block=new RewriteRuleSubtreeStream(adaptor,"rule block");
        RewriteRuleSubtreeStream stream_parameters=new RewriteRuleSubtreeStream(adaptor,"rule parameters");
        RewriteRuleSubtreeStream stream_arguments1=new RewriteRuleSubtreeStream(adaptor,"rule arguments1");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:336:3: (m= PROCESS id= ID (arg= arguments1 )? (pt= processType (par= parameters )? )? block -> ^( STRUCT[$m, \"MP\"] ID ^( ID[$id, \"arguments\"] ( $arg)? ) ^( ID[$id, \"template\"] ( $pt)? ) ( ^( ID[$id, \"parameters\"] $par) )? ( block )? ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:336:5: m= PROCESS id= ID (arg= arguments1 )? (pt= processType (par= parameters )? )? block
            {
            m=(Token)match(input,PROCESS,FOLLOW_PROCESS_in_mixedProcess2550); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_PROCESS.add(m);

            id=(Token)match(input,ID,FOLLOW_ID_in_mixedProcess2554); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(id);

            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:336:24: (arg= arguments1 )?
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==LPAREN) ) {
                alt35=1;
            }
            switch (alt35) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:336:24: arg= arguments1
                    {
                    pushFollow(FOLLOW_arguments1_in_mixedProcess2558);
                    arg=arguments1();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_arguments1.add(arg.getTree());

                    }
                    break;

            }

            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:336:37: (pt= processType (par= parameters )? )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==COLON) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:336:38: pt= processType (par= parameters )?
                    {
                    pushFollow(FOLLOW_processType_in_mixedProcess2564);
                    pt=processType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_processType.add(pt.getTree());
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:336:56: (par= parameters )?
                    int alt36=2;
                    int LA36_0 = input.LA(1);

                    if ( (LA36_0==LPAREN) ) {
                        alt36=1;
                    }
                    switch (alt36) {
                        case 1 :
                            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:336:56: par= parameters
                            {
                            pushFollow(FOLLOW_parameters_in_mixedProcess2568);
                            par=parameters();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_parameters.add(par.getTree());

                            }
                            break;

                    }


                    }
                    break;

            }

            pushFollow(FOLLOW_block_in_mixedProcess2573);
            block105=block();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_block.add(block105.getTree());


            // AST REWRITE
            // elements: ID, arg, par, ID, pt, ID, ID, block
            // token labels: 
            // rule labels: par, arg, retval, pt
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_par=new RewriteRuleSubtreeStream(adaptor,"rule par",par!=null?par.tree:null);
            RewriteRuleSubtreeStream stream_arg=new RewriteRuleSubtreeStream(adaptor,"rule arg",arg!=null?arg.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_pt=new RewriteRuleSubtreeStream(adaptor,"rule pt",pt!=null?pt.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 336:77: -> ^( STRUCT[$m, \"MP\"] ID ^( ID[$id, \"arguments\"] ( $arg)? ) ^( ID[$id, \"template\"] ( $pt)? ) ( ^( ID[$id, \"parameters\"] $par) )? ( block )? )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:336:80: ^( STRUCT[$m, \"MP\"] ID ^( ID[$id, \"arguments\"] ( $arg)? ) ^( ID[$id, \"template\"] ( $pt)? ) ( ^( ID[$id, \"parameters\"] $par) )? ( block )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRUCT, m, "MP"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:336:102: ^( ID[$id, \"arguments\"] ( $arg)? )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, id, "arguments"), root_2);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:336:125: ( $arg)?
                if ( stream_arg.hasNext() ) {
                    adaptor.addChild(root_2, stream_arg.nextTree());

                }
                stream_arg.reset();

                adaptor.addChild(root_1, root_2);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:336:132: ^( ID[$id, \"template\"] ( $pt)? )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, id, "template"), root_2);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:336:154: ( $pt)?
                if ( stream_pt.hasNext() ) {
                    adaptor.addChild(root_2, stream_pt.nextTree());

                }
                stream_pt.reset();

                adaptor.addChild(root_1, root_2);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:336:160: ( ^( ID[$id, \"parameters\"] $par) )?
                if ( stream_par.hasNext()||stream_ID.hasNext() ) {
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:336:161: ^( ID[$id, \"parameters\"] $par)
                    {
                    CommonTree root_2 = (CommonTree)adaptor.nil();
                    root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, id, "parameters"), root_2);

                    adaptor.addChild(root_2, stream_par.nextTree());

                    adaptor.addChild(root_1, root_2);
                    }

                }
                stream_par.reset();
                stream_ID.reset();
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:336:193: ( block )?
                if ( stream_block.hasNext() ) {
                    adaptor.addChild(root_1, stream_block.nextTree());

                }
                stream_block.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "mixedProcess"

    public static class templateCompartment_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "templateCompartment"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:339:1: templateCompartment : t= TEMPLATE COMPARTMENT ID block -> ^( STRUCT[$t, \"TK\"] ID ( block )? ) ;
    public final PBFParser.templateCompartment_return templateCompartment() throws RecognitionException {
        PBFParser.templateCompartment_return retval = new PBFParser.templateCompartment_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token t=null;
        Token COMPARTMENT106=null;
        Token ID107=null;
        PBFParser.block_return block108 = null;


        CommonTree t_tree=null;
        CommonTree COMPARTMENT106_tree=null;
        CommonTree ID107_tree=null;
        RewriteRuleTokenStream stream_TEMPLATE=new RewriteRuleTokenStream(adaptor,"token TEMPLATE");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_COMPARTMENT=new RewriteRuleTokenStream(adaptor,"token COMPARTMENT");
        RewriteRuleSubtreeStream stream_block=new RewriteRuleSubtreeStream(adaptor,"rule block");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:340:3: (t= TEMPLATE COMPARTMENT ID block -> ^( STRUCT[$t, \"TK\"] ID ( block )? ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:340:5: t= TEMPLATE COMPARTMENT ID block
            {
            t=(Token)match(input,TEMPLATE,FOLLOW_TEMPLATE_in_templateCompartment2629); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_TEMPLATE.add(t);

            COMPARTMENT106=(Token)match(input,COMPARTMENT,FOLLOW_COMPARTMENT_in_templateCompartment2631); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COMPARTMENT.add(COMPARTMENT106);

            ID107=(Token)match(input,ID,FOLLOW_ID_in_templateCompartment2633); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(ID107);

            pushFollow(FOLLOW_block_in_templateCompartment2661);
            block108=block();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_block.add(block108.getTree());


            // AST REWRITE
            // elements: ID, block
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 340:65: -> ^( STRUCT[$t, \"TK\"] ID ( block )? )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:340:68: ^( STRUCT[$t, \"TK\"] ID ( block )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRUCT, t, "TK"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:340:90: ( block )?
                if ( stream_block.hasNext() ) {
                    adaptor.addChild(root_1, stream_block.nextTree());

                }
                stream_block.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "templateCompartment"

    public static class instanceCompartment_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "instanceCompartment"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:343:1: instanceCompartment : i= COMPARTMENT id= ID ct= compType compBlock -> ^( STRUCT[$i, \"IK\"] ID ^( ID[$ct.tree.getToken(), \"template\"] $ct) ( compBlock )? ) ;
    public final PBFParser.instanceCompartment_return instanceCompartment() throws RecognitionException {
        PBFParser.instanceCompartment_return retval = new PBFParser.instanceCompartment_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token i=null;
        Token id=null;
        PBFParser.compType_return ct = null;

        PBFParser.compBlock_return compBlock109 = null;


        CommonTree i_tree=null;
        CommonTree id_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_COMPARTMENT=new RewriteRuleTokenStream(adaptor,"token COMPARTMENT");
        RewriteRuleSubtreeStream stream_compType=new RewriteRuleSubtreeStream(adaptor,"rule compType");
        RewriteRuleSubtreeStream stream_compBlock=new RewriteRuleSubtreeStream(adaptor,"rule compBlock");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:344:3: (i= COMPARTMENT id= ID ct= compType compBlock -> ^( STRUCT[$i, \"IK\"] ID ^( ID[$ct.tree.getToken(), \"template\"] $ct) ( compBlock )? ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:344:5: i= COMPARTMENT id= ID ct= compType compBlock
            {
            i=(Token)match(input,COMPARTMENT,FOLLOW_COMPARTMENT_in_instanceCompartment2692); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COMPARTMENT.add(i);

            id=(Token)match(input,ID,FOLLOW_ID_in_instanceCompartment2696); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(id);

            pushFollow(FOLLOW_compType_in_instanceCompartment2700);
            ct=compType();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_compType.add(ct.getTree());
            pushFollow(FOLLOW_compBlock_in_instanceCompartment2722);
            compBlock109=compBlock();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_compBlock.add(compBlock109.getTree());


            // AST REWRITE
            // elements: ID, ct, compBlock, ID
            // token labels: 
            // rule labels: retval, ct
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_ct=new RewriteRuleSubtreeStream(adaptor,"rule ct",ct!=null?ct.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 344:67: -> ^( STRUCT[$i, \"IK\"] ID ^( ID[$ct.tree.getToken(), \"template\"] $ct) ( compBlock )? )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:344:70: ^( STRUCT[$i, \"IK\"] ID ^( ID[$ct.tree.getToken(), \"template\"] $ct) ( compBlock )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRUCT, i, "IK"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:344:92: ^( ID[$ct.tree.getToken(), \"template\"] $ct)
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, (ct!=null?((CommonTree)ct.tree):null).getToken(), "template"), root_2);

                adaptor.addChild(root_2, stream_ct.nextTree());

                adaptor.addChild(root_1, root_2);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:344:135: ( compBlock )?
                if ( stream_compBlock.hasNext() ) {
                    adaptor.addChild(root_1, stream_compBlock.nextTree());

                }
                stream_compBlock.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "instanceCompartment"

    public static class incompleteCompartment_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "incompleteCompartment"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:347:1: incompleteCompartment : i= COMPARTMENT id= ID ct= compType compBlockNull -> ^( STRUCT[$i, \"IK\"] ID ^( ID[$ct.tree.getToken(), \"template\"] $ct) ( compBlockNull )? ) ;
    public final PBFParser.incompleteCompartment_return incompleteCompartment() throws RecognitionException {
        PBFParser.incompleteCompartment_return retval = new PBFParser.incompleteCompartment_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token i=null;
        Token id=null;
        PBFParser.compType_return ct = null;

        PBFParser.compBlockNull_return compBlockNull110 = null;


        CommonTree i_tree=null;
        CommonTree id_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_COMPARTMENT=new RewriteRuleTokenStream(adaptor,"token COMPARTMENT");
        RewriteRuleSubtreeStream stream_compBlockNull=new RewriteRuleSubtreeStream(adaptor,"rule compBlockNull");
        RewriteRuleSubtreeStream stream_compType=new RewriteRuleSubtreeStream(adaptor,"rule compType");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:348:3: (i= COMPARTMENT id= ID ct= compType compBlockNull -> ^( STRUCT[$i, \"IK\"] ID ^( ID[$ct.tree.getToken(), \"template\"] $ct) ( compBlockNull )? ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:348:5: i= COMPARTMENT id= ID ct= compType compBlockNull
            {
            i=(Token)match(input,COMPARTMENT,FOLLOW_COMPARTMENT_in_incompleteCompartment2757); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COMPARTMENT.add(i);

            id=(Token)match(input,ID,FOLLOW_ID_in_incompleteCompartment2761); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(id);

            pushFollow(FOLLOW_compType_in_incompleteCompartment2765);
            ct=compType();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_compType.add(ct.getTree());
            pushFollow(FOLLOW_compBlockNull_in_incompleteCompartment2787);
            compBlockNull110=compBlockNull();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_compBlockNull.add(compBlockNull110.getTree());


            // AST REWRITE
            // elements: ID, compBlockNull, ct, ID
            // token labels: 
            // rule labels: retval, ct
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_ct=new RewriteRuleSubtreeStream(adaptor,"rule ct",ct!=null?ct.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 348:71: -> ^( STRUCT[$i, \"IK\"] ID ^( ID[$ct.tree.getToken(), \"template\"] $ct) ( compBlockNull )? )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:348:74: ^( STRUCT[$i, \"IK\"] ID ^( ID[$ct.tree.getToken(), \"template\"] $ct) ( compBlockNull )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRUCT, i, "IK"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:348:96: ^( ID[$ct.tree.getToken(), \"template\"] $ct)
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, (ct!=null?((CommonTree)ct.tree):null).getToken(), "template"), root_2);

                adaptor.addChild(root_2, stream_ct.nextTree());

                adaptor.addChild(root_1, root_2);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:348:139: ( compBlockNull )?
                if ( stream_compBlockNull.hasNext() ) {
                    adaptor.addChild(root_1, stream_compBlockNull.nextTree());

                }
                stream_compBlockNull.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "incompleteCompartment"

    public static class compBlock_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "compBlock"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:351:1: compBlock : l= LBRACE (ies+= instanceEntity | ips+= instanceProcess | iks+= instanceCompartment )* RBRACE -> ^( ID[$l, \"ies\"] ( $ies)* ) ^( ID[$l, \"ips\"] ( $ips)* ) ^( ID[$l, \"iks\"] ( $iks)* ) ;
    public final PBFParser.compBlock_return compBlock() throws RecognitionException {
        PBFParser.compBlock_return retval = new PBFParser.compBlock_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token l=null;
        Token RBRACE111=null;
        List list_ies=null;
        List list_ips=null;
        List list_iks=null;
        RuleReturnScope ies = null;
        RuleReturnScope ips = null;
        RuleReturnScope iks = null;
        CommonTree l_tree=null;
        CommonTree RBRACE111_tree=null;
        RewriteRuleTokenStream stream_RBRACE=new RewriteRuleTokenStream(adaptor,"token RBRACE");
        RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
        RewriteRuleSubtreeStream stream_instanceCompartment=new RewriteRuleSubtreeStream(adaptor,"rule instanceCompartment");
        RewriteRuleSubtreeStream stream_instanceProcess=new RewriteRuleSubtreeStream(adaptor,"rule instanceProcess");
        RewriteRuleSubtreeStream stream_instanceEntity=new RewriteRuleSubtreeStream(adaptor,"rule instanceEntity");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:352:3: (l= LBRACE (ies+= instanceEntity | ips+= instanceProcess | iks+= instanceCompartment )* RBRACE -> ^( ID[$l, \"ies\"] ( $ies)* ) ^( ID[$l, \"ips\"] ( $ips)* ) ^( ID[$l, \"iks\"] ( $iks)* ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:352:5: l= LBRACE (ies+= instanceEntity | ips+= instanceProcess | iks+= instanceCompartment )* RBRACE
            {
            l=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_compBlock2822); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACE.add(l);

            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:353:7: (ies+= instanceEntity | ips+= instanceProcess | iks+= instanceCompartment )*
            loop38:
            do {
                int alt38=4;
                switch ( input.LA(1) ) {
                case ENTITY:
                    {
                    alt38=1;
                    }
                    break;
                case PROCESS:
                    {
                    alt38=2;
                    }
                    break;
                case COMPARTMENT:
                    {
                    alt38=3;
                    }
                    break;

                }

                switch (alt38) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:353:8: ies+= instanceEntity
            	    {
            	    pushFollow(FOLLOW_instanceEntity_in_compBlock2833);
            	    ies=instanceEntity();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_instanceEntity.add(ies.getTree());
            	    if (list_ies==null) list_ies=new ArrayList();
            	    list_ies.add(ies.getTree());


            	    }
            	    break;
            	case 2 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:353:30: ips+= instanceProcess
            	    {
            	    pushFollow(FOLLOW_instanceProcess_in_compBlock2839);
            	    ips=instanceProcess();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_instanceProcess.add(ips.getTree());
            	    if (list_ips==null) list_ips=new ArrayList();
            	    list_ips.add(ips.getTree());


            	    }
            	    break;
            	case 3 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:353:53: iks+= instanceCompartment
            	    {
            	    pushFollow(FOLLOW_instanceCompartment_in_compBlock2846);
            	    iks=instanceCompartment();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_instanceCompartment.add(iks.getTree());
            	    if (list_iks==null) list_iks=new ArrayList();
            	    list_iks.add(iks.getTree());


            	    }
            	    break;

            	default :
            	    break loop38;
                }
            } while (true);

            RBRACE111=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_compBlock2871); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACE.add(RBRACE111);



            // AST REWRITE
            // elements: ies, ips, iks
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: ips, iks, ies
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_ips=new RewriteRuleSubtreeStream(adaptor,"token ips",list_ips);
            RewriteRuleSubtreeStream stream_iks=new RewriteRuleSubtreeStream(adaptor,"token iks",list_iks);
            RewriteRuleSubtreeStream stream_ies=new RewriteRuleSubtreeStream(adaptor,"token ies",list_ies);
            root_0 = (CommonTree)adaptor.nil();
            // 354:55: -> ^( ID[$l, \"ies\"] ( $ies)* ) ^( ID[$l, \"ips\"] ( $ips)* ) ^( ID[$l, \"iks\"] ( $iks)* )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:354:58: ^( ID[$l, \"ies\"] ( $ies)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, l, "ies"), root_1);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:354:74: ( $ies)*
                while ( stream_ies.hasNext() ) {
                    adaptor.addChild(root_1, stream_ies.nextTree());

                }
                stream_ies.reset();

                adaptor.addChild(root_0, root_1);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:354:81: ^( ID[$l, \"ips\"] ( $ips)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, l, "ips"), root_1);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:354:97: ( $ips)*
                while ( stream_ips.hasNext() ) {
                    adaptor.addChild(root_1, stream_ips.nextTree());

                }
                stream_ips.reset();

                adaptor.addChild(root_0, root_1);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:354:104: ^( ID[$l, \"iks\"] ( $iks)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, l, "iks"), root_1);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:354:120: ( $iks)*
                while ( stream_iks.hasNext() ) {
                    adaptor.addChild(root_1, stream_iks.nextTree());

                }
                stream_iks.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "compBlock"

    public static class compBlockNull_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "compBlockNull"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:357:1: compBlockNull : l= LBRACE (ies+= incompleteEntity | ips+= incompleteProcess | iks+= incompleteCompartment )* RBRACE -> ^( ID[$l, \"ies\"] ( $ies)* ) ^( ID[$l, \"ips\"] ( $ips)* ) ^( ID[$l, \"iks\"] ( $iks)* ) ;
    public final PBFParser.compBlockNull_return compBlockNull() throws RecognitionException {
        PBFParser.compBlockNull_return retval = new PBFParser.compBlockNull_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token l=null;
        Token RBRACE112=null;
        List list_ies=null;
        List list_ips=null;
        List list_iks=null;
        RuleReturnScope ies = null;
        RuleReturnScope ips = null;
        RuleReturnScope iks = null;
        CommonTree l_tree=null;
        CommonTree RBRACE112_tree=null;
        RewriteRuleTokenStream stream_RBRACE=new RewriteRuleTokenStream(adaptor,"token RBRACE");
        RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
        RewriteRuleSubtreeStream stream_incompleteEntity=new RewriteRuleSubtreeStream(adaptor,"rule incompleteEntity");
        RewriteRuleSubtreeStream stream_incompleteProcess=new RewriteRuleSubtreeStream(adaptor,"rule incompleteProcess");
        RewriteRuleSubtreeStream stream_incompleteCompartment=new RewriteRuleSubtreeStream(adaptor,"rule incompleteCompartment");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:358:3: (l= LBRACE (ies+= incompleteEntity | ips+= incompleteProcess | iks+= incompleteCompartment )* RBRACE -> ^( ID[$l, \"ies\"] ( $ies)* ) ^( ID[$l, \"ips\"] ( $ips)* ) ^( ID[$l, \"iks\"] ( $iks)* ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:358:5: l= LBRACE (ies+= incompleteEntity | ips+= incompleteProcess | iks+= incompleteCompartment )* RBRACE
            {
            l=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_compBlockNull2958); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACE.add(l);

            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:359:7: (ies+= incompleteEntity | ips+= incompleteProcess | iks+= incompleteCompartment )*
            loop39:
            do {
                int alt39=4;
                switch ( input.LA(1) ) {
                case ENTITY:
                    {
                    alt39=1;
                    }
                    break;
                case PROCESS:
                    {
                    alt39=2;
                    }
                    break;
                case COMPARTMENT:
                    {
                    alt39=3;
                    }
                    break;

                }

                switch (alt39) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:359:8: ies+= incompleteEntity
            	    {
            	    pushFollow(FOLLOW_incompleteEntity_in_compBlockNull2969);
            	    ies=incompleteEntity();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_incompleteEntity.add(ies.getTree());
            	    if (list_ies==null) list_ies=new ArrayList();
            	    list_ies.add(ies.getTree());


            	    }
            	    break;
            	case 2 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:359:32: ips+= incompleteProcess
            	    {
            	    pushFollow(FOLLOW_incompleteProcess_in_compBlockNull2975);
            	    ips=incompleteProcess();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_incompleteProcess.add(ips.getTree());
            	    if (list_ips==null) list_ips=new ArrayList();
            	    list_ips.add(ips.getTree());


            	    }
            	    break;
            	case 3 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:359:57: iks+= incompleteCompartment
            	    {
            	    pushFollow(FOLLOW_incompleteCompartment_in_compBlockNull2982);
            	    iks=incompleteCompartment();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_incompleteCompartment.add(iks.getTree());
            	    if (list_iks==null) list_iks=new ArrayList();
            	    list_iks.add(iks.getTree());


            	    }
            	    break;

            	default :
            	    break loop39;
                }
            } while (true);

            RBRACE112=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_compBlockNull3007); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACE.add(RBRACE112);



            // AST REWRITE
            // elements: iks, ips, ies
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: ips, ies, iks
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_ips=new RewriteRuleSubtreeStream(adaptor,"token ips",list_ips);
            RewriteRuleSubtreeStream stream_ies=new RewriteRuleSubtreeStream(adaptor,"token ies",list_ies);
            RewriteRuleSubtreeStream stream_iks=new RewriteRuleSubtreeStream(adaptor,"token iks",list_iks);
            root_0 = (CommonTree)adaptor.nil();
            // 360:55: -> ^( ID[$l, \"ies\"] ( $ies)* ) ^( ID[$l, \"ips\"] ( $ips)* ) ^( ID[$l, \"iks\"] ( $iks)* )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:360:58: ^( ID[$l, \"ies\"] ( $ies)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, l, "ies"), root_1);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:360:74: ( $ies)*
                while ( stream_ies.hasNext() ) {
                    adaptor.addChild(root_1, stream_ies.nextTree());

                }
                stream_ies.reset();

                adaptor.addChild(root_0, root_1);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:360:81: ^( ID[$l, \"ips\"] ( $ips)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, l, "ips"), root_1);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:360:97: ( $ips)*
                while ( stream_ips.hasNext() ) {
                    adaptor.addChild(root_1, stream_ips.nextTree());

                }
                stream_ips.reset();

                adaptor.addChild(root_0, root_1);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:360:104: ^( ID[$l, \"iks\"] ( $iks)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, l, "iks"), root_1);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:360:120: ( $iks)*
                while ( stream_iks.hasNext() ) {
                    adaptor.addChild(root_1, stream_iks.nextTree());

                }
                stream_iks.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "compBlockNull"

    public static class templateDefs_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "templateDefs"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:365:1: templateDefs : (tes+= templateEntity | tps+= templateProcess | tks+= templateCompartment )* -> ^( ID[\"tes\"] ( $tes)* ) ^( ID[\"tps\"] ( $tps)* ) ^( ID[\"tks\"] ( $tks)* ) ;
    public final PBFParser.templateDefs_return templateDefs() throws RecognitionException {
        PBFParser.templateDefs_return retval = new PBFParser.templateDefs_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        List list_tes=null;
        List list_tps=null;
        List list_tks=null;
        RuleReturnScope tes = null;
        RuleReturnScope tps = null;
        RuleReturnScope tks = null;
        RewriteRuleSubtreeStream stream_templateProcess=new RewriteRuleSubtreeStream(adaptor,"rule templateProcess");
        RewriteRuleSubtreeStream stream_templateEntity=new RewriteRuleSubtreeStream(adaptor,"rule templateEntity");
        RewriteRuleSubtreeStream stream_templateCompartment=new RewriteRuleSubtreeStream(adaptor,"rule templateCompartment");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:366:3: ( (tes+= templateEntity | tps+= templateProcess | tks+= templateCompartment )* -> ^( ID[\"tes\"] ( $tes)* ) ^( ID[\"tps\"] ( $tps)* ) ^( ID[\"tks\"] ( $tks)* ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:366:5: (tes+= templateEntity | tps+= templateProcess | tks+= templateCompartment )*
            {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:366:5: (tes+= templateEntity | tps+= templateProcess | tks+= templateCompartment )*
            loop40:
            do {
                int alt40=4;
                int LA40_0 = input.LA(1);

                if ( (LA40_0==TEMPLATE) ) {
                    switch ( input.LA(2) ) {
                    case ENTITY:
                        {
                        alt40=1;
                        }
                        break;
                    case PROCESS:
                        {
                        alt40=2;
                        }
                        break;
                    case COMPARTMENT:
                        {
                        alt40=3;
                        }
                        break;

                    }

                }


                switch (alt40) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:366:6: tes+= templateEntity
            	    {
            	    pushFollow(FOLLOW_templateEntity_in_templateDefs3098);
            	    tes=templateEntity();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_templateEntity.add(tes.getTree());
            	    if (list_tes==null) list_tes=new ArrayList();
            	    list_tes.add(tes.getTree());


            	    }
            	    break;
            	case 2 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:366:28: tps+= templateProcess
            	    {
            	    pushFollow(FOLLOW_templateProcess_in_templateDefs3104);
            	    tps=templateProcess();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_templateProcess.add(tps.getTree());
            	    if (list_tps==null) list_tps=new ArrayList();
            	    list_tps.add(tps.getTree());


            	    }
            	    break;
            	case 3 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:366:51: tks+= templateCompartment
            	    {
            	    pushFollow(FOLLOW_templateCompartment_in_templateDefs3111);
            	    tks=templateCompartment();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_templateCompartment.add(tks.getTree());
            	    if (list_tks==null) list_tks=new ArrayList();
            	    list_tks.add(tks.getTree());


            	    }
            	    break;

            	default :
            	    break loop40;
                }
            } while (true);



            // AST REWRITE
            // elements: tes, tks, tps
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: tks, tes, tps
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_tks=new RewriteRuleSubtreeStream(adaptor,"token tks",list_tks);
            RewriteRuleSubtreeStream stream_tes=new RewriteRuleSubtreeStream(adaptor,"token tes",list_tes);
            RewriteRuleSubtreeStream stream_tps=new RewriteRuleSubtreeStream(adaptor,"token tps",list_tps);
            root_0 = (CommonTree)adaptor.nil();
            // 366:79: -> ^( ID[\"tes\"] ( $tes)* ) ^( ID[\"tps\"] ( $tps)* ) ^( ID[\"tks\"] ( $tks)* )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:366:82: ^( ID[\"tes\"] ( $tes)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, "tes"), root_1);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:366:94: ( $tes)*
                while ( stream_tes.hasNext() ) {
                    adaptor.addChild(root_1, stream_tes.nextTree());

                }
                stream_tes.reset();

                adaptor.addChild(root_0, root_1);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:366:101: ^( ID[\"tps\"] ( $tps)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, "tps"), root_1);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:366:113: ( $tps)*
                while ( stream_tps.hasNext() ) {
                    adaptor.addChild(root_1, stream_tps.nextTree());

                }
                stream_tps.reset();

                adaptor.addChild(root_0, root_1);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:366:120: ^( ID[\"tks\"] ( $tks)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, "tks"), root_1);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:366:132: ( $tks)*
                while ( stream_tks.hasNext() ) {
                    adaptor.addChild(root_1, stream_tks.nextTree());

                }
                stream_tks.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "templateDefs"

    public static class instanceDefs_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "instanceDefs"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:369:1: instanceDefs : (ies+= instanceEntity | ips+= instanceProcess | iks+= instanceCompartment )* -> ^( ID[\"ies\"] ( $ies)* ) ^( ID[\"ips\"] ( $ips)* ) ^( ID[\"iks\"] ( $iks)* ) ;
    public final PBFParser.instanceDefs_return instanceDefs() throws RecognitionException {
        PBFParser.instanceDefs_return retval = new PBFParser.instanceDefs_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        List list_ies=null;
        List list_ips=null;
        List list_iks=null;
        RuleReturnScope ies = null;
        RuleReturnScope ips = null;
        RuleReturnScope iks = null;
        RewriteRuleSubtreeStream stream_instanceCompartment=new RewriteRuleSubtreeStream(adaptor,"rule instanceCompartment");
        RewriteRuleSubtreeStream stream_instanceProcess=new RewriteRuleSubtreeStream(adaptor,"rule instanceProcess");
        RewriteRuleSubtreeStream stream_instanceEntity=new RewriteRuleSubtreeStream(adaptor,"rule instanceEntity");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:370:3: ( (ies+= instanceEntity | ips+= instanceProcess | iks+= instanceCompartment )* -> ^( ID[\"ies\"] ( $ies)* ) ^( ID[\"ips\"] ( $ips)* ) ^( ID[\"iks\"] ( $iks)* ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:370:5: (ies+= instanceEntity | ips+= instanceProcess | iks+= instanceCompartment )*
            {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:370:5: (ies+= instanceEntity | ips+= instanceProcess | iks+= instanceCompartment )*
            loop41:
            do {
                int alt41=4;
                switch ( input.LA(1) ) {
                case ENTITY:
                    {
                    alt41=1;
                    }
                    break;
                case PROCESS:
                    {
                    alt41=2;
                    }
                    break;
                case COMPARTMENT:
                    {
                    alt41=3;
                    }
                    break;

                }

                switch (alt41) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:370:6: ies+= instanceEntity
            	    {
            	    pushFollow(FOLLOW_instanceEntity_in_instanceDefs3158);
            	    ies=instanceEntity();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_instanceEntity.add(ies.getTree());
            	    if (list_ies==null) list_ies=new ArrayList();
            	    list_ies.add(ies.getTree());


            	    }
            	    break;
            	case 2 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:370:29: ips+= instanceProcess
            	    {
            	    pushFollow(FOLLOW_instanceProcess_in_instanceDefs3165);
            	    ips=instanceProcess();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_instanceProcess.add(ips.getTree());
            	    if (list_ips==null) list_ips=new ArrayList();
            	    list_ips.add(ips.getTree());


            	    }
            	    break;
            	case 3 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:370:52: iks+= instanceCompartment
            	    {
            	    pushFollow(FOLLOW_instanceCompartment_in_instanceDefs3172);
            	    iks=instanceCompartment();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_instanceCompartment.add(iks.getTree());
            	    if (list_iks==null) list_iks=new ArrayList();
            	    list_iks.add(iks.getTree());


            	    }
            	    break;

            	default :
            	    break loop41;
                }
            } while (true);



            // AST REWRITE
            // elements: ips, ies, iks
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: ips, iks, ies
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_ips=new RewriteRuleSubtreeStream(adaptor,"token ips",list_ips);
            RewriteRuleSubtreeStream stream_iks=new RewriteRuleSubtreeStream(adaptor,"token iks",list_iks);
            RewriteRuleSubtreeStream stream_ies=new RewriteRuleSubtreeStream(adaptor,"token ies",list_ies);
            root_0 = (CommonTree)adaptor.nil();
            // 370:81: -> ^( ID[\"ies\"] ( $ies)* ) ^( ID[\"ips\"] ( $ips)* ) ^( ID[\"iks\"] ( $iks)* )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:370:85: ^( ID[\"ies\"] ( $ies)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, "ies"), root_1);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:370:97: ( $ies)*
                while ( stream_ies.hasNext() ) {
                    adaptor.addChild(root_1, stream_ies.nextTree());

                }
                stream_ies.reset();

                adaptor.addChild(root_0, root_1);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:370:104: ^( ID[\"ips\"] ( $ips)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, "ips"), root_1);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:370:116: ( $ips)*
                while ( stream_ips.hasNext() ) {
                    adaptor.addChild(root_1, stream_ips.nextTree());

                }
                stream_ips.reset();

                adaptor.addChild(root_0, root_1);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:370:123: ^( ID[\"iks\"] ( $iks)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, "iks"), root_1);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:370:135: ( $iks)*
                while ( stream_iks.hasNext() ) {
                    adaptor.addChild(root_1, stream_iks.nextTree());

                }
                stream_iks.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "instanceDefs"

    public static class incompleteDefs_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "incompleteDefs"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:373:1: incompleteDefs : (ies+= incompleteEntity | ips+= incompleteProcess | iks+= incompleteCompartment )* -> ^( ID[\"ies\"] ( $ies)* ) ^( ID[\"ips\"] ( $ips)* ) ^( ID[\"iks\"] ( $iks)* ) ;
    public final PBFParser.incompleteDefs_return incompleteDefs() throws RecognitionException {
        PBFParser.incompleteDefs_return retval = new PBFParser.incompleteDefs_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        List list_ies=null;
        List list_ips=null;
        List list_iks=null;
        RuleReturnScope ies = null;
        RuleReturnScope ips = null;
        RuleReturnScope iks = null;
        RewriteRuleSubtreeStream stream_incompleteEntity=new RewriteRuleSubtreeStream(adaptor,"rule incompleteEntity");
        RewriteRuleSubtreeStream stream_incompleteProcess=new RewriteRuleSubtreeStream(adaptor,"rule incompleteProcess");
        RewriteRuleSubtreeStream stream_incompleteCompartment=new RewriteRuleSubtreeStream(adaptor,"rule incompleteCompartment");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:374:3: ( (ies+= incompleteEntity | ips+= incompleteProcess | iks+= incompleteCompartment )* -> ^( ID[\"ies\"] ( $ies)* ) ^( ID[\"ips\"] ( $ips)* ) ^( ID[\"iks\"] ( $iks)* ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:374:5: (ies+= incompleteEntity | ips+= incompleteProcess | iks+= incompleteCompartment )*
            {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:374:5: (ies+= incompleteEntity | ips+= incompleteProcess | iks+= incompleteCompartment )*
            loop42:
            do {
                int alt42=4;
                switch ( input.LA(1) ) {
                case ENTITY:
                    {
                    alt42=1;
                    }
                    break;
                case PROCESS:
                    {
                    alt42=2;
                    }
                    break;
                case COMPARTMENT:
                    {
                    alt42=3;
                    }
                    break;

                }

                switch (alt42) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:374:6: ies+= incompleteEntity
            	    {
            	    pushFollow(FOLLOW_incompleteEntity_in_incompleteDefs3221);
            	    ies=incompleteEntity();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_incompleteEntity.add(ies.getTree());
            	    if (list_ies==null) list_ies=new ArrayList();
            	    list_ies.add(ies.getTree());


            	    }
            	    break;
            	case 2 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:374:31: ips+= incompleteProcess
            	    {
            	    pushFollow(FOLLOW_incompleteProcess_in_incompleteDefs3228);
            	    ips=incompleteProcess();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_incompleteProcess.add(ips.getTree());
            	    if (list_ips==null) list_ips=new ArrayList();
            	    list_ips.add(ips.getTree());


            	    }
            	    break;
            	case 3 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:374:56: iks+= incompleteCompartment
            	    {
            	    pushFollow(FOLLOW_incompleteCompartment_in_incompleteDefs3235);
            	    iks=incompleteCompartment();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_incompleteCompartment.add(iks.getTree());
            	    if (list_iks==null) list_iks=new ArrayList();
            	    list_iks.add(iks.getTree());


            	    }
            	    break;

            	default :
            	    break loop42;
                }
            } while (true);



            // AST REWRITE
            // elements: ips, ies, iks
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: ips, iks, ies
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_ips=new RewriteRuleSubtreeStream(adaptor,"token ips",list_ips);
            RewriteRuleSubtreeStream stream_iks=new RewriteRuleSubtreeStream(adaptor,"token iks",list_iks);
            RewriteRuleSubtreeStream stream_ies=new RewriteRuleSubtreeStream(adaptor,"token ies",list_ies);
            root_0 = (CommonTree)adaptor.nil();
            // 374:87: -> ^( ID[\"ies\"] ( $ies)* ) ^( ID[\"ips\"] ( $ips)* ) ^( ID[\"iks\"] ( $iks)* )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:374:91: ^( ID[\"ies\"] ( $ies)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, "ies"), root_1);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:374:103: ( $ies)*
                while ( stream_ies.hasNext() ) {
                    adaptor.addChild(root_1, stream_ies.nextTree());

                }
                stream_ies.reset();

                adaptor.addChild(root_0, root_1);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:374:110: ^( ID[\"ips\"] ( $ips)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, "ips"), root_1);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:374:122: ( $ips)*
                while ( stream_ips.hasNext() ) {
                    adaptor.addChild(root_1, stream_ips.nextTree());

                }
                stream_ips.reset();

                adaptor.addChild(root_0, root_1);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:374:129: ^( ID[\"iks\"] ( $iks)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, "iks"), root_1);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:374:141: ( $iks)*
                while ( stream_iks.hasNext() ) {
                    adaptor.addChild(root_1, stream_iks.nextTree());

                }
                stream_iks.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "incompleteDefs"

    public static class fullDefs_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "fullDefs"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:377:1: fullDefs : (fes+= fullEntity | fps+= fullProcess )* -> ^( ID[\"fes\"] ( $fes)* ) ^( ID[\"fps\"] ( $fps)* ) ;
    public final PBFParser.fullDefs_return fullDefs() throws RecognitionException {
        PBFParser.fullDefs_return retval = new PBFParser.fullDefs_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        List list_fes=null;
        List list_fps=null;
        RuleReturnScope fes = null;
        RuleReturnScope fps = null;
        RewriteRuleSubtreeStream stream_fullProcess=new RewriteRuleSubtreeStream(adaptor,"rule fullProcess");
        RewriteRuleSubtreeStream stream_fullEntity=new RewriteRuleSubtreeStream(adaptor,"rule fullEntity");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:378:3: ( (fes+= fullEntity | fps+= fullProcess )* -> ^( ID[\"fes\"] ( $fes)* ) ^( ID[\"fps\"] ( $fps)* ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:378:5: (fes+= fullEntity | fps+= fullProcess )*
            {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:378:5: (fes+= fullEntity | fps+= fullProcess )*
            loop43:
            do {
                int alt43=3;
                int LA43_0 = input.LA(1);

                if ( (LA43_0==ENTITY) ) {
                    alt43=1;
                }
                else if ( (LA43_0==PROCESS) ) {
                    alt43=2;
                }


                switch (alt43) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:378:6: fes+= fullEntity
            	    {
            	    pushFollow(FOLLOW_fullEntity_in_fullDefs3284);
            	    fes=fullEntity();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_fullEntity.add(fes.getTree());
            	    if (list_fes==null) list_fes=new ArrayList();
            	    list_fes.add(fes.getTree());


            	    }
            	    break;
            	case 2 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:378:24: fps+= fullProcess
            	    {
            	    pushFollow(FOLLOW_fullProcess_in_fullDefs3290);
            	    fps=fullProcess();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_fullProcess.add(fps.getTree());
            	    if (list_fps==null) list_fps=new ArrayList();
            	    list_fps.add(fps.getTree());


            	    }
            	    break;

            	default :
            	    break loop43;
                }
            } while (true);



            // AST REWRITE
            // elements: fps, fes
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: fes, fps
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_fes=new RewriteRuleSubtreeStream(adaptor,"token fes",list_fes);
            RewriteRuleSubtreeStream stream_fps=new RewriteRuleSubtreeStream(adaptor,"token fps",list_fps);
            root_0 = (CommonTree)adaptor.nil();
            // 378:69: -> ^( ID[\"fes\"] ( $fes)* ) ^( ID[\"fps\"] ( $fps)* )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:378:73: ^( ID[\"fes\"] ( $fes)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, "fes"), root_1);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:378:85: ( $fes)*
                while ( stream_fes.hasNext() ) {
                    adaptor.addChild(root_1, stream_fes.nextTree());

                }
                stream_fes.reset();

                adaptor.addChild(root_0, root_1);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:378:92: ^( ID[\"fps\"] ( $fps)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, "fps"), root_1);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:378:104: ( $fps)*
                while ( stream_fps.hasNext() ) {
                    adaptor.addChild(root_1, stream_fps.nextTree());

                }
                stream_fps.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "fullDefs"

    public static class mixedDefs_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "mixedDefs"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:381:1: mixedDefs : (mes+= mixedEntity | mps+= mixedProcess )* -> ^( ID[\"mes\"] ( $mes)* ) ^( ID[\"mps\"] ( $mps)* ) ;
    public final PBFParser.mixedDefs_return mixedDefs() throws RecognitionException {
        PBFParser.mixedDefs_return retval = new PBFParser.mixedDefs_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        List list_mes=null;
        List list_mps=null;
        RuleReturnScope mes = null;
        RuleReturnScope mps = null;
        RewriteRuleSubtreeStream stream_mixedEntity=new RewriteRuleSubtreeStream(adaptor,"rule mixedEntity");
        RewriteRuleSubtreeStream stream_mixedProcess=new RewriteRuleSubtreeStream(adaptor,"rule mixedProcess");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:382:3: ( (mes+= mixedEntity | mps+= mixedProcess )* -> ^( ID[\"mes\"] ( $mes)* ) ^( ID[\"mps\"] ( $mps)* ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:382:5: (mes+= mixedEntity | mps+= mixedProcess )*
            {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:382:5: (mes+= mixedEntity | mps+= mixedProcess )*
            loop44:
            do {
                int alt44=3;
                int LA44_0 = input.LA(1);

                if ( (LA44_0==ENTITY) ) {
                    alt44=1;
                }
                else if ( (LA44_0==PROCESS) ) {
                    alt44=2;
                }


                switch (alt44) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:382:6: mes+= mixedEntity
            	    {
            	    pushFollow(FOLLOW_mixedEntity_in_mixedDefs3355);
            	    mes=mixedEntity();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_mixedEntity.add(mes.getTree());
            	    if (list_mes==null) list_mes=new ArrayList();
            	    list_mes.add(mes.getTree());


            	    }
            	    break;
            	case 2 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:382:25: mps+= mixedProcess
            	    {
            	    pushFollow(FOLLOW_mixedProcess_in_mixedDefs3361);
            	    mps=mixedProcess();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_mixedProcess.add(mps.getTree());
            	    if (list_mps==null) list_mps=new ArrayList();
            	    list_mps.add(mps.getTree());


            	    }
            	    break;

            	default :
            	    break loop44;
                }
            } while (true);



            // AST REWRITE
            // elements: mps, mes
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: mes, mps
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_mes=new RewriteRuleSubtreeStream(adaptor,"token mes",list_mes);
            RewriteRuleSubtreeStream stream_mps=new RewriteRuleSubtreeStream(adaptor,"token mps",list_mps);
            root_0 = (CommonTree)adaptor.nil();
            // 382:67: -> ^( ID[\"mes\"] ( $mes)* ) ^( ID[\"mps\"] ( $mps)* )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:382:71: ^( ID[\"mes\"] ( $mes)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, "mes"), root_1);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:382:83: ( $mes)*
                while ( stream_mes.hasNext() ) {
                    adaptor.addChild(root_1, stream_mes.nextTree());

                }
                stream_mes.reset();

                adaptor.addChild(root_0, root_1);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:382:90: ^( ID[\"mps\"] ( $mps)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, "mps"), root_1);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:382:102: ( $mps)*
                while ( stream_mps.hasNext() ) {
                    adaptor.addChild(root_1, stream_mps.nextTree());

                }
                stream_mps.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "mixedDefs"

    public static class libDecl_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "libDecl"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:415:1: libDecl : LIBRARY ID SEMI -> ID ;
    public final PBFParser.libDecl_return libDecl() throws RecognitionException {
        PBFParser.libDecl_return retval = new PBFParser.libDecl_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LIBRARY113=null;
        Token ID114=null;
        Token SEMI115=null;

        CommonTree LIBRARY113_tree=null;
        CommonTree ID114_tree=null;
        CommonTree SEMI115_tree=null;
        RewriteRuleTokenStream stream_LIBRARY=new RewriteRuleTokenStream(adaptor,"token LIBRARY");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_SEMI=new RewriteRuleTokenStream(adaptor,"token SEMI");

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:416:3: ( LIBRARY ID SEMI -> ID )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:416:5: LIBRARY ID SEMI
            {
            LIBRARY113=(Token)match(input,LIBRARY,FOLLOW_LIBRARY_in_libDecl3427); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LIBRARY.add(LIBRARY113);

            ID114=(Token)match(input,ID,FOLLOW_ID_in_libDecl3429); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(ID114);

            SEMI115=(Token)match(input,SEMI,FOLLOW_SEMI_in_libDecl3431); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_SEMI.add(SEMI115);



            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 416:55: -> ID
            {
                adaptor.addChild(root_0, stream_ID.nextNode());

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "libDecl"

    public static class modelDecl_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "modelDecl"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:419:1: modelDecl : MODEL modelID= ID c= COLON libID= ID SEMI -> $modelID ^( ID[$c, \"template\"] $libID) ;
    public final PBFParser.modelDecl_return modelDecl() throws RecognitionException {
        PBFParser.modelDecl_return retval = new PBFParser.modelDecl_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token modelID=null;
        Token c=null;
        Token libID=null;
        Token MODEL116=null;
        Token SEMI117=null;

        CommonTree modelID_tree=null;
        CommonTree c_tree=null;
        CommonTree libID_tree=null;
        CommonTree MODEL116_tree=null;
        CommonTree SEMI117_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_MODEL=new RewriteRuleTokenStream(adaptor,"token MODEL");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_SEMI=new RewriteRuleTokenStream(adaptor,"token SEMI");

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:420:3: ( MODEL modelID= ID c= COLON libID= ID SEMI -> $modelID ^( ID[$c, \"template\"] $libID) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:420:5: MODEL modelID= ID c= COLON libID= ID SEMI
            {
            MODEL116=(Token)match(input,MODEL,FOLLOW_MODEL_in_modelDecl3482); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_MODEL.add(MODEL116);

            modelID=(Token)match(input,ID,FOLLOW_ID_in_modelDecl3486); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(modelID);

            c=(Token)match(input,COLON,FOLLOW_COLON_in_modelDecl3490); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COLON.add(c);

            libID=(Token)match(input,ID,FOLLOW_ID_in_modelDecl3494); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(libID);

            SEMI117=(Token)match(input,SEMI,FOLLOW_SEMI_in_modelDecl3496); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_SEMI.add(SEMI117);



            // AST REWRITE
            // elements: modelID, libID, ID
            // token labels: modelID, libID
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_modelID=new RewriteRuleTokenStream(adaptor,"token modelID",modelID);
            RewriteRuleTokenStream stream_libID=new RewriteRuleTokenStream(adaptor,"token libID",libID);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 420:65: -> $modelID ^( ID[$c, \"template\"] $libID)
            {
                adaptor.addChild(root_0, stream_modelID.nextNode());
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:420:77: ^( ID[$c, \"template\"] $libID)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, c, "template"), root_1);

                adaptor.addChild(root_1, stream_libID.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "modelDecl"

    public static class incompleteModelDecl_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "incompleteModelDecl"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:423:1: incompleteModelDecl : INCOMPLETE MODEL modelID= ID c= COLON libID= ID SEMI -> $modelID ^( ID[$c, \"template\"] $libID) ;
    public final PBFParser.incompleteModelDecl_return incompleteModelDecl() throws RecognitionException {
        PBFParser.incompleteModelDecl_return retval = new PBFParser.incompleteModelDecl_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token modelID=null;
        Token c=null;
        Token libID=null;
        Token INCOMPLETE118=null;
        Token MODEL119=null;
        Token SEMI120=null;

        CommonTree modelID_tree=null;
        CommonTree c_tree=null;
        CommonTree libID_tree=null;
        CommonTree INCOMPLETE118_tree=null;
        CommonTree MODEL119_tree=null;
        CommonTree SEMI120_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_MODEL=new RewriteRuleTokenStream(adaptor,"token MODEL");
        RewriteRuleTokenStream stream_INCOMPLETE=new RewriteRuleTokenStream(adaptor,"token INCOMPLETE");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_SEMI=new RewriteRuleTokenStream(adaptor,"token SEMI");

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:424:3: ( INCOMPLETE MODEL modelID= ID c= COLON libID= ID SEMI -> $modelID ^( ID[$c, \"template\"] $libID) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:424:5: INCOMPLETE MODEL modelID= ID c= COLON libID= ID SEMI
            {
            INCOMPLETE118=(Token)match(input,INCOMPLETE,FOLLOW_INCOMPLETE_in_incompleteModelDecl3543); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_INCOMPLETE.add(INCOMPLETE118);

            MODEL119=(Token)match(input,MODEL,FOLLOW_MODEL_in_incompleteModelDecl3545); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_MODEL.add(MODEL119);

            modelID=(Token)match(input,ID,FOLLOW_ID_in_incompleteModelDecl3549); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(modelID);

            c=(Token)match(input,COLON,FOLLOW_COLON_in_incompleteModelDecl3553); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COLON.add(c);

            libID=(Token)match(input,ID,FOLLOW_ID_in_incompleteModelDecl3557); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(libID);

            SEMI120=(Token)match(input,SEMI,FOLLOW_SEMI_in_incompleteModelDecl3559); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_SEMI.add(SEMI120);



            // AST REWRITE
            // elements: ID, libID, modelID
            // token labels: modelID, libID
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_modelID=new RewriteRuleTokenStream(adaptor,"token modelID",modelID);
            RewriteRuleTokenStream stream_libID=new RewriteRuleTokenStream(adaptor,"token libID",libID);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 424:67: -> $modelID ^( ID[$c, \"template\"] $libID)
            {
                adaptor.addChild(root_0, stream_modelID.nextNode());
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:424:79: ^( ID[$c, \"template\"] $libID)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, c, "template"), root_1);

                adaptor.addChild(root_1, stream_libID.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "incompleteModelDecl"

    public static class fullModelDecl_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "fullModelDecl"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:427:1: fullModelDecl : MODEL ID c= COLON UNSPECIFIED SEMI -> ID ^( ID[$c, \"template\"] UNSPECIFIED ) ;
    public final PBFParser.fullModelDecl_return fullModelDecl() throws RecognitionException {
        PBFParser.fullModelDecl_return retval = new PBFParser.fullModelDecl_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token c=null;
        Token MODEL121=null;
        Token ID122=null;
        Token UNSPECIFIED123=null;
        Token SEMI124=null;

        CommonTree c_tree=null;
        CommonTree MODEL121_tree=null;
        CommonTree ID122_tree=null;
        CommonTree UNSPECIFIED123_tree=null;
        CommonTree SEMI124_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_MODEL=new RewriteRuleTokenStream(adaptor,"token MODEL");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_UNSPECIFIED=new RewriteRuleTokenStream(adaptor,"token UNSPECIFIED");
        RewriteRuleTokenStream stream_SEMI=new RewriteRuleTokenStream(adaptor,"token SEMI");

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:428:3: ( MODEL ID c= COLON UNSPECIFIED SEMI -> ID ^( ID[$c, \"template\"] UNSPECIFIED ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:428:5: MODEL ID c= COLON UNSPECIFIED SEMI
            {
            MODEL121=(Token)match(input,MODEL,FOLLOW_MODEL_in_fullModelDecl3597); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_MODEL.add(MODEL121);

            ID122=(Token)match(input,ID,FOLLOW_ID_in_fullModelDecl3599); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(ID122);

            c=(Token)match(input,COLON,FOLLOW_COLON_in_fullModelDecl3603); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COLON.add(c);

            UNSPECIFIED123=(Token)match(input,UNSPECIFIED,FOLLOW_UNSPECIFIED_in_fullModelDecl3605); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_UNSPECIFIED.add(UNSPECIFIED123);

            SEMI124=(Token)match(input,SEMI,FOLLOW_SEMI_in_fullModelDecl3607); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_SEMI.add(SEMI124);



            // AST REWRITE
            // elements: UNSPECIFIED, ID, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 428:61: -> ID ^( ID[$c, \"template\"] UNSPECIFIED )
            {
                adaptor.addChild(root_0, stream_ID.nextNode());
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:428:67: ^( ID[$c, \"template\"] UNSPECIFIED )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, c, "template"), root_1);

                adaptor.addChild(root_1, stream_UNSPECIFIED.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "fullModelDecl"

    public static class mixedModelDecl_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "mixedModelDecl"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:431:1: mixedModelDecl : MODEL modelID= ID c= COLON UNSPECIFIED libID= ID SEMI -> $modelID ^( ID[$c, \"template\"] $libID) ;
    public final PBFParser.mixedModelDecl_return mixedModelDecl() throws RecognitionException {
        PBFParser.mixedModelDecl_return retval = new PBFParser.mixedModelDecl_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token modelID=null;
        Token c=null;
        Token libID=null;
        Token MODEL125=null;
        Token UNSPECIFIED126=null;
        Token SEMI127=null;

        CommonTree modelID_tree=null;
        CommonTree c_tree=null;
        CommonTree libID_tree=null;
        CommonTree MODEL125_tree=null;
        CommonTree UNSPECIFIED126_tree=null;
        CommonTree SEMI127_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_MODEL=new RewriteRuleTokenStream(adaptor,"token MODEL");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_UNSPECIFIED=new RewriteRuleTokenStream(adaptor,"token UNSPECIFIED");
        RewriteRuleTokenStream stream_SEMI=new RewriteRuleTokenStream(adaptor,"token SEMI");

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:432:3: ( MODEL modelID= ID c= COLON UNSPECIFIED libID= ID SEMI -> $modelID ^( ID[$c, \"template\"] $libID) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:432:5: MODEL modelID= ID c= COLON UNSPECIFIED libID= ID SEMI
            {
            MODEL125=(Token)match(input,MODEL,FOLLOW_MODEL_in_mixedModelDecl3653); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_MODEL.add(MODEL125);

            modelID=(Token)match(input,ID,FOLLOW_ID_in_mixedModelDecl3657); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(modelID);

            c=(Token)match(input,COLON,FOLLOW_COLON_in_mixedModelDecl3661); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COLON.add(c);

            UNSPECIFIED126=(Token)match(input,UNSPECIFIED,FOLLOW_UNSPECIFIED_in_mixedModelDecl3663); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_UNSPECIFIED.add(UNSPECIFIED126);

            libID=(Token)match(input,ID,FOLLOW_ID_in_mixedModelDecl3667); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(libID);

            SEMI127=(Token)match(input,SEMI,FOLLOW_SEMI_in_mixedModelDecl3669); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_SEMI.add(SEMI127);



            // AST REWRITE
            // elements: libID, ID, modelID
            // token labels: modelID, libID
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_modelID=new RewriteRuleTokenStream(adaptor,"token modelID",modelID);
            RewriteRuleTokenStream stream_libID=new RewriteRuleTokenStream(adaptor,"token libID",libID);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 432:69: -> $modelID ^( ID[$c, \"template\"] $libID)
            {
                adaptor.addChild(root_0, stream_modelID.nextNode());
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:432:81: ^( ID[$c, \"template\"] $libID)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, c, "template"), root_1);

                adaptor.addChild(root_1, stream_libID.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "mixedModelDecl"

    public static class libFile_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "libFile"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:437:1: libFile : libDecl templateDefs -> ^( FILE libDecl templateDefs ) ;
    public final PBFParser.libFile_return libFile() throws RecognitionException {
        PBFParser.libFile_return retval = new PBFParser.libFile_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        PBFParser.libDecl_return libDecl128 = null;

        PBFParser.templateDefs_return templateDefs129 = null;


        RewriteRuleSubtreeStream stream_templateDefs=new RewriteRuleSubtreeStream(adaptor,"rule templateDefs");
        RewriteRuleSubtreeStream stream_libDecl=new RewriteRuleSubtreeStream(adaptor,"rule libDecl");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:438:3: ( libDecl templateDefs -> ^( FILE libDecl templateDefs ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:438:5: libDecl templateDefs
            {
            pushFollow(FOLLOW_libDecl_in_libFile3711);
            libDecl128=libDecl();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_libDecl.add(libDecl128.getTree());
            pushFollow(FOLLOW_templateDefs_in_libFile3717);
            templateDefs129=templateDefs();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_templateDefs.add(templateDefs129.getTree());


            // AST REWRITE
            // elements: templateDefs, libDecl
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 439:55: -> ^( FILE libDecl templateDefs )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:439:58: ^( FILE libDecl templateDefs )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FILE, "FILE"), root_1);

                adaptor.addChild(root_1, stream_libDecl.nextTree());
                adaptor.addChild(root_1, stream_templateDefs.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "libFile"

    public static class modelFile_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "modelFile"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:442:1: modelFile : modelDecl instanceDefs -> ^( FILE modelDecl instanceDefs ) ;
    public final PBFParser.modelFile_return modelFile() throws RecognitionException {
        PBFParser.modelFile_return retval = new PBFParser.modelFile_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        PBFParser.modelDecl_return modelDecl130 = null;

        PBFParser.instanceDefs_return instanceDefs131 = null;


        RewriteRuleSubtreeStream stream_instanceDefs=new RewriteRuleSubtreeStream(adaptor,"rule instanceDefs");
        RewriteRuleSubtreeStream stream_modelDecl=new RewriteRuleSubtreeStream(adaptor,"rule modelDecl");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:443:3: ( modelDecl instanceDefs -> ^( FILE modelDecl instanceDefs ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:443:5: modelDecl instanceDefs
            {
            pushFollow(FOLLOW_modelDecl_in_modelFile3777);
            modelDecl130=modelDecl();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_modelDecl.add(modelDecl130.getTree());
            pushFollow(FOLLOW_instanceDefs_in_modelFile3783);
            instanceDefs131=instanceDefs();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_instanceDefs.add(instanceDefs131.getTree());


            // AST REWRITE
            // elements: modelDecl, instanceDefs
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 444:55: -> ^( FILE modelDecl instanceDefs )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:444:58: ^( FILE modelDecl instanceDefs )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FILE, "FILE"), root_1);

                adaptor.addChild(root_1, stream_modelDecl.nextTree());
                adaptor.addChild(root_1, stream_instanceDefs.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "modelFile"

    public static class incompleteFile_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "incompleteFile"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:447:1: incompleteFile : incompleteModelDecl incompleteDefs ;
    public final PBFParser.incompleteFile_return incompleteFile() throws RecognitionException {
        PBFParser.incompleteFile_return retval = new PBFParser.incompleteFile_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        PBFParser.incompleteModelDecl_return incompleteModelDecl132 = null;

        PBFParser.incompleteDefs_return incompleteDefs133 = null;



        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:448:3: ( incompleteModelDecl incompleteDefs )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:448:5: incompleteModelDecl incompleteDefs
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_incompleteModelDecl_in_incompleteFile3845);
            incompleteModelDecl132=incompleteModelDecl();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, incompleteModelDecl132.getTree());
            pushFollow(FOLLOW_incompleteDefs_in_incompleteFile3851);
            incompleteDefs133=incompleteDefs();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, incompleteDefs133.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "incompleteFile"

    public static class fullFile_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "fullFile"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:452:1: fullFile : fullModelDecl fullDefs -> ^( FILE fullModelDecl fullDefs ) ;
    public final PBFParser.fullFile_return fullFile() throws RecognitionException {
        PBFParser.fullFile_return retval = new PBFParser.fullFile_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        PBFParser.fullModelDecl_return fullModelDecl134 = null;

        PBFParser.fullDefs_return fullDefs135 = null;


        RewriteRuleSubtreeStream stream_fullModelDecl=new RewriteRuleSubtreeStream(adaptor,"rule fullModelDecl");
        RewriteRuleSubtreeStream stream_fullDefs=new RewriteRuleSubtreeStream(adaptor,"rule fullDefs");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:453:3: ( fullModelDecl fullDefs -> ^( FILE fullModelDecl fullDefs ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:453:5: fullModelDecl fullDefs
            {
            pushFollow(FOLLOW_fullModelDecl_in_fullFile3864);
            fullModelDecl134=fullModelDecl();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_fullModelDecl.add(fullModelDecl134.getTree());
            pushFollow(FOLLOW_fullDefs_in_fullFile3870);
            fullDefs135=fullDefs();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_fullDefs.add(fullDefs135.getTree());


            // AST REWRITE
            // elements: fullModelDecl, fullDefs
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 454:55: -> ^( FILE fullModelDecl fullDefs )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:454:58: ^( FILE fullModelDecl fullDefs )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FILE, "FILE"), root_1);

                adaptor.addChild(root_1, stream_fullModelDecl.nextTree());
                adaptor.addChild(root_1, stream_fullDefs.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "fullFile"

    public static class mixedFile_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "mixedFile"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:457:1: mixedFile : mixedModelDecl mixedDefs -> ^( FILE mixedModelDecl mixedDefs ) ;
    public final PBFParser.mixedFile_return mixedFile() throws RecognitionException {
        PBFParser.mixedFile_return retval = new PBFParser.mixedFile_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        PBFParser.mixedModelDecl_return mixedModelDecl136 = null;

        PBFParser.mixedDefs_return mixedDefs137 = null;


        RewriteRuleSubtreeStream stream_mixedModelDecl=new RewriteRuleSubtreeStream(adaptor,"rule mixedModelDecl");
        RewriteRuleSubtreeStream stream_mixedDefs=new RewriteRuleSubtreeStream(adaptor,"rule mixedDefs");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:458:3: ( mixedModelDecl mixedDefs -> ^( FILE mixedModelDecl mixedDefs ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:458:5: mixedModelDecl mixedDefs
            {
            pushFollow(FOLLOW_mixedModelDecl_in_mixedFile3934);
            mixedModelDecl136=mixedModelDecl();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_mixedModelDecl.add(mixedModelDecl136.getTree());
            pushFollow(FOLLOW_mixedDefs_in_mixedFile3940);
            mixedDefs137=mixedDefs();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_mixedDefs.add(mixedDefs137.getTree());


            // AST REWRITE
            // elements: mixedDefs, mixedModelDecl
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 459:53: -> ^( FILE mixedModelDecl mixedDefs )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:459:56: ^( FILE mixedModelDecl mixedDefs )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FILE, "FILE"), root_1);

                adaptor.addChild(root_1, stream_mixedModelDecl.nextTree());
                adaptor.addChild(root_1, stream_mixedDefs.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "mixedFile"

    public static class header_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "header"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:464:1: header : ( libDecl | modelDecl | incompleteModelDecl | fullModelDecl | mixedModelDecl );
    public final PBFParser.header_return header() throws RecognitionException {
        PBFParser.header_return retval = new PBFParser.header_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        PBFParser.libDecl_return libDecl138 = null;

        PBFParser.modelDecl_return modelDecl139 = null;

        PBFParser.incompleteModelDecl_return incompleteModelDecl140 = null;

        PBFParser.fullModelDecl_return fullModelDecl141 = null;

        PBFParser.mixedModelDecl_return mixedModelDecl142 = null;



        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:465:3: ( libDecl | modelDecl | incompleteModelDecl | fullModelDecl | mixedModelDecl )
            int alt45=5;
            alt45 = dfa45.predict(input);
            switch (alt45) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:465:5: libDecl
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_libDecl_in_header4005);
                    libDecl138=libDecl();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, libDecl138.getTree());
                    if ( state.backtracking==0 ) {
                      fileType = Type.LIBRARY;
                    }

                    }
                    break;
                case 2 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:466:5: modelDecl
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_modelDecl_in_header4021);
                    modelDecl139=modelDecl();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, modelDecl139.getTree());
                    if ( state.backtracking==0 ) {
                      fileType = Type.MODEL;
                    }

                    }
                    break;
                case 3 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:467:5: incompleteModelDecl
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_incompleteModelDecl_in_header4035);
                    incompleteModelDecl140=incompleteModelDecl();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, incompleteModelDecl140.getTree());
                    if ( state.backtracking==0 ) {
                      fileType = Type.INCOMPLETE_MODEL;
                    }

                    }
                    break;
                case 4 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:468:5: fullModelDecl
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_fullModelDecl_in_header4043);
                    fullModelDecl141=fullModelDecl();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, fullModelDecl141.getTree());
                    if ( state.backtracking==0 ) {
                      fileType = Type.FULL_MODEL;
                    }

                    }
                    break;
                case 5 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:469:5: mixedModelDecl
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_mixedModelDecl_in_header4055);
                    mixedModelDecl142=mixedModelDecl();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, mixedModelDecl142.getTree());
                    if ( state.backtracking==0 ) {
                      fileType = Type.MIXED_MODEL;
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "header"

    public static class defs_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "defs"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:472:1: defs : ({...}? => templateDefs | {...}? => instanceDefs | {...}? => incompleteDefs | {...}? => fullDefs | {...}? => mixedDefs );
    public final PBFParser.defs_return defs() throws RecognitionException {
        PBFParser.defs_return retval = new PBFParser.defs_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        PBFParser.templateDefs_return templateDefs143 = null;

        PBFParser.instanceDefs_return instanceDefs144 = null;

        PBFParser.incompleteDefs_return incompleteDefs145 = null;

        PBFParser.fullDefs_return fullDefs146 = null;

        PBFParser.mixedDefs_return mixedDefs147 = null;



        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:473:3: ({...}? => templateDefs | {...}? => instanceDefs | {...}? => incompleteDefs | {...}? => fullDefs | {...}? => mixedDefs )
            int alt46=5;
            alt46 = dfa46.predict(input);
            switch (alt46) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:473:5: {...}? => templateDefs
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    if ( !(((fileType == Type.LIBRARY))) ) {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        throw new FailedPredicateException(input, "defs", "(fileType == Type.LIBRARY)");
                    }
                    pushFollow(FOLLOW_templateDefs_in_defs4084);
                    templateDefs143=templateDefs();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, templateDefs143.getTree());

                    }
                    break;
                case 2 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:474:5: {...}? => instanceDefs
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    if ( !(((fileType == Type.MODEL))) ) {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        throw new FailedPredicateException(input, "defs", "(fileType == Type.MODEL)");
                    }
                    pushFollow(FOLLOW_instanceDefs_in_defs4101);
                    instanceDefs144=instanceDefs();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, instanceDefs144.getTree());

                    }
                    break;
                case 3 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:475:5: {...}? => incompleteDefs
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    if ( !(((fileType == Type.INCOMPLETE_MODEL))) ) {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        throw new FailedPredicateException(input, "defs", "(fileType == Type.INCOMPLETE_MODEL)");
                    }
                    pushFollow(FOLLOW_incompleteDefs_in_defs4111);
                    incompleteDefs145=incompleteDefs();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, incompleteDefs145.getTree());

                    }
                    break;
                case 4 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:476:5: {...}? => fullDefs
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    if ( !(((fileType == Type.FULL_MODEL))) ) {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        throw new FailedPredicateException(input, "defs", "(fileType == Type.FULL_MODEL)");
                    }
                    pushFollow(FOLLOW_fullDefs_in_defs4125);
                    fullDefs146=fullDefs();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, fullDefs146.getTree());

                    }
                    break;
                case 5 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:477:5: {...}? => mixedDefs
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    if ( !(((fileType == Type.MIXED_MODEL))) ) {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        throw new FailedPredicateException(input, "defs", "(fileType == Type.MIXED_MODEL)");
                    }
                    pushFollow(FOLLOW_mixedDefs_in_defs4138);
                    mixedDefs147=mixedDefs();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, mixedDefs147.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "defs"

    public static class file_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "file"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:480:1: file : header defs EOF -> ^( STRUCT[fileType.toString()] header defs ) ;
    public final PBFParser.file_return file() throws RecognitionException {
        PBFParser.file_return retval = new PBFParser.file_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token EOF150=null;
        PBFParser.header_return header148 = null;

        PBFParser.defs_return defs149 = null;


        CommonTree EOF150_tree=null;
        RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
        RewriteRuleSubtreeStream stream_defs=new RewriteRuleSubtreeStream(adaptor,"rule defs");
        RewriteRuleSubtreeStream stream_header=new RewriteRuleSubtreeStream(adaptor,"rule header");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:481:3: ( header defs EOF -> ^( STRUCT[fileType.toString()] header defs ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:481:5: header defs EOF
            {
            pushFollow(FOLLOW_header_in_file4151);
            header148=header();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_header.add(header148.getTree());
            pushFollow(FOLLOW_defs_in_file4157);
            defs149=defs();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_defs.add(defs149.getTree());
            EOF150=(Token)match(input,EOF,FOLLOW_EOF_in_file4161); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_EOF.add(EOF150);



            // AST REWRITE
            // elements: header, defs
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 482:61: -> ^( STRUCT[fileType.toString()] header defs )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:482:64: ^( STRUCT[fileType.toString()] header defs )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRUCT, fileType.toString()), root_1);

                adaptor.addChild(root_1, stream_header.nextTree());
                adaptor.addChild(root_1, stream_defs.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "file"

    public static class expression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expression"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:488:1: expression : assignmentExpression ;
    public final PBFParser.expression_return expression() throws RecognitionException {
        PBFParser.expression_return retval = new PBFParser.expression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        PBFParser.assignmentExpression_return assignmentExpression151 = null;



        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:489:3: ( assignmentExpression )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:489:5: assignmentExpression
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_assignmentExpression_in_expression4213);
            assignmentExpression151=assignmentExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, assignmentExpression151.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "expression"

    public static class assignmentExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "assignmentExpression"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:492:1: assignmentExpression : eqExpression ( EQ assignmentExpression )? ;
    public final PBFParser.assignmentExpression_return assignmentExpression() throws RecognitionException {
        PBFParser.assignmentExpression_return retval = new PBFParser.assignmentExpression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token EQ153=null;
        PBFParser.eqExpression_return eqExpression152 = null;

        PBFParser.assignmentExpression_return assignmentExpression154 = null;


        CommonTree EQ153_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:493:3: ( eqExpression ( EQ assignmentExpression )? )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:493:5: eqExpression ( EQ assignmentExpression )?
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_eqExpression_in_assignmentExpression4226);
            eqExpression152=eqExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, eqExpression152.getTree());
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:493:18: ( EQ assignmentExpression )?
            int alt47=2;
            int LA47_0 = input.LA(1);

            if ( (LA47_0==EQ) ) {
                alt47=1;
            }
            switch (alt47) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:493:19: EQ assignmentExpression
                    {
                    EQ153=(Token)match(input,EQ,FOLLOW_EQ_in_assignmentExpression4229); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    EQ153_tree = (CommonTree)adaptor.create(EQ153);
                    root_0 = (CommonTree)adaptor.becomeRoot(EQ153_tree, root_0);
                    }
                    pushFollow(FOLLOW_assignmentExpression_in_assignmentExpression4232);
                    assignmentExpression154=assignmentExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, assignmentExpression154.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "assignmentExpression"

    public static class eqExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "eqExpression"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:496:1: eqExpression : relExpression ( eqOperator relExpression )* ;
    public final PBFParser.eqExpression_return eqExpression() throws RecognitionException {
        PBFParser.eqExpression_return retval = new PBFParser.eqExpression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        PBFParser.relExpression_return relExpression155 = null;

        PBFParser.eqOperator_return eqOperator156 = null;

        PBFParser.relExpression_return relExpression157 = null;



        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:497:3: ( relExpression ( eqOperator relExpression )* )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:497:5: relExpression ( eqOperator relExpression )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_relExpression_in_eqExpression4247);
            relExpression155=relExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, relExpression155.getTree());
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:497:19: ( eqOperator relExpression )*
            loop48:
            do {
                int alt48=2;
                int LA48_0 = input.LA(1);

                if ( ((LA48_0>=EQEQ && LA48_0<=BANGEQ)) ) {
                    alt48=1;
                }


                switch (alt48) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:497:20: eqOperator relExpression
            	    {
            	    pushFollow(FOLLOW_eqOperator_in_eqExpression4250);
            	    eqOperator156=eqOperator();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(eqOperator156.getTree(), root_0);
            	    pushFollow(FOLLOW_relExpression_in_eqExpression4253);
            	    relExpression157=relExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, relExpression157.getTree());

            	    }
            	    break;

            	default :
            	    break loop48;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "eqExpression"

    public static class relExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "relExpression"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:500:1: relExpression : addExpression ( relOperator addExpression )* ;
    public final PBFParser.relExpression_return relExpression() throws RecognitionException {
        PBFParser.relExpression_return retval = new PBFParser.relExpression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        PBFParser.addExpression_return addExpression158 = null;

        PBFParser.relOperator_return relOperator159 = null;

        PBFParser.addExpression_return addExpression160 = null;



        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:501:3: ( addExpression ( relOperator addExpression )* )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:501:5: addExpression ( relOperator addExpression )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_addExpression_in_relExpression4270);
            addExpression158=addExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, addExpression158.getTree());
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:501:19: ( relOperator addExpression )*
            loop49:
            do {
                int alt49=2;
                int LA49_0 = input.LA(1);

                if ( ((LA49_0>=GT && LA49_0<=LT)) ) {
                    alt49=1;
                }


                switch (alt49) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:501:20: relOperator addExpression
            	    {
            	    pushFollow(FOLLOW_relOperator_in_relExpression4273);
            	    relOperator159=relOperator();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(relOperator159.getTree(), root_0);
            	    pushFollow(FOLLOW_addExpression_in_relExpression4276);
            	    addExpression160=addExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, addExpression160.getTree());

            	    }
            	    break;

            	default :
            	    break loop49;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "relExpression"

    public static class addExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "addExpression"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:504:1: addExpression : mulExpression ( addOperator mulExpression )* ;
    public final PBFParser.addExpression_return addExpression() throws RecognitionException {
        PBFParser.addExpression_return retval = new PBFParser.addExpression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        PBFParser.mulExpression_return mulExpression161 = null;

        PBFParser.addOperator_return addOperator162 = null;

        PBFParser.mulExpression_return mulExpression163 = null;



        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:505:3: ( mulExpression ( addOperator mulExpression )* )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:505:5: mulExpression ( addOperator mulExpression )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_mulExpression_in_addExpression4291);
            mulExpression161=mulExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, mulExpression161.getTree());
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:505:19: ( addOperator mulExpression )*
            loop50:
            do {
                int alt50=2;
                int LA50_0 = input.LA(1);

                if ( ((LA50_0>=PLUS && LA50_0<=MINUS)) ) {
                    alt50=1;
                }


                switch (alt50) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:505:20: addOperator mulExpression
            	    {
            	    pushFollow(FOLLOW_addOperator_in_addExpression4294);
            	    addOperator162=addOperator();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(addOperator162.getTree(), root_0);
            	    pushFollow(FOLLOW_mulExpression_in_addExpression4297);
            	    mulExpression163=mulExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, mulExpression163.getTree());

            	    }
            	    break;

            	default :
            	    break loop50;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "addExpression"

    public static class mulExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "mulExpression"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:508:1: mulExpression : unaryExpression ( mulOperator unaryExpression )* ;
    public final PBFParser.mulExpression_return mulExpression() throws RecognitionException {
        PBFParser.mulExpression_return retval = new PBFParser.mulExpression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        PBFParser.unaryExpression_return unaryExpression164 = null;

        PBFParser.mulOperator_return mulOperator165 = null;

        PBFParser.unaryExpression_return unaryExpression166 = null;



        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:509:3: ( unaryExpression ( mulOperator unaryExpression )* )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:509:5: unaryExpression ( mulOperator unaryExpression )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_unaryExpression_in_mulExpression4314);
            unaryExpression164=unaryExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, unaryExpression164.getTree());
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:509:21: ( mulOperator unaryExpression )*
            loop51:
            do {
                int alt51=2;
                int LA51_0 = input.LA(1);

                if ( ((LA51_0>=STAR && LA51_0<=SLASH)) ) {
                    alt51=1;
                }


                switch (alt51) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:509:22: mulOperator unaryExpression
            	    {
            	    pushFollow(FOLLOW_mulOperator_in_mulExpression4317);
            	    mulOperator165=mulOperator();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(mulOperator165.getTree(), root_0);
            	    pushFollow(FOLLOW_unaryExpression_in_mulExpression4320);
            	    unaryExpression166=unaryExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, unaryExpression166.getTree());

            	    }
            	    break;

            	default :
            	    break loop51;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "mulExpression"

    public static class unaryExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "unaryExpression"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:512:1: unaryExpression : ( unaryOperator unaryExpression | primary );
    public final PBFParser.unaryExpression_return unaryExpression() throws RecognitionException {
        PBFParser.unaryExpression_return retval = new PBFParser.unaryExpression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        PBFParser.unaryOperator_return unaryOperator167 = null;

        PBFParser.unaryExpression_return unaryExpression168 = null;

        PBFParser.primary_return primary169 = null;



        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:513:3: ( unaryOperator unaryExpression | primary )
            int alt52=2;
            int LA52_0 = input.LA(1);

            if ( ((LA52_0>=PLUS && LA52_0<=MINUS)) ) {
                alt52=1;
            }
            else if ( ((LA52_0>=INT_LITERAL && LA52_0<=LPAREN)||LA52_0==LT||LA52_0==ID) ) {
                alt52=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 52, 0, input);

                throw nvae;
            }
            switch (alt52) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:513:5: unaryOperator unaryExpression
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_unaryOperator_in_unaryExpression4337);
                    unaryOperator167=unaryOperator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(unaryOperator167.getTree(), root_0);
                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression4340);
                    unaryExpression168=unaryExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, unaryExpression168.getTree());

                    }
                    break;
                case 2 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:514:5: primary
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_primary_in_unaryExpression4346);
                    primary169=primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, primary169.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "unaryExpression"

    public static class primary_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "primary"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:517:1: primary : ( parExpression | iterOrIDArg ( DOT iterOrIDArg )* | literal );
    public final PBFParser.primary_return primary() throws RecognitionException {
        PBFParser.primary_return retval = new PBFParser.primary_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token DOT172=null;
        PBFParser.parExpression_return parExpression170 = null;

        PBFParser.iterOrIDArg_return iterOrIDArg171 = null;

        PBFParser.iterOrIDArg_return iterOrIDArg173 = null;

        PBFParser.literal_return literal174 = null;


        CommonTree DOT172_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:518:3: ( parExpression | iterOrIDArg ( DOT iterOrIDArg )* | literal )
            int alt54=3;
            switch ( input.LA(1) ) {
            case LPAREN:
                {
                alt54=1;
                }
                break;
            case LT:
            case ID:
                {
                alt54=2;
                }
                break;
            case INT_LITERAL:
            case DOUBLE_LITERAL:
            case STRING_LITERAL:
                {
                alt54=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 54, 0, input);

                throw nvae;
            }

            switch (alt54) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:518:5: parExpression
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_parExpression_in_primary4359);
                    parExpression170=parExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, parExpression170.getTree());

                    }
                    break;
                case 2 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:519:5: iterOrIDArg ( DOT iterOrIDArg )*
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_iterOrIDArg_in_primary4365);
                    iterOrIDArg171=iterOrIDArg();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, iterOrIDArg171.getTree());
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:519:17: ( DOT iterOrIDArg )*
                    loop53:
                    do {
                        int alt53=2;
                        int LA53_0 = input.LA(1);

                        if ( (LA53_0==DOT) ) {
                            alt53=1;
                        }


                        switch (alt53) {
                    	case 1 :
                    	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:519:18: DOT iterOrIDArg
                    	    {
                    	    DOT172=(Token)match(input,DOT,FOLLOW_DOT_in_primary4368); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    DOT172_tree = (CommonTree)adaptor.create(DOT172);
                    	    root_0 = (CommonTree)adaptor.becomeRoot(DOT172_tree, root_0);
                    	    }
                    	    pushFollow(FOLLOW_iterOrIDArg_in_primary4371);
                    	    iterOrIDArg173=iterOrIDArg();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) adaptor.addChild(root_0, iterOrIDArg173.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop53;
                        }
                    } while (true);


                    }
                    break;
                case 3 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:520:7: literal
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_literal_in_primary4386);
                    literal174=literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, literal174.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "primary"

    public static class iterOrIDArg_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "iterOrIDArg"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:523:1: iterOrIDArg : (i= iterOrID -> iterOrID ) ( arguments -> ^( CALL[$i.tree.getToken(), \"CALL\"] $iterOrIDArg ( arguments )? ) )? ;
    public final PBFParser.iterOrIDArg_return iterOrIDArg() throws RecognitionException {
        PBFParser.iterOrIDArg_return retval = new PBFParser.iterOrIDArg_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        PBFParser.iterOrID_return i = null;

        PBFParser.arguments_return arguments175 = null;


        RewriteRuleSubtreeStream stream_iterOrID=new RewriteRuleSubtreeStream(adaptor,"rule iterOrID");
        RewriteRuleSubtreeStream stream_arguments=new RewriteRuleSubtreeStream(adaptor,"rule arguments");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:524:3: ( (i= iterOrID -> iterOrID ) ( arguments -> ^( CALL[$i.tree.getToken(), \"CALL\"] $iterOrIDArg ( arguments )? ) )? )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:524:5: (i= iterOrID -> iterOrID ) ( arguments -> ^( CALL[$i.tree.getToken(), \"CALL\"] $iterOrIDArg ( arguments )? ) )?
            {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:524:5: (i= iterOrID -> iterOrID )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:524:6: i= iterOrID
            {
            pushFollow(FOLLOW_iterOrID_in_iterOrIDArg4402);
            i=iterOrID();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_iterOrID.add(i.getTree());


            // AST REWRITE
            // elements: iterOrID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 524:17: -> iterOrID
            {
                adaptor.addChild(root_0, stream_iterOrID.nextTree());

            }

            retval.tree = root_0;}
            }

            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:524:30: ( arguments -> ^( CALL[$i.tree.getToken(), \"CALL\"] $iterOrIDArg ( arguments )? ) )?
            int alt55=2;
            int LA55_0 = input.LA(1);

            if ( (LA55_0==LPAREN) ) {
                alt55=1;
            }
            switch (alt55) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:524:31: arguments
                    {
                    pushFollow(FOLLOW_arguments_in_iterOrIDArg4410);
                    arguments175=arguments();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_arguments.add(arguments175.getTree());


                    // AST REWRITE
                    // elements: iterOrIDArg, arguments
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 524:42: -> ^( CALL[$i.tree.getToken(), \"CALL\"] $iterOrIDArg ( arguments )? )
                    {
                        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:524:45: ^( CALL[$i.tree.getToken(), \"CALL\"] $iterOrIDArg ( arguments )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(CALL, (i!=null?((CommonTree)i.tree):null).getToken(), "CALL"), root_1);

                        adaptor.addChild(root_1, stream_retval.nextTree());
                        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:524:93: ( arguments )?
                        if ( stream_arguments.hasNext() ) {
                            adaptor.addChild(root_1, stream_arguments.nextTree());

                        }
                        stream_arguments.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "iterOrIDArg"

    public static class iterOrID_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "iterOrID"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:527:1: iterOrID : ( ID | iterator );
    public final PBFParser.iterOrID_return iterOrID() throws RecognitionException {
        PBFParser.iterOrID_return retval = new PBFParser.iterOrID_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token ID176=null;
        PBFParser.iterator_return iterator177 = null;


        CommonTree ID176_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:528:3: ( ID | iterator )
            int alt56=2;
            int LA56_0 = input.LA(1);

            if ( (LA56_0==ID) ) {
                alt56=1;
            }
            else if ( (LA56_0==LT) ) {
                alt56=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 56, 0, input);

                throw nvae;
            }
            switch (alt56) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:528:5: ID
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    ID176=(Token)match(input,ID,FOLLOW_ID_in_iterOrID4439); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    ID176_tree = (CommonTree)adaptor.create(ID176);
                    adaptor.addChild(root_0, ID176_tree);
                    }

                    }
                    break;
                case 2 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:529:5: iterator
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_iterator_in_iterOrID4445);
                    iterator177=iterator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, iterator177.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "iterOrID"

    public static class iterator_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "iterator"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:532:1: iterator : lt= LT elem= ID COLON set= ID GT -> ^( ITER[$lt, \"ITER\"] $elem $set) ;
    public final PBFParser.iterator_return iterator() throws RecognitionException {
        PBFParser.iterator_return retval = new PBFParser.iterator_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token lt=null;
        Token elem=null;
        Token set=null;
        Token COLON178=null;
        Token GT179=null;

        CommonTree lt_tree=null;
        CommonTree elem_tree=null;
        CommonTree set_tree=null;
        CommonTree COLON178_tree=null;
        CommonTree GT179_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_GT=new RewriteRuleTokenStream(adaptor,"token GT");
        RewriteRuleTokenStream stream_LT=new RewriteRuleTokenStream(adaptor,"token LT");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:533:3: (lt= LT elem= ID COLON set= ID GT -> ^( ITER[$lt, \"ITER\"] $elem $set) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:533:5: lt= LT elem= ID COLON set= ID GT
            {
            lt=(Token)match(input,LT,FOLLOW_LT_in_iterator4460); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LT.add(lt);

            elem=(Token)match(input,ID,FOLLOW_ID_in_iterator4464); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(elem);

            COLON178=(Token)match(input,COLON,FOLLOW_COLON_in_iterator4466); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COLON.add(COLON178);

            set=(Token)match(input,ID,FOLLOW_ID_in_iterator4470); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(set);

            GT179=(Token)match(input,GT,FOLLOW_GT_in_iterator4472); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_GT.add(GT179);



            // AST REWRITE
            // elements: set, elem
            // token labels: elem, set
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_elem=new RewriteRuleTokenStream(adaptor,"token elem",elem);
            RewriteRuleTokenStream stream_set=new RewriteRuleTokenStream(adaptor,"token set",set);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 533:63: -> ^( ITER[$lt, \"ITER\"] $elem $set)
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:533:66: ^( ITER[$lt, \"ITER\"] $elem $set)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ITER, lt, "ITER"), root_1);

                adaptor.addChild(root_1, stream_elem.nextNode());
                adaptor.addChild(root_1, stream_set.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "iterator"

    public static class parExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "parExpression"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:536:1: parExpression : LPAREN expression RPAREN ;
    public final PBFParser.parExpression_return parExpression() throws RecognitionException {
        PBFParser.parExpression_return retval = new PBFParser.parExpression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LPAREN180=null;
        Token RPAREN182=null;
        PBFParser.expression_return expression181 = null;


        CommonTree LPAREN180_tree=null;
        CommonTree RPAREN182_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:537:3: ( LPAREN expression RPAREN )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:537:5: LPAREN expression RPAREN
            {
            root_0 = (CommonTree)adaptor.nil();

            LPAREN180=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_parExpression4526); if (state.failed) return retval;
            pushFollow(FOLLOW_expression_in_parExpression4529);
            expression181=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, expression181.getTree());
            RPAREN182=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_parExpression4531); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "parExpression"

    public static class arguments_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "arguments"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:540:1: arguments : LPAREN ( valueList )? RPAREN ;
    public final PBFParser.arguments_return arguments() throws RecognitionException {
        PBFParser.arguments_return retval = new PBFParser.arguments_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LPAREN183=null;
        Token RPAREN185=null;
        PBFParser.valueList_return valueList184 = null;


        CommonTree LPAREN183_tree=null;
        CommonTree RPAREN185_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:541:3: ( LPAREN ( valueList )? RPAREN )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:541:5: LPAREN ( valueList )? RPAREN
            {
            root_0 = (CommonTree)adaptor.nil();

            LPAREN183=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arguments4545); if (state.failed) return retval;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:541:13: ( valueList )?
            int alt57=2;
            int LA57_0 = input.LA(1);

            if ( ((LA57_0>=PLUS && LA57_0<=MINUS)||(LA57_0>=INT_LITERAL && LA57_0<=LPAREN)||LA57_0==LBRACKET||LA57_0==LT||(LA57_0>=LIBRARY && LA57_0<=UNSPECIFIED)||LA57_0==ID) ) {
                alt57=1;
            }
            switch (alt57) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:541:13: valueList
                    {
                    pushFollow(FOLLOW_valueList_in_arguments4548);
                    valueList184=valueList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, valueList184.getTree());

                    }
                    break;

            }

            RPAREN185=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arguments4551); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "arguments"

    public static class arguments1_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "arguments1"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:544:1: arguments1 : LPAREN ( argumentList )? RPAREN ;
    public final PBFParser.arguments1_return arguments1() throws RecognitionException {
        PBFParser.arguments1_return retval = new PBFParser.arguments1_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LPAREN186=null;
        Token RPAREN188=null;
        PBFParser.argumentList_return argumentList187 = null;


        CommonTree LPAREN186_tree=null;
        CommonTree RPAREN188_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:545:3: ( LPAREN ( argumentList )? RPAREN )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:545:5: LPAREN ( argumentList )? RPAREN
            {
            root_0 = (CommonTree)adaptor.nil();

            LPAREN186=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arguments14611); if (state.failed) return retval;
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:545:13: ( argumentList )?
            int alt58=2;
            int LA58_0 = input.LA(1);

            if ( (LA58_0==LBRACKET||LA58_0==ID) ) {
                alt58=1;
            }
            switch (alt58) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:545:13: argumentList
                    {
                    pushFollow(FOLLOW_argumentList_in_arguments14614);
                    argumentList187=argumentList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, argumentList187.getTree());

                    }
                    break;

            }

            RPAREN188=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arguments14617); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "arguments1"

    public static class argumentList_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "argumentList"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:548:1: argumentList : argument ( COMMA argument )* ;
    public final PBFParser.argumentList_return argumentList() throws RecognitionException {
        PBFParser.argumentList_return retval = new PBFParser.argumentList_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token COMMA190=null;
        PBFParser.argument_return argument189 = null;

        PBFParser.argument_return argument191 = null;


        CommonTree COMMA190_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:549:3: ( argument ( COMMA argument )* )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:549:5: argument ( COMMA argument )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_argument_in_argumentList4633);
            argument189=argument();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, argument189.getTree());
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:549:14: ( COMMA argument )*
            loop59:
            do {
                int alt59=2;
                int LA59_0 = input.LA(1);

                if ( (LA59_0==COMMA) ) {
                    alt59=1;
                }


                switch (alt59) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:549:15: COMMA argument
            	    {
            	    COMMA190=(Token)match(input,COMMA,FOLLOW_COMMA_in_argumentList4636); if (state.failed) return retval;
            	    pushFollow(FOLLOW_argument_in_argumentList4639);
            	    argument191=argument();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, argument191.getTree());

            	    }
            	    break;

            	default :
            	    break loop59;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "argumentList"

    public static class argument_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "argument"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:552:1: argument : (instID= ref -> ^( STRUCT[$instID.tree.getToken(), \"AE\"] ^( ID[$instID.tree.getToken(), \"arg\"] $instID) ) | instIDs= refList -> ^( STRUCT[($instIDs.tree.getToken()!=null)?$instIDs.tree.getToken():((CommonTree)$instIDs.tree.getChild(0)).getToken(), \"AE\"] ^( ID[($instIDs.tree.getToken()!=null)?$instIDs.tree.getToken():((CommonTree)$instIDs.tree.getChild(0)).getToken(), \"arg\"] $instIDs) ) | instIDss= refListList -> ^( STRUCT[($instIDss.tree.getToken()!=null)?$instIDss.tree.getToken():((CommonTree)$instIDss.tree.getChild(0)).getToken(), \"AE\"] $instIDss) );
    public final PBFParser.argument_return argument() throws RecognitionException {
        PBFParser.argument_return retval = new PBFParser.argument_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        PBFParser.ref_return instID = null;

        PBFParser.refList_return instIDs = null;

        PBFParser.refListList_return instIDss = null;


        RewriteRuleSubtreeStream stream_ref=new RewriteRuleSubtreeStream(adaptor,"rule ref");
        RewriteRuleSubtreeStream stream_refList=new RewriteRuleSubtreeStream(adaptor,"rule refList");
        RewriteRuleSubtreeStream stream_refListList=new RewriteRuleSubtreeStream(adaptor,"rule refListList");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:553:3: (instID= ref -> ^( STRUCT[$instID.tree.getToken(), \"AE\"] ^( ID[$instID.tree.getToken(), \"arg\"] $instID) ) | instIDs= refList -> ^( STRUCT[($instIDs.tree.getToken()!=null)?$instIDs.tree.getToken():((CommonTree)$instIDs.tree.getChild(0)).getToken(), \"AE\"] ^( ID[($instIDs.tree.getToken()!=null)?$instIDs.tree.getToken():((CommonTree)$instIDs.tree.getChild(0)).getToken(), \"arg\"] $instIDs) ) | instIDss= refListList -> ^( STRUCT[($instIDss.tree.getToken()!=null)?$instIDss.tree.getToken():((CommonTree)$instIDss.tree.getChild(0)).getToken(), \"AE\"] $instIDss) )
            int alt60=3;
            int LA60_0 = input.LA(1);

            if ( (LA60_0==ID) ) {
                alt60=1;
            }
            else if ( (LA60_0==LBRACKET) ) {
                int LA60_2 = input.LA(2);

                if ( (LA60_2==LBRACKET) ) {
                    alt60=3;
                }
                else if ( (LA60_2==RBRACKET||LA60_2==ID) ) {
                    alt60=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 60, 2, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 60, 0, input);

                throw nvae;
            }
            switch (alt60) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:553:5: instID= ref
                    {
                    pushFollow(FOLLOW_ref_in_argument4658);
                    instID=ref();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_ref.add(instID.getTree());


                    // AST REWRITE
                    // elements: instID
                    // token labels: 
                    // rule labels: retval, instID
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_instID=new RewriteRuleSubtreeStream(adaptor,"rule instID",instID!=null?instID.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 553:37: -> ^( STRUCT[$instID.tree.getToken(), \"AE\"] ^( ID[$instID.tree.getToken(), \"arg\"] $instID) )
                    {
                        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:553:40: ^( STRUCT[$instID.tree.getToken(), \"AE\"] ^( ID[$instID.tree.getToken(), \"arg\"] $instID) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRUCT, (instID!=null?((CommonTree)instID.tree):null).getToken(), "AE"), root_1);

                        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:553:80: ^( ID[$instID.tree.getToken(), \"arg\"] $instID)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, (instID!=null?((CommonTree)instID.tree):null).getToken(), "arg"), root_2);

                        adaptor.addChild(root_2, stream_instID.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:554:5: instIDs= refList
                    {
                    pushFollow(FOLLOW_refList_in_argument4702);
                    instIDs=refList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_refList.add(instIDs.getTree());


                    // AST REWRITE
                    // elements: instIDs
                    // token labels: 
                    // rule labels: instIDs, retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_instIDs=new RewriteRuleSubtreeStream(adaptor,"rule instIDs",instIDs!=null?instIDs.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 554:41: -> ^( STRUCT[($instIDs.tree.getToken()!=null)?$instIDs.tree.getToken():((CommonTree)$instIDs.tree.getChild(0)).getToken(), \"AE\"] ^( ID[($instIDs.tree.getToken()!=null)?$instIDs.tree.getToken():((CommonTree)$instIDs.tree.getChild(0)).getToken(), \"arg\"] $instIDs) )
                    {
                        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:554:44: ^( STRUCT[($instIDs.tree.getToken()!=null)?$instIDs.tree.getToken():((CommonTree)$instIDs.tree.getChild(0)).getToken(), \"AE\"] ^( ID[($instIDs.tree.getToken()!=null)?$instIDs.tree.getToken():((CommonTree)$instIDs.tree.getChild(0)).getToken(), \"arg\"] $instIDs) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRUCT, ((instIDs!=null?((CommonTree)instIDs.tree):null).getToken()!=null)?(instIDs!=null?((CommonTree)instIDs.tree):null).getToken():((CommonTree)(instIDs!=null?((CommonTree)instIDs.tree):null).getChild(0)).getToken(), "AE"), root_1);

                        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:554:169: ^( ID[($instIDs.tree.getToken()!=null)?$instIDs.tree.getToken():((CommonTree)$instIDs.tree.getChild(0)).getToken(), \"arg\"] $instIDs)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, ((instIDs!=null?((CommonTree)instIDs.tree):null).getToken()!=null)?(instIDs!=null?((CommonTree)instIDs.tree):null).getToken():((CommonTree)(instIDs!=null?((CommonTree)instIDs.tree):null).getChild(0)).getToken(), "arg"), root_2);

                        adaptor.addChild(root_2, stream_instIDs.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:555:5: instIDss= refListList
                    {
                    pushFollow(FOLLOW_refListList_in_argument4745);
                    instIDss=refListList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_refListList.add(instIDss.getTree());


                    // AST REWRITE
                    // elements: instIDss
                    // token labels: 
                    // rule labels: retval, instIDss
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_instIDss=new RewriteRuleSubtreeStream(adaptor,"rule instIDss",instIDss!=null?instIDss.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 555:45: -> ^( STRUCT[($instIDss.tree.getToken()!=null)?$instIDss.tree.getToken():((CommonTree)$instIDss.tree.getChild(0)).getToken(), \"AE\"] $instIDss)
                    {
                        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:555:48: ^( STRUCT[($instIDss.tree.getToken()!=null)?$instIDss.tree.getToken():((CommonTree)$instIDss.tree.getChild(0)).getToken(), \"AE\"] $instIDss)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRUCT, ((instIDss!=null?((CommonTree)instIDss.tree):null).getToken()!=null)?(instIDss!=null?((CommonTree)instIDss.tree):null).getToken():((CommonTree)(instIDss!=null?((CommonTree)instIDss.tree):null).getChild(0)).getToken(), "AE"), root_1);

                        adaptor.addChild(root_1, stream_instIDss.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "argument"

    public static class refList_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "refList"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:559:1: refList : LBRACKET ( ref ( COMMA ref )* )? RBRACKET -> ( ref )* ;
    public final PBFParser.refList_return refList() throws RecognitionException {
        PBFParser.refList_return retval = new PBFParser.refList_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LBRACKET192=null;
        Token COMMA194=null;
        Token RBRACKET196=null;
        PBFParser.ref_return ref193 = null;

        PBFParser.ref_return ref195 = null;


        CommonTree LBRACKET192_tree=null;
        CommonTree COMMA194_tree=null;
        CommonTree RBRACKET196_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_ref=new RewriteRuleSubtreeStream(adaptor,"rule ref");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:560:3: ( LBRACKET ( ref ( COMMA ref )* )? RBRACKET -> ( ref )* )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:560:5: LBRACKET ( ref ( COMMA ref )* )? RBRACKET
            {
            LBRACKET192=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_refList4788); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET192);

            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:561:7: ( ref ( COMMA ref )* )?
            int alt62=2;
            int LA62_0 = input.LA(1);

            if ( (LA62_0==ID) ) {
                alt62=1;
            }
            switch (alt62) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:561:8: ref ( COMMA ref )*
                    {
                    pushFollow(FOLLOW_ref_in_refList4797);
                    ref193=ref();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_ref.add(ref193.getTree());
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:561:12: ( COMMA ref )*
                    loop61:
                    do {
                        int alt61=2;
                        int LA61_0 = input.LA(1);

                        if ( (LA61_0==COMMA) ) {
                            alt61=1;
                        }


                        switch (alt61) {
                    	case 1 :
                    	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:561:13: COMMA ref
                    	    {
                    	    COMMA194=(Token)match(input,COMMA,FOLLOW_COMMA_in_refList4800); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA194);

                    	    pushFollow(FOLLOW_ref_in_refList4802);
                    	    ref195=ref();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_ref.add(ref195.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop61;
                        }
                    } while (true);


                    }
                    break;

            }

            RBRACKET196=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_refList4812); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET196);



            // AST REWRITE
            // elements: ref
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 562:41: -> ( ref )*
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:562:44: ( ref )*
                while ( stream_ref.hasNext() ) {
                    adaptor.addChild(root_0, stream_ref.nextTree());

                }
                stream_ref.reset();

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "refList"

    public static class refListList_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "refListList"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:565:1: refListList : LBRACKET LBRACKET (lower+= ref ( COMMA lower+= ref )* )? RBRACKET COMMA LBRACKET (upper+= ref ( COMMA upper+= ref )* )? RBRACKET RBRACKET -> ^( ID[\"lowerArg\"] ( $lower)* ) ^( ID[\"upperArg\"] ( $upper)* ) ;
    public final PBFParser.refListList_return refListList() throws RecognitionException {
        PBFParser.refListList_return retval = new PBFParser.refListList_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LBRACKET197=null;
        Token LBRACKET198=null;
        Token COMMA199=null;
        Token RBRACKET200=null;
        Token COMMA201=null;
        Token LBRACKET202=null;
        Token COMMA203=null;
        Token RBRACKET204=null;
        Token RBRACKET205=null;
        List list_lower=null;
        List list_upper=null;
        RuleReturnScope lower = null;
        RuleReturnScope upper = null;
        CommonTree LBRACKET197_tree=null;
        CommonTree LBRACKET198_tree=null;
        CommonTree COMMA199_tree=null;
        CommonTree RBRACKET200_tree=null;
        CommonTree COMMA201_tree=null;
        CommonTree LBRACKET202_tree=null;
        CommonTree COMMA203_tree=null;
        CommonTree RBRACKET204_tree=null;
        CommonTree RBRACKET205_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_ref=new RewriteRuleSubtreeStream(adaptor,"rule ref");
        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:566:3: ( LBRACKET LBRACKET (lower+= ref ( COMMA lower+= ref )* )? RBRACKET COMMA LBRACKET (upper+= ref ( COMMA upper+= ref )* )? RBRACKET RBRACKET -> ^( ID[\"lowerArg\"] ( $lower)* ) ^( ID[\"upperArg\"] ( $upper)* ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:566:5: LBRACKET LBRACKET (lower+= ref ( COMMA lower+= ref )* )? RBRACKET COMMA LBRACKET (upper+= ref ( COMMA upper+= ref )* )? RBRACKET RBRACKET
            {
            LBRACKET197=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_refListList4857); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET197);

            LBRACKET198=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_refListList4863); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET198);

            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:568:7: (lower+= ref ( COMMA lower+= ref )* )?
            int alt64=2;
            int LA64_0 = input.LA(1);

            if ( (LA64_0==ID) ) {
                alt64=1;
            }
            switch (alt64) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:568:8: lower+= ref ( COMMA lower+= ref )*
                    {
                    pushFollow(FOLLOW_ref_in_refListList4874);
                    lower=ref();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_ref.add(lower.getTree());
                    if (list_lower==null) list_lower=new ArrayList();
                    list_lower.add(lower.getTree());

                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:568:19: ( COMMA lower+= ref )*
                    loop63:
                    do {
                        int alt63=2;
                        int LA63_0 = input.LA(1);

                        if ( (LA63_0==COMMA) ) {
                            alt63=1;
                        }


                        switch (alt63) {
                    	case 1 :
                    	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:568:20: COMMA lower+= ref
                    	    {
                    	    COMMA199=(Token)match(input,COMMA,FOLLOW_COMMA_in_refListList4877); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA199);

                    	    pushFollow(FOLLOW_ref_in_refListList4881);
                    	    lower=ref();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_ref.add(lower.getTree());
                    	    if (list_lower==null) list_lower=new ArrayList();
                    	    list_lower.add(lower.getTree());


                    	    }
                    	    break;

                    	default :
                    	    break loop63;
                        }
                    } while (true);


                    }
                    break;

            }

            RBRACKET200=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_refListList4891); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET200);

            COMMA201=(Token)match(input,COMMA,FOLLOW_COMMA_in_refListList4898); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COMMA.add(COMMA201);

            LBRACKET202=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_refListList4904); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET202);

            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:572:7: (upper+= ref ( COMMA upper+= ref )* )?
            int alt66=2;
            int LA66_0 = input.LA(1);

            if ( (LA66_0==ID) ) {
                alt66=1;
            }
            switch (alt66) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:572:8: upper+= ref ( COMMA upper+= ref )*
                    {
                    pushFollow(FOLLOW_ref_in_refListList4915);
                    upper=ref();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_ref.add(upper.getTree());
                    if (list_upper==null) list_upper=new ArrayList();
                    list_upper.add(upper.getTree());

                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:572:19: ( COMMA upper+= ref )*
                    loop65:
                    do {
                        int alt65=2;
                        int LA65_0 = input.LA(1);

                        if ( (LA65_0==COMMA) ) {
                            alt65=1;
                        }


                        switch (alt65) {
                    	case 1 :
                    	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:572:20: COMMA upper+= ref
                    	    {
                    	    COMMA203=(Token)match(input,COMMA,FOLLOW_COMMA_in_refListList4918); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA203);

                    	    pushFollow(FOLLOW_ref_in_refListList4922);
                    	    upper=ref();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_ref.add(upper.getTree());
                    	    if (list_upper==null) list_upper=new ArrayList();
                    	    list_upper.add(upper.getTree());


                    	    }
                    	    break;

                    	default :
                    	    break loop65;
                        }
                    } while (true);


                    }
                    break;

            }

            RBRACKET204=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_refListList4932); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET204);

            RBRACKET205=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_refListList4939); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET205);



            // AST REWRITE
            // elements: lower, upper
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: upper, lower
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_upper=new RewriteRuleSubtreeStream(adaptor,"token upper",list_upper);
            RewriteRuleSubtreeStream stream_lower=new RewriteRuleSubtreeStream(adaptor,"token lower",list_lower);
            root_0 = (CommonTree)adaptor.nil();
            // 574:41: -> ^( ID[\"lowerArg\"] ( $lower)* ) ^( ID[\"upperArg\"] ( $upper)* )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:574:43: ^( ID[\"lowerArg\"] ( $lower)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, "lowerArg"), root_1);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:574:60: ( $lower)*
                while ( stream_lower.hasNext() ) {
                    adaptor.addChild(root_1, stream_lower.nextTree());

                }
                stream_lower.reset();

                adaptor.addChild(root_0, root_1);
                }
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:574:69: ^( ID[\"upperArg\"] ( $upper)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, "upperArg"), root_1);

                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:574:86: ( $upper)*
                while ( stream_upper.hasNext() ) {
                    adaptor.addChild(root_1, stream_upper.nextTree());

                }
                stream_upper.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "refListList"

    public static class ref_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ref"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:578:1: ref : parts+= ID (parts+= DOT parts+= ID )* -> ^( ID[((CommonToken)$parts.get(0)), concat($parts)] ) ;
    public final PBFParser.ref_return ref() throws RecognitionException {
        PBFParser.ref_return retval = new PBFParser.ref_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token parts=null;
        List list_parts=null;

        CommonTree parts_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_DOT=new RewriteRuleTokenStream(adaptor,"token DOT");

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:579:3: (parts+= ID (parts+= DOT parts+= ID )* -> ^( ID[((CommonToken)$parts.get(0)), concat($parts)] ) )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:579:5: parts+= ID (parts+= DOT parts+= ID )*
            {
            parts=(Token)match(input,ID,FOLLOW_ID_in_ref5001); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(parts);

            if (list_parts==null) list_parts=new ArrayList();
            list_parts.add(parts);

            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:579:15: (parts+= DOT parts+= ID )*
            loop67:
            do {
                int alt67=2;
                int LA67_0 = input.LA(1);

                if ( (LA67_0==DOT) ) {
                    alt67=1;
                }


                switch (alt67) {
            	case 1 :
            	    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:579:16: parts+= DOT parts+= ID
            	    {
            	    parts=(Token)match(input,DOT,FOLLOW_DOT_in_ref5006); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_DOT.add(parts);

            	    if (list_parts==null) list_parts=new ArrayList();
            	    list_parts.add(parts);

            	    parts=(Token)match(input,ID,FOLLOW_ID_in_ref5010); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_ID.add(parts);

            	    if (list_parts==null) list_parts=new ArrayList();
            	    list_parts.add(parts);


            	    }
            	    break;

            	default :
            	    break loop67;
                }
            } while (true);



            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 579:53: -> ^( ID[((CommonToken)$parts.get(0)), concat($parts)] )
            {
                // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:579:56: ^( ID[((CommonToken)$parts.get(0)), concat($parts)] )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ID, ((CommonToken)list_parts.get(0)), concat(list_parts)), root_1);

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "ref"

    public static class eqOperator_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "eqOperator"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:585:1: eqOperator : ( EQEQ | BANGEQ );
    public final PBFParser.eqOperator_return eqOperator() throws RecognitionException {
        PBFParser.eqOperator_return retval = new PBFParser.eqOperator_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set206=null;

        CommonTree set206_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:586:3: ( EQEQ | BANGEQ )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set206=(Token)input.LT(1);
            if ( (input.LA(1)>=EQEQ && input.LA(1)<=BANGEQ) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set206));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "eqOperator"

    public static class relOperator_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "relOperator"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:590:1: relOperator : ( LT EQ | GT EQ | LT | GT );
    public final PBFParser.relOperator_return relOperator() throws RecognitionException {
        PBFParser.relOperator_return retval = new PBFParser.relOperator_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LT207=null;
        Token EQ208=null;
        Token GT209=null;
        Token EQ210=null;
        Token LT211=null;
        Token GT212=null;

        CommonTree LT207_tree=null;
        CommonTree EQ208_tree=null;
        CommonTree GT209_tree=null;
        CommonTree EQ210_tree=null;
        CommonTree LT211_tree=null;
        CommonTree GT212_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:591:3: ( LT EQ | GT EQ | LT | GT )
            int alt68=4;
            int LA68_0 = input.LA(1);

            if ( (LA68_0==LT) ) {
                int LA68_1 = input.LA(2);

                if ( (LA68_1==EQ) ) {
                    alt68=1;
                }
                else if ( ((LA68_1>=PLUS && LA68_1<=MINUS)||(LA68_1>=INT_LITERAL && LA68_1<=LPAREN)||LA68_1==LT||LA68_1==ID) ) {
                    alt68=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 68, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA68_0==GT) ) {
                int LA68_2 = input.LA(2);

                if ( (LA68_2==EQ) ) {
                    alt68=2;
                }
                else if ( ((LA68_2>=PLUS && LA68_2<=MINUS)||(LA68_2>=INT_LITERAL && LA68_2<=LPAREN)||LA68_2==LT||LA68_2==ID) ) {
                    alt68=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 68, 2, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 68, 0, input);

                throw nvae;
            }
            switch (alt68) {
                case 1 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:591:5: LT EQ
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    LT207=(Token)match(input,LT,FOLLOW_LT_in_relOperator5071); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    LT207_tree = (CommonTree)adaptor.create(LT207);
                    adaptor.addChild(root_0, LT207_tree);
                    }
                    EQ208=(Token)match(input,EQ,FOLLOW_EQ_in_relOperator5073); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    EQ208_tree = (CommonTree)adaptor.create(EQ208);
                    adaptor.addChild(root_0, EQ208_tree);
                    }

                    }
                    break;
                case 2 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:592:5: GT EQ
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    GT209=(Token)match(input,GT,FOLLOW_GT_in_relOperator5079); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    GT209_tree = (CommonTree)adaptor.create(GT209);
                    adaptor.addChild(root_0, GT209_tree);
                    }
                    EQ210=(Token)match(input,EQ,FOLLOW_EQ_in_relOperator5081); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    EQ210_tree = (CommonTree)adaptor.create(EQ210);
                    adaptor.addChild(root_0, EQ210_tree);
                    }

                    }
                    break;
                case 3 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:593:5: LT
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    LT211=(Token)match(input,LT,FOLLOW_LT_in_relOperator5087); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    LT211_tree = (CommonTree)adaptor.create(LT211);
                    adaptor.addChild(root_0, LT211_tree);
                    }

                    }
                    break;
                case 4 :
                    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:594:5: GT
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    GT212=(Token)match(input,GT,FOLLOW_GT_in_relOperator5093); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    GT212_tree = (CommonTree)adaptor.create(GT212);
                    adaptor.addChild(root_0, GT212_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "relOperator"

    public static class addOperator_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "addOperator"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:597:1: addOperator : ( PLUS | MINUS );
    public final PBFParser.addOperator_return addOperator() throws RecognitionException {
        PBFParser.addOperator_return retval = new PBFParser.addOperator_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set213=null;

        CommonTree set213_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:598:3: ( PLUS | MINUS )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set213=(Token)input.LT(1);
            if ( (input.LA(1)>=PLUS && input.LA(1)<=MINUS) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set213));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "addOperator"

    public static class mulOperator_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "mulOperator"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:602:1: mulOperator : ( STAR | SLASH );
    public final PBFParser.mulOperator_return mulOperator() throws RecognitionException {
        PBFParser.mulOperator_return retval = new PBFParser.mulOperator_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set214=null;

        CommonTree set214_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:603:3: ( STAR | SLASH )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set214=(Token)input.LT(1);
            if ( (input.LA(1)>=STAR && input.LA(1)<=SLASH) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set214));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "mulOperator"

    public static class unaryOperator_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "unaryOperator"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:607:1: unaryOperator : ( PLUS | MINUS );
    public final PBFParser.unaryOperator_return unaryOperator() throws RecognitionException {
        PBFParser.unaryOperator_return retval = new PBFParser.unaryOperator_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set215=null;

        CommonTree set215_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:608:3: ( PLUS | MINUS )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set215=(Token)input.LT(1);
            if ( (input.LA(1)>=PLUS && input.LA(1)<=MINUS) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set215));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "unaryOperator"

    public static class sign_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "sign"
    // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:612:1: sign : ( PLUS | MINUS );
    public final PBFParser.sign_return sign() throws RecognitionException {
        PBFParser.sign_return retval = new PBFParser.sign_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set216=null;

        CommonTree set216_tree=null;

        try {
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:613:3: ( PLUS | MINUS )
            // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set216=(Token)input.LT(1);
            if ( (input.LA(1)>=PLUS && input.LA(1)<=MINUS) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set216));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }


        catch (RecognitionException ex) {
          reportError(ex);
        }
          
        finally {
        }
        return retval;
    }
    // $ANTLR end "sign"

    // $ANTLR start synpred1_PBFParser
    public final void synpred1_PBFParser_fragment() throws RecognitionException {   
        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:184:5: ( INT_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )
        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:184:6: INT_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN )
        {
        match(input,INT_LITERAL,FOLLOW_INT_LITERAL_in_synpred1_PBFParser882); if (state.failed) return ;
        if ( input.LA(1)==RPAREN||input.LA(1)==RBRACKET||input.LA(1)==RBRACE||(input.LA(1)>=SEMI && input.LA(1)<=COMMA) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }


        }
    }
    // $ANTLR end synpred1_PBFParser

    // $ANTLR start synpred2_PBFParser
    public final void synpred2_PBFParser_fragment() throws RecognitionException {   
        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:185:5: ( DOUBLE_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )
        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:185:6: DOUBLE_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN )
        {
        match(input,DOUBLE_LITERAL,FOLLOW_DOUBLE_LITERAL_in_synpred2_PBFParser920); if (state.failed) return ;
        if ( input.LA(1)==RPAREN||input.LA(1)==RBRACKET||input.LA(1)==RBRACE||(input.LA(1)>=SEMI && input.LA(1)<=COMMA) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }


        }
    }
    // $ANTLR end synpred2_PBFParser

    // $ANTLR start synpred3_PBFParser
    public final void synpred3_PBFParser_fragment() throws RecognitionException {   
        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:186:5: ( STRING_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )
        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:186:6: STRING_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN )
        {
        match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_synpred3_PBFParser955); if (state.failed) return ;
        if ( input.LA(1)==RPAREN||input.LA(1)==RBRACKET||input.LA(1)==RBRACE||(input.LA(1)>=SEMI && input.LA(1)<=COMMA) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }


        }
    }
    // $ANTLR end synpred3_PBFParser

    // $ANTLR start synpred4_PBFParser
    public final void synpred4_PBFParser_fragment() throws RecognitionException {   
        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:188:5: ( ID ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )
        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:188:6: ID ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN )
        {
        match(input,ID,FOLLOW_ID_in_synpred4_PBFParser996); if (state.failed) return ;
        if ( input.LA(1)==RPAREN||input.LA(1)==RBRACKET||input.LA(1)==RBRACE||(input.LA(1)>=SEMI && input.LA(1)<=COMMA) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }


        }
    }
    // $ANTLR end synpred4_PBFParser

    // $ANTLR start synpred5_PBFParser
    public final void synpred5_PBFParser_fragment() throws RecognitionException {   
        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:195:5: ( NULL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )
        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:195:6: NULL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN )
        {
        match(input,NULL,FOLLOW_NULL_in_synpred5_PBFParser1066); if (state.failed) return ;
        if ( input.LA(1)==RPAREN||input.LA(1)==RBRACKET||input.LA(1)==RBRACE||(input.LA(1)>=SEMI && input.LA(1)<=COMMA) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }


        }
    }
    // $ANTLR end synpred5_PBFParser

    // $ANTLR start synpred6_PBFParser
    public final void synpred6_PBFParser_fragment() throws RecognitionException {   
        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:196:5: ( INT_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )
        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:196:6: INT_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN )
        {
        match(input,INT_LITERAL,FOLLOW_INT_LITERAL_in_synpred6_PBFParser1109); if (state.failed) return ;
        if ( input.LA(1)==RPAREN||input.LA(1)==RBRACKET||input.LA(1)==RBRACE||(input.LA(1)>=SEMI && input.LA(1)<=COMMA) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }


        }
    }
    // $ANTLR end synpred6_PBFParser

    // $ANTLR start synpred7_PBFParser
    public final void synpred7_PBFParser_fragment() throws RecognitionException {   
        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:197:5: ( DOUBLE_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )
        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:197:6: DOUBLE_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN )
        {
        match(input,DOUBLE_LITERAL,FOLLOW_DOUBLE_LITERAL_in_synpred7_PBFParser1147); if (state.failed) return ;
        if ( input.LA(1)==RPAREN||input.LA(1)==RBRACKET||input.LA(1)==RBRACE||(input.LA(1)>=SEMI && input.LA(1)<=COMMA) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }


        }
    }
    // $ANTLR end synpred7_PBFParser

    // $ANTLR start synpred8_PBFParser
    public final void synpred8_PBFParser_fragment() throws RecognitionException {   
        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:198:5: ( STRING_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )
        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:198:6: STRING_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN )
        {
        match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_synpred8_PBFParser1182); if (state.failed) return ;
        if ( input.LA(1)==RPAREN||input.LA(1)==RBRACKET||input.LA(1)==RBRACE||(input.LA(1)>=SEMI && input.LA(1)<=COMMA) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }


        }
    }
    // $ANTLR end synpred8_PBFParser

    // $ANTLR start synpred9_PBFParser
    public final void synpred9_PBFParser_fragment() throws RecognitionException {   
        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:200:5: ( ID ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )
        // D:\\Workspace\\Eclipse Workspace\\ParserProject\\PBFParser.g:200:6: ID ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN )
        {
        match(input,ID,FOLLOW_ID_in_synpred9_PBFParser1223); if (state.failed) return ;
        if ( input.LA(1)==RPAREN||input.LA(1)==RBRACKET||input.LA(1)==RBRACE||(input.LA(1)>=SEMI && input.LA(1)<=COMMA) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }


        }
    }
    // $ANTLR end synpred9_PBFParser

    // Delegated rules

    public final boolean synpred9_PBFParser() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred9_PBFParser_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred3_PBFParser() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred3_PBFParser_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred6_PBFParser() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred6_PBFParser_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred5_PBFParser() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred5_PBFParser_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred4_PBFParser() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred4_PBFParser_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred7_PBFParser() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred7_PBFParser_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred8_PBFParser() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred8_PBFParser_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred2_PBFParser() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_PBFParser_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred1_PBFParser() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_PBFParser_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA15 dfa15 = new DFA15(this);
    protected DFA16 dfa16 = new DFA16(this);
    protected DFA30 dfa30 = new DFA30(this);
    protected DFA45 dfa45 = new DFA45(this);
    protected DFA46 dfa46 = new DFA46(this);
    static final String DFA15_eotS =
        "\16\uffff";
    static final String DFA15_eofS =
        "\16\uffff";
    static final String DFA15_minS =
        "\1\11\3\0\1\11\1\27\10\uffff";
    static final String DFA15_maxS =
        "\1\56\3\0\1\56\1\41\10\uffff";
    static final String DFA15_acceptS =
        "\6\uffff\1\6\1\7\1\10\1\1\1\2\1\3\1\4\1\5";
    static final String DFA15_specialS =
        "\1\uffff\1\1\1\3\1\2\1\uffff\1\0\10\uffff}>";
    static final String[] DFA15_transitionS = {
            "\2\6\5\uffff\1\1\1\2\1\3\1\6\1\uffff\1\10\4\uffff\1\4\11\uffff"+
            "\10\7\2\uffff\1\5",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\2\14\5\uffff\2\14\30\uffff\1\14\3\uffff\1\6",
            "\1\7\11\uffff\1\7",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA15_eot = DFA.unpackEncodedString(DFA15_eotS);
    static final short[] DFA15_eof = DFA.unpackEncodedString(DFA15_eofS);
    static final char[] DFA15_min = DFA.unpackEncodedStringToUnsignedChars(DFA15_minS);
    static final char[] DFA15_max = DFA.unpackEncodedStringToUnsignedChars(DFA15_maxS);
    static final short[] DFA15_accept = DFA.unpackEncodedString(DFA15_acceptS);
    static final short[] DFA15_special = DFA.unpackEncodedString(DFA15_specialS);
    static final short[][] DFA15_transition;

    static {
        int numStates = DFA15_transitionS.length;
        DFA15_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA15_transition[i] = DFA.unpackEncodedString(DFA15_transitionS[i]);
        }
    }

    class DFA15 extends DFA {

        public DFA15(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 15;
            this.eot = DFA15_eot;
            this.eof = DFA15_eof;
            this.min = DFA15_min;
            this.max = DFA15_max;
            this.accept = DFA15_accept;
            this.special = DFA15_special;
            this.transition = DFA15_transition;
        }
        public String getDescription() {
            return "183:1: value : ( ( INT_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> INT_LITERAL | ( DOUBLE_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> DOUBLE_LITERAL | ( STRING_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> STRING_LITERAL | interval | ( ID ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> ID | expression | structure | list );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA15_5 = input.LA(1);

                         
                        int index15_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA15_5==LBRACE||LA15_5==EQ) ) {s = 7;}

                        else if ( (synpred4_PBFParser()) ) {s = 13;}

                        else if ( (true) ) {s = 6;}

                         
                        input.seek(index15_5);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA15_1 = input.LA(1);

                         
                        int index15_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_PBFParser()) ) {s = 9;}

                        else if ( (true) ) {s = 6;}

                         
                        input.seek(index15_1);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA15_3 = input.LA(1);

                         
                        int index15_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred3_PBFParser()) ) {s = 11;}

                        else if ( (true) ) {s = 6;}

                         
                        input.seek(index15_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA15_2 = input.LA(1);

                         
                        int index15_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_PBFParser()) ) {s = 10;}

                        else if ( (true) ) {s = 6;}

                         
                        input.seek(index15_2);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 15, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA16_eotS =
        "\17\uffff";
    static final String DFA16_eofS =
        "\17\uffff";
    static final String DFA16_minS =
        "\1\11\1\uffff\3\0\1\11\1\27\10\uffff";
    static final String DFA16_maxS =
        "\1\56\1\uffff\3\0\1\56\1\41\10\uffff";
    static final String DFA16_acceptS =
        "\1\uffff\1\1\5\uffff\1\7\1\10\1\11\1\2\1\3\1\4\1\5\1\6";
    static final String DFA16_specialS =
        "\1\3\1\uffff\1\1\1\2\1\0\1\uffff\1\4\10\uffff}>";
    static final String[] DFA16_transitionS = {
            "\2\7\5\uffff\1\2\1\3\1\4\1\7\1\uffff\1\11\4\uffff\1\5\11\uffff"+
            "\10\10\1\uffff\1\1\1\6",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\2\15\5\uffff\2\15\30\uffff\1\15\3\uffff\1\7",
            "\1\10\11\uffff\1\10",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA16_eot = DFA.unpackEncodedString(DFA16_eotS);
    static final short[] DFA16_eof = DFA.unpackEncodedString(DFA16_eofS);
    static final char[] DFA16_min = DFA.unpackEncodedStringToUnsignedChars(DFA16_minS);
    static final char[] DFA16_max = DFA.unpackEncodedStringToUnsignedChars(DFA16_maxS);
    static final short[] DFA16_accept = DFA.unpackEncodedString(DFA16_acceptS);
    static final short[] DFA16_special = DFA.unpackEncodedString(DFA16_specialS);
    static final short[][] DFA16_transition;

    static {
        int numStates = DFA16_transitionS.length;
        DFA16_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA16_transition[i] = DFA.unpackEncodedString(DFA16_transitionS[i]);
        }
    }

    class DFA16 extends DFA {

        public DFA16(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 16;
            this.eot = DFA16_eot;
            this.eof = DFA16_eof;
            this.min = DFA16_min;
            this.max = DFA16_max;
            this.accept = DFA16_accept;
            this.special = DFA16_special;
            this.transition = DFA16_transition;
        }
        public String getDescription() {
            return "194:1: valueNull : ( ( NULL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> NULL | ( INT_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> INT_LITERAL | ( DOUBLE_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> DOUBLE_LITERAL | ( STRING_LITERAL ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> STRING_LITERAL | interval | ( ID ( COMMA | SEMI | RBRACE | RBRACKET | RPAREN ) )=> ID | expression | structureNull | list );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA16_4 = input.LA(1);

                         
                        int index16_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred8_PBFParser()) ) {s = 12;}

                        else if ( (true) ) {s = 7;}

                         
                        input.seek(index16_4);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA16_2 = input.LA(1);

                         
                        int index16_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred6_PBFParser()) ) {s = 10;}

                        else if ( (true) ) {s = 7;}

                         
                        input.seek(index16_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA16_3 = input.LA(1);

                         
                        int index16_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_PBFParser()) ) {s = 11;}

                        else if ( (true) ) {s = 7;}

                         
                        input.seek(index16_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA16_0 = input.LA(1);

                         
                        int index16_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA16_0==NULL) && (synpred5_PBFParser())) {s = 1;}

                        else if ( (LA16_0==INT_LITERAL) ) {s = 2;}

                        else if ( (LA16_0==DOUBLE_LITERAL) ) {s = 3;}

                        else if ( (LA16_0==STRING_LITERAL) ) {s = 4;}

                        else if ( (LA16_0==LT) ) {s = 5;}

                        else if ( (LA16_0==ID) ) {s = 6;}

                        else if ( ((LA16_0>=PLUS && LA16_0<=MINUS)||LA16_0==LPAREN) ) {s = 7;}

                        else if ( ((LA16_0>=LIBRARY && LA16_0<=UNSPECIFIED)) ) {s = 8;}

                        else if ( (LA16_0==LBRACKET) ) {s = 9;}

                         
                        input.seek(index16_0);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA16_6 = input.LA(1);

                         
                        int index16_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA16_6==LBRACE||LA16_6==EQ) ) {s = 8;}

                        else if ( (synpred9_PBFParser()) ) {s = 14;}

                        else if ( (true) ) {s = 7;}

                         
                        input.seek(index16_6);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 16, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA30_eotS =
        "\31\uffff";
    static final String DFA30_eofS =
        "\31\uffff";
    static final String DFA30_minS =
        "\1\51\1\47\1\56\1\23\1\24\2\uffff\1\36\1\27\1\56\1\24\1\20\1\56"+
        "\1\31\1\36\1\20\1\24\1\56\1\31\1\24\1\20\1\31\1\20\1\24\1\31";
    static final String DFA30_maxS =
        "\1\51\1\47\1\56\1\36\1\56\2\uffff\2\36\1\56\1\34\1\52\1\56\1\34"+
        "\1\36\1\52\1\34\1\56\1\31\1\34\1\52\1\34\1\52\1\34\1\31";
    static final String DFA30_acceptS =
        "\5\uffff\1\2\1\1\22\uffff";
    static final String DFA30_specialS =
        "\31\uffff}>";
    static final String[] DFA30_transitionS = {
            "\1\1",
            "\1\2",
            "\1\3",
            "\1\4\3\uffff\1\5\6\uffff\1\6",
            "\1\10\31\uffff\1\7",
            "",
            "",
            "\1\11",
            "\1\5\6\uffff\1\6",
            "\1\12",
            "\1\10\5\uffff\1\13\1\uffff\1\14",
            "\1\15\31\uffff\1\15",
            "\1\16",
            "\1\20\2\uffff\1\17",
            "\1\21",
            "\1\22\31\uffff\1\22",
            "\1\10\7\uffff\1\14",
            "\1\23",
            "\1\20",
            "\1\10\5\uffff\1\24\1\uffff\1\14",
            "\1\25\31\uffff\1\25",
            "\1\27\2\uffff\1\26",
            "\1\30\31\uffff\1\30",
            "\1\10\7\uffff\1\14",
            "\1\27"
    };

    static final short[] DFA30_eot = DFA.unpackEncodedString(DFA30_eotS);
    static final short[] DFA30_eof = DFA.unpackEncodedString(DFA30_eofS);
    static final char[] DFA30_min = DFA.unpackEncodedStringToUnsignedChars(DFA30_minS);
    static final char[] DFA30_max = DFA.unpackEncodedStringToUnsignedChars(DFA30_maxS);
    static final short[] DFA30_accept = DFA.unpackEncodedString(DFA30_acceptS);
    static final short[] DFA30_special = DFA.unpackEncodedString(DFA30_specialS);
    static final short[][] DFA30_transition;

    static {
        int numStates = DFA30_transitionS.length;
        DFA30_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA30_transition[i] = DFA.unpackEncodedString(DFA30_transitionS[i]);
        }
    }

    class DFA30 extends DFA {

        public DFA30(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 30;
            this.eot = DFA30_eot;
            this.eof = DFA30_eof;
            this.min = DFA30_min;
            this.max = DFA30_max;
            this.accept = DFA30_accept;
            this.special = DFA30_special;
            this.transition = DFA30_transition;
        }
        public String getDescription() {
            return "299:1: templateProcess : (t= TEMPLATE PROCESS id= ID (par= parameters )? sp= superProcess block -> ^( STRUCT[$t, \"TP\"] ID ^( ID[$id, \"parameters\"] ( $par)? ) ^( ID[$sp.tree.getToken(), \"super\"] $sp) ( block )? ) | t= TEMPLATE PROCESS id= ID (par= parameters )? block -> ^( STRUCT[$t, \"TP\"] ID ^( ID[$id, \"parameters\"] ( $par)? ) ^( ID[$id, \"super\"] ID[$id, \"Process\"] ) ( block )? ) );";
        }
    }
    static final String DFA45_eotS =
        "\12\uffff";
    static final String DFA45_eofS =
        "\12\uffff";
    static final String DFA45_minS =
        "\1\44\1\uffff\1\56\1\uffff\1\36\1\53\1\uffff\1\33\2\uffff";
    static final String DFA45_maxS =
        "\1\54\1\uffff\1\56\1\uffff\1\36\1\56\1\uffff\1\56\2\uffff";
    static final String DFA45_acceptS =
        "\1\uffff\1\1\1\uffff\1\3\2\uffff\1\2\1\uffff\1\4\1\5";
    static final String DFA45_specialS =
        "\12\uffff}>";
    static final String[] DFA45_transitionS = {
            "\1\1\1\2\6\uffff\1\3",
            "",
            "\1\4",
            "",
            "\1\5",
            "\1\7\2\uffff\1\6",
            "",
            "\1\10\22\uffff\1\11",
            "",
            ""
    };

    static final short[] DFA45_eot = DFA.unpackEncodedString(DFA45_eotS);
    static final short[] DFA45_eof = DFA.unpackEncodedString(DFA45_eofS);
    static final char[] DFA45_min = DFA.unpackEncodedStringToUnsignedChars(DFA45_minS);
    static final char[] DFA45_max = DFA.unpackEncodedStringToUnsignedChars(DFA45_maxS);
    static final short[] DFA45_accept = DFA.unpackEncodedString(DFA45_acceptS);
    static final short[] DFA45_special = DFA.unpackEncodedString(DFA45_specialS);
    static final short[][] DFA45_transition;

    static {
        int numStates = DFA45_transitionS.length;
        DFA45_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA45_transition[i] = DFA.unpackEncodedString(DFA45_transitionS[i]);
        }
    }

    class DFA45 extends DFA {

        public DFA45(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 45;
            this.eot = DFA45_eot;
            this.eof = DFA45_eof;
            this.min = DFA45_min;
            this.max = DFA45_max;
            this.accept = DFA45_accept;
            this.special = DFA45_special;
            this.transition = DFA45_transition;
        }
        public String getDescription() {
            return "464:1: header : ( libDecl | modelDecl | incompleteModelDecl | fullModelDecl | mixedModelDecl );";
        }
    }
    static final String DFA46_eotS =
        "\12\uffff";
    static final String DFA46_eofS =
        "\1\2\11\uffff";
    static final String DFA46_minS =
        "\1\46\1\uffff\4\0\4\uffff";
    static final String DFA46_maxS =
        "\1\51\1\uffff\4\0\4\uffff";
    static final String DFA46_acceptS =
        "\1\uffff\1\1\4\uffff\1\2\1\3\1\4\1\5";
    static final String DFA46_specialS =
        "\1\0\1\uffff\1\1\1\2\1\3\1\4\4\uffff}>";
    static final String[] DFA46_transitionS = {
            "\1\5\1\4\1\3\1\1",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA46_eot = DFA.unpackEncodedString(DFA46_eotS);
    static final short[] DFA46_eof = DFA.unpackEncodedString(DFA46_eofS);
    static final char[] DFA46_min = DFA.unpackEncodedStringToUnsignedChars(DFA46_minS);
    static final char[] DFA46_max = DFA.unpackEncodedStringToUnsignedChars(DFA46_maxS);
    static final short[] DFA46_accept = DFA.unpackEncodedString(DFA46_acceptS);
    static final short[] DFA46_special = DFA.unpackEncodedString(DFA46_specialS);
    static final short[][] DFA46_transition;

    static {
        int numStates = DFA46_transitionS.length;
        DFA46_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA46_transition[i] = DFA.unpackEncodedString(DFA46_transitionS[i]);
        }
    }

    class DFA46 extends DFA {

        public DFA46(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 46;
            this.eot = DFA46_eot;
            this.eof = DFA46_eof;
            this.min = DFA46_min;
            this.max = DFA46_max;
            this.accept = DFA46_accept;
            this.special = DFA46_special;
            this.transition = DFA46_transition;
        }
        public String getDescription() {
            return "472:1: defs : ({...}? => templateDefs | {...}? => instanceDefs | {...}? => incompleteDefs | {...}? => fullDefs | {...}? => mixedDefs );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA46_0 = input.LA(1);

                         
                        int index46_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA46_0==TEMPLATE) && (((fileType == Type.LIBRARY)))) {s = 1;}

                        else if ( (LA46_0==EOF) && ((((fileType == Type.INCOMPLETE_MODEL))||((fileType == Type.FULL_MODEL))||((fileType == Type.MIXED_MODEL))||((fileType == Type.MODEL))||((fileType == Type.LIBRARY))))) {s = 2;}

                        else if ( (LA46_0==ENTITY) && ((((fileType == Type.INCOMPLETE_MODEL))||((fileType == Type.FULL_MODEL))||((fileType == Type.MIXED_MODEL))||((fileType == Type.MODEL))))) {s = 3;}

                        else if ( (LA46_0==PROCESS) && ((((fileType == Type.INCOMPLETE_MODEL))||((fileType == Type.FULL_MODEL))||((fileType == Type.MIXED_MODEL))||((fileType == Type.MODEL))))) {s = 4;}

                        else if ( (LA46_0==COMPARTMENT) && ((((fileType == Type.INCOMPLETE_MODEL))||((fileType == Type.MODEL))))) {s = 5;}

                         
                        input.seek(index46_0);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA46_2 = input.LA(1);

                         
                        int index46_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (((fileType == Type.LIBRARY))) ) {s = 1;}

                        else if ( (((fileType == Type.MODEL))) ) {s = 6;}

                        else if ( (((fileType == Type.INCOMPLETE_MODEL))) ) {s = 7;}

                        else if ( (((fileType == Type.FULL_MODEL))) ) {s = 8;}

                        else if ( (((fileType == Type.MIXED_MODEL))) ) {s = 9;}

                         
                        input.seek(index46_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA46_3 = input.LA(1);

                         
                        int index46_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (((fileType == Type.MODEL))) ) {s = 6;}

                        else if ( (((fileType == Type.INCOMPLETE_MODEL))) ) {s = 7;}

                        else if ( (((fileType == Type.FULL_MODEL))) ) {s = 8;}

                        else if ( (((fileType == Type.MIXED_MODEL))) ) {s = 9;}

                         
                        input.seek(index46_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA46_4 = input.LA(1);

                         
                        int index46_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (((fileType == Type.MODEL))) ) {s = 6;}

                        else if ( (((fileType == Type.INCOMPLETE_MODEL))) ) {s = 7;}

                        else if ( (((fileType == Type.FULL_MODEL))) ) {s = 8;}

                        else if ( (((fileType == Type.MIXED_MODEL))) ) {s = 9;}

                         
                        input.seek(index46_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA46_5 = input.LA(1);

                         
                        int index46_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (((fileType == Type.MODEL))) ) {s = 6;}

                        else if ( (((fileType == Type.INCOMPLETE_MODEL))) ) {s = 7;}

                         
                        input.seek(index46_5);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 46, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

    public static final BitSet FOLLOW_set_in_literal0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_keyword0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_keyword_in_structure391 = new BitSet(new long[]{0x00004FF000000000L});
    public static final BitSet FOLLOW_ID_in_structure394 = new BitSet(new long[]{0x0000000200800000L});
    public static final BitSet FOLLOW_block_in_structure404 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_EQ_in_structure407 = new BitSet(new long[]{0x00004FF0042F0600L});
    public static final BitSet FOLLOW_value_in_structure409 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EQ_in_structure421 = new BitSet(new long[]{0x00004FF0042F0600L});
    public static final BitSet FOLLOW_value_in_structure423 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_keyword_in_structureNull512 = new BitSet(new long[]{0x00004FF000000000L});
    public static final BitSet FOLLOW_ID_in_structureNull515 = new BitSet(new long[]{0x0000000200800000L});
    public static final BitSet FOLLOW_blockNull_in_structureNull525 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_EQ_in_structureNull528 = new BitSet(new long[]{0x00006FF0042F0600L});
    public static final BitSet FOLLOW_valueNull_in_structureNull530 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EQ_in_structureNull542 = new BitSet(new long[]{0x00006FF0042F0600L});
    public static final BitSet FOLLOW_valueNull_in_structureNull544 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_block634 = new BitSet(new long[]{0x0000400001000000L});
    public static final BitSet FOLLOW_propDefs_in_block643 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACE_in_block651 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_blockNull667 = new BitSet(new long[]{0x0000400001000000L});
    public static final BitSet FOLLOW_propDefsNull_in_blockNull676 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACE_in_blockNull682 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_propDef_in_propDefs699 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_SEMI_in_propDefs702 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_propDef_in_propDefs706 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_SEMI_in_propDefs710 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_propDefNull_in_propDefsNull754 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_SEMI_in_propDefsNull757 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_propDefNull_in_propDefsNull761 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_SEMI_in_propDefsNull765 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_propDef800 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_COLON_in_propDef803 = new BitSet(new long[]{0x00004FF0042F0600L});
    public static final BitSet FOLLOW_valueList_in_propDef806 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_propDefNull819 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_COLON_in_propDefNull822 = new BitSet(new long[]{0x00006FF0042F0600L});
    public static final BitSet FOLLOW_valueListNull_in_propDefNull825 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_in_valueList839 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_COMMA_in_valueList842 = new BitSet(new long[]{0x00004FF0042F0600L});
    public static final BitSet FOLLOW_value_in_valueList845 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_valueNull_in_valueListNull860 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_COMMA_in_valueListNull863 = new BitSet(new long[]{0x00006FF0042F0600L});
    public static final BitSet FOLLOW_valueNull_in_valueListNull866 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_INT_LITERAL_in_value913 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOUBLE_LITERAL_in_value948 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_value983 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interval_in_value989 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_value1032 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_value1038 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_structure_in_value1044 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_list_in_value1050 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NULL_in_valueNull1102 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_LITERAL_in_valueNull1140 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOUBLE_LITERAL_in_valueNull1175 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_valueNull1210 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interval_in_valueNull1216 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_valueNull1259 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_valueNull1265 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_structureNull_in_valueNull1271 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_list_in_valueNull1277 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_list1293 = new BitSet(new long[]{0x00004FF0042F0600L});
    public static final BitSet FOLLOW_value_in_list1301 = new BitSet(new long[]{0x0000000010400000L});
    public static final BitSet FOLLOW_COMMA_in_list1304 = new BitSet(new long[]{0x00004FF0042F0600L});
    public static final BitSet FOLLOW_value_in_list1306 = new BitSet(new long[]{0x0000000010400000L});
    public static final BitSet FOLLOW_RBRACKET_in_list1314 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_interval1376 = new BitSet(new long[]{0x0000040000030600L});
    public static final BitSet FOLLOW_signedNumberOrInfinity_in_interval1380 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_COMMA_in_interval1382 = new BitSet(new long[]{0x0000040000030600L});
    public static final BitSet FOLLOW_signedNumberOrInfinity_in_interval1386 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_GT_in_interval1388 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_signedNumber_in_signedNumberOrInfinity1421 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_signedInfinity_in_signedNumberOrInfinity1427 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_signedInteger_in_signedNumber1440 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_signedDouble_in_signedNumber1446 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sign_in_signedInteger1459 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_INT_LITERAL_in_signedInteger1463 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sign_in_signedDouble1476 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_DOUBLE_LITERAL_in_signedDouble1480 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sign_in_signedInfinity1493 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_INF_in_signedInfinity1497 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_integerOrInfinity0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_parameters1534 = new BitSet(new long[]{0x0000400000100000L});
    public static final BitSet FOLLOW_parameterList_in_parameters1537 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_RPAREN_in_parameters1540 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameter_in_parameterList1554 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_COMMA_in_parameterList1557 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_parameter_in_parameterList1560 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_ID_in_parameter1576 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_COLON_in_parameter1580 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_parameter1584 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_card_in_parameter1588 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_card1655 = new BitSet(new long[]{0x0000040000010000L});
    public static final BitSet FOLLOW_integerOrInfinity_in_card1659 = new BitSet(new long[]{0x0000000012000000L});
    public static final BitSet FOLLOW_COMMA_in_card1662 = new BitSet(new long[]{0x0000040000010000L});
    public static final BitSet FOLLOW_integerOrInfinity_in_card1666 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_GT_in_card1670 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COLON_in_superEntity1709 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_superEntity1712 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COLON_in_entityType1725 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_entityType1728 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COLON_in_superProcess1742 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_superProcess1745 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COLON_in_processType1758 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_processType1761 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COLON_in_compType1774 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_compType1777 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TEMPLATE_in_templateEntity1796 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_ENTITY_in_templateEntity1798 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_templateEntity1802 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_superEntity_in_templateEntity1806 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_templateEntity1823 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TEMPLATE_in_templateEntity1853 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_ENTITY_in_templateEntity1855 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_templateEntity1859 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_templateEntity1887 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TEMPLATE_in_templateProcess1924 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_PROCESS_in_templateProcess1926 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_templateProcess1930 = new BitSet(new long[]{0x0000000040080000L});
    public static final BitSet FOLLOW_parameters_in_templateProcess1934 = new BitSet(new long[]{0x0000000040080000L});
    public static final BitSet FOLLOW_superProcess_in_templateProcess1939 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_templateProcess1944 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TEMPLATE_in_templateProcess1983 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_PROCESS_in_templateProcess1985 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_templateProcess1989 = new BitSet(new long[]{0x0000000000880000L});
    public static final BitSet FOLLOW_parameters_in_templateProcess1993 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_templateProcess2011 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ENTITY_in_instanceEntity2057 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_instanceEntity2061 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_entityType_in_instanceEntity2065 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_instanceEntity2090 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PROCESS_in_instanceProcess2127 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_instanceProcess2131 = new BitSet(new long[]{0x0000000040080000L});
    public static final BitSet FOLLOW_arguments1_in_instanceProcess2135 = new BitSet(new long[]{0x0000000040080000L});
    public static final BitSet FOLLOW_processType_in_instanceProcess2140 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_instanceProcess2153 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ENTITY_in_incompleteEntity2199 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_incompleteEntity2203 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_entityType_in_incompleteEntity2207 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_blockNull_in_incompleteEntity2232 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PROCESS_in_incompleteProcess2266 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_incompleteProcess2270 = new BitSet(new long[]{0x0000000040080000L});
    public static final BitSet FOLLOW_arguments1_in_incompleteProcess2274 = new BitSet(new long[]{0x0000000040080000L});
    public static final BitSet FOLLOW_processType_in_incompleteProcess2279 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_blockNull_in_incompleteProcess2292 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ENTITY_in_fullEntity2338 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_fullEntity2342 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_fullEntity2375 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PROCESS_in_fullProcess2406 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_fullProcess2410 = new BitSet(new long[]{0x0000000000880000L});
    public static final BitSet FOLLOW_arguments1_in_fullProcess2414 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_fullProcess2439 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ENTITY_in_mixedEntity2479 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_mixedEntity2483 = new BitSet(new long[]{0x0000000040800000L});
    public static final BitSet FOLLOW_entityType_in_mixedEntity2487 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_mixedEntity2512 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PROCESS_in_mixedProcess2550 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_mixedProcess2554 = new BitSet(new long[]{0x0000000040880000L});
    public static final BitSet FOLLOW_arguments1_in_mixedProcess2558 = new BitSet(new long[]{0x0000000040880000L});
    public static final BitSet FOLLOW_processType_in_mixedProcess2564 = new BitSet(new long[]{0x0000000000880000L});
    public static final BitSet FOLLOW_parameters_in_mixedProcess2568 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_mixedProcess2573 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TEMPLATE_in_templateCompartment2629 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_COMPARTMENT_in_templateCompartment2631 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_templateCompartment2633 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_block_in_templateCompartment2661 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COMPARTMENT_in_instanceCompartment2692 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_instanceCompartment2696 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_compType_in_instanceCompartment2700 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_compBlock_in_instanceCompartment2722 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COMPARTMENT_in_incompleteCompartment2757 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_incompleteCompartment2761 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_compType_in_incompleteCompartment2765 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_compBlockNull_in_incompleteCompartment2787 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_compBlock2822 = new BitSet(new long[]{0x000001C001000000L});
    public static final BitSet FOLLOW_instanceEntity_in_compBlock2833 = new BitSet(new long[]{0x000001C001000000L});
    public static final BitSet FOLLOW_instanceProcess_in_compBlock2839 = new BitSet(new long[]{0x000001C001000000L});
    public static final BitSet FOLLOW_instanceCompartment_in_compBlock2846 = new BitSet(new long[]{0x000001C001000000L});
    public static final BitSet FOLLOW_RBRACE_in_compBlock2871 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_compBlockNull2958 = new BitSet(new long[]{0x000001C001000000L});
    public static final BitSet FOLLOW_incompleteEntity_in_compBlockNull2969 = new BitSet(new long[]{0x000001C001000000L});
    public static final BitSet FOLLOW_incompleteProcess_in_compBlockNull2975 = new BitSet(new long[]{0x000001C001000000L});
    public static final BitSet FOLLOW_incompleteCompartment_in_compBlockNull2982 = new BitSet(new long[]{0x000001C001000000L});
    public static final BitSet FOLLOW_RBRACE_in_compBlockNull3007 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_templateEntity_in_templateDefs3098 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_templateProcess_in_templateDefs3104 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_templateCompartment_in_templateDefs3111 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_instanceEntity_in_instanceDefs3158 = new BitSet(new long[]{0x000001C000000002L});
    public static final BitSet FOLLOW_instanceProcess_in_instanceDefs3165 = new BitSet(new long[]{0x000001C000000002L});
    public static final BitSet FOLLOW_instanceCompartment_in_instanceDefs3172 = new BitSet(new long[]{0x000001C000000002L});
    public static final BitSet FOLLOW_incompleteEntity_in_incompleteDefs3221 = new BitSet(new long[]{0x000001C000000002L});
    public static final BitSet FOLLOW_incompleteProcess_in_incompleteDefs3228 = new BitSet(new long[]{0x000001C000000002L});
    public static final BitSet FOLLOW_incompleteCompartment_in_incompleteDefs3235 = new BitSet(new long[]{0x000001C000000002L});
    public static final BitSet FOLLOW_fullEntity_in_fullDefs3284 = new BitSet(new long[]{0x0000018000000002L});
    public static final BitSet FOLLOW_fullProcess_in_fullDefs3290 = new BitSet(new long[]{0x0000018000000002L});
    public static final BitSet FOLLOW_mixedEntity_in_mixedDefs3355 = new BitSet(new long[]{0x0000018000000002L});
    public static final BitSet FOLLOW_mixedProcess_in_mixedDefs3361 = new BitSet(new long[]{0x0000018000000002L});
    public static final BitSet FOLLOW_LIBRARY_in_libDecl3427 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_libDecl3429 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_libDecl3431 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MODEL_in_modelDecl3482 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_modelDecl3486 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_COLON_in_modelDecl3490 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_modelDecl3494 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_modelDecl3496 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INCOMPLETE_in_incompleteModelDecl3543 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_MODEL_in_incompleteModelDecl3545 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_incompleteModelDecl3549 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_COLON_in_incompleteModelDecl3553 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_incompleteModelDecl3557 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_incompleteModelDecl3559 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MODEL_in_fullModelDecl3597 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_fullModelDecl3599 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_COLON_in_fullModelDecl3603 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_UNSPECIFIED_in_fullModelDecl3605 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_fullModelDecl3607 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MODEL_in_mixedModelDecl3653 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_mixedModelDecl3657 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_COLON_in_mixedModelDecl3661 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_UNSPECIFIED_in_mixedModelDecl3663 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_mixedModelDecl3667 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_mixedModelDecl3669 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_libDecl_in_libFile3711 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_templateDefs_in_libFile3717 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modelDecl_in_modelFile3777 = new BitSet(new long[]{0x000001C000000000L});
    public static final BitSet FOLLOW_instanceDefs_in_modelFile3783 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_incompleteModelDecl_in_incompleteFile3845 = new BitSet(new long[]{0x000001C000000000L});
    public static final BitSet FOLLOW_incompleteDefs_in_incompleteFile3851 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fullModelDecl_in_fullFile3864 = new BitSet(new long[]{0x0000018000000000L});
    public static final BitSet FOLLOW_fullDefs_in_fullFile3870 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mixedModelDecl_in_mixedFile3934 = new BitSet(new long[]{0x0000018000000000L});
    public static final BitSet FOLLOW_mixedDefs_in_mixedFile3940 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_libDecl_in_header4005 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modelDecl_in_header4021 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_incompleteModelDecl_in_header4035 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fullModelDecl_in_header4043 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mixedModelDecl_in_header4055 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_templateDefs_in_defs4084 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_instanceDefs_in_defs4101 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_incompleteDefs_in_defs4111 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fullDefs_in_defs4125 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mixedDefs_in_defs4138 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_header_in_file4151 = new BitSet(new long[]{0x000003C000000000L});
    public static final BitSet FOLLOW_defs_in_file4157 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_file4161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentExpression_in_expression4213 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_eqExpression_in_assignmentExpression4226 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_EQ_in_assignmentExpression4229 = new BitSet(new long[]{0x00004000040F0600L});
    public static final BitSet FOLLOW_assignmentExpression_in_assignmentExpression4232 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_relExpression_in_eqExpression4247 = new BitSet(new long[]{0x0000000C00000002L});
    public static final BitSet FOLLOW_eqOperator_in_eqExpression4250 = new BitSet(new long[]{0x00004000040F0600L});
    public static final BitSet FOLLOW_relExpression_in_eqExpression4253 = new BitSet(new long[]{0x0000000C00000002L});
    public static final BitSet FOLLOW_addExpression_in_relExpression4270 = new BitSet(new long[]{0x0000000006000002L});
    public static final BitSet FOLLOW_relOperator_in_relExpression4273 = new BitSet(new long[]{0x00004000040F0600L});
    public static final BitSet FOLLOW_addExpression_in_relExpression4276 = new BitSet(new long[]{0x0000000006000002L});
    public static final BitSet FOLLOW_mulExpression_in_addExpression4291 = new BitSet(new long[]{0x0000000000000602L});
    public static final BitSet FOLLOW_addOperator_in_addExpression4294 = new BitSet(new long[]{0x00004000040F0600L});
    public static final BitSet FOLLOW_mulExpression_in_addExpression4297 = new BitSet(new long[]{0x0000000000000602L});
    public static final BitSet FOLLOW_unaryExpression_in_mulExpression4314 = new BitSet(new long[]{0x0000000180000002L});
    public static final BitSet FOLLOW_mulOperator_in_mulExpression4317 = new BitSet(new long[]{0x00004000040F0600L});
    public static final BitSet FOLLOW_unaryExpression_in_mulExpression4320 = new BitSet(new long[]{0x0000000180000002L});
    public static final BitSet FOLLOW_unaryOperator_in_unaryExpression4337 = new BitSet(new long[]{0x00004000040F0600L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression4340 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primary_in_unaryExpression4346 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parExpression_in_primary4359 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_iterOrIDArg_in_primary4365 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_DOT_in_primary4368 = new BitSet(new long[]{0x0000400004000000L});
    public static final BitSet FOLLOW_iterOrIDArg_in_primary4371 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_literal_in_primary4386 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_iterOrID_in_iterOrIDArg4402 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_arguments_in_iterOrIDArg4410 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_iterOrID4439 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_iterator_in_iterOrID4445 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_iterator4460 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_iterator4464 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_COLON_in_iterator4466 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_iterator4470 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_GT_in_iterator4472 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_parExpression4526 = new BitSet(new long[]{0x00004000040F0600L});
    public static final BitSet FOLLOW_expression_in_parExpression4529 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_RPAREN_in_parExpression4531 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_arguments4545 = new BitSet(new long[]{0x00004FF0043F0600L});
    public static final BitSet FOLLOW_valueList_in_arguments4548 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_RPAREN_in_arguments4551 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_arguments14611 = new BitSet(new long[]{0x0000400000300000L});
    public static final BitSet FOLLOW_argumentList_in_arguments14614 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_RPAREN_in_arguments14617 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_argument_in_argumentList4633 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_COMMA_in_argumentList4636 = new BitSet(new long[]{0x0000400000200000L});
    public static final BitSet FOLLOW_argument_in_argumentList4639 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_ref_in_argument4658 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_refList_in_argument4702 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_refListList_in_argument4745 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_refList4788 = new BitSet(new long[]{0x0000400000400000L});
    public static final BitSet FOLLOW_ref_in_refList4797 = new BitSet(new long[]{0x0000000010400000L});
    public static final BitSet FOLLOW_COMMA_in_refList4800 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ref_in_refList4802 = new BitSet(new long[]{0x0000000010400000L});
    public static final BitSet FOLLOW_RBRACKET_in_refList4812 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_refListList4857 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_LBRACKET_in_refListList4863 = new BitSet(new long[]{0x0000400000400000L});
    public static final BitSet FOLLOW_ref_in_refListList4874 = new BitSet(new long[]{0x0000000010400000L});
    public static final BitSet FOLLOW_COMMA_in_refListList4877 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ref_in_refListList4881 = new BitSet(new long[]{0x0000000010400000L});
    public static final BitSet FOLLOW_RBRACKET_in_refListList4891 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_COMMA_in_refListList4898 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_LBRACKET_in_refListList4904 = new BitSet(new long[]{0x0000400000400000L});
    public static final BitSet FOLLOW_ref_in_refListList4915 = new BitSet(new long[]{0x0000000010400000L});
    public static final BitSet FOLLOW_COMMA_in_refListList4918 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ref_in_refListList4922 = new BitSet(new long[]{0x0000000010400000L});
    public static final BitSet FOLLOW_RBRACKET_in_refListList4932 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_RBRACKET_in_refListList4939 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_ref5001 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_DOT_in_ref5006 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_ID_in_ref5010 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_set_in_eqOperator0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_relOperator5071 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_EQ_in_relOperator5073 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_relOperator5079 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_EQ_in_relOperator5081 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_relOperator5087 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_relOperator5093 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_addOperator0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_mulOperator0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_unaryOperator0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_sign0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_LITERAL_in_synpred1_PBFParser882 = new BitSet(new long[]{0x0000000019500000L});
    public static final BitSet FOLLOW_set_in_synpred1_PBFParser887 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOUBLE_LITERAL_in_synpred2_PBFParser920 = new BitSet(new long[]{0x0000000019500000L});
    public static final BitSet FOLLOW_set_in_synpred2_PBFParser922 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_synpred3_PBFParser955 = new BitSet(new long[]{0x0000000019500000L});
    public static final BitSet FOLLOW_set_in_synpred3_PBFParser957 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_synpred4_PBFParser996 = new BitSet(new long[]{0x0000000019500000L});
    public static final BitSet FOLLOW_set_in_synpred4_PBFParser1006 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NULL_in_synpred5_PBFParser1066 = new BitSet(new long[]{0x0000000019500000L});
    public static final BitSet FOLLOW_set_in_synpred5_PBFParser1076 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_LITERAL_in_synpred6_PBFParser1109 = new BitSet(new long[]{0x0000000019500000L});
    public static final BitSet FOLLOW_set_in_synpred6_PBFParser1114 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOUBLE_LITERAL_in_synpred7_PBFParser1147 = new BitSet(new long[]{0x0000000019500000L});
    public static final BitSet FOLLOW_set_in_synpred7_PBFParser1149 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_synpred8_PBFParser1182 = new BitSet(new long[]{0x0000000019500000L});
    public static final BitSet FOLLOW_set_in_synpred8_PBFParser1184 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_synpred9_PBFParser1223 = new BitSet(new long[]{0x0000000019500000L});
    public static final BitSet FOLLOW_set_in_synpred9_PBFParser1233 = new BitSet(new long[]{0x0000000000000002L});

}