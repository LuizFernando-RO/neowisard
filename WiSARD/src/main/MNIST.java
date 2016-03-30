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

import model.Discriminator;
import model.WiSARD;

public class MNIST {

	public static long startTime;
	public static long endTime;
	
	public static int index = 0;
	public static ArrayList<String> combinations = new ArrayList<String>();
	
	public static void main(String[] args) {
		
		startTime = System.nanoTime();
		
		//test();
		
		//two();
		
		//three();
		
		crossZero(1,10);
		
		//crossOne(1,10);
		
		//crossTwo(1,10);
		
		//crossThree(1,10);
		
		endTime = System.nanoTime();
		
		System.out.println("\n-- Execution time: " + duration() + "ms --");
	}
	
	public static void test() {
		
		WiSARD w0 = new WiSARD("w0", 28, 28, 28);
		WiSARD w1 = new WiSARD("w1", 28, 28, 28);
		WiSARD w01 = new WiSARD("w01", 28, 28, 28);
		
		String[] labels = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		
		for (int k = 0; k < labels.length; k++) {
			
			Discriminator discriminator = new Discriminator(28, 28);
			discriminator.setId(labels[k]);
			
			Discriminator discriminator1 = new Discriminator(28, 28);
			discriminator1.setId(labels[k]);
			
			Discriminator discriminator2 = new Discriminator(28, 28);
			discriminator2.setId(labels[k]);
			
			discriminator1.setTuplas(discriminator.getTuplas());
			discriminator2.setTuplas(discriminator.getTuplas());
			
			w0.getMap().put(labels[k], discriminator);
			w0.getRel1().put(w0.getRel1().size(), labels[k]);
			w0.getRel2().put(labels[k], w0.getRel2().size());
			
			w1.getMap().put(labels[k], discriminator1);
			w1.getRel1().put(w1.getRel1().size(), labels[k]);
			w1.getRel2().put(labels[k], w1.getRel2().size());
			
			w01.getMap().put(labels[k], discriminator2);
			w01.getRel1().put(w01.getRel1().size(), labels[k]);
			w01.getRel2().put(labels[k], w01.getRel2().size());
		}
		
		System.out.println(w0.getMap().get("0").toString());
		System.out.println(w1.getMap().get("0").toString());
		System.out.println(w01.getMap().get("0").toString());
		
		System.out.println(w0.getMap().size());
		System.out.println(w1.getMap().size());
		System.out.println(w01.getMap().size());
		
	}
	
	public static void one() {
		
		WiSARD w1 = new WiSARD("w1", 28,28,28);
		
		train(w1, 60000, "Input/MNIST/Original/training.csv");
		test(w1, 10000, "Input/MNIST/Original/testing.csv");
		w1.generateMentalImages();
		w1.syntheticTrainingSet();
		
		int[][] teste = w1.getSyntheticTrainingSet().get("0");
		
		for(int j = 0; j < teste[0].length; j++) {
			System.out.print(teste[0][j]);
		}
		
		System.out.println();
		
		for(int j = 0; j < teste[0].length; j++) {
			System.out.print(teste[1][j]);
		}
		
		/*
		w1.mentalImage("0");
		w1.mentalImage("1");
		w1.mentalImage("2");
		w1.mentalImage("3");
		w1.mentalImage("4");
		w1.mentalImage("5");
		w1.mentalImage("6");
		w1.mentalImage("7");
		w1.mentalImage("8");
		w1.mentalImage("9");*/
	}
	
