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
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import model.WiSARD;

public class Main {

	public static long startTime;
	public static long endTime;
	
	public static void main(String[] args) {
		
		startTime = System.nanoTime();
		
		WiSARD w1 = new WiSARD("w1", 28,28,28);
		
		//splitFiles();
		train(w1, 60000, "Input/MNIST/Original/training.csv");
		
		//System.out.println(w1.toString());
		
		//w1.mentalImage("0");
		
		test(w1, 10000, "Input/MNIST/Original/testing.csv");
		
		endTime = System.nanoTime();
		
		System.out.println("-- Execution time: " + duration() + "ms --");
	}
	
	public static void train(WiSARD w1, int trainingSize, String path) {
		
		System.out.println("-- Initializing training phase --");
		
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
		
		System.out.println("-- Training phase finished successfully --");
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
		
		System.out.println("-- Testing phase finished successfully --");
	}
	
	public static void splitFiles() {
		
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
			
			f = new File("Input/MNIST/Divided/" + element.getKey() + ".txt");
			
			FileWriter fw;
			
			try {
				
				fw = new FileWriter(f);
				BufferedWriter bw = new BufferedWriter(fw);
				
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
