package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class WiSARD {
	
	private String name;
	
	private int height;
	private int width;
	
	private int tuples;
	private int rams;
	
	private int maxBleaching;
	
	private HashMap<String, Discriminator> mapa;
	private HashMap<Integer, String> relacao1 = new HashMap<Integer, String>();
	private HashMap<String, Integer> relacao2 = new HashMap<String, Integer>();
	
	boolean valid;
	
	public WiSARD(String name, int height, int width, int tuples) {
		
		if((height * width % tuples) != 0 ) {
			
			this.valid = false;
			
			System.out.println("This is not a valid configuration of retina x tuple!");	
		}
		
		else {
			
			this.valid = true;
		}
		
		this.mapa = new HashMap<String, Discriminator>();
		this.relacao1 = new HashMap<Integer, String>();
		this.relacao2 = new HashMap<String, Integer>();
		
		this.name = name;
		this.height = height;
		this.width = width;
		this.tuples = tuples;
		this.rams = height * width / tuples;
	}
	
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
	
	public int numberOfPatterns() {
		return this.mapa.size();
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
	
	public void training(String label, String example ) {
		
		if(!getValid()) {
			
			System.out.println("I can't work on this configuration, sorry!");
			
			return;
		}
		
		StringBuilder value;
		int l;
		
		if(mapa.get(label) == null) {
			
			System.out.println("New pattern: '" + label + "' learned");
			
			Discriminator discriminator = new Discriminator(getRams(), getTuples());
			discriminator.setId(label);
			
			mapa.put(label, discriminator);
			
			relacao1.put(relacao1.size(), label);
			relacao2.put(label, relacao2.size());
		}
		
		l = 0;
		
		for (int j = 0; j < mapa.get(label).getTuplas().size(); j = j + getTuples()) {
			
			value = new StringBuilder();
			
			for (int k = j; k < j + getTuples(); k++)
				
				value.append(example.charAt(mapa.get(label).getTuplas().get(k))) ;
			
			if(mapa.get(label).getRams().get(l).getMapa().get(value.toString()) == null) 
			
				mapa.get(label).getRams().get(l).getMapa().put(value.toString(), 1);
			
			else {
				
				mapa.get(label).getRams().get(l).getMapa().put(value.toString(), mapa.get(label).getRams().get(l).getMapa().get(value.toString()) + 1);
			
				if(mapa.get(label).getRams().get(l).getMapa().get(value.toString()) > getMaxBleaching()) {
					
					setMaxBleaching(mapa.get(label).getRams().get(l).getMapa().get(value.toString()));
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
		
			int[] similarity = new int[mapa.size()];
		
			Iterator<Entry<String, Discriminator>> iterador = mapa.entrySet().iterator();
		
			while(iterador.hasNext()) {
			
				Entry<String, Discriminator> elemento = iterador.next();
			
				l = 0;
			
				for (int j = 0; j < elemento.getValue().getTuplas().size(); j = j + getTuples()) {
				
					value = new StringBuilder();
				
					for (int k = j; k < j + getTuples(); k++)
					
						value.append(test.charAt(mapa.get(elemento.getKey()).getTuplas().get(k)));
				
					if(mapa.get(elemento.getKey()).getRams().get(l).getMapa().get(value.toString()) != null ) {
						
						
						
						if(mapa.get(elemento.getKey()).getRams().get(l).getMapa().get(value.toString()) > b)
							
							similarity[relacao2.get(elemento.getKey())]++;
				
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
		
		label = relacao1.get(maxIndex);
		
		return label;
	}
	
	public void mentalImage(String label) {
		
		if(mapa.get(label) == null) {
			
			System.out.println("Sorry, but I don't know what you are talking about...");
			
			return;
		}
		
		int[] image = new int[getHeight() * getWidth()];
		
		Discriminator d = mapa.get(label);
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
		
		System.out.println("This is what I understand of a '" + label + "':\n\n");
		
		int acc = 0;
		
		for(int i = 0; i < getWidth(); i++) {
			
			for (int j = 0; j < getHeight(); j++) {
				
				acc += image[i*getWidth()+j];
				
				System.out.print(image[i*getWidth()+j] + "\t");
			}
			
			System.out.println();
		}
		
		System.out.println("The mean is " + (acc / (getWidth()*getHeight())) + ". Now I'm gonna use this as threshold for you to recognize it..." );
		
		for(int i = 0; i < getWidth(); i++) {
			
			for (int j = 0; j < getHeight(); j++) {
				
				if(image[i*getWidth()+j] < (acc / (getWidth()*getHeight())) )
				
					System.out.print("0");
				
				else
					
					System.out.print("1");
			}
			
			System.out.println();
		}
	}
	
	@Override
	public String toString() {
		String presentation = "";
		
		presentation += "I'm " + getName() + ", a WiSARD instance.\n\n";
		
		presentation += "I'm supposed to see a " + getHeight() + " x " + getWidth() + " retina.\n\n";
		
		if(numberOfPatterns() == 0)
		
			presentation += "Currently, I recognize no patterns. I can't wait to start learning!\n\n";
		
		else if(numberOfPatterns() == 1)
			
			presentation += "Currently, I recognize " + numberOfPatterns() + " pattern, described as follows:\n\n";
		
		else
			
			presentation += "Currently, I recognize " + numberOfPatterns() + " patterns, described as follows:\n\n";
		
		Iterator<Entry<String, Discriminator>> iterador = mapa.entrySet().iterator();
		
		while(iterador.hasNext()) {
			
			Entry<String, Discriminator> elemento = iterador.next();
			
			presentation += elemento.getKey() + " ";	
		}
		
		presentation += "\n";
		
		return presentation;
	}
}