	public static void two() {
		
		int m = 28;
		
		StringBuilder example;
		String[] splitter;
		
		String path = "Input/MNIST/Original/training.csv";
		int trainingSize = 60000;
		
		File f;
		FileReader fr;
		BufferedReader br;

		//Load the training file
		
		String[] trainingSet = new String[60000];
		
		f = new File(path);
		
		try {
			
			fr = new FileReader(f);
			br = new BufferedReader(fr);
		
			for(int i = 0; i < trainingSize; i++) {
				
				example = new StringBuilder();
				
				splitter = br.readLine().trim().split(",");
				
				example.append(splitter[0]);
				
				for(int j = 1; j < m*m + 1; j++) {
					
					if(Integer.valueOf(splitter[j]) > 0)
						
						example.append(",1");
					
					else
						
						example.append(",0");
				}
				
				trainingSet[i] = example.toString();
				//trainingSet[i] = br.readLine().trim();
			}
			
			br.close();
			
		} catch (FileNotFoundException e) {
			
			System.out.println("File not found!");
			
		} catch (IOException e) {
			
			System.out.println("Error!");
		}
		
		System.out.println("Training set loaded successfully!");
		
		// Dividing into 6 blocks of 10k test samples and 50k training samples
		
		allCombinations(new StringBuilder(), 5, 5, 10);
		
		for(int i = 0; i <1 ; i++) {
			
			System.out.println("Block " + i);
			
			int testStart = i * 10000;
			int testEnd = (i+1) * 10000;
			
			for (int j = 0; j < 252; j++) {
				
				System.out.println("Environment " + j);
				
				String combination = combinations.get(j);
				
				int combinationIndex = 0;
				
				ArrayList<String> testSet = new ArrayList<String>();
				ArrayList<String> trainSet1 = new ArrayList<String>();
				ArrayList<String> trainSet2 = new ArrayList<String>();
				
				for (int k = 0; k < 60000; k++) {
					
					//Test set
					if(k >= testStart && k < testEnd) {
						
						testSet.add(trainingSet[k]);
					}
					
					// Training set
					else {
						
						// Training for first WiSARD
						if(Integer.valueOf(combination.charAt(combinationIndex) - '0') == 1) 
							
							trainSet1.add(trainingSet[k]);
						
						// Training for second WiSARD
						else
							
							trainSet2.add(trainingSet[k]);
						
						if( k % 5000 == 0 && k != testEnd && k > 0) {
							
							combinationIndex++;
						}		
					}
				}
				
				f = new File("Input/MNIST/CrossValidation/Block" + i + "/env" + j + ".txt");
				FileWriter fw;
				BufferedWriter bw;
				
				try {
					
					fw = new FileWriter(f);
					
					bw = new BufferedWriter(fw);
					
					//bw.write("w1\n");
					
					for(int k = 0; k < trainSet1.size(); k++) 
					
						bw.write(trainSet1.get(k) + "\n");
					
					//bw.write("w2\n");
					
					for(int k = 0; k < trainSet2.size(); k++) 
						
						bw.write(trainSet2.get(k) + "\n");
					
					//bw.write("test\n");
					
					for(int k = 0; k < testSet.size(); k++) 
						
						bw.write(testSet.get(k) + "\n");
					
					bw.close();
					
				} catch (FileNotFoundException e) {
					
					System.out.println("File not found!");
				} catch (IOException e) {
					
					System.out.println("Error!");
				}
			}			
		}
	}
	
