package ca.mohawkcollege.enes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    /**
     * This activity is the homepage of the application, where the user is able to click and go around the application
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button logout = findViewById(R.id.logoutButton);
        Button calendar = findViewById(R.id.calendarButton);
        Button workout = findViewById(R.id.workoutButton);
        Button workoutPlan = findViewById(R.id.workoutPlanButton);
        Button progress = findViewById(R.id.progressButton);
        Button permission = findViewById(R.id.permissionButton);
        Button account = findViewById(R.id.accountButton);
        logout.setOnClickListener(this::sendToMainPage);
        calendar.setOnClickListener(this::sendToCalendarPage);
        workout.setOnClickListener(this::sendToWorkoutPage);
        workoutPlan.setOnClickListener(this::sendToWorkoutPlanPage);
        progress.setOnClickListener(this::sendToProgressPage);
        permission.setOnClickListener(this::sendToPermissionPage);
        account.setOnClickListener(this::sendToAccountPage);
    }

    private void sendToMainPage(View view){
        MainActivity.currentUserID = -1;
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
    }

    private void sendToCalendarPage(View view){
        startActivity(new Intent(HomeActivity.this, CalendarActivity.class));
    }

    private void sendToWorkoutPage(View view){
        startActivity(new Intent(HomeActivity.this, WorkoutActivity.class));
    }

    private void sendToWorkoutPlanPage(View view){
        startActivity(new Intent(HomeActivity.this, WorkoutPlansActivity.class));
    }

    private void sendToProgressPage(View view){
        startActivity(new Intent(HomeActivity.this, ProgressActivity.class));
    }

    private void sendToPermissionPage(View view){
        startActivity(new Intent(HomeActivity.this, PermissionsActivity.class));
    }

    private void sendToAccountPage(View view){
        startActivity(new Intent(HomeActivity.this, AccountActivity.class));
    }
}