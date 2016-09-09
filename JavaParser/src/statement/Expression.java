package statement;

import com.jonheard.compilers.javaParser.ir.BaseIrType;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.util.RewindableQueue;

public class Expression extends BaseIrType
{
	public Expression(RewindableQueue<JavaToken> tokenQueue)
	{
		tokenQueue.poll();
		addChild(new Expression(tokenQueue));
	}

	@Override
	public String getHeaderString() { return ""; }
}
