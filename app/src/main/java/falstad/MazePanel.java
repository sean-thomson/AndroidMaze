package falstad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.Color;

//import java.awt.Color;
//import java.awt.Font;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.Image;
//import java.awt.Panel;
//import java.awt.RenderingHints;



import generation.Coordinates;

/**
 * Add functionality for double buffering to an AWT Panel class.
 * Used for drawing a maze.
 * 
 * @author pk
 *
 */
public class MazePanel extends View {
	/* Panel operates a double buffer see
	 * http://www.codeproject.com/Articles/2136/Double-buffer-in-standard-Java-AWT
	 * for details
	 */
	// bufferImage can only be initialized if the container is displayable,
	// uses a delayed initialization and relies on client class to call initBufferImage()
	// before first use

	//private Image bufferImage;
	//private Graphics2D graphics;

	// obtained from bufferImage,
	// graphics is stored to allow clients to draw on same graphics object repeatedly
	// has benefits if color settings should be remembered for subsequent drawing operations

	private Canvas canvas;
	private Bitmap graphicsBitmap;
	private Paint paint;

	/**
	 * Constructor. Object is not focusable.
	 */
	public MazePanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFocusable(false);
		graphicsBitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(graphicsBitmap);
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		//bufferImage = null; // bufferImage initialized separately and later
		//graphics = null;
		
	}

	public MazePanel(Context context)
	{
		super(context);
		setFocusable(true);
		graphicsBitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(graphicsBitmap);
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
	}

	Rect rectangle = new Rect(0, 0, 770, 770);
	@Override
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		canvas.drawBitmap(graphicsBitmap,null, rectangle, paint);
	}

	public void update(String placeHolder/*Graphics g*/) {
		//paint(g);
	}


	/**
	 * Method to draw the buffer image on a graphics object that is
	 * obtained from the superclass. The method is used in the MazeController.
	 * Warning: do not override getGraphics() or drawing might fail. 
	 */

	public void update() {
		invalidate();
		//paint(getGraphics());
	}

	public void setCanvas(Canvas newCanvas)
	{
		this.canvas = newCanvas;
	}


	/**
	 * Draws the buffer image to the given graphics object.
	 * This method is called when this panel should redraw itself.
	 */


	public void paint(/*Graphics g*/) {
		/*
		if (null == g) {
			System.out.println("MazePanel.paint: no graphics object, skipping drawImage operation");
		}
		else {
			//g.drawImage(bufferImage,0,0,null);
		}
		*/
	}


	public void initBufferImage() {
		/*
		bufferImage = createImage(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
		if (null == bufferImage)
		{
			System.out.println("Error: creation of buffered image failed, presumedly container not displayable");
		}
		*/

	}


	/**
	 * Obtains a graphics object that can be used for drawing.
	 * The object internally stores the graphics object and will return the
	 * same graphics object over multiple method calls. 
	 * To make the drawing visible on screen, one needs to trigger 
	 * a call of the paint method, which happens 
	 * when calling the update method. 
	 * @return graphics object to draw on, null if impossible to obtain image
	 */

	public Object getBufferGraphics() {
		/*
		if (null == graphics) {
			// instantiate and store a graphics object for later use
			if (null == bufferImage)
				initBufferImage();
			if (null == bufferImage)
				return null;
			graphics = (Graphics2D) bufferImage.getGraphics();
			if (null == graphics) {
				System.out.println("Error: creation of graphics for buffered image failed, presumedly container not displayable");
			}
			// success case

			//System.out.println("MazePanel: Using Rendering Hint");
			// For drawing in FirstPersonDrawer, setting rendering hint
			// became necessary when lines of polygons
			// that were not horizontal or vertical looked ragged
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		}

		return graphics;
		*/
		return null;
	}
	
	public Object getColor(String color)
	{
		switch (color) {
		case ("white"):
			return Color.WHITE;
		case("gray"):
			return Color.GRAY;
		case("red"):
			return Color.RED;
		case("yellow"):
			return Color.YELLOW;
		case("darkGray"):
			return Color.DKGRAY;
		case ("black"):
			return Color.BLACK;
		case ("blue"):
			return Color.BLUE;
		}
		return null;
	}
	
	public void setMazeColor(String color)
	{
		switch (color) {
			case ("white"):
				paint.setColor(Color.WHITE);
				break;
			case("gray"):
				paint.setColor(Color.GRAY);
				break;
			case("red"):
				paint.setColor(Color.RED);
				break;
			case("yellow"):
				paint.setColor(Color.YELLOW);
				break;
			case("darkGray"):
				paint.setColor(Color.DKGRAY);
				break;
			case ("black"):
				paint.setColor(Color.BLACK);
				break;
			case ("blue"):
				paint.setColor(Color.BLUE);
				break;
		}
	}
	
	public void setMazeColor(Object color)
	{
		//graphics.setColor((Color) color);
	}

	public void drawLine(int x1, int y1, int x2, int y2)
	{
		//graphics.drawLine(x1, y1, x2, y2);
		canvas.drawLine(x1,y1,x2,y2,paint);
	}

	public void fillOval(int x, int y, int width, int height)
	{
		//graphics.fillOval(x, y, width, height);
		RectF oval = new RectF(x, y, x+width, y+height);
		canvas.drawOval(oval, paint);
	}

	public void fillRect(int x, int y, int width, int height)
	{
		//graphics.fillRect(x,y,width,height);
		canvas.drawRect(x, y, x+width, y+height, paint);
	}

	public void setColor(int color)
	{
		paint.setColor(color);
	}

	public int getRGB(/*Color c*/)
	{
		//return c.getRGB();
		return 0;
	}

	public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints)
	{
		//graphics.fillPolygon(xPoints, yPoints, nPoints);
		Path path = new Path();
		path.reset();
		path.moveTo(xPoints[0], yPoints[0]);
		for(int i = 1; i < nPoints; i++)
		{
			path.lineTo(xPoints[i], yPoints[i]);
		}
		path.close();
		canvas.drawPath(path, paint);
	}
	
	public void setFont(/*Font font*/)
	{
		//graphics.setFont(font);
	}
	
	public void setFont(String font)
	{
		switch(font)
		{
		case ("largeBannerFont"):
			//setFont(largeBannerFont);
			break;
		case ("smallBannerFont"):
			//setFont(smallBannerFont);
			break;
		}
	}

	public void centerString(String str, int ypos)
	{
		//graphics.drawString(str, (Constants.VIEW_WIDTH-graphics.getFontMetrics().stringWidth(str))/2, ypos);
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		canvas.drawText(str, (Constants.VIEW_WIDTH/2), ypos, paint);
	}

	public boolean intersect(RangeSet range, Coordinates point)
	{
		return range.intersect(point);
	}

}
