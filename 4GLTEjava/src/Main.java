/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;

import org.jscience.mathematics.vector.ComplexMatrix;

import weka.core.matrix.Matrix;
import weka.core.matrix.QRDecomposition;

/**
 *
 * @author User
 */
public class Main {
	static ComplexMatrix H;

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) throws IOException {
		// //////////////////////////////////////////////////// don't change the
		// block size
		final int data_block_size = 40;
		double[][] fftout;
		double[][] ifftout;
		double RX[][] = new double[2][24];
		double[][] antenna1_ch = new double[2][12];
		double[][] antenna2_ch = new double[2][12];
		Modulate mod = new Modulate();
		Channel ch = new Channel("EPA 5Hz", "Low", 2, 2);
		DeMapper DM = new DeMapper();
		Equalizer Eq = new Equalizer();
		Demodulator Dmod = new Demodulator();
		Decoder dec = new Decoder();
		Matrix Y;
		String received_data = "";
		double Y_[][] = new double[32][1];
		double y_[] = new double[32];
		// ////////////////////////////////////////////////////
		int decoder_num = 2; // this is for decoder testing purposes
		// ////////////////////////////////////////////////////
		double startTime = System.nanoTime();
		dataGen datagen = new dataGen();

		// datagen.run(size_data);

		int size_data = 320 * 100000; // bit size should be a multiple of 320

		double endTime = System.nanoTime(); // create data file
		double duration = (endTime - startTime);
		System.out
				.println("datagen runtime =  " + (duration / 1000000) + " ms");
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
			String data = read.readFile(); // read data from file
			/*
			 * System.out.println("Data length :" + data.length() + " : " +
			 * i_size / 320);
			 */read.closeFile();
			endTime = System.nanoTime();
			duration = duration + (endTime - startTime);
			/*
			 * System.out.println("readData runtime =  " + (duration / 1000000)
			 * + " ms"); // System.out.println(data.length()); System.out
			 * .println(
			 * "********************************************************************"
			 * );
			 */// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				// turbo encoding

			startTime = System.nanoTime();
			turboEncode turbo = new turboEncode();
			String encodeddata = turbo.run(data);

			endTime = System.nanoTime();
			// double encode_time = (endTime - startTime);
			encode_time_full = encode_time_full + (endTime - startTime);
			// System.out.println("encoded data length "+encodeddata.length());
			/*
			 * System.out.println("turbo encode runtime =  " + (encode_time /
			 * 1000000) + " ms");
			 */
			// System.out.println(encodeddata);

			/*
			 * System.out .println(
			 * "********************************************************************"
			 * );
			 */
			// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// modulation
			startTime = System.nanoTime();

			double[][] modulateddata = mod.modulate64QAM(encodeddata);
			endTime = System.nanoTime();
			// System.out.println("mod : "+encodeddata.length()/6);
			modulate_time_full = modulate_time_full + (endTime - startTime);
			/*
			 * System.out.println("modulating runtime =  " + (modulate_time /
			 * 1000000) + " ms");
			 */
			/*
			 * for (int i = 0; i < 2; i++) {
			 * 
			 * for (int j = 0; j < encodeddata.length() / 6; j++) {
			 * System.out.print(modulateddata[i][j] + " "); }
			 * System.out.println(); }
			 * System.out.println(modulateddata[0].length);
			 */
			/*
			 * System.out .println(
			 * "********************************************************************"
			 * );
			 */
			// ////////////////////////////////////////////////////////////////////////////////////////////////FFT
			/*
			 * for (int j = 0; j < encodeddata.length() / 6; j++) {
			 * System.out.println(modulateddata[0][j] + "+" +
			 * modulateddata[0][j] + "i"); }
			 */
			/*double mod_array[][] = new double[32][1];
			for (int i = 0; i < 16; i++) {
				mod_array[i][0] = modulateddata[0][i];
				mod_array[i + 16][0] = modulateddata[1][i];
			}
			Matrix S = Matrix.constructWithCopy(mod_array);
			System.out.println(S);*/
			FFT fft = new FFT(modulateddata);
			startTime = System.nanoTime();
			fftout = fft.calculate();
			endTime = System.nanoTime();

			fft_time_full = fft_time_full + (endTime - startTime);

			/*
			 * System.out.println("FFT data : "); for (int i = 0; i <
			 * fftout[0].length; ++i) { System.out.println(i + " " +
			 * fftout[0][i] + " + " + fftout[1][i]+"i"); } System.out.println();
			 */
			// /////////////////////////////////////////////////////////////////////////////////////////////////IFFT

			/*
			 * IFFT_8 ifft = new IFFT_8(fftout); //only use for check fft
			 * correctness startTime = System.nanoTime(); ifftout =
			 * ifft.calculate(); endTime = System.nanoTime();
			 * 
			 * ifft_time_full = ifft_time_full + (endTime - startTime);
			 */

			IFFT ifft = new IFFT(fftout);
			startTime = System.nanoTime();
			ifftout = ifft.calculate();
			endTime = System.nanoTime();

			ifft_time_full = ifft_time_full + (endTime - startTime);

			/*
			 * System.out.println("IFFT data : "); for (int i = 0; i <
			 * ifftout[0].length; ++i) { System.out.println(i + " " +
			 * ifftout[0][i] + "   " + ifftout[1][i]); } System.out.println();
			 */
			// ///////////////////////////////////////////////////////////////////////////////////////////////////
			/*
			 * FFT_12 fft12 = new FFT_12(ifftout); //use to check ifft 12 can be
			 * successfully get by fft 12
			 * 
			 * double[][] fft12out = fft12.calculate(); int count_fft1=0; for
			 * (int s = 0; s < fft12out[0].length; s++) {
			 * 
			 * System.out.print(s+"  "+fft12out[0][s]+"\t+"+fft12out[1][s]+"i : "
			 * );
			 * 
			 * // if (s % 16 == 0 || s % 16 == 1 ||s % 16 == 15 || s % 16 == 14
			 * ||s % 16 == 2 || s % 16 == 3 ||s % 16 == 13 || s % 16 == 12) { if
			 * (s % 12 == 0 || s % 12 == 1 ||s % 12 == 11 || s % 12 == 10 ) {
			 * System.out.println(0.0+"+"+0.0+"i"); }else{
			 * System.out.println(fftout
			 * [0][count_fft1]+"+"+fftout[1][count_fft1]+"i"); count_fft1++; }
			 * 
			 * }
			 */
			// ////////////////////////////////////////////////////////////////////////////////////////////

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
			// System.out.println("size :" + antenna2[0].length);
			received_data = "";
			int count_print = 0;
			int count_fft = 0;
			int count_ifft = 0;

			for (int i = 0; i < antenna2[0].length; i = i + 12) { // count1 = 0;

				startTime = System.nanoTime();
				for (int j = 0; j < 12; j++) {
					antenna1_ch[0][j] = antenna1[0][i + j];
					antenna1_ch[1][j] = antenna1[1][i + j];

					antenna2_ch[0][j] = antenna2[0][i + j];
					antenna2_ch[1][j] = antenna2[1][i + j];
					// count1++;
				}

				RX = ch.run_channel(antenna1_ch, antenna2_ch); // commented to
				// test correctness
				// /////////////////////////////////////////////////
				/*
				 * for (int j_1 = 0; j_1 < antenna2_ch[0].length; j_1++) {
				 * 
				 * RX[0][j_1] = antenna1_ch[0][j_1]; RX[1][j_1] =
				 * antenna1_ch[1][j_1]; RX[0][j_1 + 12] = antenna2_ch[0][j_1];
				 * RX[1][j_1 + 12] = antenna2_ch[1][j_1];
				 * 
				 * }
				 */
				// ///////////////////////////////////////////
				endTime = System.nanoTime();
				channel_time_full = channel_time_full + (endTime - startTime);
				// ////////////////////////////////////////////////////////////////////////
				startTime = System.nanoTime();

				y_ = DM.get_demapped_rx(RX);

				endTime = System.nanoTime();
				demapper_time_full = demapper_time_full + (endTime - startTime);
				/*
				 * System.out.println(y_.length); for (int j_2 = 0; j_2 < 16;
				 * j_2++) { System.out.print(y_[j_2] + "+" + y_[j_2 + 16] +
				 * "i\t");
				 * 
				 * } System.out.println();
				 * 
				 * for (int j_3 = 0; j_3 < 16; j_3++) {
				 * System.out.print(modulateddata[0][count_print] + "+" +
				 * modulateddata[1][count_print] + "i\t"); count_print++; }
				 * System.out.println();
				 */
				startTime = System.nanoTime();

				for (int j1 = 0; j1 < 32; j1++) {
					Y_[j1][0] = y_[j1];
				}
				Y = Matrix.constructWithCopy(Y_);

				Eq.genH(ch.getHout()); // calculate new H in Equalizer
				//Eq.RS(S);
				//System.out.println("**********************");
				endTime = System.nanoTime();
				getH_time_full = getH_time_full + (endTime - startTime);
				startTime = System.nanoTime();
				y_ = Eq.GetLSD_Y(Y); //
				endTime = System.nanoTime();

				equalizer_time_full = equalizer_time_full
						+ (endTime - startTime);

				// //////////////////////////////////////////////////////////////////////////////////
				// /////////*************************************************************************
				//
				// Below code has been checked for correctness
				//
				// ///////*******************************************************************************
				// /////////////////////////////////////////////////////////////////////////////////////////
				startTime = System.nanoTime();
				received_data = received_data + Dmod.runDemodulator(y_);
				endTime = System.nanoTime();
				demodulator_time_full = demodulator_time_full
						+ (endTime - startTime);

				/*
				 * System.out.println("********************"); for (int j = 0; j
				 * < 16; j++) { System.out.print(y_[j]+"+"+y_[j+16]+"\t");
				 * 
				 * } System.out.println(); for (int j = 0; j < 16; j++) {
				 * System.
				 * out.print(modulateddata[0][j+i]+"+"+modulateddata[1][j+
				 * i]+"\t");
				 * 
				 * } System.out.println("******************");
				 */
				/*
				 * System.out.println("y array: "); for (int j = 0; j <
				 * y_.length; j++) { System.out.print(y_[j]+"\t "); }
				 * System.out.println();
				 */

				/*
				 * System.out.println("Y dash : "); System.out.println(Y);
				 * 
				 * System.out.println("R : "); System.out.println(R);
				 */
				/*
				 * System.out.println("Y Array : "+ Y.length); for (int j = 0; j
				 * < Y.length; j++) { System.out.print(Y[j]+"\t"); }
				 * System.out.println();
				 */
				// decoder
				/*
				 * System.out.println(received_data.length() + " : " +
				 * encodeddata.length());
				 */

			}
			/*
			 * System.out.println(received_data.length() + " : " +
			 * encodeddata.length());
			 */
			/* System.out.println("Received data : "+received_data); */
			/* System.out.println("Encoded  data : "+encodeddata); */
			int decode[] = new int[data_block_size];
			int decoded_data[] = new int[data.length()];
			int count = 0;
			startTime = System.nanoTime();
			for (int i1 = 0; i1 < encodeddata.length(); i1 = i1 + 132) {

				decode = dec.decoder_log_map(
						received_data.substring(i1, i1 + 131), decoder_num,
						data_block_size);
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

		System.out
				.println("Receiver Total time             : "
						+ (decoder_time_full + demodulator_time_full
								+ demapper_time_full + equalizer_time_full + getH_time_full)
						/ 1000000 + " ms");
		// +
		// decoder_time_full+demodulator_time_full+demapper_time_full+equalizer_time_full
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
			// cout << " ERROR : Bit counts are different : " <<
			// rawdata_in.length()
			// << " " << count << endl;
			return 677;
		}
	}

}