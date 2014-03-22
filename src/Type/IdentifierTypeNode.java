/* Justin Robb
 * Adam Croissant
 * 2-18-14
 * Type representing classes as identifiers
*/

package Type;

public class IdentifierTypeNode extends TypeNode {
	public ClassTypeNode c;	// represented class node
	public String name;		// name of class
	
  public IdentifierTypeNode(ClassTypeNode c, String name) {
    super();
	this.c = c;
	this.name = name;
  }
  
  public void print(String ind){
	System.out.print(name);
  }
}
