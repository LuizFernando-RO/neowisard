package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import model.WiSARD;

public class MNIST {

	public static long startTime;
	public static long endTime;
	
	public static void main(String[] args) {
		
		startTime = System.nanoTime();
		
		WiSARD w1 = new WiSARD("w1", 28,28,28);
		WiSARD w2 = new WiSARD("w2", 28,28,28);
		
		//autoTraining(w1, w2);
		//randomTraining(w1, w2);
		
		splitTotalSplittedFiles();
		balancedTraining(w1, w2);
		
		test(w1, 10000, "Input/MNIST/Original/testing.csv");
		test(w2, 10000, "Input/MNIST/Original/testing.csv");
		
		w1.mentalImage("3");
		
		w2.mentalImage("3");
		
		endTime = System.nanoTime();
		
		System.out.println("\n-- Execution time: " + duration() + "ms --");
	}
	
	public static void train(WiSARD w1, int trainingSize, String path) {
		
		System.out.println("-- Initializing training phase --\n");
		
		int m = 28;
		
		StringBuilder example;
		String[] splitter;
		String label;
		
		File f;
		FileReader fr;
		BufferedReader br;

		f = new File(path);
		
		try {
			
			fr = new FileReader(f);
			br = new BufferedReader(fr);
		
			for(int i = 0; i < trainingSize; i++) {
			
				example = new StringBuilder();
				
				splitter = br.readLine().trim().split(",");
	
				label = splitter[0];
				
				for(int j = 1; j < m*m + 1; j++) {
					
					if(Integer.valueOf(splitter[j]) > 0)
						
						example.append("1");
					
					else
						
						example.append("0");
				}
				
				w1.training(label, example.toString());
			}
			
			br.close();
			
		} catch (FileNotFoundException e) {
			
			System.out.println("File not found!");
			
		} catch (IOException e) {
			
			System.out.println("Error!");
		}
		
		System.out.println("\n-- Training phase finished successfully --");
	}
	
	public static void test(WiSARD w1, int testingSize, String path) {
		
		System.out.println("-- Initializing testing phase --");
		
		int m = 28;
		
		StringBuilder example;
		String[] splitter;
		String label, result = "";
		int counter = 0;
		
		File f;
		FileReader fr;
		BufferedReader br;
		
		f = new File(path);
		
		try {
			
			fr = new FileReader(f);
			br = new BufferedReader(fr);
		
			for(int i = 0; i < testingSize; i++) {
			
				example = new StringBuilder();
				
				splitter = br.readLine().trim().split(",");
	
				label = splitter[0];
				
				for(int j = 1; j < m*m + 1; j++) {
					
					if(Integer.valueOf(splitter[j]) > 0)
						
						example.append("1");
					
					else
						
						example.append("0");
				}
				
				result = w1.testing(example.toString());
				
				if(result.equals(label))
					
					counter++;
			}
			
			br.close();
			
		} catch (FileNotFoundException e) {
			
			System.out.println("File not found!");
			
		} catch (IOException e) {
			
			System.out.println("Error!");
		}
		
		System.out.println(counter + " / " + testingSize + " : " + ((float) counter / testingSize) + "%");
		
		System.out.println("\n-- Testing phase finished successfully --");
	}
	
	public static void autoTraining(WiSARD w1, WiSARD w2) {
		
		train(w1, 30000, "Input/MNIST/Auto Split/train1.txt");
		train(w2, 30000, "Input/MNIST/Auto Split/train2.txt");
	}
	
