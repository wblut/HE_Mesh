package wblut.nurbs;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import wblut.geom.WB_DoubleIntMap;
import wblut.geom.WB_DoubleList;
import wblut.math.WB_Epsilon;

public class WB_NurbsKnot {
	protected final double[] values;
	protected final int degree;
	protected final int n;
	protected final int m;

	public WB_NurbsKnot(final int ncp, final int degree) {
		if (degree > ncp - 1 || degree < 1) {
			throw new IllegalArgumentException("Degree too high for number of control points or smaller than 1.");
		}
		this.degree = degree;
		n = ncp - 1;
		m = n + this.degree + 1;
		values = new double[m + 1];
		for (int i = 0; i <= this.degree; i++) {
			values[i] = 0;
			values[m - i] = 1;
		}
		for (int i = 0; i < n - this.degree; i++) {
			values[this.degree + 1 + i] = (i + 1.0) / (n - this.degree + 1);
		}
	}

	public WB_NurbsKnot(final int ncp, final int degree, final double[] val) {
		this(ncp, degree);
		if (val.length != ncp - 1 - degree) {
			throw new IllegalArgumentException("Number of provided values doesn't match knot size and/or degree.");
		}
		if (val[ncp - 2 - degree] > 1.0) {
			throw new IllegalArgumentException("Highest provided value is higher than 1.");
		}
		for (int i = 0; i <= degree; i++) {
			values[i] = 0;
			values[m - i] = 1;
		}
		for (int i = 0; i < n - degree; i++) {
			if (val[i] < values[degree + i]) {
				throw new IllegalArgumentException(
						"Provided values are not non-decreasing or first value is smaller than 0.");
			}
			values[degree + 1 + i] = val[i];
		}
	}

	public WB_NurbsKnot(final int degree, final double[] val) {
		m = val.length - 1;
		values = new double[m + 1];
		this.degree = degree;
		n = m - this.degree - 1;
		for (int i = 0; i <= m; i++) {
			values[i] = val[i];
		}
	}

	public WB_NurbsKnot(final WB_NurbsKnot knot) {
		degree = knot.degree;
		n = knot.n;
		m = n + degree + 1;
		values = new double[m + 1];
		for (int i = 0; i <= m; i++) {
			values[i] = knot.value(i);
		}
	}

	public int p() {
		return degree;
	}

	public int n() {
		return n;
	}

	public int m() {
		return m;
	}

	public int s() {
		double cv, ov;
		ov = Double.NaN;
		int s = 0;
		for (int i = 0; i <= m; i++) {
			cv = values[i];
			if (cv != ov) {
				s++;
			}
			ov = cv;
		}
		return s - 2;
	}

	@Override
	public String toString() {
		final StringBuffer str = new StringBuffer();
		str.append("Knot {");
		for (int i = 0; i < m; i++) {
			str.append(values[i] + ", ");
		}
		str.append(values[m] + "}");
		return str.toString();
	}

	public double[] values() {
		return values;
	}

	public double value(final int i) {
		if (i < 0 || i > m) {
			throw new IllegalArgumentException("Index " + i + " doesn't exist in knot.");
		}
		return values[i];
	}

	public void setValue(final int i, final double k) {
		if (i < 0 || i > m) {
			throw new IllegalArgumentException("Index " + i + " doesn't exist in knot.");
		}
		values[i] = k;
	}

	public int span(final double u) {
		if (u < values[0] || u > values[n + 1]) {
			throw new IllegalArgumentException("Value outside of knot range.");
		}
		if (u == values[n + 1]) {
			return n;
		}
		int low = degree;
		int high = n + 1;
		int mid = (low + high) / 2;
		while (u < values[mid] || u >= values[mid + 1]) {
			if (u < values[mid]) {
				high = mid;
			} else {
				low = mid;
			}
			mid = (low + high) / 2;
		}
		return mid;
	}

	public int multiplicity(final double u) {
		if (u < values[0] || u > values[n + 1]) {
			throw new IllegalArgumentException("Value outside of knot range.");
		}
		int mult = 0;
		final int i = span(u);
		int li = i;
		int ui = i + 1;
		while (li >= 0 && WB_Epsilon.isEqualAbs(values[li], u)) {
			mult++;
			li--;
		}
		while (ui < m + 1 && WB_Epsilon.isEqualAbs(values[ui], u)) {
			mult++;
			ui++;
		}
		return mult;
	}