	public static void three() {
		
		System.out.println("-- Different Mapping --\n");
		
		File f1, f2, f3;
		FileReader fr1;
		BufferedReader br1;
		
		FileWriter fw1;
		BufferedWriter bw1;
		
		StringBuilder example;
		String[] splitter;
		
		String label;
		
		int c0, c1, c01;
		
		System.out.println("-- Initializing experiments --\n");
		
		WiSARD w0 = new WiSARD("w0", 28, 28, 28);
		WiSARD w1 = new WiSARD("w1", 28, 28, 28);
		WiSARD w01 = new WiSARD("w01", 28, 28, 28);
		
		for(int i = 0; i < 1; i++) {
			
			System.out.println("Block " + i);
			
			f1 = new File("Input/MNIST/CrossValidation/Results/Block"+i+"Accuracy.txt");
			
			for (int j = 0; j < 252; j++) {
				
				w0.clear();
				w1.clear();
				w01.clear();
				
				System.out.println("Environment " + j);
				
				f2 = new File("Input/MNIST/CrossValidation/Results/Block"+i+"env"+j+"DM.txt");
				f3 = new File("Input/MNIST/CrossValidation/Block"+i+"/env"+j+".txt");
				
				try {
					
					//System.out.println("-- Training --\n");
					
					fr1 = new FileReader(f3);
					br1 = new BufferedReader(fr1);
					
					for (int k = 0; k < 25000; k++) {
						
						splitter = br1.readLine().toString().split(",");
						example = new StringBuilder();
						
						for (int l = 1; l < splitter.length; l++) {
							
							example.append(splitter[l]);
						}
						
						w0.train(splitter[0], example.toString());
						w01.train(splitter[0], example.toString());
					}
					
					for (int k = 25000; k < 50000; k++) {
					
						splitter = br1.readLine().toString().split(",");
						example = new StringBuilder();
						
						for (int l = 1; l < splitter.length; l++) {
							
							example.append(splitter[l]);
						}
						
						w1.train(splitter[0], example.toString());
						w01.train(splitter[0], example.toString());
					}

					//System.out.println("-- Testing --\n");
					
					c0 = 0;
					c1 = 0;
					c01 = 0;
					
					for (int k = 50000; k < 60000; k++) {
						
						splitter = br1.readLine().toString().split(",");
						example = new StringBuilder();
						
						for (int l = 1; l < splitter.length; l++) {
							
							example.append(splitter[l]);
						}
						
						label = w0.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c0++;
						}
						
						label = w1.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c1++;
						}

						label = w01.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c01++;
						}
					}
					
					fw1 = new FileWriter(f1, true);
					bw1 = new BufferedWriter(fw1);
					
					bw1.write(j+","+ String.valueOf( (double) c0 / 10000 ) + "," + String.valueOf( (double) c1 / 10000 ) + "," + String.valueOf( (double) c01 / 10000 ) + "\n" );
					
					bw1.close();
					
					//System.out.println(j+","+ String.valueOf( (double) c0 / 10000 ) + "," + String.valueOf( (double) c1 / 10000 ) + "," + String.valueOf( (double) c01 / 10000 ) );
					
					//System.out.println("-- Mental Images --\n");
					
					w0.generateMentalImages();
					w1.generateMentalImages();
					w01.generateMentalImages();
					
					fw1 = new FileWriter(f2, true);
					bw1 = new BufferedWriter(fw1);
					
					StringBuilder strB;
					
					Iterator<Entry<String, int[]>> iterator = w0.getMentalImage().entrySet().iterator();
					
					while(iterator.hasNext()) {
						
						Entry<String, int[]> element = iterator.next();
						
						strB = new StringBuilder();
						
						strB.append(element.getKey() + ",");
						
						for (int l = 0; l < element.getValue().length - 1; l++) {
							
							strB.append(String.valueOf(element.getValue()[l]) + ",");
						}
						
						strB.append(String.valueOf(element.getValue()[element.getValue().length - 1]));
						
						bw1.write(strB.toString() + "\n");
					}
					
					iterator = w1.getMentalImage().entrySet().iterator();
					
					while(iterator.hasNext()) {
						
						Entry<String, int[]> element = iterator.next();
						
						strB = new StringBuilder();
						
						strB.append(element.getKey() + ",");
						
						for (int l = 0; l < element.getValue().length - 1; l++) {
							
							strB.append(String.valueOf(element.getValue()[l]) + ",");
						}
						
						strB.append(String.valueOf(element.getValue()[element.getValue().length - 1]));
						
						bw1.write(strB.toString() + "\n");
					}
					
					iterator = w01.getMentalImage().entrySet().iterator();
					
					while(iterator.hasNext()) {
						
						Entry<String, int[]> element = iterator.next();
						
						strB = new StringBuilder();
						
						strB.append(element.getKey() + ",");
						
						for (int l = 0; l < element.getValue().length - 1; l++) {
							
							strB.append(String.valueOf(element.getValue()[l]) + ",");
						}
						
						strB.append(String.valueOf(element.getValue()[element.getValue().length - 1]));
						
						bw1.write(strB.toString() + "\n");
					}
										
					bw1.close();
					
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
					
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("-- Finish --\n");
	}
	
