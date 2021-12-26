package network.layers;

import network.NetworkConfig;
import network.activation.ActivationFunction;
import utilities.NetworkUtil;
import utilities.TrainingData;
import utilities.Vector3;

public class FullyConnectedLayer extends Layer {
	ActivationFunction activationFunction;
	
	//Input Layer
	public FullyConnectedLayer(float input[][][]) {
		this.neurons = new Neuron[input.length][input[0].length][input[0][0].length];

		this.neuronConfig = new Vector3(input.length, input[0].length, input[0][0].length);		
		super.type = NetworkConfig.FULLY_CONNECTED;
		
		for (int i = 0; i < this.neurons.length; i++) {
			for (int j = 0; j < this.neurons[0].length; j++) {
				for (int k = 0; k < this.neurons[0][0].length; k++) {
					this.neurons[i][j][k] = new Neuron(input[i][j][k]);
				}
			}
		}
	}
	
	//Hidden and output layers
	public FullyConnectedLayer(Vector3 inputNeuronNum ,Vector3 neuronNum, ActivationFunction activation) {
		this.neurons = new Neuron[neuronNum.x][neuronNum.y][neuronNum.z];	
		this.neuronConfig = new Vector3(neuronNum.x, neuronNum.y, neuronNum.z);
		
		this.activationFunction = activation;
		super.type = NetworkConfig.FULLY_CONNECTED;
		
		for (int i = 0; i < neuronNum.x; i++) {
			for (int j = 0; j < neuronNum.y; j++) {
				for (int k = 0; k < neuronNum.z; k++) {
					float[][][] weights = new float[inputNeuronNum.x][inputNeuronNum.y][inputNeuronNum.z];
			
					for (int l = 0; l < inputNeuronNum.x; l++) {
						for (int m = 0; m < inputNeuronNum.y; m++) {
							for (int n = 0; n < inputNeuronNum.z; n++) {
								weights[l][m][n] = NetworkUtil.randomRange(Neuron.minWeight, Neuron.maxWeight);
							}
						}
					}	
					neurons[i][j][k] = new Neuron(weights, (float) Math.random());
				}
			}
		}
	}
	
	@Override
	public void forward(Layer prevLayer) {
		//For all neurons
		for (int i = 0; i < this.neuronConfig.x; i++) {
			for (int j = 0; j < this.neuronConfig.y; j++) {
				for (int k = 0; k < this.neuronConfig.z; k++) {
					
					if (this.neurons[i][j][k].enabled) {
						float sum = this.neurons[i][j][k].bias;
						
						//For previous neurons
						for (int l = 0; l < prevLayer.neuronConfig.x; l++) {
							for (int m = 0; m < prevLayer.neuronConfig.y; m++) {
								for (int n = 0; n < prevLayer.neuronConfig.z; n++) { 
									sum += prevLayer.neurons[l][m][n].value * this.neurons[i][j][k].weights[l][m][n];
								}
							}
						}
						//Update neuron value
						this.neurons[i][j][k].value = this.activationFunction.activation(sum);
					}
				}
			}
		}
	}
	
	@Override
	public void backward(Layer nextLayer, Layer prevLayer, TrainingData data, float learningRate) {
		//For all neurons
		for (int i = 0; i < this.neuronConfig.x; i++) {
			for (int j = 0; j < this.neuronConfig.y; j++) {
				for (int k = 0; k < this.neuronConfig.z; k++) {
					float output = this.neurons[i][j][k].value;
					float error;
					
					//Calculate error signal
					if (nextLayer == null) {
						//Output layer
						float target = data.expectedOutput[i][j][k];
						error = output - target;
					} else {
						//Middle layer
						error = super.sumGradient(new Vector3(i, j, k), nextLayer);
					}
					float errorSignal = error * this.activationFunction.derivative(output);
					
					//Update gradient
					this.neurons[i][j][k].gradient = errorSignal;
					
					//Update bias
					float deltaBias = -learningRate * errorSignal;
					this.neurons[i][j][k].bias += deltaBias;
					
					//For all weights
					for (int l = 0; l < this.neurons[i][j][k].weights.length; l++) {
						for (int m = 0; m < this.neurons[i][j][k].weights[0].length; m++) {
							for (int n = 0; n < this.neurons[i][j][k].weights[0][0].length; n++) {
								float previousValue = prevLayer.neurons[l][m][n].value;
								float deltaWeight = deltaBias * previousValue;
								
								//Update cached weights
								this.neurons[i][j][k].cachedWeights[l][m][n] = this.neurons[i][j][k].weights[l][m][n] + deltaWeight;
							}
						}
					}
				}
			}
		}
		
		//Update all weights
		for (int i = 0; i < this.neuronConfig.x; i++) {
			for (int j = 0; j < this.neuronConfig.y; j++) {
				for (int k = 0; k < this.neuronConfig.z; k++) {
					this.neurons[i][j][k].updateWeights();
				}
			}
		}
	}
}
