package network.layers;

public class Neuron {

	static float maxWeight;
	static float minWeight;
	
	public float value = 0;
	float[][][] weights;
	float[][][] cachedWeights;
	float gradient;
	float bias;
	
	boolean enabled = true;
	
	//Hidden and output neurons
	public Neuron(float[][][] weights, float bias) {
		this.weights = weights;
		this.bias = bias;
		this.cachedWeights = this.weights;
		this.gradient = 0;
	}
	
	//Input neurons
	public Neuron(float value) {
		this.value = value;
		this.weights = null;
		this.cachedWeights = null;
		this.bias = -1;
		this.gradient = -1;
	}
	
	public static void setWeightRange(float min, float max) {
		maxWeight = max;
		minWeight = min;
	}
	
	public void updateWeights() {
		this.weights = this.cachedWeights;
	}
	
}
