package Type;
import Type.Visitor.Visitor;

public class BooleanType extends Type {

  public BooleanType(int lineNumber) {
    super(lineNumber);
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
