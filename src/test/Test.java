package test;

import java.util.ArrayList;

import system.Mote;

public class Test {

	public static void main(String[] args) {
		
		Mote m1 = new Mote(1);
		Mote m2 = new Mote(2);
		Mote m3 = new Mote(3);
		Mote m4 = new Mote(4);
		
		m1.getNeighbors().add(m2.getId());
		m1.getNeighbors().add(m3.getId());

		m2.getNeighbors().add(m1.getId());
		m2.getNeighbors().add(m4.getId());

		m3.getNeighbors().add(m4.getId());
		
		
		ArrayList<Mote> motes = new ArrayList<Mote>();
		motes.add(m1);
		motes.add(m2);
		motes.add(m3);
		motes.add(m4);
		
//		testVertexConnectivity(motes);
//		testEdgeConnectivity(motes);
	}
}
