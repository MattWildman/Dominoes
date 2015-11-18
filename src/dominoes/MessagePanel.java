package dominoes;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ben Griffiths and Matt Wildman
 * MSc Computer Science 
 * 31/03/2013
 * MessagePanel
 * @extends JPanel
 * 
 */
@SuppressWarnings("serial")
public class MessagePanel extends JPanel {
	
	@Getter (AccessLevel.PRIVATE) private final String INIT_MESSAGE = "Game started!\n" +
			"Use your mouse or select a bone with the up and down arrows\n" +
			"Use L and R keys to play on the left/right. Hit space to take a bone";
	@Getter (AccessLevel.PRIVATE) @Setter (AccessLevel.PRIVATE) private JTextArea messageArea;
	@Getter (AccessLevel.PRIVATE) @Setter (AccessLevel.PRIVATE) private JScrollPane scrollPane;
	
	/**
	 * Constructor
	 */
	public MessagePanel() {
		super();
		setMessageArea(new JTextArea(getINIT_MESSAGE(), DominoesStyle.getMESSAGE_AREA_ROWS(), DominoesStyle.getMESSAGE_AREA_COLUMNS()));
		getMessageArea().setEditable(false);
		getMessageArea().setFocusable(false);
		getMessageArea().setBorder(DominoesStyle.getSMALL_PADDING());
		setScrollPane(new JScrollPane(getMessageArea()));
		getScrollPane().setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		getScrollPane().setBorder(null);
		add(getScrollPane());
		setVisible(true);
		setOpaque(false);
	}
	
	/**
	 * addMessage
	 * @param message - adds message to message area followed by a line break then scrolls to that message
	 */
	public void addMessage(String message) {
		getMessageArea().append(message + "\n");
		getMessageArea().setCaretPosition(getMessageArea().getDocument().getLength());
		getMessageArea().revalidate();
	}

}
