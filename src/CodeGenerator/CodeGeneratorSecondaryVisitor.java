/* Justin Robb
 * Adam Croissant
 * 3-4-14
 * The visitor which generates assembly code 
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
import java.io.*;

public class CodeGeneratorSecondaryVisitor implements Visitor {

  private CodeGenerator cg;	// tool for generating code
  private String callee;	// for call's callee.f() 
  private TypeChecker tc;	// type checker graph used to represent all of our classes and methods
  private String recent_class;	// the most recently visited class (this*)
  private boolean count_lines;
  private String input_filename;
  
  public CodeGeneratorSecondaryVisitor(CodeGenerator cg, TypeChecker tc) {
    this.cg = cg;
	callee = "NULL";
	this.tc = tc;
	recent_class = "NULL";
	count_lines = false;
  }
  
  public CodeGeneratorSecondaryVisitor(CodeGenerator cg, TypeChecker tc, boolean count_lines, String imput_filename) {
    this(cg, tc);
	this.count_lines = count_lines;
	this.input_filename = imput_filename;
  }

  // Display added for toy example language.  Not used in regular MiniJava
  public void visit(Display n) {
    if (count_lines) cg.genUpdateCount(n.line_number);
    n.e.accept(this);
    cg.genDisplay(n.line_number, count_lines, n.e.isDouble);
  }

  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n) {
	if (count_lines)
		System.out.println("#COUNTING LINES!");
	n.m.accept(this);
    for (int i = 0; i < n.cl.size(); i++) {
      n.cl.get(i).accept(this);
    }
	cg.genVtbles(tc, count_lines);
  }

  // Identifier i1,i2;
  // Block b;
  public void visit(MainClass n) {
	tc.PushClass("asm_main");
	recent_class = "asm_main";
	cg.genMainEntry("asm_main");
	if (count_lines) {
	    cg.genLineCounting(input_filename);
	    cg.genUpdateCount(n.i2.line_number);
	}
    n.i1.accept(this);
    n.i2.accept(this);
	for (int i = 0; i < n.b.sl.size(); i ++) {
        n.b.sl.get(i).accept(this);
    }
	if (count_lines) cg.genCountFinish(count(input_filename));
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
	int local_count = n.vl.size();
    cg.genFunctionEntry(n.i.s);
	if (count_lines) cg.genUpdateCount(n.line_number);
    String[] registers = {"%rsi", "%rdx", "%rcx", "%r8", "%r9"};
    String[] doubleregs = {"%xmm0", "%xmm1", "%xmm2", "%xmm3", "%xmm4"};
    if (n.fl.size() > 5) System.exit(1); // more than 5 params is illegal (at the moment)
    cg.genFormal("%rdi", n.line_number, false); // add this ptr to stack
    int i = 0;
    int j = 0;
    while ((i+j) < n.fl.size()) {
	if (n.fl.get(i+j).t instanceof DoubleType) {
	    cg.genFormal(doubleregs[n.fl.size() - 1 - j], n.line_number, true);
	    j++;
	} else {
	    cg.genFormal(registers[n.fl.size() - 1 - i], n.line_number, false);
	    i++;
	}
    }

    /*    for (int i = 0; i < n.fl.size(); i ++) {
	  cg.genFormal(registers[n.fl.size() - 1 - i], n.line_number);
	  }
	  for (int i = n.fl.size() - 1; i >= 0; i--) {
	  cg.genFormal(registers[i], n.line_number);
	  }*/
    cg.addLocalsToStack(local_count);

    for (int k = 0; k < n.sl.size(); k++) {
      n.sl.get(k).accept(this);
    }
	if (count_lines) cg.genUpdateCount(n.e.line_number);
    n.e.accept(this);
    boolean returnDouble = false;
    if (n.t instanceof DoubleType)
	returnDouble = true;
    cg.genFunctionExit(n.i.s, local_count, n.fl.size() + 1, returnDouble);
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
    if (count_lines) cg.genUpdateCount(n.line_number);
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
	if (count_lines) cg.genUpdateCount(n.line_number);
	int label = cg.genWhileBeg(n.line_number);
	for (int i = 0; i < n.s.size(); i++) {
      n.s.get(i).accept(this);
    }
	cg.genWhileMid(label);
	if (count_lines) cg.genUpdateCount(n.line_number);
	n.e.accept(this);
	cg.genWhileEnd(label);
  }

  // Exp e;
  public void visit(Print n) {
    if (count_lines) cg.genUpdateCount(n.line_number);
	n.e.accept(this);
	cg.genDisplay(n.line_number, count_lines, n.e.isDouble);
  }

  // Identifier i;
  // Exp e;
  public void visit(Assign n) {
    if (count_lines) cg.genUpdateCount(n.line_number);
	n.e.accept(this);
	n.i.accept(this);
	int offset = tc.GetMemOffSet(n.i.s); // returns zero if not local var
	if (offset == 0){
		offset = tc.GetGlobalMemOffSet(n.i.s);
		cg.storeNonLocal(recent_class, offset);
	} else{
		cg.storeLocal(offset);
	}
  }

  // Identifier i;
  // Exp e1,e2;
  public void visit(ArrayAssign n) {
    if (count_lines) cg.genUpdateCount(n.line_number);
	int offset = tc.GetMemOffSet(n.i.s); // returns zero if not local var
    if (offset == 0){
	offset = tc.GetGlobalMemOffSet(n.i.s);
	cg.loadNonLocal(recent_class, n.i.s, tc, offset);
    } else {
	cg.loadLocal(offset);
    }

    n.e1.accept(this);
    n.e2.accept(this);
    cg.genArrayStore(n.line_number);
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
    cg.genLessThan(n.line_number, n.e1.isDouble);
  }
  
  // Exp e1,e2;
  public void visit(LessThanOrEqualTo n) {
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genLessThanOrEqualTo(n.line_number, n.e1.isDouble);
  }
  
  // Exp e1,e2;
  public void visit(GreaterThanOrEqualTo n) {
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genGreaterThanOrEqualTo(n.line_number, n.e1.isDouble);
  }
  
  // Exp e1,e2;
  public void visit(GreaterThan n) {
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genGreaterThan(n.line_number, n.e1.isDouble);
  }
  
  // Exp e1,e2;call n
  public void visit(Equals n) {
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genEqual(n.line_number, n.e1.isDouble);
  }
  
  // Exp e1,e2;
  public void visit(NotEqual n) {
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genNotEqual(n.line_number, n.e1.isDouble);
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
    cg.genAdd(n.line_number, n.isDouble);
  }

  // Exp e1,e2;
  public void visit(Minus n) {
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genMinus(n.line_number, n.isDouble);
  }

  // Exp e1,e2;
  public void visit(Times n) {
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genTimes(n.line_number, n.isDouble);
  }
  
  // Exp e1,e2;
  public void visit(Divide n) {
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genDivide(n.line_number, n.isDouble);
  }

  // Exp e1,e2;
  public void visit(ArrayLookup n) {
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genArrayLookup(n.line_number);
  }

  // Exp e;
  public void visit(ArrayLength n) {
    n.e.accept(this);
	cg.genArrayLength();
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
	int offset = tc.GetMethodMemOffSet(n.i.s, callee); // vtable offset of method
	cg.genCall(className, n.i.s, offset, n.line_number, n.el, n.isDouble);
  }

  // long i;
  public void visit(IntegerLiteral n) {
      cg.genIntegerLiteral(n.i);
  }
  
    // double i
  public void visit(DoubleLiteral n) {
	// TODO doubles
      cg.genDoubleLiteral(n.i);
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
		offset = tc.GetGlobalMemOffSet(n.s);
		cg.loadNonLocal(recent_class, n.s, tc, offset);
	} else {
		cg.loadLocal(offset);
	}
  }

  public void visit(ConstantExp n) {
      cg.genConstant(n.value);
  }

  public void visit(This n) {
	System.out.println("#~~~~~~~~~~~~~~~~~~~Setting callee to " + recent_class);
	callee = recent_class;
	cg.genThis();
  }

  // Exp e;
  public void visit(NewIntArray n) {
    n.e.accept(this);
	cg.genNewArray(n.line_number);
  }

  // Exp e;
  public void visit(NewDoubleArray n) {
    n.e.accept(this);
    cg.genNewArray(n.line_number);
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
  
  private int count(String filename) {
    try {
		InputStream is = new BufferedInputStream(new FileInputStream(filename));
        byte[] c = new byte[1024];
        int count = 0;
        int readChars = 0;
        boolean empty = true;
        while ((readChars = is.read(c)) != -1) {
            empty = false;
            for (int i = 0; i < readChars; ++i) {
                if (c[i] == '\n') {
                    ++count;
                }
            }
        }
		is.close();
        return (count == 0 && !empty) ? 1 : count;
    } catch (IOException e) {
		System.err.println("Internal I/O Exception, aborting...");
		System.exit(1);
	}
	return 0;
  }
  
  // \\\\\\\\\\\\\\\\\\\\\\\\\\\\ BELOW ARE USELESS METHODS \\\\\\\\\\\\\\\\\\\\\\\\
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
}
