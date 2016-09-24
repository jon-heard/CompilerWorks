package com.jonheard.compilers.javaClasspathDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class ClassFileMemberReader
{
	public ClassFileMemberReader(byte[] data)
	{
		if(!init(data))
		{
			valid = false;
		}
	}

	public boolean isValid() { return valid; }

	public boolean hasMember(String name)
	{
		return memberMap.containsKey(name);
	}
	
	public String getMemberDescriptor(String name)
	{
		if(hasMember(name))
		{
			return memberMap.get(name).descriptor;
		}
		else
		{
			return null;
		}
	}
	
	public boolean isMemberStatic(String name)
	{
		if(hasMember(name))
		{
			return memberMap.get(name).staticMember;
		}
		else
		{
			return false;
		}
	}
	
	public boolean isMemberAMethod(String name)
	{
		if(hasMember(name))
		{
			return memberMap.get(name).descriptor.startsWith("(");
		}
		else
		{
			return false;
		}
	}

	public boolean isMemberAField(String name)
	{
		if(hasMember(name))
		{
			return !memberMap.get(name).descriptor.startsWith("(");
		}
		else
		{
			return false;
		}
	}
	
	public int getMemberCount() { return memberMap.size(); }
	
	public Set<String> getMemberList() { return memberMap.keySet(); }


	private boolean valid = true;
	private HashMap<String, MemberInfo> memberMap =
			new HashMap<String, MemberInfo>();
	
	private int unsign(byte value) { return (int)value & 0xFF; }

	private int unsignedShort(byte value1, byte value2)
	{
		return (unsign(value1) << 8) + unsign(value2);
	}
	
	private int unsignedInt(byte value1, byte value2, byte value3, byte value4)
	{
		return	(unsign(value1) << 24) + (unsign(value2) << 16) +
				(unsign(value3) << 8) + (unsign(value4));
	}

	private boolean init(byte[] data)
	{
		if(data == null)
		{
			return false;
		}
		if(
				unsign(data[0]) != 0xCA || unsign(data[1]) != 0xFE ||
				unsign(data[2]) != 0xBA || unsign(data[3]) != 0xBE)
		{
			return false;
		}
		
		HashMap<Integer,String> utf8Map = new HashMap<Integer,String>();
		int poolCount = unsignedShort(data[8], data[9])-1;
		int ptr = 10;
		for(int i = 0; i < poolCount; i++)
		{
			int length = 0;
			switch(data[ptr])
			{
				case 7: // class
				case 8: // string
				case 16: // methodtype
					length = 3;
					break;
				case 15: // methodhandle
					length = 4;
					break;
				case 3: // integer
				case 4: // float
				case 9: // fieldref
				case 10: // methodref
				case 11: // interfacemethodref
				case 12: // nameandtype
				case 18: // invokedynamic
					length = 5;
					break;
				case 5: // long
				case 6: // double
					length = 9;
					break;
				case 1: // utf8
					length = unsignedShort(data[ptr+1], data[ptr+2]);
					ptr+=3;
					utf8Map.put(
							i+1,
							new String(Arrays.copyOfRange(data,ptr,ptr+length)));
					break;
				default:
					return false;
			}
			ptr += length;
		}
		ptr += 6;
		ptr += unsignedShort(data[ptr], data[ptr+1]) * 2;
		ptr += 2;
		int fieldCount = unsignedShort(data[ptr], data[ptr+1]);
		ptr += 2;
		for(int i = 0; i < fieldCount; i++)
		{
			int modifiers = unsignedShort(data[ptr], data[ptr+1]);
			ptr += 2;
			String name = utf8Map.get(unsignedShort(data[ptr], data[ptr+1]));
			ptr += 2;
			String descriptor =
					utf8Map.get(unsignedShort(data[ptr], data[ptr+1]));
			ptr += 2;
			int attributeCount = unsignedShort(data[ptr], data[ptr+1]);
			ptr += 2;
			for(int j = 0; j < attributeCount; j++)
			{
				ptr += 2;
				ptr += unsignedInt(
						data[ptr], data[ptr+1], data[ptr+2], data[ptr+3]);
				ptr += 4;
				
			}
			memberMap.put(name, new MemberInfo(descriptor, modifiers));
		}
		int methodCount = unsignedShort(data[ptr], data[ptr+1]);
		ptr += 2;
		for(int i = 0; i < methodCount; i++)
		{
			int modifiers = unsignedShort(data[ptr], data[ptr+1]);
			ptr += 2;
			String name = utf8Map.get(unsignedShort(data[ptr], data[ptr+1]));
			ptr += 2;
			String descriptor =
					utf8Map.get(unsignedShort(data[ptr], data[ptr+1]));
			ptr += 2;
			int attributeCount = unsignedShort(data[ptr], data[ptr+1]);
			ptr += 2;
			for(int j = 0; j < attributeCount; j++)
			{
				ptr += 2;
				ptr += unsignedInt(
						data[ptr], data[ptr+1], data[ptr+2], data[ptr+3]);
				ptr += 4;
				
			}
			memberMap.put(name, new MemberInfo(descriptor, modifiers));
		}

		return true;
	}
}

class MemberInfo
{
	public String descriptor;
	public boolean staticMember = false;
	public MemberInfo(String descriptor, int modifiers)
	{
		this.descriptor = descriptor;
		if((modifiers & 0x0008) != 0) staticMember = true;
	}
}

