package network;

import tools.NetworkUtil;
import tools.Vector3;

public class Network {
	
	NetworkConfig netConfig;
	Layer[] layers;
	
	public Network(NetworkConfig netConfig) {
		this.netConfig = netConfig;
		this.layers = new Layer[this.netConfig.networkLayers];
		
		//Initialize first layer
		this.layers[0] = null;
		
		//For all hidden layers
		for (int i = 1; i < this.netConfig.outputIndex; i++) {	
			//Initialize layer type
			if (this.netConfig.getLayerType(i) == NetworkConfig.FULLY_CONNECTED) {
				//Create the layer as a fully connected layer			
				this.layers[i] = new FullyConnectedLayer(this.netConfig.neuronConfig[i-1], this.netConfig.neuronConfig[i], this.netConfig.activationFunction[i]);
			
			} else if (this.netConfig.getLayerType(i) == NetworkConfig.CONVOLUTIONAL) {
				//Create the layer as a convolutional layer
				this.layers[i] = new ConvolutionalLayer(this.netConfig.neuronConfig[i-1], this.netConfig.depth[i], this.netConfig.kernelFilter[i], this.netConfig.stride[i], this.netConfig.activationFunction[i]);
			
			} else if (this.netConfig.getLayerType(i) == NetworkConfig.POOLING) {
				//Create the layer as a pooling layer
				this.layers[i] = new PoolingLayer(this.netConfig.neuronConfig[i-1], this.netConfig.kernelFilter[i]);
			}
			this.netConfig.neuronConfig[i] = this.layers[i].neuronConfig;
		}
		
		//Initialize output layer
		int outputLayerIndex = this.netConfig.outputIndex;
		this.layers[outputLayerIndex] = new FullyConnectedLayer(this.netConfig.neuronConfig[outputLayerIndex-1], this.netConfig.neuronConfig[outputLayerIndex], this.netConfig.activationFunction[outputLayerIndex]);
	}
	
	public float[][][] process(float[][][] input) {
		Vector3 outputNeurons = this.netConfig.neuronConfig[this.netConfig.outputIndex];
		float[][][] output = new float[outputNeurons.x][outputNeurons.y][outputNeurons.z];
		
		forward(input);
		
		for (int i = 0; i < outputNeurons.x; i++) {
			for (int j = 0; j < outputNeurons.y; j++) {
				for (int k = 0; k < outputNeurons.z; k++) {
					output[i][j][k] = this.layers[this.netConfig.outputIndex].neurons[i][j][k].value;
				}
			}
		}	
		// output = ActivationFunction.softmax(output);
		
		return output;
	}
	
	public void forward(float[][][] input) {
		this.layers[0] = new FullyConnectedLayer(input);
		
		for (int i = 1; i < this.netConfig.networkLayers; i++) {
			this.layers[i].forward(this.layers[i-1]);
		}
	}
	
	public void backward(TrainingData data, float learningRate) {
		for (int i = this.netConfig.outputIndex; i > 0; i--) {
			Layer nextLayer = null;
			if (i < this.netConfig.outputIndex) {
				nextLayer = this.layers[i+1];
//				if (this.netConfig.getLayerType(i) == NetworkConfig.CONVOLUTIONAL) {
//					nextLayer = this.layers[i+2];
//				}
			}
			
			this.layers[i].backward(nextLayer, this.layers[i-1], data, learningRate);
		}
	}
	
	public float getError(float[][][] target) {
		Vector3 outputNeurons = this.netConfig.neuronConfig[this.netConfig.outputIndex];
		float sum = 0;
		
		for (int i = 0; i < outputNeurons.x; i++) {
			for (int j = 0; j < outputNeurons.y; j++) {
				for (int k = 0; k < outputNeurons.z; k++) {
					sum += NetworkUtil.avgSqrError(this.layers[this.netConfig.outputIndex].neurons[i][j][k].value, target[i][j][k]);
				}
			}
		}
		return sum;
	}
	
	public void train(TrainingData[] dataSet, float learningRate, float dropOutPercent, int trainingIterations) {
		for (int i = 0; i < trainingIterations; i++) {
			float avgError = 0;
			for (int j = 0; j < dataSet.length; j++) {
				dropOut(dropOutPercent);
				forward(dataSet[j].data);
				backward(dataSet[j], learningRate);
				
				avgError += getError(dataSet[j].expectedOutput);
				dropOut(0);
			}
			avgError /= dataSet.length;
			
			System.out.println("[DEBUG] Iteration: " + i + " | Average Error: " + avgError);
			NeuralNetwork.outputData(this, NeuralNetwork.trainingDataSet);
		}
	}
	
	public void dropOut(float percentage) {
		for (int i = 1; i < this.netConfig.outputIndex; i++) {
			if (this.netConfig.getLayerType(i) == NetworkConfig.FULLY_CONNECTED) {
				this.layers[i].dropOut(percentage);
			}
		}
	}
}
