package tests;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import dominoes.Dominoes;

import dominoes.DominoUI;
import dominoes.PlayerType;
import dominoes.players.DominoPlayer;
import dominoes.players.Player;

/**
 * @author Ben Griffiths and Matt Wildman
 * MSc Computer Science 
 * 31/03/2013
 * 
 */
public class AITest {
	
	private int maxPips;
	private int scoreTarget;
	private Player hardPlayer;
	private Player mediumPlayer;
	private Player easyPlayer;
	private Player veryEasyPlayer;
	private Dominoes game;
	private DominoPlayer winner;
	private DominoUI ui;
	final private int SAMPLE_SIZE = 10000;
	
	@Before
	public void Before() {
		maxPips = 6;
		scoreTarget = 50;
		hardPlayer = new Player(PlayerType.HARD, maxPips);
		hardPlayer.setPauseForThinking(false);
		mediumPlayer = new Player(PlayerType.MEDIUM, maxPips);
		mediumPlayer.setPauseForThinking(false);
		easyPlayer = new Player(PlayerType.EASY, maxPips);
		easyPlayer.setPauseForThinking(false);
		veryEasyPlayer = new Player(PlayerType.VERY_EASY, maxPips);
		veryEasyPlayer.setPauseForThinking(false);
		ui = new UIForTesting();	
	}
	
	@Test
	public void hardVersusMediumTest()	{	
		int hardPlayerWins = 0;
		int mediumPlayerWins = 0;
		
		for(int i = 0; i < SAMPLE_SIZE; i++) {
			game = new Dominoes(ui, hardPlayer, mediumPlayer, scoreTarget, maxPips);
			winner = game.play();
			if(winner.equals(hardPlayer))
				hardPlayerWins++;
			else mediumPlayerWins++;
		}
		System.out.println("Hard player won " + hardPlayerWins + " games");
		System.out.println("Medium player won " + mediumPlayerWins + " games");
		
		Boolean hardPlayerWonMoreGames = (hardPlayerWins > mediumPlayerWins);
		assertTrue("Hard player didn't win more games", hardPlayerWonMoreGames);
		
	}
	
	@Test
	public void hardVersusEasyTest()	{
		
		int hardPlayerWins = 0;
		int easyPlayerWins = 0;
		
		for(int i = 0; i < SAMPLE_SIZE; i++) {
			Dominoes game = new Dominoes(ui, hardPlayer, easyPlayer, scoreTarget, maxPips);
			DominoPlayer winner = game.play();
			if(winner.equals(hardPlayer))
				hardPlayerWins++;
			else easyPlayerWins++;
		}
		System.out.println("Hard player won " + hardPlayerWins + " games");
		System.out.println("Easy player won " + easyPlayerWins + " games");
		
		Boolean hardPlayerWonMoreGames = (hardPlayerWins > easyPlayerWins);
		assertTrue("Hard player didn't win more games", hardPlayerWonMoreGames);
		
	}
	
	@Test
	public void hardVersusVeryEasyTest()	{
		
		int hardPlayerWins = 0;
		int veryEasyPlayerWins = 0;
		
		for(int i = 0; i < SAMPLE_SIZE; i++) {
			Dominoes game = new Dominoes(ui, hardPlayer, veryEasyPlayer, scoreTarget, maxPips);
			DominoPlayer winner = game.play();
			if(winner.equals(hardPlayer))
				hardPlayerWins++;
			else veryEasyPlayerWins++;
		}
		System.out.println("Hard player won " + hardPlayerWins + " games");
		System.out.println("Very Easy player won " + veryEasyPlayerWins + " games");
		
		Boolean hardPlayerWonMoreGames = (hardPlayerWins > veryEasyPlayerWins);
		assertTrue("Hard player didn't win more games", hardPlayerWonMoreGames);
		
	}
	
	@Test
	public void mediumVersusEasyTest()	{
		int mediumPlayerWins = 0;
		int easyPlayerWins = 0;
		
		for(int i = 0; i < SAMPLE_SIZE; i++) {
			Dominoes game = new Dominoes(ui, mediumPlayer, easyPlayer, scoreTarget, maxPips);
			DominoPlayer winner = game.play();
			if(winner.equals(mediumPlayer))
				mediumPlayerWins++;
			else easyPlayerWins++;
		}
		System.out.println("Medium player won " + mediumPlayerWins + " games");
		System.out.println("Easy player won " + easyPlayerWins + " games");
		
		Boolean mediumPlayerWonMoreGames = (mediumPlayerWins > easyPlayerWins);
		assertTrue("medium player didn't win more games", mediumPlayerWonMoreGames);
		
	}
	
	@Test
	public void mediumVersusVeryEasyTest()	{
		int mediumPlayerWins = 0;
		int veryEasyPlayerWins = 0;
		
		for(int i = 0; i < SAMPLE_SIZE; i++) {
			Dominoes game = new Dominoes(ui, mediumPlayer, veryEasyPlayer, scoreTarget, maxPips);
			DominoPlayer winner = game.play();
			if(winner.equals(mediumPlayer))
				mediumPlayerWins++;
			else veryEasyPlayerWins++;
		}
		System.out.println("Medium player won " + mediumPlayerWins + " games");
		System.out.println("Very Easy player won " + veryEasyPlayerWins + " games");
		
		Boolean mediumPlayerWonMoreGames = (mediumPlayerWins > veryEasyPlayerWins);
		assertTrue("medium player didn't win more games", mediumPlayerWonMoreGames);
		
	}
	
	@Test
	public void easyVersusVeryEasyTest()	{
		int easyPlayerWins = 0;
		int veryEasyPlayerWins = 0;
		
		
		for(int i = 0; i < SAMPLE_SIZE; i++) {
			Dominoes game = new Dominoes(ui, easyPlayer, veryEasyPlayer, scoreTarget, maxPips);
			DominoPlayer winner = game.play();
			if(winner.equals(easyPlayer))
				easyPlayerWins++;
			else veryEasyPlayerWins++;
		}
		
		System.out.println("Easy player won " + easyPlayerWins + " games");
		System.out.println("Very Easy player won " + veryEasyPlayerWins + " games");
		
		Boolean easyPlayerWonMoreGames = (easyPlayerWins > veryEasyPlayerWins);
		assertTrue("easy player didn't win more games", easyPlayerWonMoreGames);
		
	}
}
