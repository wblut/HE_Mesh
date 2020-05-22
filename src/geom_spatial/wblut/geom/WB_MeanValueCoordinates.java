package wblut.geom;

import wblut.math.WB_Epsilon;
import wblut.math.WB_NeumaierSum;

/**
 *
 */
public class WB_MeanValueCoordinates {
	/**
	 *
	 *
	 * @param x
	 * @param vertices
	 * @param values
	 * @param triangles
	 * @return
	 */
	public static double getValue(final WB_Coord x, final WB_CoordCollection vertices, final double[] values,
			final int[] triangles) {
		final WB_NeumaierSum totalF = new WB_NeumaierSum();
		final WB_NeumaierSum totalW = new WB_NeumaierSum();
		final int num = vertices.size();
		final double[] d = new double[num];
		final WB_Vector[] u = new WB_Vector[num];
		for (int j = 0; j < num; j++) {
			u[j] = WB_Vector.sub(vertices.get(j), x);
			d[j] = u[j].normalizeSelf();
			if (WB_Epsilon.isZero(d[j])) {
				return values[j];
			}
		}
		int i1, i2, i3;
		double l1, l2, l3, t1, t2, t3, h, w1, w2, w3, c1, c2, c3, s1, s2, s3, det;
		for (int i = 0; i < triangles.length; i += 3) {
			i1 = triangles[i];
			i2 = triangles[i + 1];
			i3 = triangles[i + 2];
			l1 = WB_Vector.getDistance3D(u[i2], u[i3]);
			if (WB_Epsilon.isZero(l1)) {
				continue;
			}
			l2 = WB_Vector.getDistance3D(u[i3], u[i1]);
			if (WB_Epsilon.isZero(l2)) {
				continue;
			}
			l3 = WB_Vector.getDistance3D(u[i1], u[i2]);
			if (WB_Epsilon.isZero(l3)) {
				continue;
			}
			t1 = 2.0 * Math.asin(0.5 * l1);
			t2 = 2.0 * Math.asin(0.5 * l2);
			t3 = 2.0 * Math.asin(0.5 * l3);
			h = 0.5 * (t1 + t2 + t3);
			if (WB_Epsilon.isEqual(Math.PI, h)) {
				w1 = Math.sin(t1) * d[i3] * d[i2];
				w2 = Math.sin(t2) * d[i1] * d[i3];
				w3 = Math.sin(t3) * d[i2] * d[i1];
				return (w1 * values[i1] + w2 * values[i2] + w3 * values[i3]) / (w1 + w2 + w3);
			}
			det = WB_Predicates.orient3D(vertices.get(i1), vertices.get(i2), vertices.get(i3), x);
			c1 = Math.min(Math.max(2 * Math.sin(h) * Math.sin(h - t1) / Math.sin(t2) / Math.sin(t3) - 1.0, -1.0), 1.0);
			s1 = Math.signum(det) * Math.sqrt(1.0 - c1 * c1);
			if (WB_Epsilon.isZero(s1)) {
				continue;
			}
			c2 = Math.min(Math.max(2 * Math.sin(h) * Math.sin(h - t2) / Math.sin(t3) / Math.sin(t1) - 1.0, -1.0), 1.0);
			s2 = Math.signum(det) * Math.sqrt(1.0 - c2 * c2);
			if (WB_Epsilon.isZero(s2)) {
				continue;
			}
			c3 = Math.min(Math.max(2 * Math.sin(h) * Math.sin(h - t3) / Math.sin(t1) / Math.sin(t2) - 1.0, -1.0), 1.0);
			s3 = Math.signum(det) * Math.sqrt(1.0 - c3 * c3);
			if (WB_Epsilon.isZero(s3)) {
				continue;
			}
			w1 = (t1 - c2 * t3 - c3 * t2) / (d[i1] * Math.sin(t3) * s2);
			w2 = (t2 - c3 * t1 - c1 * t3) / (d[i2] * Math.sin(t1) * s3);
			w3 = (t3 - c1 * t2 - c2 * t1) / (d[i3] * Math.sin(t2) * s1);
			totalF.add(w1 * values[i1] + w2 * values[i2] + w3 * values[i3]);
			totalW.add(w1 + w2 + w3);
		}
		return totalF.getSum() / totalW.getSum();
	}

