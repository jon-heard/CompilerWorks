package statement;

import com.jonheard.compilers.javaParser.ir.BaseIrType;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;

public class While extends BaseIrType
{
	public While(RewindableQueue<JavaToken> tokenQueue)
	{
		mustBe(tokenQueue, JavaTokenType._WHILE);
		mustBe(tokenQueue, JavaTokenType.PAREN_LEFT);
		addChild(new Expression(tokenQueue));
		mustBe(tokenQueue, JavaTokenType.PAREN_RIGHT);
		addChild(getNextStatement(tokenQueue));
	}

	@Override
	public String getHeaderString() { return ""; }

	public static boolean isNext(RewindableQueue<JavaToken> tokenQueue)
	{
		return see(tokenQueue, JavaTokenType._WHILE);
	}
}
