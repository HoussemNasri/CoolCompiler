/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;
%%
%{

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

%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

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
%eofval}

%class CoolLexer
%cup
%ignorecase
%line
%char
%type java_cup.runtime.Symbol
%state COMMENT_BLOCK
%state SINGLE_LINE_COMMENT
%state STRING_BLOCK
%state STRING_ERROR
%unicode


ANY = (.|\n)
LPAREN = "("
RPAREN = ")"
WHITESPACE = [ \r\n\t\f\b\013]+
NUMBER = [0-9]+
#STRING_LITERAL = \"(\\(.|\n)|[^\"\\])*\"
STRING_LITERAL = \"(\\(.|\n)|[^\"\\\n])*\"
NEWLINE = [\n]

COMMENT1 = "(*"(\([^\*]|\*[^\)]|([^\(\*]|\n))*"*)"
SINGLE_LINE_COMMENT = "--".*[\n]
COMMENT = ({COMMENT1}|{SINGLE_LINE_COMMENT})

IDENTIFIER = [a-zA-Z][a-zA-Z0-9_]*
KEYWORD = ("class"|"else"|"fi"|"if"|"in"|"inherits"|"isvoid"|"let"|"loop"|"pool"|"then"|"while"|"case"|"esac"|"new"|"of"|("true")|"false"|"not")
%%
	
<YYINITIAL>"--" {
	yybegin(SINGLE_LINE_COMMENT);
}

<SINGLE_LINE_COMMENT> .* {
	//Ignore
}	

<SINGLE_LINE_COMMENT> \n {
	yybegin(YYINITIAL);
}


<YYINITIAL, COMMENT_BLOCK> "(*" {
	//System.out.println("OPEN_PARENTHESES >>>>"+yychar);
	yybegin(COMMENT_BLOCK);commentLevel++;	
}	

<COMMENT_BLOCK> "*)" {
	if(--commentLevel == 0){
		yybegin(YYINITIAL);
		//System.out.println("CLOSED_PARENTHESES >>>>"+yychar);
	}
}	

<YYINITIAL> "*)" {
	return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}

<COMMENT_BLOCK>(.|\n|\r) {
	//Ignore comment body.
}

<YYINITIAL>{WHITESPACE} {
	//Ignoring whitespaces
}

<YYINITIAL>{KEYWORD} {
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

<YYINITIAL>{IDENTIFIER} {
	if(Character.isLowerCase(yytext().charAt(0))){
		return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
	}
	else{
		return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
	}
}


<YYINITIAL> \" {
	yybegin(STRING_BLOCK);
	string_buf = new StringBuffer();
}

<STRING_BLOCK> (\\(.|\n)|[^\"\\\n])* {
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

<STRING_BLOCK,STRING_ERROR> ({NEWLINE}) {
	if(yy_lexical_state != STRING_ERROR){
		yybegin(YYINITIAL);
		return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
	}
	yybegin(YYINITIAL);
}

<STRING_BLOCK,STRING_ERROR> \" {
	if(yy_lexical_state != STRING_ERROR){
		yybegin(YYINITIAL);	
		return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(cleanStringLiteral(string_buf.toString())));
	}
	yybegin(YYINITIAL);	 
}


<YYINITIAL>{NUMBER} { 
	return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext()));
}

<YYINITIAL>"=>"	{ /* Sample lexical rule for "=>" arrow.
    Further lexical rules should be defined
    here, after the last %% separator */
	return new Symbol(TokenConstants.DARROW); 
}

<YYINITIAL>"<=" {
	return new Symbol(TokenConstants.LE);
}									 

<YYINITIAL>"<-" {
	return new Symbol(TokenConstants.ASSIGN);
}

<YYINITIAL>";" {
	return new Symbol(TokenConstants.SEMI);
}

<YYINITIAL>":" {
	return new Symbol(TokenConstants.COLON);
}

<YYINITIAL>"{" {
	return new Symbol(TokenConstants.LBRACE);
}

<YYINITIAL>"}" {
	return new Symbol(TokenConstants.RBRACE);
}

<YYINITIAL>"," {
	return new Symbol(TokenConstants.COMMA);
}

<YYINITIAL>"(" {
	return new Symbol(TokenConstants.LPAREN);
}

<YYINITIAL>")" {
	return new Symbol(TokenConstants.RPAREN);
}

<YYINITIAL>"." {
	return new Symbol(TokenConstants.DOT);
}

<YYINITIAL>"<" {
	return new Symbol(TokenConstants.LT);
}

<YYINITIAL>"~" {
	return new Symbol(TokenConstants.NEG);
}


<YYINITIAL>"@" {
	return new Symbol(TokenConstants.AT);
}

<YYINITIAL>"+" {
	return new Symbol(TokenConstants.PLUS);
}

<YYINITIAL>"/" {
	return new Symbol(TokenConstants.DIV);
}

<YYINITIAL>"-" {
	return new Symbol(TokenConstants.MINUS);
}

<YYINITIAL>"*" {
	return new Symbol(TokenConstants.MULT);
}

<YYINITIAL>"=" {
	return new Symbol(TokenConstants.EQ);
}

<YYINITIAL>. { /* This rule should be the very last
    in your lexical specification and
    will match match everything not
    matched by other lexical rules. */
	return new Symbol(TokenConstants.ERROR, yytext());
}
