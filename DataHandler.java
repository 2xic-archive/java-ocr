package com;

import java.io.FileWriter;  
import java.io.FileReader;  
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Collections;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public class DataHandler{	
	public static double[][] X;
	public static double[][] y;
	public static int max;

	DataHandler(int max){
		this.max = max;
	}

	DataHandler(){
		this.max = -1;
	}
		
	/**
	 * Reads an image.
	 *
	 * @param      file  The file
	 *
	 * @return     single row with 49 bits representing the image
	 */
	public static double[] loadImage(File file) {
		try {
			BufferedImage image = ImageIO.read(file);
			if (image.getWidth() == 7 && image.getHeight() == 7) {
				double[] flattenPixels = new double[image.getWidth() * image.getHeight()];
				int index = 0;
				for (int y = 0; y < image.getHeight(); y++) {
					for (int x = 0; x < image.getWidth(); x++) {
						int  colour = image.getRGB(x, y);
						//	r,g,b will share the same value because of our color chosie.
						int red = (colour >> 16) & 0xff;
						if(125 < red){
							flattenPixels[index++] = 1;
						}else{
							flattenPixels[index++] = 0;
						}
					}
				}
				return flattenPixels;
			}else{
				throw new IllegalArgumentException("image has to be 7x7");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * Search for a file pattern in a folder
	 *
	 * @param      pattern  The pattern
	 * @param      folder   The folder
	 * @param      result   The result
	 */
	public static void searchFiles(final String pattern, final File folder, List<String> result) {
		int count = 0;
		for (final File file : folder.listFiles()) {
			if (file.isFile()) {
				if (file.getName().matches(pattern)) {
					result.add(file.getAbsolutePath());
				}
			}
			if(count == max){
				break;
			}
			count++;
		}
	}


	/**
	 * Parse the dataset
	 *
	 * @param      folderPath  The folder path with the data
	 */
	public static void makeDataSet(String folderPath){
		final File folder = new File(folderPath);
			
		if(folder.exists()) {
			List<String> result = new ArrayList<>();

			searchFiles(".*\\.jpg", folder, result);

			X =  new double[result.size()][7];
			y =  new double[result.size()][10];

			int index = 0;
			for (String s : result) {
				int expectedNumber = s.replace(folderPath, "").charAt(0) - '0';
				
				X[index] = loadImage(new File(s));
				y[index][expectedNumber] = 1;
				index++;
			}	
		}else{
			System.out.println("folder does not exsist!!!");
		}
	}

	/**
	 * Return the feature for the dataset
	 */
	public static double[][] getX(){
		return X;
	}

	/**
	 * Return the label for the dataset
	 */
	public static double[][] getY(){
		return y;
	}


}


