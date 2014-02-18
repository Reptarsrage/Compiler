package Type;

public class UndefTypeNode extends TypeNode {

  public UndefTypeNode(int lineNumber) {
    super(lineNumber);
  }
  
  public void print(String ind){
	System.out.print("UNDEFINED");
  }
}
