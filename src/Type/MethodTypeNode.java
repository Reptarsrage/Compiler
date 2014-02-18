package Type;
import java.util.*;


public class MethodTypeNode extends TypeNode {
  public TypeNode return_type;
  public Map<String, TypeNode> args;
  public BlockTypeNode inside;

  public MethodTypeNode(TypeNode return_type, BlockTypeNode inside, int lineNumber) {
    super(lineNumber);
	args = new LinkedHashMap<String, TypeNode>();
	this.return_type = return_type;
	this.inside = inside;
  }
  public void print(String ind){
	System.out.print(ind + "Return Type: ");
	return_type.print(ind + "   ");
	System.out.print('\n');
	Iterator<String> it = args.keySet().iterator();
	while (it.hasNext()){
		String elt = it.next();
		System.out.print(ind + "Argument: " + elt);
		System.out.print(", of type: ");
		args.get(elt).print(ind + "   ");
		System.out.print("\n");
	}
	inside.print(ind + "   ");
  }
}
