
public class ML_zeroforcing {

	double y_[]=new double[32];
	public double[] Decision(double[][] yout){
		for (int i = 0; i < 16; i++) {
			y_[i]=(yout[0][i]+1)/2;
			y_[i+16]=(yout[1][i]+1)/2;
		}
		for (int i = 0; i < 32; i++){
			y_[i]=Math.round(y_[i]);
			if(y_[i]>4){
				y_[i]=4;
			}else if(y_[i]<-3){
				y_[i]=-3;
			}
			y_[i]=2*y_[i]-1;
		}
		
	/*	for (int i = 0; i < 16; i++) {
			System.out.println(yout[0][i]+" + "+yout[1][i]+" ---- "+y_[i]+" + "+y_[i+16]+"i");
		}*/
		
		return y_;
	}
	
}
