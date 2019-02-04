import javax.swing.*;
import java.util.concurrent.BlockingQueue;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *  View class contains 2 queues to communicate with Controllers. It sends information about user interaction (taken from key listener)
 *	and receive information what to change and redraw. It contains Panel (full board with points and "Game Over" information). 
 *	My board is 10x16 boxes (40px*40px) and side bar (46px width). Key listener reacts on:
 *	1) VK_UP (up arrow) - left rotation
 *	2) VK_DOWN (down arrow) - right rotation
 *	3) VK_LEFT (left arrow) - move left
 *	4) VK_RIGHT (right arrow) - move right
 *	5) VK_SPACE (space) - move faster falling piece (while pressed)
 *	6) VK_ENTER (enter) - when the game is lost pressing enter starts new game and restart all parameters of the game
 *
 *	Data receiving from controller (about all the pieces) is 2 dimensional array of numbers (integer type). To draw them on the board 
 *	we need to change numbers to colors and to do it we have private method called redraw. It creates 2 dimensional array of awt.Color 
 *	objects and pass it to Panel.
 *  @author Bart³omiej Jurek
 */

public class View implements Runnable
{
	private Color [][] colors;
	private BlockingQueue <Message> controllerToViewQueue;
	private BlockingQueue <Message> viewToControllerQueue;
	private Panel panel;
	private JFrame frame;
	private Message message;
	private boolean loose = false;
	
	public View()
	{
		/* Creating JFrame */
		frame = new JFrame("TETRIS GAME");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(10*40+16 + 4*25+6, 16*40+35);
		frame.setVisible(true);
		frame.setLocation(500, 200);
		frame.setResizable(false);
		
		/* Creating Panel and java.awt.Color array */
		colors = new Color[16][10];
		for(int i=0; i<16; ++i)
			for(int j=0; j<10; ++j)
				colors[i][j] = Color.DARK_GRAY;
		panel = new Panel(colors);
		frame.add(panel);
		
		/* Adding KeyListener */
		frame.addKeyListener(new KeyListener() 
		{
			public void keyTyped(KeyEvent e) {}
			
			public void keyPressed(KeyEvent e)
			{
				switch (e.getKeyCode()) 
				{
				case KeyEvent.VK_UP:
					message = new Message(Message.TYPE.ROTATE_LEFT);
					try 
					{
						viewToControllerQueue.put(message);
					} catch (InterruptedException e1) {}
					break;
				case KeyEvent.VK_DOWN:
					message = new Message(Message.TYPE.ROTATE_RIGHT);
					try 
					{
						viewToControllerQueue.put(message);
					} catch (InterruptedException e1) {}
					break;
				case KeyEvent.VK_LEFT:
					message = new Message(Message.TYPE.MOVE_LEFT);
					try 
					{
						viewToControllerQueue.put(message);
					} catch (InterruptedException e1) {}
					break;
				case KeyEvent.VK_RIGHT:
					message = new Message(Message.TYPE.MOVE_RIGHT);
					try 
					{
						viewToControllerQueue.put(message);
					} catch (InterruptedException e1) {}
					break;
				case KeyEvent.VK_SPACE:
					message = new Message(Message.TYPE.FASTER);
					try 
					{
						viewToControllerQueue.put(message);
					} catch (InterruptedException e1) {}
					break;
					/* If the game is lost and user pressed enter button we restart the game */
				case KeyEvent.VK_ENTER:
					if(loose) 
					{
						loose = false;
						panel.setLoose(loose);
						message = new Message(Message.TYPE.RESTART);
						try 
						{
							viewToControllerQueue.put(message);
						} catch (InterruptedException e1) {}
					}
					break;
				} 
			}
			
			public void keyReleased(KeyEvent e) {}
		});
		
		
	}
	
	public void run()
	{
		try 
		{
			loop();
		}
		catch (Exception exception) {}
	}
	
	/**
	 * Loop that performs Controller and TimeController requests.
	 */
	private void loop() throws InterruptedException
	{
		Message message;
		while(true)
		{
			/* At the begin we check the request queue */
			message = controllerToViewQueue.take();
			if(message == null)
				continue;
			switch(message.getType())
			{
			case REDRAW:	
					redraw(message.getData());
					break;
			case CHANGE_NEXT: 
					panel.setNext(message.getAdd());
					break;
			case SCORES_UPDATE:
					panel.setScores(message.getAdd());
					break;
			case LOOSE:
					loose = true;
					panel.setLoose(loose);
					panel.redraw();
					break;
			default: 
					break;
			}
		}
	}
	
	/**
	 * @param c BlockingQueue used to receiving requests from Controller and TimeController
	 */
	public void setControllerToViewQueue(BlockingQueue<Message> c) 
	{
		controllerToViewQueue = c;
    }

	/**
	 * @param v Blocking Queue used to sending requests to Controller
	 */
    public void setViewToControllerQueue(BlockingQueue<Message> v) 
    {
        viewToControllerQueue = v;
    }
	
    /**
     * This method replace the figure id by adequate java.awt.Color. After doing this, it call redraw method from Panel and pass there
     * as a parameter two dimensional array of java.awt.Color objects.
     * @param board	two dimensional array of data (int values which are figures id).
     */
	public void redraw(int [][] board)
	{
		for(int i = 0; i<16; ++i)
		{
			for( int j = 0; j<10; ++j)
			{
				switch (board[i][j])
				{
				case 0:	colors[i][j] = Color.GRAY;
						break;
				case 1:	colors[i][j] = Color.YELLOW;
						break;
				case 2:	colors[i][j] = Color.RED;
						break;
				case 3:	colors[i][j] = Color.ORANGE;
						break;
				case 4:	colors[i][j] = Color.GREEN;
						break;
				case 5:	colors[i][j] = Color.CYAN;
						break;
				case 6:	colors[i][j] = Color.BLUE;
						break;
				case 7:	colors[i][j] = Color.PINK;
						break;	
				}
			}
		}
		panel.redraw(colors);
	}
}