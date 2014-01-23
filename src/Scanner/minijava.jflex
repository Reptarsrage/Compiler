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
      case sym.DISPLAY:
        return "DISPLAY";
      case sym.IDENTIFIER:
        return "ID(" + (String)s.value + ")";
      case sym.CONSTANT:
        return "CONSTANT(" + (String)s.value + ")";
	case sym.DOUBLE:
        return "DOUBLE(" + (String)s.value + ")";
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

/* reserved words */
/* (put here so that reserved words take precedence over identifiers) */
"public" { return symbol(sym.PUBLIC); }
"class" { return symbol(sym.CLASS); }
"display" { return symbol(sym.DISPLAY); }
"this" { return symbol(sym.THIS); }
"true" { return symbol(sym.TRUE); }
"false" { return symbol(sym.FALSE); }
"new" { return symbol(sym.NEW); }
"int" { return symbol(sym.INT); }
"double" { return symbol(sym.DOUBLE); }
"boolean" { return symbol(sym.BOOLEAN); }

/* operators */
"&&" { return symbol(sym.AND); }
"||" { return symbol(sym.OR); }
"==" { return symbol(sym.EQ); }
"<=" { return symbol(sym.LESSEQ); }
"=>" { return symbol(sym.GREATEREQ); }
"<" { return symbol(sym.LESS); }
">" { return symbol(sym.GREATER); }
"+" { return symbol(sym.PLUS); }
"-" { return symbol(sym.MINUS); }
"*" { return symbol(sym.MULT); }
"/" { return symbol(sym.DIV); }
"%" { return symbol(sym.MOD); }
"=" { return symbol(sym.BECOMES); }


/* delimiters */
"(" { return symbol(sym.LPAREN); }
")" { return symbol(sym.RPAREN); }
";" { return symbol(sym.SEMICOLON); }

/* identifiers */
{letter} ({letter}|{digit}|_)* { return symbol(sym.IDENTIFIER, yytext()); }

/* constants */
{digit}*\.{digit}*(d|D)? { return symbol(sym.DOUBLE, yytext()); }
{digit}+(e|E)(\+|\-)?{digit}+ { return symbol(sym.DOUBLE, yytext()); }
{digit}+(d|D) { return symbol(sym.DOUBLE, yytext()); }
{digit}+ { return symbol(sym.CONSTANT, yytext()); }


/* whitespace */
{white}+ { /* ignore whitespace */ }

/* lexical errors (put last so other matches take precedence) */
. { System.err.println(
    "\nunexpected character in input: '" + yytext() + "' at line " +
    (yyline+1) + " column " + (yycolumn+1));
  }
