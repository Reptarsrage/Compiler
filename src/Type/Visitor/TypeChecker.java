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
    public PackageTypeNode program;		// Contains a list of all classes
    public IntTypeNode int_type;			// Singleton integer type
    public DoubleTypeNode double_type;		// Singleton double type
    public UndefTypeNode undef_type;		// Singleton undefined type
    public IdentifierTypeNode undef_id;	// Singleton undefined class type (for non extended classes)
    public BooleanTypeNode boolean_type;	// Singleton boolean type
    public IntArrayTypeNode int_array_type; 		// Singleton integer array type
    public DoubleArrayTypeNode double_array_type;  // Singleton double array type
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
        int_array_type = new IntArrayTypeNode();
        double_array_type = new DoubleArrayTypeNode();
        undef_id = new IdentifierTypeNode(null, "0");
        nest = new Stack<TypeNode>();
        nest.push(program);
    }
	
	public void AddMethodMemOffSet(String id_name, String className, int offset) {
		System.out.println("#^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ setting method: "+id_name);
		while(!(nest.peek() instanceof ClassTypeNode)) 
		    nest.pop();
		    
		// set a global's offset (or possibly a methods)
		ClassTypeNode c = (ClassTypeNode)nest.peek();
		if (c.methods.get(id_name) != null){
		    System.out.println("#^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ set method: "+id_name +" with offset "+ offset);
		    c.vtble_offset.put(id_name, offset); 
			c.vtble_offset_to_class.put(offset, className);
		} else {
		    // fail
		    System.err.println("Internal error, trying to set a memory offset to an unrecognized function: " +
				       id_name + ".");
		    System.exit(1);	
		}
	}
	
	
	public void AddGlobalMemOffSet(String id_name, int offset) {
		System.out.println("#^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ setting global: "+id_name);
		while(!(nest.peek() instanceof ClassTypeNode)) 
		    nest.pop();
		    
		// set a global's offset (or possibly a methods)
		ClassTypeNode c = (ClassTypeNode)nest.peek();
		if (c.fields.get(id_name) != null){
		    System.out.println("#^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ set global: "+id_name +" with offset "+ offset);
		    c.mem_offset.put(id_name, offset);
		} else {
		    // fail
		    System.err.println("Internal error, trying to set a memory offset to an unrecognized identifier: " +
				       id_name + ".");
		    System.exit(1);	
		}
	}
	
	public boolean topOfStackIsClass() {
		return nest.peek() instanceof ClassTypeNode;
	}
	
	
	public void AddMemOffSet(String var_name, int offset) {
		System.out.println("#^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ setting local: "+var_name);
		if (nest.peek() instanceof BlockTypeNode){
			BlockTypeNode block = (BlockTypeNode) nest.peek();
			if (block.locals.get(var_name) != null){
				// set a local's offset
				System.out.println("#^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ set local: "+var_name +" with offset "+ offset);
				block.mem_offset.put(var_name, offset); 
				//System.out.println("Set " + var_name + ", with offset " + offset);
			} else {
				// fail
				System.err.println("Internal error, trying to set a memory offset to an unrecognized variable: " +
							   var_name + ".");
				System.exit(1);	
			}
		} else {
			System.err.println("Internal error, expected a BlockTypeNode on the stack but found a " + nest.peek());
			System.exit(1);
		}	
	}
	
	public int GetMemOffSet(String var_name) {
		System.out.println("#^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ searching for local: "+var_name);
		if (nest.peek() instanceof BlockTypeNode){
			BlockTypeNode block = (BlockTypeNode) nest.peek();
			if (block.locals.get(var_name) != null){
				// retrieve a local's offset
				System.out.println("#^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ found local: "+var_name +" at offset "+ block.mem_offset.get(var_name));
				return block.mem_offset.get(var_name); 
			} else {
				// parameter's offset
				Stack<TypeNode> popped = new Stack<TypeNode>();
				while (!(nest.peek() instanceof MethodTypeNode))
					popped.push(nest.pop());
				MethodTypeNode m = (MethodTypeNode)nest.peek();
				while (!popped.isEmpty())
					nest.push(popped.pop());
				int count = 0;
				int total_offset = (m.args.size() + 1) * -8;
				for (String arg_name : m.args.keySet()) {
					if (arg_name.equals(var_name)){
						System.out.println("#^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ found parameter: "+var_name +" at offset "+ (-(1 + count) * 8));
						return total_offset + count * 8;
					} else
						count++;
				}
			}
		} else {
			System.err.println("Internal error, expected a BlockTypeNode on the stack but found a " + nest.peek());
			System.exit(1);
		}
		return 0;
	}
	
	public int GetMethodMemOffSet(String id_name, String className) {
		// retrieve a method's offset
		System.out.println("#^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ searching for method: "+id_name +", "+className);
		TypeNode t = CheckSymbolTables( className, TypeLevel.VARIABLE);
		ClassTypeNode c ;
		if (t == null || !(t instanceof IdentifierTypeNode))
			c = program.classes.get(className);
		else
			c = ((IdentifierTypeNode)t).c;
		if (c != null && c.methods.get(id_name) != null){
			System.out.println("#^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ offset of method "+id_name+", is "+c.mem_offset.get(id_name));
			return c.vtble_offset.get(id_name);
		}
		System.err.println("Internal error, trying to get a memory offset to an unrecognized function: " +
							   id_name + ".");
		System.exit(1);
		return 0;
	}
	
	public int GetGlobalMemOffSet(String id_name) {
		System.out.println("#^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ searching for field: "+id_name +".");
		// retrieve a global's offset
		Stack<TypeNode> popped = new Stack<TypeNode>();	
		while (!(nest.peek() instanceof ClassTypeNode))
			popped.push(nest.pop());
		ClassTypeNode c = (ClassTypeNode)nest.peek();
		while (!popped.isEmpty())
			nest.push(popped.pop());
		if (c.mem_offset.get(id_name) != null){
			System.out.println("#^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ offset of field "+id_name+", is "+c.mem_offset.get(id_name));
			return c.mem_offset.get(id_name);
		}
		System.err.println("Internal error, trying to get a memory offset to an unrecognized identifier: " +
							   id_name + ".");
		System.exit(1);
		return 0;
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
    public void UpdateClassExtends(String name, String extending, int line_number){
        if (program.classes.get(extending) == null){
            System.out.println("Error at line " +line_number+". Class " + extending + " not recognized.");
            System.exit(1);	
        }
        IdentifierTypeNode e = new IdentifierTypeNode(
                                                      program.classes.get(extending), extending);
        ClassTypeNode c = program.classes.get(name);
        c.base_type = e;
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
            TypeNode formal = GetType(t);
            formal.initialized = true;
            m.args.put(name, formal);
        } else {
            System.err.println("Failure at line: "+line_number
                               + ". Formal argument " + name + " must be part of a method declaration.");
            System.exit(1); // Fail
        }
    }
	
    // Used on secondary/tertiary sweep, pushes the already created (on first sweep) class onto the stack
    public void PushClass(String name) {
        ClassTypeNode c = program.classes.get(name);
        while (!(nest.peek() instanceof PackageTypeNode))
            nest.pop();
        nest.push(c);
    }
	
    // Used on secondary/tertiary sweep, pushes the already created (on second sweep) method onto the stack
    public void PushMethod(String name) {
        while (!(nest.peek() instanceof ClassTypeNode))
            nest.pop();
        ClassTypeNode c = (ClassTypeNode)nest.peek();
        MethodTypeNode m = c.methods.get(name);
        nest.push(m);
        nest.push(m.inside);
    }
	
    public void printStack(){
        while (!nest.empty()){
            System.err.println(nest.pop());
        }
    }
	
    // Used on secondary/tertiary sweep, pushes the already created (on second sweep) block onto the stack
    public void PushBlock() {
        if (!(nest.peek() instanceof BlockTypeNode)){
            // FAIL
            System.err.println("Internal error, attempting to push a block" +
                               " statement with a " + nest.peek() + " on top of stack.");
            System.exit(1);
        }
			
        BlockTypeNode b = (BlockTypeNode)nest.peek();
        BlockTypeNode b_i = b.inside;
        nest.push(b_i);
    }
	
    // Adds a local variable or field to the current scope
    public void AddVariable(String name, Type t, int line_number) {
        if (CheckSymbolTables(name, TypeLevel.VARIABLE) != null){
            // FAIL
            System.err.println("Failure at line: "+ line_number
                               + ". Variable " + name +" already declared.");
            System.exit(1);
        }
		
        if (GetType(t) == undef_type || GetType(t) == undef_id) {
            System.err.println("Failure at line: "+ line_number
                               + ". Variable " + name +" has invalid type "+t+".");
            System.exit(1);
        }
        if (nest.peek() instanceof ClassTypeNode) {
            ClassTypeNode c = (ClassTypeNode)nest.peek();
            TypeNode elt = GetType(t);
            elt.initialized = true; // must assume class fields to be initialized
            c.fields.put(name, elt);
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
        if (t instanceof IntArrayType)
            return int_array_type;
        if (t instanceof DoubleArrayType)
            return double_array_type;
        if (t instanceof IdentifierType){
            if (program.classes.get(((IdentifierType)t).s) == null)
                return undef_id;
            IdentifierTypeNode id = new IdentifierTypeNode(
                                                           program.classes.get(((IdentifierType)t).s), ((IdentifierType)t).s);	
            return id;
        }
        return undef_type;
    }
	
    public boolean CheckClassAssignability(IdentifierTypeNode lhs, IdentifierTypeNode rhs) {
        ClassTypeNode rhs_class = rhs.c;
        if (rhs_class == lhs.c)
            return true;
		
        while (rhs_class.base_type != undef_id){
            rhs_class = rhs_class.base_type.c;
            if (lhs.c ==  rhs_class)
                return true;
        }
        return false;
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

    public IdentifierTypeNode GetThis() {
	Stack<TypeNode> popped = new Stack<TypeNode>();
	while (!(nest.peek() instanceof ClassTypeNode)){
            popped.push(nest.pop());
	}
	ClassTypeNode c = (ClassTypeNode)nest.peek();
	while (!popped.empty())
            nest.push(popped.pop());
	IdentifierTypeNode id = new IdentifierTypeNode(c, "this");
	return id;
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
            } else if ( current instanceof ClassTypeNode ) {
		ClassTypeNode curr = (ClassTypeNode) current;
		//System.out.println("Checking for "+id+" in " + curr);
		ret = curr.fields.get(id);
		while (ret == null && curr.base_type != undef_id){
                    curr = curr.base_type.c;
                    //System.out.println("Checking for "+id+" in " + curr);
                    ret = curr.fields.get(id);
		}
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
        if ( !c.base_type.name.equals("0") ) {
            if (program.classes.get(c.base_type.name) == null) {
                // FAIL
                System.err.println("Failure at line: "+ line_number
                                   + ". Extended class " + c.base_type.name +" not recognized.");
                System.exit(1);
            }
            ClassTypeNode parent = c.base_type.c;
            // check to see if parent class has method of same name
	  
            if ( parent != null && parent.methods.get(id) != null ) {	
		MethodTypeNode parent_method = parent.methods.get(id);
		CompareMethods(method, id, method, id, line_number);
            }
        }
        nest.push(c);
        nest.push(method);
        nest.push(popped);
    }
  
    public void CompareMethods(MethodTypeNode method1, String id1, MethodTypeNode method2, String id2, int line_number) {
        // check to make sure return type is the same
        if ( !(method1.return_type.getClass().equals(method2.return_type.getClass())) ) {
            // can only happen in extends cases, not calls
            System.err.println("Error on line " + line_number + ". Return type of method " + id1
                               + " doesn't match return type of same method in parent class");
            System.exit(1);
        }
		
        // check to make sure number of args is same
        if ( method1.args.size() != method2.args.size() ) {
            System.err.println("Error on line " + line_number + ". Method " + id1
                               + " has wrong number of arguments. Expected: " + method1.args.size() + ", Actual: " + method2.args.size());
            System.exit(1);
        }

        // check to make sure types of all arguments are same
        Iterator<Map.Entry<String, TypeNode>> itr1 = method1.args.entrySet().iterator();
        Iterator<Map.Entry<String, TypeNode>> itr2 = method2.args.entrySet().iterator();
        while ( itr1.hasNext() ){
            //                  Map.Entry this_entry = this_itr.next();
            //                  Map.Entry par_entry = parent_itr.next();
			
            TypeNode el1 = itr1.next().getValue();
            TypeNode el2 = itr2.next().getValue();
            if (el1 instanceof IdentifierTypeNode && el2 instanceof IdentifierTypeNode){
                if (((IdentifierTypeNode)el1).c != ((IdentifierTypeNode)el1).c) {
                    System.err.println("Error on line " + line_number + ". Method " + id1
                                       + " has incorrect parameter types (comparing to method " + id2 + ")");
                    System.exit(1);
                } 
            } else if (el1 != el2) {		
                System.err.println("Error on line " + line_number + ". Method " + id1
                                   + " has non matching parameter types (comparing to method " + id2 + ")");
                System.exit(1);
            }
        }
    }
}
