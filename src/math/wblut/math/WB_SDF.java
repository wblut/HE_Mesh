package wblut.math;

import wblut.geom.WB_Coord;
import wblut.geom.WB_Vector;

////////////////////////////////////////////////////////////////
//
//HG_SDF
//
//GLSL LIBRARY FOR BUILDING SIGNED DISTANCE BOUNDS
//
//version 2016-01-10
//
//Check http://mercury.sexy/hg_sdf for updates
//and usage examples. Send feedback to spheretracing@mercury.sexy.
//
//Brought to you by MERCURY http://mercury.sexy
//
//
//
//Released as Creative Commons Attribution-NonCommercial (CC BY-NC)
//
////////////////////////////////////////////////////////////////
//
//How to use this:
//
//1. Build some system to #include glsl files in each other.
//Include this one at the very start. Or just paste everywhere.
//2. Build a sphere tracer. See those papers:
//* "Sphere Tracing" http://graphics.cs.illinois.edu/sites/default/files/zeno.pdf
//* "Enhanced Sphere Tracing" http://lgdv.cs.fau.de/get/2234
//The Raymnarching Toolbox Thread on pouet can be helpful as well
//http://www.pouet.net/topic.php?which=7931&page=1
//and contains links to many more resources.
//3. Use the tools in this library to build your distance bound f().
//4. ???
//5. Win a compo.
//
//(6. Buy us a beer or a good vodka or something, if you like.)
//
////////////////////////////////////////////////////////////////
//
//Table of Contents:
//
//* Helper functions and macros
//* Collection of some primitive objects
//* Domain Manipulation operators
//* Object combination operators
//
////////////////////////////////////////////////////////////////
//
//Why use this?
//
//The point of this lib is that everything is structured according
//to patterns that we ended up using when building geometry.
//It makes it more easy to write code that is reusable and that somebody
//else can actually understand. Especially code on Shadertoy (which seems
//to be what everybody else is looking at for "inspiration") tends to be
//really ugly. So we were forced to do something about the situation and
//release this lib ;)
//
//Everything in here can probably be done in some better way.
//Please experiment. We'd love some feedback, especially if you
//use it in a scene production.
//
//The main patterns for building geometry this way are:
//* Stay Lipschitz continuous. That means: don't have any distance
//gradient larger than 1. Try to be as close to 1 as possible -
//Distances are euclidean distances, don't fudge around.
//Underestimating distances will happen. That's why calling
//it a "distance bound" is more correct. Don't ever multiply
//distances by some value to "fix" a Lipschitz continuity
//violation. The invariant is: each fSomething() function returns
//a correct distance bound.
//* Use very few primitives and combine them as building blocks
//using combine opertors that preserve the invariant.
//* Multiply objects by repeating the domain (space).
//If you are using a loop inside your distance function, you are
//probably doing it wrong (or you are building boring fractals).
//* At right-angle intersections between objects, build a new local
//coordinate system from the two distances to combine them in
//interesting ways.
//* As usual, there are always times when it is best to not follow
//specific patterns.
//
////////////////////////////////////////////////////////////////
//
//FAQ
//
//Q: Why is there no sphere tracing code in this lib?
//A: Because our system is way too complex and always changing.
//This is the constant part. Also we'd like everyone to
//explore for themselves.
//
//Q: This does not work when I paste it into Shadertoy!!!!
//A: Yes. It is GLSL, not GLSL ES. We like real OpenGL
//because it has way more features and is more likely
//to work compared to browser-based WebGL. We recommend
//you consider using OpenGL for your productions. Most
//of this can be ported easily though.
//
//Q: How do I material?
//A: We recommend something like this:
//Write a material ID, the distance and the local coordinate
//p into some global variables whenever an object's distance is
//smaller than the stored distance. Then, at the end, evaluate
//the material to get color, roughness, etc., and do the shading.
//
//Q: I found an error. Or I made some function that would fit in
//in this lib. Or I have some suggestion.
//A: Awesome! Drop us a mail at spheretracing@mercury.sexy.
//
//Q: Why is this not on github?
//A: Because we were too lazy. If we get bugged about it enough,
//we'll do it.
//
//Q: Your license sucks for me.
//A: Oh. What should we change it to?
//
//Q: I have trouble understanding what is going on with my distances.
//A: Some visualization of the distance field helps. Try drawing a
//plane that you can sweep through your scene with some color
//representation of the distance field at each point and/or iso
//lines at regular intervals. Visualizing the length of the
//gradient (or better: how much it deviates from being equal to 1)
//is immensely helpful for understanding which parts of the
//distance field are broken.
//
////////////////////////////////////////////////////////////////

