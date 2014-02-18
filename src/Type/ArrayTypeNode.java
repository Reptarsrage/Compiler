/* Justin Robb, xreptarx
 * Adam Croissant, adamc41
 * 2-18-14
 * Type representing Arrays
*/

package Type;

public class ArrayTypeNode extends TypeNode {
	public TypeNode base;	// the base type
	public TypeNode index;  // index of the array

	public ArrayTypeNode(TypeNode base, TypeNode index) {
		super();
		this.base = base;
		this.index = index;
	}
  
  	public void print(String ind){
		System.out.print(ind + "Array with base: ");
		base.print(ind + "   ");
		System.out.print(", and index: ");
		index.print(ind + "   ");
		System.out.print('\n');
	}
}
