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
	
	private HashMap<String, Discriminator> mapa = new HashMap<String, Discriminator>();
	private HashMap<Integer, String> relacao1 = new HashMap<Integer, String>();
	private HashMap<String, Integer> relacao2 = new HashMap<String, Integer>();
	
	public WiSARD(String name, int height, int width, int tuples) {
		
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
	
	public void training(String label, String example ) {
		
		long value;
		long aux;
		int l;
		
		if(mapa.get(label) == null) {
			
			Discriminator discriminator = new Discriminator(getRams(), getTuples());
			discriminator.setId(label);
			
			mapa.put(label, discriminator);
			
			relacao1.put(relacao1.size(), label);
			relacao2.put(label, relacao2.size());
		}
		
		l = 0;
		
		for (int j = 0; j < mapa.get(label).getTuplas().size(); j = j + getTuples()) {
			
			value = 0;
			aux = (int) Math.pow(2, getTuples());
			
			for (int k = j; k < j + getTuples(); k++) {
				
				value += Integer.valueOf(example.charAt(mapa.get(label).getTuplas().get(k))) * aux;
				aux /= 2;
			}
			
			if(mapa.get(label).getRams().get(l).getMapa().get(value) == null)
			
				mapa.get(label).getRams().get(l).getMapa().put(value, 1);
			
			else
				
				mapa.get(label).getRams().get(l).getMapa().put(value, mapa.get(label).getRams().get(l).getMapa().get(value) + 1);
			
			l++;
		}
	}
	
	public String testing(String test) {
		
		String label = "";
		
		long value;
		long aux;
		int l;
		
		int[] similarity = new int[mapa.size()];
		
		Iterator<Entry<String, Discriminator>> iterador = mapa.entrySet().iterator();
		
		while(iterador.hasNext()) {
			
			Entry<String, Discriminator> elemento = iterador.next();
			
			l = 0;
			
			for (int j = 0; j < elemento.getValue().getTuplas().size(); j = j + getTuples()) {
				
				value = 0;
				aux = (int) Math.pow(2, getTuples());
				
				for (int k = j; k < j + getTuples(); k++) {
					
					value += Integer.valueOf(test.charAt(mapa.get(elemento.getKey()).getTuplas().get(k))) * aux;
					aux /= 2;
				}
				
				if(mapa.get(elemento.getKey()).getRams().get(l).getMapa().get(value) != null)
				
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
	
	public void mentalImage() {
		
	}
	
	@Override
	public String toString() {
		String presentation = "";
		
		presentation += "I'm " + getName() + ", a WiSARD instance.\n\n";
		
		presentation += "I'm supposed to see a " + getHeight() + " x " + getWidth() + " retina.\n\n";
		
		if(numberOfPatterns() == 0)
		
			presentation += "Currently, I recognize no patterns. I can't wait to start learning!\n\n";
		
		else
			
			presentation += "Currently, I recognize " + numberOfPatterns() + " patterns. described as follows:\n\n";
		
		return presentation;
	}
}
