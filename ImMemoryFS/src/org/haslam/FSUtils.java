package org.haslam;

import java.util.ArrayList;
import java.util.List;

public class FSUtils 
{

	public static String getParentPath(String path)
	{
		int fileNameStart = path.lastIndexOf('\\');
		String parentPath = path.substring(0, fileNameStart);
		return parentPath;
	}
	
	public static String getPathLeafName(String path)
	{
		int fileNameStart = path.lastIndexOf('\\');
		String filename = path.substring(fileNameStart + 1);
		return filename;
	}
	
	public static void dumpConponent(FSComponent comp)
	{
		System.out.println (comp.getPath() + ":" + comp.getName() + ":" + comp.getType() + ":" + comp.getSize());
	}
	
	public static String[] getPathElements(String path)
	{
		int start =0;
		int end = 0;
		String c = "";
		List<String> elements = new ArrayList<String>();
		while(end != -1)
		{
			start = path.indexOf("\\");
			String remainder = path.substring(start+1);
			end = remainder.indexOf("\\");
			if (end == -1)
			{
				c=remainder.substring(0);
			}
			else
			{
				c = remainder.substring(0, end);
			}
			if (c.length() != 0)
			{
				elements.add(c);
			}
			if (end != -1)
			{
				path = remainder.substring(end);
			}
		}
		String[] components = elements.toArray(new String[elements.size()]);
		return components;
	}

	public static void updateSizesForPath(String path) throws Exception 
	{
		while (path.length() > 0)
		{
			
			FSComponent comp = FSComponent.findElement(path);
			// Must work from the bottom up
			comp.computeSize(comp);
			int index =path.lastIndexOf("\\");
			path = path.substring(0, index);
		}
	}

	
}
