package ca.mohawkcollege.enes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultsActivity extends AppCompatActivity {
Button finishResultsPage;
ListView resultListView;
DBHelper DB;
public static String[] finalValues;
private static final DecimalFormat df = new DecimalFormat("0.00");
String[] metricNames;
String[] units;

    /**
     * This activity is where a listview is displayed to the user and allows the user to enter in the information they want to track for the day after a workout/cardio
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        finalValues = new String[0];
        finishResultsPage = findViewById(R.id.finishResultsButton);
        finishResultsPage.setOnClickListener(this::sendToHomeActivity);
        resultListView = findViewById(R.id.resultsListView);
        resultListView.setItemsCanFocus(true);
        DB = new DBHelper(this);
        ArrayList<String> metricsArrayList = DB.getMetric(MainActivity.currentUserID);
        ArrayList<String> displayMetricsArrayList = new ArrayList<>();
        metricNames = new String[metricsArrayList.size()];
        units = new String[metricsArrayList.size()];
        Map<String, ArrayList<String>> dictionary = new HashMap<String, ArrayList<String>>();
        dictionary.put("name", new ArrayList<>());
        dictionary.put("unit", new ArrayList<>());
        int q = 0;
        for(String metric : metricsArrayList){
            String[] split = metric.split("/-/");
            dictionary.get("name").add(split[0]);
            metricNames[q] = split[0];
            dictionary.get("unit").add(split[1]);
            units[q] = split[1];
            String  displayName = split[0] + " " + split[1] ;
            displayMetricsArrayList.add(displayName);
            q++;
        }
        int indexCount = displayMetricsArrayList.size();
        String metrics[] = new String[indexCount];
        String values[] = new String[indexCount];
        int i = 0;
        for(String metric : displayMetricsArrayList){
            metrics[i] = metric;
            values[i] = "";
            i++;
        }
        values[0] = WorkoutActivity.timeString;
        finalValues = values;


        MyResultListAdapter adapter = new MyResultListAdapter(this, metrics, values);

        resultListView.setAdapter(adapter);
        resultListView.setDividerHeight(2);

    }

    /**
     * This method is used to send the user back to the home activity as well as save the users inputted information for metrics
     * @param view
     */
    private void sendToHomeActivity(View view){

        String[] timerValue = finalValues[0].split(":");
        String doubleValueString = timerValue[0] + "." + timerValue[1];


        if(!DB.checkCalendarEntry(MainActivity.currentUserID, String.valueOf(LocalDate.now()))){
            DB.insertCalendar(MainActivity.currentUserID, String.valueOf(LocalDate.now()), null, true,  Double.parseDouble(doubleValueString));
        }
        else{
            //update workout status
            String entry = DB.getCalendarDayRecord(MainActivity.currentUserID, String.valueOf(LocalDate.now()));
            String[] separated = entry.split("/-/");
            Double addedDoubleValue = Double.parseDouble(separated[5]) + Double.parseDouble(doubleValueString);
            addedDoubleValue = Double.parseDouble(df.format(addedDoubleValue));
            byte[] image = DB.getCalendarDayRecordImage(MainActivity.currentUserID, separated[3]);
            if(!WorkoutActivity.type) {
                if (image != null) {
                    DB.updateCalendarRecord(Integer.parseInt(separated[0]), MainActivity.currentUserID, separated[2], image, true, addedDoubleValue);
                } else {
                    DB.updateCalendarRecord(Integer.parseInt(separated[0]), MainActivity.currentUserID, separated[2], null, true, addedDoubleValue);
                }
            }
        }

        for(int i = 0; i <= finalValues.length - 1; i++){
            if(!finalValues[i].equals("")) {
                DB.insertRecord(MainActivity.currentUserID, String.valueOf(LocalDate.now()), metricNames[i], units[i], finalValues[i]);
            }
        }

        startActivity(new Intent(ResultsActivity.this, HomeActivity.class));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}