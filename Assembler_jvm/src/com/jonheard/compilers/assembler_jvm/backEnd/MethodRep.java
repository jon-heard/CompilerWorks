package com.jonheard.compilers.assembler_jvm.backEnd;

import java.util.Collection;

/*
 * MethodRep - Represents a method of a class.
 */
public class MethodRep extends MemberRep
{
	private DataBuffer codeData = new DataBuffer();
	private short stackSize = 0, localSize = 0;
	private int cpIndex_codeAttribute = 0;

	public MethodRep(String name, String descriptor,
			Collection<String> modifiers, ConstantPool constantPool)
	{
		super(name, descriptor, modifiers, constantPool);
		cpIndex_codeAttribute = constantPool.addUtf8("Code");
	}
	
	/// Gets a MethodCodeBuilder object for this methodRep.  A MethodCodebuilder
	/// holds functionality to write code data for a MethodRep.
	public MethodCodeBuilder getMethodCodeBuilder()
	{
		return new MethodCodeBuilder(this, codeData, constantPool);
	}

	public short getLocalSize()
	{
		return localSize;
	}
	public short getStackSize()
	{
		return stackSize;
	}
	void setLocalSize(int value)
	{
		localSize = (short)value;
	}
	void setStackSize(int value)
	{
		stackSize = (short)value;
	}

	/// Create a byte array filled with a jvm representation of this method
	@Override
	public DataBuffer serialize()
	{
		DataBuffer result = new DataBuffer();

		result.add((short)getModifiers());        /// flags
		result.add((short)getNameIndex());        /// name index
		result.add((short)getDescriptorIndex());  /// descriptor index
		result.add((short)1);                     /// attribute count(1: code)
		result.add((short)cpIndex_codeAttribute); /// name index (code)
		result.add(codeData.size() + 12);         /// length
		result.add(stackSize);                    /// max stack
		result.add(localSize);                    /// max locals
		result.add(codeData.size());              /// code length
		result.add(codeData);                     /// code
		result.add((short)0);                     /// exception table length
		result.add((short)0);                     /// attribute table length

		return result;
	}
}
