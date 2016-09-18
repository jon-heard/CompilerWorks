package com.jonheard.compilers.javaClassHeirarchyReader;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;

class SourceFolder extends Source
{
	public SourceFolder(String filename)
	{
		this(filename, null);
	}
	public SourceFolder(String filename, Item myItem)
	{
		this.myItem = myItem;
		File thisFolder = new File(filename);
		if(!thisFolder.exists() || !thisFolder.isDirectory())
		{
			isBad = true;
		}
		else
		{
			/// classes
			File[] classFiles = thisFolder.listFiles(new FileFilter()
			{
			    @Override
			    public boolean accept(File file)
			    {
			    	return file.getName().endsWith(".class");
			    }
			});
			for(File classFile : classFiles)
			{
				String className = classFile.getName().replace(".class", "");
				Item newItem = new Item(className, ItemType.CLASS, myItem);
				classes.put(className, newItem);
			}

			/// packages
			File[] folders = thisFolder.listFiles(new FileFilter()
			{
			    @Override
			    public boolean accept(File file)
			    {
			        return file.isDirectory();
			    }
			});
			for(File folder : folders)
			{
				Item newItem =
						new Item(folder.getName(), ItemType.PACKAGE, myItem); 
				SourceFolder newChild =
						new SourceFolder(folder.getPath(), newItem);
				children.put(folder.getName(), newChild);
			}
		}
	}
	
	public Item getHeirarchy(String address)
	{
		Item result = getMemoized(address);
		if(result != null)
		{
			return result;
		}
		
		
		return null;
	}
	
	protected HashMap<String, SourceFolder> children =
			new HashMap<String, SourceFolder>();
	protected HashMap<String, Item> classes = new HashMap<String, Item>();
	protected Item myItem;
}
