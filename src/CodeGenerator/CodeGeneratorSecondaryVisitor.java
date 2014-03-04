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
import AST.Visitor.Visitor;
import Type.Visitor.TypeChecker;
import Type.*;

// Sample code generation visitor.
// Robert R. Henry 2013-11-12

public class CodeGeneratorSecondaryVisitor implements Visitor {

  private CodeGenerator cg;
  private String callee;
  private TypeChecker tc;
  private String recent_class;
  
  public CodeGeneratorSecondaryVisitor(CodeGenerator cg, TypeChecker tc) {
    this.cg = cg;
	callee = "NULL";
	this.tc = tc;
	recent_class = "NULL";
  }

  // Display added for toy example language.  Not used in regular MiniJava
  public void visit(Display n) {
    n.e.accept(this);
    cg.genDisplay(n.line_number);
  }

  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n) {
	n.m.accept(this);
    for (int i = 0; i < n.cl.size(); i++) {
      n.cl.get(i).accept(this);
    }
	cg.genVtbles(tc);
  }

  // Identifier i1,i2;
  // Block b;
  public void visit(MainClass n) {
	tc.PushClass("asm_main");
	recent_class = "asm_main";
	cg.genMainEntry("asm_main");
    n.i1.accept(this);
    n.i2.accept(this);
    for (int i = 0; i < n.b.sl.size(); i ++) {
        n.b.sl.get(i).accept(this);
    }
    cg.genMainExit("asm_main");
  }

  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
	tc.PushClass(n.i.s);
	recent_class = n.i.s;
	cg.setClass(n.i.s);
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
	tc.PushClass(n.i.s);
	recent_class = n.i.s;
	cg.setClass(n.i.s);
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
	// method
	tc.PushMethod(n.i.s);
	// n.t.accept(this);
    // n.i.accept(this);
	int local_count = n.vl.size();
    cg.genFunctionEntry(n.i.s);
    String[] registers = {"%rsi", "%rdx", "%rcx", "%r8", "%r9"};
    if (n.fl.size() > 5) System.exit(1); // more than 5 params is illegal (at the moment)
    cg.genFormal("%rdi", n.line_number); // add this ptr to stack
	for (int i = n.fl.size() - 1; i >= 0; i--) {
	  cg.genFormal(registers[i], n.line_number);
    }
	cg.addLocalsToStack(local_count);
    // for (int i = 0; i < n.vl.size(); i++) {
      // n.vl.get(i).accept(this);
    // }
    for (int i = 0; i < n.sl.size(); i++) {
      n.sl.get(i).accept(this);
    }
    n.e.accept(this);
	
    cg.genFunctionExit(n.i.s, local_count, n.fl.size() + 1);
  }

  // Type t;
  // Identifier i;
  public void visit(Formal n) {
    // n.t.accept(this);
    // n.i.accept(this);
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
    int label = cg.genIfBeg(n.line_number);
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
    int label = cg.genWhileBeg(n.line_number);
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
	cg.genDisplay(n.line_number);
  }

  // Identifier i;
  // Exp e;
  public void visit(Assign n) {
    n.e.accept(this);
	n.i.accept(this);
	int offset = tc.GetMemOffSet(n.i.s); // returns zero if not local var
	if (offset == 0){
		offset = tc.GetGlobalMemOffSet(n.i.s, callee);
		cg.storeNonLocal(recent_class, offset);
	} else{
		cg.storeLocal(offset);
	}
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
	int label = cg.genShortCircuitAndMid(n.line_number);
    n.e2.accept(this);
	cg.genShortCircuitAndEnd(label);
  }
  
  // Exp e1,e2;
  public void visit(ShortCircuitOr n) {
    n.e1.accept(this);
	int label = cg.genShortCircuitOrMid(n.line_number);
    n.e2.accept(this);
	cg.genShortCircuitOrEnd(label);
  }

  // Exp e1,e2;
  public void visit(LessThan n) {
    n.e1.accept(this);
    n.e2.accept(this);
	cg.genLessThan(n.line_number);
  }
  
  // Exp e1,e2;
  public void visit(LessThanOrEqualTo n) {
    n.e1.accept(this);
    n.e2.accept(this);
	cg.genLessThanOrEqualTo(n.line_number);
  }
  
  // Exp e1,e2;
  public void visit(GreaterThanOrEqualTo n) {
    n.e1.accept(this);
    n.e2.accept(this);
	cg.genGreaterThanOrEqualTo(n.line_number);
  }
  
  // Exp e1,e2;
  public void visit(GreaterThan n) {
    n.e1.accept(this);
    n.e2.accept(this);
	cg.genGreaterThan(n.line_number);
  }
  
  // Exp e1,e2;call n
  public void visit(Equals n) {
    n.e1.accept(this);
    n.e2.accept(this);
	cg.genEqual(n.line_number);
  }
  
  // Exp e1,e2;
  public void visit(NotEqual n) {
    n.e1.accept(this);
    n.e2.accept(this);
	cg.genNotEqual(n.line_number);
  }
  
  // Exp e1,e2;
  public void visit(Mod n) {
    n.e1.accept(this);
    n.e2.accept(this);
	cg.genMod(n.line_number);
  }

  // Exp e1,e2;
  public void visit(Plus n) {
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genAdd(n.line_number);
  }

  // Exp e1,e2;
  public void visit(Minus n) {
    n.e1.accept(this);
    n.e2.accept(this);
	cg.genMinus(n.line_number);
  }

  // Exp e1,e2;
  public void visit(Times n) {
    n.e1.accept(this);
    n.e2.accept(this);
	cg.genTimes(n.line_number);
  }
  
  // Exp e1,e2;
  public void visit(Divide n) {
    n.e1.accept(this);
    n.e2.accept(this);
	cg.genDivide(n.line_number);
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
    n.i.accept(this); // does not push anything onto stack, but saves method name in callee
    for (int i = 0; i < n.el.size(); i++) {
	  n.el.get(i).accept(this);
    }
	n.e.accept(this); // this pushes addr of struct to the stack
	String className = callee;
	int offset = tc.GetGlobalMemOffSet(n.i.s, callee); // vtable offset of method
    cg.genCall(className, n.i.s, offset, n.el.size(), n.line_number);
  }

  // long i;
  public void visit(IntegerLiteral n) {
      cg.genIntegerLiteral(n.i);
  }
  
  // double i;
  public void visit(DoubleLiteral n) {
  }

  public void visit(True n) {
    cg.genTrue();
  }

  public void visit(False n) {
    cg.genFalse();
  }

  public void visit(IdentifierExp n) {
	callee = n.s;
	int offset = tc.GetMemOffSet(n.s); // returns zero if not local var
	if (offset == 0){
		offset = tc.GetGlobalMemOffSet(n.s, callee);
		cg.loadNonLocal(recent_class, n.s, tc, offset);
	} else {
		cg.loadLocal(offset);
	}
  }

  public void visit(ConstantExp n) {
      cg.genConstant(n.value);
  }

  public void visit(This n) {
  }

  // Exp e;
  public void visit(NewIntArray n) {
    n.e.accept(this);
	cg.genNewArray();
  }

  // Exp e;
  public void visit(NewDoubleArray n) {
    n.e.accept(this);
	cg.genNewArray();
  }
  
  // Identifier i;
  public void visit(NewObject n) {
	callee = n.i.s;
	cg.genNewObj(n.i.s, tc);
  }

  // Exp e;
  public void visit(Not n) {
    n.e.accept(this);
	cg.genNot(n.line_number);
  }

  // String s;
  public void visit(Identifier n) {
	callee = n.s;
  }
}
