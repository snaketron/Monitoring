package system.connectivity;

import java.util.ArrayList;
import java.util.HashMap;

import system.Mote;
import utils.GlobalConst;

public class MatrixOps {
	
	public static double[][] getAdjMatrix(ArrayList<Mote> motes) {
		int size = getMaxMoteId(motes);
		double [][] matrix = new double[size][size];
		for(Mote m : motes) {
			for(Integer neighbor : m.getNeighbors()) {
				matrix[neighbor][m.getId()] += 0.5;
				matrix[m.getId()][neighbor] += 0.5;
			}
		}
		return clearMatrix(matrix);
	}
	
	
	private static double [][] clearMatrix(double [][] m) {
		for(int x = 0; x < m.length; x++) {
			for(int y = 0; y < m.length; y++) {
				if(m[x][y] == 0.5) {
					m[x][y] = 0;
				}
			}
		}
		return m;
	}


	private static int getMaxMoteId(ArrayList<Mote> motes) {
		int max = 0;
		for(Mote m : motes) {
			if(m.getId() > max) {
				max = m.getId();
			}
		}
		return max + 1;
	}
	
	/**
	 * A function that checks whether the network is partitioned or not.
	 * Partition => FALSE, else => TRUE.
	 */
	private static boolean graphIsConnected(ArrayList<Mote> motes) {
		double [][] mat = getAdjMatrix(motes);
		
		if(motes.isEmpty()) {
			return false;
		}
		
		if(mat.length == 0) {
			return false;
		}
		
		for(Mote m : motes) {
			if(!checkConnectivity(m.getId(), mat, new ArrayList<Integer>())) {
				return false;
			}
		}
		
		return true;
	}


	/**
	 * A recursive function that returns TRUE if  there exists a connection 
	 * between an initial node and the sink node. Else FALSE.
	 **/
	private static boolean checkConnectivity(int curId, double [][] m, 
			ArrayList<Integer> hist) {
		
		if(curId == GlobalConst.SINK_ID) {
			return true;
		}

		for(int i = 1; i < m.length; i++) {
			if(m[curId][i] == 1) {
				if(!hist.contains(i)) {
					hist.add(i);
					if(checkConnectivity(i, m, hist)) {
						return true;
					}
				}
			}
		}

		return false;
	}
	

	public static void displayMatrix(double [][] matrix) {
		for(int x = 1; x < matrix.length; x++) {
			StringBuffer sb = new StringBuffer();
			for(int y = 1; y < matrix.length; y++) {
				sb.append(matrix[x][y] + " , ");
			}
			System.out.println(sb);
		}
		System.out.println();
	}
	
	
	private static double[][] clearRowAndCol(double [][] m, int id) {
		for(int i = 0; i < m.length; i++) {
			m[id][i] = 0;
			m[i][id] = 0;
		}
		return m;
	}
	
	/**Not used anymore**/
	@Deprecated
	public static void testVertexConnectivity(ArrayList<Mote> motes) {
		double [][] m = MatrixOps.getAdjMatrix(motes);
		MatrixOps.displayMatrix(m);
		
		System.out.println(MatrixOps.graphIsConnected(motes));
		
		if(MatrixOps.graphIsConnected(motes)) {
			for(Mote mote : motes) {
				m = MatrixOps.getAdjMatrix(motes);
				m = MatrixOps.clearRowAndCol(m, mote.getId());
				StringBuffer sb = new StringBuffer();
				sb.append("connectivity when : " + mote.getId() + " is removed \n"); 
				
				int count = 0;
				for(Mote inMote : motes) {
					if(!MatrixOps.checkConnectivity(inMote.getId(), m, new ArrayList<Integer>())) {
						count++;
					}
					sb.append(inMote.getId() + " : " + MatrixOps.checkConnectivity(inMote.getId(), m, new ArrayList<Integer>()) + "\n"); 
				}
				System.out.println(sb + " count: " + count);
				System.out.println();
			}
		}
	}
	
	
	public static HashMap<MapKey, Double> testEdgeConnectivity(ArrayList<Mote> motes) {
		HashMap<MapKey, Double> disconnectionMap = new HashMap<MapKey, Double>();
		double [][] m = MatrixOps.getAdjMatrix(motes);
		
		for(int x = 0; x < m.length; x++) {
			for(int y = 0; y < m.length; y++) {
				if(m[x][y] == 1) {
					m[x][y] = 0;
					m[y][x] = 0;

					double count = 0;
					for(Mote inMote : motes) {
						ArrayList<Integer> array = new ArrayList<Integer>();
						array.add(inMote.getId());
						if(!MatrixOps.checkConnectivity(inMote.getId(), m, array)) {
							count++;
						}
					}
					m[x][y] = 1;
					m[y][x] = 1;
					disconnectionMap.put(new MapKey(x, y), count);
				}
			}
		}
		
		return disconnectionMap;
	}
}