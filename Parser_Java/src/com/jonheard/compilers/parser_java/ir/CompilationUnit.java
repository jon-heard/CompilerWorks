package com.jonheard.compilers.parser_java.ir;

import static com.jonheard.compilers.parser_java.JavaParser.*;

import java.util.List;

import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.util.Logger;
import com.jonheard.util.RewindableQueue;

public class CompilationUnit extends BaseIrType
{
	public CompilationUnit(List<Token> tokenList, int index)
	{
		super(tokenList.get(0).getRow());
		RewindableQueue<Token> tokenQueue =
				new RewindableQueue<Token>(tokenList);
		finalToken = tokenList.get(tokenList.size()-1);

		if(Package.isNext(tokenQueue))
		{
			addChild(new Package(tokenQueue));
			unnamedPackage = false;
		}
		while(Import.isNext(tokenQueue))
		{
			addChild(new Import(tokenQueue));
			importCount++;
		}
		while(!tokenQueue.isEmpty())
		{
			if(Class.isNext(tokenQueue))
			{
				addChild(new Class(tokenQueue));
			}
			else if(Interface.isNext(tokenQueue))
			{
				addChild(new Interface(tokenQueue));
			}
			else if(Enum.isNext(tokenQueue))
			{
				addChild(new Enum(tokenQueue));
			}
			else
			{
				Token next = tokenQueue.peek();
				Logger.error(
						"class, interface or enum expected",
						next.getFilename(), next.getRow(), next.getCol(),
						next.getLine());
			}
			typeCount++;
		}
	}
	
	@Override
	public String getHeaderString()
	{
		return  "importCount='" + getImportCount() + "' " +
				"typeCount='" + getTypeCount() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 0; }
	
	public Package getPackage()
	{
		if(unnamedPackage)
		{
			return null;
		}
		else
		{
			return (Package)getChild(0);
		}
	}

	public int getImportCount()
	{
		return importCount;
	}

	public int getTypeCount()
	{
		return typeCount;
	}

	private int importCount = 0;
	private int typeCount = 0;
	private boolean unnamedPackage = true;
}
