/* Justin Robb, xreptarx
 * Adam Croissant, adamc41
 * 2-18-14
 * Type representing Programs
*/

package Type;
import java.util.*;

public class PackageTypeNode extends TypeNode {
  public Map<String, ClassTypeNode> classes;	// class list name->object

  public PackageTypeNode() {
    super();
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
