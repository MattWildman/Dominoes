package dominoes;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import dominoes.players.DominoPlayer;
import dominoes.players.InteractivePlayer;
import dominoes.players.Player;

/**
 * @author Ben Griffiths and Matt Wildman
 * MSc Computer Science 
 * 31/03/2013
 * DominoUIImpl
 * This interface represents a user interface for the dominoes game. It has methods for updating the display at appropriate times.
 * @extends JFrame
 * @implements DominoUI
 * @implements Observer
 */
@SuppressWarnings("serial")
public class DominoUIImpl extends JFrame implements DominoUI, Observer {
		
		private static final String PLAY_LEFT_COMMAND = "playLeft";
		private static final String PLAY_RIGHT_COMMAND = "playRight";
		private static final String DRAW_PASS_COMMAND = "drawPass";
		private static final String HELP_FILE_LOCATION = "helpFile.txt";
		private Thread gameThread;
		private int roundNumber = 1;
    	private volatile int pointGoal = 0;
    	private volatile int maxPips = 6;
    	private Player p1, p2, currentPlayer;
        private JMenuBar gameMenuBar;
    	private JMenu gameMenu, helpMenu;
    	private JMenuItem resumeMenuItem, pauseMenuItem, startNewGameMenuItem, 
    					  howToPlayMenuItem, aboutMenuItem, exitMenuItem;
        private JLabel roundLabel = new JLabel("Round " + roundNumber);
        private JLabel pointGoalLabel = new JLabel("First to reach ?? points");
        private JLabel pauseLabel;
        private JButton newGameButton, startGameButton;
        private PlayerPanel player1Panel, player2Panel;
        private TablePanel tablePanel;
        private BoneYardPanel boneYardPanel;
        private ConfigPanel configPanel;
        private JPanel topContent, centreContent, gameContent, pausePanel;
        private MessagePanel messagePanel;
        private String helpText;
		private boolean paused = false, gameInProgress = false;
		
		/**
		 * confirmExit
		 * Prompts the user to confirm an exit command
		 */
		private void confirmExit() {
			int confirmExit = JOptionPane.showConfirmDialog(getContentPane(), "Are you sure you want to exit?", 
															"Exit?", JOptionPane.YES_NO_OPTION);
			if(confirmExit == JOptionPane.YES_OPTION)
				System.exit(0);
		}
		
		/**
		 * actionHandler
		 * Handles interactive players' actions
		 * @param command - represents the action to be performed
		 */
		private void actionHandler(String command) {
			synchronized(getCurrentPlayer()) {
					if(getCurrentPlayer().getClass().equals(InteractivePlayer.class)) {
						InteractivePlayer player = (InteractivePlayer)getCurrentPlayer();
						CubbyHole cubbyHole = player.getCubbyHole();
						if(command.equals(DRAW_PASS_COMMAND))
			                cubbyHole.put(new String(player.getName() + "wants to draw from the boneyard"));
						if(command.equals(PLAY_LEFT_COMMAND))	{
							int	chosenBone = (player == p1 ? player1Panel.getSelectedBone() : player2Panel.getSelectedBone());
							if(chosenBone > -1 && chosenBone < player.numInHand())
								cubbyHole.put(new Play(player.bonesInHand()[chosenBone], Play.LEFT));
						}
						if(command.equals(PLAY_RIGHT_COMMAND)) {
							int	chosenBone = (player == p1 ? player1Panel.getSelectedBone() : player2Panel.getSelectedBone());
							if(chosenBone > -1 && chosenBone < player.numInHand())
								cubbyHole.put(new Play(player.bonesInHand()[chosenBone], Play.RIGHT));
						}
					}
			}
        }
		
