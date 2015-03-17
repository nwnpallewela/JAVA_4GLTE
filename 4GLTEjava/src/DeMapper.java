import java.util.Arrays;

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
		
		
	/*	double rxtemp1[][] = ifft(fft(rx1));// use if you are not using Equalizer in testing
		double rxtemp2[][] = ifft(fft(rx2));
		*/
		
		double rxout[] = new double[32];
		for (int i = 0; i < 8; i++) {
			rxout[i] = rxtemp1[0][i + 2];
			rxout[i + 8] = rxtemp2[0][i + 2];
			rxout[i + 16] = rxtemp1[1][i + 2];
			rxout[i + 24] = rxtemp2[1][i + 2];
			
			
		}
		
		//////////////////////////////////////////////////////////////////use only you dont use Equalizer
		
		/*for (int i = 0; i < rxout.length; i++) {
			double temp=(2*Math.round((Math.abs(rxout[i])-1)/2)+1);
			
			if(Double.compare(temp, 7.0)>0){
				temp =7.0;
			}
			if(Double.compare(rxout[i], 0.0)>0){
				
				rxout[i]=temp; 
			}else{
				rxout[i]= -temp;
			}
		}*/
		/////////////////////////////////////only run above code if you dont use equalizer
		
		
		////////////////////////////////////
		
		/*
		 * System.out.println("******************* rxout "); for (int i = 0; i <
		 * rxout.length; i++) { System.out.print(" "+i+" : "+rxout[i]+"\t"); }
		 */
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
	
	private double[][] ifft(double[][] tx) {
		double in[] = new double[16];

		for (int i = 0; i < tx[0].length-4; ++i) {
			in[2 * i] = tx[0][i+2];
			in[2 * i + 1] = tx[1][i+2];

		}

		DoubleFFT_1D fftDo = new DoubleFFT_1D(8);

		fftDo.complexInverse(in,true);
		int count = 0;
		for (int j = 0; j < 8; ++j) {
			tx[0][count+2] = in[2 * j];
			tx[1][count+2] = in[2 * j + 1];
			++count;
		}

		return tx;
	}


	

}
