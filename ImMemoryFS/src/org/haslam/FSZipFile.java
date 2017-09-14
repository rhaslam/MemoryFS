package org.haslam;

import java.util.ArrayList;
import java.util.List;

public class FSZipFile extends FSComponent
{

	@Override
	public FSComponent create(String type, String name, String parent) throws Exception 
	{ 
		FSComponent component = new FSZipFile();
		component.setType(FSComponent.ZIP_FILE);
		component.setName(name);
		component.setPath(parent + "\\" + name);
		component.setSize(0);
		component.setElements(new ArrayList<FSComponent>());
		
		FSComponent parentComponent = findElement(parent);
		if (! parentComponent.getType().equals(FSComponent.FOLDER))
		{
			throw new Exception("Zip files can only be added to folders");
		}
		
		List<FSComponent> currentParentComponents = parentComponent.getElements();
		currentParentComponents.add(component);
		return component;
	}

	@Override
	public void writeToFile(FSComponent component, String text) throws Exception 
	{
		component.setContent(text);
		component.setSize(text.length());
		String fullPath = component.getPath();
		String parentPath = FSUtils.getParentPath(fullPath);
		FSUtils.updateSizesForPath(parentPath);
		
	}
	@Override
	public int computeSize(FSComponent comp) 
	{
		int total = 0;
		
		List<FSComponent>elements = comp.getElements();
		for (FSComponent elem : elements)
		{
				total += elem.getSize();
		}
		size = total / 2;
		return size;
	}
}
