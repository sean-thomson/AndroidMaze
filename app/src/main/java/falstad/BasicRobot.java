package falstad;

import falstad.Constants.StateGUI;
import generation.CardinalDirection;

/**
 * @author Sean Thomson
 * Project 3
 *
 *Responsibilities:
 *	Moves and manipulates a users position on a maze
 *	Gives the user or maze traversion algorithm information regarding how the maze is laid out and its attributes
 *	Keeps track of your position and direction on a maze
 *
 *Collaborators:
 *	Driver class that can employ some algorithm
 *	MazeController class that is operated on top of to manipulate the maze
 *	Robot class that provides an interface and basic functionality
 *
 */
public class BasicRobot implements Robot {
	
	public MazeController basicController;
	int odometer;
	float battery;
	
	boolean stopped;
	
	boolean roomSensor;
	boolean distanceSensorBackward;
	boolean distanceSensorForward;
	boolean distanceSensorLeft;
	boolean distanceSensorRight;
	
	// constants for battery usage
	static final int MOVE_STEP = 5;
	static final int ROTATION = 3;
	static final int DISTANCE_SENSING = 1;
	
	public BasicRobot(MazeController controller)
	{
		battery = 3000;
		odometer = 0;
		stopped = false;
		basicController = controller;
	}
	
	public BasicRobot(MazeController controller, boolean sensorForward, boolean roomSensor, boolean sensorBackward, boolean sensorLeft, boolean sensorRight)
	{
		battery = 3000;
		odometer = 0;
		stopped = false;
		basicController = controller;
		distanceSensorBackward = sensorBackward;
		distanceSensorForward = sensorForward;
		distanceSensorLeft = sensorLeft;
		distanceSensorRight = sensorRight;
	}

	
	/* 
	 * @see falstad.Robot#rotate(falstad.Robot.Turn)
	 * 
	 * Rotates your position on the maze by manipulating the MazeController class using its own rotate method
	 * Accounts for battery usage
	 */
	@Override
	public void rotate(Turn turn) {
		if (turn == Turn.LEFT && hasStopped() == false)
		{
			basicController.rotate(1);
			battery = battery - 3;
			if (battery <= 0)
			{
				stopped = true;
			}
		}
		if (turn == Turn.RIGHT && hasStopped() == false)
		{
			basicController.rotate(-1);
			battery = battery - 3;
			if (battery <= 0)
			{
				stopped = true;
			}
		}
		if (turn == Turn.AROUND && hasStopped() == false)
		{
			basicController.rotate(-1);
			battery = battery - 3;
			if (battery <= 0)
			{
				stopped = true;
			}
			if (hasStopped() == false)
			{
				basicController.rotate(-1);
				battery = battery - 3;
			}
			if (battery <= 0)
			{
				stopped = true;
			}
		}
	}

	/* 
	 * @see falstad.Robot#move(int, boolean)
	 * 
	 * Moves your position around the maze for a given distance
	 * Accounts for battery usage and updates the odometer
	 */
	@Override
	public void move(int distance, boolean manual) {
		if (distance > 0)
		{
			for (int i = 0; i < distance; i++)
			{
				if (hasStopped() == false)
				{
					basicController.walk(1);
					odometer = odometer + 1;
					battery = battery - MOVE_STEP;
					if (battery <= 0)
					{
						stopped = true;
					}
					int[] pos = basicController.getCurrentPosition();
					if (basicController.isOutside(pos[0], pos[1]))
					{
						//basicController.setState(StateGUI.STATE_PLAY);
						//basicController.switchToFinishScreen();
					}
				}
			}
		}
		if (distance < 0)
		{
			for (int i = 0; i > distance; i--)
			{
				if (hasStopped() == false)
				{
					basicController.walk(-1);
					odometer = odometer + 1;
					battery = battery - MOVE_STEP;
					if (battery <= 0)
					{
						stopped = true;
					}
					int[] pos = basicController.getCurrentPosition();
					if (basicController.isOutside(pos[0], pos[1]))
					{
						//basicController.setState(StateGUI.STATE_PLAY);
						//basicController.switchToFinishScreen();
					}
				}
			}
		}

	}

	/* 
	 * @see falstad.Robot#getCurrentPosition()
	 * 
	 * Returns the current position of the maze in an int array
	 * Throws an exception if the current position is outside of the maze
	 */
	@Override
	public int[] getCurrentPosition() throws Exception {
		int[] pos = basicController.getCurrentPosition();
		if (basicController.isOutside(pos[0], pos[1]))
			throw new Exception();
		return basicController.getCurrentPosition();
	}
	
