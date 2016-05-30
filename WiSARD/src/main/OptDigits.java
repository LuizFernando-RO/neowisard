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
		
		binarize();
		
		//run();
		
		//crossZero(1,10);
		
		//crossOne(1,10);
		
		//crossTwo(1,10);
		
		//crossThree(1,10);
		
		endTime = System.nanoTime();
		
		System.out.println("\n-- Execution time: " + duration() + "ms --");
	}
	
	public static void binarize() {
		
		int[] vector = {0,8,16,16,16,16,16,16,5,16,16,16,16,16,16,15,5,16,16,16,16,16,16,8,1,16,16,16,16,16,16,2,1,15,16,16,16,16,14,0,7,16,16,16,16,16,16,6,10,16,16,16,16,16,16,13,1,10,16,16,16,16,16,16};
		
		int foldLimit = 10, trainLimit = 5058, testLimit = 562;
		
		for (int i = 1; i <= foldLimit; i++) {
			
			String trainName = "Input/OptDigits/10-fold/fold"+i+"/optdigits-10-"+i+"tra.dat", 
				   testName = "Input/OptDigits/10-fold/fold"+i+"/optdigits-10-"+i+"tst.dat",
				   outTrainName = "Input/OptDigits/10-fold/fold"+i+"/optdigits-10-"+i+"trabin.dat",
				   outTestName = "Input/OptDigits/10-fold/fold"+i+"/optdigits-10-"+i+"tstbin.dat";
			
			StringBuilder example;
			String[] splitter;
			
			File inFile;			
			FileReader fr;
			BufferedReader br;
			
			File outFile;
			FileWriter fw;
			BufferedWriter bw;
			
			try {
				
				inFile = new File(trainName);
				fr = new FileReader(inFile);
				br = new BufferedReader(fr);
				
				outFile = new File(outTrainName);
				fw = new FileWriter(outFile);
				bw = new BufferedWriter(fw);
				
				for (int j = 0; j < trainLimit; j++) {
					
					splitter = br.readLine().split(",");
					
					example = new StringBuilder();
					
					int count = 0, avg = 0;
					
					for (int k = 0; k < splitter.length - 1; k++) {
						
						if(Integer.valueOf(splitter[k].trim()) > 0) {
							
							count++;
							avg += Integer.valueOf(splitter[k].trim());
						}
					}
					
					avg = avg / count;
					
					for (int k = 0; k < splitter.length - 1; k++) {
						
						if(Integer.valueOf(splitter[k].trim()) >= avg) 
							
							example.append("1");
						
						else
							
							example.append("0");
					}
					
					bw.write(example + splitter[splitter.length - 1].trim() + "\n");
				}
				
				bw.close();
				
				inFile = new File(testName);
				fr = new FileReader(inFile);
				br = new BufferedReader(fr);
				
				outFile = new File(outTestName);
				fw = new FileWriter(outFile);
				bw = new BufferedWriter(fw);
				
				for (int j = 0; j < testLimit; j++) {
					
					splitter = br.readLine().split(",");
					
					example = new StringBuilder();
					
					int count = 0, avg = 0;
					
					for (int k = 0; k < splitter.length - 1; k++) {
						
						if(Integer.valueOf(splitter[k].trim()) > 0) {
							
							count++;
							avg += Integer.valueOf(splitter[k].trim());
						}
					}
					
					avg = avg / count;
					
					for (int k = 0; k < splitter.length - 1; k++) {
						
						if(Integer.valueOf(splitter[k].trim()) >= avg) 
							
							example.append("1");
						
						else
							
							example.append("0");
					}
					
					bw.write(example + splitter[splitter.length - 1].trim() + "\n");
				}
				
				bw.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void run() {
		
		String fileNamePrefix ="Input/OptDigits/10-fold/fold",
			   fileNameTrainingSufix = "tra.dat", fileNameTestingSufix = "tst.dat";
		
		String fullName;
		
		int combinationSize = 6;
		
		File file;
		FileReader fr;
		BufferedReader br;
		
		FileWriter fw;
		BufferedWriter bw;
		
		allCombinations(new StringBuilder(), combinationSize / 2, combinationSize / 2, combinationSize);
		
		try {
			
			// Iterate over 10 folds
			for (int i = 1; i <= 10; i++) {
				
				System.out.println("Environments for fold " + i);
				
				fullName = fileNamePrefix + i + "/optdigits-10-" + i + fileNameTrainingSufix;
				
				System.out.println(fullName);
				
				file = new File(fullName);
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				
				String[] trainingSet = new String[5058];
				
				for(int j = 0; j < trainingSet.length; j++) {
					
					trainingSet[j] = br.readLine();
				}
			
				for (int j = 0; j < combinations.size(); j++) {
					
					ArrayList<String> trainSet1 = new ArrayList<String>();
					ArrayList<String> trainSet2 = new ArrayList<String>();
					
					String combination = combinations.get(j);
					
					int combinationIndex = 0;
					
					for (int k = 0; k < 5058; k++) {
						
						if(Integer.valueOf(combination.charAt(combinationIndex) - '0') == 1) 
							
							trainSet1.add(trainingSet[k]);
						
						else
							
							trainSet2.add(trainingSet[k]);
						
						if((k+1) % (trainingSet.length / combinationSize) == 0 && k != 0){
							System.out.println("up on k = " + k);
							combinationIndex++;
						}		
					}
					
					file = new File("Input/OptDigits/10-fold/Results.txt");
					
					fw = new FileWriter(file);
					bw = new BufferedWriter(fw);
					
					System.out.println("Full set "+trainingSet.length);
					System.out.println("train 1 "+trainSet1.size());
					System.out.println("train 2"+trainSet1.size() + "\n-");
					
					for(int k = 0; k < trainSet1.size(); k++) 
						
						bw.write(trainSet1.get(k) + "\n");
					
					for(int k = 0; k < trainSet2.size(); k++) 
						
						bw.write(trainSet2.get(k) + "\n");
					
					bw.close();
				}
			}
			
		} catch (FileNotFoundException e) {
			
			System.out.println("Error: file not found.");
		
		} catch (IOException e) {
			
			System.out.println("Error while reading file.");
		}
		
	}
		
	public static void crossZero(int foldLimit, int environmentLimit) {
		
		/* Random Mapping configuration
		 * Intra-environment: equal
		 * Inter-environment: equal
		 */
		
		System.out.println("-- Test Reference: 0 --\n");
		
		WiSARD w0 = new WiSARD("w0", 8, 8, 8);
		WiSARD w1 = new WiSARD("w1", 8, 8, 8);
		WiSARD w01 = new WiSARD("w01", 8, 8, 8);
		
		String[] labels = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		
		for (int i = 0; i < labels.length; i++) {
			
			Discriminator discriminator = new Discriminator(8, 8);
			discriminator.setId(labels[i]);
			
			Discriminator discriminator1 = new Discriminator(8, 8);
			discriminator1.setId(labels[i]);
			
			Discriminator discriminator2 = new Discriminator(8, 8);
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
		
		for(int i = 1; i <= 10; i++) {
			
			f1 = new File("Input/OptDigits/10-fold/fold"+i+"/R0/AccuracyR0.txt");
			
			for (int j = 0; j < environmentLimit; j++) {
				
				w0.clear();
				w1.clear();
				w01.clear();
				
				System.out.println("Environment " + j);
				
				f2 = new File("Input/OptDigits/10-fold/fold"+i+"/R0/Env"+j+"MI.txt");
				f3 = new File("Input/OptDigits/10-fold/fold"+i+"/R0/Env"+j+".txt");
				
				try {
					
					//System.out.println("-- Training --\n");
					
					fr1 = new FileReader(f3);
					br1 = new BufferedReader(fr1);
					
					for (int k = 0; k < 2529; k++) {
						
						splitter = br1.readLine().toString().split(",");
						example = new StringBuilder();
						
						for (int l = 1; l < splitter.length; l++) {
							
							example.append(splitter[l]);
						}
						
						w0.train(splitter[0], example.toString());
						w01.train(splitter[0], example.toString());
					}
					
					for (int k = 2529; k < 5058; k++) {
					
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
