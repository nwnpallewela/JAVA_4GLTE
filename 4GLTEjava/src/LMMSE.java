import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;

import weka.core.matrix.Matrix;

public class LMMSE {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// //////////////////////////////////////////////////// don't change the
		// block size
		ComplexMatrix H;
		ComplexMatrix H_her;
		Complex H_her_array[][] = new Complex[24][24];
	
		ComplexMatrix C;
		ComplexMatrix C_her;
		Complex C_her_array[][] = new Complex[24][24];
		ComplexMatrix Ydash;
		//ComplexMatrix Pd;
		Complex Pd_array[][] = new Complex[24][24];

		for (int i = 0; i < 24; i++) {

			for (int j = 0; j < 24; j++) {
				if(i==j){
					Pd_array[i][i] = Complex.valueOf(42.0, 0.0);
				if (i == 0 || i == 1 || i == 10 || i == 11 || i == 12
						|| i == 13 || i == 22 || i == 23) {
					Pd_array[i][j] = Complex.valueOf(0.0, 0.0);
				} 
				}else{
					Pd_array[i][j] = Complex.valueOf(0.0, 0.0);
					
				}
			}
		}
		ComplexMatrix Pd = ComplexMatrix.valueOf(Pd_array);
		//System.out.println(Pd);
		Complex Y_array[][] = new Complex[16][1];

		double[][] y_out = new double[2][16];
		final int data_block_size = 40;
		double[][] fftout;
		double[][] ifftout;
		double RX[][] = new double[2][24];
		double[][] antenna1_ch = new double[2][12];
		double[][] antenna2_ch = new double[2][12];
		Modulate mod = new Modulate();
		IFFT_zeroforcing IFFT_z = new IFFT_zeroforcing();
		ML_zeroforcing ML = new ML_zeroforcing();
		DeMapper DM = new DeMapper();

		Demodulator Dmod = new Demodulator();
		Decoder dec = new Decoder();
		Channel ch = new Channel("EPA 5Hz", "Low", 2, dec.getSigma()
				/ Math.sqrt(2)); // Eb_No=63 sigma=0.24 sigma^2=0.05
		double Rsq = (20.36 * dec.getSigma() * dec.getSigma());
		Rsq = 100000;
		
		Complex sigma_mat_array[][] = new Complex[24][24];
		for (int i = 0; i < 24; i++) {

			for (int j = 0; j < 24; j++) {
				if(i==j){
					sigma_mat_array[i][i] = Complex.valueOf(dec.getSigma() * dec.getSigma(), 0.0);
				
				}else{
					sigma_mat_array[i][j] = Complex.valueOf(0.0, 0.0);
					
				}
			}
		}
		ComplexMatrix sigma_mat=ComplexMatrix.valueOf(sigma_mat_array);
		Equalizer Eq = new Equalizer(Rsq);
		// Equalizer Eq = new Equalizer();
		Matrix Y;
		String received_data = "";
		double Y_[][] = new double[32][1];
		double y_[] = new double[32];
		// ////////////////////////////////////////////////////
		// int decoder_num = 2; // this is for decoder testing purposes
		double lc = 2.5;
		int iterations = 6;
		double LLR[] = new double[40];

		/*
		 * double LLR1[] = new double[40]; double LLR2[] = new double[40];
		 * double A1[][] = new double[17][44]; double A2[][] = new
		 * double[17][44];
		 */
		// int I1[][] = new int[40][2];
		int I2[][] = new int[40][2];
		double leuk1[] = new double[40];
		double leuk2[] = new double[40];
		double luk[] = new double[40];
		double R1[] = new double[40];
		double R2[] = new double[40];
		int y1[] = new int[40];
		int y2[] = new int[40];
		double LEUK[] = new double[40];
		// ////////////////////////////////////////////////////
		double startTime = System.nanoTime();

		int size_data = 320 * 10000; // bit size should be a multiple of 320

		double endTime = System.nanoTime(); // create data file
		double duration = (endTime - startTime);
		System.out
				.println("datagen runtime =  " + (duration / 1000000) + " ms");
		System.out.println(Rsq);
		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		startTime = System.nanoTime();
		readData read = new readData();
		read.openFile();

