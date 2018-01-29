package fr.openmole.geotools.tiff.test;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import fr.openmole.geotools.tiff.TiffReaderTask;


public class TestTiffReaderTask {
	
	@Test
	public  void testReader() throws IOException{
		
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("./tif/test.tif").getFile());

		if(! file.exists()){
			System.out.println("File do not exist");
		}
		double d = TiffReaderTask.readGeoTiff(file);
		
		Assert.assertTrue(! Double.isNaN(d));
		
	}

}
