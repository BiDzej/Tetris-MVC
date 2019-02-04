import java.util.Random;
import java.awt.Point;

/**
 * Class which contains all the game rules, objects, data about falling object and other objects which have already fallen down.
 * @author Bart³omiej Jurek
 */
public class Model 
{
	/**
	 * Main data array (there is no information about current falling figure). 0 means no figure, numbers 1-7 are id of the figure).
	 * Every figure has it own id, shape and color.
	 */
	private int [][] board;	
	
	/**
	 * Data array passing to View. It contains information about falling figure.
	 */
	private int [][] board_ex = new int [16][10];
	
	/**
	 * Shape of the falling figure (id).
	 */
	private int fShape;
	/**
	 * Actual rotation (it can be 0, 1, 2 or 3).
	 */
	private int fRotation;
	/**
	 * Position of the left top square from which we count the distance of single blocks of falling figure.
	 */
	private Point fPosition;
	/**
	 * Next figure in queue id.
	 */
	private int nextFigure;
	/**
	 * Our scores.
	 */
	private int scores;
	/**
	 * Information if the game is lost. True means game is over.
	 */
	public boolean loose;
	
	Random generator = new Random();
	
	/**
	 * It just call restart() method.
	 */
	public Model()
	{
		restart();
	}
	
	/**
	 * It prepares all the data for the new game.
	 */
	public void restart()
	{
		/* Creating main data board */
		board = new int [18][10];
		for (int i=0; i<18; ++i)
			for(int j=0; j<10; ++j)
				board[i][j] = 0;
		
		/* Random current falling figure and next falling figure to be shown as next */
		fShape = randFigure();
		fRotation = 0;
						  /* Coordinates keeping in Point (x, y) */
		fPosition = new Point(4, 0);
		nextFigure = randFigure();
		scores = 0;
		loose = false;
	}
	
	/**
	 * This method change current figure ID to next figure id, random new figure and set falling figure coordinates.
	 */
	public void newFigure()
	{
		fPosition.x = 4;
		fPosition.y = 0;
		fShape = nextFigure;
		nextFigure = randFigure();
	}
	
	/**
	 * @param val int value
	 * @return absolute value of val
	 */
	private int abs(int val)
	{
		if(val>0) return val;
		return -val;
	}
	
	/**
	 * Random figure (number from 1 to 7)
	 * @return random figure id
	 */
	private int randFigure()
	{
		return abs(generator.nextInt()%7) + 1;
	}
	
	/**
	 * @return next figure id
	 */
	public int getNext()
	{
		return nextFigure;
	}
	
	/**
	 * @return our scores
	 */
	public int getScores()
	{
		return scores;
	}
	
	/**
	 * This method checks is it possible for the figure to drop one lvl down.
	 * @return true if it is possible, false if it is not.
	 */
	public boolean isFallEnable()
	{
		for(int i = 0; i<4; ++i)
		{
			/* We check every block of falling figure if it will not be under the board */
			if(fPosition.y + Figures.FiguresList[fShape-1][fRotation][i].y >= 17)
				return false;
			
			/* We check the collision of every figure's block */
			if(board[fPosition.y + Figures.FiguresList[fShape-1][fRotation][i].y + 1][fPosition.x + Figures.FiguresList[fShape-1][fRotation][i].x]!=0)
				return false;
		}
		return true;
	}
	
	/**
	 * Method which checks if it is possible to move left the falling figure.
	 * @return true if it is possible, false if it is not.
	 */
	public boolean isLeftEnable()
	{
		for(int i=0; i<4; ++i)
		{
			/* Checking if we are not on the board edge */
			if(fPosition.x + Figures.FiguresList[fShape-1][fRotation][i].x <= 0) 
				return false;
			
			/* Checking collision for every figure's block */
			if(board[fPosition.y + Figures.FiguresList[fShape-1][fRotation][i].y][fPosition.x + Figures.FiguresList[fShape-1][fRotation][i].x-1]!=0)
				return false;
		}
		return true;
	}
	
	/**
	 * Method which checks if it is possible to move right the falling figure.
	 * @return true if it is possible, false if it is not.
	 */
	public boolean isRightEnable()
	{
		for(int i=0; i<4; ++i)
		{
			/* Checking if we are not on the board edge */
			if(fPosition.x + Figures.FiguresList[fShape-1][fRotation][i].x >= 9) 
				return false;
			
			/* Checking collision for every figure's block */
			if(board[fPosition.y + Figures.FiguresList[fShape-1][fRotation][i].y][fPosition.x + Figures.FiguresList[fShape-1][fRotation][i].x+1]!=0)
				return false;
		}
		return true;
	}
	
