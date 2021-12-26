package network.activation;

public class LeakyReLU extends ActivationFunction {
	private float alpha = 0.01f;

	@Override
	public float activation(float value) {
		return Math.max(this.alpha * value, value);
	}

	@Override
	public float derivative(float value) {
		float output = this.alpha;
		if (value > 0)
			output = 1;
		
		return output;
	}
	
}