public class WB_SDF {

	////////////////////////////////////////////////////////////////
	//
	// HELPER FUNCTIONS/MACROS
	//
	////////////////////////////////////////////////////////////////

	public static final double PI = Math.PI;
	public static final double TAU = 2 * 2 * PI;
	public static final double PHI = Math.sqrt(5) * 0.5 + 0.5;

	// Clamp to [0,1] - this operation is free under certain circumstances.
	// For further information see
	// http://www.humus.name/Articles/Persson_LowLevelThinking.pdf and
	// http://www.humus.name/Articles/Persson_LowlevelShaderOptimization.pdf
	static double saturate(final double x) {
		return clamp(x, 0, 1);
	}

	static double clamp(final double x, final double min, final double max) {
		return x < min ? min : x > max ? max : x;
	}

	static double sqrt(final double x) {
		return Math.sqrt(x);
	}

	static double length3D(final WB_Coord v) {
		return Math.sqrt(v.xd() * v.xd() + v.yd() * v.yd() + v.zd() * v.zd());
	}

	static double length2D(final WB_Coord v) {
		return Math.sqrt(v.xd() * v.xd() + v.yd() * v.yd());
	}

	static double abs(final double x) {
		return Math.abs(x);
	}

	static WB_Vector abs(final WB_Coord v) {
		return new WB_Vector(abs(v.xd()), abs(v.yd()), abs(v.zd()));

	}

	static double step(final double edge, final double x) {
		return x < edge ? 0.0 : 1.0;

	}

	static double mix(final double x, final double y, final double a) {
		return (1.0 - a) * x + a * y;

	}

	static WB_Vector normalize(final WB_Coord v) {
		WB_Vector result = new WB_Vector(v);
		result.normalizeSelf();
		return result;
	}

	static double dot3D(final WB_Coord v, final WB_Coord w) {
		return WB_Vector.dot(v, w);

	}

	static double dot2D(final WB_Coord v, final WB_Coord w) {
		return WB_Vector.dot2D(v, w);

	}

	// Sign function that doesn't return 0
	static double sgn(final double x) {
		return x < 0 ? -1 : 1;
	}

	static WB_Vector sgn2D(final WB_Coord v) {
		return new WB_Vector(v.xd() < 0 ? -1 : 1, v.yd() < 0 ? -1 : 1);
	}

	static double square(final double x) {
		return x * x;
	}

	static WB_Vector square2D(final WB_Coord v) {
		return new WB_Vector(v.xd() * v.xd(), v.yd() * v.yd());
	}

	static WB_Vector square3D(final WB_Coord v) {
		return new WB_Vector(v.xd() * v.xd(), v.yd() * v.yd(), v.zd() * v.zd());
	}

	// Maximum/minumum elements of a vector

	static double max(final double a, final double b) {
		return b > a ? b : a;
	}

	static double min(final double a, final double b) {
		return b < a ? b : a;
	}

	static WB_Vector max3D(final WB_Coord v, final WB_Coord w) {
		return new WB_Vector(max(v.xd(), w.xd()), max(v.yd(), w.yd()), max(v.zd(), w.zd()));
	}

	static WB_Vector min3D(final WB_Coord v, final WB_Coord w) {
		return new WB_Vector(min(v.xd(), w.xd()), min(v.yd(), w.yd()), min(v.zd(), w.zd()));
	}

	static WB_Vector max2D(final WB_Coord v, final WB_Coord w) {
		return new WB_Vector(max(v.xd(), w.xd()), max(v.yd(), w.yd()));
	}

	static WB_Vector min2D(final WB_Coord v, final WB_Coord w) {
		return new WB_Vector(min(v.xd(), w.xd()), min(v.yd(), w.yd()));
	}

	static WB_Vector max3D(final WB_Coord v, final double x) {
		return new WB_Vector(max(v.xd(), x), max(v.yd(), x), max(v.zd(), x));
	}

