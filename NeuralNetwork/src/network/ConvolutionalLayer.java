package network;

import tools.NetworkUtil;
import tools.Vector3;

public class ConvolutionalLayer extends Layer {
	ActivationFunction activationFunction;
	Neuron[] kernelFilter;
	Vector3 kernelConfig;
	
	int stride = 1;
	int padding;
	
	public ConvolutionalLayer(Vector3 prevLayerConfig, int depth, int kernelFilter, int stride, ActivationFunction function) {
		this.padding =  (kernelFilter - 1) / 2;
		
		int x = (int) Math.round((prevLayerConfig.x - padding * 2) / (float) stride);
		int y = (int) Math.round((prevLayerConfig.y - padding * 2) / (float) stride);
		
		super.neurons = new Neuron[x][y][depth];	
		super.neuronConfig = new Vector3(x, y, depth);
		super.type = NetworkConfig.CONVOLUTIONAL;
		
		this.activationFunction = function;
		this.stride = stride;
		this.kernelFilter = new Neuron[depth];
		
		for (int i = 0; i < super.neuronConfig.x; i++) {
			for (int j = 0; j < super.neuronConfig.y; j++) {
				for (int k = 0; k < super.neuronConfig.z; k++) {
					super.neurons[i][j][k] = new Neuron(0);
				}
			}
		}
		
		for (int i = 0; i < depth; i++) {
			float[][][] weights = new float[kernelFilter][kernelFilter][prevLayerConfig.z];
			this.kernelConfig = new Vector3(kernelFilter, kernelFilter, prevLayerConfig.z);
			
			for (int l = 0; l < kernelFilter; l++) {
				for (int m = 0; m < kernelFilter; m++) {
					for (int n = 0; n < prevLayerConfig.z; n++) {
						weights[l][m][n] = NetworkUtil.randomRange(Neuron.minWeight, Neuron.maxWeight);
					}
				}
			}			
			this.kernelFilter[i] = new Neuron(weights, (float) Math.random());
		}
	}

	@Override
	public void forward(Layer prevLayer) {
		//For all neurons
		for (int i = 0; i < super.neuronConfig.x; i++) {
			for (int j = 0; j < super.neuronConfig.y; j++) {
				for (int k = 0; k < super.neuronConfig.z; k++) {	
					float sum = this.kernelFilter[k].bias;
					
					int prevX = (i * this.stride);
					int prevY = (j * this.stride);
					
					//For kernel size
					for (int l = 0; l < this.kernelConfig.x; l++) {
						for (int m = 0; m < this.kernelConfig.y; m++) {
							for (int n = 0; n < this.kernelConfig.z; n++) { 								
								sum += prevLayer.neurons[prevX + l][prevY + m][n].value * this.kernelFilter[k].weights[l][m][n];
							}
						}
					}
					//Update neuron value
					this.neurons[i][j][k].value = this.activationFunction.getActivation(sum);
				}
			}
		}	
	}

	@Override
	public void backward(Layer nextLayer, Layer prevLayer, TrainingData data, float learningRate) {	
		
		//For all kernels "i"
		for (int i = 0; i < super.neuronConfig.z; i++) {
			
			//Set cached weights to weights
			this.kernelFilter[i].cachedWeights = this.kernelFilter[i].weights;
			
			//For all neurons on layer "i"
			for (int m = 0; m < super.neuronConfig.x; m++) {
				for (int n = 0; n < super.neuronConfig.y; n++) {
					float error = 0;
					if (nextLayer.type == NetworkConfig.POOLING) {
						error = super.neurons[m][n][i].gradient;
					} else {
						error = super.sumGradient(new Vector3(m, n, i), nextLayer);
					}
					
					if (error != 0) {
						float output = super.neurons[m][n][i].value;
						float errorSignal = error * this.activationFunction.getDerivative(output);
						
						int prevX = (m * this.stride);
						int prevY = (n * this.stride);
						
						//Update gradient
						this.neurons[m][n][i].gradient = errorSignal;
						
						//Update bias
						float deltaBias = -learningRate * errorSignal;
						this.kernelFilter[i].bias += deltaBias;
						
						//For all weights used on the input
						for (int o = 0; o < this.kernelConfig.x; o++) {
							for (int p = 0; p < this.kernelConfig.y; p++) {
								for (int q = 0; q < this.kernelConfig.z; q++) {
									float previousValue = prevLayer.neurons[prevX + o][prevY + p][q].value;
									float deltaWeight = deltaBias * previousValue;

									//Update cached weights
									this.kernelFilter[i].cachedWeights[o][p][q] += deltaWeight;
								}
							}
						} 
					}
				}
			}
		}
		
		//Update all weights
		for (int i = 0; i < this.neuronConfig.z; i++) {
			this.kernelFilter[i].updateWeights();
		}
	}
}
