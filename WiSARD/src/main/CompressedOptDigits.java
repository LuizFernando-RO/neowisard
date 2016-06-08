package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import model.Discriminator;
import model.WiSARD;

public class CompressedOptDigits {

	public static long startTime;
	public static long endTime;
	
	public static int index = 0;
	public static ArrayList<String> combinations = new ArrayList<String>();
	
	public static void main(String[] args) {
		
		startTime = System.nanoTime();
		
		//binarize();
		
		//run();
		
		int foldLimit = 10,
			envLimit = 20,
			n = 8,
			m = 8,
			tuples = 16;
		
		crossZero(foldLimit,envLimit, n, m, tuples);
		
		crossOne(foldLimit,envLimit, n, m, tuples);
		
		crossTwo(foldLimit,envLimit, n, m, tuples);
		
		crossThree(foldLimit,envLimit, n, m, tuples);
		
		endTime = System.nanoTime();
		
		System.out.println("\n-- Execution time: " + duration() + "ms --");
	}
	
	public static void binarize() {
		
		int[] vector = {0,8,16,16,16,16,16,16,5,16,16,16,16,16,16,15,5,16,16,16,16,16,16,8,1,16,16,16,16,16,16,2,1,15,16,16,16,16,14,0,7,16,16,16,16,16,16,6,10,16,16,16,16,16,16,13,1,10,16,16,16,16,16,16};
		
		int foldLimit = 10, trainLimit = 5058, testLimit = 562;
		
		for (int i = 1; i <= foldLimit; i++) {
			
			String trainName = "Input/CompressedOptDigits/10-fold/fold"+i+"/optdigits-10-"+i+"tra.dat", 
				   testName = "Input/CompressedOptDigits/10-fold/fold"+i+"/optdigits-10-"+i+"tst.dat",
				   outTrainName = "Input/CompressedOptDigits/10-fold/fold"+i+"/optdigits-10-"+i+"trabin.dat",
				   outTestName = "Input/CompressedOptDigits/10-fold/fold"+i+"/optdigits-10-"+i+"tstbin.dat";
			
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
							
							example.append(",1");
						
						else
							
							example.append(",0");
					}
					
					bw.write(splitter[splitter.length - 1].trim() + example + "\n");
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
							
							example.append(",1");
						
						else
							
							example.append(",0");
					}
					
					bw.write(splitter[splitter.length - 1].trim() + example + "\n");
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
		
		String fileNamePrefix ="Input/CompressedOptDigits/10-fold/fold",
			   fileNameTrainingSufix = "trabin.dat", fileNameTestingSufix = "tstbin.dat";
		
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
					
					file = new File("Input/CompressedOptDigits/10-fold/fold" + i + "/Env" + j + ".txt");
					
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
		
		ArrayList<String> testSet;
		
		int c0, c1, c01, c20, c21;
		
		System.out.println("-- Initializing experiments --\n");
		
		for(int i = 1; i <= foldLimit; i++) {
			
			f1 = new File("Input/CompressedOptDigits/10-fold/results/R0/Accuracy-fold"+i+"R0.txt");
			
			for (int j = 0; j < environmentLimit; j++) {
				
				w0.clear();
				w1.clear();
				w01.clear();
				
				System.out.println("Environment " + j);
				
				f2 = new File("Input/CompressedOptDigits/10-fold/results/R0/fold"+i+"Env"+j+"MI.txt");
				f3 = new File("Input/CompressedOptDigits/10-fold/fold"+i+"/Env"+j+".txt");
				
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
				
				f2 = new File("Input/CompressedOptDigits/10-fold/results/R1/fold"+i+"Env"+j+"MI.txt");
				f3 = new File("Input/CompressedOptDigits/10-fold/fold"+i+"/Env"+j+".txt");
				
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
			
			f1 = new File("Input/CompressedOptDigits/10-fold/results/R2/Accuracy-fold"+i+"R2.txt");
			
			for (int j = 0; j < environmentLimit; j++) {
				
				w0.clear();
				w1.clear();
				w01.clear();
				
				System.out.println("Environment " + j);
				
				f2 = new File("Input/CompressedOptDigits/10-fold/results/R2/fold"+i+"Env"+j+"MI.txt");
				f3 = new File("Input/CompressedOptDigits/10-fold/fold"+i+"/Env"+j+".txt");
				
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
			
			f1 = new File("Input/CompressedOptDigits/10-fold/results/R3/Accuracy-fold"+i+"R3.txt");
			
			for (int j = 0; j < environmentLimit; j++) {
				
				WiSARD w0 = new WiSARD("w0", n, m, tuples);
				WiSARD w1 = new WiSARD("w1", n, m, tuples);
				WiSARD w01 = new WiSARD("w01", n, m, tuples);
				
				System.out.println("Environment " + j);
				
				f2 = new File("Input/CompressedOptDigits/10-fold/results/R3/fold"+i+"Env"+j+"MI.txt");
				f3 = new File("Input/CompressedOptDigits/10-fold/fold"+i+"/Env"+j+".txt");
				
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
