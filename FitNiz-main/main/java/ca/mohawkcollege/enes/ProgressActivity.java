package ca.mohawkcollege.enes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressActivity extends AppCompatActivity {
ImageView backButtonProgress;
Spinner monthSpinner;
Spinner metricSpinner;
Button addMeasurementButton;
Button viewProgressButton;
Button photoLibraryButton;
DBHelper DB;

public static int selectedMonth = 0;
public static int selectedMetric = 0;

    /**
     *
     * This activity is used before viewing the progress page where its supposed to display a graph based on the users progress on the selected metric in the month
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        DB = new DBHelper(this);
        addMeasurementButton = findViewById(R.id.addMeasurementButton);
        addMeasurementButton.setOnClickListener(this::sendToAddMeasurementPage);
        viewProgressButton = findViewById(R.id.viewProgressButton);
        viewProgressButton.setOnClickListener(this::sendToViewProgressPage);
        photoLibraryButton = findViewById(R.id.viewPhotoLibraryButton);
        photoLibraryButton.setOnClickListener(this::sendToPhotoLibraryPage);
        backButtonProgress = findViewById(R.id.backButtonProgress);
        backButtonProgress.setOnClickListener(this::sendToHomePage);
        monthSpinner = findViewById(R.id.monthSpinner);
        metricSpinner = findViewById(R.id.metricSpinner);
        ArrayList<String> metricTypesList = DB.getMetric(MainActivity.currentUserID);
        Map<String, ArrayList<String>> dictionary = new HashMap<String, ArrayList<String>>();
        dictionary.put("name", new ArrayList<>());
        List<String> list = new ArrayList<String>();
        for(String exercise : metricTypesList){
            String[] split = exercise.split("/-/");
            dictionary.get("name").add(split[0]);
            String displayName = split[0];
            list.add(displayName);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        metricSpinner.setAdapter(adapter);
        if(selectedMonth != 0){
            monthSpinner.setSelection(selectedMonth);
            selectedMonth = 0;
        }
        if(selectedMetric != 0){
            metricSpinner.setSelection(selectedMetric);
            selectedMetric = 0;
        }
    }

    private void sendToAddMeasurementPage(View view) {
        startActivity(new Intent(ProgressActivity.this, AddMeasurementActivity.class));
    }

    private void sendToPhotoLibraryPage(View view) {
        startActivity(new Intent(ProgressActivity.this, PhotoLibraryActivity.class));
    }

    private void sendToViewProgressPage(View view) {
        selectedMonth = monthSpinner.getSelectedItemPosition();
        selectedMetric = metricSpinner.getSelectedItemPosition();
        String[] monthSeparated = String.valueOf(monthSpinner.getSelectedItem()).split(" ");
        String unit = DB.getMetricUnit(MainActivity.currentUserID, String.valueOf(metricSpinner.getSelectedItem()));
        Intent intent = new Intent(ProgressActivity.this, MetricProgressActivity.class);
        intent.putExtra("month", monthSeparated[0]);
        intent.putExtra("metricUnit", unit);
        intent.putExtra("monthNumber", monthSeparated[1]);
        intent.putExtra("metricName", String.valueOf(metricSpinner.getSelectedItem()));
        startActivity(intent);
    }


    private void sendToHomePage(View view){
        startActivity(new Intent(ProgressActivity.this, HomeActivity.class));
    }
}