package dominoes;

import java.util.concurrent.CancellationException;

import javax.swing.SwingUtilities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import dominoes.players.DominoPlayer;

/**
 * 
 * @author Ben Griffiths and Matt Wildman
 * MSc Computer Science
 * 31/03/13
 * GameRunnable
 * Creates a new game thread
 * @implements Runnable
 *
 */
public class GameRunnable implements Runnable {

	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Dominoes game;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private DominoUIImpl gui;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private DominoPlayer winner;
	
	public GameRunnable(DominoUIImpl gui, DominoPlayer player1, DominoPlayer player2, int scoreTarget, int maxPips) {
		this.setGui(gui);
		this.setGame(new Dominoes(gui, player1, player2, scoreTarget, maxPips));
	}

	@Override
	public void run() {
		try {
			winner = this.getGame().play();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					getGui().addMessage("The winner of the game is " + winner.getName() + " with " + winner.getPoints() + " points!");
				}
			});
	        
		}
		catch(CancellationException e) {
			// Game was cancelled. Exit run method and let thread die.
		}
	}

}
