package falstad;

import generation.Distance;

import falstad.MazeController.UserInput;
import falstad.Robot.Direction;
import falstad.Robot.Turn;

/**
 * @author Sean Thomson
 * Project 3
 * 
 * Responsibilities:
 * 	Operate on top of the Robot class
 * 	Feeds information and inputs to the Robot class to control it
 * 
 * Collaborators:
 * 	RobotDriver class that is a basic interface
 * 	Robot class that allows a user or algorithm to control the maze directly
 *
 */
public class ManualDriver implements RobotDriver {

	Robot robot;
	Distance distanceValue;
	
	int mazeWidth;
	int mazeHeight;
	
	/* 
	 * @see falstad.RobotDriver#setRobot(falstad.Robot)
	 * 
	 * Sets what robot will be used by the driver
	 */
	@Override
	public void setRobot(Robot r) {
		robot = r;
	}
	
	/* 
	 * @see falstad.RobotDriver#setDimensions(int, int)
	 * 
	 * Tells the robot what the dimensions of the maze are
	 */
	@Override
	public void setDimensions(int width, int height) {
		mazeWidth = width;
		mazeHeight = height;
	}

	/* 
	 * @see falstad.RobotDriver#setDistance(generation.Distance)
	 * 
	 * Tells the robot what the distances between cells and the exit are
	 */
	@Override
	public void setDistance(Distance distance) {
		distanceValue = distance;
	}

	/*
	 * @see falstad.RobotDriver#drive2Exit()
	 * 
	 * Returns whether or not a robot can drive to the exit
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		if (robot.isAtExit())
			return true;
		return false;
	}

	/*
	 * @see falstad.RobotDriver#getEnergyConsumption()
	 * 
	 * Returns the energy consumed by the robot
	 */
	@Override
	public float getEnergyConsumption() {
		return 3000 - robot.getBatteryLevel();
	}

	/*
	 * @see falstad.RobotDriver#getPathLength()
	 * 
	 * Returns the path length of the robot
	 */
	@Override
	public int getPathLength() {
		return robot.getOdometerReading();
	}
	
	/**
	 * @param key
	 * 
	 * Allows for manual manipulation of the robot
	 */
	public void keyDown(UserInput key) {
		switch (key){
		case Start:
			break;
		case Up:
			robot.move(1, true);
			break;
		case Left:
			robot.rotate(Turn.LEFT);
			break;
		case Right:
			robot.rotate(Turn.RIGHT);
			break;
		case Down:
			robot.move(-1, true);
			break;
		case Obstacle:
			System.out.println(robot.distanceToObstacle(Direction.FORWARD));
			break;
		case ObstacleBackward:
			System.out.println(robot.distanceToObstacle(Direction.BACKWARD));
			break;
		case ObstacleLeft:
			System.out.println(robot.distanceToObstacle(Direction.LEFT));
			break;
		case ObstacleRight:
			System.out.println(robot.distanceToObstacle(Direction.RIGHT));
			break;
		}
	}
	

}
