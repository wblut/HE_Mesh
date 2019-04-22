/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.math;

/**
 * 4x4 matrix.
 *
 * @author Frederik Vanhoutte (W:Blut) 2010
 */
public class WB_M44 {
	/** First row. */
	public double m11, m12, m13, m14;
	/** Second row. */
	public double m21, m22, m23, m24;
	/** Third row. */
	public double m31, m32, m33, m34;
	/** Fourth row. */
	public double m41, m42, m43, m44;

	/**
	 * Instantiates a new WB_M44.
	 */
	public WB_M44() {
	}

	/**
	 * Instantiates a new WB_M44.
	 * 
	 * @param matrix44
	 *            double[4][4] values
	 */
	public WB_M44(final double[][] matrix44) {
		m11 = matrix44[0][0];
		m12 = matrix44[0][1];
		m13 = matrix44[0][2];
		m14 = matrix44[0][3];
		m21 = matrix44[1][0];
		m22 = matrix44[1][1];
		m23 = matrix44[1][2];
		m24 = matrix44[1][3];
		m31 = matrix44[2][0];
		m32 = matrix44[2][1];
		m33 = matrix44[2][2];
		m34 = matrix44[2][3];
		m41 = matrix44[3][0];
		m42 = matrix44[3][1];
		m43 = matrix44[3][2];
		m44 = matrix44[3][3];
	}

	/**
	 * Instantiates a new WB_M44.
	 * 
	 * @param m11
	 *            m11
	 * @param m12
	 *            m12
	 * @param m13
	 *            m13
	 * @param m14
	 *            m14
	 * @param m21
	 *            m21
	 * @param m22
	 *            m22
	 * @param m23
	 *            m23
	 * @param m24
	 *            m24
	 * @param m31
	 *            m31
	 * @param m32
	 *            m32
	 * @param m33
	 *            m33
	 * @param m34
	 *            m34
	 * @param m41
	 *            m41
	 * @param m42
	 *            m42
	 * @param m43
	 *            m43
	 * @param m44
	 *            m44
	 */
	public WB_M44(final double m11, final double m12, final double m13, final double m14, final double m21,
			final double m22, final double m23, final double m24, final double m31, final double m32, final double m33,
			final double m34, final double m41, final double m42, final double m43, final double m44) {
		this.m11 = m11;
		this.m12 = m12;
		this.m13 = m13;
		this.m14 = m14;
		this.m21 = m21;
		this.m22 = m22;
		this.m23 = m23;
		this.m24 = m24;
		this.m31 = m31;
		this.m32 = m32;
		this.m33 = m33;
		this.m34 = m34;
		this.m41 = m41;
		this.m42 = m42;
		this.m43 = m43;
		this.m44 = m44;
	}

	/**
	 * Set values.
	 * 
	 * @param matrix44
	 *            double[4][4] values
	 */
	public void set(final double[][] matrix44) {
		m11 = matrix44[0][0];
		m12 = matrix44[0][1];
		m13 = matrix44[0][2];
		m14 = matrix44[0][3];
		m21 = matrix44[1][0];
		m22 = matrix44[1][1];
		m23 = matrix44[1][2];
		m24 = matrix44[1][3];
		m31 = matrix44[2][0];
		m32 = matrix44[2][1];
		m33 = matrix44[2][2];
		m34 = matrix44[2][3];
		m41 = matrix44[3][0];
		m42 = matrix44[3][1];
		m43 = matrix44[3][2];
		m44 = matrix44[3][3];
	}

	/**
	 * Set values.
	 * 
	 * @param matrix44
	 *            float[4][4] values
	 */
	public void set(final float[][] matrix44) {
		m11 = matrix44[0][0];
		m12 = matrix44[0][1];
		m13 = matrix44[0][2];
		m14 = matrix44[0][3];
		m21 = matrix44[1][0];
		m22 = matrix44[1][1];
		m23 = matrix44[1][2];
		m24 = matrix44[1][3];
		m31 = matrix44[2][0];
		m32 = matrix44[2][1];
		m33 = matrix44[2][2];
		m34 = matrix44[2][3];
		m41 = matrix44[3][0];
		m42 = matrix44[3][1];
		m43 = matrix44[3][2];
		m44 = matrix44[3][3];
	}

