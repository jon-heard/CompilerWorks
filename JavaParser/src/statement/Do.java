package statement;

import com.jonheard.compilers.javaParser.ir.BaseIrType;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;

import expression.Parser_Expression;
import static com.jonheard.compilers.javaParser.JavaParser.*;

public class Do extends BaseIrType
{
	public Do(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue.peek());
		mustBe(tokenQueue, JavaTokenType._DO);
		BaseIrType body = Parser_Statement.getNextStatement(tokenQueue);
		mustBe(tokenQueue, JavaTokenType._WHILE);
		mustBe(tokenQueue, JavaTokenType.PAREN_LEFT);
		addChild(Parser_Expression.parseExpression(tokenQueue));
		mustBe(tokenQueue, JavaTokenType.PAREN_RIGHT);
		mustBe(tokenQueue, JavaTokenType.SEMICOLON);
		addChild(body);
	}

	@Override
	public String getHeaderString() { return ""; }
	
	public static boolean isNext(RewindableQueue<JavaToken> tokenQueue)
	{
		return see(tokenQueue, JavaTokenType._DO);
	}
}
