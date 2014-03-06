/* Justin Robb, xreptarx
 * Adam Croissant, adamc41
 * 3-4-14
 * The visitor which builds up vtables and class object offsets
 */

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

  private CodeGenerator cg;	// tool for generating code
  private TypeChecker tc;		// sets the offsets using TypeNode in our type checker graph
  private int stack_offset;		// offset in the local stack from rbp
  private int vtble_offset;		// offset in vtable	(first is always parent class)
  private int field_offset;		// offset in class (first is alwys vtble pointer)
  private String recent_class;	// the most recently visited class (this*)
  private boolean count_lines;
  
  public CodeGeneratorVisitor(TypeChecker tc) {
	this.tc = tc;
	stack_offset = -8;
	vtble_offset = 0;
	field_offset = 0;
	recent_class = "NULL";
	this.count_lines = false;
  }
  
  public CodeGeneratorVisitor(TypeChecker tc, CodeGenerator cg, boolean count_lines) {
	this(tc);
	this.cg = cg;
	this.count_lines = count_lines;
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
	n.b.accept(this);
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
	for (int i = 0; i < n.sl.size(); i++) {
      n.sl.get(i).accept(this);
    }
	stack_offset = -8;
	if (count_lines) cg.addFlaggedLine(n.e.line_number);
  }

  // StatementList sl;
  public void visit(Block n) {
	for (int i = 0; i < n.sl.size(); i++) {
      n.sl.get(i).accept(this);
    }
  }
  
  public void visit(If n) {
	if (count_lines) cg.addFlaggedLine(n.line_number);
	for (int i = 0; i < n.s1.size(); i++) {
      n.s1.get(i).accept(this);
    }
    for (int i = 0; i < n.s2.size(); i++) {
      n.s2.get(i).accept(this);
    }
  }
  public void visit(While n) {
	if (count_lines) cg.addFlaggedLine(n.line_number);
	for (int i = 0; i < n.s.size(); i++) {
      n.s.get(i).accept(this);
    }
  }
  public void visit(Print n) {
	if (count_lines) cg.addFlaggedLine(n.line_number);
  }
  public void visit(Assign n) {
	if (count_lines) cg.addFlaggedLine(n.line_number);
  }
  public void visit(ArrayAssign n) {
	if (count_lines) cg.addFlaggedLine(n.line_number);
  }
  public void visit(Display n) {
	if (count_lines) cg.addFlaggedLine(n.line_number);
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
  public void visit(IdentifierType n) {
  }
  public void visit(ShortCircuitAnd n) {
  }
  public void visit(ShortCircuitOr n) {
  }
  public void visit(LessThan n) {
  }
  public void visit(LessThanOrEqualTo n) {	
  }
  public void visit(GreaterThanOrEqualTo n) {	
  }
  public void visit(GreaterThan n) {	
  }
  public void visit(Equals n) {	
  }
  public void visit(NotEqual n) {	
  }
  public void visit(Mod n) {
  }
  public void visit(Plus n) {
  }
  public void visit(Minus n) {
  }
  public void visit(Times n) {
  }
  public void visit(Divide n) {
  }
  public void visit(ArrayLookup n) {
  }
  public void visit(ArrayLength n) {
  }
  public void visit(Call n) {
  }
  public void visit(IntegerLiteral n) {
  }
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
  public void visit(NewIntArray n) {
  }
  public void visit(NewDoubleArray n) {
  }
  public void visit(NewObject n) {
  }
  public void visit(Not n) {
  }
  public void visit(Identifier n) {
  }
}
