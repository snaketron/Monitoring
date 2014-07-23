package gui.chart;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import system.Mote;

/**
 * The Summary class represents a panel in which a piechart is visualized. The piechart shows
 * the ratio of messages sent by each active mote. 
 * TODO should be extended to show other statistics as well.
 */
public class Summary extends JPanel {
	private static final long serialVersionUID = 1L;
	private ArrayList<Mote> motes;
	private JFreeChart jfc;
	private DefaultPieDataset dataset;
	
	public Summary(ArrayList<Mote> motes) {
		this.motes = motes;
		setupDataset();
		this.setLayout(new BorderLayout());
        this.add(getAsChartPanel(), BorderLayout.CENTER);
	}
	
	private void setupDataset() {
        this.dataset = new DefaultPieDataset();
        if(motes != null) {
        	for(Mote mote : motes) {
        		dataset.setValue("Mote " + mote.getId(), mote.getData().size());
        	}
        }
        this.jfc = ChartCreatorRoutine.createPieChart(dataset);
    }
	
	public void enterData(Mote mote) {
		if(motes.contains(mote)) {
			dataset.setValue("Mote " + mote.getId(), mote.getData().size());
		}
	}
	
	private ChartPanel getAsChartPanel() {
		return (new ChartPanel(this.jfc));
	}
}