	public static void balancedTraining(WiSARD w1, WiSARD w2) {
		
		train(w1, 5923/2, "Input/MNIST/Balanced/01.txt");
		train(w2, 5923/2 + 1, "Input/MNIST/Balanced/02.txt");
		
		train(w1, 6742/2, "Input/MNIST/Balanced/11.txt");
		train(w2, 6742/2, "Input/MNIST/Balanced/12.txt");
		
		train(w1, 5958/2, "Input/MNIST/Balanced/21.txt");
		train(w2, 5958/2, "Input/MNIST/Balanced/22.txt");
		
		train(w1, 6131/2, "Input/MNIST/Balanced/31.txt");
		train(w2, 6131/2 + 1, "Input/MNIST/Balanced/32.txt");
		
		train(w1, 5842/2, "Input/MNIST/Balanced/41.txt");
		train(w2, 5842/2, "Input/MNIST/Balanced/42.txt");
		
		train(w1, 5421/2, "Input/MNIST/Balanced/51.txt");
		train(w2, 5421/2 + 1, "Input/MNIST/Balanced/52.txt");
		
		train(w1, 5918/2, "Input/MNIST/Balanced/61.txt");
		train(w2, 5918/2, "Input/MNIST/Balanced/62.txt");
		
		train(w1, 6265/2, "Input/MNIST/Balanced/71.txt");
		train(w2, 6265/2 + 1, "Input/MNIST/Balanced/72.txt");
		
		train(w1, 5851/2, "Input/MNIST/Balanced/81.txt");
		train(w2, 5851/2 + 1, "Input/MNIST/Balanced/82.txt");
		
		train(w1, 5949/2, "Input/MNIST/Balanced/91.txt");
		train(w2, 5949/2 + 1, "Input/MNIST/Balanced/92.txt");
	}
	
	public static void randomTraining(WiSARD w1, WiSARD w2) {
		
		train(w1, 30000, "Input/MNIST/Random Split/train1.txt");
		train(w2, 30000, "Input/MNIST/Random Split/train2.txt");
	}
	
	// This method divide the dataset into 2 files
	public static void autoSplitFiles(String pathOrigin, String pathDestiny1, String pathDestiny2, int trainingSize) {
		
		File f = new File(pathOrigin);
		File f2 = new File(pathDestiny1);
		
		FileReader fr;
		FileWriter fw;
		
		try {
			
			fr = new FileReader(f);
			fw = new FileWriter(f2);
			
			BufferedReader br = new BufferedReader(fr);
			BufferedWriter bw = new BufferedWriter(fw);
			
			for(int i = 0; i < trainingSize / 2; i++) 
			
				bw.write(br.readLine().trim() + "\n");
			
			bw.close();
			
			f2 = new File(pathDestiny2);
			
			fw = new FileWriter(f2);
			bw = new BufferedWriter(fw);
			
			for(int i = trainingSize / 2; i < trainingSize; i++) 
				
				bw.write(br.readLine().trim() + "\n");
			
			bw.close();
			br.close();
			
		} catch (FileNotFoundException e) {
			
			System.out.println("File not found!");
		} catch (IOException e) {
			
			System.out.println("Error!");
		}
	}
	
	// This method divide the dataset into 2 files with random samples
	public static void randomSplitFiles(String pathOrigin, String pathDestiny1, String pathDestiny2, int trainingSize) {
				
		ArrayList<String> arr = new ArrayList<String>();
		
		int[] array = new int[trainingSize];
		int posicao, aux;
		
		Random rand = new Random();

		for (int i = 0; i < array.length; i++) {
			
			array[i] = i;	
		}
		
		for (int i = 0; i < array.length; i++) {
			
			posicao = rand.nextInt(trainingSize);
			
			aux = array[i];
			array[i] = array[posicao];
			array[posicao] = aux;
		}
		
		File f = new File(pathOrigin);
		
		FileReader fr;
		BufferedReader br;
		
		try {
			
			fr = new FileReader(f);
			br = new BufferedReader(fr);
			
			for(int i = 0; i < trainingSize; i++)
				
				arr.add(br.readLine().trim());
			
			br.close();
			
		} catch (FileNotFoundException e) {
			
			System.out.println("File not found!");
		} catch (IOException e) {
			
			System.out.println("Error!");
		}
	
		File f2 = new File(pathDestiny1);
		
		FileWriter fw;
		BufferedWriter bw;
		
		try {
			
			fw = new FileWriter(f2);
			bw = new BufferedWriter(fw);
			
			for(int i = 0; i < trainingSize / 2; i++)
				
				bw.write(arr.get(array[i]) + "\n");
			
			bw.close();
			
			f2 = new File(pathDestiny2);

			fw = new FileWriter(f2);
			bw = new BufferedWriter(fw);
			
			for(int i = trainingSize / 2; i < trainingSize; i++)
				
				bw.write(arr.get(array[i]) + "\n");
			
			bw.close();

			
		} catch (FileNotFoundException e) {
			
			System.out.println("File not found!");
		} catch (IOException e) {
			
			System.out.println("Error!");
		}
	}
	
