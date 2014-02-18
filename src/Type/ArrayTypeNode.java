package Type;

public class ArrayTypeNode extends TypeNode {
	public TypeNode base;
	public TypeNode index;

	public ArrayTypeNode(TypeNode base, TypeNode index, int lineNumber) {
	super(lineNumber);
	this.base = base;
	this.index = index;
	}
  
  	public void print(String ind){
		System.out.print(ind + "Array with base: ");
		base.print(ind + "   ");
		System.out.print(" and index: ");
		index.print(ind + "   ");
		System.out.print('\n');
	}
}
