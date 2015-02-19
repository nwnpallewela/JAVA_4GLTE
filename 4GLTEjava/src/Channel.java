import weka.core.matrix.*;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;

public class Channel {

	String channel_type;
	String corr_type;
	double fcarry;
	double sigma;

	public Channel(String channel_type, String corr_type, double fcarry,
			double sigma) {
		this.channel_type = channel_type;
		this.corr_type = corr_type;
		this.fcarry = fcarry;
		this.sigma = sigma;

	}

	public void run_channel(double[][] tx1, double[][] tx2) {

		double tx_corr_coeff = 0.0;
		double rx_corr_coeff = 0.0;

		switch (corr_type) {
		case "Low":
			tx_corr_coeff = 0;
			rx_corr_coeff = 0;
			break;

		case "Medium":
			tx_corr_coeff = 0.3;
			rx_corr_coeff = 0.9;
			break;

		default:
			tx_corr_coeff = 0.9;
			rx_corr_coeff = 0.9;

		}
		double dopp_freq = 6;
		double no_taps = 0;
		int path_delays[] = { 0, 30, 70, 90, 110, 190, 410, 0, 0 };
		double path_gains[] = { 0, -1, -2, -3, -8, -17.2, -20.8, 0, 0 };
		/*
		 * int[] path_delays; double[] path_gains;
		 */
		if (channel_type.equals("EPA 5Hz")) {

			dopp_freq = dopp_freq-1;//5
			no_taps = 7;
		} else if (channel_type.equals("EVA 5Hz")) {
			path_delays[2] = 150;
			path_delays[3] = 310;
			path_delays[4] = 370;
			path_delays[5] = 710;
			path_delays[6] = 1090;
			path_delays[7] = 1730;
			path_delays[8] = 2510;

			path_gains[1] = -1.5;
			path_gains[2] = -1.4;
			path_gains[3] = -3.6;
			path_gains[4] = -0.6;
			path_gains[5] = -9.1;
			path_gains[6] = -7;
			path_gains[7] = -12;
			path_gains[8] = -16.9;
			dopp_freq = 5;
			no_taps = 9;
		} else if (channel_type.equals("EVA 70Hz")) {
			path_delays[2] = 150;
			path_delays[3] = 310;
			path_delays[4] = 370;
			path_delays[5] = 710;
			path_delays[6] = 1090;
			path_delays[7] = 1730;
			path_delays[8] = 2510;

			path_gains[1] = -1.5;
			path_gains[2] = -1.4;
			path_gains[3] = -3.6;
			path_gains[4] = -0.6;
			path_gains[5] = -9.1;
			path_gains[6] = -7;
			path_gains[7] = -12;
			path_gains[8] = -16.9;

			dopp_freq = 70;
			no_taps = 9;
		} else if (channel_type.equals("ETU 70Hz")) {
			path_delays[1] = 50;
			path_delays[2] = 120;
			path_delays[3] = 200;
			path_delays[4] = 230;
			path_delays[5] = 500;
			path_delays[6] = 1600;
			path_delays[7] = 2300;
			path_delays[8] = 5000;

			path_gains[0] = -1;
			path_gains[1] = -1;
			path_gains[2] = -1;
			path_gains[3] = 0;
			path_gains[4] = 0;
			path_gains[5] = 0;
			path_gains[6] = -3;
			path_gains[7] = -5;
			path_gains[8] = -7;
			dopp_freq = 70;
			no_taps = 9;
		} else {// ETU 300Hz
			path_delays[1] = 50;
			path_delays[2] = 120;
			path_delays[3] = 200;
			path_delays[4] = 230;
			path_delays[5] = 500;
			path_delays[6] = 1600;
			path_delays[7] = 2300;
			path_delays[8] = 5000;

			path_gains[0] = -1;
			path_gains[1] = -1;
			path_gains[2] = -1;
			path_gains[3] = 0;
			path_gains[4] = 0;
			path_gains[5] = 0;
			path_gains[6] = -3;
			path_gains[7] = -5;
			path_gains[8] = -7;
			dopp_freq = 300;
			no_taps = 9;
		}
		// ////////////////////////////////////////

		double[][] valstx = { { 1., tx_corr_coeff }, { tx_corr_coeff, 1. } };
		double[][] valsrx = { { 1., rx_corr_coeff }, { rx_corr_coeff, 1. } };
		/*
		 * Matrix tx_corr_matrix = new Matrix(valstx); Matrix rx_corr_matrix =
		 * new Matrix(valsrx);
		 */
		double[][] corr_matrix_val = {
				{ valstx[0][0] * valsrx[0][0], valstx[0][0] * valsrx[0][1],
						valstx[0][1] * valsrx[0][0],
						valstx[0][1] * valsrx[0][1] },

				{ valstx[0][0] * valsrx[1][0], valstx[0][0] * valsrx[1][1],
						valstx[0][1] * valsrx[1][0],
						valstx[0][1] * valsrx[1][1] },

				{ valstx[1][0] * valsrx[0][0], valstx[1][0] * valsrx[0][1],
						valstx[1][1] * valsrx[0][0],
						valstx[1][1] * valsrx[0][1] },

				{ valstx[1][0] * valsrx[1][0], valstx[1][0] * valsrx[1][1],
						valstx[1][1] * valsrx[1][0],
						valstx[1][1] * valsrx[1][1] } };

		Matrix corr_matrix = new Matrix(corr_matrix_val);

		Matrix sqrt_corr_matrix = corr_matrix.sqrt();

		/*
		 * System.out.println("Printing channel");
		 * System.out.println("Corelation matrix : ");
		 * System.out.println(corr_matrix);
		 * System.out.println("Square root matrix : ");
		 * System.out.println(sqrt_corr_matrix);
		 * System.out.println("Square root matrix * Square root matrix : ");
		 * System.out.println(sqrt_corr_matrix.times(sqrt_corr_matrix));
		 */

		int l = tx1[0].length;

		double[] f = new double[l * 2];
		for (int k = 0; k < l; ++k) {

			f[k] = fcarry - 59 * 15 * 0.000001 + 15 * 0.000001 * k;
			f[l + k] = fcarry - 59 * 15 * 0.000001 + 15 * 0.000001 * k;

		}

		Complex H_array[][] = new Complex[2 * l][2 * l];
		ComplexMatrix H = ComplexMatrix.valueOf(H_array);

		Complex tr_1_coeff = Complex.valueOf(0.0, 0.0);
		Complex tr_2_coeff = Complex.valueOf(0.0, 0.0);
		Complex tr_1_calc = Complex.valueOf(0.0, 0.0);
		Complex tr_2_calc = Complex.valueOf(0.0, 0.0);
		Complex A_array[] = new Complex[4];
		// ////////////////////////////////////////////////////////////////////////////////
		//generate random numbers for A
		
		/////////////////////////////////////////////////////////////////////////////////////

		
		Complex B_array[] = new Complex[4];
		for (int i = 0; i < 4; ++i) {
			B_array[i] = (A_array[0].times(sqrt_corr_matrix.get(0, i)))
					.plus((A_array[1].times(sqrt_corr_matrix.get(1, i)))
							.plus((A_array[2].times(sqrt_corr_matrix.get(2, i)))
									.plus((A_array[3].times(sqrt_corr_matrix
											.get(3, i))))));
		}
		// ComplexMatrix B = ComplexMatrix.valueOf(B_array);
		for (int k = 0; k < l; ++k) {
			
			// /////////////////////////////////////////////////////////////////
			// generate random numbers for A
			// /////////////////////////////////////////////////////////////////
			for (int i = 0; i < 4; ++i) {
				B_array[i] = (A_array[0].times(sqrt_corr_matrix.get(0, i)))
						.plus((A_array[1].times(sqrt_corr_matrix.get(1, i)))
								.plus((A_array[2].times(sqrt_corr_matrix.get(2,
										i))).plus((A_array[3]
										.times(sqrt_corr_matrix.get(3, i))))));
			}
			// B = ComplexMatrix.valueOf(B_array);

			tr_1_coeff = Complex.valueOf(1.0, 0.0);
			tr_2_coeff = Complex.valueOf(1.0, 0.0);
			// cout<<"EXP : "<<tr_1_coeff(0, 0)<<" : "<<exp(tr_1_coeff(0,
			// 0))<<endl;
			// for m =1:no_taps
			for (int m = 0; m < no_taps; ++m) {
				tr_1_calc = Complex.valueOf(0.0, 2.0 * Math.PI * f[k]
						* path_delays[m]);
				tr_2_calc = Complex.valueOf(0.0, 2.0 * Math.PI * f[k + 8]
						* path_delays[m]);

				// cout << "************** " << m << " " << k
				// <<" EXP : "<<(tr_1_calc(0, 0))<<" : "<<exp(tr_1_calc(0, 0))<<
				// endl;

				tr_2_coeff = (Complex.valueOf(10.0, 0).pow((tr_1_calc.exp()
						.times(path_gains[m])))).sqrt().plus(tr_1_coeff);
				tr_2_coeff = (Complex.valueOf(10.0, 0).pow((tr_2_calc.exp()
						.times(path_gains[m])))).sqrt().plus(tr_2_coeff);
			}

			/*
			 * H_array[k][ k]= B.get(0, 0).times(tr_1_coeff); // 2 by 2 MIMO -->
			 * 4 Paths H_array[k][ k + l] = B.get(1, 0).times(tr_2_coeff);
			 * H_array[k + l][ k] = B.get(2, 0).times(tr_1_coeff);
			 * 
			 * H_array[k + l][ k + l] = B.get(3, 0).times(tr_2_coeff);
			 */
			
			// 2 by 2 MIMO --> 4Paths
			H_array[k][k] = B_array[0].times(tr_1_coeff);
			H_array[k][k + l] = B_array[1].times(tr_2_coeff);
			H_array[k + l][k] = B_array[2].times(tr_1_coeff);
			H_array[k + l][k + l] = B_array[3].times(tr_2_coeff);

		}

		H = ComplexMatrix.valueOf(H_array);
		//////////////////////////////////////
		H=H.times(H);///////////////////////////////////nothing special
		
		/////////////////////////////////////////////////
		//noise = randn(24,1)+1i*randn(24,1); // specific for 24
		//arma::cx_mat noise = randn < cx_mat > (2 * l, 1);
	///////////////////////////////////////////////////////////////////////////////////////////
		//arma::cx_mat R = H*[fft(Tx1); fft(Tx2)] + sigma*noise;

//		arma::cx_mat Rx1 = ifft(R(1:8));
//		arma::cx_mat Rx2 = ifft(R(9:16));
		///////////////////////////////////////////////////////////////////////////////////////

		//arma::cx_mat H_1 = zeros < cx_mat > (8, 8);

		
	/*	for (int y = 0; y < 8; ++y) {
			for (int z = 0; z < 8; ++z) {
				H_1(y, z) = H(y + 2, z + 2);
			}
		}

		for (int y = 0; y < 8; ++y) {
			for (int z = 8; z < 16; ++z) {
				H_1(y, z) = H(y + 2, z + 6);
			}
		}

		for (int y = 8; y < 16; ++y) {
			for (int z = 0; z < 8; ++z) {
				H_1(y, z) = H(y + 6, z + 2);
			}
		}

		for (int y = 8; y < 16; ++y) {
			for (int z = 8; z < 16; ++z) {
				H_1(y, z) = H(y + 6, z + 6);
			}
		}*/
	}
}
