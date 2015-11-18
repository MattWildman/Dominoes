package dominoes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ben Griffiths and Matt Wildman
 * MSc Computer Science 
 * 31/03/2013
 * TablePanel
 * Displays a table of dominoes
 * @extends JPanel
 * 
 */
@SuppressWarnings("serial")
public class TablePanel extends JPanel {

	@Getter (AccessLevel.PUBLIC) private static final int E_PAD = 4; //space between bones
	@Getter (AccessLevel.PUBLIC) private static final int HALF_BONE = 25;	//the length of bone short side
	@Getter (AccessLevel.PUBLIC) private static final int WHOLE_BONE = 50; //the length of bone long side
	@Getter (AccessLevel.PUBLIC) private static final int MAX_WIDTH = DominoesStyle.getTableWidth() - (WHOLE_BONE * 2);
	private static final String BUTTON_TOOL_TIP_TEXT = "Click to play selected bone here";
	@Getter (AccessLevel.PUBLIC) private static Color tableColour = new Color(124, 205, 124);
	private int tableHeight;
	private int tableSize;
	private int pips;
	private int centre_x = MAX_WIDTH / 2 + HALF_BONE; 
	private int centre_y;
	@Setter (AccessLevel.PUBLIC) @Getter (AccessLevel.PUBLIC) private int lx, ly, rx, ry;
	@Setter (AccessLevel.PUBLIC) @Getter (AccessLevel.PUBLIC) private int lrow = 0, rrow = 1;
	private JScrollPane tableScrollPane;
	private JPanel tablePanel;
	@Getter (AccessLevel.PUBLIC) private JButton leftButton, rightButton;
	private Bone leftBone, rightBone;
	private Insets insets;
	@Setter (AccessLevel.PUBLIC) @Getter (AccessLevel.PUBLIC) private boolean noRotate = false;
	
	/**
	 * calculateMaxTableHeight
	 * @param pips
	 * @return max height a table could be depending on domino set
	 */
	private int calculateMaxTableHeight(int pips) {
		int maxBones = ((pips + 1) * (pips + 2)) / 2; //maximum bones which could be on table
		int minBonesPerRow = MAX_WIDTH / WHOLE_BONE; //min bones which could make up one row
		int maxRows = maxBones / minBonesPerRow;
		int rowHeight = WHOLE_BONE + HALF_BONE;
		int padding = WHOLE_BONE * 2;
		return 2 * (maxRows * rowHeight + padding); //players could add to only one side all game, 
													//making potential max height double the max height of one game
	}
	
	/**
	 * setButton
	 * Positions a button at x and y coordinates
	 * @param button
	 * @param x
	 * @param y
	 */
	private void setButton(JButton button, int x, int y) {
		int w = WHOLE_BONE;
		int h = HALF_BONE;
		if (isInEndZone(x)) {
			x -= HALF_BONE + E_PAD;
    		y += button == leftButton ? ((WHOLE_BONE + E_PAD) * -1) : (HALF_BONE + E_PAD);
    		w = HALF_BONE;
    		h = WHOLE_BONE;
		}
		else if (isInStartZone(x)) {
			x += WHOLE_BONE + E_PAD;
    		y += button == leftButton ? ((WHOLE_BONE + E_PAD) * -1) : HALF_BONE + E_PAD;
    		w = HALF_BONE;
    		h = WHOLE_BONE;
		}
		Rectangle r = new Rectangle(x, y, w, h);
		
		SwingUtilities.invokeLater(new DominoUIImplUpdater(r, button) {
    		@Override
			public void run() {
    			this.getButton().setBounds(this.getRectangle());
			}
		});
		
		y += button == leftButton ? (HALF_BONE * -1): HALF_BONE;		
		r = new Rectangle(x, y, w, h);
		
		SwingUtilities.invokeLater(new DominoUIImplUpdater(r) {
    		@Override
			public void run() {
    			tablePanel.scrollRectToVisible(this.getRectangle());
			}
		});
	}
	
