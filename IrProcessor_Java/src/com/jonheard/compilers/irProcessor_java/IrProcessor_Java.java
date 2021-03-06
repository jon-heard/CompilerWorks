package com.jonheard.compilers.irProcessor_java;

import java.util.LinkedList;
import java.util.List;

import com.jonheard.compilers.javaClasspathDatabase.JavaClasspathDatabase;
import com.jonheard.compilers.javaClasspathDatabase.Item.*;
import com.jonheard.compilers.parser_java.ir.*;
import com.jonheard.compilers.parser_java.ir.Class;
import com.jonheard.compilers.parser_java.ir.Package;
import com.jonheard.compilers.parser_java.ir.expression.Expression;
import com.jonheard.compilers.parser_java.ir.expression.Reference;
import com.jonheard.compilers.parser_java.ir.statement.CodeBlock;
import com.jonheard.util.Logger;
import com.jonheard.util.SourceFile;

public class IrProcessor_Java
{
	public void process(
			SourceFile source, JavaClasspathDatabase libs,
			CompilationUnit toProcess)
	{
		this.source = source;
		this.libs = libs;
		scopes.clear();
		process_helper(toProcess);
	}

	private LinkedList<Scope> scopes = new LinkedList<Scope>();
	private SourceFile source;
	private JavaClasspathDatabase libs;
	
	private Scope getCurrentScope()
	{
		return scopes.peek();
	}

	private BaseIrType process_helper(BaseIrType ir)
	{
		if(ir instanceof CompilationUnit)
		{
			preHandleCompilationUnit(ir);
		}
		else if(ir instanceof Package)
		{
			preHandlePackage(ir);
		}
		else if(ir instanceof Import)
		{
			preHandleImport(ir);
		}
		else if(ir instanceof Class)
		{
			preHandleClass(ir);
		}
		else if(ir instanceof CodeBlock)
		{
			preHandleCodeBlock(ir);
		}
		else if(ir instanceof Member)
		{
			preHandleMethodOrVariable(ir);
		}
		else if(ir instanceof Reference)
		{
			preHandleReference(ir);
		}


		for(int i = 0; i < ir.getChildCount(); i++)
		{
			ir.replaceChild(i, process_helper(ir.getChild(i)));
		}
		
		if(	ir instanceof CompilationUnit ||
			ir instanceof Class ||
			(ir instanceof Member &&
					((Member)ir).getIsMethod()) ||
			ir instanceof CodeBlock)
		{
			scopes.pop();
		}
		else if(ir instanceof Reference)
		{
			postHandleReference(ir);
			((Expression)ir).calcJavaType();
		}
		else if(ir instanceof Expression)
		{
			((Expression)ir).calcJavaType();
		}

		return ir;
	}
	
	private void preHandleCompilationUnit(BaseIrType ir)
	{
		CompilationUnit data = (CompilationUnit)ir;
		scopes.push(new Scope(ScopeType.COMPILATION_UNIT));
		Item path = libs.getValue("java.lang");
		if(path instanceof Item_Err_NotFound)
		{
			Logger.error(
					"namespace 'java.lang' not found", source.getFilename(),
					data.getRow(), data.getColumn(),
					source.getRowText(data.getRow()));
		}
		for(Item item : path)
		{
			getCurrentScope().add(item.getName(), item.getJavaAddressPrefix());
		}
	}

	private void preHandlePackage(BaseIrType ir)
	{
		Package data = (Package)ir;
		Item path = libs.getValue(data.getId().getValue());
		if(!(path instanceof Item_Err_NotFound))
		{
			for(Item item : path)
			{
				getCurrentScope().add(
						item.getName(), item.getJavaAddressPrefix());
			}
		}
	}
	
	private void preHandleImport(BaseIrType ir)
	{
		Import data = (Import)ir;
		Item path = libs.getValue(data.getId().getValue());
		if(data.getIsStatic())
		{
			if(data.getIsOnDemand())
			{
				if(path instanceof Item_Class)
				{
					for(Item item : path)
					{
						getCurrentScope().add(
								item.getName(), item.getJavaAddressPrefix());
						return;
					}
				}
			}
			else
			{
				if(path instanceof Item_Member)
				{
					getCurrentScope().add(
							path.getName(), path.getJavaAddressPrefix());
					return;
				}
			}
		}
		else
		{
			if(data.getIsOnDemand())
			{
				if(path instanceof Item_Package)
				{
					for(Item item : path)
					{
						getCurrentScope().add(
								item.getName(), item.getJavaAddressPrefix());
						return;
					}
				}
			}
			else
			{
				if(path instanceof Item_Class)
				{
					getCurrentScope().add(
							path.getName(), path.getJavaAddressPrefix());
					return;
				}
			}
		}
		Logger.error(
				"cannot find symbol: " + data.getId().getValue(),
				source.getFilename(), data.getRow(), data.getColumn(),
				source.getRowText(data.getRow()));
	}

