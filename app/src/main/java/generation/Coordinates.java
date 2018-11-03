package generation;

import java.util.ArrayList;

public class Coordinates {
	
	public int x;
	
	public int y;
	
	public ArrayList<Coordinates> set;
	
	public boolean vert;
	
	public Coordinates(int y, int x)
	{
		ArrayList<Coordinates> newList = new ArrayList<Coordinates>();
		this.x = x;
		this.y = y;
		this.vert = false;
		newList.add(this);
		this.set = newList;
	}
	
	public Coordinates(int y, int x, ArrayList<Coordinates> newSet)
	{
		this.x = x;
		this.y = y;
		this.set = newSet;
		this.vert = false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinates other = (Coordinates) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

}
