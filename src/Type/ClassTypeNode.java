/* Justin Robb, xreptarx
 * Adam Croissant, adamc41
 * 2-18-14
 * The type representing classes
*/

package Type;
import java.util.*;

public class ClassTypeNode extends TypeNode {
  public IdentifierTypeNode base_type;			// extends base_type
  public Map<String, TypeNode> fields;			// local variable list name->object
  public Map<String, Integer> mem_offset;		// local variable list name->offset in memory
  public Map<String, MethodTypeNode> methods;	// method list name->object

  public ClassTypeNode(IdentifierTypeNode base_type) {
    super();
	fields = new HashMap<String, TypeNode>();
	methods = new HashMap<String, MethodTypeNode>();
	mem_offset = new HashMap<String, Integer>();
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
		System.out.print(", of type: ");
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