	/*
	 * @see falstad.Robot#setMaze(falstad.MazeController)
	 * 
	 * Setter method to set what MazeController the robot will manipulate
	 */
	@Override
	public void setMaze(MazeController maze) {
		basicController = maze;
	}

	/* 
	 * @see falstad.Robot#isAtExit()
	 * 
	 * Returns whether or not the current position on the maze is right in front of the exit
	 */
	@Override
	public boolean isAtExit() {
		int pos[] = basicController.getCurrentPosition();
		if (basicController.getMazeConfiguration().getDistanceToExit(pos[0], pos[1]) == 1)
			return true;
		return false;
	}
	
	/**
	 * @param x
	 * @param y
	 * @return whether or not the given position on the maze is right in front of the exit
	 * Overloaded version of isAtExit method to accept x and y coordinates as parameters
	 *
	 */
	public boolean isAtExit(int x, int y)
	{
		if (basicController.getMazeConfiguration().getDistanceToExit(x, y) == 1)
			return true;
		return false;
	}

	/*
	 * @see falstad.Robot#canSeeExit(falstad.Robot.Direction)
	 * 
	 * Returns whether or not the exit is within sight in a given direction from the vantage point of your current position in the maze
	 */
	@Override
	public boolean canSeeExit(Direction direction) throws UnsupportedOperationException {
		int pos[] = basicController.getCurrentPosition();
		
		if (basicController.isOutside(pos[0], pos[1]))
			return true;
		
		if (direction == Direction.BACKWARD)
		{
			if (basicController.getCurrentDirection() == CardinalDirection.South)
			{
				int y = pos[1];
				while (!basicController.isOutside(pos[0], y) && basicController.getMazeConfiguration().getWidth() < y && (basicController.getMazeConfiguration().hasWall(pos[0], y, CardinalDirection.North) == false || !isAtExit(pos[0], y)))
				{
					if (!basicController.isOutside(pos[0], y--))
						y--;
				}
				if (basicController.isOutside(pos[0], y))
					return true;
			}
			if (basicController.getCurrentDirection() == CardinalDirection.North)
			{
				int y = pos[1];
				while (!basicController.isOutside(pos[0], y) && basicController.getMazeConfiguration().getWidth() < y && (basicController.getMazeConfiguration().hasWall(pos[0], y, CardinalDirection.South) == false || !isAtExit(pos[0], y)))
				{
					if (!basicController.isOutside(pos[0], y++))
						y++;
				}
				if (basicController.isOutside(pos[0], y++))
					return true;
			}
			if (basicController.getCurrentDirection() == CardinalDirection.East)
			{
				int x = pos[0];
				while (!basicController.isOutside(x, pos[1]) && basicController.getMazeConfiguration().getWidth() < x && (basicController.getMazeConfiguration().hasWall(x, pos[1], CardinalDirection.West) == false || !isAtExit(x, pos[1])))
				{
					if (!basicController.isOutside(x--, pos[1]))
						x--;
				}
				if (isAtExit(x, pos[1]))
					return true;
			}
			if (basicController.getCurrentDirection() == CardinalDirection.West)
			{
				int x = pos[0];
				while (!basicController.isOutside(x, pos[1]) && basicController.getMazeConfiguration().getWidth() < x && (basicController.getMazeConfiguration().hasWall(x, pos[1], CardinalDirection.East) == false || !isAtExit(x, pos[1])))
				{
					if (!basicController.isOutside(x++, pos[1]))
						x++;
				}
				if (isAtExit(x, pos[1]))
					return true;
			}
		}
		if (direction == Direction.FORWARD)
		{
			if (basicController.getCurrentDirection() == CardinalDirection.South)
			{
				int y = pos[1];
				while (!basicController.isOutside(pos[0], y) && basicController.getMazeConfiguration().hasWall(pos[0], y, CardinalDirection.South) == false)
				{
					if (!basicController.isOutside(pos[0], y++))
						y++;
				}
				if (basicController.isOutside(pos[0], y++))
					return true;
			}
			if (basicController.getCurrentDirection() == CardinalDirection.North)
			{
				int y = pos[1];
				while (!basicController.isOutside(pos[0], y) && basicController.getMazeConfiguration().hasWall(pos[0], y, CardinalDirection.North) == false)
				{
					if (!basicController.isOutside(pos[0], y--))
						y--;
				}
				if (basicController.isOutside(pos[0], y--))
					return true;
			}
			if (basicController.getCurrentDirection() == CardinalDirection.East)
			{
				int x = pos[0];
				while (!basicController.isOutside(x, pos[1]) && basicController.getMazeConfiguration().hasWall(x, pos[1], CardinalDirection.East) == false)
				{
					if (!basicController.isOutside(x++, pos[1]))
						x++;
				}
				if (basicController.isOutside(x++, pos[1])) 
					return true;
			}
			if (basicController.getCurrentDirection() == CardinalDirection.West)
			{
				int x = pos[0];
				while (!basicController.isOutside(x, pos[1]) && basicController.getMazeConfiguration().hasWall(x, pos[1], CardinalDirection.West) == false)
				{
					if (!basicController.isOutside(x--, pos[1]))
						x--;
				}
				if (basicController.isOutside(x--, pos[1]))
					return true;
			}
		}
		if (direction == Direction.RIGHT)
		{
			if (basicController.getCurrentDirection() == CardinalDirection.South)
			{
				int x = pos[0];
				while (!basicController.isOutside(x, pos[1]) && basicController.getMazeConfiguration().hasWall(x, pos[1], CardinalDirection.East) == false)
				{
					if (!basicController.isOutside(x++, pos[1]))
						x++;
				}
				if (basicController.isOutside(x++, pos[1]))
					return true;
			}
			if (basicController.getCurrentDirection() == CardinalDirection.North)
			{
				int x = pos[0];
				while (!basicController.isOutside(x, pos[0]) && basicController.getMazeConfiguration().hasWall(x, pos[1], CardinalDirection.West) == false)
				{
					if (!basicController.isOutside(x--, pos[1]))
						x--;
				}
				if (basicController.isOutside(x--, pos[1]))
					return true;
			}
			if (basicController.getCurrentDirection() == CardinalDirection.East)
			{
				int y = pos[1];
				while (!basicController.isOutside(pos[0], y) && basicController.getMazeConfiguration().hasWall(pos[0], y, CardinalDirection.North) == false)
				{
					if (!basicController.isOutside(pos[0], y--))
						y--;
				}
				if (basicController.isOutside(pos[0], y--))
					return true;
			}
			if (basicController.getCurrentDirection() == CardinalDirection.West)
			{
				int y = pos[1];
				while (!basicController.isOutside(pos[0], y) && basicController.getMazeConfiguration().hasWall(pos[0], y, CardinalDirection.South) == false)
				{
					if (!basicController.isOutside(pos[0], y++))
						y++;
				}
				if (basicController.isOutside(pos[0], y++))
					return true;
			}
		}
		if (direction == Direction.LEFT)
		{
			if (basicController.getCurrentDirection() == CardinalDirection.South)
			{
				int x = pos[0];
				while (!basicController.isOutside(x, pos[1]) && basicController.getMazeConfiguration().hasWall(x, pos[1], CardinalDirection.West) == false)
				{
					if (!basicController.isOutside(x--, pos[1]))
						x--;
				}
				if (basicController.isOutside(x--, pos[1]))
					return true;
			}
			if (basicController.getCurrentDirection() == CardinalDirection.North)
			{
				int x = pos[0];
				while (!basicController.isOutside(x, pos[1]) && basicController.getMazeConfiguration().hasWall(x, pos[1], CardinalDirection.East) == false)
				{
					if (!basicController.isOutside(x++, pos[1]))
						x++;
				}
				if (basicController.isOutside(x++, pos[1]))
					return true;
			}
			if (basicController.getCurrentDirection() == CardinalDirection.East)
			{
				int y = pos[1];
				while (!basicController.isOutside(pos[0], y) && basicController.getMazeConfiguration().hasWall(pos[0], y, CardinalDirection.South) == false)
				{
					if (!basicController.isOutside(pos[0], y++))
						y++;
				}
				if (basicController.isOutside(pos[0], y++))
					return true;
			}
			if (basicController.getCurrentDirection() == CardinalDirection.West)
			{
				int y = pos[1];
				while (!basicController.isOutside(pos[0], y) && basicController.getMazeConfiguration().hasWall(pos[0], y, CardinalDirection.North) == false)
				{
					if (!basicController.isOutside(pos[0], y--))
						y--;
				}
				if (basicController.isOutside(pos[0], y--))
					return true;
			}
		}
		return false;
	}