		// //////////////////////////////////////////////////////////////////////////////
		// //////////////////////////////////////////////////////////////////////////////
		double encode_time_full = 0;
		double modulate_time_full = 0;
		double fft_time_full = 0;
		double ifft_time_full = 0;
		double multiplexing_time_full = 0;
		double decoder_time_full = 0;
		double channel_time_full = 0;
		double demapper_time_full = 0;
		double equalizer_time_full = 0;
		double demodulator_time_full = 0;
		double getH_time_full = 0.0;

		int error = 0;
		duration = 0;
		double total_time = System.nanoTime();
		for (int i_size = 0; i_size < size_data; i_size = i_size + 320) {
			startTime = System.nanoTime();
			/*
			 * datagen.openFile(); datagen.run(320); datagen.closeFile();
			 */
			read.openFile();
			String data = read.readFile(); // ///////////////////////////////////////////////////////////////////////
											// read data from file
			read.closeFile();
			endTime = System.nanoTime();
			duration = duration + (endTime - startTime);

			// /
			// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// //////////////////////////////////////////////////////////////////////////////////////////////////////
			// turbo encoding

			startTime = System.nanoTime();
			turboEncode turbo = new turboEncode();
			String encodeddata = turbo.run(data);

			endTime = System.nanoTime();
			encode_time_full = encode_time_full + (endTime - startTime);

			// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// ////////////////////////////////////////////////////////////////////////////////////////////////////
			// modulation
			startTime = System.nanoTime();

			double[][] modulateddata = mod.modulate64QAM(encodeddata);
			endTime = System.nanoTime();
			modulate_time_full = modulate_time_full + (endTime - startTime);

			// ////////////////////////////////////////////////////////////////////////////////////////////////////FFT

			FFT fft = new FFT(modulateddata);
			startTime = System.nanoTime();
			fftout = fft.calculate();
			endTime = System.nanoTime();

			fft_time_full = fft_time_full + (endTime - startTime);

			// ///////////////////////////////////////////////////////////////////////////////////////////////////IFFT

			IFFT ifft = new IFFT(fftout);
			startTime = System.nanoTime();
			ifftout = ifft.calculate();
			endTime = System.nanoTime();

			ifft_time_full = ifft_time_full + (endTime - startTime);

			// ///////////////////////////////////////////////////////////////////////////////////////////////////multiplexing

			int size = 0;
			if ((ifftout[0].length / 2) % 12 != 0) {
				size = (ifftout[0].length / 2)
						+ (12 - (ifftout[0].length / 2) % 8);
			} else {
				size = (ifftout[0].length / 2);
			}

			double[][] antenna1 = new double[2][size];
			double[][] antenna2 = new double[2][size];
			int count1 = 0;
			int count2 = 0;
			startTime = System.nanoTime();
			for (int i = 0; i < ifftout[0].length; ++i) {
				if (i % 24 >= 12) {
					antenna2[0][count2] = ifftout[0][i];
					antenna2[1][count2] = ifftout[1][i];
					count2++;
				} else {
					antenna1[0][count1] = ifftout[0][i];
					antenna1[1][count1] = ifftout[1][i];
					count1++;
				}

			}

			endTime = System.nanoTime();

			multiplexing_time_full = multiplexing_time_full
					+ (endTime - startTime);

			// ///////////////////////////////////////////////////////////
			// /////////////*******************************************************************************
			// Transmitter ends from here
			//
			// Above code has been checked for correctness
			//
			// ////////////********************************************************************************
			// ////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			//
			// Each iteration of the transmitter send 11 antenna outs from each
			// antenna
			// in sets of 12 complex numbers. So channel and receiver have to
			// run 11 iterations
			// to process all transmitted data
			//
			// ////////////////////////////////////////////////////////////////////////////////////////////////////
			// ///////////////////////////////////////////////////////////////////////////////////////////////////

			// /////////////////////////////////////////////////////////////////////////////////////////////////Channel
			//
			//

			//
			received_data = "";

			for (int i = 0; i < antenna2[0].length; i = i + 12) { // count1 = 0;

				startTime = System.nanoTime();
				for (int j = 0; j < 12; j++) {
					antenna1_ch[0][j] = antenna1[0][i + j];
					antenna1_ch[1][j] = antenna1[1][i + j];

					antenna2_ch[0][j] = antenna2[0][i + j];
					antenna2_ch[1][j] = antenna2[1][i + j];
					// count1++;
				}

				RX = ch.run_channel(antenna1_ch, antenna2_ch); // /////////////////////////////////////////////Run
																// channel to
																// get Rx
				// test correctness
				// /////////////////////////////////////////////////

				// ///////////////////////////////////////////
				endTime = System.nanoTime();
				channel_time_full = channel_time_full + (endTime - startTime);
				// //////////////////////////////////////////////////////////////////////////////////////////De-mapping
				// Rx
				startTime = System.nanoTime();
				H=ch.getH();
				//H_her=H;
				for (int j = 0; j < 24; j++) {
					for (int k = 0; k < 24; k++) {
						H_her_array[j][k]= Complex.valueOf(H.get(j, k).getReal(), (-1)*H.get(j, k).getImaginary());  //(i, j)=Complex ;// H.get(i, j).getImaginary();
					}
				}
				H_her=ComplexMatrix.valueOf(H_her_array);
				H_her=H_her.transpose();
				//System.out.println(Pd.times(H).times(H_her).plus(sigma_mat));
				C=Pd.times(H).times(H_her).plus(sigma_mat).inverse().times(Pd).times(H);
				//System.out.println(C_her);
				for (int j = 0; j < 24; j++) {
					for (int k = 0; k < 24; k++) {
						C_her_array[j][k]= Complex.valueOf(C.get(j, k).getReal(), (-1)*C.get(j, k).getImaginary());  //(i, j)=Complex ;// H.get(i, j).getImaginary();
					}
				}
				C_her=ComplexMatrix.valueOf(C_her_array);
				C_her=C_her.transpose();
				//H = Pd.times(ch.getH()).times(ch.getH().);//ch.getH().inverse();
				// y_ = DM.get_demapped_rx(RX);
				y_ = DM.get_demapped_rx_ZF(RX, C_her);
				endTime = System.nanoTime();
				demapper_time_full = demapper_time_full + (endTime - startTime);
				// //////////////////////////////////////////////////////////////////////////////////////////
				startTime = System.nanoTime();

				for (int j1 = 0; j1 < 16; j1++) {
					// Y_[j1][0] = y_[j1];
					Y_array[j1][0] = Complex.valueOf(y_[j1], y_[j1 + 16]);
				}

				// Y = Matrix.constructWithCopy(Y_);
				Ydash = ComplexMatrix.valueOf(Y_array);
				// System.out.println(Ydash);

				// System.out.println(H);

				// Eq.genH(ch.getHout());
				// /////////////////////////////////////////////////////////////////////calculate
				// new H in Equalizer
				endTime = System.nanoTime();

				getH_time_full = getH_time_full + (endTime - startTime);
				// //////////////////////////////////////////////////////////////////////////////////////////
				// //////////////////////////////////////////////////////////////////////////////////////////
				// Running Equalizer and get Y
				startTime = System.nanoTime();
				// y_ = Eq.GetLSD_Y(Y, Rsq); //
				// Ydash = H.times(Ydash);
				// System.out.println(Ydash);

				y_out = IFFT_z.calculate(Ydash);
				y_ = ML.Decision(y_out);
				endTime = System.nanoTime();

				equalizer_time_full = equalizer_time_full
						+ (endTime - startTime);

				/*
				 * for (int k = 0; k < y_out.length; k++) {
				 * System.out.println(y_out[0][k]+" + "+y_out[1][k]+" i"); }
				 */

				// //////////////////////////////////////////////////////////////////////////////////
				// /////////*************************************************************************
				//
				// Below code has been checked for correctness
				//
				// ///////*******************************************************************************
				// /////////////////////////////////////////////////////////////////////////////////////////Running
				// demodulator
				startTime = System.nanoTime();
				received_data = received_data + Dmod.runDemodulator(y_);
				endTime = System.nanoTime();
				demodulator_time_full = demodulator_time_full
						+ (endTime - startTime);

			}

			int decode[] = new int[data_block_size];
			int decoded_data[] = new int[data.length()];
			int count = 0;
			startTime = System.nanoTime();
			for (int i1 = 0; i1 < encodeddata.length(); i1 = i1 + 132) {
				// ////////////////////////////////////////////////////////////////////////////////////////trubo
				// decoder
				dec.decoder_log_map_it(received_data.substring(i1, i1 + 132),
						1, data_block_size, luk);

				leuk1 = dec.get_leuk();

				y1 = dec.getY();
				dec.decoder_log_map_it(received_data.substring(i1, i1 + 132),
						2, data_block_size, leuk1);
				// LLR2 = dec.getLLR1();

				I2 = dec.get_interleave_table();
				leuk2 = dec.get_leuk();
				R2 = dec.getR();
				y2 = dec.getY();
				LEUK = leuk2; // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

				lc = dec.getLC();
				for (int n = 3; n <= iterations; ++n) { // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
					if ((n % 2) == 1) {
						for (int i = 0; i < data_block_size; ++i) {
							LLR[i] = R1[i] + LEUK[i];
							LEUK[i] = LLR[i] - LEUK[i] - 2 * lc * y1[2 * i];
						}
					} else {
						for (int i = 0; i < data_block_size; ++i) {
							LLR[I2[i][1]] = R2[i] + LEUK[I2[i][1]];// %%%%%%%%%%%%%%%%%%%%%%%
							LEUK[I2[i][1]] = LLR[I2[i][1]] - LEUK[I2[i][1]] - 2
									* lc * y2[2 * i]; // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
						}

					}

				}
				for (int i = 0; i < 40; i++) {

					if (LLR[i] > 0) {
						decode[i] = 1;
					} else {
						decode[i] = 0;
					}

				}
				// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

				for (int j = 0; j < data_block_size; ++j) {
					decoded_data[count] = decode[j];
					count++;
				}

			}
			endTime = System.nanoTime();

			decoder_time_full = decoder_time_full + (endTime - startTime);

			error = error + error_calc(data, decoded_data, data.length());

		}

