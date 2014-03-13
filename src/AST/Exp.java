package AST;
import AST.Visitor.Visitor;

public abstract class Exp extends ASTNode {
    public boolean isDouble;

  public Exp(int lineNumber) {
    super(lineNumber);
    this.isDouble = false;
  }

  public Exp(int lineNumber, boolean iD) {
      super(lineNumber);
      this.isDouble = iD;
  }
  public abstract void accept(Visitor v);
}
