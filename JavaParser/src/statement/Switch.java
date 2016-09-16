package statement;

import com.jonheard.compilers.javaParser.ir.BaseIrType;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;

import expression.Parser_Expression;
import static com.jonheard.compilers.javaParser.JavaParser.*;

public class Switch extends BaseIrType
{
	public Switch(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue);
		mustBe(tokenQueue, JavaTokenType._SWITCH);
		mustBe(tokenQueue, JavaTokenType.PAREN_LEFT);
		addChild(Parser_Expression.parseExpression(tokenQueue));
		mustBe(tokenQueue, JavaTokenType.PAREN_RIGHT);
		addChild(new CodeBlock(tokenQueue));
	}

	public static boolean isNext(RewindableQueue<JavaToken> tokenQueue)
	{
		return see(tokenQueue, JavaTokenType._SWITCH);
	}
}
