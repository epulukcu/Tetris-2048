import java.util.Random;  // import Random class
import java.awt.Color; // import Color class
import java.awt.Point; // import Point class
import java.util.List;
import java.util.ArrayList;


// A class for representing tetrominoes with 7 different shapes ('I', 'S', 'Z', 'O', 'T', 'L' and 'J')
public class Tetrominos {
    // Private data fields
	private Color color;                                                // color of the tetromino
	private int gridWidth, gridHeight;                                  // dimensions of the tetris game grid   
	private Tile[][] tileMatrix;
	private Point bottomLeftCorner;

	// Constructor
	Tetrominos (char randomName, int gridHeight, int gridWidth) {
		this.gridHeight = gridHeight;
		this.gridWidth = gridWidth;
		// color of the tetrominos is determined randomly
		Random random = new Random();
		int red = random.nextInt(256), green = random.nextInt(256), blue = random.nextInt(256);
		color = new Color(red, green, blue);
		// set the shape of the tetromino based on the given name
		
		if (randomName == 'I') {
			// shape of the tetromino I in its initial orientation
			tileMatrix = new Tile[4][4];
			tileMatrix[1][0] = new Tile(new Point(5, gridHeight + 3));
			tileMatrix[1][1] = new Tile(new Point(5, gridHeight + 2));
			tileMatrix[1][2] = new Tile(new Point(5, gridHeight + 1));
			tileMatrix[1][3] = new Tile(new Point(5, gridHeight));
		}
		else if (randomName == 'S') {
			// shape of the tetromino Z in its initial orientation
			tileMatrix = new Tile[3][3];
			tileMatrix[1][0] = new Tile(new Point(6, gridHeight + 2));
			tileMatrix[2][0] = new Tile(new Point(7, gridHeight + 2));
			tileMatrix[0][1] = new Tile(new Point(5, gridHeight + 1));
			tileMatrix[1][1] = new Tile(new Point(6, gridHeight + 1));
		}
		else if (randomName == 'Z') {
			// shape of the tetromino S in its initial orientation
			tileMatrix = new Tile[3][3];
			tileMatrix[0][0] = new Tile(new Point(5, gridHeight + 2));
			tileMatrix[1][0] = new Tile(new Point(6, gridHeight + 2));
			tileMatrix[1][1] = new Tile(new Point(6, gridHeight + 1));
			tileMatrix[2][1] = new Tile(new Point(7, gridHeight + 1));
		}	
		else if (randomName == 'O') {
			// shape of the tetromino O in its initial orientation
			tileMatrix = new Tile[2][2];
			tileMatrix[0][0] = new Tile(new Point(5, gridHeight));
			tileMatrix[0][1] = new Tile(new Point(5, gridHeight + 1));
			tileMatrix[1][0] = new Tile(new Point(6, gridHeight));
			tileMatrix[1][1] = new Tile(new Point(6, gridHeight + 1));
		}			
		else if (randomName == 'T') {
			// shape of the tetromino T in its initial orientation
			tileMatrix = new Tile[3][3];
			tileMatrix[0][1] = new Tile(new Point(5, gridHeight + 1));
			tileMatrix[1][1] = new Tile(new Point(6, gridHeight + 1));
			tileMatrix[2][1] = new Tile(new Point(7, gridHeight + 1));
			tileMatrix[1][2] = new Tile(new Point(6, gridHeight));		}
		else if (randomName == 'L') {
			// shape of the tetromino L in its initial orientation
			tileMatrix = new Tile[3][3];
			tileMatrix[1][0] = new Tile(new Point(5, gridHeight + 2));
			tileMatrix[1][1] = new Tile(new Point(5, gridHeight + 1));
			tileMatrix[1][2] = new Tile(new Point(5, gridHeight));
			tileMatrix[2][2] = new Tile(new Point(6, gridHeight));
		}	
		else {
			// shape of the tetromino J in its initial orientation
			tileMatrix = new Tile[3][3];
			tileMatrix[1][0] = new Tile(new Point(6, gridHeight + 2));
			tileMatrix[1][1] = new Tile(new Point(6, gridHeight + 1));
			tileMatrix[0][2] = new Tile(new Point(5, gridHeight));
			tileMatrix[1][2] = new Tile(new Point(6, gridHeight));	
		}

		bottomLeftCorner = new Point(5, gridHeight);
	}	
    