	public static void crossZero(int blockLimit, int environmentLimit) {
		
		/* Random Mapping configuration
		 * Intra-environment: equal
		 * Inter-environment: equal
		 * */
		
		System.out.println("-- Test Reference: 0 --\n");
		
		WiSARD w0 = new WiSARD("w0", 28, 28, 28);
		WiSARD w1 = new WiSARD("w1", 28, 28, 28);
		WiSARD w01 = new WiSARD("w01", 28, 28, 28);
		
		String[] labels = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		
		for (int i = 0; i < labels.length; i++) {
			Discriminator discriminator = new Discriminator(28, 28);
			discriminator.setId(labels[i]);
			
			Discriminator discriminator1 = new Discriminator(28, 28);
			discriminator1.setId(labels[i]);
			
			Discriminator discriminator2 = new Discriminator(28, 28);
			discriminator2.setId(labels[i]);
			
			discriminator1.setTuplas(discriminator.getTuplas());
			discriminator2.setTuplas(discriminator.getTuplas());
			
			w0.getMap().put(labels[i], discriminator);
			w0.getRel1().put(w0.getRel1().size(), labels[i]);
			w0.getRel2().put(labels[i], w0.getRel2().size());
			
			w1.getMap().put(labels[i], discriminator1);
			w1.getRel1().put(w1.getRel1().size(), labels[i]);
			w1.getRel2().put(labels[i], w1.getRel2().size());
			
			w01.getMap().put(labels[i], discriminator2);
			w01.getRel1().put(w01.getRel1().size(), labels[i]);
			w01.getRel2().put(labels[i], w01.getRel2().size());
		}
		
		File f1, f2, f3;
		FileReader fr1;
		BufferedReader br1;
		
		FileWriter fw1;
		BufferedWriter bw1;
		
		StringBuilder example;
		String[] splitter;
		
		String label;
		
		int c0, c1, c01;
		
		System.out.println("-- Initializing experiments --\n");
		
		for(int i = 0; i < blockLimit; i++) {
			
			System.out.println("Block " + i);
			
			f1 = new File("Input/MNIST/CrossValidation/Results/R0/Block"+i+"AccuracyR0.txt");
			
			for (int j = 0; j < environmentLimit; j++) {
				
				w0.clear();
				w1.clear();
				w01.clear();
				
				System.out.println("Environment " + j);
				
				f2 = new File("Input/MNIST/CrossValidation/Results/R0/Block"+i+"env"+j+"R0.txt");
				f3 = new File("Input/MNIST/CrossValidation/Block"+i+"/env"+j+".txt");
				
				try {
					
					//System.out.println("-- Training --\n");
					
					fr1 = new FileReader(f3);
					br1 = new BufferedReader(fr1);
					
					for (int k = 0; k < 25000; k++) {
						
						splitter = br1.readLine().toString().split(",");
						example = new StringBuilder();
						
						for (int l = 1; l < splitter.length; l++) {
							
							example.append(splitter[l]);
						}
						
						w0.train(splitter[0], example.toString());
						w01.train(splitter[0], example.toString());
					}
					
					for (int k = 25000; k < 50000; k++) {
					
						splitter = br1.readLine().toString().split(",");
						example = new StringBuilder();
						
						for (int l = 1; l < splitter.length; l++) {
							
							example.append(splitter[l]);
						}
						
						w1.train(splitter[0], example.toString());
						w01.train(splitter[0], example.toString());
					}

					//System.out.println("-- Testing --\n");
					
					c0 = 0;
					c1 = 0;
					c01 = 0;
					
					for (int k = 50000; k < 60000; k++) {
						
						splitter = br1.readLine().toString().split(",");
						example = new StringBuilder();
						
						for (int l = 1; l < splitter.length; l++) {
							
							example.append(splitter[l]);
						}
						
						label = w0.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c0++;
						}
						
						label = w1.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c1++;
						}

						label = w01.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c01++;
						}
					}
					
					fw1 = new FileWriter(f1, true);
					bw1 = new BufferedWriter(fw1);
					
					bw1.write(j+","+ String.valueOf( (double) c0 / 10000 ) + "," + String.valueOf( (double) c1 / 10000 ) + "," + String.valueOf( (double) c01 / 10000 ) + "\n" );
					
					bw1.close();
					
					//System.out.println(j+","+ String.valueOf( (double) c0 / 10000 ) + "," + String.valueOf( (double) c1 / 10000 ) + "," + String.valueOf( (double) c01 / 10000 ) );
					
					//System.out.println("-- Mental Images --\n");
					
					w0.generateMentalImages();
					w1.generateMentalImages();
					w01.generateMentalImages();
					
					fw1 = new FileWriter(f2, true);
					bw1 = new BufferedWriter(fw1);
					
					StringBuilder strB;
					
					Iterator<Entry<String, int[]>> iterator = w0.getMentalImage().entrySet().iterator();
					
					while(iterator.hasNext()) {
						
						Entry<String, int[]> element = iterator.next();
						
						strB = new StringBuilder();
						
						strB.append(element.getKey() + ",");
						
						for (int l = 0; l < element.getValue().length - 1; l++) {
							
							strB.append(String.valueOf(element.getValue()[l]) + ",");
						}
						
						strB.append(String.valueOf(element.getValue()[element.getValue().length - 1]));
						
						bw1.write(strB.toString() + "\n");
					}
					
					iterator = w1.getMentalImage().entrySet().iterator();
					
					while(iterator.hasNext()) {
						
						Entry<String, int[]> element = iterator.next();
						
						strB = new StringBuilder();
						
						strB.append(element.getKey() + ",");
						
						for (int l = 0; l < element.getValue().length - 1; l++) {
							
							strB.append(String.valueOf(element.getValue()[l]) + ",");
						}
						
						strB.append(String.valueOf(element.getValue()[element.getValue().length - 1]));
						
						bw1.write(strB.toString() + "\n");
					}
					
					iterator = w01.getMentalImage().entrySet().iterator();
					
					while(iterator.hasNext()) {
						
						Entry<String, int[]> element = iterator.next();
						
						strB = new StringBuilder();
						
						strB.append(element.getKey() + ",");
						
						for (int l = 0; l < element.getValue().length - 1; l++) {
							
							strB.append(String.valueOf(element.getValue()[l]) + ",");
						}
						
						strB.append(String.valueOf(element.getValue()[element.getValue().length - 1]));
						
						bw1.write(strB.toString() + "\n");
					}
										
					bw1.close();
					
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
					
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("-- Finish --\n");
	}
	
