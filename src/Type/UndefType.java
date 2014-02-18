package Type;
import Type.Visitor.Visitor;

public class UndefType extends Type {

  public UndefType(int lineNumber) {
    super(lineNumber);
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
