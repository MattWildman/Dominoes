package dominoes;

/**
 * @author Ben Griffiths and Matt Wildman
 * MSc Computer Science
 * 31/03/13
 * PlayerActionType
 * Represents one of the set of actions that a player in a game of dominoes can make:
 * PLAY_BONE - the player takes a bone out of its hand and plays it on the table
 * TAKE_BONE - the player adds a bone to its hand
 * DRAW - the player draws a bone from the boneyard
 * PASS - the player passes its turn
 * START_TURN - the player starts taking its turn
 * END_TURN - the player finishes taking its turn
 * NEW_ROUND - the player starts a new round of dominoes
 */
public enum PlayerActionType {
	PLAY_BONE, TAKE_BONE, DRAW, PASS, START_TURN, END_TURN, NEW_ROUND;
}
