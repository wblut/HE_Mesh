import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;
import java.util.*;

HE_Mesh mesh;
ArrayList<HE_Mesh> meshes;
WB_Render render;
HE_Vertex[][] environments;
void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  mesh=new HE_Mesh(new HEC_Geodesic().setB(5).setC(3).setRadius(300));
  meshes=new ArrayList<HE_Mesh>();
  meshes.add(new HE_Mesh(new HEC_Geodesic().setB(5).setC(3).setRadius(300)).modify(new HEM_Slice().setPlane(0, 0, 0, 0, 0, 1)));

  collectNeighbors();
  for (int i=0; i<5; i++) {
    for (int r=0; r<20; r++) average();
    meshes.add(mesh.get().modify(new HEM_Slice().setPlane(0, 0, 0, 0, 0, (i%2)==0?-1:1)));
  }
  mesh.subdivide(new HES_CatmullClark(), 2);
    render=new WB_Render(this);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2, 0);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  for (int i=0; i<meshes.size(); i++) {
    stroke(0,200);
    render.drawEdges(meshes.get(i));
  }
  noStroke();
  render.drawFacesSmooth(mesh);
}

void collectNeighbors() {
  int nv=mesh.getNumberOfVertices();
  environments=new HE_Vertex[nv][];
  HE_Vertex[] vertices=mesh.getVerticesAsArray();
  Iterator<HE_Vertex> vItr=mesh.vItr();
  int i=0;
  while (vItr.hasNext ()) {
    if (random(100)<2) {
      environments[i]=new HE_Vertex[1];
      environments[i][0]=vItr.next();
    } else if (random(100)<8) {
      List<HE_Vertex> points=vItr.next().getNeighborVertices();
      environments[i]= new HE_Vertex[points.size()+1];
      for (int j=0; j<points.size(); j++) {
        environments[i][j]=points.get(j);
      }
      environments[i][points.size()]=vertices[(int)random(nv)];
    } else {
      List<HE_Vertex> points=vItr.next().getNeighborVertices();
      environments[i]= new HE_Vertex[points.size()];
      for (int j=0; j<points.size(); j++) {
        environments[i][j]=points.get(j);
      }
    }

    i++;
  }
}

void mousePressed() {
  mesh=new HE_Mesh(new HEC_Geodesic().setB(5).setC(3).setRadius(300));
  meshes=new ArrayList<HE_Mesh>();
  meshes.add(new HE_Mesh(new HEC_Geodesic().setB(5).setC(3).setRadius(300)).modify(new HEM_Slice().setPlane(0, 0, 0, 0, 0, 1)));

  collectNeighbors();
  for (int i=0; i<5; i++) {
    for (int r=0; r<20; r++) average();
    meshes.add(mesh.get().modify(new HEM_Slice().setPlane(0, 0, 0, 0, 0, (i%2)==0?-1:1)));
  }
  mesh.subdivide(new HES_CatmullClark(), 2);
}

void keyPressed() {
  saveFrame("collapse.png");
}

void average() {
  int nv=mesh.getNumberOfVertices();
  WB_Point[] newPos=new WB_Point[nv];
  for (int i=0; i<nv; i++) {
    int nn=environments[i].length;
    newPos[i]=new WB_Point();
    for (int j=0; j<nn; j++) {
      newPos[i].addSelf(environments[i][j]);
    }
    newPos[i].mulSelf(1.0/nn);
  }  

  Iterator<HE_Vertex> vItr=mesh.vItr();
  vItr=mesh.vItr();  
  int i=0;
  while (vItr.hasNext ()) {
    vItr.next().set(newPos[i]); 
    i++;
  }
}