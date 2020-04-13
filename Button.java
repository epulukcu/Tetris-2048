//The class designed for button control on the game screen
public class Button {

    // constant
    private static final double margin = 0.05;   
    // instance variables for button properties
    private Button child;
    private final double c_x;
    private final double c_y;
    private final double h_w;
    private final double h_h;
    public final String name;

    //create a new button
    public Button(double c_x, double c_y, double h_w, double h_h, String name) {
	this.c_x = c_x;
	this.c_y = c_y;
	this.h_w = h_w;
	this.h_h = h_h;
	this.name = name;
    }

    //draw the button with StdDraw
    public void draw() {
	    StdDraw.rectangle(this.c_x, this.c_y, this.h_w, this.h_h);
	    StdDraw.text(this.c_x, this.c_y, this.name);
    }
}
