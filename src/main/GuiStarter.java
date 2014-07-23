package main;

import gui.Dashboard;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import system.Engine;

public class GuiStarter {

	/**
	 * Entry point. Generates the JFrame, displays the Dashboard panel and starts the 
	 * Engine in a separate thread. <br>
	 * 
	 * Check the Const interface in the utils package if you want to change 
	 * some crucial variables.
	 */
	public static void main(String[] args) {
		Engine engine = new Engine();
		Thread thread = new Thread(engine);
		
		JFrame frame = new JFrame("WSN Monitoring Tool");
		frame.setSize(1300, 700);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		frame.getContentPane().add(new Dashboard(engine));
		thread.start();
		frame.validate();
		frame.repaint();
	}
}
