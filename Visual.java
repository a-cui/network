import java.util.Arrays;
import java.awt.*;

public class Visual {
	private final int WIDTH, HEIGHT, OFFSET;
	private double[] distances;

	public Visual() {
		WIDTH = 600;
		HEIGHT = 400;
		OFFSET = 10;
	}

	public void start() {
		StdDraw.setCanvasSize(WIDTH, HEIGHT);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.text(WIDTH/2, HEIGHT/2, "A visual will be shown here!");
	}

	public void display(double[] distances) {
		this.distances = distances;

		//drawing the axes
        StdDraw.line(OFFSET, 2*OFFSET, WIDTH, 2*OFFSET);
        StdDraw.line(OFFSET, 2*OFFSET, OFFSET, HEIGHT*.90);

        String[] langs = {"English", "Italian", "Spanish", "Dutch", 
        							"French", "German", "Norwegian"};

        //finding a "scale factor" such that the closest language has the 
     	//highest bar and fills up the axis, while the farthest language
        //gets the lowest bar
        double smallest = distances[LanguageDetector.getSmallest(distances)];
        double scaleMax = (1/smallest) * (1/smallest);
        double scaleFactor = (HEIGHT*.90 - 2*OFFSET)/scaleMax;

        //draws the bars one at a time by selection sort
        for(int i = 1; i <= distances.length; i++) {
        	int index = LanguageDetector.getSmallest(distances);
        	double min = distances[index];
        	distances[index] = 10000; 
        	double size = (1/min)*(1/min);
        	//this allows the first bar to be dark blue with an even gradient
        	//to light blue
        	StdDraw.setPenColor(17 + 27*i, 80 + 21*i, 187 + 9*i);
        	StdDraw.filledRectangle(2*OFFSET*i + 60*i, 
        		2*OFFSET + scaleFactor*size/2.0, 30, scaleFactor*size/2.0);
        	StdDraw.setPenColor(Color.BLACK);
        	StdDraw.text(2*OFFSET*i + 60*i, OFFSET/2, langs[index]);
        }
        Font message = new Font("Arial", Font.ITALIC, 12);
        StdDraw.setFont(message);
        StdDraw.text(WIDTH - 100, HEIGHT - OFFSET,
        	 "Values used are (1/distance) squared.");
	}

	public void clear() {
		StdDraw.clear();
	}

}