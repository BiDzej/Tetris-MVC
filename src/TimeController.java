import java.util.concurrent.BlockingQueue;

/**
 * Second controller needed to create Tetris game. It is responsible for falling figure. It contains BlockingQueue
 * to communicate with view and model to change data of the game and it current status.
 * @author Bart³omiej Jurek
 *
 */
public class TimeController implements Runnable
{
	private Model model;
	private BlockingQueue <Message> controllerToViewQueue;	
	
	/**
	 * @param m Model shared with controller.
	 */
	public void setModel(Model m)
	{
		model = m;
	}
	
	/**
	 * @param c BlockingQueue shared with controller.
	 */
	public void setControllerToViewQueue(BlockingQueue<Message> c) 
	{
		this.controllerToViewQueue = c;
	}
	
	/**
	 * Main method of this class. It checks is it possible for falling figure to move one lvl down.
	 * If not it checks is it a loose or not, remove full lines, add points and pass it to View, random new figure,
	 * send information about falling figure and next figure to the View by BlockingQueue of Message objects. Figure drops every 0.5s.
	 */
	private void startTimer() throws InterruptedException
	{	
		Message message;
		
		/* Usypiany na 500ms co obieg watek, odpowiada za opadanie figury po planszy*/
		while(true)
		{
			if(model.loose) 
				{
					Thread.sleep(500);
					continue;
				}
			/* Jezeli da sie opasc to opada */
			if(model.isFallEnable()) {model.moveDown();}
			/* Jesli nie to sprawdzamy czy nieprzegrana gra */
			else if(model.isEnd())
			{
				model.loose = true;
				message = new Message(Message.TYPE.LOOSE);
				controllerToViewQueue.put(message);
			}
				else /* Jesli nieprzegrana to losujemy nowa figure, usuwamy pelne linie, update punktow */
				{
					model.figureCopy();
					model.newFigure();
					if(model.removeFullLines())
					{
						message = new Message(Message.TYPE.SCORES_UPDATE, model.getScores());
						controllerToViewQueue.put(message);
					}
					message = new Message(Message.TYPE.CHANGE_NEXT, model.getNext());
					controllerToViewQueue.put(message);
				}
			message = new Message(Message.TYPE.REDRAW, model.getData());
			controllerToViewQueue.put(message);
			Thread.sleep(500);
		}
	}
	
	public void run()
	{
		try 
		{
			startTimer();
		}
		catch (Exception exception) {}
	}
}
