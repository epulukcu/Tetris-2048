import java.util.Random;  // import Random class
import java.awt.event.KeyEvent;
import java.awt.Font;
import java.awt.Point;


public class TetrisBasicsA {

	static public Tetrominos nextTetromino;

	// A method for creating the incoming tetromino to enter the game grid
	public static Tetrominos createIncomingTetromino(int gridHeight, int gridWidth) {
		// shape of the tetromino is determined randomly
		char[] tetrominoNames = {'I', 'S', 'Z', 'O', 'T', 'L', 'J'};
		Random random = new Random();
		int randomIndex = random.nextInt(7);		
		char randomName = tetrominoNames[randomIndex];
		// create and return the tetromino
		Tetrominos tet = new Tetrominos(randomName, gridHeight, gridWidth);
		return tet;
	}

	public static void drawNextTetromino(Tetrominos tet) {
		for (Tile tile: tet.getTileList()) {
			Point p = tile.getPosition();
			p.translate(8, -20); 

			StdDraw.setPenColor(tile.getBackgroundColor());
			StdDraw.filledSquare(p.x, p.y, 0.5);

			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.text(p.x, p.y, Integer.toString(tile.getNumber()));
		}
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.text(14, 4 , "NEXT");
		StdDraw.text(14, 18 , "SCORE");
	}

	public static void main(String[] args) {
		int gridWidth = 12, gridHeight = 20;
		// set the size of the drawing canvas
		StdDraw.setCanvasSize(660, 800);
		// set the scale of the coordinate system
		StdDraw.setXscale(-1.5, gridWidth + 4.5);
		StdDraw.setYscale(-1.5, gridHeight - 0.5);
		// double buffering is used for speeding up drawing needed to enable computer animations
		// check https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdDraw.html for details
		StdDraw.enableDoubleBuffering();

		// create a grid as the tetris game environment
		GridA gameGrid = new GridA(gridHeight, gridWidth);
		// create the first tetromino to enter the game grid
		Tetrominos tet = createIncomingTetromino(gridHeight, gridWidth);
		Tetrominos nextTetromino = createIncomingTetromino(gridHeight, gridWidth);
		boolean createANewTetromino = false; 

		// main animation loop
		while (true)  {
			// keyboard interaction for moving the active tetromino left or right
			boolean success = false;
			if(StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
				success = tet.goLeft(gameGrid);
			}
			else if(StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
				success = tet.goRight(gameGrid);
			}
			else if(StdDraw.isKeyPressed(KeyEvent.VK_UP)) {
				success = tet.Rotate(gameGrid);
			}

			// move the active tetromino down by one if a successful move left/right is not performed
			if (!success)
				success = tet.goDown(gameGrid);
			if(!success && tet.GameOver) {
				String gameOverMsg = "Game Over!";
				StdDraw.setPenColor(StdDraw.BLACK);
				Font gameOverFont = new Font("Arial", Font.BOLD, 50);
				StdDraw.setFont(gameOverFont);
				StdDraw.clear(StdDraw.GRAY);
				StdDraw.text((gridWidth + 3)/2, gridHeight/2, gameOverMsg);
				StdDraw.show();
				break;


				// place (stop) the active tetromino on the game grid if it cannot go down anymore
			}
			// place (stop) the active tetromino on the game grid if it cannot go down anymore
			createANewTetromino = !success;
			if (createANewTetromino) {
				// update the game grid by adding the placed tetromino
				gameGrid.updateGrid(tet);
				gameGrid.do2048(tet);
				gameGrid.checkFullLines();

				// create the next tetromino to enter the game grid
				tet = nextTetromino;
				nextTetromino = createIncomingTetromino(gridHeight, gridWidth);
			}

			// clear the background (double buffering)
			StdDraw.clear(StdDraw.GRAY);

			// draw the next tetromino
			drawNextTetromino(nextTetromino);

			// draw the game grid
			gameGrid.display();

			// draw the active tetromino
			tet.display();

			// show the score
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.text(14, 16, gameGrid.getScoreAsString());

			// copy offscreen buffer to onscreen (double buffering)
			StdDraw.show();

			// pause for 150 ms (double buffering)
			StdDraw.pause(150);
		}
	}
}