package ca.mohawkcollege.enes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class ViewCalendarDayActivity extends AppCompatActivity {
    ImageView backButtonViewDay;
    ImageView userImage;
    TextView titleText;
    TextView workoutStatusText;

    Button imageButton;
    TextView timeLengthText;
    String day;
    String month;
    String monthNumber;
    String year;
    String date;
    String[] separatedData;
    byte[] image;
    DBHelper DB;

    /**
     * This activity is entered once a user taps on a calendar day in the calendar activity, the user is able to view if they have completed workout for the day as well
     * as the time spent for both activities
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_calendar_day);
        DB = new DBHelper(this);
        Intent intent = getIntent();
        day = intent.getStringExtra("day");
        String dayValue = day;
        monthNumber = intent.getStringExtra("monthnumber");
        month = intent.getStringExtra("month");
        year = intent.getStringExtra("year");
        if(day.length() != 2){
            dayValue = "0" + dayValue;
        }
        date = year + "-" + monthNumber + "-" + dayValue;
        boolean result = DB.checkCalendarEntry(MainActivity.currentUserID, date);
        userImage = findViewById(R.id.userImage);
        backButtonViewDay = findViewById(R.id.backButtonViewDay);
        workoutStatusText = findViewById(R.id.workoutStatusText);
        timeLengthText = findViewById(R.id.timeLengthDayText);
        imageButton = findViewById(R.id.imageButton);
        titleText = findViewById(R.id.currentDayTitleText);
        titleText.setText(day + " " + month + " " + year);
        backButtonViewDay.setOnClickListener(this::sendToCalendarPage);
        imageButton.setOnClickListener(this::sendToSetPhotoPage);
        if(!result){
             DB.insertCalendar(MainActivity.currentUserID, date, null, false,  00.00);
        }
        //Get image from db
        image = DB.getCalendarDayRecordImage(MainActivity.currentUserID, date);
        if(image != null){
            Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
            userImage.setImageBitmap(bmp);
        }
        String data = DB.getCalendarDayRecord(MainActivity.currentUserID, date);
        separatedData = data.split("/-/");
        if(separatedData[4].equals("1")){
            workoutStatusText.setText("Complete");
        }
        timeLengthText.setText(separatedData[5] + " Minutes");

    }



    private void sendToCalendarPage(View view){
        startActivity(new Intent(ViewCalendarDayActivity.this, CalendarActivity.class));
    }

    /**
     * This method ensures the information of the calendar day is brought over to stay on the same day when saving a new image to the database or when coming back
     * that the user is still on the same day
     * @param view
     */
    private void sendToSetPhotoPage(View view){
        Intent intent = new Intent(ViewCalendarDayActivity.this, SetPhotoActivity.class);
        if(image != null) {
            Drawable drawable = userImage.getDrawable();
            BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
            Bitmap bitmap = bitmapDrawable .getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageInByte = stream.toByteArray();
            intent.putExtra("image", imageInByte);
        }
        intent.putExtra("dbinfo", separatedData);
        intent.putExtra("day", day);
        intent.putExtra("monthnumber", monthNumber);
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}