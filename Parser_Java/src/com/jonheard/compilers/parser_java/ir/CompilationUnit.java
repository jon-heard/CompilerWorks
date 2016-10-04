package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.util.Logger;

public class CompilationUnit extends BaseIrType
{
	public CompilationUnit(Parser_Java parser)
	{
		super(parser);

		if(Package.isNext(parser))
		{
			addChild(new Package(parser));
			unnamedPackage = false;
		}
		while(Import.isNext(parser))
		{
			addChild(new Import(parser));
			importCount++;
		}
		while(!parser.getTokenQueue().isEmpty())
		{
			if(Class.isNext(parser))
			{
				addChild(new Class(parser));
			}
			else if(Interface.isNext(parser))
			{
				addChild(new Interface(parser));
			}
			else if(Enum.isNext(parser))
			{
				addChild(new Enum(parser));
			}
			else
			{
				Token next = parser.getTokenQueue().poll();
				Logger.error(
						"class, interface or enum expected",
						parser.getSource().getFilename(), next.getRow(),
						next.getColumn(),
						parser.getSource().getLine(next.getRow()));
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
