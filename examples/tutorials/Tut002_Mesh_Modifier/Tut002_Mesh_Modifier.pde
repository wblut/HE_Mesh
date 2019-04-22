import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;


HE_Mesh box;
WB_Render render;

void setup(){
    size(600,600,P3D);
    
    HEC_Box boxCreator=new HEC_Box().setWidth(400).setWidthSegments(5)
      .setHeight(200).setHeightSegments(3)
      .setDepth(200).setDepthSegments(2);
    boxCreator.setCenter(100,100,0).setZAxis(1,1,1);
    box=new HE_Mesh(boxCreator);
    
    //Modifiers are separate objects, not unlike creators
    HEM_Wireframe wireframe=new HEM_Wireframe();
    
     //Set parameters one by one ...
    wireframe.setStrutRadius(10);
    
     //... or string them together
    wireframe.setStrutFacets(6).setMaximumStrutOffset(20);
    
    //Modify the mesh by calling a modifier in a modify command
    box.modify(wireframe);
    
    // Modifiers can also be specified inline. Using modifiers inline has some disadvantages.
    // The modifier abject is not available for further reference, you would need this if you 
    // wanted to access mesh selections stored in the modifier after application. E.g. all extruded
    // faces.
    // box.modify(new HEM_Wireframe().setRadius(10).setFacets(4).setMaximumJointRadius(20));
    
    render=new WB_Render(this);
  }

  void draw(){
    background(120);
    lights();
    translate(300,300,0);
    rotateY(mouseX*1.0f/width*TWO_PI);
    rotateX(mouseY*1.0f/height*TWO_PI);
    noStroke();
    render.drawFaces(box);
    stroke(0);
   render.drawEdges(box);

  }