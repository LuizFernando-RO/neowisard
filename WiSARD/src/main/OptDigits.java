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

public class OptDigits {

	public static long startTime;
	public static long endTime;
	
	public static int index = 0;
	public static ArrayList<String> combinations = new ArrayList<String>();
	
	public static void main(String[] args) {
		
		startTime = System.nanoTime();
		
		teste();
		
		//two();
		
		//three();
		
		//crossZero(1,10);
		
		//crossOne(1,10);
		
		//crossTwo(1,10);
		
		//crossThree(1,10);
		
		endTime = System.nanoTime();
		
		System.out.println("\n-- Execution time: " + duration() + "ms --");
	}
	
	public static void teste() {
		
		//allCombinations(new StringBuilder(), 9, 9, 18);
		
		String fileNamePrefix ="Input/OptDigits/10-fold/fold",
			   fileNameTrainingSufix = "tra.dat", fileNameTestingSufix = "tst.dat";
		
		String fullName;
		
		File file = new File("");
		FileReader fr;
		BufferedReader br;
		
		FileWriter fw;
		BufferedWriter bw;
		
		try {
			
			// Iterate over 10 folds
			for (int i = 1; i <= 10; i++) {
				
				fullName = fileNamePrefix + i + "/optdigits-10-" + i + fileNameTrainingSufix;
				
				System.out.println(fullName);
				
				// Training file
				
				file = new File(fullName);
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				
				allCombinations(new StringBuilder(), 9, 9, 18);
				
				for (int j = 0; j < 48620; j++) {
					
					
				}
				
				ArrayList<String> trainSet1 = new ArrayList<String>();
				ArrayList<String> trainSet2 = new ArrayList<String>();
				
				
			}
			
		} catch (FileNotFoundException e) {
			
			System.out.println("Error: file '" + file.getName() +"' not found.");
		}
		
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
		
		int m = 32;
		
		StringBuilder example;
		
		String path = "Input/OptDigits/Original/training.txt";
		int trainingSize = 1934;
		
		File f;
		FileReader fr;
		BufferedReader br;

		//Load the training file
		
		String[] trainingSet = new String[1934];
		
		f = new File(path);
		
		try {
			
			fr = new FileReader(f);
			br = new BufferedReader(fr);
		
			for(int i = 0; i < trainingSize; i++) {
				
				example = new StringBuilder();
				
				for (int j = 0; j < m; j++) {
					
					example.append(br.readLine());
				}
				
				example.append(br.readLine().trim());
				
				trainingSet[i] = example.toString();
			}
			
			br.close();
			
		} catch (FileNotFoundException e) {
			
			System.out.println("File not found!");
			
		} catch (IOException e) {
			
			System.out.println("Error!");
		}
		
		System.out.println("Training set loaded successfully!");
		
		// Dividing into 6 blocks
		
		allCombinations(new StringBuilder(), 5, 5, 10);
		
		for(int i = 0; i < 6 ; i++) {
			
			System.out.println("Block " + i);
			
			int testStart = i * 322, testEnd;
			
			if(i < 5)
			
				testEnd = (i+1) * 322;
			
			else 
				
				testEnd = 1934;
				
			for (int j = 0; j < 252; j++) {
				
				System.out.println("Environment " + j);
				
				String combination = combinations.get(j);
				
				int combinationIndex = 0;
				
				ArrayList<String> testSet = new ArrayList<String>();
				ArrayList<String> trainSet1 = new ArrayList<String>();
				ArrayList<String> trainSet2 = new ArrayList<String>();
				
				for (int k = 0; k < 1934; k++) {
					
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
						
						if(i < 5) {
						
							if( k % 161 == 0 && k != testEnd && k > 0 && k <= 1610) {
								
								combinationIndex++;
							}
						}
						
						else {
							
							if( k == 1772) {
								
								combinationIndex++;
							}
						}
					}
				}
				
				f = new File("Input/OptDigits/CrossValidation/Block" + i + "/env" + j + ".txt");
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
			System.out.println("Combination " + index + " - " + combination.toString());
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
	
	// Computes the time spent
	public static long duration() {
		
		return  TimeUnit.NANOSECONDS.toMillis(endTime) - TimeUnit.NANOSECONDS.toMillis(startTime);
	}
}
