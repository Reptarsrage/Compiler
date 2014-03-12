package AST;
import AST.Visitor.Visitor;

public class Minus extends Exp {
  public Exp e1;
  public Exp e2;

  public Minus(Exp ae1, Exp ae2, int lineNumber) {
      super(lineNumber, ae1.t);
    this.e1 = ae1;
    this.e2 = ae2;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
