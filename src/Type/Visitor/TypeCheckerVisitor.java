/* Justin Robb, xreptarx
 * Adam Croissant, adamc41
 * 2-18-14
 * Visitor for the second and final sweep of the AST
 * Checks to make sure all static type info is correct
*/

package Type.Visitor;

import Type.*;
import java.util.*;
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

public class TypeCheckerVisitor implements Visitor {
	private TypeChecker tc;
	private Stack<TypeNode> type_stack;
	
	public TypeCheckerVisitor(TypeChecker tc) {
		this.tc = tc;
		type_stack = new Stack<TypeNode>();
	}

  // Display added for toy example language.  Not used in regular MiniJava
  public void visit(Display n) {
    n.e.accept(this);
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
    tc.PushClass("asm_main");
    for (int i = 0; i < n.b.sl.size(); i ++) {
        n.b.sl.get(i).accept(this);
    }
  }

  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
    tc.PushClass(n.i.toString());
    for (int i = 0; i < n.ml.size(); i++) {
      n.ml.get(i).accept(this);
    }
  }

  // Identifier i;
  // Identifier j;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclExtends n) {
    tc.PushClass(n.i.toString());
    for (int i = 0; i < n.ml.size(); i++) {
      n.ml.get(i).accept(this);
    }
  }

  // Type t;
  // Identifier i;
  public void visit(VarDecl n) {
  }

  // Type t;
  // Identifier i;
  // FormalList fl;
  // VarDeclList vl;
  // StatementList sl;
  // Exp e;
  public void visit(MethodDecl n) {
	tc.PushMethod(n.i.toString());
    for (int i = 0; i < n.sl.size(); i++) {
      n.sl.get(i).accept(this);
    }
    n.e.accept(this);
	TypeNode e = type_stack.pop();
	MethodTypeNode m = (MethodTypeNode) tc.CheckSymbolTables(n.i.toString(), TypeChecker.TypeLevel.METHOD);
	if (m == null) {
		System.err.println("Error at line: "+n.line_number+". Method "+n.i.toString()+" not recognized.");
		System.exit(1);
	}
	
	if (e != m.return_type) {
		System.err.println("Error at line: "+n.line_number+". Expression of type "+e+" does not match return type "+m.return_type+".");
		System.exit(1);
	}
  }

  // Type t;
  // Identifier i;
  public void visit(Formal n) {
  }

  public void visit(IntArrayType n) {
	type_stack.push(tc.int_array_type);
  }
  
  public void visit(DoubleArrayType n) {
    type_stack.push(tc.double_array_type);
  }

  public void visit(BooleanType n) {
	type_stack.push(tc.boolean_type);
  }

  public void visit(IntegerType n) {
	type_stack.push(tc.int_type);
  }
  
  public void visit(DoubleType n) {
	type_stack.push(tc.double_type);
  }

  // String s;
  public void visit(IdentifierType n) {
	type_stack.push(tc.undef_id); // TODO 
  }

  // StatementList sl;
  public void visit(Block n) {
    tc.PushBlock();
	for (int i = 0; i < n.sl.size(); i++) {
      n.sl.get(i).accept(this);
    }
  }

  // Exp e;
  // Statement s1,s2;
  public void visit(If n) {
    n.e.accept(this);
	if (type_stack.pop() != tc.boolean_type){
		System.err.println("Error at line: "+n.line_number+". If expression must be a boolean.");
		System.exit(1);
	}
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
	n.e.accept(this);
	if (type_stack.pop() != tc.boolean_type){
		System.err.println("Error at line: "+n.line_number+". While expression must be a boolean.");
		System.exit(1);
	}
	for (int i = 0; i < n.s.size(); i++) {
      n.s.get(i).accept(this);
    }
  }

  // Exp e;
  public void visit(Print n) {
    n.e.accept(this);
  }

  // Identifier i;
  // Exp e;
  public void visit(Assign n) {
    n.i.accept(this);
    n.e.accept(this);
	TypeNode exp = type_stack.pop();
	TypeNode id = type_stack.pop();
	if (exp != id){
		System.err.println("Error at line: "+n.line_number+". Can't assign a "+exp+" to a "+id+".");
		System.exit(1);
	}
  }

  // Identifier i;
  // Exp e1,e2;
  public void visit(ArrayAssign n) {
    n.i.accept(this);
    n.e1.accept(this);
    n.e2.accept(this);
	TypeNode e2 = type_stack.pop();
	TypeNode e1 = type_stack.pop();
	TypeNode id = type_stack.pop();
	if (e1 != tc.int_type){
		System.err.println("Error at line: "+n.line_number+". Array index must be an integer.");
		System.exit(1);
	}
	if ((id == tc.int_array_type && e2 != tc.int_type) ||
		(id == tc.double_array_type && e2 != tc.double_type))	{
		System.err.println("Error at line: "+n.line_number+". Can't assign a "+e2+" to a "+id+".");
		System.exit(1);
	} else {
		System.err.println("Error at line: "+n.line_number+". Array type "+id+" not recognized.");
		System.exit(1);
	}
	
  }

  // Exp e1,e2;
  public void visit(ShortCircuitAnd n) {
    n.e1.accept(this);
    n.e2.accept(this);
	TypeNode e2 = type_stack.pop();
	TypeNode e1 = type_stack.pop();
	if (e1 != tc.boolean_type || e2 != tc.boolean_type){
		System.err.println("Error at line: "+n.line_number+". Oporator && takes two booleans, not "+e1+" and "+e2+".");
		System.exit(1);
	}
  }
  
  // Exp e1,e2;
  public void visit(ShortCircuitOr n) {
    n.e1.accept(this);
    n.e2.accept(this);
	TypeNode e2 = type_stack.pop();
	TypeNode e1 = type_stack.pop();
	if (e1 != tc.boolean_type || e2 != tc.boolean_type){
		System.err.println("Error at line: "+n.line_number+". Oporator || takes two booleans, not "+e1+" and "+e2+".");
		System.exit(1);
	}
  }

  // Exp e1,e2;
  public void visit(LessThan n) {
    n.e1.accept(this);
    n.e2.accept(this);
	TypeNode e2 = type_stack.pop();
	TypeNode e1 = type_stack.pop();
	if  ((e1 == tc.int_type && e2 != tc.int_type) ||
		(e1 == tc.double_type && e2 != tc.double_type)) {
		System.err.println("Error at line: "+n.line_number+". Type mismatch between "+e1+" and "+e2+".");
		System.exit(1);
	}
  }
  
  // Exp e1,e2;
  public void visit(LessThanOrEqualTo n) {
    n.e1.accept(this);
    n.e2.accept(this);
	TypeNode e2 = type_stack.pop();
	TypeNode e1 = type_stack.pop();
	if  ((e1 == tc.int_type && e2 != tc.int_type) ||
		(e1 == tc.double_type && e2 != tc.double_type)) {
		System.err.println("Error at line: "+n.line_number+". Type mismatch between "+e1+" and "+e2+".");
		System.exit(1);
	}
  }
  
  // Exp e1,e2;
  public void visit(GreaterThanOrEqualTo n) {
    n.e1.accept(this);
    n.e2.accept(this);
	TypeNode e2 = type_stack.pop();
	TypeNode e1 = type_stack.pop();
	if  ((e1 == tc.int_type && e2 != tc.int_type) ||
		(e1 == tc.double_type && e2 != tc.double_type)) {
		System.err.println("Error at line: "+n.line_number+". Type mismatch between "+e1+" and "+e2+".");
		System.exit(1);
	}
  }
  
  // Exp e1,e2;
  public void visit(GreaterThan n) {
    n.e1.accept(this);
    n.e2.accept(this);
	TypeNode e2 = type_stack.pop();
	TypeNode e1 = type_stack.pop();
	if (e1 == tc.int_type && e2 != tc.int_type){
		System.err.println("Error at line: "+n.line_number+". Type mismatch between "+e1+" and "+e2+".");
		System.exit(1);
	} else if (e1 == tc.double_type && e2 != tc.double_type){
		System.err.println("Error at line: "+n.line_number+". Type mismatch between "+e1+" and "+e2+".");
		System.exit(1);
	}
  }
  
  // Exp e1,e2;
  public void visit(Equals n) {
    n.e1.accept(this);
    n.e2.accept(this);
	TypeNode e2 = type_stack.pop();
	TypeNode e1 = type_stack.pop();
	if  ((e1 == tc.int_type && e2 != tc.int_type) ||
		(e1 == tc.double_type && e2 != tc.double_type) ||
		(e1 == tc.boolean_type && e2 != tc.boolean_type)) {
		System.err.println("Error at line: "+n.line_number+". Type mismatch between "+e1+" and "+e2+".");
		System.exit(1);
	}
  }
  
  // Exp e1,e2;
  public void visit(NotEqual n) {
    n.e1.accept(this);
    n.e2.accept(this);
	TypeNode e2 = type_stack.pop();
	TypeNode e1 = type_stack.pop();
	if  ((e1 == tc.int_type && e2 != tc.int_type) ||
		(e1 == tc.double_type && e2 != tc.double_type) ||
		(e1 == tc.boolean_type && e2 != tc.boolean_type)) {
		System.err.println("Error at line: "+n.line_number+". Type mismatch between "+e1+" and "+e2+".");
		System.exit(1);
	}
  }
  
  // Exp e1,e2;
  public void visit(Mod n) {
    n.e1.accept(this);
    n.e2.accept(this);
	TypeNode e2 = type_stack.pop();
	TypeNode e1 = type_stack.pop();
	if  ((e1 == tc.int_type && e2 != tc.int_type) ||
		(e1 == tc.double_type && e2 != tc.double_type)) {
		System.err.println("Error at line: "+n.line_number+". Type mismatch between "+e1+" and "+e2+".");
		System.exit(1);
	}
  }

  // Exp e1,e2;
  public void visit(Plus n) {
    n.e1.accept(this);
    n.e2.accept(this);
	TypeNode e2 = type_stack.pop();
	TypeNode e1 = type_stack.pop();
	if  ((e1 == tc.int_type && e2 != tc.int_type) ||
		(e1 == tc.double_type && e2 != tc.double_type)) {
		System.err.println("Error at line: "+n.line_number+". Type mismatch between "+e1+" and "+e2+".");
		System.exit(1);
	}
  }

  // Exp e1,e2;
  public void visit(Minus n) {
    n.e1.accept(this);
    n.e2.accept(this);
	TypeNode e2 = type_stack.pop();
	TypeNode e1 = type_stack.pop();
	if  ((e1 == tc.int_type && e2 != tc.int_type) ||
		(e1 == tc.double_type && e2 != tc.double_type)) {
		System.err.println("Error at line: "+n.line_number+". Type mismatch between "+e1+" and "+e2+".");
		System.exit(1);
	}
  }

  // Exp e1,e2;
  public void visit(Times n) {
    n.e1.accept(this);
    n.e2.accept(this);
	TypeNode e2 = type_stack.pop();
	TypeNode e1 = type_stack.pop();
	if  ((e1 == tc.int_type && e2 != tc.int_type) ||
		(e1 == tc.double_type && e2 != tc.double_type)) {
		System.err.println("Error at line: "+n.line_number+". Type mismatch between "+e1+" and "+e2+".");
		System.exit(1);
	}
  }
  
  // Exp e1,e2;
  public void visit(Divide n) {
    n.e1.accept(this);
    n.e2.accept(this);
	TypeNode e2 = type_stack.pop();
	TypeNode e1 = type_stack.pop();
	if  ((e1 == tc.int_type && e2 != tc.int_type) ||
		(e1 == tc.double_type && e2 != tc.double_type)) {
		System.err.println("Error at line: "+n.line_number+". Type mismatch between "+e1+" and "+e2+".");
		System.exit(1);
	}
  }

  // Exp e1,e2;
  public void visit(ArrayLookup n) {
    n.e1.accept(this);
    n.e2.accept(this);
	TypeNode e2 = type_stack.pop();
	TypeNode e1 = type_stack.pop();
	if  ((e1 == tc.int_array_type && e2 != tc.int_type) ||
		(e1 == tc.double_array_type && e2 != tc.double_type)) {
		System.err.println("Error at line: "+n.line_number+". Type mismatch between "+e1+" and "+e2+".");
		System.exit(1);
	}
  }

  // Exp e;
  public void visit(ArrayLength n) {
    n.e.accept(this);
	TypeNode e = type_stack.pop();
	if  (e != tc.int_array_type && e != tc.double_array_type) {
		System.err.println("Error at line: "+n.line_number+". Length must be called on an array, not a "+e+".");
		System.exit(1);
	}
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  public void visit(Call n) {
    // TODO
	n.e.accept(this);
	if (!(type_stack.peek() instanceof ClassTypeNode)){
		System.err.println("Error at line: "+n.line_number+
			". Internal stack error, expected: ClassTypeNode, actual: "+ type_stack.peek()+".");
		System.exit(1);
	}
	ClassTypeNode class_i = (ClassTypeNode)type_stack.pop();
    n.i.accept(this);
	if (!(type_stack.peek() instanceof MethodTypeNode)){
		System.err.println("Error at line: "+n.line_number+
			". Internal stack error, expected: MethodTypeNode, actual: "+ type_stack.peek()+".");
		System.exit(1);
	}
	MethodTypeNode m = (MethodTypeNode)type_stack.pop();
  MethodTypeNode dyn = new MethodTypeNode(null, null);
	dyn.return_type = m.return_type;
	
	for (int i = 0; i < n.el.size(); i++) {
      n.el.get(i).accept(this);
      dyn.args.put("param" + i, type_stack.pop());
    }
	tc.CompareMethods(dyn, n.i.s, m, n.i.s, n.line_number);
	type_stack.push(m.return_type);
  }

  // long i;
  public void visit(IntegerLiteral n) {
	type_stack.push(tc.int_type);
  }
  
  // double i;
  public void visit(DoubleLiteral n) {
	type_stack.push(tc.double_type);
  }

  public void visit(True n) {
    type_stack.push(tc.boolean_type);
  }

  public void visit(False n) {
    type_stack.push(tc.boolean_type);
  }

  public void visit(IdentifierExp n) {
	// TODO
	TypeNode t = tc.CheckSymbolTables(n.s, TypeChecker.TypeLevel.VARIABLE);
	if (t == null) {
		System.err.println("Error at line: "+n.line_number+". Expression "+n.s+" not recognized.");
		System.exit(1);
	}
	type_stack.push(t);
  }

  public void visit(ConstantExp n) {
    type_stack.push(tc.undef_type);
  }

  public void visit(This n) {
	// TODO
	type_stack.push(tc.GetThis());
  }

  // Exp e;
  public void visit(NewArray n) {
    n.e.accept(this);
	TypeNode e = type_stack.pop();
	if  (e != tc.int_array_type && e != tc.double_array_type) {
		System.err.println("Error at line: "+n.line_number+". A new array must be a double or int array, not a "+e+".");
		System.exit(1);
	}
  }

  // Identifier i;
  public void visit(NewObject n) {
    // TODO
      ClassTypeNode c = (ClassTypeNode) tc.CheckSymbolTables(n.i.toString(), TypeChecker.TypeLevel.CLASS);
	if (c == null) {
      System.err.println("Error at line: "+n.line_number+". New "+ n.i.toString() +" not recognized.");
		System.exit(1);
	}
  }

  // Exp e;
  public void visit(Not n) {
    n.e.accept(this);
	TypeNode e = type_stack.pop();
	if  (e != tc.boolean_type) {
		System.err.println("Error at line: "+n.line_number+". A not oporator must take a boolean argument, not a "+e+".");
		System.exit(1);
	}
  }

  // String s;
  public void visit(Identifier n) {
	// TODO
  }
}