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
	
	public void AddMethod(Type ret, String name){
		MethodTypeNode m = new MethodTypeNode(undef_type, 0);
		m.return_type = GetType(ret);
		while (!(nest.peek() instanceof ClassTypeNode))
			nest.pop();
		ClassTypeNode c = (ClassTypeNode)nest.peek(); // TODO, chack classtype fail if incorrect
		c.methods.put(name, m);
		nest.push(m);
	}
	
	private TypeNode GetType(Type t){
		if (t instanceof IntegerType)
			return int_type;
		if (t instanceof DoubleType)
			return double_type;
		if (t instanceof BooleanType)
			return boolean_type;
		
		return undef_type;
	}
	
	public void print(){
		program.print("");
	}
}