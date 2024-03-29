package AST;
import AST.Visitor.Visitor;

public class NewObject extends Exp {
  public Identifier i;

  public NewObject(Identifier ai, int lineNumber) {
      super(lineNumber);
    this.i = ai;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
