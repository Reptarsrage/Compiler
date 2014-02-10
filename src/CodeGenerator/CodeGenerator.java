package CodeGenerator;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class CodeGenerator {

  private PrintStream outputStream;
  public String assemblerPrefixName;

  public CodeGenerator(String outputFileName) {
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

public void genShortCircuitAnd() { // TODO make short circuit
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
	printInsn("testq", "%rax", "%rax");  // %rax && %rbx
	printInsn("setne", "%al");
	printInsn("testq", "%rbx", "%rbx");  // %rax && %rbx
	printInsn("setne", "%dl");
	printInsn("movzbq", "%dl", "%rdx");
	printInsn("andq", "%rdx", "%rax");
    printInsn("pushq", "%rax");
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
