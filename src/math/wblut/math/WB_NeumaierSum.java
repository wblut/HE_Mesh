package wblut.math;
/*
 * https://en.wikipedia.org/wiki/Kahan_summation_algorithm 
 */

public class WB_NeumaierSum {
	private double sum;
	private double compensation;
	private int count;
	private double tmp;

	public WB_NeumaierSum() {

	}

	public void reset() {
		sum = 0.0;
		compensation = 0.0;
		count = 0;
	}

	public int getCount() {
		return count;
	}

	public double getSum() {
		return sum + compensation;
	}

	public double getAverage() {
		if (count == 0) {
			return 0;
		}
		return (sum + compensation) / count;
	}

	public void add(double value) {
		tmp = sum + value;
		if (Math.abs(sum) >= Math.abs(value)) {
			compensation += (sum - tmp) + value; // If sum is bigger, low-order digits of input[i] are lost.
		} else {
			compensation += (value - tmp) + sum; // Else low-order digits of sum are lost
		}
		sum = tmp;
	}

	
	public static void main(String[] args) {
		WB_NeumaierSum sum=new WB_NeumaierSum();
		sum.add(1);
		sum.add(1e100);
		sum.add(1);
		sum.add(-1e100);
		double s=1;
		s+=1e100;
		s+=1;
		s-=1e100;
		System.out.println(sum.getSum()+" "+s);
		
	
	}
}
