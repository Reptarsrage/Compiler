package Type;
import Type.Visitor.Visitor;

public class DoubleType extends Type {

  public DoubleType(int lineNumber) {
    super(lineNumber);
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