	/* 
	 * @see falstad.Robot#isInsideRoom()
	 * 
	 * Returns whether or not your current position in the maze is inside of a room
	 */
	@Override
	public boolean isInsideRoom() throws UnsupportedOperationException {
		if (roomSensor == false)
			throw new UnsupportedOperationException();
		if (basicController.isPerfect())
			return false;
		int pos[] = basicController.getCurrentPosition();
		if (basicController.seencells.isInRoom(pos[0], pos[1]))
			return true;
		return false;
	}

	/* 
	 * @see falstad.Robot#hasRoomSensor()
	 * 
	 * Returns whether or not the robot using the interface has a room sensor equipped
	 */
	@Override
	public boolean hasRoomSensor() {
		return roomSensor;
	}

	/*
	 * @see falstad.Robot#getCurrentDirection()
	 * 
	 * Returns the current direction you are facing in the maze
	 */
	@Override
	public CardinalDirection getCurrentDirection() {
		return basicController.getCurrentDirection();
	}

	/* 
	 * @see falstad.Robot#getBatteryLevel()
	 * 
	 * Returns your current battery level
	 */
	@Override
	public float getBatteryLevel() {
		return battery;
	}

	/* 
	 * @see falstad.Robot#setBatteryLevel(float)
	 * 
	 * Sets your battery level to a given value
	 */
	@Override
	public void setBatteryLevel(float level) {
		battery = level;
	}

