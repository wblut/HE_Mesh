# HE_Mesh
=========

HE_Mesh, a Java library for creating and manipulating polygonal meshes. Aimed primarily at [Processing](http://processing.org/).

## Building HE_Mesh from source.

The first thing you need to do is download or fork this repository and import the code in Eclipse. Dependencies are managed by Maven except for these two:

* `hemesh-external-6_0_0.jar`: HE_Mesh contains source code from other authors that was converted to use my geometry classes for convenience. To protect the rights of the original authors whose work is not in the public domain, the source code is only available on request.
* `hemesh-data-3_0_0.jar`: Data for HE_Mesh.


## Current release: 2019.0.2 - Phoenix

Download the current release here: https://www.wblut.com/hemesh/hemesh.zip.


## Version in progress: 2020.0.0 - Quarante (2020/04/05)

Download a recent built here: https://www.wblut.com/hemesh/hemesh20200105.zip.

## Examples

Download: https://github.com/wblut/HE_Mesh_Examples.


## License

HE_Mesh, with the below exceptions, is dedicated to the public domain. 
To the extent possible under law, I, Frederik Vanhoutte, have waived all copyright and related or neighboring rights to HE_Mesh. This work is published from Belgium.
(http://creativecommons.org/publicdomain/zero/1.0/)

The following classes are subject to the license agreement of their original authors, included in the source file:

* wblut.geom.WB_PolygonDecomposer
* wblut.geom.WB_PolygonSplitter
* wblut.hemesh.HEC_SuperDuper
* wblut.hemesh.HET_FaceSplitter
* wblut.math.WB_DoubleDouble
* wblut.math.WB_Ease
* wblut.math.WB_MTRandom
* wblut.math.WB_OSNoise
* wblut.math.WB_PNoise
* wblut.math.WB_SNoise

The following packages are part of hemesh-external.jar and are subject to the license agreement of their original authors:

* wblut.external.constrainedDelaunay https://www2.eecs.berkeley.edu/Pubs/TechRpts/2009/EECS-2009-56.html
* wblut.external.Delaunay https://github.com/visad/visad 
* wblut.external.ProGAL http://www.diku.dk/~rfonseca/ProGAL/
* wblut.external.straightskeleton https://code.google.com/p/campskeleton/
* wblut.external.QuickHull3D https://www.cs.ubc.ca/~lloyd/java/quickhull3d.html
* The JTS Topology Suite is an API of spatial predicates and functions for processing planar geometry. http://tsusiatsoftware.net/jts/main.html

The modified code is available on request.
