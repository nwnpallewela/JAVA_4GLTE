import org.jscience.mathematics.vector.ComplexMatrix;

import weka.core.matrix.Matrix;
import weka.core.matrix.QRDecomposition;

public class Equalizer {

	Matrix H;
	Matrix Q;
	Matrix R;
	LSDTree Tree;

	double y[];

	Equalizer() {

		Tree = new LSDTree(1000000, 32);// R and K
		y = new double[32];

	}

	Equalizer(double R) {
		Tree = new LSDTree(R, 32);// R and K
		y = new double[32];

	}

	void genH(ComplexMatrix H1) {

		double h_temp[][] = new double[32][32];
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {

				h_temp[i][j] = H1.get(i, j).getReal();
				h_temp[i + 16][j + 16] = h_temp[i][j];
				h_temp[i + 16][j] = H1.get(i, j).getImaginary();
				h_temp[i][j + 16] = (-1) * h_temp[i + 16][j];
			}
		}

		H = Matrix.constructWithCopy(h_temp);

	}

	double[] GetLSD_Y(Matrix Y, double Rsq) {

		QRDecomposition QRD = new QRDecomposition(H);
		Q = QRD.getQ();
		R = QRD.getR();

		Y = Q.transpose().times(Y);

		LSDTree tree = new LSDTree(Rsq, 32);
		tree.setRMatrix(R);
		tree.generateFirstlevel(Y.get(31, 0));

		for (int i = 0; i < 31; i++) {
			tree.generateNextlevel(Y.get(31 - (i + 1), 0), i + 1);

		}

		Node minNode = tree.getMinnode();
		for (int i = 0; i < 32; i++) {
			y[i] = minNode.getNode_S();
			minNode = minNode.getparent();
		}

		return y;
	}

	void RS(Matrix S) {
		QRDecomposition QRD = new QRDecomposition(H);
		Q = QRD.getQ();
		R = QRD.getR();
		Matrix RS = R.times(S);
		System.out.println(RS.transpose());
	}

}
