package org.haslam;

import java.util.List;

public class FSText extends FSComponent
{
	@Override
	public FSComponent create(String type, String name, String parent) throws Exception 
	{ 
		FSComponent component = new FSText();
		component.setType(FSComponent.TEXT_FILE);
		component.setName(name);
		component.setPath(parent + "\\" + name);
		component.setSize(0);
		component.setElements(null);
		
		FSComponent parentComponent = findElement(parent);

		String fsType = parentComponent.getType();
		if (! fsType.equalsIgnoreCase(FSComponent.FOLDER) && (! fsType.equalsIgnoreCase(FSComponent.ZIP_FILE)))
		{
			throw new Exception("Text files can only be added to folders and zip files");
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
		return size;
	}

}