	public static void crossOne(int blockLimit, int environmentLimit) {
		
		/* Random Mapping configuration
		 * Intra-environment: equal
		 * Inter-environment: different
		 * */
		
		System.out.println("-- Test Reference: 1 --\n");
		
		File f1, f2, f3;
		FileReader fr1;
		BufferedReader br1;
		
		FileWriter fw1;
		BufferedWriter bw1;
		
		StringBuilder example;
		String[] splitter;
		
		String label;
		
		int c0, c1, c01;
		
		System.out.println("-- Initializing experiments --\n");
		
		for(int i = 0; i < blockLimit; i++) {
			
			System.out.println("Block " + i);
			
			f1 = new File("Input/MNIST/CrossValidation/Results/R1/Block"+i+"AccuracyR1.txt");
			
			for (int j = 0; j < environmentLimit; j++) {
				
				WiSARD w0 = new WiSARD("w0", 28, 28, 28);
				WiSARD w1 = new WiSARD("w1", 28, 28, 28);
				WiSARD w01 = new WiSARD("w01", 28, 28, 28);
				
				String[] labels = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
				
				for (int k = 0; k < labels.length; k++) {
					Discriminator discriminator = new Discriminator(28, 28);
					discriminator.setId(labels[k]);
					
					Discriminator discriminator1 = new Discriminator(28, 28);
					discriminator1.setId(labels[k]);
					
					Discriminator discriminator2 = new Discriminator(28, 28);
					discriminator2.setId(labels[k]);
					
					discriminator1.setTuplas(discriminator.getTuplas());
					discriminator2.setTuplas(discriminator.getTuplas());
					
					w0.getMap().put(labels[k], discriminator);
					w0.getRel1().put(w0.getRel1().size(), labels[k]);
					w0.getRel2().put(labels[k], w0.getRel2().size());
					
					w1.getMap().put(labels[k], discriminator1);
					w1.getRel1().put(w1.getRel1().size(), labels[k]);
					w1.getRel2().put(labels[k], w1.getRel2().size());
					
					w01.getMap().put(labels[k], discriminator2);
					w01.getRel1().put(w01.getRel1().size(), labels[k]);
					w01.getRel2().put(labels[k], w01.getRel2().size());
				}
				
				System.out.println("Environment " + j);
				
				f2 = new File("Input/MNIST/CrossValidation/Results/R1/Block"+i+"env"+j+"R1.txt");
				f3 = new File("Input/MNIST/CrossValidation/Block"+i+"/env"+j+".txt");
				
				try {
					
					fr1 = new FileReader(f3);
					br1 = new BufferedReader(fr1);
					
					for (int k = 0; k < 25000; k++) {
						
						splitter = br1.readLine().toString().split(",");
						example = new StringBuilder();
						
						for (int l = 1; l < splitter.length; l++) {
							
							example.append(splitter[l]);
						}
						
						w0.train(splitter[0], example.toString());
						w01.train(splitter[0], example.toString());
					}
					
					for (int k = 25000; k < 50000; k++) {
					
						splitter = br1.readLine().toString().split(",");
						example = new StringBuilder();
						
						for (int l = 1; l < splitter.length; l++) {
							
							example.append(splitter[l]);
						}
						
						w1.train(splitter[0], example.toString());
						w01.train(splitter[0], example.toString());
					}
					
					c0 = 0;
					c1 = 0;
					c01 = 0;
					
					for (int k = 50000; k < 60000; k++) {
						
						splitter = br1.readLine().toString().split(",");
						example = new StringBuilder();
						
						for (int l = 1; l < splitter.length; l++) {
							
							example.append(splitter[l]);
						}
						
						label = w0.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c0++;
						}
						
						label = w1.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c1++;
						}

						label = w01.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c01++;
						}
					}
					
					fw1 = new FileWriter(f1, true);
					bw1 = new BufferedWriter(fw1);
					
					bw1.write(j+","+ String.valueOf( (double) c0 / 10000 ) + "," + String.valueOf( (double) c1 / 10000 ) + "," + String.valueOf( (double) c01 / 10000 ) + "\n" );
					
					bw1.close();
					
					//System.out.println(j+","+ String.valueOf( (double) c0 / 10000 ) + "," + String.valueOf( (double) c1 / 10000 ) + "," + String.valueOf( (double) c01 / 10000 ) );
					
					//System.out.println("-- Mental Images --\n");
					
					w0.generateMentalImages();
					w1.generateMentalImages();
					w01.generateMentalImages();
					
					fw1 = new FileWriter(f2, true);
					bw1 = new BufferedWriter(fw1);
					
					StringBuilder strB;
					
					Iterator<Entry<String, int[]>> iterator = w0.getMentalImage().entrySet().iterator();
					
					while(iterator.hasNext()) {
						
						Entry<String, int[]> element = iterator.next();
						
						strB = new StringBuilder();
						
						strB.append(element.getKey() + ",");
						
						for (int l = 0; l < element.getValue().length - 1; l++) {
							
							strB.append(String.valueOf(element.getValue()[l]) + ",");
						}
						
						strB.append(String.valueOf(element.getValue()[element.getValue().length - 1]));
						
						bw1.write(strB.toString() + "\n");
					}
					
					iterator = w1.getMentalImage().entrySet().iterator();
					
					while(iterator.hasNext()) {
						
						Entry<String, int[]> element = iterator.next();
						
						strB = new StringBuilder();
						
						strB.append(element.getKey() + ",");
						
						for (int l = 0; l < element.getValue().length - 1; l++) {
							
							strB.append(String.valueOf(element.getValue()[l]) + ",");
						}
						
						strB.append(String.valueOf(element.getValue()[element.getValue().length - 1]));
						
						bw1.write(strB.toString() + "\n");
					}
					
					iterator = w01.getMentalImage().entrySet().iterator();
					
					while(iterator.hasNext()) {
						
						Entry<String, int[]> element = iterator.next();
						
						strB = new StringBuilder();
						
						strB.append(element.getKey() + ",");
						
						for (int l = 0; l < element.getValue().length - 1; l++) {
							
							strB.append(String.valueOf(element.getValue()[l]) + ",");
						}
						
						strB.append(String.valueOf(element.getValue()[element.getValue().length - 1]));
						
						bw1.write(strB.toString() + "\n");
					}
										
					bw1.close();
					
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
					
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("-- Finish --\n");
	}
	
