package com.jonheard.compilers.javaClasspathDatabase;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jonheard.compilers.javaClasspathDatabase.JavaClasspathDatabase;
import com.jonheard.compilers.javaClasspathDatabase.Item.*;

public class JavaClasspathDatabaseTest
{
	@Test
	public void main()
	{
		JavaClasspathDatabase database = new JavaClasspathDatabase();
		assertFalse(database.addSource_Folder("badData"));
		assertTrue(database.addSource_Folder("bin"));

		String rtJarPath = System.getProperty("java.home") + "/lib/rt.jar";
		assertFalse(database.addSource_Jar("badData"));
		assertTrue(database.addSource_Jar(rtJarPath));
		
		Item i01 = database.getValue("badData");
		assertTrue(i01 instanceof Item_Err_NotFound);

		Item i02 = database.getValue("com");
		Item i03 = database.getValue("com.jonheard");
		Item i04 = database.getValue("com.jonheard.compilers");
		Item i05 = database.getValue(
				"com.jonheard.compilers.javaClasspathDatabase");
		Item i06 = database.getValue(
				"com.jonheard.compilers.javaClasspathDatabase.Source");
		Item i07 = database.getValue(
				"com.jonheard.compilers.javaClasspathDatabase.Source.isValid");
		Item i08 = database.getValue(
				"com.jonheard.compilers.javaClasspathDatabase.Source.valid");

		Item i09 = database.getValue("java");
		Item i10 = database.getValue("java.lang");
		Item i11 = database.getValue("java.lang.System");
		Item i12 = database.getValue("java.lang.System.out");
		Item i13 = database.getValue("java.lang.System.currentTimeMillis");
	

		checkItem(i02, "com", Item_Package.class, i02.getParent());
		checkItem(i03, "jonheard", Item_Package.class, i02);
		checkItem(i04, "compilers", Item_Package.class, i03);
		checkItem(i05, "javaClasspathDatabase", Item_Package.class, i04);
		checkItem(i06, "Source", Item_Class.class, i05);
		checkItem(i07, "isValid", Item_Member.class, i06);
		checkItem(i08, "valid", Item_Member.class, i06);
		
		assertFalse(((Item_Member)i07).isField());
		assertTrue(((Item_Member)i07).isMethod());
		assertTrue(((Item_Member)i08).isField());
		assertFalse(((Item_Member)i08).isMethod());
		
		assertFalse(((Item_Member)i07).isStatic());
		assertFalse(((Item_Member)i08).isStatic());
		
		checkItem(i09, "java", Item_Package.class, i09.getParent());
		checkItem(i10, "lang", Item_Package.class, i09);
		checkItem(i11, "System", Item_Class.class, i10);
		checkItem(i12, "out", Item_Member.class, i11);
		checkItem(i13, "currentTimeMillis", Item_Member.class, i11);

		Item i14 = database.getValue("com.jonheard.compilers.badData");
		assertEquals("com.jonheard.compilers", i14.getName());
	}

	@SuppressWarnings("rawtypes")
	public void checkItem(Item item, String name, Class type, Item parent)
	{
		assertTrue(type.isInstance(item));
		assertEquals(name, item.getName());
		assertEquals(parent, item.getParent());
	}
}
