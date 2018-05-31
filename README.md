# test_geotools_openmole

This repository is an example to embed the raster functinos from GeoTools into a OSGi bundle. This has been done in order to use models into the [OpenMOLE platform](https://www.openmole.org/).

The repository contains : 
- pom.xml : a maven description of the project with dependencies to GeoTools
- src : Java sources folder that contains two tasks (TiffReaderTask and TiffWriterTask) adapted to the use through an OSGi bundle
- test_geotools_openmole : that contains the necessary function for a use through OpenMOLE (an OpenMOLE script, an OSGi bundle produced with maven-shade plugin and a data folder used in the tasks)

[Mickaël Brasebin](http://recherche.ign.fr/labos/cogit/cv.php?nom=Brasebin) & [Julien Perret](http://recherche.ign.fr/labos/cogit/cv.php?prenom=Julien&nom=Perret)
[COGIT Laboratory](http://recherche.ign.fr/labos/cogit/accueilCOGIT.php)
