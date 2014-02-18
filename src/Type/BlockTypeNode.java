package Type;
import java.util.*;

public class BlockTypeNode extends Type {
	public Map<String, Type> locals;

	public BlockTypeNode(int lineNumber) {
	super(lineNumber);
	locals = new HashMap<String, Type>();
	}
	
	public void print(String ind){
		Iterator<String> it = locals.keySet().iterator();
		while (it.hasNext()){
			String elt = it.next();
			System.out.print(ind + "Local Variable: " + elt);
			System.out.print(" of type: ");
			locals.get(elt).print(ind + "   ");
			System.out.print("\n");
		}
	}
}
