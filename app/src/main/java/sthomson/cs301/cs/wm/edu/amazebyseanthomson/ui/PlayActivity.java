package sthomson.cs301.cs.wm.edu.amazebyseanthomson.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.util.Log;
import android.widget.ToggleButton;

import falstad.BasicRobot;
import falstad.MazeFileReader;
import falstad.MazeFileWriter;
import falstad.MazePanel;
import falstad.MazeController;
import falstad.MazeHolder;

import falstad.Pledge;
import falstad.Robot;
import falstad.RobotDriver;
import falstad.WallFollower;
import falstad.Wizard;
import generation.Distance;
import sthomson.cs301.cs.wm.edu.amazebyseanthomson.R;


public class PlayActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static final String ENERGY_CONSUMED = "com.example.myfirstapp.ENERGY";
    public static final String PATH = "com.example.myfirstapp.PATH";
    private static final String TAG = "PlayActivity";

    private String robotType;
    private int pathLength;
    private int energy;
    private MazeController mazeController;
    private MazePanel panel;

    private Robot robot;
    private RobotDriver robotDriver;

    public Handler driverHandler = new Handler();
    public Runnable DriverRunnable;
    public Thread driverThread;

    public boolean isMoving;
    private boolean win;


    /**
     * @param savedInstanceState
     *
     * Method runs when PlayActivity is created
     * Sets energy and pathLength ints to their proper values
     * Unpacks intent string extra and decides what buttons to display to the user
     * Displayed buttons depend on what robotType is selected
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        pathLength = 0;
        energy = 3000;

        isMoving = false;

        //mazeController = MazeHolder.getInstance().getMaze();

        Intent intent = getIntent();
        robotType = intent.getStringExtra(GeneratingActivity.ROBOT_TYPE);

        mazeController = MazeHolder.getInstance().getMaze();
        panel = (MazePanel) findViewById(R.id.maze);
        mazeController.setPanel(panel);

        Log.v(TAG, "Skill level: " + mazeController.getSkillLevel() + " Builder: " + mazeController.getBuilder().toString());

        mazeController.notifyViewerRedraw();

        if (robotType.equals("Manual"))
        {
            Log.v(TAG, "Showing buttons for user operation");
            Button upButton = findViewById(R.id.up_arrow);
            upButton.setVisibility(View.VISIBLE);

            Button rightButton = findViewById(R.id.right_arrow);
            rightButton.setVisibility(View.VISIBLE);

            Button leftButton = findViewById(R.id.left_arrow);
            leftButton.setVisibility(View.VISIBLE);
        }

        if (robotType.equals("Wizard"))
        {
            Log.v(TAG, "Creating new Wizard to explore maze");
            ToggleButton mazeExplore = findViewById(R.id.explore_toggle);
            mazeExplore.setVisibility(View.VISIBLE);
            robot = new BasicRobot(mazeController,true,true,true,true,true);
            robotDriver = new Wizard();
            robotDriver.setRobot(robot);
            ((Wizard) robotDriver).setActivity(this);
        }

        if (robotType.equals("Wall Follower"))
        {
            Log.v(TAG, "Creating new WallFollower to explore maze");
            ToggleButton mazeExplore = findViewById(R.id.explore_toggle);
            mazeExplore.setVisibility(View.VISIBLE);
            robot = new BasicRobot(mazeController,true,false,false,true,false);
            robotDriver = new WallFollower();
            robotDriver.setRobot(robot);
            ((WallFollower) robotDriver).setActivity(this);
        }

        if (robotType.equals("Pledge"))
        {
            Log.v(TAG, "Creating new Pledge to explore maze");
            ToggleButton mazeExplore = findViewById(R.id.explore_toggle);
            mazeExplore.setVisibility(View.VISIBLE);
            robot = new BasicRobot(mazeController,true,false,false,true,true);
            robotDriver = new Pledge();
            robotDriver.setRobot(robot);
            ((Pledge) robotDriver).setActivity(this);
        }
    }


    /**
     * @param view
     *
     * Called when a user presses the navigation buttons that are shown when a Manual exploration is chosen
     * Updates pathLength and energy ints depending on what direction a user moves in the maze
     */
    public void moveManual(View view)
    {
        int viewId = view.getId();

        if (energy <= 0)
        {
            energyEmpty();
        }

        if (R.id.left_arrow == viewId)
        {
            energy = energy - 3;
            ProgressBar energyBar = findViewById(R.id.energy_bar);
            energyBar.setProgress(energy);
            Log.v(TAG, "Rotate to the left; Decrement energy by 3");
            //Toast.makeText(getApplicationContext(), "Rotate left", Toast.LENGTH_SHORT).show();
            mazeController.operateGameInPlayingState(MazeController.UserInput.Left);
        }
        else if (R.id.right_arrow == viewId)
        {
            energy = energy - 3;
            ProgressBar energyBar = findViewById(R.id.energy_bar);
            energyBar.setProgress(energy);
            Log.v(TAG, "Rotate to the right; Decrement energy by 3");
            //Toast.makeText(getApplicationContext(), "Rotate right", Toast.LENGTH_SHORT).show();
            mazeController.operateGameInPlayingState(MazeController.UserInput.Right);
        }
        else if (R.id.up_arrow == viewId)
        {
            pathLength++;
            energy = energy - 5;
            ProgressBar energyBar = findViewById(R.id.energy_bar);
            energyBar.setProgress(energy);
            Log.v(TAG, "Move forward; Decrement energy by 5");
            //Toast.makeText(getApplicationContext(), "Move forward", Toast.LENGTH_SHORT).show();
            mazeController.operateGameInPlayingState(MazeController.UserInput.Up);
            int[] currentPosition = mazeController.getCurrentPosition();
            if (mazeController.isOutside(currentPosition[0],currentPosition[1]))
            {
                switchToFinish("win");
            }
        }
    }

    /**
     * @param view
     *
     * Called when a user presses the toggle solution togglebutton
     * At this point, simply alerts the user with a toast and Log.v that they toggled the solution to the maze
     */
    public void toggleSolution(View view)
    {
        Log.v(TAG, "Toggling solution to maze at request of user");
        //Toast.makeText(getApplicationContext(), "Toggling solution", Toast.LENGTH_LONG).show();
        mazeController.operateGameInPlayingState(MazeController.UserInput.ToggleSolution);
    }

    /**
     * @param view
     *
     * Called when a user presses the toggle map togglebutton
     * At this point, simply alerts the user with a toast and Log.v that they toggled the map of the maze
     */
    public void toggleMap(View view)
    {
        Log.v(TAG, "Toggling map of maze at request of user");
        //Toast.makeText(getApplicationContext(), "Toggling map", Toast.LENGTH_LONG).show();
        mazeController.operateGameInPlayingState(MazeController.UserInput.ToggleFullMap);
    }

    /**
     * @param view
     *
     * Called when a user presses the toggle walls togglebutton
     * At this point, simply alerts the user with a toast and Log.v that they toggled the wall map of the maze
     */
    public void toggleWalls(View view)
    {
        Log.v(TAG, "Toggling walls at request of user");
        //Toast.makeText(getApplicationContext(), "Toggling walls", Toast.LENGTH_LONG).show();
        mazeController.operateGameInPlayingState(MazeController.UserInput.ToggleLocalMap);
    }

    /**
     * @param view
     *
     * Called when a user presses the Start/Pause Maze Exploration toggle button
     */
    public void toggleExploration(View view)
    {
        ToggleButton button = (ToggleButton) view;
        if (button.getText().toString().equals("Pause maze exploration"))
        {
            Log.v(TAG, "Starting maze exploration");
            isMoving = true;
            DriverRunnable = new DriverRunnable();
            driverHandler.postDelayed(DriverRunnable, 200);
            driverThread = new Thread(DriverRunnable);
            driverThread.run();
        }
        if (button.getText().toString().equals("Start maze exploration"))
        {
            Log.v(TAG, "Pausing maze exploration");
            isMoving = false;
        }
    }

    /**
     * Called when a user runs out of energy
     * Creates an intent with a bundle added to it
     * Bundle includes energy, pathLength values and shows FinishActivity with lose condition
     */
    public void energyEmpty()
    {
        Log.v(TAG, "Out of energy! Switching to FinishActivity");
        Intent intent = new Intent(this, FinishActivity.class);
        Bundle extras = new Bundle();

        String lose = "lose";
        extras.putString(EXTRA_MESSAGE, lose);

        String energyUsed = Integer.toString(energy);
        extras.putString(ENERGY_CONSUMED, energyUsed);

        String path = Integer.toString(pathLength);
        extras.putString(PATH, path);

        intent.putExtras(extras);
        startActivity(intent);
    }

    /**
     * @param button
     *
     * Called from onClick method
     * Creates an intent to switch to FinishActivity and adds a bundle to it
     * Bundle includes pathLength and energy values that are displayed in FinishActivity
     * Bundle also includes whether the user wins or loses which is passed to this method in the button parameter
     */
    public void switchToFinish(String button)
    {
        if (button.equals("win"))
        {
            Log.v(TAG, "Switching to finish activity with win condition");
            //Toast.makeText(getApplicationContext(), "Switching to finish activity with win condition", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, FinishActivity.class);
            Bundle extras = new Bundle();

            String win = "win";
            extras.putString(EXTRA_MESSAGE, win);

            String energyUsed = Integer.toString(energy);
            extras.putString(ENERGY_CONSUMED, energyUsed);

            String path = Integer.toString(pathLength);
            extras.putString(PATH, path);

            intent.putExtras(extras);
            startActivity(intent);
        }
        if(button.equals("lose"))
        {
            Log.v(TAG, "Switching to finish activity with lose condition");
            //Toast.makeText(getApplicationContext(), "Switching to finish activity with lose condition", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, FinishActivity.class);
            Bundle extras = new Bundle();

            String lose = "lose";
            extras.putString(EXTRA_MESSAGE, lose);

            String energyUsed = Integer.toString(energy);
            extras.putString(ENERGY_CONSUMED, energyUsed);

            String path = Integer.toString(pathLength);
            extras.putString(PATH, path);

            intent.putExtras(extras);
            startActivity(intent);
        }
    }

    /**
     * Overrides default functionality for when a user presses the Android back button
     * Returns user to AMazeActivity
     */
    @Override
    public void onBackPressed()
    {
        //Toast.makeText(getApplicationContext(),"Switching back to AMazeActivity", Toast.LENGTH_LONG).show();
        Log.v(TAG, "Switching back to AMazeActivity");
        Intent intent = new Intent(this, AMazeActivity.class);
        startActivity(intent);
    }

    /**
     * Checks win condition between each movement
     * Switches to FinishActivity with win condition if win condition is met
     */
    private void winCondition()
    {
        int[] curPos = mazeController.getCurrentPosition();
        if (win == false && mazeController.isOutside(curPos[0], curPos[1]))
        {
            win = true;
            switchToFinish("win");
        }
    }

    /**
     * Updates the energy and path length to be shown in FinishActivity and energy progress bar in PlayActivity
     */
    private void updateEnergyAndPathLength()
    {
        Log.v(TAG,"Updating energy and path length values");
        energy = (int) (robot.getBatteryLevel());
        pathLength = robot.getOdometerReading();
        if (energy <= 0)
        {
            energyEmpty();
        }
    }

    /**
     * Private class that is used to run robot drivers such that the animation is shown smoothly
     * Uses a handler to call invalidate method to show robot movements incrementally
     */
    private class DriverRunnable implements Runnable
    {
        @Override
        public void run() {
            try {
                if (isMoving == true) {
                    robotDriver.drive2Exit();
                    updateEnergyAndPathLength();
                    winCondition();
                    driverHandler.postDelayed(this, 1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            panel.invalidate();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
