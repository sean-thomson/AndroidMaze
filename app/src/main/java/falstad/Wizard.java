package falstad;

import falstad.Robot.Direction;
import falstad.Robot.Turn;
import generation.CardinalDirection;
import generation.Distance;
import sthomson.cs301.cs.wm.edu.amazebyseanthomson.ui.PlayActivity;

/**
 * @author Sean Thomson
 * 
 * Responsibilities:
 * 	To navigate the maze in the most efficient way possible
 * 	Feed commands to robot to navigate said maze
 * 
 * Collaborators:
 * 	RobotDriver
 * 	Robot
 *
 */
public class Wizard implements RobotDriver {

	private Robot robot;
	private Distance distanceValue;

	private PlayActivity playActivity;
	
	private int mazeWidth;
	private int mazeHeight;
	
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
	 * Uses the robot to drive to the exit using the most efficient path possible
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		//while (!robot.isAtExit())
		{
			int[] pos = robot.getCurrentPosition();
			int[] closerPos = ((BasicRobot) robot).getMaze().getMazeConfiguration().getNeighborCloserToExit(pos[0], pos[1]);

			int newX = closerPos[0] - pos[0];
			int newY = closerPos[1] - pos[1];

			CardinalDirection currentDirection = robot.getCurrentDirection();

			// if this is true, move east
			if (newX > 0) {
				currentDirection = robot.getCurrentDirection();
				if (!currentDirection.equals(CardinalDirection.East)) {
					robot.rotate(chooseTurn(CardinalDirection.East));
					currentDirection = robot.getCurrentDirection();
				}

				if (currentDirection.equals(CardinalDirection.East)) {
					robot.move(1, false);
				}
			}

			// if this is true, move west
			if (newX < 0) {
				currentDirection = robot.getCurrentDirection();
				if (!currentDirection.equals(CardinalDirection.West)) {
					robot.rotate(chooseTurn(CardinalDirection.West));
					currentDirection = robot.getCurrentDirection();
				}

				if (currentDirection.equals(CardinalDirection.West)) {
					robot.move(1, false);
				}
			}

			// if this is true, move north
			if (newY < 0) {
				currentDirection = robot.getCurrentDirection();
				if (!currentDirection.equals(CardinalDirection.North)) {
					robot.rotate(chooseTurn(CardinalDirection.North));
					currentDirection = robot.getCurrentDirection();
				}

				if (currentDirection.equals(CardinalDirection.North)) {
					robot.move(1, false);
				}
			}

			// if this is true, move south
			if (newY > 0) {
				currentDirection = robot.getCurrentDirection();
				if (!currentDirection.equals(CardinalDirection.South)) {
					robot.rotate(chooseTurn(CardinalDirection.South));
					currentDirection = robot.getCurrentDirection();
				}

				if (currentDirection.equals(CardinalDirection.South)) {
					robot.move(1, false);
				}
			}

			if (robot.isAtExit())
			{
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
	}

	private Turn chooseTurn(CardinalDirection dir)
	{
		CardinalDirection currentDirection = robot.getCurrentDirection();

		Turn desiredTurn = null;

		if (currentDirection.equals(CardinalDirection.North))
		{
			if (dir.equals(CardinalDirection.West))
			{
				desiredTurn = Turn.RIGHT;
			}

			if (dir.equals(CardinalDirection.East))
			{
				desiredTurn = Turn.LEFT;
			}

			if (dir.equals(CardinalDirection.South))
			{
				desiredTurn = Turn.AROUND;
			}
		}

		if (currentDirection.equals(CardinalDirection.South))
		{
			if (dir.equals(CardinalDirection.East))
			{
				desiredTurn = Turn.RIGHT;
			}

			if (dir.equals(CardinalDirection.West))
			{
				desiredTurn = Turn.LEFT;
			}

			if (dir.equals(CardinalDirection.North))
			{
				desiredTurn = Turn.AROUND;
			}

		}

		if (currentDirection.equals(CardinalDirection.East))
		{
			if (dir.equals(CardinalDirection.West))
			{
				desiredTurn = Turn.AROUND;
			}

			if (dir.equals(CardinalDirection.South))
			{
				desiredTurn = Turn.LEFT;
			}

			if (dir.equals(CardinalDirection.North))
			{
				desiredTurn = Turn.RIGHT;
			}
		}

		if (currentDirection.equals(CardinalDirection.West))
		{
			if (dir.equals(CardinalDirection.East))
			{
				desiredTurn = Turn.AROUND;
			}

			if (dir.equals(CardinalDirection.South))
			{
				desiredTurn = Turn.RIGHT;
			}

			if (dir.equals(CardinalDirection.North))
			{
				desiredTurn = Turn.LEFT;
			}

		}

		return desiredTurn;
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
