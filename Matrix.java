package com;

/**
 * This class describes a matrix.
 */
class Matrix{
	public double[][] numbers;
	public int[]shape;

	/**
	 * Constructs a new instance with matrix size.
	 *
	 * @param      row      The row count
	 * @param      columns  The columns count
	 */
	public Matrix(int row, int columns){
		this.numbers = new double[row][columns];

		this.shape = new int[2];
		this.shape[0] = row;
		this.shape[1] = columns;
	}

	/**
	 * Constructs a new instance with a matrix.
	 *
	 * @param      matrix  input matrix
	 */
	public Matrix(double[][] matrix){
		this.numbers = matrix;
		
		this.shape = new int[2];
		this.shape[0] = matrix.length;
		this.shape[1] = matrix[0].length;	
	}

	/**
	 * get the matrix shape (rows, columns)
	 *
	 * @return     string tuple of (rows, columns) of matrix
	 */
	public String shape(){
		return "(" + this.shape[0] + ", " + this.shape[1] + ")";
	}

	/**
	 * Multiply two matrices
	 *
	 * @param      other     a matrix
	 *
	 * @return     the results of the dot product
	 */
	public Matrix dot(Matrix other){
		return new Matrix(multiplyMatrix(this.numbers, other.numbers));
	}

	/**
	 * Switch columns and rows
	 *
	 * @return     a transposed matrix
	 */
	public Matrix transpose(){
		double[][] temp = new double[this.numbers[0].length][this.numbers.length];
		for (int i = 0; i < this.numbers.length; i++){
			for (int j = 0; j < this.numbers[0].length; j++){
				temp[j][i] = this.numbers[i][j];
			}
		}
		return new Matrix(temp);
	}