	/**
	 * Method that checks if it is possible to rotate right the falling figure.
	 * @return true if it is possible, false if it is not.
	 */
	public boolean isRightRotationEnable()
	{
		int nextPos = fRotation - 1;
		if(nextPos<0) nextPos = 3;
		
		for(int i=0; i<4; ++i)
		{
			/* Checking if any piece of the figure won't be apart the board */
			if(fPosition.x + Figures.FiguresList[fShape-1][nextPos][i].x > 9) return false;
			/* Checking collision for every figure's block */
			if(board[fPosition.y + Figures.FiguresList[fShape-1][nextPos][i].y][fPosition.x + Figures.FiguresList[fShape-1][nextPos][i].x]!=0) return false;
		}
		return true;
	}
	
	/**
	 * Method that checks if it is possible to rotate left the falling figure.
	 * @return true if it is possible, false if it is not.
	 */
	public boolean isLeftRotationEnable()
	{
		int nextPos = (fRotation + 1)%4;
		
		for(int i=0; i<4; ++i)
		{
			/* Checking if any piece of the figure won't be apart the board */
			if(fPosition.x + Figures.FiguresList[fShape-1][nextPos][i].x > 9) return false;
			/* Checking collision for every figure's block */
			if(board[fPosition.y + Figures.FiguresList[fShape-1][nextPos][i].y][fPosition.x + Figures.FiguresList[fShape-1][nextPos][i].x]!=0) return false;
		}
		return true;
	}
	
	/**
	 * Move left falling figure (change figure's position)
	 */
	public void moveLeft()
	{
		fPosition.x = fPosition.x - 1;
	}
	
	/**
	 * Move left falling figure (change figure's position)
	 */
	public void moveRight()
	{
		fPosition.x = fPosition.x + 1;
	}
	
	/**
	 * Move left falling figure (change figure's position)
	 */
	public void moveDown()
	{
		fPosition.y = fPosition.y + 1;
	}
	
	/**
	 * Method changes rotation number to greater one. If it is 4 it is changed to 0.
	 */
	public void rotateRight()
	{
		fRotation = (fRotation+1)%4;
	}
	
	/**
	 * Method changes rotation number to lower one. If it is lower than 0 it is changed to 3.
	 */
	public void rotateLeft()
	{
		if(fRotation==0) fRotation = 3;
		else fRotation = fRotation - 1;
	}
	
	/**
	 * Method that checks if we have any full lines on the board. What is more it deletes full rows and adds calculated amount of points.
	 * @return Amount of full lines detected. 
	 */
	public boolean removeFullLines()
	{
		boolean isLineFull = true;
		int lines = 0;
		int tmp = 10;
		
		/* Checking all rows starting from the top */
		for(int i=2; i<18; ++i)
		{
			/* If it detects empty block line is not full so change isLineFull to false and break */
			for (int j=0; j<10; ++j)
			{
				if(board[i][j]==0) 
				{
					isLineFull = false;
					break;
				}
			}
			/* If it wasn't a full line we check next one */
			if(isLineFull==false) 
			{
				isLineFull=true; 
				continue;
			}
			
			/* Move everything (except falling figure) */
			++lines;
			for(int j=0; j<10; ++j)
				for(int k=i; k>=2; --k)
				{
					board[k][j]=board[k-1][j];
				}
		}
		/* We simply calculate amount of points */
		for (int i=0; i<lines; ++i)
		{
			scores += tmp;
			tmp*=2;
		}
		if(lines!=0) return true;
		return false;
	}
	
	/**
	 * Method checks if the game is lost (is any figure's block on forbidden place).
	 * @return true if game is lost, false if it is not.
	 */
	public boolean isEnd()
	{
		for(int i=0; i<4; ++i)
			if(fPosition.y + Figures.FiguresList[fShape-1][fRotation][i].y < 2) return true;
		return false;
	}

	/**
	 * After the figure fell down we cane copy every single figure's block on the main data array (from this time it's static).
	 */
	public void figureCopy()
	{
		for(int i=0; i<4; ++i)
		{
			board[fPosition.y + Figures.FiguresList[fShape-1][fRotation][i].y][fPosition.x + Figures.FiguresList[fShape-1][fRotation][i].x] = fShape;
		}
	}
	
	/**
	 * @return Two dimensional array with all data (blocks which have already fell down and falling figure with actual position and rotation).
	 */
	public int [][] getData()
	{
		/* Copying main array of data (only the part which is visible on the board) */
		for(int i=0; i<16; ++i)
			for(int j=0; j<10; ++j)
			{
				board_ex[i][j] = board[i+2][j];
			}
		/* Copying the falling figure */
		for(int i=0; i<4; ++i)
		{
			if(fPosition.y + Figures.FiguresList[fShape-1][fRotation][i].y-2 >= 0)
				board_ex[fPosition.y + Figures.FiguresList[fShape-1][fRotation][i].y-2][fPosition.x + Figures.FiguresList[fShape-1][fRotation][i].x] = fShape;
		}
		return board_ex;
	}
}