	/*
	 * @see falstad.Robot#getOdometerReading()
	 * 
	 * Returns the current odometer reading
	 */
	@Override
	public int getOdometerReading() {
		return odometer;
	}

	/*
	 * @see falstad.Robot#resetOdometer()
	 * 
	 * Sets the current odometer reading to 0
	 */
	@Override
	public void resetOdometer() {
		odometer = 0;
	}

	/* 
	 * @see falstad.Robot#getEnergyForFullRotation()
	 * 
	 * Returns the constant energy usage that is taken for a full rotation
	 */
	@Override
	public float getEnergyForFullRotation() {
		return ROTATION + ROTATION;
	}

	/*
	 * @see falstad.Robot#getEnergyForStepForward()
	 * 
	 * Returns the constant energy usage that is taken for a step forward
	 */
	@Override
	public float getEnergyForStepForward() {
		return MOVE_STEP;
	}

	/*
	 * @see falstad.Robot#hasStopped()
	 * 
	 * Returns whether or not the robot has stopped moving
	 */
	@Override
	public boolean hasStopped() {
		return stopped;
	}

	/*
	 * @see falstad.Robot#distanceToObstacle(falstad.Robot.Direction)
	 * 
	 * Returns what the distance to an obstacle in a given direction is (measured in cells)
	 */
	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		if (!hasDistanceSensor(direction))
			throw new UnsupportedOperationException();
		int pos[] = basicController.getCurrentPosition();
		int count = 0;
		if (direction == Direction.BACKWARD)
		{
			if (basicController.getCurrentDirection() == CardinalDirection.South)
			{
				int y = pos[1];
				while (!isAtExit(pos[0], y) && basicController.getMazeConfiguration().hasWall(pos[0], y, CardinalDirection.North) == false)
				{
					count++;
					y--;
				}
			}
			if (basicController.getCurrentDirection() == CardinalDirection.North)
			{
				int y = pos[1];
				while (!isAtExit(pos[0], y) && basicController.getMazeConfiguration().hasWall(pos[0], y, CardinalDirection.South) == false)
				{
					count++;
					y++;
				}
			}
			if (basicController.getCurrentDirection() == CardinalDirection.East)
			{
				int x = pos[0];
				while (!isAtExit(x, pos[1]) && basicController.getMazeConfiguration().hasWall(x, pos[1], CardinalDirection.West) == false)
				{
					count++;
					x--;
				}
			}
			if (basicController.getCurrentDirection() == CardinalDirection.West)
			{
				int x = pos[0];
				while (!isAtExit(x, pos[1]) && basicController.getMazeConfiguration().hasWall(x, pos[1], CardinalDirection.East) == false)
				{
					count++;
					x++;
				}
			}
		}
		if (direction == Direction.FORWARD)
		{
			if (basicController.getCurrentDirection() == CardinalDirection.South)
			{
				int y = pos[1];
				while (!isAtExit(pos[0], y) && basicController.getMazeConfiguration().hasWall(pos[0], y, CardinalDirection.South) == false)
				{
					count++;
					y++;
				}
			}
			if (basicController.getCurrentDirection() == CardinalDirection.North)
			{
				int y = pos[1];
				while (!isAtExit(pos[0], y) && basicController.getMazeConfiguration().hasWall(pos[0], y, CardinalDirection.North) == false)
				{
					count++;
					y--;
				}
			}
			if (basicController.getCurrentDirection() == CardinalDirection.East)
			{
				int x = pos[0];
				while (!isAtExit(x, pos[1]) && basicController.getMazeConfiguration().hasWall(x, pos[1], CardinalDirection.East) == false)
				{
					count++;
					x++;
				}
			}
			if (basicController.getCurrentDirection() == CardinalDirection.West)
			{
				int x = pos[0];
				while (!isAtExit(x, pos[1]) && basicController.getMazeConfiguration().hasWall(x, pos[1], CardinalDirection.West) == false)
				{
					count++;
					x--;
				}
			}
		}
		if (direction == Direction.RIGHT)
		{
			if (basicController.getCurrentDirection() == CardinalDirection.South)
			{
				int x = pos[0];
				while (!isAtExit(x, pos[1]) && basicController.getMazeConfiguration().hasWall(x, pos[1], CardinalDirection.East) == false)
				{
					count++;
					x++;
				}
			}
			if (basicController.getCurrentDirection() == CardinalDirection.North)
			{
				int x = pos[0];
				while (!isAtExit(x, pos[1]) && basicController.getMazeConfiguration().hasWall(x, pos[1], CardinalDirection.West) == false)
				{
					count++;
					x--;
				}
			}
			if (basicController.getCurrentDirection() == CardinalDirection.East)
			{
				int y = pos[1];
				while (!isAtExit(pos[0], y) && basicController.getMazeConfiguration().hasWall(pos[0], y, CardinalDirection.North) == false)
				{
					count++;
					y--;
				}
			}
			if (basicController.getCurrentDirection() == CardinalDirection.West)
			{
				int y = pos[1];
				while (!isAtExit(pos[0], y) && basicController.getMazeConfiguration().hasWall(pos[0], y, CardinalDirection.South) == false)
				{
					count++;
					y++;
				}
			}
		}
		if (direction == Direction.LEFT)
		{
			if (basicController.getCurrentDirection() == CardinalDirection.South)
			{
				int x = pos[0];
				while (!isAtExit(x, pos[1]) && basicController.getMazeConfiguration().hasWall(x, pos[1], CardinalDirection.West) == false)
				{
					count++;
					x--;
				}
			}
			if (basicController.getCurrentDirection() == CardinalDirection.North)
			{
				int x = pos[0];
				while (!isAtExit(x, pos[1]) && basicController.getMazeConfiguration().hasWall(x, pos[1], CardinalDirection.East) == false)
				{
					count++;
					x++;
				}
			}
			if (basicController.getCurrentDirection() == CardinalDirection.East)
			{
				int y = pos[1];
				while (!isAtExit(pos[0], y) && basicController.getMazeConfiguration().hasWall(pos[0], y, CardinalDirection.South) == false)
				{
					count++;
					y++;
				}
			}
			if (basicController.getCurrentDirection() == CardinalDirection.West)
			{
				int y = pos[1];
				while (!isAtExit(pos[0], y) && basicController.getMazeConfiguration().hasWall(pos[0], y, CardinalDirection.North) == false)
				{
					count++;
					y--;
				}
			}
		}
		return count;
	}

	/*
	 * @see falstad.Robot#hasDistanceSensor(falstad.Robot.Direction)
	 * 
	 * Returns whether or not the robot comes equipped with a distance sensor
	 */
	@Override
	public boolean hasDistanceSensor(Direction direction) {
		if (direction == Direction.FORWARD)
			return distanceSensorForward;
		if (direction == Direction.BACKWARD)
			return distanceSensorBackward;
		if (direction == Direction.LEFT)
			return distanceSensorLeft;
		if (direction == Direction.RIGHT)
			return distanceSensorRight;
		return false;
	}

	public MazeController getMaze()
	{
		return basicController;
	}

}
