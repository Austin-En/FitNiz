package ca.mohawkcollege.enes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Switch;

public class WorkoutActivity extends AppCompatActivity {
ImageView backWorkoutButton;
Switch startSwitch;
Button viewWorkoutPlansButton;
Button finishButton;
Chronometer simpleChronometer;
public static long timeWhenStopped = 0;
public static String timeString;
public static int returnPage = 0;
public static Boolean type;

    /**
     * This activity allows the user to access workout plans and time themselves while they workout, once the user is finished the current time of the timer
     * will be brought over to the results page to be saved to the database
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        simpleChronometer = (Chronometer) findViewById(R.id.workoutTimer); // initiate a chronometer
        type = false;
        if(returnPage == 0){
            timeWhenStopped = 0;
        }
        else{
            returnPage = 0;
            simpleChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
            timeWhenStopped = simpleChronometer.getBase() - SystemClock.elapsedRealtime();
        }
        startSwitch = findViewById(R.id.startSwitch);
        viewWorkoutPlansButton = findViewById(R.id.viewWorkoutPlansButton);
        finishButton = findViewById(R.id.finishButton);
        backWorkoutButton = findViewById(R.id.backButtonWorkout);
        viewWorkoutPlansButton.setOnClickListener(this::sendToViewWorkoutPlans);
        finishButton.setOnClickListener(this::sendToResultsPage);
        backWorkoutButton.setOnClickListener(this::sendToWorkAndCardio);
        startSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startSwitch.isChecked() == true) {
                    simpleChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                    simpleChronometer.start(); // start a chronometer
                } else {
                    simpleChronometer.stop(); // stop a chronometer
                    timeWhenStopped = simpleChronometer.getBase() - SystemClock.elapsedRealtime();
                }
            }
        });
    }


    private void sendToWorkAndCardio(View view){
        startActivity(new Intent(WorkoutActivity.this, HomeActivity.class));
    }

    private void sendToViewWorkoutPlans(View view){
        if (startSwitch.isChecked() == false) {
            simpleChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
            simpleChronometer.start();
        }
        simpleChronometer.stop();
        timeWhenStopped = simpleChronometer.getBase() - SystemClock.elapsedRealtime();
        returnPage = 2;
        startActivity(new Intent(WorkoutActivity.this, WorkoutPlansActivity.class));
    }

    private void sendToResultsPage(View view){
        simpleChronometer.stop();
        timeWhenStopped = simpleChronometer.getBase() - SystemClock.elapsedRealtime();
        timeString = simpleChronometer.getText().toString();
        startActivity(new Intent(WorkoutActivity.this, ResultsActivity.class));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}