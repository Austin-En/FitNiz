package ca.mohawkcollege.enes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddExerciseActivity extends AppCompatActivity {
Button saveButton;
ImageView backButtonAddExercise;
EditText titleText;
EditText setsText;
EditText repsText;
EditText weightsUsedText;
DBHelper DB;

    /**
     * This activity is used to take in user input and add exercise to user created workout plans
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
        DB = new DBHelper(this);
        backButtonAddExercise = findViewById(R.id.backButtonAddExercise);
        saveButton = findViewById(R.id.saveButton);
        titleText = findViewById(R.id.exerciseTitleText);
        setsText = findViewById(R.id.setsText);
        repsText = findViewById(R.id.repsText);
        weightsUsedText = findViewById(R.id.weightsUsedText);
        backButtonAddExercise.setOnClickListener(this::sendToCreateWorkoutPage);
        saveButton.setOnClickListener(this::sendToCreateWorkoutPageWithSave);
    }


    private void sendToCreateWorkoutPage(View view) {
        if(WorkoutPlansActivity.currentWorkoutPlanID != 0){//This verifies that the plan does exist before adding it to the database
            startActivity(new Intent(AddExerciseActivity.this, CreateWorkoutPlanActivity.class));
        }
        else{
            startActivity(new Intent(AddExerciseActivity.this, EditWorkoutPlanActivity.class));
        }

    }

    /**
     * This method is used as a onclicklistener to verify that the user has inputted information for all of the text fields
     * @param view
     */
    private void sendToCreateWorkoutPageWithSave(View view) {
        String title = titleText.getText().toString();
        String sets = setsText.getText().toString();
        String reps = repsText.getText().toString();
        String weights = weightsUsedText.getText().toString();
        if(title.equals("") || sets.equals("") || reps.equals("") || weights.equals("")){//Verifies that the textfields aren't empty
            Toast.makeText(AddExerciseActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
        }
        else if(WorkoutPlansActivity.currentWorkoutPlanID != 0){//Verifies that the workout plan exists before adding the exercise to the database
            if(DB.insertExercise(WorkoutPlansActivity.currentWorkoutPlanID, MainActivity.currentUserID, title, Integer.parseInt(sets), Integer.parseInt(reps), Integer.parseInt(weights))) {
                startActivity(new Intent(AddExerciseActivity.this, CreateWorkoutPlanActivity.class));
            }
        }
        else if(EditWorkoutPlanActivity.currentPlanID != 0){//Verifies that the workout plan exists before adding the exercise to the database
            if(DB.insertExercise(EditWorkoutPlanActivity.currentPlanID, MainActivity.currentUserID, title, Integer.parseInt(sets), Integer.parseInt(reps), Integer.parseInt(weights))) {
                startActivity(new Intent(AddExerciseActivity.this, EditWorkoutPlanActivity.class));
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}