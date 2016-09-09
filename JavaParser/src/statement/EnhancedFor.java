package statement;

import com.jonheard.compilers.javaParser.ir.BaseIrType;

import com.jonheard.compilers.javaParser.ir.Variable;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;

public class EnhancedFor extends BaseIrType
{
	public EnhancedFor(RewindableQueue<JavaToken> tokenQueue)
	{
		mustBe(tokenQueue, JavaTokenType._FOR);
		mustBe(tokenQueue, JavaTokenType.PAREN_LEFT);
		addChild(new Variable(tokenQueue));
		mustBe(tokenQueue, JavaTokenType.COLON);
		addChild(new Expression(tokenQueue));
		mustBe(tokenQueue, JavaTokenType.PAREN_RIGHT);
		addChild(getNextStatement(tokenQueue));
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
