/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
public class turboEncode {
	private int K = 40; // block size of data

	public String run(String data) {

		String data_out = encode(data);
		// System.out.println(data_out);
		return data_out;
	}

	public String encode(String data) {

		String code1 = new String();
		String code2 = new String();
		String code_word = "";
		String data_atr = new String();
		String interleaved_data = new String();
		// /////////////////////////////////////////////////////////

		int j = 0;

		Integer D0 = 0;
		Integer D1 = 0;
		Integer D2 = 0;
		Integer D3 = 0;

		@SuppressWarnings("unused")
		Integer tD0 = 0;
		Integer tD1 = 0;
		Integer tD2 = 0;
		Integer tD3 = 0;
		int j1 = 0;

		Integer D02 = 0;
		Integer D12 = 0;
		Integer D22 = 0;
		Integer D32 = 0;

		@SuppressWarnings("unused")
		Integer tD02 = 0;
		Integer tD12 = 0;
		Integer tD22 = 0;
		Integer tD32 = 0;
		int j12 = 0;
		// System.out.println(data.length()-K);
		for (j = 0; j <= (data.length() - K); j = j + K) {
			int i1 = 0;
			int i12 = 0;
			// System.out.println("loop");
			data_atr = data.substring(j, j + K);
			// System.out.println(data_atr);
			data_atr = data_atr.concat("2");
			for (int i = 0; i < K; i++) {
				tD0 = D0;
				tD1 = D1;
				tD2 = D2;
				tD3 = D3;
				D0 = Integer.parseInt(data_atr.substring(i, i + 1));
				D3 = tD2;
				D2 = tD1;
				D1 = ((D0 + tD2 + tD3) % 2);

				// System.out.println();

				// System.out.println(D0+" "+D1+" "+D2+" "+(D3));

			//	System.out.print(D0 + "");
			//	System.out.print((D0 + D1 + D3) % 2 + "");

				code1 = code1.concat(D0.toString());
				code1 = code1.concat(((D1 + tD1 + tD3) % 2) + "");

				i1 = 2 * i;
			}
			for (int k = 0; k < 3; k++) {
				tD0 = D0;
				tD1 = D1;
				tD2 = D2;
				tD3 = D3;
				D3 = tD2;
				D2 = tD1;

				D0 = ((tD2 + tD3) % 2);
				D1 = ((D0 + tD2 + tD3) % 2);

				code1 = code1.concat(D0.toString());
				code1 = code1.concat(((D1 + tD1 + tD3) % 2) + "");
			}

			interleaved_data = interleave(data_atr);

			for (int i = 0; i < K; i++) {

				tD02 = D02;
				tD12 = D12;
				tD22 = D22;
				tD32 = D32;
				D02 = Integer.parseInt(interleaved_data.substring(i, i + 1));
				D32 = tD22;
				D22 = tD12;
				D12 = ((D02 + tD22 + tD32) % 2);

				code2 = code2.concat(D02.toString());
				code2 = code2.concat(((D12 + tD12 + tD32) % 2) + "");

				i12 = 2 * i;
			}
			for (int k = 0; k < 3; k++) {
				tD02 = D02;
				tD12 = D12;
				tD22 = D22;
				tD32 = D32;
				D32 = tD22;
				D22 = tD12;

				D02 = ((tD22 + tD32) % 2);
				D12 = ((D02 + tD22 + tD32) % 2);
				code2 = code2.concat(D02.toString());
				code2 = code2.concat(((D12 + tD12 + tD32) % 2) + "");
			}
		//	System.out.println("Code 2 " +code2);

			code_word = code_word.concat(multiplex(
					code1.substring(j1, j1 + i1 + 2),
					code2.substring(j12, j12 + i12 + 2)));
			// System.out.println(code_word.length());
			code_word = code_word.concat(code1.substring(j1 + i1 + 2, j1 + i1
					+ 8));
			// System.out.println(code_word.length());
			code_word = code_word.concat(code2.substring(j12 + i12 + 2, j12
					+ i12 + 8));

			j1 = j1 + 2 * K + 6;
			j12 = j12 + 2 * K + 6;

		}

		// //////////////////////////////////////////////////////////
		// System.out.println(code_word.length());
		return code_word;
	}

	public String multiplex(String code1, String code2) {

		// System.out.println(code1.length());
		String arr = new String();

		for (int i = 0; i < code1.length(); i = i + 2) {
			arr = arr.concat(code1.substring(i, i + 1));

			arr = arr.concat(code1.substring(i + 1, i + 2));
			arr = arr.concat(code2.substring(i + 1, i + 2));

		}

		return arr;
	}

	public String interleave(String data_in) {
		int f1 = 3;
		int f2 = 10;
		int index;

		String data_out = new String();

		for (int i = 0; i < K; i++) {
			index = (((f1 * (i) + f2 * (i) * (i)) % K));

			data_out = data_out.concat(data_in.substring(index, index + 1));

		}

		return data_out;
	}
}