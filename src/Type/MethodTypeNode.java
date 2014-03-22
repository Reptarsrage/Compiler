/* Justin Robb
 * Adam Croissant
 * 2-18-14
 * Type representing a method
*/

package Type;
import java.util.*;


public class MethodTypeNode extends TypeNode {
  public TypeNode return_type;			// the return type
  public Map<String, TypeNode> args;	// list of formal parameters name->type
  public BlockTypeNode inside;			// nested block

  public MethodTypeNode(TypeNode return_type, BlockTypeNode inside) {
    super();
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