	/**
	 * addBoneIcon
	 * Calculates coordinates for boneIcon label to be added
	 * @param bone
	 * @param nextX
	 * @param nextY
	 * @param row
	 * @param leftOrRight
	 */
	private void addBoneIcon(Bone bone, int nextX, int nextY, int row, int leftOrRight) { 
		BoneIcon boneIcon = new BoneIcon(bone);
		boolean noRotate = false;
		boolean left = leftOrRight == Play.LEFT;
		int thisX = nextX, thisY = nextY;
		if (isNearEdge(thisX))
    		noRotate  = true; //prevents a double from rotating near the extremes of the table
		if (!(isInEndZone(thisX) || isInStartZone(thisX))) {
			if (!isOnReverseTrack(row)) {
	        	if (isDouble(bone) && !noRotate) {
	        		boneIcon.rotate();
	        		thisY -= (WHOLE_BONE - HALF_BONE) / 2;
	        		nextX = nextX + E_PAD + HALF_BONE;
	        	}
	        	else {
	        		if (left)
	        			boneIcon.rotate180();
	        		nextX = nextX + E_PAD + WHOLE_BONE;
	        	}
        	}
        	if (isOnReverseTrack(row)) {
        		if (isDouble(bone) && !noRotate) {
            		boneIcon.rotate();
            		thisX = nextX + HALF_BONE;
            		thisY -= (WHOLE_BONE - HALF_BONE) / 2;
            		nextX = thisX - (E_PAD + WHOLE_BONE);
            	}
            	else {
            		if (!left)
            			boneIcon.rotate180();
            		nextX = nextX - (E_PAD + WHOLE_BONE);
            	}
        	}
    	}
    	else {
    		boneIcon.rotate();
	    	if (isInEndZone(thisX)) {
	    		thisX -= HALF_BONE + E_PAD;
	    		nextX = thisX - HALF_BONE;
	    	}
	    	else { //bone is in start zone
	    		thisX += WHOLE_BONE + E_PAD;
	    		nextX = thisX;
	    	}
	    	thisY += left ? ((WHOLE_BONE + E_PAD) * -1) : HALF_BONE + E_PAD;
	    	nextY = left ? thisY - (HALF_BONE + E_PAD) : thisY + WHOLE_BONE + E_PAD;
    		row++;
    	}
		if (left) {
			setLx(nextX);
			setLy(nextY);
			setLrow(row);
		}
		else {
			setRx(nextX);
			setRy(nextY);
			setRrow(row);
		}
		addBoneLabel(boneIcon, thisX, thisY);
	}
	
	/**
	 * addBoneLabel
	 * Adds an icon label to table at x and y coordinates
	 * @param icon
	 * @param x
	 * @param y
	 */
	private void addBoneLabel(BoneIcon icon, int x, int y) {
		SwingUtilities.invokeLater(new DominoUIImplUpdater(icon, x, y) {
    		@Override
			public void run() {
    			JLabel boneLabel = new JLabel();
    			Dimension size = new Dimension(this.getIcon().getIconWidth(), this.getIcon().getIconHeight());
    	    	Rectangle rect = new Rectangle(this.getX() + insets.left, this.getY() + insets.top, size.width, size.height);
    			boneLabel.setIcon(this.getIcon());
    			tablePanel.add(boneLabel);
    	    	boneLabel.setBounds(rect);
			}
		});
	}
	
	/**
	 * Constructor
	 * @param pips
	 */
	public TablePanel(int pips) {
		super();
    	this.pips = pips;
    	tableSize = 0;
    	tableHeight = calculateMaxTableHeight(this.pips);
    	centre_y = tableHeight / 2 - HALF_BONE;
    	tablePanel = new JPanel();
        tablePanel.setBackground(tableColour);
        tablePanel.setLayout(null);
        tablePanel.setPreferredSize(new Dimension(MAX_WIDTH, tableHeight));
        tablePanel.setVisible(true);
        insets = tablePanel.getInsets();
        leftButton = new JButton("L");
        rightButton = new JButton("R");
        leftButton.setToolTipText(BUTTON_TOOL_TIP_TEXT);
        rightButton.setToolTipText(BUTTON_TOOL_TIP_TEXT);
        tablePanel.add(leftButton);
        tablePanel.add(rightButton);
        styleButton(leftButton);
        styleButton(rightButton);
        leftButton.setFocusable(false);
        rightButton.setFocusable(false);
        tableScrollPane = new JScrollPane(tablePanel);
        tableScrollPane.setPreferredSize(DominoesStyle.getTableDimension());
        tableScrollPane.setBorder(null);
        add(tableScrollPane);
        setOpaque(false);
        clearTable();
    }
	
	/**
	 * hoverButton
	 * Style of table button on mouse over
	 * @param button
	 */
	public void hoverButton(JButton button) {
		SwingUtilities.invokeLater(new DominoUIImplUpdater(button) {
    		@Override
			public void run() {
    			this.getButton().setBackground(Color.WHITE);
			}
		});
	}
	
	/**
	 * styleButton
	 * Default style of table button
	 * @param button
	 */
	public void styleButton(JButton button) {
		SwingUtilities.invokeLater(new DominoUIImplUpdater(button) {
    		@Override
			public void run() {
    			this.getButton().setBackground(tablePanel.getBackground());
    	        this.getButton().setBorder(BorderFactory.createLineBorder(Color.BLUE));
			}
		});
	}
	
