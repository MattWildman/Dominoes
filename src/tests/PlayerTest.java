package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CancellationException;

import org.junit.Before;
import org.junit.Test;

import dominoes.Bone;
import dominoes.BoneYard;
import dominoes.CantPlayException;
import dominoes.Dominoes;
import dominoes.PlayerType;
import dominoes.Table;
import dominoes.players.DominoPlayer;
import dominoes.players.InteractivePlayer;
import dominoes.players.Player;

/**
 * @author Ben Griffiths and Matt Wildman
 * MSc Computer Science 
 * 31/03/2013
 * 
 */
public class PlayerTest {
        
        Player bot;
        InteractivePlayer humanPlayer;
        Player p1;
        Player p2;
        Thread t;
        Method removeFromHandMethod;
        @SuppressWarnings("rawtypes")
		Class[] removeFromHandParameterTypes;
        Method checkIfInterruptedMethod;
        
        @Before
        public void Before() {
        	bot = new Player(PlayerType.EASY, 6);
        	humanPlayer = new InteractivePlayer(PlayerType.INTERACTIVE, 6);
        	p1 = new Player(PlayerType.EASY, 6);
        	p2 = new Player(PlayerType.EASY, 6);
        	
        	/* Use reflection to instantiate protected methods */
        	removeFromHandParameterTypes = new Class[1];
            removeFromHandParameterTypes[0] = dominoes.Bone.class;
    		try {
    			removeFromHandMethod = Player.class.getDeclaredMethod("removeFromHand", removeFromHandParameterTypes);
    			removeFromHandMethod.setAccessible(true);
    	       	checkIfInterruptedMethod = Player.class.getDeclaredMethod("checkIfInterrupted");
    	       	checkIfInterruptedMethod.setAccessible(true);
    		}
    		catch (NoSuchMethodException e) {
    			e.printStackTrace();
    		} 
        }
        
        @Test
        public void playerConstructorTest() {
                assertNull(bot.getName());
                assertEquals(0, bot.getPoints());
                Bone[] hand = bot.bonesInHand();
                assertEquals(0, hand.length);
                assertEquals(PlayerType.EASY, bot.getPlayerType());
                
                assertNull(humanPlayer.getName());
                assertEquals(0, humanPlayer.getPoints());
                Bone[] anotherHand = humanPlayer.bonesInHand();
                assertEquals(0, anotherHand.length);
        }
        
        @Test
        public void bonesInHandTest() {
                int maxPips = 6;
                BoneYard boneYard = new BoneYard(maxPips);
                
                assertEquals(0, bot.bonesInHand().length);
                bot.draw(boneYard);
                assertEquals(1, bot.bonesInHand().length);
                bot.draw(boneYard);
                assertEquals(2, bot.bonesInHand().length);
                bot.draw(boneYard);
                assertEquals(3, bot.bonesInHand().length);
                bot.newRound();
                assertEquals(0, bot.bonesInHand().length);
                bot.draw(boneYard);
                bot.draw(boneYard);
                bot.draw(boneYard);
                assertEquals(3, bot.bonesInHand().length);
                
                bot = new Player(PlayerType.EASY, maxPips);
                assertEquals(0, bot.bonesInHand().length);
        }
        
        @Test
        public void testDrawAndTakeBack() {
        		int maxPips = 6;
                BoneYard boneYard = new BoneYard(maxPips);
                int bonesInBoneYardAtStart = boneYard.size();
                assertEquals(28, bonesInBoneYardAtStart);
                
                while(boneYard.size() > 0)
                    bot.draw(boneYard);
                assertEquals(bonesInBoneYardAtStart, bot.bonesInHand().length);
                
                bot.draw(boneYard); // boneyard now empty, so player's hand shouldn't change.
                int numBonesAfterDrawingEveryBone = bot.bonesInHand().length;
                assertEquals(bonesInBoneYardAtStart, numBonesAfterDrawingEveryBone);
                
                for(int i = bot.bonesInHand().length; i < 100; i++)      {
                    bot.takeBack(new Bone(i, i));
                    assertEquals(i + 1, bot.numInHand());
                }
                assertEquals(100, bot.bonesInHand().length);
        }
        
