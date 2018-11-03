package sthomson.cs301.cs.wm.edu.amazebyseanthomson.ui;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Button;
import android.widget.Toast;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import java.io.File;

import falstad.Constants;
import falstad.FirstPersonDrawer;
import falstad.MazeFileReader;
import falstad.MazeFileWriter;
import falstad.MazePanel;
import falstad.MazeController;
import falstad.RobotDriver;
import falstad.Wizard;
import falstad.WallFollower;
import falstad.Pledge;
import falstad.MazeHolder;

import generation.BSPNode;
import generation.Cells;
import generation.Distance;
import generation.MazeFactory;

import sthomson.cs301.cs.wm.edu.amazebyseanthomson.R;

public class GeneratingActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button button;
    private int progressStatus = 1;
    private static final String TAG = "GeneratingActivity";

    private String robotType;
    private String generator;
    private int skillLevel;

    private MazePanel panel;

    private RobotDriver driver;
    private MazeController mazeController;

    public static final String ROBOT_TYPE = "com.example.myfirstapp.MESSAGE";

    /**
     * @param savedInstanceState
     *
     * Method runs when GeneratingActivity is created
     * Issues default method calls and gets the stringextra from the intent to create this activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        robotType = extras.getString(AMazeActivity.ROBOT);
        generator = extras.getString(AMazeActivity.GENERATOR);
        skillLevel = extras.getInt(AMazeActivity.SKILL);
    }

    /**
     * Method runs when GeneratingActivity starts
     * Issues default method calls and calls the fillProgressBar method
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        setContentView(R.layout.activity_generating);
        //fillProgressBar();
        if (generator.equals("load"))
        {
            loadMaze();
        }
        else
        {
            createMaze();
        }
    }

    /**
     * Method called from onStart() to create a new maze
     * Generates a new maze using MazeController and MazeFactory and creates a new intent to go to PlayActivity
     */
    private void createMaze()
    {
        Log.v(TAG, "Creating new MazeController and MazePanel");
        panel = new MazePanel(this);
        mazeController = new MazeController(this);

        new Thread(new Runnable () {
            @Override
            public void run()
            {
                final ProgressBar loadingBar = (ProgressBar) findViewById(R.id.progressBar);
                mazeController.setPanel(panel);
                loadingBar.setProgress(20);
                mazeController.setSkillLevel(skillLevel);
                loadingBar.setProgress(40);
                mazeController.setBuilder(generator);
                loadingBar.setProgress(50);

                Log.v(TAG, "Creating new MazeFactory");
                MazeFactory factory = new MazeFactory();
                loadingBar.setProgress(60);
                mazeController.init();
                loadingBar.setProgress(70);
                Log.v(TAG, "Generating new maze");
                factory.order(mazeController);
                loadingBar.setProgress(80);
                factory.waitTillDelivered();
                MazeHolder.getInstance().setMaze(mazeController);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (skillLevel <= 3) {
                            Log.v(TAG, "Maze has skill level <= 3, saving maze for later use");
                            Context context = getApplicationContext();
                            String fileName = context.getFilesDir().getAbsolutePath() + "/maze" + skillLevel + ".xml";

                            Log.v(TAG,"Getting maze attributes to save for later use...");
                            int mazeWidth = mazeController.getMazeConfiguration().getWidth();
                            int mazeHeight = mazeController.getMazeConfiguration().getHeight();
                            int skillLevel = mazeController.getSkillLevel();
                            BSPNode rootNode = mazeController.getMazeConfiguration().getRootnode();
                            Cells cells = mazeController.getMazeConfiguration().getMazecells();
                            int[] startPos = mazeController.getMazeConfiguration().getStartingPosition();
                            Distance dists = mazeController.getMazeConfiguration().getMazedists();
                            int[][] distsArray = dists.getDists();
                            Log.v(TAG, "Saving maze...");
                            MazeFileWriter.store(fileName, mazeWidth, mazeHeight, Constants.SKILL_ROOMS[skillLevel], Constants.SKILL_PARTCT[skillLevel],
                                    rootNode, cells, distsArray, startPos[0], startPos[1]);
                            Log.v(TAG, "Maze saved successfully!");
                        }
                        loadingBar.setProgress(90);
                        Log.v(TAG, "Switching to play activity after generation finished");
                        //Toast.makeText(getApplicationContext(), "Switching to play activity", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                        intent.putExtra(ROBOT_TYPE, robotType);
                        loadingBar.setProgress(100);
                        startActivity(intent);
                    }
                });
            }
        }).start();
    }

    /**
     * Method called from onStart()
     * Loads maze from file depending on what skill level is selected
     * Begins PlayActivity after maze is loaded
     */
    private void loadMaze()
    {
        final ProgressBar loadingBar = (ProgressBar) findViewById(R.id.progressBar);
        Context context = getApplicationContext();
        String fileName = context.getFilesDir().getAbsolutePath() + "/maze" + skillLevel + ".xml";
        Log.v(TAG,"Loading maze from " + fileName);
        panel = new MazePanel(this);
        mazeController = new MazeController(fileName, this);
        loadingBar.setProgress(10);

        new Thread(new Runnable(){
            @Override
            public void run()
            {
                mazeController.setPanel(panel);

                Context context = getApplicationContext();
                String fileName = context.getFilesDir().getAbsolutePath() + "/maze" + skillLevel +".xml";

                File file = new File(fileName);
                MazeFileReader fileReader = new MazeFileReader(file.getPath());
                loadingBar.setProgress(20);

                Log.v(TAG,"Populating MazeConfiguration fields...");
                mazeController.getMazeConfiguration().setStartingPosition(fileReader.getStartX(), fileReader.getStartY());
                loadingBar.setProgress(30);
                Distance dist = new Distance(fileReader.getDistances());
                loadingBar.setProgress(40);
                mazeController.getMazeConfiguration().setMazedists(dist);
                loadingBar.setProgress(50);
                mazeController.getMazeConfiguration().setMazecells(fileReader.getCells());
                loadingBar.setProgress(60);
                mazeController.getMazeConfiguration().setWidth(fileReader.getWidth());
                loadingBar.setProgress(70);
                mazeController.getMazeConfiguration().setHeight(fileReader.getHeight());
                loadingBar.setProgress(80);
                mazeController.getMazeConfiguration().setRootnode(fileReader.getRootNode());
                loadingBar.setProgress(80);

                Log.v(TAG,"Initializing MazeController...");
                mazeController.init();
                mazeController.deliver(mazeController.getMazeConfiguration());
                MazeHolder.getInstance().setMaze(mazeController);
                Log.v(TAG,"Maze loaded and set successfully!");
                loadingBar.setProgress(90);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.v(TAG, "Switching to play activity after maze loaded");
                        //Toast.makeText(getApplicationContext(), "Switching to play activity", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                        intent.putExtra(ROBOT_TYPE, robotType);
                        loadingBar.setProgress(100);
                        startActivity(intent);
                    }
                });
            }
        }).start();
    }

    /**
     * Called from onStart method
     * Creates a new thread to increase progress bar progress in a delayed manner
     * Once progress bar is filled to 100%, intent is created to switch to PlayActivity
     */
    protected void fillProgressBar()
    {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        new Thread(new Runnable(){
            @Override
            public void run() {
                while (progressStatus <= 100)
                {
                    progressStatus++;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        break;
                    }
                    progressBar.setProgress(progressStatus);
                    if (progressStatus == 100)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.v(TAG, "Switching to play activity after generation finished");
                                Toast.makeText(getApplicationContext(),"Switching to play activity", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                                intent.putExtra(ROBOT_TYPE, robotType);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
        }).start();

    }

    /**
     * Overrides the default method that is called when a user presses the Android back button
     * Returns the user back to AMazeActivity
     */
    @Override
    public void onBackPressed()
    {
        //Toast.makeText(getApplicationContext(),"Switching back to AMazeActivity", Toast.LENGTH_LONG).show();
        Log.v(TAG, "Switching back to AMazeActivity");
        Intent intent = new Intent(this, AMazeActivity.class);
        startActivity(intent);
    }
}
