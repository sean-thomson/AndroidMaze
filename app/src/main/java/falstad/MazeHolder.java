package falstad;

import android.graphics.Canvas;
import android.app.Application;

/**
 * Created by seanthomson on 11/30/17.
 */

public class MazeHolder extends Application{

    private MazeController maze;

    private static final MazeHolder holder = new MazeHolder();

    public void setMaze(MazeController newMaze)
    {
        this.maze = newMaze;
    }

    public MazeController getMaze()
    {
        return maze;
    }

    public static MazeHolder getInstance()
    {
        return holder;
    }
}
