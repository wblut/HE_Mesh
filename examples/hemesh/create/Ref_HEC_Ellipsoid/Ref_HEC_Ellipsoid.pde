import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render render;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  HEC_Ellipsoid creator=new HEC_Ellipsoid();
  creator.setRadius(300,200,100); 
  creator.setUFacets(16);
  creator.setVFacets(16);
  mesh=new HE_Mesh(creator); 
  render=new WB_Render(this);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2, 0);
  rotateY(map(mouseX, 0, width, -PI/2, PI/2));
  rotateX(map(mouseY, 0, height, PI/2, -PI/2));
  noStroke();
  fill(255);
  render.drawFaces(mesh);
  noFill();
  strokeWeight(1);
  stroke(0);
  render.drawEdges(mesh);
  HE_VertexIterator vItr=mesh.vItr(); 
  HE_Vertex v;
  stroke(255,0,0);
  while(vItr.hasNext()){
   v=vItr.next();
    WB_Coord n=v.getVertexNormal();
    render.drawVector(v,n,50.0);
    
  }
  
  
}