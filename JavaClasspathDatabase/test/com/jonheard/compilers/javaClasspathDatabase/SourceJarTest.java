package com.jonheard.compilers.javaClasspathDatabase;

import static org.junit.Assert.*;
import org.junit.Test;

import com.jonheard.compilers.javaClasspathDatabase.SourceJar;
import com.jonheard.compilers.javaClasspathDatabase.Item.Item;
import com.jonheard.compilers.javaClasspathDatabase.Item.Item_Class;
import com.jonheard.compilers.javaClasspathDatabase.Item.Item_Err_NotFound;
import com.jonheard.compilers.javaClasspathDatabase.Item.Item_Member;
import com.jonheard.compilers.javaClasspathDatabase.Item.Item_Package;

public class SourceJarTest
{

	@Test
	public void main()
	{
		String rtJarPath = System.getProperty("java.home") + "/lib/rt.jar";

		SourceJar source = new SourceJar(null, "badData");
		assertFalse(source.isValid());

		source = new SourceJar(null, rtJarPath);
		assertTrue(source.isValid());

		Item i1 = source.getValue("badData");
		Item i2 = source.getValue("java");
		Item i3 = source.getValue("java.lang");
		Item i4 = source.getValue("java.lang.System");
		Item i5 = source.getValue("java.lang.System.out");
		Item i6 = source.getValue("java.lang.System.currentTimeMillis");

		assertTrue(i1 instanceof Item_Err_NotFound);

		checkItem(i2, "java", Item_Package.class, i2.getParent());
		checkItem(i3, "lang", Item_Package.class, i2);
		checkItem(i4, "System", Item_Class.class, i3);
		checkItem(i5, "out", Item_Member.class, i4);
		checkItem(i6, "currentTimeMillis", Item_Member.class, i4);

		Item i7 = source.getValue("java.lang.badData");
		assertEquals("java.lang", i7.getName());
	}

	@SuppressWarnings("rawtypes")
	public void checkItem(Item item, String name, Class type, Item parent)
	{
		assertEquals(name, item.getName());
		assertTrue(type.isInstance(item));
		assertEquals(parent, item.getParent());
	}
}
