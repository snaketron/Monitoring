package gui.chart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import system.Mote;
import utils.GlobalConst;

/**
 * Displays the network topology which is dynamically update based on 
 * events received from the Engine. This class uses the JUNG library.
 */
public class NetworkTopology extends JPanel {
	private static final long serialVersionUID = 1L;
	private ArrayList<Mote> motes;
	private Graph<String,Integer> graph;
    private VisualizationViewer<String,Integer> vv;
    private SpringLayout <String, Integer> layout;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public NetworkTopology() {
		this.motes = new ArrayList<Mote>();
		this.graph = new SparseMultigraph<String, Integer>();
		Dimension dim = new Dimension(300, 300);
		this.layout = new SpringLayout<String, Integer>(graph);
		this.layout.setStretch(0.1);
		
		final VisualizationModel<String,Integer> visModel = new DefaultVisualizationModel<String,Integer>(layout, dim);
		
		vv = new VisualizationViewer<String, Integer>(visModel, dim);
        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setArrowFillPaintTransformer(new ConstantTransformer(Color.lightGray));
        vv.getRenderContext().setVertexFillPaintTransformer(new ConstantTransformer(Color.lightGray));
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        
        final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
        vv.setGraphMouse(graphMouse);
        graphMouse.setMode(ModalGraphMouse.Mode.PICKING);        
        
        this.setLayout(new BorderLayout());
        this.add(vv, BorderLayout.CENTER);
		
		this.validate();
		this.repaint();
	}
	
	public void updateMotes(HashMap<Integer, Mote> motesMap) {
		this.motes.clear();
		this.motes.addAll(motesMap.values());
		refreshTopology();
	}
	
	private void refreshTopology() {
		synchronized (this.graph) {
			this.graph = new DelegateForest<String,Integer>();

			for(Mote m : motes) {
				if(m.isConnected()) {
					this.graph.addVertex(String.valueOf(m.getId()));
				}
			}
			
			for(Mote m: motes) {
				if(m.isConnected() && m.getParent() != GlobalConst.DUMMY) {
					this.graph.addEdge(m.getId(), String.valueOf(m.getId()), String.valueOf(m.getParent()));
				}
			}
			
			layout.setLocation(String.valueOf(GlobalConst.SINK_ID), 150, 50);
			layout.reset();
			layout.setGraph(graph);
		}
		this.validate();
		this.repaint();
	}
}