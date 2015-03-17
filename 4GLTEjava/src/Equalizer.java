import org.jscience.mathematics.vector.ComplexMatrix;

import weka.core.matrix.Matrix;
import weka.core.matrix.QRDecomposition;

public class Equalizer {
	
	Matrix H;
	Matrix Q;
	Matrix R;
	LSDTree Tree;
	
	double y[];
	Equalizer(){
		Tree=new LSDTree(1000000,32);//R and K
		y=new double[32];
		
	}

	void genH(ComplexMatrix H1){
		
		double h_temp[][]=new double[32][32];
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				
				h_temp[i][j]=H1.get(i, j).getReal();
				h_temp[i+16][j+16]=h_temp[i][j];
				h_temp[i+16][j]=H1.get(i, j).getImaginary();
				h_temp[i][j+16]=(-1)*h_temp[i+16][j];
			}
		}
		
		H=Matrix.constructWithCopy(h_temp);
		
	
	}
	
	double[] GetLSD_Y(Matrix Y){
		
		QRDecomposition QRD = new QRDecomposition(H);
		Q=QRD.getQ();
		R=QRD.getR();
		/*System.out.println("Y row: ");
		System.out.println(Y);
		System.out.println();
		*/
		Y=Q.transpose().times(Y);
		/*System.out.println("Y dash: ");
		System.out.print(Y.transpose());
		System.out.println();*/
		LSDTree tree=new LSDTree(1000000,32);
		tree.setRMatrix(R);
		tree.generateFirstlevel(Y.get(31, 0));
		//System.out.println(Y.get(31, 0));
		//tree.printTree();
		//tree.printcurrentlevel();
		//System.out.println("***************");
		for (int i = 0; i < 31; i++) {
		//	System.out.println("************************** After level : "+(i+1));
			//System.out.println("Y index: "+i+1);
			tree.generateNextlevel(Y.get(31-(i+1), 0),i+1);
			//tree.printTree();
			//tree.printcurrentlevel();
		//	System.out.println("*************************************");
		}
		//System.out.println("*************************************");
		
		Node minNode=tree.getMinnode();
		//System.out.println(minNode.getNode_S()+" : "+minNode.getvalue());
		for (int i = 0; i < 32; i++) {
		//	System.out.println(i+" "+minNode.getvalue());
			y[i]=minNode.getNode_S();
			minNode=minNode.getparent();
		}
		
		
		
		return y;
	}
	
	void RS(Matrix S){
		QRDecomposition QRD = new QRDecomposition(H);
		Q=QRD.getQ();
		R=QRD.getR();
		Matrix RS = R.times(S);
		System.out.println(RS.transpose());
	}
	
	
}
