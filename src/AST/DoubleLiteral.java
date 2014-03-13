package AST;
import AST.Visitor.Visitor;

public class DoubleLiteral extends Exp {
  public double i;

  public DoubleLiteral(double ai, int lineNumber) {
      super(lineNumber, true);
      this.i = ai;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
