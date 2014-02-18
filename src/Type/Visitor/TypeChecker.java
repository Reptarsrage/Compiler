/* Justin Robb, xreptarx
 * Adam Croissant, adamc41
 * 2-18-14
 * The graph representing our symbol tables
*/

package Type.Visitor;
import Type.*;
import AST.*;
import java.util.*;

public class TypeChecker {
	PackageTypeNode program;		// Contains a list of all classes
	IntTypeNode int_type;			// Singleton integer type
	DoubleTypeNode double_type;		// Singleton double type
	UndefTypeNode undef_type;		// Singleton undefined type
	IdentifierTypeNode undef_id;	// Singleton undifined class type (for non extended classes)
	BooleanTypeNode boolean_type;	// Singleton boolean type
	Stack<TypeNode> nest;			// a stack of symbol tables representing the current 
									// scope and parent scopes of the program at any point
									
  // Holds values for what to check in the CheckSymbolTables method.
  public enum TypeLevel { CLASS, METHOD, VARIABLE }  
	
	// Initializes Stack and program
	// Prefills the graph with 1 node representing each of the fundamental scalar types
	public TypeChecker() {
		program = new PackageTypeNode();
		int_type = new IntTypeNode();
		double_type = new DoubleTypeNode();
		undef_type = new UndefTypeNode();
		boolean_type = new BooleanTypeNode();
		undef_id = new IdentifierTypeNode(null, "UNDIFINED");
		nest = new Stack<TypeNode>();
		nest.push(program);
	}
	
	// Adds a class to our graph
	public void AddClass(String name, int line_number){
		if (CheckSymbolTables(name, TypeLevel.CLASS) != null){
			// FAIL
			System.err.println("Failure at line: "+ line_number
				+ ". Class " + name +" already declared.");
			System.exit(1);
		}
		ClassTypeNode c = new ClassTypeNode(undef_id);
		program.classes.put(name, c);
		while (nest.peek() != program)
			nest.pop();
		nest.push(c);
	}
	
	// Adds an extended class to our graph
	public void AddClassExtends(String name, String extending, int line_number){
		if (CheckSymbolTables(name, TypeLevel.CLASS) != null){
			// FAIL
			System.err.println("Failure at line: "+ line_number
				+ ". Class " + name +" already declared.");
			System.exit(1);
		} else if (program.classes.get(extending) == null) {
			// FAIL
			System.err.println("Failure at line: "+ line_number
				+ ". Extended class " + extending +" not recognized.");
			System.exit(1);
		}
		ClassTypeNode c = new ClassTypeNode(undef_id);
		IdentifierTypeNode e = new IdentifierTypeNode(
			program.classes.get(extending), extending);
		c.base_type = e;
		program.classes.put(name, c);
		while (nest.peek() != program)
			nest.pop();
		nest.push(e.c);
		nest.push(c);
	}
	
	// Adds a block (scope) to our graph
	public void AddBlock(int line_number){
		if (nest.peek() instanceof BlockTypeNode){
			BlockTypeNode block = (BlockTypeNode) nest.peek();
			BlockTypeNode block_in = new BlockTypeNode();
			block.inside = block_in;
			nest.push(block_in);
		} else {
			System.err.println("Failure at line: "+line_number
				+ ". Blocks must be declared within a valid block.");
			System.exit(1); // Fail
		}	
	}
	
