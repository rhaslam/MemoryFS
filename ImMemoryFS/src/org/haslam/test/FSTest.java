package org.haslam.test;

import org.junit.Test;
import static org.junit.Assert.*;

import org.haslam.Drive;
import org.haslam.FSComponent;
import org.haslam.FSFolder;
import org.haslam.FSRoot;
import org.haslam.FSText;
import org.haslam.FSUtils;
import org.haslam.FSZipFile;

public class FSTest 
{

    @Test
    public void testBasic() throws Exception 
    {
		//Create Root Element
		FSComponent root = new FSRoot().create(FSComponent.ROOT, null, null);
		assertNotNull(root);
		
		//Create Drive
		FSComponent drive = new Drive().create(FSComponent.DRIVE, "A", root.getPath());
		assertNotNull(drive);
		
		// Find Drive
		FSComponent targetDrive = FSComponent.findElement(drive.getPath());
		FSUtils.dumpConponent(targetDrive);
		assertEquals("\\A", targetDrive.getPath());
		assertEquals(targetDrive.getElements().size(),0);
		assertEquals(targetDrive.getType(), FSComponent.DRIVE);
		assertEquals(targetDrive.getName(), "A");
		assertNull(targetDrive.getContent());
	
		// Add folder to drive
		FSComponent folder = new FSFolder().create(FSComponent.FOLDER, "folder1", drive.getPath());
		assertNotNull(folder);
		
		// Find Folder
		FSComponent targetFolder = FSComponent.findElement(folder.getPath());
		FSUtils.dumpConponent(targetFolder);
		assertEquals(targetFolder.getPath(), "\\A\\folder1");
		assertEquals(targetFolder.getElements().size(),0);
		assertEquals(targetFolder.getType(), FSComponent.FOLDER);
		assertEquals(targetFolder.getName(), "folder1");
		assertNull(targetFolder.getContent());
		
		
		// Add text file
		try
		{
			FSComponent textFile = new FSText().create(FSComponent.TEXT_FILE, "text1", folder.getPath());
		
			// Find Text File
			FSComponent targetTextFile = FSComponent.findElement(textFile.getPath());
			targetTextFile.writeToFile(targetTextFile, "This is the content of file1");
			FSUtils.dumpConponent(targetTextFile);
			assertEquals(targetTextFile.getPath(), "\\A\\folder1\\text1");
			assertNull(targetTextFile.getElements());
			assertEquals(targetTextFile.getType(), FSComponent.TEXT_FILE);
			assertEquals(targetTextFile.getName(), "text1");
			assertNotNull(targetTextFile.getContent());
		
			targetTextFile.delete(targetTextFile.getPath());
		}
		catch (Exception e)
		{
		}
		
		// Add Zip File
		try
		{
			FSComponent zipFile = new FSZipFile().create(FSComponent.ZIP_FILE, "zip1", folder.getPath());
		
			// Find Text File
			FSComponent targetZipFile = FSComponent.findElement(zipFile.getPath());
			FSUtils.dumpConponent(targetZipFile);
			assertEquals(targetZipFile.getPath(), "\\A\\folder1\\zip1");
			assertEquals(targetZipFile.getElements().size(),0);
			assertEquals(targetZipFile.getType(), FSComponent.ZIP_FILE);
			assertEquals(targetZipFile.getName(), "zip1");
			assertNull(targetZipFile.getContent());
		
			//targetZipFile.delete(targetZipFile.getPath());
			
			// Add stuff to ZIP file
			new FSFolder().create(FSComponent.FOLDER, "zipfolder1", targetZipFile.getPath());
			FSComponent zipTextFile = new FSText().create(FSComponent.TEXT_FILE, "ziptext1", targetZipFile.getPath());
			zipTextFile.writeToFile(zipTextFile, "This is the text of a file enclosed in a ZIP file");
		}
		catch (Exception e)
		{
		}
    }
    
