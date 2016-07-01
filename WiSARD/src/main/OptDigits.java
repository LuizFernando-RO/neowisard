package main;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
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

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import model.Discriminator;
import model.WiSARD;

public class OptDigits {

	public static long startTime;
	public static long endTime;
	
	public static int index = 0;
	public static ArrayList<String> combinations = new ArrayList<String>();
	
	public static void main(String[] args) {
		
		startTime = System.nanoTime();
		
		//prepare();
		
		//run();
		
		
		int foldLimit = 1,
			envLimit = 1,
			n = 32,
			m = 32,
			tuples = 32;
		
		//crossZero2(foldLimit,envLimit, n, m, tuples, 4);
		
		crossZero3(foldLimit,envLimit, n, m, tuples, 4);
		
		//crossZero4(foldLimit,envLimit, n, m, tuples, 4, 4);
		
		//crossZero(foldLimit,envLimit, n, m, tuples);
		
		//crossOne(foldLimit,envLimit, n, m, tuples);
		
		//crossTwo(foldLimit,envLimit, n, m, tuples);
		
		//crossThree(foldLimit,envLimit, n, m, tuples);
		
		//imageAnalysis(n, m);
		
		endTime = System.nanoTime();
		
		System.out.println("\n-- Execution time: " + duration() + "ms --");
	}
	
	/* Domain */
	
