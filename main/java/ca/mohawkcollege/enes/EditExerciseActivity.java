package ca.mohawkcollege.enes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EditExerciseActivity extends AppCompatActivity {
    Button editSaveButton;
    ImageView editBackButton;
    EditText editTitleText;
    EditText editSetsText;
    EditText editRepsText;
    EditText editWeightsText;
    DBHelper DB;
    int currentExerciseID;
    int currentPlanID;
    int returnPage;//0 is to the create page 1 is to the edit workout page


    /**
     * This method is accessed after the user taps on an exercise that has been created in a workoutplan, from this activity the user
     * is able to edit the information in the exercise
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exercise);
        DB = new DBHelper(this);
        Intent intent = getIntent();
        returnPage = Integer.parseInt(intent.getStringExtra("returnPage"));
        currentExerciseID = Integer.parseInt(intent.getStringExtra("exerciseID"));
        currentPlanID = Integer.parseInt(intent.getStringExtra("planID"));
        editBackButton = findViewById(R.id.editBackButton);
        editSaveButton = findViewById(R.id.editSaveButton);
        editTitleText = findViewById(R.id.editExerciseTitleText);
        editTitleText.setText(intent.getStringExtra("name"));
        editSetsText = findViewById(R.id.editSetsText);
        editSetsText.setText(intent.getStringExtra("sets"));
        editRepsText = findViewById(R.id.editRepsText);
        editRepsText.setText(intent.getStringExtra("reps"));
        editWeightsText = findViewById(R.id.editWeightsUsedText);
        editWeightsText.setText(intent.getStringExtra("weights"));
        editBackButton.setOnClickListener(this::sendToWorkoutPlansPage);
        editSaveButton.setOnClickListener(this::sendToWorkoutPlansPageWithSave);

    }

    /**
     * This method checks the user input to ensure its valid input before making the update on the database
     * @param view
     */
    private void sendToWorkoutPlansPageWithSave(View view) {
        String title = editTitleText.getText().toString();
        String sets = editSetsText.getText().toString();
        String reps = editRepsText.getText().toString();
        String weight = editWeightsText.getText().toString();
        if(title.equals("") || sets.equals("") || reps.equals("") || weight.equals("")){
            Toast.makeText(EditExerciseActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
        }
        else if(DB.updateExercise(currentExerciseID, currentPlanID, MainActivity.currentUserID, title, Integer.parseInt(sets), Integer.parseInt(reps), Integer.parseInt(weight))){
            if(returnPage == 0){
                startActivity(new Intent(EditExerciseActivity.this, CreateWorkoutPlanActivity.class));
            }
            else {
                startActivity(new Intent(EditExerciseActivity.this, EditWorkoutPlanActivity.class));
            }
        }
        else{
            Toast.makeText(EditExerciseActivity.this, "Not Valid", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendToWorkoutPlansPage(View view) {
        if(returnPage == 0){
            startActivity(new Intent(EditExerciseActivity.this, CreateWorkoutPlanActivity.class));
        }
        else {
            startActivity(new Intent(EditExerciseActivity.this, EditWorkoutPlanActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}