		/**
		 * startGame
		 * Set up the user interface for a new game and start the game
		 */
		private void startGame() {
        	setGameInProgress(true);
        	startNewGameMenuItem = new JMenuItem("Start new game");
        	startNewGameMenuItem.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent actionEvent) {
        			boolean gameWasAlreadyPaused = isPaused();
        			if(!gameWasAlreadyPaused)
        				pauseGame();
        			int confirmNewGame = JOptionPane.showConfirmDialog(getContentPane(), "Are you sure you sure?", "Start a new game?", JOptionPane.YES_NO_OPTION);
        			if(confirmNewGame == JOptionPane.YES_OPTION)
        				newGame();
        			else {
        				if(!gameWasAlreadyPaused)
        					resumeGame();
        			}
        		}
        	});
        	gameMenu.add(startNewGameMenuItem, 0);
        	pauseMenuItem = new JMenuItem("Pause");
        	pauseMenuItem.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent actionEvent) {
        			pauseGame();
        		}
        	});
        	gameMenu.add(pauseMenuItem, 0);
        	
        	String p1Name = configPanel.getNameInput(ConfigPanel.PLAYER_1);
        	String p2Name = configPanel.getNameInput(ConfigPanel.PLAYER_2);
        	pointGoal = configPanel.getPointGoalInput();
            maxPips = configPanel.getPipSelection();
            roundNumber = 1;
            
        	newGameButton = new JButton("New game");
        	newGameButton.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent actionEvent) {
        			boolean gameWasAlreadyPaused = isPaused();
        			if(!gameWasAlreadyPaused)
        				pauseGame();
        			int confirmNewGame = JOptionPane.showConfirmDialog(getContentPane(), "Are you sure you sure?", 
        								 "Start a new game?", JOptionPane.YES_NO_OPTION);
        			if(confirmNewGame == JOptionPane.YES_OPTION)
        				newGame();
        			else {
        				if(!gameWasAlreadyPaused)
        					resumeGame();
        			}
        		}
        	});
            newGameButton.setPreferredSize(DominoesStyle.getButtonDimension());
            newGameButton.setFocusable(false);
        	
            if (configPanel.selectedHuman(ConfigPanel.PLAYER_1))
            	p1 = new InteractivePlayer(configPanel.getDifficulty(ConfigPanel.PLAYER_1), maxPips);
            else
            	p1 = new Player(configPanel.getDifficulty(ConfigPanel.PLAYER_1), maxPips);
            if (configPanel.selectedHuman(ConfigPanel.PLAYER_2))
            	p2 = new InteractivePlayer(configPanel.getDifficulty(ConfigPanel.PLAYER_2), maxPips);
            else
            	p2 = new Player(configPanel.getDifficulty(ConfigPanel.PLAYER_2), maxPips);
            
        	p1.setName(p1Name);
            p2.setName(p2Name);
            player1Panel = new PlayerPanel(p1.getName());
            player2Panel = new PlayerPanel(p2.getName());
            
            //create key listener for player panels
            KeyAdapter keyListener = new KeyAdapter() {
    			@Override
    			public void keyPressed(KeyEvent e) {
    				int keyCode = e.getKeyCode();
    				int modifier = e.getModifiersEx();
    				if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_L)
    					actionHandler(PLAY_LEFT_COMMAND);
    				if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_R)
    					actionHandler(PLAY_RIGHT_COMMAND);
    				if (keyCode == KeyEvent.VK_SPACE)
    					actionHandler(DRAW_PASS_COMMAND);
    				if (keyCode == KeyEvent.VK_N && modifier == KeyEvent.CTRL_DOWN_MASK) {
	    				boolean gameWasAlreadyPaused = isPaused(); // THIS CODE IS REPEATED FROM ABOVE
	        			if(!gameWasAlreadyPaused)
	        				pauseGame();
	        			int confirmNewGame = JOptionPane.showConfirmDialog(getContentPane(), "Are you sure you sure?", "Start a new game?", JOptionPane.YES_NO_OPTION);
	        			if(confirmNewGame == JOptionPane.YES_OPTION)
	        				newGame();
	        			else {
	        				if(!gameWasAlreadyPaused)
	        					resumeGame();
	        			}
    				}
    			}
            };
            player1Panel.getPlayerBonesList().addKeyListener(keyListener);
            player2Panel.getPlayerBonesList().addKeyListener(keyListener);
            
            // set players to observe each other
            p1.addObserver(p2);
            p2.addObserver(p1);
            // set UI to observe both players
            p1.addObserver(this);
            p2.addObserver(this);
            
            tablePanel = new TablePanel(maxPips);
            if (maxPips > 7)
            	DominoesStyle.boneYardScrollBar(true);
            else
            	DominoesStyle.boneYardScrollBar(false);
            boneYardPanel = new BoneYardPanel();
     	    boneYardPanel.getBoneYardList().addMouseListener(new MouseListener() {
    			@Override
    			public void mouseClicked(MouseEvent e) {
    					if(getCurrentPlayer() != null && getCurrentPlayer().getClass() == InteractivePlayer.class) {
    						InteractivePlayer player = (InteractivePlayer)getCurrentPlayer();
    						if(!player.isPauseIndefinitely()) {
    							CubbyHole cubbyHole = player.getCubbyHole();
    							cubbyHole.put(new String(player.getName() + "wants to draw from the boneyard"));
    						}
    					}
    			}
    			@Override
    			public void mouseEntered(MouseEvent e) {
    			}
    			@Override
    			public void mouseExited(MouseEvent e) {
    			}
    			@Override
    			public void mousePressed(MouseEvent e) {
    			}
    			@Override
    			public void mouseReleased(MouseEvent e) {
    			}
    		});
            boneYardPanel.getPassButton().addActionListener(new ActionListener() {
     	    	@Override
     	    	public void actionPerformed(ActionEvent actionEvent) {
    	 	    		if(getCurrentPlayer() != null && getCurrentPlayer().getClass().equals(InteractivePlayer.class)) {
    	 	    		InteractivePlayer player = (InteractivePlayer)getCurrentPlayer();
    	 	    			if(!player.isPauseIndefinitely()) {
    	 	    				CubbyHole cubbyHole = player.getCubbyHole();
    	 	    				cubbyHole.put(new String(player.getName() + "wants to pass"));
    	 	    			}
    	 	    		}
     	    	}
     	    });

            topContent = new JPanel();
            centreContent = new JPanel();
            gameContent = new JPanel();
            messagePanel = new MessagePanel();
            
            roundLabel.setBorder(BorderFactory.createCompoundBorder(null,DominoesStyle.getLARGE_PADDING()));
            pointGoalLabel.setBorder(BorderFactory.createCompoundBorder(null,DominoesStyle.getLARGE_PADDING()));
            roundLabel.setFont(DominoesStyle.getMEDIUM_FONT());
            pointGoalLabel.setFont(DominoesStyle.getMEDIUM_FONT());
            
            topContent.setOpaque(false);
            JPanel newGameButtonPanel = new JPanel();
            newGameButtonPanel.setOpaque(false);
            newGameButtonPanel.setMaximumSize(DominoesStyle.getButtonDimension());
            newGameButtonPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 0));
            newGameButtonPanel.add(newGameButton);
            topContent.setMaximumSize(DominoesStyle.getBoneYardDimension());
            topContent.setLayout(new BorderLayout());
            topContent.add(roundLabel, BorderLayout.LINE_START);
            topContent.add(pointGoalLabel, BorderLayout.CENTER);
            topContent.add(newGameButtonPanel, BorderLayout.LINE_END);
            
            centreContent.setOpaque(false);
            centreContent.setPreferredSize(DominoesStyle.getCentreContentDimension());
            centreContent.setLayout(new BoxLayout(centreContent, BoxLayout.PAGE_AXIS));
            centreContent.add(topContent);
            centreContent.add(boneYardPanel);
            centreContent.add(tablePanel);
            centreContent.add(messagePanel);
            
            gameContent.setBackground(DominoesStyle.SECONDARY_COLOUR);
            gameContent.setLayout(new BorderLayout());
            gameContent.add(player1Panel, BorderLayout.LINE_START);
            gameContent.add(centreContent, BorderLayout.CENTER);
            gameContent.add(player2Panel, BorderLayout.LINE_END);
            
        	roundLabel.setText("Round " + roundNumber);
        	pointGoalLabel.setText("First to reach " + pointGoal + " points");
            
            setContentPane(gameContent);
            gameContent.requestFocusInWindow();
            setPaused(false);

            // Create a new thread, which will play a single game of Dominoes
            gameThread = new Thread(new GameRunnable(this, p1, p2, pointGoal, maxPips));
            gameThread.start();
        }
        
		/**
		 * newGame
		 * Stops the game currently being played and reverts to the configuration options
		 */
        private void newGame() {
        	synchronized(p1) {
        		p1.setStopped(true);
        		p1.setPauseIndefinitely(false);
        		if(p1.getClass().equals(InteractivePlayer.class)) {
        			InteractivePlayer player = (InteractivePlayer)p1;
        			player.getCubbyHole().put("STOP PLAYING");
        		}
        	}
        	synchronized(p2) {
        		p2.setStopped(true);
        		p2.setPauseIndefinitely(false);
        		if(p2.getClass().equals(InteractivePlayer.class)) {
        			InteractivePlayer player = (InteractivePlayer)p2;
        			player.getCubbyHole().put("STOP PLAYING");
        		}
        	}
        	synchronized(gameThread) {
        		gameThread.interrupt(); // interrupt the game logic thread, causing it to terminate
        	}
        	tablePanel.clearTable();
        	gameMenu.remove(startNewGameMenuItem);
        	if(isPaused())
        		gameMenu.remove(resumeMenuItem);
        	else gameMenu.remove(pauseMenuItem);
        	setPaused(false);
        	setContentPane(configPanel);
        	setGameInProgress(false);
        	this.revalidate();
        	this.repaint();
        }
        
        /**
         * pauseGame
         * Pauses the game and hides the table
         */
		private void pauseGame() {
			if(!isPaused()) {
				this.setPaused(true);
				synchronized(p1) {
					p1.setPauseIndefinitely(true);
				}
				synchronized(p2) {
					p2.setPauseIndefinitely(true);
				}
	        	gameMenu.remove(pauseMenuItem);
	        	resumeMenuItem = new JMenuItem("Resume");
	        	resumeMenuItem.addActionListener(new ActionListener() {
	        		public void actionPerformed(ActionEvent actionEvent) {
	        			resumeGame();
	        		}
	        	});
	        	gameMenu.add(resumeMenuItem, 0); // add to top of menu
	        	
	        	centreContent.remove(messagePanel);
	        	centreContent.remove(tablePanel);
	        	pausePanel = new JPanel();
	        	pausePanel.setOpaque(false);
	        	pausePanel.setLayout(new GridLayout(1,1));
	        	pausePanel.setPreferredSize(tablePanel.getPreferredSize());
	        	pauseLabel = new JLabel("Game paused");
	        	pauseLabel.setFont(DominoesStyle.getMEDIUM_FONT());
	        	pausePanel.add(pauseLabel);
	        	pauseLabel.setHorizontalAlignment(JLabel.CENTER);
	        	centreContent.add(pausePanel);
	        	centreContent.add(messagePanel);
	        	centreContent.revalidate();
	        	centreContent.repaint();
	        	
	        	this.addMessage("Game paused");
			}
        }
		
		/**
		 * resumeGame
		 * Resumes a paused game
		 */
		private void resumeGame() {
			if(isPaused()) {
				synchronized(p1) {
					p1.setPauseIndefinitely(false);
				}
				synchronized(p2) {
					p2.setPauseIndefinitely(false);
				}
	        	gameMenu.remove(resumeMenuItem);
	        	pauseMenuItem = new JMenuItem("Pause");
	        	pauseMenuItem.addActionListener(new ActionListener() {
	        		public void actionPerformed(ActionEvent actionEvent) {
	        			pauseGame();
	        		}
	        	});
	        	gameMenu.add(pauseMenuItem, 0);
	        	
	        	centreContent.remove(messagePanel);
	        	centreContent.remove(pausePanel);
	        	centreContent.add(tablePanel);
	        	centreContent.add(messagePanel);
	        	centreContent.revalidate();
	        	centreContent.repaint();
	        	
	        	this.addMessage("Game resumed");
	        	this.setPaused(false);
			}
        }
		
		private boolean isPaused() {
			return this.paused;
		}
        
        private void setPaused(boolean paused) {
			this.paused = paused;
		}
        
        private void setCurrentPlayer(Player player) {
			currentPlayer = player;
		}
 
		private boolean isGameInProgress() {
			return gameInProgress;
		}

		private void setGameInProgress(boolean gameInProgress) {
			this.gameInProgress = gameInProgress;
		}
		
		/**
		 * updateButtons
		 * Activates table buttons and pass button during an interactive player's turn.
		 * Deactivates them during a bot's turn
		 * @param player - the player whose turn it is to play
		 */
		private void updateButtons(Player player) {
			boolean interactivePlayer = player.getPlayerType().equals(PlayerType.INTERACTIVE);
			SwingUtilities.invokeLater(new DominoUIImplUpdater(interactivePlayer) {
        		@Override
				public void run() {
        			tablePanel.getLeftButton().setEnabled(this.isCondition());
        			tablePanel.getRightButton().setEnabled(this.isCondition());
        			boneYardPanel.getPassButton().setEnabled(this.isCondition());
				}
			});
			if (!interactivePlayer) {
				MouseListener[] mouseListeners = tablePanel.getLeftButton().getMouseListeners();
				for(MouseListener listener : mouseListeners)
					tablePanel.getLeftButton().removeMouseListener(listener);
				mouseListeners = tablePanel.getRightButton().getMouseListeners();
				for(MouseListener listener : mouseListeners)
					tablePanel.getRightButton().removeMouseListener(listener);
			}
			else {			
				tablePanel.getLeftButton().addMouseListener(new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent e) {
						actionHandler(PLAY_LEFT_COMMAND);
					}
					@Override
					public void mouseEntered(MouseEvent e) {
						tablePanel.hoverButton(tablePanel.getLeftButton());
					}
					@Override
					public void mouseExited(MouseEvent e) {
						tablePanel.styleButton(tablePanel.getLeftButton());
					}
					@Override
					public void mousePressed(MouseEvent e) {
					}
					@Override
					public void mouseReleased(MouseEvent e) {
					}
				});
				tablePanel.getRightButton().addMouseListener(new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent e) {
						actionHandler(PLAY_RIGHT_COMMAND);
					}
					@Override
					public void mouseEntered(MouseEvent e) {
						tablePanel.hoverButton(tablePanel.getRightButton());
					}
					@Override
					public void mouseExited(MouseEvent e) {
						tablePanel.styleButton(tablePanel.getRightButton());
					}
					@Override
					public void mousePressed(MouseEvent e) {
					}
					@Override
					public void mouseReleased(MouseEvent e) {
					}
				});
			}
		}

		/**
		 * createAndShowGUI
		 * Creates the user interface and displays the game configuration options
		 */
        public void createAndShowGUI() {
            exitMenuItem = new JMenuItem("Exit");
            howToPlayMenuItem = new JMenuItem("How to play");
            aboutMenuItem = new JMenuItem("About Fat Dominoes");
            exitMenuItem.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent actionEvent) {
        			confirmExit();
        		}
            });
            
            // Load the file containing text for the help dialog and copy contents into helpText string
            ClassLoader classLoader = this.getClass().getClassLoader();
			InputStream inputStream = classLoader.getResourceAsStream(HELP_FILE_LOCATION);
			StringBuffer stringBuffer = new StringBuffer();
			if(inputStream == null)
				helpText = "Help file not found";
			else {
				Scanner scanner = new Scanner(inputStream);
				while (scanner.hasNext ())
					stringBuffer.append (scanner.nextLine ());
				scanner.close();
				helpText = new String(stringBuffer);
			}
            // create menu items and menu bar
            howToPlayMenuItem.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent actionEvent) {
            			boolean gameWasAlreadyPaused = isPaused();
            			if(isGameInProgress() && !gameWasAlreadyPaused)
            				pauseGame();
            			JOptionPane.showMessageDialog(getContentPane(), helpText, "Fat Dominoes Help", JOptionPane.QUESTION_MESSAGE);
            			if(isGameInProgress() && !gameWasAlreadyPaused)
            				resumeGame();
            		}
            });
            aboutMenuItem.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent actionEvent) {
        			boolean gameWasAlreadyPaused = isPaused();
        			if(isGameInProgress() && !gameWasAlreadyPaused)
        				pauseGame();
        				
        			Bone bone = new Bone(6, 6);
        			BoneIcon boneIcon = new BoneIcon(bone);
        			boneIcon.rotate();
        			String credits = "This game was created by Matt Wildman and Ben Griffiths.\n"
        						   + "The game logic was provided to us by Keith and Oded";
        			JOptionPane.showMessageDialog(getContentPane(), credits, "About Fat Dominoes", JOptionPane.INFORMATION_MESSAGE, boneIcon);
        			if(isGameInProgress() && !gameWasAlreadyPaused)
        				resumeGame();
        		}
            });
            gameMenu = new JMenu("Game");
            gameMenu.add(exitMenuItem);
            helpMenu = new JMenu("Help");
            helpMenu.add(howToPlayMenuItem);
            helpMenu.add(aboutMenuItem);
            gameMenuBar = new JMenuBar();
            gameMenuBar.add(gameMenu);
            gameMenuBar.add(helpMenu);
            this.setJMenuBar(gameMenuBar);
            this.setTitle("Fat Dominoes");
            
            // create config panel and game options
            configPanel = new ConfigPanel();
            this.setContentPane(configPanel);
        	startGameButton = configPanel.getStartGameButton();
            startGameButton.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent actionEvent) {
        			startGame();
        		}
        	});
            startGameButton.setPreferredSize(DominoesStyle.getButtonDimension());
            
            // prepare window
            this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            this.setPreferredSize(DominoesStyle.getWindowDimension());
            this.setMinimumSize(DominoesStyle.getWindowDimension());
            this.addWindowListener(new WindowListener() {
            	@Override
				public void windowClosing(WindowEvent e) {
					confirmExit();
				}
				@Override
				public void windowActivated(WindowEvent e) {
				}
				@Override
				public void windowClosed(WindowEvent e) {
				}
				@Override
				public void windowDeactivated(WindowEvent e) {	
				}
				@Override
				public void windowDeiconified(WindowEvent e) {
				}
				@Override
				public void windowIconified(WindowEvent e) {
				}
				@Override
				public void windowOpened(WindowEvent e) {
				}
            });
            this.setVisible(true);
            this.addComponentListener(new ComponentAdapter() { // component Listener to respond to window resizing
                public void componentResized(ComponentEvent e) {
                    JFrame frame = (JFrame)e.getSource();
                    frame.validate();
                }
            });
        }
        
        /**
         * addMessage
         * Adds a message to the message panel
         * @param s - the message to add
         */
        public void addMessage(String message) {
        	messagePanel.addMessage(message);
        }
        
        /**
         * display
         * Displays or updates the bone yard, table, player hands and other game information
         */
        @Override
        public void display(DominoPlayer[] players, Table table, BoneYard boneYard) {
        	player1Panel.display(players[0].bonesInHand(), players[0].getName(), 
					players[0].getPoints(), 
					players[0].getClass().equals(InteractivePlayer.class));
        	player2Panel.display(players[1].bonesInHand(), players[1].getName(), 
					players[1].getPoints(), 
					players[1].getClass().equals(InteractivePlayer.class));
        	boneYardPanel.display(boneYard);
        	tablePanel.display(table);
        }
        
        /**
         * displayInvalidPlay
         * Displays message when a player makes an invalid play
         */
        @Override
        public void displayInvalidPlay(DominoPlayer player) {
        	SwingUtilities.invokeLater(new DominoUIImplUpdater(player) {
        		@Override
				public void run() {
        			messagePanel.addMessage("Invalid play by " + this.getPlayer().getName() + "!");
				}
			});
        }
        
        /**
         * displayRoundWinner
         * Updates the interface when a round has finished and displays who has won the round
         */
        @Override
        public void displayRoundWinner(DominoPlayer player) {
        	SwingUtilities.invokeLater(new DominoUIImplUpdater(player) {
        		@Override
				public void run() {
                    tablePanel.clearTable();
                    boneYardPanel.clearBoneYard();
                    player1Panel.clearHand();
                    player2Panel.clearHand();
                    if(this.getPlayer() == p1)
                    	player1Panel.getPlayerPointsLabel().setText("Points: " + this.getPlayer().getPoints());
                    else player2Panel.getPlayerPointsLabel().setText("Points: " + this.getPlayer().getPoints());
                    messagePanel.addMessage(this.getPlayer().getName() + " has won round " + roundNumber + "!");
                    roundLabel.setText("Round " + ++roundNumber); //repeated code!
                    messagePanel.addMessage("Round " + roundNumber + " started...");
				}
			});
        }
        
        /**
         * update
         * Updates the interface according to whose turn it is to play
         */
		@Override
		public void update(Observable observable, Object objectPassed) {
			if(objectPassed.getClass().equals(PlayerAction.class))	{
				PlayerAction playerAction = (PlayerAction)objectPassed;
				Player player = (Player)observable;
				if(playerAction.getActionType() == PlayerActionType.START_TURN) {
					if(p1 == player) {
						setCurrentPlayer(p1);
						player1Panel.highlight(true);
						player2Panel.highlight(false);
					}
					else {
						setCurrentPlayer(p2);
						player2Panel.highlight(true);
						player1Panel.highlight(false);
					}
					setCurrentPlayer(player);
					updateButtons(player);
				}
				if(playerAction.getActionType() == PlayerActionType.END_TURN) { // DO WE NEED TO DO ANYTHING AT END OF TURN?
					if(p1 == player) {
						setCurrentPlayer(p2);
						player1Panel.highlight(false);
						player2Panel.highlight(true);
					}
					else {
						setCurrentPlayer(p1);
						player2Panel.highlight(false);
						player1Panel.highlight(true);
					}
				}
			}
		}
		
		public Player getCurrentPlayer() {
			return currentPlayer;
		}
}
