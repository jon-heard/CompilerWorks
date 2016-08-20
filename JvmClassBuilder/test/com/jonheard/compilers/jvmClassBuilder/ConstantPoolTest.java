package com.jonheard.compilers.jvmClassBuilder;

import static org.junit.Assert.*;
import org.junit.Test;

import com.jonheard.compilers.jvmClassBuilder.ConstantPool;
import com.jonheard.compilers.jvmClassBuilder.DataBuffer;

import java.util.ArrayList;

public class ConstantPoolTest
{
	class ConstantPoolTestClass extends ConstantPool
	{
		public ArrayList<DataBuffer> getData()
		{
			return data;
		}
	}

	@Test
	public void initialize()
	{
		ConstantPoolTestClass c = new ConstantPoolTestClass();
		assertEquals(0, c.getData().size());
	}

	@Test
	public void clear()
	{
		ConstantPoolTestClass c = new ConstantPoolTestClass();
		c.getData().add(new DataBuffer());
		assertEquals(1, c.getData().size());
		c.clear();
		assertEquals(0, c.getData().size());
	}

	@Test
	public void serialize()
	{
		ConstantPoolTestClass c = new ConstantPoolTestClass();
		DataBuffer db1 = new DataBuffer();
		db1.add((byte)1);
		db1.add("hi");
		DataBuffer db2 = new DataBuffer();
		db2.add((byte)1);
		db2.add("shy");
		c.getData().add(db1);
		c.getData().add(db2);
		byte[] expected =
		{
				0, 3,
				1, 0, 2, 'h', 'i',
				1, 0, 3, 's', 'h', 'y'
		};
		byte[] actual = c.serialize().toByteArray();
		assertArrayEquals(expected, actual);
	}

	@Test
	public void addUtf8String()
	{
		ConstantPoolTestClass c = new ConstantPoolTestClass();
		DataBuffer db1 = new DataBuffer();
			db1.add((byte)1);
			db1.add("first");
			c.getData().add(db1);
		c.addUtf8("first");
		c.addUtf8("second");

		DataBuffer data = c.serialize();
		assertEquals(3, data.nextShort());
		assertEquals(1, data.nextByte());
		assertEquals("first", data.nextString());
		assertEquals(1, data.nextByte());
		assertEquals("second", data.nextString());
		assertFalse(data.hasNext());
	}

	@Test
	public void getUtf8String() /// Relies on 'addUtf8String' tests
	{
		ConstantPoolTestClass c = new ConstantPoolTestClass();
		short index1 = c.addUtf8("first");
		short index2 = c.addUtf8("second");

		String output1 = c.getUtf8(index1);
		assertEquals("first", output1);

		String output2 = c.getUtf8(index2);
		assertEquals("second", output2);
	}
	
	@Test
	public void addDataBuffer()
	{
		ConstantPoolTestClass c = new ConstantPoolTestClass();
		DataBuffer db1 = new DataBuffer();
			db1.add((byte)5);
			db1.add((byte)25);
			db1.add((byte)58);
			c.getData().add(db1);
		DataBuffer db2 = new DataBuffer();
			db2.add((byte)5);
			db2.add((byte)25);
			db2.add((byte)58);
		DataBuffer db3 = new DataBuffer();
			db3.add((byte)32);
			db3.add((byte)15);
			db3.add((byte)90);
		int index1 = c.addDataBuffer(db2);
		int index2 = c.addDataBuffer(db3);

		assertEquals(1, index1);
		assertEquals(2, index2);
		assertEquals(2, c.getData().size());

		DataBuffer data = c.serialize();
		assertEquals(3, data.nextShort());
		assertEquals(5, data.nextByte());
		assertEquals(25, data.nextByte());
		assertEquals(58, data.nextByte());
		assertEquals(32, data.nextByte());
		assertEquals(15, data.nextByte());
		assertEquals(90, data.nextByte());
		assertFalse(data.hasNext());
	}
	
