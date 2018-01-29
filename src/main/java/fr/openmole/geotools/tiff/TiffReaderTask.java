package fr.openmole.geotools.tiff;

import java.io.File;
import java.io.IOException;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.factory.Hints;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class TiffReaderTask {

	public static double readGeoTiff(String folderIn, String fileIn) throws IOException {

		File file = new File(folderIn, fileIn);

		return readGeoTiff(file);
	}

	public static double readGeoTiff(File file) throws IOException {
		if (file.exists()) {
			System.out.println("File exist : " + file.getPath());
		} else {
			System.out.println("File does not exist : " + file.getAbsolutePath());
		}

		GridCoverage2D coverageSet = getCoverage(file);
		CoordinateReferenceSystem crs = coverageSet.getCoordinateReferenceSystem2D();
		Envelope env = coverageSet.getEnvelope();

		return env.getMaximum(0);

	}

	public static GridCoverage2D getCoverage(File file) throws IOException {

		// setting of useless parameters
		ParameterValue<OverviewPolicy> policy = AbstractGridFormat.OVERVIEW_POLICY.createValue();
		policy.setValue(OverviewPolicy.IGNORE);
		// this will basically read 4 tiles worth of data at once from the
		// disk...
		ParameterValue<String> gridsize = AbstractGridFormat.SUGGESTED_TILE_SIZE.createValue();
		// Setting read type: use JAI ImageRead (true) or ImageReaders read
		// methods (false)
		ParameterValue<Boolean> useJaiRead = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
		useJaiRead.setValue(false);

		GeneralParameterValue[] params = new GeneralParameterValue[] { policy, gridsize, useJaiRead };

		GeoTiffReader reader = new GeoTiffReader(file, new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));
		GridCoverage2D coverageSet = reader.read(params);

		return coverageSet;
	}

}
