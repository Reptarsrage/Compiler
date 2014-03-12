package AST;
import AST.Visitor.Visitor;

public class IntegerLiteral extends Exp {
  public long i;

  public IntegerLiteral(long ai, int lineNumber) {
      super(lineNumber, ExprType.OTHER);
    this.i = ai;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
