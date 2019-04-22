//WB_Coord is an interface for coordinate type classes. It exposes methods to access coordinates.
//It supports up to 4 ordinates x, y, z and w. The ordinates are stored as double precision but can be 
//retrieved as single precision float for ease of use in Processing.

import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.math.*;
import wblut.nurbs.*;
import wblut.processing.*;

//WB_GeometryFactory is a special class that can be used to create basic geometry such as points, lines and circles.
WB_GeometryFactory gf=new WB_GeometryFactory();

//https://docs.oracle.com/javase/tutorial/java/concepts/interface.html
//A JAVA interface is a collection of methods common for several classes. These classes are said
//to implement the interface. The most important classes implementing WB_Coord are WB_Point and
//WB_Vector. Interfaces can not be instantiated, so something like
//
// WB_Coord coord=new WB_Coord()
//
//is not possible since WB_Coord is not a class and does not have constructors. But this is possible
//
// WB_Coord coord=new WB_Point().
//
//The difference with
//
// WB_Point coord=new WB_Point()
//
//is that only the methods of WB_Coord will be visible, not those of WB_Point. This is useful for
//separating functionality or limiting access to members of a class, less mistakes, easier to debug. 

void setup() {
  //An object declared as a WB_Coord is immutable. It cannot be changed after creation.
  WB_Coord p = new WB_SimpleCoordinate4D(0,1,2,3);
  
  println(p);
  
  //Getting coordinates as a double precision float
  double xCoordD=p.xd();
  double yCoordD=p.yd();
  double zCoordD=p.zd();
  double wCoordD=p.wd();
  int ordinate=0;
  double iCoordD=p.getd(ordinate); //x=0,y=1,z=2,w=3;
  
  //Getting coordinates as a single precision float
  float xCoordF=p.xf();
  float yCoordF=p.yf();
  float zCoordF=p.zf();
  float wCoordF=p.wf();
  float iCoordF=p.getf(ordinate); //x=0,y=1,z=2,w=3;
  
}