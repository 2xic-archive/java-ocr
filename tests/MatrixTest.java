package com;

import junit.framework.TestCase;
import com.Matrix;

public class MatrixTest extends TestCase {
	public void setUp() throws Exception {

	}

	public void testAccessor(){
		Matrix x = new Matrix(new double[][] {
			{0, 0, 1},
			{0, 1, 1}
		});
		Matrix y = new Matrix(new double[][] {
			{0, 0, 1},
			{0, 1, 1}
		});

		Matrix w = x.add(y);
		assertEquals(w.equalMatrix(new Matrix(new double[][] {
						{0, 0, 2},
						{0, 2, 2}
					})), true);
	}

	public void testSigmoid(){
		Matrix inverse = new Matrix(new double[][] {
			{1}
		});
		double x = inverse.sigmoid(inverse.numbers[0][0]);
		assertEquals(x == 0.7310585786300049, true);
		double x_deriviation = inverse.sigmoidDerivative(x);
		System.out.println(x_deriviation);
		assertEquals(x_deriviation == 0.19661193324148185, true);
	}

	public void testDot(){
		Matrix matrixValue = new Matrix(new double[][] {
			{1, 1, 1},
			{1, 1, 1},
			{1, 1, 1}
		});

		Matrix truth = new Matrix(new double[][] {
			{3, 3, 3},
			{3, 3, 3},
			{3, 3, 3}
		});
		assertEquals(true, (matrixValue.dot(matrixValue)).equalMatrix(truth));

		Matrix matrixValue2 = new Matrix(new double[][] {
			{1}
		});

		Matrix truth2 = new Matrix(new double[][] {
			{2}
		});
		assertEquals((matrixValue2.dot(truth2)).equalMatrix(new Matrix(new double[][]{{2}})), true);
	
		Matrix matrixValue3 = new Matrix(new double[][] {
			{1, 2}
		});

		Matrix truth3 = new Matrix(new double[][] {
			{2},
			{3}
		});
		assertEquals((matrixValue3.dot(truth3)).equalMatrix(new Matrix(new double[][]{{8}})), true);

		Matrix matrixValue4 = new Matrix(new double[][] {
			{1, 2, 3}
		});

		Matrix truth4 = new Matrix(new double[][] {
			{1},
			{2},
			{3}
		});
		assertEquals((matrixValue4.dot(truth4)).equalMatrix(new Matrix(new double[][]{{14}})), true);

		Matrix matrixValue5 = new Matrix(new double[][] {
			{1, 2, 3},
			{1, 2, 3}
		});

		Matrix truth5 = new Matrix(new double[][] {
			{1, 1},
			{2, 2},
			{3, 3}
		});
		assertEquals(true, (matrixValue5.dot(truth5)).equalMatrix(new Matrix(new double[][] {
			{14, 14},
			{14, 14}
		})));
	}
}





