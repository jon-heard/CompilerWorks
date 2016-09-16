package statement;

import com.jonheard.compilers.javaParser.ir.BaseIrType;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;
import expression.Parser_Expression;
import static com.jonheard.compilers.javaParser.JavaParser.*;

public class If extends BaseIrType
{
	public If(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue);
		mustBe(tokenQueue, JavaTokenType._IF);
		mustBe(tokenQueue, JavaTokenType.PAREN_LEFT);
		addChild(Parser_Expression.parseExpression(tokenQueue));
		mustBe(tokenQueue, JavaTokenType.PAREN_RIGHT);
		addChild(Parser_Statement.getNextStatement(tokenQueue));
	}

	public static boolean isNext(RewindableQueue<JavaToken> tokenQueue)
	{
		return see(tokenQueue, JavaTokenType._IF);
	}
}
