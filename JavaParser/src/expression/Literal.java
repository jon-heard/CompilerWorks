package expression;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;

public class Literal extends Expression
{
	public Literal(int line, JavaToken data)
	{
		super(ExpressionType.Literal, line);
		this.data = data;
	}
	
	@Override
	public String getTypeName()
	{
		return "Literal_" + getLiteralType().toString();
	}
	
	@Override
	public String getHeaderString()
	{
		return "value='" + getLiteralValue() + "'";
	}
	
	public JavaTokenType getLiteralType()
	{
		return data.getType();
	}
	
	public String getLiteralValue()
	{
		return data.getText();
	}
	
	private JavaToken data;
}
