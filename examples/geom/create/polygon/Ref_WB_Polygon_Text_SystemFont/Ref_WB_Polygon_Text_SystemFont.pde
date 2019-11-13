import wblut.processing.*;
import wblut.hemesh.*;
import wblut.geom.*;
import wblut.math.*;
import java.util.List;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

WB_GeometryFactory gf=new WB_GeometryFactory();
WB_Render2D render;
List<WB_Polygon> text;
String[] fontnames;
String fontname;
WB_AABB AABB;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  render=new WB_Render2D(this);
  
  //Use this to get all fontnames on your system.
  GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
    fontnames = e.getAvailableFontFamilyNames();
    for (String s : fontnames) {
      System.out.println(s);
  }
  fontname=fontnames[(int)random(fontnames.length)];
  text=gf.createText("Hemesh doesn't do text!", fontname,0, 48.0,0.4);//text; fontname; style: REGULAR:0, BOLD:1, ITALIC:2, BOLD-ITALIC:3 ; font size; flatness
  
  createAABB();
  textAlign(CENTER); // no effect on HE_Mesh
  background(55);
}

void draw() {
  background(55);
  translate(width/2, height/2);
  fill(0);
  text("click" , 0,350);
   text("Some fonts don't work very well... Current: "+fontname , 0,370);
  scale(1, -1);
  fill(255, 0, 0);
  translate(-AABB.getCenter().xf(), -AABB.getCenter().yf());
  for (WB_Polygon poly : text) {
    render.drawPolygon2D(poly);
  }
  
}


void createAABB() {
  AABB=new WB_AABB();
  for (WB_Polygon poly : text) {
    for (int i=0; i<poly.getNumberOfShellPoints(); i++) {
      AABB.expandToInclude(poly.getPoint(i));
    }
  }
}

void mousePressed(){
  fontname=fontnames[(int)random(fontnames.length)];
  text=gf.createText("Hemesh doesn't do text!", fontname,0, 48.0,0.4);
   createAABB();
}