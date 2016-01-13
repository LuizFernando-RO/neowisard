package model;

import java.util.HashMap;

public class RAM {
	
	private int nBits;
	private HashMap<String, Integer> mapa;
	
	public RAM() {
		this.mapa = new HashMap<String, Integer>();
	}
	
	public RAM(int m) {
		this.nBits = m;
		this.mapa = new HashMap<String, Integer>();
	}

	public int getnBits() {
		return nBits;
	}

	public void setnBits(int nBits) {
		this.nBits = nBits;
	}

	public RAM(HashMap<String, Integer> mapa) {
		this.mapa = mapa;
	}
	
	public HashMap<String, Integer> getMapa() {
		return mapa;
	}

	public void setMapa(HashMap<String, Integer> mapa) {
		this.mapa = mapa;
	}
}
