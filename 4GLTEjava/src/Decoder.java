public class Decoder {

	int[] decoder_log_map(String data, int decoder_num, int data_block_size) {

		int le = 3 * data_block_size + 12;
		int y1[] = new int[((2 * data_block_size) + 6)];
		for (int i = 0; i < ((2 * data_block_size) + 6); ++i) {
			y1[i] = 0;
		}

		int lim = 3 * data_block_size;
		int intlv_table[][] = new int[data_block_size][2];
		for (int j = 0; j < 2; ++j) {
			for (int i = 0; i < ((data_block_size)); ++i) {
				intlv_table[i][j] = 0;
			}
		}
		int k = data_block_size + 4;
		double gamma[][] = new double[4][k];

		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < k; ++j) {
				gamma[i][j] = 0;
			}
		}

		double a[][] = new double[17][k];
		for (int i = 0; i < 17; ++i) {
			for (int j = 0; j < k; ++j) {
				a[i][j] = 0.0;
			}
		}
		double lc = 2.5;
		int s = 1;
		int l = 0;
		int intlv = 0;
		int pos = 1;

		if (decoder_num == 1) {

			// data = data.erase(le - 7, 6);
			data = data.substring(0, le - 6);

			pos = 1;
			while (s < (lim + 6)) {

				// y1[pos - 1] = data[s - 1] - 48;
				// System.out.println(data.length()+" "+(s-1)+" "+(lim +
				// 6)+" "+((int)data.charAt(s-1)));
				y1[pos - 1] = (int) data.charAt(s - 1) - 48;
				y1[pos] = (int) data.charAt(s) - 48;

				if (s < lim)
					s = s + 3;
				else
					s = s + 2;

				pos = pos + 2;

			}
		} else {

			// data = data.erase(le - 12, 6);
			data = data.substring(0, le - 11) + data.substring(le - 5);
			pos = 0;
			s = 1;
			l = 0;
			intlv = 0;
		//	System.out.println("Data string lenght = " + data.length());
			// ////////////////////////////////////////////////////////////////////////////
			while (pos < 86) {
				if (s < lim) {
					// /////////////////////////////////////////////////////

					// ///////////////////////////////////////////////////
					intlv = (((3 * (l + 1)) + (10 * (l + 1) * (l + 1))) % data_block_size);
					l = l + 1;
					if (l < 40) {
						intlv_table[l][0] = l;
						intlv_table[l][1] = intlv;
					}
					s = s + 3;
				}

				// / y1[pos] = code2[pos] - 48;
				// cout << y1[pos];
				pos++;

			}
			s = 1;
			pos = 1;
			l = 0;
			// ////////////////////////////////////////////////////////////////////////////
			while (s < (le - 7)) {

				if (s < lim) {
					// /////////////////////////////////////////////////////

					// ///////////////////////////////////////////////////

					// y1[pos - 1] = data[(3 * intlv_table[pos - 1][1])] - 48;
					// y1[pos] = data[s + 1] - 48;
					y1[pos] = data.charAt(s + 1) - 48;
					// ///////////////////////////////////
					s = s + 3;
				} else {

					// y1[pos - 1] = data[s - 1] - 48;
					// y1[pos] = data[s] - 48;

					y1[pos - 1] = data.charAt(s - 1) - 48;
					y1[pos] = data.charAt(s) - 48;
					s = s + 2;
				}

				pos = pos + 2;

			}

			// ////////////////////////////////////////////////////////////
		//	System.out.println("y1 bits ");
		//	System.out.println(((int) data.charAt(3 * 0) - 48) + " "
		//			+ ((int) data.charAt(0) - 48));
			y1[0] = (int) data.charAt(0) - 48;
			y1[2 * 1] = (int) data.charAt(3 * 13) - 48;
			y1[2 * 2] = (int) data.charAt(3 * 6) - 48;
			y1[2 * 3] = (int) data.charAt(3 * 19) - 48;
			y1[2 * 4] = (int) data.charAt(3 * 12) - 48;
			y1[2 * 5] = (int) data.charAt(3 * 25) - 48;
			y1[2 * 6] = (int) data.charAt(3 * 18) - 48;
			y1[2 * 7] = (int) data.charAt(3 * 31) - 48;
			y1[2 * 8] = (int) data.charAt(3 * 24) - 48;
			y1[2 * 9] = (int) data.charAt(3 * 37) - 48;
			y1[2 * 10] = (int) data.charAt(3 * 30) - 48;
			y1[2 * 11] = (int) data.charAt(3 * 3) - 48;
			y1[2 * 12] = (int) data.charAt(3 * 36) - 48;
			y1[2 * 13] = (int) data.charAt(3 * 9) - 48;
			y1[2 * 14] = (int) data.charAt(3 * 2) - 48;
			y1[2 * 15] = (int) data.charAt(3 * 15) - 48;
			y1[2 * 16] = (int) data.charAt(3 * 8) - 48;
			y1[2 * 17] = (int) data.charAt(3 * 21) - 48;
			y1[2 * 18] = (int) data.charAt(3 * 14) - 48;
			y1[2 * 19] = (int) data.charAt(3 * 27) - 48;
			y1[2 * 20] = (int) data.charAt(3 * 20) - 48;
			y1[2 * 21] = (int) data.charAt(3 * 33) - 48;
			y1[2 * 22] = (int) data.charAt(3 * 26) - 48;
			y1[2 * 23] = (int) data.charAt(3 * 39) - 48;
			y1[2 * 24] = (int) data.charAt(3 * 32) - 48;
			y1[2 * 25] = (int) data.charAt(3 * 5) - 48;
			y1[2 * 26] = (int) data.charAt(3 * 38) - 48;
			y1[2 * 27] = (int) data.charAt(3 * 11) - 48;
			y1[2 * 28] = (int) data.charAt(3 * 4) - 48;
			y1[2 * 29] = (int) data.charAt(3 * 17) - 48;
			y1[2 * 30] = (int) data.charAt(3 * 10) - 48;
			y1[2 * 31] = (int) data.charAt(3 * 23) - 48;
			y1[2 * 32] = (int) data.charAt(3 * 16) - 48;
			y1[2 * 33] = (int) data.charAt(3 * 29) - 48;
			y1[2 * 34] = (int) data.charAt(3 * 22) - 48;
			y1[2 * 35] = (int) data.charAt(3 * 35) - 48;
			y1[2 * 36] = (int) data.charAt(3 * 28) - 48;
			y1[2 * 37] = (int) data.charAt(3 * 1) - 48;
			y1[2 * 38] = (int) data.charAt(3 * 34) - 48;
			y1[2 * 39] = (int) data.charAt(3 * 7) - 48;
			// ////////////////////////////////////////////////////////////

			// cout<<"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"<<endl;
		}

		int y[] = new int[((2 * data_block_size) + 6)];
		for (int i = 0; i < ((2 * data_block_size) + 6); ++i) {
			y[i] = 2 * y1[i] - 1;
		}

		// ///////74
		int i = 0;
		for (int j = 0; j < (2 * k - 3); j = j + 2) {
			i = i + 1;

			gamma[0][i] = (lc * (-y[j] - y[j + 1]));
			gamma[3][i] = (lc * (y[j] + y[j + 1]));
			gamma[1][i] = (lc * (-y[j] + y[j + 1]));
			gamma[2][i] = (lc * (y[j] - y[j + 1]));
			if (i == 1) {
				a[0][i] = gamma[0][i];
				a[4][i] = gamma[3][i];

			}
			if (i == 2) {
				a[0][i] = a[0][i - 1] + gamma[0][i];
				a[4][i] = a[0][i - 1] + gamma[3][i];
				a[2][i] = a[4][i - 1] + gamma[1][i];
				a[6][i] = a[4][i - 1] + gamma[2][i];
			}
			if (i == 3) {
				a[0][i] = a[0][i - 1] + gamma[0][i];
				a[4][i] = a[0][i - 1] + gamma[3][i];
				a[2][i] = a[4][i - 1] + gamma[1][i];
				a[6][i] = a[4][i - 1] + gamma[2][i];
				// //////////////////////////
				a[1][i] = a[2][i - 1] + gamma[2][i];
				a[5][i] = a[2][i - 1] + gamma[1][i];
				a[3][i] = a[6][i - 1] + gamma[3][i];
				a[7][i] = a[6][i - 1] + gamma[0][i];
				// ///////////////////////////////

			}
			if (((i > 3) && i < (k - 3))) {

				a[0][i] = max_e(a[0][i - 1] + gamma[0][i], a[1][i - 1]
						+ gamma[3][i]);
				a[4][i] = max_e(a[0][i - 1] + gamma[3][i], a[1][i - 1]
						+ gamma[0][i]);
				a[2][i] = max_e(a[4][i - 1] + gamma[1][i], a[5][i - 1]
						+ gamma[2][i]);

				a[6][i] = max_e(a[4][i - 1] + gamma[2][i], a[5][i - 1]
						+ gamma[1][i]);
				a[1][i] = max_e(a[2][i - 1] + gamma[2][i], a[3][i - 1]
						+ gamma[1][i]);
				a[5][i] = max_e(a[2][i - 1] + gamma[1][i], a[3][i - 1]
						+ gamma[2][i]);
				a[3][i] = max_e(a[6][i - 1] + gamma[3][i], a[7][i - 1]
						+ gamma[0][i]);
				a[7][i] = max_e(a[6][i - 1] + gamma[0][i], a[7][i - 1]
						+ gamma[3][i]);

			}
			if (i == (k - 3)) {

				a[0][i] = max_e(a[0][i - 1] + gamma[0][i], a[1][i - 1]
						+ gamma[3][i]);
				a[1][i] = max_e(a[2][i - 1] + gamma[2][i], a[3][i - 1]
						+ gamma[1][i]);
				a[2][i] = max_e(a[4][i - 1] + gamma[1][i], a[5][i - 1]
						+ gamma[2][i]);
				a[3][i] = max_e(a[6][i - 1] + gamma[3][i], a[7][i - 1]
						+ gamma[0][i]);

			}
			if (i == (k - 2)) {
				a[0][i] = max_e(a[0][i - 1] + gamma[0][i], a[1][i - 1]
						+ gamma[3][i]);
				a[1][i] = max_e(a[2][i - 1] + gamma[2][i], a[3][i - 1]
						+ gamma[1][i]);
				// /////////////////////////////////////////////////////////////////////////////

			}
			if (i == k - 1) {
				a[0][i] = max_e(a[0][i - 1] + gamma[0][i], a[1][i - 1]
						+ gamma[3][i]);

			}

		}

		// /////////////////////////////////////////////////////////////////////////

		// /////////////////////////////////////////////////////////////////update
		// beta
		i = k - 1;
		double r0, r1;

		while (i >= 0) {
			if (i == k - 1) {
				a[8][i - 1] = gamma[0][i];
				a[9][i - 1] = gamma[3][i];

				r0 = a[0][i - 1] + gamma[0][i];
				// //////////////////////////////////////Check
				// THISSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
				r1 = a[1][i - 1] + gamma[3][i];
				a[16][i] = r1 - r0;

			} else if (i == k - 2) {
				a[8][i - 1] = a[8][i] + gamma[0][i];
				a[9][i - 1] = a[8][i] + gamma[3][i];
				a[10][i - 1] = a[9][i] + gamma[2][i];
				a[11][i - 1] = a[9][i] + gamma[1][i];

				r0 = max_e(a[0][i - 1] + gamma[0][i] + a[8][i], a[3][i - 1]
						+ gamma[1][i] + a[9][i]);
				// //////////////////////////////////////////////Check
				// THISSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
				r1 = max_e(a[1][i - 1] + gamma[3][i] + a[8][i], a[2][i - 1]
						+ gamma[2][i] + a[9][i]);
				a[16][i] = r1 - r0;

			} else if (i == k - 3) {

				a[8][i - 1] = a[8][i] + gamma[0][i];
				a[9][i - 1] = a[8][i] + gamma[3][i];
				a[10][i - 1] = a[9][i] + gamma[2][i];
				a[11][i - 1] = a[9][i] + gamma[1][i];
				a[12][i - 1] = a[10][i] + gamma[1][i];
				a[13][i - 1] = a[10][i] + gamma[2][i];
				a[14][i - 1] = a[11][i] + gamma[3][i];
				a[15][i - 1] = a[11][i] + gamma[0][i];
				// //////////////////////////////////////

				r0 = max_e4(a[0][i - 1] + gamma[0][i] + a[8][i], a[3][i - 1]
						+ gamma[1][i] + a[9][i], a[4][i - 1] + gamma[1][i]
						+ a[10][i], a[7][i - 1] + gamma[0][i] + a[11][i]);
				// /////////////////////////////////////////////////////////////////////////
				// Check THISSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
				r1 = max_e4(a[1][i - 1] + gamma[3][i] + a[8][i], a[2][i - 1]
						+ gamma[2][i] + a[9][i], a[5][i - 1] + gamma[2][i]
						+ a[10][i], a[6][i - 1] + gamma[3][i] + a[11][i]);
				a[16][i] = r1 - r0;

			} else if (((i < k - 3) && (i > 3))) {

				a[8][i - 1] = max_e(a[8][i] + gamma[0][i], a[12][i]
						+ gamma[3][i]);
				a[9][i - 1] = max_e(a[8][i] + gamma[3][i], a[12][i]
						+ gamma[0][i]);
				a[10][i - 1] = max_e(a[9][i] + gamma[2][i], a[13][i]
						+ gamma[1][i]);
				a[11][i - 1] = max_e(a[9][i] + gamma[1][i], a[13][i]
						+ gamma[2][i]);
				a[12][i - 1] = max_e(a[10][i] + gamma[1][i], a[14][i]
						+ gamma[2][i]);
				a[13][i - 1] = max_e(a[10][i] + gamma[2][i], a[14][i]
						+ gamma[1][i]);
				a[14][i - 1] = max_e(a[11][i] + gamma[3][i], a[15][i]
						+ gamma[0][i]);
				a[15][i - 1] = max_e(a[11][i] + gamma[0][i], a[15][i]
						+ gamma[3][i]);

				r0 = max_e8(a[0][i - 1] + gamma[0][i] + a[8][i], a[3][i - 1]
						+ gamma[1][i] + a[9][i], a[4][i - 1] + gamma[1][i]
						+ a[10][i], a[7][i - 1] + gamma[0][i] + a[11][i],
						a[1][i - 1] + gamma[0][i] + a[12][i], a[2][i - 1]
								+ gamma[1][i] + a[13][i], a[5][i - 1]
								+ gamma[1][i] + a[14][i], a[6][i - 1]
								+ gamma[0][i] + a[15][i]);
				// ///////////////////////////////////////////////// Check
				// THISSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
				r1 = max_e8(a[1][i - 1] + gamma[3][i] + a[8][i], a[2][i - 1]
						+ gamma[2][i] + a[9][i], a[5][i - 1] + gamma[2][i]
						+ a[10][i], a[6][i - 1] + gamma[3][i] + a[11][i],
						a[0][i - 1] + gamma[3][i] + a[12][i], a[3][i - 1]
								+ gamma[2][i] + a[13][i], a[4][i - 1]
								+ gamma[2][i] + a[14][i], a[7][i - 1]
								+ gamma[3][i] + a[15][i]);

				a[16][i] = r1 - r0;

			} else if (i == 3) {

				a[8][i - 1] = max_e(a[8][i] + gamma[0][i], a[12][i]
						+ gamma[3][i]);
				a[10][i - 1] = max_e(a[9][i] + gamma[2][i], a[13][i]
						+ gamma[1][i]);
				a[12][i - 1] = max_e(a[10][i] + gamma[1][i], a[14][i]
						+ gamma[2][i]);
				a[14][i - 1] = max_e(a[11][i] + gamma[3][i], a[15][i]
						+ gamma[0][i]);

				r0 = max_e4(a[0][i - 1] + gamma[0][i] + a[8][i], a[4][i - 1]
						+ gamma[1][i] + a[10][i], a[2][i - 1] + gamma[1][i]
						+ a[13][i], a[6][i - 1] + gamma[0][i] + a[15][i]);
				// /////////////////////////////////////////////////// Check
				// THISSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
				r1 = max_e4(a[2][i - 1] + gamma[2][i] + a[9][i], a[6][i - 1]
						+ gamma[3][i] + a[11][i], a[0][i - 1] + gamma[3][i]
						+ a[12][i], a[4][i - 1] + gamma[2][i] + a[14][i]);
				a[16][i] = r1 - r0;

			}

			else if (i == 2) {

				a[8][i - 1] = max_e(a[8][i] + gamma[0][i], a[12][i]
						+ gamma[3][i]);
				a[12][i - 1] = max_e(a[10][i] + gamma[1][i], a[14][i]
						+ gamma[2][i]);

				r0 = max_e(a[0][i - 1] + gamma[0][i] + a[8][i], a[4][i - 1]
						+ gamma[1][i] + a[10][i]);
				// //////////////////////////////////////////////////// Check
				// THISSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
				r1 = max_e(a[0][i - 1] + gamma[3][i] + a[12][i], a[4][i - 1]
						+ gamma[2][i] + a[14][i]);
				a[16][i] = r1 - r0;

			}

			else if (i == 1) {
				a[8][i - 1] = max_e(a[8][i] + gamma[0][i], a[12][i]
						+ gamma[3][i]);

				r0 = a[0][i - 1] + gamma[0][i] + a[8][i];
				// ///////////////////////////////////////Check
				// THISSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
				r1 = a[0][i - 1] + gamma[3][i] + a[12][i];
				a[16][i] = r1 - r0;

			}

			i = i - 1;
		}
		// /////////////////////////////////////////////////////////////

		int decoded[] = new int[data_block_size]; // = zeros(data_block_size,
													// 1);

		double llr[] = new double[data_block_size]; // = zeros(data_block_size,
													// 1);
		if (decoder_num == 1) {
			for (int h = 0; h < (k - 4); ++h) { // h=1:k-4
				if (a[16][h + 1] >= 0) {
					decoded[h] = 1;

					llr[h] = a[16][h + 1];
				} else {
					decoded[h] = 0;

					llr[h] = a[16][h + 1];
				}

			}
		} else {
			for (int h = 0; h < (k - 4); ++h) {
				if (a[16][h + 1] >= 0) {
					decoded[intlv_table[h][1]] = 1;

					llr[intlv_table[h][1]] = a[16][h + 1];
				} else {
					decoded[intlv_table[h][1]] = 0;

					llr[intlv_table[h][1]] = a[16][h + 1];
				}

			}
		}
		// decode = decoded;

		/*
		 * System.out.println( "llr values" ); for (int j = 0; j <
		 * data_block_size; ++j) {
		 * 
		 * System.out.print( llr[j]+" " ); } System.out.println( );
		 * 
		 * 
		 * System.out.println( "a[] matrix values" ); for (int ji = 0; ji < 17;
		 * ++ji) { for (int j = 35; j < k; ++j) {
		 * 
		 * System.out.print(a[ji][j]+" "); }
		 * 
		 * System.out.println( ); }
		 */

		/*
		 * System.out.println( "decoded bits" ); for (int j = 0; j <
		 * data_block_size; ++j) {
		 * 
		 * System.out.print(decoded[j]+" " ); }
		 * 
		 * System.out.println( );
		 */

		/*
		 * System.out.println("Y bits"); for (int j = 0; j < 86; ++j) {
		 * 
		 * System.out.print(y1[j]); }
		 * 
		 * System.out.println();
		 */
		/*
		 * System.out.println( "interleave table values" ); for (int ji = 0; ji
		 * < 2; ++ji) { for (int j = 0; j < k - 4; ++j) {
		 * 
		 * System.out.print( intlv_table[j][ji]+" " ); }
		 * 
		 * System.out.println( ); }
		 */

		return decoded;

	}

	double max_e8(double a, double b, double c, double d, double e, double f,
			double g, double h) {
		double i = max_e4(a, b, c, d);
		double j = max_e4(e, f, g, h);
		f = max_e(i, j);

		return f;
	}

	double max_e4(double a, double b, double c, double d) {
		double f = max_e(a, b);
		double g = max_e(c, d);
		f = max_e(f, g);

		return f;
	}

	double max_e(double a, double b) {
		double max = 0;
		double c = 0;
		if (a > b) {
			max = a;
			c = a - b;
		} else {
			max = b;
			c = b - a;
		}
		return max + Math.log10(1 + Math.exp(-c));
	}

}
