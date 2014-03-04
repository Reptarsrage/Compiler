package CodeGenerator;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import Type.*;
import Type.Visitor.TypeChecker;

public class CodeGenerator {

  private int labelCount;
  private PrintStream outputStream;
  public String assemblerPrefixName;
  private String current_class;

  public CodeGenerator(String outputFileName) {
    labelCount = 0;
	if (outputFileName != null && outputFileName != "stdout") {
      try {
        outputStream = new PrintStream(outputFileName);
      } catch (FileNotFoundException e) {
      }
    } else {
      outputStream = System.out;
    }

    String osName = System.getProperty("os.name");
    // System.out.println("os.name is " + System.getProperty("os.name"));
    if (osName.equals("Mac OS X")) {
      assemblerPrefixName = "_";
    } else {
      assemblerPrefixName = "";
    }
  }
  
  
  public void genVtbles(TypeChecker tc) {
	printSection(".data");
	for (String name : tc.program.classes.keySet()){
		ClassTypeNode c = tc.program.classes.get(name);
		 printLabel(name + "$$");
		 if (c.base_type == tc.undef_id) {
			printInsn(".quad", "0"); // vtable pointer, no extends
		} else {
			printInsn(".quad", c.base_type.name + "$$"); // vtable pointer, extends
		}
		String[] toPrint = new String[c.vtble_offset.size()];
		 for (String meth : c.vtble_offset.keySet()) {
			int i = c.vtble_offset.get(meth);
			System.out.println("#"+meth + " has offset of " + i+"!");
			toPrint[i / 8 - 1] = c.vtble_offset_to_class.get(i) +"$" +meth;
		 }

		 for (String s : toPrint)
			printInsn(".quad", s);
	}
}
  
  public void loadNonLocal(String className, String id, TypeChecker tc, int offset) {
	ClassTypeNode c = tc.program.classes.get(className);
	if (c.fields.get(id) != null){
		// loading a field
		// expect our class addr to be in -8(rbp)
		printInsn("movq", "-8(%rbp)","%r14");
		printInsn("pushq", offset + "(%r14)");
	} else {
		// loading a method
		// TODO extended
		printInsn("movq", "-8(%rbp)","%r14");  // get vtable addr
		printInsn("movq", "(%r14)", "%r15"); // get method addr
		printInsn("pushq", offset + "(%r15)");
	}
  }
  
  public void storeNonLocal(String className, int offset) {
	// storing a field
	// expect our class addr to be on rbp
	printInsn("movq", "-8(%rbp)","%r14");
	printInsn("popq", offset + "(%r14)");
  }
  
  public void loadLocal(int offset) {
	printInsn("pushq", offset + "(%rbp)");
  }
  
  public void genNewArray(int line_number) {
      printComment("-- Generating new array --");
	printInsn("popq", "%rdi");
	printInsn("movq", "%rdi", "%r14");
	printInsn("movq", "%rdi", "%r15");
	printInsn("movq", "$" + line_number, "%rsi");
	printInsn("call", "check_initial_bounds");
	printInsn("incq", "%r14");
	printInsn("imulq", "$8", "%r14");
	printInsn("movq", "%r14", "%rdi");
	printInsn("call", "mjmalloc");
	printInsn("movq", "%r15", "(%rax)");
	printInsn("addq", "$8", "%rax");
	printInsn("pushq", "%rax");
  }
  
  public void genArrayLength() {
	printComment("-- Call to arrray length --");
	printInsn("popq", "%r15");
	printInsn("pushq", "-8(%r15)");
  }
  
  public void genNewObj(String name, TypeChecker tc) {
	ClassTypeNode c = tc.program.classes.get(name);
	int size = (c.mem_offset.size() + 1) * 8;
	//System.out.println("Created new object " + name + ", with size " + size);
	printInsn("movq", "$" + size, "%rdi"); // RDI the first parameter?
	printInsn("call", "mjmalloc");
	printInsn("movq", "$" + name + "$$", "%r13");
	printInsn("movq", "%r13", "(%rax)");
	printInsn("pushq", "%rax");
  }
  
    public void genArrayLookup(int line_number) {
	printComment("-- Array lookup from line "+line_number);
	printInsn("popq", "%rsi"); // pop index into rsi
	printInsn("popq", "%rdi"); // pop pointer to array into rdi
	printInsn("movq", "$" + line_number, "%rdx");
	printInsn("call", "check_bounds"); // call check_bounds(address, index)
	printInsn("imulq", "$8", "%rsi"); // multiple index by 8 to get offset
	printInsn("addq", "%rsi", "%rdi"); // add offset to address in rdi
	printInsn("movq", "(%rdi)", "%rax"); // move memory from address in rdi offset by amount in rsi to rax
	printInsn("pushq", "%rax");
	printComment("-- End array lookup --");
    }

