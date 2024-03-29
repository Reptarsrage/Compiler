package AST.Visitor;

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
import AST.While;

//
// Sample print visitor from MiniJava web site with small modifications for UW CSE.
// Hal Perkins 10/2011
//

public class PrettyPrintVisitor implements Visitor {

  //
  // Display added for toy example language.  Not used in regular MiniJava
  //
  public void visit(Display n) {
    System.out.print("display ");
    n.e.accept(this);
    System.out.print(";");
  }

  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n) {
    n.m.accept(this);
    for (int i = 0; i < n.cl.size(); i++) {
        System.out.println();
        n.cl.get(i).accept(this);
    }
  }

  // Identifier i1,i2;
  // Statement s;
  public void visit(MainClass n) {
    System.out.print("class ");
    n.i1.accept(this);
    System.out.println(" {");
    System.out.print("  public static void main (String [] ");
    n.i2.accept(this);
    System.out.println(") {");
    for (int i = 0; i < n.b.sl.size(); i++) {
      System.out.print("    ");
      n.b.sl.get(i).accept(this);
      if (i+1 <= n.b.sl.size()) { System.out.println(); }
    }
    //n.s.accept(this);
    System.out.println("  }");
    System.out.println("}");
  }

  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
    System.out.print("class ");
    n.i.accept(this);
    System.out.println(" { ");
    for (int i = 0; i < n.vl.size(); i++) {
      System.out.print("  ");
      n.vl.get(i).accept(this);
      if (i+1 < n.vl.size()) { System.out.println(); }
    }
    for (int i = 0; i < n.ml.size(); i++) {
      System.out.println();
      n.ml.get(i).accept(this);
    }
    System.out.println();
    System.out.println("}");
  }

  // Identifier i;
  // Identifier j;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclExtends n) {
    System.out.print("class ");
    n.i.accept(this);
    System.out.print(" extends ");
    n.j.accept(this);
    System.out.println(" { ");
    for (int i = 0; i < n.vl.size(); i++) {
      System.out.print("  ");
      n.vl.get(i).accept(this);
      if (i+1 < n.vl.size()) { System.out.println(); }
    }
    for (int i = 0; i < n.ml.size(); i++) {
      System.out.println();
      n.ml.get(i).accept(this);
    }
    System.out.println();
    System.out.println("}");
  }

  // Type t;
  // Identifier i;
  public void visit(VarDecl n) {
    n.t.accept(this);
    System.out.print(" ");
    n.i.accept(this);
    System.out.print(";");
  }

  // Type t;
  // Identifier i;
  // FormalList fl;
  // VarDeclList vl;
  // StatementList sl;
  // Exp e;
  public void visit(MethodDecl n) {
    System.out.println();
    System.out.print("  public ");
    n.t.accept(this);
    System.out.print(" ");
    n.i.accept(this);
    System.out.print(" (");
    for (int i = 0; i < n.fl.size(); i++) {
	  n.fl.get(i).accept(this);
      if (i+1 < n.fl.size()) { System.out.print(", "); }
    }
    System.out.println(") { ");
    for (int i = 0; i < n.vl.size(); i++) {
      System.out.print("    ");
      n.vl.get(i).accept(this);
      System.out.println("");
    }
    for (int i = 0; i < n.sl.size(); i++) {
      System.out.print("    ");
      n.sl.get(i).accept(this);
      if (i < n.sl.size()) { System.out.println(""); }
    }
    System.out.print("    return ");
    n.e.accept(this);
    System.out.println(";");
    System.out.print("  }");
  }

  // Type t;
  // Identifier i;
  public void visit(Formal n) {
    n.t.accept(this);
    System.out.print(" ");
    n.i.accept(this);
  }

  public void visit(IntArrayType n) {
    System.out.print("int []");
  }
  
  public void visit(DoubleArrayType n) {
    System.out.print("double []");
  }

  public void visit(BooleanType n) {
    System.out.print("boolean");
  }

  public void visit(IntegerType n) {
    System.out.print("int");
  }
  
  public void visit(DoubleType n) {
    System.out.print("double");
  }

  // String s;
  public void visit(IdentifierType n) {
    System.out.print(n.s);
  }

  // StatementList sl;
  public void visit(Block n) {
    System.out.println(" { ");
    for (int i = 0; i < n.sl.size(); i++) {
      System.out.print("      ");
      n.sl.get(i).accept(this);
      System.out.println();
    }
    System.out.print("    } ");
  }

  // Exp e;
  // StatementList s1,s2;
  public void visit(If n) {
    System.out.print("if (");
    n.e.accept(this);
    System.out.println(") {");
    //    System.out.print("    ");
    for (int i = 0; i < n.s1.size(); i++) {
      System.out.print("        ");
	  n.s1.get(i).accept(this);
      if ( i < n.s1.size() - 1) { System.out.println(); }
	}
    System.out.println();
    System.out.println("    } else {");
    for (int i = 0; i < n.s2.size(); i++) {
        System.out.print("         ");
	  n.s2.get(i).accept(this);
      System.out.println();
	}
	System.out.print("    }");
  }

  // Exp e;
  // StatementList s;
  public void visit(While n) {
    System.out.print("while (");
    n.e.accept(this);
    System.out.print(") {\n");
    for (int i = 0; i < n.s.size(); i++) {
      System.out.print("\t  ");
	  n.s.get(i).accept(this);
	  System.out.print("\n");
    }
	System.out.print("\t}");
  }

  // Exp e;
  public void visit(Print n) {
    System.out.print("System.out.println(");
    n.e.accept(this);
    System.out.print(");");
  }

  // Identifier i;
  // Exp e;
  public void visit(Assign n) {
    n.i.accept(this);
    System.out.print(" = ");
    n.e.accept(this);
    System.out.print(";");
  }

  // Identifier i;
  // Exp e1,e2;
  public void visit(ArrayAssign n) {
    n.i.accept(this);
    System.out.print("[");
    n.e1.accept(this);
    System.out.print("] = ");
    n.e2.accept(this);
    System.out.print(";");
  }

  // Exp e1,e2;
  public void visit(ShortCircuitAnd n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" && ");
    n.e2.accept(this);
    System.out.print(")");
  }
  
  // Exp e1,e2;
  public void visit(ShortCircuitOr n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" || ");
    n.e2.accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(LessThan n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" < ");
    n.e2.accept(this);
    System.out.print(")");
  }
  
  // Exp e1,e2;
  public void visit(LessThanOrEqualTo n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" <= ");
    n.e2.accept(this);
    System.out.print(")");
  }
  
  // Exp e1,e2;
  public void visit(GreaterThanOrEqualTo n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" >= ");
    n.e2.accept(this);
    System.out.print(")");
  }
  
  // Exp e1,e2;
  public void visit(GreaterThan n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" > ");
    n.e2.accept(this);
    System.out.print(")");
  }
  
  // Exp e1,e2;
  public void visit(Equals n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" == ");
    n.e2.accept(this);
    System.out.print(")");
  }
  
  // Exp e1,e2;
  public void visit(NotEqual n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" != ");
    n.e2.accept(this);
    System.out.print(")");
  }
  
  // Exp e1,e2;
  public void visit(Mod n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" % ");
    n.e2.accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(Plus n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" + ");
    n.e2.accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(Minus n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" - ");
    n.e2.accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(Times n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" * ");
    n.e2.accept(this);
    System.out.print(")");
  }
  
  // Exp e1,e2;
  public void visit(Divide n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" / ");
    n.e2.accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(ArrayLookup n) {
    n.e1.accept(this);
    System.out.print("[");
    n.e2.accept(this);
    System.out.print("]");
  }

  // Exp e;
  public void visit(ArrayLength n) {
    n.e.accept(this);
    System.out.print(".length");
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  public void visit(Call n) {
    n.e.accept(this);
    System.out.print(".");
    n.i.accept(this);
    System.out.print("(");
    for (int i = 0; i < n.el.size(); i++) {
      n.el.get(i).accept(this);
      if (i+1 < n.el.size()) {
        System.out.print(", ");
      }
    }
    System.out.print(")");
  }

  // long i;
  public void visit(IntegerLiteral n) {
    System.out.print(n.i);
  }
  
  // double i;
  public void visit(DoubleLiteral n) {
    System.out.print(n.i);
  }

  public void visit(True n) {
    System.out.print("true");
  }

  public void visit(False n) {
    System.out.print("false");
  }

  public void visit(IdentifierExp n) {
    System.out.print(n.s);
  }

  public void visit(ConstantExp n) {
    System.out.print(n.value);
  }

  public void visit(This n) {
    System.out.print("this");
  }

  // Exp e;
  public void visit(NewIntArray n) {
    System.out.print("new int[");
    n.e.accept(this);
    System.out.print("]");
  }
  
   // Exp e;
  public void visit(NewDoubleArray n) {
    System.out.print("new double[");
    n.e.accept(this);
    System.out.print("]");
  }

  // Identifier i;
  public void visit(NewObject n) {
    System.out.print("new ");
    System.out.print(n.i.s);
    System.out.print("()");
  }

  // Exp e;
  public void visit(Not n) {
    System.out.print("!");
    n.e.accept(this);
  }

  // String s;
  public void visit(Identifier n) {
    System.out.print(n.s);
  }
}
