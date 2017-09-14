package org.haslam;

import java.util.ArrayList;
import java.util.List;

public class FSFolder extends FSComponent 
{
	@Override
	public FSComponent create(String type, String name, String parent) throws Exception 
	{ 
		FSComponent component = new FSFolder();
		component.setType(FSComponent.FOLDER);
		component.setName(name);
		component.setPath(parent + "\\" + name);
		component.setSize(0);
		component.setElements(new ArrayList<FSComponent>());
		
		FSComponent parentComponent = findElement(parent);
		
		List<FSComponent> currentParentComponents = parentComponent.getElements();
		currentParentComponents.add(component);
		return component;
	}
	
	@Override
	public void delete(String path) throws Exception 
	{
		// Find folder name
		
		String filename = FSUtils.getPathLeafName(path);
	
		FSComponent folder = findElement(path);
		List<FSComponent> elements = folder.getElements();
		for (FSComponent entry : elements)
		{
			if(entry.getName().equalsIgnoreCase(filename))
			{
				elements.remove(entry);
			}
			
		}
	}
	

	@Override
	public void writeToFile(FSComponent comp, String text) throws Exception
	{
		throw new Exception("Cannot write text to Folder object.");
		
	}

	@Override
	public int computeSize(FSComponent comp) 
	{
		int sum = 0;

		List<FSComponent>elements = comp.getElements();
		for (FSComponent elem : elements)
		{
			sum += elem.getSize();
		}
		size = sum;
		return size;
	}
}
