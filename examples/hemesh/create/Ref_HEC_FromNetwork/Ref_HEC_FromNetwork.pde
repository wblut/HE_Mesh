import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;


HE_Mesh mesh;
WB_Render render;

void setup() {
  size(1000,1000,P3D);
  smooth(8);
  // Creates a mesh from a frame. A WB_Frame is a collection of points and a list of 
  // indexed connections.

  //Array of all points
  float[][] vertices=new float[1681][3];
  int index = 0;
  for (int j = 0; j < 41; j++) {
    for (int i = 0; i < 41; i++) {
      vertices[index][0] =-400+ i * 20;
      vertices[index][1] =-400+j * 20;
      vertices[index][2] = sin(TWO_PI/30*i)*40+cos(TWO_PI/25*j)*40;
      index++;
    }
  }

  // WB_Network network=new WB_Network(vertices);
 

  //For more control add the nodes one by one, a value can be given to each node for future use.
  WB_Network network=new WB_Network();
  index=0;
  for (int j = 0; j < 41; j++) {
    for (int i = 0; i < 41; i++) {
       network.addNode(vertices[index][0],vertices[index][1],vertices[index][2],noise(0.1*i,0.1*j));
       index++;
    }
  }
      
  //adding random connections to the frame
  for (int j = 0; j < 40; j++) {
    for (int i = 0; i < 40; i++) {
      if (random(100)>30) network.addConnection(i+41*j, i+1+41*j);
      if (random(100)>30) network.addConnection(i+41*j, i+41*(j+1));
    }
  }
  // If the connections are known in advance these can be given as parameter as int[][]. The
  // second index gives index of first and second point of connection in the points array.
  // network=new WB_Network(vertices, connections);
  

  HEC_FromNetwork creator=new HEC_FromNetwork();
  creator.setNetwork(network);
  //alternatively you can specify a HE_Mesh instead of a WB_Network.
  creator.setConnectionRadius(6);// connection radius
  creator.setConnectionFacets(6);// number of faces in the connections, min 3, max whatever blows up the CPU
  creator.setAngleOffset(0.25);// rotate the connections by a fraction of a facet. 0 is no rotation, 1 is a rotation over a full facet. More noticeable for low number of facets.
  creator.setMinimumBalljointAngle(TWO_PI/3.0);//Threshold angle to include sphere in joint.
  creator.setMaximumConnectionLength(30);//divide connection into equal parts if larger than maximum length.
  creator.setCap(true); //cap open endpoints of connections?
  creator.setTaper(true);// allow connections to have different radii at each end?
  creator.setCreateIsolatedNodes(false);// create spheres for isolated points?
  creator.setUseNodeValues(true);// use the value of the WB_Node as scaling factor, only useful if the frame was created using addNode().
  mesh=new HE_Mesh(creator);
  mesh.stats();
mesh.validate();
  render=new WB_Render(this);
}

void draw() {
  background(55);
  lights();
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  noStroke();
  render.drawFaces(mesh);
  stroke(0);
  render.drawEdges(mesh);
}