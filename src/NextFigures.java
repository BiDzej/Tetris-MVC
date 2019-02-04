import java.awt.Color;

/**
 * @author Bart³omiej Jurek
 * Class without any methods. All we have there is 3 dimensional array (7 shapes with square 4x4 each one). 
 * It's array of awt.Color objects which is used to draw object which will fall next. Array is static and final so we can
 * use it without creating NextFigures object and it's always constant.
 */

/* Tablica kolorow (opisany kwadrat 4x4 pola) sluzaca do wyswietlania kolejnej figury (nastepna wylosowana figura) */
public class NextFigures {
	public static final Color[][][] FiguresList = 
		{
				{	//J - shape
					{Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY},
					{Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.GRAY},
					{Color.GRAY, Color.GRAY, Color.YELLOW, Color.GRAY},
					{Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY}
				},
				
				{	//Z - shape
					{Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY},
					{Color.RED, Color.RED, Color.GRAY, Color.GRAY},
					{Color.GRAY, Color.RED, Color.RED, Color.GRAY},
					{Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY}
				},
				
				{	//L - shape
					{Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY},
					{Color.GRAY, Color.GRAY, Color.ORANGE, Color.GRAY},
					{Color.ORANGE, Color.ORANGE, Color.ORANGE, Color.GRAY},
					{Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY}
				},
				
				{	//S - shape
					{Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY},
					{Color.GRAY, Color.GRAY, Color.GREEN, Color.GREEN},
					{Color.GRAY, Color.GREEN, Color.GREEN, Color.GRAY},
					{Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY}
				},
				
				{	//I - shape
					{Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY},
					{Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY},
					{Color.CYAN, Color.CYAN, Color.CYAN, Color.CYAN},
					{Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY}
				},
				
				{	//O - shape
					{Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY},
					{Color.GRAY, Color.BLUE, Color.BLUE, Color.GRAY},
					{Color.GRAY, Color.BLUE, Color.BLUE, Color.GRAY},
					{Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY}
				},
				
				{	//T - shape
					{Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY},
					{Color.GRAY, Color.PINK, Color.GRAY, Color.GRAY},
					{Color.PINK, Color.PINK, Color.PINK, Color.GRAY},
					{Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY}
				}
		};
}
