/**
 * 
 */
package dominoes;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;

/**
 * 
 * @author Ben Griffiths and Matt Wildman
 * MSc Computer Science
 * 31/03/13
 * GameConfigPanel
 * @extends JPanel
 */
@SuppressWarnings("serial")
public class GameConfigPanel extends JPanel {
	
	private final int DEFAULT_POINT_GOAL = 100;
	private final int DEFAULT_PIPS = 6;
	private final int MINIMUM_PIPS = 4;
	private final int MAXIMUM_PIPS = 9;
	private final int FORMATTED_INPUT_COLUMNS = 5;
	
	private JLabel pointGoalLabel;
	private JLabel pipSelectorLabel;
	private JFormattedTextField pointGoalInput;
	private JSpinner pipSelector;
	private SpinnerListModel pipSelectorModel;
	
	private HashMap<String, Integer> pipsMap;
	
	public GameConfigPanel() {
		this.setLayout(new BorderLayout());
		JPanel pipPanel = new JPanel();
		JPanel pointPanel = new JPanel();
		this.setOpaque(false);
		pipPanel.setOpaque(false);
		pointPanel.setOpaque(false);
		pointGoalLabel = new JLabel("Points required to win:");
		pipSelectorLabel = new JLabel("Set of bones to use:");
		pointGoalInput = new JFormattedTextField();
		pointGoalInput.setValue(DEFAULT_POINT_GOAL);
		pointGoalInput.setColumns(FORMATTED_INPUT_COLUMNS);
		ArrayList<String> pipOptionsList = new ArrayList<String>();
		pipsMap = new HashMap<String, Integer>();
		for(int i = MINIMUM_PIPS; i <= MAXIMUM_PIPS; i++) {
			String key = "Double " + i;
			pipsMap.put(key, i);
			pipOptionsList.add(key);
		}
		pipSelectorModel = new SpinnerListModel(pipOptionsList);
		pipSelectorModel.setValue(pipOptionsList.get(DEFAULT_PIPS - MINIMUM_PIPS));
		pipSelector = new JSpinner((pipSelectorModel));
		pipPanel.add(pipSelectorLabel);
		pipPanel.add(pipSelector);
		pointPanel.add(pointGoalLabel);
		pointPanel.add(pointGoalInput);
		pipPanel.setBorder(BorderFactory.createCompoundBorder(null, DominoesStyle.getSMALL_PADDING()));
		pointPanel.setBorder(BorderFactory.createCompoundBorder(null, DominoesStyle.getSMALL_PADDING()));
		this.add(pipPanel, BorderLayout.NORTH);
		this.add(pointPanel, BorderLayout.CENTER);
	}
	
	public int getPointGoalInput() {
		return (Integer)pointGoalInput.getValue();
	}
	
	public int getPipSelection() {
		return (int)pipsMap.get(pipSelector.getModel().getValue());
	}

}
