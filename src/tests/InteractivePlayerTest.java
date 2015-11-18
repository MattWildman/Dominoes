package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CancellationException;

import org.junit.Before;
import org.junit.Test;

import dominoes.Bone;
import dominoes.CantPlayException;
import dominoes.CubbyHole;
import dominoes.Play;
import dominoes.PlayerType;
import dominoes.Table;
import dominoes.players.InteractivePlayer;
import dominoes.players.Player;

/**
 * @author Ben Griffiths and Matt Wildman
 * MSc Computer Science 
 * 31/03/2013
 * 
 */
public class InteractivePlayerTest {

    InteractivePlayer humanPlayer1;
    InteractivePlayer humanPlayer2;
    Thread t;
    Method checkIfInterruptedMethod;
    Play play;
    
    @Before
    public void BeforeClass() {
    	humanPlayer1 = new InteractivePlayer(PlayerType.INTERACTIVE, 6);
    	humanPlayer2 = new InteractivePlayer(PlayerType.INTERACTIVE, 6);
    	
    	/* Use reflection to instantiate protected method */
		try {
	       	checkIfInterruptedMethod = Player.class.getDeclaredMethod("checkIfInterrupted");
	       	checkIfInterruptedMethod.setAccessible(true);
		}
		catch (NoSuchMethodException e) {
			e.printStackTrace();
		} 
    }
    
    @Test
	public void constructorTest() {
    	humanPlayer1 = new InteractivePlayer(PlayerType.VERY_EASY, 9); // PlayerType should be ignored
    	humanPlayer2 = new InteractivePlayer(PlayerType.EASY, 9);
    	InteractivePlayer humanPlayer3 = new InteractivePlayer(PlayerType.MEDIUM, 6);
    	InteractivePlayer humanPlayer4 = new InteractivePlayer(PlayerType.HARD, 6);
    	InteractivePlayer humanPlayer5 = new InteractivePlayer(PlayerType.INTERACTIVE, 6);
    	
        assertEquals(PlayerType.INTERACTIVE, humanPlayer1.getPlayerType());
        assertEquals(PlayerType.INTERACTIVE, humanPlayer2.getPlayerType());
        assertEquals(PlayerType.INTERACTIVE, humanPlayer3.getPlayerType());
        assertEquals(PlayerType.INTERACTIVE, humanPlayer4.getPlayerType());
        assertEquals(PlayerType.INTERACTIVE, humanPlayer5.getPlayerType());
	}
    
    @Test
    public void makePlayTest() {
    	CubbyHole cubbyHole = humanPlayer1.getCubbyHole();
    	cubbyHole.put("A_STRING");
    	String s = (String)cubbyHole.get();
    	assertEquals("A_STRING", s);
    	
    	Table table = new Table(); // the interactive player doesn't actually use the table
    	Bone bone = new Bone(6,6);
    	play = new Play(bone, Play.LEFT);
    	
    	humanPlayer1.getCubbyHole().put(play); // put a play object in the cubbyhole
    	Play result = null;
    	try {
			result = humanPlayer1.makePlay(table); // ask the player to make a play
			assertEquals(play, result);
		} catch (CantPlayException e) { // shouldn't be thrown
			assertEquals(true, false);
		}
    	
    	// call makePlay BEFORE putting a play object in the cubbyhole from another thread
    	Thread newThread = new Thread(new Runnable() {
    		public void run(){
				try {
					Thread.sleep(2000); // wait for two seconds
				} catch (InterruptedException e) {
					assertEquals(true, false);
				}
    			humanPlayer1.getCubbyHole().put(play);
    		}
    	});
    	
    	result = null;
    	try {
    		newThread.start();
			result = humanPlayer1.makePlay(table); // ask the player to make a play - this thread should block until the play can be made
			assertEquals(play, result);
		} catch (CantPlayException e) { // shouldn't be thrown
			assertEquals(true, false);
		}
    	
    	humanPlayer1.getCubbyHole().put("THIS IS NOT A PLAY OBJECT"); // put something that isn't a play in the cubbyhole
    	result = null;
    	try {
			result = humanPlayer1.makePlay(table); // ask the player to make a play
		} catch (CantPlayException e) { // should be thrown this time
			assertEquals(CantPlayException.class, e.getClass());
			assertNull(result);
		}
    	
    	
    }
    
    /*
     * Test cancellation of the thread the player is playing on, with an inactive player
     */
    @Test(expected=CancellationException.class)
    public void checkThreadInterruption1() throws CancellationException, IllegalAccessException, IllegalArgumentException {
    	
    	t = Thread.currentThread();
    	Thread newThread = new Thread(new Runnable() {
    		public void run(){
    			t.interrupt();
    		}
    	});
    	newThread.start();
    	humanPlayer1.setStopped(true);
    	
    	try {
			checkIfInterruptedMethod.invoke(humanPlayer1); // should throw cancellationException
		} catch (InvocationTargetException e) {
			throw (CancellationException) e.getTargetException();
		}
    }
    
    /*
     * Test cancellation of the thread the player is playing on, with a player being asked to make a play immediately
     * before and after cancellation
     */
    @Test(expected=CancellationException.class)
    public void checkThreadInterruption2() throws CancellationException, IllegalAccessException, IllegalArgumentException {
    	t = Thread.currentThread();
    	Thread newThread = new Thread(new Runnable() {
    		public void run(){
    			humanPlayer1.setStopped(true);
    			humanPlayer1.getCubbyHole().put("A STRING"); // place something in the cubbyhole to stop the player's thread blocking
        		t.interrupt();
    		}
    	});
    	Table table = new Table();
    	humanPlayer1.getCubbyHole().put("SOME STRING"); // the player will try to play with this String and will throw a CantPlayException
    	try {
			humanPlayer1.makePlay(table);
		} catch (CantPlayException exception) {
			assertEquals(CantPlayException.class, exception.getClass());
		}
		try {
			newThread.start();
			humanPlayer1.makePlay(table); // Player should sleep and cancel and NOT throw a CantPlayException
		} catch (CantPlayException e1) {
			assertEquals(true, false);
		}
		
    	
    	try {
			checkIfInterruptedMethod.invoke(humanPlayer1); // should throw cancellationException
		} catch (InvocationTargetException e) {
			throw (CancellationException) e.getTargetException();
		}
    }
}
