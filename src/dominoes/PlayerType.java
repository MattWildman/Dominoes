/**
 * 
 */
package dominoes;

/**
 * 
 * @author Ben Griffiths and Matt Wildman
 * MSc Computer Science
 * 31/03/13
 * PlayerType
 * Describes the type of a Player object, which defines its playing strategy
 * VERY_EASY - a bot whose plays use the strategy in PlayFactory.veryEasyPlay()
 * EASY - a bot whose plays use the strategy in PlayFactory.EasyPlay()
 * MEDIUM - a bot whose plays use the strategy in PlayFactory.mediumPlay()
 * HARD - a bot whose plays use the strategy in PlayFactory.hardPlay()
 * INTERACTIVE - a human player
 */
public enum PlayerType {
		VERY_EASY, EASY, MEDIUM, HARD, INTERACTIVE
}
