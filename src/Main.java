/* INFORMATION ABOUT FIGURE'S COLOR AND SHAPE
 * 0 - No figure (GRAY)
 * 1 - J-shape (YELLOW)
 * 2 - Z-shape (RED)
 * 3 - L-shape (ORANGE)
 * 4 - S-shape (GREEN)
 * 5 - I-shape (CYAN)
 * 6 - O-shape (BLUE)
 * 7 - T-shape (PINK)
 */

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
/**
 * Main class of my Tetris game. It creates all objects, runs them as threads, gives them BlockingQueues 
 * to communicate and give Model object to both Controllers.
 * @author Bart³omiej Jurek
 */
public class Main 
{	
	private View view;
	private Model model;
	private Controller controller;
	private TimeController tController;
	
	private Thread controllerThread;
	private Thread viewThread;
	private Thread tControllerThread;
	
	/* Creating BlockongQueues */
    private BlockingQueue<Message> controllerToViewQueue= new ArrayBlockingQueue<>(1024);
    private BlockingQueue<Message> viewToControllerQueue= new ArrayBlockingQueue<>(1024);
	
	public Main()
	{
		/* Creating MVC (with 2 controllers) */
		model = new Model();
		view = new View();
		controller  = new Controller();
		tController = new TimeController();
		
		/* Passing BlockingQueues and model to Controller */
		controller.setModel(model);
		controller.setControllerToViewQueue(controllerToViewQueue);
        controller.setViewToControllerQueue(viewToControllerQueue);
        
        /* TimeController does not need viewToController BlockingQueue because it does not interact with user */
        tController.setModel(model);
        tController.setControllerToViewQueue(controllerToViewQueue);
        
        /* View does not need Model (data) because all it needs it receives from messages */
        view.setControllerToViewQueue(controllerToViewQueue);
        view.setViewToControllerQueue(viewToControllerQueue);
        
        /* Creating and running threads */
        controllerThread = new Thread(controller);
        viewThread = new Thread(view);
        tControllerThread = new Thread(tController);
        controllerThread.start();
        viewThread.start();
        tControllerThread.start();
	}
	
	
	public static void main(String [] args)
	{
		/* Creating Main class object starts the game */
		Main tetris = new Main();
	}
}
