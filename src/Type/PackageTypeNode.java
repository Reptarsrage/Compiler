package Type;
import java.util.*;

public class PackageTypeNode extends Type {
  public Map<String, ClassTypeNode> classes;

  public PackageTypeNode(int lineNumber) {
    super(lineNumber);
	classes = new HashMap<String, ClassTypeNode>();
  }
  
  public void print(String ind){
	Iterator<String> it = classes.keySet().iterator();
	while (it.hasNext()){
		String elt = it.next();
		System.out.println(ind + "Class: " + elt);
		classes.get(elt).print(ind + "   ");
	}
	}
}
