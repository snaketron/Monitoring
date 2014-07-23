
package gui.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYDataset;

import utils.DataParamType;

/**
 * This class is a routine used to create the appropriate jfreechart.
 */
public class ChartCreatorRoutine {
	private final static String CHART = "Chart";
	private final static String TIME = "Time";
	
	public static JFreeChart createPieChart(PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart("Mote Statistics", 
        		dataset, true, true, false);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        return chart;
    }
    
    
    @SuppressWarnings("deprecation")
	public static JFreeChart createJFreeChart(DataParamType pt, XYDataset dataset) {
    	String chartName = pt.getName() + CHART;
    	
    	JFreeChart chart = ChartFactory.createScatterPlot(chartName, TIME, pt.getName(),
    	         dataset, PlotOrientation.VERTICAL, true, true, false);
    	chart.setBackgroundPaint(Color.white);
    	
    	XYPlot plot = chart.getXYPlot();
    	plot.setBackgroundPaint(Color.white);
    	plot.setDomainGridlinePaint(Color.white);
    	plot.setRangeGridlinePaint(Color.white);
    	plot.setDomainCrosshairVisible(true);
    	plot.setRangeCrosshairVisible(false);
    	

    	XYItemRenderer renderer = plot.getRenderer();
    	if(renderer instanceof StandardXYItemRenderer) {
    		renderer.setSeriesStroke(0, new BasicStroke(2.0f));
    		renderer.setSeriesStroke(1, new BasicStroke(2.0f));
    	}

    	plot.setDomainAxis(new DateAxis());
    	DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
    	dateAxis.setDateFormatOverride(new SimpleDateFormat("HH:mm:ss"));
    	long prevDay = System.currentTimeMillis() - (1000 * 60 * 5);
    	long nextDay = (15 * 60 * 1000);
    	dateAxis.setMinimumDate(new Date(prevDay));
    	dateAxis.setMaximumDate(new Date(System.currentTimeMillis() + nextDay));
    	dateAxis.setTickLabelsVisible(true);
    	dateAxis.setTickUnit(new DateTickUnit(DateTickUnit.MINUTE, 5));
    	
    	ValueAxis valueAxis = plot.getRangeAxis();
    	valueAxis.setLowerBound(pt.getMin());
    	valueAxis.setUpperBound(pt.getMax());
    	
    	return chart;
    }
}