	private void preHandleClass(BaseIrType ir)
	{
		Class data = (Class)ir;
		QualifiedId dataSuper = data.getSuper();
		dataSuper.addPrefix(getScopedValue(dataSuper.getValue()));
		scopes.push(new Scope(ScopeType.CLASS, getCurrentScope()));
		for(int i = data.getFirstPrintedChildIndex(); i < data.getChildCount();
				i++)
		{
			Member mCurrent = (Member)data.getChild(i);
			String name = mCurrent.getId().getValue();
			if(mCurrent.getModifiers().getIsStatic())
			{
				getCurrentScope().add(name, data.getName().getValue());
			}
			else
			{
				getCurrentScope().add(name, "this");
			}
			getCurrentScope().addJavaType(
					name, mCurrent.getType().getValue());
		}
	}

	private void preHandleCodeBlock(BaseIrType ir)
	{
		scopes.push(new Scope(ScopeType.CODE_BLOCK, getCurrentScope()));
	}
	
	private void preHandleMethodOrVariable(BaseIrType ir)
	{
		Member data = (Member)ir;
		String id = data.getType().getId().getValue();
		Item path = libs.getValue(id);
		if(path instanceof Item_Err_NotFound)
		{
			id = data.getType().getId().getFirst().getValue();
			String scopedId = getScopedValue(id) + "." + id;
			if(id == null)
			{
				Logger.error(
						"cannot find symbol: " + data.getId().getValue(),
						source.getFilename(), data.getRow(), data.getColumn(),
						source.getRowText(data.getRow()));
			}
			else
			{
				data.getType().getId().setValue(scopedId);
			}
		}
		
		if(	getCurrentScope().getScopeType() == ScopeType.CODE_BLOCK ||
			getCurrentScope().getScopeType() == ScopeType.METHOD)
		{
			getCurrentScope().add(data.getId().getValue());
			getCurrentScope().addJavaType(
					data.getId().getValue(), data.getType().getValue());
			String newIdValue =
					getCurrentScope().get(data.getId().getValue());
			data.setId(new Id(newIdValue));
		}
		
		if(data.getIsMethod())
		{
			scopes.push(new Scope(ScopeType.METHOD, getCurrentScope()));
		}
	}
	
	private void preHandleReference(BaseIrType ir)
	{
		Reference data = (Reference)ir;
		String scopedId = getScopedValue(
				data.getId().getFirst().getValue());
		if(scopedId == null)
		{
			Logger.error(
					"cannot find symbol: " + data.getId().getValue(),
					source.getFilename(), data.getRow(), data.getColumn(),
					source.getRowText(data.getRow()));
		}
		else
		{
			String prefix = scopedId;
			String javaType = getScopedJavaType(
					data.getId().getFirst().getValue());
			if(prefix.startsWith("#"))
			{
				data.setId(prefix);
				data.setJavaType(javaType);
			}
			else
			{
				data.addPrefix(prefix);
				Item path = libs.getValue(data.getId().getValue());
				if(path instanceof Item_Member)
				{
					String type = ((Item_Member)path).getDescriptor();
					int parenIndex = type.indexOf(")");
					if(parenIndex != -1)
					{
						type = type.substring(parenIndex+1);
					}
					type = descriptorToType(type);
					data.setJavaType(type);						
				}
				else
				{
					data.setJavaType(javaType);
				}

			}
		}
	}
	private String descriptorToType(String value)
	{
		if(value.equals("B")) value = "byte";
		else if(value.equals("C")) value = "char";
		else if(value.equals("D")) value = "double";
		else if(value.equals("F")) value = "float";
		else if(value.equals("I")) value = "int";
		else if(value.equals("J")) value = "long";
		else if(value.equals("S")) value = "short";
		else if(value.equals("Z")) value = "boolean";
		else if(value.equals("V")) value = "void";
		else value = value.substring(1, value.length()-1).replace('/', '.');
		return value;
	}
	
	private void postHandleReference(BaseIrType ir)
	{
		Reference data = (Reference)ir;
		String firstValue = data.getId().getFirst().getValue();
		if(!firstValue.equals("this") && !firstValue.startsWith("Test1") &&
				!firstValue.startsWith("#"))
		{
			List<Item> path = libs.getValue(data.getId().getValue()).
					getForwardAddress();
			int splitPoint = 0;
			while(path.get(splitPoint) instanceof Item_Package)
			{
				splitPoint++;
			}
			splitPoint++;
			data = data.makeSubReference(splitPoint);
		}
		do
		{
			data = data.makeSubReference();
		}
		while(data != null);
	}
	
	private String getScopedValue(String key)
	{
		String result = null;
		for(Scope scope : scopes)
		{
			if(scope.contains(key))
			{
				result = scope.get(key);
				break;
			}
		}
		return result;
	}
	
	private String getScopedJavaType(String key)
	{
		String result = "";
		for(Scope scope : scopes)
		{
			if(scope.containsJavaType(key))
			{
				result = scope.getJavaType(key);
				break;
			}
		}
		return result;
	}
}
