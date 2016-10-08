
package com.jonheard.compilers.irProcessor_java;

import java.util.ArrayList;
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
import com.jonheard.util.SourceFileInfo;

public class IrProcessor_Java
{
	public void process(
			SourceFileInfo source, JavaClasspathDatabase libs,
			CompilationUnit toProcess)
	{
		this.source = source;
		this.libs = libs;
		scopes.clear();
		process_helper(toProcess);
	}

	private LinkedList<Scope> scopes = new LinkedList<Scope>();
	private SourceFileInfo source;
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
		else if(ir instanceof MethodOrVariable)
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
			(ir instanceof MethodOrVariable &&
					((MethodOrVariable)ir).isMethod()) ||
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
					data.getLine(), data.getColumn(),
					source.getLine(data.getLine()));
		}
		for(Item item : path)
		{
			getCurrentScope().add(item.getName(), "", item.getJavaAddress());
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
				getCurrentScope().add(item.getName(),"",item.getJavaAddress());
			}
		}
	}
	
	private void preHandleImport(BaseIrType ir)
	{
		Import data = (Import)ir;
		Item path = libs.getValue(data.getId().getValue());
		if(data.isStatic())
		{
			if(data.isOnDemand())
			{
				if(path instanceof Item_Class)
				{
					for(Item item : path)
					{
						getCurrentScope().add(
								item.getName(), "", item.getJavaAddress());
						return;
					}
				}
			}
			else
			{
				if(path instanceof Item_Member)
				{
					getCurrentScope().add(
							path.getName(), "", path.getJavaAddress());
					return;
				}
			}
		}
		else
		{
			if(data.isOnDemand())
			{
				if(path instanceof Item_Package)
				{
					for(Item item : path)
					{
						getCurrentScope().add(
								item.getName(), "", item.getJavaAddress());
						return;
					}
				}
			}
			else
			{
				if(path instanceof Item_Class)
				{
					getCurrentScope().add(
							path.getName(), "", path.getJavaAddress());
					return;
				}
			}
		}
		Logger.error(
				"cannot find symbol: " + data.getId().getValue(),
				source.getFilename(), data.getLine(), data.getColumn(),
				source.getLine(data.getLine()));
	}

	private void preHandleClass(BaseIrType ir)
	{
		Class data = (Class)ir;
		scopes.push(new Scope(ScopeType.CLASS, getCurrentScope()));
		for(int i = data.getFirstPrintedChildIndex(); i < data.getChildCount();
				i++)
		{
			MethodOrVariable mCurrent = (MethodOrVariable)data.getChild(i);
			String name = mCurrent.getId().getValue();
			getCurrentScope().add(
					name, mCurrent.getJavaType().getValue(), "this." + name);
		}
	}

	private void preHandleCodeBlock(BaseIrType ir)
	{
		scopes.push(new Scope(ScopeType.CODE_BLOCK, getCurrentScope()));
	}
	
	private void preHandleMethodOrVariable(BaseIrType ir)
	{
		MethodOrVariable data = (MethodOrVariable)ir;
		String id = data.getJavaType().getId().getValue();
		Item path = libs.getValue(id);
		if(path instanceof Item_Err_NotFound)
		{
			id = data.getJavaType().getId().getFirst().getValue();
			ScopeItem scopedId = getScopedValue(id);
			if(id == null)
			{
				Logger.error(
						"cannot find symbol: " + data.getId().getValue(),
						source.getFilename(), data.getLine(), data.getColumn(),
						source.getLine(data.getLine()));
			}
			else
			{
				List<Id> items = makeIdListFromAddress(scopedId.getValue());
				data.getJavaType().setId(new QualifiedId(
						data.getJavaType().getLine(),
						data.getJavaType().getColumn(),
						items));

			}
		}
		
		if(	getCurrentScope().getScopeType() == ScopeType.CODE_BLOCK ||
			getCurrentScope().getScopeType() == ScopeType.METHOD)
		{
			getCurrentScope().add(
					data.getId().getValue(), data.getJavaType().getValue());
			String newIdValue =
					getCurrentScope().get(data.getId().getValue()).getValue();
			data.setId(new Id(newIdValue));
		}
		
		if(data.isMethod())
		{
			scopes.push(new Scope(ScopeType.METHOD, getCurrentScope()));
		}
	}
	
	private void preHandleReference(BaseIrType ir)
	{
		Reference data = (Reference)ir;
		Item path = libs.getValue(data.getId().getValue());
		if(path instanceof Item_Err_NotFound)
		{
			ScopeItem scopedId = getScopedValue(
					data.getId().getFirst().getValue());
			if(scopedId == null)
			{
				Logger.error(
						"cannot find symbol: " + data.getId().getValue(),
						source.getFilename(), data.getLine(), data.getColumn(),
						source.getLine(data.getLine()));
			}
			else
			{
				List<Id> lhs = makeIdListFromAddress(scopedId.getValue());
				if(!lhs.get(0).getValue().startsWith("#"))
				{
					List<Id> rhs = makeIdListFromQualifiedId(data.getId());
					if(lhs.get(lhs.size()-1).equals(rhs.get(0)))
					{
						rhs.remove(0);
					}
					lhs.addAll(rhs);
				}
				data.setId(new QualifiedId(
						data.getId().getLine(), data.getId().getColumn(), lhs));
				data.setJavaType(scopedId.getJavaType());
			}
		}
	}
	
	private void postHandleReference(BaseIrType ir)
	{
		Reference data = (Reference)ir;
		String firstValue = data.getId().getFirst().getValue();
		if(!firstValue.equals("this") && !firstValue.startsWith("#"))
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
	
	private ScopeItem getScopedValue(String key)
	{
		ScopeItem result = null;
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

	private List<Id> makeIdListFromAddress(String source)
	{
		String[] path = source.split("\\.");
		List<Id> result = new ArrayList<Id>();
		for(String node : path)
		{
			result.add(new Id(node));
		}
		return result;
	}
	
	private List<Id> makeIdListFromQualifiedId(QualifiedId source)
	{
		List<Id> result = new ArrayList<Id>();
		for(int i = 0; i < source.getChildCount(); i++)
		{
			result.add((Id)source.getChild(i));
		}
		return result;
	}
}