	@Test
	public void addClass()
	{
		ConstantPoolTestClass c = new ConstantPoolTestClass();
		DataBuffer db1 = new DataBuffer();
			db1.add((byte)1);
			db1.add("first");
			c.getData().add(db1);
		DataBuffer db2 = new DataBuffer();
			db2.add((byte)1);
			db2.add("second");
			c.getData().add(db2);
		DataBuffer db3 = new DataBuffer();
			db3.add((byte)7);
			db3.add((short)1);
			c.getData().add(db3);
		int index1 = c.addClass("first");
		int index2 = c.addClass("second");
		int index3 = c.addClass("third");

		assertEquals(3, index1);
		assertEquals(4, index2);
		assertEquals(6, index3);

		DataBuffer data = c.serialize();
		assertEquals(7, data.nextShort());
		assertEquals(1, data.nextByte());
		assertEquals("first", data.nextString());
		assertEquals(1, data.nextByte());
		assertEquals("second", data.nextString());
		assertEquals(7, data.nextByte());
		assertEquals(1, data.nextShort());
		assertEquals(7, data.nextByte());
		assertEquals(2, data.nextShort());
		assertEquals(1, data.nextByte());
		assertEquals("third", data.nextString());
		assertEquals(7, data.nextByte());
		assertEquals(5, data.nextShort());
		assertFalse(data.hasNext());
	}
	
	@Test
	public void addNameAndType()
	{
		ConstantPoolTestClass c = new ConstantPoolTestClass();
		c.addUtf8("name");
		c.addUtf8("descriptor");
		int index = c.addNameAndType("name", "descriptor");

		assertEquals(3, index);

		DataBuffer data = c.serialize();
		assertEquals(4, data.nextShort());
		assertEquals(1, data.nextByte());
		assertEquals("name", data.nextString());
		assertEquals(1, data.nextByte());
		assertEquals("descriptor", data.nextString());
		assertEquals(12, data.nextByte());
		assertEquals(1, data.nextShort());
		assertEquals(2, data.nextShort());
		assertFalse(data.hasNext());
	}

	@Test
	public void addField() /// Relies on 'addUtf8' and 'addNameAndType' tests
	{
		ConstantPoolTestClass c = new ConstantPoolTestClass();
		c.addUtf8("class");
		c.addClass("class");
		c.addUtf8("name");
		c.addUtf8("descriptor");
		c.addNameAndType("name", "descriptor");
		int index = c.addField("class", "name", "descriptor");

		assertEquals(6, index);
		
		byte[] serialized = c.serialize().toByteArray();
		DataBuffer data = new DataBuffer();
		data.add(serialized);
		assertEquals(7, data.nextShort());
		assertEquals(1, data.nextByte());
		assertEquals("class", data.nextString());
		assertEquals(7, data.nextByte());
		assertEquals(1, data.nextShort());
		assertEquals(1, data.nextByte());
		assertEquals("name", data.nextString());
		assertEquals(1, data.nextByte());
		assertEquals("descriptor", data.nextString());
		assertEquals(12, data.nextByte());
		assertEquals(3, data.nextShort());
		assertEquals(4, data.nextShort());
		assertEquals(9, data.nextByte());
		assertEquals(2, data.nextShort());
		assertEquals(5, data.nextShort());
		assertFalse(data.hasNext());
	}

	@Test
	public void addMethod() /// Relies on 'addUtf8' and 'addNameAndType' tests
	{
		ConstantPoolTestClass c = new ConstantPoolTestClass();
		c.addUtf8("class");
		c.addClass("class");
		c.addUtf8("name");
		c.addUtf8("descriptor");
		c.addNameAndType("name", "descriptor");
		int index = c.addMethod("class", "name", "descriptor");
		
		assertEquals(6, index);
		
		DataBuffer data = c.serialize();
		assertEquals(7, data.nextShort());
		assertEquals(1, data.nextByte());
		assertEquals("class", data.nextString());
		assertEquals(7, data.nextByte());
		assertEquals(1, data.nextShort());
		assertEquals(1, data.nextByte());
		assertEquals("name", data.nextString());
		assertEquals(1, data.nextByte());
		assertEquals("descriptor", data.nextString());
		assertEquals(12, data.nextByte());
		assertEquals(3, data.nextShort());
		assertEquals(4, data.nextShort());
		assertEquals(10, data.nextByte());
		assertEquals(2, data.nextShort());
		assertEquals(5, data.nextShort());
		assertFalse(data.hasNext());
	}
	
