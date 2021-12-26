package network.activation;

public class Tanh extends ActivationFunction {

	@Override
	public float activation(float value) {
		return (float) Math.tanh(value);
	}

	@Override
	public float derivative(float value) {
		return (float) (1.0 / Math.pow(Math.cosh(value), 2));
	}
	
}
