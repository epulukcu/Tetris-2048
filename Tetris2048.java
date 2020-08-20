import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.awt.event.KeyEvent;
import java.awt.Font;
import java.awt.Point;

//The Tetris2048 class for representing game
public class Tetris2048 {

	//static public Tetrominoes nextTetromino;
	
	//The method for creating the incoming tetromino to enter the game grid
	public static Tetrominoes createIncomingTetromino(int gridHeight, int gridWidth) {
		//shape of the tetromino is determined randomly
		char[] tetrominoNames = {'I', 'S', 'Z', 'O', 'T', 'L', 'J'};
		Random random = new Random();
		int randomIndex = random.nextInt(7);		
		char randomName = tetrominoNames[randomIndex];
		// create and return the tetromino
		Tetrominoes tet = new Tetrominoes(randomName, gridHeight, gridWidth);
		return tet;
	}
    //The method of displaying the next tetromino on the game screen when a new tetromino arrives
	public static void drawNextTetromino(Tetrominoes tet) {
		/*
		 * The position of the tiles in the incoming tetromino is updated 
		 * to the desired area to appear on the game screen.
		 */
		for (Tile tile: tet.getTileList()) {
			Point p = tile.getPosition();
			p.translate(8, -20); 
            //StdDraw commands required to draw tetromino to the desired place
			StdDraw.setPenColor(tile.getBackgroundColor());
			StdDraw.filledSquare(p.x, p.y, 0.5);
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.text(p.x, p.y, Integer.toString(tile.getNumber())); //to show the number printed on each tile
		}
		//StdDraw commands required to write "NEXT" and "SCORE" to the desired place
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.text(14, 4 , "NEXT");
		StdDraw.text(14, 18 , "SCORE");
	}

	public static void main(String[] args) throws InterruptedException {
		
		int gridWidth = 12, gridHeight = 20;       //set the height and width of grid
		StdDraw.setCanvasSize(660, 800);  		   // set the size of the drawing canvas
		StdDraw.setXscale(-1.5, gridWidth + 4.5);  // set the x scale of the coordinate system
		StdDraw.setYscale(-1.5, gridHeight - 0.5); // set the y scale of the coordinate system
		// double buffering is used for speeding up drawing needed to enable computer animations
		StdDraw.enableDoubleBuffering();
		Grid gameGrid = new Grid(gridHeight, gridWidth);  // create a grid as the tetris game environment
		// create the first tetromino to enter the game grid
		Tetrominoes tet = createIncomingTetromino(gridHeight, gridWidth);
		// create the next tetromino to enter the game grid
		Tetrominoes nextTetromino = createIncomingTetromino(gridHeight, gridWidth);
		boolean createANewTetromino = false; 
		
		// main animation loop	
		label1:   
			while (true)  {
				//control of the pause status according to mouse click on the screen
				if (StdDraw.isMousePressed()) {
					if (Grid.getAction() == true)
					{Grid.setAction(false);}
					else {
						Grid.setAction(true);
						TimeUnit.SECONDS.sleep(1);  //stop time controlled with TimeUnit
					}
				}
				//label 1 is repeated as the button is activated
				if (Grid.getAction() == true){
					continue label1;
				}
				//keyboard interaction for moving the active tetromino left, right or Rotate
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
	
				//move the active tetromino down by one if a successful move left/right is not performed
				if (!success)
					success = tet.goDown(gameGrid);
				    //displaying the "Game Over" on the screen
				    if(!success && tet.GameOver) {
				    	String gameOverMsg = "Game Over!";
				    	StdDraw.setPenColor(StdDraw.BLACK);
				    	Font gameOverFont = new Font("Arial", Font.BOLD, 50);
				    	StdDraw.setFont(gameOverFont);
				    	StdDraw.clear(StdDraw.GRAY);
				        StdDraw.text((gridWidth + 3)/2, gridHeight/2, gameOverMsg);
				        StdDraw.show();
				        break;
				    }
				    
				createANewTetromino = !success;  //place (stop) the active tetromino on the game grid if it cannot go down anymore
				if (createANewTetromino) {
					gameGrid.updateGrid(tet);    //update the game grid by adding the placed tetromino
					gameGrid.do2048(tet);        //check the do2048 by adding the placed tetromino
					gameGrid.checkFullLines();   //check the lines  by adding the placed tetromino
					tet = nextTetromino;         //create the next tetromino to enter the game grid
					nextTetromino = createIncomingTetromino(gridHeight, gridWidth);
				}
	
			
				StdDraw.clear(StdDraw.GRAY);        //clear the background (double buffering)
				drawNextTetromino(nextTetromino);   //draw the next tetromino			
				gameGrid.display();                 //draw the game grid
				tet.display();                      //draw the active tetromino
				StdDraw.setPenColor(StdDraw.RED);   //show the score
				StdDraw.text(14, 16, gameGrid.getScoreAsString());
				StdDraw.show();  // copy offscreen buffer to onscreen (double buffering)
				StdDraw.pause(150);  // pause for 150 ms (double buffering)
			} 
		}
}
