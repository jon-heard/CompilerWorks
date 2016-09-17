package com.jonheard.compilers.assembler_jvm.backEnd;

import static org.junit.Assert.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.Test;

import com.jonheard.compilers.assembler_jvm.backEnd.DataBuffer;

public class DataBufferTest
{
	private DataBuffer db;

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
		db = new DataBuffer();
		assertEquals(0, db.size());
		db.add((byte)1);
		assertEquals(1, db.size());
		db.add((short)1);
		assertEquals(3, db.size());
		db.add(1);
		assertEquals(7, db.size());
		db.clear();
		assertEquals(0, db.size());
	}




	@Test
	public void basicAddMethods_toByteArray()
	{
		db = new DataBuffer();
		int i1 = db.add((byte)1);
		int i2 = db.add((short)2);
		int i3 = db.add(3);
		int i4 = db.add("hi");
		int i5 = db.add("yo", false);

		/// check given indices
		assertEquals(0, i1);
		assertEquals(1, i2);
		assertEquals(3, i3);
		assertEquals(7, i4);
		assertEquals(11, i5);

		/// toByteArray
		byte[] expected = {1, 0, 2, 0, 0, 0, 3, 0, 2, 'h', 'i', 'y', 'o'};
		byte[] actual = db.toByteArray();
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void addByteArray()
	{
		db = new DataBuffer();
		byte[] toAdd =
		{
			0, 50, 13, -25	
		};
		int index = db.add(toAdd);
		assertEquals(0, index);
		assertEquals(50, db.getShort(0));
		assertEquals(13, db.getByte(2));
		assertEquals(-25, db.getByte(3));
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
		db = new DataBuffer();
		TestSerializable toAdd = new TestSerializable(67, "tester");
		db.add((short)45);
		db.add((byte)100);
		
		int i3 = db.add(toAdd);
		int i4 = db.add((byte)81);
		
		assertEquals(3, i3);
		assertEquals(12, i4);
		
		byte[] expected =
				{0, 45, 100, 67, 0, 6, 't', 'e', 's', 't', 'e', 'r', 81};
		assertArrayEquals(expected, db.toByteArray());		
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
		db = new DataBuffer();
		int i1 = db.add((byte)123);
		int i2 = db.add((short)12345);
		int i3 = db.add(1234567890);
		int i4 = db.add("hi");
		int i5 = db.add("yo", false);
		int i6 = db.add((byte)-123);
		int i7 = db.add((short)-12345);
		int i8 = db.add(-1234567890);
	
		assertEquals(123, db.getByte(i1));
		assertEquals(12345, db.getShort(i2));
		assertEquals(1234567890, db.getInt(i3));
		assertEquals("hi", db.getString(i4));
		assertEquals("yo", db.getString(i5, 2));
		assertEquals(-123, db.getByte(i6));
		assertEquals(-12345, db.getShort(i7));
		assertEquals(-1234567890, db.getInt(i8));
	}
	











	@Test
	public void setMethods()
	{
		db = new DataBuffer();
		db.add((byte)1);
		db.add((short)2);
		db.add(3);
		db.add("hi");
		db.add("yo", false);
		
		db.setByte(0, (byte)5);
		db.setShort(1, (short)520);
		db.setInt(3, 525);
		db.setString(7, "yup");
		db.setStringWithoutSize(12, "a");
		
		byte[] expected = {5, 2, 8, 0, 0, 2, 13, 0, 3, 'y', 'u', 'p', 'a'};
		byte[] actual = db.toByteArray();
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void Iteration()
	{
		db = new DataBuffer();
		assertFalse(db.hasNext());
		db.add((byte)-123);
		assertTrue(db.hasNext());
		db.add((short)-12345);
		db.add(-1234567890);
		db.add("hello");
		assertTrue(db.hasNext());

		assertEquals(-123, db.nextByte());
		assertEquals(-12345, db.nextShort());
		assertEquals(-1234567890, db.nextInt());
		assertEquals("hello", db.nextString());
		assertFalse(db.hasNext());

		db.resetIterator();
		assertTrue(db.hasNext());
		assertEquals(-123, db.nextByte());
		db.nextString(8);
		assertEquals("hello", db.nextString(5));
		assertFalse(db.hasNext());
		
		db.setIterator(3);
		db.add(-1234567890);
	}




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
			method = db.getClass().getMethod(
				"getString", int.class, int.class);
			assertTrue (throwsException(method, -1, 3));
			assertFalse(throwsException(method, 0, 3));
			assertFalse(throwsException(method, 1, 3));
			assertFalse(throwsException(method, 2, 3));
			assertTrue(throwsException(method, 3, 3));
			assertTrue(throwsException(method, 4, 3));
			assertTrue (throwsException(method, 5, 3));
			assertTrue (throwsException(method, 6, 3));
			method = db.getClass().getMethod(
				"setByte", int.class, byte.class);
			assertTrue (throwsException(method, -1, (byte)0));
			assertFalse(throwsException(method, 0, (byte)0));
			assertFalse(throwsException(method, 1, (byte)0));
			assertFalse(throwsException(method, 2, (byte)0));
			assertFalse(throwsException(method, 3, (byte)0));
			assertFalse(throwsException(method, 4, (byte)0));
			assertTrue (throwsException(method, 5, (byte)0));
			assertTrue (throwsException(method, 6, (byte)0));
			method = db.getClass().getMethod(
				"setShort", int.class,short.class);
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
			db.resetIterator();
			assertFalse(throwsException(method, null, null));
			assertFalse(throwsException(method, null, null));
			assertFalse(throwsException(method, null, null));
			assertFalse(throwsException(method, null, null));
			assertFalse(throwsException(method, null, null));
			assertTrue (throwsException(method, null, null));
			method = db.getClass().getMethod("nextShort");
			db.resetIterator();
			assertFalse(throwsException(method, null, null));
			assertFalse(throwsException(method, null, null));
			assertTrue (throwsException(method, null, null));
			method = db.getClass().getMethod("nextInt");
			db.resetIterator();
			assertFalse(throwsException(method, null, null));
			assertTrue (throwsException(method, null, null));
			method = db.getClass().getMethod("nextString", int.class);
			db.resetIterator();
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