import java.awt.Color;
import java.awt.Point;
import java.util.Random;

public class Tile {

	private final static int[] numbers = {2, 4, 8, 16};
	private final static Color[] colors = {Color.YELLOW, Color.ORANGE, Color.RED};
		
	private int number; // number on the tile
	private Color backgroundColor; // color for displaying the tile
	private Color foregroundColor = Color.BLACK; // color for displaying the number
	private Point position;
	
	Tile(Point pos){ //default constructor
		Random rand = new Random();
		int i = rand.nextInt(2);
		this.number = numbers[i];
		this.backgroundColor = colors[i];
		this.position = (Point) pos.clone();;
	}
	
	public Point getPosition(){
		return position;
	}
	
	public void setPosition(Point position){
			this.position = position;
	}

	public void move(int x, int y){
		position.translate(x, y);
	}
	
	public void display() {
		StdDraw.setPenColor(backgroundColor);
		StdDraw.filledSquare(position.x, position.y, 0.5);

		StdDraw.setPenColor(foregroundColor);
		StdDraw.text(position.x, position.y, Integer.toString(number));
	}
	
}