	public static void prepare() {
			
		String inFileName, trainFileName, testFileName;
		String rawExample;
		StringBuilder example;
		
		String[] dataset = new String[5620];
		
		ArrayList<String> trainingSet, testSet;
		
		File inFile, trainFile, testFile;
		
		FileReader fr;
		BufferedReader br;
		
		FileWriter fw;
		BufferedWriter bw;
		
		try {
			// Iterate over the dataset
			
			inFileName = "Input/OptDigits/Original/optdigits.txt";
			
			inFile = new File(inFileName);
			
			fr = new FileReader(inFile);
			br = new BufferedReader(fr);
			
			for (int i = 0; i < 5620; i++) {
				
				example = new StringBuilder();
				
				for(int j = 0; j < 32; j++) {
					
					rawExample = br.readLine().trim();
					
					for (int k = 0; k < 32; k++) {
						
						example.append("," + rawExample.charAt(k));
					}
				}
				
				dataset[i] = br.readLine().trim() + example.toString();
			}
			
			for (int i = 0; i < 10; i++) {
				
				trainFileName = "Input/OptDigits/10-fold/fold" + (i+1) + "/optdigits-10-" + (i+1) + "tra.dat";
				testFileName = "Input/OptDigits/10-fold/fold" + (i+1) + "/optdigits-10-" + (i+1) + "tst.dat";
				
				trainFile = new File(trainFileName);
				testFile = new File(testFileName);
				
				trainingSet = new ArrayList<String>();
				testSet = new ArrayList<String>();
				
				for(int j = 0; j < 5620; j++) {
					
					if(j >= (i*562) && j < ((i+1)*562))
						
						testSet.add(dataset[j]);
					
					else 
						
						trainingSet.add(dataset[j]);
				}
				
				fw = new FileWriter(trainFile);
				bw = new BufferedWriter(fw);
				
				for (int j = 0; j < trainingSet.size(); j++) {
					
					bw.write(trainingSet.get(j) + "\n");
				}
				
				bw.close();
				
				fw = new FileWriter(testFile);
				bw = new BufferedWriter(fw);
				
				for (int j = 0; j < testSet.size(); j++) {
					
					bw.write(testSet.get(j) + "\n");
				}
				
				bw.close();
			}
			
		} catch (FileNotFoundException e) {
			
			System.out.println("Error: file not found.");
		
		} catch (IOException e) {
			
			System.out.println("Error while reading file.");
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
		File testFile;
		
		FileWriter fw;
		BufferedWriter bw;
		
		allCombinations(new StringBuilder(), combinationSize / 2, combinationSize / 2, combinationSize);
		
		try {
			// Iterate over 10 folds
			for (int i = 1; i <= 10; i++) {
				
				System.out.println("Environments for fold " + i);
				
				fullName = fileNamePrefix + i + "/optdigits-10-" + i + fileNameTrainingSufix;
				
				file = new File(fullName);
				testFile = new File(fileNamePrefix + i + "/optdigits-10-" + i + fileNameTestingSufix);
				
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
					
					for (int k = 0; k < trainingSet.length; k++) {
						
						if(Integer.valueOf(combination.charAt(combinationIndex) - '0') == 1) 
							
							trainSet1.add(trainingSet[k]);
						
						else
							
							trainSet2.add(trainingSet[k]);
						
						if((k+1) % (trainingSet.length / combinationSize) == 0 && k != 0){
							
							combinationIndex++;
						}		
					}
					
					br.close();
					
					fr = new FileReader(testFile);
					br = new BufferedReader(fr);
					
					file = new File("Input/OptDigits/10-fold/fold" + i + "/Env" + j + ".txt");
					
					fw = new FileWriter(file);
					bw = new BufferedWriter(fw);
					
					for(int k = 0; k < trainSet1.size(); k++) 
						
						bw.write(trainSet1.get(k) + "\n");
					
					for(int k = 0; k < trainSet2.size(); k++) 
						
						bw.write(trainSet2.get(k) + "\n");
					
					for(int k = 0; k < 562; k++) {
						
						bw.write(br.readLine() + "\n");
					}
					
					bw.close();
				}
			}
			
		} catch (FileNotFoundException e) {
			
			System.out.println("Error: file not found.");
		
		} catch (IOException e) {
			
			System.out.println("Error while reading file.");
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
	
	//
	/* Two thresholds */
	
	public static void crossZero4(int foldLimit, int environmentLimit, int n, int m, int tuples, int threshold, int upperThreshold) {
		
		System.out.println("-- Experiments with lower and upper threshold --");
		
		/* Random Mapping configuration
		 * Intra-environment: equal
		 * Inter-environment: equal
		 */
		
		System.out.println("-- Test Reference: 0 --\n");
		
		WiSARD w0 = new WiSARD("w0", n, m, tuples);
		WiSARD w1 = new WiSARD("w1", n, m, tuples);
		WiSARD w01 = new WiSARD("w01", n, m, tuples);
		
		String[] labels = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		
		for (int i = 0; i < labels.length; i++) {
			
			Discriminator discriminator = new Discriminator(n, m);
			discriminator.setId(labels[i]);
			
			Discriminator discriminator1 = new Discriminator(n, m);
			discriminator1.setId(labels[i]);
			
			Discriminator discriminator2 = new Discriminator(n, m);
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
		
		ArrayList<String> testSet;
		
		int c0, c1, c01, c20, c21;
		
		System.out.println("-- Initializing experiments --\n");
		
		for(int i = 1; i <= foldLimit; i++) {
			
			f1 = new File("Input/OptDigits/10-fold/resultsLimiar/"+ threshold +"/R0/Accuracy-fold"+i+"R0.txt");
			
			for (int j = 0; j < environmentLimit; j++) {
				
				w0.clear();
				w1.clear();
				w01.clear();
				
				System.out.println("Environment " + j);
				
				f2 = new File("Input/OptDigits/10-fold/resultsLimiar/"+threshold+"/R0/fold"+i+"Env"+j+"MI.txt");
				f3 = new File("Input/OptDigits/10-fold/fold"+i+"/Env"+j+".txt");
				
				testSet = new ArrayList<String>();
				
				try {
					
					/*
					 * TRAINING PHASE
					 * */
					
					System.out.println("-- Training --");
					
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

					/*
					 * TESTING PHASE
					 * */
					
					c0 = 0;
					c1 = 0;
					c01 = 0;
					
					for (int k = 5058; k < 5620; k++) {
						
						testSet.add(br1.readLine().toString());
						
						splitter = testSet.get(k - 5058).split(",");
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
					
					/*
					 * MENTAL IMAGE GENERATION
					 * */
					
					System.out.println("-- Testing --");
					
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
					
					/*
					 * MEMORY TRANSFER
					 * */
					
					System.out.println("-- Memory Transfer --");
					
					w0.syntheticTrainingSet();
					w1.syntheticTrainingSet();
					
					int[] w0counter0 = new int[10];
					int[] w1counter0 = new int[10];
					
					int[] w0counter1 = new int[10];
					int[] w1counter1 = new int[10];
					
					int[] w0counter2 = new int[10];
					int[] w1counter2 = new int[10];
					
					// Alterar aqui para treinar com todo o conjunto
					for (int k = 0; k < 10; k++) {	
						
						//System.out.println(k);
					
						int[][] w0Knownledge = w0.getSyntheticTrainingSet().get(String.valueOf(k)),
								w1Knownledge = w1.getSyntheticTrainingSet().get(String.valueOf(k));
						
						StringBuilder prototype;
						
						System.out.println("w0knowledge: " + w0Knownledge.length);
						System.out.println("w1knowledge: " + w1Knownledge.length);
												
						for (int l = 0; l < w0Knownledge.length; l++) {
							
							prototype = new StringBuilder();
							
							for (int l2 = 0; l2 < w0Knownledge[0].length; l2++) {
								
								prototype.append(w0Knownledge[l][l2]);
							}
							
							//Alterar aqui parar ver a resposta da rede frente às amostras
							
							String str = w1.test2(prototype.toString());							
							String[] spl = str.split("\t");
							
							String cl = spl[0];
							int m1 = Integer.valueOf(spl[1]), m2 = Integer.valueOf(spl[2]), sum = Integer.valueOf(spl[3]);
							
							if(sum != 0) {
								
								if( ((m1-m2)/ (double) sum) < (10 / (double) threshold) ) {
									
									w1.train(String.valueOf(k), prototype.toString());
									
									w1counter0[k]++;
								}
								
								/*if( ((m1-m2)/ (double) sum) < 0.6 ) {
									
									w1.train(String.valueOf(k), prototype.toString());
									
									w1counter1[k]++;
								}
								
								if( ((m1-m2)/ (double) sum) < 0.4 ) {
									
									w1.train(String.valueOf(k), prototype.toString());
									
									w1counter2[k]++;
								}*/
							}
							
							else {
									
								w1.train(String.valueOf(k), prototype.toString());
								
								w1counter0[k]++;
								w1counter1[k]++;
								w1counter2[k]++;
							}
								
							
							if(Integer.valueOf(cl) != k) {
								
								w1.train(String.valueOf(k), prototype.toString());
								
								w1counter0[k]++;
								w1counter1[k]++;
								w1counter2[k]++;
							}
							
							//w1.train(String.valueOf(k), prototype.toString());
						}
						
						for (int l = 0; l < w1Knownledge.length; l++) {
							
							prototype = new StringBuilder();
							
							for (int l2 = 0; l2 < w1Knownledge[0].length; l2++) {
								prototype.append(w1Knownledge[l][l2]);
							}
							
							String str = w0.test2(prototype.toString());							
							String[] spl = str.split("\t");
							
							String cl = spl[0];
							int m1 = Integer.valueOf(spl[1]), m2 = Integer.valueOf(spl[2]), sum = Integer.valueOf(spl[3]);
							
							if(sum != 0) {
								
								if( ((m1-m2)/ (double) sum) < (10 / (double) threshold) ) {
									
									w0.train(String.valueOf(k), prototype.toString());
									
									w0counter0[k]++;
								}
								/*
								if( ((m1-m2)/ (double) sum) < 0.6 ) {
									
									w0.train(String.valueOf(k), prototype.toString());
									
									w0counter1[k]++;
								}
								
								if( ((m1-m2)/ (double) sum) < 0.4 ) {
									
									w0.train(String.valueOf(k), prototype.toString());
									
									w0counter2[k]++;
								}*/
							}
							
							else {
								
								w0.train(String.valueOf(k), prototype.toString());
								
								w0counter0[k]++;
								w0counter1[k]++;
								w0counter2[k]++;
							}
							
							if(Integer.valueOf(cl) != k) {
								
								w0.train(String.valueOf(k), prototype.toString());
								
								w0counter0[k]++;
								w0counter1[k]++;
								w0counter2[k]++;
							}
							
							//w0.train(String.valueOf(k), prototype.toString());
						}
						
						
					}
					
					/*
					for (int o = 0; o < 10; o++) {
						
						System.out.print(w0counter0[o] + "\t");
					}
					System.out.println();
					for (int o = 0; o < 10; o++) {
						
						System.out.print(w1counter0[o] + "\t");
					}
					System.out.println();
					for (int o = 0; o < 10; o++) {
						
						System.out.print(w0counter1[o] + "\t");
					}
					System.out.println();
					for (int o = 0; o < 10; o++) {
						
						System.out.print(w1counter1[o] + "\t");
					}
					System.out.println();
					for (int o = 0; o < 10; o++) {
						
						System.out.print(w0counter2[o] + "\t");
					}
					System.out.println();
					for (int o = 0; o < 10; o++) {
						
						System.out.print(w1counter2[o] + "\t");
					}
					System.out.println();*/
					
					c20 = 0;
					c21 = 0;
					
					for (int k = 5058; k < 5620; k++) {
						
						splitter = testSet.get(k - 5058).split(",");
						example = new StringBuilder();
						
						for (int l = 1; l < splitter.length; l++) {
							
							example.append(splitter[l]);
						}
						
						label = w0.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c20++;
						}
						
						label = w1.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c21++;
						}
					}
					
					fw1 = new FileWriter(f1, true);
					bw1 = new BufferedWriter(fw1);
					
					bw1.write(String.valueOf( (double) c0 / 562 ) + "\t" + String.valueOf( (double) c20 / 562 ) + "\t" + String.valueOf( (double) c1 / 562 ) + "\t"+ String.valueOf( (double) c21 / 562 ) + "\t" + String.valueOf( (double) c01 / 562 ) + "\n" );
					
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
	
	//
	/* Transferring differences */
	
	public static void crossZero3(int foldLimit, int environmentLimit, int n, int m, int tuples, int threshold) {
		
		System.out.println("-- Experiments with transferring only differences --");
		
		/* Random Mapping configuration
		 * Intra-environment: equal
		 * Inter-environment: equal
		 */
		
		System.out.println("-- Test Reference: 0 --\n");
		
		WiSARD w0 = new WiSARD("w0", n, m, tuples);
		WiSARD w1 = new WiSARD("w1", n, m, tuples);
		WiSARD w01 = new WiSARD("w01", n, m, tuples);
		
		String[] labels = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		
		for (int i = 0; i < labels.length; i++) {
			
			Discriminator discriminator = new Discriminator(n, m);
			discriminator.setId(labels[i]);
			
			Discriminator discriminator1 = new Discriminator(n, m);
			discriminator1.setId(labels[i]);
			
			Discriminator discriminator2 = new Discriminator(n, m);
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
		
		ArrayList<String> testSet;
		
		int c0, c1, c01, c20, c21;
		
		System.out.println("-- Initializing experiments --\n");
		
		for(int i = 1; i <= foldLimit; i++) {
			
			f1 = new File("Input/OptDigits/10-fold/resultsDifferences/R0/Accuracy-fold"+i+"R0.txt");
			
			for (int j = 0; j < environmentLimit; j++) {
				
				w0.clear();
				w1.clear();
				w01.clear();
				
				System.out.println("Environment " + j);
				
				f2 = new File("Input/OptDigits/10-fold/resultsDifferences/R0/fold"+i+"Env"+j+"MI.txt");
				f3 = new File("Input/OptDigits/10-fold/fold"+i+"/Env"+j+".txt");
				
				testSet = new ArrayList<String>();
				
				try {
					
					/*
					 * TRAINING PHASE
					 * */
					
					System.out.println("-- Training --");
					
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

					/*
					 * TESTING PHASE
					 * */
					
					System.out.println("-- Testing --");
					
					c0 = 0;
					c1 = 0;
					c01 = 0;
					
					for (int k = 5058; k < 5620; k++) {
						
						testSet.add(br1.readLine().toString());
						
						splitter = testSet.get(k - 5058).split(",");
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
					
					/*
					 * MENTAL IMAGE GENERATION
					 * */
					
					System.out.println("-- Mental Image Generation --");
					
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
					
					/*
					 * MEMORY TRANSFER
					 * */
					
					System.out.println("-- Memory Transfer --");
					
					//For each mental image, get the difference
					
					int[] w0Image, w1Image, diffImage;
					ArrayList<Integer> temp;
					
					for (int k = 0; k < 1; k++) {
						
						ArrayList<Integer> temp2 = w0.getMap().get("0").getTuplas();
						
						for (int l = 0; l < temp2.size(); l++) {
							
							System.out.print(l + "\t");							
						}
						
						System.out.println();
						
						for (int l = 0; l < temp2.size(); l++) {
							
							System.out.print(temp2.get(l) + "\t");							
						}
						
						w0Image = w0.getMentalImage().get(String.valueOf(k));
						w1Image = w1.getMentalImage().get(String.valueOf(k));
						
						diffImage = new int[w0Image.length];
						
						for (int k2 = 0; k2 < w1Image.length; k2++) {
							diffImage[k2] = w0Image[k2] - w1Image[k2];
						}
						
						for (int k2 = 0; k2 < w1Image.length; k2++) {
							
							//w1 has something to offer to w0
							if(diffImage[k2] < 0) {
								
								temp = w0.getMap().get(String.valueOf(k)).getTuplas();
								
								int index = 0;
								
								for (int l = 0; l < temp.size(); l++) {
									
									if(temp.get(l) == k2) {
										
										index = l;
										l = temp.size();
									}
								}
								
								System.out.println("-" + k2 + " is on " + index);
								
								int ram = index / tuples;
								
								HashMap<String, Integer> tempMap = w0.getMap().get(String.valueOf(k)).getRams().get(ram).getMapa();
								
								System.out.println(tempMap.size());
							}

							//w0 has something to offer to w1
							if(diffImage[k2] > 0) {
								
								temp = w1.getMap().get(String.valueOf(k)).getTuplas();
								
								for (int l = 0; l < temp.size(); l++) {
									
									if(temp.get(l) == k2) {
										
										index = l;
										l = temp.size();
									}
								}
								
								System.out.println("+" + k2 + " is on " + index);
							}
						}						
					}					
					
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
					
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("-- Finish --\n");
	}	
	
	//25%
	/* Threshold analysis */
	
	public static void crossZero2(int foldLimit, int environmentLimit, int n, int m, int tuples, int threshold) {
		
		System.out.println("-- Experiments with threshold --");
		
		/* Random Mapping configuration
		 * Intra-environment: equal
		 * Inter-environment: equal
		 */
		
		System.out.println("-- Test Reference: 0 --\n");
		
		WiSARD w0 = new WiSARD("w0", n, m, tuples);
		WiSARD w1 = new WiSARD("w1", n, m, tuples);
		WiSARD w01 = new WiSARD("w01", n, m, tuples);
		
		String[] labels = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		
		for (int i = 0; i < labels.length; i++) {
			
			Discriminator discriminator = new Discriminator(n, m);
			discriminator.setId(labels[i]);
			
			Discriminator discriminator1 = new Discriminator(n, m);
			discriminator1.setId(labels[i]);
			
			Discriminator discriminator2 = new Discriminator(n, m);
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
		
		ArrayList<String> testSet;
		
		int c0, c1, c01, c20, c21;
		
		System.out.println("-- Initializing experiments --\n");
		
		for(int i = 1; i <= foldLimit; i++) {
			
			f1 = new File("Input/OptDigits/10-fold/resultsLimiar/"+ threshold +"/R0/Accuracy-fold"+i+"R0.txt");
			
			for (int j = 0; j < environmentLimit; j++) {
				
				w0.clear();
				w1.clear();
				w01.clear();
				
				System.out.println("Environment " + j);
				
				f2 = new File("Input/OptDigits/10-fold/resultsLimiar/"+threshold+"/R0/fold"+i+"Env"+j+"MI.txt");
				f3 = new File("Input/OptDigits/10-fold/fold"+i+"/Env"+j+".txt");
				
				testSet = new ArrayList<String>();
				
				try {
					
					/*
					 * TRAINING PHASE
					 * */
					
					System.out.println("-- Training --");
					
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

					/*
					 * TESTING PHASE
					 * */
					
					c0 = 0;
					c1 = 0;
					c01 = 0;
					
					for (int k = 5058; k < 5620; k++) {
						
						testSet.add(br1.readLine().toString());
						
						splitter = testSet.get(k - 5058).split(",");
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
					
					/*
					 * MENTAL IMAGE GENERATION
					 * */
					
					System.out.println("-- Testing --");
					
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
					
					/*
					 * MEMORY TRANSFER
					 * */
					
					System.out.println("-- Memory Transfer --");
					
					w0.syntheticTrainingSet();
					w1.syntheticTrainingSet();
					
					int[] w0counter0 = new int[10];
					int[] w1counter0 = new int[10];
					
					int[] w0counter1 = new int[10];
					int[] w1counter1 = new int[10];
					
					int[] w0counter2 = new int[10];
					int[] w1counter2 = new int[10];
					
					// Alterar aqui para treinar com todo o conjunto
					for (int k = 0; k < 10; k++) {	
						
						//System.out.println(k);
					
						int[][] w0Knownledge = w0.getSyntheticTrainingSet().get(String.valueOf(k)),
								w1Knownledge = w1.getSyntheticTrainingSet().get(String.valueOf(k));
						
						StringBuilder prototype;
						
						System.out.println("w0knowledge: " + w0Knownledge.length);
						System.out.println("w1knowledge: " + w1Knownledge.length);
												
						for (int l = 0; l < w0Knownledge.length; l++) {
							
							prototype = new StringBuilder();
							
							for (int l2 = 0; l2 < w0Knownledge[0].length; l2++) {
								
								prototype.append(w0Knownledge[l][l2]);
							}
							
							//Alterar aqui parar ver a resposta da rede frente às amostras
							
							String str = w1.test2(prototype.toString());							
							String[] spl = str.split("\t");
							
							String cl = spl[0];
							int m1 = Integer.valueOf(spl[1]), m2 = Integer.valueOf(spl[2]), sum = Integer.valueOf(spl[3]);
							
							if(sum != 0) {
								
								if( ((m1-m2)/ (double) sum) < (10 / (double) threshold) ) {
									
									w1.train(String.valueOf(k), prototype.toString());
									
									w1counter0[k]++;
								}
								
								/*if( ((m1-m2)/ (double) sum) < 0.6 ) {
									
									w1.train(String.valueOf(k), prototype.toString());
									
									w1counter1[k]++;
								}
								
								if( ((m1-m2)/ (double) sum) < 0.4 ) {
									
									w1.train(String.valueOf(k), prototype.toString());
									
									w1counter2[k]++;
								}*/
							}
							
							else {
									
								w1.train(String.valueOf(k), prototype.toString());
								
								w1counter0[k]++;
								w1counter1[k]++;
								w1counter2[k]++;
							}
								
							
							if(Integer.valueOf(cl) != k) {
								
								w1.train(String.valueOf(k), prototype.toString());
								
								w1counter0[k]++;
								w1counter1[k]++;
								w1counter2[k]++;
							}
							
							//w1.train(String.valueOf(k), prototype.toString());
						}
						
						for (int l = 0; l < w1Knownledge.length; l++) {
							
							prototype = new StringBuilder();
							
							for (int l2 = 0; l2 < w1Knownledge[0].length; l2++) {
								prototype.append(w1Knownledge[l][l2]);
							}
							
							String str = w0.test2(prototype.toString());							
							String[] spl = str.split("\t");
							
							String cl = spl[0];
							int m1 = Integer.valueOf(spl[1]), m2 = Integer.valueOf(spl[2]), sum = Integer.valueOf(spl[3]);
							
							if(sum != 0) {
								
								if( ((m1-m2)/ (double) sum) < (10 / (double) threshold) ) {
									
									w0.train(String.valueOf(k), prototype.toString());
									
									w0counter0[k]++;
								}
								/*
								if( ((m1-m2)/ (double) sum) < 0.6 ) {
									
									w0.train(String.valueOf(k), prototype.toString());
									
									w0counter1[k]++;
								}
								
								if( ((m1-m2)/ (double) sum) < 0.4 ) {
									
									w0.train(String.valueOf(k), prototype.toString());
									
									w0counter2[k]++;
								}*/
							}
							
							else {
								
								w0.train(String.valueOf(k), prototype.toString());
								
								w0counter0[k]++;
								w0counter1[k]++;
								w0counter2[k]++;
							}
							
							if(Integer.valueOf(cl) != k) {
								
								w0.train(String.valueOf(k), prototype.toString());
								
								w0counter0[k]++;
								w0counter1[k]++;
								w0counter2[k]++;
							}
							
							//w0.train(String.valueOf(k), prototype.toString());
						}
						
						
					}
					
					/*
					for (int o = 0; o < 10; o++) {
						
						System.out.print(w0counter0[o] + "\t");
					}
					System.out.println();
					for (int o = 0; o < 10; o++) {
						
						System.out.print(w1counter0[o] + "\t");
					}
					System.out.println();
					for (int o = 0; o < 10; o++) {
						
						System.out.print(w0counter1[o] + "\t");
					}
					System.out.println();
					for (int o = 0; o < 10; o++) {
						
						System.out.print(w1counter1[o] + "\t");
					}
					System.out.println();
					for (int o = 0; o < 10; o++) {
						
						System.out.print(w0counter2[o] + "\t");
					}
					System.out.println();
					for (int o = 0; o < 10; o++) {
						
						System.out.print(w1counter2[o] + "\t");
					}
					System.out.println();*/
					
					c20 = 0;
					c21 = 0;
					
					for (int k = 5058; k < 5620; k++) {
						
						splitter = testSet.get(k - 5058).split(",");
						example = new StringBuilder();
						
						for (int l = 1; l < splitter.length; l++) {
							
							example.append(splitter[l]);
						}
						
						label = w0.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c20++;
						}
						
						label = w1.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c21++;
						}
					}
					
					fw1 = new FileWriter(f1, true);
					bw1 = new BufferedWriter(fw1);
					
					bw1.write(String.valueOf( (double) c0 / 562 ) + "\t" + String.valueOf( (double) c20 / 562 ) + "\t" + String.valueOf( (double) c1 / 562 ) + "\t"+ String.valueOf( (double) c21 / 562 ) + "\t" + String.valueOf( (double) c01 / 562 ) + "\n" );
					
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
		
	// done
	/* Classical Memory Transfer */ 
	
	public static void crossZero(int foldLimit, int environmentLimit, int n, int m, int tuples) {
		
		/* Random Mapping configuration
		 * Intra-environment: equal
		 * Inter-environment: equal
		 */
		
		System.out.println("-- Test Reference: 0 --\n");
		
		WiSARD w0 = new WiSARD("w0", n, m, tuples);
		WiSARD w1 = new WiSARD("w1", n, m, tuples);
		WiSARD w01 = new WiSARD("w01", n, m, tuples);
		
		String[] labels = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		
		for (int i = 0; i < labels.length; i++) {
			
			Discriminator discriminator = new Discriminator(n, m);
			discriminator.setId(labels[i]);
			
			Discriminator discriminator1 = new Discriminator(n, m);
			discriminator1.setId(labels[i]);
			
			Discriminator discriminator2 = new Discriminator(n, m);
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
		
		ArrayList<String> testSet;
		
		int c0, c1, c01, c20, c21;
		
		System.out.println("-- Initializing experiments --\n");
		
		for(int i = 1; i <= foldLimit; i++) {
			
			f1 = new File("Input/OptDigits/10-fold/results/R0/Accuracy-fold"+i+"R0.txt");
			
			for (int j = 0; j < environmentLimit; j++) {
				
				w0.clear();
				w1.clear();
				w01.clear();
				
				System.out.println("Environment " + j);
				
				f2 = new File("Input/OptDigits/10-fold/results/R0/fold"+i+"Env"+j+"MI.txt");
				f3 = new File("Input/OptDigits/10-fold/fold"+i+"/Env"+j+".txt");
				
				testSet = new ArrayList<String>();
				
				try {
					
					/*
					 * TRAINING PHASE
					 * */
					
					System.out.println("-- Training --");
					
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

					/*
					 * TESTING PHASE
					 * */
					
					c0 = 0;
					c1 = 0;
					c01 = 0;
					
					for (int k = 5058; k < 5620; k++) {
						
						testSet.add(br1.readLine().toString());
						
						splitter = testSet.get(k - 5058).split(",");
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
					
					/*
					 * MENTAL IMAGE GENERATION
					 * */
					
					System.out.println("-- Testing --");
					
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
					
					/*
					 * MEMORY TRANSFER
					 * */
					
					System.out.println("-- Memory Transfer --");
					
					w0.syntheticTrainingSet();
					w1.syntheticTrainingSet();
					
					for (int k = 0; k < 10; k++) {	
					
						int[][] w0Knownledge = w0.getSyntheticTrainingSet().get(String.valueOf(k)),
								w1Knownledge = w1.getSyntheticTrainingSet().get(String.valueOf(k));
						
						StringBuilder prototype;
						
						for (int l = 0; l < w0Knownledge.length; l++) {
							
							prototype = new StringBuilder();
							
							for (int l2 = 0; l2 < w0Knownledge[0].length; l2++) {
								
								prototype.append(w0Knownledge[l][l2]);
							}
							
							w1.train(String.valueOf(k), prototype.toString());
						}
						
						for (int l = 0; l < w1Knownledge.length; l++) {
							
							prototype = new StringBuilder();
							
							for (int l2 = 0; l2 < w1Knownledge[0].length; l2++) {
								prototype.append(w1Knownledge[l][l2]);
							}
							
							w0.train(String.valueOf(k), prototype.toString());
						}
					}
					
					c20 = 0;
					c21 = 0;
					
					for (int k = 5058; k < 5620; k++) {
						
						splitter = testSet.get(k - 5058).split(",");
						example = new StringBuilder();
						
						for (int l = 1; l < splitter.length; l++) {
							
							example.append(splitter[l]);
						}
						
						label = w0.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c20++;
						}
						
						label = w1.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c21++;
						}
					}
					
					fw1 = new FileWriter(f1, true);
					bw1 = new BufferedWriter(fw1);
					
					bw1.write(j+"\t"+ String.valueOf( (double) c0 / 562 ) + "\t" + String.valueOf( (double) c20 / 562 ) + "\t" + String.valueOf( (double) c1 / 562 ) + "\t"+ String.valueOf( (double) c21 / 562 ) + "\t" + String.valueOf( (double) c01 / 562 ) + "\n" );
					
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
	
	public static void crossOne(int foldLimit, int environmentLimit, int n, int m, int tuples) {
		
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
		
		int c0, c1, c01, c20, c21;
		
		ArrayList<String> testSet;
		
		System.out.println("-- Initializing experiments --\n");
		
		for(int i = 1; i <= foldLimit; i++) {
			
			System.out.println("Block " + i);
			
			f1 = new File("Input/CompressedOptDigits/10-fold/results/R1/Accuracy-fold"+i+"R1.txt");
			
			for (int j = 0; j < environmentLimit; j++) {
				
				WiSARD w0 = new WiSARD("w0", n, m, tuples);
				WiSARD w1 = new WiSARD("w1", n, m, tuples);
				WiSARD w01 = new WiSARD("w01", n, m, tuples);
				
				String[] labels = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
				
				for (int k = 0; k < labels.length; k++) {
					Discriminator discriminator = new Discriminator(n, m);
					discriminator.setId(labels[k]);
					
					Discriminator discriminator1 = new Discriminator(n, m);
					discriminator1.setId(labels[k]);
					
					Discriminator discriminator2 = new Discriminator(n, m);
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
				
				f2 = new File("Input/OptDigits/10-fold/results/R1/fold"+i+"Env"+j+"MI.txt");
				f3 = new File("Input/OptDigits/10-fold/fold"+i+"/Env"+j+".txt");
				
				testSet = new ArrayList<String>();
				
				try {
					
					/*
					 * TRAINING PHASE
					 * */
					
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
					
					/*
					 * TESTING PHASE
					 * */
					
					c0 = 0;
					c1 = 0;
					c01 = 0;
					
					for (int k = 5058; k < 5620; k++) {
						
						testSet.add(br1.readLine().toString());
						
						splitter = testSet.get(k - 5058).split(",");
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
					
					/*
					 * MENTAL IMAGE GENERATION
					 * */
					
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
					
					/*
					 * MEMORY TRANSFER
					 * */
					
					w0.syntheticTrainingSet();
					w1.syntheticTrainingSet();
					
					for (int k = 0; k < 10; k++) {
						
						int[][] w0Knownledge = w0.getSyntheticTrainingSet().get(String.valueOf(k)),
								w1Knownledge = w1.getSyntheticTrainingSet().get(String.valueOf(k));
						
						StringBuilder prototype;
								
						for (int l = 0; l < w0Knownledge.length; l++) {
							
							prototype = new StringBuilder();
							
							for (int l2 = 0; l2 < w0Knownledge[0].length; l2++) {
								prototype.append(w0Knownledge[l][l2]);
							}
						
							w1.train(String.valueOf(k), prototype.toString());
						}
						
						for (int l = 0; l < w1Knownledge.length; l++) {
							
							prototype = new StringBuilder();
							
							for (int l2 = 0; l2 < w1Knownledge[0].length; l2++) {
								prototype.append(w1Knownledge[l][l2]);
							}
							
							w0.train(String.valueOf(k), prototype.toString());
						}
					}
					
					c20 = 0;
					c21 = 0;
					
					for (int k = 5058; k < 5620; k++) {
						
						splitter = testSet.get(k - 5058).split(",");
						example = new StringBuilder();
						
						for (int l = 1; l < splitter.length; l++) {
							
							example.append(splitter[l]);
						}
						
						label = w0.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c20++;
						}
						
						label = w1.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c21++;
						}
					}
					
					fw1 = new FileWriter(f1, true);
					bw1 = new BufferedWriter(fw1);
					
					bw1.write(j+"\t"+ String.valueOf( (double) c0 / 562 ) + "\t" + String.valueOf( (double) c20 / 562 ) + "\t" + String.valueOf( (double) c1 / 562 ) + "\t"+ String.valueOf( (double) c21 / 562 ) + "\t" + String.valueOf( (double) c01 / 562 ) + "\n" );
					
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
	
	public static void crossTwo(int foldLimit, int environmentLimit, int n, int m, int tuples) {
		
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
		
		int c0, c1, c01, c20, c21;
		
		ArrayList<String> testSet;
		
		System.out.println("-- Initializing experiments --\n");
		
		WiSARD w0 = new WiSARD("w0", n, m, tuples);
		WiSARD w1 = new WiSARD("w1", n, m, tuples);
		WiSARD w01 = new WiSARD("w01", n, m, tuples);
		
		for(int i = 1; i <= foldLimit; i++) {
			
			System.out.println("Block " + i);
			
			f1 = new File("Input/OptDigits/10-fold/results/R2/Accuracy-fold"+i+"R2.txt");
			
			for (int j = 0; j < environmentLimit; j++) {
				
				w0.clear();
				w1.clear();
				w01.clear();
				
				System.out.println("Environment " + j);
				
				f2 = new File("Input/OptDigits/10-fold/results/R2/fold"+i+"Env"+j+"MI.txt");
				f3 = new File("Input/OptDigits/10-fold/fold"+i+"/Env"+j+".txt");
				
				testSet = new ArrayList<String>();
				
				try {
					
					/*
					 * TRAINING PHASE
					 * */
					
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

					/*
					 * TESTING PHASE
					 * */
					
					c0 = 0;
					c1 = 0;
					c01 = 0;
					
					for (int k = 5058; k < 5620; k++) {
						
						testSet.add(br1.readLine().toString());
						
						splitter = testSet.get(k - 5058).split(",");
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
					
					/*
					 * MENTAL IMAGE GENERATION
					 * */
					
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
					
					/*
					 * MEMORY TRANSFER
					 * */
					
					w0.syntheticTrainingSet();
					w1.syntheticTrainingSet();
					
					for (int k = 0; k < 10; k++) {
						
						int[][] w0Knownledge = w0.getSyntheticTrainingSet().get(String.valueOf(k)),
								w1Knownledge = w1.getSyntheticTrainingSet().get(String.valueOf(k));
						
						StringBuilder prototype;
								
						for (int l = 0; l < w0Knownledge.length; l++) {
							
							prototype = new StringBuilder();
							
							for (int l2 = 0; l2 < w0Knownledge[0].length; l2++) {
								prototype.append(w0Knownledge[l][l2]);
							}
						
							w1.train(String.valueOf(k), prototype.toString());
						}
						
						for (int l = 0; l < w1Knownledge.length; l++) {
							
							prototype = new StringBuilder();
							
							for (int l2 = 0; l2 < w1Knownledge[0].length; l2++) {
								prototype.append(w1Knownledge[l][l2]);
							}
							
							w0.train(String.valueOf(k), prototype.toString());
						}
					}
					
					c20 = 0;
					c21 = 0;
					
					for (int k = 5058; k < 5620; k++) {
						
						splitter = testSet.get(k - 5058).split(",");
						example = new StringBuilder();
						
						for (int l = 1; l < splitter.length; l++) {
							
							example.append(splitter[l]);
						}
						
						label = w0.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c20++;
						}
						
						label = w1.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c21++;
						}
					}
					
					fw1 = new FileWriter(f1, true);
					bw1 = new BufferedWriter(fw1);
					
					bw1.write(j+"\t"+ String.valueOf( (double) c0 / 562 ) + "\t" + String.valueOf( (double) c1 / 562 ) + "\t" + String.valueOf( (double) c01 / 562 ) + "\t"+ String.valueOf( (double) c20 / 562 ) + "\t" + String.valueOf( (double) c21 / 562 ) + "\n" );
					
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
	
	public static void crossThree(int foldLimit, int environmentLimit, int n, int m, int tuples) {
		
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
		
		int c0, c1, c01, c20, c21;
		
		ArrayList<String> testSet;
		
		System.out.println("-- Initializing experiments --\n");
		
		for(int i = 1; i <= foldLimit; i++) {
			
			System.out.println("Block " + i);
			
			f1 = new File("Input/OptDigits/10-fold/results/R3/Accuracy-fold"+i+"R3.txt");
			
			for (int j = 0; j < environmentLimit; j++) {
				
				WiSARD w0 = new WiSARD("w0", n, m, tuples);
				WiSARD w1 = new WiSARD("w1", n, m, tuples);
				WiSARD w01 = new WiSARD("w01", n, m, tuples);
				
				System.out.println("Environment " + j);
				
				f2 = new File("Input/OptDigits/10-fold/results/R3/fold"+i+"Env"+j+"MI.txt");
				f3 = new File("Input/OptDigits/10-fold/fold"+i+"/Env"+j+".txt");
				
				testSet = new ArrayList<String>();
				
				try {
					
					/*
					 * TRAINING PHASE
					 * */
					
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
					
					/*
					 * TESTING PHASE
					 * */
					
					c0 = 0;
					c1 = 0;
					c01 = 0;
					
					for (int k = 5058; k < 5620; k++) {
						
						testSet.add(br1.readLine().toString());
						
						splitter = testSet.get(k - 5058).split(",");
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
					
					/*
					 * MENTAL IMAGE GENERATION
					 * */
					
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
					
					/*
					 * MEMORY TRANSFER
					 * */
					
					w0.syntheticTrainingSet();
					w1.syntheticTrainingSet();
					
					for (int k = 0; k < 10; k++) {
						
						int[][] w0Knownledge = w0.getSyntheticTrainingSet().get(String.valueOf(k)),
								w1Knownledge = w1.getSyntheticTrainingSet().get(String.valueOf(k));
						
						StringBuilder prototype;
								
						for (int l = 0; l < w0Knownledge.length; l++) {
							
							prototype = new StringBuilder();
							
							for (int l2 = 0; l2 < w0Knownledge[0].length; l2++) {
								prototype.append(w0Knownledge[l][l2]);
							}
						
							w1.train(String.valueOf(k), prototype.toString());
						}
						
						for (int l = 0; l < w1Knownledge.length; l++) {
							
							prototype = new StringBuilder();
							
							for (int l2 = 0; l2 < w1Knownledge[0].length; l2++) {
								prototype.append(w1Knownledge[l][l2]);
							}
							
							w0.train(String.valueOf(k), prototype.toString());
						}
					}
					
					c20 = 0;
					c21 = 0;
					
					for (int k = 5058; k < 5620; k++) {
						
						splitter = testSet.get(k - 5058).split(",");
						example = new StringBuilder();
						
						for (int l = 1; l < splitter.length; l++) {
							
							example.append(splitter[l]);
						}
						
						label = w0.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c20++;
						}
						
						label = w1.test(example.toString());
						
						if(label.equals(splitter[0])) {
							
							c21++;
						}
					}
					
					fw1 = new FileWriter(f1, true);
					bw1 = new BufferedWriter(fw1);
					
					bw1.write(j+"\t"+ String.valueOf( (double) c0 / 562 ) + "\t" + String.valueOf( (double) c1 / 562 ) + "\t" + String.valueOf( (double) c01 / 562 ) + "\t"+ String.valueOf( (double) c20 / 562 ) + "\t" + String.valueOf( (double) c21 / 562 ) + "\n" );
					
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
	
	/* Other */
	
	public static void allCombinations(StringBuilder combination, int set1, int set2, int limit) {
		
		if(combination.length() == limit) {
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
	
	/* Image Processing */
	
	public static void imageAnalysis(int width, int height) {
	
		ArrayList<int[]> w0Images = new ArrayList<int[]>();
		ArrayList<int[]> w1Images = new ArrayList<int[]>();
		ArrayList<int[]> w01Images = new ArrayList<int[]>();
		
		/*
		 * READ FILE
		 * */
		
		File f = new File("Input/OptDigits/10-fold/results/R0/fold1Env0MI.txt");
		FileReader fr;
		BufferedReader br;
		
		try {
			fr = new FileReader(f);
			br = new BufferedReader(fr);			
			
			for (int i = 0; i < 10; i++) {
				
				String[] aux1 = br.readLine().split(",");
				
				int[] aux2 = new int[width*height];
				
				for (int j = 0; j < aux2.length; j++) {
					
					aux2[j] = Integer.valueOf(aux1[j+1]);
				}
				
				w0Images.add(aux2);
			}
			
			for (int i = 0; i < 10; i++) {
				
				String[] aux1 = br.readLine().split(",");
				
				int[] aux2 = new int[width*height];
				
				for (int j = 0; j < aux2.length; j++) {
					
					aux2[j] = Integer.valueOf(aux1[j+1]);
				}
				
				w1Images.add(aux2);
			}
	
			for (int i = 0; i < 10; i++) {
				
				String[] aux1 = br.readLine().split(",");
				
				int[] aux2 = new int[width*height];
				
				for (int j = 0; j < aux2.length; j++) {
					
					aux2[j] = Integer.valueOf(aux1[j+1]);
				}
				
				w01Images.add(aux2);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		 * PROJECT IMAGE
		 * */
		
		int[] image1 = w0Images.get(1);
		int[] image2 = w1Images.get(1);
		int[] image3 = new int[image1.length];
		int[] image4 = new int[image1.length];
		
		for (int i = 0; i < image3.length; i++) {
			
			image3[i] = image1[i] - image2[i];
		}
		
		for (int i = 0; i < image3.length; i++) {
			
			image4[i] = image2[i] - image1[i];
		}
		
		plot(width, height, image1, "w0", false);
		plot(width, height, image2, "w1", false);
		plot(width, height, image3, "w0 - w1", false);
		plot(width, height, image4, "w1 - w0", false);
	}
	
	public static void plot(int width, int height, int[] image, String title, boolean binarized) {
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		if(!binarized) {
			
			for (int x = 0; x < width; x++) {
			    for (int y = 0; y < height; y++)
			    {
			        int grayLevel = image[x*width+y];
			        
			        if(grayLevel > 255)
			        	
			        	grayLevel = 255;
			        
			        if(grayLevel < 0)
			        	
			        	grayLevel = 0;
			        
			        grayLevel = Math.abs(255 - grayLevel);
			        
			        int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
			        
			        img.setRGB( y, x, gray);
			    }
			}
		}
		
		else {
			
			for (int x = 0; x < width; x++) {
			    for (int y = 0; y < height; y++)
			    {
			        int grayLevel = image[x*width+y];
			        
			        if(grayLevel == 1)
			        	
			        	grayLevel = 255;
			        
			        grayLevel = Math.abs(255 - grayLevel);
			        
			        int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
			        
			        img.setRGB( y, x, gray);
			    }
			}
		}
		
		ImageIcon icon = new ImageIcon(img);
		
		Image newImg = icon.getImage();
		Image bigImg = newImg.getScaledInstance(10*img.getWidth(), 10*img.getHeight(),  java.awt.Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(bigImg);
				
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(10*img.getWidth()+50,10*img.getHeight()+50);
        JLabel lbl = new JLabel();
        lbl.setIcon(newIcon);
        frame.add(lbl);
        frame.setTitle(title);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        File f = new File("Input/OptDigits/10-fold/Images/" + title + ".png");
        
        try {
			ImageIO.write(img, "png", f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Computes the time spent
	public static long duration() {
		
		return  TimeUnit.NANOSECONDS.toMillis(endTime) - TimeUnit.NANOSECONDS.toMillis(startTime);
	}
}
