package ca.mohawkcollege.enes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MetricProgressActivity extends AppCompatActivity {
ImageView backMetricProgressButton;
TextView metricProgressTitle;
TextView metricProgressMonthTitle;
ListView recordListView;
DBHelper DB;
String[] values;

String[] dates;

    /**
     * This activity is the supposed to be graphing of the users metrics, currently not working
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metric_progress);
        DB = new DBHelper(this);
        Intent intent = getIntent();
        String selectedMetricName = intent.getStringExtra("metricName");
        String selectedMonth = intent.getStringExtra("month");
        String selectedMonthNumber = intent.getStringExtra("monthNumber");
        String selectedMetricUnit = intent.getStringExtra("metricUnit");
        metricProgressMonthTitle = findViewById(R.id.metricProgressMonthTitle);
        metricProgressMonthTitle.setText(selectedMetricName + " " + selectedMetricUnit);
        backMetricProgressButton = findViewById(R.id.backButtonMetricProgress);
        metricProgressTitle = findViewById(R.id.metricProgressTitle);
        metricProgressTitle.setText(selectedMonth + " Progress");
        backMetricProgressButton.setOnClickListener(this::sendToMyProgressPage);
        recordListView = findViewById(R.id.recordListView);
        ArrayList<String> recordArrayList = DB.getRecord(MainActivity.currentUserID, selectedMonthNumber, selectedMetricName);
        ArrayList<String> displayMetricsArrayList = new ArrayList<>();
        dates = new String[recordArrayList.size()];
        values = new String[recordArrayList.size()];
        Map<String, ArrayList<String>> dictionary = new HashMap<String, ArrayList<String>>();
        dictionary.put("date", new ArrayList<>());
        dictionary.put("value", new ArrayList<>());
        int q = 0;
        for(String record : recordArrayList){
            String[] split = record.split("/-/");
            dictionary.get("date").add(split[0]);
            dates[q] = split[0];
            dictionary.get("value").add(split[1]);
            values[q] = split[1];
            String  displayName = split[0] + " " + split[1] ;
            displayMetricsArrayList.add(displayName);
            q++;
        }

        MyMetricRecordAdapter adapter = new MyMetricRecordAdapter(this, dates, values);

        recordListView.setAdapter(adapter);
        recordListView.setDividerHeight(2);
    }

    private void sendToMyProgressPage(View view) {
        startActivity( new Intent(MetricProgressActivity.this, ProgressActivity.class));
    }
}