	/**
	 * Calculate the matrix product
	 *
	 * @param      matrix1  The matrix 1
	 * @param      matrix2  The matrix 2
	 *
	 * @return     matrix product
	 */
	public double[][] multiplyMatrix(double[][] matrix1, double[][] matrix2) {
		int firstMatrixRow = matrix1.length;   
		int secondMatrixRow = matrix2.length;   

		int firstMatrixColumn = matrix1[0].length;
		int secondMatrixColumn = matrix2[0].length;

		if(firstMatrixColumn != secondMatrixRow){
			System.out.println("Illegal operation");
			System.out.println("[" + firstMatrixColumn + "," + secondMatrixRow + "]");
			System.exit(0);
			return null;
		}

		double[][] resultMatrix = new double[firstMatrixRow][secondMatrixColumn];
		for(int i = 0; i < firstMatrixRow; i++) {
            for (int j = 0; j < secondMatrixColumn; j++) {
                for (int k = 0; k < secondMatrixRow; k++) {
                    resultMatrix[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }

		return resultMatrix;
	}

	/**
	 * Adds or subtract a matrix.
	 *
	 * @param      inputMatrix  The input matrix
	 * @param      sign         The sign
	 *
	 * @return     resulting matrix
	 */
	public Matrix addSubMatrix(Matrix inputMatrix, int sign){
		double[][]  result = new double[this.getRows()][this.getColumn()];
		for (int i = 0; i < this.getRows() && i < inputMatrix.getRows(); i++) {
			for (int j = 0; j < this.getColumn(); j++) {
				result[i][j] = this.numbers[i][j] + sign * inputMatrix.numbers[i][j];
			}
		}
		return new Matrix(result);	
	}

	/**
	 * Adds the specified input matrix to an matrix.
	 *
	 * @param      inputMatrix  The input matrix
	 *
	 * @return     the sum of the two matrices
	 */
	public Matrix add(Matrix inputMatrix){
		return addSubMatrix(inputMatrix, 1);
	}	


	/**
	 * Subtracts the specified input matrix to an matrix.
	 *
	 * @param      inputMatrix  The input matrix
	 *
	 * @return     the results of this matrix subtracted from the input.
	 */
	public Matrix sub(Matrix inputMatrix){
		return addSubMatrix(inputMatrix, -1);
	}

	/**
	 * checks if two matrix are equal
	 *
	 * @param      inputMatrix  an input matrix
	 *
	 * @return     true/false
	 */
	public boolean equalMatrix(Matrix inputMatrix){
		if(this.getRows() != inputMatrix.getRows()){
			return false;
		}
		if(this.getColumn() != inputMatrix.getColumn()){
			return false;
		}
		for (int i = 0; i < this.getRows(); i++) {
			for (int j = 0; j < this.getColumn(); j++) {
				if(this.numbers[i][j] != inputMatrix.numbers[i][j]){
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * sum all the values in a matrix
	 *
	 * @return     the sum of all the numbers in the matrix
	 */
	public float sum(){
		float result = 0;
		for (int i = 0; i < this.getRows(); i++) {
			for (int j = 0; j < this.getColumn(); j++) {
				result += this.numbers[i][j];
			}
		}		
		return result;
	}

	/**
	 * Round to closest decimal
	 *
	 * @param      decimalCount   decimal count
	 *
	 * @return     matrix with the result
	 */
	public Matrix round(int decimalCount){
		double decimals = Math.pow(10, decimalCount);
		double[][]  result = new double[this.getRows()][this.getColumn()];
		for (int i = 0; i < this.getRows(); i++) {
			for (int j = 0; j < this.getColumn(); j++) {
				result[i][j] =  Math.round(this.numbers[i][j]*decimals)/decimals;
			}
		}	
		return new Matrix(result);	
	}

	/**
	 * Not a matrix dot product, but multiply row against row, column items column
	 *
	 * @param      inputMatrix  The input matrix
	 *
	 * @return     results
	 */
	public Matrix directMultiply(Matrix inputMatrix){
		double[][]  result = new double[this.getRows()][this.getColumn()];
		for (int i = 0; i < this.getRows() && i < inputMatrix.getRows(); i++) {
			for (int j = 0; j < this.getColumn() && j < inputMatrix.getColumn(); j++) {
				result[i][j] = this.numbers[i][j] * inputMatrix.numbers[i][j];
			}
		}
		return new Matrix(result);	
	}

	/**
	 * Multiply a constant to the matrix
	 *
	 * @param      x     constant
	 *
	 * @return     results
	 */
	public Matrix multiplyConstant(double x){
		double[][]  result = new double[this.getRows()][this.getColumn()];
		for (int i = 0; i < this.getRows(); i++) {
			for (int j = 0; j < this.getColumn(); j++) {
				result[i][j] = this.numbers[i][j] * x;
			}
		}
		return new Matrix(result);	
	}

	/**
	 * Gets the row count of the matrix.
	 *
	 * @return     The rows.
	 */
	int getRows(){
		return this.numbers.length;
	}

	/**
	 * Gets the column count
	 *
	 * @return     The cols.
	 */
	int getColumn(){
		return this.numbers[0].length;
	}


	/**
	 * Returns a string representation of the matrix.
	 *
	 * @return     String representation of the matrix.
	 */
	public String toString() {
		String output = "";

		if(this.numbers == null){
			return "this matrix is null";
		}

		System.out.println(this.shape());
		for(int i = 0; i < this.getRows(); i++){
			output += ("[");
			for(int j = 0; j < this.getColumn(); j++){
				if(0 < j){
					output += (", ");
				}
				output += (this.numbers[i][j]);				
			}
			output += ("]\n");
		}
		return output;
	}

	/**
	 * calculate the derivative of the sigmoid
	 *
	 * @param      x     sigmoid value
	 *
	 * @return     derivative
	 */
	public double sigmoidDerivative(double x){
		return x * (1 - x);
	}

	/**
	 * Find the sigmoid
	 *
	 * @param      x     point
	 *
	 * @return     the sigmoid value at point x
	 */
	public double sigmoid(double t) {
		return 1 / (1 + Math.pow(Math.E, (-1 * t)));
	}

	/**
	 * Calculate the sigmoid of the matrix
	 *
	 * @param      derivation  Calculate the derivation
	 *
	 * @return     new matrix with the results
	 */
	public Matrix sigmoid(boolean derivation) {
		double[][] result = new double[this.getRows()][this.getColumn()];
		for (int i = 0; i < this.getRows(); i++) {
			for (int j = 0; j < this.getColumn(); j++) {
				double value = (this.numbers[i][j]);
				if (derivation == true) {
					result[i][j] = sigmoidDerivative(value);
				} else {
					result[i][j] = sigmoid(value);
				}
			}
		}
		return new Matrix(result);
	}

	/**
	 * Get absoloute value of the matrix
	 *
	 * @return     absoloute value of the matrix
	 */
	public Matrix absolute(){
		double[][] result = new double[this.getRows()][this.getColumn()];
		for (int i = 0; i < this.getRows(); i++) {
			for (int j = 0; j < this.getColumn(); j++) {
				double value = (this.numbers[i][j]);
				if(value < 0){
					result[i][j] = (value * -1);
				}else{
					result[i][j] = value;
				}
			}
		}
		return new Matrix(result);	
	}

	/**
	 * Find the mean value of all the matrix values
	 *
	 * @return     the mean
	 */
	public double mean(){
		double mean = 0;
		for (int i = 0; i < this.getRows(); i++) {
			for (int j = 0; j < this.getColumn(); j++) {
				double value = (this.numbers[i][j]);
				mean += value;
			}
		}
		return mean / (this.getRows() * this.getColumn());
	}

	/**
	 * Add random values to the matrix
	 *
	 * @return     random matrix
	 */
	public Matrix random(){
		for (int i = 0; i < this.getRows(); i++) {
			for (int j = 0; j < this.getColumn(); j++) {
				this.numbers[i][j] = (Math.random() * 2) - 1;
			}
		}
		return this;
	}
}