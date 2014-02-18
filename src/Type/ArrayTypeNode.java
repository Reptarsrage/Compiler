package Type;

public class ArrayTypeNode extends Type {
	public Type base;
	public Type index;

	public ArrayTypeNode(Type base, Type index, int lineNumber) {
	super(lineNumber);
	this.base = base;
	this.index = index;
	}
  
  	public void print(String ind){
		System.out.println(ind + "Array with base: " + base + " and index: " + index);
	}
}
