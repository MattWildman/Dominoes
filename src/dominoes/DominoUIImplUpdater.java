package dominoes;

import java.awt.Rectangle;

import javax.swing.JButton;

import lombok.AccessLevel;
import lombok.Getter;
import dominoes.players.DominoPlayer;

/**
 * 
 * @author Ben Griffiths and Matt Wildman
 * MSc Computer Science
 * 31/03/13
 * DominoUIImplUpdater
 * Executes changes to the interface on the event dispatch thread
 * @implements Runnable
 *
 */
public class DominoUIImplUpdater implements Runnable {
	
	@Getter(AccessLevel.PUBLIC) private DominoPlayer player;
	@Getter(AccessLevel.PUBLIC) private DominoPlayer[] bothPlayers;
	@Getter(AccessLevel.PUBLIC) private Table table;
	@Getter(AccessLevel.PUBLIC) private BoneYard boneYard;
	@Getter(AccessLevel.PUBLIC) private int index;
	@Getter(AccessLevel.PUBLIC) private BoneIcon icon;
	@Getter(AccessLevel.PUBLIC) private int x;
	@Getter(AccessLevel.PUBLIC) private int y;
	@Getter(AccessLevel.PUBLIC) private Rectangle rectangle;
	@Getter(AccessLevel.PUBLIC) private JButton button;
	@Getter(AccessLevel.PUBLIC) private boolean condition;
	
	/**
	 * Default constructor
	 */
	public DominoUIImplUpdater() {
	}
	
	/**
	 * Constructor
	 * @param bothPlayers
	 * @param table
	 * @param boneYard
	 */
	public DominoUIImplUpdater(DominoPlayer[] bothPlayers, Table table, BoneYard boneYard) {
		this.bothPlayers = bothPlayers;
		this.table = table;
		this.boneYard = boneYard;
	}
	
	/**
	 * Constructor
	 * @param player
	 */
	public DominoUIImplUpdater(DominoPlayer player) {
		this.player = player;
	}
	
	/**
	 * Constructor
	 * @param index
	 */
	public DominoUIImplUpdater(int index) {
		this.index = index;
	}
	
	/**
	 * Constructor
	 * @param icon
	 */
	public DominoUIImplUpdater(BoneIcon icon) {
		this.icon = icon;
	}
	
	/**
	 * Constructor
	 * @param icon
	 * @param x - coordinate
	 * @param y - coordinate
	 */
	public DominoUIImplUpdater(BoneIcon icon, int x, int y) {
		this.icon = icon;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Constructor
	 * @param rectangle
	 * @param button
	 */
	public DominoUIImplUpdater(Rectangle rectangle, JButton button) {
		this.rectangle = rectangle;
		this.button = button;
	}
	
	/**
	 * Constructor
	 * @param button
	 */
	public DominoUIImplUpdater(JButton button) {
		this.button = button;
	}
	
	/**
	 * Constructor
	 * @param rectangle
	 */
	public DominoUIImplUpdater(Rectangle rectangle) {
		this.rectangle = rectangle;
	}
	
	/**
	 * Constructor
	 * @param condition
	 */
	public DominoUIImplUpdater(boolean condition) {
		this.condition = condition;
	}
	
	@Override
	public void run() {
	}
}
