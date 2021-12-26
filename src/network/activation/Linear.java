package network.activation;

public class Linear extends ActivationFunction {

	@Override
	public float activation(float value) {
		return value;
	}

	@Override
	public float derivative(float value) {
		return 1;
	}
	
}
