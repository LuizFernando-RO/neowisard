package model;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class WiSARD {
	
	private String name;
	
	private int height;
	private int width;
	
	private int tuples;
	private int rams;
	
	// Maximum bleaching is given by the highest value stored into some RAM slot
	private int maxBleaching;
	
	// Mapping between the pattern and it's discriminator
	private HashMap<String, Discriminator> map;
	
	private HashMap<String, int[]> mentalImage;
	
	// Mapping between the learned label and it's order of presentation
	private HashMap<Integer, String> rel1 = new HashMap<Integer, String>();
	private HashMap<String, Integer> rel2 = new HashMap<String, Integer>();
	
	// Checks for valid WiSARD configuration
	boolean valid;
	
	//Constructors
	
	public WiSARD(String name, int height, int width, int tuples) {
		
		if((height * width % tuples) != 0 ) {
			
			this.valid = false;
			
			System.out.println("This is not a valid configuration of retina x tuple!");	
		}
		
		else {
			
			this.valid = true;
		
			this.map = new HashMap<String, Discriminator>();
			this.mentalImage = new HashMap<String, int[]>();
			this.rel1 = new HashMap<Integer, String>();
			this.rel2 = new HashMap<String, Integer>();
		
			this.name = name;
			this.height = height;
			this.width = width;
			this.tuples = tuples;
			this.rams = height * width / tuples;
		}
	}
	
	// Getters and Setters
	
	public String getName() {
		return this.name;
	}
	
	public int getTuples() {
		return this.tuples;
	}
	
	public int getRams() {
		return this.rams;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public boolean getValid() {
		return this.valid;
	}
	
	public void setMaxBleaching(int maxBleaching) {
		
		this.maxBleaching = maxBleaching;
	}
	
	public int getMaxBleaching() {
		return this.maxBleaching;
	}

	public HashMap<String, int[]> getMentalImage() {
		return this.mentalImage;
	}
	
	// Domain
	
	public void training(String label, String example ) {
		
		if(!getValid()) {
			
			System.out.println("I can't work on this configuration, sorry!");
			
			return;
		}
		
		StringBuilder value;
		int l;
		
		if(map.get(label) == null) {
			
			System.out.println("New pattern: '" + label + "' learned");
			
			Discriminator discriminator = new Discriminator(getRams(), getTuples());
			discriminator.setId(label);
			
			map.put(label, discriminator);
			
			rel1.put(rel1.size(), label);
			rel2.put(label, rel2.size());
		}
		
		l = 0;
		
		for (int j = 0; j < map.get(label).getTuplas().size(); j = j + getTuples()) {
			
			value = new StringBuilder();
			
			for (int k = j; k < j + getTuples(); k++)
				
				value.append(example.charAt(map.get(label).getTuplas().get(k))) ;
			
			if(map.get(label).getRams().get(l).getMapa().get(value.toString()) == null) 
			
				map.get(label).getRams().get(l).getMapa().put(value.toString(), 1);
			
			else {
				
				map.get(label).getRams().get(l).getMapa().put(value.toString(), map.get(label).getRams().get(l).getMapa().get(value.toString()) + 1);
			
				if(map.get(label).getRams().get(l).getMapa().get(value.toString()) > getMaxBleaching()) {
					
					setMaxBleaching(map.get(label).getRams().get(l).getMapa().get(value.toString()));
				}
			}
			
			l++;
		}
	}
	
	public String testing(String test) {
		
		if(!getValid()) {
			
			System.out.println("I can't work on this configuration, sorry!");
			
			return "";
		}
		
		String label = "";
		
		StringBuilder value;
		int l;
		int maxIndex = 0;
		
		int b = 0;
		
		boolean draw = true;
		
		while(draw && b < getMaxBleaching()) {
		
			int[] similarity = new int[map.size()];
		
			Iterator<Entry<String, Discriminator>> iterador = map.entrySet().iterator();
		
			while(iterador.hasNext()) {
			
				Entry<String, Discriminator> elemento = iterador.next();
			
				l = 0;
			
				for (int j = 0; j < elemento.getValue().getTuplas().size(); j = j + getTuples()) {
				
					value = new StringBuilder();
				
					for (int k = j; k < j + getTuples(); k++)
					
						value.append(test.charAt(map.get(elemento.getKey()).getTuplas().get(k)));
				
					if(map.get(elemento.getKey()).getRams().get(l).getMapa().get(value.toString()) != null ) {
						
						
						
						if(map.get(elemento.getKey()).getRams().get(l).getMapa().get(value.toString()) > b)
							
							similarity[rel2.get(elemento.getKey())]++;
				
					}
					
					l++;
				}
			}
		
			maxIndex = 0;
			int maxValue = similarity[0];
			int occurrences = 0;
		
			for (int j = 1; j < similarity.length; j++) {
			
				if(similarity[j] > similarity[maxIndex]) {
				
					maxIndex = j;
					maxValue = similarity[j];
				
					occurrences = 0;
				}
			
				if(similarity[j] == maxValue)
				
					occurrences++;
			}
		
			if(occurrences > 1) 
			
				b++;
				
			else
			
				draw = false;
		}
		
		label = rel1.get(maxIndex);
		
		return label;
	}
	
	public void generateMentalImages() {
		
		String label;
		
		Iterator<Entry<String, Integer>> it1 = rel2.entrySet().iterator();
		
		while(it1.hasNext()) {
		
			Entry<String, Integer> el1 = it1.next();
			
			label = el1.getKey();
			
			int[] image = new int[getHeight() * getWidth()];
			
			Discriminator d = map.get(label);
			RAM r;
			
			for(int i = 0; i < d.getnRams(); i++) {
				
				r = d.getRams().get(i);
				
				Iterator<Entry<String, Integer>> iterator = r.getMapa().entrySet().iterator();
				
				while(iterator.hasNext()) {
					
					Entry<String, Integer> element = iterator.next();
					
					for(int j = 0; j < getTuples(); j++ ) 
						
						image[d.getTuplas().get(i*getTuples()+j)] += Integer.valueOf(element.getKey().charAt(j) - '0') * element.getValue();
					
				}			
			}
			
			getMentalImage().put(label, image);
		}
	}
	
	public void mentalImage(String label) {
		
		if(map.get(label) == null) {
			
			System.out.println("Sorry, but I don't know what you are talking about...");
			
			return;
		}
		
		int[] image = getMentalImage().get(label);
		
		//System.out.println("This is what I understand of a '" + label + "':\n\n");
		
		//int acc = 0;
		int maxValue = -1, minValue = getMaxBleaching();
		
		for(int i = 0; i < getWidth(); i++) {
			
			for (int j = 0; j < getHeight(); j++) {
				
				//acc += image[i*getWidth()+j];
				
				if(image[i*getWidth()+j] > maxValue)
					
					maxValue = image[i*getWidth()+j];
				
				if(image[i*getWidth()+j] < minValue)
					
					minValue = image[i*getWidth()+j];
				
				//System.out.print(image[i*getWidth()+j] + "\t");
			}
			
			//System.out.println();
		}
		
		//System.out.println("The mean is " + (acc / (getWidth()*getHeight())) + ". Now I'm gonna use this as threshold for you to recognize it..." );
		
		/*
		for(int i = 0; i < getWidth(); i++) {
			
			for (int j = 0; j < getHeight(); j++) {
				
				if(image[i*getWidth()+j] < (acc / (getWidth()*getHeight())) )
				
					System.out.print("0");
				
				else
					
					System.out.print("1");
			}
			
			System.out.println();
		}
		*/
		
		BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		
		for (int x = 0; x < img.getWidth(); x++) {
		    for (int y = 0; y < img.getHeight(); y++)
		    {
		        int grayLevel = (int) ( 0 + ( 255 * ( (double) (image[x*getWidth()+y] - minValue) / (maxValue - minValue) ) ) );
		        
		        int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
		        img.setRGB( y, x, gray);
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
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
		
	}
	
	public void syntheticTrainingSet() {
		
	}
	
	public int getNumberOfPatterns() {
		return this.map.size();
	}
	
	@Override
	public String toString() {
		String presentation = "";
		
		presentation += "I'm " + getName() + ", a WiSARD instance.\n\n";
		
		presentation += "I'm supposed to see a " + getHeight() + " x " + getWidth() + " retina.\n\n";
		
		if(getNumberOfPatterns() == 0)
		
			presentation += "Currently, I recognize no patterns. I can't wait to start learning!\n\n";
		
		else if(getNumberOfPatterns() == 1)
			
			presentation += "Currently, I recognize " + getNumberOfPatterns() + " pattern, described as follows:\n\n";
		
		else
			
			presentation += "Currently, I recognize " + getNumberOfPatterns() + " patterns, described as follows:\n\n";
		
		Iterator<Entry<String, Discriminator>> iterador = map.entrySet().iterator();
		
		while(iterador.hasNext()) {
			
			Entry<String, Discriminator> elemento = iterador.next();
			
			presentation += elemento.getKey() + " ";	
		}
		
		presentation += "\n";
		
		return presentation;
	}
}
