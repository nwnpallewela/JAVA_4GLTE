import org.jscience.mathematics.vector.ComplexMatrix;
import org.jtransforms.fft.*;


public class IFFT_zeroforcing {
	
		double[] input;
		double[][] ifft_out;

		public IFFT_zeroforcing() {
			int size = 16;
			int ifft_size = size ;

			input = new double[2 * ifft_size];

			ifft_out = new double[2][ifft_size];
			
			
		}

		public double[][] calculate(ComplexMatrix Y) {
			double in[] = new double[16];
			double out[] = new double[16];
		//	System.out.println("this is y");
			//System.out.println(Y);
			int count = 0;
			for (int i = 0; i < 9; i = i + 8) {
				
				for (int j = 0; j <8; j++) {
				//	System.out.println("**************"+i+" "+j+" "+Y.get( (j+i),0));
					in[2*j]=Y.get( (j+i),0).getReal();
					in[2*j+1]=Y.get((j+i),0).getImaginary();
					
				}
				
				out = calc_set(in);
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
