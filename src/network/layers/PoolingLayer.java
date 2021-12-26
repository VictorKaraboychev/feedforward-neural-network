package network.layers;

import network.NetworkConfiguration;
import utilities.NetworkUtil;
import utilities.TrainingData;
import utilities.Vector3;

public class PoolingLayer extends Layer {
	int reduction;
	
	public PoolingLayer(Vector3 prevLayerConfig, int dimensionalReduction) {
		this.reduction = dimensionalReduction;
		
		super.neurons = new Neuron[prevLayerConfig.x / this.reduction][prevLayerConfig.y / this.reduction][prevLayerConfig.z];	
		super.neuronConfig = new Vector3(prevLayerConfig.x / this.reduction, prevLayerConfig.y / this.reduction, prevLayerConfig.z);
		super.type = NetworkConfiguration.POOLING;
		
		for (int i = 0; i < super.neuronConfig.x; i++) {
			for (int j = 0; j < super.neuronConfig.y; j++) {
				for (int k = 0; k < super.neuronConfig.z; k++) {
					super.neurons[i][j][k] = new Neuron(0);
				}
			}
		}
	}
	
	@Override
	public void forward(Layer prevLayer) {
		//For all neurons
		for (int i = 0; i < super.neuronConfig.x; i++) {
			for (int j = 0; j < super.neuronConfig.y; j++) {
				for (int k = 0; k < super.neuronConfig.z; k++) {
					float[][][] values = new float[this.reduction][this.reduction][1];
					
					int prevX = (i * this.reduction);
					int prevY = (j * this.reduction);
					
					//For previous neurons
					for (int l = 0; l < this.reduction; l++) {
						for (int m = 0; m < this.reduction; m++) {
							values[l][m][0] = prevLayer.neurons[prevX+l][prevY+m][k].value;
						}
					}
					//Update neuron value
					this.neurons[i][j][k].value = NetworkUtil.getMaxValue(values);
				}
			}
		}
	}

	@Override
	public void backward(Layer nextLayer, Layer prevLayer, TrainingData data, float learningRate) {
		//For all neurons
		for (int i = 0; i < super.neuronConfig.x; i++) {
			for (int j = 0; j < super.neuronConfig.y; j++) {
				for (int k = 0; k < super.neuronConfig.z; k++) {
					float gradient = super.sumGradient(new Vector3(i, j, k), nextLayer);
					super.neurons[i][j][k].gradient = gradient;
							
					float[][][] values = new float[this.reduction][this.reduction][1];
							
					int prevX = (i * this.reduction);
					int prevY = (j * this.reduction);
							
					//For previous neurons
					for (int l = 0; l < this.reduction; l++) {
						for (int m = 0; m < this.reduction; m++) {
							values[l][m][0] = prevLayer.neurons[prevX+l][prevY+m][k].value;
							prevLayer.neurons[prevX+l][prevY+m][k].gradient = 0;
						}
					}
				Vector3 maxIndex = NetworkUtil.getMaxIndex(values);
				prevLayer.neurons[prevX+maxIndex.x][prevY+maxIndex.y][k].gradient = gradient;
				}
			}
		}
	}
	

}
