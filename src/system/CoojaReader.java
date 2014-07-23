package system;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import system.sympathy.util.SympathyReport;

public class CoojaReader {
	private BufferedReader dataReader;
	private BufferedReader timeReader;
	private final static String DATA_PATH = "cooja/file.txt";
	private final static String TIME_PATH = "cooja/time.txt";
	private final static String RESULT_PATH = "cooja/output.csv";
	
	public boolean connectToFile() {
		try {
			this.dataReader = new BufferedReader(new FileReader(DATA_PATH));
			this.timeReader = new BufferedReader(new FileReader(TIME_PATH));
			return true;
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void exportReports(HashMap<Integer, ArrayList<SympathyReport>> collectedReports) {
		try {
			PrintWriter pf = new PrintWriter(new FileWriter(RESULT_PATH));
			for(Integer x : collectedReports.keySet()) {
				for(SympathyReport sr : collectedReports.get(x)) {
					for(String failure : sr.getFailures()) {
						pf.println("failure;" + x + ";" + failure);
					}
					
					for(String failureSuggestion : sr.getFailureSuggestions()) {
						pf.println("failure suggestion;" + x + ";" + failureSuggestion);
					}
					
					for(String individualSuggestion : sr.getIndividualSuggestions()) {
						pf.println("individual suggestion;" + x + ";" + individualSuggestion);
					}
				}
			}
			pf.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BufferedReader getDataReader() {
		return dataReader;
	}

	public void setDataReader(BufferedReader dataReader) {
		this.dataReader = dataReader;
	}

	public BufferedReader getTimeReader() {
		return timeReader;
	}

	public void setTimeReader(BufferedReader timeReader) {
		this.timeReader = timeReader;
	}
}
