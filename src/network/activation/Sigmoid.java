package network.activation;

public class Sigmoid extends ActivationFunction {

	@Override
	public float activation(float value) {
		return (float) (1.0f / (1.0f + Math.exp(-value)));
	}

	@Override
	public float derivative(float value) {
		return (float) (0.25 * Math.exp(-0.2 * Math.pow(value, 2)));
	}
	
}