    @Test
    public void testNoSuchFile() throws Exception
    {
		//Create Root Element
		FSComponent root = new FSRoot().create(FSComponent.ROOT, null, null);
		assertNotNull(root);
		
		//Create Drive
		FSComponent drive = new Drive().create(FSComponent.DRIVE, "A", root.getPath());
		assertNotNull(drive);
		
		//Create folder bar
		// Add folder to drive
		FSComponent folder = new FSFolder().create(FSComponent.FOLDER, "bar", drive.getPath());
		assertNotNull(folder);
		
		// Add text file
		try
		{
			new FSText().create(FSComponent.TEXT_FILE, "text1", folder.getPath());
		}
		catch (Exception e)
		{
			System.out.println("File creation threw ane exception.");
		}
		
    	FSComponent unknownFolder = FSComponent.findElement("\\A\\foo");
    	assertNull(unknownFolder);
    	
    	FSComponent textFile = FSComponent.findElement("\\A\\bar\\text1");
    	assertNotNull(textFile);
    	
    	FSComponent unknownText = FSComponent.findElement("\\A\\bar\\text2");
    	assertNull(unknownText);
    			
    }
    
    @Test
    public void testSize() throws Exception
    {
		//Create Root Element
		FSComponent root = new FSRoot().create(FSComponent.ROOT, null, null);
		assertNotNull(root);
		
		//Create Drive
		FSComponent drive = new Drive().create(FSComponent.DRIVE, "A", root.getPath());
		assertNotNull(drive);
		
		//Create folder bar
		FSComponent folder = new FSFolder().create(FSComponent.FOLDER, "bar", drive.getPath());
		assertNotNull(folder);
		
		int folderSize = folder.getSize();
		assertEquals(folderSize, 0);
		
		int driveSize = drive.getSize();
		assertEquals(driveSize,0);

		// Add text File to folder
		// Add text file
		FSComponent textFile = null;
		try
		{
			textFile = new FSText().create(FSComponent.TEXT_FILE, "text1", folder.getPath());
		}
		catch (Exception e)
		{
			System.out.println("File creation threw ane exception.");
		}
		assertNotNull(textFile);
		int textFileSize = textFile.getSize();
		assertEquals(textFileSize, 0);
		
		folderSize = folder.getSize();
		assertEquals(folderSize, 0);
		
		driveSize = drive.getSize();
		assertEquals(driveSize,0);
		
		String text = "This is it.";
		int textLen = text.length();
		textFile.writeToFile(textFile, text);
		
		textFileSize = textFile.getSize();
		assertEquals(textFileSize, textLen);
		
		folderSize = folder.getSize();
		assertEquals(folderSize, textLen);
		
		driveSize = drive.getSize();
		assertEquals(driveSize,textLen);
		
		// Add Zip File
		FSComponent zipFile = null;
		int contentLen = 0;
		try
		{
			zipFile = new FSZipFile().create(FSComponent.ZIP_FILE, "zip1", folder.getPath());
			String content = "This is the text of a file enclosed in a ZIP file";
			contentLen = content.length()/2;
			
			// Add stuff to ZIP file
			FSComponent zipFolder = new FSFolder().create(FSComponent.FOLDER, "zipfolder1", zipFile.getPath());
			FSComponent zipTextFile = new FSText().create(FSComponent.TEXT_FILE, "ziptext1", zipFile.getPath());
			zipTextFile.writeToFile(zipTextFile, content );
		}
		catch (Exception e)
		{
		}
		int zipFileSize = zipFile.getSize();
		assertEquals(zipFileSize, contentLen);
		
		folderSize = folder.getSize();
		assertEquals(folderSize, textLen + contentLen);
		
		driveSize = drive.getSize();
		assertEquals(driveSize,textLen + contentLen);
    }
    
    /**
     * Test breaking a path with delimeter "\\" into component parts
     * 
     * Hate "\\"
     */
    
    
    @Test
    public void testPathComponents()
    {
    	String[] elements = FSUtils.getPathElements("\\a\\folder\\text1");
    	assertEquals(elements.length, 3);
    	
    	elements = FSUtils.getPathElements("\\");
    	assertEquals(elements.length, 0);
    	
    	elements = FSUtils.getPathElements("\\0\\1\\2\\3\\4\\5\\6\\7\\8\\9\\10");
    	assertEquals(elements.length, 11);
    }
    
