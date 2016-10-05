
package com.jonheard.compilers.irProcessor_java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jonheard.compilers.javaClasspathDatabase.JavaClasspathDatabase;
import com.jonheard.compilers.javaClasspathDatabase.Item.*;
import com.jonheard.compilers.parser_java.ir.*;
import com.jonheard.compilers.parser_java.ir.Package;
import com.jonheard.compilers.parser_java.ir.expression.MethodCall;
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
					data.getLine(), data.getId().getValue(),
					data.isStatic(), data.isOnDemand());
		}
		else if(ir instanceof Package)
		{
			Package data = (Package)ir;
			handleImport(
					data.getLine(), data.getId().getValue(),
					false, true);
		}
		else if(ir instanceof Type)
		{
			Type data = (Type)ir;
			handleType(data);
		}
		else if(ir instanceof MethodCall)
		{
			MethodCall data = (MethodCall)ir;
			handleMethodCall(data);
		}
		
		return ir;
	}
	
	private void handleType(Type data)
	{
		QualifiedId id = data.getId();
		Item path = libs.getValue(id.getValue());
		if(path instanceof Item_Err_NotFound)
		{
			QualifiedId newId = fullyQualifyId(id.getFirst());
			if(newId == null)
			{
				Logger.error(
						"cannot find symbol", source.getFilename(),
						id.getLine(), id.getColumn(),
						source.getLine(id.getLine()));
			}
			else
			{
				data.setId(newId);
			}
		}
	}
	
	private void handleMethodCall(MethodCall data)
	{
		QualifiedId id = data.getId();
		Item path = libs.getValue(id.getValue());
		if(path instanceof Item_Err_NotFound)
		{
			QualifiedId newId = fullyQualifyId(id.getFirst());
			if(newId == null)
			{
				Logger.error(
						"cannot find symbol", source.getFilename(),
						id.getLine(), id.getColumn(),
						source.getLine(id.getLine()));
			}
			else
			{
				QualifiedId finalId = mergeQualifiedIds(newId, id);
				data.setId(finalId);
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
	
	private QualifiedId fullyQualifyId(Id source)
	{
		if(!imports.containsKey(source.getValue())) return null;
		String[] path =
				imports.get(source.getValue()).getJavaAddress().split("\\.");
		List<Id> ids = new ArrayList<Id>();
		for(String node : path)
		{
			ids.add(new Id(node));
		}
		return new QualifiedId(
				source.getLine(), source.getColumn(), ids);
	}
	
	private QualifiedId mergeQualifiedIds(
			QualifiedId lhs, QualifiedId rhs)
	{
		List<Id> ids = new ArrayList<Id>();
		Id middleId = rhs.getFirst();
		for(int i = 0; i < lhs.getChildCount(); i++)
		{
			if(i == lhs.getChildCount()-1 && lhs.getChild(i).equals(middleId))
			{
				continue;
			}
			ids.add((Id)lhs.getChild(i));
		}
		for(int i = 0; i < rhs.getChildCount(); i++)
		{
			ids.add((Id)rhs.getChild(i));
		}		
		QualifiedId result = new QualifiedId(
				rhs.getLine(), rhs.getColumn(), ids);
		return result;
	}
}
