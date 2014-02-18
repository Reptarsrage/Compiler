package Type;

public class IdentifierTypeNode extends TypeNode {
	public ClassTypeNode c;
	public String name;
  public IdentifierTypeNode(ClassTypeNode c, String name, int lineNumber) {
    super(lineNumber);
	this.c = c;
	this.name = name;
  }
  
  public void print(String ind){
	System.out.print(name);
  }
}
