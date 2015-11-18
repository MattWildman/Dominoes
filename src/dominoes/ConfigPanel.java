package dominoes;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lombok.Getter;
import lombok.Setter;
import lombok.AccessLevel;

/**
 * @author Ben Griffiths and Matt Wildman
 * MSc Computer Science 
 * 31/03/2013
 * ConfigPanel
 * Contains all game configuration panels - game config panel, two player config panels and a start game button
 * @extends JPanel
 */
@SuppressWarnings("serial")
public class ConfigPanel extends JPanel {
	@Getter(AccessLevel.PRIVATE) private final String WELCOME_MESSAGE = "Welcome to Fat Dominos";
	@Getter(AccessLevel.PRIVATE) private final int GRID_ROWS = 5;
	@Getter(AccessLevel.PRIVATE) private final int GRID_COLUMNS = 2;
	@Getter(AccessLevel.PRIVATE) private final int COMPONENT_GAP = 30;
	@Getter(AccessLevel.PRIVATE) private final BoneIcon LEFT_BONE_ICON = new BoneIcon(new Bone(1, 6));
	@Getter(AccessLevel.PRIVATE) private final BoneIcon RIGHT_BONE_ICON = new BoneIcon(new Bone(1, 6));
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PRIVATE) private JButton startGameButton;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private PlayerConfigPanel player1ConfigPanel = new PlayerConfigPanel(1);
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private PlayerConfigPanel player2ConfigPanel = new PlayerConfigPanel(2);
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private GameConfigPanel gameConfigPanel = new GameConfigPanel();
	public static final int PLAYER_1 = 1;
	public static final int PLAYER_2 = 2;
	
    /**
     * Constructor
     */
	public ConfigPanel() {
		super();
		this.setBackground(DominoesStyle.BACKGROUND_COLOUR);
		this.setLayout(new GridLayout(getGRID_ROWS(),getGRID_COLUMNS(),getCOMPONENT_GAP(),getCOMPONENT_GAP()));
		setStartGameButton(new JButton("Start game"));
        getStartGameButton().setPreferredSize(DominoesStyle.getButtonDimension());
        JPanel startGamePanel = new JPanel();
        startGamePanel.add(getStartGameButton());
        startGamePanel.setOpaque(false);
        JPanel titlePanel = new JPanel();
        JLabel welcomeLabel = new JLabel(getWELCOME_MESSAGE());
        welcomeLabel.setFont(DominoesStyle.WELCOME_FONT);   
        JLabel leftBoneLabel = new JLabel(getLEFT_BONE_ICON());
        getRIGHT_BONE_ICON().rotate180();
        JLabel rightBoneLabel = new JLabel(getRIGHT_BONE_ICON());
        titlePanel.add(leftBoneLabel);
        titlePanel.add(welcomeLabel);
        titlePanel.add(rightBoneLabel);
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createCompoundBorder(null,DominoesStyle.getLARGE_PADDING()));
        this.add(titlePanel);
        this.add(getGameConfigPanel());
        this.add(getPlayer1ConfigPanel());
        this.add(getPlayer2ConfigPanel());
        this.add(startGamePanel);
	}
	
	/**
	 * getPointGoalInput
	 * @return the chosen target score for the game
	 */
	public int getPointGoalInput() {
		return getGameConfigPanel().getPointGoalInput();
	}
	
	/**
	 * getPipSelection
	 * @return the chosen set of dominoes (i.e. the chosen max pip value on a bone)
	 */
	public int getPipSelection() {
		return getGameConfigPanel().getPipSelection();
	}
	
	/**
	 * getNameInput
	 * @param player - a player in the game, either 1 or 2
	 * @return the name chosen for the player
	 */
	public String getNameInput(int player) {
		if(player == PLAYER_1)
			return getPlayer1ConfigPanel().getNameInput();
		return getPlayer2ConfigPanel().getNameInput();
	}
	
	/**
	 * selectedHuman
	 * @param player - a player in the game, either 1 or 2
	 * @return true if human player type is chosen, otherwise false
	 */
	public boolean selectedHuman(int player) {
		if(player == PLAYER_1)
			return getPlayer1ConfigPanel().selectedHuman();
		return getPlayer2ConfigPanel().selectedHuman();
	}
	
	/**
	 * getDifficulty
	 * @param player - a player in the game, either 1 or 2
	 * @return the player type selected
	 */
	public PlayerType getDifficulty(int player) {
		if(player == PLAYER_1)
			return getPlayer1ConfigPanel().getDifficulty();
		return getPlayer2ConfigPanel().getDifficulty();
	}	
}