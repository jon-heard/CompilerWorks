package com.jonheard.compilers.javaClassHeirarchy.Item;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jonheard.compilers.javaClassHeirarchy.Source;
import com.jonheard.compilers.javaClassHeirarchy.Item.Item;
import com.jonheard.compilers.javaClassHeirarchy.Item.Item_Class;
import com.jonheard.compilers.javaClassHeirarchy.Item.Item_Folder;
import com.jonheard.compilers.javaClassHeirarchy.Item.Item_Member;

public class ItemTest
{
	@Test
	public void Item_and_Item_Folder()
	{
		Item i1 = new Item_Folder("one");
		Item i2 = new Item("two", i1);
		Item i3 = new Item("three", i2);
		
		assertEquals(null, i1.getParent());
		
		assertEquals("two", i2.getName());
		assertEquals(i1,  i2.getParent());
		
		assertFalse(i1.hasChild("badData"));
		assertEquals(null, i1.getChild("badData"));
		assertTrue(i1.hasChild("two"));
		assertEquals(i2, i1.getChild("two"));
		
		assertEquals("two.three", i3.getJavaAddress());
		assertEquals("two/three", i3.getFileAddress());
		
		assertEquals("Item(two,one)", i2.toString());
	}
	
	public void Item_Member()
	{
		Item i1 = new Item_Folder("one");
		Item_Member i2 = new Item_Member("two", i1, "Z");
		Item_Member i3 = new Item_Member("three", i1, "(Z)V");
		
		assertEquals("Z", i2.getDescriptor());
		assertEquals("(Z)V", i3.getDescriptor());
		
		assertTrue(i2.isField());
		assertFalse(i2.isMethod());
		assertFalse(i3.isField());
		assertTrue(i3.isMethod());
	}
	
	public void Item_Class()
	{
		Item i1 = new Item_Folder("one");
		Item i2 = new Item("two", i1);
		Item i3 = new Item("three", i2);
		Item_Class i4 = new Item_Class("four", i3, new Source("")
				{
					@Override
					public void loadItemData(Item toLoad)
					{
						new Item_Member("member1", toLoad, "Z");
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
