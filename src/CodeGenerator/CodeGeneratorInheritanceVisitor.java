/* Justin Robb
 * Adam Croissant
 * 3-4-14
 * The visitor which modifies vtables and class object offsets to match parent and subclasses
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

public class CodeGeneratorInheritanceVisitor implements Visitor {

  private TypeChecker tc;	// sets the offsets using TypeNode in our type checker graph
  
  public CodeGeneratorInheritanceVisitor(TypeChecker tc) {
	this.tc = tc;
  }

  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n) {
    for (int i = 0; i < n.cl.size(); i++) {
      n.cl.get(i).accept(this);
    }
  }
  
   // Identifier i;
  // Identifier j;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclExtends n) {
    // CHECK VTABLES FOR INHERITANCE
	ClassTypeNode child = (ClassTypeNode)tc.CheckSymbolTables(n.i.s ,TypeChecker.TypeLevel.CLASS);
	n.j.accept(this);
	ClassTypeNode parent = child.base_type.c;
	int original_size = 8 * child.vtble_offset.size();
	int end = 8 * (1+parent.vtble_offset.size());
	for (String meth : child.vtble_offset.keySet()) {
		if (parent.vtble_offset.containsKey(meth)){
			// overridden
			int offset = parent.vtble_offset.get(meth);
			child.vtble_offset_to_class.put(offset, n.i.s);
			child.vtble_offset.put(meth, offset);
		} else {
			// not overridden
			child.vtble_offset.put(meth, end);
			child.vtble_offset_to_class.put(end, n.i.s);
			end += 8;
		}
	}
	for (String meth : parent.vtble_offset.keySet()) {
		if (!child.vtble_offset.containsKey(meth)){
			// fill in gap
			int offset = parent.vtble_offset.get(meth);
			child.vtble_offset.put(meth, offset );
			child.vtble_offset_to_class.put(offset , parent.vtble_offset_to_class.get(offset));
		}
	}
	//	for (Integer i : child.mem_offset.values())
	//	i += end - original_size;

	// CHECK FIELDS
	int field_end = 8*(1+parent.mem_offset.size());
	for (String field : child.mem_offset.keySet()) {
		if (!parent.mem_offset.containsKey(field)){
				// not overridden
				//System.out.println("#!!!!!!!!!!!!!!!!! field offset for field "+field+"= +" +field_end);
				child.mem_offset.put(field, field_end);
				field_end += 8;
			}
		}
	for (String field : parent.mem_offset.keySet()) {
		if (!child.mem_offset.containsKey(field)){
			// fill in gap
			int offset = parent.mem_offset.get(field);
			//System.out.println("#!!!!!!!!!!!!!!!!! field offset for field "+field+"= +" +offset);
			child.mem_offset.put(field, offset);
		}
	}
}
  
  // \\\\\\\\\\\\\\\\\\\\\\\BELOW ARE USELESS METHODS\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  public void visit(MainClass n) {
  }
  public void visit(Display n) {
  } 
  public void visit(ClassDeclSimple n) {
  }
  public void visit(VarDecl n) {
  }
  public void visit(MethodDecl n) {
  }
  public void visit(Block n) {
  }
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
  public void visit(If n) {
  }
  public void visit(While n) {
  }
  public void visit(Print n) {
  }
  public void visit(Assign n) {
  }
  public void visit(ArrayAssign n) {
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
