package com.jonheard.compilers.javaClasspathDatabase;

import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.jonheard.compilers.javaClasspathDatabase.Item.*;

class SourceJar extends Source
{
	public SourceJar(JavaClasspathDatabase database, String filename)
	{
		super(database, filename);
	}

	public void loadItemData(Item toLoad)
	{
		String itemName = toLoad.getFileAddress();
		try
		{
			ZipInputStream zip =
					new ZipInputStream(new FileInputStream(root.getName()));
			ZipEntry entry = zip.getNextEntry();
			while(entry != null)
			{
				if(entry.getName().equals(itemName))
				{
					long dataSize = entry.getSize();
					byte[] data = new byte[(int)dataSize];
					zip.read(data);
					loadClassItemDataFromByteArray(data, toLoad);
					break;
				}
				entry = zip.getNextEntry();
			}
			zip.close();
		}
		catch(Exception e)
		{
			System.out.println("Warning: Class data failure: " + itemName);
		}
	}

	protected boolean loadData()
	{
		try
		{
			ZipInputStream zip =
					new ZipInputStream(new FileInputStream(root.getName()));
			
			ZipEntry entry = zip.getNextEntry();
			while(entry != null)
			{
				Item current = root;
				String[] address = entry.getName().split("/");
				entry = zip.getNextEntry();
				if(!address[address.length-1].endsWith(".class"))
				{
					continue;
				}
				address[address.length-1] =
						address[address.length-1].replace(".class", "");
				for(int i = 0; i < address.length; i++)
				{
					if(!current.hasChild(address[i]))
					{
						if(i != address.length-1)
						{
							new Item_Package(address[i], current);
						}
						else
						{
							new Item_Class(address[i], current, this);
						}
					}
					current = current.getChild(address[i]);
				}
			}
			zip.close();
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}
}
