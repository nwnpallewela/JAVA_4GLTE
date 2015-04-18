
import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jtransforms.fft.DoubleFFT_1D;

public class DeMapper {

	public double[] get_demapped_rx(double[][] rx) {
		double rx1[][] = new double[2][12];
		double rx2[][] = new double[2][12];
		for (int i = 0; i < 12; i++) {
			rx1[0][i] = rx[0][i];
			rx1[1][i] = rx[1][i];
			rx2[0][i] = rx[0][i + 12];
			rx2[1][i] = rx[1][i + 12];
		}
		double rxtemp1[][] = fft(rx1);
		double rxtemp2[][] = fft(rx2);
		
	
		
		double rxout[] = new double[32];
		for (int i = 0; i < 8; i++) {
			rxout[i] = rxtemp1[0][i + 2];
			rxout[i + 8] = rxtemp2[0][i + 2];
			rxout[i + 16] = rxtemp1[1][i + 2];
			rxout[i + 24] = rxtemp2[1][i + 2];
			
			
		}
		
	
		return rxout;
	}
	public double[] get_demapped_rx_ZF(double[][] rx,ComplexMatrix H) {
		double rx1[][] = new double[2][12];
		double rx2[][] = new double[2][12];
		Complex Y[][]=new Complex[24][1];
		for (int i = 0; i < 12; i++) {
			rx1[0][i] = rx[0][i];
			rx1[1][i] = rx[1][i];
			rx2[0][i] = rx[0][i + 12];
			rx2[1][i] = rx[1][i + 12];
		}
		double rxtemp1[][] = fft(rx1);
		double rxtemp2[][] = fft(rx2);
		
		for (int i = 0; i < 12; i++) {
			 Y[i][0] =Complex.valueOf(rxtemp1[0][i],  rxtemp1[1][i]);// rxtemp1[0][i];
			
			 Y[i + 12][0] =Complex.valueOf(rxtemp2[0][i],  rxtemp2[1][i]);
			/* Y[i + 24][0] = rxtemp2[1][i];*/
			
			
		}
		ComplexMatrix Ym=ComplexMatrix.valueOf(Y);
	//	System.out.println(Ym);
	//	System.out.println(H);
		Ym=H.times(Ym);
		
		
		
		double rxout[] = new double[32];
		for (int i = 0; i < 8; i++) {
			rxout[i] = Ym.get(i+2, 0).getReal();//rxtemp1[0][i + 2];
			rxout[i + 8] = Ym.get(i+14, 0).getReal();//rxtemp2[0][i + 2];
			rxout[i + 16] =Ym.get(i+2, 0).getImaginary();// rxtemp1[1][i + 2];
			rxout[i + 24] =Ym.get(i+14, 0).getImaginary();// rxtemp2[1][i + 2];
			
			
		}
		
	
		return rxout;
	}
	
	
	private double[][] fft(double[][] tx) {
		double in[] = new double[24];

		for (int i = 0; i < tx[0].length; ++i) {
			in[2 * i] = tx[0][i];
			in[2 * i + 1] = tx[1][i];

		}

		DoubleFFT_1D fftDo = new DoubleFFT_1D(12);

		fftDo.complexForward(in);
		int count = 0;
		for (int j = 0; j < 12; ++j) {
			tx[0][count] = in[2 * j];
			tx[1][count] = in[2 * j + 1];
			++count;
		}

		return tx;
	}
	



	

}