	/**
	 *
	 *
	 * @param x
	 * @param vertices
	 * @param values
	 * @param triangles
	 * @return
	 */
	public static double[] getValue(final WB_Coord x, final WB_CoordCollection vertices, final double[][] values,
			final int[] triangles) {
		final int num = vertices.size();
		final int numv = values[0].length;
		final double[] d = new double[num];
		final WB_Vector[] u = new WB_Vector[num];
		for (int j = 0; j < num; j++) {
			u[j] = WB_Vector.sub(vertices.get(j), x);
			d[j] = u[j].normalizeSelf();
			if (WB_Epsilon.isZero(d[j])) {
				return values[j];
			}
		}
		final WB_NeumaierSum[] totalF = new WB_NeumaierSum[numv];
		for (int n = 0; n < numv; n++) {
			totalF[n] = new WB_NeumaierSum();
		}
		final WB_NeumaierSum totalW = new WB_NeumaierSum();
		int i1, i2, i3;
		double l1, l2, l3, t1, t2, t3, h, w1, w2, w3, c1, c2, c3, s1, s2, s3, det;
		for (int i = 0; i < triangles.length; i += 3) {
			i1 = triangles[i];
			i2 = triangles[i + 1];
			i3 = triangles[i + 2];
			l1 = WB_Vector.getDistance3D(u[i2], u[i3]);
			if (WB_Epsilon.isZero(l1)) {
				continue;
			}
			l2 = WB_Vector.getDistance3D(u[i3], u[i1]);
			if (WB_Epsilon.isZero(l2)) {
				continue;
			}
			l3 = WB_Vector.getDistance3D(u[i1], u[i2]);
			if (WB_Epsilon.isZero(l3)) {
				continue;
			}
			t1 = 2.0 * Math.asin(0.5 * l1);
			t2 = 2.0 * Math.asin(0.5 * l2);
			t3 = 2.0 * Math.asin(0.5 * l3);
			h = 0.5 * (t1 + t2 + t3);
			if (WB_Epsilon.isEqual(Math.PI, h)) {
				w1 = Math.sin(t1) * d[i3] * d[i2];
				w2 = Math.sin(t2) * d[i1] * d[i3];
				w3 = Math.sin(t3) * d[i2] * d[i1];
				final double sw = w1 + w2 + w3;
				final double[] result = new double[numv];
				for (int n = 0; n < numv; n++) {
					result[n] = (w1 * values[i1][n] + w2 * values[i2][n] + w3 * values[i3][n]) / sw;
				}
				return result;
			}
			det = WB_Predicates.orient3D(vertices.get(i1), vertices.get(i2), vertices.get(i3), x);
			c1 = Math.min(Math.max(2 * Math.sin(h) * Math.sin(h - t1) / Math.sin(t2) / Math.sin(t3) - 1.0, -1.0), 1.0);
			s1 = Math.signum(det) * Math.sqrt(1.0 - c1 * c1);
			if (WB_Epsilon.isZero(s1)) {
				continue;
			}
			c2 = Math.min(Math.max(2 * Math.sin(h) * Math.sin(h - t2) / Math.sin(t3) / Math.sin(t1) - 1.0, -1.0), 1.0);
			s2 = Math.signum(det) * Math.sqrt(1.0 - c2 * c2);
			if (WB_Epsilon.isZero(s2)) {
				continue;
			}
			c3 = Math.min(Math.max(2 * Math.sin(h) * Math.sin(h - t3) / Math.sin(t1) / Math.sin(t2) - 1.0, -1.0), 1.0);
			s3 = Math.signum(det) * Math.sqrt(1.0 - c3 * c3);
			if (WB_Epsilon.isZero(s3)) {
				continue;
			}
			w1 = (t1 - c2 * t3 - c3 * t2) / (d[i1] * Math.sin(t2) * s3);
			w2 = (t2 - c3 * t1 - c1 * t3) / (d[i2] * Math.sin(t3) * s1);
			w3 = (t3 - c1 * t2 - c2 * t1) / (d[i3] * Math.sin(t1) * s2);
			for (int n = 0; n < numv; n++) {
				totalF[n].add(w1 * values[i1][n] + w2 * values[i2][n] + w3 * values[i3][n]);
			}
			totalW.add(w1 + w2 + w3);
		}
		final double[] result = new double[numv];
		final double tw = totalW.getSum();
		for (int n = 0; n < numv; n++) {
			result[n] = totalF[n].getSum() / tw;
		}
		return result;
	}

