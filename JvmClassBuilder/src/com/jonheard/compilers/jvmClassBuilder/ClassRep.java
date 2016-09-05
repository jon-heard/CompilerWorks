package com.jonheard.compilers.jvmClassBuilder;

import java.util.ArrayList;
import java.util.Collection;

import com.jonheard.util.HelperMethods;

/*
 * ClassRep - Represents the class being built for writing to a class file.
 */
public class ClassRep
{
	/// A bitflag representing modifiers for this class
	private short modFlags;
	/// The constant pool object that will be written to the class file
	private ConstantPool constantPool = new ConstantPool();

	/// A collection of the fields for this class
	private ArrayList<MemberRep> fieldList = new ArrayList<MemberRep>();
	/// A collection of the methods for this class
	private ArrayList<MemberRep> methodList = new ArrayList<MemberRep>();

	/// A series of indices into the constantPool.
	private short cpIndex_name, cpIndex_sourceFile, cpIndex_thisClass;
	private short cpIndex_superClass, cpIndex_sourceFileAttribute;

	public ClassRep(
			String name, String superType, Collection<String> modifiers,
			String sourceFile)
	{
		/// Most ClassRep state is stored in the constant pool
		cpIndex_name = constantPool.addUtf8(name);
		cpIndex_sourceFile = constantPool.addUtf8(sourceFile);
		cpIndex_thisClass = constantPool.addClass(name);
		cpIndex_superClass = constantPool.addClass(superType);
		cpIndex_sourceFileAttribute = constantPool.addUtf8("SourceFile");
		/// Mods are given as a collection of strings ("public", "static", etc)
		/// and stored as bit flags in a short.
		this.modFlags = HelperMethods.generateFlagsFromModifierList(modifiers);
		this.modFlags |= 0x0020; /// Modern Java requires this implicit modifier
	}

	public String getName()
	{
		/// The name is stored in the constant pool
		return constantPool.getUtf8(cpIndex_name);
	}
	
	public ConstantPool getConstantPool()
	{
		return constantPool;
	}

	public void addField(
			String name, String descriptor, Collection<String> modifiers)
	{
		FieldRep result = new FieldRep(
				name, descriptor, modifiers, constantPool);
		fieldList.add(result);
	}

	/// The returned MethodCodeBuilder object is used to fill code data for the
	/// method added here
	public MethodCodeBuilder addMethod(
			String name, String type, Collection<String> args,
			Collection<String> modifiers)
	{
		return addMethod(
				name,
				HelperMethods.buildMethodDescriptor(type, args),
				modifiers);
	}
	public MethodCodeBuilder addMethod(
			String name, String descriptor, Collection<String> modifiers)
	{
		/// Constructors & static block names are changed to fit jvm convention
		if(name.equals(getName())) name = "<init>";
		if(name.equals("static")) name = "<clinit>";

		MethodRep result = new MethodRep(
				name, descriptor, modifiers, constantPool);
		methodList.add(result);
		return result.getMethodCodeBuilder();
	}

	/// Get a jvm representation of this class
	public byte[] getJvmBytes()
	{
		DataBuffer resultData = new DataBuffer();

		/// Class header data
		resultData.add(0xCAFEBABE);      /// magic word
		resultData.add((short) 0x0000);  /// version
		resultData.add((short) 0x0034);
		resultData.add(constantPool);    /// constant pool
		resultData.add(modFlags);        /// access flags
		resultData.add(cpIndex_thisClass);  /// this class
		resultData.add(cpIndex_superClass); /// super class

		/// Interfaces
		resultData.add((short) 0);

		/// Fields
		resultData.add((short) fieldList.size());
		for (MemberRep i : fieldList)
		{
			resultData.add(i);
		}

		/// Methods
		resultData.add((short) methodList.size());
		for (MemberRep i : methodList)
		{
			resultData.add(i);
		}

		/// attributes (1 attribute: Source file name)
		resultData.add((short) 1);
		resultData.add((short) cpIndex_sourceFileAttribute);
		resultData.add(2);
		resultData.add((short) cpIndex_sourceFile);

		return resultData.toByteArray();
	}
}
