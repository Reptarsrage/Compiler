package Type;
import java.util.Map;
import java.util.HashMap;

public class ClassTypeNode extends Type {
  public Type base_type;
  Map<String, Type> fields;
  Map<String, MethodTypeNode> methods;

  public ClassTypeNode(int lineNumber) {
    super(lineNumber);
	fields = new HashMap<String, Type>();
	methods = new HashMap<String, MethodTypeNode>();
  }
}
