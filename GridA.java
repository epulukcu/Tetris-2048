import java.awt.Color; // import Color class
import java.awt.Point; // import Point class
import java.util.List;

import javax.xml.ws.handler.PortInfo;

import java.util.ArrayList;


// A class representing the tetris game grid
public class GridA {
	// Private data fields
	private List<Tile> TileList = new ArrayList<Tile>();
	private int Height;
	private int Width;

	// Constructor
	GridA (int n_rows, int n_cols) {
		this.Height = n_rows;
		this.Width = n_cols;
	}
	
	// Method used for checking whether the square with given indices is occupied or empty
	public boolean isOccupied(Point tilePos) {
		Boolean rsp = false;
		for (Tile tile : TileList) {
			Point p = tile.getPosition();
			if ((p.x == tilePos.x) && (p.y == tilePos.y)) {
				rsp = true;
				break;
			}
		}
		return rsp;
	}
	// Method for updating the game grid with a placed (stopped) tetromino
	public void updateGrid(Tetrominos tet) {
		for (Tile tile : tet.getTileList()) {
			TileList.add(tile);
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
	
}