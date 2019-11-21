package com;

import java.util.HashMap; 

/**
 * This class describes a layer.
 */
class Layer{
	public int value;
	public Layer next;
	public Layer back;

	public Matrix weight;
	public Matrix weightSigmoidFree;
	public Matrix synapse;

	//	used for testing
	public Matrix delta; 
	public Matrix backPropValue;

	public int id;
	public String name;
	public int [] shape;

	/**
	 * Constructs a new layer.
	 *
	 * @param      inputMatrix     set the weight and synapse
	 */
	public Layer(Matrix inputMatrix){
		this.weight = inputMatrix;
		this.synapse = inputMatrix;

		this.next = null;
		this.back = null;		
	}

	/**
	 * Constructs a new layer.
	 */
	public Layer(){
		this.next = null;
		this.back = null;
	}

	/**
	 * Constructs a new layer.
	 *
	 * @param      rows     The row count
	 * @param      columns  The column count
	 */
	public Layer(int rows, int columns){
		this.shape = new int[3];
		this.shape[0] = rows;
		this.shape[1] = columns;

		this.weight = new Matrix(rows, columns).random();
		this.synapse = new Matrix(rows, columns).random();

		this.next = null;
		this.back = null;

		this.id = 0;
	}

	/**
	 * Constructs a new layer.
	 *
	 * @param      row     The row count
	 * @param      column  The column count
	 * @param      name    The name of the layer
	 */
	public Layer(int row, int column, String name){
		this.shape = new int[3];
		this.shape[0] = row;
		this.shape[1] = column;

		this.weight = new Matrix(row, column).random();
		this.synapse = new Matrix(row, column).random();
		this.name = name;

		this.next = null;
		this.back = null;

		this.id = 0;
	}

	/**
	 * Constructs a new instance.
	 *
	 * @param      inputMatrix  The input matrix
	 * @param      name         The name
	 */
	public Layer(Matrix inputMatrix, String name){
		this.shape = new int[3];
		this.shape[0] = inputMatrix.numbers.length;
		this.shape[1] = inputMatrix.numbers[0].length;

		this.weight = inputMatrix;
		this.synapse = inputMatrix;
		this.name = name;

		this.next = null;
		this.back = null;

		this.id = 0;
	}

	/**
	 * Set the synapse + weight value of the layer
	 *
	 * @param      input  The input
	 */
	public void intialize(Matrix input){
		this.shape = new int[3];
		this.shape[0] = input.getRows();
		this.shape[1] = input.getColumn();

		this.synapse = input;
		this.weight = input;

	}

	/**
	 * Sets the identifier.
	 *
	 * @param      i     The new value
	 */
	public void setId(int i){
		this.id = i;
	}

	/**
	 * get the layer shape
	 *
	 * @return     String representation of layer shaep
	 */
	public String shape(){
		return "(" + this.shape[0] + ", " + this.shape[1] + ")";
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return     String representation of the object.
	 */
	public String toString(){
		return this.name + "\t" + shape();
	}
}

/**
 * This class describes a network .
 */
class Network{
	public Layer previousLayer;
	public Layer orginalRoot;
	public Layer lastLayer;
	public Layer root;
	
	public int count;
	public Matrix error; 
	public HashMap<String, Layer> layerLookup;

	public boolean fitSetup;
	public double learningRate;
	public boolean silent;

	/**
	 * Constructs a new network.
	 */
	public Network(){
		this.root = new Layer();
		this.layerLookup  = new HashMap<String, Layer>();
		this.orginalRoot = this.root;
		this.previousLayer = null;
		this.fitSetup = false;

		// 0 will be used for data Layer later
		this.count = 1; 
		this.learningRate = 0.001;
	}

	/**
	 * Make the network learn without printing error (used in testing)
	 */
	public void silent(){
		silent = true;
	}

