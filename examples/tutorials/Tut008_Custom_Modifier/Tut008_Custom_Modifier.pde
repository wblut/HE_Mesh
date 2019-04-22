import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;


HE_Mesh box;
HE_Mesh modifiedBox;
WB_Render render;

void setup(){
    size(800,800,P3D);
 
   
    //Create the box
    HEC_Box boxCreator=new HEC_Box().setWidth(400).setWidthSegments(20)
    .setHeight(200).setHeightSegments(10)
    .setDepth(200).setDepthSegments(10);
    box=new HE_Mesh(boxCreator);
    
    //The easiest way to create a simple modifier is by exporting all vertex coordinates, change them and
    //recreate the mesh with the new coordinates. Writing a full-blown implementation of a HEM_Modifier
    //is best done in Eclipse with full access to the code repository.
    
    //Export the faces and vertices
    float[][] vertices =box.getVerticesAsFloat(); // first index = vertex index, second index = 0..2, x,y,z coordinate
    int [][] faces = box.getFacesAsInt();// first index = face index, second index = index of vertex belonging to face
     
    //Do something with the vertices
    for(int i=0;i<box.getNumberOfVertices();i++){
     vertices[i][0]*=1.2+.2*sin(HALF_PI/10*i+HALF_PI); 
     vertices[i][1]*=1.2+.2*sin(HALF_PI/17*i);
     vertices[i][2]*=1.2+.2*cos(HALF_PI/25*i);
    }
     
    //Use the exported faces and vertices as source for a HEC_FaceList
    HEC_FromFacelist faceList=new HEC_FromFacelist().setFaces(faces).setVertices(vertices);
    modifiedBox=new HE_Mesh(faceList);
    
    render=new WB_Render(this);

}

  void draw(){
    background(25);
    lights();
    translate(width/2,height/2,0);
    rotateY(mouseX*1.0f/width*TWO_PI);
    rotateX(mouseY*1.0f/height*TWO_PI);
    noStroke();
    render.drawFaces(box);
    stroke(160,0,0);
    render.drawEdges(box);
    render.drawEdges(modifiedBox);
  }