    public void genArrayStore(int line_number) {
	printComment("-- Array store from line "+line_number);
	printInsn("popq", "%r14"); // pop expression we are assigning into r14
	printInsn("popq", "%rsi"); // pop index into rsi
	printInsn("popq", "%rdi"); // pop pointer to array into rdi
	printInsn("movq", "$" + line_number, "%rdx");
	printInsn("call", "check_bounds"); // call check_bounds(address, index)
	printInsn("imulq", "$8", "%rsi");
	printInsn("addq", "%rsi", "%rdi");
	printInsn("movq", "%r14", "(%rdi)"); // move expression result into correct address at offset (index*8) from base address of array
	printComment(" -- End array store --");
    }

  public void storeLocal(int offset) {
	printInsn("popq", offset + "(%rbp)");
  }

  
  public void addLocalsToStack(int count) {
	for (int i = 0; i < count; i++) {
		printInsn("pushq", "$0"); // local vars initialized to zero
	}
  }
  
  public void removeLocalsFromStack(int count) {
	for (int i = 0; i < count; i++) {
		printInsn("popq", "%r14"); // local vars dumped
	}
  }
	
  public void setClass(String name) {
	current_class = name;
  }
  
   public void genMainEntry(String functionName) {
		printSection(".text");
		genFunctionEntry(functionName);
   }
   
   public void genMainExit(String functionName) {
	String label = functionName;
	if (current_class != null) {
		label = current_class + "$" + label;
	}
	printComment("return point for " + assemblerPrefixName + label);
    printInsn("popq", "%rbp");
    printInsn("ret");
   }
  
  public void genFunctionEntry(String functionName) {
    String label = functionName;
	if (current_class != null) { // null in main
		label = current_class + "$" + label;
	}
	printGlobalName(label);
    printLabel(label);
    printInsn("pushq", "%rbp");
    printInsn("movq", "%rsp", "%rbp");
  }

  public void genTrue() {
    printInsn("pushq", "$1");
  }
  
  public void genFalse() {
    printInsn("pushq", "$0");
  }
  
  public void genThis() {
	printInsn("pushq", "-8(%rbp)");
  }
  
  public void genFunctionExit(String functionName, int local_count, int param_count) {
	 printInsn("popq", "%rax");
	 removeLocalsFromStack(local_count + param_count); // TODO optimize
	 genMainExit(functionName );
  }

  // currently only works with <= 6 args
  public void genCall(String className, String functionName, int offset, int argc, int linenum) {
    printComment("method call for " + className +"."+functionName + " from line " + linenum);
	  if (argc > 5) System.exit(1); // too many params passed
	printInsn("popq", "%rdi"); // addr of class
    String[] registers = {"%rsi", "%rdx", "%rcx", "%r8", "%r9"};
    for (int i = 0; i < argc; i ++) {
      // have to pop args in reverse order
      printInsn("popq", registers[argc - 1 - i]);
    }
	
	printInsn("movq", "(%rdi)", "%r14"); // get the start of our class vtable
	printInsn("call", "*"+offset+"(%r14)"); // goto correct mthod
    printInsn("pushq", "%rax"); // push result to ret value
  }
  public void genFormal(String register, int linenum) {
      printComment("Formal from line " + linenum);
      printInsn("pushq", register);
      printComment("-- end formal --");
  }

  public void genConstant(int value) {
    printInsn("movq", String.format("$%d", value), "%rax");
    printInsn("pushq", "%rax");
  }
  
  public void genIntegerLiteral(long value) {
    printInsn("movq", String.format("$%d", value), "%rax"); // should be %l??
    printInsn("pushq", "%rax");
  }
  
  private void printLabel(String labelName) {
    outputStream.println(assemblerPrefixName + labelName + ":");
  }

  private void printSection(String directive) {
    outputStream.println(directive);
  }

  private void printGlobalName(String name) {
    outputStream.println(".globl " + assemblerPrefixName + name);
  }

  private void printInsn(String opcode) {
    outputStream.print("\t");
    outputStream.println(opcode);
  }

  private void printInsn(String opcode, String op1) {
    outputStream.print("\t");
    outputStream.print(opcode);
    outputStream.print("\t");
    outputStream.print(op1);
    outputStream.println("");
  }

