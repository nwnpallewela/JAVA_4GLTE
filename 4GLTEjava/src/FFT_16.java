
import org.jtransforms.fft.DoubleFFT_1D;

public class FFT_16 {

	double[] input;
	double[][] fft_out;

	public FFT_16(double[][] modulateddata) {
		int size=0;
		
			size=modulateddata[0].length;
		
		
		input = new double[2 * size];
		
		fft_out=new double[2][size];
		for (int i = 0; i < modulateddata[0].length; ++i) {
			input[2 * i] = modulateddata[0][i];
			input[2 * i + 1] = modulateddata[1][i];

		}
	}

	public double[][] calculate() {
		double in[]=new double[32];
		double out[]=new double[32];
		int count=0;
		for(int i=0;i<input.length;i=i+32){
			System.arraycopy(input, i, in, 0, 32);
			out=calc_set(in);
			for(int j=0;j<16;++j){
				fft_out[0][count]=out[2*j];
				fft_out[1][count]=out[2*j+1];
				++count;
			}
		}
		
		return fft_out;

	}
	private double[] calc_set(double[] in){
		DoubleFFT_1D fftDo = new DoubleFFT_1D(16);
		double[] fft = new double[32];
		System.arraycopy(in, 0, fft, 0, 32);
		fftDo.complexForward(fft);
		return fft;
	}

}

