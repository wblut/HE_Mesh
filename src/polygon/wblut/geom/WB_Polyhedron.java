/**
 *
 */
package wblut.geom;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;

/**
 * @author FVH
 *
 */
public class WB_Polyhedron implements WB_Geometry {
	private WB_Point[]	vertices;
	private int[][]		faces;
	private String		name;
	double				scale;

	public WB_Polyhedron() {
		vertices = new WB_Point[0];
		faces = new int[0][0];
		name = "";
		scale = 1.0;
	}

	public WB_Polyhedron(final WB_Polyhedron polyhedron) {
		vertices = new WB_Point[polyhedron.vertices.length];
		for (int i = 0; i < polyhedron.vertices.length; i++) {
			vertices[i] = new WB_Point(polyhedron.vertices[i]);
		}
		faces = new int[polyhedron.faces.length][];
		for (int i = 0; i < polyhedron.faces.length; i++) {
			faces[i] = new int[polyhedron.faces[i].length];
			for (int j = 0; j < polyhedron.faces[i].length; j++) {
				faces[i][j] = polyhedron.faces[i][j];
			}
		}
		name = polyhedron.name;
		scale = polyhedron.scale;
	}

	public void setScale(final double s) {
		scale = s;
	}

	public double getScale() {
		return scale;
	}

	/**
	 * @return the vertices
	 */
	public WB_Coord[] getVertices() {
		return vertices;
	}

	public WB_Coord getVertex(final int i) {
		return vertices[i];
	}

	/**
	 * @return the faces
	 */
	public int[][] getFaces() {
		return faces;
	}

	public int[] getFace(final int i) {
		return faces[i];
	}

	public int getNumberOfVertices() {
		return vertices.length;
	}

