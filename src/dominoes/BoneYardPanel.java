package dominoes;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Ben Griffiths and Matt Wildman
 * MSc Computer Science 
 * 31/03/2013
 * BoneYardPanel
 * @extends JPanel
 *
 */
@SuppressWarnings("serial")
public class BoneYardPanel extends JPanel {
	
	@Getter (AccessLevel.PUBLIC) private static final int boneYardCellHeight = 30;
	@Getter (AccessLevel.PUBLIC) private static final int boneYardCellWidth = 55;
	
	private int boneYardSize = 0;
	
	private JLabel boneYardLabel;
	private DefaultListModel<ImageIcon> boneYardListModel;
	@Getter (AccessLevel.PUBLIC) private JList<ImageIcon> boneYardList;
    private JScrollPane boneYardScrollPane;
    @Getter (AccessLevel.PUBLIC) private JButton passButton;
    
    private JPanel boneYardPanel;
    private JPanel passButtonPanel;
	private boolean boneYardEmpty;
    
	/**
	 * Constructor
	 * 
	 */
    public BoneYardPanel() {
    	boneYardLabel = new JLabel(boneYardSize + " bones remaining");
    	boneYardListModel = new DefaultListModel<ImageIcon>();
    	boneYardList = new JList<ImageIcon>(boneYardListModel);
    	boneYardScrollPane = new JScrollPane(boneYardList);
    	
    	boneYardScrollPane.setPreferredSize(DominoesStyle.getBoneYardDimension());
    	boneYardScrollPane.setBorder(null);
    	
    	boneYardList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
 	    boneYardList.setVisibleRowCount(2);
 	    boneYardList.setBorder(BorderFactory.createEmptyBorder(1, 4, 2, 2));
 	    boneYardList.setFixedCellHeight(boneYardCellHeight);
 	    boneYardList.setFixedCellWidth(boneYardCellWidth);
 	    boneYardList.setToolTipText("Click to take a bone from the bone yard");
 	    boneYardList.setFocusable(false);
 	    
 	    passButtonPanel = new JPanel();
 	    passButton = new JButton("Boneyard is empty: Click here to pass");
 	    passButtonPanel.setLayout(new BoxLayout(passButtonPanel, BoxLayout.PAGE_AXIS));
 	    passButtonPanel.add(passButton);
 	    boneYardPanel = new JPanel();
 	    boneYardPanel.setLayout(new BoxLayout(boneYardPanel, BoxLayout.PAGE_AXIS));
        boneYardPanel.add(boneYardLabel);
        boneYardPanel.add(boneYardScrollPane);
        boneYardPanel.setPreferredSize(DominoesStyle.getBoneYardDimension());
        boneYardPanel.setOpaque(false);
        this.setOpaque(false);
        this.add(boneYardPanel);
    }
    
    /**
     * clearBoneYard
     * Called at start of round.
     */
    public void clearBoneYard() {
    	if(boneYardEmpty == true) {
	    	this.boneYardEmpty = false;
			this.remove(passButtonPanel);
			this.add(boneYardPanel);
			this.revalidate();
			this.repaint();
    	}
    }
    
    /**
     * display
     * @param boneYard - the boneYard to display
     */
	public void display(BoneYard boneYard) {
		boneYardSize = boneYard.size();
		SwingUtilities.invokeLater(new DominoUIImplUpdater() {
    		@Override
			public void run() {
    			boneYardLabel.setText(boneYardSize + " bones remaining");
			}
		});
    	if(boneYardSize == 0) {
    		boneYardEmpty = true;
    		SwingUtilities.invokeLater(new DominoUIImplUpdater() {
        		@Override
				public void run() {
            		remove(boneYardPanel);
            		add(passButtonPanel);
            		revalidate();
            		repaint();
				}
			});
    	}
    	int bonesAddedToBoneYard = boneYardSize - boneYardListModel.size();
    	if(bonesAddedToBoneYard > 0) {
	    	SwingUtilities.invokeLater(new DominoUIImplUpdater(bonesAddedToBoneYard) {
	    		@Override
				public void run() {
	    			for(int i = 0; i < this.getIndex(); i++)
	            		boneYardListModel.addElement(new BoneIcon());
				}
			});
    	}
    	else if(bonesAddedToBoneYard < 0) {
	    		SwingUtilities.invokeLater(new DominoUIImplUpdater(bonesAddedToBoneYard) {
		    		@Override
					public void run() {
		    			for(int i = 0; i > this.getIndex(); i--)
		            		boneYardListModel.remove(boneYardListModel.size() - 1);
					}
				});
    	}
    }
}
