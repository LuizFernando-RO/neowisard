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

public class Main {

	public static long startTime;
	public static long endTime;
	
	public static void main(String[] args) {
		
		startTime = System.nanoTime();
		
		WiSARD w1 = new WiSARD("w1", 28,28,28);
		WiSARD w2 = new WiSARD("w1", 28,28,28);
		
		totalSplitFiles();
		
		//splitFiles();
		
		//train(w1, 60000, "Input/MNIST/Original/training.csv");
		
		//System.out.println(w1.toString());
		
		//test(w1, 10000, "Input/MNIST/Original/testing.csv");
		
		//w1.mentalImage("5");
		
		//randomSplitFiles();
		
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
	
	// This method divide the dataset into 2 files
	public static void autoSplitFiles() {
		
		int trainingSize = 60000;
		
		File f = new File("Input/MNIST/Original/training.csv");
		File f2 = new File("Input/MNIST/Auto Split/train1.txt");
		
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
			
			f2 = new File("Input/MNIST/Auto Split/train2.txt");
			
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
	public static void randomSplitFiles() {
		
		int trainingSize = 60000;
		
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
		
		File f = new File("Input/MNIST/Original/training.csv");
		
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
	
		File f2 = new File("Input/MNIST/Random Split/train1.txt");
		
		FileWriter fw;
		BufferedWriter bw;
		
		try {
			
			fw = new FileWriter(f2);
			bw = new BufferedWriter(fw);
			
			for(int i = 0; i < trainingSize / 2; i++)
				
				bw.write(arr.get(array[i]) + "\n");
			
			bw.close();
			
			f2 = new File("Input/MNIST/Random Split/train2.txt");

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
