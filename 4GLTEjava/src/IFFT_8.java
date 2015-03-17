import org.jtransforms.fft.DoubleFFT_1D;

public class IFFT_8 {
	double[] input;
	double[][] ifft_out;

	public IFFT_8(double[][] fftdata) {
		int size = fftdata[0].length;
		int ifft_size = size ;

		input = new double[2 * ifft_size];

		ifft_out = new double[2][ifft_size];
		int count = 0;
		for (int i = 0; i < fftdata[0].length; ++i) {
			
			input[count] = fftdata[0][i];
			input[count + 1] = fftdata[1][i];
			count = count + 2;
			
		}
		
		/*for(int i=0;i<input.length;i=i+2){
			System.out.println((i/2)+"  "+input[i]+"   "+input[i+1]);
		}*/
	}

	public double[][] calculate() {
		double in[] = new double[16];
		double out[] = new double[16];
		int count = 0;
		for (int i = 0; i < input.length; i = i + 16) {
			System.arraycopy(input, i, in, 0, 16);
			out = calc_set(in);
			for (int i_out = 0; i_out < out.length; i_out++) {
				double temp=(2*Math.round((Math.abs(out[i_out])-1)/2)+1);
				
				if(Double.compare(temp, 7.0)>0){
					temp =7.0;
				}
				if(Double.compare(out[i_out], 0.0)>0){
					
					out[i_out]=temp; 
				}else{
					out[i_out]= -temp;
				}
			}
			for (int j = 0; j < 8; ++j) {
				ifft_out[0][count] = out[2 * j];
				ifft_out[1][count] = out[2 * j + 1];
				++count;
			}
		}
		
	

		return ifft_out;

	}

	private double[] calc_set(double[] in) {
		DoubleFFT_1D ifftDo = new DoubleFFT_1D(8);
		double[] ifft = new double[16];
		System.arraycopy(in, 0, ifft, 0, 16);
		ifftDo.complexInverse(ifft, true);
		return ifft;
	}

}
