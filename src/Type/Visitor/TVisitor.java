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

public interface TVisitor {
  public void visit(ArrayType n);
  public void visit(BlockType n);
  public void visit(BooleanType n);
  public void visit(ClassType n);
  public void visit(DoubleType n);
  public void visit(IntType n);
  public void visit(MethodType n);
  public void visit(PackageType n);
  public void visit(TypeNode n);
  public void visit(UndefType n);
}
