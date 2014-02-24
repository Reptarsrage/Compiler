/* Justin Robb, xreptarx
 * Adam Croissant, adamc41
 * 2-18-14
 * General Type
*/
package Type;

abstract public class TypeNode {
    public boolean initialized; // for the nodes that can represent variables

  public TypeNode() {
      initialized = false;
  }
  
  // Used for debugging, prints info about this node in the graph.
  public void print(String ind){
	System.out.print("TYPE");
  }
}
