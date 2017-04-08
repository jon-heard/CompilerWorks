package com.jonheard.compilers.assembler_jvm.backEnd;

import java.util.ArrayList;
import java.util.List;

/*
 * DataBuffer - Stores byte, short, int and string data to be turned into
 * a byte array for binary file storage.
 *
 * Note: All 'add' methods return the index to the data being added.  This
 * allows for accessing that data later.
 */
public class DataBuffer
{
	/*
	 * BASIC
	 */
	/// Internal representation of this data buffer	
	private ArrayList<Byte> data = new ArrayList<Byte>();
	/// Expose data size
	public int size()
	{
		return data.size();
	}
	/// Expose data clearing
	public void clear()
	{
		data.clear();
	}
	/// Convert the data to a byte array for storage
	public byte[] toByteArray()
	{
		int size = data.size();
		byte[] result = new byte[size];
		for(int i = 0; i < size; i++)
		{
			result[i] = data.get(i).byteValue();
		}
		return result;
	}


	/*
	 *  COMPARISON
	 */
	/// A hash of the data.  Used to optimize frequent comparison during
	/// ConstantPool filling.
	private int hash = 0;
	/// Compare data buffers by their data
	public boolean equals(Object rhs)
	{
		if(!(rhs instanceof DataBuffer))
		{
			return false;
		}
		DataBuffer other = (DataBuffer)rhs;
		if(this.hash != other.hash) return false;
		return data.equals(other.data);
	}
	/// Called each time the data is changed to keep the hash value in sync
	private void recalcHash()
	{
		hash = 0;
		for(int i = 0; i < data.size(); i++)
		{
			hash = (hash + data.get(i) * (i+1)) % Integer.MAX_VALUE;
		}
	}


	/*
	 * ADD
	 */
	/// This interface is implemented to allow object storage in a DataBuffer
	public interface Serializable
	{
		/// Return a databuffer representation of this object
		DataBuffer serialize();
	}
	public int add(byte toAdd)
	{
		int result = data.size();
		data.add(toAdd);
		recalcHash();
		return result;
	}
	public int add(short toAdd)
	{
		int result = data.size();
		data.add( (byte)(toAdd >>> 8)  );
		data.add( (byte)(toAdd)        );
		recalcHash();
		return result;
	}	
	public int add(int toAdd)
	{
		int result = data.size();
		data.add( (byte)(toAdd >>> 24) );
		data.add( (byte)(toAdd >>> 16) );
		data.add( (byte)(toAdd >>>  8) );
		data.add( (byte)(toAdd)        );
		recalcHash();
		return result;
	}
	public int add(String toAdd)
	{
		return add(toAdd, true);
	}
	public int add(String toAdd, boolean includeSize)
	{
		int result = data.size();
		if(includeSize)
		{
			add((short)toAdd.length());
		}
		for(int i = 0; i < toAdd.length(); i++)
		{
			data.add((byte)toAdd.charAt(i));
		}
		recalcHash();
		return result;
	}
	/// Allow adding byte arrays directly
	public int add(byte[] toAdd)
	{
		int result = data.size();
		List<Byte> toAddAdjusted = new ArrayList<Byte>();
		for(byte i : toAdd)
		{
			toAddAdjusted.add(i);
		}
		data.addAll(toAddAdjusted);
		recalcHash();
		return result;
	}
	/// Allow merging data buffers
	public int add(DataBuffer toAdd)
	{
		int result = data.size();
		data.addAll(toAdd.data);
		recalcHash();
		return result;
	}
	/// Allow having types define their own way to be added to a databuffer
	public int add(Serializable toAdd)
	{
		int result = data.size();
		this.add(toAdd.serialize()); /// Uses the 'addDataBuffer' method above
		return result;
	}


