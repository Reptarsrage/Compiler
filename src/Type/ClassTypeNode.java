package Type;
import java.util.*;

public class ClassTypeNode extends TypeNode {
  public TypeNode base_type;
  public Map<String, TypeNode> fields;
  public Map<String, MethodTypeNode> methods;

  public ClassTypeNode(TypeNode base_type, int lineNumber) {
    super(lineNumber);
	fields = new HashMap<String, TypeNode>();
	methods = new HashMap<String, MethodTypeNode>();
	this.base_type = base_type;
  }
  
  public void print(String ind){
	System.out.print(ind + "Base Type: ");
	base_type.print(ind + "   ");
	System.out.print('\n');
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
