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

import model.Discriminator;
import model.WiSARD;

public class Main {

	public static void main(String[] args) {
		
		splitFiles();
	}
	
	public static void brainStorm() {
		
		int testingSize = 10000;
		int trainingSize = 60000;
		int m = 28;
		
		WiSARD w1 = new WiSARD("w1", 28,28,28);
		
		System.out.println(w1.toString());
		
		WiSARD w2 = new WiSARD("w2", 28,28,28);
		
		System.out.println(w2.toString());
		
		StringBuilder example;
		String[] splitter;
		String label;
		
		File f = new File("Input/MNIST/training.csv");
		
		FileReader fr;
		
		try {
			fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			
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
			
			br.close();
			
		} catch (FileNotFoundException e) {
			
			System.out.println("File not found!");
		} catch (IOException e) {
			
			System.out.println("Error!");
		}
		
		System.out.println(w1.toString());
		
		System.out.println(w2.toString());
	}
	
	public static void splitFiles() {
		
		int testingSize = 10000;
		int trainingSize = 60000;
		int m = 28;
		
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
		
		// - 
		
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
}