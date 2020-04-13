import java.awt.Point; 
import java.util.List;
import java.util.ArrayList;


//The Tetrominoes class for representing tetrominoes with 7 different shapes ('I', 'S', 'Z', 'O', 'T', 'L' and 'J')
public class Tetrominoes {
    // Private data fields
	private int gridWidth, gridHeight;    //dimensions of the tetris game grid   
	private Tile[][] tileMatrix;          //keep each shape of tetromino in tileMatrix
	private Point bottomLeftCorner;       //reference point of the matrix
	private char Name;                    //tetromino's name
	public Boolean GameOver = false;      //check the Game Over

	// Constructor
	Tetrominoes (char randomName, int gridHeight, int gridWidth) {
		this.gridHeight = gridHeight;
		this.gridWidth = gridWidth;
		this.Name = randomName;
		
		// set the shape of the tetromino based on the given name
		if (randomName == 'I') {
			// shape of the tetromino I in its initial orientation
			tileMatrix = new Tile[4][4];
			tileMatrix[1][0] = new Tile(new Point(5, gridHeight + 3));
			tileMatrix[1][1] = new Tile(new Point(5, gridHeight + 2));
			tileMatrix[1][2] = new Tile(new Point(5, gridHeight + 1));
			tileMatrix[1][3] = new Tile(new Point(5, gridHeight));
		}
		else if (randomName == 'Z') {
			// shape of the tetromino Z in its initial orientation
			tileMatrix = new Tile[3][3];
			tileMatrix[1][0] = new Tile(new Point(6, gridHeight + 2));
			tileMatrix[2][0] = new Tile(new Point(7, gridHeight + 2));
			tileMatrix[0][1] = new Tile(new Point(5, gridHeight + 1));
			tileMatrix[1][1] = new Tile(new Point(6, gridHeight + 1));
		}
		else if (randomName == 'S') {
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
			tileMatrix[1][2] = new Tile(new Point(6, gridHeight));		
		}
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
		//control of the entry of each tetromino into the grid with bottomLeftCorner
		bottomLeftCorner = new Point(5, gridHeight);
	}	
	/*
	 * Each tetromino in the grid has been subjected to method 
	 * checks according to its name, so it was the method written 
	 * for the shapes to come randomly.
	 */
	public char Name() {
		return Name;
	}
	
	//The method for rotate the tetromino clockwise by 90 degrees
	public boolean Rotate(Grid gameGrid) {
        /*
         * Tetromino cannot be rotated when the sum of the size of the tileMatrix 
         * and the y coordinate of the received reference point exceeds the height of the grid
         */
		if (bottomLeftCorner.y + tileMatrix.length > gridHeight) 
			return false;

	/*
	 * Check whether tetromino can rotate: all the blocks of the tetromino (including  the empty ones)
	 *  must be inside the game grid and there must be no occupied grid square within the blocks of the tetromino
	 */
		int n = tileMatrix.length;  // n = the rows number of tetromino = the columns number of tetromino
		for (int row = 0; row < n; row++) {
			for (int col = 0; col < n; col++) {				
				Point position = new Point();
				/*
				 * If any block of tileMatrix was ​​full, the getPosition of the full block assigned the position. 
				 * If the block was empty, the x and y coordinate of the empty block was defined by the reference point.
				 */
				if(tileMatrix[row][col] != null)
					position = tileMatrix[row][col].getPosition();
				else {
					position.x = bottomLeftCorner.x + col;
					position.y = bottomLeftCorner.y + (n-1) - row;
				}
				//If the tile's x coordinate is less than 0 or larger than the width of the grid, the tile cannot move.
				if(position.x < 0 || position.x >= gridWidth)
					return false;
				//Block filled in tileMatrix cannot overlap a filled block in gameGrid.
				if (gameGrid.isOccupied(position))
					return false;
			}
		}

		Tile [][] rotatedMatrix = new Tile[n][n];  //new array to hold the rotated matrix
		for (int row = 0; row < n; row++) {        //tileMatrix is ​​checked along its rows and columns
			for (int col = 0; col < n; col++) {
				if(tileMatrix[row][col]==null)     //if any block of tileMatrix is empty, processing continues
					continue;
                /*
                 * The position of the empty block is checked then tileMatrix is ​​returned 
                 * by updating the required positions according to the empty blocks. 
                 * The new position of the returned tileMatrix is ​​updated with setPosition
                 */
				Point position = tileMatrix[row][col].getPosition(); 	
				rotatedMatrix[col][n-1-row] = tileMatrix[row][col];  
				position.x=bottomLeftCorner.x + (n-1-row);
				position.y=bottomLeftCorner.y + (n-1) - col;
				tileMatrix[row][col].setPosition(position);
			}
		}	    
		tileMatrix = rotatedMatrix;  //tileMatrix is now a rotated matrix
		return true;
	}

