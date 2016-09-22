package com.jonheard.compilers.javaClassHeirarchy;

import static org.junit.Assert.*;
import org.junit.Test;

import com.jonheard.compilers.javaClassHeirarchy.SourceJar;
import com.jonheard.compilers.javaClassHeirarchy.Item.Item;
import com.jonheard.compilers.javaClassHeirarchy.Item.Item_Class;
import com.jonheard.compilers.javaClassHeirarchy.Item.Item_Err_NotFound;
import com.jonheard.compilers.javaClassHeirarchy.Item.Item_Member;
import com.jonheard.compilers.javaClassHeirarchy.Item.Item_Package;

public class SourceJarTest
{

	@Test
	public void main()
	{
		String rtJarPath = System.getProperty("java.home") + "/lib/rt.jar";

		SourceJar source = new SourceJar("badData");
		assertFalse(source.isValid());

		source = new SourceJar(rtJarPath);
		assertTrue(source.isValid());

		Item i1 = source.getHeirarchy("badData");
		Item i2 = source.getHeirarchy("java");
		Item i3 = source.getHeirarchy("java.lang");
		Item i4 = source.getHeirarchy("java.lang.System");
		Item i5 = source.getHeirarchy("java.lang.System.out");
		Item i6 = source.getHeirarchy("java.lang.System.currentTimeMillis");

		assertTrue(i1 instanceof Item_Err_NotFound);

		checkItem(i2, "java", Item_Package.class, i2.getParent());
		checkItem(i3, "lang", Item_Package.class, i2);
		checkItem(i4, "System", Item_Class.class, i3);
		checkItem(i5, "out", Item_Member.class, i4);
		checkItem(i6, "currentTimeMillis", Item_Member.class, i4);
		
		assertTrue(((Item_Member)i5).isField());
		assertFalse(((Item_Member)i5).isMethod());
		assertFalse(((Item_Member)i6).isField());
		assertTrue(((Item_Member)i6).isMethod());
		
		Item i7 = source.getHeirarchy("java.lang.badData");
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
