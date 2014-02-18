package Type;
import Type.Visitor.Visitor;

public class ArrayType extends Type {
  public Type base;
  public Type index;

  public ArrayType(Type base, Type index, int lineNumber) {
    super(lineNumber);
	this.base = base;
	this.index = index;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
