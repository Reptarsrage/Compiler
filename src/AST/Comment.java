package AST;
import AST.Visitor.Visitor;

public class Comment extends Statement{

  public Comment(int lineNumber) {
    super(lineNumber);
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
