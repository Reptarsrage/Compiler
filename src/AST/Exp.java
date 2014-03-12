package AST;
import AST.Visitor.Visitor;

public abstract class Exp extends ASTNode {
    public enum ExprType {DOUBLE, OTHER};

    public ExprType t;
    public Exp(int lineNumber, ExprType t) {
	super(lineNumber);
	this.t = t;
    }
    public abstract void accept(Visitor v);
}
