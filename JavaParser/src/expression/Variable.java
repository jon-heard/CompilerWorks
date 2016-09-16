package expression;

import com.jonheard.compilers.javaParser.ir.Identifier;

public class Variable extends Expression
{

	public Variable(int line, Identifier id)
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
	
	public Identifier getName()
	{
		return (Identifier)getChild(0);
	}
}
