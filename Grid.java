import java.awt.Point; 
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Collections;


//The Grid class representing the Tetris2048 game grid
public class Grid {
	
	
	public volatile static boolean paused = false;
	
	// Private data fields
	private int Height;  //height of grid
	private int Width;   //width of grid
	private List<Tile> TileList = new ArrayList<Tile>();  //list of tiles on the grid
	private int Score;   //score for game Tetris2048

	// Constructor
	Grid (int n_rows, int n_cols) {
		this.Height = n_rows;
		this.Width = n_cols;
		this.Score = 0;
	}
	
	//The method used for checking whether the square with given indices is occupied or empty
	public boolean isOccupied(Point blockPos) {
		Boolean response = false;
		for (Tile tile : TileList) {
			/*
			 * It is checked whether the position of any block of the grid is equal 
			 * to the position of any tile in the TileList. If there is such equality, 
			 * the block in that position is full, if not, the block is empty.
			 */
			if (tile.getPosition().equals(blockPos)) {
				response = true;
				break;
			}
		}
		return response;
	}
	
	//The method for updating the game grid with a placed (stopped) tetromino
	public void updateGrid(Tetrominoes tet) {
		
		TileList.addAll(tet.getTileList());      //TileList holds every tile in the created tetromino

		List<Tile> list = new ArrayList<Tile>(); //the list of tiles that the tetromino that was the last display on the grid should go down
		list = tet.getTileList();
		//The tiles that need to go down are sorted by position.y before they are processed in the correct order.
		Collections.sort(list, Collections.reverseOrder());
		for (Tile tile : list) {
			Point position = tile.getPosition();
			int i;  //the number of i that tells how much the tiles that need to go down
			for (i = position.y-1; i >= 0; i--) {   //found y coordinate where the first full block under the tile 
				/*
				 * If the block under the tile is full, the tile will not 
				 * go down anyway and the loop will end with break
				 */
				if (isOccupied(new Point(position.x, i))) {
					break;
				}
			}
				if (i+1 != position.y) {  //when the tile goes down at least 1 line
				Point newPosition = new Point(position.x, i+1); //new position to move the tile
				//with indexOf, the position of the tile in the TileList was found and updated with the new position
				TileList.get( TileList.indexOf(tile) ).setPosition(newPosition);			
			}
		}
	}
	//The method used to display the grid.
	public void display() throws InterruptedException {
		for (int row = 0; row < this.Width; row++)
			for (int col = 0; col < this.Height; col++) {
				StdDraw.setPenColor(StdDraw.BLACK);
				StdDraw.filledSquare(row, col, 0.5);
			}

		//drawing the grid
		StdDraw.setPenColor(StdDraw.BOOK_BLUE);
		for (double x = -0.5; x < this.Width; x++)       // vertical lines on the grid
			StdDraw.line(x, -0.5, x, this.Height - 0.5);
		for (double y = -0.5; y < this.Height; y++)      // horizontal lines on the grid
			StdDraw.line(-0.5, y, this.Width - 0.5, y);
		//display tiles on the grid
		for (Tile tile : TileList){
			tile.display();
		}
		
		Button btn = new Button(14, 13, 100, 100, "PAUSE");  //create a button in desired positions on the screen
		btn.draw();    //draw a button with specified features
       
		//pause button works if mouse clicked
		if (StdDraw.isMousePressed()) {
			
			if (paused == true){
				setAction(false);
			}
				else{
				setAction(true);
			}		
		}	
	}
   //The method set of pause state.
   public static void setAction(boolean x) {
	  paused = x;	  
   }
   //The method of reach the pause action of the game from other class.
   public static boolean getAction() {
	  return paused;
   }	
	//The method where full lines are determined and deleted.
	public void checkFullLines() {
		int[] tileCounts = new int[this.Height];  //array that holds how many filled tiles are in each row
		for (int i = 0; i < this.Height; i++) {   //initially lines are filled with 0 as if empty
			tileCounts[i] = 0;
		}

		List<Integer> lineList = new ArrayList<Integer>(); //keep the numbers of the rows to be deleted in the list
		for (Tile tile : TileList) {			
			Point position = tile.getPosition();
			tileCounts[position.y]++;                   //in each line where tiles, the number of tiles in that line is increased
			if (tileCounts[position.y] == this.Width) { //the number of tiles with the same position.y is equal to the number of columns
				lineList.add(position.y);               //the number of the line to be deleted is added to the list when the condition is met
			}
		}
        //If the number of elements in lineList is greater than 0, there line/lines to be deleted
		if (lineList.size() > 0) {
			List<Tile> removeList = new ArrayList<Tile>();           //list of tiles to be deleted			
			for (Tile tile : TileList) {
				if (lineList.indexOf(tile.getPosition().y) > -1) {   //if the tile has a position.y in the lineList
					removeList.add(tile);                            //tile is added to removeList list to be deleted when condition is met
					this.Score += tile.getNumber();                  //numbers on deleted tiles are added to Score
				}
			}
            //Tt is a problem to delete something from the list while navigating inside the list, so we keep it in a list and delete it completely.
			TileList.removeAll(removeList);     //delete all tiles in removeList
			
			//The Collections.sort method is to sort lineList elements in descending order so that tiles can go down in the correct order.
			Collections.sort(lineList, Collections.reverseOrder()); 
			
			//After the deleted line, every tile in the top line should go down.
			for (int line_no : lineList) {
				for (Tile tile : TileList) {
					/*
					 * If the tile's y coordinate is greater than the number of the deleted line, 
					 * the tile must go down in the y coordinate with the move command
					 */
					if (tile.getPosition().y > line_no) {
						tile.move(0, -1);
					}
				}
			}		
		}
	}
	//The method that determines in which columns tile merged will occur.
	public void do2048(Tetrominoes tet) {
		List<Integer> x_list = new ArrayList<Integer>(); //list for the position.x of the tiles in tetromino
		/*
		 * getTileList, where each tile of the tetromino is held, 
		 * is checked and tiles with the same x coordinate are added to x_list. 
		 * Make sure that there is one of each x coordinate in the list.
		 */
		for (Tile tile : tet.getTileList()) {
			Point position = tile.getPosition();
			if (!x_list.contains(position.x))   //there will be one from each x coordinate
				x_list.add(position.x);
		}
        //the checkColumn method is applied for each coordinate in x_list
		for (int X : x_list) {           
			checkColumn(X);
		}
	}
	//The method to the process of tiles for each column.
	private void checkColumn(int column) {
		List<Tile> list = new ArrayList<Tile>(); //a list of the tiles that may be mergers
		/*
		 * By looking at the positions of the tiles in the TileList, 
		 * whichever x coordinate is in the specified column has been added to the list. 
		 * In this way, all the tiles in the x column of tetromino were kept in a list.
		 */
		for (Tile tile : TileList) {			
			Point position = tile.getPosition();
			if (column == position.x)
				list.add(tile);  
		}
		
		if (list.size() > 1) {
			Collections.sort(list);  //the list is sorted from top to bottom

			ListIterator<Tile> it = list.listIterator();  //the elements of the list are accessed by going back and forth
			Tile prevTile = it.next();                    //determination of the tile to be treated
			Tile currentTile;
			while (it.hasNext()) {                        //hasNext is checked to see if it is at the end of the list
				currentTile = it.next();					
				if (currentTile != prevTile) {
					if (currentTile.getNumber() == prevTile.getNumber()) {  //if the number above the tiles in the column is equal
						currentTile.incNumber(prevTile.getNumber());        //add the number on the previous tile to the current tile
						this.Score += currentTile.getNumber(); 
						removeTile(prevTile);                               //top tile should now be removed
						checkColumn(column);                                //the same operation will continue in the column -> recursive function
					}
					prevTile = currentTile;
				}
			}
		}
	}
    //The method to delete the tiles and to go down the suspended tiles.
	private void removeTile(Tile tile) {
		
		TileList.remove(tile);  //prevTile is deleted from the TileList after the merger has been completed between the two tiles

		List<Tile> list = new ArrayList<Tile>();  //list of tiles whose bottom block has been emptied and which have to go down
		Point position = tile.getPosition();      //position is assigned to the position of the tile
		/*
		 * The position of the tile, which must go down at least 1 line, 
		 * is at least above the other tile, and at most at the top of the grid.
		 */
		for (int i = position.y+1; i < this.Height; i++) { 
			/*
			 * The tile that will go down is in the same x coordinate, 
			 * but the y coordinate will go down as much as "i".
			 */
			Point newPosition = new Point(position.x, i);  	
			for (Tile T : TileList) {
				/*
				 * If the position of the tile in the TileList is equal to the new position determined, 
				 * it means this tile should go down. Then, the tile is added to the list.
				 */
				if (T.getPosition().equals(newPosition)) {
					list.add(T);
				}
			}
		}
        /*
         * If there is no tile in the upper block of the erased tile, the list is empty, 
         * so if the list is not checked whether it is empty, an error is received.
         */
		if (list.isEmpty()) return; 
		int differences = list.get(0).getPosition().y - position.y; //calculated how much the tiles on the list should go down
		for (Tile aTile : list) {
			aTile.move(0, -differences);  //the tiles moved to go down by the differences value
		}
	}
    //The method used for converting score data calculated as integer into a string.
	public String getScoreAsString() {
		return Integer.toString(this.Score);
	}

}