package org.haslam;

import java.util.ArrayList;

public class FSRoot extends FSComponent
{
	@Override
	public FSComponent create(String type, String name, String drive) 
	{ 
		FSComponent component = new FSRoot();
		component.setRoot(component);
		component.setType(FSComponent.ROOT);
		component.setSize(-1);
		component.setPath("\\");
		component.setElements(new ArrayList<FSComponent>());
		return component;
	}

	@Override
	public void delete(String path) throws Exception 
	{
		throw new Exception("FS Root object cannot be deleted");
	}

	
	@Override
	public void writeToFile(FSComponent comp, String text) throws Exception
	{
		throw new Exception("Cannot write text to FS Root object.");
		
	}
	@Override
	public int computeSize(FSComponent comp) 
	{
		return -1;
	}
}
