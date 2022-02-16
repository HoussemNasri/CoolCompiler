/*
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 65536;
	private final int YY_EOF = 65537;

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */
    // Max size of string constants
    static int MAX_STR_CONST = 1025;
    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();
    int get_curr_lineno() {
	return yyline + 1;
    }
    private AbstractSymbol filename;
    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }
    AbstractSymbol curr_filename() {
	return filename;
    }
	private int commentLevel = 0;
	String cleanStringLiteral(String literal){
		if(literal.length() == 0) return "";
		//if(literal.charAt(0) == '"') literal = literal.substring(1);
		//if(literal.charAt(literal.length() - 1) == '"') literal = literal.substring(0, literal.length() - 1);
		String result = "";	
		int consecutiveBackslashes = 0;
		for(int i = 0;i < literal.length();i++){
			char c = literal.charAt(i);
			if(c == '\\' && i < literal.length() - 1){
				consecutiveBackslashes++;
				char next = literal.charAt(i + 1);
				if(consecutiveBackslashes % 2 == 1){
					if(next == 'n'){
						result+='\n';
					}else if(next == 'f'){
						result+='\f';
					}
					else if(next == 't'){
						result+='\t';
					}
					else if(next == 'b'){
						result+='\b';
					}else{
						result+=next;
					}
				}
				else{
					result+=next;
				}
				if(next == '\\'){
					consecutiveBackslashes++;
				}else{
					consecutiveBackslashes = 0;
				}
				i++;
			}else{
				result += c;
				consecutiveBackslashes = 0;
			}
		}
		return result;
	}
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yychar;
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yychar = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int STRING_ERROR = 4;
	private final int YYINITIAL = 0;
	private final int SINGLE_LINE_COMMENT = 2;
	private final int COMMENT_BLOCK = 1;
	private final int STRING_BLOCK = 3;
	private final int yy_state_dtrans[] = {
		0,
		37,
		32,
		34,
		45
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yychar = yychar
			+ yy_buffer_index - yy_buffer_start;
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NOT_ACCEPT,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NOT_ACCEPT,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NOT_ACCEPT,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,65538,
"2:8,8:2,3,8:2,7,2:18,8,2,28,2:5,4,6,5,42,38,1,39,43,30:10,35,34,33,31,32,2," +
"41,11,26,9,22,13,14,26,17,15,26:2,10,26,16,21,23,26,18,12,19,25,20,24,26:3," +
"2,29,2:2,27,2,11,26,9,22,13,14,26,17,15,26:2,10,26,16,21,23,26,18,12,19,25," +
"20,24,26:3,36,2,37,40,2:65409,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,82,
"0,1,2,1,3,4,5,1,6,1,7,8,9,1:13,10,1:5,11,1,12,1:2,13,14,4,15,16,17,18,19,20" +
",21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45" +
",46,47,48,49,50,10,51,52,53,54,55")[0];

	private int yy_nxt[][] = unpackFromString(56,44,
"1,2,3,4,5,6,7,4:2,8,56,76:2,77,38,43,58,76:2,78,76,46,76,79,80,76:2,3,9,3,1" +
"0,11,3,12,13,14,15,16,17,18,19,20,21,22,-1:45,23,-1:45,4,-1:3,4:2,-1:40,24," +
"-1:44,25,-1:46,76,81,59,76:16,-1:2,76,-1:43,10,-1:45,27,-1:12,28,-1:29,29,-" +
"1:21,76:19,-1:2,76,-1:13,1,40:2,33,40:3,-1,40:36,1,41:2,35,41:24,36,42,41:1" +
"4,1,30:3,39,44,30:38,-1:9,76:2,62,76:3,26,76:12,-1:2,76,-1:14,40:2,-1,40:3," +
"-1,40:36,-1,41:2,-1,41:24,-1,42,41:14,-1,41:6,-1,41:36,-1:9,76:3,63,76,26,7" +
"6,57,76:11,-1:2,76,-1:19,31,-1:37,1,-1:2,35,-1:24,36,-1:24,76:5,26,76:13,-1" +
":2,76,-1:22,76:10,26,76:8,-1:2,76,-1:22,76:15,26,76:3,-1:2,76,-1:22,76:4,26" +
",76:14,-1:2,76,-1:22,76:14,26,76:4,-1:2,76,-1:22,26,76:18,-1:2,76,-1:22,76:" +
"7,26,76:11,-1:2,76,-1:22,76,26,76:17,-1:2,76,-1:22,76:3,26,76:15,-1:2,76,-1" +
":22,76:13,26,76:5,-1:2,76,-1:22,76:4,47,76:7,60,76:6,-1:2,76,-1:22,76:8,70," +
"76:10,-1:2,76,-1:22,76:4,48,76:7,47,76:6,-1:2,76,-1:22,76:3,49,76:15,-1:2,7" +
"6,-1:22,76:12,50,76:6,-1:2,76,-1:22,76:2,51,76:16,-1:2,76,-1:22,76,59,76:17" +
",-1:2,76,-1:22,76:11,69,76:7,-1:2,76,-1:22,76:4,52,76:14,-1:2,76,-1:22,76:1" +
"6,49,76:2,-1:2,76,-1:22,76:12,53,76:6,-1:2,76,-1:22,76:6,71,76:12,-1:2,76,-" +
"1:22,76:3,54,76:15,-1:2,76,-1:22,76:12,72,76:6,-1:2,76,-1:22,76:4,73,76:14," +
"-1:2,76,-1:22,76,49,76:17,-1:2,76,-1:22,76:6,55,76:12,-1:2,76,-1:22,76:9,74" +
",76:9,-1:2,76,-1:22,76:6,75,76:12,-1:2,76,-1:22,76:10,54,76:8,-1:2,76,-1:22" +
",76,59,76,61,76:15,-1:2,76,-1:22,76:8,64,65,76:9,-1:2,76,-1:22,76:12,66,76:" +
"6,-1:2,76,-1:22,76:8,67,76:10,-1:2,76,-1:22,76:2,68,76:16,-1:2,76,-1:13");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */
    switch(yy_lexical_state) {
	case STRING_BLOCK:
		yybegin(YYINITIAL);
		return new Symbol(TokenConstants.ERROR, "EOF in string constant");
	case COMMENT_BLOCK: 
		yybegin(YYINITIAL);
		return new Symbol(TokenConstants.ERROR, "EOF in comment");
    }
    return new Symbol(TokenConstants.EOF);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{
	return new Symbol(TokenConstants.MINUS);
}
					case -3:
						break;
					case 3:
						{ /* This rule should be the very last
    in your lexical specification and
    will match match everything not
    matched by other lexical rules. */
	return new Symbol(TokenConstants.ERROR, yytext());
}
					case -4:
						break;
					case 4:
						{
	//Ignoring whitespaces
}
					case -5:
						break;
					case 5:
						{
	return new Symbol(TokenConstants.LPAREN);
}
					case -6:
						break;
					case 6:
						{
	return new Symbol(TokenConstants.MULT);
}
					case -7:
						break;
					case 7:
						{
	return new Symbol(TokenConstants.RPAREN);
}
					case -8:
						break;
					case 8:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -9:
						break;
					case 9:
						{
	yybegin(STRING_BLOCK);
	string_buf = new StringBuffer();
}
					case -10:
						break;
					case 10:
						{ 
	return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext()));
}
					case -11:
						break;
					case 11:
						{
	return new Symbol(TokenConstants.EQ);
}
					case -12:
						break;
					case 12:
						{
	return new Symbol(TokenConstants.LT);
}
					case -13:
						break;
					case 13:
						{
	return new Symbol(TokenConstants.SEMI);
}
					case -14:
						break;
					case 14:
						{
	return new Symbol(TokenConstants.COLON);
}
					case -15:
						break;
					case 15:
						{
	return new Symbol(TokenConstants.LBRACE);
}
					case -16:
						break;
					case 16:
						{
	return new Symbol(TokenConstants.RBRACE);
}
					case -17:
						break;
					case 17:
						{
	return new Symbol(TokenConstants.COMMA);
}
					case -18:
						break;
					case 18:
						{
	return new Symbol(TokenConstants.DOT);
}
					case -19:
						break;
					case 19:
						{
	return new Symbol(TokenConstants.NEG);
}
					case -20:
						break;
					case 20:
						{
	return new Symbol(TokenConstants.AT);
}
					case -21:
						break;
					case 21:
						{
	return new Symbol(TokenConstants.PLUS);
}
					case -22:
						break;
					case 22:
						{
	return new Symbol(TokenConstants.DIV);
}
					case -23:
						break;
					case 23:
						{
	yybegin(SINGLE_LINE_COMMENT);
}
					case -24:
						break;
					case 24:
						{
	//System.out.println("OPEN_PARENTHESES >>>>"+yychar);
	yybegin(COMMENT_BLOCK);commentLevel++;	
}
					case -25:
						break;
					case 25:
						{
	return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}
					case -26:
						break;
					case 26:
						{
	String match = yytext();
	if(match.equalsIgnoreCase("class")){
		return new Symbol(TokenConstants.CLASS);
	}else if(match.equalsIgnoreCase("else")){
		return new Symbol(TokenConstants.ELSE);
	}else if(match.equalsIgnoreCase("fi")){
		return new Symbol(TokenConstants.FI);
	}else if(match.equalsIgnoreCase("if")){
		return new Symbol(TokenConstants.IF);
	}else if(match.equalsIgnoreCase("in")){
		return new Symbol(TokenConstants.IN);
	}else if(match.equalsIgnoreCase("inherits")){
		return new Symbol(TokenConstants.INHERITS);
	}else if(match.equalsIgnoreCase("isvoid")){
		return new Symbol(TokenConstants.ISVOID);
	}else if(match.equalsIgnoreCase("let")){
		return new Symbol(TokenConstants.LET);
	}else if(match.equalsIgnoreCase("loop")){
		return new Symbol(TokenConstants.LOOP);
	}else if(match.equalsIgnoreCase("pool")){
		return new Symbol(TokenConstants.POOL);
	}else if(match.equalsIgnoreCase("then")){
		return new Symbol(TokenConstants.THEN);
	}else if(match.equalsIgnoreCase("while")){
		return new Symbol(TokenConstants.WHILE);
	}else if(match.equalsIgnoreCase("case")){
		return new Symbol(TokenConstants.CASE);
	}else if(match.equalsIgnoreCase("esac")){
		return new Symbol(TokenConstants.ESAC);
	}else if(match.equalsIgnoreCase("new")){
		return new Symbol(TokenConstants.NEW);
	}else if(match.equalsIgnoreCase("of")){
		return new Symbol(TokenConstants.OF);
	}else if(match.equalsIgnoreCase("true")){
		if(Character.isLowerCase(match.charAt(0))){
			return new Symbol(TokenConstants.BOOL_CONST, true);
		}
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}else if(match.equalsIgnoreCase("false")){
		if(Character.isLowerCase(match.charAt(0))){
			return new Symbol(TokenConstants.BOOL_CONST, false);
		}
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}else /*(match.equalsIgnoreCase("not"))*/{
		return new Symbol(TokenConstants.NOT);
	}
}
					case -27:
						break;
					case 27:
						{ /* Sample lexical rule for "=>" arrow.
    Further lexical rules should be defined
    here, after the last %% separator */
	return new Symbol(TokenConstants.DARROW); 
}
					case -28:
						break;
					case 28:
						{
	return new Symbol(TokenConstants.ASSIGN);
}
					case -29:
						break;
					case 29:
						{
	return new Symbol(TokenConstants.LE);
}
					case -30:
						break;
					case 30:
						{
	//Ignore comment body.
}
					case -31:
						break;
					case 31:
						{
	if(--commentLevel == 0){
		yybegin(YYINITIAL);
		//System.out.println("CLOSED_PARENTHESES >>>>"+yychar);
	}
}
					case -32:
						break;
					case 32:
						{
	//Ignore
}
					case -33:
						break;
					case 33:
						{
	yybegin(YYINITIAL);
}
					case -34:
						break;
					case 34:
						{
	for(int i = 0;i < yytext().length();i++){
		if(yytext().charAt(i) == '\0'){
			yybegin(STRING_ERROR);
			return new Symbol(TokenConstants.ERROR, "String contains null character");
		}	
	}
	//System.out.println(">>> " + yytext());
	if(cleanStringLiteral(yytext()).length() >= MAX_STR_CONST){
		yybegin(STRING_ERROR);
		return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
	string_buf.append(yytext());
}
					case -35:
						break;
					case 35:
						{
	if(yy_lexical_state != STRING_ERROR){
		yybegin(YYINITIAL);
		return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
	}
	yybegin(YYINITIAL);
}
					case -36:
						break;
					case 36:
						{
	if(yy_lexical_state != STRING_ERROR){
		yybegin(YYINITIAL);	
		return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(cleanStringLiteral(string_buf.toString())));
	}
	yybegin(YYINITIAL);	 
}
					case -37:
						break;
					case 38:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -38:
						break;
					case 39:
						{
	//Ignore comment body.
}
					case -39:
						break;
					case 40:
						{
	//Ignore
}
					case -40:
						break;
					case 41:
						{
	for(int i = 0;i < yytext().length();i++){
		if(yytext().charAt(i) == '\0'){
			yybegin(STRING_ERROR);
			return new Symbol(TokenConstants.ERROR, "String contains null character");
		}	
	}
	//System.out.println(">>> " + yytext());
	if(cleanStringLiteral(yytext()).length() >= MAX_STR_CONST){
		yybegin(STRING_ERROR);
		return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
	string_buf.append(yytext());
}
					case -41:
						break;
					case 43:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -42:
						break;
					case 44:
						{
	//Ignore comment body.
}
					case -43:
						break;
					case 46:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -44:
						break;
					case 47:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -45:
						break;
					case 48:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -46:
						break;
					case 49:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -47:
						break;
					case 50:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -48:
						break;
					case 51:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -49:
						break;
					case 52:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -50:
						break;
					case 53:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -51:
						break;
					case 54:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -52:
						break;
					case 55:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -53:
						break;
					case 56:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -54:
						break;
					case 57:
						{
	String match = yytext();
	if(match.equalsIgnoreCase("class")){
		return new Symbol(TokenConstants.CLASS);
	}else if(match.equalsIgnoreCase("else")){
		return new Symbol(TokenConstants.ELSE);
	}else if(match.equalsIgnoreCase("fi")){
		return new Symbol(TokenConstants.FI);
	}else if(match.equalsIgnoreCase("if")){
		return new Symbol(TokenConstants.IF);
	}else if(match.equalsIgnoreCase("in")){
		return new Symbol(TokenConstants.IN);
	}else if(match.equalsIgnoreCase("inherits")){
		return new Symbol(TokenConstants.INHERITS);
	}else if(match.equalsIgnoreCase("isvoid")){
		return new Symbol(TokenConstants.ISVOID);
	}else if(match.equalsIgnoreCase("let")){
		return new Symbol(TokenConstants.LET);
	}else if(match.equalsIgnoreCase("loop")){
		return new Symbol(TokenConstants.LOOP);
	}else if(match.equalsIgnoreCase("pool")){
		return new Symbol(TokenConstants.POOL);
	}else if(match.equalsIgnoreCase("then")){
		return new Symbol(TokenConstants.THEN);
	}else if(match.equalsIgnoreCase("while")){
		return new Symbol(TokenConstants.WHILE);
	}else if(match.equalsIgnoreCase("case")){
		return new Symbol(TokenConstants.CASE);
	}else if(match.equalsIgnoreCase("esac")){
		return new Symbol(TokenConstants.ESAC);
	}else if(match.equalsIgnoreCase("new")){
		return new Symbol(TokenConstants.NEW);
	}else if(match.equalsIgnoreCase("of")){
		return new Symbol(TokenConstants.OF);
	}else if(match.equalsIgnoreCase("true")){
		if(Character.isLowerCase(match.charAt(0))){
			return new Symbol(TokenConstants.BOOL_CONST, true);
		}
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}else if(match.equalsIgnoreCase("false")){
		if(Character.isLowerCase(match.charAt(0))){
			return new Symbol(TokenConstants.BOOL_CONST, false);
		}
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}else /*(match.equalsIgnoreCase("not"))*/{
		return new Symbol(TokenConstants.NOT);
	}
}
					case -55:
						break;
					case 58:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -56:
						break;
					case 59:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -57:
						break;
					case 60:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -58:
						break;
					case 61:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -59:
						break;
					case 62:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -60:
						break;
					case 63:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -61:
						break;
					case 64:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -62:
						break;
					case 65:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -63:
						break;
					case 66:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -64:
						break;
					case 67:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -65:
						break;
					case 68:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -66:
						break;
					case 69:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -67:
						break;
					case 70:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -68:
						break;
					case 71:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -69:
						break;
					case 72:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -70:
						break;
					case 73:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -71:
						break;
					case 74:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -72:
						break;
					case 75:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -73:
						break;
					case 76:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -74:
						break;
					case 77:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -75:
						break;
					case 78:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -76:
						break;
					case 79:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -77:
						break;
					case 80:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -78:
						break;
					case 81:
						{
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}
					case -79:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
