package network.activation;

public class ReLU extends ActivationFunction {

	@Override
	public float activation(float value) {
		return Math.max(0, value);
	}

	@Override
	public float derivative(float value) {
		float output = 0;
		if (value > 0)
			output = 1;
		
		return output;
	}
	
}
