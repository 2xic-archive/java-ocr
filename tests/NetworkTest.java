package com;

import junit.framework.TestCase;
import com.Matrix;
import com.DataHandler;

public class NetworkTest extends TestCase {
	public Network model;
	public Matrix x;
	public Matrix y;

	public void setUp() throws Exception {
		x = new Matrix(new double[][] {
			{0, 0, 1},
			{0, 1, 1},
			{1, 0, 1},
			{1, 1, 1}
		});

		y = new Matrix(new double[][] {
			{0},
			{1},
			{1},
			{0}
		});

		model = new Network();
		model.add(new Layer(new Matrix(new double[][]{  
			{0.09762701, 0.43037873, 0.20552675, 0.08976637},
			{-0.1526904,0.29178823, -0.12482558,  0.783546},
			{0.92732552,-0.23311696,  0.58345008,  0.05778984}
		}), "test"));
		
		model.add(new Layer(new Matrix(new double[][]{  
			{0.13608912},
			{0.85119328},
			{-0.85792788},
			{-0.8257414}
		}), "test2"));
		model.silent();
	}
	
	
	/**
	 * Test that network will learn xor
	 */
	public void testXOR(){
		Matrix x = new Matrix(new double[][] {
			{0, 0, 1},
			{0, 1, 1},
			{1, 0, 1},
			{1, 1, 1}
		});

		Matrix y = new Matrix(new double[][] {
			{0},
			{1},
			{1},
			{0}
		});

		Network model = new Network();
		model.add(new Layer(new Matrix(new double[][]{  
			{0.09762701, 0.43037873, 0.20552675, 0.08976637},
			{-0.1526904,0.29178823, -0.12482558,  0.783546},
			{0.92732552,-0.23311696,  0.58345008,  0.05778984}
		}), "test"));
		
		model.add(new Layer(new Matrix(new double[][]{  
			{0.13608912},
			{0.85119328},
			{-0.85792788},
			{-0.8257414}
		}), "test2"));
		model.silent();
	
		model.fit(x, y, 1000);

		assert (Math.round(model.predict(new Matrix(new double[][]{{0, 0, 1}})).numbers[0][0])) == 0;       
		assert (Math.round(model.predict(new Matrix(new double[][]{{0, 1, 1}})).numbers[0][0])) == 1;   
	}
	/**
	 * Testing that the netowrk will fit small parts of the actual dataset
	 */
	public void testSmall(){
		DataHandler dataset = new DataHandler(256);
		dataset.makeDataSet("/target/training/");
		x = new Matrix(dataset.getX());
		y = new Matrix(dataset.getY());

		Layer layer1 = new Layer(new Matrix(49, 64).random(), "test");
		Layer layer2 = new Layer(new Matrix(64, 10).random(), "test2");

		model = new Network();
		model.silent();
		model.add(layer1);
		model.add(layer2);
		model.fit(x, y, 1);

		double error = model.getError();

		model.fit(x, y, 100);

		assertEquals(model.getError() < error, true);
	}

	/**
	 * Testing that forward propagation works as expected
	 */
	public void testForward(){
		model.fit(x, y, 1);
		Matrix erro_matrix = new Matrix(new double[][]{
			{ 0.37713134},
			{-0.63908421},
			{-0.61434337},
			{ 0.36933085}
		});

		Matrix layer_one_weigths = new Matrix(new double[][]{
			{0.71653238,0.44198326,0.64186088,0.51444344},
			{0.68452271,0.51466361,0.61268782,0.69874648},
			{0.73593617,0.54915615,0.68761159,0.53682227},
			{0.70521618,0.61988261,0.66019229,0.71729885}
		});
		assertEquals((model.orginalRoot.next.weight.round(8)).equalMatrix(layer_one_weigths), true);

		Matrix layer_two_weigths = new Matrix(new double[][]{
			{0.37713134},
			{0.36091579},
			{0.38565663},
			{0.36933085}
		});
		assertEquals((model.orginalRoot.next.next.weight.round(8)).equalMatrix(layer_two_weigths), true);		
		assertEquals(erro_matrix.equalMatrix(model.error.round(8)), true);
	}

	/**
	 * Testing that backpropagation works as expected
	 */
	public void testBackward(){
		model.fit(x, y, 1);
		Matrix erro_matrix = new Matrix(new double[][]{
			{ 0.37713134},
			{-0.63908421},
			{-0.61434337},
			{ 0.36933085}
		});

		Matrix layer2Delta = new Matrix(new double[][]{
			{ 0.08858939},
			{-0.14740834},
			{-0.14555367},
			{ 0.0860266}
		});

		Matrix layer1Delta = new Matrix(new double[][]{
			{ 0.00244875,0.01859786, -0.0174713,-0.01827272},
			{-0.00433213, -0.03134127, 0.0300105, 0.02562229},
			{-0.00384942, -0.03067421, 0.02682329, 0.02988446},
			{ 0.00243378 , 0.01725393, -0.01655721, -0.01440471}
		});

		assertEquals((model.layerLookup.get("test2").delta.round(8)).equalMatrix(layer2Delta), true);		
		assertEquals((model.layerLookup.get("test").delta.round(8)).equalMatrix(layer1Delta), true);		
		
		Matrix layer2Backpropvalue = new Matrix(new double[][]{
			{-0.08387805},
			{-0.06331598},
			{-0.07674352},
			{-0.0738565}
		});


		Matrix layer1Backpropvalue = new Matrix(new double[][]{
			{-0.00141564, -0.01342027, 0.01026608, 0.01547975},
			{-0.00189835, -0.01408733, 0.01345329, 0.01121758},
			{-0.00329902, -0.02616368, 0.02280528, 0.02282931}
		});

		assertEquals(model.layerLookup.get("test2").backPropValue.round(8).equalMatrix(layer2Backpropvalue), true);		
		assertEquals(model.layerLookup.get("test").backPropValue.round(8).equalMatrix(layer1Backpropvalue), true);		

		Matrix newSynapseValue2 = new Matrix(new double[][]{
			{0.136173},
			{0.8512566},
			{-0.85785114},
			{-0.82566754}
		});


		Matrix newSynapseValue = new Matrix(new double[][]{
			{0.09762843, 0.43039215, 0.20551648, 0.08975089},
			{-0.1526885, 0.29180232, -0.12483903, 0.78353478},
			{0.92732882, -0.2330908, 0.58342727, 0.05776701}
		});

		assertEquals(model.layerLookup.get("test2").synapse.round(8).equalMatrix(newSynapseValue2), true);		
		assertEquals(model.layerLookup.get("test").synapse.round(8).equalMatrix(newSynapseValue), true);				
	}
}





