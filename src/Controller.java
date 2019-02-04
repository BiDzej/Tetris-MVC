import java.util.concurrent.BlockingQueue;

/**
 * Class used to react on user requests. It contains 2 BlockingQueues (View to Controller and Controller to View) and Model object.
 * @author Bart³omiej Jurek
 */
public class Controller implements Runnable
{
	private Model model;
	
	private BlockingQueue <Message> controllerToViewQueue;
	private BlockingQueue <Message> viewToControllerQueue;
	
	/**
	 * @param m Model object shared with TimeController
	 */
	public void setModel(Model m)
	{
		model = m;
	}
	
	/**
	 * @param c BlockingQueue used to sending requests to View (shared with TimeController)
	 */
	public void setControllerToViewQueue(BlockingQueue<Message> c) 
	{
		this.controllerToViewQueue = c;
	}
	
	/**
	 * @param v Blocking Queue used to receiving requests from user (View)
	 */
	public void setViewToControllerQueue(BlockingQueue<Message> v) 
	{
		this.viewToControllerQueue = v;
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
	 * Main controller method (mainly loop). It is responsible for rotating figures, moving them right and left and rotate them.
	 * All of this actions are done by Model methods but Controller decides what to do and when by calling Model methods.
	 */
	private void loop() throws InterruptedException
	{
		/* We create new message and send to View next figure id */
		Message message;
		message = new Message(Message.TYPE.CHANGE_NEXT, model.getNext());
		controllerToViewQueue.put(message);
		
		/* main loop */
		while(true)
		{
			message = viewToControllerQueue.take();
			if(message == null)
				continue;
			
			/* If the game is lost and user pressed enter the game is being restarted. All other messages are ignored */
			if(model.loose)
			{
				if(message.getType()==Message.TYPE.RESTART) 
					{
						model.restart();
						message = new Message(Message.TYPE.SCORES_UPDATE, model.getScores());
						controllerToViewQueue.put(message);
					}
				else continue;
			}
			
			switch (message.getType())
			{
			case MOVE_LEFT:
				if(model.isLeftEnable()) 
				{
					model.moveLeft();
					message = new Message(Message.TYPE.REDRAW, model.getData());
					controllerToViewQueue.put(message);
				}
				break;
			case MOVE_RIGHT:
				if(model.isRightEnable())
				{
					model.moveRight();
					message = new Message(Message.TYPE.REDRAW, model.getData());
					controllerToViewQueue.put(message);
				}
				break;
			case ROTATE_LEFT:
				if(!model.isLeftRotationEnable()) break;
				model.rotateLeft();
				message = new Message(Message.TYPE.REDRAW, model.getData());
				controllerToViewQueue.put(message);
				break;
			case ROTATE_RIGHT:
				if(!model.isRightRotationEnable()) break;
				model.rotateRight();
				message = new Message(Message.TYPE.REDRAW, model.getData());
				controllerToViewQueue.put(message);
				break;
				/* Faster figure's falling is dropping it one lvl down while space bar is pressed */
			case FASTER:
				if(model.isFallEnable()) {model.moveDown();}
					/* Checking if the game is not lost */
					else if(model.isEnd()&&!model.loose)
					{
						model.loose = true;
						message = new Message(Message.TYPE.LOOSE);
						controllerToViewQueue.put(message);
					}
						else 
						{
							model.figureCopy();
							model.newFigure();
							/* If there were any full lines we send to View new scores amount */
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
				break;
			default:
				break;
			}
		}
	}
}
