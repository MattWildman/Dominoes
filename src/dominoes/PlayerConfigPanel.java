package dominoes;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * 
 * @author Ben Griffiths and Matt Wildman
 * MSc Computer Science
 * 31/03/13
 * PlayerConfigPanel
 * Game config options relating to a game player
 * @extends JPanel
 * @implements ActionListener
 *
 */
@SuppressWarnings("serial")
public class PlayerConfigPanel extends JPanel implements ActionListener {
	
	private int playerNumber;
	private boolean selectedHuman;
	
	private JPanel formPanel;
	private JPanel humanCompPanel;
	private JPanel difficultyPanel;
	private JPanel namePanel;
	
	private JLabel nameLabel;
	private JLabel playerTypeLabel;
	private JLabel compDifficultyLabel;
	
	private ButtonGroup playerTypeGroup;
	private ButtonGroup compDifficultyGroup;
	private JRadioButton humanSelect;
	private JRadioButton compSelect;
	private JRadioButton veryEasySelect;
	private JRadioButton easySelect;
	private JRadioButton mediumSelect;
	private JRadioButton hardSelect;
	
	private JTextField nameInput;
	
	public PlayerConfigPanel(int i) {
		super();
		setOpaque(false);
		formPanel = new JPanel();
		humanCompPanel = new JPanel();
		difficultyPanel = new JPanel();
		namePanel = new JPanel();
		
		playerNumber = i;
		
		selectedHuman = true;
		
		nameLabel = new JLabel("Name:");
		playerTypeLabel = new JLabel("Player " + playerNumber + ":");
		playerTypeLabel.setFont(DominoesStyle.getMEDIUM_FONT());
		compDifficultyLabel = new JLabel("Choose difficulty:");
		
		humanSelect = new JRadioButton("Human");
		compSelect = new JRadioButton("Computer");
		
		veryEasySelect = new JRadioButton("Very Easy");
		easySelect = new JRadioButton("Easy");
		mediumSelect = new JRadioButton("Medium");
		hardSelect = new JRadioButton("Hard");
		
		humanSelect.setSelected(true);
		mediumSelect.setSelected(true);
		
		humanSelect.addActionListener(this);
		compSelect.addActionListener(this);
		veryEasySelect.addActionListener(this);
		easySelect.addActionListener(this);
		mediumSelect.addActionListener(this);
		hardSelect.addActionListener(this);
		
		humanSelect.setOpaque(false);
		compSelect.setOpaque(false);
		veryEasySelect.setOpaque(false);
		easySelect.setOpaque(false);
		mediumSelect.setOpaque(false);
		hardSelect.setOpaque(false);
		
		compDifficultyLabel.setEnabled(false);
		veryEasySelect.setEnabled(false);
		easySelect.setEnabled(false);
		mediumSelect.setEnabled(false);
		hardSelect.setEnabled(false);
		
		playerTypeGroup = new ButtonGroup();
		compDifficultyGroup = new ButtonGroup();
		
		playerTypeGroup.add(compSelect);
		playerTypeGroup.add(humanSelect);
		
		compDifficultyGroup.add(veryEasySelect);
		compDifficultyGroup.add(easySelect);
		compDifficultyGroup.add(mediumSelect);
		compDifficultyGroup.add(hardSelect);
		nameInput = new JTextField(20);
		nameInput.setText("Enter your name");
		
		formPanel.setLayout(new GridLayout(2,2));
		formPanel.setBackground(DominoesStyle.SECONDARY_COLOUR);
		formPanel.setBorder(BorderFactory.createCompoundBorder(null, DominoesStyle.getSMALL_PADDING()));
		
		humanCompPanel.add(playerTypeLabel);
		humanCompPanel.add(humanSelect);
		humanCompPanel.add(compSelect);
		difficultyPanel.add(compDifficultyLabel);
		difficultyPanel.add(veryEasySelect);
		difficultyPanel.add(easySelect);
		difficultyPanel.add(mediumSelect);
		difficultyPanel.add(hardSelect);
		namePanel.add(nameLabel);
		namePanel.add(nameInput);
		formPanel.add(humanCompPanel);
		formPanel.add(namePanel);
		formPanel.add(difficultyPanel);
		add(formPanel);
		humanCompPanel.setOpaque(false);
		difficultyPanel.setOpaque(false);
		namePanel.setOpaque(false);
	}
	
	public String getNameInput() {
		return nameInput.getText();
	}
	
	public boolean selectedHuman() {
		return selectedHuman;
	}
	
	public PlayerType getDifficulty() {
		if (veryEasySelect.isSelected())
			return PlayerType.VERY_EASY;
		if (easySelect.isSelected())
			return PlayerType.EASY;
		if (mediumSelect.isSelected())
			return PlayerType.MEDIUM;
		if (hardSelect.isSelected())
			return PlayerType.HARD;
		if (humanSelect.isSelected())
			return PlayerType.INTERACTIVE;
		return null;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		nameInput.setText(this.getDifficulty().name() + " bot");
		
		if (compSelect.isSelected()) {
			
			selectedHuman = false;
			compDifficultyLabel.setEnabled(true);
			veryEasySelect.setEnabled(true);
			easySelect.setEnabled(true);
			mediumSelect.setEnabled(true);
			hardSelect.setEnabled(true);
			nameInput.setEnabled(false);
			nameLabel.setEnabled(false);
		}
		else {
			selectedHuman = true;
			compDifficultyLabel.setEnabled(false);
			veryEasySelect.setEnabled(false);
			easySelect.setEnabled(false);
			mediumSelect.setEnabled(false);
			hardSelect.setEnabled(false);
			nameInput.setText("Enter your name");
			nameLabel.setEnabled(true);
			nameInput.setEnabled(true);
		}
	}

}
