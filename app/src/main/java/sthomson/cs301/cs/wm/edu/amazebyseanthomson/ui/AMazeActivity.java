package sthomson.cs301.cs.wm.edu.amazebyseanthomson.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;
import android.util.Log;
import android.widget.SeekBar;

import java.io.File;

import falstad.MazeFileReader;
import sthomson.cs301.cs.wm.edu.amazebyseanthomson.R;

public class AMazeActivity extends AppCompatActivity {

    private static final String TAG = "AMazeActivity";
    public static final String ROBOT = "cs.wm.edu.ROBOT";
    public static final String GENERATOR = "cs.wm.edu.GENERATOR";
    public static final String SKILL = "cs.wm.edu.SKILL";

    /**
     * @param savedInstanceState
     *
     * Method runs when AMazeActivity is created
     * Just issues default method calls
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amaze);
    }

    /**
     * @param view
     *
     * Runs when "generate new maze" and "load previous maze" buttons are selected by user
     * Takes option from robot spinner to decide what options are displayed on PlayActivity
     */
    public void switchToGenerating(View view)
    {
        Bundle bundle = new Bundle();

        Spinner robotSpinner = (Spinner) findViewById(R.id.robot_spinner);
        String selectedRobot = robotSpinner.getSelectedItem().toString();
        bundle.putString(ROBOT, selectedRobot);

        Spinner genSpinner = (Spinner) findViewById(R.id.generation_spinner);
        String generation = genSpinner.getSelectedItem().toString();
        bundle.putString(GENERATOR, generation);

        SeekBar levelBar = (SeekBar) findViewById(R.id.seekBar);
        int level = levelBar.getProgress();
        bundle.putInt(SKILL, level);

        //Toast.makeText(getApplicationContext(),"Switching to generating activity to create maze with " + selectedRobot + " driver, " + generation + " maze generation, and " + level + " difficulty", Toast.LENGTH_LONG).show();
        Log.v(TAG, "Generating maze: " + selectedRobot + " robot, " + generation + " generation algorithm, skill level " + level);
        Log.v(TAG, "Switching to GeneratingActivity to generate selected maze");

        Intent intent = new Intent(this, GeneratingActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void loadMaze(View view)
    {
        Log.v(TAG, "Checking to see if maze loading is possible");
        SeekBar levelBar = (SeekBar) findViewById(R.id.seekBar);
        int level = levelBar.getProgress();

        Context context = getApplicationContext();
        String fileName =  context.getFilesDir().getAbsolutePath() + "/maze" + level + ".xml";
        File file = new File(fileName);
        MazeFileReader fileReader = new MazeFileReader(file.getPath());

        if (level <= 3 && fileReader.getCells() != null)
        {
            Bundle bundle = new Bundle();

            Spinner robotSpinner = (Spinner) findViewById(R.id.robot_spinner);
            String selectedRobot = robotSpinner.getSelectedItem().toString();
            bundle.putString(ROBOT, selectedRobot);

            bundle.putString(GENERATOR, "load");

            bundle.putInt(SKILL, level);

            Log.v(TAG, "Switching to GeneratingActivity to load selected maze");
            Intent intent = new Intent(this, GeneratingActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        else if (level > 3)
        {
            switchToGenerating(view);
        }

        else
        {
            Toast.makeText(getApplicationContext(), "No previous maze exists for this skill level", Toast.LENGTH_LONG).show();
            Log.v(TAG, "No mazes saved for this skill level");
        }
    }
}
