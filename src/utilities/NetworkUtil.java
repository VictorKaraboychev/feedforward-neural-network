package utilities;

public class NetworkUtil {

	public static float randomRange(float min, float max) {
		return (float) (Math.random() * (max - min) + min);
	}
	
	// flattens a 3D matrix into a 1D array
	public static float[] flattenArray(float[][][] input) {
		int outputLength = input.length * input[0].length * input[0][0].length;	
		float[] output = new float[outputLength];
		
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[0].length; j++) {
				for (int k = 0; k < input[0][0].length; k++) {
					int index = i * (input[0].length * input[0][0].length) + j * (input[0][0].length) + k;
					output[index] = input[i][j][k];
				}
			}
		}
		return output;
	}
	
	public static float avgSqrError(float value, float target) {
		return (float) (Math.pow(target - value, 2) / 2);
	}
	
	public static float sumSqrError(float[] values, float[] targets) {
		float sum = 0;
		for (int i = 0; i < values.length; i++) {
			sum += avgSqrError(values[i], targets[i]);
		}
		return sum;
	}
	
	public static float[][][] randomArray(Vector3 size) {
		float[][][] random = new float[size.x][size.y][size.z];
				
		for (int i = 0; i < size.x; i++) {
			for (int j = 0; j < size.y; j++) {
				for (int k = 0; k < size.z; k++) {
					random[i][j][k] = (float) Math.random();
				}
			}
		}
		return random;
	}
	
	public static Vector3 getMaxIndex(float[][][] values) {
		float maxValue = 0;
		Vector3 maxIndex = new Vector3();
		
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[0].length; j++) {
				for (int k = 0; k < values[0][0].length; k++) {
					if (values[i][j][k] > maxValue) {
						maxValue = values[i][j][k];
						maxIndex.set(i, j, k);
					}
				}
			}
		}
		return maxIndex;
	}
	
	public static float getMaxValue(float[][][] values) {
		Vector3 index = getMaxIndex(values);
		return values[index.x][index.y][index.z];
	}
}
