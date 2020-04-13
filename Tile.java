import java.awt.Button;
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
			new Color(255, 229, 204),   // 2
			new Color(255, 204, 153),   //4
			new Color(255, 178, 102),   //8
			new Color(255, 153, 51),    //16
			new Color(236, 92, 92),     //32
			new Color(238, 50, 50),     //64
			new Color(226, 226, 49),    //128
			new Color(191, 191, 20),    //256
			new Color(156, 156, 30),    //512
			new Color(124, 124, 43),    //1024
			new Color(144, 144, 66)};   //2048
		
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
	//The method of reaching the positions of the tiles
	public Point getPosition(){
		return (Point) position.clone(); //the position point was cloned for later use for reference
	}
	//The method set the position of tiles
	public void setPosition(Point position){
			this.position = position;
	}
    //The method that moves or translates tile movements to desired points
	public void move(int x, int y){
		position.translate(x, y);
	}
    //The method to reach the numbers on the tile
	public Integer getNumber() {
		return this.number;
	}
	//The method of increasing the number written on the merged tiles
	public void incNumber(int value) {
		
		this.number += value;    //the desired number is added to the number on the tile
		
		if (this.number > 2048)  //the maximum number of 2048 is written on the tile
			this.number = 2048;
		/*
		 * According to the new number written on the tile, 
		 * the index number was found in the "numbers" array. 
		 * Thus, it was assigned to the tile in the color 
		 * corresponding to the index of the number.	
		 */
		int i = Arrays.asList(numbers).indexOf(this.number); 
		this.backgroundColor = colors[i];
	}
    //The method of reach the colors of the tiles
	public Color getBackgroundColor() {
		return this.backgroundColor;
	}
	//The method of displaying the tiles in the desired feature on the grid
	public void display() {
		StdDraw.setPenColor(backgroundColor);
		StdDraw.filledSquare(position.x, position.y, 0.5);
		StdDraw.setPenColor(foregroundColor);
		StdDraw.text(position.x, position.y, Integer.toString(number));
	}

	//The method be used to sort tiles in a list by position.y
	public int compareTo(Tile other)  { 
        return other.getPosition().y - this.getPosition().y;
    } 
}