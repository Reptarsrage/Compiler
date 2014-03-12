package AST;
import AST.Visitor.Visitor;

public class False extends Exp {
  public False(int lineNumber) {
      super(lineNumber, ExprType.OTHER);
  }
  public void accept(Visitor v) {
    v.visit(this);
  }
}