  private void printInsn(String opcode, String op1, String op2) {
    outputStream.print("\t");
    outputStream.print(opcode);
    outputStream.print("\t");
    outputStream.print(op1);
    outputStream.print(",");
    outputStream.print(op2);
    outputStream.println("");
  }

  public void printComment(String comment) {
    outputStream.println("# " + comment);
  }
  
  public int genIfBeg(int linenum) {
  printComment("-- if from line " + linenum + " --");
  printInsn("popq", "%rax"); // expression
	printInsn("cmpq", "$0", "%rax");
	printInsn("je", "L" + labelCount); // jump to L0 when false
	labelCount += 2;
	return labelCount;
  }
  
  public void genIfMid(int labelCount) {
	printInsn("jmp", "L" + (labelCount - 1)); // jump to L1 when done
	printLabel("L" + (labelCount - 2)); // create L0
  }
  
  public void genIfEnd(int labelCount) {
	printLabel("L" + (labelCount - 1)); // create L1
  printComment("-- end if --");
  } 
  
   public int genWhileBeg(int linenum) {
  printComment("-- while from line " + linenum + " --");
	printInsn("jmp", "L" + (labelCount + 1)); // jump to L1
	printLabel("L" + labelCount); // create L0
	labelCount += 2;
	return labelCount;
  }
  
  public void genWhileMid(int labelCount) {
	printLabel("L" + (labelCount - 1)); // create L1
  } 
  
  public void genWhileEnd(int labelCount) {
	printInsn("popq", "%rax");  // right operand
	printInsn("cmpq", "$0", "%rax");  // right operand
	printInsn("jne", "L" + (labelCount - 2));  // right operand
  printComment("-- end while --");
  } 
  
  public void genAdd(int linenum) {
    printComment("-- add from line " + linenum + " --");
    printInsn("popq", "%rcx");  // right operand
    printInsn("popq", "%rax");  // left operand
    printInsn("addq", "%rcx", "%rax");  // %rax += %rcx  (2nd operand is dst)
    printInsn("pushq", "%rax");
    printComment("-- end add --");
  }
  
  public void genMinus(int linenum) {
    printComment("-- subtract from line " + linenum + " --");
    printInsn("popq", "%rcx");  // right operand
    printInsn("popq", "%rax");  // left operand
    printInsn("subq", "%rcx", "%rax");  // %rax -= %rcx  (2nd operand is dst)
    printInsn("pushq", "%rax");
    printComment("-- end subtract --");
  }
  
  public void genTimes(int linenum) {
    printComment("-- multiply from line " + linenum + " --");
    printInsn("popq", "%rcx");  // right operand
    printInsn("popq", "%rax");  // left operand
    printInsn("imulq", "%rcx", "%rax");  // %rax *= %rcx  (2nd operand is dst)
    printInsn("pushq", "%rax");
    printComment("-- end multiply --");
  }
  
 public void genDivide(int linenum) {
    printComment("-- div from line " + linenum + " --");
    printInsn("popq", "%rcx");  // right operand
    printInsn("popq", "%rax");  // left operand
    printInsn("cqto");			// gcc does this?
	printInsn("idivq", "%rcx");  // %rax /= %rcx
    printInsn("pushq", "%rax");
    printComment("-- end divide --");
  }
  
public void genMod(int linenum) {
    printComment("-- mod from line " + linenum + " --");
    printInsn("popq", "%rcx");  // right operand
    printInsn("popq", "%rax");  // left operand
    printInsn("cqto");			// gcc does this?
	printInsn("idivq", "%rcx");  // %rax /= %rcx
    printInsn("movq", "%rdx", "%rax"); // not sure what this does
	printInsn("pushq", "%rax");
    printComment("-- end mod --");
}
  
