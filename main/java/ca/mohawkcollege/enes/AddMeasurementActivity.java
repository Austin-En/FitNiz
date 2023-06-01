package ca.mohawkcollege.enes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


public class AddMeasurementActivity extends AppCompatActivity {
ImageView backButtonMeasurement;
EditText addMetricNameText;
Spinner addMetricSpinner;
Button addButton;
DBHelper DB;

    /**
     * The activity is used to add measurements to the users account that can be tracked after every workout and cardio
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_measurement);
        DB = new DBHelper(this);
        addMetricNameText = findViewById(R.id.addMetricNameText);
        addMetricSpinner = findViewById(R.id.addMetricSpinner);
        addButton = findViewById(R.id.addButton);
        backButtonMeasurement = findViewById(R.id.backButtonMeasurement);
        backButtonMeasurement.setOnClickListener(this::sendToProgressPage);
        addButton.setOnClickListener(this::sendToSaveMetricPage);
    }

    private void sendToProgressPage(View view){
        startActivity(new Intent(AddMeasurementActivity.this, ProgressActivity.class));
    }

    /**
     * This method is used as a onclicklistener to save the metric and verify that a name and a unit is given
     * @param view
     */
    private void sendToSaveMetricPage(View view){
        String metricName = addMetricNameText.getText().toString();
        String unit = (String) addMetricSpinner.getSelectedItem();
        if(metricName.equals("") || unit.equals("")){
            Toast.makeText(AddMeasurementActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
        }
        else if(DB.checkMatchMetric(MainActivity.currentUserID, metricName, unit)){
            Toast.makeText(AddMeasurementActivity.this, "This metric already exists", Toast.LENGTH_SHORT).show();
        }
        else if(DB.insertMetric(MainActivity.currentUserID, metricName, unit)){
            startActivity(new Intent(AddMeasurementActivity.this, ProgressActivity.class));
        }
    }
}