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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateWorkoutPlanActivity extends AppCompatActivity {
    ImageView backButtonCreateWorkoutPlan;
    ListView planListView;
    Button addExerciseButton;
    TextView lengthText;
    TextView titleText;
    DBHelper DB;
    Button createButton;
    public static String titleName = "";
    public static String timeLength = "";

    /**
     * From this activity the user is able to create a workout plan accessing a public static int from the original workout plan page which gets the next id in the database
     * from this activity users are able to give the workoutplan a name and add exercises to the plan
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout_plan);
        DB = new DBHelper(this);
        backButtonCreateWorkoutPlan = findViewById(R.id.backButtonCreateWorkoutPlan);
        planListView = findViewById(R.id.planList);
        lengthText = findViewById(R.id.lengthOfWorkoutText);
        titleText = findViewById(R.id.workoutTitleText);
        createButton = findViewById(R.id.createButton);
        addExerciseButton = findViewById(R.id.addExerciseButton);
        addExerciseButton.setOnClickListener(this::sendToAddExercisePage);
        backButtonCreateWorkoutPlan.setOnClickListener(this::sendToViewWorkoutPlansPage);
        createButton.setOnClickListener(this::sendToViewWorkoutPlansPageWithSave);
        titleText.setText(titleName);
        lengthText.setText(timeLength);
        ArrayList<String> exercisesArrayList = DB.getExerciseRecords(WorkoutPlansActivity.currentWorkoutPlanID, MainActivity.currentUserID);
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, displayExercisesArrayList){ //This creates the view of the listview
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

        planListView.setAdapter(adapter);
        planListView.setDividerHeight(2);

        planListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // Switches to the patient information activity
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
                intent.putExtra("returnPage", "0");//0 is to the create page 1 is to the edit workout page
                titleName = titleText.getText().toString();
                timeLength = lengthText.getText().toString();
                startActivity(intent);
            }
        });
    }

    private void sendToViewWorkoutPlansPage(View view) {
        DB.deleteExercise(WorkoutPlansActivity.currentWorkoutPlanID);
        titleName = "";
        timeLength = "";
        startActivity(new Intent(CreateWorkoutPlanActivity.this, WorkoutPlansActivity.class));
    }

    /**
     * This method is used in a onclicklistener that checks if a title and a length is given to the workout plan before saving it to the database
     * @param view
     */
    private void sendToViewWorkoutPlansPageWithSave(View view) {
        String title = titleText.getText().toString();
        String length = lengthText.getText().toString();
        if(title.equals("") || length.equals("")){
            Toast.makeText(CreateWorkoutPlanActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
        }
        else if(DB.insertWorkout(MainActivity.currentUserID, title, Integer.parseInt(length))){
            titleName = "";
            timeLength = "";
            startActivity(new Intent(CreateWorkoutPlanActivity.this, WorkoutPlansActivity.class));
        }
        else{
            Toast.makeText(CreateWorkoutPlanActivity.this, "Not Valid", Toast.LENGTH_SHORT).show();
        }

    }

    private void sendToAddExercisePage(View view) {
        titleName = titleText.getText().toString();
        timeLength = lengthText.getText().toString();
        startActivity(new Intent(CreateWorkoutPlanActivity.this, AddExerciseActivity.class));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}