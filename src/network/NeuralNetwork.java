package network;

import network.activation.*;
import network.layers.Neuron;
import utilities.FileUtil;
import utilities.TrainingData;
import utilities.Vector3;

public class NeuralNetwork {
	static Network network;
	
	static NetworkConfiguration netConfig;
	
	static final String path = "src/data/";
	
	static TrainingData[] trainingDataSet;
	static TrainingData[] testingDataSet;
	
	public static void main(String[] args) {	
		Neuron.setWeightRange(-1, 1);
		
		netConfig = new NetworkConfiguration();
		netConfig.addFullyConnectedLayer(new Vector3(10, 1, 1), new Linear());
		netConfig.addFullyConnectedLayer(new Vector3(16, 1, 1), new Sigmoid());
		netConfig.addFullyConnectedLayer(new Vector3(16, 1, 1), new Sigmoid());
		netConfig.addFullyConnectedLayer(new Vector3(32, 32, 1), new Sigmoid());
		
		network = new Network(netConfig);
		
		populateTrainingData();

		System.out.println("[INFO] Initial Outputs");
		outputData(network, testingDataSet);
		
		System.out.println("[INFO] Training");
		network.train(trainingDataSet, 0.05f, 0.01f, 10);
		
		System.out.println("[INFO] Final Outputs");
		outputData(network, testingDataSet);
	}
	
	public static void populateTrainingData() {
		String training = path + "training/digits/";	
		String testing = path + "testing/digits/";

		trainingDataSet = FileUtil.getImageData(training, false);
		testingDataSet = FileUtil.getImageData(testing, false);

		System.out.println(trainingDataSet.length);
	}
	
	public static void outputData(Network neuralNetwork, TrainingData[] dataSet) {
		for (int i = 0; i < dataSet.length; i++) {
			float[][][] outputData = neuralNetwork.process(dataSet[i].data);
			
			String outputPath = path + "output/";
			
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
			FileUtil.dataToImage(outputPath, layer, i);
		}
	}
}
