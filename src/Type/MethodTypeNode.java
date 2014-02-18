package Type;
import java.util.Map;
import java.util.LinkedHashMap;


public class MethodTypeNode extends Type {
  public Type return_type;
  Map<String, Type> args;

  public MethodTypeNode(int lineNumber) {
    super(lineNumber);
	args = new LinkedHashMap<String, Type>();
  }
}
