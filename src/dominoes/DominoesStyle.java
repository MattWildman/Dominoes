/**
 * 
 */
package dominoes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author MattWildman
 * Contains variables and dimension objects for GUI layout
 *
 */

public class DominoesStyle {
	
	private static final int H_PAD = 48;
	private static final int V_PAD = 30;
	private static final int SCROLL_BAR = 18;
	private static final int HALF_BONE = 25;
	@Getter (AccessLevel.PUBLIC) private static final int MESSAGE_AREA_COLUMNS = 51;
	@Getter (AccessLevel.PUBLIC) private static final int MESSAGE_AREA_ROWS = 3;
	
	private static int smallWidth = 180; //player panel width
	private static int largeWidth = 600; //central panel width
	
	private static int sharedDetailsWidth = smallWidth;
	private static int sharedDetailsHeight = smallWidth / 3;
	
	private static int playerDetailsWidth = smallWidth;
	private static int playerDetailsHeight = smallWidth / 2;
	
	@Getter private static int tableWidth = largeWidth;
	@Getter private static int tableHeight = largeWidth * 3/5;
	
	private static int boneYardWidth = largeWidth;
	private static int boneYardHeight = BoneYardPanel.getBoneYardCellHeight() * 2;
	
	private static int handWidth = smallWidth;
	private static int handHeight = tableHeight;
	
	private static int buttonWidth = smallWidth;
	private static int buttonHeight = smallWidth / 6;
	
	private static int messagePanelWidth = largeWidth - SCROLL_BAR;
	private static int messagePanelHeight = smallWidth;
	
	private static int centreContentWidth = largeWidth;
	private static int centreContentHeight = tableHeight + boneYardHeight + messagePanelHeight + buttonHeight + V_PAD;
	
	private static int windowWidth = smallWidth * 2 + largeWidth + H_PAD;
	private static int windowHeight = centreContentHeight + SCROLL_BAR;
	
	@Getter private static final Border TINY_PADDING = BorderFactory.createEmptyBorder(5,5,5,5);
	@Getter private static final Border SMALL_PADDING = BorderFactory.createEmptyBorder(10,10,10,10);
	@Getter private static final Border LARGE_PADDING = BorderFactory.createEmptyBorder(15,15,0,15);
	@Getter private static final Font MEDIUM_FONT = new Font(Font.SANS_SERIF,Font.BOLD,16);
	@Getter private static final Font SMALL_FONT = new Font(Font.SANS_SERIF,Font.BOLD,14);
	public static final Font WELCOME_FONT = new Font(Font.SANS_SERIF,Font.ITALIC,28);
	public static final Color BACKGROUND_COLOUR = new Color(188,210,238);
	public static final Color SECONDARY_COLOUR = new Color(230,230,250);
	
	public static int getWindowWidth() {
		return windowWidth;
	}

	public static Dimension getWindowDimension() {
		return new Dimension(getWindowWidth(), getWindowHeight());
	}

	public static Dimension getTableDimension() {
		return new Dimension(tableWidth, tableHeight);
	}
	
	public static Dimension getBoneYardDimension() {
		return new Dimension(boneYardWidth, boneYardHeight);
	}

	public static Dimension getHandDimension() {
		return new Dimension(handWidth, handHeight);
	}

	public static Dimension getSharedDetailsDimension() {
		return new Dimension(sharedDetailsWidth, sharedDetailsHeight);
	}

	public static Dimension getPlayerDetailsDimension() {
		return new Dimension(playerDetailsWidth, playerDetailsHeight);
	}

	public static Dimension getButtonDimension() {
		return new Dimension(buttonWidth, buttonHeight);
	}
	
	public static Dimension getCentreContentDimension() {
		return new Dimension(centreContentWidth, centreContentHeight);
	}
	
	public static Dimension getMessagePanelDimension() {
		return new Dimension(messagePanelWidth, messagePanelHeight);
	}

	public static void boneYardScrollBar(boolean scroll) {
		if (scroll) 
			boneYardHeight = HALF_BONE * 2 + V_PAD + SCROLL_BAR;
		else
			boneYardHeight = HALF_BONE * 2 + V_PAD;
	}

	private static int getWindowHeight() {
		return windowHeight;
	}

}
