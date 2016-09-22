package com.jonheard.compilers.javaClassHeirarchy;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jonheard.compilers.javaClassHeirarchy.Item.Item;
import com.jonheard.compilers.javaClassHeirarchy.Item.Item_Class;
import com.jonheard.compilers.javaClassHeirarchy.Item.Item_Err_NotFound;
import com.jonheard.compilers.javaClassHeirarchy.Item.Item_Member;
import com.jonheard.compilers.javaClassHeirarchy.Item.Item_Package;

public class JavaClassHeirarchyTest
{
	@Test
	public void main()
	{
		JavaClassHeirarchy heirarchy = new JavaClassHeirarchy();

		assertFalse(heirarchy.addSource_Folder("badData"));
		assertTrue(heirarchy.addSource_Folder("bin"));

		String rtJarPath = System.getProperty("java.home") + "/lib/rt.jar";
		assertFalse(heirarchy.addSource_Jar("badData"));
		assertTrue(heirarchy.addSource_Jar(rtJarPath));
		
		Item i01 = heirarchy.getHeirarchy("badData");
		assertTrue(i01 instanceof Item_Err_NotFound);

		Item i02 = heirarchy.getHeirarchy("com");
		Item i03 = heirarchy.getHeirarchy("com.jonheard");
		Item i04 = heirarchy.getHeirarchy("com.jonheard.compilers");
		Item i05 = heirarchy.getHeirarchy(
				"com.jonheard.compilers.javaClassHeirarchy");
		Item i06 = heirarchy.getHeirarchy(
				"com.jonheard.compilers.javaClassHeirarchy.Source");
		Item i07 = heirarchy.getHeirarchy(
				"com.jonheard.compilers.javaClassHeirarchy.Source.isValid");
		Item i08 = heirarchy.getHeirarchy(
				"com.jonheard.compilers.javaClassHeirarchy.Source.valid");

		Item i09 = heirarchy.getHeirarchy("java");
		Item i10 = heirarchy.getHeirarchy("java.lang");
		Item i11 = heirarchy.getHeirarchy("java.lang.System");
		Item i12 = heirarchy.getHeirarchy("java.lang.System.out");
		Item i13 = heirarchy.getHeirarchy("java.lang.System.currentTimeMillis");
	

		checkItem(i02, "com", Item_Package.class, i02.getParent());
		checkItem(i03, "jonheard", Item_Package.class, i02);
		checkItem(i04, "compilers", Item_Package.class, i03);
		checkItem(i05, "javaClassHeirarchy", Item_Package.class, i04);
		checkItem(i06, "Source", Item_Class.class, i05);
		checkItem(i07, "isValid", Item_Member.class, i06);
		checkItem(i08, "valid", Item_Member.class, i06);
		
		assertFalse(((Item_Member)i07).isField());
		assertTrue(((Item_Member)i07).isMethod());
		assertTrue(((Item_Member)i08).isField());
		assertFalse(((Item_Member)i08).isMethod());
		
		checkItem(i09, "java", Item_Package.class, i09.getParent());
		checkItem(i10, "lang", Item_Package.class, i09);
		checkItem(i11, "System", Item_Class.class, i10);
		checkItem(i12, "out", Item_Member.class, i11);
		checkItem(i13, "currentTimeMillis", Item_Member.class, i11);
		
		assertTrue(((Item_Member)i12).isField());
		assertFalse(((Item_Member)i12).isMethod());
		assertFalse(((Item_Member)i13).isField());
		assertTrue(((Item_Member)i13).isMethod());
		
		Item i14 = heirarchy.getHeirarchy("com.jonheard.compilers.badData");
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
