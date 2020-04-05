import java.util.Random;  // import Random class


import java.awt.Color; // import Color class
import java.awt.Point; // import Point class
import java.awt.event.KeyEvent; 

public class TetrisBasicsA {
	
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

	public static void main(String[] args) {
		int gridWidth = 12, gridHeight = 20;
		// set the size of the drawing canvas
		StdDraw.setCanvasSize(560, 800);
		// set the scale of the coordinate system
		StdDraw.setXscale(-1.5, gridWidth + 0.5);
		StdDraw.setYscale(-1.5, gridHeight - 0.5);
		// double buffering is used for speeding up drawing needed to enable computer animations
		// check https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdDraw.html for details
		StdDraw.enableDoubleBuffering();

		// create a grid as the tetris game environment
		GridA gameGrid = new GridA(gridHeight, gridWidth);
		// create the first tetromino to enter the game grid
		Tetrominos tet = createIncomingTetromino(gridHeight, gridWidth); 
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
			// place (stop) the active tetromino on the game grid if it cannot go down anymore
			createANewTetromino = !success;
			if (createANewTetromino) {
				// update the game grid by adding the placed tetromino
				gameGrid.updateGrid(tet);
				// create the next tetromino to enter the game grid
				tet = createIncomingTetromino(gridHeight, gridWidth);
			}

			// clear the background (double buffering)
			StdDraw.clear(StdDraw.GRAY);
			// draw the game grid
			gameGrid.display();
			// draw the active tetromino
			tet.display();
			// copy offscreen buffer to onscreen (double buffering)
			StdDraw.show();
			// pause for 200 ms (double buffering)
			StdDraw.pause(200);
		}
	}
}