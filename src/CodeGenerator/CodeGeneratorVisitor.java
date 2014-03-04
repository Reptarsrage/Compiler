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

// ONLY BUILDS UP VTABLES AND OFFSETS

public class CodeGeneratorVisitor implements Visitor {

  private CodeGenerator cg;
  private TypeChecker tc;
  private int stack_offset;
  private int vtble_offset;
  private int field_offset;
  private String recent_class;
  
  public CodeGeneratorVisitor(CodeGenerator cg, TypeChecker tc) {
    this.cg = cg;
	this.tc = tc;
	stack_offset = -8;
	vtble_offset = 0;
	field_offset = 0;
	recent_class = "NULL";
  }

  // Display added for toy example language.  Not used in regular MiniJava
  public void visit(Display n) {
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
  // Block b;
  public void visit(MainClass n) {
	recent_class = "asm_main";
	vtble_offset = 0;
  }

  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
	recent_class = n.i.s;
    vtble_offset = 0;
	field_offset = 0;
	tc.PushClass(n.i.s);
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
  recent_class = n.i.s;
    vtble_offset = 0;
	field_offset = 0;
	tc.PushClass(n.i.s);
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
	if (tc.topOfStackIsClass()) {
		// field
		tc.AddGlobalMemOffSet(n.i.s, 8 + field_offset);
		field_offset += 8;
	} else {
		// local var
		tc.AddMemOffSet(n.i.s, -8 + stack_offset);
		stack_offset -= 8;
	}
  }

  // Type t;
  // Identifier i;
  // FormalList fl;
  // VarDeclList vl;
  // StatementList sl;
  // Exp e;
  public void visit(MethodDecl n) {
	// method
	tc.AddMethodMemOffSet(n.i.s, recent_class, 8 + vtble_offset);
	vtble_offset += 8;
	tc.PushMethod(n.i.s);
	stack_offset -= n.fl.size() * 8;
    for (int i = 0; i < n.vl.size(); i++) {
      n.vl.get(i).accept(this);
    }
	stack_offset = -8;
  }

  // StatementList sl;
  public void visit(Block n) {
	for (int i = 0; i < n.sl.size(); i++) {
      n.sl.get(i).accept(this);
    }
  }
  
  // \\\\\\\\\\\\\\\\\\\\\\\BELOW ARE USELESS METHODS\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  public void visit(Formal n) {
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

  // Exp e;
  // Statement s1,s2;
  public void visit(If n) {
  }

  // Exp e;
  // StatementList s;
  public void visit(While n) {
  }

  // Exp e;
  public void visit(Print n) {
  }

  // Identifier i;
  // Exp e;
  public void visit(Assign n) {
  }

  // Identifier i;
  // Exp e1,e2;
  public void visit(ArrayAssign n) {
  }

  // Exp e1,e2;
  public void visit(ShortCircuitAnd n) {
  }
  
  // Exp e1,e2;
  public void visit(ShortCircuitOr n) {
  }

  // Exp e1,e2;
  public void visit(LessThan n) {
	
  }
  
  // Exp e1,e2;
  public void visit(LessThanOrEqualTo n) {	
  }
  
  // Exp e1,e2;
  public void visit(GreaterThanOrEqualTo n) {	
  }
  
  // Exp e1,e2;
  public void visit(GreaterThan n) {	
  }
  
  // Exp e1,e2;
  public void visit(Equals n) {	
  }
  
  // Exp e1,e2;
  public void visit(NotEqual n) {	
  }
  
  // Exp e1,e2;
  public void visit(Mod n) {
  }

  // Exp e1,e2;
  public void visit(Plus n) {
  }

  // Exp e1,e2;
  public void visit(Minus n) {
  }

  // Exp e1,e2;
  public void visit(Times n) {
  }
  
  // Exp e1,e2;
  public void visit(Divide n) {
  }

  // Exp e1,e2;
  public void visit(ArrayLookup n) {
  }

  // Exp e;
  public void visit(ArrayLength n) {
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  public void visit(Call n) {
  }

  // long i;
  public void visit(IntegerLiteral n) {
      
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
      
  }

  public void visit(This n) {
  }

  // Exp e;
  public void visit(NewIntArray n) {
  }

  // Exp e;
  public void visit(NewDoubleArray n) {
  }
  
  // Identifier i;
  public void visit(NewObject n) {
  }

  // Exp e;
  public void visit(Not n) {
	
  }

  // String s;
  public void visit(Identifier n) {
  }
}
