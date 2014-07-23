package gui.chart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import system.Mote;
import system.sympathy.util.SympathyReport;
import utils.GlobalConst;

/**
 * Displays the network topology which is dynamically update based on 
 * events received from the Engine. This class uses the JUNG library.
 */
public class SympathyNetworkTopology extends JPanel {
	private static final long serialVersionUID = 1L;
	private ArrayList<Mote> motes;
	private HashMap<Integer, SympathyReport> reportMap;
	private Graph<String,Integer> graph;
    private VisualizationViewer<String,Integer> vv;
    private  SpringLayout <String, Integer> layout;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SympathyNetworkTopology() {
		this.motes = new ArrayList<Mote>();
		this.reportMap = new HashMap<Integer, SympathyReport>();
		this.graph = new SparseMultigraph<String, Integer>();
		Dimension dim = new Dimension(300, 300);
		this.layout = new SpringLayout<String, Integer>(graph);
		this.layout.setStretch(0.1);
		
		final VisualizationModel<String,Integer> visModel = new DefaultVisualizationModel<String,Integer>(layout, dim);
		
		vv = new VisualizationViewer<String, Integer>(visModel, dim);
        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setArrowFillPaintTransformer(new ConstantTransformer(Color.lightGray));
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        
        final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
        vv.setGraphMouse(graphMouse);
        graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
        
        Transformer<String, Paint> vertexColor = new Transformer<String, Paint>() {
			@Override
			public Paint transform(String st) {
				if(!reportMap.isEmpty()) {
					if(reportMap.get(Integer.valueOf(st)) != null) {
						if(reportMap.get(Integer.valueOf(st)).getFailures().isEmpty()) {
							if(reportMap.get(Integer.valueOf(st)).getIndividualSuggestions().isEmpty()) {
								return Color.GREEN;
							}
							else {
								return Color.YELLOW;
							}
						}
						else {
							return Color.RED;
						}
					}
				}
				return Color.LIGHT_GRAY;
			}
        };
        vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
        
        Transformer<Integer, Paint> edgeDrawColor = new Transformer<Integer, Paint>() {
			@Override
			public Paint transform(Integer st) {
				if(st == null) {
					return Color.BLACK;
				}
				
				if(st > 100) {
					return new Color(50,50,50,50);
				}
				return Color.BLACK;
			}
        };
        vv.getRenderContext().setEdgeDrawPaintTransformer(edgeDrawColor);
        
        Transformer<Integer, Paint> edgeArrowColor = new Transformer<Integer, Paint>() {
			@Override
			public Paint transform(Integer st) {
				if(st == null) {
					return Color.BLACK;
				}
				
				if(st > 100) {
					return new Color(50,50,50,1);
				}
				return Color.black;
			}
        };
        vv.getRenderContext().setArrowDrawPaintTransformer(edgeArrowColor);
        
        
        vv.addGraphMouseListener(new GraphMouseListener<String>() {
			
			@Override
			public void graphReleased(String st, MouseEvent me) {
				JPopupMenu menu = new JPopupMenu();
				JMenuItem label = new JMenuItem("Mote " + st);
				menu.add(label);
				
				if(!reportMap.isEmpty()) {
					if(reportMap.get(Integer.valueOf(st)) != null) {
						if(!reportMap.get(Integer.valueOf(st)).getFailures().isEmpty()) {
							menu.add(getPanel(reportMap.get(Integer.valueOf(st)).getFailures(), "1) Failures: ", Color.red));
						}
						if(!reportMap.get(Integer.valueOf(st)).getFailureSuggestions().isEmpty()) {
							menu.add(getPanel(reportMap.get(Integer.valueOf(st)).getFailureSuggestions(), "2) Failure Suggestions: ", Color.yellow));
						}
						if(!reportMap.get(Integer.valueOf(st)).getIndividualSuggestions().isEmpty()) {
							menu.add(getPanel(reportMap.get(Integer.valueOf(st)).getIndividualSuggestions(), "3) Independent Suggestions: ", Color.yellow));
						}
					}
				}
				menu.show(vv, me.getX(), me.getY());
			}
			
			@Override
			public void graphPressed(String arg0, MouseEvent arg1) {}
			
			@Override
			public void graphClicked(String arg0, MouseEvent arg1) {}
		});
        
        
        this.setLayout(new BorderLayout());
        this.add(vv, BorderLayout.CENTER);
		
		this.validate();
		this.repaint();
	}
	
	public void updateMotes(HashMap<Integer, Mote> motesMap) {
		synchronized (motes) {
			for(Mote m : motesMap.values()) {
				if(motes.contains(m)) {
					motes.add(motesMap.get(m.getId()));
					motes.remove(m);
				}
				else {
					motes.add(motesMap.get(m.getId()));
				}
			}
			refreshTopology();
		}
	}
	
	public void updateReports(HashMap<Integer, SympathyReport> reportMap) {
		synchronized (reportMap) {
			this.reportMap = reportMap;
			refreshTopology();
		}
	}
	
	private void refreshTopology() {
		synchronized (motes) {
			this.graph = new SparseMultigraph<String, Integer>();

			for(Mote m : motes) {
				this.graph.addVertex(String.valueOf(m.getId()));
			}
			
			for(Mote m: motes) {
				for(Integer n : m.getNeighbors()) {
					int id = m.getId() * 100 + n; 
					this.graph.addEdge(id, String.valueOf(n), String.valueOf(m.getId()), EdgeType.DIRECTED);
				}
				
				if(m.getId() != GlobalConst.SINK_ID) {
					this.graph.addEdge(m.getId(), String.valueOf(m.getId()), String.valueOf(m.getParent()), EdgeType.DIRECTED);
				}
			}
			
			layout.setLocation(String.valueOf(GlobalConst.SINK_ID), 150, 50);
			layout.reset();
			layout.setGraph(graph);
		}
		this.validate();
		this.repaint();
	}
	
	private JPanel getPanel(ArrayList<String> values, String title, Color col) {
		JPanel panel = new JPanel(new GridLayout(values.size() + 1, 1));
		JLabel titleLabel = new JLabel(title);
		panel.add(titleLabel);
		for(String ps : values) {
			JLabel l = new JLabel(ps);
			l.setOpaque(true);
			l.setBackground(col);
			panel.add(l);
		}

		return panel;
	}
}