        @Test
        public void testMakePlay() {
                UIForTesting userInterface = new UIForTesting();
                p1.setName("Player 1");
                p2.setName("Player 2");
                p1.setPauseForThinking(false); // turn off the one-second pause before making a move
                p2.setPauseForThinking(false);
                Dominoes game = new Dominoes(userInterface, p1, p2, 20, 6);
                DominoPlayer winner = game.play();
                assertEquals(true, (winner.equals(p1) || winner.equals(p2)) && !(winner.equals(p1) && winner.equals(p2)));
        }
        
        @Test(expected=CantPlayException.class)
        public void testCantPlayException() throws CantPlayException {
        	Table table = new Table();
        	bot.makePlay(table);
        }
        
        @Test
        public void testNewRoundAndNumInHand() {
                assertEquals(0,bot.bonesInHand().length);
                assertEquals(0,bot.numInHand());
                Bone[] bunchOfBones = new Bone[7];
                for(int i = 0; i < 7; i++)      {
                        bunchOfBones[i] = new Bone(i,i);
                        bot.takeBack(bunchOfBones[i]); // add a bone to the bot's hand
                        assertEquals(i + 1, bot.bonesInHand().length);
                        assertEquals(i + 1, bot.numInHand());
                }
                assertEquals(7, bot.bonesInHand().length);
                assertEquals(7, bot.numInHand());
                bot.newRound(); // Start a new round
                assertEquals(0, bot.bonesInHand().length);
                assertEquals(0, bot.numInHand());
                bot.newRound(); // Start another new round
                assertEquals(0, bot.bonesInHand().length);
                assertEquals(0, bot.numInHand()); 
        }
        
        
        @Test
        public void testName()  {
                assertNull(bot.getName());
                bot.setName("A bot");
                assertEquals("A bot", bot.getName());
        }
        
        @Test
        public void testPoints() {
                assertEquals(0, bot.getPoints());
                bot.setPoints(10);
                assertEquals(10, bot.getPoints());
                bot = new Player(PlayerType.EASY, 6);
                assertEquals(0, bot.getPoints());
                bot.setPoints(10);
                bot.setPoints(20);
                assertEquals(20, bot.getPoints());
                bot.setPoints(bot.getPoints() + 1);
                assertEquals(21, bot.getPoints());
                bot.newRound();
                assertEquals(21, bot.getPoints());
        }
        
        @Test
        public void testRemoveFromHand() {
        	int maxPips = 9;
        	Bone[] bunchOfBones = new Bone[maxPips];
        	for(int i = 0; i < maxPips; i++)      {
                bunchOfBones[i] = new Bone(i,i);
                bot.takeBack(bunchOfBones[i]); // add a bone to the bot's hand
                assertEquals(i + 1, bot.numInHand());
        	}
        	for(int i = 0; i < maxPips; i++)	{
        		Object[] arguments = new Object[1];
    	        arguments[0] = bunchOfBones[i];
    	     
    			Bone returnedBone;
				try {
					returnedBone = (Bone)removeFromHandMethod.invoke(bot, arguments);
					assertEquals(bunchOfBones[i], returnedBone);
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
				}
        	}
        	assertEquals(0, bot.numInHand());
        }
        
        @Test(expected=CancellationException.class)
        public void checkThreadInterruption() throws CancellationException, IllegalAccessException, IllegalArgumentException {
        	t = Thread.currentThread();
        	Thread newThread = new Thread(new Runnable() {
        		public void run(){
        			t.interrupt();
        		}
        	});
        	newThread.start();
        	bot.setStopped(true);

			try {
				checkIfInterruptedMethod.invoke(bot); // should throw cancellationException
			} catch (InvocationTargetException e) {
				throw (CancellationException) e.getTargetException();
			}
        }
}