package gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import system.Engine;

/**
 * This is the uppermost panel which is added by the JFrame, containing the two
 * crucial panels: control and visualization. The event listeners are bound together
 * in this class.
 */
public class Dashboard extends JPanel {
	private static final long serialVersionUID = 1L;
	private Control controlPanel;
	private Visualization visualizationPanel;
	private Engine engine;
	
	public Dashboard(Engine engine) {
		this.setLayout(new BorderLayout());

		this.controlPanel = new Control();
		this.visualizationPanel = new Visualization();
		this.engine = engine;
		
		this.add(visualizationPanel, BorderLayout.CENTER);
		this.add(controlPanel, BorderLayout.WEST);
		
		this.controlPanel.addListener(engine, visualizationPanel);
		this.engine.addMoteListeners(controlPanel);
		this.engine.addMoteListeners(visualizationPanel);
		this.engine.addDataListeners(visualizationPanel);
		
		this.validate();
		this.repaint();
	}
}