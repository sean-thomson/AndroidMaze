package falstad;

import android.os.Handler;

import falstad.Robot.Direction;
import falstad.Robot.Turn;
import generation.Distance;
import sthomson.cs301.cs.wm.edu.amazebyseanthomson.ui.PlayActivity;

/**
 * @author Sean Thomson
 * 
 * Responsibilities:
 * 	Move around maze using a wall following algorithm
 * 	Provide inputs to Robot class to move around the maze
 * 
 * Collaborators:
 * 	RobotDriver
 * 	Robot
 *
 */
public class WallFollower implements RobotDriver {
	
	private Robot robot;
	private Distance distanceValue;
	
	private int mazeWidth;
	private int mazeHeight;

	private PlayActivity playActivity;

	Handler handler = new Handler();

	@Override
	public void setRobot(Robot r) {
		robot = r;
	}

	@Override
	public void setDimensions(int width, int height) {
		mazeWidth = width;
		mazeHeight = height;
	}

	public void setActivity(PlayActivity play)
	{
		playActivity = play;
	}

	@Override
	public void setDistance(Distance distance) {
		distanceValue = distance;
	}

	/* (non-Javadoc)
	 * @see falstad.RobotDriver#drive2Exit()
	 * 
	 * Drives to the exit of a maze by using a wall following algorithm
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		/*
		while (!robot.isAtExit())
		{
			if (robot.distanceToObstacle(Direction.LEFT) == 0 && robot.distanceToObstacle(Direction.FORWARD) != 0) {
				move(1);
			}
			else if (robot.distanceToObstacle(Direction.LEFT) != 0) {
				rotate(Turn.LEFT);
				move(1);
			} else {
				rotate(Turn.RIGHT);
				move(1);
			}
			if (robot.hasStopped())
				throw new Exception();
		}
		if (robot.canSeeExit(Direction.FORWARD))
		{
			move(1);
		}
		else if (robot.canSeeExit(Direction.LEFT))
		{
			rotate(Turn.LEFT);
			move(1);
		}
		else
		{
			rotate(Turn.RIGHT);
			move(1);
		}
		return true;
		*/

		//while (!robot.isAtExit())
		if (robot.distanceToObstacle(Direction.LEFT) != 0)
		{
			robot.rotate(Turn.LEFT);
			robot.move(1,false);
		}
		else if (robot.distanceToObstacle(Direction.LEFT) == 0 && robot.distanceToObstacle(Direction.FORWARD) != 0)
		{
			robot.move(1, false);
		}
		else
		{
			robot.rotate(Turn.RIGHT);
			robot.move(1, false);
		}
		if (robot.hasStopped())
			throw new Exception();

		if (robot.isAtExit()) {

			if (robot.canSeeExit(Direction.FORWARD))
			{
				robot.move(1, false);
				return true;
			}
			else if (robot.canSeeExit(Direction.LEFT))
			{
				robot.rotate(Turn.LEFT);
				robot.move(1, false);
				return true;
			}
			else
			{
				robot.rotate(Turn.RIGHT);
				robot.move(1, false);
				return true;
			}
		}
		return true;

	}

	@Override
	public float getEnergyConsumption() {
		return 3000 - robot.getBatteryLevel();
	}

	@Override
	public int getPathLength() {
		return robot.getOdometerReading();
	}

}
