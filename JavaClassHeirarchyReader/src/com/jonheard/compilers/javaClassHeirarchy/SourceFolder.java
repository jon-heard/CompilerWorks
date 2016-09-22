package com.jonheard.compilers.javaClassHeirarchy;

import java.io.File;

import com.jonheard.compilers.javaClassHeirarchy.Item.*;
import com.jonheard.util.UtilMethods;

class SourceFolder extends Source
{
	public SourceFolder(String filename)
	{
		super(filename);
	}

	public void loadItemData(Item toLoad)
	{
		String itemSrc = root.getName() + "/" + toLoad.getFileAddress();
		byte[] data = UtilMethods.fileToByteArray(itemSrc);
		loadClassItemDataFromByteArray(data, toLoad);
	}

	protected boolean loadData()
	{
		File thisFolder = new File(root.getName());
		if(!thisFolder.exists() || !thisFolder.isDirectory())
		{
			return false;
		}
		return loadFolder(thisFolder, root);
	}
	
	private boolean loadFolder(File folder, Item root)
	{
		File[] files = folder.listFiles();
		for(File file : files)
		{
			if(file.isDirectory())
			{
				if(!loadFolder(file, new Item_Package(file.getName(), root)))
				{
					return false;
				}
			}
			else if(file.getName().endsWith(".class"))
			{
				String className = file.getName().replace(".class", "");
				new Item_Class(className, root, this);
			}
		}
		return true;
	}
}
