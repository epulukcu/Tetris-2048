import java.awt.Color;
import java.awt.Point;
import java.util.Random;
import java.util.Arrays;
//The Tile class is a comparable class for comparing the tiles from top to bottom
public class Tile implements Comparable<Tile> {

	//final numbers on the tile
	private final static Integer[] numbers = {2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048};
	//final colors on the tile
	private final static Color[] colors = {
			new Color(255, 255, 0), 
			new Color(255, 225, 0), 
			new Color(255, 220, 0),
			new Color(255, 175, 0),
			new Color(255, 150, 0),
			new Color(255, 125, 0),
			new Color(255, 100, 0),
			new Color(255, 75, 0),
			new Color(255, 50, 0),
			new Color(255, 25, 0),
			new Color(255, 0, 0)};

	private int number;              // number on the tile
	private Color backgroundColor;   // color for displaying the tile
	private Color foregroundColor = Color.BLACK; // color for displaying the number
	private Point position;          //position of tile

	Tile(Point pos){                 //default constructor
		Random rand = new Random();  //for random cases
		int i = rand.nextInt(2);
		this.number = numbers[i];
		this.backgroundColor = colors[i];
		this.position = (Point) pos.clone(); //the exact copy of an object
	}

	public Point getPosition(){
		return (Point) position.clone();
	}

	public void setPosition(Point position){
		this.position = position;
	}

	public void move(int x, int y){
		position.translate(x, y);
	}

	public Integer getNumber() {
		return this.number;
	}
	//The function of the collection of overlapping tiles
	public void incNumber(int value) {

		this.number += value;       //add the same tile numbers
		if (this.number > 2048)
			this.number = 2048;

		int i = Arrays.asList(numbers).indexOf(this.number);
		this.backgroundColor = colors[i];
	}

	public Color getBackgroundColor() {
		return this.backgroundColor;
	}

	public void display() {
		StdDraw.setPenColor(backgroundColor);
		StdDraw.filledSquare(position.x, position.y, 0.5);
		StdDraw.setPenColor(foregroundColor);
		StdDraw.text(position.x, position.y, Integer.toString(number));
	}

	//The function be used to sort tiles in a list by position.y
	public int compareTo(Tile other) 
	{ 
		return other.getPosition().y - this.getPosition().y;
	} 

}