
public class TestFFT {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double test[][]=new double[2][8];
		for (int k = 0; k < 8; k++) {
			test[0][k]=k+1;
			System.out.println(test[0][k]+"+"+test[1][k]);
		}
		FFT fft=new FFT(test);
		test=fft.calculate();
		
		for (int k = 0; k < 8; k++) {
			System.out.println(test[0][k]+" + "+test[1][k]);
		}
		

	}

}