	// adds a method to the graph
	public void AddMethod(Type ret, String name, int line_number){
    if (CheckSymbolTables(name, TypeLevel.METHOD) != null){
			// FAIL
			System.err.println("Failure at line: "+ line_number
				+ ". Method " + name +" already declared.");
			System.exit(1);
    }
		BlockTypeNode block = new BlockTypeNode();
		MethodTypeNode m = new MethodTypeNode(undef_type, block);
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
	
	// Adds a argument to the formal list of the last method added to the stack
	// Expects last addded method to be on stack above current position
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
	
	// Used on secondary sweep, pushes the already created (on first sweep) class onto the stack
	public void PushClass(String name) {
		ClassTypeNode c = program.classes.get(name);
		while (!(nest.peek() instanceof PackageTypeNode))
			nest.pop();
		nest.push(c);
	}
	
	// Adds a local variable or field to the current scope
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
	
	// Takes the Type defined in AST and converts it to Type defined here
	private TypeNode GetType(Type t){
		if (t instanceof IntegerType)
			return int_type;
		if (t instanceof DoubleType)
			return double_type;
		if (t instanceof BooleanType)
			return boolean_type;
		if (t instanceof IntArrayType){
			ArrayTypeNode a = new ArrayTypeNode(int_type);
			return a;
		}
		if (t instanceof DoubleArrayType){
			ArrayTypeNode a = new ArrayTypeNode(double_type);
			return a;
		}
		if (t instanceof IdentifierType){
			IdentifierTypeNode id = new IdentifierTypeNode(
				program.classes.get(((IdentifierType)t).s), ((IdentifierType)t).s);
			return id;
		}
		return undef_type;
	}
	
	// Helpful for debugging, prints all symbol table information
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

  // Checks for the special case when a class extends another, and mthods can be overridden
  public void CheckMethodInheritance(String id, int line_number) {
    if (!(nest.peek() instanceof BlockTypeNode) ) {
      // something went wrong - top of nest stack should always be a single block here
      System.err.println("Internal error - problem with nest stack in semantic analysis");
      System.exit(1);
    }
    BlockTypeNode popped = (BlockTypeNode) nest.pop();

    if (!(nest.peek() instanceof MethodTypeNode) ) {
      // something went wrong - top of nest stack should always be the target method here
      System.err.println("Internal error - problem with nest stack in semantic analysis");
      System.exit(1);
    }
    MethodTypeNode method = (MethodTypeNode) nest.pop();

    if (!(nest.peek() instanceof ClassTypeNode) ) {
      // something went wrong - top of nest stack should always be the target class here
      System.err.println("Internal error - problem with nest stack in semantic analysis");
      System.exit(1);
    }
    ClassTypeNode c = (ClassTypeNode) nest.pop();
    // if our class is extending another class
    if ( !c.base_type.name.equals("UNDIFINED") ) {
      //          ClassTypeNode parent = ((ClassTypeNode) nest.peek());
      ClassTypeNode parent = c.base_type.c;
      // check to see if parent class has method of same name
      if ( parent.methods.get(id) != null ) {
        MethodTypeNode parent_method = parent.methods.get(id);
        // check to make sure return type is the same
        if ( !(parent_method.return_type.getClass().equals(method.return_type.getClass())) ) {
          System.err.println("Error on line " + line_number + ". Return type of method " + id
                             + " doesn't match return type of same method in parent class.");
          System.exit(1);
        }
        // check to make sure number of args is same
        if ( parent_method.args.size() != method.args.size() ) {
          System.err.println("Error on line " + line_number + ". Method " + id
                             + " has different number of arguments than same method in parent class.");
          System.exit(1);
        }

        // check to make sure types of all arguments are same
        Iterator<Map.Entry<String, TypeNode>> this_itr = method.args.entrySet().iterator();
        Iterator<Map.Entry<String, TypeNode>> parent_itr = parent_method.args.entrySet().iterator();
        while ( this_itr.hasNext() ){
            //                  Map.Entry this_entry = this_itr.next();
            //                  Map.Entry par_entry = parent_itr.next();
            if ( !(this_itr.next().getValue().getClass().equals(parent_itr.next().getValue().getClass())) ) {
                System.err.println("Error on line " + line_number + ". Method " + id
                                   + " has different parameter types than same method in parent class.");
                System.exit(1);
            }
        }
      }
    }
    nest.push(c);
    nest.push(method);
    nest.push(popped);
  }
}