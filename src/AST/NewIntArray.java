package AST;
import AST.Visitor.Visitor;

public class NewIntArray extends Exp {
  public Exp e;

  public NewIntArray(Exp ae, int lineNumber) {
      super(lineNumber, ExprType.OTHER);
    this.e = ae;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
