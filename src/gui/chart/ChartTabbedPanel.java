package gui.chart;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTabbedPane;

import msg.DataMsg;
import system.Mote;
import utils.DataParamType;

/**
 * This class is a container for the Chart class.
 */
public class ChartTabbedPanel extends JTabbedPane {
	private static final long serialVersionUID = 1L;
	private ArrayList<Mote> motes;
	private HashMap<DataParamType, Chart> chartMap;

	public ChartTabbedPanel(ArrayList<Mote> motes) {
		this.motes = motes;
		this.chartMap = new HashMap<DataParamType, Chart>();
		
		for(DataParamType pt : DataParamType.values()) {
			Chart chart = new Chart(pt, this.motes);
			this.addTab(pt.name(), chart.getAsChartPanel());
			chartMap.put(pt, chart);
		}
	}
	
	public void enterDataToChart(DataMsg data) {
		this.chartMap.get(DataParamType.HUMIDITY).enterData(data.getOrigin(), 
				data.getHumidity(), data.getSecond());
		this.chartMap.get(DataParamType.TEMPERATURE).enterData(data.getOrigin(), 
				data.getTemperature(), data.getSecond());
		this.chartMap.get(DataParamType.LIGHT).enterData(data.getOrigin(), 
				data.getLight(), data.getSecond());
		this.chartMap.get(DataParamType.VOLTAGE).enterData(data.getOrigin(), 
				data.getVolt(), data.getSecond());
	}
}
