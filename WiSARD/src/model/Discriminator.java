package model;

import java.util.ArrayList;
import java.util.Random;

public class Discriminator {

	private String id;
	private int nRams;
	private ArrayList<Integer> tuplas;
	private ArrayList<RAM> rams;
	
	public Discriminator() {
		
	}
	
	public Discriminator(int n, int m) {
		
		this.nRams = n;
		inicializarTupla(n * m);
		inicializarRams(n, m);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getnRams() {
		return nRams;
	}

	public void setnRams(int nRams) {
		this.nRams = nRams;
	}

	public ArrayList<Integer> getTuplas() {
		return tuplas;
	}

	public void setTuplas(ArrayList<Integer> tuplas) {
		this.tuplas = tuplas;
	}

	public ArrayList<RAM> getRams() {
		return rams;
	}

	public void setRams(ArrayList<RAM> rams) {
		this.rams = rams;
	}
	
	private void inicializarTupla(int tamanho) {
		
		this.tuplas = new ArrayList<Integer>();
		
		int[] array = new int[tamanho];
		int posicao, aux;
		
		Random rand = new Random();

		for (int i = 0; i < array.length; i++) {
			
			array[i] = i;	
		}
		
		for (int i = 0; i < array.length; i++) {
			
			posicao = rand.nextInt(tamanho);
			
			aux = array[i];
			array[i] = array[posicao];
			array[posicao] = aux;
		}
		
		for (int i = 0; i < array.length; i++) {
			
			this.tuplas.add(array[i]);
		}
	}
	
	private void inicializarRams(int n, int m) {
		
		this.rams = new ArrayList<RAM>();
		
		for (int i = 0; i < n; i++) {
			
			RAM r = new RAM(m);
			
			this.rams.add(r);
		}
	}
	
	@Override
	public String toString(){
		
		return "Sou o discriminador responsavel pelo " + this.id;
	}
}