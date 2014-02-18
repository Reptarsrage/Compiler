package Type;
import java.util.*;

public class ClassTypeNode extends Type {
  public Type base_type;
  public Map<String, Type> fields;
  public Map<String, MethodTypeNode> methods;

  public ClassTypeNode(int lineNumber) {
    super(lineNumber);
	fields = new HashMap<String, Type>();
	methods = new HashMap<String, MethodTypeNode>();
  }
  
  public void print(String ind){
	System.out.println(ind + "Base Type: " + base_type);
	Iterator<String> it = fields.keySet().iterator();
	while (it.hasNext()){
		String elt = it.next();
		System.out.print(ind + "Field: " + elt);
		System.out.print(" of type: ");
		fields.get(elt).print(ind + "   ");
		System.out.print("\n");
	}
	
	it = methods.keySet().iterator();
	while (it.hasNext()){
		String elt = it.next();
		System.out.println(ind + "Method: " + elt);
		methods.get(elt).print(ind + "   ");
	}
  }
}
