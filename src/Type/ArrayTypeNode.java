/* Justin Robb, xreptarx
 * Adam Croissant, adamc41
 * 2-18-14
 * Type representing Arrays
*/

package Type;

public class ArrayTypeNode extends TypeNode {
	public TypeNode base;	// the base type

	public ArrayTypeNode(TypeNode base) {
		super();
		this.base = base;
	}
  
  	public void print(String ind){
		System.out.print("Array with base: ");
		base.print(ind + "   ");
		System.out.print('\n');
	}
}
