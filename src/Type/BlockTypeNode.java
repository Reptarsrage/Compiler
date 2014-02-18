package Type;
import java.util.Map;
import java.util.HashMap;

public class BlockTypeNode extends Type {
  Map<String, Type> locals;

  public BlockTypeNode(int lineNumber) {
    super(lineNumber);
	locals = new HashMap<String, Type>();
  }
}