	public static void crossTwo(int blockLimit, int environmentLimit) {
		
		/* Random Mapping configuration
		 * Intra-environment: different
		 * Inter-environment: equal
		 * */
		
		System.out.println("-- Test Reference: 2 --\n");
		
		File f1, f2, f3;
		FileReader fr1;
		BufferedReader br1;
		
		FileWriter fw1;
		BufferedWriter bw1;
		
		StringBuilder example;
		String[] splitter;
		
		String label;
		
		int c0, c1, c01;
		
		System.out.println("-- Initializing experiments --\n");
		
		WiSARD w0 = new WiSARD("w0", 28, 28, 28);
		WiSARD w1 = new WiSARD("w1", 28, 28, 28);
		WiSARD w01 = new WiSARD("w01", 28, 28, 28);
		
		for(int i = 0; i < blockLimit; i++) {
			
			System.out.println("Block " + i);
			
			f1 = new File("Input/MNIST/CrossValidation/Results/R2/Block"+i+"AccuracyR2.txt");
			
			for (int j = 0; j < environmentLimit; j++) {
				
				w0.clear();
				w1.clear();
				w01.clear();
				
				System.out.println("Environment " + j);
				
				f2 = new File("Input/MNIST/CrossValidation/Results/R2/Block"+i+"env"+j+"R2.txt");
				f3 = new File("Input/MNIST/CrossValidation/Block"+i+"/env"+j+".txt");
				
				try {
					
					//System.out.println("-- Training --\n");
					
					fr1 = new FileReader(f3);
					br1 = new BufferedReader(fr1);
					
					for (int k = 0; k < 25000; k++) {
						
						splitter = br1.readLine().toString().split(",");
						example = new StringBuilder();
						
						for (int l = 1; l < splitter.length; l++) {
							
							example.append(splitter[l]);
						}
						
						w0.train(splitter[0], example.toString());
						w01.train(splitter[0], example.toString());
					}
					
					for (int k = 25000; k < 50000; k++) {
					
						splitter = br1.readLine().toString().split(",");
						example = new StringBuilder();
						
						for (int l = 1; l < splitter.length; l++) {
							
							example.append(splitter[l]);
						}
						
						w1.train(splitter[0], example.toString());
						w01.train(splitter[0], example.toString());
					}

					//System.out.println("-- Testing --\n");
					
					c0 = 0;
					c1 = 0;
					c01 = 0;
					
					for (int k = 50000; k < 60000; k++) {
						
						splitter = br1.readLine().toString().split(",");
						example = new StringBuilder();
						
						for (int l = 1; l < splitter.length; l++) {
							
							example.append(splitter[l]);
						}
						
						label = w0.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c0++;
						}
						
						label = w1.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c1++;
						}

						label = w01.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c01++;
						}
					}
					
					fw1 = new FileWriter(f1, true);
					bw1 = new BufferedWriter(fw1);
					
					bw1.write(j+","+ String.valueOf( (double) c0 / 10000 ) + "," + String.valueOf( (double) c1 / 10000 ) + "," + String.valueOf( (double) c01 / 10000 ) + "\n" );
					
					bw1.close();
					
					//System.out.println(j+","+ String.valueOf( (double) c0 / 10000 ) + "," + String.valueOf( (double) c1 / 10000 ) + "," + String.valueOf( (double) c01 / 10000 ) );
					
					//System.out.println("-- Mental Images --\n");
					
					w0.generateMentalImages();
					w1.generateMentalImages();
					w01.generateMentalImages();
					
					fw1 = new FileWriter(f2, true);
					bw1 = new BufferedWriter(fw1);
					
					StringBuilder strB;
					
					Iterator<Entry<String, int[]>> iterator = w0.getMentalImage().entrySet().iterator();
					
					while(iterator.hasNext()) {
						
						Entry<String, int[]> element = iterator.next();
						
						strB = new StringBuilder();
						
						strB.append(element.getKey() + ",");
						
						for (int l = 0; l < element.getValue().length - 1; l++) {
							
							strB.append(String.valueOf(element.getValue()[l]) + ",");
						}
						
						strB.append(String.valueOf(element.getValue()[element.getValue().length - 1]));
						
						bw1.write(strB.toString() + "\n");
					}
					
					iterator = w1.getMentalImage().entrySet().iterator();
					
					while(iterator.hasNext()) {
						
						Entry<String, int[]> element = iterator.next();
						
						strB = new StringBuilder();
						
						strB.append(element.getKey() + ",");
						
						for (int l = 0; l < element.getValue().length - 1; l++) {
							
							strB.append(String.valueOf(element.getValue()[l]) + ",");
						}
						
						strB.append(String.valueOf(element.getValue()[element.getValue().length - 1]));
						
						bw1.write(strB.toString() + "\n");
					}
					
					iterator = w01.getMentalImage().entrySet().iterator();
					
					while(iterator.hasNext()) {
						
						Entry<String, int[]> element = iterator.next();
						
						strB = new StringBuilder();
						
						strB.append(element.getKey() + ",");
						
						for (int l = 0; l < element.getValue().length - 1; l++) {
							
							strB.append(String.valueOf(element.getValue()[l]) + ",");
						}
						
						strB.append(String.valueOf(element.getValue()[element.getValue().length - 1]));
						
						bw1.write(strB.toString() + "\n");
					}
										
					bw1.close();
					
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
					
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("-- Finish --\n");
	}
	
