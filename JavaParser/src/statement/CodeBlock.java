package statement;

import com.jonheard.compilers.javaParser.ir.BaseIrType;
import com.jonheard.compilers.javaParser.ir.Variable;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;

public class CodeBlock extends BaseIrType
{
	public CodeBlock(RewindableQueue<JavaToken> tokenQueue)
	{
		mustBe(tokenQueue, JavaTokenType.CURL_BRACE_LEFT);
		while(!have(tokenQueue, JavaTokenType.CURL_BRACE_RIGHT))
		{
			if(Variable.isNext(tokenQueue))
			{
				addChild(new Variable(tokenQueue));
			}
			else
			{
				addChild(getNextStatement(tokenQueue));
			}
		}
	}

	@Override
	public String getHeaderString() { return ""; }
	
	public static boolean isNext(RewindableQueue<JavaToken> tokenQueue)
	{
		return see(tokenQueue, JavaTokenType.CURL_BRACE_LEFT);
	}
}
