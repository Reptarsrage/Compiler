package Type.Visitor;
import Type.*;
import AST.*;
import java.util.*;

public class TypeChecker {
	PackageTypeNode program;
	IntTypeNode int_type;
	DoubleTypeNode double_type;
	UndefTypeNode undef_type;
	BooleanTypeNode boolean_type;
	Stack<TypeNode> nest;

  public enum TypeLevel { CLASS, METHOD, VARIABLE }
	
	public TypeChecker() {
		program = new PackageTypeNode(0);
		int_type = new IntTypeNode(0);
		double_type = new DoubleTypeNode(0);
		undef_type = new UndefTypeNode(0);
		boolean_type = new BooleanTypeNode(0);
		nest = new Stack<TypeNode>();
		nest.push(program);
	}
	
	public void AddClass(String name){
		ClassTypeNode c = new ClassTypeNode(undef_type, 0);
		program.classes.put(name, c);
		while (nest.peek() != program)
			nest.pop();
		nest.push(c);
	}
	
	public void AddClassExtends(String name, String extending){
		ClassTypeNode c = new ClassTypeNode(undef_type, 0);
		ClassTypeNode e = program.classes.get(extending);
		c.base_type = e;
		program.classes.put(name, c);
		while (nest.peek() != program)
			nest.pop();
		nest.push(e);
		nest.push(c);
	}
	
	public void AddBlock(){
		if (nest.peek() instanceof BlockTypeNode){
			BlockTypeNode block = (BlockTypeNode) nest.peek();
			BlockTypeNode block_in = new BlockTypeNode(0);
			block.inside = block_in;
			nest.push(block_in);
		} else {
			System.err.println("FAILURE: actual=" + nest.peek() + ", expected=BlockTypeNode.");
			System.exit(1); // Fail
		}	
	}
	
	public void AddMethod(Type ret, String name){
		BlockTypeNode block = new BlockTypeNode(0);
		MethodTypeNode m = new MethodTypeNode(undef_type, block, 0);
		m.return_type = GetType(ret);
		m.inside = block;
		while (!(nest.peek() instanceof ClassTypeNode))
			nest.pop();
		ClassTypeNode c = (ClassTypeNode)nest.peek(); // TODO, check classtype fail if incorrect
		c.methods.put(name, m);
		nest.push(m);
		nest.push(block); // The block for inside the method
	}
	
	public void AddFormal(String name, Type t) {
		if (nest.peek() instanceof BlockTypeNode){
			BlockTypeNode block = (BlockTypeNode) nest.pop();
			AddFormal(name, t);
			nest.push(block);
		} else if (nest.peek() instanceof MethodTypeNode) {
			MethodTypeNode m = (MethodTypeNode)nest.peek();
			m.args.put(name, GetType(t));
		} else {
			System.err.println("FAILURE: actual=" + nest.peek() + ", expected=MethodTypeNode.");
			System.exit(1); // Fail
		}
	}
	
	public void AddVariable(String name, Type t) {
		if (nest.peek() instanceof ClassTypeNode) {
			ClassTypeNode c = (ClassTypeNode)nest.peek();
			c.fields.put(name, GetType(t));
		} else if (nest.peek() instanceof BlockTypeNode){
			BlockTypeNode c = (BlockTypeNode)nest.peek();
			c.locals.put(name, GetType(t));
		} else{
			System.exit(1); // Fail
		}
	}
	
	private TypeNode GetType(Type t){
		if (t instanceof IntegerType)
			return int_type;
		if (t instanceof DoubleType)
			return double_type;
		if (t instanceof BooleanType)
			return boolean_type;
		if (t instanceof IdentifierType){
			IdentifierTypeNode id = new IdentifierTypeNode(
				program.classes.get(((IdentifierType)t).s), ((IdentifierType)t).s, 0);
			return id;
		}
		return undef_type;
	}
	
	public void print(){
		program.print("");
	}

  // checks current scope and recurses up the stack to check higher scopes for
  // identifier. If found, returns its type. If not, returns NULL
  public TypeNode CheckSymbolTables(String id, TypeLevel t) {
    switch (t)  {
      case CLASS:
        return CheckSymbolTablesClass(id);
      case METHOD:
        return CheckSymbolTablesMethod(id);
      case VARIABLE:
        return CheckSymbolTablesVariable(id);
    }

      // while (!current.map.contains(id) && current != NULL)
      //   pop current off of nest, push on to checked_stack
      // if (current != NULL)
      //   Type ret = current.type
      // else
      //   Type ret = NULL;
      // push all of checked_stack back on to nest
      // return ret
    return null;
  }

  private TypeNode CheckSymbolTablesClass(String id) {
    return program.classes.get(id);
  }

  private TypeNode CheckSymbolTablesMethod(String id) {
      TypeNode current;
      Stack<TypeNode> checked = new Stack<TypeNode>();

      while ( !(nest.peek() instanceof ClassTypeNode) )
          checked.push(nest.pop());

      current = nest.peek();
      return ((ClassTypeNode) current).methods.get(id);
  }

  private TypeNode CheckSymbolTablesVariable(String id) {
      TypeNode current;
      TypeNode ret = null;
      Stack<TypeNode> checked = new Stack<TypeNode>();

      while ( nest.peek() != program && ret == null) {
          current = nest.pop();
          checked.push(current);
          if ( current instanceof ClassTypeNode ) {
              ret = ((ClassTypeNode) current).fields.get(id);
          } else if ( current instanceof BlockTypeNode ) {
              ret = ((BlockTypeNode) current).locals.get(id);
          } else if ( current instanceof MethodTypeNode ) {
              ret = ((MethodTypeNode) current).args.get(id);
          }
      }
      while ( !checked.empty() )
          nest.push(checked.pop());

      return ret;
  }

}