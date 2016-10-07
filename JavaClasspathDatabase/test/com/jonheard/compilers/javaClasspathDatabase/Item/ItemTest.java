package com.jonheard.compilers.javaClasspathDatabase.Item;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.jonheard.compilers.javaClasspathDatabase.Source;
import com.jonheard.compilers.javaClasspathDatabase.Item.Item;
import com.jonheard.compilers.javaClasspathDatabase.Item.Item_Class;
import com.jonheard.compilers.javaClasspathDatabase.Item.Item_Member;
import com.jonheard.compilers.javaClasspathDatabase.Item.Item_Non;

public class ItemTest
{
	@Test
	public void Item_and_Item_Folder()
	{
		Item i1 = new Item_Non("one");
		Item i2 = new Item("two", i1);
		Item i3 = new Item("three", i2);
		Item i4 = new Item("four", i1);
		assertEquals(null, i1.getParent());
		assertEquals("two", i2.getName());
		assertEquals(i1, i2.getParent());
		assertFalse(i1.hasChild("badData"));
		assertEquals(null, i1.getChild("badData"));
		assertTrue(i1.hasChild("two"));
		assertEquals(i2, i1.getChild("two"));
		assertEquals("two.three", i3.getJavaAddress());
		assertEquals("two/three", i3.getFileAddress());
		assertEquals("Item(two,one)", i2.toString());
		Set<String> childList = i1.getChildList();
		assertEquals(2, childList.size());
		assertTrue(childList.contains("two"));
		assertTrue(childList.contains("four"));
		List<Item> items = i3.getForwardAddress();
		assertEquals(3, items.size());
		assertEquals(i1, items.get(0));
		assertEquals(i2, items.get(1));
		assertEquals(i3, items.get(2));
	}
	
	public void Item_Non()
	{
		Item i1 = new Item_Non("one");
		assertEquals("", i1.getJavaAddress());
		assertEquals("", i1.getFileAddress());
	}
	
	public void Item_Member()
	{
		Item i1 = new Item_Non("one");
		Item_Member i2 = new Item_Member("two", i1, null, "Z", true);
		Item_Member i3 = new Item_Member("three", i1, null, "(Z)V", false);
		Item_Member i4 = new Item_Member("three", i1, null, "([Z)[[V", false);
		Item_Member i5 = new Item_Member("three", i1, null, "[[[Z", false);
		assertEquals("Z", i2.getDescriptor());
		assertEquals("(Z)V", i3.getDescriptor());
		assertEquals("([Z)V", i4.getDescriptor());
		assertEquals("Z", i5.getDescriptor());
		assertTrue(i2.isField());
		assertFalse(i2.isMethod());
		assertFalse(i3.isField());
		assertTrue(i3.isMethod());
		assertTrue(i2.isStatic());
		assertFalse(i3.isStatic());
		assertEquals(2, i4.getArrayDimension());
		assertEquals(3, i5.getArrayDimension());
	}
	
	public void Item_Class()
	{
		Item i1 = new Item_Non("one");
		Item i2 = new Item("two", i1);
		Item i3 = new Item("three", i2);
		Item_Class i4 = new Item_Class("four", i3, new Source(null, "")
				{
					@Override
					public void loadItemData(Item toLoad)
					{
						new Item_Member("member1", toLoad, null, "Z", false);
					}
					@Override
					protected boolean loadData() { return true; }
				});
		assertEquals("two.three.four", i4.getJavaAddress());
		assertEquals("two/three/four.class", i4.getFileAddress());
		assertEquals(false, i4.hasChild("badData"));
		assertEquals(true, i4.hasChild("member1"));
		Item child1 = i4.getChild("member1");
		assertTrue(child1 instanceof Item_Member);
		Item_Member member1 = (Item_Member)child1;
		assertEquals("member1", member1.getName());
		assertEquals(i4, member1.getParent());
		assertEquals("Z", member1.getDescriptor());
	}
}