	public int multiplicity(final double u, final int span) {
		if (u < values[0] || u > values[n + 1]) {
			throw new IllegalArgumentException("Value outside of knot range.");
		}
		int mult = 0;
		int li = span;
		int ui = span + 1;
		while (li >= 0 && values[li] == u) {
			mult++;
			li--;
		}
		while (ui < m + 1 && values[ui] == u) {
			mult++;
			ui++;
		}
		return mult;
	}

	protected double[] basisFunctions(final int span, final double u) {
		final double[] N = new double[degree + 1];
		final double[] left = new double[degree + 1];
		final double[] right = new double[degree + 1];
		double saved, temp;
		N[0] = 1.0;
		for (int j = 1; j <= degree; j++) {
			left[j] = u - values[span + 1 - j];
			right[j] = values[span + j] - u;
			saved = 0.0;
			for (int r = 0; r < j; r++) {
				temp = N[r] / (right[r + 1] + left[j - r]);
				N[r] = saved + right[r + 1] * temp;
				saved = left[j - r] * temp;
			}
			N[j] = saved;
		}
		return N;
	}

	protected double[][] allBasisFunctions(final int span, final double u, final int p) {
		final double[][] N = new double[p + 1][p + 1];
		for (int i = 0; i <= p; i++) {
			final double[] left = new double[i + 1];
			final double[] right = new double[i + 1];
			double saved, temp;
			N[0][i] = 1.0;
			for (int j = 1; j <= i; j++) {
				left[j] = u - values[span + 1 - j];
				right[j] = values[span + j] - u;
				saved = 0.0;
				for (int r = 0; r < j; r++) {
					temp = N[r][i] / (right[r + 1] + left[j - r]);
					N[r][i] = saved + right[r + 1] * temp;
					saved = left[j - r] * temp;
				}
				N[j][i] = saved;
			}
		}
		return N;
	}

	public void normalize() {
		final double low = values[0];
		final double high = values[m];
		double denom = high - low;
		if (WB_Epsilon.isZero(denom)) {
			throw new IllegalArgumentException("Knot value range too small to normalize.");
		}
		denom = 1.0 / denom;
		for (int i = 0; i <= m; i++) {
			values[i] -= low;
			values[i] *= denom;
			if (WB_Epsilon.isZero(values[i])) {
				values[i] = 0.0;
			}
			if (WB_Epsilon.isZero(values[i] - 1.0)) {
				values[i] = 1.0;
			}
		}
	}

	public static WB_NurbsKnot merge(final WB_NurbsKnot UA, final WB_NurbsKnot UB) {
		if (UA.p() != UB.p()) {
			throw new IllegalArgumentException("Cannot merge knots of different degree.");
		}
		final WB_DoubleIntMap knotvalues = new WB_DoubleIntMap();
		int total = 0;
		for (int i = 0; i <= UA.m;) {
			final double ua = UA.value(i);
			final int mul = UA.multiplicity(ua);
			final int mulex = knotvalues.get(ua);
			if (mulex < 0) {
				knotvalues.put(ua, mul);
				total += mul;
			} else if (mulex < mul) {
				knotvalues.put(ua, mul);
				total += mul - mulex;
			}
			i += mul;
		}
		for (int i = 0; i <= UB.m;) {
			final double ub = UB.value(i);
			final int mul = UB.multiplicity(ub);
			final Integer mulex = knotvalues.get(ub);
			if (mulex < 0) {
				knotvalues.put(ub, mul);
				total += mul;
			} else if (mulex < mul) {
				knotvalues.put(ub, mul);
				total += mul - mulex;
			}
			i += mul;
		}
		final WB_DoubleList distinctValues = new WB_DoubleList();
		distinctValues.addAll(knotvalues.keySet());
		distinctValues.sortThis();
		final double[] allValues = new double[total];
		int offset = 0;
		for (int i = 0; i < distinctValues.size(); i++) {
			final int mul = knotvalues.get(distinctValues.get(i));
			for (int j = 0; j < mul; j++) {
				allValues[offset + j] = distinctValues.get(i);
			}
			offset += mul;
		}
		return new WB_NurbsKnot(UA.degree, allValues);
	}

	public double[][] multVal() {
		final Set<Double> uniqValues = new LinkedHashSet<>();
		for (final double value : values) {
			uniqValues.add(value);
		}
		final double[][] multVal = new double[uniqValues.size()][2];
		final Iterator<Double> itr = uniqValues.iterator();
		for (int i = 0; i < uniqValues.size(); i++) {
			multVal[i][0] = itr.next();
			multVal[i][1] = multiplicity(multVal[i][0]);
		}
		return multVal;
	}
}
