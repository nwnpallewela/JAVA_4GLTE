import org.jtransforms.fft.*;

public class IFFT {
	double[] input;
	double[][] ifft_out;

	public IFFT(double[][] fftdata) {
		int size = fftdata[0].length;
		int ifft_size = size * 3 / 2;

		input = new double[2 * ifft_size];

		ifft_out = new double[2][ifft_size];
		int count = 0;
		for (int i = 0; i < fftdata[0].length; ++i) {
			if (i % 8 == 0) {
				count = count + 4;
			}
			input[count] = fftdata[0][i];
			input[count + 1] = fftdata[1][i];
			count = count + 2;
			if (i % 8 == 7) {
				count = count + 4;
			}
		}
		
		
	}

	public double[][] calculate() {
		double in[] = new double[24];
		double out[] = new double[24];
		int count = 0;
		for (int i = 0; i < input.length; i = i + 24) {
			System.arraycopy(input, i, in, 0, 24);
			out = calc_set(in);
			for (int j = 0; j < 12; ++j) {
				ifft_out[0][count] = out[2 * j];
				ifft_out[1][count] = out[2 * j + 1];
				++count;
			}
		}

		return ifft_out;

	}

	private double[] calc_set(double[] in) {
		DoubleFFT_1D ifftDo = new DoubleFFT_1D(12);
		double[] ifft = new double[24];
		System.arraycopy(in, 0, ifft, 0, 24);
		ifftDo.complexInverse(ifft, true);
		return ifft;
	}

}