    @Test
    public void testMove() throws Exception
    {
		//Create Root Element
		FSComponent root = new FSRoot().create(FSComponent.ROOT, null, null);
		assertNotNull(root);
		
		//Create Drive
		FSComponent drive = new Drive().create(FSComponent.DRIVE, "A", root.getPath());
		assertNotNull(drive);
		
		//Create folder bar
		FSComponent folderBar = new FSFolder().create(FSComponent.FOLDER, "bar", drive.getPath());
		assertNotNull(folderBar);

		//Create folder foo
		FSComponent folderFoo = new FSFolder().create(FSComponent.FOLDER, "foo", drive.getPath());
		assertNotNull(folderFoo);
		
		// Add Zip File
		FSComponent zipFile = null;
		int contentLen = 0;
		try
		{
			zipFile = new FSZipFile().create(FSComponent.ZIP_FILE, "zip1", folderBar.getPath());
			String content = "This is the text of a file enclosed in a ZIP file";
			contentLen = content.length()/2;
			
			// Add stuff to ZIP file
			new FSFolder().create(FSComponent.FOLDER, "zipfolder1", zipFile.getPath());
			FSComponent zipTextFile = new FSText().create(FSComponent.TEXT_FILE, "ziptext1", zipFile.getPath());
			zipTextFile.writeToFile(zipTextFile, content );
		}
		catch (Exception e)
		{
		}
		int zipFileSize = zipFile.getSize();
		assertEquals(zipFileSize, contentLen);
		
		int folderBarSize = folderBar.getSize();
		assertEquals(folderBarSize, contentLen);

		int folderFooSize = folderFoo.getSize();
		assertEquals(folderFooSize, 0);
		
		int driveSize = drive.getSize();
		assertEquals(driveSize,contentLen);
		
		folderBar.move("\\A\\bar\\zip1", "\\A\\Foo");
		
		folderBarSize = folderBar.getSize();
		assertEquals(folderBarSize, 0);

		folderFooSize = folderFoo.getSize();
		assertEquals(folderFooSize, contentLen);
		
		driveSize = drive.getSize();
		assertEquals(driveSize,contentLen);
		
    }
    
    @Test
    public void testDelete() throws Exception
    {
		//Create Root Element
		FSComponent root = new FSRoot().create(FSComponent.ROOT, null, null);
		assertNotNull(root);
		
		//Create Drive
		FSComponent drive = new Drive().create(FSComponent.DRIVE, "A", root.getPath());
		assertNotNull(drive);
		
		//Create folder bar
		FSComponent folderBar = new FSFolder().create(FSComponent.FOLDER, "bar", drive.getPath());
		assertNotNull(folderBar);
		
		// Add Zip File
		FSComponent zipFile = null;
		int contentLen = 0;
		try
		{
			zipFile = new FSZipFile().create(FSComponent.ZIP_FILE, "zip1", folderBar.getPath());
			String content = "This is the text of a file enclosed in a ZIP file";
			contentLen = content.length()/2;
			
			// Add stuff to ZIP file
			new FSFolder().create(FSComponent.FOLDER, "zipfolder1", zipFile.getPath());
			FSComponent zipTextFile = new FSText().create(FSComponent.TEXT_FILE, "ziptext1", zipFile.getPath());
			zipTextFile.writeToFile(zipTextFile, content );
		}
		catch (Exception e)
		{
		}
		int zipFileSize = zipFile.getSize();
		assertEquals(zipFileSize, contentLen);
		
		int folderBarSize = folderBar.getSize();
		assertEquals(folderBarSize, contentLen);
		
		int driveSize = drive.getSize();
		assertEquals(driveSize,contentLen);
		
		zipFile.delete("\\A\\bar\\zip1");
		
		assertNull(FSComponent.findElement(zipFile.getPath()));
		
		folderBarSize = folderBar.getSize();
		assertEquals(folderBarSize, 0);

		driveSize = drive.getSize();
		assertEquals(driveSize,0);
    }
}
