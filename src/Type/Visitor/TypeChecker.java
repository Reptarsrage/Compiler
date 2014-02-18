package Type.Visitor;
import Type.*;
import AST.*;
import java.util.*;

public class TypeChecker {
	PackageTypeNode program;
	IntTypeNode int_type;
	DoubleTypeNode double_type;
	UndefTypeNode undef_type;
	IdentifierTypeNode undef_id;
	BooleanTypeNode boolean_type;
	Stack<TypeNode> nest;

  public enum TypeLevel { CLASS, METHOD, VARIABLE }
	
	public TypeChecker() {
		program = new PackageTypeNode(0);
		int_type = new IntTypeNode(0);
		double_type = new DoubleTypeNode(0);
		undef_type = new UndefTypeNode(0);
		boolean_type = new BooleanTypeNode(0);
		undef_id = new IdentifierTypeNode(null, "UNDIFINED", 0);
		nest = new Stack<TypeNode>();
		nest.push(program);
	}
	
	public void AddClass(String name, int line_number){
		if (CheckSymbolTables(name, TypeLevel.CLASS) != null){
			// FAIL
			System.err.println("Failure at line: "+ line_number
				+ ". Class " + name +" already declared.");
			System.exit(1);
		}
		ClassTypeNode c = new ClassTypeNode(undef_id, 0);
		program.classes.put(name, c);
		while (nest.peek() != program)
			nest.pop();
		nest.push(c);
	}
	
	public void AddClassExtends(String name, String extending, int line_number){
		if (CheckSymbolTables(name, TypeLevel.CLASS) != null){
			// FAIL
			System.err.println("Failure at line: "+ line_number
				+ ". Class " + name +" already declared.");
			System.exit(1);
		}
		ClassTypeNode c = new ClassTypeNode(undef_id, 0);
		IdentifierTypeNode e = new IdentifierTypeNode(
			program.classes.get(extending), extending, 0);
		c.base_type = e;
		program.classes.put(name, c);
		while (nest.peek() != program)
			nest.pop();
		nest.push(e);
		nest.push(c);
	}
	
	public void AddBlock(int line_number){
		if (nest.peek() instanceof BlockTypeNode){
			BlockTypeNode block = (BlockTypeNode) nest.peek();
			BlockTypeNode block_in = new BlockTypeNode(0);
			block.inside = block_in;
			nest.push(block_in);
		} else {
			System.err.println("Failure at line: "+line_number
				+ ". Blocks must be declared within a valid block.");
			System.exit(1); // Fail
		}	
	}
	
	public void AddMethod(Type ret, String name, int line_number){
		if (CheckSymbolTables(name, TypeLevel.METHOD) != null){
			// FAIL
			System.err.println("Failure at line: "+ line_number
				+ ". Method " + name +" already declared.");
			System.exit(1);
		}
		BlockTypeNode block = new BlockTypeNode(0);
		MethodTypeNode m = new MethodTypeNode(undef_type, block, 0);
		m.return_type = GetType(ret);
		m.inside = block;
		while (!(nest.peek() instanceof ClassTypeNode)){
			if (nest.peek() instanceof PackageTypeNode){
				System.err.println("Failure at line: "+line_number
					+ ". Method " + name + " must be nested in a class.");
				System.exit(1); // Fail
			}
			nest.pop();
		}
		ClassTypeNode c = (ClassTypeNode)nest.peek();
		c.methods.put(name, m);
		nest.push(m);
		nest.push(block); // The block for inside the method
	}
	
	public void AddFormal(String name, Type t, int line_number) {
		// TODO check type of formal against what?
		
		if (nest.peek() instanceof BlockTypeNode){
			BlockTypeNode block = (BlockTypeNode) nest.pop();
			AddFormal(name, t, line_number);
			nest.push(block);
		} else if (nest.peek() instanceof MethodTypeNode) {
			MethodTypeNode m = (MethodTypeNode)nest.peek();
			m.args.put(name, GetType(t));
		} else {
			System.err.println("Failure at line: "+line_number
				+ ". Formal argument " + name + " must be part of a method declaration.");
			System.exit(1); // Fail
		}
	}
	
	public void AddVariable(String name, Type t, int line_number) {
		if (CheckSymbolTables(name, TypeLevel.VARIABLE) != null){
			// FAIL
			System.err.println("Failure at line: "+ line_number
				+ ". Variable " + name +" already declared.");
			System.exit(1);
		}
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
  // @params
  //   - id: the identifier to check for in the tables
  //   - t: the TypeLevel of the thing you are looking for. TypeLevel enum is
  //       defined at the top of this file as CLASS, METHOD, or VARIABLE. This method
  //       expects one of those values
  public TypeNode CheckSymbolTables(String id, TypeLevel t) {
    switch (t)  {
      case CLASS:
        return CheckSymbolTablesClass(id);
      case METHOD:
        return CheckSymbolTablesMethod(id);
      case VARIABLE:
        return CheckSymbolTablesVariable(id);
    }

    return null;
  }

  // helper method for checking symbol tables when searching for a class
  private TypeNode CheckSymbolTablesClass(String id) {
    return program.classes.get(id);
  }

  // helper method for checking symbol tables when searching for a method
  private TypeNode CheckSymbolTablesMethod(String id) {
    TypeNode ret;
    Stack<TypeNode> checked = new Stack<TypeNode>();

    while ( !(nest.peek() instanceof ClassTypeNode) )
      checked.push(nest.pop());

    ret = ((ClassTypeNode) nest.peek()).methods.get(id);
    // restore nest stack
    while ( !checked.empty() )
      nest.push(checked.pop());

    return ret;
  }

  // helper method for checking symbol tables when searching for variable/parameter
  private TypeNode CheckSymbolTablesVariable(String id) {
    TypeNode current;
    TypeNode ret = null;
    Stack<TypeNode> checked = new Stack<TypeNode>();

    while ( nest.peek() != program && ret == null) {
      current = nest.pop();
      checked.push(current);
      if ( current instanceof BlockTypeNode ) {
        ret = ((BlockTypeNode) current).locals.get(id);
      } else if ( current instanceof MethodTypeNode ) {
        ret = ((MethodTypeNode) current).args.get(id);
      }
    }
    // restore the nest stack
    while ( !checked.empty() )
      nest.push(checked.pop());

    return ret;
  }

}