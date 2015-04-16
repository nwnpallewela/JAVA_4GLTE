public class Demodulator {
	final int real[] = { -7, -7, -7, -7, -7, -7, -7, -7, -5, -5, -5, -5, -5,
			-5, -5, -5, -1, -1, -1, -1, -1, -1, -1, -1, -3, -3, -3, -3, -3, -3,
			-3, -3, 7, 7, 7, 7, 7, 7, 7, 7, 5, 5, 5, 5, 5, 5, 5, 5, 1, 1, 1, 1,
			1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3 };
	final int complex[] = { -7, -5, -1, -3, 7, 5, 1, 3, -7, -5, -1, -3, 7, 5,
			1, 3, -7, -5, -1, -3, 7, 5, 1, 3, -7, -5, -1, -3, 7, 5, 1, 3, -7,
			-5, -1, -3, 7, 5, 1, 3, -7, -5, -1, -3, 7, 5, 1, 3, -7, -5, -1, -3,
			7, 5, 1, 3, -7, -5, -1, -3, 7, 5, 1, 3 };

	String runDemodulator(double[] y) {
		String data = "";
		int start = 0;
		String bin="";
		double rx1[][] = new double[2][8];
		double rx2[][] = new double[2][8];
		/*
		 * for (int i = 0; i < 8; i++) { rx1[0][i]=y[i]; rx1[1][i]=y[i+16];
		 * rx2[0][i]=y[i+8]; rx2[1][i]=y[i+24]; }
		 */
		for (int i = 0; i < 16; i++) {
			switch ((int) y[i]) {
			case -5:
				start = 8;
				break;
			case -1:
				start = 16;
				break;
			case -3:
				start = 24;
				break;
			case 7:
				start = 32;
				break;
			case 5:
				start = 40;
				break;
			case 1:
				start = 48;
				break;
			case 3:
				start = 56;
				break;
			default:
				start = 0;

			}
			int t=0;
			for (int j = start; j < (start+8); j++) {
				
				if(complex[j]==(int)y[i+16]){
					bin="";
					t=j;
					for (int j2 = 0; j2 < 6; j2++) {
						if(t%2==0){
							bin=bin+"0";
						//	System.out.println(" t : bit --> "+t+" : "+"0");
							t=t/2;//
						}else{
							bin=bin+"1";
						//	System.out.println(" t : bit --> "+t+" : "+"1");
							t=(t-1)/2;//
						}
						//t=t/2;
					}
					//System.out.println(j+" : "+bin);
					data=data+bin;
					
				//	System.out.println(j+" : "+findindex(bin)+"   ---    "+y[i]+"+"+y[i+16]+"i : "+real[j]+"+"+complex[j]+"i");
					
					break;
					
				}
			}
			
		}

		return data;
	}
	int findindex(String code1) {
		
		char code[]=code1.toCharArray();
		return ((code[0] - 48) + (code[1] - 48) * 2 + (code[2] - 48) * 4
				+ (code[3] - 48) * 8 + (code[4] - 48) * 16 + (code[5] - 48) * 32);
	}

}
