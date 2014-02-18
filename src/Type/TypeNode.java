package Type;

abstract public class TypeNode {
  //
  // The line number where the node is in the source file, for use
  // in printing error messages about this Type node
  //
  public int line_number;

  public TypeNode(int lineNumber) {
    this.line_number = lineNumber;
  }
  
  public void print(String ind){
	System.out.print("TYPE");
  }
}
