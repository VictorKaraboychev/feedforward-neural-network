package utilities;

public class TrainingData {

	public float[][][] data;
	public float[][][] expectedOutput;
	
	public TrainingData(float[][][] data, float[][][] expectedOutput) {
		this.data = data;
		this.expectedOutput = expectedOutput;
	}
	
	public TrainingData(float[][] data, float[][] expectedOutput) {
		this.data = new float[1][data.length][data[0].length];
		this.expectedOutput = new float[1][expectedOutput.length][expectedOutput[0].length];
		
		this.data[0] = data;
		this.expectedOutput[0] = expectedOutput;
	}
	
	public TrainingData(float[] data, float[] expectedOutput) {
		this.data = new float[1][1][data.length];
		this.expectedOutput = new float[1][1][expectedOutput.length];
		
		this.data[0][0] = data;
		this.expectedOutput[0][0] = expectedOutput;
	}
}
