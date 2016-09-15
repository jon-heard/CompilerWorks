package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.util.RewindableQueue;

public final class PostIncrement extends BaseIrType
{

	public PostIncrement(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue.peek());
	}

	@Override
	public String getHeaderString()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