	static WB_Vector min3D(final WB_Coord v, final double x) {
		return new WB_Vector(min(v.xd(), x), min(v.yd(), x), min(v.zd(), x));
	}

	static WB_Vector max2D(final WB_Coord v, final double x) {
		return new WB_Vector(max(v.xd(), x), max(v.yd(), x));
	}

	static WB_Vector min2D(final WB_Coord v, final double x) {
		return new WB_Vector(min(v.xd(), x), min(v.yd(), x));
	}

	static double vmax2D(final WB_Coord v) {
		return max(v.xd(), v.yd());
	}

	static double vmax3D(final WB_Coord v) {
		return max(max(v.xd(), v.yd()), v.zd());
	}

	static double vmin2D(final WB_Coord v) {
		return min(v.xd(), v.yd());
	}

	static double vmin3D(final WB_Coord v) {
		return min(min(v.xd(), v.yd()), v.zd());
	}

	////////////////////////////////////////////////////////////////
	//
	// PRIMITIVE DISTANCE FUNCTIONS
	//
	////////////////////////////////////////////////////////////////
	//
	// Conventions:
	//
	// Everything that is a distance function is called fSomething.
	// The first argument is always a point in 2 or 3-space called <p>.
	// Unless otherwise noted, (if the object has an intrinsic "up"
	// side or direction) the y axis is "up" and the object is
	// centered at the origin.
	//
	////////////////////////////////////////////////////////////////

	public static double fSphere(final WB_Coord p, final double r) {
		return WB_Vector.getLength3D(p) - r;
	}

	// Plane with normal n (n is normalized) at some distance from the origin
	public static double fPlane(final WB_Coord p, final WB_Coord n, final double distanceFromOrigin) {
		return WB_Vector.dot(p, n) + distanceFromOrigin;
	}

	// Cheap Box: distance to corners is overestimated
	public static double fBoxCheap(final WB_Coord p, final WB_Coord b) { // cheap
																			// box
		return vmax3D(WB_Vector.sub(abs(p), b));
	}

	// Box: correct distance to corners
	public static double fBox(final WB_Coord p, final WB_Coord b) {
		WB_Vector d = WB_Vector.sub(abs(p), b);
		return length3D(max3D(d, new WB_Vector(0, 0, 0))) + vmax3D(min3D(d, new WB_Vector(0, 0, 0)));
	}

	// Same as above, but in two dimensions (an endless box)
	public static double fBox2Cheap(final WB_Coord p, final WB_Coord b) {
		return vmax2D(WB_Vector.sub(abs(p), b));
	}

	public static double fBox2(final WB_Coord p, final WB_Coord b) {
		WB_Vector d = WB_Vector.sub(abs(p), b);
		return length2D(max2D(d, new WB_Vector(0, 0))) + vmax2D(min2D(d, new WB_Vector(0, 0)));
	}

	// Endless "corner"
	public static double fCorner(final WB_Coord p) {
		return length2D(max2D(p, new WB_Vector(0, 0))) + vmax2D(min2D(p, new WB_Vector(0, 0)));
	}

	// Blobby ball object. You've probably seen it somewhere. This is not a
	// correct distance bound, beware.
	public static double fBlob(final WB_Coord q) {
		WB_Vector p = new WB_Vector(abs(q));
		if (p.xd() < max(p.yd(), p.zd())) {
			p.set(p.yd(), p.zd(), p.xd());
		}
		if (p.xd() < max(p.yd(), p.zd())) {
			p.set(p.yd(), p.zd(), p.xd());
		}
		double b = max(
				max(max(dot3D(p, normalize(new WB_Vector(1, 1, 1))),
						dot2D(new WB_Vector(p.xd(), p.zd()), normalize(new WB_Vector(PHI + 1, 1)))),
						dot2D(new WB_Vector(p.yd(), p.xd()), normalize(new WB_Vector(1, PHI)))),
				dot2D(new WB_Vector(p.xd(), p.zd()), normalize(new WB_Vector(1, PHI))));
		double l = length3D(p);
		return l - 1.5 - 0.2 * (1.5 / 2) * Math.cos(min(sqrt(1.01 - b / l) * (PI / 0.25), PI));
	}

