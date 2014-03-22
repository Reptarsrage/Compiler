/* Justin Robb
 * Adam Croissant
 * 2-18-14
 * Type representing blocks
*/

package Type;
import java.util.*;

public class BlockTypeNode extends TypeNode {
	public Map<String, TypeNode> locals; // local variable list name->object
	public BlockTypeNode inside;		// A nested block
	public Map<String, Integer> mem_offset;	// list of local variables name->stack memory offset

	public BlockTypeNode() {
	super();
	locals = new HashMap<String, TypeNode>();
	mem_offset = new HashMap<String, Integer>();
	}
	
	public void print(String ind){
		System.out.println(ind + "BLOCK:");
		ind += "   ";
		Iterator<String> it = locals.keySet().iterator();
		while (it.hasNext()){
			String elt = it.next();
			System.out.print(ind + "Local Variable: " + elt);
			System.out.print(", of type: ");
			locals.get(elt).print(ind + "   ");
			System.out.print("\n");
		}
		if (inside != null)
			inside.print(ind + "   ");
	}
}
