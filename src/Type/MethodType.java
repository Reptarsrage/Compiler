package Type;
import Type.Visitor.Visitor;
import java.util.Map;
import java.util.LinkedHashMap;


public class MethodType extends Type {
  public Type return_type;
  Map<String, Type> args;

  public MethodType(int lineNumber) {
    super(lineNumber);
	args = new LinkedHashMap<String, Type>();
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