	/**
	 * Set values.
	 * 
	 * @param matrix44
	 *            int[4][4] values
	 */
	public void set(final int[][] matrix44) {
		m11 = matrix44[0][0];
		m12 = matrix44[0][1];
		m13 = matrix44[0][2];
		m14 = matrix44[0][3];
		m21 = matrix44[1][0];
		m22 = matrix44[1][1];
		m23 = matrix44[1][2];
		m24 = matrix44[1][3];
		m31 = matrix44[2][0];
		m32 = matrix44[2][1];
		m33 = matrix44[2][2];
		m34 = matrix44[2][3];
		m41 = matrix44[3][0];
		m42 = matrix44[3][1];
		m43 = matrix44[3][2];
		m44 = matrix44[3][3];
	}

	/**
	 * Sets values.
	 * 
	 * @param m11
	 *            m11
	 * @param m12
	 *            m12
	 * @param m13
	 *            m13
	 * @param m14
	 *            m14
	 * @param m21
	 *            m21
	 * @param m22
	 *            m22
	 * @param m23
	 *            m23
	 * @param m24
	 *            m24
	 * @param m31
	 *            m31
	 * @param m32
	 *            m32
	 * @param m33
	 *            m33
	 * @param m34
	 *            m34
	 * @param m41
	 *            m41
	 * @param m42
	 *            m42
	 * @param m43
	 *            m43
	 * @param m44
	 *            m44
	 */
	public void set(final double m11, final double m12, final double m13, final double m14, final double m21,
			final double m22, final double m23, final double m24, final double m31, final double m32, final double m33,
			final double m34, final double m41, final double m42, final double m43, final double m44) {
		this.m11 = m11;
		this.m12 = m12;
		this.m13 = m13;
		this.m14 = m14;
		this.m21 = m21;
		this.m22 = m22;
		this.m23 = m23;
		this.m24 = m24;
		this.m31 = m31;
		this.m32 = m32;
		this.m33 = m33;
		this.m34 = m34;
		this.m41 = m41;
		this.m42 = m42;
		this.m43 = m43;
		this.m44 = m44;
	}

	/**
	 * Get copy.
	 * 
	 * @return copy
	 */
	public WB_M44 get() {
		return new WB_M44(m11, m12, m13, m14, m21, m22, m23, m24, m31, m32, m33, m34, m41, m42, m43, m44);
	}

	/**
	 * Add matrix.
	 * 
	 * @param m
	 *            matrix
	 */
	public void add(final WB_M44 m) {
		m11 += m.m11;
		m12 += m.m12;
		m13 += m.m13;
		m14 += m.m14;
		m21 += m.m21;
		m22 += m.m22;
		m23 += m.m23;
		m24 += m.m24;
		m31 += m.m31;
		m32 += m.m32;
		m33 += m.m33;
		m34 += m.m34;
		m41 += m.m41;
		m42 += m.m42;
		m43 += m.m43;
		m44 += m.m44;
	}

	/**
	 * Subtract matrix.
	 * 
	 * @param m
	 *            matrix
	 */
	public void sub(final WB_M44 m) {
		m11 -= m.m11;
		m12 -= m.m12;
		m13 -= m.m13;
		m14 -= m.m14;
		m21 -= m.m21;
		m22 -= m.m22;
		m23 -= m.m23;
		m24 -= m.m24;
		m31 -= m.m31;
		m32 -= m.m32;
		m33 -= m.m33;
		m34 -= m.m34;
		m41 -= m.m41;
		m42 -= m.m42;
		m43 -= m.m43;
		m44 -= m.m44;
	}

	/**
	 * Multiply with scalar.
	 * 
	 * @param f
	 *            factor
	 */
	public void mul(final double f) {
		m11 *= f;
		m12 *= f;
		m13 *= f;
		m14 *= f;
		m21 *= f;
		m22 *= f;
		m23 *= f;
		m24 *= f;
		m31 *= f;
		m32 *= f;
		m33 *= f;
		m34 *= f;
		m41 *= f;
		m42 *= f;
		m43 *= f;
		m44 *= f;
	}

