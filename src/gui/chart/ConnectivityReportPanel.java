package gui.chart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import system.Mote;
import system.connectivity.MapKey;
import system.sympathy.util.SympathyConst;

public class ConnectivityReportPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JPanel view;
	
	public ConnectivityReportPanel() {
		this.setLayout(new BorderLayout());
		this.view = new JPanel(new GridLayout(1, 1));
		this.add(this.view, BorderLayout.CENTER);
	}
	
	public void addReport(ArrayList<Mote> motes, HashMap<MapKey, Double> report) {
		if(!report.isEmpty()) {
			HashMap<MapKey, Double> filteredReports = filterReports(motes, report);
			this.remove(this.view);
			
			if(!filteredReports.isEmpty()) {
				this.view = new JPanel(new GridLayout(filteredReports.size(), 1));
				
				for(MapKey mk : filteredReports.keySet()) {
					double ratio = report.get(mk)/(double)motes.size();
					if(ratio >= SympathyConst.K_EDGE) {
						JLabel label = new JLabel(mk.toString() + " -> affects " + filteredReports.get(mk) + " nodes");
						label.setOpaque(true);
						label.setBorder(BorderFactory.createLineBorder(Color.black));
						label.setBackground(Color.yellow);
						this.view.add(label);
					}
				}
			}
		}
		
		this.add(this.view, BorderLayout.CENTER);
		this.validate();
		this.repaint();
	}
	
	private HashMap<MapKey, Double> filterReports(ArrayList<Mote> motes, HashMap<MapKey, Double> report) {
		HashMap<MapKey, Double> filtered = new HashMap<MapKey, Double>();
		
		for(MapKey mk : report.keySet()) {
			MapKey mk2 = new MapKey(mk.getY(), mk.getX());
			if((filtered.get(mk) == null) && (filtered.get(mk2) == null)) {
				filtered.put(mk, report.get(mk));
			}
		}
		
		return filtered;
	}

}