	// Method for rotating tetromino clockwise by 90 degrees
	public boolean Rotate(GridA gameGrid) {

		if (bottomLeftCorner.y + tileMatrix.length > gridHeight)
			return false;

		// Check whether tetromino can rotate: all the blocks of the tetromino (including 
		// the empty ones) must be inside the game grid and there must be no occupied
		// grid square within the blocks of the tetromino
		int n = tileMatrix.length; // n = number of rows = number of columns
		for (int row = 0; row < n; row++) {
			for (int col = 0; col < n; col++) {				
				Point position = new Point();
				if(tileMatrix[row][col] != null)
					position = tileMatrix[row][col].getPosition();
				else {
					position.x = bottomLeftCorner.x + col;
					position.y = bottomLeftCorner.y + (n-1) - row;
				}
				
				if(position.x < 0 || position.x >= gridWidth)
					return false;
				
				if (gameGrid.isOccupied(position))
					return false;
			}
		}

		// rotate tetromino clockwise by 90 degrees
		Tile [][] rotatedMatrix = new Tile[n][n];
		for (int row = 0; row < n; row++) {
			for (int col = 0; col < n; col++) {
				if(tileMatrix[row][col]==null)
					continue;

				Point position = tileMatrix[row][col].getPosition();
				
				rotatedMatrix[col][n-1-row] = tileMatrix[row][col];
				position.x=bottomLeftCorner.x + (n-1-row);
				position.y=bottomLeftCorner.y + (n-1) - col;
				tileMatrix[row][col].setPosition(position);
			}
		}
		    
		tileMatrix = rotatedMatrix;		
		return true;
	}

	// Getter method for getting the color of tetromino
	public Color getColor() {
		return color;
	}

	// Method for displaying tetromino on the game grid	
	public void display() {
		for (Tile tile : getTileList())
		{
			tile.display();
		}
	}

	public boolean canMove(GridA gameGrid, Point move)
	{
		for (Tile tile : getTileList())
		{			
			Point nextPosition = new Point(tile.getPosition().x + move.x, tile.getPosition().y + move.y);
			if(nextPosition.x < 0 || 
					nextPosition.x >= gridWidth ||
					nextPosition.y < 0)
				return false;
			
			if (gameGrid.isOccupied(nextPosition))
				return false;
		}
		return true;
	}

	// Method for moving tetromino down by 1 in the game grid
	public boolean goDown(GridA gameGrid) {
		// Check whether tetromino can go down or not
		if (!canMove(gameGrid, new Point(0, -1))) 
			return false;

		for (Tile tile : getTileList())
		{
			tile.move(0, -1);
		}
		bottomLeftCorner.translate(0, -1);
		
		return true;
	}
	// Method for moving tetromino left by 1 in the game grid
	public boolean goLeft(GridA gameGrid) {
		if (!canMove(gameGrid, new Point(-1, 0)))
			return false;
		
		for (Tile tile : getTileList())
		{
			tile.move(-1, 0);
		}
		bottomLeftCorner.translate(-1, 0);
	
		return true;
	}

	// Method for moving tetromino right by 1 in the game grid
	public boolean goRight(GridA gameGrid) {
		if (!canMove(gameGrid, new Point(1, 0))) 
			return false;
		
		for (Tile tile : getTileList())
		{
			tile.move(1, 0);
		}
		bottomLeftCorner.translate(1, 0);
		
		return true;
	}	

	public List<Tile> getTileList() {
		List<Tile> aList = new ArrayList<Tile>(4);
		int n = tileMatrix.length;
		for (int row = 0; row < n; row++) {
			for (int col = 0; col < n; col++) {
				if(tileMatrix[row][col]==null)
					continue;
				aList.add(tileMatrix[row][col]);
			}
		}
		return aList;	
	}
}