	/**
	 * Adds the specified layer to the network.
	 *
	 * @param      newNode  The new node
	 */
	public void add(Layer newNode){
		//	linkedlist style
		if(this.previousLayer == null){
			newNode.back = this.root;
			this.root.next = newNode;
			this.previousLayer = newNode;
		}else{
			newNode.back = this.previousLayer;
			this.previousLayer.next = newNode;
			this.previousLayer = newNode;
		}
		newNode.setId(this.count++);
		if(newNode.name != null){
			layerLookup.put(newNode.name, newNode);
		}
	}

	/**
	 * forward propgate the input
	 *
	 * @param      predict  to predict or not
	 */
	public void forwardPropgate(boolean predict){
		Layer currentLayer = root.next;
		while(currentLayer.next != null || (predict == true)){
			currentLayer.weightSigmoidFree = currentLayer.back.weight.dot(currentLayer.synapse);
			currentLayer.weight = currentLayer.weightSigmoidFree.sigmoid(false);	

			if(currentLayer.next == null){
				break;
			}
			currentLayer = currentLayer.next;
		}
		if(!predict){
			error = currentLayer.back.weight.sub(currentLayer.weight);
			lastLayer = currentLayer.back;
		}else{
			lastLayer = currentLayer;
		}
	}

	/**
	 * Preform backpropgate
	 */
	public void backwardPropgate(){
		//	need to calculate the delta of change from the last Layer first.
		Layer currentLayer = lastLayer;

		currentLayer.delta = (error.directMultiply(currentLayer.weight.sigmoid(true)));		
		currentLayer.backPropValue = currentLayer.back.weight.transpose().dot(currentLayer.delta);
		
		currentLayer = currentLayer.back;
		
		//	how big was the error in each Layer?
		while(1 <= currentLayer.id){
			currentLayer.delta = currentLayer.next.delta.dot(currentLayer.next.synapse.transpose()).directMultiply(currentLayer.weight.sigmoid(true));			

			// we can not update the synapse in the forward layer			
			currentLayer.next.synapse = currentLayer.next.synapse.sub(currentLayer.next.backPropValue.multiplyConstant(this.learningRate));
	
			currentLayer.backPropValue = currentLayer.back.weight.transpose().dot(currentLayer.delta);
			currentLayer = currentLayer.back;
		}
		currentLayer.next.synapse = currentLayer.next.synapse.sub(currentLayer.next.backPropValue.multiplyConstant(this.learningRate));
	}

	/**
	 * Try to create a function that maps x to y
	 *
	 * @param      x       x
	 * @param      y       y
	 * @param      epochs  how many times to FF+Backprop
	 */
	public void fit(Matrix x, Matrix y, int epochs){
		root.intialize(x);
		root.name = "input layer";

		if(!this.fitSetup){
			Layer lastLayer = new Layer(y);
			lastLayer.back = previousLayer;
			lastLayer.name = "output layer";
	
			previousLayer.next = lastLayer;
			previousLayer = lastLayer;
			this.fitSetup = true;
		}else{
			previousLayer.intialize(y);
		}

		for(int i = 0; i < epochs; i++){
			forwardPropgate(false);
			backwardPropgate();
			if(i % 100 == 0 && !silent){
				System.out.print("Error : ");
				System.out.println(error.absolute().mean());
			}
		}
	}

	/**
	 * Get the error from last run
	 *
	 * @return     The error.
	 */
	public double getError(){
		return error.absolute().mean();
	}

	/**
	 * Predict based on the input
	 *
	 * @param      input  The input
	 *
	 * @return     the output weigths
	 */
	public Matrix predict(Matrix input){
		this.root.intialize(input);
		this.root.setId(0);

		this.forwardPropgate(true);
		return this.lastLayer.weight;
	}

	/**
	 * view th model state
	 */
	public void viewState(){
		Layer start = this.root;
		while(start != null){
			System.out.println(start.name);

			System.out.println(start.weight);
			System.out.println(start.synapse);
			System.out.println("\n");
			
			start = start.next;
		}
	}

	/**
	 * Print the model structure
	 */
	public void summary(){
		Layer start = this.root;
		while(start != null){
			System.out.println(start == null);
			System.out.println(start.name);
			System.out.println(start.shape());
			
			start = start.next;
		}
	}
}