	/*
	 * GETTERS
	 */
	/// Exception thrown when data is requested with an invalid index
	public class InvalidDataIndexException extends RuntimeException
	{
		private static final long serialVersionUID = 1087233952509149221L;
		public InvalidDataIndexException(int index)
		{
			super("DataBuffer does not have data for index: " + index);
		}
	}
	public byte getByte(int index) throws InvalidDataIndexException
	{
		if(index < 0 || index > data.size()-1)
		{
			throw new InvalidDataIndexException(index);
		}
		return data.get(index);
	}
	public short getShort(int index) throws InvalidDataIndexException
	{
		if(index < 0 || index > data.size()-2)
		{
			throw new InvalidDataIndexException(index);
		}
		short result = 0;
		result |= (short)((data.get(index)&0xFF) << 8);
		result |= data.get(index+1)&0xFF;
		return result;
	}
	public int getInt(int index) throws InvalidDataIndexException
	{
		if(index < 0 || index > data.size()-4)
		{
			throw new InvalidDataIndexException(index);
		}
		int result = 0;
		result |= data.get(index) << 24;
		result |= (data.get(index+1)&0xFF) << 16;
		result |= (data.get(index+2)&0xFF) <<  8;
		result |= data.get(index+3)&0xFF;
		return result;
	}
	public String getString(int index) throws InvalidDataIndexException
	{
		if(index < 0 || index > data.size()-1)
		{
			throw new InvalidDataIndexException(index);
		}
		short length = getShort(index);
		StringBuilder buffer = new StringBuilder();
		for(int i = 0; i < length; i++)
		{
			buffer.append((char)(byte)data.get(index+2+i));
		}
		return buffer.toString();
	}
	public String getString(int index, int length)
			throws InvalidDataIndexException
	{
		if(index < 0 || index > data.size()-length)
		{
			throw new InvalidDataIndexException(index);
		}
		StringBuilder buffer = new StringBuilder();
		for(int i = 0; i < length; i++)
		{
			buffer.append((char)(byte)data.get(index+i));
		}
		return buffer.toString();
	}


	/*
	 * ITERATOR
	 */
	/// Keeps track of a position in the data used to iterate over the data
	private int iteratorIndex = 0;
	/// Reset iteration
	public void resetIterator()
	{
		iteratorIndex = 0;
	}
	public void setIterator(int index)
	{
		iteratorIndex = index;
	}
	public boolean hasNext()
	{
		return iteratorIndex < data.size();
	}
	public byte nextByte() throws InvalidDataIndexException
	{
		byte result = getByte(iteratorIndex);
		iteratorIndex++;
		return result;
	}
	public short nextShort() throws InvalidDataIndexException
	{
		short result = getShort(iteratorIndex);
		iteratorIndex += 2;
		return result;
	}
	public int nextInt() throws InvalidDataIndexException
	{
		int result = getInt(iteratorIndex);
		iteratorIndex += 4;
		return result;
	}
	public String nextString() throws InvalidDataIndexException
	{
		String result = getString(iteratorIndex);
		iteratorIndex += result.length() + 2;
		return result;
	}
	public String nextString(int length) throws InvalidDataIndexException
	{
		String result = getString(iteratorIndex, length);
		iteratorIndex += length;
		return result;
	}


	/*
	 * SETTERS
	 */
	public void setByte(int index, byte value)
			throws InvalidDataIndexException
	{
		if(index < 0 || index > data.size()-1)
		{
			throw new InvalidDataIndexException(index);
		}
		data.set(index, value);
		recalcHash();
	}
	public void setShort(int index, short value)
			throws InvalidDataIndexException
	{
		if(index < 0 || index > data.size()-2)
		{
			throw new InvalidDataIndexException(index);
		}
		data.set(index, (byte)(value >>> 8));
		data.set(index+1, (byte)value);
		recalcHash();
	}
	public void setInt(int index, int value)
			throws InvalidDataIndexException
	{
		if(index < 0 || index > data.size()-4)
		{
			throw new InvalidDataIndexException(index);
		}
		data.set(index,   (byte)(value >>> 24));
		data.set(index+1, (byte)(value >>> 16));
		data.set(index+2, (byte)(value >>> 8));
		data.set(index+3, (byte)value);
		recalcHash();
	}
	public void setString(int index, String value)
			throws InvalidDataIndexException
	{
		int length = value.length();
		if(index < 0 || index > data.size()-length-2)
		{
			throw new InvalidDataIndexException(index);
		}
		setShort(index, (short)length);
		setStringWithoutSize(index+2, value);
	}
	public void setStringWithoutSize(int index, String value)
			throws InvalidDataIndexException
	{
		int length = value.length();
		if(index < 0 || index > data.size()-length)
		{
			throw new InvalidDataIndexException(index);
		}
		for(int i = 0; i < length; i++)
		{
			data.set(index+i, (byte)value.charAt(i));
		}
		recalcHash();
	}
}
