package network.layers;

import utilities.TrainingData;
import utilities.Vector3;

public abstract class Layer {
	public Neuron[][][] neurons;
	public Vector3 neuronConfig;
	int type;
	
	public abstract void forward(Layer prevLayer);
	public abstract void backward(Layer nextLayer, Layer prevLayer, TrainingData data, float learningRate);
	
	public void dropOut(float percent) {
		for (int i = 0; i < neuronConfig.x; i++) {
			for (int j = 0; j < neuronConfig.y; j++) {
				for (int k = 0; k < neuronConfig.z; k++) {
					neurons[i][j][k].enabled = true;
					neurons[i][j][k].enabled = Math.random() > percent;
				}
			}
		}
	}
	
	public void enable() {
		for (int i = 0; i < neuronConfig.x; i++) {
			for (int j = 0; j < neuronConfig.y; j++) {
				for (int k = 0; k < neuronConfig.z; k++) {
					neurons[i][j][k].enabled = true;
				}
			}
		}
	}
	
	protected float sumGradient(Vector3 neuronIndex, Layer currentLayer) {
		float gradientSum = 0;
		
		//For all neurons
		for (int i = 0; i < currentLayer.neuronConfig.x; i++) {
			for (int j = 0; j < currentLayer.neuronConfig.y; j++) {
				for (int k = 0; k < currentLayer.neuronConfig.z; k++) {
					Neuron currentNeuron = currentLayer.neurons[i][j][k];
					gradientSum += currentNeuron.weights[neuronIndex.x][neuronIndex.y][neuronIndex.z] * currentNeuron.gradient;	
				}
			}	
		}
		return gradientSum;
	}
}
