package AST;
import AST.Visitor.Visitor;

public class True extends Exp {
  public True(int lineNumber) {
      super(lineNumber, ExprType.OTHER);
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
