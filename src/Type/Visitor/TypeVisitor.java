package Type.Visitor;

import Type.ArrayType;
import Type.BlockType;
import Type.BooleanType;
import Type.ClassType;
import Type.DoubleType;
import Type.IntType;
import Type.MethodType;
import Type.PackageType;
import Type.TypeNode;
import Type.UndefType;

public class TypeVisitor implements TVisitor {
	private TypeChecker tc;
	
	public TypeVisitor(TypeChecker tc) {
		this.tc = tc;
	}
	
	// base
	// index
	public void visit(ArrayType n){
		System.out.println("TYPE");
		n.base.accept(this);
		n.index.accept(this);
	}
	
	// map<String, Type> locals
	public void visit(BlockType n){
		System.out.println("TYPE");
	}
	
	// One instance
	public void visit(BooleanType n){
		System.out.println("TYPE");
	}
	
	// base_type
	// map<String, Type> fields
	// map<String, MethodType> methods
	public void visit(ClassType n){
		System.out.println("TYPE");
	}
	
	// One instance
	public void visit(DoubleType n){
		System.out.println("TYPE");
	}
	
	// One instance
	public void visit(IntType n){
		System.out.println("TYPE");
	}
	
	// return_type
	// map<String, Type> args
	public void visit(MethodType n){
		System.out.println("TYPE");
	}
	
	// map<String, ClassType> classes
	public void visit(PackageType n){
		System.out.println("TYPE");
	}
	// One instance
	public void visit(UndefType n){
		System.out.println("TYPE");
	}
}
