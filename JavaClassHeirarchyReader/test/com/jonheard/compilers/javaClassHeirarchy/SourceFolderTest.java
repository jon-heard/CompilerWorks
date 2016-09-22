package com.jonheard.compilers.javaClassHeirarchy;

import static org.junit.Assert.*;
import org.junit.Test;

import com.jonheard.compilers.javaClassHeirarchy.SourceFolder;
import com.jonheard.compilers.javaClassHeirarchy.Item.*;

public class SourceFolderTest
{

	@Test
	public void main()
	{
		SourceFolder source = new SourceFolder("badData");
		assertFalse(source.isValid());

		source = new SourceFolder("bin");
		assertTrue(source.isValid());

		Item i1 = source.getHeirarchy("badData");
		Item i2 = source.getHeirarchy("com");
		Item i3 = source.getHeirarchy("com.jonheard");
		Item i4 = source.getHeirarchy("com.jonheard.compilers");
		Item i5 = source.getHeirarchy(
				"com.jonheard.compilers.javaClassHeirarchy");
		Item i6 = source.getHeirarchy(
				"com.jonheard.compilers.javaClassHeirarchy.Source");
		Item i7 = source.getHeirarchy(
				"com.jonheard.compilers.javaClassHeirarchy.Source.isValid");
		Item i8 = source.getHeirarchy(
				"com.jonheard.compilers.javaClassHeirarchy.Source.valid");

		assertTrue(i1 instanceof Item_Err_NotFound);

		checkItem(i2, "com", Item_Package.class, i2.getParent());
		checkItem(i3, "jonheard", Item_Package.class, i2);
		checkItem(i4, "compilers", Item_Package.class, i3);
		checkItem(i5, "javaClassHeirarchy", Item_Package.class, i4);
		checkItem(i6, "Source", Item_Class.class, i5);
		checkItem(i7, "isValid", Item_Member.class, i6);
		checkItem(i8, "valid", Item_Member.class, i6);
		
		assertFalse(((Item_Member)i7).isField());
		assertTrue(((Item_Member)i7).isMethod());
		assertTrue(((Item_Member)i8).isField());
		assertFalse(((Item_Member)i8).isMethod());
		
		Item i9 = source.getHeirarchy("com.jonheard.compilers.badData");
		assertEquals("com.jonheard.compilers", i9.getName());
	}

	@SuppressWarnings("rawtypes")
	public void checkItem(Item item, String name, Class type, Item parent)
	{
		assertTrue(type.isInstance(item));
		assertEquals(name, item.getName());
		assertEquals(parent, item.getParent());
	}
}
