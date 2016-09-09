package statement;

import com.jonheard.compilers.javaParser.ir.BaseIrType;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;

public class Return extends BaseIrType
{
	public Return(RewindableQueue<JavaToken> tokenQueue)
	{
		mustBe(tokenQueue, JavaTokenType._RETURN);
		if(!see(tokenQueue, JavaTokenType.SEMICOLON))
		{
			addChild(new Expression(tokenQueue));
		}
		mustBe(tokenQueue, JavaTokenType.SEMICOLON);
	}

	@Override
	public String getHeaderString() { return ""; }

	public static boolean isNext(RewindableQueue<JavaToken> tokenQueue)
	{
		return see(tokenQueue, JavaTokenType._RETURN);
	}
}
