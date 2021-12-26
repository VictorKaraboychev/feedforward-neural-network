package utilities;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

public class FileUtil {
	public static TrainingData[] getImageData(String path, boolean color) {
		int directories = getFileCount(path);
		int totalImages = 0;
		
		//count total files in all directories
		for (int i = 0; i < directories; i++) {
			String subPath = path + i + "/";		
			totalImages += getFileCount(subPath);
		}
		TrainingData[] data = new TrainingData[totalImages];
		
		int index = 0;

		// for all directories
		for (int i = 0; i < directories; i++) {
			String sub = path + i + "/";
			int imagesInCat = getFileCount(sub);

			// add each image in the directory to the training data array
			for (int j = 0; j < imagesInCat; j++) {
				File image = new File(sub + "image" + j + ".png");
				
				// SWITCH THESE TO CHANGE THE FLOW DIRECTION OF THE NETWORK
				float[][][] dataInput = numberToData(i);
				float[][][] dataOutput = imageToData(image, color);

				data[index] = new TrainingData(dataInput, dataOutput);
				
				index++;
			}
		}
		
		return data;
	}
	
	private static int getFileCount(String path) {
		int count = 0;

		try (Stream<Path> files = Files.list(Paths.get(path))) {
		   count = (int) files.count();
		} catch (IOException e) {
			System.out.println("[ERROR] Failed to read path " + path);
		}
		
		return count;
	}
	
	public static float[][][] imageToData(File file, boolean color) {
		BufferedImage image = null;

		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			System.out.println("[ERROR] Failed image read");
		}
		
		Point dimensions = new Point(image.getWidth(), image.getHeight());
		float[][][] values;
		
		if (color) {
			values = new float[dimensions.x][dimensions.y][3];
		} else {
			values = new float[dimensions.x][dimensions.y][1];
		}
		
		for (int y = 0; y < dimensions.y; y++) {
			for (int x = 0; x < dimensions.x; x++) {
				Color pixel = new Color(image.getRGB(x, y));
				
				values[x][y][0] = pixel.getRed() / 255.0f;
				if (color) {
					values[x][y][1] = pixel.getGreen() / 255.0f;
					values[x][y][2] = pixel.getBlue() / 255.0f;
				}
			}
		}
		return values;
	}
	
	public static void dataToImage(String path, float[][][] data, int imageNum) {
		BufferedImage image = new BufferedImage(data.length, data[0].length, BufferedImage.TYPE_INT_RGB);
		
		Point dimensions = new Point(image.getWidth(), image.getHeight());
		
		for (int y = 0; y < dimensions.y; y++) {
			for (int x = 0; x < dimensions.x; x++) {
				int red = Math.max(Math.min(Math.round(data[x][y][0] * 255.0f), 225), 0);
				int green = red;
				int blue = red;
				
				if (data[0][0].length == 3) {
					green = Math.round(data[x][y][1] * 255.0f);
					blue = Math.round(data[x][y][2] * 255.0f);
				}
				
				Color pixel = new Color(red, green, blue);
				image.setRGB(x, y, pixel.getRGB());
			}
		}
		
		try {
			//src/neuralNetwork/TrainingImages/0/output.png
	    	File out = new File(path + "output" + imageNum + ".png");
		    ImageIO.write(image, "png", out);
		} catch(IOException e) {
		    System.out.println("[ERROR] Failed image write");
		}
	}
	
	public static float[][][] numberToData(int number) {	
		float[][][] values = new float[10][1][1];

		for (int i = 0; i < values.length; i++) {
			values[1][0][0] = 0;
		}
		values[number][0][0] = 1;
		return values;
	}
}
