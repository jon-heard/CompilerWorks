
package com.jonheard.compilers.irProcessor_java;

import java.util.HashMap;

import com.jonheard.compilers.javaClasspathDatabase.JavaClasspathDatabase;
import com.jonheard.compilers.javaClasspathDatabase.Item.*;
import com.jonheard.compilers.parser_java.ir.*;
import com.jonheard.util.Logger;

public class IrProcessor_Java
{
	HashMap<String, Item> imports = new HashMap<String, Item>();
	JavaClasspathDatabase libs;
	
	public IrProcessor_Java(JavaClasspathDatabase libs)
	{
		this.libs = libs;
	}
	
	public void process(CompilationUnit toProcess)
	{
		imports.clear();
		process_helper(toProcess);
	}
	
	private BaseIrType process_helper(BaseIrType ir)
	{
		for(int i = 0; i < ir.getChildCount(); i++)
		{
			ir.replaceChild(i, process_helper(ir.getChild(i)));
		}

		if(ir instanceof Import)
		{
			Import val = (Import)ir;
			String id = val.getIdentifier().getValue();
			Item path = libs.getValue(id);
			if(val.isStatic())
			{
				if(val.isOnDemand())
				{
					if(!(path instanceof Item_Class))
					{
						Logger.error("cannot find symbol", "", ir.getLine(), 0, "");
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
						Logger.error("cannot find symbol", "", ir.getLine(), 0, "");
					}
					imports.put(path.getName(), path);
				}
			}
			else
			{
				if(val.isOnDemand())
				{
					if(!(path instanceof Item_Package))
					{
						Logger.error("cannot find symbol", "", ir.getLine(), 0, "");
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
						Logger.error("cannot find symbol", "", ir.getLine(), 0, "");
					}
					imports.put(path.getName(), path);
				}
			}
		}
		
		return ir;
	}
}
