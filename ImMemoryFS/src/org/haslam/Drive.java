package org.haslam;

import java.util.ArrayList;
import java.util.List;

public class Drive extends FSComponent
{
	@Override
	public FSComponent create(String type, String name, String parentPath) 
	{ 
		FSComponent component = new Drive();
		component.setType(FSComponent.DRIVE);
		component.setName(name);
		component.setPath(parentPath + name);
		component.setSize(0);
		component.setElements(new ArrayList<FSComponent>());

		List<FSComponent> currentParentComponents = component.getRoot().getElements();
		currentParentComponents.add(component);
		
		return component;
	}

	@Override
	public void writeToFile(FSComponent comp, String text) throws Exception
	{
		throw new Exception("Cannot write text to Drive object.");
		
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
		size=sum;
		return sum;
	}


}