	// Cylinder standing upright on the xz plane
	public static double fCylinder(final WB_Coord p, final double r, final double height) {
		double d = length2D(new WB_Vector(p.xd(), p.zd())) - r;
		d = max(d, abs(p.yd()) - height);
		return d;
	}

	// Capsule: A Cylinder with round caps on both sides
	public static double fCapsule(final WB_Coord p, final double r, final double c) {
		return mix(length2D(new WB_Vector(p.xd(), p.zd())) - r,
				length3D(new WB_Vector(p.xd(), abs(p.yd()) - c, p.zd())) - r, step(c, abs(p.yd())));
	}

	// Distance to line segment between <a> and <b>, used for fCapsule() version
	// 2below
	public static double fLineSegment(final WB_Coord p, final WB_Coord a, final WB_Coord b) {
		WB_Vector ab = WB_Vector.sub(b, a);
		double t = saturate(dot3D(WB_Vector.sub(p, a), ab) / dot3D(ab, ab));
		return length3D(ab.mulSelf(t).addSelf(a).subSelf(p));
	}

	// Capsule version 2: between two end points <a> and <b> with radius r
	public static double fCapsule(final WB_Coord p, final WB_Coord a, final WB_Coord b, final double r) {
		return fLineSegment(p, a, b) - r;
	}

	// Torus in the XZ-plane
	public static double fTorus(final WB_Coord p, final double smallRadius, final double largeRadius) {
		return length2D(new WB_Vector(length2D(new WB_Vector(p.xd(), p.zd())) - largeRadius, p.yd())) - smallRadius;
	}

	// A circle line. Can also be used to make a torus by subtracting the
	// smaller radius of the torus.
	public static double fCircle(final WB_Coord p, final double r) {
		double l = length2D(new WB_Vector(p.xd(), p.zd())) - r;
		return length2D(new WB_Vector(p.yd(), l));
	}

	// A circular disc with no thickness (i.e. a cylinder with no height).
	// Subtract some value to make a flat disc with rounded edge.
	public static double fDisc(final WB_Coord p, final double r) {
		double l = length2D(new WB_Vector(p.xd(), p.zd())) - r;
		return l < 0 ? abs(p.yd()) : length2D(new WB_Vector(p.yd(), l));
	}

	// Hexagonal prism, circumcircle variant
	public static double fHexagonCircumcircle(final WB_Coord p, final WB_Coord h) {
		WB_Coord q = abs(p);
		return max(q.yd() - h.yd(), max(q.xd() * sqrt(3) * 0.5 + q.zd() * 0.5, q.zd()) - h.xd());
		// this is mathematically equivalent to this line, but less efficient:
		// return max(q.y - h.y, max(dot(new WB_Vector(cos(PI/3), sin(PI/3)),
		// q.zx), q.z)
		// - h.x);
	}

	// Hexagonal prism, incircle variant
	public static double fHexagonIncircle(final WB_Coord p, final WB_Coord h) {
		return fHexagonCircumcircle(p, new WB_Vector(h.xd() * sqrt(3) * 0.5, h.yd()));
	}

	// Cone with correct distances to tip and base circle. Y is up, 0 is in the
	// middle of the base.
	public static double fCone(final WB_Coord p, final double radius, final double height) {
		WB_Vector q = new WB_Vector(length2D(new WB_Vector(p.xd(), p.zd())), p.yd());
		WB_Vector tip = q.sub(new WB_Vector(0, height));
		WB_Vector mantleDir = normalize(new WB_Vector(height, radius));
		double mantle = dot2D(tip, mantleDir);
		double d = max(mantle, -q.yd());
		double projected = dot2D(tip, new WB_Vector(mantleDir.yd(), -mantleDir.xd()));

		// distance to tip
		if (q.yd() > height && projected < 0) {
			d = max(d, length2D(tip));
		}

		// distance to base ring
		if (q.xd() > radius && projected > length2D(new WB_Vector(height, radius))) {
			d = max(d, length2D(q.subSelf(new WB_Vector(radius, 0))));
		}
		return d;
	}

	//
	// "Generalized Distance Functions" by Akleman and Chen.
	// see the Paper at
	// https://www.viz.tamu.edu/faculty/ergun/research/implicitmodeling/papers/sm99.pdf
	//
	// This set of constants is used to construct a large variety of geometric
	// primitives.
	// Indices are shifted by 1 compared to the paper because we start counting
	// at Zero.
	// Some of those are slow whenever a driver decides to not unroll the loop,
	// which seems to happen for fIcosahedron und fTruncatedIcosahedron on
	// nvidia 350.12 at least.
	// Specialized implementations can well be faster in all cases.
	//

