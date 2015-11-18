/**
 * 
 */
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
 * PlayOutcome
 * A PlayOutcome represents a potential play in a game of Dominoes, and the state of the game after that play
 */
public class PlayOutcome {
	
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Bone bone;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private int endOfTable; // possible values: Play.LEFT and Play.RIGHT
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private int coveredSuitOnThisEnd; // the suit that this play will be made upon
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private int exposedSuitOnThisEnd; // the suit that will be exposed on the end of the table this play is made on
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private int exposedSuitOnOtherEnd;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private boolean playToOpponentsWeaknesses;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private ArrayList<Integer> opponentsWeaknesses;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private ArrayList<Integer> mySuits;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PRIVATE) private Play play;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PRIVATE) private int priority = 0; // the higher the priority, the more favourable the play
	
	/**
	 * assignPriority
	 * Assigns a priority to the play that this PlayOutcome contains.
	 * The more likely the play is to lead to a winning position, the higher the priority
	 */
	private void assignPriority() {
		if(bone.left() == bone.right())
			this.incrementPriority(); // double scores + 1 priority
		if(this.isPlayToOpponentsWeaknesses()) {
			ArrayList<Integer> mySuitsAfterThisPlay = new ArrayList<Integer>(this.getMySuits());
			for(int i = 0; i < mySuitsAfterThisPlay.size(); i++) { // remove bone.left() from my suits
				if(mySuitsAfterThisPlay.get(i) == bone.left()) {
					mySuitsAfterThisPlay.remove(i);
					break;
				}
			}
			for(int i = 0; i < mySuitsAfterThisPlay.size(); i++) { // remove bone.right() from my suits
				if(mySuitsAfterThisPlay.get(i) == bone.right()) {
					mySuitsAfterThisPlay.remove(i);
					break;
				}
			}
			if(this.getOpponentsWeaknesses().contains(this.getCoveredSuitOnThisEnd()))
				this.decrementPriority(); // -1 if we are covering a weak suit for our opponent
			if(getOpponentsWeaknesses().contains(this.getExposedSuitOnOtherEnd()))
				this.incrementPriority(); // + 1 if we are avoiding covering a weak suit for our opponent
			if(!mySuitsAfterThisPlay.contains(this.getCoveredSuitOnThisEnd()))
				this.incrementPriority(); // +1 if we are covering a weak suit for ourselves
			if(getOpponentsWeaknesses().contains(this.getExposedSuitOnThisEnd()))
				this.incrementPriority(); // + 1 if we leave a weak suit for our opponent
			if(!mySuitsAfterThisPlay.contains(this.getExposedSuitOnThisEnd()))
				this.decrementPriority(); // -1 if we will leave a weak suit for ourselves
			if(this.getExposedSuitOnThisEnd() == this.getExposedSuitOnOtherEnd()) { // if both sides will have same suit...
				if(mySuitsAfterThisPlay.contains(this.getExposedSuitOnThisEnd()))
					this.incrementPriority(); // +1 if we have that suit
				if(getOpponentsWeaknesses().contains(this.getExposedSuitOnThisEnd()))
					this.incrementPriority(); // +1 if that suit is a weakness for our opponent
			}
		}
		
	}
	
	/**
	 * decrementPriority
	 */
	private void decrementPriority() {
		this.priority--;
	}
	
	/**
	 * incrementPriority
	 */
	private void incrementPriority() {
		this.priority++;
	}
	
	/**
	 * Constructor
	 * @param bone
	 * @param endOfTable
	 * @param suitOnThisEndOfTable
	 * @param suitOnOtherEndOfTable
	 * @param playToOpponentsWeaknesses
	 * @param opponentsWeaknesses
	 * @param mySuits
	 */
	public PlayOutcome(Bone bone, int endOfTable, int suitOnThisEndOfTable, int suitOnOtherEndOfTable,
					   boolean playToOpponentsWeaknesses, ArrayList<Integer> opponentsWeaknesses, 
					   ArrayList<Integer> mySuits) {
		this.setBone(bone);
		this.setEndOfTable(endOfTable);
		this.setPlay(new Play(bone, endOfTable));
		if(bone.left() == suitOnThisEndOfTable)
			this.setExposedSuitOnThisEnd(bone.right());
		else this.setExposedSuitOnThisEnd(bone.left());
		this.setCoveredSuitOnThisEnd(suitOnThisEndOfTable);
		this.setExposedSuitOnOtherEnd(suitOnOtherEndOfTable);
		this.setPlayToOpponentsWeaknesses(playToOpponentsWeaknesses);
		this.setOpponentsWeaknesses(opponentsWeaknesses);
		this.setMySuits(mySuits);
		this.assignPriority();
	}
}
