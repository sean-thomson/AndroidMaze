package sthomson.cs301.cs.wm.edu.amazebyseanthomson.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import falstad.Constants;
import falstad.MazeController;
import falstad.MazeFileWriter;
import falstad.MazeHolder;
import generation.BSPNode;
import generation.Cells;
import generation.Distance;
import sthomson.cs301.cs.wm.edu.amazebyseanthomson.R;

public class FinishActivity extends AppCompatActivity {

    private static final String TAG = "FinishActivity";

    /**
     * @param savedInstanceState
     *
     * Called when FinishActivity is created
     * Calls default methods
     * Depending on what intent bundle contains, either reveals lose or win TextView
     * Displays pathLength and energy values passed in the bundle in the intent
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String message = extras.getString(PlayActivity.EXTRA_MESSAGE);

        Log.v(TAG, "Getting path length and energy from intent to show values to user");
        String pathLength = extras.getString(PlayActivity.PATH);
        TextView pathView = (TextView) findViewById(R.id.path_num);
        pathView.setText(pathLength);

        String energy = extras.getString(PlayActivity.ENERGY_CONSUMED);
        TextView energyView = (TextView) findViewById(R.id.energy_num);
        energyView.setText(energy);

        TextView winText = (TextView) findViewById(R.id.win_message);
        winText.setVisibility(View.INVISIBLE);

        TextView loseText = (TextView) findViewById(R.id.lose_message);
        loseText.setVisibility(View.INVISIBLE);

        if (message.equals("win"))
        {
            Log.v(TAG, "Showing win text");
            winText.setVisibility(View.VISIBLE);
        }
        if (message.equals("lose"))
        {
            Log.v(TAG, "Showing lose text");
            loseText.setVisibility(View.VISIBLE);
        }


    }

    /**
     * Overrides default functionality for when a user presses the Android back button
     * Returns a user back to AMazeActivity
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
