package CodeGenerator;

import java.io.FileNotFoundException;
import java.io.PrintStream;

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
	if (current_class != null) {
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
  
  public void genFunctionExit(String functionName) {
	 printInsn("popq", "%rax");
	 genMainExit(functionName );
  }

  // currently only works with <= 6 args
  public void genCall(String className, String functionName, int argc, int linenum) {
    printComment("method call for " + className +"."+functionName + " from line " + linenum);
	  if (argc > 6) System.exit(1); // too many params passed

    String[] registers = {"%rdi", "%rsi", "%rdx", "%rcx", "%r8", "%r9"};
    for (int i = 0; i < argc; i ++) {
      // have to pop args in reverse order
      printInsn("popq", registers[argc - 1 - i]);
    }
    String label = className + "$" + functionName;	
	printInsn("call", label);
    printInsn("pushq", "%rax");
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
