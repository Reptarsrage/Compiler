/*
 * JFlex specification for the lexical analyzer for a simple demo language.
 * Change this into the scanner for your implementation of MiniJava.
 * CSE 401/P501 Au11
 */

package Scanner;

import Parser.sym;

import java_cup.runtime.Symbol;

%%

%public
%final
%class scanner
%unicode
%cup
%line
%column

/* Code copied into the generated scanner class.  */
/* Can be referenced in scanner action code. */
%{
  // Return new symbol objects with line and column numbers in the symbol
  // left and right fields. This abuses the original idea of having left
  // and right be character positions, but is is more useful and
  // follows an example in the JFlex documentation.
  private Symbol symbol(int type) {
    return new Symbol(type, yyline+1, yycolumn+1);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline+1, yycolumn+1, value);
  }

  // Return a readable representation of symbol s (aka token)
  public String symbolToString(Symbol s) {
    String rep;
    switch (s.sym) {
      case sym.AND:
        return "AND";
      case sym.OR:
        return "OR";
      case sym.EQ:
        return "EQ";
      case sym.LESSEQ:
        return "LESSEQ";
      case sym.GREATEREQ:
        return "GREATEREQ";
      case sym.LESS:
        return "LESS";
      case sym.GREATER:
        return "GREATER";
      case sym.MINUS:
        return "MINUS";
      case sym.MULT:
        return "MULT";
      case sym.DIV:
        return "DIV";
      case sym.MOD:
        return "MOD";
      case sym.BECOMES:
        return "BECOMES";
      case sym.SEMICOLON:
        return "SEMICOLON";
      case sym.PLUS:
        return "PLUS";
      case sym.LPAREN:
        return "LPAREN";
      case sym.RPAREN:
        return "RPAREN";
	  case sym.LCURLYBRACE:
	    return "LCURLYBRACE";
	  case sym.RCURLYBRACE:
	    return "RCURLYBRACE";
      case sym.LBRACKET:
        return "LBRACKET";
      case sym.RBRACKET:
        return "RBRACKET";
	  case sym.DOT:
	    return "DOT";
      case sym.DISPLAY:
        return "DISPLAY";
	  case sym.PRINTLN:
		return "PRINTLN";
	  case sym.RETURN:
	    return "RETURN";
	  case sym.PUBLIC:
        return "PUBLIC";
      case sym.CLASS:
        return "CLASS";
      case sym.THIS:
        return "THIS";
      case sym.TRUE:
        return "TRUE";
      case sym.FALSE:
        return "FALSE";
      case sym.NEW:
        return "NEW";
      case sym.INT:
        return "INT";
      case sym.BOOLEAN:
        return "BOOLEAN";
      case sym.IF:
        return "IF";
      case sym.ELSE:
        return "ELSE";
      case sym.WHILE:
        return "WHILE";
	  case sym.NOT:
	    return "NOT";
      case sym.STATIC:
        return "STATIC";
	  case sym.DOUBLE:
	    return "DOUBLE";
      case sym.VOID:
        return "VOID";
      case sym.LENGTH:
        return "LENGTH";
	  case sym.NOTEQ:
	    return "NOTEQ";
      case sym.STRING:
        return "STRING";
	  case sym.EXTENDS:
		return "EXTENDS";
	  case sym.MAIN:
	    return "MAIN";
	  case sym.COMMA:
	    return "COMMA";
      case sym.IDENTIFIER:
        return "ID(" + (String)s.value + ")";
      case sym.CONSTANT:
        return "CONSTANT(" + (String)s.value + ")";
	case sym.D_CONSTANT:
        return "D_CONSTANT(" + (String)s.value + ")";
      case sym.EOF:
        return "<EOF>";
      case sym.error:
        return "<ERROR>";
      default:
        return "<UNEXPECTED TOKEN " + s.toString() + ">";
    }
  }
%}

/* Helper definitions */
letter = [a-zA-Z]
digit = [0-9]
eol = [\r\n]
white = {eol}|[ \t]

%%

/* Token definitions */

/* comments */
\/\/.*{eol} {} // do nothing

/* reserved words */
/* (put here so that reserved words take precedence over identifiers) */
"display" { return symbol(sym.DISPLAY); }
"System.out.println" { return symbol(sym.PRINTLN); }
"return" { return symbol(sym.RETURN); }
"if" { return symbol(sym.IF); }
"else" { return symbol(sym.ELSE); }
"while" { return symbol(sym.WHILE); }
"public" { return symbol(sym.PUBLIC); }
"static" { return symbol(sym.STATIC); }
"class" { return symbol(sym.CLASS); }
"display" { return symbol(sym.DISPLAY); }
"this" { return symbol(sym.THIS); }
"true" { return symbol(sym.TRUE); }
"false" { return symbol(sym.FALSE); }
"new" { return symbol(sym.NEW); }
"int" { return symbol(sym.INT); }
"double" { return symbol(sym.DOUBLE); }
"boolean" { return symbol(sym.BOOLEAN); }
"String" { return symbol(sym.STRING); }
"void" { return symbol(sym.VOID); }
"length" { return symbol(sym.LENGTH); }
"extends" { return symbol(sym.EXTENDS); }
"main" { return symbol(sym.MAIN); }

/* operators */
"&&" { return symbol(sym.AND); }
"||" { return symbol(sym.OR); }
"==" { return symbol(sym.EQ); }
"!=" { return symbol(sym.EQ); }
"<=" { return symbol(sym.LESSEQ); }
">=" { return symbol(sym.GREATEREQ); }
"<" { return symbol(sym.LESS); }
">" { return symbol(sym.GREATER); }
"+" { return symbol(sym.PLUS); }
"-" { return symbol(sym.MINUS); }
"*" { return symbol(sym.MULT); }
"/" { return symbol(sym.DIV); }
"%" { return symbol(sym.MOD); }
"=" { return symbol(sym.BECOMES); }
"!" { return symbol(sym.NOT); }


/* delimiters */
"," { return symbol(sym.COMMA); }
"." { return symbol(sym.DOT); }
"{" { return symbol(sym.LCURLYBRACE); }
"}" { return symbol(sym.RCURLYBRACE); }
"[" { return symbol(sym.LBRACKET); }
"]" { return symbol(sym.RBRACKET); }
"(" { return symbol(sym.LPAREN); }
")" { return symbol(sym.RPAREN); }
";" { return symbol(sym.SEMICOLON); }

/* identifiers */
{letter} ({letter}|{digit}|_)* { return symbol(sym.IDENTIFIER, yytext()); }

/* constants */
{digit}*\.{digit}*(d|D)? { return symbol(sym.D_CONSTANT, yytext()); }
{digit}+(e|E)(\+|\-)?{digit}+ { return symbol(sym.D_CONSTANT, yytext()); }
{digit}+(d|D) { return symbol(sym.D_CONSTANT, yytext()); }
{digit}+ { return symbol(sym.CONSTANT, yytext()); }


/* whitespace */
{white}+ { /* ignore whitespace */ }

/* lexical errors (put last so other matches take precedence) */
. { System.err.println(
    "\nunexpected character in input: '" + yytext() + "' at line " +
    (yyline+1) + " column " + (yycolumn+1));
  }