	public int getNumberOfFaces() {
		return faces.length;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public void tetrahedron() {
		name = "T";
		faces = new int[][] { { 0, 1, 2 }, { 0, 2, 3 }, { 0, 3, 1 },
				{ 1, 3, 2 } };
		vertices = new WB_Point[] { new WB_Point(1.0, 1.0, 1.0),
				new WB_Point(1.0, -1.0, -1.0), new WB_Point(-1.0, 1.0, -1.0),
				new WB_Point(-1.0, -1.0, 1.0) };
	}

	public void octahedron() {
		name = "O";
		faces = new int[][] { { 0, 1, 2 }, { 0, 2, 3 }, { 0, 3, 4 },
				{ 0, 4, 1 }, { 1, 4, 5 }, { 1, 5, 2 }, { 2, 5, 3 },
				{ 3, 5, 4 } };
		vertices = new WB_Point[] { new WB_Point(0, 0, 1.414),
				new WB_Point(1.414, 0, 0), new WB_Point(0, 1.414, 0),
				new WB_Point(-1.414, 0, 0), new WB_Point(0, -1.414, 0),
				new WB_Point(0, 0, -1.414) };
	}

	public void cube() {
		name = "C";
		faces = new int[][] { { 3, 0, 1, 2 }, { 3, 4, 5, 0 }, { 0, 5, 6, 1 },
				{ 1, 6, 7, 2 }, { 2, 7, 4, 3 }, { 5, 4, 7, 6 } };
		vertices = new WB_Point[] { new WB_Point(0.707, 0.707, 0.707),
				new WB_Point(-0.707, 0.707, 0.707),
				new WB_Point(-0.707, -0.707, 0.707),
				new WB_Point(0.707, -0.707, 0.707),
				new WB_Point(0.707, -0.707, -0.707),
				new WB_Point(0.707, 0.707, -0.707),
				new WB_Point(-0.707, 0.707, -0.707),
				new WB_Point(-0.707, -0.707, -0.707) };
	}

	public void icosahedron() {
		name = "I";
		faces = new int[][] { { 0, 1, 2 }, { 0, 2, 3 }, { 0, 3, 4 },
				{ 0, 4, 5 }, { 0, 5, 1 }, { 1, 5, 7 }, { 1, 7, 6 }, { 1, 6, 2 },
				{ 2, 6, 8 }, { 2, 8, 3 }, { 3, 8, 9 }, { 3, 9, 4 },
				{ 4, 9, 10 }, { 4, 10, 5 }, { 5, 10, 7 }, { 6, 7, 11 },
				{ 6, 11, 8 }, { 7, 10, 11 }, { 8, 11, 9 }, { 9, 11, 10 } };
		vertices = new WB_Point[] { new WB_Point(0, 0, 1.176),
				new WB_Point(1.051, 0, 0.526), new WB_Point(0.324, 1.0, 0.525),
				new WB_Point(-0.851, 0.618, 0.526),
				new WB_Point(-0.851, -0.618, 0.526),
				new WB_Point(0.325, -1.0, 0.526),
				new WB_Point(0.851, 0.618, -0.526),
				new WB_Point(0.851, -0.618, -0.526),
				new WB_Point(-0.325, 1.0, -0.526),
				new WB_Point(-1.051, 0, -0.526),
				new WB_Point(-0.325, -1.0, -0.526),
				new WB_Point(0, 0, -1.176) };
	}

	public void dodecahedron() {
		name = "D";
		faces = new int[][] { { 0, 1, 4, 7, 2 }, { 0, 2, 6, 9, 3 },
				{ 0, 3, 8, 5, 1 }, { 1, 5, 11, 10, 4 }, { 2, 7, 13, 12, 6 },
				{ 3, 9, 15, 14, 8 }, { 4, 10, 16, 13, 7 }, { 5, 8, 14, 17, 11 },
				{ 6, 12, 18, 15, 9 }, { 10, 11, 17, 19, 16 },
				{ 12, 13, 16, 19, 18 }, { 14, 15, 18, 19, 17 } };
		vertices = new WB_Point[] { new WB_Point(0, 0, 1.07047),
				new WB_Point(0.713644, 0, 0.797878),
				new WB_Point(-0.356822, 0.618, 0.797878),
				new WB_Point(-0.356822, -0.618, 0.797878),
				new WB_Point(0.797878, 0.618034, 0.356822),
				new WB_Point(0.797878, -0.618, 0.356822),
				new WB_Point(-0.934172, 0.381966, 0.356822),
				new WB_Point(0.136294, 1.0, 0.356822),
				new WB_Point(0.136294, -1.0, 0.356822),
				new WB_Point(-0.934172, -0.381966, 0.356822),
				new WB_Point(0.934172, 0.381966, -0.356822),
				new WB_Point(0.934172, -0.381966, -0.356822),
				new WB_Point(-0.797878, 0.618, -0.356822),
				new WB_Point(-0.136294, 1.0, -0.356822),
				new WB_Point(-0.136294, -1.0, -0.356822),
				new WB_Point(-0.797878, -0.618034, -0.356822),
				new WB_Point(0.356822, 0.618, -0.797878),
				new WB_Point(0.356822, -0.618, -0.797878),
				new WB_Point(-0.713644, 0, -0.797878),
				new WB_Point(0, 0, -1.07047) };
	}

	public void prism(final int N) {
		double theta = 2.0 * Math.PI / N;
		double h = Math.sin(0.5 * theta);
		name = "P" + N;
		vertices = new WB_Point[2 * N];
		for (int i = 0; i < N; i++) {
			vertices[i] = new WB_Point(-Math.cos(i * theta),
					-Math.sin(i * theta), -h);
		}
		for (int i = N; i < 2 * N; i++) {
			vertices[i] = new WB_Point(-Math.cos(i * theta),
					-Math.sin(i * theta), h);
		}
		faces = new int[N + 2][];
		faces[0] = new int[N];
		faces[1] = new int[N];
		for (int i = 0; i < N; i++) {
			faces[0][i] = N - i - 1;
			faces[1][i] = N + i;
		}
		for (int i = 0; i < N; i++) {
			faces[2 + i] = new int[] { i, (i + 1) % N, N + (i + 1) % N, i + N };
		}
	}

	public void antiprism(final int N) {
		double theta = 2.0 * Math.PI / N;
		double h = Math.sqrt(1.0 - 4.0
				/ (4.0 + 2.0 * Math.cos(0.5 * theta) - 2 * Math.cos(theta)));
		double r = Math.sqrt(1.0 - h * h);
		double f = Math.sqrt(h * h + Math.pow(r * Math.cos(0.5 * theta), 2));
		r = r / f;
		h = h / f;
		name = "A" + N;
		vertices = new WB_Point[2 * N];
		for (int i = 0; i < N; i++) {
			vertices[i] = new WB_Point(-r * Math.cos(i * theta),
					-r * Math.sin(i * theta), -h);
		}
		for (int i = N; i < 2 * N; i++) {
			vertices[i] = new WB_Point(-r * Math.cos((i + 0.5) * theta),
					-r * Math.sin((i + 0.5) * theta), h);
		}
		faces = new int[2 * N + 2][];
		faces[0] = new int[N];
		faces[1] = new int[N];
		for (int i = 0; i < N; i++) {
			faces[0][i] = N - i - 1;
			faces[1][i] = N + i;
		}
		for (int i = 0; i < N; i++) {
			faces[2 + 2 * i] = new int[] { i, (i + 1) % N, i + N };
			faces[2 + 2 * i + 1] = new int[] { i, i + N, (N + i - 1) % N + N };
		}
	}

	public void kis() {
		kis(0, 0.0);
	}

	public void kis(final int N) {
		kis(N, 0.0);
	}

	public void kis(final int N, final double apex) {
		if (N == 0) {
			name = "k".concat(name);
		} else {
			name = "k".concat(Integer.toString(N)).concat(name);
		}
		PolyFlag flag = new PolyFlag();
		for (int i = 0; i < vertices.length; i++) {
			flag.newV("v" + i, vertices[i]);
		}
		WB_Vector[] normals = normals();
		WB_Point[] centers = centers();
		int[] face;
		String v1, v2, vapex, fname;
		for (int i = 0; i < faces.length; i++) {
			face = faces[i];
			v1 = "v" + face[face.length - 1];
			for (int j = 0; j < face.length; j++) {
				v2 = "v" + face[j];
				if (N == 0 || face.length == N) {
					vapex = "apex" + i;
					fname = "f" + i + v1;
					flag.newV(vapex, centers[i].addMul(apex, normals[i]));
					flag.newFlag(fname, v1, v2);
					flag.newFlag(fname, v2, vapex);
					flag.newFlag(fname, vapex, v1);
				} else {
					flag.newFlag("f" + i, v1, v2);
				}
				v1 = v2;
			}
		}
		PolyFlagCompile pfc = flag.compilePolyhedron();
		vertices = pfc.vertices;
		faces = pfc.faces;
	}

	public void ambo() {
		name = "a".concat(name);
		PolyFlag flag = new PolyFlag();
		String v2;
		int[] face;
		for (int i = 0; i < faces.length; i++) {
			face = faces[i];
			for (int j3 = 0, j1 = face.length - 2, j2 = face.length
					- 1; j3 < face.length; j1 = j2, j2 = j3, j3++) {
				v2 = "v" + face[j2];
				if (face[j1] < face[j2]) {
					flag.newV(midName(face[j1], face[j2]), WB_Point.mulAddMul(
							0.5, vertices[face[j1]], 0.5, vertices[face[j2]]));
				}
				flag.newFlag("orig" + i, midName(face[j1], face[j2]),
						midName(face[j2], face[j3]));
				flag.newFlag("dual" + v2, midName(face[j2], face[j3]),
						midName(face[j1], face[j2]));
			}
		}
		PolyFlagCompile pfc = flag.compilePolyhedron();
		vertices = pfc.vertices;
		faces = pfc.faces;
	}

	private String midName(final int i1, final int i2) {
		if (i1 < i2) {
			return "v" + i1 + "-v" + i2;
		} else {
			return "v" + i2 + "-v" + i1;
		}
	}

	public void gyro() {
		name = "g".concat(name);
		PolyFlag flag = new PolyFlag();
		for (int i = 0; i < vertices.length; i++) {
			WB_Point p = new WB_Point(vertices[i]);
			p.normalizeSelf();
			flag.newV("v" + i, p);
		}
		WB_Point[] centers = centers();
		int[] face;
		for (int i = 0; i < faces.length; i++) {
			WB_Point p = new WB_Point(centers[i]);
			p.normalizeSelf();
			flag.newV("center" + i, p);
		}
		String v1, v2, v3, fname;
		for (int i = 0; i < faces.length; i++) {
			face = faces[i];
			for (int j3 = 0, j1 = face.length - 2, j2 = face.length
					- 1; j3 < face.length; j1 = j2, j2 = j3, j3++) {
				v1 = "v" + face[j1];
				v2 = "v" + face[j2];
				v3 = "v" + face[j3];
				flag.newV(v1 + "~" + v2, WB_Point.mulAddMul(2.0 / 3.0,
						vertices[face[j1]], 1.0 / 3.0, vertices[face[j2]]));
				fname = "f" + i + v1;
				flag.newFlag(fname, "center" + i, v1 + "~" + v2);
				flag.newFlag(fname, v1 + "~" + v2, v2 + "~" + v1);
				flag.newFlag(fname, v2 + "~" + v1, v2);
				flag.newFlag(fname, v2, v2 + "~" + v3);
				flag.newFlag(fname, v2 + "~" + v3, "center" + i);
			}
		}
		PolyFlagCompile pfc = flag.compilePolyhedron();
		vertices = pfc.vertices;
		faces = pfc.faces;
	}

	public void propellor() {
		name = "p".concat(name);
		PolyFlag flag = new PolyFlag();
		for (int i = 0; i < vertices.length; i++) {
			WB_Point p = new WB_Point(vertices[i]);
			p.normalizeSelf();
			flag.newV("v" + i, p);
		}
		int[] face;
		String v1, v2, v3, fname;
		for (int i = 0; i < faces.length; i++) {
			face = faces[i];
			for (int j3 = 0, j1 = face.length - 2, j2 = face.length
					- 1; j3 < face.length; j1 = j2, j2 = j3, j3++) {
				v1 = "v" + face[j1];
				v2 = "v" + face[j2];
				v3 = "v" + face[j3];
				flag.newV(v1 + "~" + v2, WB_Point.mulAddMul(2.0 / 3.0,
						vertices[face[j1]], 1.0 / 3.0, vertices[face[j2]]));
				fname = "f" + i + v2;
				flag.newFlag("v" + i, v1 + "~" + v2, v2 + "~" + v3);
				flag.newFlag(fname, v1 + "~" + v2, v2 + "~" + v1);
				flag.newFlag(fname, v2 + "~" + v1, v2);
				flag.newFlag(fname, v2, v2 + "~" + v3);
				flag.newFlag(fname, v2 + "~" + v3, v1 + "~" + v2);
			}
		}
		PolyFlagCompile pfc = flag.compilePolyhedron();
		vertices = pfc.vertices;
		faces = pfc.faces;
	}

	public void whirl() {
		name = "w".concat(name);
		PolyFlag flag = new PolyFlag();
		WB_Point p;
		for (int i = 0; i < vertices.length; i++) {
			p = vertices[i];
			p.normalizeSelf();
			flag.newV("v" + i, p);
		}
		WB_Point[] centers = centers();
		int[] face;
		int j1, j2, j3;
		String v1, v2, v3, fname, cv1name, cv2name;
		WB_Point v12;
		for (int i = 0; i < faces.length; i++) {
			face = faces[i];
			for (j3 = 0, j2 = face.length - 1, j1 = face.length
					- 2; j3 < face.length; j1 = j2, j2 = j3, j3++) {
				v1 = "v" + face[j1];
				v2 = "v" + face[j2];
				v3 = "v" + face[j3];
				v12 = vertices[face[j1]].mulAddMul(2.0 / 3.0, 1.0 / 3.0,
						vertices[face[j2]]);
				flag.newV(v1 + "~" + v2, v12);
				cv1name = "center" + i + "~" + v1;
				cv2name = "center" + i + "~" + v2;
				p = centers[i].mulAddMul(2.0 / 3.0, 1.0 / 3.0, v12);
				p.normalizeSelf();
				flag.newV(cv1name, p);
				fname = "f" + i + v1;
				flag.newFlag(fname, cv1name, v1 + "~" + v2);
				flag.newFlag(fname, v1 + "~" + v2, v2 + "~" + v1);
				flag.newFlag(fname, v2 + "~" + v1, v2);
				flag.newFlag(fname, v2, v2 + "~" + v3);
				flag.newFlag(fname, v2 + "~" + v3, cv2name);
				flag.newFlag(fname, cv2name, cv1name);
				flag.newFlag("c" + i, cv1name, cv2name);
			}
		}
		PolyFlagCompile pfc = flag.compilePolyhedron();
		vertices = pfc.vertices;
		faces = pfc.faces;
	}

	public void chamfer() {
		chamfer(0.5);
	}

	public void chamfer(final double d) {
		name = "c".concat(name);
		PolyFlag flag = new PolyFlag();
		WB_Vector[] normals = normals();
		int[] face;
		String v1, v2, v1new, v2new, fname;
		for (int i = 0; i < faces.length; i++) {
			face = faces[i];
			for (int j2 = 0, j1 = face.length
					- 1; j2 < face.length; j1 = j2, j2++) {
				v1 = "v" + face[j1];
				v1new = "f" + i + "_" + v1;
				v2 = "v" + face[j2];
				flag.newV(v2, WB_Point.mul(vertices[face[j2]], 1.0 + d));
				v2new = "f" + i + "_" + v2;
				flag.newV(v2new, WB_Point.addMul(vertices[face[j2]], 1.5 * d,
						normals[i]));
				flag.newFlag("orig" + i, v1new, v2new);
				fname = face[j1] < face[j2] ? "hex" + v1 + "_" + v2
						: "hex" + v2 + "_" + v1;
				flag.newFlag(fname, v2, v2new);
				flag.newFlag(fname, v2new, v1new);
				flag.newFlag(fname, v1new, v1);
			}
		}
		PolyFlagCompile pfc = flag.compilePolyhedron();
		vertices = pfc.vertices;
		faces = pfc.faces;
	}

	public void reflect() {
		name = "r".concat(name);
		for (WB_Point p : vertices) {
			p.mulSelf(-1);
		}
		for (int[] face : faces) {
			for (int i = 0; i < face.length / 2; i++) {
				int temp = face[i];
				face[i] = face[face.length - i - 1];
				face[face.length - i - 1] = temp;
			}
		}
	}

	public void join() {
		String lname = "j".concat(name);
		dual();
		ambo();
		dual();
		name = lname;
	}

	public void truncate(final int N) {
		String lname = N == 0 ? "t".concat(name)
				: "t".concat(Integer.toString(N)).concat(name);
		dual();
		kis(N);
		dual();
		name = lname;
	}

	public void truncate() {
		truncate(0);
	}

	public void needle() {
		String lname = "n".concat(name);
		dual();
		kis();
		name = lname;
	}

	public void zip() {
		String lname = "z".concat(name);
		kis();
		dual();
		name = lname;
	}

	public void ortho() {
		String lname = "o".concat(name);
		ambo();
		ambo();
		dual();
		name = lname;
	}

	public void expand() {
		String lname = "e".concat(name);
		ambo();
		ambo();
		name = lname;
	}

	public void snub() {
		String lname = "s".concat(name);
		gyro();
		dual();
		name = lname;
	}

	public void bevel() {
		String lname = "b".concat(name);
		ambo();
		truncate();
		name = lname;
	}

	public void meta() {
		String lname = "m".concat(name);
		join();
		kis();
		name = lname;
	}

	public void hollow() {
		hollow(0.5, 0.2);
	}

	public void hollow(final double inset_dist, final double thickness) {
		name = "l".concat(name);
		WB_Polyhedron dual = new WB_Polyhedron(this);
		dual.dual();
		WB_Vector[] dualnormals = dual.normals();
		WB_Vector[] normals = normals();
		WB_Point[] centers = centers();
		PolyFlag flag = new PolyFlag();
		for (int i = 0; i < vertices.length; i++) {
			flag.newV("v" + i, vertices[i]);
			flag.newV("downv" + i,
					vertices[i].addMul(-1.0 * thickness, dualnormals[i]));
		}
		int[] f;
		for (int i = 0; i < faces.length; i++) {
			f = faces[i];
			for (int j = 0; j < f.length; j++) {
				flag.newV("fin" + i + "v" + f[j], WB_Point
						.interpolate(vertices[f[j]], centers[i], inset_dist));
				flag.newV("findown" + i + "v" + f[j],
						WB_Point.interpolate(vertices[f[j]], centers[i],
								inset_dist)
								.addMul(-1.0 * thickness, normals[i]));
			}
		}
		String v1, v2, fname;
		for (int i = 0; i < faces.length; i++) {
			f = faces[i];
			for (int j2 = 0, j1 = f.length - 1; j2 < f.length; j1 = j2, j2++) {
				v1 = "v" + f[j1];
				v2 = "v" + f[j2];
				fname = "f" + i + v1;
				flag.newFlag(fname, v1, v2);
				flag.newFlag(fname, v2, "fin" + i + v2);
				flag.newFlag(fname, "fin" + i + v2, "fin" + i + v1);
				flag.newFlag(fname, "fin" + i + v1, v1);
				fname = "sides" + i + v1;
				flag.newFlag(fname, "fin" + i + v1, "fin" + i + v2);
				flag.newFlag(fname, "fin" + i + v2, "findown" + i + v2);
				flag.newFlag(fname, "findown" + i + v2, "findown" + i + v1);
				flag.newFlag(fname, "findown" + i + v1, "fin" + i + v1);
				fname = "bottom" + i + v1;
				flag.newFlag(fname, "down" + v2, "down" + v1);
				flag.newFlag(fname, "down" + v1, "findown" + i + v1);
				flag.newFlag(fname, "findown" + i + v1, "findown" + i + v2);
				flag.newFlag(fname, "findown" + i + v2, "down" + v2);
				v1 = v2;
			}
		}
		PolyFlagCompile pfc = flag.compilePolyhedron();
		vertices = pfc.vertices;
		faces = pfc.faces;
	}

	public void unit() {
		for (WB_Point p : vertices) {
			p.normalizeSelf();
		}
	}

	public void dual() {
		name = "d".concat(name);
		PolyFlag flag = new PolyFlag();
		Map<String, String> faceFlags = new LinkedHashMap<String, String>();
		int[] f;
		int j1, j2;
		for (int i = 0; i < faces.length; i++) {
			f = faces[i];
			j1 = f[f.length - 1];
			for (int j = 0; j < f.length; j++) {
				j2 = f[j];
				faceFlags.put("f" + j1 + "v" + j2, "center" + i);
				j1 = j2;
			}
		}
		WB_Point[] centers = centers();
		for (int i = 0; i < centers.length; i++) {
			flag.newV("center" + i, centers[i]);
		}
		for (int i = 0; i < faces.length; i++) {
			f = faces[i];
			j1 = f[f.length - 1];
			for (int j = 0; j < f.length; j++) {
				j2 = f[j];
				flag.newFlag("v" + j1, faceFlags.get("f" + j2 + "v" + j1),
						"center" + i);
				j1 = j2;
			}
		}
		PolyFlagCompile pfc = flag.compilePolyhedron();
		vertices = pfc.vertices;
		int[][] sorted = new int[pfc.faces.length][];
		int k;
		for (int i = 0; i < pfc.faces.length; i++) {
			f = pfc.faces[i];
			k = intersect(faces[f[0]], faces[f[1]], faces[f[2]]);
			sorted[k] = f;
		}
		faces = sorted;
	}

	public WB_Point[] centers() {
		WB_Point[] centers = new WB_Point[faces.length];
		for (int i = 0; i < faces.length; i++) {
			centers[i] = new WB_Point();
			for (int j = 0; j < faces[i].length; j++) {
				centers[i].addSelf(vertices[faces[i][j]]);
			}
			centers[i].divSelf(faces[i].length);
		}
		return centers;
	}

	public WB_Vector[] normals() {
		WB_Vector[] normals = new WB_Vector[faces.length];
		for (int i = 0; i < faces.length; i++) {
			normals[i] = new WB_Vector();
			for (int j3 = 0, j1 = faces[i].length - 2, j2 = faces[i].length
					- 1; j3 < faces[i].length; j1 = j2, j2 = j3, j3++) {
				normals[i].addSelf(orthogonal(vertices[faces[i][j1]],
						vertices[faces[i][j2]], vertices[faces[i][j3]]));
			}
			normals[i].normalizeSelf();
		}
		return normals;
	}

	public int[][] edges() {
		List<int[]> edgeList = new FastList<int[]>();
		int[] face;
		for (int i = 0; i < faces.length; i++) {
			face = faces[i];
			for (int j2 = 0, j1 = face.length
					- 1; j2 < face.length; j1 = j2, j2++) {
				if (face[j1] < face[j2]) {
					edgeList.add(new int[] { face[j1], face[j2] });
				}
			}
		}
		int[][] result = new int[edgeList.size()][];
		for (int i = 0; i < edgeList.size(); i++) {
			result[i] = edgeList.get(i);
		}
		return result;
	};

	public WB_Point[] edgeCenters() {
		int[][] edges = edges();
		WB_Point[] edgeCenters = new WB_Point[edges.length];
		for (int i = 0; i < edges.length; i++) {
			edgeCenters[i] = tangentPoint(vertices[edges[i][0]],
					vertices[edges[i][1]]);
		}
		return edgeCenters;
	};

	private WB_Vector orthogonal(final WB_Coord v1, final WB_Coord v2,
			final WB_Coord v3) {
		WB_Vector d1, d2;
		d1 = WB_Vector.subToVector3D(v2, v1);
		d2 = WB_Vector.subToVector3D(v3, v2);
		return d1.crossSelf(d2);
	}

	private void tangentify(final double f, final WB_Point[] newVertices,
			final int[][] edges) {
		for (int i = 0; i < vertices.length; i++) {
			newVertices[i].set(vertices[i]);
		}
		WB_Point t, c;
		for (int i = 0; i < edges.length; i++) {
			t = tangentPoint(newVertices[edges[i][0]],
					newVertices[edges[i][1]]);
			double d = t.normalizeSelf();
			c = t.mul(0.5 * f * (1.0 - d));
			if (newVertices[edges[i][0]].getLength() > d) {
				newVertices[edges[i][0]].addSelf(c);
			}
			newVertices[edges[i][1]].addSelf(c);
		}
	}

	private WB_Point tangentPoint(final WB_Coord v1, final WB_Coord v2) {
		WB_Vector d = WB_Vector.subToVector3D(v2, v1);
		return new WB_Point(v1).addMulSelf(-d.dot(v1) / d.dot(d), d);
	}

	private void recenter(final int[][] edges) {
		WB_Point center = new WB_Point();
		for (int[] edge : edges) {
			center.addSelf(tangentPoint(vertices[edge[0]], vertices[edge[1]]));
		}
		center.divSelf(edges.length);
		for (WB_Point p : vertices) {
			p.subSelf(center);
		}
	}

	public void recenter() {
		WB_Point center = new WB_Point();
		int[][] edges = edges();
		for (int[] edge : edges) {
			center.addSelf(tangentPoint(vertices[edge[0]], vertices[edge[1]]));
		}
		center.divSelf(edges.length);
		for (WB_Point p : vertices) {
			p.subSelf(center);
		}
	}

	public void rescale() {
		double d2max;
		d2max = 0;
		for (WB_Point p : vertices) {
			d2max = Math.max(d2max, p.getLength3D());
		}
		double id2m = 1.0 / d2max;
		for (WB_Point p : vertices) {
			p.mulSelf(id2m);
		}
	}

	private void planarize(final double f, final WB_Point[] newVertices) {
		for (int i = 0; i < vertices.length; i++) {
			newVertices[i].set(vertices[i]);
		}
		WB_Vector[] normals = normals();
		WB_Point[] centers = centers();
		for (int i = 0; i < faces.length; i++) {
			if (normals[i].dot(centers[i]) < 0) {
				normals[i].mulSelf(-1.0);
			}
			for (int j = 0; j < faces[i].length; j++) {
				newVertices[faces[i][j]].addMulSelf(
						centers[i].subToVector3D(vertices[faces[i][j]])
								.dot(normals[i]) * f,
						normals[i]);
			}
		}
	}

	public void planarize(final int iterations, final double f) {
		WB_Point[] newVertices = new WB_Point[vertices.length];
		for (int i = 0; i < vertices.length; i++) {
			newVertices[i] = new WB_Point();
		}
		WB_Point[] tmp;
		for (int i = 0; i < iterations; i++) {
			planarize(f, newVertices);
			tmp = vertices;
			vertices = newVertices;
			newVertices = tmp;
		}
	}

	public void canonicalize(final int iterations) {
		canonicalize(iterations, 0.1);
	}

	public void canonicalize(final int iterations, final double f) {
		WB_Point[] newVertices = new WB_Point[vertices.length];
		WB_Point[] tmp;
		int[][] edges = edges();
		for (int i = 0; i < vertices.length; i++) {
			newVertices[i] = new WB_Point();
		}
		for (int i = 0; i < iterations; i++) {
			tangentify(f, newVertices, edges);
			tmp = vertices;
			vertices = newVertices;
			newVertices = tmp;
			recenter(edges);
			planarize(f, newVertices);
			tmp = vertices;
			vertices = newVertices;
			newVertices = tmp;
		}
	}

	public void adjust(final int iterations) {
		WB_Polyhedron dpoly = new WB_Polyhedron(this);
		dpoly.dual();
		for (int i = 0; i < iterations; i++) {
			dpoly.vertices = reciprocalC(this);
			vertices = reciprocalC(dpoly);
		}
	}

	private WB_Point[] reciprocalC(final WB_Polyhedron poly) {
		WB_Point[] centers = poly.centers();
		for (WB_Point c : centers) {
			c.divSelf(c.dot(c));
		}
		return centers;
	}

	public void adjustCanonical(final int iterations) {
		WB_Polyhedron dpoly = new WB_Polyhedron(this);
		dpoly.dual();
		for (int i = 0; i < iterations; i++) {
			dpoly.vertices = reciprocalN(this);
			vertices = reciprocalN(dpoly);
		}
	}

	private WB_Point[] reciprocalN(final WB_Polyhedron poly) {
		WB_Point[] result = new WB_Point[poly.faces.length];
		int[] f;
		WB_Point centroid;
		WB_Vector normalV;
		WB_Vector tmp;
		double avgEdgeDist = 0.0;
		for (int i = 0; i < poly.faces.length; i++) {
			f = poly.faces[i];
			centroid = new WB_Point();
			normalV = new WB_Vector();
			avgEdgeDist = 0.0;
			for (int j3 = 0, j2 = f.length - 1, j1 = f.length
					- 2; j3 < f.length; j1 = j2, j2 = j3, j3++) {
				centroid.addSelf(poly.vertices[f[j3]]);
				normalV.addSelf(orthogonal(poly.vertices[f[j1]],
						poly.vertices[f[j2]], poly.vertices[f[j3]]));
				avgEdgeDist += edgeDist(poly.vertices[f[j1]],
						poly.vertices[f[j2]]);
			}
			centroid.divSelf(f.length);
			normalV.normalizeSelf();
			avgEdgeDist /= f.length;
			tmp = reciprocal(normalV.mul(centroid.dot(normalV)));
			result[i] = new WB_Point(tmp).mulSelf(0.5 + 0.5 * avgEdgeDist);
		}
		return result;
	}

	private double edgeDist(final WB_Coord p1, final WB_Coord p2) {
		return tangentPoint(p1, p2).getLength();
	}

	private WB_Vector reciprocal(final WB_Coord p) {
		return new WB_Vector(p).divSelf(WB_Vector.getSqLength(p));
	}

	private int intersect(final int[] set1, final int[] set2,
			final int[] set3) {
		int s1, s2, s3;
		for (int l = 0; l < set1.length; l++) {
			s1 = set1[l];
			for (int o = 0; o < set2.length; o++) {
				s2 = set2[o];
				if (s1 == s2) {
					for (int u = 0; u < set3.length; u++) {
						s3 = set3[u];
						if (s1 == s3) {
							return s1;
						}
					}
				}
			}
		}
		return -1;
	};

	static class PolyFlag {
		private Map<String, WB_Point>	vertexPos;
		private Map<String, Integer>	vertexID;
		private Map<String, String>		edgeID;
		private Map<String, String>		faceID;

		PolyFlag() {
			vertexPos = new LinkedHashMap<String, WB_Point>();
			vertexID = new LinkedHashMap<String, Integer>();
			edgeID = new LinkedHashMap<String, String>();
			faceID = new LinkedHashMap<String, String>();
		}

		void newV(final String name, final WB_Point p) {
			if (!vertexID.containsKey(name)) {
				vertexID.put(name, 0);
				vertexPos.put(name, p);
			}
		}

		void newFlag(final String f, final String v1, final String v2) {
			String key = f + "_" + v1;
			if (!faceID.containsKey(f)) {
				faceID.put(f, v1);
			}
			if (!edgeID.containsKey(key)) {
				edgeID.put(key, v2);
			}
		}

		PolyFlagCompile compilePolyhedron() {
			WB_Point[] vertices = new WB_Point[vertexPos.size()];
			int i = 0;
			for (String v : vertexID.keySet()) {
				vertices[i] = vertexPos.get(v);
				vertexID.put(v, i);
				i++;
			}
			int[][] faces = new int[faceID.size()][];
			i = 0;
			String v, v0;
			IntArrayList face = new IntArrayList();
			for (String f : faceID.keySet()) {
				face = new IntArrayList();
				v0 = faceID.get(f);
				v = v0;
				do {
					face.add(vertexID.get(v));
					v = edgeID.get(f + "_" + v);
				} while (!v.equals(v0));
				faces[i] = face.toArray();
				i++;
			}
			return new PolyFlagCompile(vertices, faces);
		}
	}

	static class PolyFlagCompile {
		private WB_Point[]	vertices;
		private int[][]		faces;

		PolyFlagCompile(final WB_Point[] vertices, final int[][] faces) {
			this.vertices = vertices;
			this.faces = faces;
		}
	}

	public static void main(final String[] args) {
		WB_Polyhedron test = new WB_Polyhedron();
		test.tetrahedron();
		test.bevel();
		test.adjustCanonical(10);
	}

	@Override
	public WB_Polyhedron apply2D(WB_Transform2D T) {
		WB_Polyhedron poly = new WB_Polyhedron(this);
		poly.apply2DSelf(T);
		return this;
	}

	@Override
	public WB_Polyhedron apply(WB_Transform3D T) {
		WB_Polyhedron poly = new WB_Polyhedron(this);
		poly.applySelf(T);
		return this;
	}

	@Override
	public WB_Polyhedron apply2DSelf(WB_Transform2D T) {
		for (WB_Point p : vertices) {
			p.applyAsPoint2DSelf(T);
		}
		return this;
	}

	@Override
	public WB_Polyhedron applySelf(WB_Transform3D T) {
		for (WB_Point p : vertices) {
			p.applyAsPointSelf(T);
		}
		return this;
	}
}