	public static void crossThree(int blockLimit, int environmentLimit) {
		
		/* Random Mapping configuration
		 * Intra-environment: different
		 * Inter-environment: different
		 * */
		
		System.out.println("-- Test Reference: 3 --\n");
		
		File f1, f2, f3;
		FileReader fr1;
		BufferedReader br1;
		
		FileWriter fw1;
		BufferedWriter bw1;
		
		StringBuilder example;
		String[] splitter;
		
		String label;
		
		int c0, c1, c01;
		
		System.out.println("-- Initializing experiments --\n");
		
		for(int i = 0; i < blockLimit; i++) {
			
			System.out.println("Block " + i);
			
			f1 = new File("Input/MNIST/CrossValidation/Results/R3/Block"+i+"AccuracyR3.txt");
			
			for (int j = 0; j < environmentLimit; j++) {
				
				WiSARD w0 = new WiSARD("w0", 28, 28, 28);
				WiSARD w1 = new WiSARD("w1", 28, 28, 28);
				WiSARD w01 = new WiSARD("w01", 28, 28, 28);
				
				System.out.println("Environment " + j);
				
				f2 = new File("Input/MNIST/CrossValidation/Results/R3/Block"+i+"env"+j+"R3.txt");
				f3 = new File("Input/MNIST/CrossValidation/Block"+i+"/env"+j+".txt");
				
				try {
					
					//System.out.println("-- Training --\n");
					
					fr1 = new FileReader(f3);
					br1 = new BufferedReader(fr1);
					
					for (int k = 0; k < 25000; k++) {
						
						splitter = br1.readLine().toString().split(",");
						example = new StringBuilder();
						
						for (int l = 1; l < splitter.length; l++) {
							
							example.append(splitter[l]);
						}
						
						w0.train(splitter[0], example.toString());
						w01.train(splitter[0], example.toString());
					}
					
					for (int k = 25000; k < 50000; k++) {
					
						splitter = br1.readLine().toString().split(",");
						example = new StringBuilder();
						
						for (int l = 1; l < splitter.length; l++) {
							
							example.append(splitter[l]);
						}
						
						w1.train(splitter[0], example.toString());
						w01.train(splitter[0], example.toString());
					}

					//System.out.println("-- Testing --\n");
					
					c0 = 0;
					c1 = 0;
					c01 = 0;
					
					for (int k = 50000; k < 60000; k++) {
						
						splitter = br1.readLine().toString().split(",");
						example = new StringBuilder();
						
						for (int l = 1; l < splitter.length; l++) {
							
							example.append(splitter[l]);
						}
						
						label = w0.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c0++;
						}
						
						label = w1.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c1++;
						}

						label = w01.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c01++;
						}
					}
					
					fw1 = new FileWriter(f1, true);
					bw1 = new BufferedWriter(fw1);
					
					bw1.write(j+","+ String.valueOf( (double) c0 / 10000 ) + "," + String.valueOf( (double) c1 / 10000 ) + "," + String.valueOf( (double) c01 / 10000 ) + "\n" );
					
					bw1.close();
					
					//System.out.println(j+","+ String.valueOf( (double) c0 / 10000 ) + "," + String.valueOf( (double) c1 / 10000 ) + "," + String.valueOf( (double) c01 / 10000 ) );
					
					//System.out.println("-- Mental Images --\n");
					
					w0.generateMentalImages();
					w1.generateMentalImages();
					w01.generateMentalImages();
					
					fw1 = new FileWriter(f2, true);
					bw1 = new BufferedWriter(fw1);
					
					StringBuilder strB;
					
					Iterator<Entry<String, int[]>> iterator = w0.getMentalImage().entrySet().iterator();
					
					while(iterator.hasNext()) {
						
						Entry<String, int[]> element = iterator.next();
						
						strB = new StringBuilder();
						
						strB.append(element.getKey() + ",");
						
						for (int l = 0; l < element.getValue().length - 1; l++) {
							
							strB.append(String.valueOf(element.getValue()[l]) + ",");
						}
						
						strB.append(String.valueOf(element.getValue()[element.getValue().length - 1]));
						
						bw1.write(strB.toString() + "\n");
					}
					
					iterator = w1.getMentalImage().entrySet().iterator();
					
					while(iterator.hasNext()) {
						
						Entry<String, int[]> element = iterator.next();
						
						strB = new StringBuilder();
						
						strB.append(element.getKey() + ",");
						
						for (int l = 0; l < element.getValue().length - 1; l++) {
							
							strB.append(String.valueOf(element.getValue()[l]) + ",");
						}
						
						strB.append(String.valueOf(element.getValue()[element.getValue().length - 1]));
						
						bw1.write(strB.toString() + "\n");
					}
					
					iterator = w01.getMentalImage().entrySet().iterator();
					
					while(iterator.hasNext()) {
						
						Entry<String, int[]> element = iterator.next();
						
						strB = new StringBuilder();
						
						strB.append(element.getKey() + ",");
						
						for (int l = 0; l < element.getValue().length - 1; l++) {
							
							strB.append(String.valueOf(element.getValue()[l]) + ",");
						}
						
						strB.append(String.valueOf(element.getValue()[element.getValue().length - 1]));
						
						bw1.write(strB.toString() + "\n");
					}
										
					bw1.close();
					
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
					
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("-- Finish --\n");
	}
	
