import java.awt.Point; // import Point class
import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.Collections;


// A class representing the tetris game grid
public class GridA {
	// Private data fields
	private int Height;
	private int Width;
	private List<Tile> TileList = new ArrayList<Tile>();
	private int Score;

	// Constructor
	GridA (int n_rows, int n_cols) {
		this.Height = n_rows;
		this.Width = n_cols;
		this.Score = 0;
	}

	// Method used for checking whether the square with given indices is occupied or empty
	public boolean isOccupied(Point tilePos) {
		Boolean response = false;
		for (Tile tile : TileList) {
			if (tile.getPosition().equals(tilePos)) {
				response = true;
				break;
			}
		}
		return response;
	}

	// Method for updating the game grid with a placed (stopped) tetromino
	public void updateGrid(Tetrominos tet) {

		TileList.addAll(tet.getTileList());

		List<Tile> list = new ArrayList<Tile>();
		list = tet.getTileList();
		Collections.sort(list, Collections.reverseOrder());
		for (Tile tile : list) {
			Point position = tile.getPosition();
			int i;
			for (i = position.y-1; i >= 0; i--) {
				if (isOccupied(new Point(position.x, i))) {
					break;
				}
			}
			if (i+1 != position.y) {
				Point newPosition = new Point(position.x, i+1);
				TileList.get( TileList.indexOf(tile) ).setPosition(newPosition);

			}
		}
	}
	// Method used for displaying the grid
	public void display() {
		for (int row = 0; row < this.Width; row++)
			for (int col = 0; col < this.Height; col++) 
			{
				StdDraw.setPenColor(StdDraw.BLACK);
				StdDraw.filledSquare(row, col, 0.5);
			}

		// drawing the grid
		StdDraw.setPenColor(StdDraw.BOOK_BLUE);
		for (double x = -0.5; x < this.Width; x++) // vertical lines
			StdDraw.line(x, -0.5, x, this.Height - 0.5);
		for (double y = -0.5; y < this.Height; y++) // horizontal lines
			StdDraw.line(-0.5, y, this.Width - 0.5, y);

		for (Tile tile : TileList)
		{
			tile.display();
		}
	}

	public void checkFullLines() {
		int[] tileCounts = new int[this.Height];  //declare the array with same position.y tiles
		for (int i = 0; i < this.Height; i++) {   //initially array is empty
			tileCounts[i] = 0;
		}

		List<Integer> lineList = new ArrayList<Integer>(); //keep the numbers of the rows to be deleted in the list
		for (Tile tile : TileList) {			
			Point position = tile.getPosition();
			tileCounts[position.y]++;                   //add the position.y of the tiles to tileCounts
			if (tileCounts[position.y] == this.Width) { //the number of tiles with the same position.y is equal to the number of columns
				lineList.add(position.y);               //the number of the line to be deleted is added to the list when the condition is met
			}
		}

		if (lineList.size() > 0) {
			List<Tile> removeList = new ArrayList<Tile>();           //list of tiles to be deleted			
			for (Tile tile : TileList) {
				if (lineList.indexOf(tile.getPosition().y) > -1) {  //if the tile has a position.y in the lineList
					removeList.add(tile);                           //tile is added to removeList list to be deleted when condition is met
					this.Score += tile.getNumber();                 //numbers on deleted tiles are added to Score
				}
			}

			TileList.removeAll(removeList);     //delete all tiles in removeList

			//Collections.sort method is sorting the elements of lineList in descending order
			Collections.sort(lineList, Collections.reverseOrder());

			//every tile on the top line after the deleted line goes down
			for (int line_no : lineList) {
				for (Tile tile : TileList) {
					if (tile.getPosition().y > line_no) {
						tile.move(0, -1);
					}
				}
			}

		}

	}

	public void do2048(Tetrominos tet) {
		List<Integer> x_list = new ArrayList<Integer>(); //list for the position.x of the tiles in tetromino
		for (Tile tile : tet.getTileList())
		{
			Point position = tile.getPosition();
			if (!x_list.contains(position.x))    //there will be one from each x 
				x_list.add(position.x);
		}

		for (int X : x_list) {           //in order to process columns
			checkColumn(X);
		}
	}
	//The function to process for each column
	private void checkColumn(int column) {
		List<Tile> list = new ArrayList<Tile>(); 

		for (Tile tile : TileList) {			
			Point position = tile.getPosition();
			if (column == position.x)
				list.add(tile);  //add to this list which tiles have position.x in x_list
			//so all the tiles in column x occupied by the tetromino are in the list
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

	private void removeTile(Tile tile) {
		TileList.remove(tile);

		List<Tile> list = new ArrayList<Tile>(); //aþaðýya indirileceklerin listesi
		Point position = tile.getPosition();
		for (int i = position.y+1; i < this.Height; i++) {
			Point newPosition = new Point(position.x, i);				
			for (Tile T : TileList) {
				if (T.getPosition().equals(newPosition)) {
					list.add(T);
				}
			}
		}

		if (list.isEmpty()) return;

		int differences = list.get(0).getPosition().y - position.y; //listedekileri ne kadar aþaðýya indireceðiz
		for (Tile aTile : list) {
			aTile.move(0, -differences);
		}
	}

	public String getScoreAsString() {
		return Integer.toString(this.Score);
	}

}