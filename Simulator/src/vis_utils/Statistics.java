package vis_utils;

import java.util.ArrayList;
import java.util.Collections;

public class Statistics {
	ArrayList<Double> data;
	int size;

	public Statistics(ArrayList<Double> data) {
		this.data = data;
		size = data.size();
	}

	double getMean() {
		double sum = 0.0;
		for (double a : data)
			sum += a;
		return sum / size;
	}

	double getVariance() {
		double mean = getMean();
		double temp = 0;
		for (double a : data)
			temp += (a - mean) * (a - mean);
		return temp / size;
	}

	double getStdDev() {
		return Math.sqrt(getVariance());
	}

	public double median() {
		Collections.sort(data);

		if (data.size() % 2 == 0) {
			return (data.get((data.size() / 2) - 1) + data.get(data.size() / 2)) / 2.0;
		} else {
			return data.get(data.size() / 2);
		}
	}

	@Override
	public String toString() {
		return "Statistics [size=" + size + ", Mean=" + getMean()
				+ ", Variance=" + getVariance() + ", StdDev=" + getStdDev()
				+ ", Median=" + median() + "]";
	}
}