	public static void splitTotalSplittedFiles() {
		
		randomSplitFiles("Input/MNIST/Total Split/0.txt", "Input/MNIST/Balanced/01.txt", "Input/MNIST/Balanced/02.txt", 5923);
		randomSplitFiles("Input/MNIST/Total Split/1.txt", "Input/MNIST/Balanced/11.txt", "Input/MNIST/Balanced/12.txt", 6742);
		randomSplitFiles("Input/MNIST/Total Split/2.txt", "Input/MNIST/Balanced/21.txt", "Input/MNIST/Balanced/22.txt", 5958);
		randomSplitFiles("Input/MNIST/Total Split/3.txt", "Input/MNIST/Balanced/31.txt", "Input/MNIST/Balanced/32.txt", 6131);
		randomSplitFiles("Input/MNIST/Total Split/4.txt", "Input/MNIST/Balanced/41.txt", "Input/MNIST/Balanced/42.txt", 5842);
		randomSplitFiles("Input/MNIST/Total Split/5.txt", "Input/MNIST/Balanced/51.txt", "Input/MNIST/Balanced/52.txt", 5421);
		randomSplitFiles("Input/MNIST/Total Split/6.txt", "Input/MNIST/Balanced/61.txt", "Input/MNIST/Balanced/62.txt", 5918);
		randomSplitFiles("Input/MNIST/Total Split/7.txt", "Input/MNIST/Balanced/71.txt", "Input/MNIST/Balanced/72.txt", 6265);
		randomSplitFiles("Input/MNIST/Total Split/8.txt", "Input/MNIST/Balanced/81.txt", "Input/MNIST/Balanced/82.txt", 5851);
		randomSplitFiles("Input/MNIST/Total Split/9.txt", "Input/MNIST/Balanced/91.txt", "Input/MNIST/Balanced/92.txt", 5949);
	}
	
	// This method divide the dataset into 10 files, according to the sample label
	public static void totalSplitFiles() {
		
		int trainingSize = 60000;
		
		String example, label;
		String[] splitter;
		
		HashMap<String, ArrayList<String>> classes = new HashMap<String, ArrayList<String>>();
		ArrayList<String> arr;
		
		File f = new File("Input/MNIST/Original/training.csv");
		
		FileReader fr;
		
		try {
			
			fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			
			for(int i = 0; i < trainingSize; i++) {
			
				example = br.readLine().trim();
				
				splitter = example.split(",");
	
				label = splitter[0];
	
				if(classes.get(label) == null) {
					
					arr = new ArrayList<String>();
					
					arr.add(example);
					
					classes.put(label, arr);
				}
				
				else {
					
					classes.get(label).add(example);
				}
			}
			
			br.close();
			
		} catch (FileNotFoundException e) {
			
			System.out.println("File not found!");
		} catch (IOException e) {
			
			System.out.println("Error!");
		}
				
		Iterator<Entry<String, ArrayList<String>>> iterator = classes.entrySet().iterator();
		
		while(iterator.hasNext()) {
			
			Entry<String, ArrayList<String>> element = iterator.next();
			
			f = new File("Input/MNIST/Total Split/" + element.getKey() + ".txt");
			
			FileWriter fw;
			BufferedWriter bw;
			
			try {
				
				if(!f.exists())
					
					f.createNewFile();
				
				fw = new FileWriter(f);
				bw = new BufferedWriter(fw);
				
				for(int i = 0; i < element.getValue().size(); i++) {
					
					fw.write(element.getValue().get(i) + "\n");
				}
				
				bw.close();
				
			} catch (FileNotFoundException e) {
				
				System.out.println("File not found!");
			} catch (IOException e) {
				
				System.out.println("Error!");
			}
		}
	}
	
	public static long duration() {
		
		return  TimeUnit.NANOSECONDS.toMillis(endTime) - TimeUnit.NANOSECONDS.toMillis(startTime);
	}
}
