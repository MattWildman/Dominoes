package dominoes;

import javax.swing.SwingUtilities;

/**
 * @author Ben Griffiths and Matt Wildman 
 * MSc Computer Science 
 * 31/03/2013
 * DominoesRunner 
 * Contains main method which creates and shows the DominoUI
 * 
 */
public class DominoesRunner {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				DominoUIImpl gUI = new DominoUIImpl();
				gUI.createAndShowGUI();
			}
		});
	}
	
}