	/**
	 *
	 *
	 * @param x
	 * @param vertices
	 * @param values
	 * @param triangles
	 * @return
	 */
	public static WB_Coord getValue(final WB_Coord x, final WB_CoordCollection vertices,
			final WB_CoordCollection values, final int[] triangles) {
		final int num = vertices.size();
		final double[] d = new double[num];
		final WB_Vector[] u = new WB_Vector[num];
		for (int j = 0; j < num; j++) {
			u[j] = WB_Vector.sub(vertices.get(j), x);
			d[j] = u[j].normalizeSelf();
			if (WB_Epsilon.isZero(d[j])) {
				return values.get(j);
			}
		}
		final WB_NeumaierSum[] totalF = new WB_NeumaierSum[3];
		for (int n = 0; n < 3; n++) {
			totalF[n] = new WB_NeumaierSum();
		}
		final WB_NeumaierSum totalW = new WB_NeumaierSum();
		int i1, i2, i3;
		double l1, l2, l3, t1, t2, t3, h, w1, w2, w3, c1, c2, c3, s1, s2, s3, det;
		for (int i = 0; i < triangles.length; i += 3) {
			i1 = triangles[i];
			i2 = triangles[i + 1];
			i3 = triangles[i + 2];
			l1 = WB_Vector.getDistance3D(u[i2], u[i3]);
			if (WB_Epsilon.isZero(l1)) {
				continue;
			}
			l2 = WB_Vector.getDistance3D(u[i3], u[i1]);
			if (WB_Epsilon.isZero(l2)) {
				continue;
			}
			l3 = WB_Vector.getDistance3D(u[i1], u[i2]);
			if (WB_Epsilon.isZero(l3)) {
				continue;
			}
			t1 = 2.0 * Math.asin(0.5 * l1);
			t2 = 2.0 * Math.asin(0.5 * l2);
			t3 = 2.0 * Math.asin(0.5 * l3);
			h = 0.5 * (t1 + t2 + t3);
			final WB_Vector result = new WB_Vector();
			if (WB_Epsilon.isEqual(Math.PI, h)) {
				w1 = Math.sin(t1) * d[i3] * d[i2];
				w2 = Math.sin(t2) * d[i1] * d[i3];
				w3 = Math.sin(t3) * d[i2] * d[i1];
				result.addMulSelf(w1, values.get(i1)).addMulSelf(w2, values.get(i2)).addMulSelf(w3, values.get(i3))
						.divSelf(w1 + w2 + w3);
				return result;
			}
			det = WB_Predicates.orient3D(vertices.get(i1), vertices.get(i2), vertices.get(i3), x);
			c1 = Math.min(Math.max(2 * Math.sin(h) * Math.sin(h - t1) / Math.sin(t2) / Math.sin(t3) - 1.0, -1.0), 1.0);
			s1 = Math.signum(det) * Math.sqrt(1.0 - c1 * c1);
			if (WB_Epsilon.isZero(s1)) {
				continue;
			}
			c2 = Math.min(Math.max(2 * Math.sin(h) * Math.sin(h - t2) / Math.sin(t3) / Math.sin(t1) - 1.0, -1.0), 1.0);
			s2 = Math.signum(det) * Math.sqrt(1.0 - c2 * c2);
			if (WB_Epsilon.isZero(s2)) {
				continue;
			}
			c3 = Math.min(Math.max(2 * Math.sin(h) * Math.sin(h - t3) / Math.sin(t1) / Math.sin(t2) - 1.0, -1.0), 1.0);
			s3 = Math.signum(det) * Math.sqrt(1.0 - c3 * c3);
			if (WB_Epsilon.isZero(s3)) {
				continue;
			}
			w1 = (t1 - c2 * t3 - c3 * t2) / (d[i1] * Math.sin(t2) * s3);
			w2 = (t2 - c3 * t1 - c1 * t3) / (d[i2] * Math.sin(t3) * s1);
			w3 = (t3 - c1 * t2 - c2 * t1) / (d[i3] * Math.sin(t1) * s2);
			totalF[0].add(w1 * values.getX(i1) + w2 * values.getX(i2) + w3 * values.getX(i2));
			totalF[1].add(w1 * values.getY(i1) + w2 * values.getY(i2) + w3 * values.getY(i2));
			totalF[2].add(w1 * values.getZ(i1) + w2 * values.getZ(i2) + w3 * values.getZ(i2));
			totalW.add(w1 + w2 + w3);
		}
		final WB_Vector result = new WB_Vector(totalF[0].getSum(), totalF[1].getSum(), totalF[2].getSum());
		result.divSelf(totalW.getSum());
		return result;
	}

