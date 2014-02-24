// CSE 401 Group C
// Adam Croissant (adamc41) and Justin Robb (xreptarx)

// run this file by doing java -cp "build/classes:lib/CUP.jar" TypeCheckerMain < (input file name)
// after running ant clean and ant compile

import AST.ASTNode;
import AST.ArrayAssign;
import AST.ArrayLength;
import AST.ArrayLookup;
import AST.Assign;
import AST.Block;
import AST.BooleanType;
import AST.Call;
import AST.ClassDecl;
import AST.ClassDeclExtends;
import AST.ClassDeclList;
import AST.ClassDeclSimple;
import AST.ConstantExp;
import AST.Display;
import AST.Divide;
import AST.DoubleArrayType;
import AST.DoubleLiteral;
import AST.DoubleType;
import AST.Equals;
import AST.Exp;
import AST.ExpList;
import AST.False;
import AST.Formal;
import AST.FormalList;
import AST.GreaterThan;
import AST.GreaterThanOrEqualTo;
import AST.Identifier;
import AST.IdentifierExp;
import AST.IdentifierType;
import AST.If;
import AST.IntArrayType;
import AST.IntegerLiteral;
import AST.IntegerType;
import AST.LessThan;
import AST.LessThanOrEqualTo;
import AST.MainClass;
import AST.MethodDecl;
import AST.MethodDeclList;
import AST.Minus;
import AST.Mod;
import AST.NewIntArray;
import AST.NewDoubleArray;
import AST.NewObject;
import AST.Not;
import AST.NotEqual;
import AST.Plus;
import AST.Print;
import AST.Program;
import AST.ShortCircuitAnd;
import AST.ShortCircuitOr;
import AST.Statement;
import AST.StatementList;
import AST.This;
import AST.Times;
import AST.True;
import AST.Type;
import AST.VarDecl;
import AST.VarDeclList;
import AST.Visitor.PrettyPrintVisitor;
import AST.Visitor.Visitor;
import AST.While;
import CodeGenerator.CodeGenerator;
import CodeGenerator.CodeGeneratorVisitor;
import Parser.parser;
import Scanner.scanner;
import Type.Visitor.*;

import java_cup.runtime.Symbol;

import java.util.List;

public class TypeCheckerMain {

    public static void main(String [] args) {
        try {
            //
            // create a scanner on the input file
            //
            scanner s = new scanner(System.in);
            parser p = new parser(s);
            TypeChecker tc = new TypeChecker();
            Symbol root;

            root = p.parse();
            Program program = (Program)root.value;
            program.accept(new InitialTypeVisitor(tc));
            program.accept(new SecondaryTypeVisitor(tc));
            program.accept(new TertiaryTypeVisitor(tc));
            program.accept(new TypeCheckerVisitor(tc));
            System.out.println("Program passed semantic analysis.");
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