	public static void allCombinations(StringBuilder combination, int set1, int set2, int limit) {
		
		if(combination.length() == limit) {
			//System.out.println("Combination " + index + " - " + combination.toString());
			combinations.add(combination.toString());
			index++;
		}
		else {
			if(set1 > 0) {
				StringBuilder b1 = new StringBuilder();
				b1.append(combination.toString());
				b1.append("1");
				allCombinations(b1, set1 - 1, set2, limit);
			}
			
			if(set2 > 0) {
				StringBuilder b2 = new StringBuilder();
				b2.append(combination.toString());
				b2.append("0");
				allCombinations(b2, set1, set2 - 1, limit);				
			}
		}
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
				
				w1.train(label, example.toString());
			}
			
			br.close();
			
		} catch (FileNotFoundException e) {
			
			System.out.println("File not found!");
			
		} catch (IOException e) {
			
			System.out.println("Error!");
		}
		
		System.out.println("Max Bleaching = " + w1.getMaxBleaching());
		
		System.out.println("\n-- Training phase finished successfully --\n");
	}
	
	public static void test(WiSARD w1, int testingSize, String path) {
		
		System.out.println("-- Initializing testing phase --\n");
		
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
				
				result = w1.test(example.toString());
				
				if(result.equals(label))
					
					counter++;
			}
			
			br.close();
			
		} catch (FileNotFoundException e) {
			
			System.out.println("File not found!");
			
		} catch (IOException e) {
			
			System.out.println("Error!");
		}
		
		System.out.println("Accuracy: " + counter + " / " + testingSize + " : " + (((float) counter / testingSize) * 100 ) + "%");
		
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
	
	// Computes the time spent
	public static long duration() {
		
		return  TimeUnit.NANOSECONDS.toMillis(endTime) - TimeUnit.NANOSECONDS.toMillis(startTime);
	}
}