	/**
	 * Divide with scalar.
	 * 
	 * @param f
	 *            factor
	 */
	public void div(final double f) {
		final double invf = WB_Epsilon.isZero(f) ? 0 : 1.0 / f;
		m11 *= invf;
		m12 *= invf;
		m13 *= invf;
		m14 *= invf;
		m21 *= invf;
		m22 *= invf;
		m23 *= invf;
		m24 *= invf;
		m31 *= invf;
		m32 *= invf;
		m33 *= invf;
		m34 *= invf;
		m41 *= invf;
		m42 *= invf;
		m43 *= invf;
		m44 *= invf;
	}

	/**
	 * Add matrix into the provided matrix.
	 * 
	 * @param m
	 *            matrix
	 * @param result
	 *            result
	 */
	public void addInto(final WB_M44 m, final WB_M44 result) {
		result.m11 = m11 + m.m11;
		result.m12 = m12 + m.m12;
		result.m13 = m13 + m.m13;
		result.m14 = m14 + m.m14;
		result.m21 = m21 + m.m21;
		result.m22 = m22 + m.m22;
		result.m23 = m23 + m.m23;
		result.m24 = m24 + m.m24;
		result.m31 = m31 + m.m31;
		result.m32 = m32 + m.m32;
		result.m33 = m33 + m.m33;
		result.m34 = m34 + m.m34;
		result.m41 = m41 + m.m41;
		result.m42 = m42 + m.m42;
		result.m43 = m43 + m.m43;
		result.m44 = m44 + m.m44;
	}

	/**
	 * Subtract matrix into the provided matrix.
	 * 
	 * @param m
	 *            matrix
	 * @param result
	 *            result
	 */
	public void subInto(final WB_M44 m, final WB_M44 result) {
		result.m11 = m11 - m.m11;
		result.m12 = m12 - m.m12;
		result.m13 = m13 - m.m13;
		result.m14 = m14 - m.m14;
		result.m21 = m21 - m.m21;
		result.m22 = m22 - m.m22;
		result.m23 = m23 - m.m23;
		result.m24 = m24 - m.m24;
		result.m31 = m31 - m.m31;
		result.m32 = m32 - m.m32;
		result.m33 = m33 - m.m33;
		result.m34 = m34 - m.m34;
		result.m41 = m41 - m.m41;
		result.m42 = m42 - m.m42;
		result.m43 = m43 - m.m43;
		result.m44 = m44 - m.m44;
	}

	/**
	 * Multiply with scalar into provided matrix.
	 * 
	 * @param f
	 *            factor
	 * @param result
	 *            result
	 */
	public void multInto(final double f, final WB_M44 result) {
		result.m11 = f * m11;
		result.m12 = f * m12;
		result.m13 = f * m13;
		result.m14 = f * m14;
		result.m21 = f * m21;
		result.m22 = f * m22;
		result.m23 = f * m23;
		result.m24 = f * m24;
		result.m31 = f * m31;
		result.m32 = f * m32;
		result.m33 = f * m33;
		result.m34 = f * m34;
		result.m41 = f * m41;
		result.m42 = f * m42;
		result.m43 = f * m43;
		result.m44 = f * m44;
	}

	/**
	 * Divide with scalar into provided matrix.
	 * 
	 * @param f
	 *            factor
	 * @param result
	 *            result
	 */
	public void divInto(final double f, final WB_M44 result) {
		final double invf = WB_Epsilon.isZero(f) ? 0 : 1.0 / f;
		result.m11 = invf * m11;
		result.m12 = invf * m12;
		result.m13 = invf * m13;
		result.m14 = invf * m14;
		result.m21 = invf * m21;
		result.m22 = invf * m22;
		result.m23 = invf * m23;
		result.m24 = invf * m24;
		result.m31 = invf * m31;
		result.m32 = invf * m32;
		result.m33 = invf * m33;
		result.m34 = invf * m34;
		result.m41 = invf * m41;
		result.m42 = invf * m42;
		result.m43 = invf * m43;
		result.m44 = invf * m44;
	}

