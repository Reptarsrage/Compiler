package AST;
import AST.Visitor.Visitor;

public class VarDecl extends ASTNode {
  public Type t;
  public Identifier i;

  public VarDecl(Type at, Identifier ai, int lineNumber) {
    super(lineNumber);
    this.t = at;
    this.i = ai;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
