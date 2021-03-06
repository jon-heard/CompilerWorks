package com.jonheard.compilers.assembler_jvm.backEnd;

import java.util.Collection;

import com.jonheard.util.UtilMethods;

/*
 * MemberRep - Represents a member (field or method) of a class
 */
public abstract class MemberRep implements DataBuffer.Serializable
{

	@Override
	public abstract DataBuffer serialize();

	protected ConstantPool constantPool;

	private short cpIndex_name;
	private short cpIndex_descriptor;
	private short modifiers;
	
	public MemberRep(
		String name, String descriptor,
		Collection<String> modifierList, ConstantPool constantPool)
	{
		this.constantPool = constantPool;
		modifiers = UtilMethods.generateFlagsFromModifierList(modifierList);
		/// name and descriptor are held in the constant pool
		this.cpIndex_name = constantPool.addUtf8(name);
		this.cpIndex_descriptor = constantPool.addUtf8(descriptor);
	}
	
	public String getName()
	{
		return constantPool.getUtf8(cpIndex_name);
	}
	public String getDescriptor()
	{
		return constantPool.getUtf8(cpIndex_descriptor);
	}
	public short getModifiers()
	{
		return modifiers;
	}

	protected short getNameIndex()
	{
		return cpIndex_name;
	}
	protected short getDescriptorIndex()
	{
		return cpIndex_descriptor;
	}
}
