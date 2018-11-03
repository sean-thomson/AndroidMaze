package falstad;

import falstad.Constants.StateGUI;

/**
 * Implements the screens that are displayed whenever the game is not in 
 * the playing state. The screens shown are the title screen, 
 * the generating screen with the progress bar during maze generation,
 * and the final screen when the game finishes.
 * @author pk
 *
 */
public class MazeView extends DefaultViewer {

	// need to know the maze model to check the state 
	// and to provide progress information in the generating state
	private MazeController controller ;
	
	private MazePanel panel;
	
	public MazeView(MazeController c) {
		super() ;
		controller = c ;
		panel = controller.getPanel();
	}

	@Override
	public void redraw(MazePanel panel, int px, int py, int view_dx, int view_dy,
			int walk_step, int view_offset, RangeSet rset, int ang) {
		//dbg("redraw") ;
		/*
		switch (state) {
		case STATE_TITLE:
			redrawTitle();
			break;
		case STATE_GENERATING:
			redrawGenerating();
			break;
		case STATE_PLAY:
			// skip this one
			break;
		case STATE_FINISH:
			redrawFinish();
			break;
		}
		*/
	}
	
	private void dbg(String str) {
		System.out.println("MazeView:" + str);
	}
	
	// 
	
	/**
	 * Helper method for redraw to draw the title screen, screen is hard coded
	 * @param  gc graphics is the off screen image
	 */
	void redrawTitle() {
		// produce white background
		panel.setMazeColor("white");
		panel.fillRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
		// write the title 
		panel.setFont("largeBannerFont");
		//FontMetrics fm = gc.getFontMetrics();
		panel.setMazeColor("red");
		centerString("MAZE", 100);
		// write the reference to falstad
		panel.setMazeColor("blue");
		panel.setFont("smallBannerFont");
		//fm = gc.getFontMetrics();
		centerString("by Paul Falstad", 160);
		centerString("www.falstad.com", 190);
		
		// write the instructions
		panel.setMazeColor("black");
		centerString("To start, select a robot type from the left dropdown.", 250);
		centerString("Then, select a maze generation algorithm", 280);
		centerString("from the center dropdown.", 295);
		centerString("Then, select a difficulty level from the right dropdown.", 320);
		centerString("Version 2.0", 350);
	}
	/**
	 * Helper method for redraw to draw final screen, screen is hard coded
	 * @param gc graphics is the off screen image
	 */
	void redrawFinish() {
		// produce blue background
		panel.setMazeColor("blue");
		panel.fillRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
		// write the title 
		panel.setFont("largeBannerFont");
		//FontMetrics fm = gc.getFontMetrics();
		panel.setMazeColor("yellow");
		centerString("You won!", 100);
		// write some extra blufb
		panel.setMazeColor("orange");
		panel.setFont("smallBannerFont");
		//fm = gc.getFontMetrics();
		centerString("Congratulations!", 160);
		// write the instructions
		if (controller.robot != null)
		{
			centerString("Path length: " + controller.robot.getOdometerReading(), 200);
			centerString("Energy left: " + controller.robot.getBatteryLevel(), 220);
		}
		panel.setMazeColor("white");
		centerString("Hit any key to restart", 300);
	}

	/**
	 * Helper method for redraw to draw screen during phase of maze generation, screen is hard coded
	 * only attribute percentdone is dynamic
	 * @param gc graphics is the off screen image
	 */
	void redrawGenerating() {
		// produce yellow background
		panel.setMazeColor("yellow");
		panel.fillRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
		// write the title 
		panel.setFont("largeBannerFont");
		//FontMetrics fm = gc.getFontMetrics();
		panel.setMazeColor("red");
		centerString("Building maze", 150);
		panel.setFont("smallBannerFont");
		//fm = gc.getFontMetrics();
		// show progress
		panel.setMazeColor("black");
		if (null != controller)
			centerString(controller.getPercentDone()+"% completed", 200);
		else
			centerString("Error: no controller, no progress", 200);
		// write the instructions
		centerString("Hit escape to stop", 300);
	}
	
	private void centerString(String str, int ypos) {
		panel.centerString(str, ypos);
	}

}
