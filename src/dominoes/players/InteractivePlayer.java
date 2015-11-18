package dominoes.players;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import dominoes.CantPlayException;
import dominoes.CubbyHole;
import dominoes.Play;
import dominoes.PlayerAction;
import dominoes.PlayerActionType;
import dominoes.PlayerType;
import dominoes.Table;

/**
 * 
 * @author Ben Griffiths and Matt Wildman 
 * MSc Computer Science 
 * 31/03/2013
 * InteractivePlayer 
 * @extends Player This class represents an interactive player in a game of dominoes
 */
public class InteractivePlayer extends Player {

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PRIVATE)
	private CubbyHole cubbyHole;

	/**
	 * Constructor
	 * 
	 * @param playerType - not used
	 * @param maxPips - not used
	 */
	public InteractivePlayer(PlayerType playerType, int maxPips) {
		super(PlayerType.INTERACTIVE, maxPips);
		setCubbyHole(new CubbyHole());
	}

	/**
	 * Pick a bone to play and where it should be played
	 * 
	 * @param table - The current table configuration so the player can select a valid bone to play
	 * @return a bone and which end of the layout to place it wrapped up in a Play object.
	 * @throws CantPlayException - thrown when the player has no bones to play
	 */
	@Override
	public Play makePlay(Table table) throws CantPlayException {
		while (isPauseIndefinitely())
			; // loop until told to continue
		setChanged();
		notifyObservers(new PlayerAction(PlayerActionType.START_TURN));
		Object cubbyContents;
		checkIfInterrupted(); // check if interrupted immediately before entering cubbyhole
		cubbyContents = getCubbyHole().get();
		checkIfInterrupted(); // Check again immediately after exiting cubbyhole
		/*
		 * Try casting the contents to a Play object. If successful, Player has
		 * made a play. If unsuccessful, the Player pressed 'Take a Bone'
		 */
		try {
			Play myPlay = (Play) cubbyContents;
			removeFromHand(myPlay.bone());
			setChanged();
			notifyObservers(new PlayerAction(PlayerActionType.END_TURN));
			return myPlay;
		} catch (ClassCastException cce) {
			setChanged();
			notifyObservers(new PlayerAction(PlayerActionType.END_TURN));
			throw new CantPlayException(this.getName()
					+ " pressed 'Take a bone'");
		}
	}
}
