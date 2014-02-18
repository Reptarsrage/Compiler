package Type;
import Type.Visitor.Visitor;
import java.util.Map;
import java.util.HashMap;

public class PackageType extends Type {
  Map<String, ClassType> classes;

  public PackageType(int lineNumber) {
    super(lineNumber);
	classes = new HashMap<String, ClassType>();
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