	/**
	 * clearTable
	 * Clears table of all elements, resets layout variables
	 */
	public void clearTable() {
		tablePanel.removeAll();
		tablePanel.revalidate();
		tablePanel.repaint();
		tableSize = 0;
        lx = rx = centre_x;
        ly = ry = centre_y;
        lrow = 0;
        rrow = 1;
        tablePanel.add(leftButton);
        tablePanel.add(rightButton);
        tableScrollPane.remove(tableScrollPane.getVerticalScrollBar());
        tableScrollPane.setWheelScrollingEnabled(false);
    }
	
	/**
	 * display
	 * Displays a domino game table.
	 * @param table
	 */
	public void display(Table table) {		
		int newTableSize = table.layout().length;
    	if (tableSize != newTableSize) {
    		tableSize = table.layout().length;
    		int thisX;
    		int thisY;
    		if (table.layout().length == 1) { //first bone added
    			leftBone = table.layout()[0];
    			BoneIcon boneIcon = new BoneIcon(leftBone);
    			thisX = lx;
    			thisY = ly;
    			if (isDouble(leftBone)) {
	        		boneIcon.rotate();
	        		thisY -= (WHOLE_BONE - HALF_BONE) / 2;
	        		rx = rx + E_PAD + HALF_BONE;
	        		lx = lx - (E_PAD + WHOLE_BONE);
	        	}
	        	else {
	        		rx = rx + E_PAD + WHOLE_BONE;
	        		lx = lx - (E_PAD + WHOLE_BONE);
	        	}
    			addBoneLabel(boneIcon, thisX, thisY);
            	setButton(leftButton, lx, ly);
            	setButton(rightButton, rx, ry);
            	Rectangle rect = new Rectangle(lx, ly + DominoesStyle.getTableHeight() / 2 + HALF_BONE, 1, 1);
            	
            	SwingUtilities.invokeLater(new DominoUIImplUpdater(rect) {
            		@Override
        			public void run() {
            			tablePanel.scrollRectToVisible(this.getRectangle());
        			}
        		});
            }
    		else if (leftBone != table.layout()[0]) { //a bone has been added to left of table
    			leftBone = table.layout()[0];
    			addBoneIcon(leftBone, lx, ly, lrow, Play.LEFT);
    			setButton(leftButton, lx, ly);
    		}
    		else { //a bone has been added to right of table
    			rightBone = table.layout()[newTableSize - 1];
    			addBoneIcon(rightBone, rx, ry, rrow, Play.RIGHT);
    			setButton(rightButton, rx, ry);
    		}
    		if ((ry + WHOLE_BONE) - (ly - WHOLE_BONE) > DominoesStyle.getTableHeight()) { //allow users to scroll when height of game approaches height of viewport
    			SwingUtilities.invokeLater(new DominoUIImplUpdater() {
    	    		@Override
    				public void run() {
    	    			tableScrollPane.setVerticalScrollBar(new JScrollBar());
    	    			tableScrollPane.getVerticalScrollBar().setUnitIncrement(TablePanel.HALF_BONE);
    	    			tableScrollPane.setWheelScrollingEnabled(true);
    				}
    			});
    		}
    		SwingUtilities.invokeLater(new DominoUIImplUpdater() {
	    		@Override
				public void run() {
	    			tablePanel.revalidate();
	        		tablePanel.repaint();
				}
			});
    	}
	}
	
	/**
	 * isInEndZone
	 * @param x
	 * @return true if x is in table end zone (near right hand edge)
	 */
	public boolean isInEndZone(int x) { 
    	return x > MAX_WIDTH;
    }
    
	/**
	 * isInStartZone
	 * @param x
	 * @return true if x is in table start zone (near left hand edge)
	 */
    public boolean isInStartZone(int x) {
    	return x < HALF_BONE / 2;
    }
    
    /**
     * isNearEdge
     * @param x
     * @return true if x is a bone's length from either edge zone
     */
    public boolean isNearEdge(int x) {
    	return isInEndZone(x + WHOLE_BONE) || isInStartZone(x - (WHOLE_BONE));
    }
    
    /**
     * isOnReverseTrack
     * @param row
     * @return true if row index has reverse direction
     */
    public boolean isOnReverseTrack(int row) {
    	return row % 2 == 0;
    }
    
    /**
     * isDouble
     * @param bone
     * @return true if bone is a double
     */
    public boolean isDouble(Bone bone) {
    	return bone.left() == bone.right();
    }

}