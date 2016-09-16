package expression;

import com.jonheard.compilers.javaParser.ir.QualifiedIdentifier;

public class Field extends Expression
{

	public Field(int line, QualifiedIdentifier id)
	{
		super(ExpressionType.Variable, line);
		addChild(id);
	}
	
	@Override
	public String getHeaderString()
	{
		return	"name='" + getName().getValue() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 1; }
	
	public QualifiedIdentifier getName()
	{
		return (QualifiedIdentifier)getChild(0);
	}
}
