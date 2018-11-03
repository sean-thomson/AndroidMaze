package falstad;

import generation.Distance;
import falstad.Robot.Direction;
import generation.CardinalDirection;
import falstad.Robot.Turn;
import sthomson.cs301.cs.wm.edu.amazebyseanthomson.ui.PlayActivity;

import java.util.Random;

/**
 * @author Sean Thomson
 * 
 *Responsibilities:
 *	To traverse a maze using Pledge's algorithm
 *	To provide input to robot to move around maze
 *
 *Collaborators:
 *	RobotDriver
 *	Robot
 *
 *
 */
public class Pledge implements RobotDriver {

	private Robot robot;
	
	private Distance distanceValue;
	
	private int mazeWidth;
	private int mazeHeight;

	private PlayActivity playActivity;
	
	private CardinalDirection[] directions = CardinalDirection.values();
	private Random random = new Random();
	
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
	
	public CardinalDirection randomDirection()
	{
		return directions[random.nextInt(directions.length)];
	}

	/* (non-Javadoc)
	 * @see falstad.RobotDriver#drive2Exit()
	 * 
	 * Drives to the exit of a maze using Pledge's algorithm to traverse around obstacles
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		CardinalDirection mainDirection = randomDirection();
		int counter = 0;
		
		while (mainDirection != robot.getCurrentDirection())
		{
			robot.rotate(Turn.RIGHT);
		}
		
		//while (!robot.isAtExit())
		{
			if (counter == 0 && robot.distanceToObstacle(Direction.FORWARD) != 0)
			{
				while (mainDirection != robot.getCurrentDirection())
				{
					robot.rotate(Turn.RIGHT);
				}
				robot.move(1, false);
			}
			else
			{
				if (robot.distanceToObstacle(Direction.LEFT) != 0)
				{
					robot.rotate(Turn.LEFT);
					counter--;
					robot.move(1, false);
				}
				else if (robot.distanceToObstacle(Direction.FORWARD) != 0)
				{
					robot.move(1, false);
				}
				else
				{
					robot.rotate(Turn.RIGHT);
					counter++;
					robot.move(1, false);
				}
			}
			
			if (robot.hasStopped())
				throw new Exception();
		}
		
		if (robot.canSeeExit(Direction.FORWARD))
		{
			robot.move(1, false);
		}
		else if (robot.canSeeExit(Direction.LEFT))
		{
			robot.rotate(Turn.LEFT);
			robot.move(1, false);
		}
		else
		{
			robot.rotate(Turn.RIGHT);
			robot.move(1, false);
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
