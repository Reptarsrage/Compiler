package CodeGenerator;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class CodeGenerator {

  private int labelCount;
  private PrintStream outputStream;
  public String assemblerPrefixName;

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

  public void genFunctionEntry(String functionName) {
    printSection(".text");
    printGlobalName(functionName);
    printLabel(functionName);

    printInsn("pushq", "%rbp");
    printInsn("movq", "%rsp", "%rbp");
  }

  public void genFunctionExit(String functionName) {
    printComment("return point for " + assemblerPrefixName + functionName);
    printInsn("popq", "%rbp");
    printInsn("ret");
  }

  // currently only works with <= 6 args
  public void genCall(String functionName, int argc) {
    //  printComment("method call for " + functionName + " from line ");
    if (argc > 6) exit(1); // too many params passed

    String[] registers = {"%rdi", "%rsi", "%rdx", "%rcx", "%r8", "%r9"};
    for (int i = 0; i < argc; i ++) {
      // have to pop args in reverse order
      printInsn("popq", registers[argc - 1 - i]);
    }
    printInsn("call", functionName);
    printInsn("pushq", "%rax");
  }
 
  public void genFormal(String register) {
      printInsn("pushq", register);
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
  
  public int genIfBeg() {
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
  } 
  
   public int genWhileBeg() {
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
  } 
  
  public void genAdd() {
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
    printInsn("addq", "%rbx", "%rax");  // %rax += %rbx  (2nd operand is dst)
    printInsn("pushq", "%rax");
  }
  
  public void genMinus() {
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
    printInsn("subq", "%rbx", "%rax");  // %rax -= %rbx  (2nd operand is dst)
    printInsn("pushq", "%rax");
  }
  
  public void genTimes() {
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
    printInsn("imulq", "%rbx", "%rax");  // %rax *= %rbx  (2nd operand is dst)
    printInsn("pushq", "%rax");
  }
  
 public void genDivide() {
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
    printInsn("cqto");			// gcc does this?
	printInsn("idivq", "%rbx");  // %rax /= %rbx
    printInsn("pushq", "%rax");
  }
  
public void genMod() {
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
    printInsn("cqto");			// gcc does this?
	printInsn("idivq", "%rbx");  // %rax /= %rbx
    printInsn("movq", "%rdx", "%rax"); // not sure what this does
	printInsn("pushq", "%rax");
}
  
 public void genGreaterThan() {
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
	printInsn("cmpq", "%rbx", "%rax");  // %rax > %rbx
	printInsn("setg", "%al");
	printInsn("movzbq", "%al", "%rax");
    printInsn("pushq", "%rax");
}

public void genLessThan() {
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
	printInsn("cmpq", "%rbx", "%rax");  // %rax < %rbx
	printInsn("setl", "%al");
	printInsn("movzbq", "%al", "%rax");
    printInsn("pushq", "%rax");
}

public void genGreaterThanOrEqualTo() {
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
	printInsn("cmpq", "%rbx", "%rax");  // %rax >= %rbx
	printInsn("setge", "%al");
	printInsn("movzbq", "%al", "%rax");
    printInsn("pushq", "%rax");
}

public void genLessThanOrEqualTo() {
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
	printInsn("cmpq", "%rbx", "%rax");  // %rax <= %rbx
	printInsn("setle", "%al");
	printInsn("movzbq", "%al", "%rax");
    printInsn("pushq", "%rax");
}

public void genShortCircuitOr() { // TODO make short circuit
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
	printInsn("orq", "%rbx", "%rax");  // %rax || %rbx
	printInsn("setne", "%al");
	printInsn("movzbq", "%al", "%rax");
    printInsn("pushq", "%rax");
}

public void genShortCircuitAnd() { // TODO make short circuit - DONE
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
    printInsn("cmpq", "$0", "%rax");
    printInsn("je", "L" + labelCount);
    printInsn("cmpq", "$0", "%rbx");
    printInsn("je", "L" + labelCount);
    printInsn("movq", "$1", "%rax");
    printInsn("jmp", "L" + (labelCount + 1));
    printLabel("L" + labelCount);
    printInsn("movq", "$0", "%rax");
    printLabel("L" + (labelCount + 1));
    printInsn("pushq", "%rax");
	labelCount += 2;
    
    /*	printInsn("testq", "%rax", "%rax");  // %rax && %rbx
	printInsn("setne", "%al");
	printInsn("testq", "%rbx", "%rbx");  // %rax && %rbx
	printInsn("setne", "%dl");
	printInsn("movzbq", "%dl", "%rdx");
	printInsn("andq", "%rdx", "%rax");
    printInsn("pushq", "%rax");*/
}

public void genEqual() {
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
	printInsn("cmpq", "%rbx", "%rax");  // %rax == %rbx
	printInsn("sete", "%al");
	printInsn("movzbq", "%al", "%rax");
    printInsn("pushq", "%rax");
}

public void genNotEqual() {
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
	printInsn("cmpq", "%rbx", "%rax");  // %rax != %rbx
	printInsn("setne", "%al");
	printInsn("movzbq", "%al", "%rax");
    printInsn("pushq", "%rax");
}

public void genNot() {
    printInsn("popq", "%rax");  // operand
	printInsn("testq", "%rax", "%rax");  // !%rax
	printInsn("sete", "%al");
	printInsn("movzbq", "%al", "%rax");
    printInsn("pushq", "%rax");
}

  public void genDisplay() {
    printInsn("popq", "%rdi");  // single operand
    printInsn("call", assemblerPrefixName + "put");
  }

}
