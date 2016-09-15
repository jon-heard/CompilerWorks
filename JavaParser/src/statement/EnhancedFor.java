package statement;

import com.jonheard.compilers.javaParser.ir.BaseIrType;

import com.jonheard.compilers.javaParser.ir.Variable;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;

import expression.Parser_Expression;
import static com.jonheard.compilers.javaParser.JavaParser.*;

public class EnhancedFor extends BaseIrType
{
	public EnhancedFor(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue.peek());
		mustBe(tokenQueue, JavaTokenType._FOR);
		mustBe(tokenQueue, JavaTokenType.PAREN_LEFT);
		addChild(new Variable(tokenQueue));
		mustBe(tokenQueue, JavaTokenType.COLON);
		addChild(Parser_Expression.parseExpression(tokenQueue));
		mustBe(tokenQueue, JavaTokenType.PAREN_RIGHT);
		addChild(Parser_Statement.getNextStatement(tokenQueue));
	}

	@Override
	public String getHeaderString() { return ""; }
	
	public static boolean isNext(RewindableQueue<JavaToken> tokenQueue)
	{
		boolean result = false;
		tokenQueue.remember();
		if(have(tokenQueue, JavaTokenType._FOR))
		{
			mustBe(tokenQueue, JavaTokenType.PAREN_LEFT);
			if(Variable.isNext(tokenQueue))
			{
				new Variable(tokenQueue);
			}
			if(see(tokenQueue, JavaTokenType.COLON))
			{
				result = true;
			}
		}
		tokenQueue.rewind();
		return result;
	}
}
