import org.jtransforms.fft.DoubleFFT_1D;

public class IFFT_16 {
	double[] input;
	double[][] ifft_out;

	public IFFT_16(double[][] fftdata) {
		int size = fftdata[0].length;
		int ifft_size = size * 2;

		input = new double[2 * ifft_size];

		ifft_out = new double[2][ifft_size];
		int count = 0;
		for (int i = 0; i < fftdata[0].length; ++i) {
			if (i % 8 == 0) {
				count = count + 8;
			}
			input[count] = fftdata[0][i];
			input[count + 1] = fftdata[1][i];
			count = count + 2;
			if (i % 8 == 7) {
				count = count + 8;
			}
		}
		
		/*for(int i=0;i<input.length;i=i+2){
			System.out.println((i/2)+"  "+input[i]+"   "+input[i+1]);
		}*/
	}

	public double[][] calculate() {
		double in[] = new double[32];
		double out[] = new double[32];
		int count = 0;
		for (int i = 0; i < input.length; i = i + 32) {
			System.arraycopy(input, i, in, 0, 32);
			out = calc_set(in);
			for (int j = 0; j < 16; ++j) {
				ifft_out[0][count] = out[2 * j];
				ifft_out[1][count] = out[2 * j + 1];
				++count;
			}
		}

		return ifft_out;

	}

	private double[] calc_set(double[] in) {
		DoubleFFT_1D ifftDo = new DoubleFFT_1D(16);
		double[] ifft = new double[32];
		System.arraycopy(in, 0, ifft, 0, 32);
		ifftDo.complexInverse(ifft, true);
		return ifft;
	}

}


