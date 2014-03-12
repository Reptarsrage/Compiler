package AST;
import AST.Visitor.Visitor;

public class ConstantExp extends Exp {
  public int value;

  public ConstantExp(String argValue, int lineNumber) {
    super(lineNumber, ExprType.OTHER);
    value = Integer.parseInt(argValue);
  }

  public ConstantExp(int argValue, int lineNumber) {
    super(lineNumber, ExprType.OTHER);
    this.value = argValue;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
