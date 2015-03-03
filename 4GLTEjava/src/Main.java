/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;

/**
 *
 * @author User
 */
public class Main {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) throws IOException {
		// //////////////////////////////////////////////////// don't change the
		// block size
		final int data_block_size = 40;
		// ////////////////////////////////////////////////////
		int decoder_num = 2; // this is for decoder testing purposes
		// ////////////////////////////////////////////////////
		long startTime = System.nanoTime();
		dataGen datagen = new dataGen();
		int size_data = 320 * 1; // bit size should be a multiple of 320
		datagen.run(size_data);
		long endTime = System.nanoTime(); // create data file
		long duration = (endTime - startTime);
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

		int error = 0;

		for (int i_size = 0; i_size < size_data; i_size = i_size + 320) {

			String data = read.readFile(); // read data from file
			endTime = System.nanoTime();
			duration = (endTime - startTime);
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
			double encode_time = (endTime - startTime);
			encode_time_full = encode_time_full + encode_time;
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
			Modulate mod = new Modulate();
			double[][] modulateddata = mod.modulate64QAM(encodeddata);
			endTime = System.nanoTime();
			double modulate_time = (endTime - startTime);
			modulate_time_full = modulate_time_full + modulate_time;
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
			double[][] fftout;
			FFT fft = new FFT(modulateddata);
			startTime = System.nanoTime();
			fftout = fft.calculate();
			endTime = System.nanoTime();
			double fft_time = (endTime - startTime);
			fft_time_full = fft_time_full + fft_time;
			/*
			 * System.out.println("FFT data : "); for (int i = 0; i <
			 * fftout[0].length; ++i) { System.out.println(i + " " +
			 * fftout[0][i] + "   " + fftout[1][i]); } System.out.println();
			 */
			// /////////////////////////////////////////////////////////////////////////////////////////////////IFFT
			double[][] ifftout;
			IFFT ifft = new IFFT(fftout);
			startTime = System.nanoTime();
			ifftout = ifft.calculate();
			endTime = System.nanoTime();
			double ifft_time = (endTime - startTime);
			ifft_time_full = ifft_time_full + ifft_time;
			/*
			 * System.out.println("IFFT data : "); for (int i = 0; i <
			 * ifftout[0].length; ++i) { System.out.println(i + " " +
			 * ifftout[0][i] + "   " + ifftout[1][i]); } System.out.println();
			 */
			// ///////////////////////////////////////////////////////////////////////////////////////////////////

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
			double multiplexing_time = (endTime - startTime);
			multiplexing_time_full = multiplexing_time_full + multiplexing_time;
			// ////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			//
			// Each iteration of the transmitter send 11 antenna outs from each
			// antenna
			// in sets of 12 complex numbers. So channel and receiver have to
			// run 11 iterations
			// to process all transmitted data
			//
			//
			// ////////////////////////////////////////////////////////////////////////////////////////////////////
			// System.out.println(size);
			// ///////////////////////////////////////////////////////////////////////////////////////////////////

			// /////////////////////////////////////////////////////////////////////////////////////////////////Channel
			//
			//
			double[][] antenna1_ch = new double[2][12];
			double[][] antenna2_ch = new double[2][12];
			Channel ch = new Channel("EPA 5Hz", "High", 5, 2);
			// System.out.println("size :"+antenna2[0].length);
			for (int i = 0; i < antenna2[0].length; i = i + 12) {
				// count1 = 0;
				for (int j = 0; j < 12; j++) {
					antenna1_ch[0][j] = antenna1[0][i + j];
					antenna1_ch[1][j] = antenna1[1][i + j];

					antenna2_ch[0][j] = antenna2[0][i + j];
					antenna2_ch[1][j] = antenna2[1][i + j];
					// count1++;
				}

				ch.run_channel(antenna1_ch, antenna2_ch);

			}

			//
			//
			// ////////////////////////////////////////////////////////////////////////////////////////////////K
			// best

			// /////////////////////////////////////////////////////////////////////////////////////////////////turbo
			// decoder

			Decoder dec = new Decoder();
			int decode[] = new int[data_block_size];
			int decoded_data[] = new int[data.length()];
			int count = 0;
			startTime = System.nanoTime();
			for (int i = 0; i < encodeddata.length(); i = i + 132) {

				decode = dec.decoder_log_map(encodeddata.substring(i, i + 131),
						decoder_num, data_block_size);
				for (int j = 0; j < data_block_size; ++j) {
					decoded_data[count] = decode[j];
					count++;
				}

			}
			endTime = System.nanoTime();
			double decoder_time = (endTime - startTime);
			decoder_time_full = decoder_time_full + decoder_time;

			error = error + error_calc(data, decoded_data, data.length());
			/*
			 * System.out .println("Error count = " + error + " / " +
			 * data.length());
			 */
			/*
			 * System.out
			 * .println("*********************************************************"
			 * ); System.out
			 * .println("*********************************************************"
			 * ); System.out.println();
			 */
		}

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
		System.out.println("Decoder time           : " + decoder_time_full
				/ 1000000 + " ms");
		System.out.println();
		System.out
				.println("Total time             : "
						+ (encode_time_full + modulate_time_full
								+ fft_time_full + ifft_time_full
								+ multiplexing_time_full + decoder_time_full)
						/ 1000000 + " ms");
		System.out.println();
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