	public final static WB_Vector[] GDFVectors = new WB_Vector[] {

			normalize(new WB_Vector(1, 0, 0)), normalize(new WB_Vector(0, 1, 0)), normalize(new WB_Vector(0, 0, 1)),

			normalize(new WB_Vector(1, 1, 1)), normalize(new WB_Vector(-1, 1, 1)), normalize(new WB_Vector(1, -1, 1)),
			normalize(new WB_Vector(1, 1, -1)),

			normalize(new WB_Vector(0, 1, PHI + 1)), normalize(new WB_Vector(0, -1, PHI + 1)),
			normalize(new WB_Vector(PHI + 1, 0, 1)), normalize(new WB_Vector(-PHI - 1, 0, 1)),
			normalize(new WB_Vector(1, PHI + 1, 0)), normalize(new WB_Vector(-1, PHI + 1, 0)),

			normalize(new WB_Vector(0, PHI, 1)), normalize(new WB_Vector(0, -PHI, 1)),
			normalize(new WB_Vector(1, 0, PHI)), normalize(new WB_Vector(-1, 0, PHI)),
			normalize(new WB_Vector(PHI, 1, 0)), normalize(new WB_Vector(-PHI, 1, 0)) };

	// Version with variable exponent.
	// This is slow and does not produce correct distances, but allows for
	// bulging of objects.
	public static double fGDF(final WB_Coord p, final double r, final double e, final int begin, final int end) {
		double d = 0;
		for (int i = begin; i <= end; ++i) {
			d += Math.pow(abs(dot3D(p, GDFVectors[i])), e);
		}
		return Math.pow(d, 1.0 / e) - r;
	}

	// Version with without exponent, creates objects with sharp edges and flat
	// faces
	public static double fGDF(final WB_Coord p, final double r, final int begin, final int end) {
		double d = 0;
		for (int i = begin; i <= end; ++i) {
			d = max(d, abs(dot3D(p, GDFVectors[i])));
		}
		return d - r;
	}

	// Primitives follow:

	public static double fOctahedron(final WB_Coord p, final double r, final double e) {
		return fGDF(p, r, e, 3, 6);
	}

	public static double fDodecahedron(final WB_Coord p, final double r, final double e) {
		return fGDF(p, r, e, 13, 18);
	}

	public static double fIcosahedron(final WB_Coord p, final double r, final double e) {
		return fGDF(p, r, e, 3, 12);
	}

	public static double fTruncatedOctahedron(final WB_Coord p, final double r, final double e) {
		return fGDF(p, r, e, 0, 6);
	}

	public static double fTruncatedIcosahedron(final WB_Coord p, final double r, final double e) {
		return fGDF(p, r, e, 3, 18);
	}

	public static double fOctahedron(final WB_Coord p, final double r) {
		return fGDF(p, r, 3, 6);
	}

	public static double fDodecahedron(final WB_Coord p, final double r) {
		return fGDF(p, r, 13, 18);
	}

	public static double fIcosahedron(final WB_Coord p, final double r) {
		return fGDF(p, r, 3, 12);
	}

	public static double fTruncatedOctahedron(final WB_Coord p, final double r) {
		return fGDF(p, r, 0, 6);
	}

	public static double fTruncatedIcosahedron(final WB_Coord p, final double r) {
		return fGDF(p, r, 3, 18);
	}

	// Shortcut for 45-degrees rotation
	public static void pR45(final WB_Vector p) {
		p.addSelf(p.yd(), -p.xd());
		p.mulSelf(sqrt(0.5));
	}

	// Repeat space along one axis. Use like this to repeat along the x axis:
	// <double cell = pMod1(p.xd(),5);> - using the return value is optional.
	public static double pMod1(double p, final double size) {
		double halfsize = size * 0.5;
		// double c = floor((p + halfsize)/size);
		p = (p + halfsize) % size - halfsize;
		return p;
	}

