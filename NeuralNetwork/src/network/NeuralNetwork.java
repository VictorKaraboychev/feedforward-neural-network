package network;

import tools.ImageUtil;
import tools.NetworkUtil;
import tools.Vector3;

public class NeuralNetwork {
	static Network network;
	
	static NetworkConfig netConfig;
	
	static final String path = "src/network/data/";
	
	static TrainingData[] trainingDataSet;
	static TrainingData[] testingDataSet;
	
	public static void main(String[] args) {	
		Neuron.setWeightRange(-1, 1);
		
		netConfig = new NetworkConfig();
		netConfig.addFullyConnectedLayer(new Vector3(10, 1, 1), ActivationFunction.NONE);
		netConfig.addFullyConnectedLayer(new Vector3(16, 1, 1), ActivationFunction.SIGMOID);
		netConfig.addFullyConnectedLayer(new Vector3(32, 1, 1), ActivationFunction.SIGMOID);
		netConfig.addFullyConnectedLayer(new Vector3(32, 32, 3), ActivationFunction.SIGMOID);
		
		network = new Network(netConfig);
		
		populateTrainingData();
		
		System.out.println("[INFO] Initial Outputs");
		outputData(network, testingDataSet);
		
		System.out.println("[INFO] Training");
		network.train(trainingDataSet, 0.05f, 0.01f, 500);
		
		System.out.println("[INFO] Final Outputs");
		outputData(network, testingDataSet);
	}
	
	public static void populateTrainingData() {
		String training = path + "training/digits/";	
		String testing = path + "testing/digits/";
		
		trainingDataSet = ImageUtil.getImageData(training, false);
		testingDataSet = ImageUtil.getImageData(testing, false);
	}
	
	public static void outputData(Network neuralNetwork, TrainingData[] dataSet) {
		for (int i = 0; i < dataSet.length; i++) {
			float[][][]  outputData = neuralNetwork.process(dataSet[i].data);
			
			String output = path + "output/";
			
			
			int layerNum = network.layers.length-1;
			float[][][] layer = new float[neuralNetwork.layers[layerNum].neurons.length][neuralNetwork.layers[layerNum].neurons[0].length][neuralNetwork.layers[layerNum].neurons[0][0].length];
			for (int j = 0; j < neuralNetwork.layers[layerNum].neurons.length; j++) {
				for (int k = 0; k < neuralNetwork.layers[layerNum].neurons[0].length; k++) {
					for (int l = 0; l < neuralNetwork.layers[layerNum].neurons[0][0].length; l++) {
						layer[j][k][l] = neuralNetwork.layers[layerNum].neurons[j][k][l].value;
					}
				}
			}
			
			// Text output direct print
			// for (int j = 0; j < outputData.length; j++) {
			// 	char symbol = Math.round(outputData[j][0][0]) == 1 ? '■' : '□';
			// 	System.out.print(symbol + " ");
			// }
			// System.out.println();
			
			// Image output saved to file
			ImageUtil.dataToImage(output, layer, i);
		}
	}
}
