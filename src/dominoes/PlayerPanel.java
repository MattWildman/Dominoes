package dominoes;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * 
 * @author Ben Griffiths and Matt Wildman
 * MSc Computer Science
 * 31/03/13
 * PlayerPanel
 * Displays player-specific information during a game
 * @extends JPanel
 *
 */
@SuppressWarnings("serial")
public class PlayerPanel extends JPanel {
		
		@Getter(AccessLevel.PUBLIC) private static final String playLeftCommand = "playLeft";
		@Getter(AccessLevel.PUBLIC) private static final String playRightCommand = "playRight";
		@Getter(AccessLevel.PUBLIC) private static final String drawPassCommand = "drawPass";
		@Getter(AccessLevel.PUBLIC) private static final String requestFocusCommand = "requestFocus";
		@Getter(AccessLevel.PUBLIC) private static final int listCellHeight = 33;
		@Getter(AccessLevel.PUBLIC) private static final int listCellWidth = 55;
		
        private JPanel playerDetails;
        
        private JLabel playerNameLabel;
        private JLabel playerBonesInHandLabel;
        @Getter private JLabel playerPointsLabel;
        
        private DefaultListModel<ImageIcon> playerHandModel;
        @Getter(AccessLevel.PUBLIC) private JList<ImageIcon> playerBonesList;
        private JScrollPane playerBonesListScrollPane;
        
        private int points = 0;
        private int numberOfBones = 0;
        
		/**
		 * Constructor
		 * @param playerName
		 */
        public PlayerPanel(String playerName)    {
                super();
                setBackground(Color.WHITE);
                
                playerHandModel = new DefaultListModel<ImageIcon>();
                playerBonesList = new JList<ImageIcon>(playerHandModel);
                playerBonesListScrollPane = new JScrollPane(playerBonesList);
                playerBonesListScrollPane.setPreferredSize(DominoesStyle.getHandDimension());
                playerBonesListScrollPane.setBorder(null);
                playerBonesList.setBorder(BorderFactory.createCompoundBorder(null,DominoesStyle.getSMALL_PADDING()));
                playerBonesList.setFixedCellHeight(listCellHeight);
                
                playerNameLabel = new JLabel(playerName);
                playerBonesInHandLabel = new JLabel("Bones in hand: " + numberOfBones);
                playerPointsLabel = new JLabel("Points: " + points);
                
                playerDetails = new JPanel();
                playerDetails.setLayout(new BorderLayout());
                playerDetails.add(playerNameLabel, BorderLayout.PAGE_START);
                playerDetails.add(playerPointsLabel, BorderLayout.CENTER);
                playerDetails.add(playerBonesInHandLabel, BorderLayout.PAGE_END);
                playerDetails.setPreferredSize(DominoesStyle.getPlayerDetailsDimension());
                playerDetails.setBorder(BorderFactory.createCompoundBorder(null,DominoesStyle.getSMALL_PADDING()));
                
                setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                
                playerDetails.setBackground(Color.LIGHT_GRAY);
        		playerBonesList.setBackground(Color.LIGHT_GRAY);
        		playerBonesList.setEnabled(false); // bones not enabled and highlighted until it's your turn
        		playerBonesList.setFocusable(true);
                
                setLayout(new BorderLayout());
                add(playerDetails, BorderLayout.PAGE_START);
                add(playerBonesListScrollPane, BorderLayout.CENTER);
        }
        
        public void display(Bone[] bones, String playerName, int points,  boolean interactive) {
        	clearHand();
        	BoneIcon boneIcon;
        	numberOfBones = bones.length;
        	
        	for(int i = 0; i < numberOfBones; i++) {
        		if(interactive)
        			boneIcon = new BoneIcon(bones[i]);
        		else boneIcon = new BoneIcon();
        		
        		SwingUtilities.invokeLater(new DominoUIImplUpdater(boneIcon) {
            		@Override
    				public void run() {
            			playerHandModel.addElement(this.getIcon());
    				}
    			});
        		
        	}
        	
        	SwingUtilities.invokeLater(new DominoUIImplUpdater() {
        		@Override
				public void run() {
        			if(numberOfBones > 0)
                		playerBonesList.setSelectedIndex(playerHandModel.size() - 1);
        			playerBonesInHandLabel.setText("Bones in hand: " + numberOfBones);
				}
			});
        }
        
        public void clearHand() {
        	SwingUtilities.invokeLater(new DominoUIImplUpdater() {
        		@Override
				public void run() {
        			playerHandModel.clear();
				}
			});
        }
        
        public void highlight(boolean myTurn) {
        	SwingUtilities.invokeLater(new DominoUIImplUpdater(myTurn) {
        		@Override
				public void run() {
        			if (this.isCondition())
                		playerBonesList.requestFocusInWindow();
                	Color borderColour = this.isCondition() ? Color.BLUE : Color.LIGHT_GRAY;
                	Color detailsBackgroundColour = this.isCondition() ? Color.WHITE : Color.LIGHT_GRAY;
                	Color listBackgroundColour = this.isCondition() ? DominoesStyle.BACKGROUND_COLOUR : Color.LIGHT_GRAY;
                	playerDetails.setBackground(detailsBackgroundColour);
                	setBorder(BorderFactory.createLineBorder(borderColour));
                	playerBonesList.setBackground(listBackgroundColour);
            		playerBonesList.setEnabled(this.isCondition());
				}
			});
        	
		}
        
		public int getSelectedBone() {
			return playerBonesList.getSelectedIndex();
		}
		
}