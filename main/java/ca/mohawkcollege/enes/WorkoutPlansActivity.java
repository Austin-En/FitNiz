package ca.mohawkcollege.enes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WorkoutPlansActivity extends AppCompatActivity {
ImageView backButtonWorkoutPlan;
Button createPlanButton;
DBHelper DB;
ListView planList;
public static int currentWorkoutPlanID = 0;

    /**
     * This activity allows user to view created workout plans that are attached to the currently logged in user id, the user is able to click on the plan and view/edit
     * information of the workout plan as well as create new workoutplans
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_plans);
        DB = new DBHelper(this);
        currentWorkoutPlanID = 0;
        planList = findViewById(R.id.planList);
        backButtonWorkoutPlan = findViewById(R.id.backButtonWorkoutPlan);
        createPlanButton = findViewById(R.id.createPlanButton);
        backButtonWorkoutPlan.setOnClickListener(this::sendToHomePage);
        createPlanButton.setOnClickListener(this::sendToCreateWorkoutPlan);
        ArrayList<String> planArrayList = DB.getWorkoutRecords(MainActivity.currentUserID);
        ArrayList<String> displayPlanArrayList = new ArrayList<>();
        Map<String, ArrayList<String>> dictionary = new HashMap<String, ArrayList<String>>();
        dictionary.put("planID", new ArrayList<>());
        dictionary.put("name", new ArrayList<>());
        dictionary.put("length", new ArrayList<>());
        for(String workout : planArrayList){
            String[] split = workout.split("/-/");
            dictionary.get("planID").add(split[0]);
            dictionary.get("name").add(split[1]);
            dictionary.get("length").add(split[2]);
            displayPlanArrayList.add(split[1]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, displayPlanArrayList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position,convertView,parent);

                // Set the list view item's text color
                item.setTextColor(Color.parseColor("#000000"));

                // Set the item text style to bold
                item.setTypeface(item.getTypeface(), Typeface.BOLD);

                // Change the item text size
                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25);

                // return the view
                return item;
            }
        };

        planList.setAdapter(adapter);
        planList.setDividerHeight(2);

        planList.setOnItemClickListener(new AdapterView.OnItemClickListener() { // Switches to the patient information activity
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Inputting all of the search results that a list view item contains inside an intent to be displayed to the user in patient information activity
                Intent intent = new Intent(getApplicationContext(), EditWorkoutPlanActivity.class);
                intent.putExtra("planID", dictionary.get("planID").get(i));
                intent.putExtra("name", dictionary.get("name").get(i));
                intent.putExtra("length",dictionary.get("length").get(i));
                startActivity(intent);
            }
        });
    }

    private void sendToCreateWorkoutPlan(View view) {
        DB.deleteExercise(currentWorkoutPlanID);//This is to make sure there aren't any exercises in the database that should not be apart of the attempt of creating a new workout plan
        currentWorkoutPlanID = DB.getNextPlanID() + 1;
        startActivity(new Intent(WorkoutPlansActivity.this, CreateWorkoutPlanActivity.class));
    }

    private void sendToHomePage(View view){
        currentWorkoutPlanID = 0;
        if(WorkoutActivity.returnPage == 2){
            startActivity(new Intent(WorkoutPlansActivity.this, WorkoutActivity.class));
        }
        else{
            startActivity(new Intent(WorkoutPlansActivity.this, HomeActivity.class));
        }

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}