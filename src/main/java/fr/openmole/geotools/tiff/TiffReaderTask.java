package fr.openmole.geotools.tiff;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.ServiceLoader;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.factory.Hints;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.IIOServiceProvider;
import javax.media.jai.JAI;
import javax.media.jai.OperationRegistry;

import com.sun.media.jai.imageioimpl.ImageReadWriteSpi;

public class TiffReaderTask {

    public static void initJAI() {
        // Disable mediaLib searching that produces unwanted errors
        // See https://www.java.net/node/666373
        System.setProperty("com.sun.media.jai.disableMediaLib", "true");
	
        // As the JAI jars are bundled in the geotools plugin, JAI initialization does not work,
        // so we need to perform the tasks described here ("Initialization and automatic loading of registry objects"):
        // http://docs.oracle.com/cd/E17802_01/products/products/java-media/jai/forDevelopers/jai-apidocs/javax/media/jai/OperationRegistry.html
        OperationRegistry registry = JAI.getDefaultInstance().getOperationRegistry();
        if (registry == null) {
            System.out.println("geotools: error in JAI initialization. Cannot access default operation registry");
        } else {
            // Update registry with com.sun.media.jai.imageioimpl.ImageReadWriteSpi (only class listed javax.media.jai.OperationRegistrySpi)
            // it would be safer to parse this file instead, but a JAI update is very unlikely as it has not been modified since 2005
            try {
                new ImageReadWriteSpi().updateRegistry(registry);
            } catch (IllegalArgumentException e) {
                // See #10652: IAE: A descriptor is already registered against the name "ImageRead" under registry mode "rendered"
                System.out.println("geotools: error in JAI/ImageReadWriteSpi initialization: "+e.getMessage());
            }

            // Update registry with GeoTools registry file
            try (InputStream in = TiffReaderTask.class.getResourceAsStream("/META-INF/registryFile.jai")) {
                if (in == null) {
                    System.out.println("geotools: error in JAI initialization. Cannot access META-INF/registryFile.jai");
                } else {
                    registry.updateFromStream(in);
                }
            } catch (IOException | IllegalArgumentException e) {
                System.out.println("geotools: error in JAI/GeoTools initialization: "+e.getMessage());
            }
        }

        // Manual registering because plugin jar is not on application classpath
        IIORegistry ioRegistry = IIORegistry.getDefaultInstance();
        ClassLoader loader = TiffReaderTask.class.getClassLoader();

        Iterator<Class<?>> categories = ioRegistry.getCategories();
        while (categories.hasNext()) {
            @SuppressWarnings("unchecked")
            Iterator<IIOServiceProvider> riter = ServiceLoader.load((Class<IIOServiceProvider>) categories.next(), loader).iterator();
            while (riter.hasNext()) {
                IIOServiceProvider provider = riter.next();
                System.out.println("Registering " + provider.getClass());
                ioRegistry.registerServiceProvider(provider);
            }
        }
}
    public static ClassLoader getClassLoader() {
	return TiffReaderTask.class.getClassLoader();
    }

    public static double readGeoTiff(File file) throws IOException {
	    if (file.exists()) {
		System.out.println("File exist : " + file.getPath());
	    } else {
		System.out.println("File does not exist : " + file.getAbsolutePath());
	    }
	    System.out.println("initJAI start");
	    initJAI();
	    System.out.println("initJAI end");
	    GridCoverage2D coverageSet = getCoverage(file);
	    System.out.println("coverage : " + coverageSet);
	    CoordinateReferenceSystem crs = coverageSet.getCoordinateReferenceSystem2D();
	    System.out.println("crs : " + crs);
	    Envelope env = coverageSet.getEnvelope();
	    System.out.println("env : " + env);
	    
	    System.out.println("env : " + env.getMinimum(0) + ", " + env.getMinimum(1) + ", " + env.getMaximum(0) + ", " + env.getMaximum(1));
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
