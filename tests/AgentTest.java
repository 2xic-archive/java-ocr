package com;

import junit.framework.TestCase;
import com.Agent;

public class AgentTest extends TestCase {
	public void setUp() throws Exception {

	}

	public static double[][] flip(double[][] input){
		for(int i = 0; i < input[0].length; i++){
			if(input[0][i] == 0){
				input[0][i] = 1;
			}else{
				input[0][i] = 0;
			}
		}
		return input;
	}

	public void testPreTrainedModel(){
		Agent agent = new Agent();
		assertEquals(agent.predictNumber(
			flip(new double[][]{
				{0,1,1,1,1,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,1,1,1,1,0,0}
			}), false), 5);

		assertEquals((agent.predictNumber(flip(new double[][]{
				{0,0,1,1,1,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,1,1,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,1,1,1,1,0,0}
			}), false) 
		), 3);
	}

	public void testPreTrainedModelValidation(){
		Agent agent = new Agent();

		DataHandler validation = new DataHandler(256);
		DataHandler.makeDataSet("/target/dataset/validation/");
		double score = 0;
		Matrix x = new Matrix(validation.getX());
		Matrix y = new Matrix(validation.getY());

		for(int i = 0; i < x.getRows(); i++){
			if(agent.predictNumber((new double[][]{x.numbers[i]}), false) == (agent.getMaxIndex(y.numbers[i]))){
				score ++;
			}
		}
		assertEquals(0.8 <= (score/y.getRows()), true);
	}
}
