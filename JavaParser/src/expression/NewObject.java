package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.util.RewindableQueue;

public class NewObject extends BaseIrType
{

	public NewObject(RewindableQueue<JavaToken> tokenQueue)
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
