package statement;

import com.jonheard.compilers.javaParser.ir.BaseIrType;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;

public class Switch extends BaseIrType
{
	public Switch(RewindableQueue<JavaToken> tokenQueue)
	{
		mustBe(tokenQueue, JavaTokenType._SWITCH);
		mustBe(tokenQueue, JavaTokenType.PAREN_LEFT);
		addChild(new Expression(tokenQueue));
		mustBe(tokenQueue, JavaTokenType.PAREN_RIGHT);
		addChild(new CodeBlock(tokenQueue));
	}

	@Override
	public String getHeaderString() { return ""; }

	public static boolean isNext(RewindableQueue<JavaToken> tokenQueue)
	{
		return see(tokenQueue, JavaTokenType._SWITCH);
	}
}
