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
}
