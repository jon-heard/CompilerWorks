
package com.jonheard.compilers.irProcessor_java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jonheard.compilers.javaClasspathDatabase.JavaClasspathDatabase;
import com.jonheard.compilers.javaClasspathDatabase.Item.*;
import com.jonheard.compilers.parser_java.ir.*;
import com.jonheard.compilers.parser_java.ir.Package;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.Logger;
import com.jonheard.util.SourceFileInfo;

public class IrProcessor_Java
{
	public void process(
			SourceFileInfo source, JavaClasspathDatabase libs,
			CompilationUnit toProcess)
	{
		this.source = source;
		this.libs = libs;
		imports.clear();
		handleImport(0, "java.lang", false, true);
		process_helper(toProcess);
		for(Item item : imports.values())
		{
			System.out.println(item.getJavaAddress());
		}
	}

	private HashMap<String, Item> imports = new HashMap<String, Item>();
	private SourceFileInfo source;
	private JavaClasspathDatabase libs;

	private BaseIrType process_helper(BaseIrType ir)
	{
		for(int i = 0; i < ir.getChildCount(); i++)
		{
			ir.replaceChild(i, process_helper(ir.getChild(i)));
		}

		if(ir instanceof Import)
		{
			Import data = (Import)ir;
			handleImport(
					data.getLine(), data.getIdentifier().getValue(),
					data.isStatic(), data.isOnDemand());
		}
		else if(ir instanceof Package)
		{
			Package data = (Package)ir;
			handleImport(
					data.getLine(), data.getIdentifier().getValue(),
					false, true);
		}
		else if(ir instanceof Type)
		{
			Type data = (Type)ir;
			handleType(data);
		}
		
		return ir;
	}
	
	private void handleType(Type data)
	{
		QualifiedIdentifier id = data.getIdentifier();
		if(libs.getValue(id.getValue()) instanceof Item_Err_NotFound)
		{
			QualifiedIdentifier newId = fullyQualifyIdentifier(id);
			if(newId == null)
			{
				Logger.error(
						"cannot find symbol", source.getFilename(),
						id.getLine(), id.getColumn(),
						source.getLine(id.getLine()));
			}
			else
			{
				data.setIdentifier(newId);
			}
		}
	}
	
	private void handleImport(
			int line, String pathString, boolean isStatic, boolean isOnDemand)
	{
		Item path = libs.getValue(pathString);
		if(isStatic)
		{
			if(isOnDemand)
			{
				if(!(path instanceof Item_Class))
				{
					Logger.error("cannot find symbol", "", line, 0, "");
				}
				for(Item item : path)
				{
					imports.put(item.getName(), item);
				}
			}
			else
			{
				if(!(path instanceof Item_Member))
				{
					Logger.error("cannot find symbol", "", line, 0, "");
				}
				imports.put(path.getName(), path);
			}
		}
		else
		{
			if(isOnDemand)
			{
				if(!(path instanceof Item_Package))
				{
					Logger.error("cannot find symbol", "", line, 0, "");
				}
				for(Item item : path)
				{
					imports.put(item.getName(), item);
				}
			}
			else
			{
				if(!(path instanceof Item_Class))
				{
					Logger.error("cannot find symbol", "", line, 0, "");
				}
				imports.put(path.getName(), path);
			}
		}
	}
	
	private QualifiedIdentifier fullyQualifyIdentifier(QualifiedIdentifier id)
	{
		if(!imports.containsKey(id.getValue())) return null;
		String[] path =
				imports.get(id.getValue()).getJavaAddress().split("\\.");
		List<Identifier> identifiers = new ArrayList<Identifier>();
		for(String node : path)
		{
			identifiers.add(new Identifier(node));
		}
		Token.setCurrentRow(id.getLine());
		return new QualifiedIdentifier(
				new Token(TokenType._ABSTRACT, id.getColumn()),
				identifiers);
	}
}
