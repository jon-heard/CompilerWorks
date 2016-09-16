package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.util.RewindableQueue;

public class SuperConstructor extends BaseIrType
{

	public SuperConstructor(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue);
	}
}
