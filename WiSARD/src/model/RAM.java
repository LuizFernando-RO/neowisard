package model;

import java.util.HashMap;

public class RAM {
	
	private int nBits;
	private HashMap<Long, Integer> mapa;
	
	public RAM() {
		this.mapa = new HashMap<Long, Integer>();
	}
	
	public RAM(int m) {
		this.nBits = m;
		this.mapa = new HashMap<Long, Integer>();
	}

	public int getnBits() {
		return nBits;
	}

	public void setnBits(int nBits) {
		this.nBits = nBits;
	}

	public RAM(HashMap<Long, Integer> mapa) {
		this.mapa = mapa;
	}
	
	public HashMap<Long, Integer> getMapa() {
		return mapa;
	}

	public void setMapa(HashMap<Long, Integer> mapa) {
		this.mapa = mapa;
	}
}
