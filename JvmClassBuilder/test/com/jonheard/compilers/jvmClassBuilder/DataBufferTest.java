package com.jonheard.compilers.jvmClassBuilder;

import static org.junit.Assert.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.Test;
import com.jonheard.compilers.jvmClassBuilder.DataBuffer;

public class DataBufferTest
{
	@Test
	public void equals()
	{
		DataBuffer a = new DataBuffer();
		DataBuffer b = new DataBuffer();
		assertTrue(a.equals(b));
		assertTrue(b.equals(a));
		b.add(1);
		assertFalse(a.equals(b));
		assertFalse(b.equals(a));
		a.add(1);
		assertTrue(a.equals(b));
		assertTrue(b.equals(a));
		a.add(5);
		assertFalse(a.equals(b));
		assertFalse(b.equals(a));
		b.add(6);
		assertFalse(a.equals(b));
		assertFalse(b.equals(a));
	}

	@Test
	public void sizeAndClear()
	{
		DataBuffer a = new DataBuffer();
		assertEquals(0, a.size());
		a.add((byte)1);
		assertEquals(1, a.size());
		a.add((short)1);
		assertEquals(3, a.size());
		a.add(1);
		assertEquals(7, a.size());
		a.clear();
		assertEquals(0, a.size());
	}

	@Test
	public void basicAddMethods_toByteArray()
	{
		DataBuffer a = new DataBuffer();
		int i1 = a.add((byte)1);
		int i2 = a.add((short)2);
		int i3 = a.add(3);
		int i4 = a.add("hi");
		int i5 = a.add("yo", false);

		/// check given indices
		assertEquals(0, i1);
		assertEquals(1, i2);
		assertEquals(3, i3);
		assertEquals(7, i4);
		assertEquals(11, i5);

		/// toByteArray
		byte[] expected = {1, 0, 2, 0, 0, 0, 3, 0, 2, 'h', 'i', 'y', 'o'};
		byte[] actual = a.toByteArray();
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void addByteArray()
	{
		DataBuffer a = new DataBuffer();
		byte[] toAdd =
		{
			0, 50, 13, -25	
		};
		int index = a.add(toAdd);
		assertEquals(0, index);
		assertEquals(50, a.getShort(0));
		assertEquals(13, a.getByte(2));
		assertEquals(-25, a.getByte(3));
	}
	
	@Test
	public void addDataBuffer()
	{
		DataBuffer toAdd = new DataBuffer();
		DataBuffer toHold = new DataBuffer();
		toAdd.add(5);
		toAdd.add("test");
		toHold.add((short)45);
		toHold.add((byte)100);
		
		int i5 = toHold.add(toAdd);
		int i6 = toHold.add((byte)83);

		assertEquals(3, i5);
		assertEquals(13, i6);
		
		byte[] expected =
				{0, 45, 100, 0, 0, 0, 5, 0, 4, 't', 'e', 's', 't', 83};
		assertArrayEquals(expected, toHold.toByteArray());
	}
	
	@Test
	public void addSerializable()
	{
		TestSerializable toAdd = new TestSerializable(67, "tester");
		DataBuffer toHold = new DataBuffer();
		toHold.add((short)45);
		toHold.add((byte)100);
		
		int i3 = toHold.add(toAdd);
		int i4 = toHold.add((byte)81);
		
		assertEquals(3, i3);
		assertEquals(12, i4);
		
		byte[] expected =
				{0, 45, 100, 67, 0, 6, 't', 'e', 's', 't', 'e', 'r', 81};
		assertArrayEquals(expected, toHold.toByteArray());		
	}
	class TestSerializable implements DataBuffer.Serializable
	{
		byte a;
		String b;
		public TestSerializable(int a, String b)
		{
			this.a = (byte)a;
			this.b = b;
		}
		@Override
		public DataBuffer serialize()
		{
			DataBuffer result = new DataBuffer();
			result.add(a);
			result.add(b);
			return result;
		}
	}
	
	@Test
	public void getMethods()
	{
		DataBuffer a = new DataBuffer();
		int i1 = a.add((byte)123);
		int i2 = a.add((short)12345);
		int i3 = a.add(1234567890);
		int i4 = a.add("hi");
		int i5 = a.add("yo", false);
		int i6 = a.add((byte)-123);
		int i7 = a.add((short)-12345);
		int i8 = a.add(-1234567890);
	
		assertEquals(123, a.getByte(i1));
		assertEquals(12345, a.getShort(i2));
		assertEquals(1234567890, a.getInt(i3));
		assertEquals("hi", a.getString(i4));
		assertEquals("yo", a.getString(i5, 2));
		assertEquals(-123, a.getByte(i6));
		assertEquals(-12345, a.getShort(i7));
		assertEquals(-1234567890, a.getInt(i8));
	}
	
	@Test
	public void setMethods()
	{
		DataBuffer a = new DataBuffer();
		a.add((byte)1);
		a.add((short)2);
		a.add(3);
		a.add("hi");
		a.add("yo", false);
		
		a.setByte(0, (byte)5);
		a.setShort(1, (short)520);
		a.setInt(3, 525);
		a.setString(7, "yup");
		a.setStringWithoutSize(12, "a");
		
		byte[] expected = {5, 2, 8, 0, 0, 2, 13, 0, 3, 'y', 'u', 'p', 'a'};
		byte[] actual = a.toByteArray();
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void Iteration()
	{
		DataBuffer buffer = new DataBuffer();
		assertFalse(buffer.hasNext());
		buffer.add((byte)-123);
		assertTrue(buffer.hasNext());
		buffer.add((short)-12345);
		buffer.add(-1234567890);
		buffer.add("hello");
		assertTrue(buffer.hasNext());

		assertEquals(-123, buffer.nextByte());
		assertEquals(-12345, buffer.nextShort());
		assertEquals(-1234567890, buffer.nextInt());
		assertEquals("hello", buffer.nextString());
		assertFalse(buffer.hasNext());

		buffer.resetIteration();
		assertTrue(buffer.hasNext());
		assertEquals(-123, buffer.nextByte());
		buffer.nextString(8);
		assertEquals("hello", buffer.nextString(5));
		assertFalse(buffer.hasNext());
		
		buffer.resetIteration(3);
		buffer.add(-1234567890);
	}

	private DataBuffer db;
	///	Predicate to determine if calling the given method with the given
	/// parameters will throw an "InvalidDataIndexException"
	public boolean throwsException(Method method, Object param1,Object param2)
			throws Exception
	{
		try
		{
			if(param1 == null)
			{
				method.invoke(db);
			}
			else if(param2 == null)
			{
				method.invoke(db, param1);
			}
			else
			{
				method.invoke(db, param1, param2);
			}
		}
		catch(InvocationTargetException e)
		{
			if(e.getCause() instanceof DataBuffer.InvalidDataIndexException)
			{
				return true;
			}
			else
			{
				throw new Exception();
			}
		}
		return false;
	}	
	@Test
	public void InvalidDataIndexException()
	{
		db = new DataBuffer();
		db.add("345");

		Method method = null;
		try
		{
			method = db.getClass().getMethod("getByte", int.class);
			assertTrue (throwsException(method, -1, null));
			assertFalse(throwsException(method, 0, null));
			assertFalse(throwsException(method, 1, null));
			assertFalse(throwsException(method, 2, null));
			assertFalse(throwsException(method, 3, null));
			assertFalse(throwsException(method, 4, null));
			assertTrue (throwsException(method, 5, null));
			assertTrue (throwsException(method, 6, null));
			method = db.getClass().getMethod("getShort", int.class);
			assertTrue (throwsException(method, -1, null));
			assertFalse(throwsException(method, 0, null));
			assertFalse(throwsException(method, 1, null));
			assertFalse(throwsException(method, 2, null));
			assertFalse(throwsException(method, 3, null));
			assertTrue (throwsException(method, 4, null));
			assertTrue (throwsException(method, 5, null));
			assertTrue (throwsException(method, 6, null));
			method = db.getClass().getMethod("getInt", int.class);
			assertTrue (throwsException(method, -1, null));
			assertFalse(throwsException(method, 0, null));
			assertFalse(throwsException(method, 1, null));
			assertTrue (throwsException(method, 2, null));
			assertTrue (throwsException(method, 3, null));
			assertTrue (throwsException(method, 4, null));
			assertTrue (throwsException(method, 5, null));
			assertTrue (throwsException(method, 6, null));
			method = db.getClass().getMethod("getString", int.class, int.class);
			assertTrue (throwsException(method, -1, 3));
			assertFalse(throwsException(method, 0, 3));
			assertFalse(throwsException(method, 1, 3));
			assertFalse(throwsException(method, 2, 3));
			assertTrue(throwsException(method, 3, 3));
			assertTrue(throwsException(method, 4, 3));
			assertTrue (throwsException(method, 5, 3));
			assertTrue (throwsException(method, 6, 3));
			method = db.getClass().getMethod("setByte", int.class, byte.class);
			assertTrue (throwsException(method, -1, (byte)0));
			assertFalse(throwsException(method, 0, (byte)0));
			assertFalse(throwsException(method, 1, (byte)0));
			assertFalse(throwsException(method, 2, (byte)0));
			assertFalse(throwsException(method, 3, (byte)0));
			assertFalse(throwsException(method, 4, (byte)0));
			assertTrue (throwsException(method, 5, (byte)0));
			assertTrue (throwsException(method, 6, (byte)0));
			method = db.getClass().getMethod("setShort", int.class,short.class);
			assertTrue (throwsException(method, -1, (short)0));
			assertFalse(throwsException(method, 0, (short)0));
			assertFalse(throwsException(method, 1, (short)0));
			assertFalse(throwsException(method, 2, (short)0));
			assertFalse(throwsException(method, 3, (short)0));
			assertTrue (throwsException(method, 4, (short)0));
			assertTrue (throwsException(method, 5, (short)0));
			assertTrue (throwsException(method, 6, (short)0));
			method = db.getClass().getMethod("setInt", int.class, int.class);
			assertTrue (throwsException(method, -1, 0));
			assertFalse(throwsException(method, 0, 0));
			assertFalse(throwsException(method, 1, 0));
			assertTrue (throwsException(method, 2, 0));
			assertTrue (throwsException(method, 3, 0));
			assertTrue (throwsException(method, 4, 0));
			assertTrue (throwsException(method, 5, 0));
			assertTrue (throwsException(method, 6, 0));
			method = db.getClass().getMethod(
					"setString", int.class, String.class);
			assertTrue (throwsException(method, -1, "hi"));
			assertFalse(throwsException(method, 0, "hi"));
			assertFalse(throwsException(method, 1, "hi"));
			assertTrue (throwsException(method, 2, "hi"));
			assertTrue (throwsException(method, 3, "hi"));
			assertTrue (throwsException(method, 4, "hi"));
			assertTrue (throwsException(method, 5, "hi"));
			assertTrue (throwsException(method, 6, "hi"));
			method = db.getClass().getMethod(
					"setStringWithoutSize", int.class, String.class);
			assertTrue (throwsException(method, -1, "hi"));
			assertFalse(throwsException(method, 0, "hi"));
			assertFalse(throwsException(method, 1, "hi"));
			assertFalse(throwsException(method, 2, "hi"));
			assertFalse(throwsException(method, 3, "hi"));
			assertTrue (throwsException(method, 4, "hi"));
			assertTrue (throwsException(method, 5, "hi"));
			assertTrue (throwsException(method, 6, "hi"));
			method = db.getClass().getMethod("nextByte");
			db.resetIteration();
			assertFalse(throwsException(method, null, null));
			assertFalse(throwsException(method, null, null));
			assertFalse(throwsException(method, null, null));
			assertFalse(throwsException(method, null, null));
			assertFalse(throwsException(method, null, null));
			assertTrue (throwsException(method, null, null));
			method = db.getClass().getMethod("nextShort");
			db.resetIteration();
			assertFalse(throwsException(method, null, null));
			assertFalse(throwsException(method, null, null));
			assertTrue (throwsException(method, null, null));
			method = db.getClass().getMethod("nextInt");
			db.resetIteration();
			assertFalse(throwsException(method, null, null));
			assertTrue (throwsException(method, null, null));
			method = db.getClass().getMethod("nextString", int.class);
			db.resetIteration();
			assertFalse(throwsException(method, 2, null));
			assertFalse(throwsException(method, 2, null));
			assertTrue (throwsException(method, 2, null));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}
}