		total_time = System.nanoTime() - total_time - duration;
		System.out
				.println("*********************************************************");
		read.closeFile();
		System.out.println();
		System.out.println("Encoding time          : " + encode_time_full
				/ 1000000 + " ms");
		System.out.println("Modulation time        : " + modulate_time_full
				/ 1000000 + " ms");
		System.out.println("FFT time               : " + fft_time_full
				/ 1000000 + " ms");
		System.out.println("IFFT time              : " + ifft_time_full
				/ 1000000 + " ms");
		System.out.println("Multiplexing time      : " + multiplexing_time_full
				/ 1000000 + " ms");
		System.out.println();
		System.out.println("Channel time           : " + channel_time_full
				/ 1000000 + " ms");
		System.out.println();
		System.out.println("Demapper time          : " + demapper_time_full
				/ 1000000 + " ms");
		System.out.println("GetH time         : " + getH_time_full / 1000000
				+ " ms");
		System.out.println("Equalizer time         : " + equalizer_time_full
				/ 1000000 + " ms");
		System.out.println("Demodulator time       : " + demodulator_time_full
				/ 1000000 + " ms");
		System.out.println("Decoder time           : " + decoder_time_full
				/ 1000000 + " ms");
		System.out.println();
		System.out.println("Transmitter Total time             : "
				+ (encode_time_full + modulate_time_full + fft_time_full
						+ ifft_time_full + multiplexing_time_full) / 1000000
				+ " ms");

		System.out.println("Receiver Total time             : "
				+ (decoder_time_full + demodulator_time_full
						+ demapper_time_full + equalizer_time_full) / 1000000
				+ " ms");
		System.out.println();
		System.out.println("Total Time with every thing: " + (total_time)
				/ 1000000 + " ms");
		System.out.println("Error count = " + error + " / " + size_data);

	}

	static int error_calc(String rawdata_in, int[] decoded_data, int count) {
		int error = 0;
		char rawdata[] = rawdata_in.toCharArray();
		if (rawdata_in.length() == count) {
			for (int i = 0; i < rawdata_in.length(); ++i) {
				if (((int) rawdata[i] - 48) != decoded_data[i]) {
					error++;
				}
			}
			return error;
		} else {
			return 677;
		}
	}

}