	/**
	 * Multiply with matrix into new matrix.
	 * 
	 * @param m
	 *            matrix
	 * @return result
	 */
	public WB_M44 mult(final WB_M44 m) {
		return new WB_M44(m11 * m.m11 + m12 * m.m21 + m13 * m.m31 + m14 * m.m41,
				m11 * m.m12 + m12 * m.m22 + m13 * m.m32 + m14 * m.m42,
				m11 * m.m13 + m12 * m.m23 + m13 * m.m33 + m14 * m.m43,
				m11 * m.m14 + m12 * m.m24 + m13 * m.m34 + m14 * m.m44,
				m21 * m.m11 + m22 * m.m21 + m23 * m.m31 + m24 * m.m41,
				m21 * m.m12 + m22 * m.m22 + m23 * m.m32 + m24 * m.m42,
				m21 * m.m13 + m22 * m.m23 + m23 * m.m33 + m24 * m.m43,
				m21 * m.m14 + m22 * m.m24 + m23 * m.m34 + m24 * m.m44,
				m31 * m.m11 + m32 * m.m21 + m33 * m.m31 + m34 * m.m41,
				m31 * m.m12 + m32 * m.m22 + m33 * m.m32 + m34 * m.m42,
				m31 * m.m13 + m32 * m.m23 + m33 * m.m33 + m34 * m.m43,
				m31 * m.m14 + m32 * m.m24 + m33 * m.m34 + m34 * m.m44,
				m41 * m.m11 + m42 * m.m21 + m43 * m.m31 + m44 * m.m41,
				m41 * m.m12 + m42 * m.m22 + m43 * m.m32 + m44 * m.m42,
				m41 * m.m13 + m42 * m.m23 + m43 * m.m33 + m44 * m.m43,
				m41 * m.m14 + m42 * m.m24 + m43 * m.m34 + m44 * m.m44);
	}

	/**
	 * Multiply into provided matrix.
	 * 
	 * @param m
	 *            matrix
	 * @param result
	 *            result
	 */
	public void multInto(final WB_M44 m, final WB_M44 result) {
		result.set(m11 * m.m11 + m12 * m.m21 + m13 * m.m31 + m14 * m.m41,
				m11 * m.m12 + m12 * m.m22 + m13 * m.m32 + m14 * m.m42,
				m11 * m.m13 + m12 * m.m23 + m13 * m.m33 + m14 * m.m43,
				m11 * m.m14 + m12 * m.m24 + m13 * m.m34 + m14 * m.m44,
				m21 * m.m11 + m22 * m.m21 + m23 * m.m31 + m24 * m.m41,
				m21 * m.m12 + m22 * m.m22 + m23 * m.m32 + m24 * m.m42,
				m21 * m.m13 + m22 * m.m23 + m23 * m.m33 + m24 * m.m43,
				m21 * m.m14 + m22 * m.m24 + m23 * m.m34 + m24 * m.m44,
				m31 * m.m11 + m32 * m.m21 + m33 * m.m31 + m34 * m.m41,
				m31 * m.m12 + m32 * m.m22 + m33 * m.m32 + m34 * m.m42,
				m31 * m.m13 + m32 * m.m23 + m33 * m.m33 + m34 * m.m43,
				m31 * m.m14 + m32 * m.m24 + m33 * m.m34 + m34 * m.m44,
				m41 * m.m11 + m42 * m.m21 + m43 * m.m31 + m44 * m.m41,
				m41 * m.m12 + m42 * m.m22 + m43 * m.m32 + m44 * m.m42,
				m41 * m.m13 + m42 * m.m23 + m43 * m.m33 + m44 * m.m43,
				m41 * m.m14 + m42 * m.m24 + m43 * m.m34 + m44 * m.m44);
	}