	//The method for displaying tetromino on the game grid	
	public void display() {
		for (Tile tile : getTileList()){
			tile.display();
		}
	}
    //The method that controls which tiles can move
	public boolean canMove(Grid gameGrid, Point move)
	{
		for (Tile tile : getTileList())
		{			
			//new position was created with the "move" reference point for the movement of the tiles
			Point nextPosition = new Point(tile.getPosition().x + move.x, tile.getPosition().y + move.y);
			//tile cannot move if the tile's next position is not within the grid limits
			if (nextPosition.x < 0 || nextPosition.x >= gridWidth || nextPosition.y < 0)
				return false;
			//the tile cannot move if the next position of the tile corresponds to the position of the full block of the grid
			if (gameGrid.isOccupied(nextPosition))
				return false;
		}
		return true;
	}

	//The method for moving tetromino down by 1 in the game grid
	public boolean goDown(Grid gameGrid) {
		//check whether tetromino can go down or not
		if (!canMove(gameGrid, new Point(0, -1)))
		{
			//the game ends if any column of the grid is full
			if (bottomLeftCorner.y + tileMatrix.length >= gridHeight) {						
				GameOver = true;
			}			
			return false;
		}
      
		for (Tile tile : getTileList())	{
			tile.move(0, -1);   //each tile in getTileList goes down once along the y coordinate
		}
		bottomLeftCorner.translate(0, -1);  //the reference point of tetromino is updated
		return true;
	}
	
	//The method for moving tetromino left by 1 in the game grid
	public boolean goLeft(Grid gameGrid) {
		//unable to move, the tetromino also  cannot move to the left
		if (!canMove(gameGrid, new Point(-1, 0)))
			return false;
		
		for (Tile tile : getTileList()){
			tile.move(-1, 0);  //each tile in getTileList moves left once along the x coordinate
		}
		bottomLeftCorner.translate(-1, 0);  //the reference point of tetromino is updated
		return true;
	}

	//The method for moving tetromino right by 1 in the game grid
	public boolean goRight(Grid gameGrid) {
		//unable to move, the tetromino also  cannot move to the right
		if (!canMove(gameGrid, new Point(1, 0))) 
			return false;
		
		for (Tile tile : getTileList())	{
			tile.move(1, 0);  //each tile in getTileList moves right once along the x coordinate
		}
		bottomLeftCorner.translate(1, 0);  //the reference point of tetromino is updated		
		return true;
	}	
    //The method of the list holding blocks filled in tetromino
	public List<Tile> getTileList() {
		List<Tile> aList = new ArrayList<Tile>(4);  //each tetromino has 4 tiles so the alist of which consists of 4 tiles
		int n = tileMatrix.length;   // n = the rows number of tetromino = the columns number of tetromino
		/*
		 * Every block of TileMatrix is checked, if the block is empty, other blocks are checked, 
		 * if full, the block is added to the list.
		 */
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