	////////////////////////////////////////////////////////////////
	//
	// OBJECT COMBINATION OPERATORS
	//
	////////////////////////////////////////////////////////////////
	//
	// We usually need the following boolean operators to combine two objects:
	// Union: OR(a,b)
	// Intersection: AND(a,b)
	// Difference: AND(a,!b)
	// (a and b being the distances to the objects).
	//
	// The trivial implementations are min(a,b) for union, max(a,b) for
	//////////////////////////////////////////////////////////////// intersection
	// and max(a,-b) for difference. To combine objects in more interesting ways
	//////////////////////////////////////////////////////////////// to
	// produce rounded edges, chamfers, stairs, etc. instead of plain sharp
	//////////////////////////////////////////////////////////////// edges we
	// can use combination operators. It is common to use some kind of "smooth
	//////////////////////////////////////////////////////////////// minimum"
	// instead of min(), but we don't like that because it does not preserve
	//////////////////////////////////////////////////////////////// Lipschitz
	// continuity in many cases.
	//
	// Naming convention: since they return a distance, they are called
	//////////////////////////////////////////////////////////////// fOpSomething.
	// The different flavours usually implement all the boolean operators above
	// and are called fOpUnionRound, fOpIntersectionRound, etc.
	//
	// The basic idea: Assume the object surfaces intersect at a right angle.
	//////////////////////////////////////////////////////////////// The two
	// distances <a> and <b> constitute a new local two-dimensional coordinate
	//////////////////////////////////////////////////////////////// system
	// with the actual intersection as the origin. In this coordinate system, we
	//////////////////////////////////////////////////////////////// can
	// evaluate any 2D distance function we want in order to shape the edge.
	//
	// The operators below are just those that we found useful or interesting
	//////////////////////////////////////////////////////////////// and should
	// be seen as examples. There are infinitely more possible operators.
	//
	// They are designed to actually produce correct distances or distance
	//////////////////////////////////////////////////////////////// bounds,
	//////////////////////////////////////////////////////////////// unlike
	// popular "smooth minimum" operators, on the condition that the gradients
	//////////////////////////////////////////////////////////////// of the two
	// SDFs are at right angles. When they are off by more than 30 degrees or
	//////////////////////////////////////////////////////////////// so, the
	// Lipschitz condition will no longer hold (i.e. you might get artifacts).
	//////////////////////////////////////////////////////////////// The worst
	// case is parallel surfaces that are close to each other.
	//
	// Most have a double argument <r> to specify the radius of the feature they
	//////////////////////////////////////////////////////////////// represent.
	// This should be much smaller than the object size.
	//
	// Some of them have checks like "if ((-a < r) && (-b < r))" that restrict
	// their influence (and computation cost) to a certain area. You might
	// want to lift that restriction or enforce it. We have left it as comments
	// in some cases.
	//
	// usage example:
	//
	// double fTwoBoxes(vec3 p) {
	// double box0 = fBox(p, vec3(1));
	// double box1 = fBox(p-vec3(1), vec3(1));
	// return fOpUnionChamfer(box0, box1, 0.2);
	// }
	//
	////////////////////////////////////////////////////////////////

	// The "Chamfer" flavour makes a 45-degree chamfered edge (the diagonal of a
	// square of size <r>):
	public static double fOpUnionChamfer(final double a, final double b, final double r) {
		return min(min(a, b), (a - r + b) * sqrt(0.5));
	}

	// Intersection has to deal with what is normally the inside of the
	// resulting object
	// when using union, which we normally don't care about too much. Thus,
	// intersection
	// implementations sometimes differ from union implementations.
	public static double fOpIntersectionChamfer(final double a, final double b, final double r) {
		return max(max(a, b), (a + r + b) * sqrt(0.5));
	}

	// Difference can be built from Intersection or Union:
	public static double fOpDifferenceChamfer(final double a, final double b, final double r) {
		return fOpIntersectionChamfer(a, -b, r);
	}

	// The "Round" variant uses a quarter-circle to join the two objects
	// smoothly:
	public static double fOpUnionRound(final double a, final double b, final double r) {
		WB_Vector u = max2D(new WB_Vector(r - a, r - b), new WB_Vector(0, 0));
		return max(r, min(a, b)) - length2D(u);
	}

	public static double fOpIntersectionRound(final double a, final double b, final double r) {
		WB_Vector u = max2D(new WB_Vector(r + a, r + b), new WB_Vector(0, 0));
		return min(-r, max(a, b)) + length2D(u);
	}

