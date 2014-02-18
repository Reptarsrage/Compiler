package Type;
import Type.Visitor.Visitor;
import java.util.Map;
import java.util.HashMap;

public class BlockType extends Type {
  Map<String, Type> locals;

  public BlockType(int lineNumber) {
    super(lineNumber);
	locals = new HashMap<String, Type>();
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
