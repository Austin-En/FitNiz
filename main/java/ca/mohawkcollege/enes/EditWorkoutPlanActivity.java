package ca.mohawkcollege.enes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditWorkoutPlanActivity extends AppCompatActivity {
ImageView backViewPlanButton;
EditText editPlanNameText;
EditText editLengthText;
Button editAddExerciseButton;
ListView editPlanExerciseList;
DBHelper DB;
public static int currentPlanID = 0;
String name;
int length;
public static String editTitleName = "";
public static String editTimeLength = "";

    /**
     * This activity is used when the user wants to edit/view anything about a selected workoutplan that has been created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout_plan);
        DB = new DBHelper(this);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        if(name != null) {
            currentPlanID = Integer.parseInt(intent.getStringExtra("planID"));
            length = Integer.parseInt(intent.getStringExtra("length"));
        }
        backViewPlanButton = findViewById(R.id.backButtonEditWorkoutPlan);
        editPlanNameText = findViewById(R.id.editWorkoutTitleText);
        editLengthText = findViewById(R.id.editWorkoutLengthText);
        editAddExerciseButton = findViewById(R.id.editAddButton);
        editPlanExerciseList = findViewById(R.id.editExerciseList);
        backViewPlanButton.setOnClickListener(this::sendToViewWorkoutPlansPage);
        editAddExerciseButton.setOnClickListener(this::sendToAddExercisePage);
        if(name == null) {
            editPlanNameText.setText(editTitleName);
            editLengthText.setText(editTimeLength);
        }
        else{
            editPlanNameText.setText(name);
            editLengthText.setText(String.valueOf(length));
        }
        ArrayList<String> exercisesArrayList = DB.getExerciseRecords(currentPlanID, MainActivity.currentUserID);
        ArrayList<String> displayExercisesArrayList = new ArrayList<>();
        Map<String, ArrayList<String>> dictionary = new HashMap<String, ArrayList<String>>();
        dictionary.put("exerciseID", new ArrayList<>());
        dictionary.put("planID", new ArrayList<>());
        dictionary.put("name", new ArrayList<>());
        dictionary.put("sets", new ArrayList<>());
        dictionary.put("reps", new ArrayList<>());
        dictionary.put("weights", new ArrayList<>());
        for(String exercise : exercisesArrayList){
            String[] split = exercise.split("/-/");
            dictionary.get("exerciseID").add(split[0]);
            dictionary.get("planID").add(split[1]);
            dictionary.get("name").add(split[2]);
            dictionary.get("sets").add(split[3]);
            dictionary.get("reps").add(split[4]);
            dictionary.get("weights").add(split[5]);
            String displayName;
            if(split[5].equals("0")){
                displayName = split[2] + " \n   " + split[3] + " sets of " + split[4] + " reps";
            }
            else{
                displayName = split[2] + " \n   " + split[3] + " sets of " + split[4] + " reps using " + split[5] + " LBS";
            }
            displayExercisesArrayList.add(displayName);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, displayExercisesArrayList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position,convertView,parent);

                // Set the list view item's text color
                item.setTextColor(Color.parseColor("#000000"));

                // Change the item text size
                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);

                // return the view
                return item;
            }
        };

        editPlanExerciseList.setAdapter(adapter);
        editPlanExerciseList.setDividerHeight(2);

        editPlanExerciseList.setOnItemClickListener(new AdapterView.OnItemClickListener() { // Switches to the patient information activity
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Inputting all of the search results that a list view item contains inside an intent to be displayed to the user in patient information activity
                Intent intent = new Intent(getApplicationContext(), EditExerciseActivity.class);
                intent.putExtra("exerciseID", dictionary.get("exerciseID").get(i));
                intent.putExtra("planID", dictionary.get("planID").get(i));
                intent.putExtra("name", dictionary.get("name").get(i));
                intent.putExtra("sets", dictionary.get("sets").get(i));
                intent.putExtra("reps", dictionary.get("reps").get(i));
                intent.putExtra("weights", dictionary.get("weights").get(i));
                intent.putExtra("returnPage", "1");//0 is to the create page 1 is to the edit workout page
                editTitleName = editPlanNameText.getText().toString();
                editTimeLength = editLengthText.getText().toString();
                startActivity(intent);
            }
        });





    }


    private void sendToViewWorkoutPlansPage(View view) {
        String title = editPlanNameText.getText().toString();
        String length = editLengthText.getText().toString();
        if(title.equals("") || length.equals("")){
            Toast.makeText(EditWorkoutPlanActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
        }
        else if(DB.updateWorkout(currentPlanID, MainActivity.currentUserID, title, Integer.parseInt(length))){
            editTitleName = "";
            editTimeLength = "";
            currentPlanID = -1;
            startActivity(new Intent(EditWorkoutPlanActivity.this, WorkoutPlansActivity.class));
        }
        else{
            Toast.makeText(EditWorkoutPlanActivity.this, "Not Valid", Toast.LENGTH_SHORT).show();
        }

    }

    private void sendToAddExercisePage(View view) {
        editTitleName = editPlanNameText.getText().toString();
        editTimeLength = editLengthText.getText().toString();
        startActivity(new Intent(EditWorkoutPlanActivity.this, AddExerciseActivity.class));
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}