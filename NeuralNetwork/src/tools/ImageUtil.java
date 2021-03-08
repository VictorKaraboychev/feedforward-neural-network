package tools;

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

import network.TrainingData;

public class ImageUtil {

	
	
	public static TrainingData[] getImageData(String path, boolean color) {
		int catagories = getFileCount(path);
		int totalImages = 0;
		
		for (int i = 0; i < catagories; i++) {
			String subPath = path + i + "/";		
			totalImages += getFileCount(subPath);
		}
		TrainingData[] data = new TrainingData[totalImages];
		
		int index = 0;
		for (int i = 0; i < catagories; i++) {
			String sub = path + i + "/";
			int imagesInCat = getFileCount(sub);
			for (int j = 0; j < imagesInCat; j++) {
				File image = new File(sub + "image" + j + ".png");
				
				float[][][] dataInput = imageToData(image, color);
				float[][][] dataOutput = numToOutput(i);
				
				//CHANGED to invert output
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
		} catch (IOException e) {}
		
		return count;
	}
	
	public static float[][][] imageToData(File file, boolean color) {
		BufferedImage img = null;

		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			System.out.println("[!] Falied read");
		}	
		
		Point dimensions = new Point(img.getWidth(), img.getHeight());
		float[][][] values;
		
		if (color) {
			values = new float[dimensions.x][dimensions.y][3];
		} else {
			values = new float[dimensions.x][dimensions.y][1];
		}
		
		for (int y = 0; y < dimensions.y; y++) {
			for (int x = 0; x < dimensions.x; x++) {
				Color imgPixel = new Color(img.getRGB(x, y));
				
				values[x][y][0] = imgPixel.getRed() / 255.0f;
				if (color) {
					values[x][y][1] = imgPixel.getGreen() / 255.0f;
					values[x][y][2] = imgPixel.getBlue() / 255.0f;
				}
			}
		}
		return values;
	}
	
	public static void dataToImage(String path, float[][][] data, int imgNum) {
		BufferedImage img = new BufferedImage(data.length, data[0].length, BufferedImage.TYPE_INT_RGB);
		
		Point dimensions = new Point(img.getWidth(), img.getHeight());
		
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
				img.setRGB(x, y, pixel.getRGB());
			}
		}
		
		try {
			//src/neuralNetwork/TrainingImages/0/output.png
	    	File out = new File(path + "output" + imgNum + ".png");
		    ImageIO.write(img, "png", out);
		} catch(IOException e) {
		    System.out.println("[!] Falied write");
		}
	}
	
	public static float[][][] numToOutput(int num) {	
		float[][][] targetValue = new float[10][1][1];

		for (int i = 0; i < targetValue.length; i++) {
			targetValue[1][0][0] = 0;
		}
		targetValue[num][0][0] = 1;
		return targetValue;
	}
	
	
}
