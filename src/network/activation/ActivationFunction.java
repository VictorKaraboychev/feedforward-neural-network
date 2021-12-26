package network.activation;

public abstract class ActivationFunction {
	
	public abstract float activation(float value);
	public abstract float derivative(float value); 
	
	public static float[][][] softmax(float[][][] input) {
		float sumOfExp = 0;
		float [][][] output = new float[input.length][input[0].length][input[0][0].length];
		
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[0].length; j++) {
				for (int k = 0; k < input[0][0].length; k++) {
					sumOfExp += Math.pow(Math.E, input[i][j][k]);
				}
			}
		}
		
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[0].length; j++) {
				for (int k = 0; k < input[0][0].length; k++) {
					output[i][j][k] = (float) (Math.pow(Math.E, input[i][j][k]) / sumOfExp);
				}
			}
		}
		return output;
	}
}