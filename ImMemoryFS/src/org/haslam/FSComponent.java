package org.haslam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class FSComponent 
{
	public static final String ROOT = "ROOT";
	public static final String DRIVE = "DRIVE"; //drive
	public static final String FOLDER = "FOLDER";
	public static final String ZIP_FILE = "ZIP_FILE" ; //zip file
	public static final String TEXT_FILE = "TEXT_FILE"; // zip file
	
	protected static FSComponent root;
	protected String type;
	protected String name;
	protected String path;
	protected int size;
	protected String content;
	protected List<FSComponent> elements;
	
	// Abstract methods
	public abstract FSComponent create(String type, String name, String parent) throws Exception;

	public abstract int computeSize(FSComponent comp);
	
	public abstract void writeToFile(FSComponent comp, String newContent) throws Exception;
	
	// Concrete Methods
	public void createRoot()
	{
		root = new FSRoot().create(FSComponent.ROOT, null, null);
	}
	
	public void delete(String path) throws Exception 
	{
		// Find parent
		String parentPath = FSUtils.getParentPath(path);
		String leafName = FSUtils.getPathLeafName(path);
		FSComponent parentComponent = FSComponent.findElement(parentPath);
		List<FSComponent> elements = parentComponent.getElements();
		Iterator<FSComponent> iter = elements.iterator();
		while (iter.hasNext())
		{
			FSComponent entry = iter.next();
			if(entry.getName().equalsIgnoreCase(leafName))
			{
				iter.remove();
			}
			
		}
		FSUtils.updateSizesForPath(parentPath);
	}
	
	public void move(String srcPath, String destPath) throws Exception
	{
		FSComponent srcObject = findElement(srcPath);
		FSComponent destObject = findElement(destPath);
		String destObjectType = destObject.getType();
		if ((! destObjectType.equalsIgnoreCase(FOLDER)) && (!destObjectType.equalsIgnoreCase(ZIP_FILE)))
		{
			throw new Exception("Cannot move element to non folder object");
		}
		
		//Remove from parent
		String parentPath = FSUtils.getParentPath(srcPath);
		String leafName = FSUtils.getPathLeafName(srcPath);
		FSComponent srcParent = findElement(parentPath);
		List<FSComponent> elements =  srcParent.getElements();
		Iterator<FSComponent> iter = elements.iterator();
		while (iter.hasNext())
		{
			FSComponent current = iter.next();
			if (current.getName().equalsIgnoreCase(leafName))
			{
				iter.remove();
				break;
			}
		}
		FSUtils.updateSizesForPath(parentPath);
		
		// Add src object to destination
		destObject.getElements().add(srcObject);
		FSUtils.updateSizesForPath(destPath);
		
	}
	
	// Auto generated getters and setters
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<FSComponent> getElements() {
		return elements;
	}

	public void setElements(ArrayList<FSComponent> arrayList) {
		this.elements = arrayList;
	}

	public FSComponent getRoot() {
		return root;
	}
	
	public void setRoot(FSComponent root)
	{
		this.root = root;
	}
	


	//Utility Methods
	public static FSComponent findElement(String path) throws Exception
	{
		String pathElements[] = FSUtils.getPathElements(path);
		int numPathElements = pathElements.length;
		int pathElement = 0;
		
		if (root == null)
		{
			throw new Exception("The filesystem  has not root element.");
		}
		List<FSComponent> elements = root.getElements();
		FSComponent comp = null;
		Iterator<FSComponent> elemIter = null;
		boolean targetElementFound = false;
		while (!targetElementFound && pathElement < numPathElements)
		{
			boolean currentElementFound = false;
			elemIter = elements.iterator();
			while (elemIter.hasNext())
			{
				comp = elemIter.next();
				if (comp.getName().equalsIgnoreCase(pathElements[pathElement]))
				{
					if (pathElement == numPathElements)
					{
						targetElementFound = true;
						break;
					}
					else
					{
						currentElementFound = true;
						pathElement++;
						elements = comp.getElements();
						break;
					}
				}
			}
			if (!currentElementFound && ! targetElementFound)
			{
				return null;
			}
		}
		return comp;
	}

/*
	
	public static void main (String[] args) throws Exception
	{
		//Create Root Element
		FSComponent root = new FSRoot().create(FSComponent.ROOT, null, null);
			
		//Create Drive
		FSComponent drive = new Drive().create(FSComponent.DRIVE, "A", root.getPath());
		
		// Find Drive
		FSComponent targetDrive = FSComponent.findElement(drive.getPath());
		FSUtils.dumpConponent(targetDrive);
	
		// Add folder to drive
		FSComponent folder = new FSFolder().create(FSComponent.FOLDER, "folder1", drive.getPath());
		
		// Find Folder
		FSComponent targetFolder = FSComponent.findElement(folder.getPath());
		FSUtils.dumpConponent(targetFolder);
		
		// Add text file
		FSComponent textFile = new FSText().create(FSComponent.TEXT_FILE, "text1", folder.getPath());
		
		// Find Folder
		FSComponent targetTextFile = FSComponent.findElement(textFile.getPath());
		targetTextFile.writeToFile("This is the content of file1");
		FSUtils.dumpConponent(targetTextFile);
		
		targetTextFile.delete(targetTextFile.getPath());
	}
*/
}
