import java.awt.*;
import javax.swing.*;

/**
 * Panel is class extending JPanel. The additional variables are 2 dimensional array of data (awt.Color objects) to be drawn on the JPanel,
 * nextShape (id of the next falling figure), actual scores, 2 fonts using to show information for the player, and boolean variable loose which 
 * defines if "Game over" string should be shown on the board. setLoose, setNext, setScores are methods which allow to change private variables.
 * We have there two methods called redraw. One we use to redraw all the interface after collision, rotating, falling figure. 
 * Second (without parameter) is used to redraw the same board but with different points value or show information about lost game.
 * @author Bart³omiej Jurek
 */

public class Panel extends JPanel
{
	private static final long serialVersionUID = -8715353373678321308L;
	private Color [][] colors;
	private int nextShape;
	private int scores;
	private Font font = new Font("Dialog", Font.BOLD, 18);
	private Font font1 = new Font("Dialog", Font.BOLD, 70);
	private boolean loose = false;
	
	/**
	 * @param colors Two dimensional array of java.awt.Color objects. We need it on the start to show empty board (without figures). Without
	 * this type of constructor there would be a delay between starting game and drawing interface. 
	 */
	public Panel(Color[][] colors)
	{
		this.colors = colors;
	}
	
	/**
	 * @param l boolean variable. If true it means that the game is lost and we should write "Game Over" information on the screen.
	 */
	public void setLoose(boolean l)
	{
		loose = l;
	}
	
	/**
	 * @param scores points amount we have in current moment. We need it to show this information on the board without getting it in every redraw request.
	 */
	public void setScores(int scores)
	{
		this.scores = scores;
	}
	
	/**
	 * @param next ID of the next figure which will fall (figure which should be shown on the right panel.
	 */
	public void setNext(int next)
	{
		nextShape = next;
	}
	
	/**
	 * This method is used to simply redraw full interface of the game. All we need is colors array (when we want to redraw board 
	 * with falling object). 
	 * @param colors Two dimensional array of java.awt.Color objects
	 */
	public void redraw (Color[][] colors)
	{
		this.colors = colors;
		repaint();
	}
	
	/**
	 * Second redraw method used to redraw interface for example when the game is over or while we get additional points.
	 * (Simply where there are no changes on main board).
	 */
	public void redraw()
	{
		repaint();
	}
	
	/**
	 * First we paint all board on the dark gray color. Next we draw all the main board squares using colors double array. 
	 * Next using NextFigures we draw figure that will be next and the last one is to show scores. If the game is over it shows 
	 * this string on the board.
	 */
	public void paintComponent(Graphics g)
	{
		/* Malujemy cala plansze na ciemny szary */
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, 522, 40*16);
		
		/* Na podstawie tablicy kolorow malujemy wszytskie pola planszy glownej */
		for(int i=0; i<16; ++i)
			for(int j=0; j<10; ++j)
			{
				g.setColor(colors[i][j]);
				g.fillRect(40*j, 40*i, 38, 38);
			}
		
		/* Na podstawie informacji o kolejnym ksztalcie malujemy nastepna w kolejce figure */
		for(int i=0; i<4; ++i)
			for(int j=0; j<4; ++j)
			{
				g.setColor(NextFigures.FiguresList[nextShape-1][i][j]);
				g.fillRect(402 + 25*j, 30 + 25*i, 23, 23);
			}
		
		/* Wyswietlamy informacje o punktach */
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("SCORES", 415, 200);
		g.drawString(""+scores, 442, 230);
		
		/* Jezeli gra zostala przegrana wypisujemy stosowna informacje */
		if(loose)
		{
			g.setColor(Color.WHITE);
			g.setFont(font1);
			g.drawString("GAME OVER", 50, 300);
			g.setFont(font);
			g.drawString("(Press ENTER to play again)", 145, 350);
		}
	}
}
