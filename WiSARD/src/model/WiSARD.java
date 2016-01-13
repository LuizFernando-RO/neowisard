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
	
	public void training(String label, String example ) {
		
		if(!getValid()) {
			
			System.out.println("I can't work on this configuration, sorry!");
			
			return;
		}
		
		StringBuilder value;
		long aux;
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
			
			if(mapa.get(label).getRams().get(l).getMapa().get(value) == null) 
			
				mapa.get(label).getRams().get(l).getMapa().put(value.toString(), 1);
			
			else
				
				mapa.get(label).getRams().get(l).getMapa().put(value.toString(), mapa.get(label).getRams().get(l).getMapa().get(value.toString()) + 1);
			
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
		long aux;
		int l;
		
		int[] similarity = new int[mapa.size()];
		
		Iterator<Entry<String, Discriminator>> iterador = mapa.entrySet().iterator();
		
		while(iterador.hasNext()) {
			
			Entry<String, Discriminator> elemento = iterador.next();
			
			l = 0;
			
			for (int j = 0; j < elemento.getValue().getTuplas().size(); j = j + getTuples()) {
				
				value = new StringBuilder();
				
				for (int k = j; k < j + getTuples(); k++)
					
					value.append(test.charAt(mapa.get(elemento.getKey()).getTuplas().get(k)));
				
				if(mapa.get(elemento.getKey()).getRams().get(l).getMapa().get(value.toString()) != null)
				
					similarity[relacao2.get(elemento.getKey())]++;
				
				l++;
			}
		}
		
		int maxIndex = 0;
		
		for (int j = 1; j < similarity.length; j++) {
			
			
			if(similarity[j] > similarity[maxIndex]) {
				
				maxIndex = j;
			}
		}
		
		label = relacao1.get(maxIndex);
		
		return label;
	}
	
	public void mentalImage(String label) {
		
		if(mapa.get(label) == null) {
			
			System.out.println("Sorry, but I don't know what you are talking about...");
			
			return;
		}
		
		System.out.println("This is what I understand of a '" + label + "':\n\n");
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
