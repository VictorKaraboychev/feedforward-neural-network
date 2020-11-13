package network;

import java.util.Arrays;

import tools.Vector3;

public class NetworkConfig {

	public static int FULLY_CONNECTED = 0;
	public static int CONVOLUTIONAL = 1;
	public static int POOLING = 2;
	
	//Fully connected layers
	private int[] layerType;
	Vector3[] neuronConfig;
	ActivationFunction[] activationFunction;
	
	//Convolutional layers
	int[] depth;
	int[] kernelFilter;
	int[] stride;
	
	public int networkLayers = 0;
	public int outputIndex = -1;
	
	public NetworkConfig() {
		this.layerType = new int[0];
		this.neuronConfig = new Vector3[0];
		this.activationFunction = new ActivationFunction[0];
		
		this.depth = new int[0];
		this.kernelFilter = new int[0];
		this.stride = new int[0];
	}
	
	public void addFullyConnectedLayer(Vector3 neurons, int activationFunction) {
		this.networkLayers++;
		this.outputIndex = this.networkLayers - 1;
		
		this.layerType = Arrays.copyOf(this.layerType, this.networkLayers);
		this.neuronConfig = Arrays.copyOf(this.neuronConfig, this.networkLayers);
		this.activationFunction = Arrays.copyOf(this.activationFunction, this.networkLayers);
		
		this.depth = Arrays.copyOf(this.depth, this.networkLayers);
		this.kernelFilter = Arrays.copyOf(this.kernelFilter, this.networkLayers);
		this.stride = Arrays.copyOf(this.stride, this.networkLayers);
		
		this.layerType[this.outputIndex] = NetworkConfig.FULLY_CONNECTED;
		this.neuronConfig[this.outputIndex] = neurons;
		this.activationFunction[this.outputIndex] = new ActivationFunction(activationFunction);
		this.depth[this.outputIndex] = -1;
		this.kernelFilter[this.outputIndex] = -1;
		this.stride[this.outputIndex] = -1;
	}
	
	public void addConvolutionalLayer(int depth, int kernelFilter, int stride, int activationFunction) {
		this.networkLayers++;
		this.outputIndex = this.networkLayers - 1;
		
		this.layerType = Arrays.copyOf(this.layerType, this.networkLayers);
		this.neuronConfig = Arrays.copyOf(this.neuronConfig, this.networkLayers);
		this.activationFunction = Arrays.copyOf(this.activationFunction, this.networkLayers);
		
		this.depth = Arrays.copyOf(this.depth, this.networkLayers);
		this.kernelFilter = Arrays.copyOf(this.kernelFilter, this.networkLayers);
		this.stride = Arrays.copyOf(this.stride, this.networkLayers);
		
		this.layerType[this.outputIndex] = NetworkConfig.CONVOLUTIONAL;
		this.neuronConfig[this.outputIndex] = null;
		this.activationFunction[this.outputIndex] = new ActivationFunction(activationFunction);
		this.depth[this.outputIndex] = depth;
		this.kernelFilter[this.outputIndex] = kernelFilter;
		this.stride[this.outputIndex] = stride;
	}
	
	public void addPoolingLayer(int dimensionalReduction) {
		this.networkLayers++;
		this.outputIndex = this.networkLayers - 1;
		
		this.layerType = Arrays.copyOf(this.layerType, this.networkLayers);
		this.neuronConfig = Arrays.copyOf(this.neuronConfig, this.networkLayers);
		this.activationFunction = Arrays.copyOf(this.activationFunction, this.networkLayers);
		
		this.depth = Arrays.copyOf(this.depth, this.networkLayers);
		this.kernelFilter = Arrays.copyOf(this.kernelFilter, this.networkLayers);
		this.stride = Arrays.copyOf(this.stride, this.networkLayers);
		
		this.layerType[this.outputIndex] = NetworkConfig.POOLING;
		this.neuronConfig[this.outputIndex] = null;
		this.activationFunction[this.outputIndex] = null;
		this.depth[this.outputIndex] = -1;
		this.kernelFilter[this.outputIndex] = dimensionalReduction;
		this.stride[this.outputIndex] = -1;
	}
	
	
	public int getLayerType(int layer) {
		return layerType[layer];
	}
	
	public Vector3 getNeurons(int layer) {
		return neuronConfig[layer];
	}
}
