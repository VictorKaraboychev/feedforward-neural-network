package network.activation;

public class ELU extends ActivationFunction {
	private float alpha = 1.0f;

	@Override
	public float activation(float value) {
		return (float) Math.max(this.alpha * (Math.exp(value) - 1), value);
	}

	@Override
	public float derivative(float value) {
		float output = (float) (this.alpha * Math.exp(value));
		if (value > 0)
			output = 1;
		
		return output;
	}
	
}
