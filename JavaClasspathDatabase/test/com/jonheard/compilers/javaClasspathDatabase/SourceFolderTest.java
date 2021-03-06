package com.jonheard.compilers.javaClasspathDatabase;

import static org.junit.Assert.*;
import org.junit.Test;

import com.jonheard.compilers.javaClasspathDatabase.SourceFolder;
import com.jonheard.compilers.javaClasspathDatabase.Item.*;

public class SourceFolderTest
{

	@Test
	public void main()
	{
		SourceFolder source = new SourceFolder(null, "badData");
		assertFalse(source.isValid());

		source = new SourceFolder(null, "bin");
		assertTrue(source.isValid());

		Item i1 = source.getValue("badData");
		Item i2 = source.getValue("com");
		Item i3 = source.getValue("com.jonheard");
		Item i4 = source.getValue("com.jonheard.compilers");
		Item i5 = source.getValue(
				"com.jonheard.compilers.javaClasspathDatabase");
		Item i6 = source.getValue(
				"com.jonheard.compilers.javaClasspathDatabase.Source");
		Item i7 = source.getValue(
				"com.jonheard.compilers.javaClasspathDatabase.Source.isValid");
		Item i8 = source.getValue(
				"com.jonheard.compilers.javaClasspathDatabase.Source.valid");

		assertTrue(i1 instanceof Item_Err_NotFound);

		checkItem(i2, "com", Item_Package.class, i2.getParent());
		checkItem(i3, "jonheard", Item_Package.class, i2);
		checkItem(i4, "compilers", Item_Package.class, i3);
		checkItem(i5, "javaClasspathDatabase", Item_Package.class, i4);
		checkItem(i6, "Source", Item_Class.class, i5);
		checkItem(i7, "isValid", Item_Member.class, i6);
		checkItem(i8, "valid", Item_Member.class, i6);
		
		Item i9 = source.getValue("com.jonheard.compilers.badData");
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
