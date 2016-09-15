package statement;

import com.jonheard.compilers.javaParser.ir.BaseIrType;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;
import static com.jonheard.compilers.javaParser.JavaParser.*;

public class For extends BaseIrType
{
	public For(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue.peek());
		mustBe(tokenQueue, JavaTokenType._FOR);
		mustBe(tokenQueue, JavaTokenType.PAREN_LEFT);
		while(!have(tokenQueue, JavaTokenType.PAREN_RIGHT))
		{
			tokenQueue.poll();
		}
		addChild(Parser_Statement.getNextStatement(tokenQueue));
	}

	@Override
	public String getHeaderString() { return ""; }
	
	public static boolean isNext(RewindableQueue<JavaToken> tokenQueue)
	{
		return see(tokenQueue, JavaTokenType._FOR);
	}
}
