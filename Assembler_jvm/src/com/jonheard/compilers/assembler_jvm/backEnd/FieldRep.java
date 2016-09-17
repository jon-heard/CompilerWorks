package com.jonheard.compilers.assembler_jvm.backEnd;

import java.util.Collection;

/*
 * FieldRep - Represents a field of a class.
 */
public class FieldRep extends MemberRep
{
	public FieldRep(
			String name, String descriptor,	Collection<String> modifiers,
			ConstantPool constantPool)
	{
		super(name, descriptor, modifiers, constantPool);
	}

	/// Create a byte array filled with a jvm representation of this field
	@Override
	public DataBuffer serialize()
	{
		DataBuffer result = new DataBuffer();
		result.add((short)getModifiers());       /// flags
		result.add((short)getNameIndex());       /// name index
		result.add((short)getDescriptorIndex()); /// descriptor index
		result.add((short)0);                    /// attribute count
		return result;
	}
}
