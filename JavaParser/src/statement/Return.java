package statement;

import com.jonheard.compilers.javaParser.ir.BaseIrType;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;
import expression.Parser_Expression;
import static com.jonheard.compilers.javaParser.JavaParser.*;

public class Return extends BaseIrType
{
	public Return(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue.peek());
		mustBe(tokenQueue, JavaTokenType._RETURN);
		if(!see(tokenQueue, JavaTokenType.SEMICOLON))
		{
			addChild(Parser_Expression.parseExpression(tokenQueue));
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
