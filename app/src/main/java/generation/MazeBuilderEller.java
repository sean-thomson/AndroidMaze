package generation;

import java.util.ArrayList;
import java.util.Random;

public class MazeBuilderEller extends MazeBuilder implements Runnable {
	
	Coordinates[][] maze;
	
	public MazeBuilderEller()
	{
		super();
		System.out.println("MazeBuilderEller uses Eller's algorithm to generate maze.");
	}
	
	public MazeBuilderEller(boolean det)
	{
		super(det);
		System.out.println("MazeBuilderEller uses Eller's algorithm to generate maze.");
	}
	
	
	private void updateListOfWalls(int x, int y, ArrayList<Wall> walls) 
	{
		Wall wall = new Wall(x, y, CardinalDirection.East);
		
		maze = new Coordinates[height][width];
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				maze[i][j] = new Coordinates(i, j);
				
				wall.setWall(j,  i, CardinalDirection.East);
				if (cells.canGo(wall))
				{
					walls.add(new Wall(j, i, CardinalDirection.East));
				}
				
				wall.setWall(j,  i, CardinalDirection.South);
				if (cells.canGo(wall))
				{
					walls.add(new Wall(j, i, CardinalDirection.South));
				}
			}
		}
	}
	
	protected void findAndDeleteEastWall(ArrayList<Wall> walls, int yCoord, int xCoord)
	{
		for (int i = 0; i < walls.size(); i++)
		{
			if (walls.get(i).getDirection() == CardinalDirection.East && walls.get(i).getY() == yCoord && walls.get(i).getX() == xCoord)
			{
				cells.deleteWall(walls.get(i));
				walls.remove(i);
				System.out.println("East wall removed at " + yCoord + ", " + xCoord);
				maze[yCoord][xCoord].set.add(maze[yCoord][xCoord+1]);
				maze[yCoord][xCoord+1].set = maze[yCoord][xCoord].set;
			}
		}
	}
	
	protected void findAndDeleteSouthWall(ArrayList<Wall> walls, ArrayList<Coordinates> cellSet, int yCoord)
	{
		Random rand = new Random();
		
		ArrayList<Coordinates> sameRowCoords = new ArrayList<Coordinates>();
		
		for (int h = 0; h < cellSet.size(); h++)
		{
			if (cellSet.get(h).y == yCoord)
			{
				sameRowCoords.add(cellSet.get(h));
			}
		}
		
		Coordinates randomCoord = sameRowCoords.get(rand.nextInt(sameRowCoords.size()));
		for (int i = 0; i < walls.size(); i++)
		{
			if (walls.get(i).getDirection() == CardinalDirection.South && walls.get(i).getY() == randomCoord.y && walls.get(i).getX() == randomCoord.x)
			{
				cells.deleteWall(walls.get(i));
				walls.remove(i);
				System.out.println("South wall removed at " + randomCoord.y + ", " + randomCoord.x);
				maze[randomCoord.y][randomCoord.x].set.add(maze[randomCoord.y+1][randomCoord.x]);
				maze[randomCoord.y+1][randomCoord.x].set = maze[randomCoord.y][randomCoord.x].set;
			}
		}
		sameRowCoords.remove(randomCoord);
		for (int j = 0; j < sameRowCoords.size(); j++)
		{
			if (rand.nextBoolean() && sameRowCoords.size() != 0)
			{
				outer: for (int k = 0; k < walls.size(); k++)
				{
					if (walls.get(k).getDirection() == CardinalDirection.South && walls.get(k).getX() == sameRowCoords.get(j).x && walls.get(k).getY() == sameRowCoords.get(j).y)
					{
						cells.deleteWall(walls.get(k));
						walls.remove(k);
						System.out.println("South wall removed at " + sameRowCoords.get(j).y + ", " + sameRowCoords.get(j).x);
						maze[sameRowCoords.get(j).y][sameRowCoords.get(j).x].set.add(maze[sameRowCoords.get(j).y+1][sameRowCoords.get(j).x]);
						maze[sameRowCoords.get(j).y+1][sameRowCoords.get(j).x].set = maze[sameRowCoords.get(j).y][sameRowCoords.get(j).x].set;
						sameRowCoords.remove(j);
						break outer;
					}
				}
			}
		}
	}
	
	protected int sizeOfSetAtCurrentRow(int row, int col)
	{
		int count = 0;
		for (int i = 0; i < maze[row][col].set.size(); i++)
		{
			if (maze[row][col].set.get(i).y == row)
				count++;
		}
		return count;
	}
	
	protected void generatePathways()
	{
		Random random = new Random();
		
		ArrayList<Wall> removableWalls = new ArrayList<Wall>();
		updateListOfWalls(0, 0, removableWalls);
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				if (i != height-1 && random.nextBoolean() && j < width-1 && !maze[i][j].set.contains(maze[i][j+1]))
				{
					findAndDeleteEastWall(removableWalls, i, j);
				}
				if (i == height-1 && j < width-1 && !maze[i][j].set.contains(maze[i][j+1]))
				{
					findAndDeleteEastWall(removableWalls, i, j);
				}
			}
			ArrayList<ArrayList<Coordinates>> setList = new ArrayList<ArrayList<Coordinates>>();
			for (int k = 0; k < width; k++)
			{
				if (!setList.contains(maze[i][k].set))
					setList.add(maze[i][k].set);
			}
			for (int l = 0; l < setList.size(); l++)
			{
				findAndDeleteSouthWall(removableWalls, setList.get(l), i);
			}
		}
			
	}
}