	@Test
	public void addString()
	{
		ConstantPoolTestClass c = new ConstantPoolTestClass();
		c.addUtf8("first");
		int index = c.addString("first");

		assertEquals(2, index);
		
		DataBuffer data = c.serialize();
		assertEquals(3, data.nextShort());
		assertEquals(1, data.nextByte());
		assertEquals("first", data.nextString());
		assertEquals(8, data.nextByte());
		assertEquals(1, data.nextShort());
		assertFalse(data.hasNext());
	}

	@Test
	public void addInteger()
	{
		ConstantPoolTestClass c = new ConstantPoolTestClass();
		int index = c.addInteger(1234567);

		assertEquals(1, index);

		DataBuffer data = c.serialize();
		assertEquals(2, data.nextShort());
		assertEquals(3, data.nextByte());
		assertEquals(1234567, data.nextInt());
		assertFalse(data.hasNext());
	}
	
	@Test
	public void addFloat()
	{
		ConstantPoolTestClass c = new ConstantPoolTestClass();
		int index = c.addFloat(3.5f);

		assertEquals(1, index);

		DataBuffer data = c.serialize();
		assertEquals(2, data.nextShort());
		assertEquals(4, data.nextByte());
		assertEquals(0x40, data.nextByte());
		assertEquals(0x60, data.nextByte());
		assertEquals(0x00, data.nextByte());
		assertEquals(0x00, data.nextByte());
		assertFalse(data.hasNext());
	}

	@Test
	public void addLong()
	{
		ConstantPoolTestClass c = new ConstantPoolTestClass();
		int index = c.addLong(1311768467284833366L);

		assertEquals(1, index);

		DataBuffer data = c.serialize();
		assertEquals(2, data.nextShort());
		assertEquals(5, data.nextByte());
		assertEquals(0x12, data.nextByte() & 0xFF);
		assertEquals(0x34, data.nextByte() & 0xFF);
		assertEquals(0x56, data.nextByte() & 0xFF);
		assertEquals(0x78, data.nextByte() & 0xFF);
		assertEquals(0x90, data.nextByte() & 0xFF);
		assertEquals(0x12, data.nextByte() & 0xFF);
		assertEquals(0x34, data.nextByte() & 0xFF);
		assertEquals(0x56, data.nextByte() & 0xFF);
		assertFalse(data.hasNext());
	}

	@Test
	public void addDouble()
	{
		ConstantPoolTestClass c = new ConstantPoolTestClass();
		int index = c.addDouble(5.25);

		assertEquals(1, index);

		DataBuffer data = c.serialize();
		assertEquals(2, data.nextShort());
		assertEquals(6, data.nextByte());
		assertEquals(0x40, data.nextByte() & 0xFF);
		assertEquals(0x15, data.nextByte() & 0xFF);
		assertEquals(0x00, data.nextByte() & 0xFF);
		assertEquals(0x00, data.nextByte() & 0xFF);
		assertEquals(0x00, data.nextByte() & 0xFF);
		assertEquals(0x00, data.nextByte() & 0xFF);
		assertEquals(0x00, data.nextByte() & 0xFF);
		assertEquals(0x00, data.nextByte() & 0xFF);
		assertFalse(data.hasNext());
	}
	
	@Test
	public void nextConstantPool()
	{
		DataBuffer buffer = new DataBuffer();
		buffer.add("hi");
		buffer.add(123);
	
		ConstantPool pool = new ConstantPool();
		pool.addUtf8("primus");
		pool.addClass("c1");
		pool.addNameAndType("n1", "t1");
		pool.addField("c2", "n2", "t2");
		pool.addMethod("c3", "m1", "t3");
		pool.addString("hello");
		pool.addInteger(-1234567890);
		pool.addFloat(7.5f);
		pool.addLong(151);
		pool.addDouble(18.25);
		buffer.add(pool);

		buffer.resetIteration();
		buffer.nextString();
		buffer.nextInt();
		ConstantPool pool2 = ConstantPool.nextConstantPool(buffer);
		assertArrayEquals(
				pool.serialize().toByteArray(),
				pool2.serialize().toByteArray());
	}
}