	/**
	 *
	 *
	 * @param x
	 * @param vertices
	 * @param values
	 * @param triangles
	 * @return
	 */
	public static float getValue(final WB_Coord x, final WB_CoordCollection vertices, final float[] values,
			final int[] triangles) {
		final WB_NeumaierSum totalF = new WB_NeumaierSum();
		final WB_NeumaierSum totalW = new WB_NeumaierSum();
		final int num = vertices.size();
		final double[] d = new double[num];
		final WB_Vector[] u = new WB_Vector[num];
		for (int j = 0; j < num; j++) {
			u[j] = WB_Vector.sub(vertices.get(j), x);
			d[j] = u[j].normalizeSelf();
			if (WB_Epsilon.isZero(d[j])) {
				return values[j];
			}
		}
		int i1, i2, i3;
		double l1, l2, l3, t1, t2, t3, h, w1, w2, w3, c1, c2, c3, s1, s2, s3, det;
		for (int i = 0; i < triangles.length; i += 3) {
			i1 = triangles[i];
			i2 = triangles[i + 1];
			i3 = triangles[i + 2];
			l1 = WB_Vector.getDistance3D(u[i2], u[i3]);
			if (WB_Epsilon.isZero(l1)) {
				continue;
			}
			l2 = WB_Vector.getDistance3D(u[i3], u[i1]);
			if (WB_Epsilon.isZero(l2)) {
				continue;
			}
			l3 = WB_Vector.getDistance3D(u[i1], u[i2]);
			if (WB_Epsilon.isZero(l3)) {
				continue;
			}
			t1 = 2.0 * Math.asin(0.5 * l1);
			t2 = 2.0 * Math.asin(0.5 * l2);
			t3 = 2.0 * Math.asin(0.5 * l3);
			h = 0.5 * (t1 + t2 + t3);
			if (WB_Epsilon.isEqual(Math.PI, h)) {
				w1 = Math.sin(t1) * d[i3] * d[i2];
				w2 = Math.sin(t2) * d[i1] * d[i3];
				w3 = Math.sin(t3) * d[i2] * d[i1];
				return (float) ((w1 * values[i1] + w2 * values[i2] + w3 * values[i3]) / (w1 + w2 + w3));
			}
			det = WB_Predicates.orient3D(vertices.get(i1), vertices.get(i2), vertices.get(i3), x);
			c1 = Math.min(Math.max(2 * Math.sin(h) * Math.sin(h - t1) / Math.sin(t2) / Math.sin(t3) - 1.0, -1.0), 1.0);
			s1 = Math.signum(det) * Math.sqrt(1.0 - c1 * c1);
			if (WB_Epsilon.isZero(s1)) {
				continue;
			}
			c2 = Math.min(Math.max(2 * Math.sin(h) * Math.sin(h - t2) / Math.sin(t3) / Math.sin(t1) - 1.0, -1.0), 1.0);
			s2 = Math.signum(det) * Math.sqrt(1.0 - c2 * c2);
			if (WB_Epsilon.isZero(s2)) {
				continue;
			}
			c3 = Math.min(Math.max(2 * Math.sin(h) * Math.sin(h - t3) / Math.sin(t1) / Math.sin(t2) - 1.0, -1.0), 1.0);
			s3 = Math.signum(det) * Math.sqrt(1.0 - c3 * c3);
			if (WB_Epsilon.isZero(s3)) {
				continue;
			}
			w1 = (t1 - c2 * t3 - c3 * t2) / (d[i1] * Math.sin(t3) * s2);
			w2 = (t2 - c3 * t1 - c1 * t3) / (d[i2] * Math.sin(t1) * s3);
			w3 = (t3 - c1 * t2 - c2 * t1) / (d[i3] * Math.sin(t2) * s1);
			totalF.add(w1 * values[i1] + w2 * values[i2] + w3 * values[i3]);
			totalW.add(w1 + w2 + w3);
		}
		return (float) (totalF.getSum() / totalW.getSum());
	}

