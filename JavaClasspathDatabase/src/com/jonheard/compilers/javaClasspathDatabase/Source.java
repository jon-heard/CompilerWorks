package com.jonheard.compilers.javaClasspathDatabase;

import java.util.HashMap;
import java.util.Set;

import com.jonheard.compilers.javaClasspathDatabase.Item.*;

public abstract class Source
{
	public abstract void loadItemData(Item toLoad);
	protected abstract boolean loadData();
	
	public Source(JavaClasspathDatabase database, String filename)
	{
		this.database = database;
		root = new Item_Non(filename);
		if(!loadData())
		{
			valid = false;
		}
	}
	
	public Item getValue(String address)
	{
		Item result = getMemoized(address);
		if(result != null)
		{
			return result;
		}

		String[] addressItems = address.split("\\.");
		result = root;
		for(int i = 0; i < addressItems.length; i++)
		{
			if(result.hasChild(addressItems[i]))
			{
				result = result.getChild(addressItems[i]);
			}
			else
			{
				return new Item_Err_NotFound(result.getJavaAddress());
			}
		}

		memoization.put(address, result);
		return result;
	}
	
	protected void loadClassItemDataFromByteArray(
			byte[] data, Item classItem)
	{
		ClassFileMemberReader memberReader =
				new ClassFileMemberReader(data);
		Set<String> members = memberReader.getMemberList();
		for(String member : members)
		{
			String descriptor = memberReader.getMemberDescriptor(member);
			boolean isStatic = memberReader.isMemberStatic(member);
			String typeAddress = descriptor;
			{
				/// Get rid of parameter list in method descriptor
				int closeParenthesisIndex = typeAddress.indexOf(')');
				if(closeParenthesisIndex != -1)
				{
					typeAddress =typeAddress.substring(closeParenthesisIndex+1);
				}
				/// Expand descriptor shorthand
				if(typeAddress.equals("B")) { typeAddress = "byte"; }
				else if(typeAddress.equals("S")) { typeAddress = "short"; }
				else if(typeAddress.equals("I")) { typeAddress = "int"; }
				else if(typeAddress.equals("J")) { typeAddress = "long"; }
				else if(typeAddress.equals("F")) { typeAddress = "float"; }
				else if(typeAddress.equals("D")) { typeAddress = "double"; }
				else if(typeAddress.equals("Z")) { typeAddress = "boolean"; }
				else if(typeAddress.equals("C")) { typeAddress = "char"; }
				else if(typeAddress.equals("V")) { typeAddress = "void"; }
				else
				{
					typeAddress =
							typeAddress.substring(1, typeAddress.length()-1).
							replace("/", ".");
				}
			}
			Item type = (database==null) ? null :database.getValue(typeAddress);
			new Item_Member(member, classItem, type, descriptor, isStatic);
		}
	}
	
	protected Item getMemoized(String address)
	{
		if(memoization.containsKey(address))
		{
			return memoization.get(address);
		}
		return null;
	}
	
	public boolean isValid() { return valid; }

	protected boolean valid = true;
	protected Item_Non root;
	protected HashMap<String, Item> memoization =
			new HashMap<String, Item>();
	
	private JavaClasspathDatabase database;
}
