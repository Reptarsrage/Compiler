package Type;

abstract public class Type {
  //
  // The line number where the node is in the source file, for use
  // in printing error messages about this Type node
  //
  public int line_number;

  public Type(int lineNumber) {
    this.line_number = lineNumber;
  }
}
