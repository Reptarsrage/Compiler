package Type;
import java.util.*;


public class MethodTypeNode extends Type {
  public Type return_type;
  public Map<String, Type> args;

  public MethodTypeNode(int lineNumber) {
    super(lineNumber);
	args = new LinkedHashMap<String, Type>();
  }
  public void print(String ind){
	System.out.println(ind + "Return Type: " + return_type);
	Iterator<String> it = args.keySet().iterator();
	while (it.hasNext()){
		String elt = it.next();
		System.out.print(ind + "Argument: " + elt);
		System.out.print(" of type: ");
		args.get(elt).print(ind + "   ");
		System.out.print("\n");
	}
  }
}
