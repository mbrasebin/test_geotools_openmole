package fr.openmole.geotools.tiff;

import java.io.File;

import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffWriteParams;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;

public class TiffWriterTask {
	public static void write(File fileIn, File fileOut) {		
		System.out.println("fileOut : " + fileOut.getAbsolutePath());
		TiffReaderTask.initJAI();
		try {
			GeoTiffWriteParams wp = new GeoTiffWriteParams();
			wp.setCompressionMode(GeoTiffWriteParams.MODE_EXPLICIT);
			wp.setCompressionType("LZW");
			ParameterValueGroup params = new GeoTiffFormat().getWriteParameters();
			params.parameter(AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString()).setValue(wp);

			GeoTiffWriter writer = new GeoTiffWriter(fileOut);
			
			writer.write(TiffReaderTask.getCoverage(fileIn),
				     (GeneralParameterValue[]) params.values().toArray(new GeneralParameterValue[1]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
