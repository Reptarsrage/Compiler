package Type;
import Type.Visitor.Visitor;

public class IntType extends Type {

  public IntType(int lineNumber) {
    super(lineNumber);
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