	public static double fOpDifferenceRound(final double a, final double b, final double r) {
		return fOpIntersectionRound(a, -b, r);
	}

	// The "Columns" flavour makes n-1 circular columns at a 45 degree angle:
	public static double fOpUnionColumns(final double a, final double b, final double r, final double n) {
		if (a < r && b < r) {
			WB_Vector p = new WB_Vector(a, b);
			double columnradius = r * sqrt(2) / ((n - 1) * 2 + sqrt(2));
			pR45(p);
			p.subSelf(sqrt(2) / 2 * r, 0, 0);
			p.addSelf(columnradius * sqrt(2), columnradius * sqrt(2), columnradius * sqrt(2));
			if (n % 2 == 1) {
				p.addSelf(0, columnradius, 0);
			}
			// At this point, we have turned 45 degrees and moved at a point on
			// the
			// diagonal that we want to place the columns on.
			// Now, repeat the domain along this direction and place a circle.
			p.setY(pMod1(p.yd(), columnradius * 2));
			double result = length2D(p) - columnradius;
			result = min(result, p.xd());
			result = min(result, a);
			return min(result, b);
		} else {
			return min(a, b);
		}
	}

	public static double fOpDifferenceColumns(double a, final double b, final double r, final double n) {
		a = -a;
		double m = min(a, b);
		// avoid the expensive computation where not needed (produces
		// discontinuity though)
		if (a < r && b < r) {
			WB_Vector p = new WB_Vector(a, b);
			double columnradius = r * sqrt(2) / n / 2.0;
			columnradius = r * sqrt(2) / ((n - 1) * 2 + sqrt(2));

			pR45(p);
			p.addSelf(0, columnradius);
			p.subSelf(sqrt(2) / 2 * r);
			p.addSelf(-columnradius * sqrt(2) / 2);

			if (n % 2 == 1) {
				p.addSelf(0, columnradius);
			}
			p.setY(pMod1(p.yd(), columnradius * 2));

			double result = -length2D(p) + columnradius;
			result = max(result, p.xd());
			result = min(result, a);
			return -min(result, b);
		} else {
			return -m;
		}
	}

	public static double fOpIntersectionColumns(final double a, final double b, final double r, final double n) {
		return fOpDifferenceColumns(a, -b, r, n);
	}

	// The "Stairs" flavour produces n-1 steps of a staircase:
	// much less stupid version by paniq
	public static double fOpUnionStairs(final double a, final double b, final double r, final double n) {
		double s = r / n;
		double u = b - r;
		return min(min(a, b), 0.5 * (u + a + abs((u - a + s) % (2 * s) - s)));
	}

	// We can just call Union since stairs are symmetric.
	public static double fOpIntersectionStairs(final double a, final double b, final double r, final double n) {
		return -fOpUnionStairs(-a, -b, r, n);
	}

	public static double fOpDifferenceStairs(final double a, final double b, final double r, final double n) {
		return -fOpUnionStairs(-a, b, r, n);
	}

	// Similar to fOpUnionRound, but more lipschitz-y at acute angles
	// (and less so at 90 degrees). Useful when fudging around too much
	// by MediaMolecule, from Alex Evans' siggraph slides
	public static double fOpUnionSoft(final double a, final double b, final double r) {
		double e = max(r - abs(a - b), 0);
		return min(a, b) - e * e * 0.25 / r;
	}

	// produces a cylindical pipe that runs along the intersection.
	// No objects remain, only the pipe. This is not a boolean operator.
	public static double fOpPipe(final double a, final double b, final double r) {
		return length2D(new WB_Vector(a, b)) - r;
	}

	// first object gets a v-shaped engraving where it intersect the second
	public static double fOpEngrave(final double a, final double b, final double r) {
		return max(a, (a + r - abs(b)) * sqrt(0.5));
	}

	// first object gets a carpenter-style groove cut out
	public static double fOpGroove(final double a, final double b, final double ra, final double rb) {
		return max(a, min(a + ra, rb - abs(b)));
	}

	// first object gets a carpenter-style tongue attached
	public static double fOpTongue(final double a, final double b, final double ra, final double rb) {
		return min(a, max(a - ra, abs(b) - rb));
	}
}
