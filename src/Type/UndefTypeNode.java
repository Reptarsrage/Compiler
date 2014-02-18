/* Justin Robb, xreptarx
 * Adam Croissant, adamc41
 * 2-18-14
 * Type representing an Undefined type
*/

package Type;

public class UndefTypeNode extends TypeNode {

  public UndefTypeNode() {
    super();
  }
  
  public void print(String ind){
	System.out.print("UNDEFINED");
  }
}
