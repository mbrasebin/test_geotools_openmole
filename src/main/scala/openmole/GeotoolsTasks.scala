package openmole

import java.io.File
import fr.openmole.geotools.tiff._

object ReaderTask {
  def apply(folderIn: String, fileIn: String): (String, String) = {
    TiffReaderTask.readGeoTiff(folderIn, fileIn);
    (folderIn, fileIn)
  }

}

object WriterTask {
  def apply(folderIn: String, fileIn: String, fileOut: String): (String, String) = {
    TiffWriterTask.write(folderIn, fileIn, fileOut);
    (folderIn, fileIn)
  }

}

