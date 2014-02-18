package Type;
import java.util.Map;
import java.util.HashMap;

public class PackageTypeNode extends Type {
  Map<String, ClassTypeNode> classes;

  public PackageTypeNode(int lineNumber) {
    super(lineNumber);
	classes = new HashMap<String, ClassTypeNode>();
  }
}
