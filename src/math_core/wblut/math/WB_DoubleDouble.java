package wblut.math;

import java.io.Serializable;

public strictfp final class WB_DoubleDouble implements Serializable, Comparable<Object>, Cloneable {
	private static final long serialVersionUID = -2751466014056438637L;
	public static final WB_DoubleDouble PI = new WB_DoubleDouble(3.141592653589793116e+00, 1.224646799147353207e-16);
	public static final WB_DoubleDouble TWO_PI = new WB_DoubleDouble(6.283185307179586232e+00,
			2.449293598294706414e-16);
	public static final WB_DoubleDouble PI_2 = new WB_DoubleDouble(1.570796326794896558e+00, 6.123233995736766036e-17);
	public static final WB_DoubleDouble E = new WB_DoubleDouble(2.718281828459045091e+00, 1.445646891729250158e-16);
	public static final WB_DoubleDouble NaN = new WB_DoubleDouble(Double.NaN, Double.NaN);
	public static final double EPS = 1.23259516440783e-32;
	public static final WB_DoubleDouble ZERO = new WB_DoubleDouble(0.0, 0.0);

	private static WB_DoubleDouble createNaN() {
		return new WB_DoubleDouble(Double.NaN, Double.NaN);
	}

	public static WB_DoubleDouble valueOf(final String str) throws NumberFormatException {
		return parse(str);
	}

	public static WB_DoubleDouble valueOf(final double x) {
		return new WB_DoubleDouble(x);
	}

	private static final double SPLIT = 134217729.0D; // 2^27+1, for IEEE double
	private double hi = 0.0;
	private double lo = 0.0;

	public WB_DoubleDouble() {
		init(0.0);
	}

	public WB_DoubleDouble(final double x) {
		init(x);
	}

	public WB_DoubleDouble(final double hi, final double lo) {
		init(hi, lo);
	}

	public WB_DoubleDouble(final WB_DoubleDouble dd) {
		init(dd);
	}

	public WB_DoubleDouble(final String str) throws NumberFormatException {
		this(parse(str));
	}

	public static WB_DoubleDouble copy(final WB_DoubleDouble dd) {
		return new WB_DoubleDouble(dd);
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (final CloneNotSupportedException ex) {
			// should never reach here
			return null;
		}
	}

	private void init(final double x) {
		init(x, 0.0);
	}

	private void init(final double hi, final double lo) {
		this.hi = hi;
		this.lo = lo;
	}

	private void init(final WB_DoubleDouble dd) {
		init(dd.hi, dd.lo);
	}

	public WB_DoubleDouble add(final WB_DoubleDouble y) {
		return copy(this).selfAdd(y);
	}

	public WB_DoubleDouble add(final double y) {
		return copy(this).selfAdd(y);
	}

	public WB_DoubleDouble selfAdd(final WB_DoubleDouble y) {
		return selfAdd(y.hi, y.lo);
	}

	public WB_DoubleDouble selfAdd(final double y) {
		return selfAdd(y, 0.0);
	}

	private WB_DoubleDouble selfAdd(final double yhi, final double ylo) {
		double H, h, T, t, S, s, e, f;
		S = hi + yhi;
		T = lo + ylo;
		e = S - hi;
		f = T - lo;
		s = S - e;
		t = T - f;
		s = yhi - e + (hi - s);
		t = ylo - f + (lo - t);
		e = s + T;
		H = S + e;
		h = e + (S - H);
		e = t + h;
		final double zhi = H + e;
		final double zlo = e + (H - zhi);
		hi = zhi;
		lo = zlo;
		return this;
	}

	public WB_DoubleDouble subtract(final WB_DoubleDouble y) {
		return add(y.negate());
	}

	public WB_DoubleDouble subtract(final double y) {
		return add(-y);
	}

	public WB_DoubleDouble selfSubtract(final WB_DoubleDouble y) {
		if (isNaN()) {
			return this;
		}
		return selfAdd(-y.hi, -y.lo);
	}

	public WB_DoubleDouble selfSubtract(final double y) {
		if (isNaN()) {
			return this;
		}
		return selfAdd(-y, 0.0);
	}

	public WB_DoubleDouble negate() {
		if (isNaN()) {
			return this;
		}
		return new WB_DoubleDouble(-hi, -lo);
	}

	public WB_DoubleDouble multiply(final WB_DoubleDouble y) {
		if (y.isNaN()) {
			return createNaN();
		}
		return copy(this).selfMultiply(y);
	}

	public WB_DoubleDouble multiply(final double y) {
		if (Double.isNaN(y)) {
			return createNaN();
		}
		return copy(this).selfMultiply(y, 0.0);
	}

	public WB_DoubleDouble selfMultiply(final WB_DoubleDouble y) {
		return selfMultiply(y.hi, y.lo);
	}

	public WB_DoubleDouble selfMultiply(final double y) {
		return selfMultiply(y, 0.0);
	}

	private WB_DoubleDouble selfMultiply(final double yhi, final double ylo) {
		double hx, tx, hy, ty, C, c;
		C = SPLIT * hi;
		hx = C - hi;
		c = SPLIT * yhi;
		hx = C - hx;
		tx = hi - hx;
		hy = c - yhi;
		C = hi * yhi;
		hy = c - hy;
		ty = yhi - hy;
		c = hx * hy - C + hx * ty + tx * hy + tx * ty + (hi * ylo + lo * yhi);
		final double zhi = C + c;
		hx = C - zhi;
		final double zlo = c + hx;
		hi = zhi;
		lo = zlo;
		return this;
	}

	public WB_DoubleDouble divide(final WB_DoubleDouble y) {
		double hc, tc, hy, ty, C, c, U, u;
		C = hi / y.hi;
		c = SPLIT * C;
		hc = c - C;
		u = SPLIT * y.hi;
		hc = c - hc;
		tc = C - hc;
		hy = u - y.hi;
		U = C * y.hi;
		hy = u - hy;
		ty = y.hi - hy;
		u = hc * hy - U + hc * ty + tc * hy + tc * ty;
		c = (hi - U - u + lo - C * y.lo) / y.hi;
		u = C + c;
		final double zhi = u;
		final double zlo = C - u + c;
		return new WB_DoubleDouble(zhi, zlo);
	}

	public WB_DoubleDouble divide(final double y) {
		if (Double.isNaN(y)) {
			return createNaN();
		}
		return copy(this).selfDivide(y, 0.0);
	}

	public WB_DoubleDouble selfDivide(final WB_DoubleDouble y) {
		return selfDivide(y.hi, y.lo);
	}

	public WB_DoubleDouble selfDivide(final double y) {
		return selfDivide(y, 0.0);
	}

	private WB_DoubleDouble selfDivide(final double yhi, final double ylo) {
		double hc, tc, hy, ty, C, c, U, u;
		C = hi / yhi;
		c = SPLIT * C;
		hc = c - C;
		u = SPLIT * yhi;
		hc = c - hc;
		tc = C - hc;
		hy = u - yhi;
		U = C * yhi;
		hy = u - hy;
		ty = yhi - hy;
		u = hc * hy - U + hc * ty + tc * hy + tc * ty;
		c = (hi - U - u + lo - C * ylo) / yhi;
		u = C + c;
		hi = u;
		lo = C - u + c;
		return this;
	}

	public WB_DoubleDouble reciprocal() {
		double hc, tc, hy, ty, C, c, U, u;
		C = 1.0 / hi;
		c = SPLIT * C;
		hc = c - C;
		u = SPLIT * hi;
		hc = c - hc;
		tc = C - hc;
		hy = u - hi;
		U = C * hi;
		hy = u - hy;
		ty = hi - hy;
		u = hc * hy - U + hc * ty + tc * hy + tc * ty;
		c = (1.0 - U - u - C * lo) / hi;
		final double zhi = C + c;
		final double zlo = C - zhi + c;
		return new WB_DoubleDouble(zhi, zlo);
	}

	public WB_DoubleDouble floor() {
		if (isNaN()) {
			return NaN;
		}
		final double fhi = Math.floor(hi);
		double flo = 0.0;
		// Hi is already integral. Floor the low word
		if (fhi == hi) {
			flo = Math.floor(lo);
		}
		// do we need to renormalize here?
		return new WB_DoubleDouble(fhi, flo);
	}

	public WB_DoubleDouble ceil() {
		if (isNaN()) {
			return NaN;
		}
		final double fhi = Math.ceil(hi);
		double flo = 0.0;
		// Hi is already integral. Ceil the low word
		if (fhi == hi) {
			flo = Math.ceil(lo);
			// do we need to renormalize here?
		}
		return new WB_DoubleDouble(fhi, flo);
	}

	public int signum() {
		if (isPositive()) {
			return 1;
		}
		if (isNegative()) {
			return -1;
		}
		return 0;
	}

	public WB_DoubleDouble rint() {
		if (isNaN()) {
			return this;
		}
		// may not be 100% correct
		final WB_DoubleDouble plus5 = this.add(0.5);
		return plus5.floor();
	}

	public WB_DoubleDouble trunc() {
		if (isNaN()) {
			return NaN;
		}
		if (isPositive()) {
			return floor();
		} else {
			return ceil();
		}
	}

	public WB_DoubleDouble abs() {
		if (isNaN()) {
			return NaN;
		}
		if (isNegative()) {
			return negate();
		}
		return new WB_DoubleDouble(this);
	}

	public WB_DoubleDouble sqr() {
		return this.multiply(this);
	}

	public static WB_DoubleDouble sqr(final double x) {
		return valueOf(x).selfMultiply(x);
	}

	public WB_DoubleDouble sqrt() {
		if (isZero()) {
			return valueOf(0.0);
		}
		if (isNegative()) {
			return NaN;
		}
		final double x = 1.0 / Math.sqrt(hi);
		final double ax = hi * x;
		final WB_DoubleDouble axdd = valueOf(ax);
		final WB_DoubleDouble diffSq = this.subtract(axdd.sqr());
		final double d2 = diffSq.hi * (x * 0.5);
		return axdd.add(d2);
	}

	public static WB_DoubleDouble sqrt(final double x) {
		return valueOf(x).sqrt();
	}

	public WB_DoubleDouble pow(final int exp) {
		if (exp == 0.0) {
			return valueOf(1.0);
		}
		WB_DoubleDouble r = new WB_DoubleDouble(this);
		WB_DoubleDouble s = valueOf(1.0);
		int n = Math.abs(exp);
		if (n > 1) {
			while (n > 0) {
				if (n % 2 == 1) {
					s.selfMultiply(r);
				}
				n /= 2;
				if (n > 0) {
					r = r.sqr();
				}
			}
		} else {
			s = r;
		}
		if (exp < 0) {
			return s.reciprocal();
		}
		return s;
	}

	public double doubleValue() {
		return hi + lo;
	}

	public int intValue() {
		return (int) hi;
	}

	public boolean isZero() {
		return hi == 0.0 && lo == 0.0;
	}

	public boolean isNegative() {
		return hi < 0.0 || hi == 0.0 && lo < 0.0;
	}

	public boolean isPositive() {
		return hi > 0.0 || hi == 0.0 && lo > 0.0;
	}

	public boolean isNaN() {
		return Double.isNaN(hi);
	}

	public boolean equals(final WB_DoubleDouble y) {
		return hi == y.hi && lo == y.lo;
	}

	public boolean gt(final WB_DoubleDouble y) {
		return hi > y.hi || hi == y.hi && lo > y.lo;
	}

	public boolean ge(final WB_DoubleDouble y) {
		return hi > y.hi || hi == y.hi && lo >= y.lo;
	}

	public boolean lt(final WB_DoubleDouble y) {
		return hi < y.hi || hi == y.hi && lo < y.lo;
	}

	public boolean le(final WB_DoubleDouble y) {
		return hi < y.hi || hi == y.hi && lo <= y.lo;
	}

	@Override
	public int compareTo(final Object o) {
		final WB_DoubleDouble other = (WB_DoubleDouble) o;
		if (hi < other.hi) {
			return -1;
		}
		if (hi > other.hi) {
			return 1;
		}
		if (lo < other.lo) {
			return -1;
		}
		if (lo > other.lo) {
			return 1;
		}
		return 0;
	}

	private static final int MAX_PRINT_DIGITS = 32;
	private static final WB_DoubleDouble TEN = WB_DoubleDouble.valueOf(10.0);
	private static final WB_DoubleDouble ONE = WB_DoubleDouble.valueOf(1.0);
	private static final String SCI_NOT_EXPONENT_CHAR = "E";
	private static final String SCI_NOT_ZERO = "0.0E0";

	public String dump() {
		return "DD<" + hi + ", " + lo + ">";
	}

	@Override
	public String toString() {
		final int mag = magnitude(hi);
		if (mag >= -3 && mag <= 20) {
			return toStandardNotation();
		}
		return toSciNotation();
	}

	public String toStandardNotation() {
		final String specialStr = getSpecialNumberString();
		if (specialStr != null) {
			return specialStr;
		}
		final int[] magnitude = new int[1];
		final String sigDigits = extractSignificantDigits(true, magnitude);
		final int decimalPointPos = magnitude[0] + 1;
		String num = sigDigits;
		// add a leading 0 if the decimal point is the first char
		if (sigDigits.charAt(0) == '.') {
			num = "0" + sigDigits;
		} else if (decimalPointPos < 0) {
			num = "0." + stringOfChar('0', -decimalPointPos) + sigDigits;
		} else if (sigDigits.indexOf('.') == -1) {
			// no point inserted - sig digits must be smaller than magnitude of
			// number
			// add zeroes to end to make number the correct size
			final int numZeroes = decimalPointPos - sigDigits.length();
			final String zeroes = stringOfChar('0', numZeroes);
			num = sigDigits + zeroes + ".0";
		}
		if (this.isNegative()) {
			return "-" + num;
		}
		return num;
	}

	public String toSciNotation() {
		// special case zero, to allow as
		if (isZero()) {
			return SCI_NOT_ZERO;
		}
		final String specialStr = getSpecialNumberString();
		if (specialStr != null) {
			return specialStr;
		}
		final int[] magnitude = new int[1];
		final String digits = extractSignificantDigits(false, magnitude);
		final String expStr = SCI_NOT_EXPONENT_CHAR + magnitude[0];
		// should never have leading zeroes
		// MD - is this correct? Or should we simply strip them if they are
		// present?
		if (digits.charAt(0) == '0') {
			throw new IllegalStateException("Found leading zero: " + digits);
		}
		// add decimal point
		String trailingDigits = "";
		if (digits.length() > 1) {
			trailingDigits = digits.substring(1);
		}
		final String digitsWithDecimal = digits.charAt(0) + "." + trailingDigits;
		if (this.isNegative()) {
			return "-" + digitsWithDecimal + expStr;
		}
		return digitsWithDecimal + expStr;
	}

	private String extractSignificantDigits(final boolean insertDecimalPoint, final int[] magnitude) {
		WB_DoubleDouble y = this.abs();
		// compute *correct* magnitude of y
		int mag = magnitude(y.hi);
		final WB_DoubleDouble scale = TEN.pow(mag);
		y = y.divide(scale);
		// fix magnitude if off by one
		if (y.gt(TEN)) {
			y = y.divide(TEN);
			mag += 1;
		} else if (y.lt(ONE)) {
			y = y.multiply(TEN);
			mag -= 1;
		}
		final int decimalPointPos = mag + 1;
		final StringBuffer buf = new StringBuffer();
		final int numDigits = MAX_PRINT_DIGITS - 1;
		for (int i = 0; i <= numDigits; i++) {
			if (insertDecimalPoint && i == decimalPointPos) {
				buf.append('.');
			}
			final int digit = (int) y.hi;
			// System.out.println("printDump: [" + i + "] digit: " + digit +
			// " y: " + y.dump() + " buf: " + buf);
			if (digit < 0 || digit > 9) {
				// System.out.println("digit > 10 : " + digit);
				// throw new
				// IllegalStateException("Internal errror: found digit = " +
				// digit);
			}
			if (digit < 0) {
				break;
				// throw new
				// IllegalStateException("Internal errror: found digit = " +
				// digit);
			}
			boolean rebiasBy10 = false;
			char digitChar = 0;
			if (digit > 9) {
				// set flag to re-bias after next 10-shift
				rebiasBy10 = true;
				// output digit will end up being '9'
				digitChar = '9';
			} else {
				digitChar = (char) ('0' + digit);
			}
			buf.append(digitChar);
			y = y.subtract(WB_DoubleDouble.valueOf(digit)).multiply(TEN);
			if (rebiasBy10) {
				y.selfAdd(TEN);
			}
			boolean continueExtractingDigits = true;
			// if (y.hi <= 0.0)
			// if (y.hi < 0.0)
			// continueExtractingDigits = false;
			final int remMag = magnitude(y.hi);
			if (remMag < 0 && Math.abs(remMag) >= numDigits - i) {
				continueExtractingDigits = false;
			}
			if (!continueExtractingDigits) {
				break;
			}
		}
		magnitude[0] = mag;
		return buf.toString();
	}

	private static String stringOfChar(final char ch, final int len) {
		final StringBuffer buf = new StringBuffer();
		for (int i = 0; i < len; i++) {
			buf.append(ch);
		}
		return buf.toString();
	}

	private String getSpecialNumberString() {
		if (isZero()) {
			return "0.0";
		}
		if (isNaN()) {
			return "NaN ";
		}
		return null;
	}

	private static int magnitude(final double x) {
		final double xAbs = Math.abs(x);
		final double xLog10 = Math.log(xAbs) / Math.log(10);
		int xMag = (int) Math.floor(xLog10);
		final double xApprox = Math.pow(10, xMag);
		if (xApprox * 10 <= xAbs) {
			xMag += 1;
		}
		return xMag;
	}

	public static WB_DoubleDouble parse(final String str) throws NumberFormatException {
		int i = 0;
		final int strlen = str.length();
		// skip leading whitespace
		while (Character.isWhitespace(str.charAt(i))) {
			i++;
		}
		// check for sign
		boolean isNegative = false;
		if (i < strlen) {
			final char signCh = str.charAt(i);
			if (signCh == '-' || signCh == '+') {
				i++;
				if (signCh == '-') {
					isNegative = true;
				}
			}
		}
		// scan all digits and accumulate into an integral value
		// Keep track of the location of the decimal point (if any) to allow
		// scaling later
		final WB_DoubleDouble val = new WB_DoubleDouble();
		int numDigits = 0;
		int numBeforeDec = 0;
		int exp = 0;
		while (true) {
			if (i >= strlen) {
				break;
			}
			final char ch = str.charAt(i);
			i++;
			if (Character.isDigit(ch)) {
				final double d = ch - '0';
				val.selfMultiply(TEN);
				// MD: need to optimize this
				val.selfAdd(d);
				numDigits++;
				continue;
			}
			if (ch == '.') {
				numBeforeDec = numDigits;
				continue;
			}
			if (ch == 'e' || ch == 'E') {
				final String expStr = str.substring(i);
				// this should catch any format problems with the exponent
				try {
					exp = Integer.parseInt(expStr);
				} catch (final NumberFormatException ex) {
					throw new NumberFormatException("Invalid exponent " + expStr + " in string " + str);
				}
				break;
			}
			throw new NumberFormatException("Unexpected character '" + ch + "' at position " + i + " in string " + str);
		}
		WB_DoubleDouble val2 = val;
		// scale the number correctly
		final int numDecPlaces = numDigits - numBeforeDec - exp;
		if (numDecPlaces == 0) {
			val2 = val;
		} else if (numDecPlaces > 0) {
			final WB_DoubleDouble scale = TEN.pow(numDecPlaces);
			val2 = val.divide(scale);
		} else if (numDecPlaces < 0) {
			final WB_DoubleDouble scale = TEN.pow(-numDecPlaces);
			val2 = val.multiply(scale);
		}
		// apply leading sign, if any
		if (isNegative) {
			return val2.negate();
		}
		return val2;
	}
}