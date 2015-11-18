package dominoes.players;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CancellationException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import dominoes.Bone;
import dominoes.BoneYard;
import dominoes.CantPlayException;
import dominoes.Play;
import dominoes.PlayFactory;
import dominoes.PlayerAction;
import dominoes.PlayerActionType;
import dominoes.PlayerType;
import dominoes.Table;

/**
 * @author Ben Griffiths and Matt Wildman 
 * MSc Computer Science 
 * 31/03/2013
 * Player
 * @extends Observable - allowing UI components and other players to observe it.
 * @implements Observer - allowing it to observe its opponent
 * @implements DominoPlayer - the provided player interface
 */
public class Player extends Observable implements DominoPlayer, Observer {

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private String name;
	@Getter(AccessLevel.PUBLIC)
	private int points;
	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private PlayerType playerType;
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private ArrayList<Bone> hand;
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private volatile int maxPips;
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private int sizeOfOpponentsHand;
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private ArrayList<Integer> opponentsWeaknesses;
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private Table table;
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private PlayerAction lastAttemptedPlay;
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PUBLIC)
	private boolean pauseForThinking = true;
	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private volatile boolean pauseIndefinitely = false;
	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private volatile boolean stopped;

	/**
	 * checkIfInterrupted
	 * 
	 * @throws CancellationException. Causes
	 *             the currently executing GameRunnable object to jump out of
	 *             its run() method and terminate without completing the game.
	 */
	protected void checkIfInterrupted() throws CancellationException {
		if (this.isStopped())
			throw new CancellationException();
	}

	/**
	 * removeFromHand
	 * 
	 * @param bone
	 *            - the bone to remove
	 * @return the bone removed from hand
	 */
	protected Bone removeFromHand(Bone bone) {
		int boneIndex = getHand().indexOf(bone);
		getHand().remove(bone);
		PlayerAction playerAction = new PlayerAction(
				PlayerActionType.PLAY_BONE, bone, boneIndex);
		setChanged();
		notifyObservers(playerAction);
		setLastAttemptedPlay(playerAction);
		return bone;
	}

	/**
	 * Constructor Creates a new player of the specified playerType
	 * 
	 * @param playerType
	 *            - very easy, easy, medium, hard or interactive
	 */
	public Player(PlayerType playerType, int maxPips) {
		this.setStopped(false);
		this.setMaxPips(maxPips);
		this.setSizeOfOpponentsHand(0);
		this.setPlayerType(playerType);
		setHand(new ArrayList<Bone>());
		setPoints(0);
		setChanged();
		notifyObservers();
	}

	/**
	 * bonesInHand The bones in the player's hand
	 * 
	 * @return an array of bones that are in the player's hand
	 */
	@Override
	public Bone[] bonesInHand() {
		return getHand().toArray(new Bone[getHand().size()]);
	}

	/**
	 * draw Draw a tile from the given boneyard
	 * 
	 * @param boneyard
	 *            - The boneyard from which a tile is drawn
	 */
	@Override
	public void draw(BoneYard boneYard) {
		Bone nextBoneFromBoneYard = boneYard.draw();
		if (nextBoneFromBoneYard != null) {
			getHand().add(nextBoneFromBoneYard);
			setChanged();
			notifyObservers(new PlayerAction(PlayerActionType.DRAW));
			setChanged();
			notifyObservers(new PlayerAction(PlayerActionType.TAKE_BONE,
					nextBoneFromBoneYard, 0));
		} else {
			setChanged();
			notifyObservers(new PlayerAction(PlayerActionType.PASS));
		}
	}

	/**
	 * makePlay Pick a bone to play and where it should be played
	 * 
	 * @param table
	 *            - The current table configuration so the player can select a
	 *            valid bone to play
	 * @return a bone and which end of the layout to place it wrapped up in a
	 *         Play object.
	 * @throw CantPlayException thrown when the player has no bones to play
	 */
	@Override
	public Play makePlay(Table table) throws CantPlayException {
		while (isPauseIndefinitely())
			; // loop until told to continue
		checkIfInterrupted();
		this.setTable(table);
		setChanged();
		notifyObservers(new PlayerAction(PlayerActionType.START_TURN));
		PlayFactory playFactory = new PlayFactory(this.getPlayerType());
		playFactory.setPauseForThinking(this.isPauseForThinking());
		Play chosenPlay = playFactory.createPlay(this.getHand(), table,
				getOpponentsWeaknesses(), getSizeOfOpponentsHand(),
				getMaxPips());
		if (chosenPlay == null)
			throw new CantPlayException(); // Hand is empty, or no bone in hand
											// can be played
		removeFromHand(chosenPlay.bone());
		setChanged();
		notifyObservers(new PlayerAction(PlayerActionType.END_TURN));
		return chosenPlay;
	}

	/**
	 * newRound About to start a new round. Clear the player's hand.
	 */
	@Override
	public void newRound() {
		setHand(new ArrayList<Bone>());
		this.setOpponentsWeaknesses(new ArrayList<Integer>());
		setChanged();
		notifyObservers(new PlayerAction(PlayerActionType.NEW_ROUND));
	}

	/**
	 * numInHand The number of bones the player has in its hand
	 * 
	 * @return how many bones the player has
	 */
	@Override
	public int numInHand() {
		return getHand().size();
	}

	/**
	 * takeBack Tell the player to take back the specified bone. This usually
	 * happens when the player has tried to play an invalid bone. The bone is
	 * removed from the player's list of played bones and added back to the
	 * player's hand.
	 * 
	 * @param bone
	 *            - The bone to take back
	 */
	@Override
	public void takeBack(Bone bone) {
		int boneIndex;
		if (lastAttemptedPlay != null
				&& bone.equals(getLastAttemptedPlay().getBone())) {
			boneIndex = getLastAttemptedPlay().getBoneIndex();
			bone.flip(); // bones always get flipped by an invalid play, so flip
							// it back
		} else
			boneIndex = numInHand();
		getHand().add(boneIndex, bone);
		setChanged();
		notifyObservers(new PlayerAction(PlayerActionType.TAKE_BONE, bone,
				boneIndex));
	}

	/**
	 * setPoints
	 * 
	 * @param points
	 */
	@Override
	public void setPoints(int points) {
		this.points = points;
	}

	/**
	 * update Used to keep track of the player's opponent.
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable opponent, Object objectPassed) {
		if (objectPassed != null) {
			if (objectPassed.getClass().equals(PlayerAction.class)) {
				PlayerAction playerAction = (PlayerAction) objectPassed;
				if (playerAction.getActionType() == PlayerActionType.PLAY_BONE)
					this.setSizeOfOpponentsHand(this.getSizeOfOpponentsHand() - 1);
				if (playerAction.getActionType() == PlayerActionType.DRAW
					|| playerAction.getActionType() == PlayerActionType.PASS) {
						if (playerAction.getActionType() == PlayerActionType.DRAW)
							this.setSizeOfOpponentsHand(this.getSizeOfOpponentsHand() + 1);
						if (getTable() != null) {
							if (!(this.getOpponentsWeaknesses().contains(getTable().left())))
								this.getOpponentsWeaknesses().add(getTable().left());
							if (!(this.getOpponentsWeaknesses().contains(getTable().right())))
								this.getOpponentsWeaknesses().add(getTable().right());
						}
				}
			}
		}
	}
}