	/**
	 *
	 *
	 * @param x
	 * @param vertices
	 * @param values
	 * @param triangles
	 * @return
	 */
	public static float[] getValue(final WB_Coord x, final WB_CoordCollection vertices, final float[][] values,
			final int[] triangles) {
		final int num = vertices.size();
		final int numv = values[0].length;
		final double[] d = new double[num];
		final WB_Vector[] u = new WB_Vector[num];
		for (int j = 0; j < num; j++) {
			u[j] = WB_Vector.sub(vertices.get(j), x);
			d[j] = u[j].normalizeSelf();
			if (WB_Epsilon.isZero(d[j])) {
				return values[j];
			}
		}
		final WB_NeumaierSum[] totalF = new WB_NeumaierSum[numv];
		for (int n = 0; n < numv; n++) {
			totalF[n] = new WB_NeumaierSum();
		}
		final WB_NeumaierSum totalW = new WB_NeumaierSum();
		int i1, i2, i3;
		double l1, l2, l3, t1, t2, t3, h, w1, w2, w3, c1, c2, c3, s1, s2, s3, det;
		for (int i = 0; i < triangles.length; i += 3) {
			i1 = triangles[i];
			i2 = triangles[i + 1];
			i3 = triangles[i + 2];
			l1 = WB_Vector.getDistance3D(u[i2], u[i3]);
			if (WB_Epsilon.isZero(l1)) {
				continue;
			}
			l2 = WB_Vector.getDistance3D(u[i3], u[i1]);
			if (WB_Epsilon.isZero(l2)) {
				continue;
			}
			l3 = WB_Vector.getDistance3D(u[i1], u[i2]);
			if (WB_Epsilon.isZero(l3)) {
				continue;
			}
			t1 = 2.0 * Math.asin(0.5 * l1);
			t2 = 2.0 * Math.asin(0.5 * l2);
			t3 = 2.0 * Math.asin(0.5 * l3);
			h = 0.5 * (t1 + t2 + t3);
			if (WB_Epsilon.isEqual(Math.PI, h)) {
				w1 = Math.sin(t1) * d[i3] * d[i2];
				w2 = Math.sin(t2) * d[i1] * d[i3];
				w3 = Math.sin(t3) * d[i2] * d[i1];
				final double sw = w1 + w2 + w3;
				final float[] result = new float[numv];
				for (int n = 0; n < numv; n++) {
					result[n] = (float) ((w1 * values[i1][n] + w2 * values[i2][n] + w3 * values[i3][n]) / sw);
				}
				return result;
			}
			det = WB_Predicates.orient3D(vertices.get(i1), vertices.get(i2), vertices.get(i3), x);
			c1 = Math.min(Math.max(2 * Math.sin(h) * Math.sin(h - t1) / Math.sin(t2) / Math.sin(t3) - 1.0, -1.0), 1.0);
			s1 = Math.signum(det) * Math.sqrt(1.0 - c1 * c1);
			if (WB_Epsilon.isZero(s1)) {
				continue;
			}
			c2 = Math.min(Math.max(2 * Math.sin(h) * Math.sin(h - t2) / Math.sin(t3) / Math.sin(t1) - 1.0, -1.0), 1.0);
			s2 = Math.signum(det) * Math.sqrt(1.0 - c2 * c2);
			if (WB_Epsilon.isZero(s2)) {
				continue;
			}
			c3 = Math.min(Math.max(2 * Math.sin(h) * Math.sin(h - t3) / Math.sin(t1) / Math.sin(t2) - 1.0, -1.0), 1.0);
			s3 = Math.signum(det) * Math.sqrt(1.0 - c3 * c3);
			if (WB_Epsilon.isZero(s3)) {
				continue;
			}
			w1 = (t1 - c2 * t3 - c3 * t2) / (d[i1] * Math.sin(t2) * s3);
			w2 = (t2 - c3 * t1 - c1 * t3) / (d[i2] * Math.sin(t3) * s1);
			w3 = (t3 - c1 * t2 - c2 * t1) / (d[i3] * Math.sin(t1) * s2);
			for (int n = 0; n < numv; n++) {
				totalF[n].add(w1 * values[i1][n] + w2 * values[i2][n] + w3 * values[i3][n]);
			}
			totalW.add(w1 + w2 + w3);
		}
		final float[] result = new float[numv];
		final double tw = totalW.getSum();
		for (int n = 0; n < numv; n++) {
			result[n] = (float) (totalF[n].getSum() / tw);
		}
		return result;
	}
}
