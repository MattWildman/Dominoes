package tests;

import dominoes.BoneYard;
import dominoes.DominoUI;
import dominoes.Table;
import dominoes.players.DominoPlayer;

/**
 * @author Ben Griffiths and Matt Wildman
 * MSc Computer Science 
 * 31/03/2013
 * 
 */
public class UIForTesting implements DominoUI {
	@Override
	public void display(DominoPlayer[] players, Table table, BoneYard boneYard) {
	}
	@Override
	public void displayInvalidPlay(DominoPlayer player) {
	}
	@Override
	public void displayRoundWinner(DominoPlayer winner) {
	}
}
