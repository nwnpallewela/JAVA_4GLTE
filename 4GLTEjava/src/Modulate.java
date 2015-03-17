/**
 * 
 */

/**
 * @author nuwan
 *
 */
public class Modulate {
	
	double[][] modulate64QAM(String encodeddata) {

		final int real[] = { -7, -7, -7, -7, -7, -7, -7, -7, -5, -5, -5, -5, -5,
				-5, -5, -5, -1, -1, -1, -1, -1, -1, -1, -1, -3, -3, -3, -3, -3, -3,
				-3, -3, 7, 7, 7, 7, 7, 7, 7, 7, 5, 5, 5, 5, 5, 5, 5, 5, 1, 1, 1, 1,
				1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3 };
		 final int complex[] = { -7, -5, -1, -3, 7, 5, 1, 3, -7, -5, -1, -3, 7, 5,
				1, 3, -7, -5, -1, -3, 7, 5, 1, 3, -7, -5, -1, -3, 7, 5, 1, 3, -7,
				-5, -1, -3, 7, 5, 1, 3, -7, -5, -1, -3, 7, 5, 1, 3, -7, -5, -1, -3,
				7, 5, 1, 3, -7, -5, -1, -3, 7, 5, 1, 3 };
	double modulateddata[][] = new double[2][encodeddata.length() / 6];
	for (int i = 0; i < 2; i++) {
		modulateddata[i] = new double[encodeddata.length() / 6];
		for (int j = 0; j < encodeddata.length() / 6; j++) {
			modulateddata[i][j] = 0;
		}
	}
	int index;
	int count = 0;
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	for (int j = 0; j <= (encodeddata.length() - 6); j = j + 6) {
		index = findindex(encodeddata.substring(j, j+6));
	//	System.out.println(encodeddata.substring(j, j+6)+" :: "+index);

		/*modulateddata[0][count] = real[index]/Math.sqrt(42.0);
		modulateddata[1][count] = complex[index]/Math.sqrt(42.0);
		*/
		modulateddata[0][count] = real[index];
		modulateddata[1][count] = complex[index];
		
		
		count++;
		//	cout<<encodeddata.substr(j, 6)<<" "<<real[index]<<" "<<complex[index]<<endl;
	}

	return modulateddata;
}

int findindex(String code1) {
	
	char code[]=code1.toCharArray();
	return ((code[0] - 48) + (code[1] - 48) * 2 + (code[2] - 48) * 4
			+ (code[3] - 48) * 8 + (code[4] - 48) * 16 + (code[5] - 48) * 32);
}

}
