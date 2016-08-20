package com.jonheard.compilers.jvmClassBuilder;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/*
 * ConstantPool - A collection of constant values to be stored in a jvm class
 * file for constant value references within the rest of the class file.
 * Note: All 'add' methods return the index of the added constant.
 */
public class ConstantPool implements DataBuffer.Serializable
{
	/// Each constant value is stored as a DataBuffer object for byte array
	/// building
	protected ArrayList<DataBuffer> data = new ArrayList<DataBuffer>();
	
	public void clear()
	{
		data.clear();
	}

	/// Convert this ConstantPool into a databuffer for building a byte array
	@Override
	public DataBuffer serialize()
	{
		DataBuffer result = new DataBuffer();
		result.add((short)(data.size()+1));
		for(DataBuffer i : data)
		{
			result.add(i);
		}
		return result;
	}

	/// Add a data buffer as a constant.  It is expected that the data buffer
	/// is properly formatted to be a jvm constant.
	/// This is primarily a helper method for the other 'add' methods below.
	public short addDataBuffer(DataBuffer toAdd)
	{
		/// Look to see if the given constant has been added previously, storing
		/// its index to be returned
		short result = (short)(data.indexOf(toAdd)+1);
		/// If the given constant has not already been added, add it now
		if(result == 0)
		{
			data.add(toAdd);
			result = (short)(data.size());
		}
		/// Return the index for this constant
		return result;
	}

	/// Add a constant representing some text (for reference by other constants)
	public short addUtf8(String toAdd)
	{
		DataBuffer newBuffer = new DataBuffer();
		newBuffer.add((byte)1);
		newBuffer.add(toAdd);
		return addDataBuffer(newBuffer);
	}

	/// Get a text constant from the ConstantPool
	public String getUtf8(short index)
	{
		return data.get(index-1).getString(1);
	}

	/// Add a constant representing a class
	public short addClass(String className)
	{
		DataBuffer newBuffer = new DataBuffer();
		newBuffer.add((byte)7);
		short nameIndex = addUtf8(className);
		newBuffer.add(nameIndex);
		return addDataBuffer(newBuffer);
	}

	/// Add a constant representing the name and descriptor for a member
	public short addNameAndType(String name, String descriptor)
	{
		DataBuffer newBuffer = new DataBuffer();
		newBuffer.add((byte)12);
		short nameIndex = addUtf8(name);
		newBuffer.add(nameIndex);
		short descriptorIndex = addUtf8(descriptor);
		newBuffer.add(descriptorIndex);
		return addDataBuffer(newBuffer);		
	}

	/// Add a constant representing a field
	public short addField(String className, String name, String descriptor)
	{
		DataBuffer newBuffer = new DataBuffer();
		newBuffer.add((byte)9);
		short classIndex = addClass(className);
		newBuffer.add(classIndex);
		short nameAndTypeIndex = addNameAndType(name, descriptor);
		newBuffer.add(nameAndTypeIndex);
		return addDataBuffer(newBuffer);		
	}

	/// Add a constant representing a method
	public short addMethod(String className, String name, String descriptor)
	{
		DataBuffer newBuffer = new DataBuffer();
		newBuffer.add((byte)10);
		short classIndex = addClass(className);
		newBuffer.add(classIndex);
		short nameAndTypeIndex = addNameAndType(name, descriptor);
		newBuffer.add(nameAndTypeIndex);
		return addDataBuffer(newBuffer);		
	}
	
	/// Add a constant representing a string value
	public short addString(String value)
	{
		DataBuffer newBuffer = new DataBuffer();
		newBuffer.add((byte)8);
		short utf8Index = addUtf8(value);
		newBuffer.add(utf8Index);
		return addDataBuffer(newBuffer);
	}

	/// Add a constant representing a int value
	public short addInteger(int value)
	{
		DataBuffer newBuffer = new DataBuffer();
		newBuffer.add((byte)3);
		newBuffer.add(value);
		return addDataBuffer(newBuffer);
	}

	/// Add a constant representing a float value
	public short addFloat(float value)
	{
		DataBuffer newBuffer = new DataBuffer();
		newBuffer.add((byte)4);
		byte[] toAdd = ByteBuffer.allocate(4).putFloat(value).array();
		newBuffer.add(toAdd[0]);
		newBuffer.add(toAdd[1]);
		newBuffer.add(toAdd[2]);
		newBuffer.add(toAdd[3]);
		return addDataBuffer(newBuffer);
	}

	/// Add a constant representing a long value
	public short addLong(long value)
	{
		DataBuffer newBuffer = new DataBuffer();
		newBuffer.add((byte)5);
		newBuffer.add((byte)(value >> 56 & 0xff));
		newBuffer.add((byte)(value >> 48 & 0xff));
		newBuffer.add((byte)(value >> 40 & 0xff));
		newBuffer.add((byte)(value >> 32 & 0xff));
		newBuffer.add((byte)(value >> 24 & 0xff));
		newBuffer.add((byte)(value >> 16 & 0xff));
		newBuffer.add((byte)(value >> 8 & 0xff));
		newBuffer.add((byte)(value >> 0 & 0xff));
		return addDataBuffer(newBuffer);
	}

	/// Add a constant representing a double value
	public short addDouble(double value)
	{
		DataBuffer newBuffer = new DataBuffer();
		newBuffer.add((byte)6);
		byte[] toAdd = ByteBuffer.allocate(8).putDouble(value).array();
		newBuffer.add(toAdd[0]);
		newBuffer.add(toAdd[1]);
		newBuffer.add(toAdd[2]);
		newBuffer.add(toAdd[3]);
		newBuffer.add(toAdd[4]);
		newBuffer.add(toAdd[5]);
		newBuffer.add(toAdd[6]);
		newBuffer.add(toAdd[7]);
		return addDataBuffer(newBuffer);
	}
	
	public static ConstantPool nextConstantPool(DataBuffer buffer)
	{
		ConstantPool result = new ConstantPool();
		short poolSize = buffer.nextShort();
		for(int i = 0; i < poolSize-1; i++)
		{
			DataBuffer toAdd = new DataBuffer();
			result.data.add(toAdd);
			byte type = buffer.nextByte();
			toAdd.add(type);
			short size = 1;
			switch(type)
			{
				case 7:
				case 8: size = 2; break;
				case 15:
				case 16: size = 3; break;
				case 3:
				case 4:
				case 9:
				case 10:
				case 11:
				case 12:
				case 18: size = 4; break;
				case 5:
				case 6: size = 8; break;
				case 1:
					size = buffer.nextShort();
					toAdd.add(size);
					break;
			}
			for(int j = 0; j < size; j++)
			{
				toAdd.add(buffer.nextByte());
			}
		}
		return result;
	}
}
