package com.jonheard.compilers.javaParser.ir;

import java.util.List;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;

public class CompilationUnit extends BaseType
{
	private int importCount = 0;
	private int typeCount = 0;
	
	public CompilationUnit(List<JavaToken> tokenList, int index)
	{
		RewindableQueue<JavaToken> tokenQueue =
				new RewindableQueue<JavaToken>(tokenList);
		if(tokenQueue.peek().getType() == JavaTokenType._PACKAGE)
		{
			children.add(new PackageDeclaration(tokenQueue));
		}
		while(tokenQueue.peek().getType() == JavaTokenType._IMPORT)
		{
			children.add(new ImportDeclaration(tokenQueue));
			importCount++;
		}
		while(!tokenQueue.isEmpty())
		{
			List_Modifiers mods = new List_Modifiers(tokenQueue);
			switch(tokenQueue.peek().getType())
			{
			case _CLASS:
				children.add(new ClassDeclaration(tokenQueue, mods));
				break;
			case _INTERFACE:
				children.add(new InterfaceDeclaration(tokenQueue, mods));
				break;
			case _ENUM:
				children.add(new EnumDeclaration(tokenQueue, mods));
				break;
			default:
				break;
			}
			typeCount++;
		}
	}
	
	@Override
	public String getHeaderString()
	{
		return  "importCount='" + importCount + "' " +
				"typeCount='" + typeCount + "'";
	}
}
