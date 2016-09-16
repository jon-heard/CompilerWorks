package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.util.RewindableQueue;

public class MethodCall extends BaseIrType
{

	public MethodCall(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue);
	}
}
