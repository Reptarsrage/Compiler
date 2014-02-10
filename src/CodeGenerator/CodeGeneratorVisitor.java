package CodeGenerator;

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
import AST.NewArray;
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

import AST.Visitor.Visitor;

// Sample code generation visitor.
// Robert R. Henry 2013-11-12

public class CodeGeneratorVisitor implements Visitor {

  private CodeGenerator cg;
  public CodeGeneratorVisitor(CodeGenerator cg) {
    this.cg = cg;
  }

  // Display added for toy example language.  Not used in regular MiniJava
  public void visit(Display n) {
    n.e.accept(this);
    cg.genDisplay();
  }

  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n) {
    n.m.accept(this);
    for (int i = 0; i < n.cl.size(); i++) {
      n.cl.get(i).accept(this);
    }
  }

  // Identifier i1,i2;
  // StatementList s;
  public void visit(MainClass n) {
    cg.genFunctionEntry("asm_main");
    n.i1.accept(this);
    n.i2.accept(this);
    for (int i = 0; i < n.s.size(); i ++) {
        n.s.get(i).accept(this);
    }
    cg.genFunctionExit("asm_main");
  }

  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
    n.i.accept(this);
    for (int i = 0; i < n.vl.size(); i++) {
      n.vl.get(i).accept(this);
    }
    for (int i = 0; i < n.ml.size(); i++) {
      n.ml.get(i).accept(this);
    }
  }

  // Identifier i;
  // Identifier j;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclExtends n) {
    n.i.accept(this);
    n.j.accept(this);
    for (int i = 0; i < n.vl.size(); i++) {
      n.vl.get(i).accept(this);
    }
    for (int i = 0; i < n.ml.size(); i++) {
      n.ml.get(i).accept(this);
    }
  }

  // Type t;
  // Identifier i;
  public void visit(VarDecl n) {
    n.t.accept(this);
    n.i.accept(this);
  }

  // Type t;
  // Identifier i;
  // FormalList fl;
  // VarDeclList vl;
  // StatementList sl;
  // Exp e;
  public void visit(MethodDecl n) {
    n.t.accept(this);
    n.i.accept(this);
    cg.genFunctionEntry(n.i.s);
    String[] registers = {"%rdi", "%rsi", "%rdx", "%rcx", "%r8", "%r9"};
    if (fl.size() > 6) exit(1); // more than 6 params is illegal (at the moment)
    for (int i = 0; i < n.fl.size(); i++) {
      n.fl.get(i).accept(this);
      gc.genFormal(registers[i]);
    }
    for (int i = 0; i < n.vl.size(); i++) {
      n.vl.get(i).accept(this);
    }
    for (int i = 0; i < n.sl.size(); i++) {
      n.sl.get(i).accept(this);
    }
    n.e.accept(this);
    cg.genFunctionExit(n.i.s);
  }

  // Type t;
  // Identifier i;
  public void visit(Formal n) {
    n.t.accept(this);
    n.i.accept(this);
  }

  public void visit(IntArrayType n) {
  }
  
  public void visit(DoubleArrayType n) {
  }

  public void visit(BooleanType n) {
  }

  public void visit(IntegerType n) {
  }
  
  public void visit(DoubleType n) {
  }

  // String s;
  public void visit(IdentifierType n) {
  }

  // StatementList sl;
  public void visit(Block n) {
    for (int i = 0; i < n.sl.size(); i++) {
      n.sl.get(i).accept(this);
    }
  }

  // Exp e;
  // Statement s1,s2;
  public void visit(If n) {
    n.e.accept(this);
    int label = cg.genIfBeg();
	for (int i = 0; i < n.s1.size(); i++) {
      n.s1.get(i).accept(this);
    }
	cg.genIfMid(label);
    for (int i = 0; i < n.s2.size(); i++) {
      n.s2.get(i).accept(this);
    }
	cg.genIfEnd(label);
  }

  // Exp e;
  // StatementList s;
  public void visit(While n) {
    int label = cg.genWhileBeg();
	for (int i = 0; i < n.s.size(); i++) {
      n.s.get(i).accept(this);
    }
	cg.genWhileMid(label);
	n.e.accept(this);
	cg.genWhileEnd(label);
  }

  // Exp e;
  public void visit(Print n) {
    n.e.accept(this);
	cg.genDisplay();
  }

  // Identifier i;
  // Exp e;
  public void visit(Assign n) {
    n.i.accept(this);
    n.e.accept(this);
  }

  // Identifier i;
  // Exp e1,e2;
  public void visit(ArrayAssign n) {
    n.i.accept(this);
    n.e1.accept(this);
    n.e2.accept(this);
  }

  // Exp e1,e2;
  public void visit(ShortCircuitAnd n) {
    n.e1.accept(this);
    n.e2.accept(this);
	cg.genShortCircuitAnd();
  }
  
  // Exp e1,e2;
  public void visit(ShortCircuitOr n) {
    n.e1.accept(this);
    n.e2.accept(this);
	cg.genShortCircuitOr();
  }

  // Exp e1,e2;
  public void visit(LessThan n) {
    n.e1.accept(this);
    n.e2.accept(this);
	cg.genLessThan();
  }
  
  // Exp e1,e2;
  public void visit(LessThanOrEqualTo n) {
    n.e1.accept(this);
    n.e2.accept(this);
	cg.genLessThanOrEqualTo();
  }
  
  // Exp e1,e2;
  public void visit(GreaterThanOrEqualTo n) {
    n.e1.accept(this);
    n.e2.accept(this);
	cg.genGreaterThanOrEqualTo();
  }
  
  // Exp e1,e2;
  public void visit(GreaterThan n) {
    n.e1.accept(this);
    n.e2.accept(this);
	cg.genGreaterThan();
  }
  
  // Exp e1,e2;
  public void visit(Equals n) {
    n.e1.accept(this);
    n.e2.accept(this);
	cg.genEqual();
  }
  
  // Exp e1,e2;
  public void visit(NotEqual n) {
    n.e1.accept(this);
    n.e2.accept(this);
	cg.genNotEqual();
  }
  
  // Exp e1,e2;
  public void visit(Mod n) {
    n.e1.accept(this);
    n.e2.accept(this);
	cg.genMod();
  }

  // Exp e1,e2;
  public void visit(Plus n) {
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genAdd();
  }

  // Exp e1,e2;
  public void visit(Minus n) {
    n.e1.accept(this);
    n.e2.accept(this);
	cg.genMinus();
  }

  // Exp e1,e2;
  public void visit(Times n) {
    n.e1.accept(this);
    n.e2.accept(this);
	cg.genTimes();
  }
  
  // Exp e1,e2;
  public void visit(Divide n) {
    n.e1.accept(this);
    n.e2.accept(this);
	cg.genDivide();
  }

  // Exp e1,e2;
  public void visit(ArrayLookup n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  // Exp e;
  public void visit(ArrayLength n) {
    n.e.accept(this);
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  public void visit(Call n) {
    n.e.accept(this);
    n.i.accept(this);
    for (int i = 0; i < n.el.size(); i++) {
      n.el.get(i).accept(this);
    }
    cg.genCall(n.i.s, n.el.size());
  }

  // long i;
  public void visit(IntegerLiteral n) {
     cg.genIntegerLiteral(n.i);
  }
  
  // double i;
  public void visit(DoubleLiteral n) {
  }

  public void visit(True n) {
  }

  public void visit(False n) {
  }

  public void visit(IdentifierExp n) {
  }

  public void visit(ConstantExp n) {
    cg.genConstant(n.value);
  }

  public void visit(This n) {
  }

  // Exp e;
  public void visit(NewArray n) {
    n.e.accept(this);
  }

  // Identifier i;
  public void visit(NewObject n) {
  }

  // Exp e;
  public void visit(Not n) {
    n.e.accept(this);
	cg.genNot();
  }

  // String s;
  public void visit(Identifier n) {
  }
}
