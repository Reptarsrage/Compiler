package Type;
import Type.Visitor.Visitor;
import java.util.Map;
import java.util.HashMap;

public class ClassType extends Type {
  public Type base_type;
  Map<String, Type> fields;
  Map<String, MethodType> methods;

  public ClassType(int lineNumber) {
    super(lineNumber);
	fields = new HashMap<String, Type>();
	methods = new HashMap<String, MethodType>();
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
