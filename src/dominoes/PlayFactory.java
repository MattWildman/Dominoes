package dominoes;

import java.util.ArrayList;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Ben Griffiths and Matt Wildman
 * MSc Computer Science
 * 31/03/13
 * PlayFactory
 * Used by Player objects to create Play objects, which represent a move on the table in a game of dominoes
 */
public class PlayFactory {
	
	private static final long THINKING_TIME = 2000;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private boolean playToOpponentsWeaknesses = false;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private PlayerType playerType;	// VERY_EASY, EASY, MEDIUM, OR HARD EXPECTED
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private ArrayList<Bone> playerHand;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Table table;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private ArrayList<Integer> opponentsWeaknesses;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private ArrayList<Integer> mySuits;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private int sizeOfOpponentsHand;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private volatile int maxPips; // DOES THIS NEED TO BE VOLATILE?
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private ArrayList<Bone> bonesPlayed;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private ArrayList<Bone> bonesRemaining;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private int pointsInMyHand;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private int pointsRemaining;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private int boneYardSize;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PUBLIC) private boolean pauseForThinking = true;
	
	/**
	 * makePlay
	 * @param possiblePlays - an arrayList of prioritised plays
	 * @return the first (highest priority) play in the list
	 */
	private Play makePlay(ArrayList<PlayOutcome> possiblePlays) {
		if(possiblePlays.isEmpty())
			return null; // hand is empty, or no bone in the hand can be played on the current table layout
		else return possiblePlays.get(0).getPlay(); // return the first possible play
	}
	
	/**
	 * veryEasyPlay
	 * Averages 31-33% win rate against easy in tests, 25-28% against Medium, and 20-22% against Hard
	 * Hard player won 5834 games
	 * The weakest play stategy, which actively trys to play to its opponent's weaknesses
	 * @return a play object representing the chosen play
	 */
	private Play veryEasyPlay() {
		this.analyseStateOfGame();
		this.setPlayToOpponentsWeaknesses(true);
		this.setPlayerHand(sortByWeight(this.getPlayerHand(), false)); // sort into ascending order of weight
		ArrayList<PlayOutcome> possiblePlays = possiblePlays(this.getPlayerHand(), this.getTable());
		possiblePlays = sortByPriority(possiblePlays, false); // sort by ascending order of priority (lowest priority first)
		return makePlay(possiblePlays);
	}

	/**
	 * easyPlay
	 * Averages 65-67% win rate against very Easy in tests, 41-43% against Medium, and 33-35% against Hard
	 * Medium player won 4166 games
	 * A weak play strategy. Go through each bone in the hand. If the bone can be played on the left end of the table layout, 
     * this bone should be played. Else, if it can be played on the right end of the table layout, it should be played.
	 * @return a Play object representing a valid play from the hand onto the table, or null if no valid play exists
	 */
	private Play easyPlay() {	
		ArrayList<PlayOutcome> possiblePlays = possiblePlays(this.getPlayerHand(), this.getTable());
		return makePlay(possiblePlays);
	}
	
	/**
	 * mediumPlay
	 * Averages 71-73% win rate against Very Easy in tests, 55-77% against Easy, and 40-42% against Hard
	 * Plays doubles first, and plays bones in weight order.
	 * @param table
	 * @return a Play object representing a valid play from the hand onto the table, or null if no valid play exists
	 */
	private Play mediumPlay() {
		ArrayList<Bone> sortedHand = new ArrayList<Bone>();		// to hold a sorted copy of the bones in hand
		ArrayList<Bone> singleBones = new ArrayList<Bone>();	// temporary holder for non-doubles in hand
		// first copy doubles into one list and non-doubles into the other
		for(Bone bone : this.getPlayerHand()) {
			if(bone.left() == bone.right())
				sortedHand.add(bone);
			else singleBones.add(bone);
		}
		// sort both lists by weight (heaviest first), then add the non-doubles onto the end of the sortedHand
		sortedHand = sortByWeight(sortedHand, true);
		singleBones = sortByWeight(singleBones, true);
		for(Bone bone : singleBones)
			sortedHand.add(bone);
		ArrayList<PlayOutcome> possiblePlays = possiblePlays(sortedHand, this.getTable());
		return makePlay(possiblePlays); // play with the first valid bone in the sorted hand
	}
	
	/**
	 * hardPlay
	 * The strongest play strategy. Improves upon medium's strategy by 
	 * (i) attempting to block the game if leading and the boneyard is empty
	 * (ii) trying to avoid playing a bone upon a weak suit for its opponent
	 * (iii) trying not to leave its own weak suits exposed on the table
	 * @return a play object
	 */
	private Play hardPlay() {
		this.analyseStateOfGame();
		/* Attempt to block the game, by passing instead of playing, if I'm in the lead, 
        opponent is weak, and boneYard is empty.*/
		if((this.getOpponentsWeaknesses().contains(this.getTable().left())) && (this.getOpponentsWeaknesses().contains(this.getTable().right())) 
			&& (this.getBoneYardSize() == 0) && (this.getPointsInMyHand() < this.getPointsRemaining()))
				return null;
		this.setPlayToOpponentsWeaknesses(true);
		setPlayerHand(sortByWeight(getPlayerHand(), true)); // sort by descending order of weight
		ArrayList<PlayOutcome> possiblePlays = possiblePlays(getPlayerHand(), getTable());
		possiblePlays = sortByPriority(possiblePlays, true); // sort by descending order of priority
		return makePlay(possiblePlays);
	}
	
	private ArrayList<PlayOutcome> possiblePlays(ArrayList<Bone> playerHand, Table table) {
		ArrayList<PlayOutcome> possiblePlays = new ArrayList<PlayOutcome>();
		for(Bone bone : playerHand) {
			if(bone.left() == table.left() || bone.right() == table.left())
				possiblePlays.add(new PlayOutcome(bone, Play.LEFT, table.left(), table.right(),
								  this.isPlayToOpponentsWeaknesses(), this.getOpponentsWeaknesses(),
								  this.getMySuits()));
			if(bone.left() == table.right()|| bone.right() == table.right())
				possiblePlays.add(new PlayOutcome(bone, Play.RIGHT, table.right(), table.left(),
						  		  this.isPlayToOpponentsWeaknesses(), this.getOpponentsWeaknesses(),
						  		  this.getMySuits()));
		}
		return possiblePlays;
	}
	
	/**
	 * analyseStateOfGame
	 * Calculates stats on the current position of the game and analyses the potential current state of the opponent's hand.
	 * This information is used by the hard and very easy play strategies
	 */
	private void analyseStateOfGame() {
		this.setBonesPlayed(new ArrayList<Bone>());
		this.setBonesRemaining(new ArrayList<Bone>());
		for(int i = 0; i < this.getTable().layout().length; i++) // find out which bones have been played on the table
			this.getBonesPlayed().add(this.getTable().layout()[i]);
		for(int i = 0; i <= this.getMaxPips(); i++) { // find out which bones have not yet been played
			for(int j = 0; j <= i; j++) {
				Bone bone = new Bone(i, j);
				if(!(this.getBonesPlayed().contains(bone)))
					this.getBonesRemaining().add(bone);
			}		
		}
        int numberOfBonesInSet = (((this.getMaxPips() * (this.getMaxPips() + 1))) / 2) + this.getMaxPips() + 1;
        int totalPointsPossible = numberOfBonesInSet * this.getMaxPips();
        int totalPointsPlayed = 0;
        for(int i = 0; i < this.getBonesPlayed().size(); i++) {
        		Bone bone = this.getBonesPlayed().get(i);
        		totalPointsPlayed += bone.left();
        		totalPointsPlayed += bone.right();
        	
        }
        this.setPointsInMyHand(0);
        for(int i = 0; i < this.getPlayerHand().size(); i++)
        	this.setPointsInMyHand(this.getPointsInMyHand()
					+ (this.getPlayerHand().get(i).left() + this.getPlayerHand().get(i).right()));
        this.setPointsRemaining(totalPointsPossible - totalPointsPlayed - this.getPointsInMyHand());
        this.setBoneYardSize(numberOfBonesInSet - this.getTable().layout().length - this.getPlayerHand().size() - this.getSizeOfOpponentsHand());
        
		// Which suits do I have in my hand?
		this.setMySuits(new ArrayList<Integer>());
		for(Bone bone : this.getPlayerHand()) {
			this.getMySuits().add(bone.left());
			this.getMySuits().add(bone.right());
		}
		
		for(int i = 0; i < this.getBonesRemaining().size(); i++)
        	if((!this.getOpponentsWeaknesses().contains(i)) && (prevalence(this.getBonesRemaining(), i) < 4)) // MAGIC NUMBER. 4 SEEMS TO BE SWEET SPOT
                    this.getOpponentsWeaknesses().add(i);	
	}
	
	/**
	 * sortByPriority
	 * Sorts a list of play outcomes into order of priority
	 * @param possiblePlays - an array of possible plays, together with their priorities
	 * @param descendingOrder - true if the list is to be sorted into descending order, false if ascending
	 * @return the sorted list
	 */
	private ArrayList<PlayOutcome> sortByPriority(
			ArrayList<PlayOutcome> possiblePlays, boolean descendingOrder) {
		for(int i = 0; i < possiblePlays.size(); i++)	{
			for(int j = i + 1; j < possiblePlays.size(); j++)	{
				boolean sortCondition = (descendingOrder ? 
										 possiblePlays.get(i).getPriority() < possiblePlays.get(j).getPriority() :
									     possiblePlays.get(i).getPriority() > possiblePlays.get(j).getPriority());
				if(sortCondition)	{
					PlayOutcome temp = possiblePlays.get(i);
					possiblePlays.set(i, possiblePlays.get(j));
					possiblePlays.set(j, temp);
				}
			}
		}
		return possiblePlays;	
	}
	
	/**
	 * prevalence
	 * @param playerHand - an array of bones representing a player's hand
	 * @param suit - an integer representing one of the possible values on the face of a bone
	 * @return the number of occurrences of the suit in the player's hand
	 */
	private int prevalence(ArrayList<Bone> playerHand, int suit) {
		int occurrences = 0;
        for(Bone bone : playerHand) {
        	if((suit == bone.left()) || (suit == bone.right()))
        		occurrences++;
        }
        return occurrences;
	}

	/**
	 * sortByWeight
	 * Used to sort a list of bones into weight-order
	 * @param bones an ArrayList<Bone>
	 * @param descendingOrder - if true, the list is sorted into descending order of weight,
	 * otherwise into ascending order of weight
	 * @return the ArrayList<Bone>, sorted into descending order of weight
	 */
	private ArrayList<Bone> sortByWeight(ArrayList<Bone> bones, boolean descendingOrder)	{
		for(int i = 0; i < bones.size(); i++)	{
			for(int j = i + 1; j < bones.size(); j++)	{
				boolean sortCondition = (descendingOrder ? 
										 bones.get(i).weight() < bones.get(j).weight() :
										 bones.get(i).weight() > bones.get(j).weight());
				if(sortCondition)	{
					Bone tempBone = bones.get(i);
					bones.set(i, bones.get(j));
					bones.set(j, tempBone);
				}
			}
		}
		return bones;
	}
	
	/**
	 * Constructor
	 * @param playerType - the type of player (level of AI) that this play factory will create plays for
	 */
	public PlayFactory(PlayerType playerType)	{
		this.setPlayerType(playerType);
	}
	
	/**
	 * createPlay
	 * Returns a play object that represents a valid play on table from playerHand. If no valid play exists, it returns null.
	 * If member playerType is not one of VERY_EASY, EASY, MEDIUM, or HARD, it returns null.
	 * @param playerHand
	 * @param table
	 * @return a new Play object
	 */
	public Play createPlay(ArrayList<Bone> playerHand, Table table, ArrayList<Integer> opponentsWeaknesses, 
						   int sizeOfOpponentsHand, int maxPips) {
		if(this.isPauseForThinking()) {
			try	{
				Thread.sleep(THINKING_TIME); // wait for 2 secondS before making move
			}
			catch(InterruptedException e)	{
				// make a play
			}
		}
		this.setPlayerHand(playerHand);
		this.setTable(table);
		this.setOpponentsWeaknesses(opponentsWeaknesses);
		this.setSizeOfOpponentsHand(sizeOfOpponentsHand);
		this.setMaxPips(maxPips);
		
		if(getPlayerType() == PlayerType.VERY_EASY)
			return veryEasyPlay();
		if(getPlayerType() == PlayerType.EASY)
			return easyPlay();
		if(getPlayerType() == PlayerType.MEDIUM)
			return mediumPlay();
		if(getPlayerType() == PlayerType.HARD)
			return hardPlay();
		
		return null; // unrecognised PlayerType
	}
}
