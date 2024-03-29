import AST.*;
import Type.Visitor.*;
import CodeGenerator.*;
import Parser.parser;
import Scanner.scanner;

import java_cup.runtime.Symbol;

import java.util.*;

/**
 * Entry point for code generation. Feel free to use this as your compiler's
 * entry point, and feel free to modify it as you see fit.
 */
public class CodeGenMain {
    public static void main(String[] args) {
	String inputFileName = null;
	boolean countLines = false;
	for (int i = 0; i < args.length; i++) {
	    if (args[i].equals("-LC")) {
		countLines = true;
	    } else if (args[i].equals("-i")) {
		inputFileName = args[i+1];
		i += 1;
	    }
	}
	if (inputFileName == null) inputFileName = "SamplePrograms/ExampleConst.java";
	try {
	  
	    //
	    // create a scanner on the input file
	    //
	    scanner s = new scanner(System.in);
	    parser p = new parser(s);
	    // print to stdout
	    CodeGenerator cg = new CodeGenerator(null);
	    TypeChecker tc = new TypeChecker();
	    Symbol root;
	    //
	    // replace p.parse() with p.debug_parse() in next line to see trace of
	    // parser shift/reduce actions during parse
	    //
	    root = p.parse();
	    Program program = (Program) root.value;
	    program.accept(new InitialTypeVisitor(tc));
	    program.accept(new SecondaryTypeVisitor(tc));
	    program.accept(new TertiaryTypeVisitor(tc));
	    program.accept(new TypeCheckerVisitor(tc));
	    program.accept(new CodeGeneratorVisitor(tc, cg, countLines));
	    program.accept(new CodeGeneratorInheritanceVisitor(tc));
	    program.accept(new CodeGeneratorSecondaryVisitor(cg, tc, countLines, inputFileName));

	    //
	    // System.out.print("\n" + "Parsing completed");
	    //
	} catch (Exception e) {
	    //
	    // yuck: some kind of error in the compiler implementation
	    // that we're not expecting (a bug!)
	    //
	    System.err.println("Unexpected internal compiler error: " + e.toString());

	    //
	    // print out a stack dump
	    //
	    e.printStackTrace();
	}
    }
}
