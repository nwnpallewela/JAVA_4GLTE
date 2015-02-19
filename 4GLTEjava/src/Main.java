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
		// TODO code application logic here
		int data_block_size = 40;
		int decoder_num = 2;
		long startTime = System.nanoTime();
		dataGen datagen = new dataGen();
		datagen.run(400);
		long endTime = System.nanoTime(); // create data file
		long duration = (endTime - startTime);
		System.out
				.println("datagen runtime =  " + (duration / 1000000) + " ms");
		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		startTime = System.nanoTime();
		readData read = new readData();
		String data = read.run(); // read data from file
		endTime = System.nanoTime();
		duration = (endTime - startTime);
		System.out.println("readData runtime =  " + (duration / 1000000)
				+ " ms");
		// System.out.println(data.length());
		System.out
				.println("********************************************************************");
		// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// turbo encoding
		startTime = System.nanoTime();
		turboEncode turbo = new turboEncode();
		String encodeddata = turbo.run(data);
		endTime = System.nanoTime();
		double encode_time = (endTime - startTime);

		System.out.println(encodeddata.length());
	/*	System.out.println("turbo encode runtime =  " + (encode_time / 1000000)
				+ " ms");*/
		// System.out.println(encodeddata);

		System.out
				.println("********************************************************************");

		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// modulation
		startTime = System.nanoTime();
		Modulate mod = new Modulate();
		double[][] modulateddata = mod.modulate64QAM(encodeddata);
		endTime = System.nanoTime();
		double modulate_time = (endTime - startTime);
		/*System.out.println("modulating runtime =  " + (modulate_time / 1000000)
				+ " ms");*/
		/*
		 * for (int i = 0; i < 2; i++) {
		 * 
		 * for (int j = 0; j < encodeddata.length() / 6; j++) {
		 * System.out.print(modulateddata[i][j] + " "); } System.out.println();
		 * } System.out.println(modulateddata[0].length);
		 */
		System.out
				.println("********************************************************************");
		// ////////////////////////////////////////////////////////////////////////////////////////////////FFT
		double[][] fftout;
		FFT fft = new FFT(modulateddata);
		startTime = System.nanoTime();
		fftout = fft.calculate();
		endTime = System.nanoTime();
		double fft_time = (endTime - startTime);
		/*System.out.println("FFT data : ");
		for (int i = 0; i < fftout[0].length; ++i) {
			System.out.println(i + " " + fftout[0][i] + "   " + fftout[1][i]);
		}
		System.out.println();*/
		// /////////////////////////////////////////////////////////////////////////////////////////////////IFFT
		double[][] ifftout;
		IFFT ifft = new IFFT(fftout);
		startTime = System.nanoTime();
		ifftout = ifft.calculate();
		endTime = System.nanoTime();
		double ifft_time = (endTime - startTime);
		/*System.out.println("IFFT data : ");
		for (int i = 0; i < ifftout[0].length; ++i) {
			System.out.println(i + " " + ifftout[0][i] + "   " + ifftout[1][i]);
		}
		System.out.println();*/
		// ///////////////////////////////////////////////////////////////////////////////////////////////////

		int size = 0;
		if ((ifftout[0].length / 2) % 12 != 0) {
			size = (ifftout[0].length / 2) + (12 - (ifftout[0].length / 2) % 8);
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

		// ///////////////////////////////////////////////////////////////////////////////////////////////////
		System.out.println();
		System.out.println("Encoding time          : " + encode_time / 1000000
				+ " ms");
		System.out.println("Modulation time        : " + modulate_time
				/ 1000000 + " ms");
		System.out.println("FFT time               : " + fft_time / 1000000
				+ " ms");
		System.out.println("IFFT time              : " + ifft_time / 1000000
				+ " ms");
		System.out.println("Multiplexing time      : " + multiplexing_time
				/ 1000000 + " ms");
		System.out.println();
		System.out
				.println("Total time             : "
						+ (encode_time + modulate_time + fft_time + ifft_time + multiplexing_time)
						/ 1000000 + " ms");
		System.out.println();

		// /////////////////////////////////////////////////////////////////////////////////////////////////Channel
		Channel ch = new Channel("EPA 5Hz","High",5,2);
		ch.run_channel(antenna1, antenna2);
		// //////////////////////////////////////////////////////////////////////////////////////////////////K
		// best

		// /////////////////////////////////////////////////////////////////////////////////////////////////turbo
		// decoder

		Decoder dec = new Decoder();
		int decode[] = new int[data_block_size];
		int decoded_data[] = new int[data.length()];
		int count = 0;
		for (int i = 0; i < encodeddata.length(); i = i + 132) {

			decode = dec.decoder_log_map(encodeddata.substring(i, i + 131),
					decoder_num, data_block_size);
			for (int j = 0; j < data_block_size; ++j) {
				decoded_data[count] = decode[j];
				count++;
			}

		}

		int error = error_calc(data, decoded_data, data.length());
		System.out.println("Error count = " + error + " / " + data.length());
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