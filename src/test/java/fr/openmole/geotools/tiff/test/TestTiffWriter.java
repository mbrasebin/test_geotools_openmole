package fr.openmole.geotools.tiff.test;

import java.io.File;

import fr.openmole.geotools.tiff.TiffWriterTask;
import org.junit.Test;

public class TestTiffWriter {
	
	@Test
	public void testWriter(){

		
		
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("./tif/").getFile());
		
		
		
			
			TiffWriterTask.write(file.getAbsolutePath(),"test.tif", "out.tif");
			


	}

}