	/**
	 * Multiply two matrices into new matrix.
	 * 
	 * @param n
	 *            matrix
	 * @param m
	 *            matrix
	 * @return result
	 */
	public static WB_M44 mult(final WB_M44 n, final WB_M44 m) {
		return new WB_M44(n.m11 * m.m11 + n.m12 * m.m21 + n.m13 * m.m31 + n.m14 * m.m41,
				n.m11 * m.m12 + n.m12 * m.m22 + n.m13 * m.m32 + n.m14 * m.m42,
				n.m11 * m.m13 + n.m12 * m.m23 + n.m13 * m.m33 + n.m14 * m.m43,
				n.m11 * m.m14 + n.m12 * m.m24 + n.m13 * m.m34 + n.m14 * m.m44,
				n.m21 * m.m11 + n.m22 * m.m21 + n.m23 * m.m31 + n.m24 * m.m41,
				n.m21 * m.m12 + n.m22 * m.m22 + n.m23 * m.m32 + n.m24 * m.m42,
				n.m21 * m.m13 + n.m22 * m.m23 + n.m23 * m.m33 + n.m24 * m.m43,
				n.m21 * m.m14 + n.m22 * m.m24 + n.m23 * m.m34 + n.m24 * m.m44,
				n.m31 * m.m11 + n.m32 * m.m21 + n.m33 * m.m31 + n.m34 * m.m41,
				n.m31 * m.m12 + n.m32 * m.m22 + n.m33 * m.m32 + n.m34 * m.m42,
				n.m31 * m.m13 + n.m32 * m.m23 + n.m33 * m.m33 + n.m34 * m.m43,
				n.m31 * m.m14 + n.m32 * m.m24 + n.m33 * m.m34 + n.m34 * m.m44,
				n.m41 * m.m11 + n.m42 * m.m21 + n.m43 * m.m31 + n.m44 * m.m41,
				n.m41 * m.m12 + n.m42 * m.m22 + n.m43 * m.m32 + n.m44 * m.m42,
				n.m41 * m.m13 + n.m42 * m.m23 + n.m43 * m.m33 + n.m44 * m.m43,
				n.m41 * m.m14 + n.m42 * m.m24 + n.m43 * m.m34 + n.m44 * m.m44);
	}

	/**
	 * Multiply two matrices into provided matrix.
	 * 
	 * @param n
	 *            matrix
	 * @param m
	 *            matrix
	 * @param result
	 *            result
	 */
	public static void multInto(final WB_M44 n, final WB_M44 m, final WB_M44 result) {
		result.set(n.m11 * m.m11 + n.m12 * m.m21 + n.m13 * m.m31 + n.m14 * m.m41,
				n.m11 * m.m12 + n.m12 * m.m22 + n.m13 * m.m32 + n.m14 * m.m42,
				n.m11 * m.m13 + n.m12 * m.m23 + n.m13 * m.m33 + n.m14 * m.m43,
				n.m11 * m.m14 + n.m12 * m.m24 + n.m13 * m.m34 + n.m14 * m.m44,
				n.m21 * m.m11 + n.m22 * m.m21 + n.m23 * m.m31 + n.m24 * m.m41,
				n.m21 * m.m12 + n.m22 * m.m22 + n.m23 * m.m32 + n.m24 * m.m42,
				n.m21 * m.m13 + n.m22 * m.m23 + n.m23 * m.m33 + n.m24 * m.m43,
				n.m21 * m.m14 + n.m22 * m.m24 + n.m23 * m.m34 + n.m24 * m.m44,
				n.m31 * m.m11 + n.m32 * m.m21 + n.m33 * m.m31 + n.m34 * m.m41,
				n.m31 * m.m12 + n.m32 * m.m22 + n.m33 * m.m32 + n.m34 * m.m42,
				n.m31 * m.m13 + n.m32 * m.m23 + n.m33 * m.m33 + n.m34 * m.m43,
				n.m31 * m.m14 + n.m32 * m.m24 + n.m33 * m.m34 + n.m34 * m.m44,
				n.m41 * m.m11 + n.m42 * m.m21 + n.m43 * m.m31 + n.m44 * m.m41,
				n.m41 * m.m12 + n.m42 * m.m22 + n.m43 * m.m32 + n.m44 * m.m42,
				n.m41 * m.m13 + n.m42 * m.m23 + n.m43 * m.m33 + n.m44 * m.m43,
				n.m41 * m.m14 + n.m42 * m.m24 + n.m43 * m.m34 + n.m44 * m.m44);
	}

