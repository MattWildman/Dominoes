package dominoes;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Ben Griffiths and Matt Wildman
 * MSc Computer Science
 * 31/03/13
 * PlayerAction
 *
 */
public class PlayerAction {

	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PRIVATE) private PlayerActionType actionType;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PRIVATE) private Bone bone;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PRIVATE) private int boneIndex; 
    
	/**
	 * Constructor
	 * @param actionType
	 * @param bone
	 * @param boneIndex
	 */
    public PlayerAction(PlayerActionType actionType, Bone bone, int boneIndex) {
            this.setBone(bone);
            this.setActionType(actionType);
            this.setBoneIndex(boneIndex);
    }
    
    /**
     * Constructor
     * @param PlayerActionType
     */
    public PlayerAction(PlayerActionType actionType) {
    	this(actionType, null, -1);
    }
}