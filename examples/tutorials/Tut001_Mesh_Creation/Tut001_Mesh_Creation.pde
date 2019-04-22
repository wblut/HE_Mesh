import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh box;
WB_Render render;

void setup(){
    size(600,600,P3D);
   
    // All meshes are built with separate objects called creators
    HEC_Box boxCreator=new HEC_Box(); 
    
    //Set parameters one by one ... (See docs for parameters)
    boxCreator.setWidth(200);
    boxCreator.setWidthSegments(4);
   
    //... or string them together
    boxCreator.setHeight(200).setHeightSegments(5).setDepth(200).setDepthSegments(3);
    
    // exceptions: setCenter, setZAngle and setZAxis cannot be combined with other setParameters or should be put last
    // these three can be applied to all creators.
    boxCreator.setCenter(100,100,0).setZAxis(1,1,1).setZAngle(PI/4);
   
   //The actual mesh is created by calling the mesh creator in the HE_Mesh constructor
    box=new HE_Mesh(boxCreator);
    
    //The WB_Render object provides functions to draw all kinds of objects form the library.
    render=new WB_Render(this); //"this" is the calling applet, the object needs this to call Processing's functions.
  }

  void draw(){
    background(120);
    lights();
    translate(300,300,100);
    rotateY(mouseX*1.0f/width*TWO_PI);
    rotateX(mouseY*1.0f/height*TWO_PI);
    fill(255);
    noStroke();
    render.drawFaces(box);
   stroke(0);
   render.drawEdges(box);
  }