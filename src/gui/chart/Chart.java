package gui.chart;

import java.util.ArrayList;
import java.util.HashMap;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import system.Mote;
import utils.DataParamType;

/**
 * The class Chart represents a scatter plot in which the data values of a given phenomenon 
 * (temperature, humidity, etc) are displayed versus. their timestamps. It is interactively 
 * updated with the arrival of the new data messages.
 */
public class Chart {
	private DataParamType pt;
	private ArrayList<Mote> motes;
	private JFreeChart jfc;
	private TimeSeriesCollection dataset;
	private HashMap<Mote, TimeSeries> seriesMap;
	
	public Chart(DataParamType pt, ArrayList<Mote> motes) {
		super();
		this.pt = pt;
		this.motes = motes;
		this.seriesMap = new HashMap<Mote, TimeSeries>();
		setupDataset();
		this.jfc = ChartCreatorRoutine.createJFreeChart(pt, dataset);
	}
	
	private void setupDataset() {
		this.dataset = new TimeSeriesCollection();
		if(motes != null) {
			for(Mote m : motes) {
				TimeSeries serie = new TimeSeries("Mote " + m.getId());
				this.dataset.addSeries(serie);
				this.seriesMap.put(m, serie);
			}
		}
	}
	
	private Mote findMoteFromInt(int id) {
		for(Mote m : motes) {
			if(m.getId() == id) {
				return m;
			}
		}
		return null;
	}

	public DataParamType getPt() {
		return pt;
	}

	public void setPt(DataParamType pt) {
		this.pt = pt;
	}

	public ArrayList<Mote> getMotes() {
		return motes;
	}

	public void setMotes(ArrayList<Mote> motes) {
		this.motes = motes;
	}
	
	public JFreeChart getJfc() {
		return jfc;
	}

	public void setJfc(JFreeChart jfc) {
		this.jfc = jfc;
	}

	public void enterData(int id, double value, Second second) {
		Mote mote = findMoteFromInt(id);
		if(mote != null) {
			this.seriesMap.get(mote).add(second, value);
		}
	}

	public void refresh() {
		//TODO check if needed
	}
	
	public ChartPanel getAsChartPanel() {
		return (new ChartPanel(this.jfc));
	}
}
