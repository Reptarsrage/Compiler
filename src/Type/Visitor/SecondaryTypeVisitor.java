package Type.Visitor;

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

public class SecondaryTypeVisitor implements Visitor {
	private TypeChecker tc;
	
	public SecondaryTypeVisitor(TypeChecker tc) {
		this.tc = tc;
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
    n.i1.accept(this);
    n.i2.accept(this);
    for (int i = 0; i < n.b.sl.size(); i ++) {
        n.b.sl.get(i).accept(this);
    }
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
	tc.AddVariable(n.i.toString(), n.t, n.line_number);
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
	tc.AddMethod(n.t, n.i.toString(), n.line_number);
	n.t.accept(this);
    n.i.accept(this);
    for (int i = n.fl.size() - 1; i >= 0; i--) {
      n.fl.get(i).accept(this);
    }
    for (int i = 0; i < n.vl.size(); i++) {
      n.vl.get(i).accept(this);
    }
    for (int i = 0; i < n.sl.size(); i++) {
      n.sl.get(i).accept(this);
    }
    n.e.accept(this);
  }

  // Type t;
  // Identifier i;
  public void visit(Formal n) {
    tc.AddFormal(n.i.toString(), n.t, n.line_number);
	n.t.accept(this);
    n.i.accept(this);
  }

  // StatementList sl;
  public void visit(Block n) {
    tc.AddBlock(n.line_number);
	for (int i = 0; i < n.sl.size(); i++) {
      n.sl.get(i).accept(this);
    }
  }

  // Exp e;
  // Statement s1,s2;
  public void visit(If n) {
    // TODO make sure that inside is handled by a block
	n.e.accept(this);
	for (int i = 0; i < n.s1.size(); i++) {
      n.s1.get(i).accept(this);
    }
    for (int i = 0; i < n.s2.size(); i++) {
      n.s2.get(i).accept(this);
    }
  }

  // Exp e;
  // StatementList s;
  public void visit(While n) {
	// TODO make sure that inside is handled by a block
	for (int i = 0; i < n.s.size(); i++) {
      n.s.get(i).accept(this);
    }
	n.e.accept(this);
  }
  
  // METHODS BELOW DO NOT MODIFY OUR TYPE TREE
  public void visit(Display n) {
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
  public void visit(NewArray n) {
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