	/**
	 * Inverse matrix.
	 * 
	 * @return inverse
	 */
	public WB_M44 inverse() {
		final double[][] m = new double[][] { { m11, m12, m13, m14 }, { m21, m22, m23, m24 }, { m31, m32, m33, m34 },
				{ m41, m12, m43, m44 } };
		final int[] indxc = new int[4];
		final int[] indxr = new int[4];
		final int[] ipiv = new int[4];
		final double[][] minv = new double[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				minv[i][j] = m[i][j];
			}
		}
		for (int i = 0; i < 4; i++) {
			int irow = -1, icol = -1;
			double big = 0.;
			// Choose pivot
			for (int j = 0; j < 4; j++) {
				if (ipiv[j] != 1) {
					for (int k = 0; k < 4; k++) {
						if (ipiv[k] == 0) {
							if (WB_Math.fastAbs(minv[j][k]) >= big) {
								big = WB_Math.fastAbs(minv[j][k]);
								irow = j;
								icol = k;
							}
						} else if (ipiv[k] > 1) {
							return null;
						}
					}
				}
			}
			++ipiv[icol];
			// Swap rows _irow_ and _icol_ for pivot
			double tmp;
			if (irow != icol) {
				for (int k = 0; k < 4; ++k) {
					tmp = minv[irow][k];
					minv[irow][k] = minv[icol][k];
					minv[icol][k] = tmp;
				}
			}
			indxr[i] = irow;
			indxc[i] = icol;
			if (minv[icol][icol] == 0.) {
				return null;
			}
			// Set $m[icol][icol]$ to one by scaling row _icol_ appropriately
			final double pivinv = 1.0 / minv[icol][icol];
			minv[icol][icol] = 1.0;
			for (int j = 0; j < 4; j++) {
				minv[icol][j] *= pivinv;
			}
			// Subtract this row from others to zero out their columns
			for (int j = 0; j < 4; j++) {
				if (j != icol) {
					final double save = minv[j][icol];
					minv[j][icol] = 0;
					for (int k = 0; k < 4; k++) {
						minv[j][k] -= minv[icol][k] * save;
					}
				}
			}
		}
		double tmp;
		// Swap columns to reflect permutation
		for (int j = 3; j >= 0; j--) {
			if (indxr[j] != indxc[j]) {
				for (int k = 0; k < 4; k++) {
					tmp = minv[k][indxr[j]];
					minv[k][indxr[j]] = minv[k][indxc[j]];
					minv[k][indxc[j]] = tmp;
				}
			}
		}
		final WB_M44 I = new WB_M44(minv);
		return I;
	}

	/**
	 * Transpose matrix.
	 */
	public void transpose() {
		double tmp = m12;
		m12 = m21;
		m21 = tmp;
		tmp = m13;
		m13 = m31;
		m31 = tmp;
		tmp = m14;
		m14 = m41;
		m41 = tmp;
		tmp = m23;
		m23 = m32;
		m32 = tmp;
		tmp = m24;
		m24 = m42;
		m42 = tmp;
		tmp = m34;
		m34 = m43;
		m43 = tmp;
	}

	/**
	 * Get transposed matrix.
	 * 
	 * @return transposed matrix
	 */
	public WB_M44 getTranspose() {
		return new WB_M44(m11, m21, m31, m41, m12, m22, m32, m42, m13, m23, m33, m43, m14, m24, m34, m44);
	}

	/**
	 * Put transposed matrix into provided matrix.
	 * 
	 * @param result
	 *            the result
	 */
	public void transposeInto(final WB_M44 result) {
		result.set(m11, m21, m31, m41, m12, m22, m32, m42, m13, m23, m33, m43, m14, m24, m34, m44);
	}
}
