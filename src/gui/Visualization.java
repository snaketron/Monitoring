package gui;

import gui.chart.ChartTabbedPanel;
import gui.chart.ConnectivityReportPanel;
import gui.chart.NetworkTopology;
import gui.chart.Summary;
import gui.chart.SympathyNetworkTopology;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import msg.DataMsg;
import system.Mote;
import system.connectivity.MapKey;
import system.sympathy.util.SympathyReport;
import utils.event.IDataEvent;
import utils.event.IMoteEvent;
import utils.event.ISystemEvent;

/**
 * The visualization panels displays all the panels which represent data. It also relays 
 * events heard by the Dashboard panel to the appropriate data panels.
 *
 */
public class Visualization extends JPanel implements IDataEvent, ISystemEvent, IMoteEvent {
	private static final long serialVersionUID = 1L;
	private JPanel southPanel; //place-holder
	private JPanel chartPanel; //measurements visualization panel (charts)
	private JPanel topologyPanel; //network visualization
	private JTabbedPane sympathyTopologyPanel; //sympathy network visualization
	private JPanel summaryPanel; //system properties pie-chart
	
	private ChartTabbedPanel ctp;
	private Summary summary;
	private ConnectivityReportPanel crp;
	private NetworkTopology nt;
	private SympathyNetworkTopology snt;
	
	public Visualization() {
		this.setLayout(new GridLayout(2, 1));
		
		createChartPanel();
		createTopologyPanel();
		createSummaryPanel();
		createSouthPanel();
		
		this.add(chartPanel);
		this.add(southPanel);
		
		this.validate();
		this.repaint();
	}

	private void createSouthPanel() {
		southPanel = new JPanel(new GridLayout(1, 3));
		southPanel.add(topologyPanel);
		southPanel.add(sympathyTopologyPanel);
		southPanel.add(summaryPanel);
	}
	
	private void createChartPanel() {
		chartPanel = new JPanel(new BorderLayout());
		chartPanel.setBorder(BorderFactory.createTitledBorder("Data Visualization"));
	}
	
	private void createTopologyPanel() {
		topologyPanel = new JPanel(new BorderLayout());
		topologyPanel.setBorder(BorderFactory.createTitledBorder("Network Topology"));
		nt = new NetworkTopology();
		topologyPanel.add(nt, BorderLayout.CENTER);
		
		sympathyTopologyPanel = new JTabbedPane();
		sympathyTopologyPanel.setBorder(BorderFactory.createTitledBorder("Detection Network Topology"));
		snt = new SympathyNetworkTopology();
		crp = new ConnectivityReportPanel();
		sympathyTopologyPanel.add("topology", snt);
		sympathyTopologyPanel.add("connectivity", crp);
	}
	
	private void createSummaryPanel() {
		summaryPanel = new JPanel(new BorderLayout());
		summaryPanel.setBorder(BorderFactory.createTitledBorder("System Summary"));
	}

	@Override
	public void pushDataMessage(DataMsg dm) {
		if(ctp != null) {
			ctp.enterDataToChart(dm);
		}
	}

	@Override
	public void pushMoteStats(Mote mote) {
		if(summary != null) {
			summary.enterData(mote);
		}
	}
	
	@Override
	public void startVisualization(ArrayList<Mote> motes) {
		ctp = new ChartTabbedPanel(motes);
		summary = new Summary(motes);
		chartPanel.add(ctp, BorderLayout.CENTER);
		summaryPanel.add(summary, BorderLayout.CENTER);
		this.validate();
		this.repaint();
	}

	@Override
	public void stopVisualization() {
		chartPanel.remove(ctp);
		summaryPanel.remove(summary);
		this.validate();
		this.repaint();
	}


	@Override
	public void addReport(HashMap<Integer, SympathyReport> reportMap) {
		if(nt != null && snt != null) {
			snt.updateReports(reportMap);
			this.validate();
			this.repaint();
		}
	}

	
	@Override
	public void updateMotes(HashMap<Integer, Mote> motesMap) {
		if(nt != null && snt != null) {
			nt.updateMotes(motesMap);
			snt.updateMotes(motesMap);
			this.validate();
			this.repaint();
		}
	}

	@Override
	public void addEdgeConnectivityReport(ArrayList<Mote> motes, HashMap<MapKey, Double> report) {
		if(crp != null) {
			crp.addReport(motes, report);
			this.validate();
			this.repaint();
		}
	}
}