 public void genGreaterThan(int linenum) {
    printComment("-- greater than from line " + linenum + " --");
    printInsn("popq", "%rcx");  // right operand
    printInsn("popq", "%rax");  // left operand
	printInsn("cmpq", "%rcx", "%rax");  // %rax > %rcx
	printInsn("setg", "%al");
	printInsn("movzbq", "%al", "%rax");
    printInsn("pushq", "%rax");
    printComment("-- end greater than --");
}

public void genLessThan(int linenum) {
    printComment("-- less than from line " + linenum + " --");
    printInsn("popq", "%rcx");  // right operand
    printInsn("popq", "%rax");  // left operand
	printInsn("cmpq", "%rcx", "%rax");  // %rax < %rcx
	printInsn("setl", "%al");
	printInsn("movzbq", "%al", "%rax");
    printInsn("pushq", "%rax");
    printComment("-- end less than --");
}

public void genGreaterThanOrEqualTo(int linenum) {
    printComment("-- greater or equal from line " + linenum + " --");
    printInsn("popq", "%rcx");  // right operand
    printInsn("popq", "%rax");  // left operand
	printInsn("cmpq", "%rcx", "%rax");  // %rax >= %rcx
	printInsn("setge", "%al");
	printInsn("movzbq", "%al", "%rax");
    printInsn("pushq", "%rax");
    printComment("-- end greater or equal --");
}

public void genLessThanOrEqualTo(int linenum) {
    printComment("-- less or equal from line " + linenum + " --");
    printInsn("popq", "%rcx");  // right operand
    printInsn("popq", "%rax");  // left operand
	printInsn("cmpq", "%rcx", "%rax");  // %rax <= %rcx
	printInsn("setle", "%al");
	printInsn("movzbq", "%al", "%rax");
    printInsn("pushq", "%rax");
    printComment("-- end less or equal --");
}

public int genShortCircuitOrMid(int linenum) { // TODO make short circuit - DONE
    printComment("-- or from line " + linenum + " --");
    printInsn("popq", "%rax");  // left operand
    printInsn("cmpq", "$0", "%rax");
    printInsn("jne", "L" + labelCount);
	labelCount += 2;
	return (labelCount - 2);
}

public void genShortCircuitOrEnd(int labelCount) {
    printInsn("popq", "%rax");  // right operand
    printInsn("cmpq", "$0", "%rax");
    printInsn("jne", "L" + labelCount);
    printInsn("movq", "$0", "%rax");
    printInsn("jmp", "L" + (labelCount + 1));
    printLabel("L" + labelCount);
    printInsn("movq", "$1", "%rax");
    printLabel("L" + (labelCount + 1));
    printInsn("pushq", "%rax");
	labelCount += 2;
  printComment("-- end or --");
}

public int genShortCircuitAndMid(int linenum) { // TODO make short circuit - DONE
    printComment("-- and from line " + linenum + " --");
    printInsn("popq", "%rax");  // left operand
    printInsn("cmpq", "$0", "%rax");
    printInsn("je", "L" + labelCount);
	labelCount += 2;
	return (labelCount - 2);
}

public void genShortCircuitAndEnd(int labelCount) { // TODO make short circuit - DONE
    printInsn("popq", "%rax");  // right operand
    printInsn("cmpq", "$0", "%rax");
    printInsn("je", "L" + labelCount);
    printInsn("movq", "$1", "%rax");
    printInsn("jmp", "L" + (labelCount + 1));
    printLabel("L" + labelCount);
    printInsn("movq", "$0", "%rax");
    printLabel("L" + (labelCount + 1));
    printInsn("pushq", "%rax");
    labelCount += 2;
    printComment("-- end and --");
}

public void genEqual(int linenum) {
    printComment("-- equal from line " + linenum + " --");
    printInsn("popq", "%rcx");  // right operand
    printInsn("popq", "%rax");  // left operand
	printInsn("cmpq", "%rcx", "%rax");  // %rax == %rcx
	printInsn("sete", "%al");
	printInsn("movzbq", "%al", "%rax");
    printInsn("pushq", "%rax");
    printComment("-- end equal --");
}

public void genNotEqual(int linenum) {
    printComment("-- not equal from line " + linenum + " --");
    printInsn("popq", "%rcx");  // right operand
    printInsn("popq", "%rax");  // left operand
	printInsn("cmpq", "%rcx", "%rax");  // %rax != %rcx
	printInsn("setne", "%al");
	printInsn("movzbq", "%al", "%rax");
    printInsn("pushq", "%rax");
    printComment("-- end not equal --");
}

public void genNot(int linenum) {
    printComment("-- not from line " + linenum + " --");
    printInsn("popq", "%rax");  // operand
	printInsn("testq", "%rax", "%rax");  // !%rax
	printInsn("sete", "%al");
	printInsn("movzbq", "%al", "%rax");
    printInsn("pushq", "%rax");
    printComment("-- end not --");
}

  public void genDisplay(int linenum) {
    printComment("-- display/print from line " + linenum + " --");
    printInsn("popq", "%rdi");  // single operand
    printInsn("call", assemblerPrefixName + "put");
    printComment("-- end display/print --");
  }

}
