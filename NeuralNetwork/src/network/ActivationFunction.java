package network;

public class ActivationFunction {

	public static int NONE = 0;
	public static int SIGMOID = 1;
	public static int RELU = 2;
	public static int LEAKY_RELU = 3;
	public static int TANH = 4;
	
	private int function;
	
	public ActivationFunction(int function) {
		this.function = function;
	}
	
	public float getActivation(float value) {
		float output = 0;
		
		switch (function) {
			case 0:	
				output = value;
			break;	
			case 1:	
				output = sigmoid(value);
			break;
			case 2:	
				output = ReLU(value);
			break;
			case 3:	
				output = leakyReLU(value);
			break;
			case 4:
				output = tanh(value);
			break;
		}
		
		return output;
	}
	
	public float getDerivative(float value) {
		float derivative = 0;
		
		switch (function) {
			case 1:	
				derivative = sigmoidDerivative(value);
			break;
			case 2:	
				derivative = ReLUDerivative(value);
			break;
			case 3:	
				derivative = leakyReLUDerivative(value);
			break;
			case 4:
			derivative = tanhDerivative(value);
			break;
		}
		
		return derivative;
	}
	
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
	
	private static float sigmoid(float value) {
		return (float) (1.0f / (1.0f + Math.exp(-value)));
	}
	
	private static float sigmoidDerivative(float value) {
		return (float) (0.25 * Math.exp(-0.2 * Math.pow(value, 2)));
	}
	
	private static float ReLU(float value) {
		return Math.max(0, value);
	}
	
	private static float ReLUDerivative(float value) {
		float output = 0;
		if (value > 0)
			output = 1;
		return output;
	}
	
	private static float leakyReLU(float value) {
		return Math.max(0.05f * value, value);
	}
	
	private static float leakyReLUDerivative(float value) {
		float output = -0.05f;
		if (value > 0)
			output = 1;
		return output;
	}

	private static float tanh(float value) {
		return (float) Math.tanh(value);
	}

	private static float tanhDerivative(float value) {
		return (float) (1.0 / Math.pow(Math.cosh(value), 2));
	}
}
