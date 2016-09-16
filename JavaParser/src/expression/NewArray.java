package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.util.RewindableQueue;

public class NewArray extends BaseIrType
{

	public NewArray(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue);
	}
}
