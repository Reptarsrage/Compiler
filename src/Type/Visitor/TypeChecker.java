package Type.Visitor;
import Type.*;

public class TypeChecker {
	PackageTypeNode program;
	IntTypeNode int_type;
	DoubleTypeNode double_type;
	UndefTypeNode undef_type;
	BooleanTypeNode boolean_type;
	
	public TypeChecker() {
		program = new PackageTypeNode(0);
		int_type = new IntTypeNode(0);
		double_type = new DoubleTypeNode(0);
		undef_type = new UndefTypeNode(0);
		boolean_type = new BooleanTypeNode(0);
	}
	
	public void AddClass(String name){
		System.out.println("Added Class: " + name);
	}
}