package Type;
import java.util.*;

public class BlockTypeNode extends TypeNode {
	public Map<String, TypeNode> locals;

	public BlockTypeNode(int lineNumber) {
	super(lineNumber);
	locals = new HashMap<String, TypeNode>();
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
	}
}
