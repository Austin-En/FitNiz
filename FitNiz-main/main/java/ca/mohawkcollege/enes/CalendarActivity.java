package ca.mohawkcollege.enes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    public static String previouslySelectedDate = "0000-00-00";//Previously selected date is used to ensure when the user goes through the calendar pages they return back to the same date
    ImageView backButtonCalendar;

    /**
     * This activity displays the calendar
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        backButtonCalendar = findViewById(R.id.backButtonCalendar);
        backButtonCalendar.setOnClickListener(this::sendToHomePage);
        initWidgets();
        if(previouslySelectedDate.equals("0000-00-00")) {
            selectedDate = LocalDate.now();
        }
        else {
            selectedDate = LocalDate.parse(previouslySelectedDate);
            previouslySelectedDate = "0000-00-00";
        }
        setMonthView();
    }



    /**
     * This method displays the calendarAdapter that creates the layout for the activity
     */
    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    /**
     * Creates the array full of the days in a month
     * @param date
     * @return Returns the array of days in a month
     */
    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++){
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek){
                daysInMonthArray.add("");
            }
            else{
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    public void previousMonthAction(View view) {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /**
     * This method is used when a day has been clicked on the calendar activity and brings that date into the details for that day in another activity
     * @param position The position of the day in the calendar
     * @param dayText Contains the day in text
     */
    @Override
    public void onItemClick(int position, String dayText) {
        if(!dayText.equals("")){
            String month = monthYearFromDate(selectedDate);
            String[] separated = month.split(" ");
            String savedMonth = monthToNumber(separated[0]);
            String savedDay = dayText;
            if(Integer.parseInt(savedMonth) < 10){
                savedMonth = 0 + savedMonth;
            }
            if(savedDay.length() != 2){
                savedDay = "0" + savedDay;
            }
            previouslySelectedDate = separated[1] + "-" +savedMonth + "-" + savedDay;
            Intent intent = new Intent(CalendarActivity.this, ViewCalendarDayActivity.class);
            intent.putExtra("day", dayText);
            intent.putExtra("monthnumber", savedMonth);
            intent.putExtra("year", separated[1]);
            intent.putExtra("month", separated[0]);
            startActivity(intent);
        }
    }

    /**
     * This method was created to return a number based on the name of the month, this is used for later database calls and displaying to the user
     * @param monthName Name of the month
     * @return Returns the numeric value of the month
     */
    public String monthToNumber(String monthName){
        String number = "";
        if(monthName.equals("January")){
            number = "1";
        }
        else if(monthName.equals("February")){
            number = "2";
        }
        else if(monthName.equals("March")){
            number = "3";
        }
        else if(monthName.equals("April")){
            number = "4";
        }
        else if(monthName.equals("May")){
            number = "5";
        }
        else if(monthName.equals("June")){
            number = "6";
        }
        else if(monthName.equals("July")){
            number = "7";
        }
        else if(monthName.equals("August")){
            number = "8";
        }
        else if(monthName.equals("September")){
            number = "9";
        }
        else if(monthName.equals("October")){
            number = "10";
        }
        else if(monthName.equals("November")){
            number = "11";
        }
        else if(monthName.equals("December")){
            number = "12";
        }
        return number;
    }

    private void sendToHomePage(View view){
        startActivity(new Intent(CalendarActivity.this, HomeActivity.class));
    }
}